package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageInsWsPlanForVac;
import jp.co.takeda.util.ConvertUtil;

/**
 *
 * 施設特約店別計画編集画面を表すDTO
 *
 * @author siwamoto
 */
public class ManageInsWsPlanForVacDetailWsDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店名称
	 */
	private final String tmsTytemName;

	/**
	 * ＴＭＳ特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * B価ベース
	 */
	private final Long baseB;

	/**
	 * T価ベース（編集前）
	 */
	private final Long beforeBaseT;

	/**
	 * T価ベース
	 */
	private final Long baseT;

	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新者
	 */
	private final String upJgiName;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private final boolean planTaiGaiFlgTok;

	/**
	 * コンストラクタ
	 *
	 * @param insWsPlan 施設特約店別計画
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public ManageInsWsPlanForVacDetailWsDto(ManageInsWsPlanForVac insWsPlan, boolean planTaiGaiFlgTok) {

		this.tmsTytenCd = insWsPlan.getTmsTytenCd();
		this.tmsTytemName = insWsPlan.getTmsTytenName();
		this.baseB = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getImplPlanForVac().getPlanned2ValueY());
		this.beforeBaseT = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getImplPlanForVac().getPlanned2ValueT());
		this.baseT = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getImplPlanForVac().getPlanned2ValueT());
		this.seqKey = insWsPlan.getSeqKey();
		this.upDate = insWsPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = insWsPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * 特約店名称を取得する。
	 *
	 * @return 特約店名称
	 */
	public String getTmsTytemName() {
		return tmsTytemName;
	}

	/**
	 * ＴＭＳ特約店コードを取得する。
	 *
	 * @return ＴＭＳ特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * B価ベースを取得する。
	 *
	 * @return B価ベース
	 */
	public Long getBaseB() {
		return baseB;
	}

	/**
	 * T価ベース（編集前）を取得する。
	 *
	 * @return T価ベース（編集前）
	 */
	public Long getBeforeBaseT() {
		return beforeBaseT;
	}

	/**
	 * T価ベースを取得する。
	 *
	 * @return T価ベース
	 */
	public Long getBaseT() {
		return baseT;
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return seqKey シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
	}

	/**
	 * 最終更新日を取得する。
	 *
	 * @return 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 計画立案対象外フラグ(当期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
