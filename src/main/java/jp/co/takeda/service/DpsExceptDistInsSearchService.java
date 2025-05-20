package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ExceptDistInsPlannedProdResultDto;
import jp.co.takeda.dto.ExceptDistInsResultListDto;
import jp.co.takeda.dto.ExceptDistInsScDto;
import jp.co.takeda.dto.ExceptDistInsUpdateInitDto;
import jp.co.takeda.model.DpsCCdMst;

/**
 * 配分除外施設の検索に関するサービスインタフェース
 *
 * @author siwamoto
 */
public interface DpsExceptDistInsSearchService {
	/**
	 * 配分除外施設の検索結果DTOを取得する。
	 *
	 * @param scDto 配分除外施設の検索条件用DTO
	 * @return 配分除外施設の検索結果用DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	ExceptDistInsResultListDto searchExceptDistIns(ExceptDistInsScDto scDto) throws LogicalException;

	/**
	 * 計画対象品目の（MMP・仕入一般）の検索結果DTOを取得する。
	 *
	 * @return 計画対象品目DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	List<ExceptDistInsPlannedProdResultDto> searchExceptDistInsProdExe(String sosCd, String[] category, List<DpsCCdMst> cdMstCategoryList) throws LogicalException;

	/**
	 * 配分除外施設の編集用DTOを取得する。
	 *
	 *
	 * @param insNo 施設コード
	 * @param dispJgiName 表示用担当者名
	 * @param sosCd 組織コード
	 * @param category 組織が属するカテゴリー（複数あり）
	 * @param cdMstCategoryList 汎用マスタに登録されているカテゴリー
	 * @return 配分除外施設の編集用DTO
	 * @throws LogicalException
	 */
	ExceptDistInsUpdateInitDto searchExceptDistInsCategoryMulti(String insNo, String dispJgiName, String sosCd, String[] category, List<DpsCCdMst> cdMstCategoryList, String insType) throws LogicalException;

	/**
	 * 施設コード、品目固定コードで配分除外施設かどうかを取得（施設指定の場合でも判定）
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @return true：配分除外施設、false：配分除外施設ではない
	 */
	boolean isExceptDistIns(String insNo, String prodCode);
}
