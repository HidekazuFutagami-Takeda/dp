package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.TmsTytenMstUn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店情報（本店）のDTO
 * 
 * @author khashimoto
 */
public class TmsTytenMstHontenDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店情報
	 */
	private final TmsTytenMstUn tmsTytenMstUn;

	/**
	 * 支社を保持するかどうかのフラグ
	 */
	private final Boolean shishaExistFlg;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param tmsTytenMstUn 選択中の特約店基本統合
	 * @param tmsTytenMstList 特約店基本統合のリスト
	 */
	public TmsTytenMstHontenDto(TmsTytenMstUn tmsTytenMstUn, Boolean shishaExistFlg) {
		this.tmsTytenMstUn = tmsTytenMstUn;
		this.shishaExistFlg = shishaExistFlg;
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
	 * 支社を保持するかどうかのフラグを取得する。
	 * 
	 * @return 支社を保持するかどうかのフラグ
	 */
	public Boolean getShishaExistFlg() {
		return shishaExistFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
