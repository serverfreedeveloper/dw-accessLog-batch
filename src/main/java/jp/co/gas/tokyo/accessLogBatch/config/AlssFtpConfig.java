package jp.co.gas.tokyo.accessLogBatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jp.co.gas.tokyo.accessLogBatch.common.ftp.FtpConfig;

@Component
public class AlssFtpConfig {
  @Value("${ALSS_FTP_HOST}")
  private String ALSS_FTP_HOST;

  @Value("${ALSS_FTP_PORT}")
  private int ALSS_FTP_PORT;

  @Value("${ALSS_FTP_USER}")
  private String ALSS_FTP_USER;

  @Value("${ALSS_FTP_PASS}")
  private String ALSS_FTP_PASS;

  @Value("${ALSS_FTP_DIR}")
  private String ALSS_FTP_DIR;

  public FtpConfig getAlssFtpConfig(String localPath) {

    FtpConfig ftpConfig = new FtpConfig();
    ftpConfig.setHostName(ALSS_FTP_HOST);
    ftpConfig.setPort(ALSS_FTP_PORT);
    ftpConfig.setUserName(ALSS_FTP_USER);
    ftpConfig.setPassword(ALSS_FTP_PASS);
    ftpConfig.setBinaryTransfer(true);
    ftpConfig.setUsePassiveMode(true);
    ftpConfig.setHostPath(ALSS_FTP_DIR);
    ftpConfig.setLocalPath(localPath);
    ftpConfig.setEncoding("MS932");

    return ftpConfig;
  }
}
