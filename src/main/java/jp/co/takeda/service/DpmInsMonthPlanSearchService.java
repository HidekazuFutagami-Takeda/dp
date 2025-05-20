package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.InsMonthPlanForVacHeaderDto;
import jp.co.takeda.dto.InsMonthPlanForVacResultDto;
import jp.co.takeda.dto.InsMonthPlanResultDto;
import jp.co.takeda.dto.InsMonthPlanScDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsProdMonthPlanResultDto;
import jp.co.takeda.dto.InsProdMonthPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdMonthPlanScDto;
import jp.co.takeda.model.ManageInsMonthPlan;
import jp.co.takeda.model.div.InsType;

/**
 * 施設別計画の検索に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpmInsMonthPlanSearchService {

	/**
	 * 施設コードから施設品目別計画の検索結果ヘッダ情報を取得する。
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード（NULL可）
	 * @return 施設品目別計画の検索結果 ヘッダ情報
	 */
	InsProdMonthPlanResultHeaderDto searchInsMonthPlanHeader(String insNo,String prodCode);

	/**
	 * 施設コードから施設品目別計画の検索結果ヘッダ情報を取得する。NSG親子紐づけ対応
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード（NULL可）
	 * @param category カテゴリ
	 * @return 施設品目別計画の検索結果 ヘッダ情報
	 */
	InsProdMonthPlanResultHeaderDto searchInsMonthPlanHeaderOyako(String insNo,String prodCode, String category);

	/**
	 * 検索条件をもとに、施設別計画(医薬)を取得する。
	 *
	 * @param scDto 施設別計画の検索用DTO
	 * @param detailErrFlg 明細がない場合に、エラーとする場合はtrue
	 * @return 施設別計画の検索結果DTO
	 * @throws LogicalException データが存在しない場合、品目の立案レベルが不正の場合、施設コードが不正の場合
	 */
	InsMonthPlanResultDto searchInsMonthPlan(InsMonthPlanScDto scDto, boolean detailErrFlg) throws LogicalException;


	/**
	 * 検索条件をもとに、施設別計画(ワクチン)(明細)を取得する。
	 *
	 * @param scDto 施設別計画の検索用DTO
	 * @return 施設別計画の検索結果DTO
	 * @throws LogicalException データが存在しない場合、施設コードが不正の場合
	 */
	InsMonthPlanForVacResultDto searchInsMonthPlanForVac(InsPlanForVacScDto scDto) throws LogicalException;

//	/**
//	 * 検索条件と施設別計画(ワクチン)(明細)をもとに、施設別計画(ワクチン)(集計)を取得する
//	 *
//	 * @param scDto 施設別計画の検索用DTO
//	 * @param detailResult 施設別計画の検索結果DTO
//	 * @return 施設別計画(明細)DTO
//	 */
//	InsMonthPlanForVacResultDetailTotalDto searchInsMonthPlanTotalForVac(InsPlanForVacScDto scDto, InsMonthPlanForVacResultDto detailResult);


	/**
	 * 施設コードから施設品目別計画の検索結果ヘッダ情報を取得する。
	 *
	 * @param insNo 施設コード
	 * @return 施設品目別計画の検索結果 ヘッダ情報
	 */
	InsMonthPlanForVacHeaderDto searchInsMonthPlanHeaderForVac(String insNo);

	/**
	 *
	 * @param prodCode
	 * @param jgiNo
	 * @param insType
	 * @param insPlanList
	 * @param oyakoKb
	 * @param oyakoKb2
	 * @param oyakoKbProdCode
	 * @return
	 * @throws LogicalException
	 */
	InsMonthPlanResultDto createInsMonthPlanResultDto(String prodCode, Integer jgiNo, InsType insType, List<ManageInsMonthPlan> insPlanList, String oyakoKb,String oyakoKb2, String oyakoKbProdCode) throws LogicalException;

	/**
	 * 検索条件をもとに、施設別品目別計画を取得する。
	 *
	 * @param scDto 施設別品目別計画の検索用DTO
	 * @param jrnsCtgList JRNSカテゴリリスト
	 * @return 施設別品目別計画の検索結果DTO
	 * @throws LogicalExceptionデータが存在しない場合、品目の立案レベルが不正の場合、施設コードが不正の場合
	 */
	InsProdMonthPlanResultDto searchInsProdMonthPlan(InsProdMonthPlanScDto scDto, List<String> jrnsCtgList) throws LogicalException;
}
