package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.StatusCheckBumon;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画立案ステータスチェック用DTO
 * 
 * @author nozaki
 */
public class InsWsStatusCheckDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * チェック対象の部門ランク(営業所、チーム、担当者)
	 */
	private final StatusCheckBumon statusCheckBumon;

	/**
	 * チェック対象の組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * チェック対象の組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * チェック対象の従業員情報のリスト
	 */
	private final List<JgiMst> jgiMstList;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しない施設特約店別計画立案ステータスのリスト
	 */
	private final List<InsWsStatusForCheck> insWsStatusForCheckList;

	/**
	 * コンストラクタ <br>
	 * 組織コード(営業所)、計画対象品目を指定する。
	 * 
	 * @param sosCd3 チェック対象の組織コード(営業所)
	 * @param jgiMstList チェック対象の従業員情報
	 * @param plannedProdList チェック対象の計画対象品目
	 * @param insWsStatusForCheckList 許可しない施設特約店別計画立案ステータス
	 */
	public InsWsStatusCheckDto(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> insWsStatusForCheckList) {

		this.statusCheckBumon = StatusCheckBumon.OFFICE_TOKUYAKUTEN_G;
		this.sosCd3 = sosCd3;
		this.jgiMstList = jgiMstList;
		this.plannedProdList = plannedProdList;
		this.insWsStatusForCheckList = insWsStatusForCheckList;

		this.sosCd4 = null;
	}

	/**
	 * コンストラクタ <br>
	 * 組織コード(営業所)、組織コード(チーム)、計画対象品目を指定する。
	 * 
	 * @param sosCd3 チェック対象の組織コード(営業所)
	 * @param sosCd4 チェック対象の組織コード(チーム)
	 * @param jgiMstList チェック対象の従業員情報
	 * @param plannedProdList チェック対象の計画対象品目
	 * @param insWsStatusForCheckList 許可しない施設特約店別計画立案ステータス
	 */
	public InsWsStatusCheckDto(String sosCd3, String sosCd4, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> insWsStatusForCheckList) {

		this.statusCheckBumon = StatusCheckBumon.TEAM;
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiMstList = jgiMstList;
		this.plannedProdList = plannedProdList;
		this.insWsStatusForCheckList = insWsStatusForCheckList;

	}

	/**
	 * コンストラクタ <br>
	 * 従業員情報、計画対象品目を指定する。
	 * 
	 * @param jgiMstList チェック対象の従業員情報
	 * @param plannedProdList チェック対象の計画対象品目
	 * @param insWsStatusForCheckList 許可しない施設特約店別計画立案ステータス
	 */
	public InsWsStatusCheckDto(List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> insWsStatusForCheckList) {

		this.statusCheckBumon = StatusCheckBumon.MR;
		this.jgiMstList = jgiMstList;
		this.plannedProdList = plannedProdList;
		this.insWsStatusForCheckList = insWsStatusForCheckList;

		this.sosCd3 = null;
		this.sosCd4 = null;
	}

	/**
	 * チェック対象の部門ランク(営業所、チーム、担当者)を取得する。
	 * 
	 * @return チェック対象の部門ランク(営業所、チーム、担当者)
	 */
	public StatusCheckBumon getStatusCheckBumon() {
		return statusCheckBumon;
	}

	/**
	 * チェック対象の組織コード(営業所)を取得する。
	 * 
	 * @return チェック対象の組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * チェック対象の組織コード(チーム)を取得する。
	 * 
	 * @return チェック対象の組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * チェック対象の従業員情報を取得する。
	 * 
	 * @return チェック対象の従業員情報
	 */
	public List<JgiMst> getJgiMstList() {
		return jgiMstList;
	}

	/**
	 * チェック対象の計画対象品目を取得する。
	 * 
	 * @return チェック対象の計画対象品目
	 */
	public List<PlannedProd> getPlannedProdList() {
		return plannedProdList;
	}

	/**
	 * 許可しない施設特約店別計画立案ステータスのリストを取得する。
	 * 
	 * @return 許可しない施設特約店別計画立案ステータスのリスト
	 */
	public List<InsWsStatusForCheck> getInsWsStatusForCheck() {
		return insWsStatusForCheckList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
