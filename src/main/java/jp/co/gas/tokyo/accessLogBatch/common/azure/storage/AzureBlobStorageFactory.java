package jp.co.gas.tokyo.accessLogBatch.common.azure.storage;

import org.springframework.stereotype.Component;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AzureBlobStorageFactory {

  /**
   * 接続用クライアントを生成・返却
   */
  public BlobContainerClient createBlobContainerClient(final String storageAccountName, final String containerName) {
    // エンドポイントURI
    String endpointUri = "https://" + storageAccountName + ".blob.core.windows.net/";

    log.info(storageAccountName);
    log.info(containerName);
    log.info(endpointUri);

    DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder().build();
    defaultCredential.getToken(null);
    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint(endpointUri)
        .credential(defaultCredential).buildClient();
    return blobServiceClient.getBlobContainerClient(containerName);
  }
}
