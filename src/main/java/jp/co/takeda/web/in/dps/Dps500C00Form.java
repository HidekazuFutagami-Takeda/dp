package jp.co.takeda.web.in.dps;

import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.TmsTytenPlanDistProdResultDto;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps500C00((医)特約店別計画配分対象品目一覧画面)のフォームクラス
 *
 * @author yokokawa
 */
public class Dps500C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS500C00_DATA_R = new BoxKeyPerClassImpl(Dps500C00Form.class, TmsTytenPlanDistProdResultDto.class);

	/**
	 * 計画立案レベル・支店
	 */
	public static final ProdPlanLevel DPS500C00_PLANLEVEL_SITEN = ProdPlanLevel.SITEN;

	/**
	 * 計画立案レベル・特約店
	 */
	public static final ProdPlanLevel DPS500C00_PLANLEVEL_WS = ProdPlanLevel.WS;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 従業員コード
	 */
	private String jgiNo;

	/**
	 * カテゴリ
	 */
	private String category = ProdCategory.SHIIRE.getDbValue();

	/**
	 * 選択された固定品目コード
	 */
	private String[] prodCode;

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
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

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
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
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
	 * 選択された固定品目コードを設定する。
	 *
	 * @param prodCode 選択された固定品目コード
	 */
	public void setProdCode(String[] prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 選択された固定品目コードを取得する。
	 *
	 * @return prodCode 選択された固定品目コード
	 */
	public String[] getProdCode() {
		return prodCode;
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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.prodCode = null;
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
