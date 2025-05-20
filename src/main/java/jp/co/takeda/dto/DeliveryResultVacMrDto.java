package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 過去実績参照画面（担当者別計画）の検索条件(Search Condition)を表すDTO
 * 
 * @author siwamoto
 */
public class DeliveryResultVacMrDto extends DpDto {

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
	 * 品目固定コード
	 */
	final private String prodCode;

	/**
	 * 施設出力対象区分
	 */
	final private InsType insType;

	/**
	 * 雑担当フラグ
	 */
	final private Boolean sloppyChargeFlg;

	/**
	 * 参照年月のリスト
	 */
	final private List<Date> refDateList;

	/**
	 * 納入実績UH
	 */
	final private List<Long> monNnuUH;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo
	 * @param jgiName
	 * @param prodCode
	 * @param insType
	 * @param sloppyChargeFlg
	 * @param monNnuUH
	 * @param refDateList
	 */
	public DeliveryResultVacMrDto(String sosName, Integer jgiNo, String jgiName, String prodCode, InsType insType, Boolean sloppyChargeFlg, List<Long> monNnuUH,
		List<Date> refDateList) {
		super();
		this.sosName = sosName;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.prodCode = prodCode;
		this.insType = insType;
		this.sloppyChargeFlg = sloppyChargeFlg;
		this.monNnuUH = monNnuUH;
		this.refDateList = refDateList;
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
	 * @return refDateList
	 */
	public List<Date> getRefDateList() {
		return refDateList;
	}

	/**
	 * 納入実績UHを取得する。
	 * 
	 * @return monNnuUH
	 */
	public List<Long> getMonNnuUH() {
		return monNnuUH;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
