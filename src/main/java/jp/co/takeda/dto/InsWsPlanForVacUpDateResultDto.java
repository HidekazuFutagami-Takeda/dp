package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.view.InsWsPlanForVacForRef;
import jp.co.takeda.util.ConvertUtil;

/**
 * ワクチン用施設特約店別計画編集画面の検索結果用DTO（リスト）
 *
 * @author khashimoto
 */
public class InsWsPlanForVacUpDateResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * シーケンスキー
	 */
	private final String seqKey;

	/**
	 * JIS府県コード
	 */
	private final String addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）
	 */
	private final String shikuchosonMeiKj;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設名
	 */
	private final String insName;

	/**
	 * TMS特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 特約店名
	 */
	private final String tmsTytenName;

	/**
	 * 配分値(B)
	 */
	private final Long distValueB;

	// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	/**
	 * 修正値(B)
	 */
	private final Long modifyValueB;
	// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる

	/**
	 * 計画値(B)
	 */
	private final Long plannedValueB;

	/**
	 * 計画更新日時
	 */
	private final Date plannedUpDate;

	/**
	 * 過去実績参照部のリスト
	 */
	private final List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu;

	/**
	 * 特定施設個別計画かを示すフラグ
	 */
	private final Boolean specialInsPlanFlg;

	/**
	 * 配分除外施設かを示すフラグ
	 */
	private final Boolean exceptDistInsFlg;

	/**
	 * 削除施設かを示すフラグ
	 */
	private final Boolean delInsFlg;

	/**
	 * 施設合計表示フラグ
	 */
	private final Boolean insSumFlg;

	/**
	 * 市区郡重点先合計表示フラグ
	 */
	private final Boolean sikuJtnFlg;

	/**
	 * 市区郡一般先合計表示フラグ
	 */
	private final Boolean sikuIppanFlg;

	/**
	 * 市区郡合計表示フラグ
	 */
	private final Boolean sikuSumFlg;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private final Boolean planTaiGaiFlgTok;

	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private final Boolean planTaiGaiFlgRik;


	/**
	 * コンストラクタ(明細行)
	 *
	 * @param insWsPlan 参照用のワクチン用施設特約店別計画
	 * @param insWsPlanMonNnu 過去実績参照部
	 */
	public InsWsPlanForVacUpDateResultDto(InsWsPlanForVacForRef insWsPlan, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu) {
		this.seqKey = ConvertUtil.toString(insWsPlan.getSeqKey());
		this.addrCodePref = insWsPlan.getAddrCodePref().getDbValue();
		this.addrCodeCity = insWsPlan.getAddrCodeCity();
		this.shikuchosonMeiKj = insWsPlan.getShikuchosonMeiKj();
		this.insNo = insWsPlan.getInsNo();
		this.insName = insWsPlan.getInsAbbrName();
		this.tmsTytenCd = insWsPlan.getTmsTytenCd();
		this.tmsTytenName = insWsPlan.getTmsTytenName();
		this.distValueB = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getDistValueB());
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.modifyValueB = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getModifyValueB());
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.plannedValueB = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getPlannedValueB());
		this.plannedUpDate = insWsPlan.getUpDate();
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.specialInsPlanFlg = insWsPlan.getSpecialInsPlanFlg();
		this.exceptDistInsFlg = insWsPlan.getExceptDistInsFlg();
		this.delInsFlg = insWsPlan.getDelInsFlg();
		this.insSumFlg = false;
		this.sikuJtnFlg = false;
		this.sikuIppanFlg = false;
		this.sikuSumFlg = false;

		if(insWsPlan != null) {
			this.planTaiGaiFlgTok = insWsPlan.getPlanTaiGaiFlgTok();
			this.planTaiGaiFlgRik = insWsPlan.getPlanTaiGaiFlgRik();
		}else{
			this.planTaiGaiFlgTok = false;
			this.planTaiGaiFlgRik = false;
		}
	}

	/**
	 * コンストラクタ(各サマリー行)
	 *
	 * @param insWsPlan 参照用のワクチン用施設特約店別計画
	 * @param distValueB 配分値サマリー
	 * @param plannedValueB 計画値サマリー
	 * @param insWsPlanMonNnu 過去実績参照部
	 * @param insSumFlg 施設合計表示フラグ
	 * @param sikuJtnFlg 市区郡重点先合計表示フラグ
	 * @param sikuIppanFlg 市区郡一般先合計表示フラグ
	 * @param sikuSumFlg 市区郡合計表示フラグ
	 * @param plannedUpDate 計画更新日時
	 */
	// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	//public InsWsPlanForVacUpDateResultDto(InsWsPlanForVacForRef insWsPlan, Long distValueB, Long plannedValueB, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu,
	//		Boolean insSumFlg, Boolean sikuJtnFlg, Boolean sikuIppanFlg, Boolean sikuSumFlg, Date plannedUpDate) {
	public InsWsPlanForVacUpDateResultDto(InsWsPlanForVacForRef insWsPlan, Long distValueB, Long plannedValueB, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu,
		Boolean insSumFlg, Boolean sikuJtnFlg, Boolean sikuIppanFlg, Boolean sikuSumFlg, Date plannedUpDate, Long modifyValueB) {
	// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる

		this.seqKey = null;
		this.addrCodePref = insWsPlan.getAddrCodePref().getDbValue();
		this.addrCodeCity = insWsPlan.getAddrCodeCity();
		this.shikuchosonMeiKj = insWsPlan.getShikuchosonMeiKj();
		// 施設サマリー行以外は施設情報はセットしない。
		if (insSumFlg) {
			this.insNo = insWsPlan.getInsNo();
			this.insName = insWsPlan.getInsAbbrName();
			this.specialInsPlanFlg = insWsPlan.getSpecialInsPlanFlg();
			this.exceptDistInsFlg = insWsPlan.getExceptDistInsFlg();
			this.delInsFlg = insWsPlan.getDelInsFlg();
		} else {
			this.insNo = null;
			this.insName = null;
			this.specialInsPlanFlg = false;
			this.exceptDistInsFlg = false;
			this.delInsFlg = false;
		}
		this.tmsTytenCd = null;
		this.tmsTytenName = null;
		this.distValueB = ConvertUtil.parseMoneyToThousandUnit(distValueB);
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.modifyValueB = ConvertUtil.parseMoneyToThousandUnit(modifyValueB);
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.plannedValueB = ConvertUtil.parseMoneyToThousandUnit(plannedValueB);
		this.plannedUpDate = plannedUpDate;
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.insSumFlg = insSumFlg;
		this.sikuJtnFlg = sikuJtnFlg;
		this.sikuIppanFlg = sikuIppanFlg;
		this.sikuSumFlg = sikuSumFlg;

		if(insWsPlan != null) {
			this.planTaiGaiFlgTok = insWsPlan.getPlanTaiGaiFlgTok();
			this.planTaiGaiFlgRik = insWsPlan.getPlanTaiGaiFlgRik();
		}else{
			this.planTaiGaiFlgTok = false;
			this.planTaiGaiFlgRik = false;
		}
	}

	/**
	 * コンストラクタ(全サマリー行)
	 *
	 * @param distValueB 配分値サマリー
	 * @param plannedValueB 計画値サマリー
	 * @param insWsPlanMonNnu 過去実績参照部
	 */
	// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	//public InsWsPlanForVacUpDateResultDto(Long distValueB, Long plannedValueB, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu) {
	public InsWsPlanForVacUpDateResultDto(Long distValueB, Long plannedValueB, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu, Long modifyValueB) {
	// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.seqKey = null;
		this.addrCodePref = null;
		this.addrCodeCity = null;
		this.shikuchosonMeiKj = null;
		this.insNo = null;
		this.insName = null;
		this.tmsTytenCd = null;
		this.tmsTytenName = null;
		this.distValueB = ConvertUtil.parseMoneyToThousandUnit(distValueB);
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.modifyValueB = ConvertUtil.parseMoneyToThousandUnit(modifyValueB);
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.plannedValueB = ConvertUtil.parseMoneyToThousandUnit(plannedValueB);
		this.plannedUpDate = null;
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.specialInsPlanFlg = null;
		this.exceptDistInsFlg = null;
		this.delInsFlg = null;
		this.insSumFlg = false;
		this.sikuJtnFlg = false;
		this.sikuIppanFlg = false;
		this.sikuSumFlg = false;

		this.planTaiGaiFlgTok = false;
		this.planTaiGaiFlgRik = false;
	}

	/**
	 * コンストラクタ(追加行)
	 *
	 * @param insMst 施設情報
	 * @param tmsTytenMstUn 特約店情報
	 * @param insWsPlanMonNnu 過去実績参照部
	 */
	public InsWsPlanForVacUpDateResultDto(DpsInsMst insMst, TmsTytenMstUn tmsTytenMstUn, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu) {
		this.seqKey = null;
		this.addrCodePref = insMst.getAddrCodePref().getDbValue();
		this.addrCodeCity = insMst.getAddrCodeCity();
		this.shikuchosonMeiKj = insMst.getShikuchosonMeiKj();
		this.insNo = insMst.getInsNo();
		this.insName = insMst.getInsAbbrName();
		this.tmsTytenCd = tmsTytenMstUn.getTmsTytenCd();
		this.tmsTytenName = tmsTytenMstUn.getTmsTytenMeiKj();
		this.distValueB = null;
		// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.modifyValueB = null;
		// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
		this.plannedValueB = null;
		this.plannedUpDate = null;
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.specialInsPlanFlg = null;
		this.exceptDistInsFlg = null;
		this.delInsFlg = null;
		this.insSumFlg = false;
		this.sikuJtnFlg = false;
		this.sikuIppanFlg = false;
		this.sikuSumFlg = false;

		if(tmsTytenMstUn != null) {
			this.planTaiGaiFlgTok = tmsTytenMstUn.getPlanTaiGaiFlgTok();
			this.planTaiGaiFlgRik = tmsTytenMstUn.getPlanTaiGaiFlgRik();
		}else{
			this.planTaiGaiFlgTok = false;
			this.planTaiGaiFlgRik = false;
		}
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return seqKey シーケンスキー
	 */
	public String getSeqKey() {
		return seqKey;
	}

	/**
	 * JIS府県コードを取得する。
	 *
	 * @return JIS府県コード
	 */
	public String getAddrCodePref() {
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
	 * TMS特約店コードを取得する。
	 *
	 * @return tmsTytenCd TMS特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店名を取得する。
	 *
	 * @return tmsTytenName 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 配分値(B)を取得する。
	 *
	 * @return distValueB 配分値(B)
	 */
	public Long getDistValueB() {
		return distValueB;
	}

	// add Start 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる
	/**
	 * 修正値(B)を取得する。
	 *
	 * @return modifyValueB 修正値(B)
	 */
	public Long getModifyValueB() {
		return modifyValueB;
	}
	// add End 2022/6/28 H.Futagami バックログNo.20　施設特約店別計画編集でワクチンの一覧項目を医薬に合わせる


	/**
	 * 計画値(B)を取得する。
	 *
	 * @return plannedValueB 計画値(B)
	 */
	public Long getPlannedValueB() {
		return plannedValueB;
	}

	/**
	 * 計画更新日時を取得する。
	 *
	 * @return plannedUpDate 計画更新日時
	 */
	public Date getPlannedUpDate() {
		return plannedUpDate;
	}

	/**
	 * 過去実績参照部のリストを取得する。
	 *
	 * @return insWsPlanMonNnu 過去実績参照部のリスト
	 */
	public List<InsWsPlanUpDateResultMonNnuDto> getInsWsPlanMonNnu() {
		return insWsPlanMonNnu;
	}

	/**
	 * 特定施設個別計画かを示すフラグを取得する。
	 *
	 * @return specialInsPlanFlg 特定施設個別計画かを示すフラグ
	 */
	public Boolean getSpecialInsPlanFlg() {
		return specialInsPlanFlg;
	}

	/**
	 * 配分除外施設かを示すフラグを取得する。
	 *
	 * @return exceptDistInsFlg 配分除外施設かを示すフラグ
	 */
	public Boolean getExceptDistInsFlg() {
		return exceptDistInsFlg;
	}

	/**
	 * 削除施設かを示すフラグを取得する。
	 *
	 * @return delInsFlg 削除施設かを示すフラグ
	 */
	public Boolean getDelInsFlg() {
		return delInsFlg;
	}

	/**
	 * 施設合計表示フラグを取得する。
	 *
	 * @return insSumFlg 施設合計表示フラグ
	 */
	public Boolean getInsSumFlg() {
		return insSumFlg;
	}

	/**
	 * 市区郡重点先合計表示フラグを取得する。
	 *
	 * @return 市区郡重点先合計表示フラグ
	 */
	public Boolean getSikuJtnFlg() {
		return sikuJtnFlg;
	}

	/**
	 * 市区郡一般先合計表示フラグを取得する。
	 *
	 * @return 市区郡一般先合計表示フラグ
	 */
	public Boolean getSikuIppanFlg() {
		return sikuIppanFlg;
	}

	/**
	 * 市区郡合計表示フラグを取得する。
	 *
	 * @return 市区郡合計表示フラグ
	 */
	public Boolean getSikuSumFlg() {
		return sikuSumFlg;
	}

	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	public Boolean getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
