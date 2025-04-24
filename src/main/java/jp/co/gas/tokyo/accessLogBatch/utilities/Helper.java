package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 文字列形式変換
 */
public class Helper {

	private static final SimpleDateFormat SDF1 = new SimpleDateFormat("yyyy/MM/dd");

	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuu/MM/dd");

	/**
	 * 1111-222-XXXX
	 * @param customerNo お客さま番号
	 */
	public static String editCustomerNo(String customerNo) {
		if (customerNo == null || customerNo.trim().isEmpty()) {
			return "";
		} else {
			return customerNo.replaceAll("^(.{4})(.{3})(.{4})$", "$1-$2-$3");
		}
	}

	/**
	 * お客さま番号上4桁
	 * @param customerNo お客さま番号
	 */
	public static String editCustomerNo1(String customerNo) {
		if (customerNo == null || customerNo.trim().isEmpty()) {
			return "";
		} else {
			return customerNo.replaceAll("^(.{4})(.{3})(.{4})$", "$1");
		}
	}

	/**
	 * お客さま番号中3桁
	 * @param customerNo お客さま番号
	 */
	public static String editCustomerNo2(String customerNo) {
		if (customerNo == null || customerNo.trim().isEmpty()) {
			return "";
		} else {
			return customerNo.replaceAll("^(.{4})(.{3})(.{4})$", "$2");
		}

	}

	/**
	 * お客さま番号下4桁
	 * @param customerNo お客さま番号
	 */
	public static String editCustomerNo3(String customerNo) {
		if (customerNo == null || customerNo.trim().isEmpty()) {
			return "";
		} else {
			return customerNo.replaceAll("^(.{4})(.{3})(.{4})$", "$3");
		}

	}

	/**
	 * 日付表示形式（YYYY/MM/DD）
	 * @param date 日付
	 */
	public static String editYmd(String date) {
		if (date == null || date.trim().isEmpty()) {
			return "";
		} else {
			return date.replaceAll("^(.{4})(.{2})(.{2})$", "$1/$2/$3");
		}
	}

	/**
	 * 日付表示形式（YYYY/MM/DD）
	 * @param date 日付
	 */
	public static String editYmd(Date date) {
		if (date == null) {
			return "";
		} else {
			return SDF1.format(date);
		}
	}

	/**
	 * 日付表示形式（YYYY/MM/DD）
	 * @param date 日付
	 */
	public static String editYmd(LocalDate date) {
		if (date == null) {
			return "";
		} else {
			return date.format(DATE_TIME_FORMAT);
		}
	}

	/**
	 * HH:MM:ss
	 * @param time 時間
	 */
	public static String editTimeFormat(String time) {
		if (time == null || time.trim().isEmpty()) {
			return "";
		} else {
			return time.replaceAll("^(.{2})(.{2})(.{2})$", "$1:$2:$3");
		}
	}

	/**
	 * 郵便番号
	 * @param postNo 郵便番号
	 */
	public static String editPostNo(String postNo) {
		if (postNo == null || postNo.trim().isEmpty()) {
			return "";
		} else {
			return postNo.replaceAll("^(.{3})(.{4})$", "$1-$2");
		}
	}

	/**
	 * 郵便番号上3桁
	 * @param postNo 郵便番号
	 */
	public static String editFirstPostNo(String postNo) {
		if (postNo == null || postNo.trim().isEmpty()) {
			return "";
		} else {
			return postNo.replaceAll("^(.{3})(.{4})$", "$1");
		}
	}

	/**
	 * 郵便番号下4桁
	 * @param postNo 郵便番号
	 */
	public static String editLastPostNo(String postNo) {
		if (postNo == null || postNo.trim().isEmpty()) {
			return "";
		} else {
			return postNo.replaceAll("^(.{3})(.{4})$", "$2");
		}
	}

	/**
	 * 電話番号ハイフン区切りの1番目
	 * @param phoneNo 電話番号
	 */
	public static String editPhoneNo1(String phoneNo) {
		if (phoneNo == null || phoneNo.trim().isEmpty()) {
			return "";
		} else {
			String[] phoneNos = phoneNo.split("-");
			return phoneNos[0];
		}
	}

	/**
	 * 電話番号ハイフン区切りの2番目
	 * @param phoneNo 電話番号
	 */
	public static String editPhoneNo2(String phoneNo) {
		if (phoneNo == null || phoneNo.trim().isEmpty()) {
			return "";
		} else {
			String[] phoneNos = phoneNo.split("-");
			return phoneNos[1];
		}
	}

	/**
	 * 電話番号ハイフン区切りの3番目
	 * @param phoneNo 電話番号
	 */
	public static String editPhoneNo3(String phoneNo) {
		if (phoneNo == null || phoneNo.trim().isEmpty()) {
			return "";
		} else {
			String[] phoneNos = phoneNo.split("-");
			return phoneNos[2];
		}
	}

	/**
	 * ガスメータ設置場所番号
	 * @param gmtSetNo ガスメータ設置場所番号
	 */
	public static String editGmtSetNo(String gmtSetNo) {
		if (gmtSetNo == null || gmtSetNo.trim().isEmpty()) {
			return "";
		} else {
			return gmtSetNo.replaceAll("^(.{4})(.{3})(.{4})$", "$1-$2-$3");
		}
	}

	/**
	 * ガスメータ設置場所番号上4桁
	 * @param gmtSetNo ガスメータ設置場所番号
	 */
	public static String editGmtSetNo1(String gmtSetNo) {
		if (gmtSetNo == null || gmtSetNo.trim().isEmpty()) {
			return "";
		} else {
			return gmtSetNo.replaceAll("^(.{4})(.{3})(.{4})$", "$1");
		}
	}

	/**
	 * ガスメータ設置場所番号中3桁
	 * @param gmtSetNo ガスメータ設置場所番号
	 */
	public static String editGmtSetNo2(String gmtSetNo) {
		if (gmtSetNo == null || gmtSetNo.trim().isEmpty()) {
			return "";
		} else {
			return gmtSetNo.replaceAll("^(.{4})(.{3})(.{4})$", "$2");
		}
	}

	/**
	 * ガスメータ設置場所番号下4桁
	 * @param gmtSetNo ガスメータ設置場所番号
	 */
	public static String editGmtSetNo3(String gmtSetNo) {
		if (gmtSetNo == null || gmtSetNo.trim().isEmpty()) {
			return "";
		} else {
			return gmtSetNo.replaceAll("^(.{4})(.{3})(.{4})$", "$3");
		}
	}

	/**
	 * 供給地点特定番号
	 * @param kkuTtnTtiNo 供給地点特定番号
	 */
	public static String editKkuTtnTtiNo(String kkuTtnTtiNo) {
		if (kkuTtnTtiNo == null || kkuTtnTtiNo.trim().isEmpty()) {
			return "";
		} else {
			return kkuTtnTtiNo.replaceAll("^(.{2})(.{4})(.{4})(.{4})(.{4})(.{4})$", "$1-$2-$3-$4-$5-$6");
		}
	}

	/**
	 * 供給地点特定番号ハイフン区切りの2番目の4桁
	 * @param kkuTtnTtiNo 供給地点特定番号
	 */
	public static String editKkuTtnTtiNo2(String kkuTtnTtiNo) {
		if (kkuTtnTtiNo == null || kkuTtnTtiNo.trim().isEmpty()) {
			return "";
		} else {
			return kkuTtnTtiNo.replaceAll("^(.{2})(.{4})(.{4})(.{4})(.{4})(.{4})$", "$2");
		}
	}

	/**
	 * 供給地点特定番号ハイフン区切りの3番目の4桁
	 * @param kkuTtnTtiNo 供給地点特定番号
	 */
	public static String editKkuTtnTtiNo3(String kkuTtnTtiNo) {
		if (kkuTtnTtiNo == null || kkuTtnTtiNo.trim().isEmpty()) {
			return "";
		} else {
			return kkuTtnTtiNo.replaceAll("^(.{2})(.{4})(.{4})(.{4})(.{4})(.{4})$", "$3");
		}
	}

	/**
	 * 供給地点特定番号ハイフン区切りの4番目の4桁
	 * @param kkuTtnTtiNo 供給地点特定番号
	 */
	public static String editKkuTtnTtiNo4(String kkuTtnTtiNo) {
		if (kkuTtnTtiNo == null || kkuTtnTtiNo.trim().isEmpty()) {
			return "";
		} else {
			return kkuTtnTtiNo.replaceAll("^(.{2})(.{4})(.{4})(.{4})(.{4})(.{4})$", "$4");
		}
	}

	/**
	 * 供給地点特定番号ハイフン区切りの5番目の4桁
	 * @param kkuTtnTtiNo 供給地点特定番号
	 */
	public static String editKkuTtnTtiNo5(String kkuTtnTtiNo) {
		if (kkuTtnTtiNo == null || kkuTtnTtiNo.trim().isEmpty()) {
			return "";
		} else {
			return kkuTtnTtiNo.replaceAll("^(.{2})(.{4})(.{4})(.{4})(.{4})(.{4})$", "$5");
		}
	}

	/**
	 * 供給地点特定番号ハイフン区切りの6番目の4桁
	 * @param kkuTtnTtiNo 供給地点特定番号
	 */
	public static String editKkuTtnTtiNo6(String kkuTtnTtiNo) {
		if (kkuTtnTtiNo == null || kkuTtnTtiNo.trim().isEmpty()) {
			return "";
		} else {
			return kkuTtnTtiNo.replaceAll("^(.{2})(.{4})(.{4})(.{4})(.{4})(.{4})$", "$6");
		}
	}

	/**
	 * コロンを用いた文字列結合
	 * @param left 文字列A
	 * @param right 文字列B
	 */
	public static String editColon(String left, String right) {
		if ((left == null || left.trim().isEmpty()) && (right == null || right.trim().isEmpty())) {
			return "";
		} else {
			return left + "：" + right;
		}
	}

	/**
	 * 氏名作成
	 * @param lastNm 姓
	 * @param firstNm 名
	 */
	public static String editName(String lastNm, String firstNm) {
		if (firstNm == null || firstNm.trim().isEmpty()) {
			return lastNm;
		} else {
			return lastNm + ' ' + firstNm;
		}
	}

	/**
	 * 全角スペースを用いた氏名作成
	 * @param lastNm 姓
	 * @param firstNm 名
	 */
	public static String editNameZenkaku(String lastNm, String firstNm) {
		if (firstNm == null || firstNm.trim().isEmpty()) {
			return lastNm;
		} else {
			return lastNm + '　' + firstNm;
		}
	}

	/**
	 * 住所作成
	 * @param tknNmkj 都県名漢字
	 * @param gyokNamkj 行政区画名漢字
	 * @param matiNamkj 町名漢字
	 * @param tymkAzaNamkj 丁目・字名漢字
	 */
	public static String editTknAddr(String tknNmkj, String gyokNamkj, String matiNamkj, String tymkAzaNamkj) {
		String address = "";
		if (tknNmkj != null && !tknNmkj.trim().isEmpty()) {
			address += tknNmkj;
		}
		if (gyokNamkj != null && !gyokNamkj.trim().isEmpty()) {
			address += gyokNamkj;
		}
		if (matiNamkj != null && !matiNamkj.trim().isEmpty()) {
			address += matiNamkj;
		}
		if (tymkAzaNamkj != null && !tymkAzaNamkj.trim().isEmpty()) {
			address += tymkAzaNamkj;
		}
		return address;
	}
	
	/**
	 * 住所作成(町名+丁目・字名+番地・号)
	 * @param matiNamkj 町名漢字
	 * @param tymkAzaNamkj 丁目・字名漢字
	 * @param bantGo 番地・号
	 */
	public static String editTgCrmAddr(String matiNamkj, String tymkAzaNamkj, String bantGo) {
		String address = "";
		if (matiNamkj != null && !matiNamkj.trim().isEmpty()) {
			address += matiNamkj;
		}
		if (tymkAzaNamkj != null && !tymkAzaNamkj.trim().isEmpty()) {
			address += tymkAzaNamkj;
		}
		if (bantGo != null && !bantGo.trim().isEmpty()) {
			address += bantGo;
		}
		return address;
	}
	
	/**
	 * 棟番－部屋番(XX-XX）
	 * @param mnbn 棟番
	 * @param hybn 部屋番
	 */
	public static String editMnHybn(String mnbn, String hybn) {
		if (mnbn == null && hybn == null) {
			return "";
		} else if (mnbn == null || mnbn.trim().isEmpty()) {
			return hybn.trim();
		} else if (hybn == null || hybn.trim().isEmpty()) {
			return mnbn.trim();
		} else {
			return mnbn.trim() + "-" + hybn.trim();
		}
	}
}
