package jp.co.gas.tokyo.accessLogBatch.utilities;

import org.springframework.util.StringUtils;

/**
 * 電話番号チェック
 */
public class PhoneNumber {

	// エラーコード
	private static final String ERROR_CD = "E0007";

	/**
	 * 電話番号チェック
	 * @param phoneNumber1 電話番号１
	 * @param phoneNumber2 電話番号２
	 * @param phoneNumber3 電話番号３
	 * @return error
	 */
	public static String checkPhoneNumber(String phoneNumber1, String phoneNumber2, String phoneNumber3) {

		if (StringUtils.isEmpty(phoneNumber1) || StringUtils.isEmpty(phoneNumber2)
				|| StringUtils.isEmpty(phoneNumber3)) {
			return "";
		}

		boolean checkResult = false;

		checkResult = zitakuFormatCheck(phoneNumber1, phoneNumber2, StringUtil.removeBracketsFromTelNo(phoneNumber3));

		if (!checkResult) {
			// 自宅電話形式に該当しない場合は、携帯電話形式に該当するかチェック
			checkResult = keitaiFormatCheck(phoneNumber1, phoneNumber2, StringUtil.removeBracketsFromTelNo(phoneNumber3));
		}

		if (!checkResult) {
			return ERROR_CD;
		}

		return "";
	}

	/**
	 * 携帯電話形式チェック<br />
	 * 以下のケースをエラーとする。
	 * <ul>
	 * <li>電話番号1が050,070,080,090以外の場合
	 * <li>電話番号2と電話番号3が4桁ではない場合
	 * </ul>
	 * @param phoneNumber1 電話番号１
	 * @param phoneNumber2 電話番号２
	 * @param phoneNumber3 電話番号３
	 * @return TRUE：可／FALSE:不可
	 */
	private static boolean keitaiFormatCheck(String phoneNumber1, String phoneNumber2, String phoneNumber3) {

		if (!("050".equals(phoneNumber1) || "070".equals(phoneNumber1) || "080".equals(phoneNumber1) || "090".equals(phoneNumber1))) {
			return false;
		}

		if (phoneNumber2.length() != 4) {
			return false;
		}

		if (phoneNumber3.length() != 4) {
			return false;
		}

		return true;
	}

	/**
	 * 自宅電話形式チェック<br />
	 * 電話番号1が050の場合、以下のケースをエラーとする。
	 * <ul>
	 * <li>電話番号2と電話番号3が4桁より大きい
	 * <li>全体の桁数が11桁に一致しない
	 * </ul>
	 * 電話番号1が050以外の場合、以下のケースをエラーとする。
	 * <ul>
	 * <li>電話番号1が070,080,090のいずれか
	 * <li>電話番号1が5桁より大きい
	 * <li>電話番号2と電話番号3が4桁より大きい
	 * <li>全体の桁数が10桁に一致しない
	 * </ul>
	 * @param phoneNumber1 電話番号１
	 * @param phoneNumber2 電話番号２
	 * @param phoneNumber3 電話番号３
	 * @return TRUE：可／FALSE:不可
	 */
	private static boolean zitakuFormatCheck(String phoneNumber1, String phoneNumber2, String phoneNumber3) {

		int phoneNum1Len = phoneNumber1.length();
		int phoneNum2Len = phoneNumber2.length();
		int phoneNum3Len = phoneNumber3.length();
		int allLength = phoneNum1Len + phoneNum2Len + phoneNum3Len;

		if ("050".equals(phoneNumber1)) {
			if (phoneNum2Len > 4) {
				return false;
			}

			if (phoneNum3Len > 4) {
				return false;
			}

			if (allLength != 11) {
				return false;
			}

			return true;

		} else {

			if ("070".equals(phoneNumber1) || "080".equals(phoneNumber1) || "090".equals(phoneNumber1)) {
				return false;
			}

			if (!phoneNumber1.startsWith("0")) {
				return false;
			}

			if (phoneNum1Len > 5) {
				return false;
			}

			if (phoneNum2Len > 4) {
				return false;
			}

			if (phoneNum3Len > 4) {
				return false;
			}

			if (allLength != 10) {
				return false;
			}

			return true;
		}
	}

	/**
	 * zitakuFormatCheckの外部参照用
	 * 
	 * @param telNo ハイフンを含む電話番号文字列
	 * @return zitakuFormatCheckメソッドを参照
	 */
	public static boolean isZitakuTelNo(String telNo) {

		String phoneNum1, phoneNum2, phoneNum3;
		phoneNum1 = telNo.split("-")[0];
		phoneNum2 = telNo.split("-")[1];
		phoneNum3 = telNo.split("-")[2];

		return zitakuFormatCheck(phoneNum1, phoneNum2, phoneNum3);

	}

	/**
	 * keitaiFormatCheckの外部参照用
	 * 
	 * @param telNo ハイフンを含む電話番号文字列
	 * @return keitaiFormatCheckメソッドを参照
	 */
	public static boolean isKeitaiTelNo(String telNo) {

		String phoneNum1, phoneNum2, phoneNum3;
		phoneNum1 = telNo.split("-")[0];
		phoneNum2 = telNo.split("-")[1];
		phoneNum3 = telNo.split("-")[2];

		return keitaiFormatCheck(phoneNum1, phoneNum2, phoneNum3);

	}

	/**
	 * 電話番号にハイフンを付与し、返却
	 *
	 * @param telNo
	 * @return telNo
	 */
	public static String getHyphenToTelNo(String telNo) {

		// nullの場合は処理を行わずそのまま返却
		if (telNo == null) {
			return telNo;
		}

		String[] telNoArray = telNo.split("-");

		// ハイフンが1つしか存在しない場合
		if (telNoArray.length == 2) {

			// ハイフン区切り位置を取得
			int targetTelNoIndex = PhoneNumber.getTargetTelNoIndex(telNoArray[0]);

			if (targetTelNoIndex != 0) {
				// ハイフンを付与
				telNo = telNoArray[0] + "-" + telNoArray[1].substring(0, targetTelNoIndex) + "-" + telNoArray[1].substring(targetTelNoIndex);
			}
		}
		return telNo;
	}

	/**
	 * ハイフンの区切り位置を取得する
	 *
	 * @param telNo
	 * @return index
	 */
	public static int getTargetTelNoIndex(String telNo) {

		// 携帯番号の場合
		if (telNo.equals("050") || telNo.equals("070") || telNo.equals("080") || telNo.equals("090")) {

			return 4;
		} else {
			// 固定電話の場合

			if (telNo.length() == 5) {

				return 1;

			} else if (telNo.length() == 4) {

				return 2;

			} else if (telNo.length() == 3) {

				return 3;

			} else if (telNo.length() == 2) {

				return 4;
			}

			// 本来有り得ないが、エラー防止のため「0」を返却
			return 0;
		}
	}

}
