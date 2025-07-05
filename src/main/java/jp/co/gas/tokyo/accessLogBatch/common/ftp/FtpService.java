package jp.co.gas.tokyo.accessLogBatch.common.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FtpService {

  private FtpConfig config;
  private FTPClient client;
  private boolean isConnected;

  /**
   * コンストラクタ
   *
   * @param config FtpConfig
   */
  public FtpService(FtpConfig config) {
    this.config = config;
    this.isConnected = false;
  }

  /**
   * コンストラクタ
   *
   * @param hostName ホスト名(IPアドレス:127.0.0.1)
   * @param port ポート番号(21)
   * @param userName ユーザー
   * @param password パスワード
   * @param binaryTransfer バイナリ転送モード(true: Yes, false: No)
   * @param usePassiveMode パッシブモード(true: Yes, false: No)
   * @param hostPath ホスト側パス
   * @param localPath ローカル側パス
   * @param encoding エンコーディング(SJIS, MS932, EUC_JP など)
   */
  public FtpService(String hostName, int port, String userName, String password,
      boolean binaryTransfer, boolean usePassiveMode, String hostPath, String localPath,
      String encoding) {
    this.config = new FtpConfig();
    this.config.setHostName(hostName);
    this.config.setPort(port);
    this.config.setUserName(userName);
    this.config.setPassword(password);
    this.config.setBinaryTransfer(binaryTransfer);
    this.config.setUsePassiveMode(usePassiveMode);
    this.config.setHostPath(hostPath);
    this.config.setLocalPath(localPath);
    this.config.setEncoding(encoding);
    this.isConnected = false;
  }

  /**
   * パラメータチェック
   *
   * @return true: 正常, false: 異常
   */
  private boolean check() {
    boolean success = true;
    // ホスト名
    if (isEmpty(this.config.getHostName())) {
      log.info("hostName Parameter Failed");
      success = false;
    }
    // ポート
    if (this.config.getPort() == 0) {
      log.info("port Parameter Failed");
      success = false;
    }
    // ユーザー名
    if (isEmpty(this.config.getUserName())) {
      log.info("userName Parameter Failed");
      success = false;
    }
    // パスワード
    if (isEmpty(this.config.getPassword())) {
      log.info("password Parameter Failed");
      success = false;
    }
    // ホストパス
    if (isEmpty(this.config.getHostPath())) {
      log.info("hostPath Parameter Failed");
      success = false;
    }
    // ローカルパス
    if (isEmpty(this.config.getLocalPath())) {
      log.info("localPath Parameter Failed");
      success = false;
    }
    if (isEmpty(this.config.getEncoding())) {
      log.info("encoding Parameter Failed");
      success = false;
    }
    return success;
  }

  /**
   * 接続
   *
   * @return true: 正常, false: 異常
   * @throws Exception
   */
  public boolean connect() throws Exception {
    boolean success = check();
    if (!success)
      return false;
    this.client = new FTPClient();
    log.info("connect....");
    this.client.setControlEncoding(this.config.getEncoding());
    client.connect(this.config.getHostName(), this.config.getPort());
    log.info(
        "Connected to Server: " + this.config.getHostName() + " on " + this.client.getRemotePort());
    log.info(this.client.getReplyString());
    this.client.login(this.config.getUserName(), this.config.getPassword());
    log.info(client.getReplyString());
    if (!FTPReply.isPositiveCompletion(this.client.getReplyCode())) {
      log.info("Login Failed");
      this.client.disconnect();
      return false;
    } else {
      this.isConnected = true;
    }
    // Binary転送モードの場合
    if (this.config.isBinaryTransfer()) {
      this.client.setFileType(FTP.BINARY_FILE_TYPE);
      log.info("Mode binaryTransfer: true");
    }
    // PASVモードの場合
    if (this.config.isUsePassiveMode()) {
      this.client.enterLocalPassiveMode();
      log.info("Mode usePassiveMode: ON");
    } else {
      this.client.enterLocalActiveMode();
      log.info("Mode usePassiveMode: OFF");
    }
    // ディレクトリ移動
    success = this.client.changeWorkingDirectory(this.config.getHostPath());
    if (!success) {
      log.info("Server Directory Failed");
      this.client.disconnect();
      return false;
    }
    log.info(this.client.getReplyString());
    success = FTPReply.isPositiveCompletion(this.client.getReplyCode());
    log.info("Connection: " + (success ? "OK" : "NG"));
    log.info("-----------------------------------");
    return success;
  }

  /**
   * 切断
   *
   * @return true: 正常, false: 異常
   * @throws Exception
   */
  public boolean disconnect() throws Exception {
    if (this.isConnected) {
      client.logout();
      log.info(client.getReplyString());
      if (client.isConnected())
        client.disconnect();
    }
    return true;
  }

  /**
   * 送信
   *
   * @return true: 正常, false: 異常
   * @throws Exception
   */
  public boolean put() throws Exception {
    boolean success = true;
    if (!this.isConnected)
      success = connect();
    if (success) {
      // ディレクトリ移動
      success = this.client.changeWorkingDirectory(this.config.getHostPath());
      if (!success) {
        log.info("Server Directory Failed");
        this.client.disconnect();
        return false;
      }
      return putFiles(new File(this.config.getLocalPath()),
          this.config.getHostPath() + (this.config.getHostPath().endsWith("/") ? "" : "/"));
    }
    return success;
  }

  /**
   * ファイル送信
   *
   * @param file FTPFile
   * @param hostPath ホスト側パス
   * @throws Exception
   */
  private boolean putFiles(File file, String hostPath) throws Exception {
    boolean success = true;
    if (file.isFile()) {
      FileInputStream is = null;
      try {
        is = new FileInputStream(file);
        log.info("PUT File Name: " + file.getName());
        this.client.storeFile(hostPath + file.getName(), is);
        is.close();
        log.info("FTP PUT Completed");
      } catch (Exception e) {
        log.info("FTP PUT Failed: " + file.getName());
        log.info(e.getMessage());
        success = false;
      } finally {
        if (is != null)
          is.close();
      }
    } else if (file.isDirectory()) {
      String dirName = hostPath + file.getName() + "/";
      log.info("Make Directory: " + dirName);
      // ディレクトリがなければ作る
      success = this.client.makeDirectory(dirName);
      if (success) {
        success = this.client.changeWorkingDirectory(dirName);
      } else {
        // ディレクトリが作れない場合、移動してみる
        success = this.client.changeWorkingDirectory(dirName);
        if (!success) {
          log.info("Server Directory Failed: " + dirName);
          return success;
        }
      }
      log.info("-----------------------------------");
      File[] files = file.listFiles();
      for (File f : files) {
        success = putFiles(f, dirName);
        if (!success)
          return success;
      }
    } else {
    }
    return success;
  }

  /**
   * 受信
   */
  public boolean get() throws Exception {
    boolean success = true;
    if (!this.isConnected)
      success = connect();
    if (success) {
      // ディレクトリ移動
      success = this.client.changeWorkingDirectory(this.config.getHostPath());
      if (!success) {
        log.info("Server Directory Failed");
        this.client.disconnect();
        return false;
      }
      String fileNames[] = this.client.listNames();
      if (fileNames != null) {
        for (int i = 0; i < fileNames.length; i++) {
          log.info("Get File Name: " + fileNames[i]);
        }
      }
      log.info("-----------------------------------");
      for (FTPFile f : this.client.listFiles()) {
        getFiles(f, this.config.getLocalPath());
      }
    }
    return success;
  }

  /**
   * ファイル取得
   *
   * @param file FTPFile
   * @param localPath ローカル側パス
   * @throws Exception
   */
  private boolean getFiles(FTPFile file, String localPath) throws Exception {
    boolean success = true;
    if (!file.getName().equals(".") && !file.getName().equals("..")) {
      String currentDir = client.printWorkingDirectory();
      if (file.isFile()) {
        String filename = file.getName();
        filename = new String(filename.getBytes("MS932"), "UTF-8");
        String utf8filename =
            client.printWorkingDirectory() + (currentDir.endsWith("/") ? "" : "/") + file.getName();
        log.info("Get File Name: " + filename);
        log.info("Get UTF8 File Name: " + utf8filename);
        String localPathName = localPath + (localPath.endsWith("/") ? "" : "/") + filename;
        log.info("Local Path Name: " + localPathName);
        FileOutputStream os = null;
        try {
          os = new FileOutputStream(localPathName);
          client.retrieveFile(utf8filename, os);
          os.close();
          log.info("FTP GET Completed");
        } catch (Exception e) {
          log.info("FTP GET Failed: " + filename);
          log.info(e.getMessage());
          success = false;
        } finally {
          if (os != null)
            os.close();
        }
      } else if (file.isDirectory()) {
        File localDir = new File(localPath + (localPath.endsWith("/") ? "" : "/") + file.getName());
        String path = localPath;
        if (!localDir.exists()) {
          localDir.mkdirs();
          path = localPath + (localPath.endsWith("/") ? "" : "/") + file.getName();
        }
        success = client.doCommand("CWD", client.printWorkingDirectory()
            + (currentDir.endsWith("/") ? "" : "/") + file.getName());
        for (FTPFile f : client.listFiles()) {
          success = getFiles(f, path);
          if (!success)
            break;
        }
        // client.doCommand("CDUP", "");
      }
    }
    return success;
  }

  /**
   * 空文字列チェック
   *
   * @param value 文字列
   * @return null または 空文字列 なら true , それ以外なら false
   */
  public boolean isEmpty(String value) {
    if (value == null || value.length() == 0)
      return true;
    else
      return false;
  }

  /**
   * ホスト側パスセット
   *
   * @param hostPath ホスト側パス
   */
  public void setHostPath(String hostPath) {
    this.config.setHostPath(hostPath);
  }

  /**
   * ローカル側パスセット
   *
   * @param localPath ローカル側パス
   */
  public void setLocalPath(String localPath) {
    this.config.setLocalPath(localPath);
  }
}
