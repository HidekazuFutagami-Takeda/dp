package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.SpecialInsPlanSosSummary;

/**
 * 特定施設個別計画の組織、対象区分集約を取得するDAOインターフェイス
 * 
 * @author nozaki
 */
public interface SpecialInsPlanSosSummaryDao {

	/**
	 * 営業所組織コード、品目固定コード、対象区分で特定施設個別計画を集約し取得する。<br>
	 * ソート順は営業所組織コード、品目固定コード、対象区分の昇順
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode 品目固定コード
	 * @return 特定施設個別計画の組織、対象区分集約のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlanSosSummary> searchSosSummaryList(String sosCd3, String prodCode) throws DataNotFoundException;

	/**
	 * チーム組織コード、品目固定コード、対象区分で特定施設個別計画を集約し取得する。<br>
	 * ソート順は組織コード、品目固定コード、対象区分の昇順
	 * 
	 * @param sosCd4 組織コード(チーム)
	 * @param prodCode 品目固定コード
	 * @return 特定施設個別計画の組織、対象区分集約のリスト
	 * @throws DataNotFoundException
	 */
	List<SpecialInsPlanSosSummary> searchTeamSummaryList(String sosCd4, String prodCode) throws DataNotFoundException;
}
