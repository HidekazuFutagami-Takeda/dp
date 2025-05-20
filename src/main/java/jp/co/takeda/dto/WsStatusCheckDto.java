package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;

/**
 * 特約店別計画立案ステータスチェック用DTO
 *
 * @author nozaki
 */
public class WsStatusCheckDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * チェック対象の支店組織情報のリスト
	 */
	private final List<SosMst> sosMstList;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * チェック対象のカテゴリ
	 */
//	private final ProdCategory prodCategory;

	private final String prodCategory;

	/**
	 * 許可しない特約店別計画立案配分ステータスのリスト
	 */
	private final List<WsDistStatusForCheck> wsDistStatusForCheck;

	/**
	 * 許可しない特約店別計画立案スライドステータスのリスト
	 */
	private final List<WsSlideStatusForCheck> wsSlideStatusForCheck;

	/**
	 * コンストラクタ
	 *
	 * チェック対象の支店の組織情報、計画対象品目のリストを指定する。
	 *
	 * @param sosMstList チェック対象の支店組織情報のリスト
	 * @param plannedProdList チェック対象の計画対象品目のリスト
	 * @param wsDistStatusForCheck 許可しない特約店別計画立案配分ステータス
	 * @param wsSlideStatusForCheck 許可しない特約店別計画立案スライドステータス
	 */
	public WsStatusCheckDto(List<SosMst> sosMstList, List<PlannedProd> plannedProdList, List<WsDistStatusForCheck> wsDistStatusForCheck,
		List<WsSlideStatusForCheck> wsSlideStatusForCheck) {

		this.sosMstList = sosMstList;
		this.plannedProdList = plannedProdList;
		this.wsDistStatusForCheck = wsDistStatusForCheck;
		this.wsSlideStatusForCheck = wsSlideStatusForCheck;
		this.prodCategory = null;
	}

	/**
	 * コンストラクタ
	 *
	 * チェック対象の支店の組織情報、計画対象品目のリスト、計画対象品目のカテゴリを指定する。
	 *
	 * @param sosMstList チェック対象の支店組織情報のリスト
	 * @param plannedProdList チェック対象の計画対象品目のリスト
	 * @param plannedProdList チェック対象の計画対象品目のリスト
	 * @param wsDistStatusForCheck 許可しない特約店別計画立案配分ステータス
	 * @param wsSlideStatusForCheck 許可しない特約店別計画立案スライドステータス
	 */
//	public WsStatusCheckDto(List<SosMst> sosMstList, List<PlannedProd> plannedProdList, ProdCategory prodCategory, List<WsDistStatusForCheck> wsDistStatusForCheck,
	public WsStatusCheckDto(List<SosMst> sosMstList, List<PlannedProd> plannedProdList, String prodCategory, List<WsDistStatusForCheck> wsDistStatusForCheck,
			List<WsSlideStatusForCheck> wsSlideStatusForCheck) {
		this.sosMstList = sosMstList;
		this.plannedProdList = plannedProdList;
		this.wsDistStatusForCheck = wsDistStatusForCheck;
		this.wsSlideStatusForCheck = wsSlideStatusForCheck;
		this.prodCategory = prodCategory;
	}

	/**
	 * チェック対象の支店組織情報を取得する。
	 *
	 * @return チェック対象の支店組織情報のリスト
	 */
	public List<SosMst> getSosMstList() {
		return sosMstList;
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
	 * チェック対象のカテゴリを取得する。
	 *
	 * @return チェック対象のカテゴリ
	 */
//	public ProdCategory getProdCategory() {
//		return prodCategory;
//	}
	public String getProdCategory() {
		return prodCategory;
	}


	/**
	 * 許可しない特約店別計画立案配分ステータスのリストを取得する。
	 *
	 * @return 許可しない特約店別計画立案配分ステータスのリスト
	 */
	public List<WsDistStatusForCheck> getWsDistStatusForCheck() {
		return wsDistStatusForCheck;
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
