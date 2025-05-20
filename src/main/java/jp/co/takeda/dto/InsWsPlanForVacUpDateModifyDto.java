package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用施設特約店別計画の追加・更新用DTO
 * 
 * @author khashimoto
 */
public class InsWsPlanForVacUpDateModifyDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * JIS府県コード
	 */
	private final Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 更新行、追加行DTOのリスト<br>
	 * シーケンスキー(追加時null)・更新日時(追加時null)・施設コード・特約店コード・修正計画
	 */
	private final List<InsWsPlanUpDateModifyRowDto> modifyRow;

	/**
	 * 一般先計の計画値
	 */
	private final Long ippanValue;

	/**
	 * 一般先計の更新日時
	 */
	private final Date upDate;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param addrCodePref JIS府県コード
	 * @param addrCodeCity JIS市区町村コード
	 * @param modifyRow 更新行、追加行DTOのリスト
	 * @param ippanValue 一般先計の計画値
	 * @param upDate 一般先計の更新日時
	 * 
	 */
	public InsWsPlanForVacUpDateModifyDto(Integer jgiNo, String prodCode, String addrCodePref, String addrCodeCity, List<InsWsPlanUpDateModifyRowDto> modifyRow, Long ippanValue,
		Date upDate) {
		super();
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.addrCodePref = Prefecture.getInstance(addrCodePref);
		this.addrCodeCity = addrCodeCity;
		this.modifyRow = modifyRow;
		this.ippanValue = ippanValue;
		this.upDate = upDate;
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
	 * 品目固定コードを取得する。
	 * 
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * JIS府県コードを取得する。
	 * 
	 * @return addrCodePref JIS府県コード
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
	 * 更新行、追加行DTOのリストを取得する。
	 * 
	 * @return modifyRow 更新行、追加行DTOのリスト
	 */
	public List<InsWsPlanUpDateModifyRowDto> getModifyRow() {
		return modifyRow;
	}

	/**
	 * 一般先計の計画値を取得する。
	 * 
	 * @return 一般先計の計画値
	 */
	public Long getIppanValue() {
		return ippanValue;
	}

	/**
	 * 一般先計の更新日時を取得する。
	 * 
	 * @return 一般先計の更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
