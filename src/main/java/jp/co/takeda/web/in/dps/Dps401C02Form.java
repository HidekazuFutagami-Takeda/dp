package jp.co.takeda.web.in.dps;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.InsWsPlanForVacUpDateModifyAllDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanForVacUpDateScDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyIppanRowDto;
import jp.co.takeda.dto.InsWsPlanUpDateModifyRowDto;
import jp.co.takeda.dto.InsWsPlanUpDateMonNnuScDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultDto;
import jp.co.takeda.dto.InsWsPlanUpDateResultListDto;
import jp.co.takeda.dto.InsWsPlanUpDateScDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps401C02((医)施設特約店別計画編集画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dps401C02Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS401C02_DATA_R
	 */
	public static final BoxKey DPS401C02_DATA_R = new BoxKeyPerClassImpl(Dps401C02Form.class, InsWsPlanUpDateResultListDto.class);

	/**
	 * DPS401C02F05_DATA_R
	 */
	public static final BoxKey DPS401C02F05_DATA_R = new BoxKeyPerClassImpl(Dps401C02Form.class, InsWsPlanUpDateResultDto.class);

	/**
	 * DPS401C05_DATA_R
	 */
	public static final BoxKey DPS401C05_DATA_R = new BoxKeyPerClassImpl(Dps401C02Form.class, InsWsPlanForVacUpDateResultListDto.class);

	/**
	 * DPS401C05F05_DATA_R
	 */
	public static final BoxKey DPS401C05F05_DATA_R = new BoxKeyPerClassImpl(Dps401C02Form.class, InsWsPlanForVacUpDateResultDto.class);


	/**
	 * データ有無フラグ(TRUE：データあり、FLASE：データなし)
	 */
	public static final BoxKey DATA_EXIST = new BoxKeyPerClassImpl(Dps401C02Form.class, Boolean.class);


	/**
	 * 編集権限MMP（医薬）
	 */
	public static final DpAuthority DPS401C02_EDIT_AUTH_MMP = new DpAuthority( AuthType.EDIT);

	/**
	 * 編集権限仕入
	 */
	public static final DpAuthority DPS401C02_EDIT_AUTH_SHIIRE = new DpAuthority( AuthType.EDIT);

	/**
	 * 編集権限MMP（ワクチン）
	 */
	public static final DpAuthority DPS401C05_EDIT_AUTH = new DpAuthority( AuthType.EDIT);


	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public InsWsPlanUpDateScDto convertInsWsPlanUpDateScDto() throws Exception {

		// 従業員コード
		if (StringUtils.isEmpty(jgiNo)) {
			jgiNo = null;
		}
		// 品目固定コード
		if (StringUtils.isEmpty(prodCode)) {
			prodCode = null;
		}
		// 対象施設
		if (StringUtils.isEmpty(insType)) {
			insType = "1";
		}
		// 参照品目固定コード
		if (StringUtils.isEmpty(refProdCode)) {
			refProdCode = null;
		}
		boolean refProdAllFlg = false;
		//参照品目全表示フラグ
		if (!(StringUtils.isEmpty(refProdCode)) && refProdCode.equals("0")) {
			refProdAllFlg = true;
		}
		// カテゴリ
		if (StringUtils.isEmpty(category)) {
			category = null;
		}
		return new InsWsPlanUpDateScDto(jgiNo, prodCode, insType, refProdCode, refProdAllFlg, category);
	}

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public InsWsPlanForVacUpDateScDto convertInsWsPlanForVacUpDateScDto() throws Exception {

		// 従業員コード
		if (StringUtils.isEmpty(jgiNo)) {
			jgiNo = null;
		}
		// 品目固定コード
		if (StringUtils.isEmpty(prodCode)) {
			prodCode = null;
		}
		// 対象施設
		if (StringUtils.isEmpty(insType)) {
			insType = "1";
		}
		// 参照品目固定コード
		if (StringUtils.isEmpty(refProdCode)) {
			refProdCode = null;
		}
		boolean refProdAllFlg = false;
		//参照品目全表示フラグ
		if (!(StringUtils.isEmpty(refProdCode)) && refProdCode.equals("0")) {
			refProdAllFlg = true;
		}
		// カテゴリ
		if (StringUtils.isEmpty(category)) {
			category = null;
		}
		return new InsWsPlanForVacUpDateScDto(jgiNo, prodCode, insType, refProdCode, refProdAllFlg, category);
	}


	/**
	 * ActionForm ⇒ upDateDto 変換処理
	 *
	 * @return PlannedProdSpecialInsPlanUpDateDto 変換されたUpDateDto
	 *
	 * * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param modifyRow 更新行、追加行DTOのリスト
	 */
	public InsWsPlanUpDateModifyDto convertInsWsPlanUpDateModifyDto() throws Exception {

		List<InsWsPlanUpDateModifyRowDto> modifyRowDto = new ArrayList<InsWsPlanUpDateModifyRowDto>();

		if (selectRowIdList != null && selectRowIdList.length > 0) {
			for (int i = 0; i < selectRowIdList.length; i++) {
				String _seqKey = null;
				Date _upDate = null;
				String _insNo = null;
				String _tmsTytenCd = null;
				Long _plannedValueY = null;
				boolean _specialInsPlanFlg = false;
				boolean _exceptDistInsFlg = false;
				boolean _delInsFlg = false;

				String[] selectRowId = ConvertUtil.splitConmma(selectRowIdList[i]);
				//シーケンスキー
				if (StringUtils.isNotEmpty(selectRowId[0])) {
					_seqKey = selectRowId[0];
				}
				//計画最新更新日時
				if (StringUtils.isNotEmpty(selectRowId[1])) {
					String upDateTime = selectRowId[1];
					_upDate = ConvertUtil.parseDate(upDateTime);
				}
				//施設コード
				if (StringUtils.isNotEmpty(selectRowId[2])) {
					_insNo = selectRowId[2];
				}
				//特約店コード
				if (StringUtils.isNotEmpty(selectRowId[3])) {
					_tmsTytenCd = selectRowId[3];
				}
				//特定施設フラグ
				if (StringUtils.isNotEmpty(selectRowId[4])) {
					_specialInsPlanFlg = ConvertUtil.parseBoolean(selectRowId[4]);
				}
				//配分除外フラグ
				if (StringUtils.isNotEmpty(selectRowId[5])) {
					_exceptDistInsFlg = ConvertUtil.parseBoolean(selectRowId[5]);
				}
				//削除施設フラグ
				if (StringUtils.isNotEmpty(selectRowId[6])) {
					_delInsFlg = ConvertUtil.parseBoolean(selectRowId[6]);
				}
				//修正計画
				if (StringUtils.isNotEmpty(selectRowId[7])) {
					_plannedValueY = ConvertUtil.parseLong(selectRowId[7]);
				}
				modifyRowDto.add(new InsWsPlanUpDateModifyRowDto(_seqKey, _upDate, _insNo, _tmsTytenCd, _plannedValueY, _specialInsPlanFlg, _exceptDistInsFlg, _delInsFlg));
			}
		}
		Integer _jgiNo = null;
		String _prodCode = null;
		//従業員コード
		if (StringUtils.isNotEmpty(jgiNo)) {
			_jgiNo = ConvertUtil.parseInteger(jgiNo);
		}
		//品目固定コード
		if (StringUtils.isNotEmpty(prodCode)) {
			_prodCode = prodCode;
		}
		InsWsPlanUpDateModifyDto upDateDto = new InsWsPlanUpDateModifyDto(_jgiNo, _prodCode, modifyRowDto);
		return upDateDto;
	}

	/**
	 * ActionForm ⇒ Dto 変換処理(ワクチン)
	 *
	 * @return InsWsPlanForVacUpDateModifyAllDto 変換されたDto
	 */
	public InsWsPlanForVacUpDateModifyAllDto convertInsWsPlanForVacUpDateModifyAllDto() throws Exception {

		Integer _jgiNo = null;
		String _prodCode = null;

		//従業員コード
		if (StringUtils.isNotEmpty(this.jgiNo)) {
			_jgiNo = ConvertUtil.parseInteger(this.jgiNo);
		}
		//品目固定コード
		if (StringUtils.isNotEmpty(this.prodCode)) {
			_prodCode = this.prodCode;
		}

		//更新行、追加行DTOのリスト
		List<InsWsPlanUpDateModifyRowDto> modifyRowDto = new ArrayList<InsWsPlanUpDateModifyRowDto>();

		if (selectRowIdList != null && selectRowIdList.length > 0) {
			for (int i = 0; i < selectRowIdList.length; i++) {
				String _seqKey = null;
				Date _upDate = null;
				String _insNo = null;
				String _tmsTytenCd = null;
				Long _plannedValueY = null;
				boolean _specialInsPlanFlg = false;
				boolean _exceptDistInsFlg = false;
				boolean _delInsFlg = false;

				String[] selectRowId = ConvertUtil.splitConmma(selectRowIdList[i]);
				//シーケンスキー
				if (StringUtils.isNotEmpty(selectRowId[0])) {
					_seqKey = selectRowId[0];
				}
				//計画最新更新日時
				if (StringUtils.isNotEmpty(selectRowId[1])) {
					String upDateTime = selectRowId[1];
					_upDate = ConvertUtil.parseDate(upDateTime);
				}
				//施設コード
				if (StringUtils.isNotEmpty(selectRowId[2])) {
					_insNo = selectRowId[2];
				}
				//特約店コード
				if (StringUtils.isNotEmpty(selectRowId[3])) {
					_tmsTytenCd = selectRowId[3];
				}
				//特定施設フラグ
				if (StringUtils.isNotEmpty(selectRowId[4])) {
					_specialInsPlanFlg = ConvertUtil.parseBoolean(selectRowId[4]);
				}
				//配分除外フラグ
				if (StringUtils.isNotEmpty(selectRowId[5])) {
					_exceptDistInsFlg = ConvertUtil.parseBoolean(selectRowId[5]);
				}
				//削除施設フラグ
				if (StringUtils.isNotEmpty(selectRowId[6])) {
					_delInsFlg = ConvertUtil.parseBoolean(selectRowId[6]);
				}
				//修正計画
				if (StringUtils.isNotEmpty(selectRowId[7])) {
					_plannedValueY = ConvertUtil.parseLong(selectRowId[7]);
				}
				modifyRowDto.add(new InsWsPlanUpDateModifyRowDto(_seqKey, _upDate, _insNo, _tmsTytenCd, _plannedValueY, _specialInsPlanFlg, _exceptDistInsFlg, _delInsFlg));
			}
		}

		List<InsWsPlanUpDateModifyIppanRowDto> modifyIppanRowDto = new ArrayList<InsWsPlanUpDateModifyIppanRowDto>();

		if (selectAddrIdList != null && selectAddrIdList.length > 0) {
			for (int i = 0; i < selectAddrIdList.length; i++) {
				Prefecture _addrCodePref = null;
				String _addrCodeCity = null;
				Long _ippanValue = null;
				Date _upDate = null;
				String[] selectRowId = ConvertUtil.splitConmma(selectAddrIdList[i]);
				//JIS府県コード
				if (StringUtils.isNotEmpty(selectRowId[0])) {
					_addrCodePref = Prefecture.getInstance(selectRowId[0]);
				}
				//JIS市区町村コード
				if (StringUtils.isNotEmpty(selectRowId[1])) {
					_addrCodeCity = selectRowId[1];
				}
				//一般先計の更新日時
				if (StringUtils.isNotEmpty(selectRowId[2])) {
					String upDateTime = selectRowId[2];
					_upDate = ConvertUtil.parseDate(upDateTime);
				}
				//一般先計の計画値
				if (StringUtils.isNotEmpty(selectRowId[3])) {
					_ippanValue = ConvertUtil.parseLong(selectRowId[3]);
				}
				modifyIppanRowDto.add(new InsWsPlanUpDateModifyIppanRowDto(_addrCodePref, _addrCodeCity, _ippanValue, _upDate));
			}
		}
		InsWsPlanForVacUpDateModifyAllDto upDateDto = new InsWsPlanForVacUpDateModifyAllDto(_jgiNo, _prodCode, modifyRowDto, modifyIppanRowDto);
		// 送信データの初期化
		selectRowIdList = null;
		selectAddrIdList = null;
		return upDateDto;
	}

	/**
	 * ActionForm ⇒ ScDto 変換処理（過去実績検索用）
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public InsWsPlanUpDateMonNnuScDto convertInsWsPlanUpDateMonNnuScDto() throws Exception {
		String _insNo = null;
		String _tmsTytenCd = null;
		String _prodCode = null;
		String _refProdCode1 = null;
		String _refProdCode2 = null;
		String _refProdCode3 = null;
		String _jgiNo = null;
		String _insType = null;

		//施設コード
		if (StringUtils.isNotEmpty(insNo)) {
			_insNo = insNo;
		}
		//特約店コード
		if (StringUtils.isNotEmpty(tmsTytenCd)) {
			_tmsTytenCd = tmsTytenCd;
		}
		//品目固定コード
		if (StringUtils.isNotEmpty(prodCode)) {
			_prodCode = prodCode;
		}
		//参照品目固定コード1
		if (StringUtils.isNotEmpty(refProdCode1)) {
			_refProdCode1 = refProdCode1;
		}
		//参照品目固定コード2
		if (StringUtils.isNotEmpty(refProdCode2)) {
			_refProdCode2 = refProdCode2;
		}
		//参照品目固定コード3
		if (StringUtils.isNotEmpty(refProdCode3)) {
			_refProdCode3 = refProdCode3;
		}
		// 従業員番号
		if (StringUtils.isNotEmpty(jgiNo)) {
			_jgiNo = jgiNo;
		}
		// 対象区分
		if (StringUtils.isNotEmpty(insType)) {
			_insType = insType;
		}
		return new InsWsPlanUpDateMonNnuScDto(_insNo, _tmsTytenCd, _prodCode, _refProdCode1, _refProdCode2, _refProdCode3, _jgiNo, _insType);
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 従業員コード
	 */
	private String jgiNo;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 対象区分
	 */
	private String insType;

	/**
	 * 参照品目固定コード
	 */
	private String refProdCode;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * TMS特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 参照品目1
	 */
	private String refProdCode1;

	/**
	 * 参照品目2
	 */
	private String refProdCode2;

	/**
	 * 参照品目3
	 */
	private String refProdCode3;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * ヘッダ（UH）
	 */
	private String headerUh;

	/**
	 * ヘッダ（P）
	 */
	private String headerP;

	/**
	 * 施設特約店別計画〆フラグ
	 */
	private Boolean wsEndFlg;

//	/**
//	 * 送信データ(行)
//	 */
//	private String[] selectRowIdList;

	/**
	 * 送信データ
	 */
	private String[] selectAddrIdList;

	/**
	 * 選択行の識別IDリスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=従業員コード
	 * Sprit文字列[1]=ステータス最終更新日時(Date型のgetTime()の値)
	 * (例)---------------------------------------
	 * selectRowIdList[0] = "12345,1234954688000"
	 * selectRowIdList[1] = "22432,1122843578000"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] selectRowIdList;

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 組織コード(チーム)を設定する。
	 *
	 * @param sosCd4 組織コード(チーム)
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

	/**
	 * 従業員コードを取得する。
	 *
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員コードを設定する。
	 *
	 * @param jgiNo
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return prodCode 品目固定コード
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
	 * 対象区分を取得する。
	 *
	 * @return insType 対象区分
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param insType 対象区分
	 */
	public void setInsType(String insType) {
		this.insType = insType;
	}

	/**
	 * 参照品目固定コードを取得する。
	 *
	 * @return refProdCode 参照品目固定コード
	 */
	public String getRefProdCode() {
		return refProdCode;
	}

	/**
	 * 参照品目固定コードを設定する。
	 *
	 * @param refProdCode 参照品目固定コード
	 */
	public void setRefProdCode(String refProdCode) {
		this.refProdCode = refProdCode;
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
	 * 施設コードを設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店コードを設定する。
	 *
	 * @param tmsTytenCd 特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 参照品目1を取得する。
	 *
	 * @return refProdCode1 参照品目1
	 */
	public String getRefProdCode1() {
		return refProdCode1;
	}

	/**
	 * 参照品目1を設定する。
	 *
	 * @param refProdCode1 参照品目1
	 */
	public void setRefProdCode1(String refProdCode1) {
		this.refProdCode1 = refProdCode1;
	}

	/**
	 * 参照品目2を取得する。
	 *
	 * @return refProdCode2 参照品目2
	 */
	public String getRefProdCode2() {
		return refProdCode2;
	}

	/**
	 * 参照品目2を設定する。
	 *
	 * @param refProdCode2 参照品目2
	 */
	public void setRefProdCode2(String refProdCode2) {
		this.refProdCode2 = refProdCode2;
	}

	/**
	 * 参照品目3を取得する。
	 *
	 * @return refProdCode3 参照品目3
	 */
	public String getRefProdCode3() {
		return refProdCode3;
	}

	/**
	 * 参照品目3を設定する。
	 *
	 * @param refProdCode3 参照品目3
	 */
	public void setRefProdCode3(String refProdCode3) {
		this.refProdCode3 = refProdCode3;
	}

	/**
	 * 送信データを取得する。
	 *
	 * @return selectAddrIdList 送信データ
	 */
	public String[] getSelectAddrIdList() {
		return selectAddrIdList;
	}

	/**
	 * 送信データを設定する。
	 *
	 * @param selectAddrIdList 送信データ
	 */
	public void setSelectAddrIdList(String[] selectAddrIdList) {
		this.selectAddrIdList = selectAddrIdList;
	}

	/**
	 * 選択行の識別IDリストを取得する。
	 *
	 * @return selectRowIdList 選択行の識別IDリスト
	 */
	public String[] getSelectRowIdList() {
		return selectRowIdList;
	}

	/**
	 * 選択行の識別IDリストを設定する。
	 *
	 * @param selectRowIdList 選択行の識別IDリスト
	 */
	public void setSelectRowIdList(String[] selectRowIdList) {
		this.selectRowIdList = selectRowIdList;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * ヘッダ(UH)を取得する。
	 *
	 * @return headerUh  ヘッダ(UH)
	 */
	public String getHeaderUh() {
		return headerUh;
	}

	/**
	 * ヘッダ(UH)を設定する。
	 *
	 * @param headerUh  ヘッダ(UH)
	 */
	public void setHeaderUh(String headerUh) {
		this.headerUh = headerUh;
	}

	/**
	 * ヘッダ(P)を取得する。
	 *
	 * @return headerP  ヘッダ(P)
	 */
	public String getHeaderP() {
		return headerP;
	}

	/**
	 * ヘッダ(P)を設定する。
	 *
	 * @param headerP  ヘッダ(P)
	 */
	public void setHeaderP(String headerP) {
		this.headerP = headerP;
	}

	/**
	 * 施設特約店別計画〆フラグを取得する。
	 *
	 * @return 施設特約店別計画〆フラグ
	 */
	public Boolean getWsEndFlg() {
		return wsEndFlg;
	}

	/**
	 * 施設特約店別計画〆フラグを設定する。
	 *
	 * @param wsEndFlg 施設特約店別計画〆フラグ
	 */
	public void setWsEndFlg(Boolean wsEndFlg) {
		this.wsEndFlg = wsEndFlg;
	}


	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.insType = InsType.UH.getDbValue();
		this.refProdCode = null;
		this.insNo = null;
		this.tmsTytenCd = null;
		this.refProdCode1 = null;
		this.refProdCode2 = null;
		this.refProdCode3 = null;
		this.selectRowIdList = null;
	}
}
