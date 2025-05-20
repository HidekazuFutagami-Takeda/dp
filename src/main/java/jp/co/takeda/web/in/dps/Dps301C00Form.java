package jp.co.takeda.web.in.dps;

import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.EstimationExecDto;
import jp.co.takeda.dto.EstimationProdResultDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps301C00((医)試算対象品目一覧画面)のフォームクラス
 *
 * @author nozaki
 */
public class Dps301C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * Dps301C00_DATA_R
	 */
	public static final BoxKey DPS301C00_DATA_R = new BoxKeyPerClassImpl(Dps301C00Form.class, EstimationProdResultDto.class);

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPS301C00_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 計画立案レベル・担当者
	 */
	public static final ProdPlanLevel DPS301C00_PLANLEVEL_MR = ProdPlanLevel.MR;

	/**
	 * 計画立案レベル・全て
	 */
	public static final ProdPlanLevel DPS301C00_PLANLEVEL_ALL = ProdPlanLevel.ALL;

	/**
	 * 登録権限
	 */
	public static final DpAuthority DPS301C00_EDIT_AUTH = new DpAuthority( EDIT);

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 検索結果の存在判定フラグ
	 *
	 * <pre>
	 * 検索アクション後にセットする。
	 * TRUE=検索結果あり<br>
	 * FALSE=検索結果なし(ヒット件数0)
	 * </pre>
	 */
	private boolean existSearchDataFlag;

	/**
	 * 試算タイプ
	 */
	private String calcType;

	/**
	 * GRID行の識別IDリスト
	 *
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=選択された品目固定コード
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * 実績・未獲得市場チェック対象品目固定コード(カンマ区切り)
	 */
	private String checkProdCode;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	// 処理用フィールド
	/**
	 * 処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	/**
	 * 処理用カテゴリ
	 */
	private String categoryTran;
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更

	/**
	 * 実績・未獲得市場チェック対象品目固定コード(カンマ区切り)を取得する。
	 *
	 * @return 実績・未獲得市場チェック対象品目固定コード(カンマ区切り)
	 */
	public String getCheckProdCode() {
		return checkProdCode;
	}

	/**
	 * 実績・未獲得市場チェック対象品目固定コード(カンマ区切り)を設定する。
	 *
	 * @param checkProdCode 実績・未獲得市場チェック対象品目固定コード(カンマ区切り)
	 */
	public void setCheckProdCode(String checkProdCode) {
		this.checkProdCode = checkProdCode;
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
	 * 検索結果の存在判定フラグを取得する。
	 *
	 * @return existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public Boolean getExistSearchDataFlag() {
		return existSearchDataFlag;
	}

	/**
	 * 検索結果の存在判定フラグを設定する。
	 *
	 * @param existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public void setExistSearchDataFlag(Boolean existSearchDataFlag) {
		this.existSearchDataFlag = existSearchDataFlag;
	}

	/**
	 * 試算タイプを取得する。
	 *
	 * @return 試算タイプ
	 */
	public String getCalcType() {
		return calcType;
	}

	/**
	 * 試算タイプを設定する。
	 *
	 * @param calcType 試算タイプ
	 */
	public void setCalcType(String calcType) {
		this.calcType = calcType;
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
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	/**
	 * 処理用カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategoryTran() {
		return categoryTran;
	}
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	/**
	 * 処理用カテゴリを設定する。
	 *
	 * @param categoryTran カテゴリ
	 */
	public void setCategoryTran(String categoryTran) {
		this.categoryTran = categoryTran;
	}
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更


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

	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * ActionForm変換処理 <br>
	 * 画面から入力されたデータを、試算実行用DTOに変換する。
	 *
	 * @return 試算実行用DTOのリスト
	 */
	public List<EstimationExecDto> getEstimationExecList() throws Exception {

		// 試算実行用DTOのリスト作成
		List<EstimationExecDto> estimationExecDtoList = new ArrayList<EstimationExecDto>();
		if (rowIdList != null) {

			for (int i = 0; i < rowIdList.length; i++) {

				if (rowIdList[i] == null) {
					continue;
				}

				String[] rowId = rowIdList[i].split(",", 3);

				// 試算実行用DTO作成
				Date upDate = null;
				if (StringUtils.isNotBlank(rowId[2])) {
					upDate = new Date((ConvertUtil.parseLong(rowId[2])));
				}
				EstimationExecDto estimationExecDto = new EstimationExecDto(rowId[0], rowId[1], upDate);
				estimationExecDtoList.add(estimationExecDto);
			}
		}
		return estimationExecDtoList;
	}

	/**
	 * 検索条件を処理用フィールドに設定する。
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
		this.categoryTran = this.category;
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.existSearchDataFlag = false;
		this.rowIdList = null;
		this.checkProdCode = null;
		this.sosCd3Tran = null;
		this.category = null;
// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
		this.categoryTran = null;
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
	}
}
