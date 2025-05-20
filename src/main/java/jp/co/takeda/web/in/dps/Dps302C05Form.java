package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.MrPlanForVacResultDto;
import jp.co.takeda.dto.MrPlanForVacUpdateDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dps302C05((ワ)担当者別計画編集画面(担当者別入力))のフォームクラス
 * 
 * @author stakeuchi
 */
public class Dps302C05Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS302C05_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps302C05Form.class, MrPlanForVacResultDto.class);

	/**
	 * 検索結果有無取得キー(TRUE:1件以上,FALSE:0件)
	 */
	public static final BoxKey DPS302C05_DATA_R_SEARCH_RESULT_EXIST = new BoxKeyPerClassImpl(Dps302C05Form.class, Boolean.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPS302C05_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm⇒UpdateDto 変換処理
	 * 
	 * @return 変換されたUpdateDtoのリスト
	 */
	public List<MrPlanForVacUpdateDto> convertMrPlanForVacUpdateDto() throws Exception {
		List<MrPlanForVacUpdateDto> updateDtoList = new ArrayList<MrPlanForVacUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 検索時の計画値と更新時の計画値が同じ場合は更新処理を行わない
				if (StringUtils.equals(rowId[2], rowId[3])) {
					continue;
				}
				// シーケンスキー
				Long seqKey = ConvertUtil.parseLong(rowId[0]);
				// 最終更新日時
				Date upDate = ConvertUtil.parseDate(rowId[1]);
				// 更新時の計画値
				Long plannedValue = ConvertUtil.parseLong(rowId[3]);
				MrPlanForVacUpdateDto updateDto = new MrPlanForVacUpdateDto(seqKey, upDate, plannedValue);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 品目コード
	 */
	private String prodCode;

	/**
	 * 親ウィンドウの検索関数名
	 */
	private String searchFuncName;

	/**
	 * 更新行の識別IDリスト
	 * 
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=(ワ)担当者別計画 シーケンスキー
	 * Sprit文字列[1]=(ワ)担当者別計画 最終更新日時(Date型のgetTime()の値)
	 * Sprit文字列[2]=(ワ)担当者別計画 検索時の計画値(B)
	 * Sprit文字列[3]=(ワ)担当者別計画 編集後の計画値(B)
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * 品目コードを取得する。
	 * 
	 * @return prodCode 品目コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目コードを設定する。
	 * 
	 * @param prodCode 品目コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 親ウィンドウの検索関数名を取得する。
	 * 
	 * @return searchFuncName 親ウィンドウの検索関数名
	 */
	public String getSearchFuncName() {
		return searchFuncName;
	}

	/**
	 * 親ウィンドウの検索関数名を設定する。
	 * 
	 * @param searchFuncName 親ウィンドウの検索関数名
	 */
	public void setSearchFuncName(String searchFuncName) {
		this.searchFuncName = searchFuncName;
	}

	/**
	 * 更新行の識別IDリストを取得する。
	 * 
	 * @return rowIdList 更新行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return (String[]) rowIdList.clone();
	}

	/**
	 * 更新行の識別IDリストを設定する。
	 * 
	 * @param rowIdList 更新行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList.clone();
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.rowIdList = null;
	}
}
