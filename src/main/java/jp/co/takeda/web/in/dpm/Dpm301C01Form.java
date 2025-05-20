package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.ManageWsPlanForVacEntryDto;
import jp.co.takeda.dto.ManageWsPlanForVacProdDto;
import jp.co.takeda.dto.ManageWsPlanForVacProdHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacProdScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dpm301C01((医)特約店品目別計画編集画面)のフォームクラス
 * 
 * @author siwamoto
 */
public class Dpm301C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPM301C01_DATA_R
	 */
	public static final BoxKey DPM301C01_DATA_R = new BoxKeyPerClassImpl(Dpm301C01Form.class, ManageWsPlanForVacProdDto.class);

	/**
	 * DPM301C01_INPUT_DATA_R
	 */
	public static final BoxKey DPM301C01_INPUT_DATA_R = new BoxKeyPerClassImpl(Dpm301C01Form.class, ManageWsPlanForVacProdHeaderDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM301C01_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.tmsTytenNameTran = this.tmsTytenName;
		this.tmsTytenCdPartTran = this.tmsTytenCdPart;
		this.planDataTran = this.planData;
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.tmsTytenName = this.tmsTytenNameTran;
		this.tmsTytenCdPart = this.tmsTytenCdPartTran;
		this.planData = this.planDataTran;
	}

	/**
	 * ActionForm⇒条件Dto 変換処理
	 * 
	 * @return 変換された条件Dto
	 * 
	 */
	public ManageWsPlanForVacProdScDto convertScDto() throws Exception {
		if (StringUtils.isEmpty(this.tmsTytenCdPartTran)) {
			this.tmsTytenCdPartTran = null;
		}
		PlanData _planData = null;
		if (StringUtils.isEmpty(this.planDataTran)) {
			_planData = null;
		} else {
			_planData = PlanData.getInstance(planDataTran);
		}
		return new ManageWsPlanForVacProdScDto(tmsTytenCdPartTran, _planData);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 * 
	 * @return SosPlanUpdateDto 変換されたUpdateDto
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

				// 施設区分UHのDTO作成
				if (!isSame) {
					// [UH] シーケンスキー
					final Long seqKeyUH = ConvertUtil.parseLong(rowId[2]);

					// [UH] 最終更新日時
					final Date upDateUH = ConvertUtil.parseDate(rowId[3]);

					// [UH] 更新前計画値
					final Long yBaseValueUHBefore = ConvertUtil.parseLong(rowId[4]);

					// [UH] 更新後計画値
					final Long yBaseValueUHAfter = ConvertUtil.parseLong(rowId[5]);

					final ManageWsPlanForVacEntryDto updateDto = new ManageWsPlanForVacEntryDto(prodCode, tmsTytenCd, seqKeyUH, upDateUH, yBaseValueUHBefore, yBaseValueUHAfter);
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
		this.planData = null;
	}
}
