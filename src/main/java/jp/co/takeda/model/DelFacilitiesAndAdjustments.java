package jp.co.takeda.model;

import java.io.Serializable;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.model.view.AreaPersonInChargeAdjustment;
import jp.co.takeda.model.view.PersonInChargeFacilityAdjustmentList;
import jp.co.takeda.model.view.AreaFacilityPersonInChargeAdjustment;
import jp.co.takeda.model.view.DeletedFacilityPersonInChargeplan;

/**
 * 削除施設・調整あり計画データ情報を表すクラス
 *
 * @author khashimoto
 */
public class DelFacilitiesAndAdjustments implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * エリア・担当者調整一覧
	 */
	private List<AreaPersonInChargeAdjustment> areaPersonInChargeAdjustmentList;

	/**
	 * 担当者・施設特約店別計画調整一覧
	 */
	private List<PersonInChargeFacilityAdjustmentList> personInChargeFacilityAdjustmentList;

	/**
	 * エリア・施設特約店別調整(仕入以外)一覧
	 */
	private List<AreaFacilityPersonInChargeAdjustment> areaFacilityPersonInChargeAdjustmentList;

	/**
	 * 削除施設・施設特約店別計画(仕入以外)一覧
	 */
	private List<DeletedFacilityPersonInChargeplan> deletedFacilityPersonInChargeplanList;


	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 *
	 * @param prodName 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * エリア・担当者調整一覧を取得する。
	 *
	 * @return エリア・担当者調整一覧
	 */
	public List<AreaPersonInChargeAdjustment> getAreaPersonInChargeAdjustmentList() {
		return areaPersonInChargeAdjustmentList;
	}

	/**
	 * エリア・担当者調整一覧を設定する。
	 *
	 * @param prodName エリア・担当者調整一覧
	 */
	public void setAreaPersonInChargeAdjustmentList(List<AreaPersonInChargeAdjustment> areaPersonInChargeAdjustmentList) {
		this.areaPersonInChargeAdjustmentList = areaPersonInChargeAdjustmentList;
	}

	/**
	 * 担当者・施設特約店別計画調整一覧を取得する。
	 *
	 * @return 担当者・施設特約店別計画調整一覧
	 */
	public List<PersonInChargeFacilityAdjustmentList> getPersonInChargeFacilityAdjustmentList() {
		return personInChargeFacilityAdjustmentList;
	}

	/**
	 * 担当者・施設特約店別計画調整一覧を設定する。
	 *
	 * @param prodName 担当者・施設特約店別計画調整一覧
	 */
	public void setPersonInChargeFacilityAdjustmentList(List<PersonInChargeFacilityAdjustmentList> personInChargeFacilityAdjustmentList) {
		this.personInChargeFacilityAdjustmentList = personInChargeFacilityAdjustmentList;
	}

	/**
	 * エリア・施設特約店別調整(仕入以外)一覧を取得する。
	 *
	 * @return エリア・施設特約店別調整(仕入以外)一覧
	 */
	public List<AreaFacilityPersonInChargeAdjustment> getAreaFacilityPersonInChargeAdjustmentList() {
		return areaFacilityPersonInChargeAdjustmentList;
	}

	/**
	 * エリア・施設特約店別調整(仕入以外)一覧を設定する。
	 *
	 * @param prodName エリア・施設特約店別調整(仕入以外)一覧
	 */
	public void setAreaFacilityPersonInChargeAdjustmentList(List<AreaFacilityPersonInChargeAdjustment> areaFacilityPersonInChargeAdjustmentList) {
		this.areaFacilityPersonInChargeAdjustmentList = areaFacilityPersonInChargeAdjustmentList;
	}

	/**
	 * 削除施設・施設特約店別計画(仕入以外)一覧を取得する。
	 *
	 * @return 削除施設・施設特約店別計画(仕入以外)一覧
	 */
	public List<DeletedFacilityPersonInChargeplan> getDeletedFacilityPersonInChargeplanList() {
		return deletedFacilityPersonInChargeplanList;
	}

	/**
	 * 削除施設・施設特約店別計画(仕入以外)一覧を設定する。
	 *
	 * @param prodName 削除施設・施設特約店別計画(仕入以外)一覧
	 */
	public void setDeletedFacilityPersonInChargeplanList(List<DeletedFacilityPersonInChargeplan> deletedFacilityPersonInChargeplanList) {
		this.deletedFacilityPersonInChargeplanList = deletedFacilityPersonInChargeplanList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
