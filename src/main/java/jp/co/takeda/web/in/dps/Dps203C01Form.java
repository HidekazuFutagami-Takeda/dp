package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.MikakutokuSijouResultDto;
import jp.co.takeda.dto.MikakutokuSijouScDto;
import jp.co.takeda.dto.MikakutokuSijouUpdateDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps203C01((医)未獲得市場編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps203C01Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS203C01_DATA_R
	 */
	public static final BoxKey DPS203C01_DATA_R = new BoxKeyPerClassImpl(Dps203C01Form.class, MikakutokuSijouResultDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPS203C01_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return 変換されたScDto
	 */
	public MikakutokuSijouScDto convertMikakutokuSijouScDto() throws Exception {
		if (StringUtils.isEmpty(sosCd3)) {
			sosCd3 = null;
		}
		if (StringUtils.isEmpty(yakkouSijouCode)) {
			yakkouSijouCode = null;
		}
		return new MikakutokuSijouScDto(sosCd3, yakkouSijouCode);
	}

	/**
	 * ActionForm⇒UpdateDto 変換処理
	 *
	 * @return 変換されたUpdateDtoのリスト
	 */
	public List<MikakutokuSijouUpdateDto> convertMikakutokuSijouUpdateDto() throws Exception {
		// 更新用DTOのリスト作成
		List<MikakutokuSijouUpdateDto> updateDtoList = new ArrayList<MikakutokuSijouUpdateDto>();
		if (rowIdList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
				// 検索時の増減金額と更新時の増減金額が同じ場合は更新処理を行わない
				if (StringUtils.equals(rowId[3], rowId[4])) {
					continue;
				}
				// シーケンスキー
				Long seqKey = null;
				if (ValidationUtil.isLong(rowId[0])) {
					seqKey = Long.valueOf(rowId[0]);
				}
				// 最終更新日時
				Date upDate = null;
				if (ValidationUtil.isLong(rowId[1])) {
					upDate = new Date(Long.valueOf(rowId[1]));
				}
				// 検索時の未獲得市場
				Long yakkouSijouMikakutoku = null;
				if (ValidationUtil.isLong(rowId[2])) {
					yakkouSijouMikakutoku = Long.valueOf(rowId[2]);
				}
				// 更新時の増減金額
				Long modifyAmount = null;
				if (ValidationUtil.isLong(rowId[4])) {
					// 千円単位で入力されているため変換してセット
					modifyAmount = ConvertUtil.parseMoneyToNormalUnit(Long.valueOf(rowId[4]));
				}
				MikakutokuSijouUpdateDto updateDto = new MikakutokuSijouUpdateDto(seqKey, upDate, yakkouSijouMikakutoku, modifyAmount);
				updateDtoList.add(updateDto);
			}
		}
		return updateDtoList;
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
	 * 従業員コード
	 */
	private String jgiNo;

	/**
	 * 薬効市場コード
	 */
	private String yakkouSijouCode;

	/**
	 * 親ウィンドウの検索関数名
	 */
	private String searchFuncName;

	/**
	 * 検索結果の存在判定フラグ
	 *
	 * <pre>
	 * 検索アクション後にセットします。
	 * TRUE=検索結果あり<br>
	 * FALSE=検索結果なし(ヒット件数0)
	 * </pre>
	 */
	private Boolean existSearchDataFlag;

	/**
	 * GRID行の識別IDリスト
	 *
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=シーケンスキー
	 * Sprit文字列[1]=最終更新日時(Date型のgetTime()の値)
	 * Sprit文字列[2]=検索時の未獲得市場
	 * Sprit文字列[3]=検索時の増減金額
	 * Sprit文字列[4]=編集後の増減金額
	 * </pre>
	 */
	private String[] rowIdList;

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
	 * 従業員コードを取得する。
	 *
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員コードを設定する。
	 *
	 * @param jgiNo
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 薬効市場コードを取得する。
	 *
	 * @return yakkouSijouCode 薬効市場コード
	 */
	public String getYakkouSijouCode() {
		return yakkouSijouCode;
	}

	/**
	 * 薬効市場コードを設定する。
	 *
	 * @param yakkouSijouCode 薬効市場コード
	 */
	public void setYakkouSijouCode(String yakkouSijouCode) {
		this.yakkouSijouCode = yakkouSijouCode;
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
	 *親ウィンドウの検索関数名を設定する。
	 *
	 * @param searchFuncName 親ウィンドウの検索関数名
	 */
	public void setSearchFuncName(String searchFuncName) {
		this.searchFuncName = searchFuncName;
	}

	/**
	 * 検索結果の存在判定フラグを取得する。
	 *
	 * @return existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public Boolean getExistSearchDataFlag() {
		return existSearchDataFlag;
	}

	/**
	 * 検索結果の存在判定フラグを設定する。
	 *
	 * @param existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public void setExistSearchDataFlag(Boolean existSearchDataFlag) {
		this.existSearchDataFlag = existSearchDataFlag;
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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.existSearchDataFlag = false;
		this.rowIdList = null;
	}
}
