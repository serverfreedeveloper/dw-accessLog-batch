package jp.co.gas.tokyo.accessLogBatch.mapper.pdfHkk;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.gas.tokyo.accessLogBatch.entity.PdfHkkLogData;

/**
 * PdfHkkLogMapper インターフェース
 */
@Mapper
public interface PdfHkkLogMapper {

	/**
	 * PDF発行ログのリストを取得する
	 * （未連携のみ）
	 */
	public List<PdfHkkLogData> selectPdfHkkList();


	void updateLinkedFlg(String id);
}