package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;

import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.DwBizcomMngSendLogService;
import lombok.RequiredArgsConstructor;

/**
 * アクセスログ送信バッチ 導管管理画面（電子業連）
 */
@RequiredArgsConstructor
@Component("DW_BIZCOM_MNG")
public class DwBizcomMng extends JobBase {

    private final DwBizcomMngSendLogService dwBizcomMngSendLogService;

    @Override
    public void exec(String[] args) throws Exception {
      dwBizcomMngSendLogService.exec();
    }
}
