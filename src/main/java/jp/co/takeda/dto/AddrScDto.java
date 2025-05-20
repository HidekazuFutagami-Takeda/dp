package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 市区郡検索画面の検索条件DTO
 * 
 * @author khashimoto
 */
public class AddrScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * JIS府県コード
	 */
	private Prefecture addrCodePref;

	/**
	 * 市区郡町村名（漢字）
	 */
	private final String sikuMeiKn;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param addrCodePref JIS府県コード
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 */
	public AddrScDto(Prefecture addrCodePref, String sikuMeiKn) {
		this.addrCodePref = addrCodePref;
		this.sikuMeiKn = sikuMeiKn;
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
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getSikuMeiKn() {
		return sikuMeiKn;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
