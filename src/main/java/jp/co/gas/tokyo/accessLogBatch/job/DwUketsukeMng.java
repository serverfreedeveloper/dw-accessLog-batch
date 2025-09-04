package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;

import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.DwUketsukeMngSendLogService;
import lombok.RequiredArgsConstructor;

/**
 * アクセスログ送信バッチ 導管管理画面（導管タブ）
 */
@RequiredArgsConstructor
@Component("DW_UKETSUKE_MNG")
public class DwUketsukeMng extends JobBase {

    private final DwUketsukeMngSendLogService dwUketsukeMngSendLogService;

    @Override
    public void exec(String[] args) throws Exception {
      dwUketsukeMngSendLogService.exec();
    }
}
