package jp.co.gas.tokyo.accessLogBatch;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jp.co.gas.tokyo.accessLogBatch.batch.Job;
import jp.co.gas.tokyo.accessLogBatch.utilities.Const;

/**
 * ジョブ実行クラス
 */
@SpringBootApplication
@MapperScan("jp.co.gas.tokyo.accessLogBatch.mapper")
public class PdfHkkLogCreateBatch {

	/**
	 * バッチ実行メソッド
	 *
	 * @param args 実行するバッチID
	 */
	public static void main(String[] args) {

		// 引数なしで実行された場合
		if (args == null || args.length == 0) {
			args = new String[1];
			args[0] = "EXECUTE_ALL";
		}

		Logger logger = LoggerFactory.getLogger(PdfHkkLogCreateBatch.class);
		ApplicationContext context = SpringApplication.run(PdfHkkLogCreateBatch.class, args);

		try {

			// パラメータの数分ループ
			for (int i = 0; i < args.length; i++) {

				// ジョブ開始ログの出力
				// String startMsg = args[i] + ":" + Const.START_MESSAGE;
				// logger.info(startMsg);

				Job job = (Job) context.getBean(args[i]);
				job.exec(args);

				// 正常終了メッセージの出力
				// logger.info(args[i] + ":" + Const.NORMAL_END_MESSAGE);
			}

		} catch (Throwable e) {

			// Exceptionのメッセージによる警告終了判定
			if (Const.WARNING_END_MESSAGE.equals(e.getMessage())) {
				// 警告終了メッセージの出力（IESBT0003）
				logger.warn(args[0] + ":" + e.getMessage());

				// 返り値 警告終了:1をセット
				System.exit(Const.WARNING_CD);
			} else {
				// エラーログの出力
				logger.error(args[0] + ":" + Const.ABNORMAL_END_MASSAGE);
				logger.error(Const.STRING_EXCEPTION, e);

				// 返り値 エラー:9をセット
				System.exit(Const.ERROR_CD);
			}
		}

		// 返り値 正常終了:0をセット
		System.exit(Const.NORMAL_CD);
	}

	@Bean
	public Validator localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}

}
