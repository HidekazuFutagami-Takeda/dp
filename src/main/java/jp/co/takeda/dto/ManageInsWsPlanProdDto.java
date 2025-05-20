package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (医)施設特約店品目別計画編集画面を表すDTO
 *
 * @author siwamoto
 */
public class ManageInsWsPlanProdDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * カテゴリ名
	 */
	private final String prodCategoryName;

	/**
	 * 検索結果リスト
	 */
	private final List<ManageInsWsPlanProdDetailDto> detailList;

	/**
	 * 登録可能フラグ
	 * <ul>
	 * <li>TRUE = 登録可能</li>
	 * <li>FALSE = 登録可能でない</li>
	 * </ul>
	 */
	private final boolean enableEntry;

	/**
	 * コンストラクタ
	 *
	 * @param insNo 施設コード
	 * @param tmsTytenCd 特約店コード
	 * @param prodCategory カテゴリ名
	 * @param detailList 検索結果リスト
	 * @param enableEntry 登録可能フラグ
	 */
	public ManageInsWsPlanProdDto(String insNo, String tmsTytenCd, String prodCategoryName, List<ManageInsWsPlanProdDetailDto> detailList, boolean enableEntry) {
		this.insNo = insNo;
		this.tmsTytenCd = tmsTytenCd;
		this.prodCategoryName = prodCategoryName;
		this.detailList = detailList;
		this.enableEntry = enableEntry;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 品目カテゴリ名を取得する。
	 *
	 * @return 品目カテゴリ名
	 */
	public String getProdCategoryName() {
		return prodCategoryName;
	}

	/**
	 * 検索結果リストを取得する。
	 *
	 * @return 検索結果リスト
	 */
	public List<ManageInsWsPlanProdDetailDto> getDetailList() {
		return detailList;
	}

	/**
	 * 登録可能フラグ を取得する。
	 *
	 * @return 登録可能フラグ
	 */
	public boolean getEnableEntry() {
		return enableEntry;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
