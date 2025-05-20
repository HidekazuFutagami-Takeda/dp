package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.DistParamUpdateDto;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;

/**
 * 配分機能の実行処理に関するサービスインタフェース
 *
 * @author nozaki
 */
public interface DpsDistributionProdService {

	/**
	 * 配分前処理を実行する。
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param category カテゴリ
	 * @param distExecOrgDtoList 更新前施設特約店別計画ステータスのリスト
	 */
	void distributionPreparation(String sosCd, String category, List<DistributionExecOrgDto> distExecOrgDtoList, String appServerKbn);

	/**
	 * 特定施設個別計画を営業所案に戻す。<br>
	 * 組織を指定して、処理を実行する。
	 *
	 * @param sosCd
	 * @param insWsDistProdUpdateDtoList
	 * @deprecated
	 */
	void updateSpecialInsPlan(String sosCd, List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList);

	/**
	 * 配分基準(営業所案)の更新・登録処理を実行する。
	 *
	 * @param distributionParamUpdateDto 配分基準更新用DTO
	 */
	void updateDistParamOffice(DistParamUpdateDto distributionParamUpdateDto);

	/**
	 * 配分基準を本部案に戻す処理実行する。 <br>
	 * 営業所案の削除を行なう。
	 *
	 * @param distributionParamUpdateDto 配分基準更新用DTO
	 */
	void deleteDistParamOffice(DistParamUpdateDto distributionParamUpdateDto);

}
