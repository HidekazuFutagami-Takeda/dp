package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.TytenKisLevel;

/**
 * ワクチン用特約店別計画編集 検索条件DTO
 *
 * @author stakeuchi
 */
public class TmsTytenPlanEditForVacScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 特約店コード(部分一致)
	 */
	private String tmsTytenCdPart;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 集約方法
	 */
	private String tytenKisLevel;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分
	 */
	private KaBaseKb kaBaseKb;


	/**
	 * コンストラクタ
	 *
	 * @param tmsTytenCd 特約店コード
	 * @param prodCode 品目固定コード
	 * @param kaBaseKb 価ベース区分
	 */
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	public TmsTytenPlanEditForVacScDto(String tmsTytenCd, String prodCode, String tytenKisLevel, KaBaseKb kaBaseKb) {
		this.tmsTytenCd = tmsTytenCd;
		this.prodCode = prodCode;
		this.tytenKisLevel = tytenKisLevel;
		this.kaBaseKb = kaBaseKb;
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		if(tytenKisLevel.equals((TytenKisLevel.HONTEN.getDbValue()))){
			this.tmsTytenCdPart = tmsTytenCd.substring(0,3) + '%';
		}
		else if(tytenKisLevel.equals((TytenKisLevel.SHISHA.getDbValue()))){
			this.tmsTytenCdPart = tmsTytenCd.substring(0,5) + '%';
		}
		else if(tytenKisLevel.equals((TytenKisLevel.SHITEN.getDbValue()))){
			this.tmsTytenCdPart = tmsTytenCd.substring(0,7) + '%';
		}
		else if(tytenKisLevel.equals((TytenKisLevel.KA.getDbValue()))){
			this.tmsTytenCdPart = tmsTytenCd.substring(0,9) + '%';
		}
		else if(tytenKisLevel.equals((TytenKisLevel.BLK1.getDbValue()))){
			this.tmsTytenCdPart = tmsTytenCd.substring(0,11) + '%';
		}
		else if(tytenKisLevel.equals((TytenKisLevel.BLK2.getDbValue()))){
			this.tmsTytenCdPart = tmsTytenCd;
		}

	}

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 特約店コードを取得する
	 *
	 * @return 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
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
	 * 品目固定コードを取得する
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 集約方法を取得する
	 *
	 * @return 集約方法
	 */
	public String getTytenKisLevel() {
		return tytenKisLevel;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分を取得する
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
