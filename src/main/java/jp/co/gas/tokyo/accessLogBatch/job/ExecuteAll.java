package jp.co.gas.tokyo.accessLogBatch.job;

import org.springframework.stereotype.Component;
import jp.co.gas.tokyo.accessLogBatch.batch.JobBase;
import jp.co.gas.tokyo.accessLogBatch.service.CreatePdfHkkLogFileService;
import jp.co.gas.tokyo.accessLogBatch.service.DwBizcomMngSendLogService;
import jp.co.gas.tokyo.accessLogBatch.service.DwMicomMngSendLogService;
import jp.co.gas.tokyo.accessLogBatch.service.DwMngSendLogService;
import jp.co.gas.tokyo.accessLogBatch.service.DwSmallWorkMngSendLogService;
import jp.co.gas.tokyo.accessLogBatch.service.DwUketsukeMngSendLogService;
import lombok.RequiredArgsConstructor;

/**
 * アクセスログ送信バッチ 全て実行
 */
@RequiredArgsConstructor
@Component("EXECUTE_ALL")
public class ExecuteAll extends JobBase {

  /** 導管管理画面用 */
  private final DwMngSendLogService dwMngSendLogService;

  /** 導管管理画面（導管タブ）用 */
  private final DwUketsukeMngSendLogService dwUketsukeMngSendLogService;

  /** 導管管理画面（ガス小工事）用 */
  private final DwSmallWorkMngSendLogService dwSmallWorkMngSendLogService;

  /** 導管管理画面（マイコン）用 */
  private final DwMicomMngSendLogService dwMicomMngSendLogService;

  /** 導管管理画面（電子業連）用 */
  private final DwBizcomMngSendLogService dwBizcomMngSendLogService;
  
  /** 導管管理画面PDF用 */
  private final CreatePdfHkkLogFileService createPdfHkkLogFileService;

  @Override
  public void exec(String[] args) throws Exception {
    // 導管管理画面
    dwMngSendLogService.exec();
    // 導管管理画面（導管タブ）
    dwUketsukeMngSendLogService.exec();
    // 導管管理画面（ガス小工事）
    dwSmallWorkMngSendLogService.exec();
    // 導管管理画面（マイコン）
    dwMicomMngSendLogService.exec();
    // 導管管理画面（電子業連）
    dwBizcomMngSendLogService.exec();
    // 導管管理画面PDF出力
    createPdfHkkLogFileService.exec();
  }

}
