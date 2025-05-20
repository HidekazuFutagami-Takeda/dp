package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.dao.div.SpecialInsPlanRegType;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 登録済みワクチン用特定施設個別計画の検索条件(Search Condition)を表すDTO
 * 
 * @author khashimoto
 */
public class SpecialInsPlanForVacScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(エリア特約店G)
	 */
	private final String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private final String sosCd4;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 重点先/一般先
	 */
	private final ActivityType activityType;

	/**
	 * 都道府県
	 */
	private final Prefecture addrCodePref;

	/**
	 * 市区郡町村
	 */
	private final String addrCodeCity;

	/**
	 * 特定施設個別計画の絞込条件
	 */
	private final SpecialInsPlanRegType specialInsPlanRegType;

	/**
	 * 施設名(全角)
	 */
	private final String insFormalName;

	/**
	 * 施設名(半角カナ)
	 */
	private final String insKanaSrch;

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd3 組織コード(エリア特約店G)
	 * @param sosCd4 組織コード(チーム)
	 * @param jgiNo 従業員番号
	 * @param activityType 重点先/一般先
	 * @param addrCodePref 都道府県
	 * @param addrCodeCity 市区郡町村
	 * @param specialInsPlanRegType 特定施設個別計画の絞込条件
	 * @param insFormalName 施設名(全角)
	 * @param insKanaSrch 施設名(半角カナ)
	 */
	public SpecialInsPlanForVacScDto(String sosCd3, String sosCd4, Integer jgiNo, ActivityType activityType, Prefecture addrCodePref, String addrCodeCity,
		SpecialInsPlanRegType specialInsPlanRegType, String insFormalName, String insKanaSrch) {
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiNo = jgiNo;
		this.activityType = activityType;
		this.addrCodePref = addrCodePref;
		this.addrCodeCity = addrCodeCity;
		this.specialInsPlanRegType = specialInsPlanRegType;
		this.insFormalName = insFormalName;
		this.insKanaSrch = insKanaSrch;
	}

	/**
	 * 組織コード(エリア特約店G)を取得する。
	 * 
	 * @return 組織コード(エリア特約店G)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 * 
	 * @return 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 従業員番号を取得する。
	 * 
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 重点先/一般先を取得する。
	 * 
	 * @return activityType 重点先/一般先
	 */
	public ActivityType getActivityType() {
		return activityType;
	}

	/**
	 * 都道府県を取得する。
	 * 
	 * @return 都道府県
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 市区郡町村を取得する。
	 * 
	 * @return 市区郡町村
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * 特定施設個別計画の絞込条件を取得する。
	 * 
	 * @return 特定施設個別計画の絞込条件
	 */
	public SpecialInsPlanRegType getSpecialInsPlanRegType() {
		return specialInsPlanRegType;
	}

	/**
	 * 施設名(全角)を取得する。
	 * 
	 * @return 施設名(全角)
	 */
	public String getInsFormalName() {
		return insFormalName;
	}

	/**
	 * 施設名（半角カナ）を取得する。
	 * 
	 * @return 施設名（半角カナ）
	 */
	public String getInsKanaSrch() {
		return insKanaSrch;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
