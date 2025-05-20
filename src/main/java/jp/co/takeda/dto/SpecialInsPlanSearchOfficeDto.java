package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DpsInsMst;

/**
 * 特定施設個別計画の登録/更新用DTO
 *
 * @author khashimoto
 */
public class SpecialInsPlanSearchOfficeDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設を担当する従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 登録対象の施設情報
	 */
	private final DpsInsMst insMst;

	/**
	 * 特定施設個別計画のリスト
	 */
	private final List<SpecialInsPlanProdListResultDto> specialInsPlanProdList;

	/**
	 * 特定施設個別計画の最終更新者
	 */
	private final String upJgiName;

	/**
	 * 特定施設個別計画の最終更新日時
	 */
	private final Date upDate;

	/**
	 * 組織コード
	 */
	private final String sosCd3;

	/**
	 * 担当者別計画値
	 */
	private Map<String, Long> mrPlanValueMap;

	/**
	 * コンストラクタ
	 *
	 * @param jgiNo 施設を担当する従業員番号
	 * @param insMst 登録対象の施設情報
	 * @param specialInsPlanList 特定施設個別計画のリスト
	 * @param upDate 最終更新日時
	 * @param sosCd3
	 * @param mrPlanValueMap
	 */
	public SpecialInsPlanSearchOfficeDto(Integer jgiNo, DpsInsMst insMst, List<SpecialInsPlanProdListResultDto> specialInsPlanProdList, String upJgiName, Date upDate, String sosCd3,
		Map<String, Long> mrPlanValueMap) {
		this.jgiNo = jgiNo;
		this.insMst = insMst;
		this.specialInsPlanProdList = specialInsPlanProdList;
		this.upJgiName = upJgiName;
		this.upDate = upDate;
		this.sosCd3 = sosCd3;
		this.mrPlanValueMap = mrPlanValueMap;
	}

	/**
	 * 施設を担当する従業員番号を取得する。
	 *
	 * @return 施設を担当する従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 登録対象の施設情報を取得する。
	 *
	 * @return 登録対象の施設情報
	 */
	public DpsInsMst getInsMst() {
		return insMst;
	}

	/**
	 * 特定施設個別計画のリストを取得する。
	 *
	 * @return 特定施設個別計画のリスト
	 */
	public List<SpecialInsPlanProdListResultDto> getSpecialInsPlanProdList() {
		return specialInsPlanProdList;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
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
	 * 組織コードを取得する。
	 *
	 * @return sosCd3 組織コード
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 担当者別計画値を取得する。
	 *
	 * @return 担当者別計画値
	 */
	public Map<String, Long> getMrPlanValueMap() {
		return mrPlanValueMap;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
