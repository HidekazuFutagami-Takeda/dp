package jp.co.takeda.bean;

import java.util.Date;

import jp.co.takeda.a.bean.Model;

/**
 * 納入計画システム向け抽象モデルクラス
 * 
 * @author tkawabata
 */
public abstract class DpModel<T> extends Model<T> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * バッチ側で登録した際に表示する文字列
	 */
	public static final String BATCH_REGIST_STRING = "自動登録";

	/**
	 * シーケンスキー
	 */
	protected Long seqKey;

	/**
	 * 登録者(バッチ登録時にNULL)
	 */
	protected Integer isJgiNo;

	/**
	 * 登録者名(バッチ更新時にジョブ番号)
	 */
	protected String isJgiName;

	/**
	 * 登録日
	 */
	protected Date isDate;

	/**
	 * 更新者(バッチ登録時にNULL)
	 */
	protected Integer upJgiNo;

	/**
	 * 更新者名(バッチ更新時にジョブ番号)
	 */
	protected String upJgiName;

	/**
	 * 更新日
	 */
	protected Date upDate;

	/**
	 * シーケンスキーを取得する。
	 * 
	 * @return シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * シーケンスキーを設定する。
	 * 
	 * @param seqKey シーケンスキー
	 */
	public void setSeqKey(Long seqKey) {
		this.seqKey = seqKey;
	}

	/**
	 * 登録者を取得する。
	 * 
	 * @return 登録者
	 */
	public Integer getIsJgiNo() {
		return isJgiNo;
	}

	/**
	 * 登録者を設定する。
	 * 
	 * @param upJgiNo 登録者
	 */
	public void setIsJgiNo(Integer isJgiNo) {
		this.isJgiNo = isJgiNo;
	}

	/**
	 * 登録者名を取得する。<br>
	 * 登録者を参照して、バッチ登録の場合は規定の名前を返す。
	 * 
	 * @return 登録者名
	 */
	public String getIsJgiName() {
		return getDisplayIsJgiName();
	}

	/**
	 * 登録者名を設定する。
	 * 
	 * @param isJgiName 登録者名
	 */
	public void setIsJgiName(String isJgiName) {
		this.isJgiName = isJgiName;
	}

	/**
	 * 登録日を取得する。
	 * 
	 * @return 登録日
	 */
	public Date getIsDate() {
		return isDate;
	}

	/**
	 * 登録日を設定する。
	 * 
	 * @param isDate 登録日
	 */
	public void setIsDate(Date isDate) {
		this.isDate = isDate;
	}

	/**
	 * 更新者を取得する。
	 * 
	 * @return 更新者
	 */
	public Integer getUpJgiNo() {
		return upJgiNo;
	}

	/**
	 * 更新者を設定する。
	 * 
	 * @param upJgiNo 更新者
	 */
	public void setUpJgiNo(Integer upJgiNo) {
		this.upJgiNo = upJgiNo;
	}

	/**
	 * 更新者名を取得する。<br>
	 * 更新者を参照して、バッチ登録の場合は規定の名前を返す。
	 * 
	 * @return 更新者名
	 */
	public String getUpJgiName() {
		return getDisplayUpJgiNo();
	}

	/**
	 * 更新者名を設定する。
	 * 
	 * @param upJgiName 更新者名
	 */
	public void setUpJgiName(String upJgiName) {
		this.upJgiName = upJgiName;
	}

	/**
	 * 更新日を取得する。
	 * 
	 * @return 更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 更新日を設定する。
	 * 
	 * @param upDate 更新日
	 */
	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}

	// ------------------------------
	// Utility Method
	// ------------------------------
	/**
	 * 表示用登録者名を取得する。
	 * 
	 * @return 表示用登録者名
	 */
	protected String getDisplayIsJgiName() {
		if (isJgiNo == null) {
			return BATCH_REGIST_STRING;
		} else {
			return isJgiName;
		}
	}

	/**
	 * 表示用更新者名を取得する。
	 * 
	 * @return 表示用登録者名
	 */
	protected String getDisplayUpJgiNo() {
		if (upJgiNo == null) {
			return BATCH_REGIST_STRING;
		} else {
			return upJgiName;
		}
	}
}
