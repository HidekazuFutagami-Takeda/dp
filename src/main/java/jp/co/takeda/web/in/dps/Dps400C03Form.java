package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.InsWsDistForVacProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dps400C03((ワ)施設特約店別計画配分対象品目一覧画面)のフォームクラス
 * 
 * @author stakeuchi
 */
public class Dps400C03Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS400C03_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps400C03Form.class, InsWsDistForVacProdResultDto.class);

	/**
	 * 編集権限((ワ)施設特約店別計画配分処理)
	 */
	public static final DpAuthority DPS400C03_PLAN_HAIBUN_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 * 
	 * @return InsWsDistProdUpdateDto 変換されたUpdateDtoのリスト
	 */
	public List<InsWsDistProdUpdateDto> convertInsWsDistProdUpdateDto() throws Exception {
		List<InsWsDistProdUpdateDto> updateDtoList = new ArrayList<InsWsDistProdUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				// 品目固定コード
				String prodCode = rowId[0];
				// 品目名
				String prodName = rowId[1];
				// ステータス最終更新日
				Date statusLastUpDate = null;
				if (!StringUtils.isEmpty(rowId[2])) {
					statusLastUpDate = ConvertUtil.parseDate(rowId[2]);
				}
				// 更新用DTOの生成
				InsWsDistProdUpdateDto updateDto = new InsWsDistProdUpdateDto(prodCode, prodName, statusLastUpDate);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
	}

	// -------------------------------
	// field
	// -------------------------------

	/**
	 * 配分処理と同時にMRに公開フラグ
	 * <ul>
	 * <li>TRUE =公開する</li>
	 * <li>FALSE=公開しない</li>
	 * </ul>
	 */
	private Boolean isMrOpenCheck;

	/**
	 * GRID行の識別IDリスト
	 * <ul>
	 * <li>Sprit文字列[0]=選択行の品目固定コード</li>
	 * <li>Sprit文字列[1]=選択行の品目名</li>
	 * <li>Sprit文字列[2]=選択行のステータス最終更新日</li>
	 * </ul>
	 */
	private String[] rowIdList;

	/**
	 * 配分処理と同時にMRに公開フラグを取得する。
	 * 
	 * @return isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public Boolean getIsMrOpenCheck() {
		return isMrOpenCheck;
	}

	/**
	 * 配分処理と同時にMRに公開フラグを設定する。
	 * 
	 * @param isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public void setIsMrOpenCheck(Boolean isMrOpenCheck) {
		this.isMrOpenCheck = isMrOpenCheck;
	}

	/**
	 * GRID行の識別IDリストを取得する。
	 * 
	 * @return rowIdList GRID行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return (String[]) rowIdList.clone();
	}

	/**
	 * GRID行の識別IDリストを設定する。
	 * 
	 * @param rowIdList GRID行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList.clone();
	}

	@Override
	public void reset() {
		this.isMrOpenCheck = false;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.isMrOpenCheck = false;
		this.rowIdList = null;
	}
}
