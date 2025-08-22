package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;

import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.DwMicomMngSendLogService;
import lombok.RequiredArgsConstructor;

/**
 * アクセスログ送信バッチ 導管管理画面（マイコン）
 */
@RequiredArgsConstructor
@Component("DW_MICOM_MNG")
public class DwMicomMng extends JobBase {

    private final DwMicomMngSendLogService dwMicomMngSendLogService;

    @Override
    public void exec(String[] args) throws Exception {
      dwMicomMngSendLogService.exec();
    }
}
