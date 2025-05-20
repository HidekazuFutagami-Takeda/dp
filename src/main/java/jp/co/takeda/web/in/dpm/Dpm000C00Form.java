package jp.co.takeda.web.in.dpm;

import java.util.Calendar;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.ChoseiDataParamsDto;
import jp.co.takeda.dto.ChoseiDataResultDto;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dpm000C00(トップ画面)のフォームクラス
 * 
 * @author tkawabata
 */
public class Dpm000C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPM000C00F25_DATA_R(調整金額取込日時)
	 */
	public static final BoxKey DPM000C00_DATE_DATA_R = new BoxKeyPerClassImpl(Dpm000C00Form.class, Calendar.class);

	/**
	 * DPM000C00F25_PARAMS_S(組織一覧検索条件)
	 */
	public static final BoxKey DPM000C00F25_PARAMS_S = new BoxKeyPerClassImpl(Dpm000C00Form.class, ChoseiDataParamsDto.class);

	/**
	 * DPM000C00F25_DATA_R(組織一覧)
	 */
	public static final BoxKey DPM000C00F25_DATA_R = new BoxKeyPerClassImpl(Dpm000C00Form.class, ChoseiDataResultDto.class);

	// -------------------------------
	// field
	// -------------------------------

	/**
	 * 切替従業員番号
	 */
	private String applyJgiNo;

	/**
	 * 組織一覧切替時の組織コード(5桁)
	 */
	private String targetSosCd;

	/**
	 * 組織一覧切替時の対象組織の部門ランク
	 */
	private String targetBumonRank;

	/**
	 * 他画面に転送する際の組織コード
	 */
	private String dispatchSosCd;

	/**
	 * 他画面に転送する際の従業員番号
	 */
	private String dispatchJgiNo;

	// -------------------------------
	// Utility
	// -------------------------------

	/**
	 * フォームを更新する。
	 */
	public void reflash() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		Integer jgiNo = dpUserInfo.getSettingUserJgiNo();
		if (jgiNo != null) {
			this.applyJgiNo = jgiNo.toString();
		} else {
			this.applyJgiNo = null;
		}
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	/**
	 * 切替従業員番号を取得する。
	 * 
	 * @return 切替従業員番号
	 */
	public String getApplyJgiNo() {
		return applyJgiNo;
	}

	/**
	 * 切替従業員番号を設定する。
	 * 
	 * @param applyJgiNo 切替従業員番号
	 */
	public void setApplyJgiNo(String applyJgiNo) {
		this.applyJgiNo = applyJgiNo;
	}

	/**
	 * 組織一覧切替時の組織コード(5桁)を取得する。
	 * 
	 * @return 組織一覧切替時の組織コード(5桁)
	 */
	public String getTargetSosCd() {
		return targetSosCd;
	}

	/**
	 * 組織一覧切替時の組織コード(5桁)を設定する。
	 * 
	 * @param targetSosCd 組織一覧切替時の組織コード(5桁)
	 */
	public void setTargetSosCd(String targetSosCd) {
		this.targetSosCd = targetSosCd;
	}

	/**
	 * 織一覧切替時の対象組織の部門ランクを取得する。
	 * 
	 * @return 織一覧切替時の対象組織の部門ランク
	 */
	public String getTargetBumonRank() {
		return targetBumonRank;
	}

	/**
	 * 織一覧切替時の対象組織の部門ランクを設定する。
	 * 
	 * @param targetBumonRank 織一覧切替時の対象組織の部門ランク
	 */
	public void setTargetBumonRank(String targetBumonRank) {
		this.targetBumonRank = targetBumonRank;
	}

	/**
	 * 他画面に転送する際の組織コードを取得する。
	 * 
	 * @return 他画面に転送する際の組織コード
	 */
	public String getDispatchSosCd() {
		return dispatchSosCd;
	}

	/**
	 * 他画面に転送する際の組織コードを設定する。
	 * 
	 * @param dispatchSosCd 他画面に転送する際の組織コード
	 */
	public void setDispatchSosCd(String dispatchSosCd) {
		this.dispatchSosCd = dispatchSosCd;
	}

	/**
	 * 他画面に転送する際の従業員番号を取得する。
	 * 
	 * @return 他画面に転送する際の従業員番号
	 */
	public String getDispatchJgiNo() {
		return dispatchJgiNo;
	}

	/**
	 * 他画面に転送する際の従業員番号を設定する。
	 * 
	 * @param dispatchJgiNo 他画面に転送する際の従業員番号
	 */
	public void setDispatchJgiNo(String dispatchJgiNo) {
		this.dispatchJgiNo = dispatchJgiNo;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理につき、初期化不要
	}
}
