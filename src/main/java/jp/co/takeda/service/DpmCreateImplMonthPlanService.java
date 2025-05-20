package jp.co.takeda.service;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.logic.div.ValueChangeType;
import jp.co.takeda.model.ImplMonthPlan;
import jp.co.takeda.model.ImplPlanForVac;
import jp.co.takeda.model.div.InsType;

/**
 * 実施計画作成サービスインターフェイス
 *
 * @author khashimoto
 */
public interface DpmCreateImplMonthPlanService {

	/**
	 * 入力パラメータをもとに、入力計画値を変換し、<br>
	 * 更新対象の実施計画を作成する。<br>
	 * 入力計画値がNULLの場合、論理削除用の実施計画を返す。
	 *
	 *
	 * @param prodCode 品目固定コード
	 * @param value 入力計画値【NULL可】
	 * @param insType 対象区分
	 * @param valueChangeType 変換区分
	 * @param tmsTytenCd 特約店コード【NULL可】
	 * @param implPlan 更新前の実施計画
	 * @return 更新対象の実施計画
	 * @throws LogicalException
	 */
	ImplMonthPlan getImplMonthPlan(String prodCode, Long value, InsType insType, ValueChangeType valueChangeType, String tmsTytenCd, ImplMonthPlan implMonthPlan) throws LogicalException;

	/**
	 * 入力パラメータをもとに、入力計画値を変換し、<br>
	 * 更新対象のワクチン実施計画を作成する。 入力計画値がNULLの場合、論理削除用のワクチン実施計画を返す。
	 *
	 *
	 * @param prodCode 品目固定コード
	 * @param value 入力計画値【NULL可】
	 * @param valueChangeType 変換区分
	 * @param tmsTytenCd 特約店コード【NULL可】
	 * @param implPlanForVac 更新前のワクチン実施計画
	 * @return 更新対象のワクチン実施計画
	 * @throws LogicalException
	 */
	ImplPlanForVac getImplPlanForVac(String prodCode, Long value, ValueChangeType valueChangeType, String tmsTytenCd, ImplPlanForVac implPlanForVac) throws LogicalException;

}
