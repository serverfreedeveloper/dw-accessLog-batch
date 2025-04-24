package jp.co.gas.tokyo.accessLogBatch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ジョブベースインターフェース
 */
public abstract class JobBase implements Job {

	protected Logger logger = null;

	/**
	 * コンストラクタ
	 */
	public JobBase() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

}
