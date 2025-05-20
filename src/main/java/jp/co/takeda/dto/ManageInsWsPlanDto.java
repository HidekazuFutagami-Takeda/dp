package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (医)施設特約店別計画編集画面を表すDTO
 * 
 * @author siwamoto
 */
public class ManageInsWsPlanDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * T/Y変換率（合計行）
	 */
	private final Double tyChangeRate;

	/**
	 * 検索結果リスト
	 */
	private final List<ManageInsWsPlanDetailInsDto> detailList;

	/**
	 * 登録可能フラグ
	 * <ul>
	 * <li>TRUE = 登録可能</li>
	 * <li>FALSE = 登録可能でない</li>
	 * </ul>
	 */
	private final boolean enableEntry;

	/**
	 * 指定されている担当者が自分の組織か
	 * <ul>
	 * <li>TRUE = 自分の組織配下</li>
	 * <li>FALSE = 自分の組織配下ではない</li>
	 * </ul>
	 */
	private final boolean mySosMr;

	/**
	 * コンストラクタ
	 * 
	 * @param prodName 品目名称
	 * @param tyChangeRate T/Y変換率（合計行）
	 * @param insWsPlanAdminDtoList 検索結果リスト
	 * @param enableEntry 登録可能フラグ
	 */
	public ManageInsWsPlanDto(String prodName, Double tyChangeRate, List<ManageInsWsPlanDetailInsDto> detailList, boolean enableEntry,boolean mySosMr) {
		this.prodName = prodName;
		this.tyChangeRate = tyChangeRate;
		this.detailList = detailList;
		this.enableEntry = enableEntry;
		this.mySosMr = mySosMr;
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
	 * T/Y変換率（合計行）を取得する。
	 * 
	 * @return tyChangeRate T/Y変換率（合計行）
	 */
	public Double getTyChangeRate() {
		return tyChangeRate;
	}

	/**
	 * 検索結果リストを取得する。
	 * 
	 * @return 検索結果リスト
	 */
	public List<ManageInsWsPlanDetailInsDto> getDetailList() {
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

	/**
	 * 指定されている担当者が自分の組織か
	 * 
	 * @return 指定されている担当者が自分の組織か
	 */
	public boolean isMySosMr() {
		return mySosMr;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
