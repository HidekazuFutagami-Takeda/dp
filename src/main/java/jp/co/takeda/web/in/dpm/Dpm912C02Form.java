package jp.co.takeda.web.in.dpm;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenMstListDto;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps912C02(特約店検索(支社)画面)のフォームクラス
 * 
 * @author khashimoto
 */
public class Dpm912C02Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果のキー値
	 */
	public static final BoxKey TMS_LIST_KEY_R = new BoxKeyPerClassImpl(Dpm912C02Form.class, TmsTytenMstListDto.class);

	/**
	 * DTO ⇒ ActionForm 変換処理
	 * 
	 */
	public void convertForm(TmsTytenMstListDto dto) throws Exception {
		tytenBuName = dto.getTytenBuName();
		hontenMeiKj = dto.getHontenMeiKj();
	}

	// -------------------------------
	// field
	// -------------------------------
	// INパラメータ
	/**
	 * 適用関数名
	 */
	private String tytenApplyFuncName;

	/**
	 * 組織コード（特約店部）
	 */
	private String takedaSosCd2;

	/**
	 * 特約店部名
	 */
	private String tytenBuName;

	/**
	 * TMS特約店コード（本店）
	 */
	private String hontenTmsTytenCd;

	/**
	 * 本店名
	 */
	private String hontenMeiKj;

	/**
	 * 現在のページ番号
	 */
	private Integer crntPageNo;

	// -------------------------------
	// getter & setter
	// -------------------------------

	/**
	 * 適用関数名を取得する。
	 * 
	 * @return 適用関数名
	 */
	public String getTytenApplyFuncName() {
		return tytenApplyFuncName;
	}

	/**
	 * 適用関数名を設定する。
	 * 
	 * @param tytenApplyFuncName 適用関数名
	 */
	public void setTytenApplyFuncName(String tytenApplyFuncName) {
		this.tytenApplyFuncName = tytenApplyFuncName;
	}

	/**
	 * 組織コード（特約店部）を取得する。
	 * 
	 * @return 組織コード（特約店部）
	 */
	public String getTakedaSosCd2() {
		return takedaSosCd2;
	}

	/**
	 * 組織コード（特約店部）を設定する。
	 * 
	 * @param takedaSosCd2 組織コード（特約店部）
	 */
	public void setTakedaSosCd2(String takedaSosCd2) {
		this.takedaSosCd2 = takedaSosCd2;
	}

	/**
	 * 特約店部名を取得する。
	 * 
	 * @return 特約店部名
	 */
	public String getTytenBuName() {
		return tytenBuName;
	}

	/**
	 * 特約店部名を設定する。
	 * 
	 * @param tytenBuName 特約店部名
	 */
	public void setTytenBuName(String tytenBuName) {
		this.tytenBuName = tytenBuName;
	}

	/**
	 * TMS特約店コード（本店）を取得する。
	 * 
	 * @return hontenTmsTytenCd TMS特約店コード（本店）
	 */
	public String getHontenTmsTytenCd() {
		return hontenTmsTytenCd;
	}

	/**
	 * TMS特約店コード（本店）を設定する。
	 * 
	 * @param hontenTmsTytenCd TMS特約店コード（本店）
	 */
	public void setHontenTmsTytenCd(String hontenTmsTytenCd) {
		this.hontenTmsTytenCd = hontenTmsTytenCd;
	}

	/**
	 * 本店名を取得する。
	 * 
	 * @return 本店名
	 */
	public String getHontenMeiKj() {
		return hontenMeiKj;
	}

	/**
	 * 本店名を設定する。
	 * 
	 * @param hontenMeiKj 本店名
	 */
	public void setHontenMeiKj(String hontenMeiKj) {
		this.hontenMeiKj = hontenMeiKj;
	}

	/**
	 * 現在のページ番号を取得する。
	 * 
	 * @return 現在のページ番号
	 */
	public Integer getCrntPageNo() {
		return crntPageNo;
	}

	/**
	 * 現在のページ番号を設定する。
	 * 
	 * @param crntPageNo 現在のページ番号
	 */
	public void setCrntPageNo(Integer crntPageNo) {
		this.crntPageNo = crntPageNo;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		crntPageNo = 1;
		tytenBuName = "";
		hontenMeiKj = "";
	}
}
