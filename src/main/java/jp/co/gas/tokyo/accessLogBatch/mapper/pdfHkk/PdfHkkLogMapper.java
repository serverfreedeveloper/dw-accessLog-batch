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

	/**
	 * IDをキーに連携フラグを更新する
	 * @param id
	 */
	public void updateLinkedFlg(String id, String flg);
}