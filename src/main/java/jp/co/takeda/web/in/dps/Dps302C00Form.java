package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.MrPlanStatusUpdateDto;
import jp.co.takeda.dto.PlannedProdResultListDto;
import jp.co.takeda.dto.PlannedProdScDto;
import jp.co.takeda.dto.PlannedValueCopyDto;
import jp.co.takeda.logic.div.CopyTarget;
import jp.co.takeda.logic.div.CopyWay;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps302C00((医)計画対象品目選択画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps302C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS302C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps302C00Form.class, PlannedProdResultListDto.class);

	/**
	 * 営業所ステータス取得キー
	 */
	public static final BoxKey DPS302C00_DATA_R_OFFICE_PLAN = new BoxKeyPerClassImpl(Dps302C00Form.class, OfficePlanStatus.class);

	/**
	 * 参照権限[チーム別計画公開]
	 */
	public static final DpAuthority DPS302C00_TEAM_OPEN_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 参照権限[チーム別計画確定]
	 */
	public static final DpAuthority DPS302C00_TEAM_FIX_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 参照権限[担当者別計画公開]
	 */
	public static final DpAuthority DPS302C00_TANTO_OPEN_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 参照権限[担当者別計画確定]
	 */
	public static final DpAuthority DPS302C00_TANTO_FIX_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPS302C00_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 計画立案レベル・担当者
	 */
	public static final ProdPlanLevel DPS302C00_PLANLEVEL_MR = ProdPlanLevel.MR;

	/**
	 * 計画立案レベル・全て
	 */
	public static final ProdPlanLevel DPS302C00_PLANLEVEL_ALL = ProdPlanLevel.ALL;

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return PlannedProdScDto 変換されたScDto
	 */
	public PlannedProdScDto convertPlannedProdScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}

		// 従業員番号
		Integer iJgiNo = null;
		if (StringUtils.isEmpty(jgiNoTran)) {
			jgiNoTran = null;
		} else {
			iJgiNo = Integer.valueOf(jgiNoTran);
		}

		// カテゴリ
		if (StringUtils.isEmpty(categoryTran)) {
			categoryTran = null;
		}

		return new PlannedProdScDto(sosCd3Tran, sosCd4Tran, iJgiNo, categoryTran);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return MrPlanStatusUpdateDto 変換されたUpdateDtoのリスト
	 */
	public List<MrPlanStatusUpdateDto> convertMrPlanStatusUpdateDto() throws Exception {

		List<MrPlanStatusUpdateDto> updateDtoList = new ArrayList<MrPlanStatusUpdateDto>();

		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {

				String[] rowId = rowIdList[i].split(",");

				// 担当者別計画ステータス シーケンスキー
				final String statusSeqKeyStr = rowId[0];
				Long statusSeqKey = null;
				if (StringUtils.isNotBlank(statusSeqKeyStr)) {
					statusSeqKey = Long.valueOf(statusSeqKeyStr);
				}

				// 担当者別計画ステータス 最終更新日時
				final String statusUpDateStr = rowId[1];
				Date statusUpDate = DateUtil.getSystemTime();
				if (StringUtils.isNotBlank(statusUpDateStr)) {
					statusUpDate = ConvertUtil.parseDate(statusUpDateStr);
				}

				MrPlanStatusUpdateDto updateDto = new MrPlanStatusUpdateDto(statusSeqKey, statusUpDate);
				updateDtoList.add(updateDto);
			}
		}

		return updateDtoList;
	}

	/**
	 * ActionForm ⇒ PlannedProdList 変換処理
	 *
	 * @return PlannedProd 変換されたPlannedProdのリスト
	 */
	public List<PlannedProd> convertPlannedProdList() throws Exception {

		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();

		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {

				PlannedProd plannedProd = new PlannedProd();

				String[] rowId = rowIdList[i].split(",");

				// 品目固定コード
				plannedProd.setProdCode(rowId[2]);

				// 品目名称
				plannedProd.setProdName(rowId[3]);

				// カテゴリ
				plannedProd.setCategory(categoryTran);

				plannedProdList.add(plannedProd);
			}
		}

		return plannedProdList;
	}

	/**
	 * ActionForm ⇒ dto 変換処理
	 *
	 * @return 計画値の一括コピー処理実行用DTO
	 */
	public PlannedValueCopyDto convertPlannedValueCopyDto() throws Exception {
//		return new PlannedValueCopyDto(this.sosCd3Tran, this.convertPlannedProdList(), CopyTarget.getInstance(copyTarget), CopyWay.getInstance(copyWay));
		return new PlannedValueCopyDto(this.sosCd3Tran, this.convertPlannedProdList(), CopyTarget.getInstance(copyTarget), CopyWay.getInstance(copyWay), this.category);
	}

	/**
	 * 検索条件を処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.categoryTran = this.category;
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
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * コピー対象
	 */
	private String copyTarget;

	/**
	 * コピー方法
	 */
	private String copyWay;

	/**
	 * GRID行の識別IDリスト
	 *
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=選択行の担当者別計画ステータスのシーケンスキー
	 * Sprit文字列[1]=選択行の担当者別計画ステータスの最終更新日時
	 * Sprit文字列[2]=選択行の品目固定コード
	 * Sprit文字列[3]=選択行の品目名称
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * 調整金額チェック対象品目固定コード
	 */
	private String checkProdCode;

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
	 * 処理用カテゴリ
	 */
	private String categoryTran;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * グリッドのヘッダ(営業所)
	 */
	private String headerOffice;

	/**
	 * グリッドの追加ヘッダ(担当)
	 */
	private String attachHeaderMr;

	/**
	 * ヘッダ（UH）
	 */
	private String headerUh;

	/**
	 * ヘッダ（P）
	 */
	private String headerP;

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
	 * param sosCd3 組織コード(営業所)
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
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 *
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * コピー対象を取得する。
	 *
	 * @return コピー対象
	 */
	public String getCopyTarget() {
		return copyTarget;
	}

	/**
	 * コピー対象を設定する。
	 *
	 * @param copyTarget コピー対象
	 */
	public void setCopyTarget(String copyTarget) {
		this.copyTarget = copyTarget;
	}

	/**
	 * コピー方法を取得する。
	 *
	 * @return コピー方法
	 */
	public String getCopyWay() {
		return copyWay;
	}

	/**
	 * コピー方法を設定する。
	 *
	 * @param copyWay コピー方法
	 */
	public void setCopyWay(String copyWay) {
		this.copyWay = copyWay;
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
	 * 調整金額チェック対象品目固定コードを取得する。
	 *
	 * @return 調整金額チェック対象品目固定コード
	 */
	public String getCheckProdCode() {
		return checkProdCode;
	}

	/**
	 * 調整金額チェック対象品目固定コードを設定する。
	 *
	 * @param checkProdCode 調整金額チェック対象品目固定コード
	 */
	public void setCheckProdCode(String checkProdCode) {
		this.checkProdCode = checkProdCode;
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

	/**
	 * 処理用カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategoryTran() {
		return categoryTran;
	}

	/**
	 * 処理用カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategoryTran(String categoryTran) {
		this.categoryTran = categoryTran;
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
	 * グリッドのヘッダ(営業所)を取得する。
	 *
	 * @return headerOffice  グリッドのヘッダ(営業所)
	 */
	public String getHeaderOffice() {
		return headerOffice;
	}

	/**
	 * グリッドのヘッダ(営業所)を設定する。
	 *
	 * @param headerOffice  グリッドのヘッダ(営業所)
	 */
	public void setHeaderOffice(String headerOffice) {
		this.headerOffice = headerOffice;
	}

	/**
	 * グリッドの追加ヘッダ(担当者)を取得する。
	 *
	 * @return attachHeaderMr  グリッドの追加ヘッダ(担当者)
	 */
	public String getAttachHeaderMr() {
		return attachHeaderMr;
	}

	/**
	 * グリッドの追加ヘッダ(担当者)を設定する。
	 *
	 * @param attachHeaderMr  グリッドの追加ヘッダ(担当者)
	 */
	public void setAttachHeaderMr(String attachHeaderMr) {
		this.attachHeaderMr = attachHeaderMr;
	}

	/**
	 * ヘッダ(UH)を取得する。
	 *
	 * @return headerUh  ヘッダ(UH)
	 */
	public String getHeaderUh() {
		return headerUh;
	}

	/**
	 * ヘッダ(UH)を設定する。
	 *
	 * @param headerUh  ヘッダ(UH)
	 */
	public void setHeaderUh(String headerUh) {
		this.headerUh = headerUh;
	}

	/**
	 * ヘッダ(P)を取得する。
	 *
	 * @return headerP  ヘッダ(P)
	 */
	public String getHeaderP() {
		return headerP;
	}

	/**
	 * ヘッダ(P)を設定する。
	 *
	 * @param headerP  ヘッダ(P)
	 */
	public void setHeaderP(String headerP) {
		this.headerP = headerP;
	}


	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.sosCd4 = null;
		this.jgiNo = null;
		this.sosCd3Tran = null;
		this.sosCd4Tran = null;
		this.jgiNoTran = null;
		this.rowIdList = null;
		this.checkProdCode = null;
		this.category = null;
		this.categoryTran = null;
	}
}
