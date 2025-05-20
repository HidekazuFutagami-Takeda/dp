package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.StatusForMrPlan;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 営業所調整と営業所計画の検索結果を表すクラス
 *
 * @author nozaki
 */
public class OfficePlanResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 営業所計画シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 重点品目フラグかを示すフラグ
	 */
	private final Boolean planLevelInsDoc;

	/**
	 * 計画値UH(Y)
	 */
	private final Long plannedValueUhY;

	/**
	 * 計画値P(Y)
	 */
	private final Long plannedValuePY;

	/**
	 * 営業所計画更新日
	 */
	private final Date upDate;

	/**
	 * 営業所計画更新者
	 */
	private final String upUser;

	/**
	 * 特定施設個別計画U(Y)
	 */
	private final Long specialInsPlanValueUY;

	/**
	 * 特定施設個別計画H(Y)
	 */
	private final Long specialInsPlanValueHY;

	/**
	 *営・計画－担・計画調整額UH（Ｙ価）
	 */
	private final Long officeMrChoseiUhY;

	/**
	 * 営・計画－施特・計画調整額UH（Ｙ価）
	 */
	private final Long officeInswsChoseiUhY;

	/**
	 * 担・計画－施特・計画調整UHフラグ
	 */
	private final Boolean mrInswsChoseiUhFlg;

	/**
	 *営・計画－担・計画調整額P（Ｙ価）
	 */
	private final Long officeMrChoseiPY;

	/**
	 * 営・計画－施特・計画調整額P（Ｙ価）
	 */
	private final Long officeInswsChoseiPY;

	/**
	 * 担・計画－施特・計画調整Pフラグ
	 */
	private final Boolean mrInswsChoseiPFlg;

	/**
	 *営・計画－担・計画調整額UHP（Ｙ価）
	 */
	private final Long officeMrChoseiUHPY;

	/**
	 * 営・計画－施特・計画調整額UHP（Ｙ価）
	 */
	private final Long officeInswsChoseiUHPY;

	/**
	 * 担・計画－施特・計画調整UHPフラグ
	 */
	private final Boolean mrInswsChoseiUHPFlg;

	/**
	 * 担当者別計画向けステータス
	 */
	private final StatusForMrPlan statusForMrPlan;

	/**
	 * 担・計画－施医・計画調整UHフラグ
	 */
	private final Boolean mrDrChoseiUhFlg;

	/**
	 * 担・計画－施医・計画調整Pフラグ
	 */
	private final Boolean mrDrChoseiPFlg;

	/**
	 * 担・計画－施医・計画調整UHPフラグ
	 */
	private final Boolean mrDrChoseiUHPFlg;

	/**
	 * コンストラクタ
	 *
	 * @param seqKey 営業所計画シーケンスキー
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称
	 * @param planLevelInsDoc 重点品目フラグ
	 * @param plannedValueUhY 営業所計画UH(Y)
	 * @param plannedValuePY 営業所計画P(Y)
	 * @param lastUpDateUser 最終更新者
	 * @param lastUpDate 最終更新日
	 * @param specialInsPlanValueUY 特定施設個別計画U(Y)
	 * @param specialInsPlanValueHY 特定施設個別計画H(Y)
	 * @param officeMrChoseiUhY 営・計画－担・計画調整額UH（Ｙ価）
	 * @param officeInswsChoseiUhY 営・計画－施特・計画調整額UH（Ｙ価）
	 * @param mrInswsChoseiUhFlg 担・計画－施特・計画調整UHフラグ
	 * @param officeMrChoseiPY 営・計画－担・計画調整額P（Ｙ価）
	 * @param officeInswsChoseiPY 営・計画－施特・計画調整額P（Ｙ価）
	 * @param mrInswsChoseiPFlg 担・計画－施特・計画調整Pフラグ
	 * @param officeMrChoseiUHPY 営・計画－担・計画調整額UHP（Ｙ価）
	 * @param officeInswsChoseiUHPY 営・計画－施特・計画調整額UHP（Ｙ価）
	 * @param mrInswsChoseiUHPFlg 担・計画－施特・計画調整UHPフラグ
	 * @param statusForMrPlan 担当者別計画向けステータス
	 * @param mrDrChoseiUhFlg 担・計画－施医・計画調整UHフラグ
	 * @param mrDrChoseiPFlg 担・計画－施医・計画調整Pフラグ
	 * @param mrDrChoseiUHPFlg 担・計画－施医・計画調整UHPフラグ
	 */
	public OfficePlanResultDto(Long seqKey, String prodCode, String prodName, Boolean planLevelInsDoc, Long plannedValueUhY, Long plannedValuePY, Date upDate, String upUser, Long specialInsPlanValueUY,
		Long specialInsPlanValueHY, Long officeMrChoseiUhY, Long officeInswsChoseiUhY, Boolean mrInswsChoseiUhFlg, Long officeMrChoseiPY, Long officeInswsChoseiPY,
		Boolean mrInswsChoseiPFlg, Long officeMrChoseiUHPY, Long officeInswsChoseiUHPY, Boolean mrInswsChoseiUHPFlg, StatusForMrPlan statusForMrPlan,
		Boolean mrDrChoseiUhFlg, Boolean mrDrChoseiPFlg, Boolean mrDrChoseiUHPFlg) {
		this.seqKey = seqKey;
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.planLevelInsDoc = planLevelInsDoc;
		this.plannedValueUhY = plannedValueUhY;
		this.plannedValuePY = plannedValuePY;
		this.upDate = upDate;
		this.upUser = upUser;
		this.specialInsPlanValueUY = specialInsPlanValueUY;
		this.specialInsPlanValueHY = specialInsPlanValueHY;
		this.officeMrChoseiUhY = officeMrChoseiUhY;
		this.officeInswsChoseiUhY = officeInswsChoseiUhY;
		this.mrInswsChoseiUhFlg = mrInswsChoseiUhFlg;
		this.officeMrChoseiPY = officeMrChoseiPY;
		this.officeInswsChoseiPY = officeInswsChoseiPY;
		this.mrInswsChoseiPFlg = mrInswsChoseiPFlg;
		this.officeMrChoseiUHPY = officeMrChoseiUHPY;
		this.officeInswsChoseiUHPY = officeInswsChoseiUHPY;
		this.mrInswsChoseiUHPFlg = mrInswsChoseiUHPFlg;
		this.statusForMrPlan = statusForMrPlan;
		this.mrDrChoseiUhFlg = mrDrChoseiUhFlg;
		this.mrDrChoseiPFlg = mrDrChoseiPFlg;
		this.mrDrChoseiUHPFlg = mrDrChoseiUHPFlg;
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 品目固定を取得する。
	 *
	 * @return 品目固定
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}
	
	/**
	 * 重点品目フラグ を取得する。
	 * 
	 * @return 重点品目フラグ
	 */
	public Boolean getPlanLevelInsDoc() {
		return planLevelInsDoc;
	}

	/**
	 * 計画値UH(Y)を取得する。
	 *
	 * @return 計画値UH(Y)
	 */
	public Long getPlannedValueUhY() {
		return plannedValueUhY;
	}

	/**
	 * 計画値P(Y)を取得する。
	 *
	 * @return 計画値P(Y)
	 */
	public Long getPlannedValuePY() {
		return plannedValuePY;
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
	 * 更新者を取得する。
	 *
	 * @return 更新者
	 */
	public String getUpUser() {
		return upUser;
	}

	/**
	 * 特定施設個別計画U(Y)を取得する。
	 *
	 * @return 特定施設個別計画U(Y)
	 */
	public Long getSpecialInsPlanValueUY() {
		return specialInsPlanValueUY;
	}

	/**
	 * 特定施設個別計画H(Y)を取得する。
	 *
	 * @return 特定施設個別計画H(Y)
	 */
	public Long getSpecialInsPlanValueHY() {
		return specialInsPlanValueHY;
	}

	/**
	 * 営・計画－担・計画調整額UH（Ｙ価）を取得する。
	 *
	 * @return 営・計画－担・計画調整額UH（Ｙ価）
	 */
	public Long getOfficeMrChoseiUhY() {
		return officeMrChoseiUhY;
	}

	/**
	 * 営・計画－施特・計画調整額UH（Ｙ価）を取得する。
	 *
	 * @return 営・計画－施特・計画調整額UH（Ｙ価）
	 */
	public Long getOfficeInswsChoseiUhY() {
		return officeInswsChoseiUhY;
	}

	/**
	 * 担・計画－施特・計画調整UHフラグを取得する。
	 *
	 * @return 担・計画－施特・計画調整UHフラグ
	 */
	public Boolean getMrInswsChoseiUhFlg() {
		return mrInswsChoseiUhFlg;
	}

	/**
	 * 営・計画－施特・計画調整額P（Ｙ価）を取得する。
	 *
	 * @return 営・計画－施特・計画調整額P（Ｙ価）
	 */
	public Long getOfficeMrChoseiPY() {
		return officeMrChoseiPY;
	}

	/**
	 *営・計画－施特・計画調整額P（Ｙ価）を設定する。
	 *
	 * @param officeInswsChoseiPY 営・計画－施特・計画調整額P（Ｙ価）
	 */
	public Long getOfficeInswsChoseiPY() {
		return officeInswsChoseiPY;
	}

	/**
	 * 担・計画－施特・計画調整Pフラグを取得する。
	 *
	 * @return mrInswsChoseiPFlg 担・計画－施特・計画調整Pフラグ
	 */
	public Boolean getMrInswsChoseiPFlg() {
		return mrInswsChoseiPFlg;
	}

	/**
	 * 営・計画－施特・計画調整額UHP（Ｙ価）を取得する。
	 *
	 * @return 営・計画－施特・計画調整額UHP（Ｙ価）
	 */
	public Long getOfficeMrChoseiUHPY() {
		return officeMrChoseiUHPY;
	}

	/**
	 *営・計画－施特・計画調整額P（Ｙ価）を設定する。
	 *
	 * @param officeInswsChoseiUHPY 営・計画－施特・計画調整額UHP（Ｙ価）
	 */
	public Long getOfficeInswsChoseiUHPY() {
		return officeInswsChoseiUHPY;
	}

	/**
	 * 担・計画－施特・計画調整UHPフラグを取得する。
	 *
	 * @return mrInswsChoseiUHPFlg 担・計画－施特・計画調整UHPフラグ
	 */
	public Boolean getMrInswsChoseiUHPFlg() {
		return mrInswsChoseiUHPFlg;
	}

	/**
	 * 担当者別計画向けステータスを取得する。
	 *
	 * @return 担当者別計画向けステータス
	 */
	public StatusForMrPlan getStatusForMrPlan() {
		return statusForMrPlan;
	}

	/**
	 * 担・計画－施医・計画調整UHフラグを取得します。
	 * @return 担・計画－施医・計画調整UHフラグ
	 */
	public Boolean getMrDrChoseiUhFlg() {
	    return mrDrChoseiUhFlg;
	}

	/**
	 * 担・計画－施医・計画調整Pフラグを取得します。
	 * @return 担・計画－施医・計画調整Pフラグ
	 */
	public Boolean getMrDrChoseiPFlg() {
	    return mrDrChoseiPFlg;
	}

	/**
	 * 担・計画－施医・計画調整UHPフラグを取得します。
	 * @return 担・計画－施医・計画調整UHPフラグ
	 */
	public Boolean getMrDrChoseiUHPFlg() {
	    return mrDrChoseiUHPFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
