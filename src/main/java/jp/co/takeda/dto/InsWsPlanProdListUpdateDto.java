package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.PlannedProd;

/**
 * 施設特約店別計画 品目一覧更新用DTO
 *
 * @author nozaki
 */
public class InsWsPlanProdListUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 品目カテゴリ
	 */
	private final String category;

	/**
	 * ステータス最終更新日時
	 */
	private final Date upDate;

	/**
	 * コンストラクタ
	 *
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称
	 * @param upDate ステータス最終更新日時
	 */
	public InsWsPlanProdListUpdateDto(String prodCode, String prodName, String category, Date upDate) {
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.category = category;
		this.upDate = upDate;
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
	 * 品目名称を取得する。
	 *
	 * @return prodName 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return category 品目カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * ステータス最終更新日時を取得する。
	 *
	 * @return upDate ステータス最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 計画対象品目に変換する。
	 *
	 * @return 計画対象品目
	 */
	public PlannedProd convertPlannedProd() {
		PlannedProd plannedProd = new PlannedProd();
		plannedProd.setProdCode(prodCode);
		plannedProd.setProdName(prodName);
		plannedProd.setCategory(category);
		return plannedProd;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
