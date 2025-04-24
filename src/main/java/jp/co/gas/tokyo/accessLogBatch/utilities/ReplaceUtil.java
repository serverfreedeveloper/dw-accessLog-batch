package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * 置換ユーティリティクラス
 */
public class ReplaceUtil {

	/**
	 * []を置換する
	 * @param list リスト
	 * @return  replacedList []置換後のリスト
	 */
	public static List<String> getReplaceBrackets(List<String> list) {

		List<String> replacedList = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {

			// 先頭の場合
			if (i == 0) {

				replacedList.add(list.get(i).replace("[", "").replace("]", ""));

			} else if (i == list.size() - 1) {
				// 最後尾の場合

				replacedList.add(list.get(i).replace("]", ""));

			} else {
				// それ以外の場合

				replacedList.add(list.get(i));

			}

		}

		return replacedList;

	}

}
