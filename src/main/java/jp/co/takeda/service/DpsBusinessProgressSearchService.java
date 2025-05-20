package jp.co.takeda.service;

import jp.co.takeda.dto.ProgressEachKindInfoDto;
import jp.co.takeda.dto.ProgressEachKindInfoForVacDto;
import jp.co.takeda.dto.ProgressParamDto;
import jp.co.takeda.dto.ProgressParamForVacDto;
import jp.co.takeda.dto.ProgressPlanStatusDto;
import jp.co.takeda.dto.ProgressPlanStatusForVacDto;
import jp.co.takeda.dto.ProgressPlanWsStatusDto;
import jp.co.takeda.dto.ProgressPlanWsStatusForVacDto;
import jp.co.takeda.model.div.Term;

/**
 * 業務進捗状況を把握するサービスインターフェイス
 *
 * @author tkawabata
 */
public interface DpsBusinessProgressSearchService {

	/**
	 * 業務進捗表(医)[薬価改定][T/Y変換指定率]を取得する。
	 *
	 * @param calYear 年
	 * @param calTerm 期
	 * @return 業務進捗表[薬価改定][T/Y変換指定率]
	 */
	ProgressParamDto searchProgressParam(String calYear, Term calTerm);

	/**
	 * 業務進捗表(医)[各種登録状況]を取得する。
	 *
	 * @return 業務進捗表(医)[各種登録状況]
	 */
	ProgressEachKindInfoDto searchProgressEachKindInfo();

	/**
	 * 業務進捗表(医)[営業所計画-施設特約店別計画(SPBU)]を取得する。
	 *
	 * @return 業務進捗表(医)[営業所計画-施設特約店別計画(SPBU)]
	 */
	ProgressPlanStatusDto searchProgressPlanStatus(String category);

	/**
	 * 業務進捗表(医)[特約店別計画(MMP品・仕入品(一般・麻薬))]を取得する。
	 *
	 * @return 業務進捗表(医)[特約店別計画(MMP品・仕入品(一般・麻薬))]
	 */
	ProgressPlanWsStatusDto searchProgressPlanWsStatus();

	/**
	 * 業務進捗表(ワ)[T/B変換指定率]を取得する。
	 *
	 * @param calYear 年
	 * @param calTerm 期
	 * @return 業務進捗表[薬価改定][T/B変換指定率]
	 */
	ProgressParamForVacDto searchProgressParamForVac(String calYear, Term calTerm);

	/**
	 * 業務進捗表(ワ)[各種登録状況]を取得する。
	 *
	 * @return 業務進捗表(ワ)[各種登録状況]
	 */
	ProgressEachKindInfoForVacDto searchProgressEachKindInfoForVac();

	/**
	 * 業務進捗表(ワ)[施設特約店別計画]を取得する。
	 *
	 * @return 業務進捗表(ワ)[施設特約店別計画]
	 */
	ProgressPlanStatusForVacDto searchProgressPlanStatusForVac();

	/**
	 * 業務進捗表(ワ)[特約店別計画]を取得する。
	 *
	 * @return 業務進捗表(医)[特約店別計画]
	 */
	ProgressPlanWsStatusForVacDto searchProgressPlanWsStatusForVac();

}
