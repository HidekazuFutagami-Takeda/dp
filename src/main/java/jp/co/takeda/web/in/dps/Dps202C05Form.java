package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.dto.SpecialInsPlanForVacDto;
import jp.co.takeda.dto.SpecialInsPlanModifyForVacDto;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps202C05((ワ)特定施設個別計画立案画面(担当者立案))のフォームクラス
 * 
 * @author siwamoto
 */
public class Dps202C05Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS202C05_DATA_R
	 */
	public static final BoxKey DPS202C05_DATA_R = new BoxKeyPerClassImpl(Dps202C05Form.class, InsWsPlanUpDateResultListDto.class);

	/**
	 * DPS202C05X02_DATA_R
	 */
	public static final BoxKey DPS202C05X02_DATA_R = new BoxKeyPerClassImpl(Dps202C05Form.class, MonNnu.class);

	/**
	 * 登録権限
	 */
	public static final DpAuthority DPS202C05_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * シーケンスキー
	 */
	private Long[] seqKey;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 品目固定コード
	 */
	private String[] prodCode;

	/**
	 * TMS特約店コード
	 */
	private String[] tmsTytenCd;

	/**
	 * 計画立案区分
	 */
	private String planType;

	/**
	 * 施設コード(A調含)
	 */
	private String[] insNoA;

	/**
	 * 計画値(B)
	 */
	private String[] plannedValueB;

	/**
	 * 氏名(Ref[従業員情報].〔氏名〕)
	 */
	private String jgiName;

	/**
	 * 集計行フラグ
	 */
	private String[] sumRowFlg;

	/**
	 * 画面に表示されている更新日時
	 */
	private String upDate;

	/**
	 * 組織コード(特約店G)
	 */
	private String sosCd3;

	/**
	 * 表示品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 1行のデータ
	 */
	private String[] para;

	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * シーケンスキーを取得する。
	 * 
	 * @return 従業員番号
	 */
	public Long[] getSeqKey() {
		return seqKey;
	}

	/**
	 * シーケンスキーを設定する。
	 * 
	 * @param seqKey シーケンスキー
	 */
	public void setSeqKey(Long[] seqKey) {
		this.seqKey = seqKey;
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return 従業員番号
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 * 
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
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
	 * 品目固定コードを取得する。
	 * 
	 * @return 品目固定コード
	 */
	public String[] getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 * 
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String[] prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * TMS特約店コードを取得する。
	 * 
	 * @return TMS特約店コード
	 */
	public String[] getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * TMS特約店コードを設定する。
	 * 
	 * @param tmsTytenCd TMS特約店コード
	 */
	public void setTmsTytenCd(String[] tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 計画立案区分を取得する。
	 * 
	 * @return 計画立案区分
	 */
	public String getPlanType() {
		return planType;
	}

	/**
	 * 計画立案区分を設定する。
	 * 
	 * @param planType 計画立案区分
	 */
	public void setPlanType(String planType) {
		this.planType = planType;
	}

	/**
	 * 計画値(B)を取得する。
	 * 
	 * @return 計画値(B)
	 */
	public String[] getPlannedValueB() {
		return plannedValueB;
	}

	/**
	 * 計画値(B)を設定する。
	 * 
	 * @param plannedValueB 計画値(B)
	 */
	public void setPlannedValueB(String[] plannedValueB) {
		this.plannedValueB = plannedValueB;
	}

	/**
	 * 氏名を取得する。
	 * 
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 * 
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 集計行フラグを取得する。
	 * 
	 * @return 集計行フラグ
	 */
	public String[] getSumRowFlg() {
		return sumRowFlg;
	}

	/**
	 * 集計行フラグを設定する。
	 * 
	 * @param sumRowFlg 集計行フラグ
	 */
	public void setSumRowFlg(String[] sumRowFlg) {
		this.sumRowFlg = sumRowFlg;
	}

	/**
	 * 画面に表示されている更新日時を取得する。
	 * 
	 * @return upDate 画面に表示されている更新日時
	 */
	public String getUpDate() {
		return upDate;
	}

	/**
	 * 画面に表示されている更新日時を設定する。
	 * 
	 * @return upDate 画面に表示されている更新日時
	 */
	public void setUpDate(String upDate) {
		this.upDate = upDate;
	}

	/**
	 * 組織コード(特約店G)を取得する。
	 * 
	 * @return 組織コード(特約店G)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(特約店G)を設定する。
	 * 
	 * @param sosCd3 組織コード(特約店G)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 表示品目カテゴリを取得する。
	 * 
	 * @return 表示品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 表示品目カテゴリを設定する。
	 * 
	 * @param prodCategory 表示品目カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	/**
	 * 施設コード(A調含)を取得する。
	 * 
	 * @return 施設コード(A調含)
	 */
	public String[] getInsNoA() {
		return insNoA;
	}

	/**
	 * 施設コード(A調含)を設定する。
	 * 
	 * @param insNoA 施設コード(A調含)
	 */
	public void setInsNoA(String[] insNoA) {
		this.insNoA = insNoA;
	}

	/**
	 * パラメータを取得する。
	 * 
	 * @return パラメータ
	 */
	public String[] getPara() {
		return para;
	}

	/**
	 * パラメータを設定する。
	 * 
	 * @param para パラメータ
	 */
	public void setPara(String[] para) {
		this.para = para;
	}

	// -------------------------------
	// convert
	// -------------------------------

	/**
	 * 画面から入力されたデータを、更新用特定施設個別計画DTOに変換する。
	 * 
	 * @return 更新用特定施設個別計画DTOのリスト
	 */
	public SpecialInsPlanModifyForVacDto getSpecialInsPlanModifyForVacDto() throws Exception {

		List<SpecialInsPlanForVacDto> inputList = new ArrayList<SpecialInsPlanForVacDto>();
		for (int i = 0; i < this.getPara().length; i++) {
			String[] paraArray = this.getPara()[i].split(",");
			Long plannedValueBL = null;
			if (paraArray.length > 3) {
				plannedValueBL = ConvertUtil.parseLong(paraArray[3]);
			}
			Integer _jgiNo = (ConvertUtil.parseInteger(this.getJgiNo()));
			String _insNo = (paraArray[0]);
			String _prodCode = (paraArray[1]);
			String _tmsTytenCd = (paraArray[2]);
			PlanType _planType = (PlanType.MR);
			Long _plannedValueB = (plannedValueBL);
			String _jgiName = (this.getJgiName());
			SpecialInsPlanForVacDto specialInsPlan = new SpecialInsPlanForVacDto(_jgiNo, _insNo, _prodCode, _tmsTytenCd, _planType, _plannedValueB, null,_jgiName);
			inputList.add(specialInsPlan);
		}
		String upDateTime = this.getUpDate();
		Date upDate = null;
		if (StringUtils.isNotEmpty(upDateTime)) {
			upDate = ConvertUtil.parseDate(upDateTime);
		}
		SpecialInsPlanModifyForVacDto modifyDto = new SpecialInsPlanModifyForVacDto(ConvertUtil.parseInteger(this.getJgiNo()), this.getInsNo(), inputList, upDate, this.getSosCd3());
		return modifyDto;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.seqKey = null;
		this.prodCode = null;
		this.tmsTytenCd = null;
		this.planType = null;
		this.plannedValueB = null;
		this.insNoA = null;
		this.jgiName = null;
		this.sumRowFlg = null;
		this.upDate = null;
		this.sosCd3 = null;
		this.para = null;
	}
}
