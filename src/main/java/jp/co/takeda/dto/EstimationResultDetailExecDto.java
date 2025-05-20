package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 試算結果詳細の試算処理実行用DTO
 *
 * @author stakeuchi
 */
public class EstimationResultDetailExecDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * [立案対象品目] 品目固定コード
	 */
	private final String prodCode;

	/**
	 * [立案対象品目] 品目名称
	 */
	private final String prodName;

	/**
	 * [試算パラメータ] シーケンスキー
	 */
	private final Long paramSeqKey;

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
	 * カテゴリ
	 */
	private final String category;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param prodCode [立案対象品目] 品目固定コード
	 * @param prodName [立案対象品目] 品目名称
	 * @param paramSeqKey [試算パラメータ] シーケンスキー
	 * @param paramUpDate [試算パラメータ] 最終更新日
	 * @param indexMikakutoku [試算パラメータ] 指数(未獲得市場)
	 * @param indexDelivery [試算パラメータ] 指数(納入実績)
	 * @param indexFree1 [試算パラメータ] 指数(フリー項目1)
	 * @param indexFree2 [試算パラメータ] 指数(フリー項目2)
	 * @param indexFree3 [試算パラメータ] 指数(フリー項目3)
	 * @param category カテゴリ
	 */
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//	public EstimationResultDetailExecDto(String sosCd3, String prodCode, String prodName, Long paramSeqKey, Date paramUpDate, Integer indexMikakutoku, Integer indexDelivery,
//			Integer indexFree1, Integer indexFree2, Integer indexFree3) {
	public EstimationResultDetailExecDto(String sosCd3, String prodCode, String prodName, Long paramSeqKey, Date paramUpDate, Integer indexRyhRtsu, Integer indexMikakutoku, Integer indexDelivery,
			Integer indexFree1, Integer indexFree2, Integer indexFree3, String category) {
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.sosCd3 = sosCd3;
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.paramSeqKey = paramSeqKey;
		this.paramUpDate = paramUpDate;
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexRyhRtsu = indexRyhRtsu;
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexMikakutoku = indexMikakutoku;
		this.indexDelivery = indexDelivery;
		this.indexFree1 = indexFree1;
		this.indexFree2 = indexFree2;
		this.indexFree3 = indexFree3;
		this.category = category;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * [立案対象品目] 品目固定コードを取得する。
	 *
	 * @return prodCode [立案対象品目] 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * [立案対象品目] 品目名称を取得する。
	 *
	 * @return prodName [立案対象品目] 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * [試算パラメータ] シーケンスキーを取得する。
	 *
	 * @return paramSeqKey [試算パラメータ] シーケンスキー
	 */
	public Long getParamSeqKey() {
		return paramSeqKey;
	}

	/**
	 * [試算パラメータ] 最終更新日を取得する。
	 *
	 * @return paramUpDate [試算パラメータ] 最終更新日
	 */
	public Date getParamUpDate() {
		return paramUpDate;
	}

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * [試算パラメータ] 留保率を取得する。
	 *
	 * @return indexMikakutoku [試算パラメータ] 留保率
	 */
	public Integer getIndexRyhRtsu() {
		return indexRyhRtsu;
	}
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * [試算パラメータ] 指数(未獲得市場)を取得する。
	 *
	 * @return indexMikakutoku [試算パラメータ] 指数(未獲得市場)
	 */
	public Integer getIndexMikakutoku() {
		return indexMikakutoku;
	}

	/**
	 * [試算パラメータ] 指数(納入実績)を取得する。
	 *
	 * @return indexDelivery [試算パラメータ] 指数(納入実績)
	 */
	public Integer getIndexDelivery() {
		return indexDelivery;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目1)を取得する。
	 *
	 * @return indexFree1 [試算パラメータ] 指数(フリー項目1)
	 */
	public Integer getIndexFree1() {
		return indexFree1;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目2)を取得する。
	 *
	 * @return indexFree2 [試算パラメータ] 指数(フリー項目2)
	 */
	public Integer getIndexFree2() {
		return indexFree2;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目3)を取得する。
	 *
	 * @return indexFree3 [試算パラメータ] 指数(フリー項目3)
	 */
	public Integer getIndexFree3() {
		return indexFree3;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
