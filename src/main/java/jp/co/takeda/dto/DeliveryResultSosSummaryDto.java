package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 納入実績の組織単位のサマリーを表すDTO
 * 
 * @author khashimoto
 */
public class DeliveryResultSosSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 施設出力対象区分
	 */
	private final InsType insType;

	/**
	 * 納入実績
	 */
	private final MonNnu monNnu;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param monNnu 納入実績
	 */
	public DeliveryResultSosSummaryDto(String sosCd, String prodCode, InsType insType, MonNnu monNnu) {

		this.sosCd = sosCd;
		this.prodCode = prodCode;
		this.insType = insType;
		this.monNnu = monNnu;
	}

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 組織コードを取得する。
	 * 
	 * @return 組織コード
	 */
	public String getSosCde() {
		return sosCd;
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
	 * 納入実績を取得する。
	 * 
	 * @return 納入実績
	 */
	public MonNnu getMonNnu() {
		return monNnu;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
