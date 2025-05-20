package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.SpecialInsPlan;

/**
 * 特定施設個別計画の登録/更新用DTO
 *
 * @author khashimoto
 */
public class SpecialInsPlanModifyDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設を担当する従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 登録対象の施設コード（親子ある場合は親のみ）
	 */
	private final String insNo;

	/**
	 * 特定施設個別計画のリスト
	 */
	private final List<SpecialInsPlan> specialInsPlanList;

	/**
	 * 特定施設個別計画の最終更新日時
	 */
	private final Date upDate;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 品目カテゴリ
	 */
	private final String prodCategory;

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo 施設を担当する従業員番号
	 * @param insMst 登録対象の施設コード（親子ある場合は親のみ）
	 * @param specialInsPlanList 特定施設個別計画のリスト
	 * @param upDate 最終更新日時
	 * @param sosCd3 組織コード(営業所)
	 */

	public SpecialInsPlanModifyDto(Integer jgiNo, String insNo, List<SpecialInsPlan> specialInsPlanList, Date upDate, String sosCd3) {
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.specialInsPlanList = specialInsPlanList;
		this.upDate = upDate;
		this.sosCd3 = sosCd3;
		this.prodCategory = null;
	}

	/**
	 * コンストラクタ
	 *
	 * @param parseInteger
	 * @param insNo2
	 * @param inputList
	 * @param upDate2
	 * @param sosCd32
	 * @param prodCategory
	 */
	public SpecialInsPlanModifyDto(Integer jgiNo, String insNo, List<SpecialInsPlan> specialInsPlanList, Date upDate, String sosCd3, String prodCategory) {
		this.jgiNo = jgiNo;
		this.insNo = insNo;
		this.specialInsPlanList = specialInsPlanList;
		this.upDate = upDate;
		this.sosCd3 = sosCd3;
		this.prodCategory = prodCategory;
	}

	/**
	 * 施設を担当する従業員番号を取得する。
	 *
	 * @return 施設を担当する従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 登録対象の施設コード（親子ある場合は親のみ）を取得する。
	 *
	 * @return 登録対象の施設コード（親子ある場合は親のみ）
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 特定施設個別計画のリストを取得する。
	 *
	 * @return 特定施設個別計画のリスト
	 */
	public List<SpecialInsPlan> getSpecialInsPlanList() {
		return specialInsPlanList;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
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
	 * 品目カテゴリを取得する。
	 *
	 * @return 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
