package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.DistributionForVacParamsDto;
import jp.co.takeda.dto.DistributionParamsDto;
import jp.co.takeda.dto.DocDistributionParamsDto;
import jp.co.takeda.dto.EstimationParamsDto;
import jp.co.takeda.dto.TmsTytenDistParamDto;

/**
 * 非同期処理仕掛かり中パラメータを検索するサービスインターフェイス
 * 
 * @author tkawabata
 */
public interface DpsAsyncProcessSearchService {

	/**
	 * 試算処理パラメータリストを取得する。
	 * 
	 * @param appServerKbn サーバ区分
	 * @return 試算処理パラメータリスト
	 * @throws DataNotFoundException 試算処理パラメータリストがない場合にスロー
	 */
	List<EstimationParamsDto> searchEstimationPrameter(String appServerKbn) throws DataNotFoundException;

	/**
	 * 施設特約店別計画配分処理パラメータリストを取得する。
	 * 
	 * @param appServerKbn サーバ区分
	 * @return 施設特約店別計画配分処理パラメータリスト
	 * @throws DataNotFoundException 施設特約店別計画配分処理パラメータリストがない場合にスロー
	 */
	List<DistributionParamsDto> searchInsWsDistPrameter(String appServerKbn) throws DataNotFoundException;

	/**
	 * 施設医師別計画配分処理パラメータリストを取得する。
	 * 
	 * @param appServerKbn サーバ区分
	 * @return 施設医師別計画配分処理パラメータリスト
	 * @throws DataNotFoundException 施設医師別計画配分処理パラメータリストがない場合にスロー
	 */
	List<DocDistributionParamsDto> searchInsDocDistPrameter(String appServerKbn) throws DataNotFoundException;

	/**
	 * (ワクチン)施設特約店別計画配分処理パラメータリストを取得する。
	 * 
	 * @param appServerKbn サーバ区分
	 * @return (ワクチン)施設特約店別計画配分処理パラメータリスト
	 * @throws DataNotFoundException (ワクチン)施設特約店別計画配分処理パラメータリストがない場合にスロー
	 */
	List<DistributionForVacParamsDto> searchInsWsDistForVacPrameter(String appServerKbn) throws DataNotFoundException;

	/**
	 * 特約店別計画配分処理パラメータリストを取得する。
	 * 
	 * @param appServerKbn サーバ区分
	 * @return 特約店別計画配分処理パラメータリスト
	 * @throws DataNotFoundException 特約店別計画配分処理パラメータリストがない場合にスロー
	 */
	List<TmsTytenDistParamDto> searchWsDistPrameter(String appServerKbn) throws DataNotFoundException;

}
