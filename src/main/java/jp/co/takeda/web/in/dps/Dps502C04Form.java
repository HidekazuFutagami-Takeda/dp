package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenPlanEditForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditForVacScDto;
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps502C04((ワ)特約店別計画編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps502C04Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS502C04_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps502C04Form.class, TmsTytenPlanEditForVacResultDto.class);

	/**
	 * 検索結果有無取得キー(TRUE:1件以上,FALSE:0件)
	 */
	public static final BoxKey DPS502C04_DATA_R_SEARCH_RESULT_EXIST = new BoxKeyPerClassImpl(Dps502C04Form.class, Boolean.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPS502C04_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 品目コード
	 */
	private String prodCode;

	/**
	 * 集約方法
	 */
	private String tytenKisLevel;

	/**
	 * 親ウィンドウの検索関数名
	 */
	private String searchFuncName;

	/**
	 * 更新行の識別IDリスト
	 *
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=(ワ)特約店別計画 シーケンスキー
	 * Sprit文字列[1]=(ワ)特約店別計画 最終更新日時(Date型のgetTime()の値)
	 * Sprit文字列[2]=(ワ)特約店別計画 組織コード
	 * Sprit文字列[3]=(ワ)特約店別計画 検索時の計画値(T)
	 * Sprit文字列[4]=(ワ)特約店別計画 編集後の計画値(T)
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * 施設特約店別計画〆フラグ
	 */
	private boolean wsEndFlg;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分
	 */
	private String kaBaseKb;

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
	 * 品目コードを取得する。
	 *
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目コードを設定する。
	 *
	 * @param prodCode 品目コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 集約方法を取得する
	 *
	 * @return 集約方法
	 */
	public String getTytenKisLevel() {
		return tytenKisLevel;
	}

	/**
	 * 集約方法を設定する
	 *
	 * @param tytenKisLevel 集約方法
	 */
	public void setTytenKisLevel(String tytenKisLevel) {
		this.tytenKisLevel = tytenKisLevel;
	}

	/**
	 * 親ウィンドウの検索関数名を取得する。
	 *
	 * @return searchFuncName 親ウィンドウの検索関数名
	 */
	public String getSearchFuncName() {
		return searchFuncName;
	}

	/**
	 * 親ウィンドウの検索関数名を設定する。
	 *
	 * @param searchFuncName 親ウィンドウの検索関数名
	 */
	public void setSearchFuncName(String searchFuncName) {
		this.searchFuncName = searchFuncName;
	}

	/**
	 * 更新行の識別IDリストを取得する。
	 *
	 * @return rowIdList 更新行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return (String[]) rowIdList.clone();
	}

	/**
	 * 更新行の識別IDリストを設定する。
	 *
	 * @param rowIdList 更新行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList.clone();
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

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分を取得する
	 *
	 * @return 価ベース区分
	 */
	public String getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分を設定する
	 *
	 * @param kaBaseKb 価ベース区分
	 */
	public void setKaBaseKb(String kaBaseKb) {
		this.kaBaseKb = kaBaseKb;
	}


	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * ワクチン用特約店別計画編集 検索条件DTOに変換する
	 *
	 * @return ワクチン用特約店別計画編集 検索条件DTO
	 */
	public TmsTytenPlanEditForVacScDto convertTmsTytenPlanEditForVacScDto() {
		return new TmsTytenPlanEditForVacScDto(this.tmsTytenCd, this.prodCode, this.tytenKisLevel, KaBaseKb.getInstance(kaBaseKb));
	}

	/**
	 * ワクチン用特約店別計画編集 入力値DTOに変換する
	 *
	 * @return ワクチン用特約店別計画編集 入力値DTO
	 */
	public TmsTytenPlanEditForVacInputDto convertTmsTytenPlanEditForVacInputDto() {
		List<WsPlanForVac> inputList = new ArrayList<WsPlanForVac>();
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb baseKb = KaBaseKb.getInstance(this.kaBaseKb);
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		if (this.rowIdList == null) {
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			return new TmsTytenPlanEditForVacInputDto(inputList, baseKb);
		}
		for (String rowData : this.rowIdList) {
			// 受信データ取得
			String[] data = ConvertUtil.splitConmma(rowData);
			Long seqKey = ConvertUtil.parseLong(data[0]);
			Date upDate = ConvertUtil.parseDate(data[1]);
			String sosCd = data[2];
			Long plannedValue = ConvertUtil.parseLong(data[4]);

			// 特約店別計画入力値を設定
			WsPlanForVac wsPlanForVac = new WsPlanForVac();
			wsPlanForVac.setSeqKey(seqKey);
			wsPlanForVac.setUpDate(upDate);
			wsPlanForVac.setSosCd(sosCd);

			// 千円単位を1円単位に変換して設定
			wsPlanForVac.setPlannedValue(ConvertUtil.parseMoneyToNormalUnit(plannedValue));

			wsPlanForVac.setTmsTytenCd(this.tmsTytenCd);
			wsPlanForVac.setProdCode(this.prodCode);

			inputList.add(wsPlanForVac);
		}
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		return new TmsTytenPlanEditForVacInputDto(inputList, baseKb);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.rowIdList = null;
	}
}
