package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.TeamInputStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * チーム別入力状況チェック用DTO
 * 
 * @author nozaki
 */
public class TeamInputStatusCheckDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * チェック対象のチームの組織情報
	 */
	private final SosMst sosMst;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しないチーム別入力状況のリスト
	 */
	private final List<TeamInputStatusForCheck> mrStatusForCheckList;

	/**
	 * コンストラクタ
	 * 
	 * チェック対象のチームの組織情報、計画対象品目のリストを指定する。
	 * 
	 * @param sosMst チェック対象のチームの組織情報
	 * @param plannedProdList チェック対象の計画対象品目のリスト
	 * @param statusForOffice 許可しないチーム別入力状況
	 */
	public TeamInputStatusCheckDto(SosMst sosMst, List<PlannedProd> plannedProdList, List<TeamInputStatusForCheck> mrStatusForCheckList) {

		this.sosMst = sosMst;
		this.plannedProdList = plannedProdList;
		this.mrStatusForCheckList = mrStatusForCheckList;
	}

	/**
	 * チェック対象のチームの組織情報を取得する。
	 * 
	 * @return チェック対象のチームの組織情報
	 */
	public SosMst getSosMst() {
		return sosMst;
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
	 * 許可しないチーム別入力状況のリストを取得する。
	 * 
	 * @return 許可しないチーム別入力状況のリスト
	 */
	public List<TeamInputStatusForCheck> getTeamInputStatusForCheck() {
		return mrStatusForCheckList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
