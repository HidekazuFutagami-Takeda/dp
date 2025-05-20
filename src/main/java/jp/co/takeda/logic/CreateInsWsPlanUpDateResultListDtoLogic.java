package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultMonNnuDto;
import jp.co.takeda.dto.InsWsSummaryDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.InsPlan;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.view.InsWsPlanForRef;
import jp.co.takeda.model.view.MonNnuSummary;

/**
 * 施設特約店別計画編集画面の検索結果用DTO（画面）を生成するロジッククラス
 *
 * @author khashimoto
 */
public class CreateInsWsPlanUpDateResultListDtoLogic {

	/**
	 * 品目情報
	 */
	private final PlannedProd plannedProd;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * UH 担当者別計画
	 */
	private final Long uhMrPlanAmount;

	/**
	 * UH 施設特約店別計画積上
	 */
	private final Long uhInsWsPlanAmount;

	/**
	 * P 担当者別計画
	 */
	private final Long pMrPlanAmount;

	/**
	 * P 施設特約店別計画積上
	 */
	private final Long pInsWsPlanAmount;

	/**
	 * 「MR確定」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusMrCompleted;

	/**
	 * 参照品目のリスト
	 */
	private final List<CodeAndValue> refProdList;

	/**
	 * 施設特約店別計画のリスト
	 */
	private final List<InsWsPlanForRef> insWsList;

	/**
	 * 実績参照品目コード1
	 */
	private String resultRefProdCode1;

	/**
	 * 実績参照品目コード2
	 */
	private String resultRefProdCode2;

	/**
	 * 実績参照品目コード3
	 */
	private String resultRefProdCode3;

	/**
	 * 試算/配分パラメータ
	 */
	private BaseParam baseParam;

	/**
	 * コンストラクタ
	 *
	 * @param prod 計画対象品目
	 * @param jgiMst 従業員情報
	 * @param baseParam 試算/配分パラメータ
	 * @param insWsPlanStatus 施設特約店別計画立案ステータス
	 * @param mrPlan 担当者別計画
	 * @param insWsList 施設特約店別計画
	 * @param summaryDto 施設特約店別計画サマリー
	 * @param resultRefProdCode1 実績参照品目コード1
	 * @param resultRefProdCode2 実績参照品目コード2
	 * @param resultRefProdCode3 実績参照品目コード3
	 */
	public CreateInsWsPlanUpDateResultListDtoLogic(PlannedProd prod, JgiMst jgiMst, BaseParam baseParam, InsWsPlanStatus insWsPlanStatus, MrPlan mrPlan,
		List<InsWsPlanForRef> insWsList, InsWsPlanProdSummaryDto summaryDto, String resultRefProdCode1, String resultRefProdCode2, String resultRefProdCode3) {
		this.plannedProd = prod;
		this.jgiNo = jgiMst.getJgiNo();
		this.jgiName = jgiMst.getJgiName();
		this.uhMrPlanAmount = mrPlan.getPlannedValueUhY();
		this.pMrPlanAmount = mrPlan.getPlannedValuePY();
		this.uhInsWsPlanAmount = summaryDto.getPlannedValueUhY();
		this.pInsWsPlanAmount = summaryDto.getPlannedValuePY();
		if (insWsPlanStatus == null) {
			isStatusMrCompleted = false;
		} else if (insWsPlanStatus.getStatusForInsWsPlan().equals(StatusForInsWsPlan.PLAN_COMPLETED)) {
			isStatusMrCompleted = true;
		} else {
			isStatusMrCompleted = false;
		}
		List<CodeAndValue> _refProdList = new ArrayList<CodeAndValue>();
		if (baseParam.getResultRefProdCode1() != null) {
			_refProdList.add(new CodeAndValue(baseParam.getResultRefProdCode1(), baseParam.getResultRefProdName1()));
		}
		if (baseParam.getResultRefProdCode2() != null) {
			_refProdList.add(new CodeAndValue(baseParam.getResultRefProdCode2(), baseParam.getResultRefProdName2()));
		}
		if (baseParam.getResultRefProdCode3() != null) {
			_refProdList.add(new CodeAndValue(baseParam.getResultRefProdCode3(), baseParam.getResultRefProdName3()));
		}
		this.refProdList = _refProdList;
		this.insWsList = insWsList;
		this.resultRefProdCode1 = resultRefProdCode1;
		this.resultRefProdCode2 = resultRefProdCode2;
		this.resultRefProdCode3 = resultRefProdCode3;
		this.baseParam = baseParam;
	}

	/**
	 * DTO生成処理を実行する。
	 *
	 * @return 施設特約店別計画編集画面の検索結果用DTO
	 */
	public InsWsPlanUpDateResultListDto create() {

		List<InsWsPlanUpDateResultDto> resultList = new ArrayList<InsWsPlanUpDateResultDto>();

		// 施設合計
		InsWsSummaryDto insSum = new InsWsSummaryDto();
		// 全合計
		InsWsSummaryDto allSum = new InsWsSummaryDto();
		// 最新レコード
		InsWsPlanForRef lastInsWsPlan = new InsWsPlanForRef();
		for (int i = 0; i < insWsList.size(); i++) {
			final InsWsPlanForRef insWsPlan = insWsList.get(i);
			// 最新レコード取得
			if (insWsPlan.getUpDate() != null) {
				if (lastInsWsPlan.getUpDate() == null || insWsPlan.getUpDate().after(lastInsWsPlan.getUpDate())) {
					lastInsWsPlan = insWsPlan;
				}
			}
			// 行生成
			resultList.add(create(insWsPlan));
			// 施設合計加算
			insSum = new InsWsSummaryDto(insSum, insWsPlan);
			if (i == insWsList.size() - 1 || !insWsPlan.getInsNo().equals(insWsList.get(i + 1).getInsNo())) {
				// 最終行、または次行の施設コードが異なる場合、施設合計行を生成する。
				resultList.add(create(insWsPlan, insSum, false));
				// 全合計に加算
				allSum = new InsWsSummaryDto(allSum, insSum);
				// 施設合計初期化
				insSum = new InsWsSummaryDto();
			}
		}

		// 先頭行にサマリー行を追加
		resultList.add(0, create(allSum));

		return new InsWsPlanUpDateResultListDto(plannedProd, jgiNo, jgiName, lastInsWsPlan.getUpJgiName(), lastInsWsPlan.getUpDate(), uhMrPlanAmount,
			uhInsWsPlanAmount, pMrPlanAmount, pInsWsPlanAmount, resultList, isStatusMrCompleted, refProdList);
	}

	/**
	 * DTO生成処理を実行する。（重点品）
	 *
	 * @return 施設特約店別計画編集画面の検索結果用DTO
	 */
	public InsWsPlanUpDateResultListDto createPriorityProd(boolean existUhInsChosei, boolean existPInsChosei, Map<String, InsPlan> insPlanMap) {

		List<InsWsPlanUpDateResultDto> resultList = new ArrayList<InsWsPlanUpDateResultDto>();

		// 施設合計
		InsWsSummaryDto insSum = new InsWsSummaryDto();
		// 合算施設合計
		InsWsSummaryDto oyakoInsSum = new InsWsSummaryDto();
		// 全合計
		InsWsSummaryDto allSum = new InsWsSummaryDto();

		// 最新レコード
		InsWsPlanForRef lastInsWsPlan = new InsWsPlanForRef();
		for (int i = 0; i < insWsList.size(); i++) {
			final InsWsPlanForRef insWsPlan = insWsList.get(i);
			// 最新レコード取得
			if (insWsPlan.getUpDate() != null) {
				if (lastInsWsPlan.getUpDate() == null || insWsPlan.getUpDate().after(lastInsWsPlan.getUpDate())) {
					lastInsWsPlan = insWsPlan;
				}
			}
			// 行生成
			resultList.add(create(insWsPlan));

			// 施設合計加算
			insSum = new InsWsSummaryDto(insSum, insWsPlan);

			// 最終行、または次行の施設コードが異なる場合、施設合計行を生成する。
			if (i == insWsList.size() - 1 || !insWsPlan.getInsNo().equals(insWsList.get(i + 1).getInsNo())) {
				resultList.add(create(insWsPlan, insSum, false));
				// 親子合算合計に加算
				oyakoInsSum = new InsWsSummaryDto(oyakoInsSum, insSum);
				// 施設合計初期化
				insSum = new InsWsSummaryDto();
			}

			// 最終行、または次行の施設内部コードが異なる場合、合算施設合計行を生成する。
			if (i == insWsList.size() - 1 || !insWsPlan.getRelnInsNo().equals(insWsList.get(i + 1).getRelnInsNo())) {
				resultList.add(create(insWsPlan, oyakoInsSum, true));
				// 全合計に加算
				allSum = new InsWsSummaryDto(allSum, oyakoInsSum);
				// 施設合計初期化
				oyakoInsSum = new InsWsSummaryDto();
			}
		}

		// 先頭行にサマリー行を追加
		resultList.add(0, create(allSum));

		return new InsWsPlanUpDateResultListDto(plannedProd, jgiNo, jgiName, lastInsWsPlan.getUpJgiName(), lastInsWsPlan.getUpDate(), uhMrPlanAmount,
			uhInsWsPlanAmount, existUhInsChosei, pMrPlanAmount, pInsWsPlanAmount, existPInsChosei ,resultList, isStatusMrCompleted, refProdList,insPlanMap);
	}

	/**
	 * 全合計行の検索結果用DTOを生成する。
	 *
	 * @param summaryDto 施設特約店別計画のサマリー行を表すDTO
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanUpDateResultDto create(InsWsSummaryDto summaryDto) {
		List<InsWsPlanUpDateResultMonNnuDto> resultList = createMonNnu(summaryDto);
		return new InsWsPlanUpDateResultDto(summaryDto.getDistSum(), summaryDto.getModifySum(), summaryDto.getPlanSum(), resultList);
	}

	/**
	 * 施設合計行の検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param summaryDto 施設特約店別計画のサマリー行を表すDTO
	 * @param isOyako 親子合算合計行か、施設合算合計行か
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanUpDateResultDto create(InsWsPlanForRef insWsPlan, InsWsSummaryDto summaryDto , boolean isOyako) {
		List<InsWsPlanUpDateResultMonNnuDto> resultList = createMonNnu(summaryDto);
		return new InsWsPlanUpDateResultDto(insWsPlan, summaryDto.getDistSum(), summaryDto.getModifySum(), summaryDto.getPlanSum(), resultList, isOyako);
	}

	/**
	 * 検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanUpDateResultDto create(InsWsPlanForRef insWsPlan) {
		return new InsWsPlanUpDateResultDto(insWsPlan, createMonNnu(insWsPlan));
	}

	/**
	 * 過去実績参照部を生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @return 過去実績参照部
	 */
	protected List<InsWsPlanUpDateResultMonNnuDto> createMonNnu(InsWsPlanForRef insWsPlan) {
		return createMonNnu(insWsPlan.getMonNnuSummary(), insWsPlan.getMonNnuSummary1(), insWsPlan.getMonNnuSummary2(), insWsPlan.getMonNnuSummary3());
	}

	/**
	 * 過去実績参照部を生成する。
	 *
	 *
	 * @param summaryDto 施設特約店別計画のサマリー行を表すDTO
	 * @return 過去実績参照部
	 */
	protected List<InsWsPlanUpDateResultMonNnuDto> createMonNnu(InsWsSummaryDto insWsSum) {
		return createMonNnu(insWsSum.getMonNnuSummary(), insWsSum.getMonNnuSummary1(), insWsSum.getMonNnuSummary2(), insWsSum.getMonNnuSummary3());
	}

	/**
	 * 過去実績参照部を生成する。
	 *
	 * @param summary 立案品目の納入実績
	 * @param summary1 参照品目1の納入実績
	 * @param summary2 参照品目2の納入実績
	 * @param summary3 参照品目3の納入実績
	 * @return 過去実績参照部
	 */
	protected List<InsWsPlanUpDateResultMonNnuDto> createMonNnu(MonNnuSummary summary, MonNnuSummary summary1, MonNnuSummary summary2, MonNnuSummary summary3) {

		String prodCode = plannedProd.getProdCode();
		String prodName = plannedProd.getProdName();
		String prodType = plannedProd.getProdType();

		List<InsWsPlanUpDateResultMonNnuDto> resultList = new ArrayList<InsWsPlanUpDateResultMonNnuDto>();
		// 立案品目
		resultList.add(new InsWsPlanUpDateResultMonNnuDto(0, prodCode, prodName, prodType, summary));

		// 参照品目１
		if (resultRefProdCode1 != null) {
			resultList.add(new InsWsPlanUpDateResultMonNnuDto(1, resultRefProdCode1, baseParam.getResultRefProdName1(), baseParam.getResultRefProdType1(), summary1));
		}

		// 参照品目２
		if (resultRefProdCode2 != null) {
			resultList.add(new InsWsPlanUpDateResultMonNnuDto(2, resultRefProdCode2, baseParam.getResultRefProdName2(), baseParam.getResultRefProdType2(), summary2));
		}

		// 参照品目３
		if (resultRefProdCode3 != null) {
			resultList.add(new InsWsPlanUpDateResultMonNnuDto(3, resultRefProdCode3, baseParam.getResultRefProdName3(), baseParam.getResultRefProdType3(), summary3));
		}

		return resultList;
	}

	/**
	 * 納入実績の加算を行う。
	 *
	 * @param summary1 加算対象1
	 * @param summary2 加算対象2
	 * @return 納入実績
	 */
	protected MonNnuSummary add(MonNnuSummary summary1, MonNnuSummary summary2) {
		return new AddMonNnuSummaryLogic(summary1, summary2).execute();
	}
}
