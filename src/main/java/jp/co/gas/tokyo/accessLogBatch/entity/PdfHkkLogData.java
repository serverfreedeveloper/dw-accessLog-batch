package jp.co.gas.tokyo.accessLogBatch.entity;

import lombok.Data;

/**
 * PDF発行ログ作成ファイル用
 */
@Data
public class PdfHkkLogData {

    /** 見積番号 */
    private String mtmrNo;

    /** 見積件名 */
    private String mtmrNm;

    /** 受付番号 */
    private String uktkNo;

    /** 発行PDF種別 */
    private String pdfType;

    /** レコード登録ユーザーID */
    private String recRegUserId;

    /** レコード登録年月日 */
    private String recRegYmd;

    /** レコード登録時刻 */
    private String recRegJkk;

}