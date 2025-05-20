package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacUpdateDto;
import jp.co.takeda.dto.InsProdPlanForVacResultDto;
import jp.co.takeda.dto.InsProdPlanForVacScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dpm201C01((ワ)施設品目別計画編集画面)のフォームクラス
 * 
 * @author stakeuchi
 */
public class Dpm201C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM201C01_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dpm201C01Form.class, InsProdPlanForVacResultDto.class);

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPM201C01_DATA_R_HEADER = new BoxKeyPerClassImpl(Dpm201C01Form.class, InsPlanForVacHeaderDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM201C01_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・施設
	 */
	public static final ProdPlanLevel DPM201C01_PLANLEVEL_INS = ProdPlanLevel.INS;

	// -------------------------------
	// 部品
	// -------------------------------
	/**
	 * 重点先/一般先
	 */
	public static final List<CodeAndValue> ACTIVITY_TYPES;

	/**
	 * static initilizer
	 */
	static {
		List<CodeAndValue> tmp = null;
		// 重点先/一般先にセット
		tmp = new ArrayList<CodeAndValue>(3);
		tmp.add(new CodeAndValue("1", "重点先"));
		tmp.add(new CodeAndValue("2", "一般先"));
		ACTIVITY_TYPES = Collections.unmodifiableList(tmp);
	}

	/**
	 * [プルダウンリスト]重点先/一般先を取得する。
	 * 
	 * @return [プルダウンリスト]重点先/一般先
	 */
	public List<CodeAndValue> getActivityTypes() {
		return ACTIVITY_TYPES;
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
	 * 雑組織フラグ
	 */
	private boolean etcSosFlg;

	/**
	 * 従業員番号
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
	 * 重点先/一般先
	 */
	private String activityType;

	/**
	 * JIS府県コード
	 */
	private String addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private String addrCodeCity;

	/**
	 * 府県名（漢字）
	 */
	private String fukenMeiKj;

	/**
	 * 市区郡町村名（漢字）
	 */
	private String shikuchosonMeiKj;

	/**
	 * 計画データ
	 */
	private String planData;

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
	 * 処理用組織コード(従業員番号)
	 */
	private String jgiNoTran;

	/**
	 * 処理用組織コード(計画)
	 */
	private String planDataTran;

	/**
	 * 処理用組織コード(施設コード)
	 */
	private String insNoTran;

	/**
	 * 処理用雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 追加・更新行の情報リスト
	 * 
	 * <ul>
	 * <li>Sprit文字列[0]=施設コード</li>
	 * <li>Sprit文字列[1]=活動区分</li>
	 * <li>Sprit文字列[2]=JIS府県コード</li>
	 * <li>Sprit文字列[3]=JIS市区町村コード</li>
	 * <li>Sprit文字列[4]=品目コード</li>
	 * <li>Sprit文字列[5]=シーケンスキー</li>
	 * <li>Sprit文字列[6]=最終更新日時</li>
	 * <li>Sprit文字列[7]=Y価ベース 更新前</li>
	 * <li>Sprit文字列[8]=Y価ベース 更新後</li>
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
	 * @return insName 施設名
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
	 * 重点先/一般先を取得する。
	 * 
	 * @return activityType 重点先/一般先
	 */
	public String getActivityType() {
		return activityType;
	}

	/**
	 * 重点先/一般先を設定する。
	 * 
	 * @param activityType 重点先/一般先
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * JIS府県コードを取得する。
	 * 
	 * @return JIS府県コード
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS府県コードを設定する。
	 * 
	 * @param addrCodePref JIS府県コード
	 */
	public void setAddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * JIS市区町村コードを取得する。
	 * 
	 * @return JIS市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * JIS市区町村を設定する。
	 * 
	 * @param addrCodeCity JIS市区町村
	 */
	public void setAddrCodeCity(String addrCodeCity) {
		this.addrCodeCity = addrCodeCity;
	}

	/**
	 * 府県名（漢字）を取得する。
	 * 
	 * @return fukenMeiKj 府県名（漢字）
	 */
	public String getFukenMeiKj() {
		return fukenMeiKj;
	}

	/**
	 * 府県名（漢字）を設定する。
	 * 
	 * @param fukenMeiKj 府県名（漢字）
	 */
	public void setFukenMeiKj(String fukenMeiKj) {
		this.fukenMeiKj = fukenMeiKj;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 市区郡町村名（漢字）を設定する。
	 * 
	 * @param shikuchosonMeiKj 市区郡町村名（漢字
	 */
	public void setShikuchosonMeiKj(String shikuchosonMeiKj) {
		this.shikuchosonMeiKj = shikuchosonMeiKj;
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

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 * 
	 * @return InsProdPlanScDto 変換されたScDto
	 */
	public InsProdPlanForVacScDto convertInsProdPlanScDto() throws Exception {

		// 施設コード
		if (StringUtils.isEmpty(insNoTran)) {
			this.insNoTran = null;
		}
		// 計画検索範囲
		PlanData _planDataTran = null;
		if (StringUtils.isNotEmpty(this.planDataTran)) {
			_planDataTran = PlanData.getInstance(this.planDataTran);
		} else {
			this.planDataTran = null;
		}

		return new InsProdPlanForVacScDto(insNoTran, _planDataTran);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 * 
	 * @return InsProdPlanUpdateDto 変換されたUpdateDto
	 */

	public List<InsPlanForVacUpdateDto> convertInsPlanUpdateDto() throws Exception {
		List<InsPlanForVacUpdateDto> updateDtoList = new ArrayList<InsPlanForVacUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 計画値に変更がある場合のみ更新
				if (StringUtils.equals(rowId[4], rowId[5])) {
					continue;
				}
				// 施設コード
				final String insNo = rowId[0];
				// 品目コード
				final String prodCode = rowId[1];
				// シーケンスキー
				final Long seqKey = ConvertUtil.parseLong(rowId[2]);
				// 最終更新日時
				final Date upDate = ConvertUtil.parseDate(rowId[3]);
				// 更新前計画値
				final Long bBaseValueBefore = ConvertUtil.parseLong(rowId[4]);
				// 更新後計画値
				final Long bBaseValueAfter = ConvertUtil.parseLong(rowId[5]);
				// 更新用DTO生成
				final InsPlanForVacUpdateDto updateDto = new InsPlanForVacUpdateDto(insNo, prodCode, seqKey, upDate, bBaseValueBefore, bBaseValueAfter);
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
	public void convertHeaderDto(InsPlanForVacHeaderDto headerDto) {

		this.etcSosFlg = false;

		if (headerDto == null) {
			// insNoはそのまま
			jgiNo = null;
			insName = null;
			activityType = null;
			addrCodePref = null;
			addrCodeCity = null;
			fukenMeiKj = null;
			shikuchosonMeiKj = null;
		} else {
			insNo = headerDto.getInsNo();
			insName = headerDto.getInsName();
			jgiNo = null;
			if (headerDto.getJgiNo() != null) {
				jgiNo = headerDto.getJgiNo().toString();
			}
			activityType = null;
			if (headerDto.getActivityType() != null) {
				activityType = headerDto.getActivityType().getDbValue();
			}
			addrCodePref = null;
			if (headerDto.getAddrCodePref() != null) {
				addrCodePref = headerDto.getAddrCodePref().getDbValue();
			}
			addrCodeCity = headerDto.getAddrCodeCity();
			shikuchosonMeiKj = headerDto.getShikuchosonMeiKj();
			fukenMeiKj = headerDto.getFukenMeiKj();
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
		setActivityType(null);
		setAddrCodePref(null);
		setAddrCodeCity(null);
		setFukenMeiKj(null);
		setShikuchosonMeiKj(null);
		setPlanData("0");
	}
}
