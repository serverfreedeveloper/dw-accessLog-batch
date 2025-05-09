package jp.co.gas.tokyo.accessLogBatch.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties.Jetty.Accesslog;
import org.springframework.stereotype.Service;

import jp.co.gas.tokyo.accessLogBatch.entity.AccesssLogData;
import jp.co.gas.tokyo.accessLogBatch.entity.PdfHkkLogData;
import jp.co.gas.tokyo.accessLogBatch.ftp.Ftp;
import jp.co.gas.tokyo.accessLogBatch.mapper.pdfHkk.PdfHkkLogMapper;
import jp.co.gas.tokyo.accessLogBatch.utilities.Const;
import jp.co.gas.tokyo.accessLogBatch.utilities.DateUtil;
import jp.co.gas.tokyo.accessLogBatch.utilities.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreatePdfHkkLogFileService {

    @Value("${ALSS_EXECUTE}")
    private String ALSS_EXECUTE;

    @Value("${ALSS_SEND_FILE_DIR}")
    private String ALSS_SEND_FILE_DIR;

    @Value("${ALSS_FAIL_FILE_DIR}")
    private String ALSS_FAIL_FILE_DIR;

    @Value("${ALSS_BUSINESS_SYSTEM_CODE}")
    private String ALSS_BUSINESS_SYSTEM_CODE;

    @Value("${ALSS_FUNCTION_CODE}")
    private String ALSS_FUNCTION_CODE;

    @Value("${ALSS_LOG_DIRECTORY}")
    private String ALSS_LOG_DIRECTORY;

    @Value("${ALSS_FTP_HOST}")
    private String ALSS_FTP_HOST;

    @Value("${ALSS_FTP_PORT}")
    private int ALSS_FTP_PORT;

    @Value("${ALSS_FTP_USER}")
    private String ALSS_FTP_USER;

    @Value("${ALSS_FTP_PASS}")
    private String ALSS_FTP_PASS;

    @Value("${ALSS_FTP_DIR}")
    private String ALSS_FTP_DIR;

    @Value("${ALSS_MENTE_START}")
    private String ALSS_MENTE_START;

    @Value("${ALSS_MENTE_END}")
    private String ALSS_MENTE_END;


    @Autowired
    private PdfHkkLogMapper pdfHkkLogMapper;

    public void createService() throws IOException {

		// ジョブ開始ログの出力
		String startMsg = "CreateFile" + ":" + Const.START_MESSAGE;
		log.info(startMsg);

		List<PdfHkkLogData> dataList = pdfHkkLogMapper.selectPdfHkkList();
		if (dataList == null || dataList.isEmpty()) {
		log.info("PdfHkkLog is noFile");
			return;
		}
		log.info("対象件数 : " + dataList.size());
		
		Map<String, List<PdfHkkLogData>> groupedByDuke = dataList.stream().collect(Collectors.groupingBy(PdfHkkLogData::getRecRegUserId));

		// アクセスログ生成
		List<AccesssLogData> logFileNameList = new ArrayList<AccesssLogData>();
		groupedByDuke.forEach((dukeId, list) -> {
			AccesssLogData logData = this.outputCsvDownloadLog(list);
			if (logData != null && !StringUtils.isNullEmpty(logData.getFilaName())) {
				logFileNameList.add(logData);
			}
		});
		
		// 送信ログファイルリスト内容作成
		ArrayList<String> sendListFileLog = createSendFileListLog(logFileNameList);
		
		// ファイル内容を作成(送信ログファイルリスト)
		Calendar cl = Calendar.getInstance();
		String sendFileListName = this.getFileNameSendList(cl);
		File sendListDir = new File(ALSS_LOG_DIRECTORY);
		File sendListFile = new File(ALSS_LOG_DIRECTORY + sendFileListName);
		if (sendListFile.exists()) {
			sendListFile.delete();
		}
		if (!sendListDir.exists()) {
			sendListFile.mkdirs();
		}
		sendListFile.createNewFile();
		PrintWriter sendListPw = new PrintWriter(
			new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sendListFile), "Shift-JIS")));		
			try {
				// ファイル情報を書き出す
			sendListPw.println(String.join("\n", sendListFileLog));
			
		} finally {
			sendListPw.close();
		}
		// ファイルFTP送信
		//sendCsvDownloadLog(list);

		// 連携済みフラグ更新
		dataList.forEach(pdflog ->{
			pdfHkkLogMapper.updateLinkedFlg(pdflog.getId());
		});
		// 正常終了メッセージの出力
		log.info("CreateFile" + ":" + Const.NORMAL_END_MESSAGE);
    }


	/**
	 * アクセスログファイル送信(業務アクセスログ/取得データログ/送信ログファイルリスト)
	 *
	 * @param sendFileListName 送信ファイル名
	 */
	private synchronized void sendCsvDownloadLog(String sendFileListName) {
    
      	// アクセスログを送信しない場合は終了する
		if ("0".equals(ALSS_EXECUTE))
			return;

    //   ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
    //   総合試験でファイルを送信しないので一旦コメントアウト
    //   ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
      // アクセスログ保存システムメンテナンスの場合
    //   String nowTime = DateUtil.getSysDate("dHHmm");
    //   if (ALSS_MENTE_START.compareTo(nowTime) >= 0 && ALSS_MENTE_END.compareTo(nowTime) <= 0) {
    //     // 処理を終了する
    //     log.info("アクセスログ保存システムメンテナンスの為、ファイルを送信しませんでした。[" + sendFileListName + "]");
    //     return;
    //   }
    //   // FTPクライアントを作成する
    //   Ftp ftp = new Ftp(ALSS_FTP_HOST, ALSS_FTP_PORT, ALSS_FTP_USER, ALSS_FTP_PASS, true, true, ALSS_FTP_DIR,
    //       ALSS_LOG_DIRECTORY, "SHIFT_JIS");

    //   // 送信するファイルリストを取得する
    //   File dir = new File(ALSS_LOG_DIRECTORY);
    //   // listFilesメソッドを使用して一覧を取得する
    //   File[] list = dir.listFiles();
    //   for (int i = 0; i < list.length; i++) {
    //     // 拡張子txt以外は無視
    //     if (!list[i].getName().endsWith(".txt"))
    //       continue;
    //     try {
    //       ftp.setLocalPath(list[i].getAbsolutePath());
    //       ftp.put();
    //       log.info("アクセスログ保存システム送信成功ファイル：[" + list[i].getName() + "]");
    //       // 送信ファイルを削除
    //       list[i].delete();
    //     } catch (Exception e) {
    //       // 送信失敗時、ファイルを保存
    //       log.warn("アクセスログ保存システム送信失敗ファイル：[" + list[i].getName() + "]");
    //       // ログファイルを移動
    //       try {
    //         File failDir = new File(ALSS_FAIL_FILE_DIR);
    //         failDir.mkdirs();
    //         Files.move(Paths.get(ALSS_LOG_DIRECTORY, list[i].getName()),
    //             Paths.get(ALSS_FAIL_FILE_DIR, list[i].getName()));
    //       } catch (IOException e1) {
    //         log.warn("アクセスログ保存システム送信移動失敗ファイル：[" + list[i].getName() + "]");
    //       }
    //     }
    //   }
    //   try {
    //     ftp.disconnect();
    //   } catch (Exception e) {
    //     // FTPクローズのエラーは無視
    //   }

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
		File accessLogDir = new File(ALSS_LOG_DIRECTORY);
		File accessLogFile = new File(ALSS_LOG_DIRECTORY + accessLogFileName);
		if (accessLogFile.exists()) {
			accessLogFile.delete();
		}
		if (!accessLogDir.exists()) {
			accessLogDir.mkdirs();
		}
		PrintWriter accessLogPw = null;
		AccesssLogData logData = new AccesssLogData();
		try {
			accessLogFile.createNewFile();
			accessLogPw = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(accessLogFile), "Shift-JIS")));
			// アクセスログファイルにデータを書き出す
			accessLogPw.println(String.join("\n", logDetails));
					
		} catch (Exception e) {
			log.warn(e.getMessage());
			
		} finally {
			if (accessLogPw != null) {
				accessLogPw.close();
			}
		}
		logData.setFilaName(accessLogFileName);
		logData.setFileByte(accessLogFile.length());
		return logData;
	}

	/**
	 * アクセスログファイル内容生成(業務アクセスログ)
	 *
	 * @param userDetails     ログインユーザ情報
	 * @param csvData         出力するCSVデータ
	 * @param clientIpAddress クライアントIPアドレス
	 * @param cl              現在時刻カレンダー
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
		String clientIpAddressFix = this.fixString(pdfHkkLogData.getRemoteIpAddress());
		String dataId = sdfId.format(cl.getTime());
		final String NOTE = "";
		final String SPARE = "";

		// 業務アクセスログ内容
		ArrayList<String> bizAccessLog = new ArrayList<String>();
		String logRecordElements[] = { IDENTIFIER_HEAD, IDENTIFIER_BUSINESS_ACCESS_LOG, nowDate, nowTime,
		pdfHkkLogData.getRecRegUserId(), pdfHkkLogData.getRecRegCorpNm(), pdfHkkLogData.getRecRegUserNm(),
				BUSINESS_SYSTEM_USER_ID, BUSINESS_SYSTEM_DEPARTMENT, BUSINESS_SYSTEM_USER_NAME, clientIpAddressFix,
				ALSS_BUSINESS_SYSTEM_CODE, ALSS_FUNCTION_CODE, dataId, String.valueOf(dataList.size()), NOTE, SPARE,
				SPARE };

		bizAccessLog.add(String.join(",", logRecordElements));

		return bizAccessLog;

	}

	/**
	 * アクセスログファイル内容生成(取得データログ)
	 *
	 * @param userDetails     ログインユーザ情報
	 * @param csvData         出力するCSVデータ
	 * @param clientIpAddress クライアントIPアドレス
	 * @param cl              現在時刻カレンダー
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
		gasmeterSetNumber = this.fixString(pdfHkkLogData.getCustInfoCustNo());

		String name = StringUtils.nvl(pdfHkkLogData.getCustInfoNameSei())
					+ (StringUtils.isNullEmpty(pdfHkkLogData.getCustInfoNameMei()) ? "" : "　" + pdfHkkLogData.getCustInfoNameMei());

		String nameKn = StringUtils.nvl(pdfHkkLogData.getCustInfoNameSeiKn())
						+ (StringUtils.isNullEmpty(pdfHkkLogData.getCustInfoNameMeiKn()) ? "" : "　" + pdfHkkLogData.getCustInfoNameMeiKn());

		String logRecordElements[] = { IDENTIFIER_HEAD, IDENTIFIER_DOWNLOAD_DATA_LOG, ALSS_BUSINESS_SYSTEM_CODE,
			ALSS_FUNCTION_CODE, dataId, String.valueOf(index + 1), BRANCH_NO, name, nameKn, "", "", "", "", "", "",
			gasmeterSetNumber, SPARE, SPARE };

		downloadDataLog.add(String.join(",", logRecordElements));
    }
		return downloadDataLog;
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

		for (AccesssLogData logData : accessLogFilePathList) {
			
			String logRecordElements[] = { String.valueOf(recordNo), this.fixString(logData.getFilaName()), String.valueOf(logData.getFileByte()) };
			sendFileListLog.add(String.join(",", logRecordElements));
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

	/** 文字列を正常なフォーマットに整形する */
	private String fixString(String str) {

		if (StringUtils.isNullEmpty(str))
			return "";

		if (str.indexOf(",") >= 0) {
			return "\"" + str + "\"";
		} else {
			return str;
		}
	}

}
