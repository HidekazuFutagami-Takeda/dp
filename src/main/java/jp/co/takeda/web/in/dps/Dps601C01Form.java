package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.InsDocPlanProdListScDto;
import jp.co.takeda.dto.InsDocPlanProdListUpdateDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dps601C01((医)施設医師別計画品目一覧画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dps601C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS601C01_DATA_R
	 */
	public static final BoxKey DPS601C01_DATA_R = new BoxKeyPerClassImpl(Dps601C01Form.class, HashMap.class);

	/**
	 * データ有無フラグ(TRUE：データあり、FLASE：データなし)
	 */
	public static final BoxKey DATA_EXIST = new BoxKeyPerClassImpl(Dps601C01Form.class, Boolean.class);

	/**
	 * 副担当編集モード(TRUE：副担当モード、FLASE：主担当（通常）モード)
	 */
	public static final BoxKey SUB_MR_MODE = new BoxKeyPerClassImpl(Dps601C01Form.class, Integer.class);

	/**
	 * (医)施設医師別計画(自)
	 */
	public static final DpAuthority EDIT_AUTH_MMP_PLAN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設医師別計画(自)担当者再配分
	 */
	public static final DpAuthority EDIT_AUTH_MMP_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設医師別計画(自)公開
	 */
	public static final DpAuthority EDIT_AUTH_MMP_OPEN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設医師別計画(自)確定
	 */
	public static final DpAuthority EDIT_AUTH_MMP_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return PlannedProdSpecialInsPlanScDto 変換されたScDto
	 */
	public InsDocPlanProdListScDto convertInsDocPlanProdListScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}

		// 従業員コード
		Integer _jgiNo = null;
		if (ValidationUtil.isInteger(jgiNoTran)) {
			_jgiNo = new Integer(jgiNoTran);
		}

		return new InsDocPlanProdListScDto(sosCd3Tran, sosCd4Tran, _jgiNo);
	}

	/**
	 * ActionForm ⇒ upDateDto 変換処理
	 *
	 * @return InsDocPlanProdListUpdateDto 変換されたUpDateDto
	 */
	public List<InsDocPlanProdListUpdateDto> convertInsDocPlanProdListUpdateDtoList() throws Exception {

		// 更新用DTOのリスト作成
		List<InsDocPlanProdListUpdateDto> updateDtoList = new ArrayList<InsDocPlanProdListUpdateDto>();
		if (selectRowIdList != null) {

			for (int i = 0; i < selectRowIdList.length; i++) {

				if (selectRowIdList[i] == null) {
					continue;
				}

				String _prodCode = null;
				String _prodName = null;
				Date _lastUpDate = null;

				String[] rowId = ConvertUtil.splitConmma(selectRowIdList[i]);

				// 品目固定コード
				if (!StringUtils.isEmpty(rowId[0])) {
					_prodCode = rowId[0];
				}
				// 品目名称
				if (!StringUtils.isEmpty(rowId[1])) {
					_prodName = rowId[1];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[2])) {
					_lastUpDate = new Date(Long.parseLong(rowId[2]));
				}

				InsDocPlanProdListUpdateDto updateDto = new InsDocPlanProdListUpdateDto(_prodCode, _prodName, _lastUpDate);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	/**
	 * 検索条件を処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 従業員コード
	 */
	private String jgiNo;

	/**
	 * 計画クリアフラグ
	 * <ul>
	 * <li>TRUE =クリアする</li>
	 * <li>FALSE=クリアしない</li>
	 * </ul>
	 */
	private boolean doPlanClear;

	/**
	 * ユーザの部門ランク<br>
	 * 1：営業所以上 2：AL 3：MR
	 */
	private String rank;

	/**
	 * 医師別計画ステータス最終更新日
	 */
	private String statusLastUpdate;

	/**
	 * 選択行の識別IDリスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=従業員コード
	 * Sprit文字列[1]=ステータス最終更新日時(Date型のgetTime()の値)
	 * (例)---------------------------------------
	 * selectRowIdList[0] = "12345,1234954688000"
	 * selectRowIdList[1] = "22432,1122843578000"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] selectRowIdList;

	// 処理用フィールド
	/**
	 * 処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 処理用組織コード(チーム)
	 */
	private String sosCd4Tran;

	/**
	 * 処理用従業員番号
	 */
	private String jgiNoTran;

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 組織コード(チーム)を設定する。
	 *
	 * @param sosCd4 組織コード(チーム)
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

	/**
	 * 従業員コードを取得する。
	 *
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員コードを設定する。
	 *
	 * @param jgiNo
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 計画クリアフラグ を取得する。
	 *
	 * @return 計画クリアフラグ
	 */
	public boolean isDoPlanClear() {
		return doPlanClear;
	}

	/**
	 * 計画クリアフラグ を設定する。
	 *
	 * @return doPlanClear 計画クリアフラグ
	 */
	public void setDoPlanClear(boolean doPlanClear) {
		this.doPlanClear = doPlanClear;
	}

	/**
	 * 選択行の識別IDリストを取得する。
	 *
	 * @return selectRowIdList 選択行の識別IDリスト
	 */
	public String[] getSelectRowIdList() {
		return selectRowIdList;
	}

	/**
	 * 選択行の識別IDリストを設定する。
	 *
	 * @param selectRowIdList 選択行の識別IDリスト
	 */
	public void setSelectRowIdList(String[] selectRowIdList) {
		this.selectRowIdList = selectRowIdList;
	}

	/**
	 * 部門ランクを取得する。<br>
	 * 1：営業所以上 2：AL 3：MR
	 *
	 * @return 部門ランク
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * 部門ランクを設定する。<br>
	 * 1：営業所以上 2：AL 3：MR
	 *
	 * @param rank 部門ランク
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * 医師別計画ステータス最終更新日 を取得する。
	 * 
	 * @return 医師別計画ステータス最終更新日
	 */
	public String getStatusLastUpdate() {
		return statusLastUpdate;
	}

	/**
	 * 医師別計画ステータス最終更新日 を設定する。
	 * @param statusLastUpdate 医師別計画ステータス最終更新日
	 */
	public void setStatusLastUpdate(String statusLastUpdate) {
		this.statusLastUpdate = statusLastUpdate;
	}

	/**
	 * 処理用組織コード(営業所)を取得する。
	 *
	 * @return 処理用組織コード(営業所)
	 */
	public String getSosCd3Tran() {
		return sosCd3Tran;
	}

	/**
	 * 処理用組織コード(営業所)を設定する。
	 *
	 * @param sosCd3Tran 処理用組織コード(営業所)
	 */
	public void setSosCd3Tran(String sosCd3Tran) {
		this.sosCd3Tran = sosCd3Tran;
	}

	/**
	 * 処理用組織コード(チーム)を取得する。
	 *
	 * @return 処理用組織コード(チーム)
	 */
	public String getSosCd4Tran() {
		return sosCd4Tran;
	}

	/**
	 * 処理用組織コード(チーム)を設定する。
	 *
	 * @param sosCd4Tran 処理用組織コード(チーム)
	 */
	public void setSosCd4Tran(String sosCd4Tran) {
		this.sosCd4Tran = sosCd4Tran;
	}

	/**
	 * 処理用従業員番号を取得する。
	 *
	 * @return 処理用従業員番号
	 */
	public String getJgiNoTran() {
		return jgiNoTran;
	}

	/**
	 * 処理用従業員番号を設定する。
	 *
	 * @param jgiNoTran 処理用従業員番号
	 */
	public void setJgiNoTran(String jgiNoTran) {
		this.jgiNoTran = jgiNoTran;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.sosCd4 = null;
		this.jgiNo = null;
		this.rank = null;
		this.selectRowIdList = null;
		this.sosCd3Tran = null;
		this.sosCd4Tran = null;
		this.jgiNoTran = null;
		this.doPlanClear = false;
		this.statusLastUpdate = null;
	}
	
	@Override
	public void reset() {
		this.doPlanClear = false;
	}
}
