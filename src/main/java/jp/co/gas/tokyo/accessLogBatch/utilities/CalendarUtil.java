package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日付ユーティリティクラス
 */
public class CalendarUtil {
    /**
	 * 日付文字列(YYYYMMDD)から曜日を取得する
	 * @param ymd 日付文字列(YYYYMMDD)
	 * @return 結果文字列
	 */
	public static String getYobi(String ymd){
		try{
			//曜日
			String yobi[] = {"日","月","火","水","木","金","土"};

			//日付チェック
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			sdf.parse(ymd);

			//年・月を取得する
			int y = Integer.parseInt(ymd.substring(0,4));
			int m = Integer.parseInt(ymd.substring(4,6))-1;
			int d = Integer.parseInt(ymd.substring(6,8));

			//取得した年月の最終年月日を取得する
			Calendar cal = Calendar.getInstance();
			cal.set(y, m, d);

			//YYYYMMDD形式にして変換して返す
			return yobi[cal.get(Calendar.DAY_OF_WEEK)-1];

		} catch(Exception ex) {
			return null;
		}
	}
}
