package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosPlanUpdateDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm101C00((医)品目別計画編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dpm101C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM101C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm101C00Form.class, ProdPlanResultDto.class);

	/**
	 * 部門ランクフラグ取得キー
	 */
	public static final BoxKey DPM101C00_DATA_R_BUMON_FLAG = new BoxKeyPerClassImpl(Dpm101C00Form.class, Boolean.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM101C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・支店
	 */
	public static final ProdPlanLevel DPM101C00_PLANLEVEL_SITEN = ProdPlanLevel.SITEN;

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPM101C00_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 計画立案レベル・チーム
	 */
	public static final ProdPlanLevel DPM101C00_PLANLEVEL_TEAM = ProdPlanLevel.TEAM;

	/**
	 * 計画立案レベル・担当者
	 */
	public static final ProdPlanLevel DPM101C00_PLANLEVEL_MR = ProdPlanLevel.MR;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 雑フラグ
	 */
	private boolean etcSosFlg;

	/**
	 * ONC組織フラグ
	 */
	private boolean oncSosFlg;

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリ
	 */
	private String sosCategory;

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * ワクチンカテゴリコード
	 */
	private String vaccineCode;

	// TOP画面用

	/**
	 * [トップ用]組織コード
	 */
	private String topSosCd;

	/**
	 * [トップ用]従業員番号
	 */
	private String topJgiNo;

	/**
	 * [トップ用]部門ランク
	 */
	private String topBumonRank;

	/**
	 * 検索するかのフラグ
	 */
	private boolean searchFlg;

	// 処理用フィールド
	/**
	 * 処理用組織コード(支店)
	 */
	private String sosCd2Tran;

	/**
	 * 処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 処理用組織コード(チーム)
	 */
	private String sosCd4Tran;

	/**
	 * 処理用従業員番号
	 */
	private String jgiNoTran;

	/**
	 * 雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 処理用ONC組織フラグ
	 */
	private boolean oncSosFlgTran;

	/**
	 * カテゴリ
	 */
	private String sosCategoryTran;


	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0] =組織コード(支店)</li>
	 * <li>Sprit文字列[1] =組織コード(営業所)</li>
	 * <li>Sprit文字列[2] =組織コード(チーム)</li>
	 * <li>Sprit文字列[3] =雑組織フラグ</li>
	 * <li>Sprit文字列[4] =従業員番号</li>
	 * <li>Sprit文字列[5] =品目コード</li>
	 * <li>Sprit文字列[6] =[UH]シーケンスキー</li>
	 * <li>Sprit文字列[7] =[UH]最終更新日時</li>
	 * <li>Sprit文字列[8] =[UH]Y価ベース 更新前</li>
	 * <li>Sprit文字列[9] =[P ]シーケンスキー</li>
	 * <li>Sprit文字列[10]=[P ]最終更新日時</li>
	 * <li>Sprit文字列[11]=[P ]Y価ベース 更新前</li>
	 * <li>Sprit文字列[12]=[Z ]シーケンスキー</li>
	 * <li>Sprit文字列[13]=[Z ]最終更新日時</li>
	 * <li>Sprit文字列[14]=[Z ]Y価ベース 更新前</li>
	 * <li>Sprit文字列[15]=[UH]Y価ベース 更新後</li>
	 * <li>Sprit文字列[16]=[P ]Y価ベース 更新後</li>
	 * <li>Sprit文字列[17]=[Z ]Y価ベース 更新後</li>
	 * </ul>
	 */
	private String[] rowIdList;

	/**
	 * UH用タイトル
	 */
	private String titleUH;

	/**
	 * P用タイトル
	 */
	private String titleP;

	/**
	 * Z用タイトル
	 */
	private String titleZ;

	/**
	 * 計画（月別計画）
	 */
	private String planId;

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


	/**
	 * ダウンロードファイル名を設定する。
	 *
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}



	//---------------------
	// Getter & Setter
	// --------------------
	/**
	 * 組織コード(支店)を取得する。
	 *
	 * @return sosCd2 組織コード(支店)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(支店)を設定する。
	 *
	 * @param sosCd2 組織コード(支店)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

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
	 * 雑組織フラグを取得する。
	 *
	 * @return 雑組織フラグ
	 */
	public boolean isEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * 雑組織フラグを設定する。
	 *
	 * @param etcSosFlg 雑組織フラグ
	 */
	public void setEtcSosFlg(boolean etcSosFlg) {
		this.etcSosFlg = etcSosFlg;
	}

	/**
	 * ONC組織フラグを取得する。
	 *
	 * @return ONC組織フラグ
	 */
	public boolean isOncSosFlg() {
		return oncSosFlg;
	}

	/**
	 * ONC組織フラグを設定する。
	 *
	 * @param oncSosFlg ONC組織フラグ
	 */
	public void setOncSosFlg(boolean oncSosFlg) {
		this.oncSosFlg = oncSosFlg;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getSosCategory() {
		return sosCategory;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param sosCategory カテゴリ
	 */
	public void setSosCategory(String sosCategory) {
		this.sosCategory = sosCategory;
	}

// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

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
	 * 品目カテゴリを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 品目カテゴリを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	/**
	 * 品目カテゴリリストを取得する。
	 *
	 * @return prodCategoryList 品目カテゴリリスト
	 */
	public List<CodeAndValue> getProdCategoryList() {
		return prodCategoryList;
	}

	/**
	 *  品目カテゴリリストを取得する。
	 *
	 * @param prodCategoryList 品目カテゴリリスト
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	/**
	 * 計画（期別計画）を取得する。
	 *
	 * @return planId
	 */
	public String getPlanId() {
		return planId;
	}

	/**
	 * 計画（期別計画）を設定する。
	 *
	 * @param planId
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}

	/**
	 * ワクチンのカテゴリコードを取得する。
	 *
	 * @return vaccineCode ワクチンのカテゴリコード
	 */
	public String getVaccineCode() {
		return vaccineCode;
	}

	/**
	 * ワクチンのカテゴリコードを設定する。
	 *
	 * @param vaccineCode ワクチンのカテゴリコード
	 */
	public void setVaccineCode(String vaccineCode) {
		this.vaccineCode = vaccineCode;
	}

	/**
	 * 追加・更新行の情報リストを取得する。
	 *
	 * @return rowIdList 追加・更新行の情報リスト
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 追加・更新行の情報リストを設定する。
	 *
	 * @param rowIdList 追加・更新行の情報リスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
	}

	/**
	 * 処理用組織コード(支店)を取得する。
	 *
	 * @return 処理用組織コード(支店)
	 */
	public String getSosCd2Tran() {
		return sosCd2Tran;
	}

	/**
	 * 処理用組織コード(支店)を設定する。
	 *
	 * @param sosCd2Tran 処理用組織コード(支店)
	 */
	public void setSosCd2Tran(String sosCd2Tran) {
		this.sosCd2Tran = sosCd2Tran;
	}

	/**
	 * 処理用組織コード(営業所)を取得する。
	 *
	 * @return 処理用組織コード(営業所)
	 */
	public String getSosCd3Tran() {
		return sosCd3Tran;
	}

	/**
	 * 処理用組織コード(営業所)を設定する。
	 *
	 * @param sosCd3Tran 処理用組織コード(営業所)
	 */
	public void setSosCd3Tran(String sosCd3Tran) {
		this.sosCd3Tran = sosCd3Tran;
	}

	/**
	 * 処理用組織コード(チーム)を取得する。
	 *
	 * @return 処理用組織コード(チーム)
	 */
	public String getSosCd4Tran() {
		return sosCd4Tran;
	}

	/**
	 * 処理用組織コード(チーム)を設定する。
	 *
	 * @param sosCd4Tran 処理用組織コード(チーム)
	 */
	public void setSosCd4Tran(String sosCd4Tran) {
		this.sosCd4Tran = sosCd4Tran;
	}

	/**
	 * 処理用従業員番号を取得する。
	 *
	 * @return 処理用組織コード(チーム)
	 */
	public String getJgiNoTran() {
		return jgiNoTran;
	}

	/**
	 * 処理用従業員番号を設定する。
	 *
	 * @param jgiNoTran 処理用従業員番号
	 */
	public void setJgiNoTran(String jgiNoTran) {
		this.jgiNoTran = jgiNoTran;
	}

	/**
	 * 処理用品目カテゴリを取得する。
	 *
	 * @return 処理用品目カテゴリ
	 */
	public String getProdCategoryTran() {
		return prodCategoryTran;
	}

	/**
	 * 処理用品目カテゴリを設定する。
	 *
	 * @param prodCategoryTran 処理用品目カテゴリ
	 */
	public void setProdCategoryTran(String prodCategoryTran) {
		this.prodCategoryTran = prodCategoryTran;
	}

	/**
	 * 処理用雑組織フラグを取得する。
	 *
	 * @return 処理用雑組織フラグ
	 */
	public boolean isEtcSosFlgTran() {
		return etcSosFlgTran;
	}

	/**
	 * 処理用雑組織フラグを設定する。
	 *
	 * @param etcSosFlgTran 処理用雑組織フラグ
	 */
	public void setEtcSosFlgTran(boolean etcSosFlgTran) {
		this.etcSosFlgTran = etcSosFlgTran;
	}

	/**
	 * 処理用ONC組織フラグを取得する。
	 *
	 * @return 処理用ONC組織フラグ
	 */
	public boolean isOncSosFlgTran() {
		return oncSosFlgTran;
	}

	/**
	 * 処理用ONC組織フラグを設定する。
	 *
	 * @param etcSosFlgTran 処理用ONC組織フラグ
	 */
	public void setOncSosFlgTran(boolean oncSosFlgTran) {
		this.oncSosFlgTran = oncSosFlgTran;
	}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * 処理用カテゴリを取得する。
	 *
	 * @return 処理用カテゴリ
	 */
	public String getSosCategoryTran() {
		return sosCategoryTran;
	}

	/**
	 * 処理用カテゴリを設定する。
	 *
	 * @param sosCategoryTran 処理用カテゴリ
	 */
	public void setSosCategoryTran(String sosCategoryTran) {
		this.sosCategoryTran = sosCategoryTran;
	}
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	/**
	 * [トップ用]組織コードを取得する。
	 *
	 * @return [トップ用]組織コード
	 */
	public String getTopSosCd() {
		return topSosCd;
	}

	/**
	 * [トップ用]組織コードを設定する。
	 *
	 * @param topSosCd [トップ用]組織コード
	 */
	public void setTopSosCd(String topSosCd) {
		this.topSosCd = topSosCd;
	}

	/**
	 * [トップ用]従業員番号を取得する。
	 *
	 * @return [トップ用]従業員番号
	 */
	public String getTopJgiNo() {
		return topJgiNo;
	}

	/**
	 * [トップ用]従業員番号を設定する。
	 *
	 * @param topJgiNo [トップ用]従業員番号
	 */
	public void setTopJgiNo(String topJgiNo) {
		this.topJgiNo = topJgiNo;
	}

	/**
	 * [トップ用]部門ランクを取得する。
	 *
	 * @return [トップ用]部門ランク
	 */
	public String getTopBumonRank() {
		return topBumonRank;
	}

	/**
	 * [トップ用]部門ランクを設定する。
	 *
	 * @param topBumonRank [トップ用]部門ランク
	 */
	public void setTopBumonRank(String topBumonRank) {
		this.topBumonRank = topBumonRank;
	}

	/**
	 * 検索するかのフラグを取得する。
	 *
	 * @return 検索するかのフラグ
	 */
	public boolean isSearchFlg() {
		return searchFlg;
	}

	/**
	 * 検索するかのフラグを設定する。
	 *
	 * @param searchFlg 検索するかのフラグ
	 */
	public void setSearchFlg(boolean searchFlg) {
		this.searchFlg = searchFlg;
	}

	/**
	 * UH用タイトルを取得する。
	 * @return titleUH
	 */
	public String getTitleUH() {
		return titleUH;
	}

	/**
	 * UH用タイトルを設定する。
	 * @param titleUH セットする titleUH
	 */
	public void setTitleUH(String titleUH) {
		this.titleUH = titleUH;
	}

	/**
	 * P用タイトルを取得する。
	 * @return titleP
	 */
	public String getTitleP() {
		return titleP;
	}

	/**
	 * P用タイトルを設定する。
	 * @param titleP セットする titleP
	 */
	public void setTitleP(String titleP) {
		this.titleP = titleP;
	}

	/**
	 * Z用タイトルを取得する。
	 * @return titleZ
	 */
	public String getTitleZ() {
		return titleZ;
	}

	/**
	 * Z用タイトルを設定する。
	 * @param titleZ セットする titleZ
	 */
	public void setTitleZ(String titleZ) {
		this.titleZ = titleZ;
	}

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return ProdPlanScDto 変換されたScDto
	 */
	public ProdPlanScDto convertProdPlanScDto() throws Exception {
		// 組織コード(支店)
		if (StringUtils.isEmpty(sosCd2Tran)) {
			sosCd2Tran = null;
		}
		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}
		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}
		// 従業員番号
		Integer _jgiNoTran = null;
		if (StringUtils.isNotEmpty(this.jgiNoTran)) {
			_jgiNoTran = ConvertUtil.parseInteger(this.jgiNoTran);
		} else {
			this.jgiNoTran = null;
		}
		// 品目カテゴリ
		String _prodCategory = null;
		if (StringUtils.isNotEmpty(this.prodCategoryTran)) {
			_prodCategory = this.prodCategoryTran;
		} else {
			this.prodCategoryTran = null;
		}
		// カテゴリ
		if (StringUtils.isEmpty(sosCategoryTran)) {
			sosCategoryTran = null;
		}

		return new ProdPlanScDto(sosCd2Tran, sosCd3Tran, sosCd4Tran, _jgiNoTran, _prodCategory, oncSosFlgTran, sosCategoryTran, vaccineCode);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return ProdPlanUpdateDto 変換されたUpdateDto
	 */
	public List<SosPlanUpdateDto> convertSosPlanUpdateDto() throws Exception {
		List<SosPlanUpdateDto> updateDtoList = new ArrayList<SosPlanUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				// UH・Pともに計画値に変更がある場合のみ更新
				final boolean isUHSame = StringUtils.equals(rowId[8], rowId[15]);
				final boolean isPSame = StringUtils.equals(rowId[11], rowId[16]);
				final boolean isZSame = StringUtils.equals(rowId[14], rowId[17]);
				if (isUHSame && isPSame && isZSame) {
					continue;
				}
				// 組織コード
				String sosCd = "";
				if (StringUtils.isNotBlank(rowId[2])) {
					sosCd = rowId[2];
				} else if (StringUtils.isNotBlank(rowId[1])) {
					sosCd = rowId[1];
				} else if (StringUtils.isNotBlank(rowId[0])) {
					sosCd = rowId[0];
				}
				// 雑フラグ
				final Boolean etcSosFlg = ConvertUtil.parseBoolean(rowId[3]);
				// 従業員番号
				final Integer jgiNo = ConvertUtil.parseInteger(rowId[4]);
				// 品目コード
				final String prodCode = rowId[5];
				// 施設区分UHのDTO作成
				if (!isUHSame) {
					// [UH] シーケンスキー
					final Long seqKeyUH = ConvertUtil.parseLong(rowId[6]);
					// [UH] 最終更新日時
					final Date upDateUH = ConvertUtil.parseDate(rowId[7]);
					// [UH] 更新前計画値
					final Long yBaseValueUHBefore = ConvertUtil.parseLong(rowId[8]);
					// [UH] 更新後計画値
					final Long yBaseValueUHAfter = ConvertUtil.parseLong(rowId[15]);
					final SosPlanUpdateDto updateDto = new SosPlanUpdateDto(sosCd, etcSosFlg, jgiNo, prodCode, InsType.UH, seqKeyUH, upDateUH, yBaseValueUHBefore,
						yBaseValueUHAfter);
					updateDtoList.add(updateDto);
				}
				// 施設区分PのDTO作成
				if (!isPSame) {
					// [P] シーケンスキー
					final Long seqKeyP = ConvertUtil.parseLong(rowId[9]);
					// [P] 最終更新日時
					final Date upDateP = ConvertUtil.parseDate(rowId[10]);
					// [P] 更新前計画値
					final Long yBaseValuePBefore = ConvertUtil.parseLong(rowId[11]);
					// [P] 更新後計画値
					final Long yBaseValuePAfter = ConvertUtil.parseLong(rowId[16]);
					final SosPlanUpdateDto updateDto = new SosPlanUpdateDto(sosCd, etcSosFlg, jgiNo, prodCode, InsType.P, seqKeyP, upDateP, yBaseValuePBefore, yBaseValuePAfter);
					updateDtoList.add(updateDto);
				}
				// 施設区分PのDTO作成
				if (!isZSame) {
					// [Z] シーケンスキー
					final Long seqKeyZ = ConvertUtil.parseLong(rowId[12]);
					// [Z] 最終更新日時
					final Date upDateZ = ConvertUtil.parseDate(rowId[13]);
					// [Z] 更新前計画値
					final Long yBaseValueZBefore = ConvertUtil.parseLong(rowId[14]);
					// [Z] 更新後計画値
					final Long yBaseValueZAfter = ConvertUtil.parseLong(rowId[17]);
					final SosPlanUpdateDto updateDto = new SosPlanUpdateDto(sosCd, etcSosFlg, jgiNo, prodCode, InsType.ZATU, seqKeyZ, upDateZ, yBaseValueZBefore, yBaseValueZAfter);
					updateDtoList.add(updateDto);
				}
			}
		}
		setRowIdList(null);
		return updateDtoList;
	}

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.etcSosFlgTran = this.etcSosFlg;
		this.oncSosFlgTran = this.oncSosFlg;
		this.prodCategoryTran = this.prodCategory;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCategoryTran = this.sosCategory;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.sosCd2 = this.sosCd2Tran;
		this.sosCd3 = this.sosCd3Tran;
		this.sosCd4 = this.sosCd4Tran;
		this.jgiNo = this.jgiNoTran;
		this.etcSosFlg = this.etcSosFlgTran;
		this.oncSosFlg = this.oncSosFlgTran;
		this.prodCategory = this.prodCategoryTran;
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		this.sosCategory = this.sosCategoryTran;
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		setSosCd2(null);
		setSosCd3(null);
		setSosCd4(null);
		setJgiNo(null);
		setSearchFlg(false);
		setEtcSosFlg(false);
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//		setOncSosFlg(false);
		setOncSosFlg(true);
		setSosCategory(null);
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		setProdCategory(null);
		setTopSosCd(null);
		setTopJgiNo(null);
		setTopBumonRank(null);
		this.titleUH = "";
		this.titleP = "";
		this.titleZ = "";

	}
}
