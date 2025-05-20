package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用特約店別計画立案ステータスチェック用DTO
 * 
 * @author nozaki
 */
public class WsStatusCheckForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しない特約店別計画立案スライドステータスのリスト
	 */
	private final List<WsSlideStatusForCheck> wsSlideStatusForCheck;

	/**
	 * コンストラクタ
	 * 
	 * チェック対象の計画対象品目のリストを指定する。
	 * 
	 * @param plannedProdList チェック対象の計画対象品目のリスト
	 * @param wsSlideStatusForCheck 許可しない特約店別計画立案スライドステータス
	 */
	public WsStatusCheckForVacDto(List<PlannedProd> plannedProdList, List<WsSlideStatusForCheck> wsSlideStatusForCheck) {
		this.plannedProdList = plannedProdList;
		this.wsSlideStatusForCheck = wsSlideStatusForCheck;
	}

	/**
	 * チェック対象の計画対象品目のリストを取得する。
	 * 
	 * @return チェック対象の計画対象品目のリスト
	 */
	public List<PlannedProd> getPlannedProdList() {
		return plannedProdList;
	}

	/**
	 * 許可しない特約店別計画立案スライドステータスのリストを取得する。
	 * 
	 * @return 許可しない特約店別計画立案スライドステータスのリスト
	 */
	public List<WsSlideStatusForCheck> getWsSlideStatusForCheck() {
		return wsSlideStatusForCheck;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
