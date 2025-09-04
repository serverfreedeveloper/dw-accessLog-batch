package jp.co.gas.tokyo.accessLogBatch.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
   * コンテナ上のファイルを送信
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
      if (isMaintenance()) {
        // 処理を終了
        log.info("アクセスログ保存システムメンテナンスの為、ファイルを送信しませんでした。アクセスログ送信処理件数 成功：{}件, 失敗：{}件, 未処理：{}件", 
          successCount, 
          failureCount, 
          (fileNameList.size() - successCount - failureCount));
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
            log.warn("アクセスログファイルの取得に失敗しました。ファイル名：[{}]", fileName, e);
            continue;
          }

          try {
            // アクセスログ保存システムへ送信
            ftpService.setLocalPath(accessLogFile.getAbsolutePath());
            ftpService.put();
            log.info("アクセスログ保存システム送信成功ファイル：[{}]", fileName);

            // 送信成功時にログストレージ上のファイルを削除
            file.delete();
            // 成功カウント追加
            successCount++;
          } catch (Exception e) {
            // 失敗カウント追加
            failureCount++;
            log.warn("アクセスログ保存システム送信失敗ファイル：[{}]", fileName, e);
          } finally {
            // VM上のローカルファイルを削除
            accessLogFile.delete();
          }
        } else {
          log.warn("アクセスログファイルが存在しません。ファイル名：[{}]", fileName);
        }
      }
    }

    log.info("アクセスログ送信処理件数 成功：{}件, 失敗：{}件", successCount, failureCount);

    // 全てのファイル送信後、接続をクローズ
    try {
      ftpService.disconnect();
    } catch (Exception e) {
      // クローズ時のエラーは無視
    }
  }

  /**
   * 指定フォルダのファイルを送信
   * @param alssLogDir フォルダ
   */
  public void exec(String alssLogDir) {
    File dir = new File(alssLogDir);

    if (dir.exists() && dir.isDirectory()) {
        File[] files = dir.listFiles();
        if (files != null) {
            List<String> fileNames = Arrays.stream(files)
              .map(File::getName)
              .collect(Collectors.toList());

            // ファイル送信
            this.exec(fileNames, alssLogDir);
        }
    } else {
        log.info("指定ディレクトリが存在しないか、ディレクトリではありません。[{}]", alssLogDir);
    }
  }

  /**
   * 一時フォルダ上のファイルを送信
   * @param fileNameList ストレージ上のファイル名一覧
   * @param alssLogDir ログ一時保存ディレクトリ
   */
  public void exec(List<String> fileNameList, String alssLogDir) {

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
      if (isMaintenance()) {
        // 処理を終了
        log.info("アクセスログ保存システムメンテナンスの為、ファイルを送信しませんでした。アクセスログ送信処理件数 成功：{}件, 失敗：{}件, 未処理：{}件", 
          successCount, 
          failureCount, 
          (fileNameList.size() - successCount - failureCount));
        break;
      }

      // txtファイルのみ送信
      if (fileName.endsWith(".txt")) {
        String filePath = alssLogDir + fileName;
        File file = new File(filePath);
        
        // ファイルがフォルダ上に存在している場合
        if (file.exists()) {
          try {
            // アクセスログ保存システムへ送信
            ftpService.setLocalPath(file.getAbsolutePath());
            ftpService.put();
            log.info("アクセスログ保存システム送信成功ファイル：[{}]", fileName);

            // 成功時、ローカルファイルを削除
            file.delete();

            // 成功カウント追加
            successCount++;
          } catch (Exception e) {
            // 失敗カウント追加
            failureCount++;
            log.warn("アクセスログ保存システム送信失敗ファイル：[{}]", fileName, e);
          } finally {
            // ローカルファイルを削除
            // file.delete();
          }
        } else {
          log.warn("アクセスログファイルが存在しません。ファイル名：[{}]", fileName);
        }
      }
    }

    log.info("アクセスログ送信処理件数 成功：{}件, 失敗：{}件", successCount, failureCount);

    // 全てのファイル送信後、接続をクローズ
    try {
      ftpService.disconnect();
    } catch (Exception e) {
      // クローズ時のエラーは無視
    }
  }

  /**
   * アクセスログ保存システムメンテナンス判定
   * TODO ただし、現状この処理は常にfalseになるため機能はしていない
   * 開始日・終了日の定義が "dHHmm" フォーマットと合っていないため要調査。
   * 運用上必要であれば、"ddHHmm" のフォーマットで合わせて判定
   * @return true:メンテナンス期間 false:メンテナンス期間外
   */
  private boolean isMaintenance() {
    // アクセスログ保存システムメンテナンスの場合
    String nowTime = DateUtil.getSysDate("dHHmm");

    if (ALSS_MENTE_START.compareTo(nowTime) >= 0 && ALSS_MENTE_END.compareTo(nowTime) <= 0) {
      return true;
    }
    return false;
  }
}
