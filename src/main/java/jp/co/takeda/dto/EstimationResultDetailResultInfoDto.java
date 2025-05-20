package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlan;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算結果詳細の共通情報を表すDTO
 * 
 * @author stakeuchi
 */
public class EstimationResultDetailResultInfoDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * [立案対象品目] 品目固定コード
	 */
	private final String prodCode;

	/**
	 * [立案対象品目] 品目名称
	 */
	private final String prodName;

	/**
	 * [立案対象品目] 製品区分
	 */
	private final String prodType;

	/**
	 * [試算品目] 品目固定コード
	 */
	private final String estProdCode;

	/**
	 * [試算品目] 品目名称
	 */
	private final String estProdName;

	/**
	 * [試算品目] 製品区分
	 */
	private final String estProdType;

	/**
	 * [試算パラメータ] シーケンスキー
	 */
	private final Long paramSeqKey;

	/**
	 * [試算パラメータ] 最終更新者名
	 */
	private final String paramUpJgiName;

	/**
	 * [試算パラメータ] 最終更新日
	 */
	private final Date paramUpDate;

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * [試算パラメータ] 留保率
	 */
	private final Integer indexRyhRtsu;
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * [試算パラメータ] 指数(未獲得市場)
	 */
	private final Integer indexMikakutoku;

	/**
	 * [試算パラメータ] 指数(納入実績)
	 */
	private final Integer indexDelivery;

	/**
	 * [試算パラメータ] 指数(フリー項目1)
	 */
	private final Integer indexFree1;

	/**
	 * [試算パラメータ] 指数(フリー項目2)
	 */
	private final Integer indexFree2;

	/**
	 * [試算パラメータ] 指数(フリー項目3)
	 */
	private final Integer indexFree3;

	/**
	 * [試算パラメータ] フリー項目名称1
	 */
	private final String indexFreeName1;

	/**
	 * [試算パラメータ] フリー項目名称2
	 */
	private final String indexFreeName2;

	/**
	 * [試算パラメータ] フリー項目名称3
	 */
	private final String indexFreeName3;

	/**
	 * [試算パラメータ] 本部フラグ TRUE=本部, FALSE=本部でない
	 */
	private final Boolean isParamHonbu;

	/**
	 * [試算パラメータ] 営業所フラグ TRUE=営業所, FALSE=営業所でない
	 */
	private final Boolean isParamOffice;

	/**
	 * チーム別計画編集可否フラグ TRUE=可能, FALSE=不可能
	 */
	private final Boolean canTeamPlanEdit;

	/**
	 * 担当者別計画編集可否フラグ TRUE=可能, FALSE=不可能
	 */
	private final Boolean canMrPlanEdit;

	/**
	 * [担当者別計画] 最終更新者名
	 */
	private final String mrPlanUpJgiName;

	/**
	 * [担当者別計画] 最終更新日
	 */
	private final Date mrPlanUpDate;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode [立案対象品目] 品目固定コード
	 * @param prodName [立案対象品目] 品目名称
	 * @param prodType [立案対象品目] 製品区分
	 * @param estProdCode [試算品目] 品目固定コード
	 * @param estProdName [試算品目] 品目名称
	 * @param estProdType [試算品目] 製品区分
	 * @param paramSeqKey [試算パラメータ] シーケンスキー
	 * @param paramUpJgiName [試算パラメータ] 最終更新者名
	 * @param paramUpDate [試算パラメータ] 最終更新日
	 * @param indexMikakutoku [試算パラメータ] 指数(未獲得市場)
	 * @param indexDelivery [試算パラメータ] 指数(納入実績)
	 * @param indexFree1 [試算パラメータ] 指数(フリー項目1)
	 * @param indexFree2 [試算パラメータ] 指数(フリー項目2)
	 * @param indexFree3 [試算パラメータ] 指数(フリー項目3)
	 * @param indexFreeName1 [試算パラメータ] フリー項目名称1
	 * @param indexFreeName2 [試算パラメータ] フリー項目名称2
	 * @param indexFreeName3 [試算パラメータ] フリー項目名称3
	 * @param isParamHonbu [試算パラメータ] 本部フラグ
	 * @param isParamOffice [試算パラメータ] 営業所フラグ
	 * @param canTeamPlanEdit チーム別計画編集可否フラグ
	 * @param canMrPlanEdit 担当者別計画編集可否フラグ
	 */
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//	public EstimationResultDetailResultInfoDto(String prodCode, String prodName, String prodType, String estProdCode, String estProdName, String estProdType, Long paramSeqKey,
//			String paramUpJgiName, Date paramUpDate, Integer indexMikakutoku, Integer indexDelivery, Integer indexFree1, Integer indexFree2, Integer indexFree3, String indexFreeName1,
//			String indexFreeName2, String indexFreeName3, Boolean isParamHonbu, Boolean isParamOffice, Boolean canTeamPlanEdit, Boolean canMrPlanEdit, MrPlan mrPlan) {
	public EstimationResultDetailResultInfoDto(String prodCode, String prodName, String prodType, String estProdCode, String estProdName, String estProdType, Long paramSeqKey,
			String paramUpJgiName, Date paramUpDate, Integer indexRyhRtsu, Integer indexMikakutoku, Integer indexDelivery, Integer indexFree1, Integer indexFree2, Integer indexFree3, String indexFreeName1,
			String indexFreeName2, String indexFreeName3, Boolean isParamHonbu, Boolean isParamOffice, Boolean canTeamPlanEdit, Boolean canMrPlanEdit, MrPlan mrPlan) {
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodType = prodType;
		this.estProdCode = estProdCode;
		this.estProdName = estProdName;
		this.estProdType = estProdType;
		this.paramSeqKey = paramSeqKey;
		this.paramUpJgiName = paramUpJgiName;
		this.paramUpDate = paramUpDate;
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexRyhRtsu = indexRyhRtsu;
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexMikakutoku = indexMikakutoku;
		this.indexDelivery = indexDelivery;
		this.indexFree1 = indexFree1;
		this.indexFree2 = indexFree2;
		this.indexFree3 = indexFree3;
		this.indexFreeName1 = indexFreeName1;
		this.indexFreeName2 = indexFreeName2;
		this.indexFreeName3 = indexFreeName3;
		this.isParamHonbu = isParamHonbu;
		this.isParamOffice = isParamOffice;
		this.canTeamPlanEdit = canTeamPlanEdit;
		this.canMrPlanEdit = canMrPlanEdit;
		if (mrPlan == null) {
			this.mrPlanUpJgiName = null;
			this.mrPlanUpDate = null;
		} else {
			this.mrPlanUpJgiName = mrPlan.getUpJgiName();
			this.mrPlanUpDate = mrPlan.getUpDate();
		}
	}

	/**
	 * [立案対象品目] 品目固定コード を取得する。
	 * 
	 * @return prodCode [立案対象品目] 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * [立案対象品目] 品目名称 を取得する。
	 * 
	 * @return prodName [立案対象品目] 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * [立案対象品目] 製品区分 を取得する。
	 * 
	 * @return prodType [立案対象品目] 製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * [試算品目] 品目固定コード を取得する。
	 * 
	 * @return estProdCode [試算品目] 品目固定コード
	 */
	public String getEstProdCode() {
		return estProdCode;
	}

	/**
	 * [試算品目] 品目名称 を取得する。
	 * 
	 * @return estProdName [試算品目] 品目名称
	 */
	public String getEstProdName() {
		return estProdName;
	}

	/**
	 * [試算品目] 製品区分 を取得する。
	 * 
	 * @return estProdType [試算品目] 製品区分
	 */
	public String getEstProdType() {
		return estProdType;
	}

	/**
	 * [試算パラメータ] シーケンスキー を取得する。
	 * 
	 * @return paramSeqKey [試算パラメータ] シーケンスキー
	 */
	public Long getParamSeqKey() {
		return paramSeqKey;
	}

	/**
	 * [試算パラメータ] 最終更新者名 を取得する。
	 * 
	 * @return paramUpJgiName [試算パラメータ] 最終更新者名
	 */
	public String getParamUpJgiName() {
		return paramUpJgiName;
	}

	/**
	 * [試算パラメータ] 最終更新日 を取得する。
	 * 
	 * @return paramUpDate [試算パラメータ] 最終更新日
	 */
	public Date getParamUpDate() {
		return paramUpDate;
	}

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * [試算パラメータ] 留保率 を取得する。
	 * 
	 * @return indexRyhRtsu [試算パラメータ] 留保率
	 */
	public Integer getIndexRyhRtsu() {
		return indexRyhRtsu;
	}
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * [試算パラメータ] 指数(未獲得市場) を取得する。
	 * 
	 * @return indexMikakutoku [試算パラメータ] 指数(未獲得市場)
	 */
	public Integer getIndexMikakutoku() {
		return indexMikakutoku;
	}

	/**
	 * [試算パラメータ] 指数(納入実績) を取得する。
	 * 
	 * @return indexDelivery [試算パラメータ] 指数(納入実績)
	 */
	public Integer getIndexDelivery() {
		return indexDelivery;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目1) を取得する。
	 * 
	 * @return indexFree1 [試算パラメータ] 指数(フリー項目1)
	 */
	public Integer getIndexFree1() {
		return indexFree1;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目2) を取得する。
	 * 
	 * @return indexFree2 [試算パラメータ] 指数(フリー項目2)
	 */
	public Integer getIndexFree2() {
		return indexFree2;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目3) を取得する。
	 * 
	 * @return indexFree3 [試算パラメータ] 指数(フリー項目3)
	 */
	public Integer getIndexFree3() {
		return indexFree3;
	}

	/**
	 * [試算パラメータ] フリー項目名称1 を取得する。
	 * 
	 * @return indexFreeName1 [試算パラメータ] フリー項目名称1
	 */
	public String getIndexFreeName1() {
		return indexFreeName1;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目2) を取得する。
	 * 
	 * @return indexFreeName2 [試算パラメータ] 指数(フリー項目2)
	 */
	public String getIndexFreeName2() {
		return indexFreeName2;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目3) を取得する。
	 * 
	 * @return indexFreeName3 [試算パラメータ] 指数(フリー項目3)
	 */
	public String getIndexFreeName3() {
		return indexFreeName3;
	}

	/**
	 * [試算パラメータ] 本部フラグ を取得する。
	 * 
	 * @return isParamHonbu [試算パラメータ] 本部フラグ TRUE=本部, FALSE=本部でない
	 */
	public Boolean getIsParamHonbu() {
		return isParamHonbu;
	}

	/**
	 * [試算パラメータ] 営業所フラグ を取得する。
	 * 
	 * @return isParamOffice [試算パラメータ] 営業所フラグ TRUE=営業所, FALSE=営業所でない
	 */
	public Boolean getIsParamOffice() {
		return isParamOffice;
	}

	/**
	 * チーム別計画編集可否フラグ を取得する。
	 * 
	 * @return canTeamPlanEdit チーム別計画編集可否フラグ TRUE=可能, FALSE=不可能
	 */
	public Boolean getCanTeamPlanEdit() {
		return canTeamPlanEdit;
	}

	/**
	 * 担当者別計画編集可否フラグ を取得する。
	 * 
	 * @return canMrPlanEdit 担当者別計画編集可否フラグ TRUE=可能, FALSE=不可能
	 */
	public Boolean getCanMrPlanEdit() {
		return canMrPlanEdit;
	}

	/**
	 * [担当者別計画] 最終更新者名 を取得する。
	 * 
	 * @return mrPlanUpJgiName [担当者別計画] 最終更新者名
	 */
	public String getMrPlanUpJgiName() {
		return mrPlanUpJgiName;
	}

	/**
	 * [担当者別計画] 最終更新日 を取得する。
	 * 
	 * @return mrPlanUpDate [担当者別計画] 最終更新日
	 */
	public Date getMrPlanUpDate() {
		return mrPlanUpDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
