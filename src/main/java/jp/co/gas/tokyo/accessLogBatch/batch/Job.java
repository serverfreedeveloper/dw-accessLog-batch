package jp.co.gas.tokyo.accessLogBatch.batch;

/**
 * ジョブインターフェース
 */
public interface Job {

	/**
	 * execメソッド
	 * @param args バッチ実行時の引数
	 * @throws Exception エラー
	 */
	void exec(String[] args) throws Exception;

}
