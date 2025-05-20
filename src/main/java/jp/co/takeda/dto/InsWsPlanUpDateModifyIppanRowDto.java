package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画の追加・更新用行DTO
 * 
 * @author khashimoto
 */
public class InsWsPlanUpDateModifyIppanRowDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * JIS府県コード
	 */
	private final Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 修正計画
	 */
	private final Long ippanPlannedValue;

	/**
	 * 更新日時
	 */
	private final Date ippanUpDate;

	/**
	 * コンストラクタ
	 * 
	 * @param addrCodePref JIS府県コード
	 * @param addrCodeCity JIS市区町村コード
	 * @param ippanUpDate 更新日時
	 * @param ippanPlannedValue 修正計画
	 */
	public InsWsPlanUpDateModifyIppanRowDto(Prefecture addrCodePref, String addrCodeCity, Long ippanPlannedValue, Date ippanUpDate) {
		this.addrCodePref = addrCodePref;
		this.addrCodeCity = addrCodeCity;
		this.ippanPlannedValue = ippanPlannedValue;
		this.ippanUpDate = ippanUpDate;
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
	 * 修正計画を取得する。
	 * 
	 * @return 修正計画
	 */
	public Long getIppanPlannedValue() {
		return ippanPlannedValue;
	}

	/**
	 * 更新日時を取得する。
	 * 
	 * @return 更新日時
	 */
	public Date getIppanUpDate() {
		return ippanUpDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
