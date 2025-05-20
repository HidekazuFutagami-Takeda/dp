package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanProdSummaryDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultMonNnuDto;
import jp.co.takeda.dto.InsWsSummaryDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.view.InsWsPlanForVacForRef;
import jp.co.takeda.model.view.MonNnuSummary;

/**
 * ワクチン施設特約店別計画編集画面の検索結果用DTOを生成するロジッククラス
 *
 * @author khashimoto
 */
public class CreateInsWsPlanForVacUpDateResultListDtoLogic {

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

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
	private final List<InsWsPlanForVacForRef> insWsList;

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
	 *
	 * コンストラクタ
	 *
	 * @param prod 計画対象品目
	 * @param jgiMst 従業員情報
	 * @param baseParam 試算/配分パラメータ
	 * @param insWsPlanStatusForVac ワクチン用施設特約店別計画立案ステータス
	 * @param insWsList ワクチン用施設特約店別計画
	 * @param resultRefProdCode1 実績参照品目コード1
	 * @param resultRefProdCode2 実績参照品目コード2
	 * @param resultRefProdCode3 実績参照品目コード3
	 * @param mrPlan 担当者別計画
	 * @param summaryDto 施設特約店別計画品目単位のサマリー
	 */
	public CreateInsWsPlanForVacUpDateResultListDtoLogic(PlannedProd prod, JgiMst jgiMst, BaseParam baseParam, InsWsPlanStatusForVac insWsPlanStatusForVac,
			List<InsWsPlanForVacForRef> insWsList, String resultRefProdCode1, String resultRefProdCode2, String resultRefProdCode3
			, MrPlan mrPlan, InsWsPlanProdSummaryDto summaryDto) {
	    this.prodCode = prod.getProdCode();
		this.prodName = prod.getProdName();
		this.jgiNo = jgiMst.getJgiNo();
		this.jgiName = jgiMst.getJgiName();
		if (insWsPlanStatusForVac == null) {
			isStatusMrCompleted = false;
		} else if (insWsPlanStatusForVac.getStatusForInsWsPlan().equals(StatusForInsWsPlan.PLAN_COMPLETED)) {
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
		this.uhMrPlanAmount = mrPlan.getPlannedValueUhY();
		this.pMrPlanAmount = mrPlan.getPlannedValuePY();
		this.uhInsWsPlanAmount = summaryDto.getPlannedValueUhY();
		this.pInsWsPlanAmount = summaryDto.getPlannedValuePY();

	}

	/**
	 * DTO生成処理を実行する。
	 *
	 * @return 施設特約店別計画編集画面の検索結果用DTO
	 */
	public InsWsPlanForVacUpDateResultListDto create() {

		List<InsWsPlanForVacUpDateResultDto> resultList = new ArrayList<InsWsPlanForVacUpDateResultDto>();

		// 施設合計
		InsWsSummaryDto insSum = new InsWsSummaryDto();
		// 市区郡重点先合計
		InsWsSummaryDto jtnSum = new InsWsSummaryDto();
		// 市区郡一般先合計
		InsWsSummaryDto ippanSum = new InsWsSummaryDto();
		// 市区郡合計
		InsWsSummaryDto sikuSum = new InsWsSummaryDto();
		// 重点先合計
		InsWsSummaryDto jtnAllSum = new InsWsSummaryDto();
		// 一般先合計
		InsWsSummaryDto ippanAllSum = new InsWsSummaryDto();
		// 全合計
		InsWsSummaryDto allSum = new InsWsSummaryDto();
		// 最新レコード
		InsWsPlanForVacForRef lastInsWsPlan = new InsWsPlanForVacForRef();
		// 一般先最新レコード
		InsWsPlanForVacForRef ippanInsWsPlan = new InsWsPlanForVacForRef();

		// 重点先（特定施設行）
		List<InsWsPlanForVacUpDateResultDto> jtnSpList = new ArrayList<InsWsPlanForVacUpDateResultDto>();
		// 重点先（特定施設行以外）
		List<InsWsPlanForVacUpDateResultDto> jtnList = new ArrayList<InsWsPlanForVacUpDateResultDto>();

		for (int i = 0; i < insWsList.size(); i++) {
			final InsWsPlanForVacForRef insWsPlan = insWsList.get(i);
			// 重点先の場合
			if (ActivityType.JTN.equals(insWsPlan.getActivityType())) {
				// 最新レコード取得
				if (lastInsWsPlan.getUpDate() == null || (insWsPlan.getUpDate() != null && insWsPlan.getUpDate().after(lastInsWsPlan.getUpDate()))) {
					lastInsWsPlan = insWsPlan;
				}
				// 行生成
				Boolean spInsFlg = insWsPlan.getSpecialInsPlanFlg();
				if (spInsFlg != null && spInsFlg) {
					jtnSpList.add(create(insWsPlan));
				} else {
					jtnList.add(create(insWsPlan));
				}
				// 施設合計に加算
				insSum = new InsWsSummaryDto(insSum, insWsPlan);
				// 市区郡重点先合計に加算
				jtnSum = new InsWsSummaryDto(jtnSum, insWsPlan);
				// 最終行、または次行の施設コードが異なる場合、施設合計行を生成する。
				if (i == insWsList.size() - 1 || !insWsPlan.getInsNo().equals(insWsList.get(i + 1).getInsNo())) {
					if (spInsFlg != null && spInsFlg) {
						jtnSpList.add(createInsSum(insWsPlan, insSum));
					} else {
						jtnList.add(createInsSum(insWsPlan, insSum));
					}
					// 施設合計行の初期化
					insSum = new InsWsSummaryDto();
				}
			}
			// 一般先の場合
			else {
				// 一般先最新レコード取得
				if (ippanInsWsPlan.getUpDate() == null || (insWsPlan.getUpDate() != null && insWsPlan.getUpDate().after(ippanInsWsPlan.getUpDate()))) {
					ippanInsWsPlan = insWsPlan;
					// 最新レコード取得
					if (lastInsWsPlan.getUpDate() == null || (insWsPlan.getUpDate() != null && insWsPlan.getUpDate().after(lastInsWsPlan.getUpDate()))) {
						lastInsWsPlan = insWsPlan;
					}
				}
				// 市区郡一般先合計に加算
				ippanSum = new InsWsSummaryDto(ippanSum, insWsPlan);
			}
			if (i == insWsList.size() - 1 || !insWsPlan.getAddrCodePref().equals(insWsList.get(i + 1).getAddrCodePref())
				|| !insWsPlan.getAddrCodeCity().equals(insWsList.get(i + 1).getAddrCodeCity())) {
				// 最終行、または次行のJISコードが異なる場合、市区郡重点先合計、市区郡一般先合計、市区郡合計行を生成する。
				// 重点先の明細行(特定施設行を先頭に挿入）
				resultList.addAll(jtnSpList);
				resultList.addAll(jtnList);
				// 重点先
				resultList.add(createJtnSum(insWsPlan, jtnSum));
				// 一般先計を生成
				resultList.add(createIppanSum(insWsPlan, ippanSum, ippanInsWsPlan.getUpDate()));
				// 市区郡合計
				sikuSum = new InsWsSummaryDto(jtnSum, ippanSum);
				resultList.add(createSikuSum(insWsPlan, sikuSum));
				// 重点先合計に加算
				jtnAllSum = new InsWsSummaryDto(jtnAllSum, jtnSum);
				// 一般先合計に加算
				ippanAllSum = new InsWsSummaryDto(ippanAllSum, ippanSum);
				// 全合計
				allSum = new InsWsSummaryDto(allSum, sikuSum);
				// 各合計の初期化
				jtnSum = new InsWsSummaryDto();
				ippanSum = new InsWsSummaryDto();
				ippanInsWsPlan = new InsWsPlanForVacForRef();
				sikuSum = new InsWsSummaryDto();
				jtnSpList = new ArrayList<InsWsPlanForVacUpDateResultDto>();
				jtnList = new ArrayList<InsWsPlanForVacUpDateResultDto>();
			}
		}

		// 先頭行にサマリー行を追加
		resultList.add(0, create(jtnAllSum));
		resultList.add(0, create(ippanAllSum));
		resultList.add(0, create(allSum));

		return new InsWsPlanForVacUpDateResultListDto(prodCode, prodName, jgiNo, jgiName, lastInsWsPlan.getUpJgiName(), lastInsWsPlan.getUpDate(), resultList,
				isStatusMrCompleted, refProdList, uhMrPlanAmount,uhInsWsPlanAmount, pMrPlanAmount, pInsWsPlanAmount);
	}

	/**
	 * 全合計行の検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param data.getDistSum() 配分値合計
	 * @param data.getPlanSum() 計画値合計
	 * @param summary 立案品目の納入実績合計
	 * @param summary1 参照品目1の納入実績合計
	 * @param summary2 参照品目2の納入実績合計
	 * @param summary3 参照品目3の納入実績合計
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanForVacUpDateResultDto create(InsWsSummaryDto insWsSummary) {
		List<InsWsPlanUpDateResultMonNnuDto> resultList = createMonNnu(insWsSummary);
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		//return new InsWsPlanForVacUpDateResultDto(insWsSummary.getDistSum(), insWsSummary.getPlanSum(), resultList);
		return new InsWsPlanForVacUpDateResultDto(insWsSummary.getDistSum(), insWsSummary.getPlanSum(), resultList, insWsSummary.getModifySum());
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	}

	/**
	 * 施設合計行の検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param insSum 合計
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanForVacUpDateResultDto createInsSum(InsWsPlanForVacForRef insWsPlan, InsWsSummaryDto insSum) {
		List<InsWsPlanUpDateResultMonNnuDto> resultList = createMonNnu(insSum);
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		//return new InsWsPlanForVacUpDateResultDto(insWsPlan, insSum.getDistSum(), insSum.getPlanSum(), resultList, true, false, false, false, null);
		return new InsWsPlanForVacUpDateResultDto(insWsPlan, insSum.getDistSum(), insSum.getPlanSum(), resultList, true, false, false, false, null, insSum.getModifySum());
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	}

	/**
	 * 重点先合計行の検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param jtnSum 合計
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanForVacUpDateResultDto createJtnSum(InsWsPlanForVacForRef insWsPlan, InsWsSummaryDto jtnSum) {
		List<InsWsPlanUpDateResultMonNnuDto> resultList = createMonNnu(jtnSum);
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		//return new InsWsPlanForVacUpDateResultDto(insWsPlan, jtnSum.getDistSum(), jtnSum.getPlanSum(), resultList, false, true, false, false, null);
		return new InsWsPlanForVacUpDateResultDto(insWsPlan, jtnSum.getDistSum(), jtnSum.getPlanSum(), resultList, false, true, false, false, null, jtnSum.getModifySum());
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	}

	/**
	 * 一般先合計行の検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param ippanSum 合計
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanForVacUpDateResultDto createIppanSum(InsWsPlanForVacForRef insWsPlan, InsWsSummaryDto ippanSum, Date upDate) {
		List<InsWsPlanUpDateResultMonNnuDto> resultList = createMonNnu(ippanSum);
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		//return new InsWsPlanForVacUpDateResultDto(insWsPlan, ippanSum.getDistSum(), ippanSum.getPlanSum(), resultList, false, false, true, false, upDate);
		return new InsWsPlanForVacUpDateResultDto(insWsPlan, ippanSum.getDistSum(), ippanSum.getPlanSum(), resultList, false, false, true, false, upDate, ippanSum.getModifySum());
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	}

	/**
	 * 市区郡合計行の検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param sikuSum 合計
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanForVacUpDateResultDto createSikuSum(InsWsPlanForVacForRef insWsPlan, InsWsSummaryDto sikuSum) {
		List<InsWsPlanUpDateResultMonNnuDto> resultList = createMonNnu(sikuSum);
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		//return new InsWsPlanForVacUpDateResultDto(insWsPlan, sikuSum.getDistSum(), sikuSum.getPlanSum(), resultList, false, false, false, true, null);
		return new InsWsPlanForVacUpDateResultDto(insWsPlan, sikuSum.getDistSum(), sikuSum.getPlanSum(), resultList, false, false, false, true, null, sikuSum.getModifySum());
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	}

	/**
	 * 検索結果用DTOを生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @return 検索結果用DTO
	 */
	protected InsWsPlanForVacUpDateResultDto create(InsWsPlanForVacForRef insWsPlan) {
		return new InsWsPlanForVacUpDateResultDto(insWsPlan, createMonNnu(insWsPlan));
	}

	/**
	 * 過去実績参照部を生成する。
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @return 過去実績参照部
	 */
	protected List<InsWsPlanUpDateResultMonNnuDto> createMonNnu(InsWsPlanForVacForRef insWsPlan) {
		return createMonNnu(insWsPlan.getMonNnuSummary(), insWsPlan.getMonNnuSummary1(), insWsPlan.getMonNnuSummary2(), insWsPlan.getMonNnuSummary3());
	}

	/**
	 * 過去実績参照部を生成する。
	 *
	 * @param summary 立案品目の納入実績
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
		List<InsWsPlanUpDateResultMonNnuDto> resultList = new ArrayList<InsWsPlanUpDateResultMonNnuDto>();
		// 立案品目
		resultList.add(new InsWsPlanUpDateResultMonNnuDto(0, prodCode, prodName, null, summary));

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
	 * @param summary 加算対象1
	 * @param summaryAdd 加算対象2
	 * @return 納入実績
	 */
	protected MonNnuSummary add(MonNnuSummary summary1, MonNnuSummary summary2) {
		return new AddMonNnuSummaryLogic(summary1, summary2).execute();
	}
}
