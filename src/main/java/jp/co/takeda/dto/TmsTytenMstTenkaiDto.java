package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.TmsTytenMstUn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店情報(展開)のDTO
 *
 * @author khashimoto
 */
public class TmsTytenMstTenkaiDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店情報
	 */
	private final TmsTytenMstUn tmsTytenMstUn;

	/**
	 * 末端フラグ
	 */
	private final Boolean endFlg;

	/**
	 * 特約店親フラグ
	 */
	private final Boolean deliveryFlg;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param tmsTytenMstUn 特約店基本統合
	 * @param endFlg 末端フラグ
	 * @param parentFlg 特約店親フラグ
	 */
	public TmsTytenMstTenkaiDto(TmsTytenMstUn tmsTytenMstUn, Boolean endFlg, Boolean deliveryFlg) {
		this.tmsTytenMstUn = tmsTytenMstUn;
		this.endFlg = endFlg;
		this.deliveryFlg = deliveryFlg;
	}

	/**
	 * 特約店情報を取得する。
	 *
	 * @return 特約店情報
	 */
	public TmsTytenMstUn getTmsTytenMstUn() {
		return tmsTytenMstUn;
	}

	/**
	 * 末端フラグを取得する。<br>
	 * 末端の特約店の場合：true
	 *
	 * @return 末端フラグ
	 */
	public Boolean getEndFlg() {
		return endFlg;
	}

	/**
	 * 特約店親フラグを取得する
	 *
	 * @return parentFlg
	 */
	public Boolean getDeliveryFlg() {
		return deliveryFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
