package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.ProdPlanSummaryResultDto;
import jp.co.takeda.dto.ProdPlanSummaryScDto;
import jp.co.takeda.dto.SosPlanUpdateDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dpm501C01((医)積上結果表示ダイアログ)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dpm501C01Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM501C01_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm501C00Form.class, ProdPlanSummaryResultDto.class);

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM501C01_DATA_R_FORM = new BoxKeyPerClassImpl(Dpm501C00Form.class, ProdPlanSummaryResultDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM501C01_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0]=品目コード</li>
	 * <li>Sprit文字列[1]=[UH]シーケンスキー</li>
	 * <li>Sprit文字列[2]=[UH]最終更新日時</li>
	 * <li>Sprit文字列[3]=[UH]Y価ベース 積上前</li>
	 * <li>Sprit文字列[4]=[P ]シーケンスキー</li>
	 * <li>Sprit文字列[5]=[P ]最終更新日時</li>
	 * <li>Sprit文字列[6]=[P ]Y価ベース 積上前</li>
	 * <li>Sprit文字列[7]=[Z ]シーケンスキー</li>
	 * <li>Sprit文字列[8]=[Z ]最終更新日時</li>
	 * <li>Sprit文字列[9]=[Z ]Y価ベース 積上前</li>
	 * <li>Sprit文字列[10]=[UH]Y価ベース 積上後</li>
	 * <li>Sprit文字列[11]=[P ]Y価ベース 積上後</li>
	 * <li>Sprit文字列[12]=[Z ]Y価ベース 積上後</li>
	 * </ul>
	 *
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


	// -------------------------------
	// getter/setter
	// -------------------------------
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
	 * @return ProdPlanSummaryScDto 変換されたScDto
	 */
	public ProdPlanSummaryScDto convertProdPlanSummaryScDto() throws Exception {
		// 従業員番号
		Integer jgiNo = ConvertUtil.parseInteger(this.jgiNo);
		return new ProdPlanSummaryScDto(jgiNo, this.prodCategory);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return ProdPlanSummaryUpdateDto 変換されたUpdateDto
	 */
	public List<SosPlanUpdateDto> convertProdPlanSummaryUpdateDto() throws Exception {
		List<SosPlanUpdateDto> updateDtoList = new ArrayList<SosPlanUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				// UH・Pともに計画値に変更がある場合のみ更新
				final boolean isUHSame = StringUtils.equals(rowId[3], rowId[10]);
				final boolean isPSame = StringUtils.equals(rowId[6], rowId[11]);
				final boolean isZSame = StringUtils.equals(rowId[9], rowId[12]);
				if (isUHSame && isPSame && isZSame) {
					continue;
				}

				// 従業員番号
				final Integer jgiNo = ConvertUtil.parseInteger(this.jgiNo);

				// 品目コード
				final String prodCode = rowId[0];

				// 施設区分UHのDTO作成
				if (!isUHSame) {

					// [UH] シーケンスキー
					final Long seqKeyUH = ConvertUtil.parseLong(rowId[1]);

					// [UH] 最終更新日時
					final Date upDateUH = ConvertUtil.parseDate(rowId[2]);

					// [UH] 更新前計画値
					final Long baseValueUHBefore = ConvertUtil.parseLong(rowId[3]);

					// [UH] 更新後計画値
					final Long baseValueUHAfter = ConvertUtil.parseLong(rowId[10]);

					final SosPlanUpdateDto updateDto = new SosPlanUpdateDto(null, false, jgiNo, prodCode, InsType.UH, seqKeyUH, upDateUH, baseValueUHBefore, baseValueUHAfter);
					updateDtoList.add(updateDto);
				}

				// 施設区分PのDTO作成
				if (!isPSame) {
					// [P] シーケンスキー
					final Long seqKeyP = ConvertUtil.parseLong(rowId[4]);

					// [P] 最終更新日時
					final Date upDateP = ConvertUtil.parseDate(rowId[5]);

					// [P] 更新前計画値
					final Long baseValuePBefore = ConvertUtil.parseLong(rowId[6]);

					// [P] 更新後計画値
					final Long baseValuePAfter = ConvertUtil.parseLong(rowId[11]);

					final SosPlanUpdateDto updateDto = new SosPlanUpdateDto(null, false, jgiNo, prodCode, InsType.P, seqKeyP, upDateP, baseValuePBefore, baseValuePAfter);
					updateDtoList.add(updateDto);
				}

				// 施設区分ZのDTO作成
				if (!isZSame) {
					// [Z] シーケンスキー
					final Long seqKeyZ = ConvertUtil.parseLong(rowId[7]);

					// [Z] 最終更新日時
					final Date upDateZ = ConvertUtil.parseDate(rowId[8]);

					// [Z] 更新前計画値
					final Long baseValueZBefore = ConvertUtil.parseLong(rowId[9]);

					// [Z] 更新後計画値
					final Long baseValueZAfter = ConvertUtil.parseLong(rowId[12]);

					final SosPlanUpdateDto updateDto = new SosPlanUpdateDto(null, false, jgiNo, prodCode, InsType.ZATU, seqKeyZ, upDateZ, baseValueZBefore, baseValueZAfter);
					updateDtoList.add(updateDto);
				}

			}
		}
		setRowIdList(null);
		return updateDtoList;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.titleUH = "";
		this.titleP = "";
		this.titleZ = "";
	}

}
