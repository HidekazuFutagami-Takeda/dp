package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.view.DistributionForVacProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ワクチン用施設特約店別計画配分対象品目 検索結果用DTO
 * 
 * @author khashimoto
 */
public class InsWsDistForVacProdResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * (ワクチン)配分対象品目
	 */
	private final DistributionForVacProd distributionForVacProd;

	/**
	 * 試算配分共通パラメータ
	 * <ul>
	 * <li>配分品目情報(品目名称、製品区分)</li>
	 * <li>参照品目名称1</li>
	 * <li>参照品目名称2</li>
	 * <li>参照品目名称3</li>
	 * </ul>
	 */
	private final BaseParam baseParam;

	/**
	 * 実績参照期間FROM
	 */
	private final Date refPeriodFrom;

	/**
	 * 実績参照期間TO
	 */
	private final Date refPeriodTo;

	/**
	 * 配分基準最終更新日
	 */
	private final Date lastUpdate;

	/**
	 * コンストラクタ
	 * 
	 * @param distributionForVacProd (ワクチン)配分対象品目
	 * @param baseParam 試算配分共通パラメータ
	 * @param refPeriodFromP [P]実績参照期間FROM
	 * @param refPeriodToP [P]実績参照期間TO
	 * @param lastUpdate 配分基準最終更新日
	 */
	public InsWsDistForVacProdResultDto(DistributionForVacProd distributionForVacProd, BaseParam baseParam, Date refPeriodFrom, Date refPeriodTo, Date lastUpdate) {
		this.distributionForVacProd = distributionForVacProd;
		this.baseParam = baseParam;
		this.refPeriodFrom = refPeriodFrom;
		this.refPeriodTo = refPeriodTo;
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 計画対象品目を取得する。
	 * 
	 * @return distributionForVacProd 計画対象品目
	 */
	public DistributionForVacProd getDistributionForVacProd() {
		return distributionForVacProd;
	}

	/**
	 * 試算配分共通パラメータを取得する。
	 * 
	 * @return baseParam 試算配分共通パラメータ
	 */
	public BaseParam getBaseParam() {
		return baseParam;
	}

	/**
	 * 実績参照期間FROMを取得する。
	 * 
	 * @return refPeriodFrom 実績参照期間FROM
	 */
	public Date getRefPeriodFrom() {
		return refPeriodFrom;
	}

	/**
	 * 実績参照期間TOを取得する。
	 * 
	 * @return refPeriodTo 実績参照期間TO
	 */
	public Date getRefPeriodTo() {
		return refPeriodTo;
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
