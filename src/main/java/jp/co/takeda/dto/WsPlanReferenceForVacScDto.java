package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.TytenKisLevel;

/**
 * ワクチン用特約店別計画参照画面検索条件を表すDTO
 *
 * @author stakeuchi
 */
public class WsPlanReferenceForVacScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店コード(部分一致)
	 */
	private final String tmsTytenCdPart;

	/**
	 * 集約する特約店の階層レベル
	 */
	private final TytenKisLevel tytenKisLevel;

	/**
	 * 品目カテゴリ
	 */
	private final String category;

	/**
	 * 価ベース区分
	 */
	private final KaBaseKb kaBaseKb;

	/**
	 * コンストラクタ
	 *
	 * @param tmsTytenCdPart 特約店コード(部分一致)
	 * @param tytenKisLevel 集約する特約店の階層レベル
	 */
	public WsPlanReferenceForVacScDto(String tmsTytenCdPart, TytenKisLevel tytenKisLevel
			, String category, KaBaseKb kaBaseKb) {
		this.tmsTytenCdPart = tmsTytenCdPart;
		this.tytenKisLevel = tytenKisLevel;
		this.category = category;
		this.kaBaseKb = kaBaseKb;
	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店コード(部分一致)を取得する。
	 *
	 * @return 特約店コード(部分一致)
	 */
	public String getTmsTytenCdPart() {
		return tmsTytenCdPart;
	}

	/**
	 * 集約する特約店の階層レベルを取得する。
	 *
	 * @return 集約する特約店の階層レベル
	 */
	public TytenKisLevel getTytenKisLevel() {
		return tytenKisLevel;
	}

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return 品目カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 価ベース区分を取得する。
	 *
	 * @return 価ベース区分
	 */
	public KaBaseKb getKaBaseKb() {
		return kaBaseKb;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
