package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.dto.MikakutokuSijouUpdateDto;

/**
 * 未獲得市場の登録/更新に関するサービスインターフェイス
 * 
 * @author stakeuchi
 */
public interface DpsMikakutokuSijouService {

	/**
	 * 未獲得市場の更新用DTOを元に未獲得市場を更新する。
	 * 
	 * @param sosCd3 更新対象の組織コード(営業所)
	 * @param updateDtoList 未獲得市場の更新用DTOのリスト
	 */
	void updateMikakutokuSijou(String sosCd3, List<MikakutokuSijouUpdateDto> updateDtoList);

}
