package jp.co.gas.tokyo.accessLogBatch.utilities;

import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    /**
     * CSV用に値を変換
     * @param values 変換対象文字列リスト
     */
    public static List<String> escapeForCsv(List<String> values) {
        List<String> result = new ArrayList<>();
        for (String value : values) {
            result.add(escapeForCsv(value));
        }
        return result;
    }

    /**
     * CSV用に値を変換
     * - カンマ、改行、ダブルクォーテーションを含む場合は " で囲む
     * - 文字列内の " は "" にエスケープ
     * @param value 変換対象文字列
     */
    public static String escapeForCsv(String value) {
        if (value == null) {
            return "";
        }

        boolean needQuote = value.contains(",") || value.contains("\n") || value.contains("\"");
        if (needQuote) {
            // " を "" に置換
            String escaped = value.replace("\"", "\"\"");
            return "\"" + escaped + "\"";
        } else {
            return value;
        }
    }
}