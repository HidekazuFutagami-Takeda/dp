package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.ExceptDistInsDeleteDto;
import jp.co.takeda.dto.ExceptDistInsResultListDto;
import jp.co.takeda.dto.ExceptDistInsScDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps201C00((医)配分除外施設一覧画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dps201C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS201C00_DATA_R
	 */
	public static final BoxKey DPS201C00_DATA_R = new BoxKeyPerClassImpl(Dps201C00Form.class, ExceptDistInsResultListDto.class);

	/**
	 * 削除権限
	 */
	public static final DpAuthority DPS201C00_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return InsMstScDto 変換されたScDto
	 */
	public ExceptDistInsScDto convertExceptDistInsScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3Tran)) {
			sosCd3Tran = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4Tran)) {
			sosCd4Tran = null;
		}

		// 従業員番号
		Integer iJgiNo = null;
		if (StringUtils.isNotEmpty(jgiNoTran)) {
			iJgiNo = Integer.valueOf(jgiNoTran);
		}

		// カテゴリ
		if (StringUtils.isEmpty(categoryTran)) {
			categoryTran = null;
		}

		return new ExceptDistInsScDto(sosCd3Tran, sosCd4Tran, iJgiNo, categoryTran);
	}

	/**
	 * ActionForm⇒DeleteDto 変換処理
	 *
	 * @return 変換されたDeleteDtoのリスト
	 */
	public List<ExceptDistInsDeleteDto> convertExceptDistInsDeleteDto() throws Exception {
		List<ExceptDistInsDeleteDto> deleteDtoList = new ArrayList<ExceptDistInsDeleteDto>();
		if (selectRowIdList != null) {
			for (int i = 0; i < selectRowIdList.length; i++) {
				String[] selectRowId = selectRowIdList[i].split(",");
				// 施設コード
				final String insNo = selectRowId[0];
				// 従業員番号
				final Integer jgiNo = ConvertUtil.parseInteger(selectRowId[1]);
				// 最終更新日時
				String upDateTime = selectRowId[2];
				Date upDate = ConvertUtil.parseDate(upDateTime);
				ExceptDistInsDeleteDto deleteDto = new ExceptDistInsDeleteDto(insNo, jgiNo, upDate);
				deleteDtoList.add(deleteDto);
			}
		}
		return deleteDtoList;
	}

	/**
	 * 検索条件を処理用フィールドに設定する。
	 */
	public void setTranField() {
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.categoryTran = this.category;
	}

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
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 選択行の識別IDリスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=施設コード
	 * Sprit文字列[1]=最終更新日時(Date型のgetTime()の値)
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
	 * 処理用カテゴリ
	 */
	private String categoryTran;

	/**
	 * ワクチンコード
	 */
	private String vaccineCode;

	/**
	 *
	 * 組織に紐づくカテゴリリスト
	 *
	 * <pre>
	 * 識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=組織コード
	 * Sprit文字列[1]=カテゴリコード
	 * (例)---------------------------------------
	 * sosCdCategoryList[0] = "00961,01"
	 * sosCdCategoryList[1] = "00961,02"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] sosCdCategoryList;

	/**
	 * 組織コード(支店)を取得する。
	 *
	 * @return 組織コード(支店)
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
	 * param sosCd3 組織コード(営業所)
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
	 * 処理用カテゴリを取得する。
	 *
	 * @return 処理用カテゴリ
	 */
	public String getCategoryTran() {
		return categoryTran;
	}

	/**
	 * 処理用カテゴリを設定する。
	 *
	 * @param categoryTran 処理用カテゴリ
	 */
	public void setCategoryTran(String categoryTran) {
		this.categoryTran = categoryTran;
	}

	/**
	 * ワクチンコードを取得する。
	 *
	 * @return ワクチンコード
	 */
	public String getVaccineCode() {
		return vaccineCode;
	}

	/**
	 * ワクチンコードを設定する。
	 *
	 * @param vaccineCode ワクチンコード
	 */
	public void setVaccineCode(String vaccineCode) {
		this.vaccineCode = vaccineCode;
	}

	/**
	 * 組織に紐づくカテゴリリストを取得する。
	 *
	 * @return sosCdCategoryList 組織に紐づくカテゴリリスト
	 */
	public String[] getSosCdCategoryList() {
		return sosCdCategoryList;
	}

	/**
	 * 組織に紐づくカテゴリリストを設定する。
	 *
	 * @param sosCdCategoryList 組織に紐づくカテゴリリスト
	 */
	public void setSosCdCategoryList(String[] sosCdCategoryList) {
		this.sosCdCategoryList = sosCdCategoryList;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.sosCd3 = null;
		this.sosCd4 = null;
		this.jgiNo = null;
		this.category = null;
		this.sosCd3Tran = null;
		this.sosCd4Tran = null;
		this.jgiNoTran = null;
		this.categoryTran = null;
		this.selectRowIdList = null;
		this.vaccineCode = "";
		this.prodCategoryList = null;
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
}
