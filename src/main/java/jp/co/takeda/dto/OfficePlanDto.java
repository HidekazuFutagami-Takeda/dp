package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.OfficePlan;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画　営業所計画DTO 配下のチーム情報も含む。
 * 
 * @author nozaki
 */
public class OfficePlanDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 実績情報
	 */
	private final MrPlanResultValueDto resultValueDto;

	/**
	 * 計画情報
	 */
	private final MrPlanPlannedValueDto plannedValueDto;

	/**
	 * 営業所内チームのチーム別計画情報リスト
	 */
	private final List<TeamPlanDto> teamPlanDtoList;

	/**
	 * 営業所内チームの積み上げ情報
	 */
	private final TeamPlanDto totalTeamPlanDto;

	/**
	 * 調整金額有無フラグ<br>
	 * 表示する対象区分と逆の対象区分の計画値に調整金額が発生している場合：true
	 */
	private final Boolean choseiFlg;

	/**
	 * 参照品目の過去実績参照(担当者別計画)DTOのリスト
	 */
	private final List<DeliveryResultSummaryDto> deliveryResultSummaryDtoList;

	/**
	 * チームが存在する組織（営業所）か<br>
	 * true：存在する、false：存在しない（営業所直下MR）
	 */
	private final boolean hasTeamSos;

	/**
	 * コンストラクタ
	 * 
	 * @param insType 施設出力区分(UH,P以外の場合は合計)
	 * @param monNnu 納入実績
	 * @param officePlan 営業所計画
	 * @param teamSummary チームサマリ
	 * @param teamPlanDtoList チーム集計
	 * @param totalTeamPlanDto 担当者別計画　チーム別計画DTO
	 * @param choseiFlg 調整金額有無フラグ
	 * @param deliveryResultSummaryDtoList 参照品目の過去実績参照(担当者別計画)DTOのリスト
	 */
	public OfficePlanDto(InsType insType, MonNnu monNnu, OfficePlan officePlan, TeamPlanSosSummaryDto teamSummary, List<TeamPlanDto> teamPlanDtoList, TeamPlanDto totalTeamPlanDto,
		boolean choseiFlg, List<DeliveryResultSummaryDto> deliveryResultSummaryDtoList, boolean hasTeamSos) {
		this.resultValueDto = new MrPlanResultValueDto(monNnu);
		this.plannedValueDto = new MrPlanPlannedValueDto(insType, officePlan, teamSummary, monNnu);
		this.teamPlanDtoList = teamPlanDtoList;
		this.totalTeamPlanDto = totalTeamPlanDto;
		this.choseiFlg = choseiFlg;
		this.deliveryResultSummaryDtoList = deliveryResultSummaryDtoList;
		this.hasTeamSos = hasTeamSos;
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
	 * 営業所内チームのチーム別計画情報リストを取得する。
	 * 
	 * @return 営業所内チームのチーム別計画情報リスト
	 */
	public List<TeamPlanDto> getTeamPlanDtoList() {
		return teamPlanDtoList;
	}

	/**
	 * 営業所内チームの積み上げ情報を取得する。
	 * 
	 * @return 営業所内チームの積み上げ情報
	 */
	public TeamPlanDto getTotalTeamPlanDto() {
		return totalTeamPlanDto;
	}

	/**
	 * 調整金額有無フラグを取得する。
	 * 
	 * @return 調整金額有無フラグ
	 */
	public Boolean getChoseiFlg() {
		return choseiFlg;
	}

	/**
	 * チームが存在する組織（営業所）かを取得する。
	 * 
	 * @return true：存在する、false：存在しない（営業所直下MR）
	 */
	public boolean hasTeamSos() {
		return hasTeamSos;
	}

	/**
	 * 参照品目の過去実績参照(担当者別計画)DTOのリストを取得する。
	 * 
	 * @return 参照品目の過去実績参照(担当者別計画)DTOのリスト
	 */
	public List<DeliveryResultSummaryDto> getDeliveryResultSummaryDtoList() {
		return deliveryResultSummaryDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
