package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)施設特約店別計画編集画面を表すDTO
 *
 * @author siwamoto
 */
public class ManageInsWsPlanForVacHeaderDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設情報 検索結果DTO
	 *
	 * <pre>
	 * 検索時、施設コードが入力されいない場合はNull値となる。
	 * </pre>
	 */
	private final InsMstResultDto insMstResultDto;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 特約店名称
	 */
	private final String tmsTytenName;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private boolean planTaiGaiFlgTok;

	/**
	 * コンストラクタ
	 *
	 * @param InsMstResultDto 施設情報
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenName 特約店名称
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 */
	public ManageInsWsPlanForVacHeaderDto(InsMstResultDto insMstResultDto, String tmsTytenCd, String tmsTytenName, boolean planTaiGaiFlgTok) {
		this.insMstResultDto = insMstResultDto;
		this.tmsTytenCd = tmsTytenCd;
		this.tmsTytenName = tmsTytenName;
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * 施設情報を取得する。
	 *
	 * @return insMstResultDto 施設情報
	 */
	public InsMstResultDto getInsMstResultDto() {
		return insMstResultDto;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店名称を取得する。
	 *
	 * @return tmsTytenName 特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
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
