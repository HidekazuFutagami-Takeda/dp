package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.view.DistributionProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画配分対象品目 検索結果用DTO
 * 
 * @author stakeuchi
 */
public class InsWsDistProdResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 配分対象品目
	 */
	private final DistributionProd distributionProd;

	/**
	 * 本部/営業所判断フラグ(true：本部、false：営業所)
	 */
	private final boolean isHonbu;

	/**
	 * [UH]試算配分共通パラメータ
	 * <ul>
	 * <li>配分品目情報(品目名称、製品区分)</li>
	 * <li>参照品目名称1</li>
	 * <li>参照品目名称2</li>
	 * <li>参照品目名称3</li>
	 * </ul>
	 */
	private final BaseParam baseParamUH;

	/**
	 * [UH]実績参照期間FROM
	 */
	private final Date refPeriodFromUH;

	/**
	 * [UH]実績参照期間TO
	 */
	private final Date refPeriodToUH;

	/**
	 * [P]試算配分共通パラメータ
	 * <ul>
	 * <li>配分品目情報(品目名称、製品区分)</li>
	 * <li>参照品目名称1</li>
	 * <li>参照品目名称2</li>
	 * <li>参照品目名称3</li>
	 * </ul>
	 */
	private final BaseParam baseParamP;

	/**
	 * [P]実績参照期間FROM
	 */
	private final Date refPeriodFromP;

	/**
	 * [P]実績参照期間TO
	 */
	private final Date refPeriodToP;

	/**
	 * 配分基準最終更新日
	 */
	private final Date lastUpdate;

	/**
	 * コンストラクタ
	 * 
	 * @param plannedProd 計画対象品目
	 * @param isHonbu 本部/営業所判断フラグ
	 * @param baseParamUH [UH]試算配分共通パラメータ
	 * @param refPeriodFromUH [UH]実績参照期間FROM
	 * @param refPeriodToUH [UH]実績参照期間TO
	 * @param baseParamP [P]試算配分共通パラメータ
	 * @param refPeriodFromP [P]実績参照期間FROM
	 * @param refPeriodToP [P]実績参照期間TO
	 * @param lastUpdate 配分基準最終更新日
	 */
	public InsWsDistProdResultDto(DistributionProd distributionProd, boolean isHonbu, BaseParam baseParamUH, Date refPeriodFromUH, Date refPeriodToUH, BaseParam baseParamP,
		Date refPeriodFromP, Date refPeriodToP, Date lastUpdate) {
		this.distributionProd = distributionProd;
		this.isHonbu = isHonbu;
		this.baseParamUH = baseParamUH;
		this.refPeriodFromUH = refPeriodFromUH;
		this.refPeriodToUH = refPeriodToUH;
		this.baseParamP = baseParamP;
		this.refPeriodFromP = refPeriodFromP;
		this.refPeriodToP = refPeriodToP;
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 計画対象品目を取得する。
	 * 
	 * @return plannedProd 計画対象品目
	 */
	public DistributionProd getDistributionProd() {
		return distributionProd;
	}

	/**
	 * 本部/営業所判断フラグを取得する。
	 * 
	 * @return isHonbu 本部/営業所判断フラグ
	 */
	public boolean isHonbu() {
		return isHonbu;
	}

	/**
	 * [UH]試算配分共通パラメータを取得する。
	 * 
	 * @return baseParamUH [UH]試算配分共通パラメータ
	 */
	public BaseParam getBaseParamUH() {
		return baseParamUH;
	}

	/**
	 * [UH]実績参照期間FROMを取得する。
	 * 
	 * @return refPeriodFromUH [UH]実績参照期間FROM
	 */
	public Date getRefPeriodFromUH() {
		return refPeriodFromUH;
	}

	/**
	 * [UH]実績参照期間TOを取得する。
	 * 
	 * @return refPeriodToUH [UH]実績参照期間TO
	 */
	public Date getRefPeriodToUH() {
		return refPeriodToUH;
	}

	/**
	 * [P]試算配分共通パラメータを取得する。
	 * 
	 * @return baseParamP [P]試算配分共通パラメータ
	 */
	public BaseParam getBaseParamP() {
		return baseParamP;
	}

	/**
	 * [P]実績参照期間FROMを取得する。
	 * 
	 * @return refPeriodFromP [P]実績参照期間FROM
	 */
	public Date getRefPeriodFromP() {
		return refPeriodFromP;
	}

	/**
	 * [P]実績参照期間TOを取得する。
	 * 
	 * @return refPeriodToP [P]実績参照期間TO
	 */
	public Date getRefPeriodToP() {
		return refPeriodToP;
	}

	/**
	 * 配分基準最終更新日を取得する。
	 * 
	 * @return 配分基準最終更新日
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
