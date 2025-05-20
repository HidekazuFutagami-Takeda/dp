package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.ExceptDistInsUpdateDto;
import jp.co.takeda.dto.ExceptDistInsUpdateInitDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps201C02((医)配分除外施設編集画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dps201C02Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS201C00_DATA_R
	 */
	public static final BoxKey DPS201C02_DATA_R = new BoxKeyPerClassImpl(Dps201C02Form.class, ExceptDistInsUpdateInitDto.class);
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）
//	public static final BoxKey DPS201C02_ONC_SOS_FLG = new BoxKeyPerClassImpl(Dps201C02Form.class, Boolean.class);
	public static final BoxKey DPS201C02_SOS_MST = new BoxKeyPerClassImpl(Dps201C02Form.class, SosMst.class);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPS201C02_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm⇒EntryDto 変換処理
	 *
	 * @return InsMstEntryDto 変換されたEntryDto
	 */
	public ExceptDistInsUpdateDto convertExceptDistInsUpdateDto() throws Exception {

		// 最終更新日
		Date _upDate = ConvertUtil.parseDate(upDate);

		// 従業員番号
		Integer _jgiNo = ConvertUtil.parseInteger(jgiNo);

		// 品目コードリスト
		if (prodFlg.equals("1")) {
			//試算除外フラグ、配分除外フラグ対応で不要になったため削除
//			List<String> _prodCode = new ArrayList<String>();
//			for (String prod : estimationProdCode) {
//				_prodCode.add(prod);
//			}
			//試算除外フラグが選択された品目と配分除外フラグが選択された品目をマージ
			String strEstimationFlg = null;
			String strExceptFlg = null;
			List<ExceptProd> resultDtoList = new ArrayList<ExceptProd>();
			//試算除外フラグが選択された品目
			for (String estProdCd : estimationProdCode) {
				if (estProdCd.trim().length() == 0) {
					//試算除外フラグを選択していない
					break;
				}
				//試算除外フラグだけ選択された品目
				strEstimationFlg = "1";
				strExceptFlg = "0";
				for (String excprodCd : exceptProdCode) {
					if (excprodCd.trim().length() == 0) {
						//配分除外フラグを選択していない
						break;
					}
					//試算除外フラグが選択された品目と配分除外フラグが選択された品目が同じ
					if (estProdCd.equals(excprodCd)) {
						//配分除外フラグを登録する
						strExceptFlg = "1";
						break;
					}
				}
				ExceptProd resultDto = new ExceptProd(estProdCd, strEstimationFlg, strExceptFlg);
				resultDtoList.add(resultDto);
			}
			//配分除外フラグが選択された品目
			Boolean dupliProdCdFlg = false;
			for (String excprodCd : exceptProdCode) {
				if (excprodCd.trim().length() == 0) {
					//配分除外フラグを選択していない
					break;
				}
				dupliProdCdFlg = false;
				for (String estProdCd : estimationProdCode) {
					if (estProdCd.trim().length() == 0) {
						//試算除外フラグを選択していない
						break;
					}
					//試算除外フラグが選択された品目と配分除外フラグが選択された品目が同じ
					if (excprodCd.equals(estProdCd)) {
						//配分除外フラグを登録しない
						dupliProdCdFlg = true;
						break;
					}
				}
				if (dupliProdCdFlg) {
					continue;
				}
				//配分除外フラグだけ選択された品目
				strEstimationFlg = "0";
				strExceptFlg = "1";
				ExceptProd resultDto = new ExceptProd(excprodCd, strEstimationFlg, strExceptFlg);
				resultDtoList.add(resultDto);
			}
			ExceptDistInsUpdateDto entryDto = new ExceptDistInsUpdateDto(sosCd, insNo, _jgiNo, _upDate, category, resultDtoList);
			return entryDto;
		} else {
			ExceptDistInsUpdateDto entryDto = new ExceptDistInsUpdateDto(sosCd, insNo, _jgiNo, _upDate, category, exclusionFlg);
			return entryDto;
		}
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 試算除外フラグが選択された品目コード
	 */
	private String[] estimationProdCode;

	/**
	 * 配分除外フラグが選択された品目コード
	 */
	private String[] exceptProdCode;

	/**
	 * 最終更新日
	 */
	private String upDate;

	/**
	 * 表示用従業員名
	 */
	private String dispJgiName;

	/**
	 * 組織コード（営業所）
	 */
	private String sosCd;

	/**
	 * 指定方法フラグ
	 *
	 * <pre>
	 * 0 配分除外施設
	 * 1 配分除外品目
	 * </pre>
	 */
	private String prodFlg;

	/**
	 * 組織のカテゴリ
	 */
	private String[] category;

	/**
	 * 汎用マスタのカテゴリリスト
	 */
	private List<DpsCCdMst> cdMstCategoryList;

	/**
	 * 施設にて選択された除外フラグ
	 * exclusionFlg[0] 試算除外フラグ
	 * exclusionFlg[1] 配分除外フラグ
	 * <pre>
	 * 0 選択なし
	 * 1 選択あり
	 * </pre>
	 */
	private String[] exclusionFlg;

	private String insType;

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
	 * param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
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
	 * 試算除外フラグが選択された品目コードを取得する。
	 *
	 * @return estimationProdCode
	 */
	public String[] getEstimationProdCode() {
		return estimationProdCode;
	}

	/**
	 * 試算除外フラグが選択された品目コードを設定する。
	 *
	 * @param estimationProdCode
	 */
	public void setEstimationProdCode(String[] estimationProdCode) {
		this.estimationProdCode = estimationProdCode;
	}

	/**
	 * 配分除外フラグが選択された品目コードを取得する。
	 *
	 * @return exceptProdCode
	 */
	public String[] getExceptProdCode() {
		return exceptProdCode;
	}

	/**
	 * 配分除外フラグが選択された品目コードを設定する。
	 *
	 * @param exceptProdCode
	 */
	public void setExceptProdCode(String[] exceptProdCode) {
		this.exceptProdCode = exceptProdCode;
	}

	/**
	 * 指定方法フラグを取得する。
	 *
	 * @return prodFlg
	 */
	public String getProdFlg() {
		return prodFlg;
	}

	/**
	 * 指定方法フラグを設定する。
	 *
	 * @param prodFlg
	 */
	public void setProdFlg(String prodFlg) {
		this.prodFlg = prodFlg;
	}

	/**
	 * 最終更新日を取得する。
	 *
	 * @return 最終更新日
	 */
	public String getUpDate() {
		return upDate;
	}

	/**
	 * 最終更新日を設定する。
	 *
	 * @param upDate 最終更新日
	 */
	public void setUpDate(String upDate) {
		this.upDate = upDate;
	}

	/**
	 * 表示用担当者名を取得する
	 * @return dispJgiName
	 */
	public String getDispJgiName() {
		return dispJgiName;
	}

	/**
	 * 表示用担当者名を返却する
	 * @param dispJgiName
	 */
	public void setDispJgiName(String dispJgiName) {
		this.dispJgiName = dispJgiName;
	}

	/**
	 * 組織コードを取得する
	 * @return sosCd
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織コードを返却する
	 * @param sosCd
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * 組織のカテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String[] getCategory() {
		return category;
	}

	/**
	 * 組織のカテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String[] category) {
		this.category = category;
	}

	/**
	 * 汎用マスタのカテゴリリストを取得する。
	 *
	 * @return カテゴリリスト
	 */
	public List<DpsCCdMst> getCdMstCategoryList() {
		return cdMstCategoryList;
	}

	/**
	 * 汎用マスタのカテゴリリストを設定する。
	 *
	 * @param category カテゴリリスト
	 */
	public void setCdMstCategoryList(List<DpsCCdMst> cdMstCategoryList) {
		this.cdMstCategoryList = cdMstCategoryList;
	}

	/**
	 * 選択された除外フラグを取得する。
	 *
	 * @return 除外フラグ
	 */
	public String[] getExclusionFlg() {
		return exclusionFlg;
	}

	/**
	 * 選択された除外フラグを設定する。
	 *
	 * @param exclusionFlg 除外フラグ
	 */
	public void setExclusionFlg(String[] exclusionFlg) {
		this.exclusionFlg = exclusionFlg;
	}

	/**
	 * @return insType
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * @param insType セットする insType
	 */
	public void setInsType(String insType) {
		this.insType = insType;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.estimationProdCode = null;
		this.exceptProdCode = null;
		this.prodFlg = null;
		this.upDate = null;
		this.category = null;
		this.exclusionFlg = null;
	}
}
