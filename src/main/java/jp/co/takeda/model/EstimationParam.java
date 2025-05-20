package jp.co.takeda.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.model.div.EstimationBase;

/**
 * 試算パラメータを表すクラス
 * 
 * @author tkawabata
 */
public class EstimationParam implements Serializable {

	/**
	 * デフォルトフリー項目名称1
	 */
	public static final String DEFAULT_INDEX_FREE_NAME1 = "フリー項目①";
	/**
	 * デフォルトフリー項目名称2
	 */
	public static final String DEFAULT_INDEX_FREE_NAME2 = "フリー項目②";
	/**
	 * デフォルトフリー項目名称3
	 */
	public static final String DEFAULT_INDEX_FREE_NAME3 = "フリー項目③";

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 試算元計算値
	 */
	private EstimationBase estimationBase;

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * 留保率
	 */
	private Integer indexRyhRtsu;
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * 指数(未獲得市場)
	 */
	private Integer indexMikakutoku;

	/**
	 * 指数(納入実績)
	 */
	private Integer indexDelivery;

	/**
	 * 指数(フリー項目1)
	 */
	private Integer indexFree1;

	/**
	 * 指数(フリー項目2)
	 */
	private Integer indexFree2;

	/**
	 * 指数(フリー項目3)
	 */
	private Integer indexFree3;

	/**
	 * 指数名称(フリー項目1)
	 */
	private String indexFreeName1;

	/**
	 * 指数名称(フリー項目2)
	 */
	private String indexFreeName2;

	/**
	 * 指数名称(フリー項目3)
	 */
	private String indexFreeName3;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 試算元計算値を取得する。
	 * 
	 * @return 試算元計算値
	 */
	public EstimationBase getEstimationBase() {
		return estimationBase;
	}

	/**
	 * 試算元計算値を設定する。
	 * 
	 * @param estimationBase 試算元計算値
	 */
	public void setEstimationBase(EstimationBase estimationBase) {
		this.estimationBase = estimationBase;
	}

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * 留保率を取得する。
	 * 
	 * @return 留保率
	 */
	public Integer getIndexRyhRtsu() {
		return indexRyhRtsu;
	}

	/**
	 * 留保率を設定する。
	 * 
	 * @param indexRyhRtsu 留保率
	 */
	public void setIndexRyhRtsu(Integer indexRyhRtsu) {
		this.indexRyhRtsu = indexRyhRtsu;
	}
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * 指数(未獲得市場)を取得する。
	 * 
	 * @return 指数(未獲得市場)
	 */
	public Integer getIndexMikakutoku() {
		return indexMikakutoku;
	}

	/**
	 * 指数(未獲得市場)を設定する。
	 * 
	 * @param indexMikakutoku 指数(未獲得市場)
	 */
	public void setIndexMikakutoku(Integer indexMikakutoku) {
		this.indexMikakutoku = indexMikakutoku;
	}

	/**
	 * 指数(納入実績)を取得する。
	 * 
	 * @return 指数(納入実績)
	 */
	public Integer getIndexDelivery() {
		return indexDelivery;
	}

	/**
	 * 指数(納入実績)を設定する。
	 * 
	 * @param indexDelivery 指数(納入実績)
	 */
	public void setIndexDelivery(Integer indexDelivery) {
		this.indexDelivery = indexDelivery;
	}

	/**
	 * 指数(フリー項目1)を取得する。
	 * 
	 * @return 指数(フリー項目1)
	 */
	public Integer getIndexFree1() {
		return indexFree1;
	}

	/**
	 * 指数(フリー項目1)を設定する。
	 * 
	 * @param indexFree1 指数(フリー項目1)
	 */
	public void setIndexFree1(Integer indexFree1) {
		this.indexFree1 = indexFree1;
	}

	/**
	 * 指数(フリー項目2)を取得する。
	 * 
	 * @return 指数(フリー項目2)
	 */
	public Integer getIndexFree2() {
		return indexFree2;
	}

	/**
	 * 指数(フリー項目2)を設定する。
	 * 
	 * @param indexFree2 指数(フリー項目2)
	 */
	public void setIndexFree2(Integer indexFree2) {
		this.indexFree2 = indexFree2;
	}

	/**
	 * 指数(フリー項目3)を取得する。
	 * 
	 * @return 指数(フリー項目3)
	 */
	public Integer getIndexFree3() {
		return indexFree3;
	}

	/**
	 * 指数(フリー項目3)を設定する。
	 * 
	 * @param indexFree3 指数(フリー項目3)
	 */
	public void setIndexFree3(Integer indexFree3) {
		this.indexFree3 = indexFree3;
	}

	/**
	 * 指数(フリー項目名称1)を取得する。
	 * 
	 * @return 指数(フリー項目名称1)
	 */
	public String getIndexFreeName1() {
		return indexFreeName1;
	}

	/**
	 * 指数(フリー項目名称1)を設定する。
	 * 
	 * @param indexFreeName1 指数(フリー項目名称1)
	 */
	public void setIndexFreeName1(String indexFreeName1) {
		this.indexFreeName1 = indexFreeName1;
	}

	/**
	 * 指数(フリー項目名称2)を取得する。
	 * 
	 * @return 指数(フリー項目名称2)
	 */
	public String getIndexFreeName2() {
		return indexFreeName2;
	}

	/**
	 * 指数(フリー項目名称2)を設定する。
	 * 
	 * @param indexFreeName2 指数(フリー項目名称2)
	 */
	public void setIndexFreeName2(String indexFreeName2) {
		this.indexFreeName2 = indexFreeName2;
	}

	/**
	 * 指数(フリー項目名称3)を取得する。
	 * 
	 * @return 指数(フリー項目名称3)
	 */
	public String getIndexFreeName3() {
		return indexFreeName3;
	}

	/**
	 * 指数(フリー項目名称3)を設定する。
	 * 
	 * @param indexFreeName3 指数(フリー項目名称3)
	 */
	public void setIndexFreeName3(String indexFreeName3) {
		this.indexFreeName3 = indexFreeName3;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
