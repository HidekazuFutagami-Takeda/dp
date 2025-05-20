package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TeamPlan;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画　チーム別計画DTO<br>
 * 配下の担当者情報も含む。
 * 
 * @author nozaki
 */
public class TeamPlanDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * チーム別計画のシーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 組織名
	 */
	private final String sosName;

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
	 * チーム内担当者の担当者別計画情報リスト
	 */
	private final List<MrPlanDto> mrPlanDtoList;

	/**
	 * チーム内担当者の積み上げ情報
	 */
	private final MrPlanDto totalMrPlanDto;

	/**
	 * コンストラクタ（チーム別計画）
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param sosMst 組織情報
	 * @param plannedProd 計画対象品目
	 * @param monNnu 納入実績
	 * @param mrPlan チーム別計画
	 */
	public TeamPlanDto(InsType insType, SosMst sosMst, PlannedProd plannedProd, MonNnu monNnu, TeamPlan teamPlan, List<MrPlanDto> mrPlanDtoList, MrPlanDto totalMrPlanDto) {

		this.seqKey = teamPlan.getSeqKey();
		this.sosCd = sosMst.getSosCd();
		this.sosName = sosMst.getBumonSeiName();
		this.prodCode = plannedProd.getProdCode();
		this.resultValueDto = new MrPlanResultValueDto(monNnu);
		this.plannedValueDto = new MrPlanPlannedValueDto(insType, teamPlan, monNnu);
		this.upDate = teamPlan.getUpDate();
		this.mrPlanDtoList = mrPlanDtoList;
		this.totalMrPlanDto = totalMrPlanDto;
	}

	/**
	 * コンストラクタ（チーム別計画営業所内サマリ）
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param monNnu 営業所内担当者実績情報サマリ
	 * @param summary 営業所内チーム別計画サマリ
	 */
	public TeamPlanDto(InsType insType, MonNnu monNnu, TeamPlanSosSummaryDto summary) {
		this.seqKey = null;
		this.sosCd = null;
		this.sosName = null;
		this.prodCode = null;
		this.resultValueDto = new MrPlanResultValueDto(monNnu);
		this.plannedValueDto = new MrPlanPlannedValueDto(insType, summary, monNnu);
		this.upDate = null;
		this.mrPlanDtoList = null;
		this.totalMrPlanDto = null;
	}

	/**
	 * コンストラクタ(更新用)
	 * 
	 * @param seqKey チーム別計画のシーケンスキー
	 * @param sosCd 組織コード
	 * @param officeValueY 営業所計画UH
	 * @param plannedValueY 計画値UH
	 * @param upDate 更新日
	 */
	public TeamPlanDto(Long seqKey, String sosCd, Long officeValueY, Long plannedValueY, Date upDate) {

		this.seqKey = seqKey;
		this.sosCd = sosCd;
		this.sosName = null;
		this.prodCode = null;
		this.resultValueDto = null;
		this.plannedValueDto = new MrPlanPlannedValueDto(null, null, null, officeValueY, plannedValueY);
		this.upDate = upDate;
		this.mrPlanDtoList = null;
		this.totalMrPlanDto = null;
	}

	/**
	 * チーム別計画のシーケンスキーを取得する。
	 * 
	 * @return チーム別計画のシーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織名を取得する。
	 * 
	 * @return 組織名
	 */
	public String getSosName() {
		return sosName;
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
	 * @return 更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * チーム内担当者の担当者別計画情報リストを取得する。
	 * 
	 * @return チーム内担当者の担当者別計画情報リスト
	 */
	public List<MrPlanDto> getMrPlanDtoList() {
		return mrPlanDtoList;
	}

	/**
	 * チーム内担当者の積み上げ情報を取得する。
	 * 
	 * @return チーム内担当者の積み上げ情報
	 */
	public MrPlanDto getTotalMrPlanDto() {
		return totalMrPlanDto;
	}

	/**
	 * 更新用のチーム別計画を取得する。
	 * 
	 * @param 施設出力区分
	 * @return チーム別計画
	 */
	public TeamPlan convertTeamPlan(InsType insType) {

		TeamPlan teamPlan = new TeamPlan();
		teamPlan.setSeqKey(seqKey);
		teamPlan.setSosCd(sosCd);
		teamPlan.setProdCode(prodCode);
		if (insType == InsType.UH) {
			teamPlan.setOfficeValueUhY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getOfficeValueY()));
			teamPlan.setPlannedValueUhY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getPlannedValueY()));
		} else if (insType == InsType.P) {
			teamPlan.setOfficeValuePY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getOfficeValueY()));
			teamPlan.setPlannedValuePY(ConvertUtil.parseMoneyToNormalUnit(plannedValueDto.getPlannedValueY()));
		}
		teamPlan.setUpDate(upDate);
		return teamPlan;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
