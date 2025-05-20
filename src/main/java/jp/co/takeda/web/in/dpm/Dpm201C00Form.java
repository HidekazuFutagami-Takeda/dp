package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.InsPlanUpdateDto;
import jp.co.takeda.dto.InsProdPlanResultDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdPlanScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm201C00((医)施設品目別計画編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dpm201C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM201C00_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm201C00Form.class, InsProdPlanResultDto.class);

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM201C00_DATA_R_HEADER = new BoxKeyPerClassImpl(Dpm201C00Form.class, InsProdPlanResultHeaderDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM201C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 【未使用】組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 【未使用】組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 【未使用】組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 【未使用】雑組織フラグ
	 */
	private boolean etcSosFlg;

	/**
	 * 【未使用】従業員番号
	 */
	private String jgiNo;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 施設名
	 */
	private String insName;

	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 計画検索範囲
	 */
	private String planData;

	/**
	 * 対象区分
	 */
	private String insType;

	/**
	 * ワクチンのカテゴリコード
	 */
	private String vaccineCode;

	// 処理用フィールド
	/**
	 * 【未使用】処理用組織コード(支店)
	 */
	private String sosCd2Tran;

	/**
	 * 【未使用】処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 【未使用】処理用組織コード(チーム)
	 */
	private String sosCd4Tran;

	/**
	 * 【未使用】処理用組織コード(従業員番号)
	 */
	private String jgiNoTran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 処理用計画検索範囲
	 */
	private String planDataTran;

	/**
	 * 処理用施設コード
	 */
	private String insNoTran;

	/**
	 * 【未使用】処理用雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 全MRフラグ
	 */
	private boolean mrFlg;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0]=施設コード</li>
	 * <li>Sprit文字列[1]=対象区分</li>
	 * <li>Sprit文字列[2]=品目コード</li>
	 * <li>Sprit文字列[3]=シーケンスキー</li>
	 * <li>Sprit文字列[4]=最終更新日時</li>
	 * <li>Sprit文字列[5]=Y価ベース 更新前</li>
	 * <li>Sprit文字列[6]=Y価ベース 更新後</li>
	 * </ul>
	 */
	private String[] rowIdList;

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
	 * 全MRフラグを取得する。
	 *
	 * @return mrFlg 全MRフラグ
	 */
	public boolean getMrFlg() {
		return mrFlg;
	}

	/**
	 * 全MRフラグを設定する。
	 *
	 * @param mrFlg 全MRフラグ
	 */
	public void setMrFlg(boolean mrFlg) {
		this.mrFlg = mrFlg;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 施設名を取得する。
	 *
	 * @return 施設名
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 施設名を設定する。
	 *
	 * @param insName 施設名
	 */
	public void setInsName(String insName) {
		this.insName = insName;
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
	 * 品目カテゴリリストを設定する。
	 *
	 * @param prodCategoryList 品目カテゴリリスト
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	/**
	 * 計画検索範囲を取得する。
	 *
	 * @return planData 計画検索範囲
	 */
	public String getPlanData() {
		return planData;
	}

	/**
	 * 計画検索範囲を設定する。
	 *
	 * @param planData 計画検索範囲
	 */
	public void setPlanData(String planData) {
		this.planData = planData;
	}

	/**
	 * 対象区分を取得する。
	 *
	 * @return insType 対象区分
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param insType 対象区分
	 */
	public void setInsType(String insType) {
		this.insType = insType;
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
	 * 処理用組織コード(従業員番号)を取得する。
	 *
	 * @return 処理用組織コード(従業員番号)
	 */
	public String getJgiNoTran() {
		return jgiNoTran;
	}

	/**
	 * 処理用組織コード(従業員番号)を設定する。
	 *
	 * @param jgiNoTran 処理用組織コード(従業員番号)
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
	 * 処理用施設コードを取得する。
	 *
	 * @return 処理用施設コード
	 */
	public String getInsNoTran() {
		return insNoTran;
	}

	/**
	 * 処理用施設コードを設定する。
	 *
	 * @param insNoTran 処理用施設コード
	 */
	public void setInsNoTran(String insNoTran) {
		this.insNoTran = insNoTran;
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

	//add
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
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsProdPlanScDto 変換されたScDto
	 */
	public InsProdPlanScDto convertInsProdPlanScDto() throws Exception {
		// 従業員番号
		Integer _jgiNoTran = null;
		if (StringUtils.isNotEmpty(this.jgiNoTran)) {
			_jgiNoTran = ConvertUtil.parseInteger(this.jgiNoTran);
		} else {
			this.jgiNoTran = null;
		}
		// 施設コード
		if (StringUtils.isEmpty(insNoTran)) {
			this.insNoTran = null;
		}
		// 品目カテゴリ
		String _prodCategory = null;
		if (StringUtils.isNotEmpty(this.prodCategoryTran)) {
			_prodCategory = this.prodCategoryTran;
		} else {
			this.prodCategoryTran = null;
		}
		// 計画検索範囲
		PlanData _planDataTran = null;
		if (StringUtils.isNotEmpty(this.planDataTran)) {
			_planDataTran = PlanData.getInstance(this.planDataTran);
		} else {
			this.planDataTran = null;
		}
		return new InsProdPlanScDto(_jgiNoTran, insNoTran, _prodCategory, _planDataTran, vaccineCode);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return InsProdPlanUpdateDto 変換されたUpdateDto
	 */
	public List<InsPlanUpdateDto> convertInsPlanUpdateDto() throws Exception {
		List<InsPlanUpdateDto> updateDtoList = new ArrayList<InsPlanUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 計画値に変更がある場合のみ更新
				if (StringUtils.equals(rowId[5], rowId[6])) {
					continue;
				}
				// 施設コード
				final String insNo = rowId[0];
				// 対象区分
				final InsType insType = InsType.getInstance(rowId[1]);
				// 品目コード
				final String prodCode = rowId[2];
				// シーケンスキー
				final Long seqKey = ConvertUtil.parseLong(rowId[3]);
				// 最終更新日時
				final Date upDate = ConvertUtil.parseDate(rowId[4]);
				// 更新前計画値
				final Long yBaseValueBefore = ConvertUtil.parseLong(rowId[5]);
				// 更新後計画値
				final Long yBaseValueAfter = ConvertUtil.parseLong(rowId[6]);
				// 更新用DTO生成
				final InsPlanUpdateDto updateDto = new InsPlanUpdateDto(insNo, insType, prodCode, seqKey, upDate, yBaseValueBefore, yBaseValueAfter);
				updateDtoList.add(updateDto);
			}
		}
		rowIdList = null;
		return updateDtoList;
	}

	/**
	 * headerDto ⇒ ActionForm 変換処理
	 *
	 * @param headerDto ヘッダ情報DTO
	 */
	public void convertHeaderDto(InsProdPlanResultHeaderDto headerDto) {

		this.etcSosFlg = false;

		if (headerDto == null) {
			insName = null;
			jgiNo = null;
			insType = null;

		} else {
			insNo = headerDto.getInsNo();
			insName = headerDto.getInsName();
			jgiNo = null;
			if (headerDto.getJgiNo() != null) {
				jgiNo = headerDto.getJgiNo().toString();
			}
			insType = null;
			if (headerDto.getInsType() != null) {
				insType = headerDto.getInsType().getDbValue();
			}
		}
	}

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.prodCategoryTran = this.prodCategory;
		this.planDataTran = this.planData;
		this.insNoTran = this.insNo;
		this.etcSosFlgTran = this.etcSosFlg;
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.sosCd2 = this.sosCd2Tran;
		this.sosCd3 = this.sosCd3Tran;
		this.sosCd4 = this.sosCd4Tran;
		this.jgiNo = this.jgiNoTran;
		this.prodCategory = this.prodCategoryTran;
		this.planData = this.planDataTran;
		this.insNo = this.insNoTran;
		this.etcSosFlg = this.etcSosFlgTran;
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
		setEtcSosFlg(false);
		setInsNo(null);
		setInsName(null);
		setInsType(null);
		setProdCategory("1");
		setPlanData("0");
	}
}
