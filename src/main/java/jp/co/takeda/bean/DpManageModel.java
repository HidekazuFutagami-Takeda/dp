package jp.co.takeda.bean;

import jp.co.takeda.model.div.Term;

/**
 * 納入計画システム向け管理の抽象モデルクラス
 * 
 * @author khashimoto
 */
public abstract class DpManageModel<T> extends DpModel<T> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 年度
	 */
	protected String calYear;

	/**
	 * 期
	 */
	protected Term calTerm;

	/**
	 * 削除フラグ
	 */
	protected Boolean delFlg;

	/**
	 * 登録PGID
	 */
	protected String isPgId;

	/**
	 * 更新PGID
	 */
	protected String upPgId;

	/**
	 * 年度を取得する。
	 * 
	 * @return 年度
	 */
	public String getCalYear() {
		return calYear;
	}

	/**
	 * 年度を設定する。
	 * 
	 * @param calYear 年度
	 */
	public void setCalYear(String calYear) {
		this.calYear = calYear;
	}

	/**
	 * 期を取得する。
	 * 
	 * @return 期
	 */
	public Term getCalTerm() {
		return calTerm;
	}

	/**
	 * 期を設定する。
	 * 
	 * @param calTerm 期
	 */
	public void setCalTerm(Term calTerm) {
		this.calTerm = calTerm;
	}

	/**
	 * 削除フラグを取得する。
	 * 
	 * @return 削除フラグ
	 */
	public Boolean getDelFlg() {
		return delFlg;
	}

	/**
	 * 削除フラグを設定する。
	 * 
	 * @param delFlg 削除フラグ
	 */
	public void setDelFlg(Boolean delFlg) {
		this.delFlg = delFlg;
	}

	/**
	 * 登録PGIDを取得する。
	 * 
	 * @return 登録PGID
	 */
	public String getIsPgId() {
		return isPgId;
	}

	/**
	 * 登録PGIDを設定する。
	 * 
	 * @param isPgId 登録PGID
	 */
	public void setIsPgId(String isPgId) {
		this.isPgId = isPgId;
	}

	/**
	 * 更新PGIDを取得する。
	 * 
	 * @return 更新PGID
	 */
	public String getUpPgId() {
		return upPgId;
	}

	/**
	 * 更新PGIDを設定する。
	 * 
	 * @param upPgId 更新PGID
	 */
	public void setUpPgId(String upPgId) {
		this.upPgId = upPgId;
	}

}
