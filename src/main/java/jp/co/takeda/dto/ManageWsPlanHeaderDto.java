package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店別計画編集画面ヘッダを表すDTO
 * 
 * @author siwamoto
 */
public class ManageWsPlanHeaderDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 特約店名称
	 */
	private final String tmsTytenName;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenName 特約店名称
	 */
	public ManageWsPlanHeaderDto(String tmsTytenCd, String tmsTytenName) {
		this.tmsTytenCd = tmsTytenCd;
		this.tmsTytenName = tmsTytenName;
	}

	/**
	 * 特約店コードを取得する。
	 * 
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店名称を取得する。
	 * 
	 * @return tmsTytenName 特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
