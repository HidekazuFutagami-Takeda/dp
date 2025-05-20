package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MonNnu;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用納入実績の組織単位のサマリーを表すDTO
 * 
 * @author nozaki
 */
public class DeliveryResultForVacSosSummaryDto extends DpDto {

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
	 * 納入実績
	 */
	private final MonNnu monNnu;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード
	 * @param prodCode 品目固定コード
	 * @param monNnu 納入実績
	 */
	public DeliveryResultForVacSosSummaryDto(String sosCd, String prodCode, MonNnu monNnu) {
		this.sosCd = sosCd;
		this.prodCode = prodCode;
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
