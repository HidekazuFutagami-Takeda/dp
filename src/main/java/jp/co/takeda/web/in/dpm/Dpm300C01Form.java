package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dpm300C01((ワ)特約店別計画編集画面)のフォームクラス
 * 
 * @author khashimoto
 */
public class Dpm300C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPM300C01_DATA_R
	 */
	public static final BoxKey DPM300C01_DATA_R = new BoxKeyPerClassImpl(Dpm300C01Form.class, ManageWsPlanForVacDto.class);

	/**
	 * DPM300C01_INPUT_DATA_R
	 */
	public static final BoxKey DPM300C01_INPUT_DATA_R = new BoxKeyPerClassImpl(Dpm300C01Form.class, ManageWsPlanForVacHeaderDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM300C01_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 計画立案レベル・特約店
	 */
	public static final ProdPlanLevel DPM300C01_PLANLEVEL_WS = ProdPlanLevel.WS;

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.tmsTytenNameTran = this.tmsTytenName;
		this.tmsTytenCdPartTran = this.tmsTytenCdPart;
		this.prodCodeTran = this.prodCode;
		this.planDataTran = this.planData;
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.tmsTytenName = this.tmsTytenNameTran;
		this.tmsTytenCdPart = this.tmsTytenCdPartTran;
		this.prodCode = this.prodCodeTran;
		this.planData = this.planDataTran;
	}

	/**
	 * ActionForm⇒条件Dto 変換処理
	 * 
	 * @return 変換された条件Dto
	 * 
	 */
	public ManageWsPlanForVacScDto convertScDto() throws Exception {
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
	 * ActionForm ⇒ UpdateDto 変換処理
	 * 
	 * @return ManageWsPlanEntryDto 変換されたUpdateDto
	 */
	public List<ManageWsPlanForVacEntryDto> convertEntryDto() throws Exception {
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
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 計画
	 */
	private String planData;

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
	 * 処理用品目固定コード
	 */
	private String prodCodeTran;

	/**
	 * 処理用計画
	 */
	private String planDataTran;

	/**
	 * 追加・更新行の情報リスト
	 * 
	 * <ul>
	 * <li>Sprit文字列[0]=品目固定コード</li>
	 * <li>Sprit文字列[1]=特約店コード</li>
	 * <li>Sprit文字列[2]=シーケンスキー</li>
	 * <li>Sprit文字列[3]=最終更新日時</li>
	 * <li>Sprit文字列[4]=T価ベース 更新前</li>
	 * <li>Sprit文字列[5]=T価ベース 更新後（入力値）</li>
	 * </ul>
	 */
	private String[] rowIdList;

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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.tmsTytenName = null;
		this.tmsTytenCdPart = null;
		this.prodCode = null;
		this.planData = null;
	}
}
