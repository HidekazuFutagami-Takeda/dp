package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.OfficePlanResultListDto;
import jp.co.takeda.dto.OfficePlanUpdateDto;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps300C00((医)営業所計画編集画面)のフォームクラス
 *
 * @author nozaki
 */
public class Dps300C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Dps300C00_DATA_R
	 */
	public static final BoxKey DPS300C00_DATA_R = new BoxKeyPerClassImpl(Dps300C00Form.class, OfficePlanResultListDto.class);

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPS300C00_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * 検索結果の存在判定フラグ
	 *
	 * <pre>
	 * 検索アクション後にセットする。
	 * TRUE=検索結果あり
	 * FALSE=検索結果なし(ヒット件数0)
	 * </pre>
	 */
	private boolean existSearchDataFlag;

	/**
	 * GRID行の識別IDリスト
	 *
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=シーケンスキー
	 * Sprit文字列[1]=品目固定コード
	 * Sprit文字列[2]=最終更新日時(Date型のgetTime()の値)
	 * Sprit文字列[3]=入力された計画値UH
	 * Sprit文字列[4]=入力された計画値P
	 * </pre>
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
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * グリッドの追加ヘッダ
	 */
	private String attachHeader;

	/**
	 * ヘッダ（UH）
	 */
	private String headerUh;

	/**
	 * ヘッダ（P）
	 */
	private String headerP;

	/**
	 * 計画（月別計画）
	 */
	private String planId;

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
	 * グリッドの追加ヘッダを取得する。
	 *
	 * @return attachHeader1row  グリッド（集計）の追加ヘッダ（1行目）
	 */
	public String getAttachHeader() {
		return attachHeader;
	}

	/**
	 * グリッドの追加ヘッダを設定する。
	 *
	 * @param attachHeader1row  グリッド（集計）の追加ヘッダ（1行目）
	 */
	public void setAttachHeader(String attachHeader) {
		this.attachHeader = attachHeader;
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

	/**
	 * 計画（期別計画）を取得する。
	 *
	 * @return planId
	 */
	public String getPlanId() {
		return planId;
	}

	/**
	 * 計画（期別計画）を設定する。
	 *
	 * @param planId
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}


	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * 画面から入力されたデータを、更新用営業所計画DTOに変換する。
	 *
	 * @return 更新用営業所計画DTOのリスト
	 */
	public List<OfficePlanUpdateDto> getUpdateOfficePlanList() throws Exception {

		// 更新用DTOのリスト作成
		List<OfficePlanUpdateDto> officePlanList = new ArrayList<OfficePlanUpdateDto>();
		if (rowIdList != null) {

			for (int i = 0; i < rowIdList.length; i++) {

				if (rowIdList[i] == null) {
					continue;
				}

				Long _seqKey = null;
				String _prodCode = null;
				Long _plannedValueUHY = null;
				Long _plannedValuePY = null;
				Date _lastUpDate = null;

				String[] rowId = rowIdList[i].split(",", 5);

				// シーケンスキー
				if (ValidationUtil.isLong(rowId[0])) {
					_seqKey = Long.parseLong(rowId[0]);
				}
				// 品目固定コード
				if (!StringUtils.isEmpty(rowId[1])) {
					_prodCode = rowId[1];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[2])) {
					_lastUpDate = new Date(Long.parseLong(rowId[2]));
				}
				// 計画値UH
				if (ValidationUtil.isLong(rowId[3])) {
					_plannedValueUHY = Long.parseLong(rowId[3]);
				}
				// 計画値P
				if (ValidationUtil.isLong(rowId[4])) {
					_plannedValuePY = Long.parseLong(rowId[4]);
				}

				OfficePlanUpdateDto officePlan = new OfficePlanUpdateDto(_seqKey, sosCd3Tran, _prodCode, _plannedValueUHY, _plannedValuePY, _lastUpDate);

				// リストに追加
				officePlanList.add(officePlan);
			}
		}
		return officePlanList;
	}

	/**
	 * 検索条件を処理用フィールドに設定する。
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.categoryTran = this.category;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// categoryはトップ画面から遷移することを考慮し、初期化対象からはずす
		this.sosCd3 = null;
		this.existSearchDataFlag = false;
		this.rowIdList = null;
		this.sosCd3Tran = null;
		this.categoryTran = null;
	}
}
