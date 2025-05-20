package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.FreeIndexDto;
import jp.co.takeda.dto.FreeIndexResultDto;
import jp.co.takeda.dto.FreeIndexUpdateDto;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps301C02((医)フリー項目編集画面)のフォームクラス
 *
 * @author nozaki
 */
public class Dps301C02Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * Dps301C02_DATA_R
	 */
	public static final BoxKey DPS301C02_DATA_R = new BoxKeyPerClassImpl(Dps301C02Form.class, FreeIndexResultDto.class);

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 検索結果の存在判定フラグ
	 *
	 * <pre>
	 * 検索アクション後にセットする。
	 * TRUE=検索結果あり<br>
	 * FALSE=検索結果なし(ヒット件数0)
	 * </pre>
	 */
	private boolean existSearchDataFlag;

	/**
	 * GRID行の識別IDリスト
	 *
	 * <pre>
	 * 行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=選択された品目固定コード
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * ヘッダ（UH）
	 */
	private String headerUh;

	/**
	 * ヘッダ（P）
	 */
	private String headerP;

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * @param 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 *
	 * @param 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 検索結果の存在判定フラグを取得する。
	 *
	 * @return existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public boolean getExistSearchDataFlag() {
		return existSearchDataFlag;
	}

	/**
	 * 検索結果の存在判定フラグを設定する。
	 *
	 * @param existSearchDataFlag 検索結果の存在判定フラグ
	 */
	public void setExistSearchDataFlag(boolean existSearchDataFlag) {
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
	 * ヘッダ(UH)を取得する。
	 *
	 * @return headerUh  ヘッダ(UH)
	 */
	public String getHeaderUh() {
		return headerUh;
	}

	/**
	 * ヘッダ(UH)を設定する。
	 *
	 * @param headerUh  ヘッダ(UH)
	 */
	public void setHeaderUh(String headerUh) {
		this.headerUh = headerUh;
	}

	/**
	 * ヘッダ(P)を取得する。
	 *
	 * @return headerP  ヘッダ(P)
	 */
	public String getHeaderP() {
		return headerP;
	}

	/**
	 * ヘッダ(P)を設定する。
	 *
	 * @param headerP  ヘッダ(P)
	 */
	public void setHeaderP(String headerP) {
		this.headerP = headerP;
	}

	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * ActionForm変換処理 <br>
	 * 画面から入力されたデータを、更新用営業所計画DTOに変換する。
	 *
	 * @return 更新用フリー項目DTOのリスト
	 */
	public FreeIndexUpdateDto getUpdateFreeIndexDto() throws Exception {

		// 更新用DTOのリスト作成
		List<FreeIndexDto> indexFreeDtoList = new ArrayList<FreeIndexDto>();
		if (rowIdList != null) {

			for (int i = 0; i < rowIdList.length; i++) {

				if (rowIdList[i] == null) {
					continue;
				}

				Long _seqKey = null;
				Integer _jgiNo = null;
				String _jgiName = null;
				String _prodCode = null;
				String _shokushuName = null;
				Long _indexFreeValueUh1 = null;
				Long _indexFreeValueUh2 = null;
				Long _indexFreeValueUh3 = null;
				Long _indexFreeValueP1 = null;
				Long _indexFreeValueP2 = null;
				Long _indexFreeValueP3 = null;
				Date _upDate = null;

				String[] rowId = rowIdList[i].split(",", 10);

				// シーケンスキー
				if (ValidationUtil.isLong(rowId[0])) {
					_seqKey = Long.parseLong(rowId[0]);
				}
				// 従業員番号
				if (ValidationUtil.isInteger(rowId[1])) {
					_jgiNo = Integer.parseInt(rowId[1]);
				}
				// 品目固定コード
				if (!StringUtils.isEmpty(rowId[2])) {
					_prodCode = rowId[2];
				}
				// 最終更新日
				if (ValidationUtil.isLong(rowId[3])) {
					_upDate = new Date(Long.parseLong(rowId[3]));
				}
				// フリー項目UH1
				if (ValidationUtil.isLong(rowId[4])) {
					_indexFreeValueUh1 = Long.parseLong(rowId[4]);
				}
				// フリー項目UH2
				if (ValidationUtil.isLong(rowId[5])) {
					_indexFreeValueUh2 = Long.parseLong(rowId[5]);
				}
				// フリー項目UH3
				if (ValidationUtil.isLong(rowId[6])) {
					_indexFreeValueUh3 = Long.parseLong(rowId[6]);
				}
				// フリー項目P1
				if (ValidationUtil.isLong(rowId[7])) {
					_indexFreeValueP1 = Long.parseLong(rowId[7]);
				}
				// フリー項目P2
				if (ValidationUtil.isLong(rowId[8])) {
					_indexFreeValueP2 = Long.parseLong(rowId[8]);
				}
				// フリー項目P3
				if (ValidationUtil.isLong(rowId[9])) {
					_indexFreeValueP3 = Long.parseLong(rowId[9]);
				}

				FreeIndexDto freeIndexDto = new FreeIndexDto(_seqKey, _jgiNo, _jgiName, _prodCode, _indexFreeValueUh1, _indexFreeValueUh2, _indexFreeValueUh3, _indexFreeValueP1,
					_indexFreeValueP2, _indexFreeValueP3, _upDate, _shokushuName);

				// リストに追加
				indexFreeDtoList.add(freeIndexDto);
			}
		}
		return new FreeIndexUpdateDto(prodCode, prodName, indexFreeDtoList);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
	}
}
