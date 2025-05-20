package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 組織・従業員検索の結果格納DTO
 * 
 * @author khashimoto
 */
public class SosJgiListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 選択した組織情報の階層リスト（MAXが選択組織）
	 */
	private final List<SosMst> selectList;

	/**
	 * 選択対象組織情報のリスト
	 */
	private final List<SosMst> sosMstList;

	/**
	 * 選択対象従業員情報のリスト
	 */
	private final List<JgiMst> jgiMstList;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param selectList 選択した組織情報の階層リスト（MAXが選択組織）
	 * @param sosMstList 選択対象組織情報のリスト
	 * @param jgiMstList 選択対象従業員情報のリスト
	 */
	public SosJgiListDto(List<SosMst> selectList, List<SosMst> sosMstList, List<JgiMst> jgiMstList) {
		this.selectList = selectList;
		this.sosMstList = sosMstList;
		this.jgiMstList = jgiMstList;
	}

	/**
	 * 選択した組織情報の階層リストを取得する。
	 * 
	 * @return 選択した組織情報の階層リスト
	 */
	public List<SosMst> getSelectList() {
		return selectList;
	}

	/**
	 * 選択対象組織情報のリストを取得する。
	 * 
	 * @return 選択対象組織情報のリスト
	 */
	public List<SosMst> getSosMstList() {
		return sosMstList;
	}

	/**
	 * 選択対象従業員情報のリストを取得する。
	 * 
	 * @return 選択対象従業員情報のリスト
	 */
	public List<JgiMst> getJgiMstList() {
		return jgiMstList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
