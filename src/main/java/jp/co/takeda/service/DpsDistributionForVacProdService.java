package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.DistributionForVacExecOrgDto;

/**
 * (ワクチン)配分機能の実行処理に関するサービスインタフェース
 * 
 * @author khashimoto
 */
public interface DpsDistributionForVacProdService {

	/**
	 * (ワクチン)配分前処理を実行する。
	 * 
	 * @param distExecOrgDtoList 更新前(ワクチン)施設特約店別計画ステータスのリスト
	 */
	void distributionPreparation(List<DistributionForVacExecOrgDto> distExecOrgDtoList, String appServerKbn);

}
