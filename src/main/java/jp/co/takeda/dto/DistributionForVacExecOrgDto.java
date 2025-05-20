package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.InsWsPlanStatusForVac;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)配分実行用DTO
 * 
 * @author khashimoto
 */
public class DistributionForVacExecOrgDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 配分対象の品目固定コード
	 */
	private final String prodCode;

	/**
	 * 配分対象の品目名称
	 */
	private final String prodName;

	/**
	 * 配分対象の更新前(ワクチン)施設特約店別計画立案ステータスのリスト
	 */
	private final List<InsWsPlanStatusForVac> insWsPlanStatusForVacList;

	/**
	 * 配分対象の更新前(ワクチン)施設特約店別計画立案ステータス最終更新日
	 */
	private final Date insWsPlanStatusLastUpdate;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 配分対象の品目固定コード
	 * @param prodName 配分対象の品目名称
	 * @param insWsPlanStatusList 配分対象の更新前(ワクチン)施設特約店別計画立案ステータスのリスト
	 * @param insWsPlanStatusLastUpdate 配分対象の更新前(ワクチン)施設特約店別計画立案ステータス最終更新日
	 */
	public DistributionForVacExecOrgDto(String prodCode, String prodName, List<InsWsPlanStatusForVac> insWsPlanStatusForVacList, Date insWsPlanStatusLastUpdate) {

		this.prodCode = prodCode;
		this.prodName = prodName;
		this.insWsPlanStatusForVacList = insWsPlanStatusForVacList;
		this.insWsPlanStatusLastUpdate = insWsPlanStatusLastUpdate;
	}

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 配分対象の品目固定コードを取得する。
	 * 
	 * @return 配分対象の品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 配分対象の品目名称を取得する。
	 * 
	 * @return 配分対象の品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 配分対象の更新前(ワクチン)施設特約店別計画立案ステータスのリストを取得する。
	 * 
	 * @return 配分対象の更新前(ワクチン)施設特約店別計画立案ステータスのリスト
	 */
	public List<InsWsPlanStatusForVac> getInsWsPlanStatusForVacList() {
		return insWsPlanStatusForVacList;
	}

	/**
	 * 配分対象の更新前(ワクチン)施設特約店別計画立案ステータス最終更新日を取得する。
	 * 
	 * @return 配分対象の更新前(ワクチン)施設特約店別計画立案ステータス最終更新日
	 */
	public Date getInsWsPlanStatusLastUpdate() {
		return insWsPlanStatusLastUpdate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
