package jp.co.takeda.web.in.dps;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.MikakutokuSijouResultDto;
import jp.co.takeda.dto.MikakutokuSijouScDto;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps203C00((医)未獲得市場一覧画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps203C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS203C00_DATA_R
	 */
	public static final BoxKey DPS203C00_DATA_R = new BoxKeyPerClassImpl(Dps203C00Form.class, MikakutokuSijouResultDto.class);

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPS203C00_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 参照権限
	 */
	public static final DpAuthority DPS203C00_REFER_AUTH = new DpAuthority( AuthType.REFER);

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return MikakutokuSijouScDto 変換されたScDto
	 */
	public MikakutokuSijouScDto convertMikakutokuSijouScDto() throws Exception {
		if (StringUtils.isEmpty(sosCd3)) {
			sosCd3 = null;
		}
		if (StringUtils.isEmpty(yakkouSijouCode)) {
			yakkouSijouCode = null;
		}
		MikakutokuSijouScDto scDto = new MikakutokuSijouScDto(sosCd3, yakkouSijouCode);
		return scDto;
	}

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
	 * 処理用品目カテゴリ
	 */
	private String categoryTran;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 薬効市場コード
	 */
	private String yakkouSijouCode;

	/**
	 * 薬効市場リスト
	 */
	private List<CodeAndValue> yakkouSijouCodeList;

	/**
	 * 検索結果の存在判定フラグ
	 *
	 * <pre>
	 * 検索アクション後にセットします。
	 * TRUE=検索結果あり<br>
	 * FALSE=検索結果なし(ヒット件数0)
	 * </pre>
	 */
	private boolean existSearchDataFlag;

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
	 * 薬効市場コードを取得する。
	 *
	 * @return yakkouSijouCode 薬効市場コード
	 */
	public String getYakkouSijouCode() {
		return yakkouSijouCode;
	}

	/**
	 * 薬効市場コードを設定する。
	 *
	 * @param yakkouSijouCode 薬効市場コード
	 */
	public void setYakkouSijouCode(String yakkouSijouCode) {
		this.yakkouSijouCode = yakkouSijouCode;
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
	 * 検索結果の存在判定フラグを取得する。
	 *
	 * @return existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public boolean getExistSearchDataFlag() {
		return existSearchDataFlag;
	}

	/**
	 * 検索結果の存在判定フラグを設定する。
	 *
	 * @param existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public void setExistSearchDataFlag(boolean existSearchDataFlag) {
		this.existSearchDataFlag = existSearchDataFlag;
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

	/**
	 * 薬効市場リストを取得する。
	 *
	 * @return yakkouSijouCode 薬効市場
	 */
	public List<CodeAndValue> getYakkouSijouCodeList() {
		return yakkouSijouCodeList;
	}

	/**
	 * 薬効市場リストを設定する。
	 *
	 * @param yakkouSijouCode 薬効市場
	 */
	public void setYakkouSijouCodeList(List<CodeAndValue> yakkouSijouCodeList) {
		this.yakkouSijouCodeList = yakkouSijouCodeList;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.existSearchDataFlag = false;
	}
}
