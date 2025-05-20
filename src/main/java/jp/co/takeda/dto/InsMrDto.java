package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 施設/従業員情報を表すDTO
 *
 * @author khashimoto
 */
public class InsMrDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 選択された試算除外フラグ
	 */
	private final String estimationFlg;

	/**
	 * 選択された配分除外フラグ
	 */
	private final String exceptFlg;

	/**
	 * コンストラクタ
	 *
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param estimationFlg 試算除外フラグ
	 * @param exceptFlg 配分除外フラグ
	 */
	public InsMrDto(String insNo, Integer jgiNo, String estimationFlg, String exceptFlg) {
		this.insNo = insNo;
		this.jgiNo = jgiNo;
		this.estimationFlg = estimationFlg;
		this.exceptFlg = exceptFlg;
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
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 選択された試算除外フラグを取得する。
	 *
	 * @return estimationFlg
	 */
	public String getEstimationFlg() {
		return estimationFlg;
	}

	/**
	 * 選択された配分除外フラグを取得する。
	 *
	 * @return exceptFlg
	 */
	public String getExceptFlg() {
		return exceptFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
