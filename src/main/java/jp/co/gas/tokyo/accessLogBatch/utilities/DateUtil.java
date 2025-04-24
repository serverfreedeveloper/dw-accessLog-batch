package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;

public class DateUtil {

	public static LocalDate parseLocalDate(String dateYmd) {
		return LocalDate.parse(dateYmd, DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT));
	}

	public static YearMonth parseYearMonth(String yearMonth) {
		return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("uuuu/MM").withResolverStyle(ResolverStyle.STRICT));
	}

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
		if (date != null && !StringUtils.isNullEmpty(formatStr)) {

			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			newFormatDate = sdf.format(date);
		}

		return newFormatDate;
	}

	/**
	 * 指定された日付を yyyy/MM/dd/Users/d-hongo/Documents/00.business/00.tokyo-gas/00.Git/受付（reception）/受付管理/src/main/java/tg/cars/reception/util/StringUtils.java に整形する
	 * @param dateYmd スラッシュ区切りにしたい日付（yyyyMMdd）
	 * @return スラッシュ区切り日付
	 */
	public static String formatSlash(String dateYmd) {
		if (dateYmd == null || StringUtils.isNullEmpty(dateYmd.trim())) {
			return "";
		} else {
			return dateYmd.substring(0, 4) + "/" + dateYmd.substring(4, 6) + "/" + dateYmd.substring(6, 8);
		}
	}

	/**
	 * 指定された日付を yyyy/MM/dd hh:mm に整形する
	 *
	 * @param dateYmdHm スラッシュ区切りにしたい日付（yyyyMMddhhmm）
	 * @return yyyy/MM/dd hh:mm
	 */
	public static String formatSlashAndHhMm(String dateYmdHm) {
		if (StringUtils.isNullEmpty(dateYmdHm)) {
			return "";
		} else {
			return dateYmdHm.substring(0, 4) + "/" + dateYmdHm.substring(4, 6) + "/" + dateYmdHm.substring(6, 8)
				+ ' ' + dateYmdHm.substring(8, 10) + ':' + dateYmdHm.substring(10, 12);
		}
	}

	/**
	 * 指定された時刻を hh:mm:ss に整形する
	 * @param timeHhmmss コロン区切りにしたい時刻（hhmmss）
	 * @return コロン区切りにしたい時刻
	 */
	public static String formatColon(String timeHhmmss) {
		if (StringUtils.isNullEmpty(timeHhmmss)) {
			return "";
		} else {
			return timeHhmmss.substring(0, 2) + ":" + timeHhmmss.substring(2, 4) + ":" + timeHhmmss.substring(4, 6);
		}
	}

	/**
	 * 指定された時刻を hh:mm に整形する
	 * 
	 * @param timeHhmm コロン区切りにしたい時刻（hhmm）
	 * @return hh:mm
	 */
	public static String formatColonHhmm(String timeHhmm) {
		if (StringUtils.isNullEmpty(timeHhmm)) {
			return "";
		}
		return timeHhmm.substring(0, 2) + ":" + timeHhmm.substring(2, 4);
	}

	/**
	 * 指定された時刻を 午前hh時mm分 に整形する
	 * @param timeHhmmss 午前hh時mm分にしたい時刻（hhmm）
	 * @return 午前hh時mm分にしたい時刻
	 */
	public static String formatJpDate(String time) {

		if (StringUtils.isNullEmpty(time)) {
			return "";
		} else {
			String amOrPm = "";
			String editTime = "";
			if (Integer.parseInt(time.substring(0, 2)) <= 12) {
				if (time.substring(0, 1).equals("0")) {
					editTime = time.substring(1, 4);
				} else {
					editTime = time;
				}
				amOrPm = "午前";
			} else {
				amOrPm = "午後";
				int intTime = Integer.parseInt(time.substring(0, 2)) - 12;
				editTime = String.valueOf(intTime) + time.substring(2, 4);
			}
			if (editTime.length() == 3) {
				return amOrPm + editTime.substring(0, 1) + "時" + editTime.substring(1, 3) + "分";
			} else {
				return amOrPm + editTime.substring(0, 2) + "時" + editTime.substring(2, 4) + "分";
			}
		}
	}

	/**
	 * 指定された年月日時刻を yyyy/MM/dd HH:mm に整形する
	 * @param str yyyy-MM-dd HH:mm:ss
	 * @return yyyy/MM/dd HH:mm
	 */
	public static String strToFormat(String str) {

		if (StringUtils.isNullEmpty(str)) {
			return "";
		} else {

			String editStr = str.replace("-", "/");

			String day = editStr.substring(0, 10);
			String time = editStr.substring(11, 16);

			return day + " " + time;
		}
	}

	/**
	 * 指定された年月日時刻を yyyyMMddに整形する
	 * @param str yyyy-MM-dd HH:mm:ss
	 * @return yyyyMMdd
	 */
	public static String convertStrTo8dDateStr(String str) {

		String editStr = str.replace("-", "");

		if (StringUtils.isNullEmpty(str)) {
			return "";
		} else {
			editStr = editStr.substring(0, 8);
		}
		return editStr;
	}

	/**
	 * 日付とフォーマットを渡し、過去日かどうかを判定
	 *
	 * @param date 判定する対象の日付
	 * @param formatStr 日付の形式
	 * @return boolean 「true：対象の日付は過去日」「false：対象の日付は過去日ではない」
	 */
	public static boolean isPastDate(String date, String formatStr) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

		// システム日付を取得しDate型へ変換
		Date currentDate = sdf.parse(getSysDate(formatStr));
		// 対象の日付をDate型に変換
		Date targetDate = sdf.parse(date);

		// 対象の日付がシステム日付より過去の場合
		if (targetDate.before(currentDate)) {
			return true;
		}
		return false;
	}

	/**
	 * 日付と時刻を連結し、画面表示用の形式に変換
	 * 
	 * @param ymd	日付（yyyyMMdd形式）
	 * @param jkk	時刻（HHmmss形式）
	 * @return	日時（yyyy/MM/dd HH:mm:ss形式）
	 */
	public static String concatYmdJkk(String ymd, String jkk) {

		if (StringUtils.isNullEmpty(ymd) || StringUtils.isNullEmpty(jkk)) {
            return "";
        }

		String ymdjkk = ymd + jkk;

		java.text.DateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
        java.text.DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date d = null;
        try {
            d = df1.parse(ymdjkk);
        } catch (Exception e) {
            return "";
        }

        return df2.format(d);

	}
}