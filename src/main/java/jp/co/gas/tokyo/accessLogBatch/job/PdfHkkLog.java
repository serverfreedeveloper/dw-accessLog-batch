package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.CreatePdfHkkLogFileService;

/**
 * PDF発行ログの出力
 */
@Component("PDF_HKK_LOG")
public class PdfHkkLog extends JobBase {

    @Autowired
    private CreatePdfHkkLogFileService createFileService;


    @Override
    public void exec(String[] args) throws Exception {

        createFileService.createService();
    }
}
