package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsPlanResultDto;
import jp.co.takeda.dto.InsPlanScDto;
import jp.co.takeda.dto.InsProdPlanResultDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdPlanScDto;
import jp.co.takeda.model.ManageInsPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 施設別計画の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpmInsPlanSearchService {

	/**
	 * 施設コードから施設品目別計画の検索結果ヘッダ情報を取得する。
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード（NULL可）
	 * @return 施設品目別計画の検索結果 ヘッダ情報
	 */
	InsProdPlanResultHeaderDto searchInsPlanHeader(String insNo,String prodCode);

	/**
	 * 施設コードから施設品目別計画の検索結果ヘッダ情報を取得する。(親子紐づけ対応)
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード
	 * @param category カテゴリ
	 * @return 施設品目別計画の検索結果 ヘッダ情報
	 */
	InsProdPlanResultHeaderDto searchInsPlanHeaderOyako(String insNo,String prodCode, String category);

	/**
	 * 検索条件をもとに、施設別計画を取得する。
	 *
	 * @param scDto 施設別計画の検索用DTO
	 * @param detailErrFlg 明細がない場合に、エラーとする場合はtrue
	 * @return 施設別計画の検索結果DTO
	 * @throws LogicalException データが存在しない場合、品目の立案レベルが不正の場合、施設コードが不正の場合
	 */
	InsPlanResultDto searchInsPlan(InsPlanScDto scDto, boolean detailErrFlg) throws LogicalException;

	/**
	 *
	 * @param prodCode
	 * @param jgiNo
	 * @param insType
	 * @param insPlanList
	 * @return
	 * @throws LogicalException
	 */
	InsPlanResultDto createInsPlanResultDto(String prodCode, Integer jgiNo, InsType insType, List<ManageInsPlan> insPlanList, String oyakoKb, String oyakoKb2, String oyakoKbProdCode) throws LogicalException;

	/**
	 * 検索条件をもとに、施設別品目別計画を取得する。
	 *
	 * @param scDto 施設別品目別計画の検索用DTO
	 * @return 施設別品目別計画の検索結果DTO
	 * @throws LogicalExceptionデータが存在しない場合、品目の立案レベルが不正の場合、施設コードが不正の場合
	 */
	InsProdPlanResultDto searchInsProdPlan(InsProdPlanScDto scDto) throws LogicalException;
}
