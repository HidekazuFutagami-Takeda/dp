package jp.co.takeda.service;

import jp.co.takeda.dto.DocDistributionParamsDto;

/**
 * 配分機能の実行処理に関するサービスインタフェース
 * 
 * @author nozaki
 */
public interface DpsDocDistributionProdService {

	/**
	 * 配分前処理を実行する。
	 * 
	 * @param sosCd 組織コード(営業所)
	 * @param category カテゴリ
	 * @param distExecOrgDtoList 更新前施設特約店別計画ステータスのリスト
	 */
	void distributionPreparation(DocDistributionParamsDto dto);

}
