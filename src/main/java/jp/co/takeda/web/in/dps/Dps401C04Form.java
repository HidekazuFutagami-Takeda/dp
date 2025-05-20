package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.InsWsPlanForVacProdListResultDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListScDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListUpdateDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

import org.apache.commons.lang.StringUtils;

/**
 * Dps401C04((ワ)施設特約店別計画品目一覧画面)のフォームクラス
 * 
 * @author nozaki
 */
public class Dps401C04Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ファイル名
	 */
	protected String downloadFileName;

	@Override
	public ContentsType getContentType() {
		return ContentsType.XLS;
	}

	@Override
	public String getDownLoadFileName() {
		return downloadFileName;
	}

	@Override
	public int getContentLength() {
		return Downloadable.DEF_LENGTH;
	}

	/**
	 * DPS401C04_DATA_R
	 */
	public static final BoxKey DPS401C04_DATA_R = new BoxKeyPerClassImpl(Dps401C04Form.class, InsWsPlanForVacProdListResultDto.class);

	/**
	 * データ有無フラグ(TRUE：データあり、FLASE：データなし)
	 */
	public static final BoxKey DATA_EXIST = new BoxKeyPerClassImpl(Dps401C04Form.class, Boolean.class);

	/**
	 * (ワ)施設特約店別計画
	 */
	public static final DpAuthority EDIT_AUTH_PLAN = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画担当者再配分
	 */
	public static final DpAuthority EDIT_AUTH_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画公開
	 */
	public static final DpAuthority EDIT_AUTH_OPEN = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画確定
	 */
	public static final DpAuthority EDIT_AUTH_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画確定解除
	 */
	public static final DpAuthority EDIT_AUTH_NONFIX = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 * 
	 * @return 変換されたScDto
	 */
	public InsWsPlanForVacProdListScDto convertInsWsPlanForVacProdListScDto() throws Exception {

		// 組織コード(エリア特約店G)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 従業員番号
		Integer _jgiNo = null;
		if (ValidationUtil.isInteger(jgiNoTran)) {
			_jgiNo = new Integer(jgiNoTran);
		}

		return new InsWsPlanForVacProdListScDto(sosCd3Tran, _jgiNo);
	}

	/**
	 * ActionForm ⇒ upDateDto 変換処理
	 * 
	 * @return 変換されたUpDateDto
	 */
	public List<InsWsPlanForVacProdListUpdateDto> convertInsWsPlanForVacProdListUpdateDto() throws Exception {

		// 更新用DTOのリスト作成
		List<InsWsPlanForVacProdListUpdateDto> updateDtoList = new ArrayList<InsWsPlanForVacProdListUpdateDto>();
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

				InsWsPlanForVacProdListUpdateDto updateDto = new InsWsPlanForVacProdListUpdateDto(_prodCode, _prodName, _lastUpDate);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	/**
	 * 検索条件を処理用フィールドに設定
	 * 
	 * 
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.jgiNoTran = this.jgiNo;
		this.prodCodeTran = this.prodCode;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(エリア特約店G)
	 */
	private String sosCd3;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * ユーザの部門ランク<br>
	 * <br>
	 * 1：本部・本部ワクチンG<br>
	 * 2：特約店部長～特約店業務G<br>
	 * 3：小児科MR (J19-0010 対応・コメントのみ修正)<br>
	 */
	private String rank;

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
	 * 処理用組織コード(エリア特約店G)
	 */
	private String sosCd3Tran;

	/**
	 * 処理用従業員番号
	 */
	private String jgiNoTran;

	/**
	 * 処理用品目固定コード
	 */
	private String prodCodeTran;

	/**
	 * ダウンロードファイル名を設定する。
	 * 
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	/**
	 * 組織コード(エリア特約店G)を取得する。
	 * 
	 * @return sosCd3 組織コード(エリア特約店G)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(エリア特約店G)を設定する。
	 * 
	 * @param sosCd3 組織コード(エリア特約店G)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 * 
	 * @param jgiNo
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 * 
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 選択行の識別IDリストを取得する。
	 * 
	 * @return 選択行の識別IDリスト
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
	 * <br>
	 * 1：本部・本部ワクチンG<br>
	 * 2：特約店部長～特約店業務G<br>
	 * 3：小児科MR (J19-0010 対応・コメントのみ修正)<br>
	 * 
	 * @return 部門ランク
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * 部門ランクを設定する。<br>
	 * <br>
	 * 1：本部・本部ワクチンG<br>
	 * 2：特約店部長～特約店業務G<br>
	 * 3：小児科MR (J19-0010 対応・コメントのみ修正)<br>
	 * 
	 * @param rank 部門ランク
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * 処理用組織コード(エリア特約店G)を取得する。
	 * 
	 * @return 処理用組織コード(エリア特約店G)
	 */
	public String getSosCd3Tran() {
		return sosCd3Tran;
	}

	/**
	 * 処理用組織コード(エリア特約店G)を設定する。
	 * 
	 * @param sosCd3Tran 処理用組織コード(エリア特約店G)
	 */
	public void setSosCd3Tran(String sosCd3Tran) {
		this.sosCd3Tran = sosCd3Tran;
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

	/**
	 * 処理用品目固定コードを取得する。
	 * 
	 * @return 処理用品目固定コード
	 */
	public String getProdCodeTran() {
		return prodCodeTran;
	}

	/**
	 * 処理用品目固定コードを設定する。
	 * 
	 * @param prodCodeTran 処理用品目固定コード
	 */
	public void setProdCodeTran(String prodCodeTran) {
		this.prodCodeTran = prodCodeTran;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.jgiNo = null;
		this.prodCode = null;
		this.rank = null;
		this.selectRowIdList = null;
		this.sosCd3Tran = null;
		this.jgiNoTran = null;
		this.prodCodeTran = null;
	}
}
