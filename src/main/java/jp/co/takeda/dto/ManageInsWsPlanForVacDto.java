package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (医)施設特約店別計画編集画面を表すDTO
 * 
 * @author siwamoto
 */
public class ManageInsWsPlanForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * T/Y変換率（合計行）
	 */
	private final Double tyChangeRate;

	/**
	 * 検索結果リスト
	 */
	private final List<ManageInsWsPlanForVacDetailAddrDto> detailList;

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
	 * @param tbChangeRate T/B変換率（合計行）
	 * @param insWsPlanAdminDtoList 検索結果リスト
	 * @param enableEntry 登録可能フラグ
	 */
	public ManageInsWsPlanForVacDto(Double tyChangeRate, List<ManageInsWsPlanForVacDetailAddrDto> detailList, boolean enableEntry) {
		this.tyChangeRate = tyChangeRate;
		this.detailList = detailList;
		this.enableEntry = enableEntry;
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
	public List<ManageInsWsPlanForVacDetailAddrDto> getDetailList() {
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
