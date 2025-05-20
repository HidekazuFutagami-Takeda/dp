package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画検索結果用DTO
 * 
 * @author nozaki
 */
public class MrPlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織コード(組織コード)
	 */
	private final String sosCd3;

	/**
	 * 立案対象品目名称
	 */
	private final String prodName;

	/**
	 * 立案対象品目固定コード
	 */
	private final String prodCode;

	/**
	 * 立案対象品目の製品区分
	 */
	private final String prodType;

	/**
	 * 上位計画値(UH) <br>
	 * <ul>
	 * <li>チーム別計画、営業所の場合は、営業所計画</li>
	 * <li>チーム別計画、チームの場合は、営業所計画</li>
	 * <li>担当者別計画、営業所の場合は、チーム別計画積み上げ</li>
	 * <li>担当者別計画、チームの場合は、自チームのチーム別計画</li>
	 * </ul>
	 */
	private final Long previousPlanValueUH;

	/**
	 * 上位計画値(P) <br>
	 * <ul>
	 * <li>チーム別計画、営業所の場合は、営業所計画</li>
	 * <li>チーム別計画、チームの場合は、営業所計画</li>
	 * <li>担当者別計画、営業所の場合は、チーム別計画積み上げ</li>
	 * <li>担当者別計画、チームの場合は、自チームのチーム別計画</li>
	 * </ul>
	 */
	private final Long previousPlanValueP;

	/**
	 * 計画値積み上げ(UH) <br>
	 * <ul>
	 * <li>チーム別計画、営業所の場合は、チーム別計画積み上げ</li>
	 * <li>チーム別計画、チームの場合は、自チームのチーム別計画</li>
	 * <li>担当者別計画、営業所の場合は、担当者別計画積み上げ</li>
	 * <li>担当者別計画、チームの場合は、担当者別計画積み上げ</li>
	 * </ul>
	 */
	private final Long totalPlanValueUH;

	/**
	 * 計画値積み上げ(UH) <br>
	 * <ul>
	 * <li>チーム別計画、営業所の場合は、チーム別計画積み上げ</li>
	 * <li>チーム別計画、チームの場合は、自チームのチーム別計画</li>
	 * <li>担当者別計画、営業所の場合は、担当者別計画積み上げ</li>
	 * <li>担当者別計画、チームの場合は、担当者別計画積み上げ</li>
	 * </ul>
	 */
	private final Long totalPlanValueP;

	/**
	 * 最終更新日 <br>
	 * 
	 * <ul>
	 * <li>チーム別計画、営業所の場合は、チーム別計画の営業所内担当者の最終更新日</li>
	 * <li>チーム別計画、チームの場合は、チーム別計画のチーム内担当者の最終更新日</li>
	 * <li>担当者別計画、営業所の場合は、担当者別計画の営業所内担当者の最終更新日</li>
	 * <li>担当者別計画、チームの場合は、担当者別計画のチーム内担当者の最終更新日</li>
	 * </ul>
	 */
	private final Date lastUpdate;

	/**
	 * 最終更新者 <br>
	 * 
	 * <ul>
	 * <li>チーム別計画、営業所の場合は、チーム別計画の営業所内担当者の最終更新者</li>
	 * <li>チーム別計画、チームの場合は、チーム別計画のチーム内担当者の最終更新者</li>
	 * <li>担当者別計画、営業所の場合は、担当者別計画の営業所内担当者の最終更新者</li>
	 * <li>担当者別計画、チームの場合は、担当者別計画のチーム内担当者の最終更新者</li>
	 * </ul>
	 */
	private final String lastUpdateUser;

	/**
	 * 担当者別計画　営業所内計画情報
	 */
	private final OfficePlanDto officePlanDto;

	/**
	 * 担当者別計画ステータス
	 */
	private final StatusForMrPlan statusForMrPlan;

	/**
	 * 試算タイプ
	 */
	private final CalcType calcType;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd3 組織コード
	 * @param plannedProd 計画品目
	 * @param previousPlanValueUH 上位計画UH
	 * @param previousPlanValueP 上位計画P
	 * @param totalPlanValueUH 積み上げUH
	 * @param totalPlanValueP 積み上げP
	 * @param lastUpdate 最終更新者
	 * @param lastUpdateUser 最終更新日
	 * @param mrPlanOfficeInfoDto 担当者別計画　営業所内計画情報
	 * @param statusForMrPlan 担当者別計画ステータス
	 * @param calcType 試算タイプ
	 */
	public MrPlanResultDto(String sosCd3, PlannedProd plannedProd, Long previousPlanValueUH, Long previousPlanValueP, Long totalPlanValueUH, Long totalPlanValueP, Date lastUpdate,
		String lastUpdateUser, OfficePlanDto officePlanDto, StatusForMrPlan statusForMrPlan, CalcType calcType) {

		this.sosCd3 = sosCd3;
		this.prodName = plannedProd.getProdName();
		this.prodCode = plannedProd.getProdCode();
		this.prodType = plannedProd.getProdType();
		this.previousPlanValueUH = ConvertUtil.parseMoneyToThousandUnit(previousPlanValueUH);
		this.previousPlanValueP = ConvertUtil.parseMoneyToThousandUnit(previousPlanValueP);
		this.totalPlanValueUH = ConvertUtil.parseMoneyToThousandUnit(totalPlanValueUH);
		this.totalPlanValueP = ConvertUtil.parseMoneyToThousandUnit(totalPlanValueP);
		this.lastUpdate = lastUpdate;
		this.lastUpdateUser = lastUpdateUser;
		this.officePlanDto = officePlanDto;
		this.statusForMrPlan = statusForMrPlan;
		this.calcType = calcType;

	}

	/**
	 * 組織コード(組織コード)を取得する。
	 * 
	 * @return 組織コード(組織コード)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 立案対象品目固定コードを取得する。
	 * 
	 * @return 立案対象品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 立案対象品目名称を取得する。
	 * 
	 * @return 立案対象品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 立案対象品目の製品区分を取得する。
	 * 
	 * @return 立案対象品目の製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 上位計画値(UH)を取得する。
	 * 
	 * @return 上位計画値(UH)
	 */
	public Long getPreviousPlanValueUH() {
		return previousPlanValueUH;
	}

	/**
	 * 計画値積み上げ(UH) を取得する。
	 * 
	 * @return 計画値積み上げ(UH)
	 */
	public Long getTotalPlanValueUH() {
		return totalPlanValueUH;
	}

	/**
	 * 上位計画値(P)を取得する。
	 * 
	 * @return 上位計画値(P)
	 */
	public Long getPreviousPlanValueP() {
		return previousPlanValueP;
	}

	/**
	 * 計画値積み上げ(P)を取得する。
	 * 
	 * @return 計画値積み上げ(P)
	 */
	public Long getTotalPlanValueP() {
		return totalPlanValueP;
	}

	/**
	 * 最終更新日を取得する。
	 * 
	 * @return 最終更新日
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * 最終更新者を取得する。
	 * 
	 * @return 最終更新者
	 */
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * 担当者別計画　営業所計画情報を取得する。
	 * 
	 * @return 担当者別計画　営業所計画情報
	 */
	public OfficePlanDto getOfficePlanDto() {
		return officePlanDto;
	}

	/**
	 * 担当者別計画ステータスを取得する。
	 * 
	 * @return 担当者別計画ステータス
	 */
	public StatusForMrPlan getStatusForMrPlan() {
		return statusForMrPlan;
	}

	/**
	 * 試算タイプを取得する。
	 * 
	 * @return 試算タイプ
	 */
	public CalcType getCalcType() {
		return calcType;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
