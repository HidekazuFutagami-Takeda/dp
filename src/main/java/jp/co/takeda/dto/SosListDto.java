package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.SosMst;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 組織検索の結果格納DTO
 * 
 * @author khashimoto
 */
public class SosListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 選択対象組織情報のリスト
	 */
	private final List<SosMst> sosMstList;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param sosMstList 選択対象組織情報のリスト
	 */
	public SosListDto(List<SosMst> sosMstList) {
		this.sosMstList = sosMstList;
	}

	/**
	 * 選択対象組織情報のリストを取得する。
	 * 
	 * @return 選択対象組織情報のリスト
	 */
	public List<SosMst> getSosMstList() {
		return sosMstList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
