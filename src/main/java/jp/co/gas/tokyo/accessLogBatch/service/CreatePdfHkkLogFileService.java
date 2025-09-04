package jp.co.gas.tokyo.accessLogBatch.service;

// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileOutputStream;
import java.io.IOException;
// import java.io.OutputStreamWriter;
// import java.io.PrintWriter;
import java.nio.charset.Charset;
// import java.nio.file.Files;
// import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.autoconfigure.web.ServerProperties.Jetty.Accesslog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

// import com.azure.core.http.rest.PagedIterable;
// import com.azure.core.util.BinaryData;
// import com.azure.storage.blob.BlobClient;
// import com.azure.storage.blob.BlobContainerClient;
// import com.azure.storage.blob.models.BlobItem;

// import jp.co.gas.tokyo.accessLogBatch.common.azure.storage.AzureBlobStorageFactory;
import jp.co.gas.tokyo.accessLogBatch.entity.AccesssLogData;
import jp.co.gas.tokyo.accessLogBatch.entity.PdfHkkLogData;
// import jp.co.gas.tokyo.accessLogBatch.ftp.Ftp;
import jp.co.gas.tokyo.accessLogBatch.mapper.pdfHkk.PdfHkkLogMapper;
import jp.co.gas.tokyo.accessLogBatch.utilities.CsvUtil;
// import jp.co.gas.tokyo.accessLogBatch.utilities.DateUtil;
import jp.co.gas.tokyo.accessLogBatch.utilities.FileUtil;
import jp.co.gas.tokyo.accessLogBatch.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreatePdfHkkLogFileService {

    @Value("${ALSS_BUSINESS_SYSTEM_CODE}")
    private String ALSS_BUSINESS_SYSTEM_CODE;

    @Value("${ALSS_FUNCTION_CODE}")
    private String ALSS_FUNCTION_CODE;
	
	/** ストレージアカウント名（導管系） */
	@Value("${DW_STOR_NAME}")
	private String storageAccountName;

	/** アクセスログ保存一時ディレクトリ（導管管理PDF） */
	@Value("${ALSS_LOG_DIR_DW_ACCESSLOG}")
    private String ALSS_LOG_DIR_DW_ACCESSLOG;

	/** ログ送信Service */
	private final SendAccessLogService sendAccessLogService;

	/** PDF発行ログ */
    private final PdfHkkLogMapper pdfHkkLogMapper;

	@Transactional("primaryTransactionManager")
	public void exec() {
		log.info("==================== アクセスログ送信 PDF_HKK_LOG 処理開始 ====================");

		try {
			// アクセスログファイル生成
			createService();

			// 送信処理実行
			sendAccessLogService.exec(ALSS_LOG_DIR_DW_ACCESSLOG);
		} catch (Exception e) {
			log.error("アクセスログ送信処理に失敗しました。", e);
		} finally {
			log.info("==================== アクセスログ送信 PDF_HKK_LOG 処理終了 ====================");
		}
	}

	/**
	 * アクセスログファイルの生成を行う
	 * 
	 * @throws IOException
	 */
	@Transactional("primaryTransactionManager")
    public void createService() throws IOException {

		List<PdfHkkLogData> dataList = pdfHkkLogMapper.selectPdfHkkList();

		// 対象無し
		if (ObjectUtils.isEmpty(dataList)) {
			log.info("PDF出力ログ対象無し");
			return;
		}

		log.info("PDF出力ログ対象件数:{}件", dataList.size());
		
		Map<String, List<PdfHkkLogData>> groupedByDuke = dataList.stream().collect(Collectors.groupingBy(PdfHkkLogData::getRecRegUserId));

		// アクセスログ生成
		List<AccesssLogData> logFileNameList = new ArrayList<AccesssLogData>();
		groupedByDuke.forEach((dukeId, list) -> {
			AccesssLogData logData = this.outputCsvDownloadLog(list);
			if (logData != null && !StringUtils.isNullEmpty(logData.getFileName())) {
				logFileNameList.add(logData);
			}
		});
		
		// 送信ログファイルリスト内容作成
		ArrayList<String> sendListFileLog = createSendFileListLog(logFileNameList);
		
		// ファイル内容を作成(送信ログファイルリスト)
		Calendar cl = Calendar.getInstance();
		String sendFileListName = this.getFileNameSendList(cl);

		// アクセスログ格納処理実行
		this.execUpload(sendFileListName, sendListFileLog);

		// 連携済みに更新
		dataList.forEach(pdflog ->{
			pdfHkkLogMapper.updateLinkedFlg(pdflog.getId(), "1");
		});
    }

	/**
	 * アクセスログファイル生成(業務アクセスログ/取得データログ/送信ログファイルリスト)
	 *
	 * @param userDetails     ログインユーザ情報
	 * @param csvData         出力するCSVデータ
	 * @param clientIpAddress クライアントIPアドレス
	 * @throws IOException
	 */
	private AccesssLogData outputCsvDownloadLog(List<PdfHkkLogData> dataList) {

		Calendar cl = Calendar.getInstance();

		// 業務アクセスログ内容作成
		ArrayList<String> bizAccessLog = createBizAccessLog(dataList, cl);

		// 取得データログ内容作成
		ArrayList<String> downloadDataLog = createDownloadDataLog(dataList, cl);
		
		// ログ内容を結合
		List<String> logDetails = Stream.concat(bizAccessLog.stream(), downloadDataLog.stream())
				.collect(Collectors.toList());

		// アクセスログファイル生成(業務アクセスログ/取得データログ)
		String accessLogFileName = this.getLogFilePath(cl);
		
		// アクセスログファイルサイズ
		int accessLogFileLength = String.join("\n", logDetails).getBytes(Charset.forName("MS932")).length;

		// アクセスログ格納処理実行
		this.execUpload(accessLogFileName, logDetails);

		AccesssLogData logData = new AccesssLogData();
		logData.setFileName(accessLogFileName);
		logData.setFileByte(accessLogFileLength);

		return logData;
	}

	/**
	 * アクセスログファイル内容生成(業務アクセスログ)
	 *
	 * @param dataList 出力情報
	 * @param cl 現在時刻カレンダー
	 * @return 出力文字列リスト
	 */
	private ArrayList<String> createBizAccessLog(List<PdfHkkLogData> dataList, Calendar cl) {

    	PdfHkkLogData pdfHkkLogData = dataList.get(0);

		SimpleDateFormat sdfD = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfT = new SimpleDateFormat("HHmmss");
		SimpleDateFormat sdfId = new SimpleDateFormat("HHmmssSSS");

		final String IDENTIFIER_HEAD = "S";
		final String IDENTIFIER_BUSINESS_ACCESS_LOG = "A01";
		String nowDate = sdfD.format(cl.getTime());
		String nowTime = sdfT.format(cl.getTime());
		final String BUSINESS_SYSTEM_USER_ID = "";
		final String BUSINESS_SYSTEM_DEPARTMENT = "";
		final String BUSINESS_SYSTEM_USER_NAME = "";
		String remoteAddressIp = pdfHkkLogData.getRemoteAddressIp();
		String dataId = sdfId.format(cl.getTime());
		final String NOTE = "";
		final String SPARE = "";

		// 業務アクセスログ内容
		ArrayList<String> bizAccessLog = new ArrayList<String>();
		List<String> logRecordElements = new ArrayList<>(Arrays.asList(IDENTIFIER_HEAD, IDENTIFIER_BUSINESS_ACCESS_LOG, nowDate, nowTime,
			pdfHkkLogData.getRecRegUserId(), pdfHkkLogData.getRecRegCorpNm(), pdfHkkLogData.getRecRegUserNm(),
			BUSINESS_SYSTEM_USER_ID, BUSINESS_SYSTEM_DEPARTMENT, BUSINESS_SYSTEM_USER_NAME, remoteAddressIp,
			ALSS_BUSINESS_SYSTEM_CODE, ALSS_FUNCTION_CODE, dataId, String.valueOf(dataList.size()), NOTE, SPARE,
			SPARE));

		List<String> escapedLogRecordElements = CsvUtil.escapeForCsv(logRecordElements);
		bizAccessLog.add(String.join(",", escapedLogRecordElements));

		return bizAccessLog;

	}

	/**
	 * アクセスログファイル内容生成(取得データログ)
	 *
	 * @param dataList 出力情報
	 * @param cl 現在時刻カレンダー
	 * @return 出力文字列リスト
	 */
	private ArrayList<String> createDownloadDataLog(List<PdfHkkLogData> dataList, Calendar cl) {

    SimpleDateFormat sdfId = new SimpleDateFormat("HHmmssSSS");

    final String IDENTIFIER_HEAD = "S";
    final String IDENTIFIER_DOWNLOAD_DATA_LOG = "D01";
    final String BRANCH_NO = "1";
    String dataId = sdfId.format(cl.getTime());
    final String SPARE = "";

    // 取得データログ内容
    ArrayList<String> downloadDataLog = new ArrayList<String>();
    for (int index = 0; index < dataList.size(); index++) {

		PdfHkkLogData pdfHkkLogData = dataList.get(index);

		String gasmeterSetNumber = null;
		gasmeterSetNumber = pdfHkkLogData.getCustInfoCustNo();

		String name = StringUtils.nvl(pdfHkkLogData.getCustInfoNameSei())
					+ (StringUtils.isNullEmpty(pdfHkkLogData.getCustInfoNameMei()) ? "" : "　" + pdfHkkLogData.getCustInfoNameMei());

		String nameKn = StringUtils.nvl(pdfHkkLogData.getCustInfoNameSeiKn())
						+ (StringUtils.isNullEmpty(pdfHkkLogData.getCustInfoNameMeiKn()) ? "" : "　" + pdfHkkLogData.getCustInfoNameMeiKn());

		List<String> logRecordElements = new ArrayList<>(Arrays.asList(IDENTIFIER_HEAD, IDENTIFIER_DOWNLOAD_DATA_LOG, ALSS_BUSINESS_SYSTEM_CODE,
			ALSS_FUNCTION_CODE, dataId, String.valueOf(index + 1), BRANCH_NO, name, nameKn, "", "", "", "", "", "",
			gasmeterSetNumber, SPARE, SPARE));

		List<String> escapedLogRecordElements = CsvUtil.escapeForCsv(logRecordElements);

		downloadDataLog.add(String.join(",", escapedLogRecordElements));
    }
		return downloadDataLog;
	}

	/**
	 * ファイル格納処理実行
	 * 
	 * @param fileName    格納するファイル名
	 * @param logData     格納するログ
	 * @param userDetails ログ出力用
	 */
	private void execUpload(String fileName, List<String> logData) {

		// ログファイル生成（SHIFT-JIS(MS932)）
		byte[] logByteArray = String.join("\n", logData).getBytes(Charset.forName("MS932"));

		String filePath = ALSS_LOG_DIR_DW_ACCESSLOG + fileName;

		// 送信ログ格納処理実行
		try {
			// 一時フォルダにファイルを生成
			FileUtil.createFile(logByteArray, filePath);
			
		} catch (Exception e) {
			log.error("アクセスログファイル保存用一時フォルダへの格納失敗：[{}]", filePath, e);
		}
	}

	/**
	 * アクセスログファイル名称を取得する
	 *
	 * @param cl 現在時刻カレンダー
	 */
	private String getLogFilePath(Calendar cl) {

		SimpleDateFormat sdfD = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfId = new SimpleDateFormat("HHmmssSSS");

		final String FILE_NAME_HEAD = "s";

		return FILE_NAME_HEAD + "_" + sdfD.format(cl.getTime()) + "_" + ALSS_BUSINESS_SYSTEM_CODE + "_"
				+ ALSS_FUNCTION_CODE + "_" + sdfId.format(cl.getTime()) + ".txt";
	}

	/**
	 * 送信ログファイルリストを生成する
	 * @param accessLogFilePathList
	 * @return
	 */
	private ArrayList<String> createSendFileListLog(List<AccesssLogData> accessLogFilePathList) {

		int recordNo = 1;

		// 取得データログ内容
		ArrayList<String> sendFileListLog = new ArrayList<String>();

		List<String> logRecordElements = null;

		for (AccesssLogData logData : accessLogFilePathList) {
			logRecordElements = new ArrayList<>(Arrays.asList(String.valueOf(recordNo), logData.getFileName(), String.valueOf(logData.getFileByte())));

			List<String> escapedLogRecordElements = CsvUtil.escapeForCsv(logRecordElements);

			sendFileListLog.add(String.join(",", escapedLogRecordElements));
			recordNo++;
		}
		return sendFileListLog;

	}

	/**
	 * 送信ログファイルリスト名称を取得する
	 *
	 * @param cl 現在時刻カレンダー
	 */
	private String getFileNameSendList(Calendar cl) {

		SimpleDateFormat sdfD = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfId = new SimpleDateFormat("HHmmssSSS");

		final String FILE_NAME_HEAD = "sendlist";

		return FILE_NAME_HEAD + "-" + sdfD.format(cl.getTime()) + "-" + sdfId.format(cl.getTime()) + ".txt";

	}
}
