package jp.co.gas.tokyo.accessLogBatch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;

import jp.co.gas.tokyo.accessLogBatch.common.azure.storage.AzureBlobStorageFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 導管管理画面（ガス小工事）CSV出力時アクセスログ送信用Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DwSmallWorkMngSendLogService {

  /** ログ送信Service */
  private final SendAccessLogService sendAccessLogService;

  /** クライアント生成Factory */
  private final AzureBlobStorageFactory azureBlobStorageFactory;

  /** アクセスログ保存一時ディレクトリ（導管管理画面） */ 
  @Value("${ALSS_LOG_DIR_DW_MNG}")
  private String ALSS_LOG_DIR_DW_MNG;

  /** ストレージアカウント名（導管系） */
  @Value("${DW_STOR_NAME}")
  private String storageAccountName;

  /** コンテナ名（導管管理画面（ガス小工事）） */
  @Value("${DW_SMALL_WORK_MNG_APP_NAME}")
  private String containerName;

  public void exec() {

    log.info("==================== アクセスログ送信 ガス小工事 処理開始 ====================");

    try {

      // コンテナ操作クライアント作成
      BlobContainerClient containerClient = azureBlobStorageFactory
          .createBlobContainerClient(this.storageAccountName, this.containerName);

      log.info("url: {}, storage: {}, containername: {}", 
        containerClient.getBlobContainerUrl(), 
        containerClient.getAccountName(), 
        containerClient.getBlobContainerName());

      // アクセスログリスト取得
      PagedIterable<BlobItem> blobList = containerClient.listBlobs();
      List<String> fileNameList = new ArrayList<String>();
      blobList.forEach(blob -> {
        fileNameList.add(blob.getName());
      });

      // 送信処理実行
      sendAccessLogService.exec(fileNameList, containerClient, ALSS_LOG_DIR_DW_MNG);

    } catch (Exception e) {
      log.warn("アクセスログ ガス小工事 送信処理に失敗しました。", e);
    } finally {
      log.info("==================== アクセスログ送信 ガス小工事 処理終了 ====================");
    }
  }
}
