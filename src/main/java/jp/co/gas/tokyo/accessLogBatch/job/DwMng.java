package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;
import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.DwMngSendLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * アクセスログ送信バッチ 導管管理画面
 */
@Slf4j
@RequiredArgsConstructor
@Component("DW_MNG")
public class DwMng extends JobBase {

  private final DwMngSendLogService dwMngSendLogService;

  @Override
  public void exec(String[] args) throws Exception {

    dwMngSendLogService.exec();
  }

}
