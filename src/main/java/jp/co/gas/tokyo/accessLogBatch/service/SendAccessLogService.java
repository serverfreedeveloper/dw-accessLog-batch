package jp.co.gas.tokyo.accessLogBatch.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import jp.co.gas.tokyo.accessLogBatch.common.ftp.FtpService;
import jp.co.gas.tokyo.accessLogBatch.config.AlssFtpConfig;
import jp.co.gas.tokyo.accessLogBatch.utilities.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ログ送信Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SendAccessLogService {

  /**
   * アクセスログ保存システムメンテナンス開始時間
   */
  @Value("${ALSS_MENTE_START}")
  private String ALSS_MENTE_START;

  /**
   * アクセスログ保存システムメンテナンス終了時間
   */
  @Value("${ALSS_MENTE_END}")
  private String ALSS_MENTE_END;

  /**
   * ALSS用FTPクライアント設定
   */
  private final AlssFtpConfig alssFtpConfig;

  /**
   * 
   * @param fileNameList ストレージ上のファイル名一覧
   * @param client コンテナ操作クライアント(Azure Blob Storage)
   * @param alssLogDir ログ一時保存ディレクトリ
   */
  public void exec(List<String> fileNameList, BlobContainerClient client, String alssLogDir) {

    // ファイルリストが0件の場合ALSS_LOG_DIR_KHSN_MNG
    if (ObjectUtils.isEmpty(fileNameList)) {
      log.info("送信対象ファイルが存在しません。");
      return;
    }

    // FTPクライアント作成
    FtpService ftpService = new FtpService(alssFtpConfig.getAlssFtpConfig(alssLogDir));

    // ログ送信カウント
    int successCount = 0;
    int failureCount = 0;

    // ファイルごとに送信処理実行
    for (String fileName : fileNameList) {

      // アクセスログ保存システムメンテナンスの場合
      String nowTime = DateUtil.getSysDate("dHHmm");
      if (ALSS_MENTE_START.compareTo(nowTime) >= 0 && ALSS_MENTE_END.compareTo(nowTime) <= 0) {

        // 処理を終了
        log.info("アクセスログ保存システムメンテナンスの為、ファイルを送信しませんでした。アクセスログ送信処理件数 成功：" + successCount + "件, 失敗："
            + failureCount + "件, 未処理：" + (fileNameList.size() - successCount - failureCount));
        break;
      }

      // txtファイルのみ送信
      if (fileName.endsWith(".txt")) {
        // ファイル操作クライアント作成
        BlobClient file = client.getBlobClient(fileName);

        // ファイルがストレージ上に存在している場合
        if (file.exists()) {

          // ファイルパス
          File accessLogDir = new File(alssLogDir);
          File accessLogFile = new File(alssLogDir + fileName);

          // アクセスログの格納先が存在しない場合は作成
          if (!accessLogDir.exists()) {
            // mkdirs()で親ディレクトリごと生成
            accessLogDir.mkdirs();
          }

          try {
            // 対象のファイルをストレージから取得、送信対象のアクセスログファイルがサーバに存在する場合は上書き
            accessLogFile.createNewFile();
            file.downloadToFile(accessLogFile.getAbsolutePath(), true);
          } catch (Exception e) {
            // 失敗カウント追加
            failureCount++;

            // 処理を終了し、次のファイル送信を実行
            log.warn("アクセスログファイルの取得に失敗しました。ファイル名：" + fileName + ", エラー：" + e.getMessage());
            continue;
          }

          try {
            // アクセスログ保存システムへ送信
            ftpService.setLocalPath(accessLogFile.getAbsolutePath());
            ftpService.put();
            log.info("アクセスログ保存システム送信成功ファイル：[" + fileName + "]");

            // 送信成功時にログストレージ上のファイルを削除
            file.delete();
            // 成功カウント追加
            successCount++;
          } catch (Exception e) {
            // 失敗カウント追加
            failureCount++;
            log.warn("アクセスログ保存システム送信失敗ファイル：[" + fileName + "], エラー：" + e.getMessage());
          } finally {
            // VM上のローカルファイルを削除
            accessLogFile.delete();
          }
        } else {
          log.warn("アクセスログファイルが存在しません。ファイル名：" + fileName);
        }
      }
    }

    log.info("アクセスログ送信処理件数 成功：" + successCount + "件, 失敗：" + failureCount + "件");

    // 全てのファイル送信後、接続をクローズ
    try {
      ftpService.disconnect();
    } catch (Exception e) {
      // クローズ時のエラーは無視
    }
  }
}
