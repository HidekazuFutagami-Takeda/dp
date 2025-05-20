package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.dto.MrPlanMMPMrDto;
import jp.co.takeda.dto.MrPlanMMPProdDto;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.MathUtil;

/**
 * 担当者別計画担当者別品目別一覧 従業員情報DTOの生成するロジッククラス
 * 
 * @author khashimoto
 */
public class CreateMrPlanMMPMrDtoLogic {

	/**
	 * 従業員情報リスト
	 */
	private final List<JgiMst> jgiMstList;

	/**
	 * 計画対象品目リスト
	 */
	private final List<PlannedProd> prodList;

	/**
	 * 担当者別計画リスト
	 */
	private final List<MrPlan> mrPlanList;

	/**
	 * 納入実績情報（UH）リスト
	 */
	private final List<DeliveryResultMr> deliUhList;

	/**
	 * 納入実績情報（P）リスト
	 */
	private final List<DeliveryResultMr> deliPList;

	/**
	 * 担当者別計画リストのサイズ
	 */
	private final int mrPlanMax;

	/**
	 * 納入実績情報（UH）リストのサイズ
	 */
	private final int deliUhMax;

	/**
	 * 納入実績情報（P）リストのサイズ
	 */
	private final int deliPMax;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiMstList 従業員情報リスト
	 * @param prodList 計画対象品目リスト
	 * @param mrPlanList 担当者別計画リスト
	 * @param deliUhList 納入実績情報（UH）リスト
	 * @param deliPList 納入実績情報（P）リスト
	 */
	public CreateMrPlanMMPMrDtoLogic(List<JgiMst> jgiMstList, List<PlannedProd> prodList, List<MrPlan> mrPlanList, List<DeliveryResultMr> deliUhList,
		List<DeliveryResultMr> deliPList) {
		this.jgiMstList = jgiMstList;
		this.prodList = prodList;
		this.mrPlanList = mrPlanList;
		this.deliUhList = deliUhList;
		this.deliPList = deliPList;
		this.mrPlanMax = mrPlanList.size();
		this.deliUhMax = deliUhList.size();
		this.deliPMax = deliPList.size();
	}

	/**
	 * DTOを生成する。
	 * 
	 * @return 担当者別計画担当者別品目別一覧 従業員情報DTOのリスト
	 */
	public List<MrPlanMMPMrDto> create() {
		// 各リストの現在インデックス
		int mrPlanIndex = 0;
		int deliUhIndex = 0;
		int deliPIndex = 0;

		List<MrPlanMMPMrDto> resultList = new ArrayList<MrPlanMMPMrDto>();

		for (JgiMst jgiMst : jgiMstList) {
			// 編集対象従業員
			Integer jgiNo = jgiMst.getJgiNo();

			// 品目リストの生成
			List<MrPlanMMPProdDto> prodDtoUhPList = new ArrayList<MrPlanMMPProdDto>();
			List<MrPlanMMPProdDto> prodDtoUhList = new ArrayList<MrPlanMMPProdDto>();
			List<MrPlanMMPProdDto> prodDtoPList = new ArrayList<MrPlanMMPProdDto>();
			for (PlannedProd prod : prodList) {
				String prodCode = prod.getProdCode();
				Long plannedValueUh = null;
				Long plannedValueP = null;
				Long deliUh = null;
				Long deliP = null;
				if (hasMrPlan(mrPlanIndex, jgiNo, prodCode)) {
					plannedValueUh = mrPlanList.get(mrPlanIndex).getPlannedValueUhY();
					plannedValueP = mrPlanList.get(mrPlanIndex).getPlannedValuePY();
					mrPlanIndex++;
				}
				if (hasDeliUh(deliUhIndex, jgiNo, prodCode)) {
					deliUh = deliUhList.get(deliUhIndex).getMonNnu().getAdvancePeriod();
					deliUhIndex++;
				}
				if (hasDeliP(deliPIndex, jgiNo, prodCode)) {
					deliP = deliPList.get(deliPIndex).getMonNnu().getAdvancePeriod();
					deliPIndex++;
				}
				// UHPのDTO生成
				MrPlanMMPProdDto prodDtoUhP = new MrPlanMMPProdDto(null, prodCode, MathUtil.add(plannedValueUh, plannedValueP), MathUtil.add(deliUh, deliP));
				prodDtoUhPList.add(prodDtoUhP);
				// UHのDTO生成
				MrPlanMMPProdDto prodDtoUh = new MrPlanMMPProdDto(InsType.UH, prodCode, plannedValueUh, deliUh);
				prodDtoUhList.add(prodDtoUh);
				// PのDTO生成
				MrPlanMMPProdDto prodDtoP = new MrPlanMMPProdDto(InsType.P, prodCode, plannedValueP, deliP);
				prodDtoPList.add(prodDtoP);

			}
			List<MrPlanMMPProdDto> prodDtoList = new ArrayList<MrPlanMMPProdDto>();
			prodDtoList.addAll(prodDtoUhPList);
			prodDtoList.addAll(prodDtoUhList);
			prodDtoList.addAll(prodDtoPList);

			// 従業員情報DTOの生成
			MrPlanMMPMrDto mrDto = new MrPlanMMPMrDto(jgiNo, jgiMst.getJgiName(), prodDtoList);
			resultList.add(mrDto);

		}

		return resultList;
	}

	/**
	 * 該当する担当者別計画かどうか判定し返す。
	 * 
	 * @param index インデックス
	 * @return 該当する担当者別計画がある場合、true
	 */
	protected boolean hasMrPlan(int index, Integer jgiNo, String prodCode) {
		if (mrPlanMax > index && mrPlanList.get(index).getJgiNo().equals(jgiNo) && mrPlanList.get(index).getProdCode().equals(prodCode)) {
			return true;
		}
		return false;
	}

	/**
	 * 該当する納入実績情報(UH)かどうか判定し返す。
	 * 
	 * @param index インデックス
	 * @return 該当する納入実績情報(UH)がある場合、true
	 */
	protected boolean hasDeliUh(int index, Integer jgiNo, String prodCode) {
		if (deliUhMax > index && deliUhList.get(index).getJgiNo().equals(jgiNo) && deliUhList.get(index).getProdCode().equals(prodCode)) {
			return true;
		}
		return false;
	}

	/**
	 * 該当する納入実績情報(P)かどうか判定し返す。
	 * 
	 * @param index インデックス
	 * @return 該当する納入実績情報(P)がある場合、true
	 */
	protected boolean hasDeliP(int index, Integer jgiNo, String prodCode) {
		if (deliPMax > index && deliPList.get(index).getJgiNo().equals(jgiNo) && deliPList.get(index).getProdCode().equals(prodCode)) {
			return true;
		}
		return false;
	}
}
