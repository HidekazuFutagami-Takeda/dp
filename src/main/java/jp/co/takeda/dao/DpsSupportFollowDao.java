package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.model.view.AreaPersonInChargeAdjustment;
import jp.co.takeda.model.view.PersonInChargeFacilityAdjustmentList;
import jp.co.takeda.model.view.DeletedFacilityPersonInChargeplan;
import jp.co.takeda.model.view.AreaFacilityPersonInChargeAdjustment;

/**
 *
 *
 * @author hfutagami
 */
public interface DpsSupportFollowDao {

	/**
	 * エリア・担当者調整一覧を取得する
	 * @param category
	 * @param prodCode
	 * @return
	 */
    List<AreaPersonInChargeAdjustment> searchAreaPersonInChargeAdjustmentList(String category,String prodCode) throws DataNotFoundException;

	/**
	 * 担当者・施設特約店別計画調整一覧を取得する
	 * @param category
	 * @param statProdCode
	 * @param prodCode
	 * @return
	 */
    List<PersonInChargeFacilityAdjustmentList> searchPersonInChargeFacilityAdjustmentList(String category, String statProdCode, String prodCode) throws DataNotFoundException;

	/**
	 * エリア・施設特約店別調整(仕入以外)一覧を取得する
	 * @param category
	 * @param prodCode
	 * @return
	 */
    List<AreaFacilityPersonInChargeAdjustment> searchAreaFacilityPersonInChargeAdjustmentList(String category,String prodCode) throws DataNotFoundException;

	/**
	 * エリア・施設特約店別調整(仕入)一覧を取得する
	 * @param category
	 * @param prodCode
	 * @return
	 */
    List<AreaFacilityPersonInChargeAdjustment> searchAreaFacilityPersonInChargeAdjustmentShiireList(String category,String prodCode) throws DataNotFoundException;


	/**
	 * 削除施設・施設特約店別計画(仕入以外)一覧を取得する
	 * @param category
	 * @param prodCode
	 * @return
	 */
    List<DeletedFacilityPersonInChargeplan> searchDeletedFacilityPersonInChargeplanList(String category,String prodCode) throws DataNotFoundException;

	/**
	 * 削除施設・施設特約店別計画(仕入)一覧を取得する
	 * @param category
	 * @param prodCode
	 * @return
	 */
    List<DeletedFacilityPersonInChargeplan> searchDeletedFacilityPersonInChargeplanShiireList(String category,String prodCode) throws DataNotFoundException;


}
