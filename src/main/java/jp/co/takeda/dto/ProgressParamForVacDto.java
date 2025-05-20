package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 業務進捗表(ワ)[T/B変換指定率]を表すDTOクラス
 * 
 * @author tkawabata
 */
public class ProgressParamForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * T/B変換指定率最終更新日
	 */
	private final Date btLastUpDate;

	/**
	 * コンストラクタ
	 * 
	 * @param btLastUpDate T/B変換指定率最終更新日
	 */
	public ProgressParamForVacDto(Date ytLastUpDate) {
		this.btLastUpDate = ytLastUpDate;
	}

	/**
	 * T/B変換指定率最終更新日を取得する。
	 * 
	 * @return ytLastUpDate T/Y変換指定率最終更新日
	 */
	public Date getBtLastUpDate() {
		return btLastUpDate;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
