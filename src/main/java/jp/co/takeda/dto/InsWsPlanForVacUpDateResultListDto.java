package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.bean.DpDto;
import jp.co.takeda.util.ConvertUtil;

/**
 * ワクチン用施設特約店別計画編集画面の検索結果用DTO（画面）
 *
 * @author khashimoto
 */
public class InsWsPlanForVacUpDateResultListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 品目名称
	 */
	private final String prodName;

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
	 * 検索結果DTOリスト
	 */
	private final List<InsWsPlanForVacUpDateResultDto> insWsPlanForVacUpDateResultDto;

	/**
	 * 「MR確定」フラグ TRUE=ON, FALSE=OFF
	 */
	private final Boolean isStatusMrCompleted;

	/**
	 * 参照品目のリスト
	 */
	private final List<CodeAndValue> refProdList;

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
	 * コンストラクタ
	 *
	 * @param prodCode 品目固定コード
	 * @param prodName 品目名称
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param upJgiName 最終更新者
	 * @param upDate 最終更新日
	 * @param insWsPlanForVacUpDateResultDto 検索結果DTOリスト
	 * @param isStatusMrCompleted 「MR確定」フラグ TRUE=ON, FALSE=OFF
	 * @param refProdList 参照品目のリスト
	 * @param uhMrPlanAmount UH 担当者別計画
	 * @param uhSpecialInsPlanAmount UH 施設特約店別計画積上
	 * @param pMrPlanAmount P 担当者別計画
	 * @param pSpecialInsPlanAmount P 施設特約店別計画積上
	 */
	public InsWsPlanForVacUpDateResultListDto(String prodCode, String prodName, Integer jgiNo, String jgiName, String upJgiName, Date upDate,
			List<InsWsPlanForVacUpDateResultDto> insWsPlanForVacUpDateResultDto, boolean isStatusMrCompleted, List<CodeAndValue> refProdList,
			Long uhMrPlanAmount, Long uhSpecialInsPlanAmount, Long pMrPlanAmount, Long pSpecialInsPlanAmount) {
		super();
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.upJgiName = upJgiName;
		this.upDate = upDate;
		this.insWsPlanForVacUpDateResultDto = insWsPlanForVacUpDateResultDto;
		this.isStatusMrCompleted = isStatusMrCompleted;
		this.refProdList = refProdList;
		this.uhMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(uhMrPlanAmount);
		this.uhSpecialInsPlanAmount = ConvertUtil.parseMoneyToThousandUnit(uhSpecialInsPlanAmount);
		this.pMrPlanAmount = ConvertUtil.parseMoneyToThousandUnit(pMrPlanAmount);
		this.pSpecialInsPlanAmount = ConvertUtil.parseMoneyToThousandUnit(pSpecialInsPlanAmount);
		this.existUhInsChosei = false;
		this.existPInsChosei = false;
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
	 * 品目名称を取得する。
	 *
	 * @return prodName
	 */
	public String getProdName() {
		return prodName;
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
	 * 検索結果DTOリストを取得する。
	 *
	 * @return insWsPlanForVacUpDateResultDto
	 */
	public List<InsWsPlanForVacUpDateResultDto> getInsWsPlanForVacUpDateResultDto() {
		return insWsPlanForVacUpDateResultDto;
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

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
