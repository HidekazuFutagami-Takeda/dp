package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.dao.div.SearchInsType;
import jp.co.takeda.dao.div.SpecialInsPlanRegType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 登録済み特定施設個別計画の検索条件(Search Condition)を表すDTO
 * 
 * @author khashimoto
 */
public class SpecialInsPlanScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード（営業所）
	 */
	private final String sosCd3;

	/**
	 * 組織コード（チーム）
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 検索用対象区分
	 */
	private final SearchInsType searchInsType;

	/**
	 * 特定施設個別計画の絞込条件
	 */
	private final SpecialInsPlanRegType specialInsPlanRegType;

	/**
	 * 施設名(全角)
	 */
	private final String insFormalName;

	/**
	 * 施設名(半角カナ)
	 */
	private final String insKanaSrch;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param searchInsType 検索用対象区分
	 * @param specialInsPlanRegType 特定施設個別計画の絞込条件
	 * @param insFormalName 施設名(全角)
	 * @param insKanaSrch 施設名(半角カナ)
	 */
	public SpecialInsPlanScDto(String sosCd3, String sosCd4, Integer jgiNo, SearchInsType searchInsType, SpecialInsPlanRegType specialInsPlanRegType, String insFormalName,
		String insKanaSrch) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.searchInsType = searchInsType;
		this.specialInsPlanRegType = specialInsPlanRegType;
		this.insFormalName = insFormalName;
		this.insKanaSrch = insKanaSrch;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 * 
	 * @return 組織コード（営業所）
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 * 
	 * @return 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 検索用対象区分を取得する。
	 * 
	 * @return 検索用対象区分
	 */
	public SearchInsType getSearchInsType() {
		return searchInsType;
	}

	/**
	 * 特定施設個別計画の絞込条件を取得する。
	 * 
	 * @return 特定施設個別計画の絞込条件
	 */
	public SpecialInsPlanRegType getSpecialInsPlanRegType() {
		return specialInsPlanRegType;
	}

	/**
	 * 施設名(全角)を取得する。
	 * 
	 * @return 施設名(全角)
	 */
	public String getInsFormalName() {
		return insFormalName;
	}

	/**
	 * 施設名(半角カナ)を取得する。
	 * 
	 * @return 施設名(半角カナ)
	 */
	public String getInsKanaSrch() {
		return insKanaSrch;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
