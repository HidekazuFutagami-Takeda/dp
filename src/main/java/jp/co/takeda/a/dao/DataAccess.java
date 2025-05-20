package jp.co.takeda.a.dao;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;

/**
 * DataAccessインタフェース<br>
 * 
 * <p>
 * 当インターフェイスでは、{@link java.sql.SQLException}をスローしないため、<br>
 * 実装クラス側では実行時例外({@link RuntimeException}のサブクラス)に変換する必要がある。<br>
 * </p>
 * 
 * @author tkawabata
 */
public interface DataAccess {

	/**
	 * SQLの実行結果を指定された型にして返却する。<br>
	 * 1件のみ結果が予想される場合に利用する。
	 * 
	 * @param <E> 返却値の型
	 * @param sqlID 実行するSQLのID
	 * @param bindParams SQLにバインドする値を格納したオブジェクト
	 * @return SQLの実行結果
	 * @throws DataNotFoundException 検索結果がない場合にスロー
	 */
	<E> E queryForObject(String sqlID, Object bindParams) throws DataNotFoundException;

	/**
	 * SQLの実行結果を指定された型のListで返却する。<br>
	 * 複数件の検索結果結果が予想される場合に利用する。
	 * 
	 * @param <E> 返却値の型
	 * @param sqlID 実行するSQLのID
	 * @param bindParams SQLにバインドする値を格納したオブジェクト
	 * @return SQLの実行結果
	 * @throws DataNotFoundException 検索結果がない場合にスロー
	 */
	<E> List<E> queryForList(String sqlID, Object bindParams) throws DataNotFoundException;

	/**
	 * 引数sqlIDで指定されたSQLを実行して、結果件数を返却する。 <br>
	 * 実行するSQLは「insert, update, delete」の3種類とする。
	 * 
	 * @param sqlID 実行するSQLのID
	 * @param bindParams SQLにバインドする値を格納したオブジェクト
	 * @return SQLの影響行数を返却
	 */
	int execute(String sqlID, Object bindParams);

	/**
	 * バッチ更新処理を行う。<br>
	 * 引数の{@link SqlHolder}のリストで指定されたすべてのSQLを実行する。
	 * 
	 * @param sqlHolders バッチ更新対象のsqlId、パラメータを格納した SqlHolderインスタンスのリスト
	 * @return SQLの実行結果件数
	 * @see SqlHolder
	 */
	int executeBatch(List<SqlHolder> sqlHolders);

	/**
	 * 指定されたSQLIDのストアドプロシージャーを実行する。 <br>
	 * ストアドプロシージャーの結果であるアウトパラメータは、引数のbindParamsに反映される。
	 * 
	 * @param sqlID 実行するSQLのID
	 * @param bindParams SQLにバインドする値を格納したオブジェクト
	 */
	void executeProcedure(String sqlID, Object bindParams);
}
