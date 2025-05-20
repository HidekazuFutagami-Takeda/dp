package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.view.InsDocPlanStatSum;
import jp.co.takeda.model.view.InsWsPlanStatSum;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 品目単位の施設特約店別計画ステータスサマリーを表すDTO
 * 
 * @author khashimoto
 */
public class ProdInsWsPlanStatSummaryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称(漢字)
	 */
	private final String prodName;

	/**
	 * 施設医師別計画ステータスのサマリー
	 */
	private InsDocPlanStatSum insDocPlanStatSum;

	/**
	 * 施設特約店別計画ステータスのサマリー
	 */
	private final InsWsPlanStatSum insWsPlanStatSum;

	/**
	 * 最終更新日
	 */
	private final Date lastUpdate;

	/**
	 * 
	 * コンストラクタ（ワクチン）
	 * 
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称(漢字)
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリー
	 */
	public ProdInsWsPlanStatSummaryDto(final String prodCode, final String prodName, final InsWsPlanStatSum insWsPlanStatSum, Date lastUpdate) {
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.insDocPlanStatSum = null;
		this.insWsPlanStatSum = insWsPlanStatSum;
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 
	 * コンストラクタ（医薬）
	 * 
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称(漢字)
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリー
	 */
	public ProdInsWsPlanStatSummaryDto(final String prodCode, final String prodName, final InsDocPlanStatSum insDocPlanStatSum, final InsWsPlanStatSum insWsPlanStatSum, Date lastUpdate) {
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.insDocPlanStatSum = insDocPlanStatSum;
		this.insWsPlanStatSum = insWsPlanStatSum;
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名称(漢字)を取得する。
	 * 
	 * @return 品目名称(漢字)
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 施設医師別計画ステータスのサマリーを取得する。
	 *
	 * @return 施設医師別計画ステータスのサマリー
	 */
	public InsDocPlanStatSum getInsDocPlanStatSum() {
		return insDocPlanStatSum;
	}

	/**
	 * 施設医師別計画ステータスのサマリーを設定する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリー
	 */
	public void setInsDocPlanStatSum(InsDocPlanStatSum insDocPlanStatSum) {
		this.insDocPlanStatSum = insDocPlanStatSum;
	}

	/**
	 * 施設特約店別計画ステータスのサマリーを取得する。
	 * 
	 * @return 施設特約店別計画ステータスのサマリー
	 */
	public InsWsPlanStatSum getInsWsPlanStatSum() {
		return insWsPlanStatSum;
	}

	/**
	 * 最終更新日取得
	 * 
	 * @return 最終更新日取得
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
