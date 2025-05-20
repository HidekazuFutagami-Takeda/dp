package jp.co.takeda.web.in.dps;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenMstResultsTenkaiListDto;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps912C05(特約店実績検索(特約店)画面)のフォームクラス
 *
 * @author khashimoto
 */
public class Dps912C05Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果のキー値
	 */
	public static final BoxKey TMS_LIST_KEY_R = new BoxKeyPerClassImpl(Dps912C05Form.class, TmsTytenMstResultsTenkaiListDto.class);

	/**
	 * DTO ⇒ ActionForm 変換処理
	 *
	 */
	public void convertForm(TmsTytenMstResultsTenkaiListDto dto) throws Exception {
		hontenMeiKj = dto.getHontenMeiKj();
		shishaMeiKj = dto.getShishaMeiKj();
		setShitenMeiKj(dto.getShitenMeiKj());
	}

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return TmsTytenMstScDto 変換されたScDto
	 */
	public TmsTytenMstScDto convertTmsTytenMstScDto() throws Exception {
		// 特約店コード
		if (StringUtils.isEmpty(searchTmsTytenCd)) {
			searchTmsTytenCd = null;
		}
		// mod 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		return new TmsTytenMstScDto(null, searchTmsTytenCd, null, null, null, null, null, jgiNo, insType, null);

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
	 * 組織コード
	 */
	private String sosCd;

	/**
	 * 特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 本店名
	 */
	private String hontenMeiKj;

	/**
	 * 支社名
	 */
	private String shishaMeiKj;

	/**
	 * 支店名
	 */
	private String shitenMeiKj;

	/**
	 * 現在のページ番号
	 */
	private Integer crntPageNo5;

	/**
	 * 検索条件・特約店コード
	 */
	private String searchTmsTytenCd;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 対象区分
	 */
	private String insType;

	/**
	 * 参照用集計品目名
	 */
	private String refTotalProdNm;

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
	 * 組織コードを取得する。
	 *
	 * @return 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織コードを設定する。
	 *
	 * @param sosCd 組織コード
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店コードを設定する。
	 *
	 * @param tmsTytenCd 特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
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
	 * 支店名を取得する。
	 *
	 * @return 支店名
	 */
	public String getShitenMeiKj() {
		return shitenMeiKj;
	}

	/**
	 * 支店名を設定する。
	 *
	 * @param shitenMeiKj 支店名
	 */
	public void setShitenMeiKj(String shitenMeiKj) {
		this.shitenMeiKj = shitenMeiKj;
	}

	/**
	 * 現在のページ番号を取得する。
	 *
	 * @return 現在のページ番号
	 */
	public Integer getCrntPageNo5() {
		return crntPageNo5;
	}

	/**
	 * 現在のページ番号を設定する。
	 *
	 * @param crntPageNo5 現在のページ番号
	 */
	public void setCrntPageNo5(Integer crntPageNo5) {
		this.crntPageNo5 = crntPageNo5;
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

	/**
	 * 従業員番号を取得する
	 *
	 * @return
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する
	 *
	 * @param jgiNo
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 対象区分を取得する
	 *
	 * @return
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 対象区分を設定する
	 *
	 * @param insNo
	 */
	public void setInsType(String insType) {
		this.insType = insType;
	}

	/**
	 * 参照用集計品目名を取得する
	 *
	 * @return
	 */
	public String getRefTotalProdNm() {
		return refTotalProdNm;
	}

	/**
	 * 参照用集計品目名を設定する
	 *
	 * @param insNo
	 */
	public void setRefTotalProdNm(String refTotalProdNm) {
		this.refTotalProdNm = refTotalProdNm;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		crntPageNo5 = 1;
		searchTmsTytenCd = "";
	}
}
