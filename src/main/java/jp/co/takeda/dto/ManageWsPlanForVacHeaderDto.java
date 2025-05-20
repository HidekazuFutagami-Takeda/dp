package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特約店別計画編集画面ヘッダを表すDTO
 * 
 * @author siwamoto
 */
public class ManageWsPlanForVacHeaderDto extends ManageWsPlanHeaderDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenName 特約店名称
	 */
	public ManageWsPlanForVacHeaderDto(String tmsTytenCd, String tmsTytenName) {
		super(tmsTytenCd, tmsTytenName);
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
