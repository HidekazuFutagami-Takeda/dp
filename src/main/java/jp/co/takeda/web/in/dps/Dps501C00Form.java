package jp.co.takeda.web.in.dps;

import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.TmsTytenPlanSlideForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanSlideResultDto;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps501C00((医)特約店別計画スライド画面)のフォームクラス
 *
 * @author yokokawa
 */
public class Dps501C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS501C00_DATA_R = new BoxKeyPerClassImpl(Dps501C00Form.class, TmsTytenPlanSlideResultDto.class);

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS501C01_DATA_R = new BoxKeyPerClassImpl(Dps501C00Form.class, TmsTytenPlanSlideForVacResultDto.class);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * カテゴリ
	 */
	private String category;

	// 処理用フィールド
	/**
	 * 処理用組織コード(支店)
	 */
	private String sosCd2Tran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String categoryTran;

	/**
	 * 計画立案レベル・支店
	 */
	public static final ProdPlanLevel DPS501C00_PLANLEVEL_SITEN = ProdPlanLevel.SITEN;

	/**
	 * 計画立案レベル・特約店
	 */
	public static final ProdPlanLevel DPS501C00_PLANLEVEL_WS = ProdPlanLevel.WS;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 施設特約店別計画〆フラグ
	 */
	private Boolean wsEndFlg;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 組織コード(支店)を取得する。
	 *
	 * @return 組織コード(支店)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(支店)を設定する。
	 *
	 * @param sosCd2 組織コード(支店)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 処理用組織コード(支店)を取得する。
	 *
	 * @return 処理用組織コード(支店)
	 */
	public String getSosCd2Tran() {
		return sosCd2Tran;
	}

	/**
	 * 処理用組織コード(支店)を設定する。
	 *
	 * @param sosCd2Tran 処理用組織コード(支店)
	 */
	public void setSosCd2Tran(String sosCd2Tran) {
		this.sosCd2Tran = sosCd2Tran;
	}

	/**
	 * 処理用品目カテゴリを取得する。
	 *
	 * @return 処理用品目カテゴリ
	 */
	public String getCategoryTran() {
		return categoryTran;
	}

	/**
	 * 処理用品目カテゴリを設定する。
	 *
	 * @param categoryTran 処理用品目カテゴリ
	 */
	public void setCategoryTran(String categoryTran) {
		this.categoryTran = categoryTran;
	}

	/**
	 * 施設特約店別計画〆フラグを取得する。
	 *
	 * @return 施設特約店別計画〆フラグ
	 */
	public Boolean getWsEndFlg() {
		return wsEndFlg;
	}

	/**
	 * 施設特約店別計画〆フラグを設定する。
	 *
	 * @param wsEndFlg 施設特約店別計画〆フラグ
	 */
	public void setWsEndFlg(Boolean wsEndFlg) {
		this.wsEndFlg = wsEndFlg;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd2 = null;
		this.sosCd2Tran = null;
		this.category = null;
		this.categoryTran = null;
	}


	/**
	 * 品目カテゴリリストを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public List<CodeAndValue> getProdCategoryList() {
		return prodCategoryList;
	}

	/**
	 * 品目カテゴリリストを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}
}
