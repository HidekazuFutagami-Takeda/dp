package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageInsMonthPlan;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設品目別計画の検索結果 明細行を表すDTO
 *
 * @author stakeuchi
 */
public class InsProdMonthPlanResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目コード
	 */
	private final String prodCode;

	/**
	 * 品目名
	 */
	private final String prodName;

	/**
	 * 期別計画
	 */
	protected Long plannedTermValue;

	/**
	 * 月初計画1（YB価）
	 */
	protected Long planned1ValueYb;

	/**
	 * 月初計画2（YB価）
	 */
	protected Long planned2ValueYb;

	/**
	 * 月初計画3（YB価）
	 */
	protected Long planned3ValueYb;

	/**
	 * 月初計画4（YB価）
	 */
	protected Long planned4ValueYb;

	/**
	 * 月初計画5（YB価）
	 */
	protected Long planned5ValueYb;

	/**
	 * 月初計画6（YB価）
	 */
	protected Long planned6ValueYb;

	/**
	 * 月末見込1（YB価）
	 */
	protected Long expected1ValueYb;

	/**
	 * 月末見込2（YB価）
	 */
	protected Long expected2ValueYb;

	/**
	 * 月末見込3（YB価）
	 */
	protected Long expected3ValueYb;

	/**
	 * 月末見込4（YB価）
	 */
	protected Long expected4ValueYb;

	/**
	 * 月末見込5（YB価）
	 */
	protected Long expected5ValueYb;

	/**
	 * 月末見込6（YB価）
	 */
	protected Long expected6ValueYb;

	/**
	 * 品目集計対象フラグ
	 */
	private final String targetSummary;
	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 最終更新者
	 */
	private final String upJgiName;

	/**
	 * 施設品目の担当者従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設品目の担当者雑組織フラグ
	 */
	private final boolean etcSosFlg;

	/**
	 * 削除予定施設フラグ
	 * <ul>
	 * <li>TRUE = 削除予定</li>
	 * <li>FALSE = 削除予定でない</li>
	 * </ul>
	 */
	private final boolean isDeletePlan;

	/**
	 * 編集可能フラグ
	 * <ul>
	 * <li>TRUE = 編集可能</li>
	 * <li>FALSE = 編集可能でない</li>
	 * </ul>
	 */
	private final boolean enableEdit;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（YB価_画面表示項目）
	 */
	protected Double record1ValueYb;

	/**
	 *実績2（YB価_画面表示項目）
	 */
	protected Double record2ValueYb;

	/**
	 *実績3（YB価_画面表示項目）
	 */
	protected Double record3ValueYb;

	/**
	 *実績4（YB価_画面表示項目）
	 */
	protected Double record4ValueYb;

	/**
	 *実績5（YB価_画面表示項目）
	 */
	protected Double record5ValueYb;

	/**
	 *実績6（YB価_画面表示項目）
	 */
	protected Double record6ValueYb;

	/**
	 *累計実績（YB価_画面表示項目）
	 */
	protected Double recordTotalValueYb;

	/**
	 *当月実績（YB価_画面表示項目）
	 */
	protected Double recordTougetuValueYb;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * コンストラクタ
	 *
	 * @param insPlan 施設別計画
	 */
	public InsProdMonthPlanResultDetailDto(ManageInsMonthPlan insMonthPlan, boolean isDeletePlan, boolean enableEdit) {
		this.prodCode = insMonthPlan.getProdCode();
		this.prodName = insMonthPlan.getProdName();
		this.plannedTermValue = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getPlannedTermValue());
		this.planned1ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getPlanned1ValueYb());
		this.planned2ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getPlanned2ValueYb());
		this.planned3ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getPlanned3ValueYb());
		this.planned4ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getPlanned4ValueYb());
		this.planned5ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getPlanned5ValueYb());
		this.planned6ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getPlanned6ValueYb());
		this.expected1ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getExpected1ValueYb());
		this.expected2ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getExpected2ValueYb());
		this.expected3ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getExpected3ValueYb());
		this.expected4ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getExpected4ValueYb());
		this.expected5ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getExpected5ValueYb());
		this.expected6ValueYb = ConvertUtil.parseMoneyToThousandUnit(insMonthPlan.getImplMonthPlan().getExpected6ValueYb());
		this.targetSummary = insMonthPlan.getTargetSummary();
		this.seqKey = insMonthPlan.getSeqKey();

		// 施設品目の担当者従業員番号
		if(insMonthPlan.getJgiMst() != null){
			this.jgiNo = insMonthPlan.getJgiMst().getJgiNo();
		} else {
			this.jgiNo = null;
		}

		// 施設品目の担当者雑組織フラグ
		if(insMonthPlan.getSosMst() != null){
			this.etcSosFlg = insMonthPlan.getSosMst().getEtcSosFlg();
		} else {
			this.etcSosFlg = false;
		}

		this.upDate = insMonthPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = insMonthPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.isDeletePlan = isDeletePlan;

		// 削除施設 かつ シーケンスがnullの場合は「編集不可」、それ以外は指定された状態
		if (isDeletePlan && insMonthPlan.getSeqKey() == null) {
			this.enableEdit = false;
		} else {
			this.enableEdit = enableEdit;
		}
//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		this.record1ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecord1ValueYb());
		this.record2ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecord2ValueYb());
		this.record3ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecord3ValueYb());
		this.record4ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecord4ValueYb());
		this.record5ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecord5ValueYb());
		this.record6ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecord6ValueYb());
		this.recordTotalValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecordTotalValueYb());
		this.recordTougetuValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insMonthPlan.getImplMonthPlan().getRecordTougetuValueYb());
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	}

	/**
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目名を取得する。
	 *
	 * @return prodName 品目名
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 期別計画を取得する
	 *
	 * @return plannedTermValue 期別計画値
	 */
	public Long getPlannedTermValue() {
		return plannedTermValue;
	}

	/**
	 * 月初計画1（YB価）を取得する。
	 */
	public Long getPlanned1ValueYb() {
		return planned1ValueYb;
	}

	/**
	 * 月初計画2（YB価）を取得する。
	 */
	public Long getPlanned2ValueYb() {
		return planned2ValueYb;
	}

	/**
	 * 月初計画3（YB価）を取得する。
	 */
	public Long getPlanned3ValueYb() {
		return planned3ValueYb;
	}

	/**
	 * 月初計画4（YB価）を取得する。
	 */
	public Long getPlanned4ValueYb() {
		return planned4ValueYb;
	}

	/**
	 * 月初計画5（YB価）を取得する。
	 */
	public Long getPlanned5ValueYb() {
		return planned5ValueYb;
	}

	/**
	 * 月初計画6（YB価）を取得する。
	 */
	public Long getPlanned6ValueYb() {
		return planned6ValueYb;
	}

	/**
	 * 月末見込1（YB価）を取得する。
	 */
	public Long getExpected1ValueYb() {
		return expected1ValueYb;
	}

	/**
	 * 月末見込2（YB価）を取得する。
	 */
	public Long getExpected2ValueYb() {
		return expected2ValueYb;
	}

	/**
	 * 月末見込3（YB価）を取得する。
	 */
	public Long getExpected3ValueYb() {
		return expected3ValueYb;
	}

	/**
	 * 月末見込4（YB価）を取得する。
	 */
	public Long getExpected4ValueYb() {
		return expected4ValueYb;
	}

	/**
	 * 月末見込5（YB価）を取得する。
	 */
	public Long getExpected5ValueYb() {
		return expected5ValueYb;
	}

	/**
	 * 月末見込6（YB価）を取得する。
	 */
	public Long getExpected6ValueYb() {
		return expected6ValueYb;
	}

	/**
	 * 品目集計対象フラグを取得する。
	 *
	 * @return targetSummary 品目集計対象フラグ
	 */
	public String getTargetSummary() {
		return targetSummary;
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return seqKey シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return upJgiName 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
	}

	/**
	 * 削除予定施設フラグを取得する。
	 *
	 * @return isDeletePlan 削除予定施設フラグ
	 */
	public boolean isDeletePlan() {
		return isDeletePlan;
	}

	/**
	 * 編集可能フラグ を取得する。
	 *
	 * @return 編集可能フラグ
	 */
	public boolean getEnableEdit() {
		return enableEdit;
	}

	/**
	 * 施設品目の担当者従業員番号 を取得する。
	 *
	 * @return 施設品目の担当者従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 施設品目の担当者従業員番号 を取得する。
	 *
	 * @return 施設品目の担当者従業員番号
	 */
	public boolean isEtcSosFlg() {
		return etcSosFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（YB価_画面表示項目）を取得する
	 * @return record1ValueYb 実績1（YB価_画面表示項目）
	 */
	public Double getRecord1ValueYb() {
		return record1ValueYb;
	}

	/**
	 *実績2（YB価_画面表示項目）を取得する
	 * @return record2ValueYb 実績2（YB価_画面表示項目）
	 */
	public Double getRecord2ValueYb() {
		return record2ValueYb;
	}

	/**
	 *実績3（YB価_画面表示項目）を取得する
	 * @return record3ValueYb 実績3（YB価_画面表示項目）
	 */
	public Double getRecord3ValueYb() {
		return record3ValueYb;
	}

	/**
	 *実績4（YB価_画面表示項目）を取得する
	 * @return record4ValueYb 実績4（YB価_画面表示項目）
	 */
	public Double getRecord4ValueYb() {
		return record4ValueYb;
	}

	/**
	 *実績5（YB価_画面表示項目）を取得する
	 * @return record5ValueYb 実績5（YB価_画面表示項目）
	 */
	public Double getRecord5ValueYb() {
		return record5ValueYb;
	}

	/**
	 *実績6（YB価_画面表示項目）を取得する
	 * @return record6ValueYb 実績6（YB価_画面表示項目）
	 */
	public Double getRecord6ValueYb() {
		return record6ValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を取得する
	 * @return recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public Double getRecordTotalValueYb() {
		return recordTotalValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を取得する
	 * @return recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public Double getRecordTougetuValueYb() {
		return recordTougetuValueYb;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

}
