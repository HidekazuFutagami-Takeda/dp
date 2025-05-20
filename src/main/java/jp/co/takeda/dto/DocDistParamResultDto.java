package jp.co.takeda.dto;

import java.util.Map;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 医師計画配分　配分パラメータ検索結果用DTO
 * 
 * @author stakeuchi
 */
public class DocDistParamResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// ---------------------
	// Data Field
	// --------------------
	/**
	 * 計画品目
	 */
	private final Map<String, Object> prodMap;

	/**
	 * 本部案（UH）
	 */
	private final Map<String, Object> honbuUHMap;

	/**
	 * 本部案（P）
	 */
	private final Map<String, Object> honbuPMap;

	/**
	 * 営業所案（UH）
	 */
	private final Map<String, Object> officeUHMap;

	/**
	 * 営業所案（P）
	 */
	private final Map<String, Object> officePMap;

	/**
	 * コンストラクタ
	 * 
	 * @param prodMap 計画品目
	 * @param honbuUHMap 本部案（UH）
	 * @param honbuPMap 本部案（P）
	 * @param officeUHMap 営業所案（UH）
	 * @param officePMap 営業所案（P）
	 */
	public DocDistParamResultDto(Map<String, Object> prodMap,
			Map<String, Object> honbuUHMap, Map<String, Object> honbuPMap,
			Map<String, Object> officeUHMap, Map<String, Object> officePMap) {
		this.prodMap = prodMap;
		this.honbuUHMap = honbuUHMap;
		this.honbuPMap = honbuPMap;
		this.officeUHMap = officeUHMap;
		this.officePMap = officePMap;
	}

	/**
	 * 計画品目を取得する。
	 * 
	 * @return 計画品目
	 */
	public Map<String, Object> getProdMap() {
		return prodMap;
	}

	/**
	 * 本部案（UH）を取得する。
	 * 
	 * @return 本部案（UH）
	 */
	public Map<String, Object> getHonbuUHMap() {
		return honbuUHMap;
	}

	/**
	 * 本部案（P）を取得する。
	 * 
	 * @return 本部案（P）
	 */
	public Map<String, Object> getHonbuPMap() {
		return honbuPMap;
	}

	/**
	 * 営業所案（UH）を取得する。
	 * 
	 * @return 営業所案（UH）
	 */
	public Map<String, Object> getOfficeUHMap() {
		return officeUHMap;
	}

	/**
	 * 営業所案（P）を取得する。
	 * 
	 * @return 営業所案（P）
	 */
	public Map<String, Object> getOfficePMap() {
		return officePMap;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
