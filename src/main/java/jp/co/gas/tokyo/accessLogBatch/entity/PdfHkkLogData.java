package jp.co.gas.tokyo.accessLogBatch.entity;

import lombok.Data;

/**
 * PDF発行ログ作成ファイル用
 */
@Data
public class PdfHkkLogData {

    private String id;

    /** 発行PDFデータ */
    private byte[] hkkPdfData;

    /** 発行PDF種別 1:見積書 2:納品書兼ご請求書 3:機器伝票兼作業結果報告書 4:領収書 */
    private String pdfType;

    /** 受付番号 */
    private String uktkNo;

    /** リモートアドレスIP */
    private String remoteIpAddress;

    /** レコード登録ユーザーID */
    private String recRegUserId;

    /** レコード登録ユーザー名 */
    private String recRegUserNm;

    /** レコード登録ユーザー会社名 */
    private String recRegCorpNm;

    /** レコード登録年月日 */
    private String recRegYmd;

    /** レコード登録時刻 */
    private String recRegJkk;

    /** お客さま情報 お客さま番号(1x) */
    private String custInfoCustNo;

    /** お客さま情報 お客さま姓 */
    private String custInfoNameSei;

    /** お客さま情報 お客さま名 */
    private String custInfoNameMei;

    /** お客さま情報 お客さま姓カナ */
    private String custInfoNameSeiKn;

    /** お客さま情報 お客さま名カナ */
    private String custInfoNameMeiKn;

}