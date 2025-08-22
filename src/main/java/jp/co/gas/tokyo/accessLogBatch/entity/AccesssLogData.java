package jp.co.gas.tokyo.accessLogBatch.entity;

import lombok.Data;

/**
 * ファイル情報保持
 */
@Data
public class AccesssLogData {

    /** ファイルサイズ */
    private long fileByte;

    private String fileName;
}