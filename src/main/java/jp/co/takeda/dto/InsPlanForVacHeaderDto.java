package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワ)施設別計画編集画面ヘッダを表すDTO
 * 
 * @author siwamoto
 */
public class InsPlanForVacHeaderDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設名
	 */
	private final String insName;

	/**
	 * 活動区分
	 */
	private final ActivityType activityType;

	/**
	 * 都道府県
	 */
	private final Prefecture addrCodePref;

	/**
	 * 市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）
	 */
	private final String shikuchosonMeiKj;

	/**
	 * 府県名（漢字）
	 */
	private final String fukenMeiKj;

	/**
	 * 削除施設フラグ(削除または依頼中)
	 */
	private final boolean isDeleteIns;

	/**
	 * コンストラクタ
	 * 
	 * @param insMstResultDto 施設情報 検索結果DTO
	 */
	public InsPlanForVacHeaderDto(InsMst insMst) {
		this.jgiNo = insMst.getJgiNo();
		this.insNo = insMst.getInsNo();
		this.insName = insMst.getInsAbbrName();
		this.activityType = insMst.getActivityType();
		this.addrCodePref = insMst.getAddrCodePref();
		this.addrCodeCity = insMst.getAddrCodeCity();
		this.shikuchosonMeiKj = insMst.getShikuchosonMeiKj();
		this.fukenMeiKj = insMst.getFukenMeiKj();

		// 依頼中 または 削除済の場合は「削除施設」
		if (Boolean.TRUE.equals(insMst.getDelFlg()) || Boolean.TRUE.equals(insMst.getReqFlg())) {
			this.isDeleteIns = true;
		} else {
			this.isDeleteIns = false;
		}
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
	 * 施設コードを取得する。
	 * 
	 * @return 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設名を取得する。
	 * 
	 * @return 施設名
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 活動区分を取得する。
	 * 
	 * @return 活動区分
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
	 * 市区町村コードを取得する。
	 * 
	 * @return 市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 府県名（漢字）を取得する。
	 * 
	 * @return 府県名（漢字）
	 */
	public String getFukenMeiKj() {
		return fukenMeiKj;
	}

	/**
	 * 削除施設フラグを取得する。
	 * 
	 * @return 削除施設フラグ
	 */
	public boolean IsDeleteIns() {
		return isDeleteIns;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
