package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画　担当者別計画DTO
 *
 * @author nozaki
 */
public class MrPlanDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 担当者別計画のシーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * 職種名
	 */
	private final String shokushuName;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 実績情報
	 */
	private final MrPlanResultValueDto resultValueDto;

	/**
	 * 計画情報
	 */
	private final MrPlanPlannedValueDto plannedValueDto;

	/**
	 * 更新日
	 */
	private final Date upDate;

	/**
	 * コンストラクタ（担当者別計画）
	 *
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param jgiMst 従業員情報
	 * @param plannedProd 計画対象品目
	 * @param monNnu 納入実績
	 * @param mrPlan 担当者別計画
	 */
	public MrPlanDto(InsType insType, JgiMst jgiMst, PlannedProd plannedProd, MonNnu monNnu, MrPlan mrPlan) {

		this.seqKey = mrPlan.getSeqKey();
		this.jgiNo = jgiMst.getJgiNo();
		this.jgiName = jgiMst.getJgiName();
		this.shokushuName = jgiMst.getShokushuName();
		this.prodCode = plannedProd.getProdCode();
		this.resultValueDto = new MrPlanResultValueDto(monNnu);
		this.plannedValueDto = new MrPlanPlannedValueDto(insType, mrPlan, monNnu);
		this.upDate = mrPlan.getUpDate();
	}

	/**
	 * コンストラクタ（担当者別計画チーム内サマリ）
	 *
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param monNnu チーム内担当者実績情報サマリ
	 * @param summary チーム内担当者別計画サマリ
	 */
	public MrPlanDto(InsType insType, MonNnu monNnu, MrPlanSosSummaryDto summary) {
		this.seqKey = null;
		this.jgiNo = null;
		this.jgiName = null;
		this.shokushuName = null;
		this.prodCode = null;
		this.resultValueDto = new MrPlanResultValueDto(monNnu);
		this.plannedValueDto = new MrPlanPlannedValueDto(insType, summary, monNnu);
		this.upDate = null;
	}

	/**
	 * コンストラクタ(更新情報作成)
	 *
	 * @param seqKey 担当者別計画のシーケンスキー
	 * @param officeValueUhY 営業所計画UH
	 * @param plannedValueUhY 計画値UH
	 * @param upDate 更新日
	 */
	public MrPlanDto(Long seqKey, Long officeValueY, Long plannedValueY, Date upDate) {

		this.seqKey = seqKey;
		this.jgiNo = null;
		this.jgiName = null;
		this.shokushuName = null;
		this.prodCode = null;
		this.resultValueDto = null;
		this.plannedValueDto = new MrPlanPlannedValueDto(null, null, null, officeValueY, plannedValueY);
		this.upDate = upDate;

	}

	/**
	 * 担当者別計画のシーケンスキーを取得する。
	 *
	 * @return 担当者別計画のシーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 実績情報を取得する。
	 *
	 * @return 実績情報
	 */
	public MrPlanResultValueDto getResultValueDto() {
		return resultValueDto;
	}

	/**
	 * 計画情報を取得する。
	 *
	 * @return 計画情報
	 */
	public MrPlanPlannedValueDto getPlannedValueDto() {
		return plannedValueDto;
	}

	/**
	 * 更新日を取得する。
	 *
	 * @return
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 職種名を取得する
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 更新用の担当者別計画を取得する。
	 *
	 * @param 施設出力区分
	 * @return 担当者別計画
	 */
	public MrPlan convertMrPlan(InsType insType) {

		MrPlan mrPlan = new MrPlan();
		mrPlan.setSeqKey(seqKey);
		mrPlan.setJgiNo(jgiNo);
		mrPlan.setProdCode(prodCode);
		if (insType == InsType.UH) {
			mrPlan.setOfficeValueUhY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getOfficeValueY()));
			mrPlan.setPlannedValueUhY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getPlannedValueY()));
		} else if (insType == InsType.P) {
			mrPlan.setOfficeValuePY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getOfficeValueY()));
			mrPlan.setPlannedValuePY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getPlannedValueY()));
		}
		mrPlan.setUpDate(upDate);
		return mrPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
