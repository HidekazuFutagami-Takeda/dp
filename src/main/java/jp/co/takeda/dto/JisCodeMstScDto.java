package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * JIS府県・市区町村の検索条件(Search Condition)を表すDTO
 * 
 * @author stakeuchi
 */
public class JisCodeMstScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 都道府県コード
	 */
	private final Prefecture addrCodePref;

	/**
	 * 市区郡町村コード
	 */
	private final String addrCodeCity;

	/**
	 * コンストラクタ
	 * 
	 * @param prefecture 都道府県コード
	 * @param shikuchosonCd 市区郡町村コード
	 */
	public JisCodeMstScDto(Prefecture addrCodePref, String addrCodeCity) {
		this.addrCodePref = addrCodePref;
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * 都道府県コードを取得する。
	 * 
	 * @return addrCodePref 都道府県コード
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 市区郡町村コードを取得する。
	 * 
	 * @return addrCodeCity 市区郡町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
