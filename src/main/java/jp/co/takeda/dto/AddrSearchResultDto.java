package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 市区郡検索画面の検索結果用DTO
 * 
 * @author siwamoto
 */
public class AddrSearchResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * JIS府県コード
	 */
	private String addrCodePref;

	/**
	 * 府県名
	 */
	private final String fukenName;

	/**
	 * JIS市区町村コード
	 */
	private String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）
	 */
	private final String shikuchosonMeiKj;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param addrCodePref JIS府県コード
	 * @param fukenName 府県名
	 * @param addrCodeCity JIS市区町村コード
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 */
	public AddrSearchResultDto(String addrCodePref, String fukenName, String addrCodeCity, String shikuchosonMeiKj) {
		this.addrCodePref = addrCodePref;
		this.fukenName = fukenName;
		this.addrCodeCity = addrCodeCity;
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	/**
	 * JIS府県コードを取得する。
	 * 
	 * @return addrCodePref JIS府県コード
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 府県名を取得する。
	 * 
	 * @return fukenName 府県名
	 */
	public String getfukenName() {
		return fukenName;
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
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
