package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsWsDistProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps400C00((医)施設特約店別計画配分対象品目一覧画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps400C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS400C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps400C00Form.class, InsWsDistProdResultDto.class);

	/**
	 * 検索結果有無取得キー(TRUE:1件以上,FALSE:0件)
	 */
	public static final BoxKey DPS400C00_DATA_R_SEARCH_RESULT_EXIST = new BoxKeyPerClassImpl(Dps400C00Form.class, Boolean.class);

	/**
	 * 編集権限((医)施設特約店別計画(自)配分処理)
	 */
	public static final DpAuthority DPS400C00_MMP_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 編集権限((医)施設特約店別計画(仕)配分処理)
	 */
	public static final DpAuthority DPS400C00_SIRE_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPS400C00_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 計画立案レベル・施設特約店
	 */
	public static final ProdPlanLevel DPS400C00_PLANLEVEL_INS_WS = ProdPlanLevel.INS_WS;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return InsWsDistProdUpdateDto 変換されたUpdateDtoのリスト
	 */
	public List<InsWsDistProdUpdateDto> convertInsWsDistProdUpdateDto() throws Exception {
		List<InsWsDistProdUpdateDto> updateDtoList = new ArrayList<InsWsDistProdUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = rowIdList[i].split(",", 3);

				// 品目固定コード
				String prodCode = rowId[0];
				// 品目名
				String prodName = rowId[1];
				// ステータス最終更新日
				Date statusLastUpDate = null;
				if (!StringUtils.isEmpty(rowId[2])) {
					statusLastUpDate = ConvertUtil.parseDate(rowId[2]);
				}
				// 更新用DTOの生成
				InsWsDistProdUpdateDto updateDto = new InsWsDistProdUpdateDto(prodCode, prodName, statusLastUpDate);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	/**
	 * 設定されたカテゴリ文字列をProdCategoryに変換する。
	 *
	 * @return カテゴリ
	 */
	public ProdCategory convertCategory() {

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//		if (StringUtils.isBlank(categoryTran)) {
//			return null;
//		} else if (categoryTran.equals("1")) {
//			return ProdCategory.MMP;
//		} else if (categoryTran.equals("2")) {
//			return ProdCategory.SHIIRE;
//		} else if (categoryTran.equals("3")) {
//			return ProdCategory.ONC;
//		} else {
//			return null;
//		}

		try {
			return ProdCategory.getInstance(categoryTran);
		} catch(Exception e){
			//カテゴリーとしてありえない値だった場合ProdCategory.getInstanceでは例外が発生するため、
			//修正前のコードと同様にnullを返す
			return null;
		}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	}

	/**
	 * 検索条件を処理用フィールドに設定する。
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.categoryTran = this.category;
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
	private String category;

	/**
	 * GRID行の識別IDリスト
	 * <ul>
	 * <li>Sprit文字列[0]=選択行の品目固定コード</li>
	 * <li>Sprit文字列[1]=選択行の品目名</li>
	 * <li>Sprit文字列[2]=選択行のステータス最終更新日</li>
	 * </ul>
	 */
	private String[] rowIdList;

	// 処理用フィールド
	/**
	 * 処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 処理用カテゴリ
	 */
	private String categoryTran;

	/**
	 * 配分処理と同時にMRに公開フラグ
	 * <ul>
	 * <li>TRUE =公開する</li>
	 * <li>FALSE=公開しない</li>
	 * </ul>
	 */
	private Boolean isMrOpenCheck;

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
	 * @return category カテゴリ
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
	 * GRID行の識別IDリストを取得する。
	 *
	 * @return rowIdList GRID行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return (String[]) rowIdList.clone();
	}

	/**
	 * GRID行の識別IDリストを設定する。
	 *
	 * @param rowIdList GRID行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList.clone();
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
	 * 処理用カテゴリを取得する。
	 *
	 * @return 処理用カテゴリ
	 */
	public String getCategoryTran() {
		return categoryTran;
	}

	/**
	 * 処理用カテゴリを設定する。
	 *
	 * @param categoryTran 処理用カテゴリ
	 */
	public void setCategoryTran(String categoryTran) {
		this.categoryTran = categoryTran;
	}

	/**
	 * 配分処理と同時にMRに公開フラグを取得する。
	 *
	 * @return isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public Boolean getIsMrOpenCheck() {
		return isMrOpenCheck;
	}

	/**
	 * 配分処理と同時にMRに公開フラグを設定する。
	 *
	 * @param isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public void setIsMrOpenCheck(Boolean isMrOpenCheck) {
		this.isMrOpenCheck = isMrOpenCheck;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.sosCd3Tran = null;
		this.category = null;
		this.rowIdList = null;
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
