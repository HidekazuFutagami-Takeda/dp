package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 登録済み未獲得市場の検索条件(Search Condition)を表すDTO
 * 
 * @author stakeuchi
 */
public class MikakutokuSijouScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 薬効市場コード
	 */
	private final String yakkouSijouCode;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param yakkouSijouCode 薬効市場コード
	 */
	public MikakutokuSijouScDto(String sosCd3, String yakkouSijouCode) {
		this.sosCd3 = sosCd3;
		this.yakkouSijouCode = yakkouSijouCode;
	}

	/**
	 * 組織コード（営業所）を取得する。
	 * 
	 * @return 組織コード（営業所）
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 薬効市場コードを取得する。
	 * 
	 * @return yakkouSijouCode 薬効市場コード
	 */
	public String getYakkouSijouCode() {
		return yakkouSijouCode;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
