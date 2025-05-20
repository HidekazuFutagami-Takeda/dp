package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.ProdInsInfoErrKbn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * 過去実績参照(施設特約店別計画)DTOクラス
 * 
 * @author siwamoto
 */
public class DeliveryResultInsDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 施設名
	 */
	public final String insName;

	/**
	 * 特約店名
	 */
	public final String tmsTytenName;

	/**
	 * 1行分の実績をあらわすList<Long>
	 */
	public final List<Long> monNnuValueList;

	/**
	 * 表示文字カラーコード
	 */
	private final String dispFontColCd;

	/**
	 * エラー区分
	 */
	private final ProdInsInfoErrKbn prodInsInfoErrKbn;

	/**
	 * コンストラクタ
	 * 
	 * @param insName 施設名
	 * @param tmsTytenName 特約店名
	 * @param monNnuValueList 1行分の実績をあらわすList<Long>
	 */
	public DeliveryResultInsDto(String insName, String tmsTytenName, List<Long> monNnuValueList) {
		this(insName, tmsTytenName, monNnuValueList, null, null);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param insName 施設名
	 * @param tmsTytenName 特約店名
	 * @param monNnuValueList 1行分の実績をあらわすList<Long>
	 * @param dispFontColCd 表示文字カラーコード
	 * @param prodInsInfoErrKbn エラー区分
	 */
	public DeliveryResultInsDto(String insName, String tmsTytenName, List<Long> monNnuValueList, String dispFontColCd, ProdInsInfoErrKbn prodInsInfoErrKbn) {
		this.insName = insName;
		this.tmsTytenName = tmsTytenName;
		this.monNnuValueList = monNnuValueList;
		this.dispFontColCd = dispFontColCd;
		this.prodInsInfoErrKbn = prodInsInfoErrKbn;
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
	 * 特約店名を取得する。
	 * 
	 * @return 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 1行分の実績をあらわすList<Long>を取得する。
	 * 
	 * @return 1行分の実績をあらわすList<Long>
	 */
	public List<Long> getMonNnuValueList() {
		return monNnuValueList;
	}

	/**
	 * 表示文字カラーコードを取得する。
	 * 
	 * @return 表示文字カラーコード
	 */
	public String getDispFontColCd() {
		return dispFontColCd;
	}

	/**
	 * エラー区分を取得する。
	 * 
	 * @return エラー区分
	 */
	public ProdInsInfoErrKbn getProdInsInfoErrKbn() {
		return prodInsInfoErrKbn;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
