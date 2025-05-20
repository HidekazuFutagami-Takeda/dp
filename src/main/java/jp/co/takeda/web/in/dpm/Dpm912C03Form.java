package jp.co.takeda.web.in.dpm;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.dto.TmsTytenMstTenkaiListDto;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps912C03(特約店検索(展開)画面)のフォームクラス
 *
 * @author khashimoto
 */
public class Dpm912C03Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果のキー値
	 */
	public static final BoxKey TMS_LIST_KEY_R = new BoxKeyPerClassImpl(Dpm912C03Form.class, TmsTytenMstTenkaiListDto.class);

	/**
	 * DTO ⇒ ActionForm 変換処理
	 *
	 */
	public void convertForm(TmsTytenMstTenkaiListDto dto) throws Exception {
		tytenBuName = dto.getTytenBuName();
		hontenMeiKj = dto.getHontenMeiKj();
		shishaMeiKj = dto.getShishaMeiKj();
	}

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

		// 特約店コード
		if (StringUtils.isEmpty(searchTmsTytenCd)) {
			searchTmsTytenCd = null;
		}

		// mod 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		return new TmsTytenMstScDto(null, searchTmsTytenCd, null, null, null, takedaSosCd2, null, null, null, null);

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
	 * TMS特約店コード（支社）
	 */
	private String shishaTmsTytenCd;

	/**
	 * 支社名
	 */
	private String shishaMeiKj;

	/**
	 * 支社保持フラグ
	 */
	private boolean shishaExistFlg;

	/**
	 * 現在のページ番号
	 */
	private Integer crntPageNo;

	// 検索条件
	/**
	 * 検索条件・特約店コード
	 */
	private String searchTmsTytenCd;

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
	 * TMS特約店コード（支社）を取得する。
	 *
	 * @return TMS特約店コード（支社）
	 */
	public String getShishaTmsTytenCd() {
		return shishaTmsTytenCd;
	}

	/**
	 * TMS特約店コード（支社）を設定する。
	 *
	 * @param shishaTmsTytenCd TMS特約店コード（支社）
	 */
	public void setShishaTmsTytenCd(String shishaTmsTytenCd) {
		this.shishaTmsTytenCd = shishaTmsTytenCd;
	}

	/**
	 * 支社名を取得する。
	 *
	 * @return 支社名
	 */
	public String getShishaMeiKj() {
		return shishaMeiKj;
	}

	/**
	 * 支社名を設定する。
	 *
	 * @param shishaMeiKj 支社名
	 */
	public void setShishaMeiKj(String shishaMeiKj) {
		this.shishaMeiKj = shishaMeiKj;
	}

	/**
	 * 支社保持フラグを取得する。
	 *
	 * @return 支社保持フラグ
	 */
	public boolean isShishaExistFlg() {
		return shishaExistFlg;
	}

	/**
	 * 支社保持フラグを設定する。
	 *
	 * @param shishaExistFlg 支社保持フラグ
	 */
	public void setShishaExistFlg(boolean shishaExistFlg) {
		this.shishaExistFlg = shishaExistFlg;
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
	 * 検索条件・特約店コードを取得する。
	 *
	 * @return 検索条件・特約店コード
	 */
	public String getSearchTmsTytenCd() {
		return searchTmsTytenCd;
	}

	/**
	 * 検索条件・特約店コードを設定する。
	 *
	 * @param searchTmsTytenCd 検索条件・特約店コード
	 */
	public void setSearchTmsTytenCd(String searchTmsTytenCd) {
		this.searchTmsTytenCd = searchTmsTytenCd;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		crntPageNo = 1;
		searchTmsTytenCd = "";
	}
}
