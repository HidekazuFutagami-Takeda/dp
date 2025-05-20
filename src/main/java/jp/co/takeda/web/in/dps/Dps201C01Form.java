package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.ExceptDistInsEntryDto;
import jp.co.takeda.dto.ExceptDistInsPlannedProdResultDto;
import jp.co.takeda.dto.ExceptDistInsScDto;
import jp.co.takeda.dto.InsMrDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.SosMst; // add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps201C01((医)配分除外施設登録画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dps201C01Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS201C00_DATA_R
	 */
	public static final BoxKey DPS201C01_DATA_R = new BoxKeyPerClassImpl(Dps201C01Form.class, ExceptDistInsPlannedProdResultDto.class);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）
//	/**
//	 * DPS201C00_DATA_R
//	 */
//	public static final BoxKey DPS201C01_ONC_SOS_FLG = new BoxKeyPerClassImpl(Dps201C00Form.class, Boolean.class);

	/**
	 * DPS201C01_DATA_R
	 */
	public static final BoxKey DPS201C01_SOS_MST = new BoxKeyPerClassImpl(Dps201C01Form.class, SosMst.class);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　ONC_SOS_FLG→SOS_MST（category,subCategory）

	/**
	 * DPS201_EXCEPT_DIST_INS
	 */
	public static final BoxKey DPS201_EXCEPT_DIST_INS = new BoxKeyPerClassImpl(Dps201C01Form.class, Boolean.class);

	/**
	 * 削除権限
	 */
	public static final DpAuthority DPS201C01_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm⇒EntryDto 変換処理
	 *
	 * @return InsMstEntryDto 変換されたEntryDto
	 */
	public ExceptDistInsEntryDto convertExceptDistInsEntryDto() throws Exception {

		// 施設/従業員情報のリスト
		List<InsMrDto> insMrList = new ArrayList<InsMrDto>();
		for (int i = 0; i < selectRowIdList.length; i++) {
			String[] selectRowId = selectRowIdList[i].split(",");

			InsMrDto insMrDto = null;
			Integer intjgiNo = 0;
			String insNo = null;
			if (prodFlg.equals("1")) {
				// 施設コード
				insNo = selectRowId[0];
				// 従業員番号
				if (selectRowId.length > 1) intjgiNo =  ConvertUtil.parseInteger(selectRowId[1]);

				//試算・配分除外品目の場合、試算・配分除外フラグをセットしない
				insMrDto = new InsMrDto(insNo, intjgiNo, null, null);

			}else {
				//試算除外フラグが選択された施設と配分除外フラグが選択された施設をマージ
				String strEstimationFlg = "0";
				String strExceptFlg = "0";
				//試算除外フラグが選択された施設
				for (String estInsCd : estimationInsCode) {
					String[] estInsCdRow = estInsCd.split(",");
					if (estInsCdRow[0].trim().length() == 0) {
						//試算除外フラグを選択していない
						break;
					}
					strEstimationFlg = "0";
					if (selectRowId[0].equals(estInsCdRow[0])) {
						insNo = estInsCdRow[0];
						strEstimationFlg = "1";
						break;
					}
				}
				//配分除外フラグが選択された施設
				for (String excInsCd : exceptInsCode) {
					String[] excInsCdRow = excInsCd.split(",");
					if (excInsCdRow[0].trim().length() == 0) {
						//配分除外フラグを選択していない
						break;
					}
					strExceptFlg = "0";
					if (selectRowId[0].equals(excInsCdRow[0])) {
						insNo = excInsCdRow[0];
						strExceptFlg = "1";
						break;
					}
				}
				if (insNo == null) {
					// 施設コード
					insNo = selectRowId[0];
					// 従業員番号
					if (selectRowId.length > 1) intjgiNo =  ConvertUtil.parseInteger(selectRowId[1]);

					//試算・配分除外フラグが選択されていない場合、試算・配分除外フラグをセットしない
					insMrDto = new InsMrDto(insNo, intjgiNo, null, null);

				}else {

					insMrDto = new InsMrDto(insNo, intjgiNo, strEstimationFlg, strExceptFlg);
				}
			}
			insMrList.add(insMrDto);

		}
		// 品目コードリスト
		if (prodFlg.equals("1")) {
			//試算除外フラグ、配分除外フラグ対応で不要になったため削除
//			List<String> prodCodeList = new ArrayList<String>();
//			for (int i = 0; i < prodCode.length; i++) {
//				prodCodeList.add(prodCode[i]);
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
			ExceptDistInsEntryDto entryDto = new ExceptDistInsEntryDto(sosCd3, insMrList, resultDtoList);

//			ExceptDistInsEntryDto entryDto = new ExceptDistInsEntryDto(sosCd3, insMrList, prodCodeList);
			return entryDto;
		} else {
			ExceptDistInsEntryDto entryDto = new ExceptDistInsEntryDto(sosCd3, insMrList);

			return entryDto;
		}
	}

	/**
	 * ActionForm⇒ScDto 変換処理
	 *
	 * @return InsMstScDto 変換されたScDto
	 */
	public ExceptDistInsScDto convertExceptDistInsScDto() throws Exception {

		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3)) {
			sosCd3 = null;
		}

		// 組織コード(チーム)
		if (StringUtils.isEmpty(sosCd4)) {
			sosCd4 = null;
		}

		// 従業員番号
		Integer iJgiNo = null;
		if (StringUtils.isNotEmpty(jgiNo)) {
			iJgiNo = Integer.valueOf(jgiNo);
		}

		//TODO　史さんへカテゴリを検索条件に追加してください。
		return new ExceptDistInsScDto(sosCd3, sosCd4, iJgiNo, null);
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 表示用従業員名
	 */
	private String dispJgiName;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 選択された組織コード
	 */
	private String initSosCodeValue;

	/**
	 * 選択された施設コード（カンマ区切り）
	 */
	private String insNoList;

	/**
	 * カテゴリ
	 */
	private String[] category;

	/**
	 * 検索用カテゴリ
	 */
	private String searchCategory;

	/**
	 * カテゴリリスト
	 */
	private List<DpsCCdMst> cdMstCategoryList;

	/**
	 *
	 * 選択行の識別IDリスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=施設コード
	 * Sprit文字列[1]=担当者従業員コード
	 * (例)---------------------------------------
	 * selectRowIdList[0] = "12345,1234954688000"
	 * selectRowIdList[1] = "22432,1122843578000"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] selectRowIdList;

	/**
	 * 選択された品目コード
	 */
	private String[] prodCode;

	/**
	 * 施設コード
	 */
	private String insNo;

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
	 * 画面で追加された施設一覧
	 */
	private String[] addInsList;

	/**
	 * 配分除外フラグが選択された施設コード
	 */
	private String[] exceptInsCode;

	/**
	 * 試算除外フラグが選択された施設コード
	 */
	private String[] estimationInsCode;

	/**
	 * 配分除外フラグが選択された品目コード
	 */
	private String[] exceptProdCode;

	/**
	 * 試算除外フラグが選択された品目コード
	 */
	private String[] estimationProdCode;

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
	 * 選択された組織コードを取得する。
	 *
	 * @return initSosCodeValue
	 */
	public String getInitSosCodeValue() {
		return initSosCodeValue;
	}

	/**
	 * 選択された組織コードを設定する。
	 *
	 * @param initSosCodeValue
	 */
	public void setInitSosCodeValue(String initSosCodeValue) {
		this.initSosCodeValue = initSosCodeValue;
	}

	/**
	 * 選択された施設コード（カンマ区切り）を取得する。
	 *
	 * @return insNoList
	 */
	public String getInsNoList() {
		return insNoList;
	}

	/**
	 * 選択された施設コード（カンマ区切り）を設定する。
	 *
	 * @param insNoList
	 */
	public void setInsNoList(String insNoList) {
		this.insNoList = insNoList;
	}

	/**
	 * 選択行の識別IDリストを取得する。
	 *
	 * @return selectRowIdList 選択行の識別IDリスト
	 */
	public String[] getSelectRowIdList() {
		return selectRowIdList;
	}

	/**
	 * 選択行の識別IDリストを設定する。
	 *
	 * @param selectRowIdList 選択行の識別IDリスト
	 */
	public void setSelectRowIdList(String[] selectRowIdList) {
		this.selectRowIdList = selectRowIdList;
	}

	/**
	 * 選択された品目コードを設定する。
	 *
	 * @param prodCode
	 */
	public void setProdCode(String[] prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 選択された品目コードを取得する。
	 *
	 * @return prodCode
	 */
	public String[] getProdCode() {
		return prodCode;
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
	 * 指定方法フラグを取得する。
	 *
	 * @return prodFlg
	 */
	public String getProdFlg() {
		return prodFlg;
	}

	/**
	 * 画面で追加された施設一覧を取得する。
	 *
	 * @return addInsList
	 */
	public String[] getAddInsList() {
		return addInsList;
	}

	/**
	 * 画面で追加された施設一覧を設定する。
	 *
	 * @param addInsList
	 */
	public void setAddInsList(String[] addInsList) {
		this.addInsList = addInsList;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String[] getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String[] category) {
		this.category = category;
	}

	/**
	 * 検索用カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getSearchCategory() {
		return searchCategory;
	}

	/**
	 * 検索要カテゴリを設定する。
	 *
	 * @param searchCategory カテゴリ
	 */
	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
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
	 * param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.selectRowIdList = null;
		this.prodCode = null;
		this.prodFlg = null;
		this.addInsList = null;
		this.category = null;
		this.estimationInsCode = null;
		this.exceptInsCode = null;
		this.estimationProdCode = null;
		this.exceptProdCode = null;
	}

	/**
	 * カテゴリリストを取得する。
	 *
	 * @return cdMstCategoryList カテゴリ
	 */
	public List<DpsCCdMst> getCdMstCategoryList() {
		return cdMstCategoryList;
	}

	/**
	 * カテゴリリストを設定する。
	 *
	 * @param cdMstCategoryList カテゴリ
	 */
	public void setCdMstCategoryList(List<DpsCCdMst> cdMstCategoryList) {
		this.cdMstCategoryList = cdMstCategoryList;
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
	 * 試算除外フラグが選択された施設コードを取得する。
	 *
	 * @return estimationInsCode
	 */
	public String[] getEstimationInsCode() {
		return estimationInsCode;
	}

	/**
	 * 試算除外フラグが選択された施設コードを設定する。
	 *
	 * @param estimationProdCode
	 */
	public void setEstimationInsCode(String[] estimationInsCode) {
		this.estimationInsCode = estimationInsCode;
	}

	/**
	 * 配分除外フラグが選択された施設コードを取得する。
	 *
	 * @return exceptInsCode
	 */
	public String[] getExceptInsCode() {
		return exceptInsCode;
	}

	/**
	 * 配分除外フラグが選択された施設コードを設定する。
	 *
	 * @param exceptInsCode
	 */
	public void setExceptInsCode(String[] exceptInsCode) {
		this.exceptInsCode = exceptInsCode;
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

}
