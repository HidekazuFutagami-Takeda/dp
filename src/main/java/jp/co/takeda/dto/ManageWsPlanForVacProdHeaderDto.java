package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)特約店品目別計画編集画面ヘッダを表すDTO
 *
 * @author khashimoto
 */
public class ManageWsPlanForVacProdHeaderDto extends ManageWsPlanHeaderDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private final boolean planTaiGaiFlgTok;

	/**
	 * コンストラクタ
	 *
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenName 特約店名称
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public ManageWsPlanForVacProdHeaderDto(String tmsTytenCd, String tmsTytenName, boolean planTaiGaiFlgTok) {
		super(tmsTytenCd, tmsTytenName);
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * 計画立案対象外フラグ(当期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
