package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (医)施設特約店品目別計画編集画面ヘッダを表すDTO
 *
 * @author siwamoto
 */
public class ManageInsWsPlanProdHeaderDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 *
	 * @param InsMstResultDto 施設情報
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenName 特約店名称
	 * @param prodCategoryName カテゴリ
	 * @param planTaiGaiFlgTok 計画立案対象外フラグ(当期用)
	 *
	 */
	public ManageInsWsPlanProdHeaderDto(InsMstResultDto insMstResultDto, String tmsTytenCd, String tmsTytenName, String prodCategoryName, boolean planTaiGaiFlgTok) {
		this.insMstResultDto = insMstResultDto;
		this.tmsTytenCd = tmsTytenCd;
		this.tmsTytenName = tmsTytenName;
		this.prodCategoryName = prodCategoryName;
		this.planTaiGaiFlgTok = planTaiGaiFlgTok;
	}

	/**
	 * 施設情報 検索結果DTO<br>
	 * 検索時、施設コードが入力されいない場合はNull値となる。
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
	 * カテゴリ名
	 */
	private final String prodCategoryName;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private boolean planTaiGaiFlgTok;

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
	 * 品目カテゴリ名を取得する。
	 *
	 * @return 品目カテゴリ名
	 */
	public String getProdCategoryName() {
		return prodCategoryName;
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
