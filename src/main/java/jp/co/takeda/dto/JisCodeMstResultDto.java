package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JisCodeMst;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * JIS府県・市区町村の検索条件(Search Condition)を表すDTO
 * 
 * @author stakeuchi
 */
public class JisCodeMstResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 市区郡町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 市区郡町村名(漢字)
	 */
	private final String addrNameCity;

	/**
	 * DTO変換元のJIS府県・市区郡町村モデル
	 */
	private final JisCodeMst jisCodeMst;

	/**
	 * コンストラクタ
	 * 
	 * @param addrCodeCity 市区郡町村コード
	 * @param addrNameCity 市区郡町村名(漢字)
	 * @param jisCodeMst DTO変換元のJIS府県・市区郡町村モデル
	 */
	public JisCodeMstResultDto(String addrCodeCity, String addrNameCity, JisCodeMst jisCodeMst) {
		this.addrCodeCity = addrCodeCity;
		this.addrNameCity = addrNameCity;
		this.jisCodeMst = jisCodeMst;
	}

	/**
	 * 市区郡町村コードを取得する。
	 * 
	 * @return addrCodeCity 市区郡町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * 市区郡町村名(漢字)を取得する。
	 * 
	 * @return addrNameCity 市区郡町村名(漢字)
	 */
	public String getAddrNameCity() {
		return addrNameCity;
	}

	/**
	 * DTO変換元のJIS府県・市区郡町村モデルを取得する。
	 * 
	 * @return jisCodeMst DTO変換元のJIS府県・市区郡町村モデル
	 */
	public JisCodeMst getJisCodeMst() {
		return jisCodeMst;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
