package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.TytenKisLevel;

/**
 * (ワ)特約店別計画サマリーの検索条件(Search Condition)を表すDTO
 *
 * @author khashimoto
 */
public class WsPlanForVacSummaryScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店コード(部分一致)
	 */
	private final String tmsTytenCdPart;

	/**
	 * 集約する特約店の階層レベル
	 */
	private final TytenKisLevel tytenKisLevel;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分
	 */
//	private final KaBaseKbForVac kaBaseKb = KaBaseKbForVac.S_KA_BASE;
	private final KaBaseKb kaBaseKb;


	/**
	 * コンストラクタ
	 *
	 * @param tmsTytenCdPart 特約店コード(部分一致)
	 * @param tytenKisLevel 集約する特約店の階層レベル
	 * @param kaBaseKb 価ベース区分
	 */
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	public WsPlanForVacSummaryScDto(String tmsTytenCdPart, TytenKisLevel tytenKisLevel, KaBaseKb kaBaseKb) {
		this.tmsTytenCdPart = tmsTytenCdPart;
		this.tytenKisLevel = tytenKisLevel;
		this.kaBaseKb = kaBaseKb;
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	}

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
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
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
