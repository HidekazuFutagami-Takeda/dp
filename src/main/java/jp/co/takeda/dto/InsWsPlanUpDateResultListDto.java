package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.InsPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.util.ConvertUtil;

/**
 * 施設特約店別計画編集画面の検索結果用DTO（画面）
 *
 * @author siwamoto
 */
public class InsWsPlanUpDateResultListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目カテゴリ
	 */
	private final String category;

	/**
	 * 品目名称
	 */
	private final String prodName;

	/**
	 * 品目区分
	 */
	private final String prodType;

	/**
	 * 重点品目フラグ
	 */
	private final boolean planLevelInsDoc;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * 最終更新者
	 */
	private final String upJgiName;

	/**
	 * 最終更新日
	 */
	private final Date upDate;

	/**
	 * UH 担当者別計画
	 */
	private final Long uhMrPlanAmount;

	/**
	 * UH 施設特約店別計画積上
	 */
	private final Long uhSpecialInsPlanAmount;

	/**
	 * UH 施設調整フラグ（true:あり、false:なし）
	 */
	private final boolean existUhInsChosei;

	/**
	 * P 担当者別計画
	 */
	private final Long pMrPlanAmount;

	/**
	 * P 施設特約店別計画積上
	 */
	private final Long pSpecialInsPlanAmount;

	/**
	 * P 施設調整フラグ（true:あり、false:なし）
	 */
	private final boolean existPInsChosei;

	/**
	 * 検索結果DTOリスト
	 */
	private final List<InsWsPlanUpDateResultDto> insWsPlanUpDateResultDto;

	/**
	 * 「MR確定」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusMrCompleted;

	/**
	 * 参照品目のリスト
	 */
	private final List<CodeAndValue> refProdList;

	/**
	 * 施設計画MAP（キー：施設内部コード、値：施設計画）
	 */
	private final Map<String, InsPlan> insPlanMap;

	/**
	 * コンストラクタ（非重点品目、仕入品）
	 *
	 * @param plannedProd 品目情報
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param upJgiName 最終更新者
	 * @param upDate 最終更新日
	 * @param uhMrPlanAmount UH 担当者別計画
	 * @param uhSpecialInsPlanAmount UH 施設特約店別計画積上
	 * @param mrPlanAmount P 担当者別計画
	 * @param specialInsPlanAmount P 施設特約店別計画積上
	 * @param insWsPlanUpDateResultDto 検索結果DTOリスト
	 * @param isStatusMrCompleted 「MR確定」フラグ TRUE=ON, FALSE=OFF
	 * @param refProdList 参照品目のリスト
	 */
	public InsWsPlanUpDateResultListDto(PlannedProd plannedProd, Integer jgiNo, String jgiName, String upJgiName, Date upDate,
		Long uhMrPlanAmount, Long uhSpecialInsPlanAmount, Long mrPlanAmount, Long specialInsPlanAmount, List<InsWsPlanUpDateResultDto> insWsPlanUpDateResultDto,
		boolean isStatusMrCompleted, List<CodeAndValue> refProdList) {
		super();
		this.prodCode = plannedProd.getProdCode();
		this.category = plannedProd.getCategory();
		this.prodName = plannedProd.getProdName();
		this.prodType = plannedProd.getProdType();
		this.planLevelInsDoc = plannedProd.getPlanLevelInsDoc();
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.upJgiName = upJgiName;
		this.upDate = upDate;
		this.uhMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(uhMrPlanAmount);
		this.uhSpecialInsPlanAmount = ConvertUtil.parseMoneyToThousandUnit(uhSpecialInsPlanAmount);
		this.pMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(mrPlanAmount);
		this.pSpecialInsPlanAmount = ConvertUtil.parseMoneyToThousandUnit(specialInsPlanAmount);
		this.insWsPlanUpDateResultDto = insWsPlanUpDateResultDto;
		this.isStatusMrCompleted = isStatusMrCompleted;
		this.refProdList = refProdList;
		this.insPlanMap = null;
		this.existUhInsChosei = false;
		this.existPInsChosei = false;
	}

	/**
	 * コンストラクタ（重点品目）
	 *
	 * @param plannedProd 品目情報
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param upJgiName 最終更新者
	 * @param upDate 最終更新日
	 * @param uhMrPlanAmount UH 担当者別計画
	 * @param uhSpecialInsPlanAmount UH 施設特約店別計画積上
	 * @param mrPlanAmount P 担当者別計画
	 * @param specialInsPlanAmount P 施設特約店別計画積上
	 * @param insWsPlanUpDateResultDto 検索結果DTOリスト
	 * @param isStatusMrCompleted 「MR確定」フラグ TRUE=ON, FALSE=OFF
	 * @param refProdList 参照品目のリスト
	 * @param insPlanMap 施設計画MAP
	 */
	public InsWsPlanUpDateResultListDto(PlannedProd plannedProd, Integer jgiNo, String jgiName, String upJgiName, Date upDate,
		Long uhMrPlanAmount, Long uhSpecialInsPlanAmount, boolean existUhInsChosei, Long mrPlanAmount, Long specialInsPlanAmount,
		boolean existPInsChosei, List<InsWsPlanUpDateResultDto> insWsPlanUpDateResultDto,
		boolean isStatusMrCompleted, List<CodeAndValue> refProdList,Map<String, InsPlan> insPlanMap) {
		super();
		this.prodCode = plannedProd.getProdCode();
		this.category = plannedProd.getCategory();
		this.prodName = plannedProd.getProdName();
		this.prodType = plannedProd.getProdType();
		this.planLevelInsDoc = plannedProd.getPlanLevelInsDoc();
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.upJgiName = upJgiName;
		this.upDate = upDate;
		this.uhMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(uhMrPlanAmount);
		this.uhSpecialInsPlanAmount = ConvertUtil.parseMoneyToThousandUnit(uhSpecialInsPlanAmount);
		this.pMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(mrPlanAmount);
		this.pSpecialInsPlanAmount = ConvertUtil.parseMoneyToThousandUnit(specialInsPlanAmount);
		this.insWsPlanUpDateResultDto = insWsPlanUpDateResultDto;
		this.isStatusMrCompleted = isStatusMrCompleted;
		this.refProdList = refProdList;
		this.insPlanMap = insPlanMap;
		this.existUhInsChosei = existUhInsChosei;
		this.existPInsChosei = existPInsChosei;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目カテゴリを取得する。
	 *
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return prodName
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目区分を取得する。
	 *
	 * @return prodType
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 重点品フラグを取得する。
	 *
	 * @return 重点品フラグ
	 */
	public boolean isPlanLevelInsDoc() {
		return planLevelInsDoc;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 最終更新者を取得する。
	 *
	 * @return upJgiName 最終更新者
	 */
	public String getUpJgiName() {
		return upJgiName;
	}

	/**
	 * 最終更新日を取得する。
	 *
	 * @return upDate 最終更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * UH 担当者別計画を取得する。
	 *
	 * @return uhMrPlanAmount UH 担当者別計画
	 */
	public Long getUhMrPlanAmount() {
		return uhMrPlanAmount;
	}

	/**
	 * UH 施設特約店別計画積上を取得する。
	 *
	 * @return uhSpecialInsPlanAmount UH 施設特約店別計画積上
	 */
	public Long getUhSpecialInsPlanAmount() {
		return uhSpecialInsPlanAmount;
	}

	/**
	 * UH 施設調整フラグ（true:あり、false:なし）を取得する。
	 * @return UH 施設調整フラグ（true:あり、false:なし）
	 */
	public boolean isExistUhInsChosei() {
		return existUhInsChosei;
	}

	/**
	 * P 担当者別計画を取得する。
	 *
	 * @return pMrPlanAmount P 担当者別計画
	 */
	public Long getPMrPlanAmount() {
		return pMrPlanAmount;
	}

	/**
	 * 施設特約店別計画積上を取得する。
	 *
	 * @return pSpecialInsPlanAmount 施設特約店別計画積上
	 */
	public Long getPSpecialInsPlanAmount() {
		return pSpecialInsPlanAmount;
	}

	/**
	 * P 施設調整フラグ（true:あり、false:なし）を取得する。
	 * @return P 施設調整フラグ（true:あり、false:なし）
	 */
	public boolean isExistPInsChosei() {
		return existPInsChosei;
	}

	/**
	 * 検索結果DTOリストを取得する。
	 *
	 * @return insWsPlanUpDateResultDto
	 */
	public List<InsWsPlanUpDateResultDto> getInsWsPlanUpDateResultDto() {
		return insWsPlanUpDateResultDto;
	}

	/**
	 * 「MR確定」フラグ TRUE=ON, FALSE=OFFを取得する。
	 *
	 * @return isStatusMrCompleted
	 */
	public Boolean getIsStatusMrCompleted() {
		return isStatusMrCompleted;
	}

	/**
	 * 参照品目のリストを取得する。
	 *
	 * @return refProdList
	 */
	public List<CodeAndValue> getRefProdList() {
		return refProdList;
	}

	/**
	 * 施設計画MAPを取得する。
	 * @return 施設計画MAP
	 */
	public Map<String, InsPlan> getInsPlanMap() {
		return insPlanMap;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
