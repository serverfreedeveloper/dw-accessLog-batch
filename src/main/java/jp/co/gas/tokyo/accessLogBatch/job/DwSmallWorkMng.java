package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;

import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.DwSmallWorkMngSendLogService;
import lombok.RequiredArgsConstructor;

/**
 * アクセスログ送信バッチ 導管管理画面（ガス小工事）
 */
@RequiredArgsConstructor
@Component("DW_SMALL_WORK_MNG")
public class DwSmallWorkMng extends JobBase {

    private final DwSmallWorkMngSendLogService dwSmallWorkMngSendLogService;

    @Override
    public void exec(String[] args) throws Exception {
      dwSmallWorkMngSendLogService.exec();
    }
}
