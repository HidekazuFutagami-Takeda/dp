package jp.co.takeda.web.in.dps;

import static jp.co.takeda.security.DpAuthority.AuthType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.RefDeliveryScope;
import jp.co.takeda.dto.MrPlanDto;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.MrPlanUpdateDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps302C03((医)担当者別計画編集画面(担当者別計画))のフォームクラス
 *
 * @author yokokawa
 */
public class Dps302C03Form extends DiaDpActionForm {

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Dps302C02_DATA_R
	 */
	public static final BoxKey DPS302C03_DATA_R = new BoxKeyPerClassImpl(Dps302C03Form.class, MrPlanResultDto.class);

	/**
	 * データ有無フラグ(TRUE：データあり、FLASE：データなし)
	 */
	public static final BoxKey DATA_EXIST = new BoxKeyPerClassImpl(Dps302C03Form.class, Boolean.class);

	/**
	 * 登録権限
	 */
	public static final DpAuthority DPS302C03_EDIT_AUTH = new DpAuthority( EDIT);

	// -------------------------------
	// 部品
	// -------------------------------
	/**
	 * 参照品目の実績参照範囲
	 */
	public static final List<CodeAndValue> REF_DELIVERY_SCOPES;

	/**
	 * static initilizer
	 */
	static {

		List<CodeAndValue> tmp = null;
		// 参照品目の実績参照範囲
		tmp = new ArrayList<CodeAndValue>(2);
		tmp.add(new CodeAndValue("0", "参照品目を表示しない"));
		tmp.add(new CodeAndValue("9", "参照品目を表示する"));
		REF_DELIVERY_SCOPES = Collections.unmodifiableList(tmp);
	}

	/**
	 * [プルダウンリスト]実績参照範囲を取得する。
	 *
	 * @return [プルダウンリスト]実績参照範囲
	 */
	public List<CodeAndValue> getRefDeliveryScopes() {
		return REF_DELIVERY_SCOPES;
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
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 施設出力対象区分
	 */
	private String insType;

	/**
	 * 参照実績範囲
	 */
	private String refDeliveryScope;

	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * チーム部門フラグ TRUE=チーム, FALSE=他
	 */
	private Boolean isTeamRank;

	/**
	 * GRID行の識別IDリスト
	 *
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=選択された品目固定コード
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * グリッドのヘッダ
	 */
	private String header;

	/**
	 * ヘッダ（UH）
	 */
	private String headerUh;

	/**
	 * ヘッダ（P）
	 */
	private String headerP;

	/**
	 * 対象区分
	 */
	private List<CodeAndValue> insTypes;

	/**
	 * 担当者別計画ステータス
	 */
	private StatusForMrPlan statusForMrPlan;

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * @param 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 組織コード(チーム)を設定する。
	 *
	 * @param 組織コード(チーム)
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

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
	 * @param 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 *
	 * @param 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 施設出力対象区分を取得する。
	 *
	 * @return 施設出力対象区分
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 施設出力対象区分を設定する。
	 *
	 * @param 施設出力対象区分
	 */
	public void setInsType(String insType) {
		this.insType = insType;
	}

	/**
	 * チーム部門フラグを取得する。
	 *
	 * @return TRUE:チーム表示、FALSE：営業所表示
	 */
	public Boolean getIsTeamRank() {
		return isTeamRank;
	}

	/**
	 * チーム部門フラグを設定する。
	 *
	 * @param isTeamRank TRUE:チーム表示、FALSE：営業所表示
	 */
	public void setIsTeamRank(Boolean isTeamRank) {
		this.isTeamRank = isTeamRank;
	}

	/**
	 * GRID行の識別IDリストを取得する。
	 *
	 * @return rowIdList GRID行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return (String[]) rowIdList.clone();
	}

	/**
	 * GRID行の識別IDリストを設定する。
	 *
	 * @param rowIdList GRID行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList.clone();
	}

	/**
	 * 参照品目の実績参照範囲を取得する。
	 *
	 * @return 参照品目の実績参照範囲
	 */
	public String getRefDeliveryScope() {
		return refDeliveryScope;
	}

	/**
	 * 参照品目の実績参照範囲を設定する。
	 *
	 * @param refDeliveryScope 参照品目の実績参照範囲
	 */
	public void setRefDeliveryScope(String refDeliveryScope) {
		this.refDeliveryScope = refDeliveryScope;
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
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
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
	 * グリッドのヘッダを取得する。
	 *
	 * @return header  グリッドのヘッダ
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * グリッドのヘッダを設定する。
	 *
	 * @param header  グリッドのヘッダ
	 */
	public void setHeader(String header) {
		this.header = header;
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
	 * 対象区分を取得する。
	 *
	 * @return insTypes 対象区分
	 */
	public List<CodeAndValue> getInsTypes() {
		return insTypes;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param insTypes 対象区分
	 */
	public void setInsTypes(List<CodeAndValue> insTypes) {
		this.insTypes = insTypes;
	}

	/**
	 * 担当者別計画ステータスを取得する。
	 *
	 * @return 担当者別計画ステータス
	 */
	public StatusForMrPlan getStatusForMrPlan() {
		return statusForMrPlan;
	}

	/**
	 * 担当者別計画ステータスを設定する。
	 *
	 * @param statusForMrPlan 担当者別計画ステータス
	 */
	public void setStatusForMrPlan(StatusForMrPlan statusForMrPlan) {
		this.statusForMrPlan = statusForMrPlan;
	}

	// -------------------------------
	// convert
	// -------------------------------

	/**
	 * 画面から指定された施設出力区分を変換<br>
	 *
	 * @return 施設出力区分
	 */
	public InsType convertInsType() {
		if (StringUtils.isEmpty(insType)) {
			insType = InsType.UH.getDbValue();
			return InsType.UH;
		} else if (insType.equals("9")) {
			return null;
		} else {
			return InsType.getInstance(insType);
		}
	}

	/**
	 * 画面から指定された参照品目の実績参照範囲をString型からRefDeliveryScope型に変換する。
	 *
	 * @return 参照品目の実績参照範囲
	 */
	public RefDeliveryScope convertRefDeliveryScope() {
		if (StringUtils.isEmpty(refDeliveryScope)) {
			refDeliveryScope = "0";
			return RefDeliveryScope.NONE;
		} else if (refDeliveryScope.equals("0")) {
			return RefDeliveryScope.NONE;
		} else if (refDeliveryScope.equals("9")) {
			return RefDeliveryScope.ALL;
		}
		return RefDeliveryScope.NONE;
	}

	/**
	 * ActionForm変換処理 <br>
	 * 画面から入力されたデータを、更新用担当者別計画DTOに変換する。
	 *
	 * @return 更新用フリー項目DTOのリスト
	 */
	public MrPlanUpdateDto convertMrPlanUpdateDto() throws Exception {

		// 更新用DTOのリスト作成
		List<MrPlanDto> mrPlanUpdateDtoList = new ArrayList<MrPlanDto>();
		if (rowIdList != null) {

			for (int i = 0; i < rowIdList.length; i++) {

				if (rowIdList[i] == null) {
					continue;
				}
				Long _seqKey = null;
				Long _officeValueY = null;
				Long _plannedValueY = null;
				Date _upDate = null;

				String[] rowId = rowIdList[i].split(",", 4);

				// シーケンスキー
				if (ValidationUtil.isLong(rowId[0])) {
					_seqKey = Long.parseLong(rowId[0]);
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[1])) {
					_upDate = new Date(Long.parseLong(rowId[1]));
				}
				// 営業所案
				if (ValidationUtil.isLong(rowId[2])) {
					_officeValueY = Long.parseLong(rowId[2]);
				}
				// 決定
				if (ValidationUtil.isLong(rowId[3])) {
					_plannedValueY = Long.parseLong(rowId[3]);
				}

				MrPlanDto mrPlanDto = new MrPlanDto(_seqKey, _officeValueY, _plannedValueY, _upDate);

				// リストに追加
				mrPlanUpdateDtoList.add(mrPlanDto);
			}
		}

		// 品目情報作成
		PlannedProd plannedProd = new PlannedProd();
		plannedProd.setProdCode(prodCode);
		plannedProd.setProdName(prodName);

		return new MrPlanUpdateDto(plannedProd, convertInsType(), mrPlanUpdateDtoList);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.refDeliveryScope = "0";
		this.prodName = null;
		this.isTeamRank = true;
		this.rowIdList = null;
	}
}
