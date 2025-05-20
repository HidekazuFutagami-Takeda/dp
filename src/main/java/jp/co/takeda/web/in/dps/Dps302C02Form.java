package jp.co.takeda.web.in.dps;

import static jp.co.takeda.security.DpAuthority.AuthType.EDIT;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.RefDeliveryScope;
import jp.co.takeda.dto.MrPlanResultDto;
import jp.co.takeda.dto.TeamPlanDto;
import jp.co.takeda.dto.TeamPlanUpdateDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dps302C02((医)担当者別計画編集画面(チーム別計画入力))のフォームクラス
 * 
 * @author nozaki
 */
public class Dps302C02Form extends DiaDpActionForm {

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
	public static final BoxKey DPS302C02_DATA_R = new BoxKeyPerClassImpl(Dps302C02Form.class, MrPlanResultDto.class);

	/**
	 * データ有無フラグ(TRUE：データあり、FLASE：データなし)
	 */
	public static final BoxKey DATA_EXIST = new BoxKeyPerClassImpl(Dps302C02Form.class, Boolean.class);

	/**
	 * 登録権限
	 */
	public static final DpAuthority DPS302C02_EDIT_AUTH = new DpAuthority( EDIT);

	// -------------------------------
	// 部品
	// -------------------------------

	/**
	 * 対象区分
	 */
	public static final List<CodeAndValue> INS_TYPES;

	/**
	 * 参照品目の実績参照範囲
	 */
	public static final List<CodeAndValue> REF_DELIVERY_SCOPES;

	/**
	 * static initilizer
	 */
	static {

		List<CodeAndValue> tmp = null;

		// 対象区分
		tmp = new ArrayList<CodeAndValue>(3);
		tmp.add(new CodeAndValue(InsType.UH.getDbValue(), "UH"));
		tmp.add(new CodeAndValue(InsType.P.getDbValue(), "P"));
		tmp.add(new CodeAndValue("9", "合計"));
		INS_TYPES = Collections.unmodifiableList(tmp);

		// 参照品目の実績参照範囲
		tmp = new ArrayList<CodeAndValue>(2);
		tmp.add(new CodeAndValue("0", "参照品目を表示しない"));
		tmp.add(new CodeAndValue("9", "参照品目を表示する"));
		REF_DELIVERY_SCOPES = Collections.unmodifiableList(tmp);
	}

	/**
	 * [プルダウンリスト]対象区分を取得する。
	 * 
	 * @return [プルダウンリスト]対象区分
	 */
	public List<CodeAndValue> getInsTypes() {
		return INS_TYPES;
	}

	/**
	 * [プルダウンリスト]参照品目の実績参照範囲を取得する。
	 * 
	 * @return [プルダウンリスト]参照品目の実績参照範囲
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
	 * 参照品目の参照実績範囲
	 */
	private String refDeliveryScope;

	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * チーム部門フラグ TRUE=チーム, FALSE=他
	 */
	private boolean isTeamRank;

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
	public boolean getIsTeamRank() {
		return isTeamRank;
	}

	/**
	 * チーム部門フラグを設定する。
	 * 
	 * @param isTeamRank TRUE:チーム表示、FALSE：営業所表示
	 */
	public void setIsTeamRank(boolean isTeamRank) {
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
	 * 参照品目の参照実績範囲を取得する。
	 * 
	 * @return 参照品目の参照実績範囲
	 */
	public String getRefDeliveryScope() {
		return refDeliveryScope;
	}

	/**
	 * 参照品目の参照実績範囲を設定する。
	 * 
	 * @param refDeliveryScope 参照品目の参照実績範囲
	 */
	public void setRefDeliveryScope(String refDeliveryScope) {
		this.refDeliveryScope = refDeliveryScope;
	}

	// -------------------------------
	// convert
	// -------------------------------

	/**
	 * 画面から指定された施設出力区分を変換する。
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
	 * 画面から入力されたデータを、更新用チーム別計画DTOに変換する。
	 * 
	 * @return 更新用フリー項目DTOのリスト
	 */
	public TeamPlanUpdateDto convertTeamPlanUpdateDto() throws Exception {

		// 更新用DTOのリスト作成
		List<TeamPlanDto> teamPlanUpdateDtoList = new ArrayList<TeamPlanDto>();
		if (rowIdList != null) {

			for (int i = 0; i < rowIdList.length; i++) {

				if (rowIdList[i] == null) {
					continue;
				}
				Long _seqKey = null;
				String _sosCd = null;
				Long _officeValueY = null;
				Long _plannedValueY = null;
				Date _upDate = null;

				String[] rowId = rowIdList[i].split(",", 5);

				// シーケンスキー
				if (ValidationUtil.isLong(rowId[0])) {
					_seqKey = Long.parseLong(rowId[0]);
				}
				// 組織コード
				if (!StringUtils.isEmpty(rowId[1])) {
					_sosCd = rowId[1];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[2])) {
					_upDate = new Date(Long.parseLong(rowId[2]));
				}
				// 営業所案
				if (ValidationUtil.isLong(rowId[3])) {
					_officeValueY = Long.parseLong(rowId[3]);
				}
				// 決定
				if (ValidationUtil.isLong(rowId[4])) {
					_plannedValueY = Long.parseLong(rowId[4]);
				}

				TeamPlanDto teamPlanDto = new TeamPlanDto(_seqKey, _sosCd, _officeValueY, _plannedValueY, _upDate);

				// リストに追加
				teamPlanUpdateDtoList.add(teamPlanDto);
			}
		}

		// 品目情報作成
		PlannedProd plannedProd = new PlannedProd();
		plannedProd.setProdCode(prodCode);
		plannedProd.setProdName(prodName);

		return new TeamPlanUpdateDto(plannedProd, convertInsType(), teamPlanUpdateDtoList);
	}

	@Override
	public void reset() {
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
