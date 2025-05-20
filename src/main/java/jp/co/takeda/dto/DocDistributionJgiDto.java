package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 医師別計画配分処理パラメータクラス
 * 
 * @author nozaki
 */
public class DocDistributionJgiDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 従業員情報
	 */
	private final JgiMst jgiMst;
	
	/**
	 * 配分処理と同時にMRに公開フラグ
	 * <ul>
	 * <li>TRUE =公開する</li>
	 * <li>FALSE=公開しない</li>
	 * </ul>
	 */
	private final boolean doMrOpen;

	/**
	 * 計画をクリアするフラグ
	 * <ul>
	 * <li>TRUE =クリアする</li>
	 * <li>FALSE=クリアしない</li>
	 * </ul>
	 */
	private final boolean doPlanClear;

	/**
	 * ステータス最終更新日時
	 */
	private final Date statusLastUpdates;

	/**
	 * コンストラクタ
	 * 
	 * @param jgiMst 従業員情報
	 * @param doMrOpen 同時公開フラグ
	 * @param doPlanClear 計画クリアフラグ
	 * @param statusLastUpdates ステータス最終更新日
	 */
	public DocDistributionJgiDto(JgiMst jgiMst, boolean doMrOpen, boolean doPlanClear, Date statusLastUpdates) {
		this.jgiMst = jgiMst;
		this.doMrOpen = doMrOpen;
		this.doPlanClear = doPlanClear;
		this.statusLastUpdates = statusLastUpdates;
	}

	//---------------------
	// Getter & Setter
	// --------------------

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
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
	 * 同時公開フラグを取得する。
	 * 
	 * @return 同時公開フラグ
	 */
	public boolean getDoMrOpen() {
		return doMrOpen;
	}

	/**
	 * 計画クリアフラグを取得する。
	 * 
	 * @return 計画クリアフラグ
	 */
	public boolean getDoPlanClear() {
		return doPlanClear;
	}

	/**
	 * ステータス最終更新日時を取得する。
	 * 
	 * @return ステータス最終更新日時
	 */
	public Date getStatusLastUpdates() {
		return statusLastUpdates;
	}
}
