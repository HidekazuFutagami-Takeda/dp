package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.ManageWsPlanDto;
import jp.co.takeda.dto.ManageWsPlanEntryDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.dto.ManageWsPlanScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm300C00((医)特約店別計画編集画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dpm300C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPM300C00_DATA_R
	 */
	public static final BoxKey DPM300C00_DATA_R = new BoxKeyPerClassImpl(Dpm300C00Form.class, ManageWsPlanDto.class);

	/**
	 * DPM300C00_INPUT_DATA_R
	 */
	public static final BoxKey DPM300C00_INPUT_DATA_R = new BoxKeyPerClassImpl(Dpm300C00Form.class, ManageWsPlanForIyakuHeaderDto.class);

	/**
	 * DPM300C01_DATA_R
	 */
	public static final BoxKey DPM300C01_DATA_R = new BoxKeyPerClassImpl(Dpm300C00Form.class, ManageWsPlanForVacDto.class);

	/**
	 * DPM300C01_INPUT_DATA_R
	 */
	public static final BoxKey DPM300C01_INPUT_DATA_R = new BoxKeyPerClassImpl(Dpm300C00Form.class, ManageWsPlanForVacHeaderDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM300C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・特約店
	 */
	public static final ProdPlanLevel DPM300C00_PLANLEVEL_WS = ProdPlanLevel.WS;

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.tmsTytenNameTran = this.tmsTytenName;
		this.tmsTytenCdPartTran = this.tmsTytenCdPart;
		this.prodCategoryTran = this.prodCategory;
		this.prodCodeTran = this.prodCode;
		this.planDataTran = this.planData;
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.tmsTytenName = this.tmsTytenNameTran;
		this.tmsTytenCdPart = this.tmsTytenCdPartTran;
		this.prodCategory = this.prodCategoryTran;
		this.prodCode = this.prodCodeTran;
		this.planData = this.planDataTran;
	}


	/**
	 * ActionForm⇒条件Dto 変換処理
	 *
	 * @return 変換された条件Dto
	 *
	 */
	public ManageWsPlanScDto convertScDto() throws Exception {
		if (StringUtils.isEmpty(this.tmsTytenCdPartTran)) {
			this.tmsTytenCdPartTran = null;
		}
		if (StringUtils.isEmpty(this.prodCategoryTran)) {
			this.prodCategoryTran = null;
		}
		if (StringUtils.isEmpty(this.prodCodeTran)) {
			this.prodCodeTran = null;
		}
		PlanData _planData = null;
		if (StringUtils.isEmpty(this.planDataTran)) {
			_planData = null;
		} else {
			_planData = PlanData.getInstance(planDataTran);
		}
		return new ManageWsPlanScDto(tmsTytenCdPartTran, prodCategoryTran, prodCodeTran, _planData);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return ManageWsPlanEntryDto 変換されたUpdateDto
	 */
	public List<ManageWsPlanEntryDto> convertEntryDto() throws Exception {
		List<ManageWsPlanEntryDto> updateDtoList = new ArrayList<ManageWsPlanEntryDto>();
		if (updateDtoList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				// 更新対象行の絞込。変更のあった行のみ更新する。
				final boolean isUHSame = StringUtils.equals(rowId[4], rowId[11]);
				final boolean isPSame = StringUtils.equals(rowId[7], rowId[12]);
				final boolean isZSame = StringUtils.equals(rowId[10], rowId[13]);

				// 品目固定コード
				final String prodCode = rowId[0];

				// 特約店コード
				final String tmsTytenCd = rowId[1];

				// 施設区分UHのDTO作成
				if (!isUHSame) {
					// [UH] シーケンスキー
					final Long seqKeyUH = ConvertUtil.parseLong(rowId[2]);

					// [UH] 最終更新日時
					final Date upDateUH = ConvertUtil.parseDate(rowId[3]);

					// [UH] 更新前計画値
					final Long yBaseValueUHBefore = ConvertUtil.parseLong(rowId[4]);

					// [UH] 更新後計画値
					final Long yBaseValueUHAfter = ConvertUtil.parseLong(rowId[11]);

					final ManageWsPlanEntryDto updateDto = new ManageWsPlanEntryDto(prodCode, tmsTytenCd, InsType.UH, seqKeyUH, upDateUH, yBaseValueUHBefore, yBaseValueUHAfter);
					updateDtoList.add(updateDto);
				}

				// 施設区分PのDTO作成
				if (!isPSame) {
					// [P] シーケンスキー
					final Long seqKeyP = ConvertUtil.parseLong(rowId[5]);

					// [P] 最終更新日時
					final Date upDateP = ConvertUtil.parseDate(rowId[6]);

					// [P] 更新前計画値
					final Long yBaseValuePBefore = ConvertUtil.parseLong(rowId[7]);

					// [P] 更新後計画値
					final Long yBaseValuePAfter = ConvertUtil.parseLong(rowId[12]);

					final ManageWsPlanEntryDto updateDto = new ManageWsPlanEntryDto(prodCode, tmsTytenCd, InsType.P, seqKeyP, upDateP, yBaseValuePBefore, yBaseValuePAfter);
					updateDtoList.add(updateDto);
				}

				// 施設区分ZのDTO作成
				if (!isZSame) {
					// [Z] シーケンスキー
					final Long seqKeyP = ConvertUtil.parseLong(rowId[8]);

					// [Z] 最終更新日時
					final Date upDateP = ConvertUtil.parseDate(rowId[9]);

					// [Z] 更新前計画値
					final Long sBaseValuePBefore = ConvertUtil.parseLong(rowId[10]);

					// [Z] 更新後計画値
					final Long sBaseValuePAfter = ConvertUtil.parseLong(rowId[13]);

					final ManageWsPlanEntryDto updateDto = new ManageWsPlanEntryDto(prodCode, tmsTytenCd, InsType.ZATU, seqKeyP, upDateP, sBaseValuePBefore, sBaseValuePAfter);
					updateDtoList.add(updateDto);
				}

			}
		}
		return updateDtoList;
	}

	/**
	 * （ワ）ActionForm⇒条件Dto 変換処理
	 *
	 * @return 変換された条件Dto
	 *
	 */
	public ManageWsPlanForVacScDto convertScVacDto() throws Exception {
		if (StringUtils.isEmpty(this.tmsTytenCdPartTran)) {
			this.tmsTytenCdPartTran = null;
		}
		if (StringUtils.isEmpty(this.prodCodeTran)) {
			this.prodCodeTran = null;
		}
		PlanData _planData = null;
		if (StringUtils.isEmpty(this.planDataTran)) {
			_planData = null;
		} else {
			_planData = PlanData.getInstance(planDataTran);
		}
		return new ManageWsPlanForVacScDto(tmsTytenCdPartTran, prodCodeTran, _planData);
	}

	/**
	 * （ワ）ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return ManageWsPlanEntryDto 変換されたUpdateDto
	 */
	public List<ManageWsPlanForVacEntryDto> convertEntryVacDto() throws Exception {
		List<ManageWsPlanForVacEntryDto> updateDtoList = new ArrayList<ManageWsPlanForVacEntryDto>();
		if (updateDtoList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				// 更新対象行の絞込。変更のあった行のみ更新する。
				final boolean isSame = StringUtils.equals(rowId[4], rowId[5]);

				// 品目固定コード
				final String prodCode = rowId[0];

				// 特約店コード
				final String tmsTytenCd = rowId[1];

				// DTO作成
				if (!isSame) {
					// シーケンスキー
					final Long seqKeyUH = ConvertUtil.parseLong(rowId[2]);

					// 最終更新日時
					final Date upDateUH = ConvertUtil.parseDate(rowId[3]);

					// 更新前計画値
					final Long yBaseValueBefore = ConvertUtil.parseLong(rowId[4]);

					// 更新後計画値
					final Long yBaseValueAfter = ConvertUtil.parseLong(rowId[5]);

					final ManageWsPlanForVacEntryDto updateDto = new ManageWsPlanForVacEntryDto(prodCode, tmsTytenCd, seqKeyUH, upDateUH, yBaseValueBefore, yBaseValueAfter);
					updateDtoList.add(updateDto);
				}

			}
		}
		return updateDtoList;
	}

	/**
	 * 特約店名
	 */
	private String tmsTytenName;

	/**
	 * 特約店コード(入力欄用)
	 */
	private String tmsTytenCdPart;

	/**
	 * カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 計画
	 */
	private String planData;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	// 処理用フィールド
	/**
	 * 処理用特約店名
	 */
	private String tmsTytenNameTran;

	/**
	 * 処理用特約店コード(入力欄用)
	 */
	private String tmsTytenCdPartTran;

	/**
	 * 処理用カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 処理用品目固定コード
	 */
	private String prodCodeTran;

	/**
	 * 処理用計画
	 */
	private String planDataTran;

	/**
	 * 流通政策部であるか
	 */
	private boolean ryutsu;

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
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0]=品目固定コード</li>
	 * <li>Sprit文字列[1]=特約店コード</li>
	 * <li>Sprit文字列[2]=[UH]シーケンスキー</li>
	 * <li>Sprit文字列[3]=[UH]最終更新日時</li>
	 * <li>Sprit文字列[4]=[UH]T価ベース 更新前</li>
	 * <li>Sprit文字列[5]=[P ]シーケンスキー</li>
	 * <li>Sprit文字列[6]=[P ]最終更新日時</li>
	 * <li>Sprit文字列[7]=[P ]T価ベース 更新前</li>
	 * <li>Sprit文字列[8]=[Z ]シーケンスキー</li>
	 * <li>Sprit文字列[9]=[Z ]最終更新日時</li>
	 * <li>Sprit文字列[10]=[Z ]T価ベース 更新前</li>
	 * <li>Sprit文字列[11]=[UH]T価ベース 更新後（入力値）</li>
	 * <li>Sprit文字列[12]=[P ]T価ベース 更新後（入力値）</li>
	 * <li>Sprit文字列[13]=[Z ]T価ベース 更新後（入力値）</li>
	 * </ul>
	 */
	private String[] rowIdList;

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


	/**
	 * 特約店名を取得する。
	 *
	 * @return tmsTytenName 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 特約店名を設定する。
	 *
	 * @param tmsTytenName 特約店名
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
	}

	/**
	 * 特約店コード(入力欄用)を取得する。
	 *
	 * @return tmsTytenCdPart 特約店コード(入力欄用)
	 */
	public String getTmsTytenCdPart() {
		return tmsTytenCdPart;
	}

	/**
	 * 特約店コード(入力欄用)を設定する。
	 *
	 * @param tmsTytenCdPart 特約店コード(入力欄用)
	 */
	public void setTmsTytenCdPart(String tmsTytenCdPart) {
		this.tmsTytenCdPart = tmsTytenCdPart;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	/**
	 * 品目を取得する。
	 *
	 * @return prodCode 品目
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目を設定する。
	 *
	 * @param prodCode 品目
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 計画を取得する。
	 *
	 * @return planData 計画
	 */
	public String getPlanData() {
		return planData;
	}

	/**
	 * 計画を設定する。
	 *
	 * @param planData 計画
	 */
	public void setPlanData(String planData) {
		this.planData = planData;
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
	 * 処理用特約店名を取得する。
	 *
	 * @return 処理用特約店名
	 */
	public String getTmsTytenNameTran() {
		return tmsTytenNameTran;
	}

	/**
	 * 処理用特約店名を設定する。
	 *
	 * @param tmsTytenNameTran 処理用特約店名
	 */
	public void setTmsTytenNameTran(String tmsTytenNameTran) {
		this.tmsTytenNameTran = tmsTytenNameTran;
	}

	/**
	 * 処理用特約店コード(入力欄用)を取得する。
	 *
	 * @return 処理用特約店コード(入力欄用)
	 */
	public String getTmsTytenCdPartTran() {
		return tmsTytenCdPartTran;
	}

	/**
	 * 処理用特約店コード(入力欄用)を設定する。
	 *
	 * @param tmsTytenCdPartTran 処理用特約店コード(入力欄用)
	 */
	public void setTmsTytenCdPartTran(String tmsTytenCdPartTran) {
		this.tmsTytenCdPartTran = tmsTytenCdPartTran;
	}

	/**
	 * 処理用カテゴリを取得する。
	 *
	 * @return 処理用カテゴリ
	 */
	public String getProdCategoryTran() {
		return prodCategoryTran;
	}

	/**
	 * 処理用カテゴリを設定する。
	 *
	 * @param prodCategoryTran 処理用カテゴリ
	 */
	public void setProdCategoryTran(String prodCategoryTran) {
		this.prodCategoryTran = prodCategoryTran;
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
	 * 処理用計画を取得する。
	 *
	 * @return 処理用計画
	 */
	public String getPlanDataTran() {
		return planDataTran;
	}

	/**
	 * 処理用計画を設定する。
	 *
	 * @param planDataTran 処理用計画
	 */
	public void setPlanDataTran(String planDataTran) {
		this.planDataTran = planDataTran;
	}

	/**
	 * 送信データを取得する。
	 *
	 * @return rowIdList 送信データ
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 送信データを設定する。
	 *
	 * @param rowIdList 送信データ
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
	}

	/**
	 * 流通政策部であるかを取得する
	 *
	 * @return ryutsu 流通政策部であるか
	 */
	public boolean isRyutsu() {
		return ryutsu;
	}

	/**
	 * 流通政策部であるかを設定する。
	 *
	 * @param ryutsu 流通政策部であるか
	 */
	public void setRyutsu(boolean ryutsu) {
		this.ryutsu = ryutsu;
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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.tmsTytenName = null;
		this.tmsTytenCdPart = null;
		this.prodCategory = null;
		this.prodCode = null;
		this.planData = null;
		this.titleUH = "";
		this.titleP = "";
		this.titleZ = "";
	}
}
