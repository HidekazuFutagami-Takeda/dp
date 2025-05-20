package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

/**
 * ワクチン用施設特約店別計画の検索用DTO
 *
 * @author nozaki
 */
public class ManageInsWsPlanForVacScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  組織コード（支店）
	 */
	private final String sosCd2;

	/**
	 * 組織コード(エリア特約店)
	 */
	private final String sosCd3;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 活動区分
	 */
	private final ActivityType activityType;

	/**
	 * JIS府県コード
	 */
	private final Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * カテゴリ
	 */
	private final String prodCategory;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd2 従業員番号組織コード(支店)
	 * @param sosCd3 従業員番号組織コード(営業所)
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param insType 施設区分
	 * @param prodCategory カテゴリ
	 * @param prodCode 品目コード
	 * @param planData 計画検索範囲
	 */
	public ManageInsWsPlanForVacScDto(String sosCd2, String sosCd3, Integer jgiNo, String insNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity, String prodCategory, String prodCode,
		String tmsTytenCd) {
		this.sosCd2 = sosCd2;
		this.sosCd3 = sosCd3;
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.activityType = activityType;
		this.addrCodePref = addrCodePref;
		this.addrCodeCity = addrCodeCity;
		this.prodCategory = prodCategory;
		this.prodCode = prodCode;
		this.tmsTytenCd = tmsTytenCd;
	}



	/**
	 * 組織コード（支店）を取得する。
	 *
	 * @return sosCd2 組織コード（支店）
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 活動区分を取得する。
	 *
	 * @return activityType 活動区分
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * JIS府県コードを取得する。
	 *
	 * @return JIS府県コード
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS市区町村コードを取得する。
	 *
	 * @return JIS市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
