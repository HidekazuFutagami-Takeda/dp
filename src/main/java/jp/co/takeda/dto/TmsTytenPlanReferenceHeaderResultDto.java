package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 特約店別計画参照画面ヘッダー 結果DTO
 *
 * @author yokokawa
 */
public class TmsTytenPlanReferenceHeaderResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// constructor
	// -------------------------------
	/**
	 * コンストラクタ
	 *
	 * @param changeParamYTUpDate 変換パラメータ(Y→T価)更新日時
	 * @param tmsTytenName 特約店名称
	 */
	public TmsTytenPlanReferenceHeaderResultDto(Date changeParamYTUpDate, Date changeParamBTUpDate,
			String tmsTytenName, Boolean transTFlg, Boolean wsEndFlg) {
		this.changeParamYTUpDate = changeParamYTUpDate;
		this.changeParamBTUpDate = changeParamBTUpDate;
		this.tmsTytenName = tmsTytenName;
		this.transTFlg = transTFlg;
		this.wsEndFlg = wsEndFlg;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 変換パラメータ 更新日時
	 */
	private final Date changeParamYTUpDate;

	/**
	 * 変換パラメータ(B→T価)更新日時
	 */
	private final Date changeParamBTUpDate;

	/**
	 * 特約店名称
	 */
	private final String tmsTytenName;

	/**
	 * T価変換バッチ実施フラグ
	 */
	private Boolean transTFlg;

	/**
	 * 施設特約店別計画〆フラグ
	 */
	private Boolean wsEndFlg;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 変換パラメータ(Y→T価)更新日時を取得する
	 *
	 * @return 変換パラメータ(Y→T価)更新日時
	 */
	public Date getChangeParamYTUpDate() {
		return changeParamYTUpDate;
	}

	/**
	 * 変換パラメータ(B→T価)更新日時を取得する。
	 *
	 * @return 変換パラメータ(B→T価)更新日時
	 */
	public Date getChangeParamBTUpDate() {
		return changeParamBTUpDate;
	}

	/**
	 * 特約店名称を取得する。
	 *
	 * @return 特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * T価変換バッチ実施フラグを取得する。
	 *
	 * @return T価変換バッチ実施フラグ
	 */
	public Boolean getTransTFlg() {
		return transTFlg;
	}

	/**
	 * 施設特約店別計画〆フラグを取得する。
	 *
	 * @return 施設特約店別計画〆フラグ
	 */
	public Boolean getWsEndFlg() {
		return wsEndFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
