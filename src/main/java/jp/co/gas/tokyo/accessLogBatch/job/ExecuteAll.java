package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;
import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.CreatePdfHkkLogFileService;
import jp.co.gas.tokyo.accessLogBatch.service.DwMngSendLogService;
import lombok.RequiredArgsConstructor;

/**
 * アクセスログ送信バッチ 全て実行
 */
@RequiredArgsConstructor
@Component("EXECUTE_ALL")
public class ExecuteAll extends JobBase {

  /** 導管管理画面用 */
  private final DwMngSendLogService dwMngSendLogService;

  /** 導管管理画面PDF用 */
  private final CreatePdfHkkLogFileService createPdfHkkLogFileService;

  @Override
  public void exec(String[] args) throws Exception {
    // 導管管理画面
    // dwMngSendLogService.exec();
    // 導管管理画面PDF出力
    createPdfHkkLogFileService.exec();
  }

}
