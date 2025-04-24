package jp.co.gas.tokyo.accessLogBatch.utilities;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * DataSourceUtilクラス
 */
@Setter
@Slf4j
public class DataSourceUtil {

	// 呼び出し元のConfigurationPropertiesプレフィックス下の設定項目を取得
	private String url;
	private String username;
	private String password;
	private String jndiName;

	/**
	 * JNDI情報を用いてDB接続ができる場合、できない場合を区別して
	 * 使用できるDataSourceオブジェクトを生成、返却する
	 *
	 * @return DB接続用DataSrouce
	 */
	public DataSource getDataSource() {

		try {
			// JBoss上に該当JNDI名でDataSourceが存在する場合はJBoss上のDataSourceを使用して接続するよう設定
			JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
			DataSource dataSource = dataSourceLookup.getDataSource(jndiName);
			log.info("JBoss上のDataSourceを使用して接続します");
			log.info("接続先JNDI名: " + jndiName);
			return dataSource;

		} catch (Exception e) {
			// JNDIで接続ができない場合≒ローカル実行時 or JBoss上にDataSourceがない場合は
			// プロパティファイルのユーザ名等からDataSourceを生成、接続するよう設定
			// ※ JBoss上でこちらが使用された場合はトランザクション処理などを正常に行うことができないため注意
			log.info("JBoss上のDataSourceが使用できないため、プロパティ上の値を使用して接続します");
			log.info("接続先アドレス: " + url);
			return DataSourceBuilder.create()
				.url(url)
				.username(username)
				.password(password)
				.build();

		}

	}

}