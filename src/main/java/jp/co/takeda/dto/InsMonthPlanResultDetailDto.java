package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageInsMonthPlan;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設別計画の検索結果 明細行を表すDTO
 *
 * @author stakeuchi
 */
public class InsMonthPlanResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設名
	 */
	private final String insName;
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
	 * 都道府県
	 */
	private final Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）
	 */
	private final String shikuchosonMeiKj;

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

	/**
	 * コンストラクタ
	 *
	 * @param insPlan 施設別計画
	 * @param tyChangeRate T/Y変換率
	 */
	public InsMonthPlanResultDetailDto(ManageInsMonthPlan insPlan, boolean enableEdit) {
		this.insNo = insPlan.getInsNo();
		this.insName = insPlan.getInsAbbrName();
		this.plannedTermValue = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getPlannedTermValue());
		this.planned1ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getPlanned1ValueYb());
		this.planned2ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getPlanned2ValueYb());
		this.planned3ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getPlanned3ValueYb());
		this.planned4ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getPlanned4ValueYb());
		this.planned5ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getPlanned5ValueYb());
		this.planned6ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getPlanned6ValueYb());
		this.expected1ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getExpected1ValueYb());
		this.expected2ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getExpected2ValueYb());
		this.expected3ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getExpected3ValueYb());
		this.expected4ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getExpected4ValueYb());
		this.expected5ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getExpected5ValueYb());
		this.expected6ValueYb = ConvertUtil.parseMoneyToThousandUnit(insPlan.getImplMonthPlan().getExpected6ValueYb());
//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		this.recordTotalValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecordTotalValueYb());
		this.recordTougetuValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecordTougetuValueYb());
		this.record1ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecord1ValueYb());
		this.record2ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecord2ValueYb());
		this.record3ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecord3ValueYb());
		this.record4ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecord4ValueYb());
		this.record5ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecord5ValueYb());
		this.record6ValueYb = ConvertUtil.parseMoneyToThousandUnitDouble(insPlan.getImplMonthPlan().getRecord6ValueYb());
//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
		this.seqKey = insPlan.getSeqKey();
		this.upDate = insPlan.getUpDate();
		if (upDate != null) {
			this.upJgiName = insPlan.getUpJgiName();
		} else {
			this.upJgiName = null;
		}
		this.addrCodePref = insPlan.getAddrCodePref();
		this.addrCodeCity = insPlan.getAddrCodeCity();
		this.shikuchosonMeiKj = insPlan.getShikuchosonMeiKj();

		// 依頼中 または 削除済の場合は「削除施設」
		if (Boolean.TRUE.equals(insPlan.getReqFlg()) || Boolean.TRUE.equals(insPlan.getInsDelFlg())) {
			this.isDeletePlan = true;
		} else {
			this.isDeletePlan = false;
		}

		// 削除施設 かつ シーケンスがnullの場合は「編集不可」、それ以外は指定された状態
		if (isDeletePlan && insPlan.getSeqKey() == null) {
			this.enableEdit = false;
		} else {
			this.enableEdit = enableEdit;
		}
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設名を取得する。
	 *
	 * @return insName 施設名
	 */
	public String getInsName() {
		return insName;
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
		 *当月実績（YB価_画面表示項目）を取得する
		 * @return recordTougetuValueYb 累計実績（YB価_画面表示項目）
		 */
		public Double getRecordTougetuValueYb() {
			return recordTougetuValueYb;
		}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

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
	 * 都道府県を取得する。
	 *
	 * @return 都道府県
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS市区町村コードを取得する。
	 *
	 * @return JIS市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 *
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
