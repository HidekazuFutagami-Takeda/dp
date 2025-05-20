package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsWsPlanForVacProdListScDto;
import jp.co.takeda.dto.InsWsPlanForVacProdListUpdateDto;
import jp.co.takeda.dto.InsWsPlanProdListResultDto;
import jp.co.takeda.dto.InsWsPlanProdListScDto;
import jp.co.takeda.dto.InsWsPlanProdListUpdateDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dps401C01((医)施設特約店別計画品目一覧画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dps401C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ファイル名
	 */
	protected String downloadFileName;

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
	 * DPS401C01_DATA_R
	 */
	public static final BoxKey DPS401C01_DATA_R = new BoxKeyPerClassImpl(Dps401C01Form.class, InsWsPlanProdListResultDto.class);

	/**
	 * データ有無フラグ(TRUE：データあり、FLASE：データなし)
	 */
	public static final BoxKey DATA_EXIST = new BoxKeyPerClassImpl(Dps401C01Form.class, Boolean.class);

	/**
	 * MRによるTL編集モード(TRUE：TLモード、FLASE：MR（通常）モード)
	 */
	public static final BoxKey TL_MODE = new BoxKeyPerClassImpl(Dps401C01Form.class, Integer.class);

	/**
	 * (医)施設特約店別計画(自)
	 */
	public static final DpAuthority EDIT_AUTH_MMP_PLAN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(自)担当者再配分
	 */
	public static final DpAuthority EDIT_AUTH_MMP_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(自)確定
	 */
	public static final DpAuthority EDIT_AUTH_MMP_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(自)確定解除
	 */
	public static final DpAuthority EDIT_AUTH_MMP_NONFIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(自)公開・解除
	 */
	public static final DpAuthority EDIT_AUTH_MMP_OPEN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(仕)
	 */
	public static final DpAuthority EDIT_AUTH_SHIIRE_PLAN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(仕)担当者再配分
	 */
	public static final DpAuthority EDIT_AUTH_SHIIRE_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(仕)確定
	 */
	public static final DpAuthority EDIT_AUTH_SHIIRE_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(仕)確定
	 */
	public static final DpAuthority EDIT_AUTH_SHIIRE_NONFIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (医)施設特約店別計画(仕)公開・解除
	 */
	public static final DpAuthority EDIT_AUTH_SHIIRE_OPEN = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画担当者再配分
	 */
	public static final DpAuthority EDIT_AUTH_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画公開
	 */
	public static final DpAuthority EDIT_AUTH_OPEN = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画確定
	 */
	public static final DpAuthority EDIT_AUTH_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画確定解除
	 */
	public static final DpAuthority EDIT_AUTH_NONFIX = new DpAuthority( AuthType.EDIT);

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPS401C01_PLANLEVEL_INS_WS = ProdPlanLevel.INS_WS;

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return PlannedProdSpecialInsPlanScDto 変換されたScDto
	 */
	public InsWsPlanProdListScDto convertInsWsPlanProdListScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}

		// 従業員コード
		Integer _jgiNo = null;
		if (ValidationUtil.isInteger(jgiNoTran)) {
			_jgiNo = new Integer(jgiNoTran);
		}

		// カテゴリ
		String _category = this.category;

		return new InsWsPlanProdListScDto(sosCd3Tran, sosCd4Tran, _jgiNo, _category);
	}

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return 変換されたScDto
	 */
	public InsWsPlanForVacProdListScDto convertInsWsPlanForVacProdListScDto() throws Exception {

		// 組織コード(エリア特約店G)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 従業員番号
		Integer _jgiNo = null;
		if (ValidationUtil.isInteger(jgiNoTran)) {
			_jgiNo = new Integer(jgiNoTran);
		}

		return new InsWsPlanForVacProdListScDto(sosCd3Tran, _jgiNo);
	}

	/**
	 * ActionForm ⇒ upDateDto 変換処理
	 *
	 * @return PlannedProdSpecialInsPlanUpDateDto 変換されたUpDateDto
	 */
	public List<InsWsPlanProdListUpdateDto> convertInsWsPlanProdListUpdateDtoList() throws Exception {

		// カテゴリ
		String _category = convertCategory();

		// 更新用DTOのリスト作成
		List<InsWsPlanProdListUpdateDto> updateDtoList = new ArrayList<InsWsPlanProdListUpdateDto>();
		if (selectRowIdList != null) {

			for (int i = 0; i < selectRowIdList.length; i++) {

				if (selectRowIdList[i] == null) {
					continue;
				}

				String _prodCode = null;
				String _prodName = null;
				Date _lastUpDate = null;

				String[] rowId = ConvertUtil.splitConmma(selectRowIdList[i]);

				// 品目固定コード
				if (!StringUtils.isEmpty(rowId[0])) {
					_prodCode = rowId[0];
				}
				// 品目名称
				if (!StringUtils.isEmpty(rowId[1])) {
					_prodName = rowId[1];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[2])) {
					_lastUpDate = new Date(Long.parseLong(rowId[2]));
				}

				InsWsPlanProdListUpdateDto updateDto = new InsWsPlanProdListUpdateDto(_prodCode, _prodName, _category, _lastUpDate);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	/**
	 * ActionForm ⇒ upDateDto 変換処理
	 *
	 * @return 変換されたUpDateDto
	 */
	public List<InsWsPlanForVacProdListUpdateDto> convertInsWsPlanForVacProdListUpdateDto() throws Exception {

		// 更新用DTOのリスト作成
		List<InsWsPlanForVacProdListUpdateDto> updateDtoList = new ArrayList<InsWsPlanForVacProdListUpdateDto>();
		if (selectRowIdList != null) {

			for (int i = 0; i < selectRowIdList.length; i++) {

				if (selectRowIdList[i] == null) {
					continue;
				}

				String _prodCode = null;
				String _prodName = null;
				Date _lastUpDate = null;

				String[] rowId = ConvertUtil.splitConmma(selectRowIdList[i]);

				// 品目固定コード
				if (!StringUtils.isEmpty(rowId[0])) {
					_prodCode = rowId[0];
				}
				// 品目名称
				if (!StringUtils.isEmpty(rowId[1])) {
					_prodName = rowId[1];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[2])) {
					_lastUpDate = new Date(Long.parseLong(rowId[2]));
				}

				InsWsPlanForVacProdListUpdateDto updateDto = new InsWsPlanForVacProdListUpdateDto(_prodCode, _prodName, _lastUpDate);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	/**
	 * カテゴリ(文字列)をProdCategoryに変換する。
	 *
	 * @return ProdCategory
	 */
	public String convertCategory() {

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//		ProdCategory _category;
//		if (StringUtils.isEmpty(categoryTran)) {
//			_category = ProdCategory.MMP;
//		} else if (categoryTran.equals("1")) {
//			_category = ProdCategory.MMP;
//		} else if (categoryTran.equals("2")) {
//			_category = ProdCategory.SHIIRE;
//		} else if (categoryTran.equals("3")) {
//			_category = ProdCategory.ONC;
//		} else {
//			_category = ProdCategory.MMP;
//		}

		String _category = null;

		try {
			_category = category;
		} finally{
			// 何もしない
		}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

		return _category;
	}

	/**
	 * 検索条件を処理用フィールドに設定
	 *
	 *
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.prodCodeTran = this.prodCode;
		this.categoryTran = this.category;
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
	 * 品目カテゴリ
	 */
	private String category;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * ユーザの部門ランク<br>
	 * 1：営業所以上 2：AL 3：MR
	 */
	private String rank;

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

	// 処理用フィールド
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
	 * 処理用品目固定コード
	 */
	private String prodCodeTran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String categoryTran;

	/**
	 * 本部・本部ワクチンGユーザーであるか
	 */
	private boolean honbuUser;

	/**
	 * グリッドヘッダ
	 */
	private String header;

	/**
	 * タイトル（UH)
	 */
	private String titleUh;

	/**
	 * タイトル（P)
	 */
	private String titleP;

	/**
	 * ダウンロードファイル名を設定する。
	 *
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
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
	 * 品目カテゴリを取得する。
	 *
	 * @return category 品目カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 品目カテゴリを設定する。
	 *
	 * @param category 品目カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 品目カテゴリリストを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public List<CodeAndValue> getProdCategoryList() {
		return prodCategoryList;
	}

	/**
	 * 品目カテゴリリストを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return selectRowIdList 品目固定コード
	 */
	public String[] getSelectRowIdList() {
		return selectRowIdList;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param selectRowIdList 品目固定コード
	 */
	public void setSelectRowIdList(String[] selectRowIdList) {
		this.selectRowIdList = selectRowIdList;
	}

	/**
	 * 部門ランクを取得する。<br>
	 * 1：営業所以上 2：AL 3：MR
	 *
	 * @return 部門ランク
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * 部門ランクを設定する。<br>
	 * 1：営業所以上 2：AL 3：MR
	 *
	 * @param rank 部門ランク
	 */
	public void setRank(String rank) {
		this.rank = rank;
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
	 * @return 処理用従業員番号
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
	 * 処理用品目固定コードを取得する。
	 *
	 * @return 処理用品目固定コード
	 */
	public String getProdCodeTran() {
		return prodCodeTran;
	}

	/**
	 * 処理用品目固定コードを設定する。
	 *
	 * @param prodCodeTran 処理用品目固定コード
	 */
	public void setProdCodeTran(String prodCodeTran) {
		this.prodCodeTran = prodCodeTran;
	}

	/**
	 * 処理用品目カテゴリを取得する。
	 *
	 * @return 処理用品目カテゴリ
	 */
	public String getCategoryTran() {
		return categoryTran;
	}

	/**
	 * 処理用品目カテゴリを設定する。
	 *
	 * @param categoryTran 処理用品目カテゴリ
	 */
	public void setCategoryTran(String categoryTran) {
		this.categoryTran = categoryTran;
	}

	/**
	 * 本部・本部ワクチンGユーザであるかを取得する。
	 * @return 本部・本部ワクチンGユーザであるか
	 */
	public boolean isHonbuUser() {
		return honbuUser;
	}

	/**
	 * 本部・本部ワクチンGユーザであるかを設定する。
	 * @param honbuUser 本部・本部ワクチンGユーザであるか
	 */
	public void setHonbuUser(boolean honbuUser) {
		this.honbuUser = honbuUser;
	}

	/**
	 * グリッドヘッダを取得する
	 * @return グリッドヘッダ
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * グリッドヘッダを設定する
	 * @param header グリッドヘッダ
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * タイトル（UH)を取得する。
	 *
	 * @return タイトル（UH)
	 */
	public String getTitleUh() {
		return titleUh;
	}

	/**
	 * タイトル（UH)を設定する。
	 *
	 * @param titleUh タイトル（UH)
	 */
	public void setTitleUh(String titleUh) {
		this.titleUh = titleUh;
	}

	/**
	 * タイトル（P）を取得する。
	 *
	 * @return タイトル（P)
	 */
	public String getTitleP() {
		return titleP;
	}

	/**
	 * タイトル（P）を設定する。
	 *
	 * @param titleP タイトル（P）
	 */
	public void setTitleP(String titleP) {
		this.titleP = titleP;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// categoryはトップ画面から遷移することを考慮し、初期化対象からはずす
//		this.category = null;
		this.sosCd3 = null;
		this.sosCd4 = null;
		this.jgiNo = null;
		this.prodCode = null;
		this.rank = null;
		this.selectRowIdList = null;
		this.sosCd3Tran = null;
		this.sosCd4Tran = null;
		this.jgiNoTran = null;
		this.prodCodeTran = null;
		this.categoryTran = null;
	}
}
