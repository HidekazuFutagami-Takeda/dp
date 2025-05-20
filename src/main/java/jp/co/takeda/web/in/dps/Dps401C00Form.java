package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsWsPlanForVacJgiListUpdateDto;
import jp.co.takeda.dto.InsWsPlanJgiListResultDto;
import jp.co.takeda.dto.InsWsPlanJgiListScDto;
import jp.co.takeda.dto.InsWsPlanJgiListUpdateDto;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps401C00((医)施設特約店別計画担当者一覧画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dps401C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店別計画・支店
	 */
	public static final ProdPlanLevel DPS401C00_PLANLEVEL_INS = ProdPlanLevel.INS_WS;

	/**
	 * DPS401C00_DATA_R
	 */
	public static final BoxKey DPS401C00_DATA_R = new BoxKeyPerClassImpl(Dps401C00Form.class, InsWsPlanJgiListResultDto.class);

	/**
	 * データ有無フラグ(TRUE：データあり、FLASE：データなし)
	 */
	public static final BoxKey DATA_EXIST = new BoxKeyPerClassImpl(Dps401C00Form.class, Boolean.class);

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
	 * 計画立案レベル・施設特約店
	 */
	public static final ProdPlanLevel DPS401C00_PLANLEVEL_INS_WS = ProdPlanLevel.INS_WS;

	/**
	 * カテゴリ
	 */
	private String category;

//	/**
//	 * 品目カテゴリ
//	 */
//	private String prodCategory;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

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
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return PlannedProdSpecialInsPlanScDto 変換されたScDto
	 */
	public InsWsPlanJgiListScDto convertInsWsPlanMrListScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}

		// 品目コード
		if (StringUtils.isEmpty(prodCodeTran)) {
			prodCodeTran = null;
		}

		return new InsWsPlanJgiListScDto(sosCd3Tran, sosCd4Tran, prodCodeTran);
	}

	/**
	 * ActionForm ⇒ upDateDto 変換処理
	 *
	 * @return 変換されたUpDateDto
	 */
	public List<InsWsPlanJgiListUpdateDto> convertInsWsPlanJgiListUpdateDto() throws Exception {

		// 更新用DTOのリスト作成
		List<InsWsPlanJgiListUpdateDto> updateDtoList = new ArrayList<InsWsPlanJgiListUpdateDto>();
		if (selectRowIdList != null) {

			for (int i = 0; i < selectRowIdList.length; i++) {

				if (selectRowIdList[i] == null) {
					continue;
				}

				Integer _jgiNo = null;
				String _jgiName = null;
				Date _lastUpDate = null;

				String[] rowId = ConvertUtil.splitConmma(selectRowIdList[i]);

				// 従業員番号
				if (!StringUtils.isEmpty(rowId[0])) {
					_jgiNo = new Integer(rowId[0]);
				}
				// 従業員氏名
				if (!StringUtils.isEmpty(rowId[1])) {
					_jgiName = rowId[1];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[2])) {
					_lastUpDate = new Date(Long.parseLong(rowId[2]));
				}

				InsWsPlanJgiListUpdateDto updateDto = new InsWsPlanJgiListUpdateDto(_jgiNo, _jgiName, _lastUpDate);
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
	public List<InsWsPlanForVacJgiListUpdateDto> convertInsWsPlanForVacJgiListUpdateDto() throws Exception {

		// 更新用DTOのリスト作成
		List<InsWsPlanForVacJgiListUpdateDto> updateDtoList = new ArrayList<InsWsPlanForVacJgiListUpdateDto>();
		if (selectRowIdList != null) {

			for (int i = 0; i < selectRowIdList.length; i++) {

				if (selectRowIdList[i] == null) {
					continue;
				}

				Integer _jgiNo = null;
				String _jgiName = null;
				Date _lastUpDate = null;

				String[] rowId = ConvertUtil.splitConmma(selectRowIdList[i]);

				// 従業員番号
				if (!StringUtils.isEmpty(rowId[0])) {
					_jgiNo = new Integer(rowId[0]);
				}
				// 従業員氏名
				if (!StringUtils.isEmpty(rowId[1])) {
					_jgiName = rowId[1];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[2])) {
					_lastUpDate = new Date(Long.parseLong(rowId[2]));
				}

				InsWsPlanForVacJgiListUpdateDto updateDto = new InsWsPlanForVacJgiListUpdateDto(_jgiNo, _jgiName, _lastUpDate);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	/**
	 * 検索条件を処理用フィールドに設定する。
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.prodCodeTran = this.prodCode;
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
	 * 処理用品目固定コード
	 */
	private String prodCodeTran;

	/**
	 * ユーザの部門ランク<br>
	 * 1：営業所以上 2：AL 3：MR
	 */
	private String rank;

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
	 * 処理用品目固定コードを取得する。
	 *
	 * @return prodCodeTran 処理用品目固定コード
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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		SosMst defaultSosMst = userInfo.getDefaultSosMst();
		if (defaultSosMst == null || defaultSosMst.getBumonRank() == null || defaultSosMst.getBumonRank().getDbValue() <= 2) {
			this.prodCode = null;
			this.category = null;
		}
		this.sosCd3 = null;
		this.sosCd4 = null;
		this.selectRowIdList = null;
		this.sosCd3Tran = null;
		this.sosCd4Tran = null;
		this.prodCodeTran = null;
		this.rank = null;
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

//	/**
//	 * 品目カテゴリを取得する。
//	 *
//	 * @return prodCategory 品目カテゴリ
//	 */
//	public String getProdCategory() {
//		return prodCategory;
//	}
//
//	/**
//	 * 品目カテゴリを設定する。
//	 *
//	 * @param prodCategory 品目カテゴリ
//	 */
//	public void setProdCategory(String prodCategory) {
//		this.prodCategory = prodCategory;
//	}
}
