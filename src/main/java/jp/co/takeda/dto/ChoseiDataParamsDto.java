package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 調整金額テーブルの検索条件を表すＤＴＯクラス
 * 
 * @author tkawabata
 */
public class ChoseiDataParamsDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 展開する組織コード/部門ランクのリスト
	 */
	private final List<ChoseiData> openSosCdList;

	/**
	 * コンストラクタ
	 * 
	 * @param openSosCdList 展開する組織コードのリスト
	 */
	public ChoseiDataParamsDto(List<ChoseiData> openSosCdList) {
		this.openSosCdList = openSosCdList;
	}

	// --------------------------------
	// GETTER METHOD
	// --------------------------------

	/**
	 * 展開する組織コードのリストを取得する。
	 * 
	 * @return 展開する組織コードのリスト
	 */
	public List<ChoseiData> getOpenSosCdList() {
		return openSosCdList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
