package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.PlanData;

/**
 * 特約店品目別計画の検索用DTO
 *
 * @author siwamoto
 */
public class ManageWsPlanProdScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * カテゴリ
	 */
	private final String prodCategory;

	/**
	 * 計画
	 */
	private final PlanData planData;

	/**
	 * ワクチンフラグ
	 */
	private final boolean isVaccine;

   /**
     * 流通フラグ
     */
    private final boolean ryutsu;

    /**
     * UH用タイトル
     */
    private final String titleUH;

    /**
     * P用タイトル
     */
    private final String titleP;

    /**
     * Z用タイトル
     */
    private final String titleZ;

    /**
	 * コンストラクタ
	 *
	 * @param tmsTytenCd 特約店コード
	 * @param prodCategory カテゴリ
	 * @param planData 計画
	 * @param isVaccine
	 */
	public ManageWsPlanProdScDto(String tmsTytenCd, String prodCategory, PlanData planData, boolean isVaccine, boolean ryutsu,
	        String titleUH, String titleP, String titleZ) {
		super();
		this.tmsTytenCd = tmsTytenCd;
		this.prodCategory = prodCategory;
		this.planData = planData;
		this.isVaccine = isVaccine;
		this.ryutsu = ryutsu;
		this.titleUH = titleUH;
		this.titleP = titleP;
		this.titleZ = titleZ;

	}


    /**
	 * 特約店コードdを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 計画を取得する。
	 *
	 * @return planData 計画
	 */
	public PlanData getPlanData() {
		return planData;
	}

	/**
	 * @return isVaccine
	 */
	public boolean isVaccine() {
		return isVaccine;
	}

	/**
     * @return ryutsu
     */
    public boolean isRyutsu() {
        return ryutsu;
    }


    /**
     * @return titleUH
     */
    public String getTitleUH() {
        return titleUH;
    }


    /**
     * @return titleP
     */
    public String getTitleP() {
        return titleP;
    }


    /**
     * @return titleZ
     */
    public String getTitleZ() {
        return titleZ;
    }


    @Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
