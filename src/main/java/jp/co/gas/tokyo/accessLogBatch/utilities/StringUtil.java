package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 文字列ユーティリティクラス
 */
public class StringUtil {

	private static int Token_Length = 16;//16*2=32バイト
ｚｚ
	/**
	 * 引数の文字列のNulかブランクかを判定する
	 * @param str チェック対象文字列
	 * @return
	 * true : Nullまたはブランクの場合
	 * false: Nullまたはブランクではない場合
	 */
	public static boolean isNullEmpty(String str)
	{
		return str == null || str.isEmpty();
	}

	public static boolean isEmpty(String value) {
		boolean allSpaceFlg = false;

		if (value == null || value.length() == 0) {
			allSpaceFlg = true;
		}

		String afterTrim = trimWhitespace(value);

		if (afterTrim.length() == 0) {
			allSpaceFlg = true;
		}

		return allSpaceFlg;
	}

	/**
	 *
	 * @param value
	 * @return true nullか要素0 false リストあり
	 */
	public static boolean isListEmpty(List<String> value) {
		boolean allSpaceFlg = false;

		if (value == null || value.size() == 0) {
			allSpaceFlg = true;
		}
		return allSpaceFlg;
	}

	/**
	 * ブランクをNullに変換する
	 * @param str 対象文字列
	 * @return ブランクもしくは、Null
	 */
	public static String brankToNull(String str) {

		if("".equals(str)) {
			return null;
		}

		return str;
	}

	
	/**
	 * Nullをブランクに変換する
	 * @param str 対象文字列
	 * @return ブランクもしくは、Null
	 */
	public static String nullToBrank(String str) {

		if(str == null) {
			return "";
		}

		return str;
	}

	public static String trimWhitespace(String str) {

		String blank = "";
		if (str == null || str.length() == 0) {
			return blank;
		}

		int st = 0;
		int len = str.length();
		char[] val = str.toCharArray();
		while ((st < len) && ((val[st] <= '\u0020') || (val[st] == '\u00A0') || (val[st] == '\u3000'))) {
			st++;
		}
		while ((st < len) && ((val[len - 1] <= '\u0020') || (val[len - 1] == '\u00A0') || (val[len - 1] == '\u3000'))) {
			len--;
		}
		return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
	}

	// 名の抽出
	public static String getNameMei(String name) {
		// お客さま名前の漢字を姓と名に分ける
		// 空文字までの文字数を取得
		int index = name.indexOf("　");
		// 最初から空文字までの文字の姓を取得
		String namuSei = name.substring(0,index);
		// 姓の文字数を取得
		int index2 = namuSei.length();
		int index3 = name.length();
		// 全体から（姓の文字数＋空文字）～名の文字数分の文字を取得
		String nameMei = name.substring(index2 + 1, index3);
		return nameMei;
	}

	// 姓の取得
	public static String getNameSei(String name) {
		// お客さま名前の漢字を姓と名に分ける
		// 空文字までの文字数を取得
		int index = name.indexOf("　");
		// 最初から空文字までの文字の姓を取得
		String namuSei = name.substring(0,index);
		return namuSei;
	}

	/**
	 * 引数の文字列を0埋めする
	 * @param value 0埋め対象文字列
	 * @return formattedValue 0埋め後文字列
	 */
	public static String getFormatAddZero(String value) {

		// 0埋めにて2文字に置換
		String formattedValue = String.format("%2s", value).replace(" ", "0");

		return formattedValue;

	}

	/**
	* 電話番号からカッコ以降の文字を除去
	* @param telNo 対象の電話番号
	* @return カッコを除去後のtelNo
	*/
	public static String removeBracketsFromTelNo(String telNo) {

		// "（"が含まれている場合
		if (telNo.contains("（")) {

			// "（"以降の文字を除去
			int brackets = telNo.indexOf("（");
			telNo = telNo.substring(0, brackets);

		} else if (telNo.contains("(")) {
			// "("が含まれている場合

			// "("以降の文字を除去
			int brackets = telNo.indexOf("(");
			telNo = telNo.substring(0, brackets);

		}

		return telNo;
	}

	/**
	* 全角カナかチェック
	* @param str 対象の文字列
	* @return true 半角カナ flase 半角カナ以外
	*/
	public static boolean isHankakuKn(String str) {

		 return Pattern.matches("^[ァ-ヶー]*$", str);
	}

	/**
     * 文字列に含まれる全角数字を半角数字に変換します。
     * 
     * @param str 変換前文字列(null不可)
     * @return 変換後文字列
     */
    public static String zenkakuNumberToHankakuNumber(String str) {
        if (str == null){
            throw new IllegalArgumentException();
        }
        StringBuffer sb = new StringBuffer(str);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ('０' <= c && c <= '９') {
                sb.setCharAt(i, (char) (c - '０' + '0'));
            }
        }
        return sb.toString();
    }

    /**
     * 文字列に含まれる半角数字を全角数字に変換します。
     * 
     * @param str 変換前文字列(null不可)
     * @return 変換後文字列
     */
    public static String hankakuNumberToZenkakuNumber(String str) {
        if (str == null){
            throw new IllegalArgumentException();
        }
        StringBuffer sb = new StringBuffer(str);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ('0' <= c && c <= '9') {
                sb.setCharAt(i, (char) (c - '0' + '０'));
            }
        }
        return sb.toString();
    }

		public static String nvl(String value) {

			if (isNullEmpty(value)) {
				return "";
			}
			return value;

		}

	/**
     * 文字列を指定された文字位置でカット
     * 
     * @param str
	 * @param start
	 * @param end
     * @returns
     */
    public static String cutString(String str,int start,int end) {
		if (StringUtil.isEmpty(str)){
			return null;
		 }

        if (str.length() > end){
           return str.substring(start, end);
        }else{
			return str;
		}
    }
}
