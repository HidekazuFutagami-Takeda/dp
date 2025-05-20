package jp.co.takeda.web.in.dpm;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenMstHontenListDto;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps912C01(特約店検索(本店)画面)のフォームクラス
 *
 * @author khashimoto
 */
public class Dpm912C01Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果のキー値
	 */
	public static final BoxKey TMS_LIST_KEY_R = new BoxKeyPerClassImpl(Dpm912C01Form.class, TmsTytenMstHontenListDto.class);

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return TmsTytenMstScDto 変換されたScDto
	 */
	public TmsTytenMstScDto convertTmsTytenMstScDto() throws Exception {
		// 組織コード（特約店部）
		if (StringUtils.isEmpty(takedaSosCd2)) {
			takedaSosCd2 = null;
		}

		// 本店名（半角カナ）
		if (StringUtils.isEmpty(hontenMeiKn)) {
			hontenMeiKn = null;
		}

		// add 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		// 都道府県コード（呼び出し元）
		if (StringUtils.isEmpty(addrCodePref)) {
			addrCodePref = null;
		}

		return new TmsTytenMstScDto(TytenKisLevel.HONTEN, null, null, null, null, takedaSosCd2, hontenMeiKn, null, null, addrCodePref);
	}

	/**
	 * DTO ⇒ ActionForm 変換処理
	 *
	 */
	public void convertForm(TmsTytenMstHontenListDto dto) throws Exception {
		tytenBuName = dto.getTytenBuName();
		// add 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		takedaSosCd2 = dto.getTytenBuCode();
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
	 * 現在のページ番号
	 */
	private Integer crntPageNo;

	// 検索条件
	/**
	 * 本店名（半角カナ）
	 */
	private String hontenMeiKn;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 都道府県コード
	 */
	private String addrCodePref;

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

	/**
	 * 本店名（半角カナ）を取得する。
	 *
	 * @return 本店名（半角カナ）
	 */
	public String getHontenMeiKn() {
		return hontenMeiKn;
	}

	/**
	 * 本店名（半角カナ）を設定する。
	 *
	 * @param hontenMeiKn 本店名（半角カナ）
	 */
	public void setHontenMeiKn(String hontenMeiKn) {
		this.hontenMeiKn = hontenMeiKn;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 都道府県コードを取得する。
	 *
	 * @return 都道府県コード
	 */
	public String getaddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 都道府県コードを設定する。
	 *
	 * @param addrCodePref 都道府県コード
	 */
	public void setaddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		crntPageNo = 1;
		hontenMeiKn = "";
		tytenBuName = "";
	}
}
