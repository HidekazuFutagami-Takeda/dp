package jp.co.takeda.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;

/**
 * ワクチン用施設特約店別計画 担当者一覧更新用DTO
 * 
 * @author nozaki
 */
public class InsWsPlanForVacJgiListUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 氏名
	 */
	private String jgiName;

	/**
	 * ステータス最終更新日時
	 */
	private final Date upDate;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiNo 従業員番号
	 * @param jgiName 氏名
	 * @param upDate ステータス最終更新日時
	 */
	public InsWsPlanForVacJgiListUpdateDto(Integer jgiNo, String jgiName, Date upDate) {
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.upDate = upDate;
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
	 * 氏名を取得する。
	 * 
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * ステータス最終更新日時を取得する。
	 * 
	 * @return upDate ステータス最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 従業員情報に変換する。
	 * 
	 * @return 従業員情報
	 */
	public JgiMst convertJgiMst() {
		JgiMst jgiMst = new JgiMst();
		jgiMst.setJgiNo(jgiNo);
		jgiMst.setJgiName(jgiName);
		return jgiMst;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
