package jp.co.gas.tokyo.accessLogBatch.utilities;

public class StringUtils {

	public static String nvl(String value) {

		if (isNullEmpty(value)) {
			return "";
		}
		return value;

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

	/**
	 * CISから取得したお名前の不要なスペースを除去
	 * @param name お名前
	 * @return トリム済みお名前
	 */
	public static String trimCisName(String name) {
		String trimedName = "";

		if (StringUtils.isNullEmpty(name)) {
			return trimedName;
		}

		String trimedCustNameKana = trimWhitespace(name);
		int substringIndex = trimedCustNameKana.indexOf("　");
		if (substringIndex >= 0) {
			// ｾｲ・メイの場合
			String lstNameKana = trimedCustNameKana.substring(0, substringIndex);
			trimedName = trimWhitespace(lstNameKana);

			String fstNameKana = trimedCustNameKana.substring(substringIndex + 1);
			fstNameKana = trimWhitespace(fstNameKana);

			if (!StringUtils.isNullEmpty(fstNameKana)) {
				trimedName = trimedName + "　" + fstNameKana;
			}
		} else {
			// ｾｲのみ場合
			trimedName = trimedCustNameKana;
		}

		return trimedName;
	}

	/**
	 * 引数が Null からブランクを判定する
	 * @param str チェック対象文字列
	 * @return Null かブランクの場合は true を返す
	 */
	public static boolean isNullEmpty(String str) {
		return str == null || str.isEmpty();
	}

	/**
	 * 名前をダブルクォートで囲む。カンマが入る可能性があるためそれの対策
	 *
	 * @param nameStr 処理対象名前文字列
	 */
	public static String wrapName(String nameStr) {

		// null回避
		nameStr = StringUtils.nvl(nameStr);

		// 名前文字列が空欄でなければダブルクォートで囲む
		if (nameStr.equals("")) {
			return nameStr;
		} else {
			// ダブルクォートの2重化も実施しておく
			return '"' + nameStr.replace("\"", "\"\"") + '"';
		}

	}
}
