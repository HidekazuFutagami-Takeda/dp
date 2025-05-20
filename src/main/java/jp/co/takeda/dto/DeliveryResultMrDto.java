package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 過去実績参照画面(担当者別計画)の検索条件(Search Condition)を表すDTO
 *
 * @author siwamoto
 */
public class DeliveryResultMrDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 組織名
	 */
	final private String sosName;

	/**
	 * 従業員番号
	 */
	final private Integer jgiNo;

	/**
	 * 従業員名
	 */
	final private String jgiName;

	/**
	 * 職種名
	 */
	final private String shokushuName;

	/**
	 * 品目固定コード
	 */
	final private String prodCode;

	//add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	/**
	 * 品目
	 */
	final private String prodName;
	//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する

	/**
	 * 施設出力対象区分
	 */
	final private InsType insType;

	/**
	 * 雑担当フラグ
	 */
	final private Boolean sloppyChargeFlg;

	/**
	 * 納入実績UH
	 */
	final private List<Long> monNnuUH;

	/**
	 * 納入実績P
	 */
	final private List<Long> monNnuP;

	/**
	 * 納入実績合計
	 */
	final private List<Long> monNnuSum;

	/**
	 * 参照年月のリスト
	 */
	final private List<Date> refDateList;

	/**
	 * コンストラクタ
	 *
	 * @param sosName 組織名
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param sloppyChargeFlg 雑担当フラグ
	 * @param monNnuUH 納入実績UH
	 * @param monNnuP 納入実績P
	 * @param monNnuSum 納入実績合計
	 * @param refDateList 参照年月のリスト
	 * @param shokushuName 職種名
	 */
	public DeliveryResultMrDto(String sosName, Integer jgiNo, String jgiName, String prodCode, InsType insType, Boolean sloppyChargeFlg, List<Long> monNnuUH, List<Long> monNnuP,
		List<Long> monNnuSum, List<Date> refDateList, String shokushuName, String prodName) {
		this.sosName = sosName;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.prodCode = prodCode;
		this.insType = insType;
		this.sloppyChargeFlg = sloppyChargeFlg;
		this.monNnuUH = monNnuUH;
		this.monNnuP = monNnuP;
		this.monNnuSum = monNnuSum;
		this.refDateList = refDateList;
		this.shokushuName = shokushuName;
		//add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
		this.prodName = prodName;
		//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	}

	//---------------------
	// Getter
	// --------------------

	/**
	 * 組織名を取得する。
	 *
	 * @return 組織名
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	//add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	/**
	 * 品目を取得する。
	 *
	 * @return 品目
	 */
	public String getProdName() {
		return prodName;
	}
	//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する

	/**
	 * 施設出力対象区分を取得する。
	 *
	 * @return 施設出力対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 雑担当フラグを取得する。
	 *
	 * @return 雑担当フラグ
	 */
	public Boolean getSloppyChargeFlg() {
		return sloppyChargeFlg;
	}

	/**
	 * 参照期間リストを取得する。
	 *
	 * @return 参照期間リスト
	 */
	public List<Date> getRefDateList() {
		return refDateList;
	}

	/**
	 * 納入実績UHを取得する。
	 *
	 * @return 納入実績UH
	 */
	public List<Long> getMonNnuUH() {
		return monNnuUH;
	}

	/**
	 * 納入実績P行を取得する。
	 *
	 * @return 納入実績P行
	 */
	public List<Long> getMonNnuP() {
		return monNnuP;
	}

	/**
	 * 納入実績合計行を取得する。
	 *
	 * @return 納入実績合計行
	 */
	public List<Long> getMonNnuSum() {
		return monNnuSum;
	}

	/**
	 * 職種名
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
