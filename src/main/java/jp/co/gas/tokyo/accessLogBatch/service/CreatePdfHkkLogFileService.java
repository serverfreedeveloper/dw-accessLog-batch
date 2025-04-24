package jp.co.gas.tokyo.accessLogBatch.service;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jp.co.gas.tokyo.accessLogBatch.entity.PdfHkkLogData;
import jp.co.gas.tokyo.accessLogBatch.mapper.pdfHkk.PdfHkkLogMapper;
import jp.co.gas.tokyo.accessLogBatch.utilities.Const;
import jp.co.gas.tokyo.accessLogBatch.utilities.FileUtil;
import jp.co.gas.tokyo.accessLogBatch.utilities.StringUtil;

@Service
public class CreatePdfHkkLogFileService {


    @Autowired
    private PdfHkkLogMapper pdfHkkLogMapper;
    /**
     * ログ出力クラス
     */
    protected static final Logger logger = LoggerFactory.getLogger(CreatePdfHkkLogFileService.class);

    public int createService() {

    // ジョブ開始ログの出力
    String startMsg = "CreateFile" + ":" + Const.START_MESSAGE;
    logger.info(startMsg);

    int successRecordCount = 0; // 成功処理数
    int errorRecordCount = 0; // エラー処理数
    String uktkNo = "";


    List<PdfHkkLogData> dataList = pdfHkkLogMapper.selectPdfHkkList();
    if (dataList == null || dataList.isEmpty()) {
      logger.info("PdfHkkLog is noFile");
      return successRecordCount;

    }
    HttpHeaders httpHeaders = FileUtil.getHeadersForCsv("PDF出力ログ");
    
    for (PdfHkkLogData pdfHkkLogData : dataList) {
      logger.info("mtmrNo = "+ pdfHkkLogData.getMtmrNo() + " uktkNo= " + pdfHkkLogData.getUktkNo());
      successRecordCount++;
    }
    

    StringBuilder CSVBody = generatePdfHkkLogCsvBody(dataList);

    // 文字列に変換
		String CSVBodyStr = CSVBody.toString();

		// アクセスログ保存システムにログファイルを送信する
		//this.sendCsvDownloadLog(userDetails, csvData, request.getRemoteAddr());

		// CSVの主項目をBOM付きに変換
		//ByteBuffer buff = FileUtil.makeBomForCsv(CSVBodyStr);

    // 正常終了メッセージの出力
    logger.info("CreateFile" + ":" + Const.NORMAL_END_MESSAGE);

    return successRecordCount;
  }


  public static StringBuilder generatePdfHkkLogCsvBody(List<PdfHkkLogData> pdfHkkLogList) {
    

		StringBuilder CSV = new StringBuilder();
    // 項目不明なので、一旦こちらで
		CSV.append("見積No,");
		CSV.append("受付No,");
		CSV.append("見積件名,");
		CSV.append("出力担当者,");
		CSV.append("出力年月,");
		CSV.append("出力時刻\r\n");

		// データ書き出し
		for (PdfHkkLogData logData : pdfHkkLogList) {
			CSV.append(StringUtil.nvl(logData.getMtmrNo()) + ",");
			CSV.append(StringUtil.nvl(logData.getUktkNo()) + ",");
			CSV.append(StringUtil.nvl(logData.getMtmrNm()) + ",");
			CSV.append(StringUtil.nvl(logData.getRecRegUserId()) + ",");
			CSV.append(StringUtil.nvl(logData.getRecRegYmd()) + ",");
			CSV.append(StringUtil.nvl(logData.getRecRegJkk()) + "\r\n");
		}
		return CSV;
	}

}
