package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 市区郡検索画面の検索結果用DTO
 * 
 * @author siwamoto
 */
public class AddrSearchResultListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果リスト
	 */
	private final List<AddrSearchResultDto> addrSearchResultList;

	/**
	 * コンストラクタ
	 * 
	 * @param addrSearchResultList 検索結果リスト
	 */
	public AddrSearchResultListDto(List<AddrSearchResultDto> addrSearchResultList) {
		super();
		this.addrSearchResultList = addrSearchResultList;
	}

	/**
	 * 検索結果リストを取得する。
	 * 
	 * @return addrSearchResultList 検索結果リスト
	 */
	public List<AddrSearchResultDto> getAddrSearchResultList() {
		return addrSearchResultList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
