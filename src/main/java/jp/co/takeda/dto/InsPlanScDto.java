package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.div.InsType;

/**
 * 施設別計画の検索用DTO
 *
 * @author stakeuchi
 */
public class InsPlanScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード（支店）
	 */
	private final String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設区分
	 */
	private final InsType insType;

	/**
	 * カテゴリ
	 */
	private final String prodCategory;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 計画検索範囲
	 */
	private final PlanData planData;

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo 従業員番号
	 * @param insNo 施設コード
	 * @param insType 施設区分
	 * @param prodCategory カテゴリ
	 * @param prodCode 品目コード
	 * @param planData 計画検索範囲
	 */
	public InsPlanScDto(String sosCd2, String sosCd3, String sosCd4,Integer jgiNo,String insNo, InsType insType, String prodCategory, String prodCode, PlanData planData) {
		this.sosCd2 = sosCd2;
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.insType = insType;
		this.prodCategory = prodCategory;
		this.prodCode = prodCode;
		this.planData = planData;
	}
	/**
	 * 組織コード（支店）を取得する。
	 *
	 * @return sosCd2 組織コード（支店）
	 */
	public String getSosCd2() {
		return sosCd2;
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
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設区分を取得する。
	 *
	 * @return insType 施設区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 計画検索範囲を取得する。
	 *
	 * @return planData 計画検索範囲
	 */
	public PlanData getPlanData() {
		return planData;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
