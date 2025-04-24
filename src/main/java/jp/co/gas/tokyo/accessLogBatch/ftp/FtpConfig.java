package jp.co.gas.tokyo.accessLogBatch.ftp;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * FTP定義 Bean
 */
@Setter
@Getter
public class FtpConfig implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** ホスト名称 */
	private String hostName;
	/** ポート番号 */
	private int port;
	/** ユーザ名 */
	private String userName;
	/** パスワード */
	private String password;
	/** バイナリ転送 */
	private boolean binaryTransfer;
	/** パッシブモード */
	private boolean usePassiveMode;
	/** ホストパス */
	private String hostPath;
	/** ローカルパス */
	private String localPath;
	/** エンコード */
	private String encoding;
}