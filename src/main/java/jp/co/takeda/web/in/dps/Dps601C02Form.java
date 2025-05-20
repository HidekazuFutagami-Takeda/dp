package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.InsDocPlanUpDateDto;
import jp.co.takeda.dto.InsDocPlanUpDateScDto;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dps601C02((医)施設医師別計画編集画面)のフォームクラス
 * 
 * @author siwamoto
 */
public class Dps601C02Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS601C02_DATA_R
	 */
	public static final BoxKey DPS601C02_DATA_R = new BoxKeyPerClassImpl(Dps601C02Form.class,  ArrayList.class);

	/**
	 * DPS601C02_HEAD_R
	 */
	public static final BoxKey DPS601C02_HEAD_R = new BoxKeyPerClassImpl(Dps601C02Form.class, HashMap.class);

	/**
	 * 副担当編集モード(TRUE：副担当モード、FLASE：主担当（通常）モード)
	 */
	public static final BoxKey SUB_MR_MODE = new BoxKeyPerClassImpl(Dps601C02Form.class, Boolean.class);

	/**
	 * 編集権限MMP
	 */
	public static final DpAuthority DPS601C02_EDIT_AUTH_MMP = new DpAuthority( AuthType.EDIT);

	/**
	 * 編集権限MMP
	 */
	public static final DpAuthority DPS601C02_EDIT_AUTH_MMP_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 * 
	 * @return InsPlanScDto 変換されたScDto
	 */
	public InsDocPlanUpDateScDto convertInsDocPlanUpDateScDto() throws Exception {

		// 従業員コード
		if (StringUtils.isEmpty(jgiNo)) {
			jgiNo = null;
		}
		// 品目固定コード
		if (StringUtils.isEmpty(prodCode)) {
			prodCode = null;
		}
		// 対象施設
		if (StringUtils.isEmpty(insType)) {
			insType = "1";
		}
		// 表示区分
		if (StringUtils.isEmpty(planDispType)) {
			planDispType = "0";
		}
		return new InsDocPlanUpDateScDto(jgiNo, prodCode, insType, planDispType);
	}

	/**
	 * ActionForm ⇒ upDateDto 変換処理
	 * 
	 * @return PlannedProdSpecialInsPlanUpDateDto 変換されたUpDateDto
	 * 
	 * * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param modifyRow 更新行、追加行DTOのリスト
	 */
	public List<InsDocPlanUpDateDto> convertInsDocPlanUpDateModifyDto() throws Exception {

		List<InsDocPlanUpDateDto> resultList = new ArrayList<InsDocPlanUpDateDto>();

		if (selectRowIdList != null && selectRowIdList.length > 0) {
			for (int i = 0; i < selectRowIdList.length; i++) {
				
				InsDocPlanUpDateDto result = new InsDocPlanUpDateDto();
				String[] selectRowId = ConvertUtil.splitConmma(selectRowIdList[i]);
				
				//シーケンスキー
				if (StringUtils.isNotEmpty(selectRowId[0])) {
					result.setSeqKey(ConvertUtil.parseLong(selectRowId[0]));
				}
				//計画最新更新日時
				if (StringUtils.isNotEmpty(selectRowId[1])) {
					result.setPlannedUpDate(ConvertUtil.parseDate(selectRowId[1]));
				}
				//施設コード
				if (StringUtils.isNotEmpty(selectRowId[2])) {
					result.setInsNo(selectRowId[2]);
				}
				//親施設コード
				if (StringUtils.isNotEmpty(selectRowId[3])) {
					result.setMainInsNo(selectRowId[3]);
				}
				//医師コード
				if (StringUtils.isNotEmpty(selectRowId[4])) {
					result.setDocNo(selectRowId[4]);
				}
				//配分除外フラグ
				if (StringUtils.isNotEmpty(selectRowId[5])) {
					result.setExceptDistInsFlg(ConvertUtil.parseBoolean(selectRowId[5]));
				}
				//削除施設フラグ
				if (StringUtils.isNotEmpty(selectRowId[6])) {
					result.setDelInsFlg(ConvertUtil.parseBoolean(selectRowId[6]));
				}
				//修正計画
				if (StringUtils.isNotEmpty(selectRowId[7])) {
					result.setPlannedIncValue(ConvertUtil.parseLong(selectRowId[7]));
				}
				//計画確定値
				if (StringUtils.isNotEmpty(selectRowId[8])) {
					result.setPlannedValue(ConvertUtil.parseLong(selectRowId[8]));
				}
				//従業員コード
				if (StringUtils.isNotEmpty(jgiNo)) {
					result.setJgiNo(ConvertUtil.parseInteger(jgiNo));
				}
				//品目固定コード
				if (StringUtils.isNotEmpty(prodCode)) {
					result.setProdCode(prodCode);
				}

				resultList.add(result);
			}
		}
		
		return resultList;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 従業員コード
	 */
	private String jgiNo;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 対象施設
	 */
	private String insType;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 表示区分（実績計画あり、全施設医師）
	 */
	private String planDispType;

	/**
	 * 選択行の識別IDリスト
	 * 
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=従業員コード
	 * Sprit文字列[1]=ステータス最終更新日時(Date型のgetTime()の値)
	 * (例)---------------------------------------
	 * selectRowIdList[0] = "12345,1234954688000"
	 * selectRowIdList[1] = "22432,1122843578000"
	 * -------------------------------------------
	 * </pre>
	 */
	private String[] selectRowIdList;

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
	 * 品目固定コードを取得する。
	 * 
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 * 
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 対象施設を取得する。
	 * 
	 * @return insType 対象施設
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 対象施設を設定する。
	 * 
	 * @param insType 対象施設
	 */
	public void setInsType(String insType) {
		this.insType = insType;
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
	 * 表示区分（実績計画あり、全施設医師）を取得する。
	 * @return 表示区分（実績計画あり、全施設医師）
	 */
	public String getPlanDispType() {
		return planDispType;
	}

	/**
	 * 表示区分（実績計画あり、全施設医師）を設定する。
	 * @param planDispType 表示区分（実績計画あり、全施設医師）
	 */
	public void setPlanDispType(String planDispType) {
		this.planDispType = planDispType;
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

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.insType = InsType.UH.getDbValue();
		this.insNo = null;
		this.planDispType = "0";
		this.selectRowIdList = null;
	}
}
