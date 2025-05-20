package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.ProdInsInfoErrKbn;
import jp.co.takeda.model.view.InsWsPlanForRef;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設特約店別計画編集画面の検索結果用DTO（リスト）
 *
 * @author siwamoto
 */
public class InsWsPlanUpDateResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * シーケンスキー
	 */
	private final String seqKey;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設内部コード
	 */
	private final String relnInsNo;

	/**
	 * 施設調剤別（本院/A調/B調）
	 */
	private final String insHeader;

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
	 * 配分値(Y)
	 */
	private final Long distValueY;

	/**
	 * 修正値(Y)
	 */
	private final Long modifyValueY;

	/**
	 * 確定値(Y)
	 */
	private final Long plannedValueY;

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
	 * 合算施設合計表示フラグ
	 */
	private final Boolean oyakoInsSumFlg;

	/**
	 * 計画立案対象外フラグ(当期用)
	 */
	private final Boolean planTaiGaiFlgTok;

	/**
	 * 計画立案対象外フラグ(来期用)
	 */
	private final Boolean planTaiGaiFlgRik;

	/**
	 * 施設情報名称
	 */
	private final String insInfoName;

	/**
	 * 表示文字カラーコード
	 */
	private final String dispFontColCd;

	/**
	 * エラーフラグ
	 */
	private final Boolean errFlg;

	/**
	 * 警告フラグ
	 */
	private final Boolean alertFlg;

	/**
	 * コンストラクタ
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param insWsPlanMonNnu 過去実績参照部
	 */
	public InsWsPlanUpDateResultDto(InsWsPlanForRef insWsPlan, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu) {
		this.seqKey = ConvertUtil.toString(insWsPlan.getSeqKey());
		this.insNo = insWsPlan.getInsNo();
		this.insHeader = InsClass.getInsClassName(insWsPlan.getInsClass(), insWsPlan.getOldInsrFlg());
		this.insName = insWsPlan.getInsAbbrName();
		this.relnInsNo = insWsPlan.getRelnInsNo();
		this.tmsTytenCd = insWsPlan.getTmsTytenCd();
		this.tmsTytenName = insWsPlan.getTmsTytenName();
		this.distValueY = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getDistValueY());
		this.modifyValueY = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getModifyValueY());
		this.plannedValueY = ConvertUtil.parseMoneyToThousandUnit(insWsPlan.getPlannedValueY());
		this.plannedUpDate = insWsPlan.getUpDate();
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.specialInsPlanFlg = insWsPlan.getSpecialInsPlanFlg();
		this.exceptDistInsFlg = insWsPlan.getExceptDistInsFlg();
		this.delInsFlg = insWsPlan.getDelInsFlg();
		this.insSumFlg = false;
		this.oyakoInsSumFlg = false;
		if (insWsPlan != null) {
			this.planTaiGaiFlgTok = insWsPlan.getPlanTaiGaiFlgTok();
			this.planTaiGaiFlgRik = insWsPlan.getPlanTaiGaiFlgRik();
		} else {
			this.planTaiGaiFlgTok = false;
			this.planTaiGaiFlgRik = false;
		}
		this.insInfoName = insWsPlan.getProdIns().getInsInfoName();
		this.dispFontColCd = insWsPlan.getProdIns().getDispFontColCd();
		this.errFlg = ProdInsInfoErrKbn.ERROR == insWsPlan.getProdIns().getProdInsInfoErrKbn();
		this.alertFlg = ProdInsInfoErrKbn.ALERT == insWsPlan.getProdIns().getProdInsInfoErrKbn();
	}

	/**
	 * コンストラクタ(施設サマリー行)
	 *
	 * @param insWsPlan 参照用の施設特約店別計画
	 * @param distValueY 配分値サマリー
	 * @param modifyValueY 修正値サマリー
	 * @param plannedValueY 計画値サマリー
	 * @param insWsPlanMonNnu 過去実績参照部
	 */
	public InsWsPlanUpDateResultDto(InsWsPlanForRef insWsPlan, Long distValueY, Long modifyValueY, Long plannedValueY, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu, boolean isOyako) {
		this.seqKey = null;
		this.insNo = insWsPlan.getInsNo();
		this.insHeader = InsClass.getInsClassName(insWsPlan.getInsClass(), insWsPlan.getOldInsrFlg());
		this.insName = insWsPlan.getInsAbbrName();
		this.relnInsNo = insWsPlan.getRelnInsNo();
		this.tmsTytenCd = null;
		this.tmsTytenName = null;
		this.distValueY = ConvertUtil.parseMoneyToThousandUnit(distValueY);
		this.modifyValueY = ConvertUtil.parseMoneyToThousandUnit(modifyValueY);
		this.plannedValueY = ConvertUtil.parseMoneyToThousandUnit(plannedValueY);
		this.plannedUpDate = null;
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.specialInsPlanFlg = insWsPlan.getSpecialInsPlanFlg();
		this.exceptDistInsFlg = insWsPlan.getExceptDistInsFlg();
		this.delInsFlg = insWsPlan.getDelInsFlg();
		if(isOyako){
			this.insSumFlg = false;
			this.oyakoInsSumFlg = true;
		} else {
			this.insSumFlg = true;
			this.oyakoInsSumFlg = false;
		}
		this.planTaiGaiFlgTok = null;
		this.planTaiGaiFlgRik = null;
		this.insInfoName = insWsPlan.getProdIns().getInsInfoName();
		this.dispFontColCd = insWsPlan.getProdIns().getDispFontColCd();
		this.errFlg = ProdInsInfoErrKbn.ERROR == insWsPlan.getProdIns().getProdInsInfoErrKbn();
		this.alertFlg = ProdInsInfoErrKbn.ALERT == insWsPlan.getProdIns().getProdInsInfoErrKbn();
	}

	/**
	 * コンストラクタ(サマリー行)
	 *
	 * @param distValueY 配分値サマリー
	 * @param plannedValueY 計画値サマリー
	 * @param insWsPlanMonNnu 過去実績参照部
	 */
	public InsWsPlanUpDateResultDto(Long distValueY, Long modifyValueY, Long plannedValueY, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu) {
		this.seqKey = null;
		this.insNo = null;
		this.insHeader = null;
		this.insName = null;
		this.relnInsNo = null;
		this.tmsTytenCd = null;
		this.tmsTytenName = null;
		this.distValueY = ConvertUtil.parseMoneyToThousandUnit(distValueY);
		this.modifyValueY = ConvertUtil.parseMoneyToThousandUnit(modifyValueY);
		this.plannedValueY = ConvertUtil.parseMoneyToThousandUnit(plannedValueY);
		this.plannedUpDate = null;
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.specialInsPlanFlg = null;
		this.exceptDistInsFlg = null;
		this.delInsFlg = null;
		this.insSumFlg = false;
		this.oyakoInsSumFlg = false;
		this.planTaiGaiFlgTok = null;
		this.planTaiGaiFlgRik = null;
		this.insInfoName = null;
		this.dispFontColCd = null;
		this.errFlg = false;
		this.alertFlg = false;
	}

	/**
	 * コンストラクタ(追加行)
	 *
	 * @param insMst 施設情報
	 * @param tmsTytenMstUn 特約店情報
	 * @param insWsPlanMonNnu 過去実績参照部
	 */
	public InsWsPlanUpDateResultDto(DpsInsMst insMst, TmsTytenMstUn tmsTytenMstUn, List<InsWsPlanUpDateResultMonNnuDto> insWsPlanMonNnu) {
		this.seqKey = null;
		this.insNo = insMst.getInsNo();
		this.insHeader = InsClass.getInsClassName(insMst.getInsClass(), insMst.getOldInsrFlg());
		this.insName = insMst.getInsAbbrName();
		this.relnInsNo = insMst.getRelnInsNo();
		this.tmsTytenCd = tmsTytenMstUn.getTmsTytenCd();
		this.tmsTytenName = tmsTytenMstUn.getTmsTytenMeiKj();
		this.distValueY = null;
		this.modifyValueY = null;
		this.plannedValueY = null;
		this.plannedUpDate = null;
		this.insWsPlanMonNnu = insWsPlanMonNnu;
		this.specialInsPlanFlg = null;
		this.exceptDistInsFlg = null;
		this.delInsFlg = null;
		this.insSumFlg = false;
		this.oyakoInsSumFlg = false;
		if (tmsTytenMstUn != null) {
			this.planTaiGaiFlgTok = tmsTytenMstUn.getPlanTaiGaiFlgTok();
			this.planTaiGaiFlgRik = tmsTytenMstUn.getPlanTaiGaiFlgRik();
		} else {
			this.planTaiGaiFlgTok = false;
			this.planTaiGaiFlgRik = false;
		}
		this.insInfoName = null;
		this.dispFontColCd = null;
		this.errFlg = false;
		this.alertFlg = false;
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
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 内部施設コード を取得する。
	 *
	 * @return 内部施設コード
	 */
	public String getRelnInsNo() {
		return relnInsNo;
	}

	/**
	 * 施設調剤別（本院/A調/B調）を取得する。
	 *
	 * @return insHeader 施設調剤別（本院/A調/B調）
	 */
	public String getInsHeader() {
		return insHeader;
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
	 * 配分値(Y)を取得する。
	 *
	 * @return distValueY 配分値(Y)
	 */
	public Long getDistValueY() {
		return distValueY;
	}

	/**
	 * 修正値(Y)を取得する。
	 *
	 * @return modifyValueY 計画値(Y)
	 */
	public Long getModifyValueY() {
		return modifyValueY;
	}

	/**
	 * 確定値(Y)を取得する。
	 *
	 * @return plannedValueY 計画値(Y)
	 */
	public Long getPlannedValueY() {
		return plannedValueY;
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
	 * 合算施設合計表示フラグを取得する。
	 *
	 * @return oyakoInsSumFlg 合算施設合計表示フラグ
	 */
	public Boolean getOyakoInsSumFlg() {
		return oyakoInsSumFlg;
	}

	/**
	 * 計画立案対象外フラグ(当期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(当期用)
	 */
	public Boolean getPlanTaiGaiFlgTok() {
		return planTaiGaiFlgTok;
	}

	/**
	 * 計画立案対象外フラグ(来期用)を取得する。
	 *
	 * @return 計画立案対象外フラグ(来期用)
	 */
	public Boolean getPlanTaiGaiFlgRik() {
		return planTaiGaiFlgRik;
	}

	/**
	 * 施設情報名称を取得する。
	 *
	 * @return 施設情報名称
	 */
	public String getInsInfoName() {
		return insInfoName;
	}

	/**
	 * 表示文字カラーコードを取得する。
	 *
	 * @return 表示文字カラーコード
	 */
	public String getDispFontColCd() {
		return dispFontColCd;
	}

	/**
	 * エラーフラグを取得する。
	 *
	 * @return エラーフラグ
	 */
	public Boolean getErrFlg() {
		return errFlg;
	}

	/**
	 * アラートフラグを取得する。
	 *
	 * @return アラートフラグ
	 */
	public Boolean getAlertFlg() {
		return alertFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
