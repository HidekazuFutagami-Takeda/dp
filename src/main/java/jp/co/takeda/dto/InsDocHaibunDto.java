package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.ProdCategory;
import jp.co.takeda.security.DpUser;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設医師配分実行パラメータを定義したDtoクラス
 *
 * @author tkawabata
 */
public class InsDocHaibunDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織情報
	 */
	private SosMst sosMst;

	/**
	 * 従業員情報
	 */
	private JgiMst jgiMst;

	/**
	 * クリアフラグ
	 */
	private boolean doPlanClear;

	/**
	 * 実行者情報
	 */
	private DpUser execDpUser;

	/**
	 * カテゴリ
	 */
	private ProdCategory category;

	/**
	 * コンストラクタ
	 *
	 * @param sosMst 組織情報
	 * @param jgiMst 従業員情報
	 * @param doPlanClear クリアフラグ
	 * @param execDpUser 実行者情報
	 */
	public InsDocHaibunDto(SosMst sosMst, JgiMst jgiMst, boolean doPlanClear, DpUser execDpUser, ProdCategory category) {
		this.sosMst = sosMst;
		this.jgiMst = jgiMst;
		this.doPlanClear = doPlanClear;
		this.execDpUser = execDpUser;
		this.category = category;
	}

	/**
	 * 組織情報を取得する。
	 *
	 * @return 組織情報
	 */
	public SosMst getSosMst() {
		return sosMst;
	}

	/**
	 * 組織情報を設定する。
	 *
	 * @param sosMst 組織情報
	 */
	public void setSosMst(SosMst sosMst) {
		this.sosMst = sosMst;
	}

	/**
	 * 従業員情報を取得する。
	 *
	 * @return 従業員情報
	 */
	public JgiMst getJgiMst() {
		return jgiMst;
	}

	/**
	 * 従業員情報を設定する。
	 *
	 * @param jgiMst 従業員情報
	 */
	public void setJgiMst(JgiMst jgiMst) {
		this.jgiMst = jgiMst;
	}

	/**
	 * クリアフラグを取得する。
	 *
	 * @return クリアフラグ
	 */
	public boolean isDoPlanClear() {
		return doPlanClear;
	}

	/**
	 * クリアフラグを設定する。
	 *
	 * @param doPlanClear クリアフラグ
	 */
	public void setDoPlanClear(boolean doPlanClear) {
		this.doPlanClear = doPlanClear;
	}

	/**
	 * 実行者情報を取得する。
	 *
	 * @return 実行者情報
	 */
	public DpUser getExecDpUser() {
		return execDpUser;
	}

	/**
	 * 実行者情報を設定する。
	 *
	 * @param execDpUser 実行者情報
	 */
	public void setExecDpUser(DpUser execDpUser) {
		this.execDpUser = execDpUser;
	}

	/**
	 * カテゴリを取得する。
	 * @return カテゴリ
	 */
	public ProdCategory getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 * @param category カテゴリ
	 */
	public void setCategory(ProdCategory category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
