package jp.co.takeda.dao;

import java.util.Date;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.logic.div.ValueChangeType;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.Term;

/**
 * 管理の変換パラメータ(Y→T価)にアクセスするDAOインターフェイス
 * 
 * @author khashimoto
 */
public interface ManageChangeParamYTDao {

	/**
	 * 変換パラメータ(Y→T価)を取得する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 変換パラメータ(Y→T価)
	 */
	ChangeParamYT search(Long seqKey) throws DataNotFoundException;

	/**
	 * ユニークキーで変換パラメータ(Y→T価)を取得する。
	 * 
	 * @param calYear 年度
	 * @param calTerm 期
	 * @param prodCode 品目固定コード
	 * @param tmsTytenCdHonten TMS特約店コード本店（NULL可）
	 * @return 変換パラメータ(Y→T価)
	 */
	ChangeParamYT searchUk(String calYear, Term calTerm, String prodCode, String tmsTytenCdHonten) throws DataNotFoundException;

	/**
	 * 変換パラメータ(Y→T価)の最終更新日時を取得する。
	 * 
	 * @param calYear 年度
	 * @param calTerm 期
	 * @return 最終更新日時(データ無の場合はNULL)
	 */
	Date getLastUpDate(String calYear, Term calTerm);

	/**
	 * 変換パラメータ(Y→T価)を使用して、変換した計画値を取得する。
	 * 
	 * 
	 * @param prodCode 品目固定コード
	 * @param value 計画値
	 * @param insType 対象区分
	 * @param valueChangeType 変換区分
	 * @param tmsTytenCd 特約店コード
	 * @return 変換後の計画値
	 */
	Long getChangeValue(String prodCode, Long value, InsType insType, ValueChangeType valueChangeType, String tmsTytenCd);

}
