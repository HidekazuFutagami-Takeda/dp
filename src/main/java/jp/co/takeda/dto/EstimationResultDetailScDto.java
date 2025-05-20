package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;

/**
 * 試算結果詳細の検索用DTO
 *
 * @author stakeuchi
 */
public class EstimationResultDetailScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 対象区分
	 */
	private final InsType insType;

	/**
	 * カテゴリ
	 */
	private final String category;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @param insType 対象区分
	 * @param category カテゴリ
	 */
	public EstimationResultDetailScDto(String sosCd3, String sosCd4, String prodCode, InsType insType, String category) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.prodCode = prodCode;
		this.insType = insType;
		this.category = category;
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
	 * 組織コード(チーム)を取得する。
	 *
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
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
	 * 対象区分を取得する。
	 *
	 * @return insType 対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
