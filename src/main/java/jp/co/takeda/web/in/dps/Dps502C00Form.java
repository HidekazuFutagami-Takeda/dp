package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.TmsTytenPlanEditForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditInputDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceResultDto;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;
import jp.co.takeda.dto.WsPlanReferenceScDto;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dps502C00((医)特約店別計画参照画面)のフォームクラス
 *
 * @author yokokawa
 */
public class Dps502C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー(ヘッダ情報)
	 */
	public static final BoxKey DPS502C00_DATA_HEADER = new BoxKeyPerClassImpl(Dps502C00Form.class, TmsTytenPlanReferenceHeaderResultDto.class);
	public static final BoxKey DPS502C03_DATA_R_RESULT_HEADER = new BoxKeyPerClassImpl(Dps502C00Form.class, TmsTytenPlanReferenceHeaderForVacResultDto.class);


	/**
	 * 検索結果取得キー(一覧情報)
	 */
	public static final BoxKey DPS502C00_DATA_R = new BoxKeyPerClassImpl(Dps502C00Form.class, TmsTytenPlanReferenceResultDto.class);
	public static final BoxKey DPS502C03_DATA_R_RESULT_DETAIL = new BoxKeyPerClassImpl(Dps502C00Form.class, TmsTytenPlanReferenceForVacResultDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPS502C03_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	// -------------------------------
	// report
	// -------------------------------
	/**
	 * ファイル名
	 */
	protected String downloadFileName;

	/**
	 * コンテンツレングス
	 */
	protected int contentsLength;

	@Override
	public ContentsType getContentType() {
		return ContentsType.XLS;
	}

	@Override
	public String getDownLoadFileName() {
		return downloadFileName;
	}

	@Override
	public int getContentLength() {
		return Downloadable.DEF_LENGTH;
	}

	// -------------------------------
	// Utility
	// -------------------------------

	/**
	 * 集約方法
	 */
	public static final List<CodeAndValue> tytenKisLevels;


	/**
	 * static initilizer
	 */
	static {

		// 集約方法にセット
		List<CodeAndValue> tytenKisLevelList = new ArrayList<CodeAndValue>(5);
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.HONTEN.getDbValue(), "本店(3桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.SHISHA.getDbValue(), "支社(5桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.SHITEN.getDbValue(), "支店(7桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.KA.getDbValue(), "課(9桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.BLK1.getDbValue(), "ブロック1(11桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.BLK2.getDbValue(), "ブロック2(13桁)"));
		tytenKisLevels = Collections.unmodifiableList(tytenKisLevelList);

	}

	/**
	 * [プルダウンリスト]集約方法を取得する。
	 *
	 * @return [プルダウンリスト]集約方法
	 */
	public List<CodeAndValue> getTytenKisLevels() {
		return tytenKisLevels;
	}


	/**
	 * ダウンロードファイル名を設定する。
	 *
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	// -------------------------------
	// field
	// -------------------------------

	/**
	 * 品目カテゴリの[全て]
	 */
//	public static final String PROD_CATEGORY_ALL = "9";
	public final String CATEGORY_ALL = "99";

	/**
	 * 特約店コード(手入力)
	 */
	private String tmsTytenCdPart;

	/**
	 * 特約店名
	 */
	private String tmsTytenName;

	/**
	 * 集約方法
	 */
	private String tytenKisLevel = TytenKisLevel.BLK2.getDbValue();

	/**
	 * カテゴリ
	 */
	private String category;
//	private CodeAndValue category = new CodeAndValue(PROD_CATEGORY_ALL, "全て");;

	/**
	 * カテゴリリスト
	 */
	private List<CodeAndValue> categoryList;

	/**
	 * 価ベース区分
	 */
	private String kaBaseKb;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分選択値
	 */
	private List<CodeAndValue> KaBaseKbList;

	/**
	 * 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分（保持用）
	 */
	private String kaBaseKbTran;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=シーケンスキー
	 * Sprit文字列[1]=更新日時
	 * Sprit文字列[2]=営業所コード
	 * Sprit文字列[3]=計画値UH
	 * Sprit文字列[4]=計画値P
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * 施設特約店別計画〆フラグ
	 */
	private Boolean wsEndFlg;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 特約店コード(手入力)を取得する
	 *
	 * @return 特約店コード(手入力)
	 */
	public String getTmsTytenCdPart() {
		return tmsTytenCdPart;
	}

	/**
	 * 特約店コード(手入力)を設定する
	 *
	 * @param 特約店コード(手入力)
	 */
	public void setTmsTytenCdPart(String tmsTytenCdPart) {
		this.tmsTytenCdPart = tmsTytenCdPart;
	}

	/**
	 * 特約店名を取得する
	 *
	 * @return 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 特約店名を設定する
	 *
	 * @param tmsTytenName 特約店名
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
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
	 * カテゴリーを取得する
	 *
	 * @return カテゴリー
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * カテゴリーを設定する
	 *
	 * @param category カテゴリー
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * カテゴリリストを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public List<CodeAndValue> getCategoryList() {
		return categoryList;
	}

	/**
	 *カテゴリリストを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setCategoryList(List<CodeAndValue> categoryList) {
		this.categoryList = categoryList;
	}

	/**
	 * 価ベース区分を取得する
	 *
	 * @return 価ベース区分
	 */
	public String getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 価ベース区分を設定する
	 *
	 * @return 価ベース区分
	 */
	public void setKaBaseKb(String kaBaseKb) {
		this.kaBaseKb = kaBaseKb;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分選択値を取得する
	 *
	 * @return 価ベース区分選択値
	 */
	public List<CodeAndValue> getKaBaseKbList() {
		return KaBaseKbList;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分選択値を設定する
	 *
	 * @return 価ベース区分選択値
	 */
	public void setKaBaseKbList(List<CodeAndValue> KaBaseKbList) {
		this.KaBaseKbList = KaBaseKbList;
	}

	/**
	 * 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分（保持用）を取得する
	 *
	 * @return 価ベース区分
	 */
	public String getKaBaseKbTran() {
		return kaBaseKbTran;
	}

	/**
	 * 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分（保持用）を設定する
	 *
	 * @return 価ベース区分
	 */
	public void setKaBaseKbTran(String kaBaseKbTran) {
		this.kaBaseKbTran = kaBaseKbTran;
	}

	/**
	 * 追加・更新行の情報リストを取得する
	 *
	 * @return 選択行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 追加・更新行の情報リストを設定する
	 *
	 * @param selectRowIdList 選択行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
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

	// -------------------------------
	// convert
	// -------------------------------

	/**
	 * Formから特約店別計画参照画面検索条件を表すDTOを生成する。
	 *
	 * @return 特約店別計画参照画面検索条件を表すDTO
	 */
	public WsPlanReferenceScDto convertWsPlanReferenceScDto() {
//		ProdCategory category = null;
		String _category = null;
		if (CATEGORY_ALL.equals(this.category)) {
//			category = null;
			_category = null;
		} else {
//			category = ProdCategory.getInstance(this.category);
			_category = this.category;
		}

		TytenKisLevel tytenKisLevel = TytenKisLevel.getInstance(this.tytenKisLevel);
// mod Start 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = KaBaseKb.getInstance(this.kaBaseKbTran);
// mod End 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		return new WsPlanReferenceScDto(this.tmsTytenCdPart, tytenKisLevel, _category, kaBaseKb);
	}

	/**
	 * Formから特約店別計画参照画面検索条件を表すDTOを生成する。
	 *
	 * @return 特約店別計画参照画面検索条件を表すDTO
	 */
	public WsPlanReferenceForVacScDto convertWsPlanReferenceForVacScDto() {
		String _category = null;
		if (CATEGORY_ALL.equals(this.category)) {
			_category = null;
		} else {
			_category = this.category;
		}

		TytenKisLevel tytenKisLevel = TytenKisLevel.getInstance(this.tytenKisLevel);
// mod Start 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb kaBaseKb = KaBaseKb.getInstance(this.kaBaseKbTran);
// mod End 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする

		return new WsPlanReferenceForVacScDto(this.tmsTytenCdPart, tytenKisLevel, _category, kaBaseKb);
	}

	/**
	 * 特約店別計画編集 入力値DTOに変換する
	 *
	 * @return 特約店別計画編集 入力値DTO
	 */
	public TmsTytenPlanEditInputDto convertTmsTytenPlanEditInputDto() {
		List<WsPlan> inputList = new ArrayList<WsPlan>();
// mod Start 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		KaBaseKb baseKb = KaBaseKb.getInstance(this.kaBaseKbTran);
// mod End 2022/7/22  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		if (this.rowIdList == null) {
			return new TmsTytenPlanEditInputDto(inputList, baseKb);
		}

		for (String rowData : this.rowIdList) {
			// 受信データ取得
			String[] data = ConvertUtil.splitConmma(rowData);
			Long seqKey = ConvertUtil.parseLong(data[0]);
			Date upDate = ConvertUtil.parseDate(data[1]);
			String sosCd = data[2];
			Long plannedValueUh = ConvertUtil.parseLong(data[3]);
			Long plannedValueP = ConvertUtil.parseLong(data[4]);

			// 特約店別計画入力値を設定
			WsPlan wsPlan = new WsPlan();
			wsPlan.setSeqKey(seqKey);
			wsPlan.setUpDate(upDate);
			wsPlan.setSosCd(sosCd);

			// 千円単位を1円単位に変換して設定
			wsPlan.setPlannedValueUh(ConvertUtil.parseMoneyToNormalUnit(plannedValueUh));
			wsPlan.setPlannedValueP(ConvertUtil.parseMoneyToNormalUnit(plannedValueP));

			wsPlan.setTmsTytenCd(this.tmsTytenCdPart);
			wsPlan.setProdCode(this.category);
			wsPlan.setKaBaseKb(KaBaseKb.getInstance(this.kaBaseKb));

			inputList.add(wsPlan);
		}

		return new TmsTytenPlanEditInputDto(inputList, baseKb);
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

			wsPlanForVac.setTmsTytenCd(this.tmsTytenCdPart);
			wsPlanForVac.setProdCode(this.category);
// add Start 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			wsPlanForVac.setKaBaseKb(baseKb);
// add End 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
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
		this.tmsTytenCdPart = null;
		this.tmsTytenName = null;
		this.tytenKisLevel = TytenKisLevel.BLK2.getDbValue();
		this.category = CATEGORY_ALL;

	}

}
