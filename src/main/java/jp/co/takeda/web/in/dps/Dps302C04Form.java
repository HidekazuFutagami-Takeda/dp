package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.PlannedProdForVacResultDto;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps302C04((ワ)計画対象品目選択画面)のフォームクラス
 * 
 * @author stakeuchi
 */
public class Dps302C04Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS302C04_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps302C04Form.class, PlannedProdForVacResultDto.class);

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
	}
}
