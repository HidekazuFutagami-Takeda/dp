package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.model.div.TradeType;

/**
 * 管理時の施設別計画（月別）を表すモデルクラス
 *
 * @author khashimoto
 */
public class ManageInsMonthPlan extends DpManageModel<ManageInsMonthPlan> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 実施計画
	 */
	private ImplMonthPlan implMonthPlan;

	/**
	 * 品目名称（Ref[管理時の計画対象品目].〔品目名称〕）
	 */
	private String prodName;

	/**
	 * 施設名（Ref[施設情報].〔施設略式漢字名〕）
	 */
	private String insAbbrName;

	/**
	 * 取引区分（Ref[施設情報].〔取引区分〕）
	 */
	private TradeType tradeType;

	/**
	 * 依頼中フラグ（Ref[施設情報].〔依頼中フラグ〕）
	 */
	private Boolean reqFlg;

	/**
	 * 削除フラグ（Ref[施設情報].〔削除フラグ〕）
	 */
	private Boolean insDelFlg;

	/**
	 * 施設分類
	 */
	private InsClass insClass;

	/**
	 * サブコード分類
	 */
	private OldInsrFlg oldInsrFlg;

	/**
	 * 施設担当の従業員情報（Ref[MR施設品目またはMR施設].〔担当者番号〕に紐づく従業員情報）
	 */
	private JgiMst jgiMst;

	/**
	 * 施設担当の組織情報（Ref[MR施設品目またはMR施設].〔担当者番号〕に紐づく従業員情報）
	 */
	private SosMst sosMst;

	/**
	 * 期別計画（合計）
	 */
	protected Long sumPlannedTermValue;

	/**
	 * 月初計画1（YB価合計）
	 */
	protected Long sumPlanned1ValueYb;

	/**
	 * 月初計画2（YB価合計）
	 */
	protected Long sumPlanned2ValueYb;

	/**
	 * 月初計画3（YB価合計）
	 */
	protected Long sumPlanned3ValueYb;

	/**
	 * 月初計画4（YB価合計）
	 */
	protected Long sumPlanned4ValueYb;

	/**
	 * 月初計画5（YB価合計）
	 */
	protected Long sumPlanned5ValueYb;

	/**
	 * 月初計画6（YB価合計）
	 */
	protected Long sumPlanned6ValueYb;

	/**
	 * 月末見込1（YB価合計）
	 */
	protected Long sumExpected1ValueYb;

	/**
	 * 月末見込2（YB価合計）
	 */
	protected Long sumExpected2ValueYb;

	/**
	 * 月末見込3（YB価合計）
	 */
	protected Long sumExpected3ValueYb;

	/**
	 * 月末見込4（YB価合計）
	 */
	protected Long sumExpected4ValueYb;

	/**
	 * 月末見込5（YB価合計）
	 */
	protected Long sumExpected5ValueYb;

	/**
	 * 月末見込6（YB価合計）
	 */
	protected Long sumExpected6ValueYb;

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（Y価）
	 */
	protected Long record1ValueY;

	/**
	 *実績1（B価）
	 */
	protected Long record1ValueB;

	/**
	 *実績2（Y価）
	 */
	protected Long record2ValueY;

	/**
	 *実績2（B価）
	 */
	protected Long record2ValueB;

	/**
	 *実績3（Y価）
	 */
	protected Long record3ValueY;

	/**
	 *実績3（B価）
	 */
	protected Long record3ValueB;

	/**
	 *実績4（Y価）
	 */
	protected Long record4ValueY;

	/**
	 *実績4（B価）
	 */
	protected Long record4ValueB;

	/**
	 *実績5（Y価）
	 */
	protected Long record5ValueY;

	/**
	 *実績5（B価）
	 */
	protected Long record5ValueB;

	/**
	 *実績6（Y価）
	 */
	protected Long record6ValueY;

	/**
	 *実績6（B価）
	 */
	protected Long record6ValueB;

	/**
	*累計実績（Y価）
	 */
	protected Long recordTotalValueY;

	/**
	*累計実績（B価）
	 */
	protected Long recordTotalValueB;

	/**
	 *実績1（YB価_画面表示項目）
	 */
	protected Long record1ValueYb;

	/**
	 *実績2（YB価_画面表示項目）
	 */
	protected Long record2ValueYb;

	/**
	 *実績3（YB価_画面表示項目）
	 */
	protected Long record3ValueYb;

	/**
	 *実績4（YB価_画面表示項目）
	 */
	protected Long record4ValueYb;

	/**
	 *実績5（YB価_画面表示項目）
	 */
	protected Long record5ValueYb;

	/**
	 *実績6（YB価_画面表示項目）
	 */
	protected Long record6ValueYb;

	/**
	 *累計実績（YB価_画面表示項目）
	 */
	protected Long recordTotalValueYb;

	/**
	 *当月実績（YB価_画面表示項目）
	 */
	protected Long recordTougetuValueYb;
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * 期別計画値
	 */
	protected Long plannedTermValue;

	/**
	 * 品目集計対象フラグ
	 */
	protected String targetSummary;

	/**
	 * 活動区分
	 */
	private ActivityType activityType;

	/**
	 * JIS府県コード（Ref[施設情報].〔JIS府県コード〕）
	 */
	private Prefecture addrCodePref;

	/**
	 * JIS市区町村コード（Ref[施設情報].〔JIS市区町村コード〕）
	 */
	private String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）（Ref[JIS府県・市区町村].〔市区郡町村名（漢字）〕）
	 */
	private String shikuchosonMeiKj;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 実施計画(月別)を取得する。
	 *
	 * @return 実施計画
	 */
	public ImplMonthPlan getImplMonthPlan() {
		if (implMonthPlan == null) {
			implMonthPlan = new ImplMonthPlan();
		}
		return implMonthPlan;
	}

	/**
	 * 実施計画(月別)を設定する。
	 *
	 * @param implMonthPlan 実施計画
	 */
	public void setImplMonthPlan(ImplMonthPlan implMonthPlan) {
		this.implMonthPlan = implMonthPlan;
	}

	/**
	 * 品目名称(漢字)を取得する。
	 *
	 * @return 品目名称(漢字)
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称(漢字)を設定する。
	 *
	 * @param prodName 品目名称(漢字)
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 施設略式漢字名を取得する。
	 *
	 * @return 施設略式漢字名
	 */
	public String getInsAbbrName() {
		return insAbbrName;
	}

	/**
	 * 施設略式漢字名を設定する。
	 *
	 * @param insAbbrName 施設略式漢字名
	 */
	public void setInsAbbrName(String insAbbrName) {
		this.insAbbrName = insAbbrName;
	}

	/**
	 * 取引区分を取得する。
	 *
	 * @return 取引区分
	 */
	public TradeType getTradeType() {
		return tradeType;
	}

	/**
	 * 取引区分を設定する。
	 *
	 * @param tradeType 取引区分
	 */
	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	/**
	 * 依頼中フラグを取得する。
	 *
	 * @return 依頼中フラグ
	 */
	public Boolean getReqFlg() {
		return reqFlg;
	}

	/**
	 * 依頼中フラグを設定する。
	 *
	 * @param reqFlg 依頼中フラグ
	 */
	public void setReqFlg(Boolean reqFlg) {
		this.reqFlg = reqFlg;
	}

	/**
	 * 施設削除フラグを取得する。
	 *
	 * @return 施設削除フラグ
	 */
	public Boolean getInsDelFlg() {
		return insDelFlg;
	}

	/**
	 * 施設削除フラグを設定する。
	 *
	 * @param insDelFlg 施設削除フラグ
	 */
	public void setInsDelFlg(Boolean insDelFlg) {
		this.insDelFlg = insDelFlg;
	}

	/**
	 * 施設分類を取得する。
	 *
	 * @return 施設分類
	 */
	public InsClass getInsClass() {
		return insClass;
	}

	/**
	 * 施設分類を設定する。
	 *
	 * @param insClass 施設分類
	 */
	public void setInsClass(InsClass insClass) {
		this.insClass = insClass;
	}

	/**
	 * サブコード分類を取得する。
	 *
	 * @return サブコード分類
	 */
	public OldInsrFlg getOldInsrFlg() {
		return oldInsrFlg;
	}

	/**
	 * サブコード分類を設定する。
	 *
	 * @param oldInsrFlg サブコード分類
	 */
	public void setOldInsrFlg(OldInsrFlg oldInsrFlg) {
		this.oldInsrFlg = oldInsrFlg;
	}

	/**
	 * 施設担当の従業員情報を取得する。
	 *
	 * @return 施設担当の従業員情報
	 */
	public JgiMst getJgiMst() {
		return jgiMst;
	}

	/**
	 * 施設担当の従業員情報を設定する。
	 *
	 * @param jgiMst 施設担当の従業員情報
	 */
	public void setJgiMst(JgiMst jgiMst) {
		this.jgiMst = jgiMst;
	}

	/**
	 * 施設担当の組織情報を取得する。
	 *
	 * @return 組織情報
	 */
	public SosMst getSosMst() {
		return sosMst;
	}

	/**
	 * 施設担当の組織情報を取得する。
	 *
	 * @param sosMst 組織情報
	 */
	public void setSosMst(SosMst sosMst) {
		this.sosMst = sosMst;
	}

	/**
	 * 期別計画（合計）を取得する
	 *
	 * @return sumPlannedTermValue 期別計画（合計）
	 */
	public Long getSumPlannedTermValue() {
		return sumPlannedTermValue;
	}

	/**
	 * 月初計画1（YB価合計）を取得する
	 *
	 * @return sumPlanned1ValueYb 月初計画1（YB価合計）
	 */
	public Long getSumPlanned1ValueYb() {
		return sumPlanned1ValueYb;
	}

	/**
	 * 月初計画2（YB価合計）を取得する
	 *
	 * @return sumPlanned2ValueYb 月初計画2（YB価合計）
	 */
	public Long getSumPlanned2ValueYb() {
		return sumPlanned2ValueYb;
	}

	/**
	 * 月初計画3（YB価合計）を取得する
	 *
	 * @return sumPlanned3ValueYb 月初計画3（YB価合計）
	 */
	public Long getSumPlanned3ValueYb() {
		return sumPlanned3ValueYb;
	}

	/**
	 * 月初計画4（YB価合計）を取得する
	 *
	 * @return sumPlanned4ValueYb 月初計画4（YB価合計）
	 */
	public Long getSumPlanned4ValueYb() {
		return sumPlanned4ValueYb;
	}

	/**
	 * 月初計画5（YB価合計）を取得する
	 *
	 * @return sumPlanned5ValueYb 月初計画5（YB価合計）
	 */
	public Long getSumPlanned5ValueYb() {
		return sumPlanned5ValueYb;
	}

	/**
	 * 月初計画6（YB価合計）を取得する
	 *
	 * @return sumPlanned6ValueYb 月初計画6（YB価合計）
	 */
	public Long getSumPlanned6ValueYb() {
		return sumPlanned6ValueYb;
	}

	/**
	 * 月末見込1（YB価合計）を取得する
	 *
	 * @return sumExpected1ValueYb 月末見込1（YB価合計）
	 */
	public Long getSumExpected1ValueYb() {
		return sumExpected1ValueYb;
	}

	/**
	 * 月末見込2（YB価合計）を取得する
	 *
	 * @return sumExpected2ValueYb 月末見込2（YB価合計）
	 */
	public Long getSumExpected2ValueYb() {
		return sumExpected2ValueYb;
	}

	/**
	 * 月末見込3（YB価合計）を取得する
	 *
	 * @return sumExpected3ValueYb 月末見込3（YB価合計）
	 */
	public Long getSumExpected3ValueYb() {
		return sumExpected3ValueYb;
	}

	/**
	 * 月末見込4（YB価合計）を取得する
	 *
	 * @return sumExpected4ValueYb 月末見込4（YB価合計）
	 */
	public Long getSumExpected4ValueYb() {
		return sumExpected4ValueYb;
	}

	/**
	 * 月末見込5（YB価合計）を取得する
	 *
	 * @return sumExpected5ValueYb 月末見込5（YB価合計）
	 */
	public Long getSumExpected5ValueYb() {
		return sumExpected5ValueYb;
	}

	/**
	 * 月末見込6（YB価合計）を取得する
	 *
	 * @return sumExpected6ValueYb 月末見込6（YB価合計）
	 */
	public Long getSumExpected6ValueYb() {
		return sumExpected6ValueYb;
	}

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（Y価）を取得する
	 * @return record1ValueY 実績1（Y価）
	 */
	public Long getRecord1ValueY() {
		return record1ValueY;
	}

	/**
	 *実績1（B価）を取得する
	 * @return record1ValueB 実績1（B価）
	 */
	public Long getRecord1ValueB() {
		return record1ValueB;
	}

	/**
	 *実績2（Y価）を取得する
	 * @return record2ValueY 実績2（Y価）
	 */
	public Long getRecord2ValueY() {
		return record2ValueY;
	}

	/**
	 *実績2（B価）を取得する
	 * @return record2ValueB 実績2（B価）
	 */
	public Long getRecord2ValueB() {
		return record2ValueB;
	}

	/**
	 *実績3（Y価）を取得する
	 * @return record3ValueY 実績3（Y価）
	 */
	public Long getRecord3ValueY() {
		return record3ValueY;
	}

	/**
	 *実績3（B価）を取得する
	 * @return record3ValueB 実績3（B価）
	 */
	public Long getRecord3ValueB() {
		return record3ValueB;
	}

	/**
	 *実績4（Y価）を取得する
	 * @return record4ValueY 実績4（Y価）
	 */
	public Long getRecord4ValueY() {
		return record4ValueY;
	}

	/**
	 *実績4（B価）を取得する
	 * @return record4ValueB 実績4（B価）
	 */
	public Long getRecord4ValueB() {
		return record4ValueB;
	}

	/**
	 *実績5（Y価）を取得する
	 * @return record5ValueY 実績5（Y価）
	 */
	public Long getRecord5ValueY() {
		return record5ValueY;
	}

	/**
	 *実績5（B価）を取得する
	 * @return record5ValueB 実績5（B価）
	 */
	public Long getRecord5ValueB() {
		return record5ValueB;
	}

	/**
	 *実績6（Y価）を取得する
	 * @return record6ValueY 実績6（Y価）
	 */
	public Long getRecord6ValueY() {
		return record6ValueY;
	}

	/**
	 *実績6（B価）を取得する
	 * @return record6ValueB 実績6（B価）
	 */
	public Long getRecord6ValueB() {
		return record6ValueB;
	}

	/**
	 *累計実績（Y価）を取得する
	 * @return recordTotalValueY 累計実績（Y価）
	 */
	public Long getRecordTotalValueY() {
		return recordTotalValueY;
	}

	/**
	 *累計実績（B価）を取得する
	 * @return recordTotalValueB 累計実績（B価）
	 */
	public Long getRecordTotalValueB() {
		return recordTotalValueB;
	}

	/**
	 *実績1（YB価_画面表示項目）を取得する
	 * @return record1ValueYb 実績1（YB価_画面表示項目）
	 */
	public Long getRecord1ValueYb() {
		return record1ValueYb;
	}

	/**
	 *実績2（YB価_画面表示項目）を取得する
	 * @return record2ValueYb 実績2（YB価_画面表示項目）
	 */
	public Long getRecord2ValueYb() {
		return record2ValueYb;
	}

	/**
	 *実績3（YB価_画面表示項目）を取得する
	 * @return record3ValueYb 実績3（YB価_画面表示項目）
	 */
	public Long getRecord3ValueYb() {
		return record3ValueYb;
	}

	/**
	 *実績4（YB価_画面表示項目）を取得する
	 * @return record4ValueYb 実績4（YB価_画面表示項目）
	 */
	public Long getRecord4ValueYb() {
		return record4ValueYb;
	}

	/**
	 *実績5（YB価_画面表示項目）を取得する
	 * @return record5ValueYb 実績5（YB価_画面表示項目）
	 */
	public Long getRecord5ValueYb() {
		return record5ValueYb;
	}

	/**
	 *実績6（YB価_画面表示項目）を取得する
	 * @return record6ValueYb 実績6（YB価_画面表示項目）
	 */
	public Long getRecord6ValueYb() {
		return record6ValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を取得する
	 * @return recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public Long getRecordTotalValueYb() {
		return recordTotalValueYb;
	}

	/**
	 *当月実績（YB価_画面表示項目）を取得する
	 * @return recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public Long getRecordTougetuValueYb() {
		return recordTougetuValueYb;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * 期別計画（合計）を設定する
	 *
	 * @param sumPlannedTermValue 期別計画（合計）
	 */
	public void setSumPlannedTermValue(Long sumPlannedTermValue) {
		this.sumPlannedTermValue = sumPlannedTermValue;
	}

	/**
	 * 月初計画1（YB価合計）を設定する
	 *
	 * @param sumPlanned1ValueYb 月初計画1（YB価合計）
	 */
	public void setSumPlanned1ValueYb(Long sumPlanned1ValueYb) {
		this.sumPlanned1ValueYb = sumPlanned1ValueYb;
	}

	/**
	 * 月初計画2（YB価合計）を設定する
	 *
	 * @param sumPlanned2ValueYb 月初計画2（YB価合計）
	 */
	public void setSumPlanned2ValueYb(Long sumPlanned2ValueYb) {
		this.sumPlanned2ValueYb = sumPlanned2ValueYb;
	}

	/**
	 * 月初計画3（YB価合計）を設定する
	 *
	 * @param sumPlanned3ValueYb 月初計画3（YB価合計）
	 */
	public void setSumPlanned3ValueYb(Long sumPlanned3ValueYb) {
		this.sumPlanned3ValueYb = sumPlanned3ValueYb;
	}

	/**
	 * 月初計画4（YB価合計）を設定する
	 *
	 * @param sumPlanned4ValueYb 月初計画4（YB価合計）
	 */
	public void setSumPlanned4ValueYb(Long sumPlanned4ValueYb) {
		this.sumPlanned4ValueYb = sumPlanned4ValueYb;
	}

	/**
	 * 月初計画5（YB価合計）を設定する
	 *
	 * @param sumPlanned5ValueYb 月初計画5（YB価合計）
	 */
	public void setSumPlanned5ValueYb(Long sumPlanned5ValueYb) {
		this.sumPlanned5ValueYb = sumPlanned5ValueYb;
	}

	/**
	 * 月初計画6（YB価合計）を設定する
	 *
	 * @param sumPlanned6ValueYb 月初計画6（YB価合計）
	 */
	public void setSumPlanned6ValueYb(Long sumPlanned6ValueYb) {
		this.sumPlanned6ValueYb = sumPlanned6ValueYb;
	}

	/**
	 * 月末見込1（YB価合計）を設定する
	 *
	 * @param sumExpected1ValueYb 月末見込1（YB価合計）
	 */
	public void setSumExpected1ValueYb(Long sumExpected1ValueYb) {
		this.sumExpected1ValueYb = sumExpected1ValueYb;
	}

	/**
	 * 月末見込2（YB価合計）を設定する
	 *
	 * @param sumExpected2ValueYb 月末見込2（YB価合計）
	 */
	public void setSumExpected2ValueYb(Long sumExpected2ValueYb) {
		this.sumExpected2ValueYb = sumExpected2ValueYb;
	}

	/**
	 * 月末見込3（YB価合計）を設定する
	 *
	 * @param sumExpected3ValueYb 月末見込3（YB価合計）
	 */
	public void setSumExpected3ValueYb(Long sumExpected3ValueYb) {
		this.sumExpected3ValueYb = sumExpected3ValueYb;
	}

	/**
	 * 月末見込4（YB価合計）を設定する
	 *
	 * @param sumExpected4ValueYb 月末見込4（YB価合計）
	 */
	public void setSumExpected4ValueYb(Long sumExpected4ValueYb) {
		this.sumExpected4ValueYb = sumExpected4ValueYb;
	}

	/**
	 * 月末見込5（YB価合計）を設定する
	 *
	 * @param sumExpected5ValueYb 月末見込5（YB価合計）
	 */
	public void setSumExpected5ValueYb(Long sumExpected5ValueYb) {
		this.sumExpected5ValueYb = sumExpected5ValueYb;
	}

	/**
	 * 月末見込6（YB価合計）を設定する
	 *
	 * @param sumExpected6ValueYb 月末見込6（YB価合計）
	 */
	public void setSumExpected6ValueYb(Long sumExpected6ValueYb) {
		this.sumExpected6ValueYb = sumExpected6ValueYb;
	}

//add Start 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示
	/**
	 *実績1（Y価）を設定する
	 * @param record1ValueY 実績1（Y価）
	 */
	public void setRecord1ValueY(Long record1ValueY) {
		this.record1ValueY = record1ValueY;
	}

	/**
	 *実績1（B価）を設定する
	 * @param record1ValueB 実績1（B価）
	 */
	public void setRecord1ValueB(Long record1ValueB) {
		this.record1ValueB = record1ValueB;
	}

	/**
	 *実績2（Y価）を設定する
	 * @param record2ValueY 実績2（Y価）
	 */
	public void setRecord2ValueY(Long record2ValueY) {
		this.record2ValueY = record2ValueY;
	}

	/**
	 *実績2（B価）を設定する
	 * @param record2ValueB 実績2（B価）
	 */
	public void setRecord2ValueB(Long record2ValueB) {
		this.record2ValueB = record2ValueB;
	}

	/**
	 *実績3（Y価）を設定する
	 * @param record3ValueY 実績3（Y価）
	 */
	public void setRecord3ValueY(Long record3ValueY) {
		this.record3ValueY = record3ValueY;
	}

	/**
	 *実績3（B価）を設定する
	 * @param record3ValueB 実績3（B価）
	 */
	public void setRecord3ValueB(Long record3ValueB) {
	this.record3ValueB = record3ValueB;
	}

	/**
	 *実績4（Y価）を設定する
	 * @param record4ValueY 実績4（Y価）
	 */
	public void setRecord4ValueY(Long record4ValueY) {
		this.record4ValueY = record4ValueY;
	}

	/**
	 *実績4（B価）を設定する
	 * @param record4ValueB 実績4（B価）
	 */
	public void setRecord4ValueB(Long record4ValueB) {
		this.record4ValueB = record4ValueB;
	}

	/**
	 *実績5（Y価）を設定する
	 * @param record5ValueY 実績5（Y価）
	 */
	public void setRecord5ValueY(Long record5ValueY) {
		this.record5ValueY = record5ValueY;
	}

	/**
	 *実績5（B価）を設定する
	 * @param record5ValueB 実績5（B価）
	 */
	public void setRecord5ValueB(Long record5ValueB) {
		this.record5ValueB = record5ValueB;
	}

	/**
	 *実績6（Y価）を設定する
	 * @param record6ValueY 実績6（Y価）
	 */
	public void setRecord6ValueY(Long record6ValueY) {
		this.record6ValueY = record6ValueY;
	}

	/**
	 *実績6（B価）を設定する
	 * @param record6ValueB 実績6（B価）
	 */
	public void setRecord6ValueB(Long record6ValueB) {
		this.record6ValueB = record6ValueB;
	}

	/**
	 *累計実績（Y価）を設定する
	 * @param recordTotalValueY 累計実績（Y価）
	 */
	public void setRecordTotalValueY(Long recordTotalValueY) {
		this.recordTotalValueY = recordTotalValueY;
	}

	/**
	 *累計実績（B価）を設定する
	 * @param recordTotalValueB 累計実績（B価）
	 */
	public void setRecordTotalValueB(Long recordTotalValueB) {
		this.recordTotalValueB = recordTotalValueB;
	}

	/**
	 *実績1（YB価_画面表示項目）を設定する
	 * @param record1ValueYb 実績1（YB価_画面表示項目）
	 */
	public void setRecord1ValueYb(Long record1ValueYb) {
		this.record1ValueYb = record1ValueYb;
	}

	/**
	 *実績2（YB価_画面表示項目）を設定する
	 * @param record2ValueYb 実績2（YB価_画面表示項目）
	 */
	public void setRecord2ValueYb(Long record2ValueYb) {
		this.record2ValueYb = record2ValueYb;
	}

	/**
	 *実績3（YB価_画面表示項目）を設定する
	 * @param record3ValueYb 実績3（YB価_画面表示項目）
	 */
	public void setRecord3ValueYb(Long record3ValueYb) {
		this.record3ValueYb = record3ValueYb;
	}

	/**
	 *実績4（YB価_画面表示項目）を設定する
	 * @param record4ValueYb 実績4（YB価_画面表示項目）
	 */
	public void setRecord4ValueYb(Long record4ValueYb) {
		this.record4ValueYb = record4ValueYb;
	}

	/**
	 *実績5（YB価_画面表示項目）を設定する
	 * @param record5ValueYb 実績5（YB価_画面表示項目）
	 */
	public void setRecord5ValueYb(Long record5ValueYb) {
		this.record5ValueYb = record5ValueYb;
	}

	/**
	 *実績6（YB価_画面表示項目）を設定する
	 * @param record6ValueYb 実績6（YB価_画面表示項目）
	 */
	public void setRecord6ValueYb(Long record6ValueYb) {
		this.record6ValueYb = record6ValueYb;
	}

	/**
	 *累計実績（YB価_画面表示項目）を設定する
	 * @param recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public void setRecordTotalValueYb(Long recordTotalValueYb) {
		this.recordTotalValueYb = recordTotalValueYb;
	}

	/**
	 *当月実績（YB価_画面表示項目）を設定する
	 * @param recordTotalValueYb 累計実績（YB価_画面表示項目）
	 */
	public void setRecordTougetuValueYb(Long recordTougetuValueYb) {
		this.recordTougetuValueYb = recordTougetuValueYb;
	}
//add End 2022/08/25 R.takamoto No.8　計画管理の月別計画に納入実績の値を表示

	/**
	 * 期別計画値を取得する
	 *
	 * @return plannedTermValue 期別計画値
	 */
	public Long getPlannedTermValue() {
		return plannedTermValue;
	}

	/**
	 * 期別計画値を設定する
	 *
	 * @param plannedTermValue 期別計画値
	 */
	public void setPlannedTermValue(Long plannedTermValue) {
		this.plannedTermValue = plannedTermValue;
	}

	/**
	 * 品目集計対象フラグを取得する
	 *
	 * @return targetSummary 品目集計対象フラグ
	 */
	public String getTargetSummary() {
		return targetSummary;
	}

	/**
	 * 品目集計対象フラグを設定する
	 *
	 * @param targetSummary 品目集計対象フラグ
	 */
	public void setTargetSummary(String targetSummary) {
		this.targetSummary = targetSummary;
	}

	/**
	 * 活動区分を取得する。
	 *
	 * @return 活動区分
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * 活動区分を設定する。
	 *
	 * @param activityType 活動区分
	 */
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	/**
	 * JIS府県コードを取得する。
	 *
	 * @return addrCodePref JIS府県コード
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS府県コードを設定する。
	 *
	 * @param addrCodePref JIS府県コード
	 */
	public void setAddrCodePref(Prefecture addrCodePref) {
		this.addrCodePref = addrCodePref;
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
	 * JIS市区町村コードを設定する。
	 *
	 * @param addrCodeCity JIS市区町村コード
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 *
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 市区郡町村名（漢字）を設定する。
	 *
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
	}

	@Override
	public int compareTo(ManageInsMonthPlan obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.insNo, obj.insNo).append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ManageInsMonthPlan.class.isAssignableFrom(entry.getClass())) {
			ManageInsMonthPlan obj = (ManageInsMonthPlan) entry;
			return new EqualsBuilder().append(this.insNo, obj.insNo).append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.insNo).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
