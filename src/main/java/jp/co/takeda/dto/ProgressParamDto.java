package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 業務進捗表(医)[薬価改定][T/Y変換指定率]を表すDTOクラス
 *
 * @author tkawabata
 */
public class ProgressParamDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 薬価改定最終更新日
	 */
	private final Date yakkaLastUpDate;

	/**
	 * T/Y変換指定率最終更新日
	 */
	private final Date ytLastUpDate;

	private final Date sbLastUpDate;

	/**
	 * コンストラクタ
	 *
	 * @param yakkaLastUpDate 薬価改定最終更新日
	 * @param ytLastUpDate T/Y変換指定率最終更新日
	 * @param sbLastUpDate S/B変換指定率最終更新日
	 */
	public ProgressParamDto(Date yakkaLastUpDate, Date ytLastUpDate, Date sbLastUpDate) {
		this.yakkaLastUpDate = yakkaLastUpDate;
		this.ytLastUpDate = ytLastUpDate;
		this.sbLastUpDate = sbLastUpDate;
	}

	/**
	 * 薬価改定最終更新日を取得する。
	 *
	 * @return yakkaLastUpDate 薬価改定最終更新日
	 */
	public Date getYakkaLastUpDate() {
		return yakkaLastUpDate;
	}

	/**
	 * T/Y変換指定率最終更新日を取得する。
	 *
	 * @return ytLastUpDate T/Y変換指定率最終更新日
	 */
	public Date getYtLastUpDate() {
		return ytLastUpDate;
	}

	/**
	 * S/B変換指定率最終更新日を取得する
	 * @return sbLastUpDate
	 */
	public Date getSbLastUpDate() {
		return sbLastUpDate;
	}


	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
