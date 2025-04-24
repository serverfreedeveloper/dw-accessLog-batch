package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ビジネスロジックユーティリティ
 */
public class BizUtil {

	/**
	 * システム日付の取得を行う
	 * @param formatStr フォーマット指定を行う文字列
	 * @return システム日付
	 */
	public static String getSysDate(String formatStr) {

		String sysDateStr = "";
		Date date = new Date();

		// 指定したフォーマットへの変換を行う
		sysDateStr = dateToFormat(date, formatStr);

		return sysDateStr;
	}

	/**
	 * Date型を指定したフォーマットへの文字列に変換を行う
	 * @param date 変換を行うDate型
	 * @param formatStr フォーマット指定を行う文字列
	 * @return newFormatDate 指定したフォーマットへ変換された時間の文字列
	 */
	public static String dateToFormat(Date date, String formatStr) {

		String newFormatDate = "";

		// フォーマットが指定されている場合は変換し
		// 指定されていない場合はブランクを返却する
		if (date != null && !StringUtil.isNullEmpty(formatStr)) {

			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			newFormatDate = sdf.format(date);
		}

		return newFormatDate;
	}

	/**
	 * String型の時間を指定したフォーマットへ変換を行う
	 * @param strDate 変換を行う文字列の時間
	 * @param formatStr フォーマット指定を行う文字列
	 * @return newFormatDate 指定したフォーマットへ変換された時間の文字列
	 * @throws Exception
	 */
	public static String strDateToFormat(String strDate, String beforeFormatStr, String afterFormatStr) throws Exception {

		String newFormatDate = "";

		// フォーマットが指定されている場合は変換し
		// 指定されていない場合はブランクを返却する
		if (strDate != null
				&& !StringUtil.isNullEmpty(beforeFormatStr)
				&& !StringUtil.isNullEmpty(afterFormatStr)) {

			// Date型へ変換を行う
			SimpleDateFormat sdf = new SimpleDateFormat(beforeFormatStr);
			Date date = sdf.parse(strDate);

			// 新しいフォーマットへと変換を行う
			sdf.applyPattern(afterFormatStr);
			newFormatDate = sdf.format(date);
		}

		return newFormatDate;
	}

	/**
	 * フォーマット後年月日を文字列に変換
	 * @param value 年月日
	 * @return ymdFormat フォーマット後の年月日
	 * @throws ParseException
	 */
	public static String getformattedYmdString(String value, String format) throws ParseException {

		String ymdValue = "";
		String ymdFormat = "";

		// "/"が含まれている場合
		if (value.contains("/")) {

			// "/"を除去
			ymdValue = value.replace("/", "");

		} else {

			ymdValue = value;

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date formatDate = sdf.parse(ymdValue);

		ymdFormat = new SimpleDateFormat(format).format(formatDate);

		return ymdFormat;

	}

	/**
	 * 日数の差分を計算し差分月数を返却
	 * @param startDate 開始年月日
	 * @param endDate 終了年月日
	 * @return monthDiffcount 差分月数
	 * @throws ParseException
	 */
	public static int getMonthDiff(String startDateString, String endDateString) throws ParseException {

	    int monthDiffCount = 0;

	    Date startDate = DateFormat.getDateInstance().parse(startDateString);
	    Date endDate = DateFormat.getDateInstance().parse(endDateString);

	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(startDate);

	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endDate);

	    // 開始年月日が終了年月日より過去日の場合
	    while (startCal.before(endCal)) {

	    	// 開始年月日を一ヶ月進める
	    	startCal.add(Calendar.MONTH, 1);

	    	// 差分をインクリメント
	    	monthDiffCount++;

	    }

	    return monthDiffCount;
	}

	private static int TOKEN_LENGTH = 16;//16*2=32バイト

	/**
	 * 32バイトのトークンを作成
	 * @return 生成された32バイトのトークン文字列
	 */
	public static String getToken() {

		byte token[] = new byte[TOKEN_LENGTH];
		StringBuffer buf = new StringBuffer();
		SecureRandom random = null;

		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(token);

			for (int i = 0; i < token.length; i++) {
				buf.append(String.format("%02x", token[i]));
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return buf.toString();
	}
}
