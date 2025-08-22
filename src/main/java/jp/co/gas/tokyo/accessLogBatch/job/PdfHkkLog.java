package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;

import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.CreatePdfHkkLogFileService;
import lombok.RequiredArgsConstructor;

/**
 * PDF発行ログの出力
 */
@RequiredArgsConstructor
@Component("PDF_HKK_LOG")
public class PdfHkkLog extends JobBase {

    private final CreatePdfHkkLogFileService createFileService;

    @Override
    public void exec(String[] args) throws Exception {
        createFileService.exec();
    }
}
