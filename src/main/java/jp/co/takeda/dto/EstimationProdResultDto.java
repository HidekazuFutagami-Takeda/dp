package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.view.EstimationProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算対象品目一覧の検索結果用DTO
 * 
 * @author nozaki
 */
public class EstimationProdResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 試算対象品目
	 */
	private final EstimationProd estimationProd;

	/**
	 * 本部/営業所判断フラグ(true：本部、false：営業所)
	 */
	private final boolean isHonbu;

	/**
	 * 試算パラメータ最終更新日
	 */
	private final Date estParamLastUpdate;

	/**
	 * 試算パラメータ
	 * <ul>
	 * <li>指数1</li>
	 * <li>指数2</li>
	 * <li>指数3</li>
	 * <li>指数4</li>
	 * <li>指数5</li>
	 * </ul>
	 */
	private final EstimationParam estimationParam;

	/**
	 * 試算配分共通パラメータ
	 * <ul>
	 * <li>試算品目情報(品目固定コード、品目名称、製品区分)</li>
	 * <li>実績参照品目名称1</li>
	 * <li>実績参照品目名称2</li>
	 * <li>実績参照品目名称3</li>
	 * </ul>
	 */
	private final BaseParam baseParam;

	/**
	 * 参照期間(FROM)
	 */
	private final Date refPeriodFrom;

	/**
	 * 参照期間(TO)
	 */
	private final Date refPeriodTo;

	/**
	 * 担当者別計画立案ステータス最終更新日
	 */
	private final Date mrPlanStatusUpdate;

	/**
	 * コンストラクタ
	 * 
	 * @param estimationProd 試算対象品目情報
	 * @param isHonbu 本部案/営業所案フラグ
	 * @param estParamLastUpdate 試算パラメータ最終更新日付
	 * @param estimationParam 試算パラメータ
	 * @param baseParam 試算配分共通パラメータ
	 * @param refPeriodFrom 参照期間(FROM)
	 * @param refPeriodTo 参照期間(TO)
	 * @param mrPlanStatusUpdate 担当者別計画立案ステータス最終更新日
	 */
	public EstimationProdResultDto(EstimationProd estimationProd, boolean isHonbu, Date estParamLastUpdate, EstimationParam estimationParam, BaseParam baseParam,
		Date refPeriodFrom, Date refPeriodTo, Date mrPlanStatusUpdate) {

		this.estimationProd = estimationProd;
		this.isHonbu = isHonbu;
		this.estParamLastUpdate = estParamLastUpdate;
		this.estimationParam = estimationParam;
		this.baseParam = baseParam;
		this.refPeriodFrom = refPeriodFrom;
		this.refPeriodTo = refPeriodTo;
		this.mrPlanStatusUpdate = mrPlanStatusUpdate;
	}

	/**
	 * 試算対象品目を取得する。
	 * 
	 * @return
	 */
	public EstimationProd getEstimationProd() {
		return estimationProd;
	}

	/**
	 * 本部/営業所判断フラグを取得する。
	 * 
	 * @return 本部/営業所判断フラグ(true：本部、false：営業所)
	 */
	public boolean isHonbu() {
		return isHonbu;
	}

	/**
	 * 試算パラメータ最終更新日を取得する。
	 * 
	 * @return 試算パラメータ最終更新日
	 */
	public Date getEstParamLastUpdate() {
		return estParamLastUpdate;
	}

	/**
	 * 試算パラメータを取得する。
	 * 
	 * @return 試算パラメータ
	 */
	public EstimationParam getEstimationParam() {
		return estimationParam;
	}

	/**
	 * 試算配分共通パラメータを取得する。
	 * 
	 * @return 試算配分共通パラメータ
	 */
	public BaseParam getBaseParam() {
		return baseParam;
	}

	/**
	 * 参照期間(FROM)を取得する。
	 * 
	 * @return 参照期間(FROM)
	 */
	public Date getRefPeriodFrom() {
		return refPeriodFrom;
	}

	/**
	 * 参照期間(TO)を取得する。
	 * 
	 * @return 参照期間(TO)
	 */
	public Date getRefPeriodTo() {
		return refPeriodTo;
	}

	/**
	 * 担当者別計画立案ステータス最終更新日を取得する。
	 * 
	 * @return 担当者別計画立案ステータス最終更新日
	 */
	public Date getMrPlanStatusUpdate() {
		return mrPlanStatusUpdate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
