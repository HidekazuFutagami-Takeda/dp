package jp.co.takeda.dao;

import java.util.List;

import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsDocHaibunDto;

/**
 * 施設医師配分処理を管理するDAOインターフェイス
 *
 * @author tkawabata
 */
public interface InsDocHaibunDao {

	/**
	 * 施設医師別計画配分処理を実行する。
	 *
	 * @param insDocHaibunDto 施設医師配分パラメータ
	 * @return 配分ミスリスト(なければ空リスト)
	 */
	List<DistributionMissDto> executeHaibun(InsDocHaibunDto insDocHaibunDto);
}
