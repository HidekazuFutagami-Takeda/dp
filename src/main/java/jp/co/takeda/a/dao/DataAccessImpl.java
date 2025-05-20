package jp.co.takeda.a.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * DataAccessインタフェースの実装クラス<br>
 * 
 * <p>
 * SpringのiBatis連携クラスである<code>SqlMapClientDaoSupport</code>を継承し、一連のデータアクセス機能を提供する。<br>
 * 以下に設定例を示す。
 * </p>
 * 
 * <ol>
 * <li>Springの設定</li>
 * <fieldset> <legend> applicationContext.xml </legend> <br>
 * &lt;!-- Scan Injection target --&gt;<br>
 * &lt;context:annotation-config /&gt;<br>
 * &lt;context:component-scan base-package="com.xxx.dao"/&gt;<br>
 * <br>
 * &lt;!-- DataAccess --&gt;<br>
 * &lt;bean id="dataSource" class="..." destroy-method="..."&gt;<br>
 * 　:<br>
 * &lt;/bean&gt;<br>
 * <br>
 * &lt;!-- TransactionManager --&gt;<br>
 * &lt;bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"&gt;<br>
 * 　　&lt;property name="dataSource" ref="dataSource"/&gt;<br>
 * &lt;/bean&gt;<br>
 * <br>
 * &lt;!-- TransactionIntercepter and AnotationManagement --&gt;<br>
 * &lt;tx:annotation-driven transaction-manager="transactionManager" /&gt;<br>
 * <br>
 * &lt;!-- SqlMapClient --&gt;<br>
 * &lt;bean id="sqlMapClient" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean"&gt;<br>
 * 　　&lt;property name="configLocation"&gt;<br>
 * 　　　　&lt;value&gt;/.../sqlmapconfig.xml&lt;/value&gt;<br>
 * 　　&lt;/property&gt;<br>
 * &lt;/bean&gt;<br>
 * <br>
 * &lt;!-- DataAccess --&gt;<br>
 * &lt;bean id="dataAccess" class="jp.co.takeda.a.dao.DataAccessImpl"&gt;<br>
 * 　　&lt;property name="sqlMapClient" ref="sqlMapClient" /&gt;<br>
 * 　　&lt;property name="dataSource" ref="dataSource" /&gt;<br>
 * &lt;/bean&gt;<br>
 * </fieldset> <br>
 * <br>
 * <li>Daoインターフェイスの作成</li>
 * <fieldset> <legend> HogeDao </legend> <code>
 * public interface HogeDao {<br>
 * 　　findHoge(String key) throws DataNotFoundException;<br>
 * }
 * </code> </fieldset> <br>
 * <br>
 * <li>Dao実装クラスの作成</li>
 * <fieldset> <legend> HogeDao </legend> <code>
 * &#064;Repository("hogeDao")<br>
 * public class HogeDaoImpl implements HogeDao {<br>
 * 　　&#064;Autowired<br>
 * 　　&#064;Qualifier("dataAccess")<br>
 * 　　protected DataAccess dataAccess;<br>
 * <br>
 * 　　public findHoge(String key) throws DataNotFoundException {<br>
 * 　　　　final String sqlID = "Hoge.select";<br>
 * 　　　　Map params = new HashMap(1);<br>
 * 　　　　params.put("key", key);<br>
 * 　　　　return this.dataAccess.executeForObject(sqlID, params);<br>
 * 　　}<br>
 * }<br>
 * </code> </fieldset> <br>
 * <br>
 * <p>
 * 上記の例では、当クラスのインスタンスは、"dataAccess"という名前でSpringに管理されている。<br>
 * 各Dao実装クラスは、"dataAccess"を<b>メンバとして保持</b>し、Springのアノテーション機能を利用してSpring起動時に自動でインジェクションされるようにする。<br>
 * <code>&#064;Repository("hogeDao")</code>部分の記述によって"hogeDao"がSpring管理Beanに登録され、
 * &#064;Autowiredおよび&#064;Qualifier("dataAccess")部分の記述によって"dataAccess"が当インスタンスのメンバ変数に自動でインジェクションされることになる。<br>
 * (dataAccess変数に対するgetterおよびsetterメソッドは不要。ただし単体テストを考慮し、dataAccessはprotectedメンバとして宣言する。) なお、当クラスのインスタンスはスレッドセーフである。
 * </p>
 * </ol>
 * 
 * @see DataAccess
 * @see SqlMapClientDaoSupport
 * @author tkawabata
 */
public class DataAccessImpl extends SqlMapClientDaoSupport implements DataAccess {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(DataAccessImpl.class);

	/**
	 * データがない場合のメッセージ
	 */
	protected String dataNotFoundMsg = "データベースアクセスで検索処理を実行したが結果が0件";

	/**
	 * データがない場合の例外
	 */
	protected final DataNotFoundException dataNotFoundExp = new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR, dataNotFoundMsg));

	/**
	 * データがない場合の例外を取得する。
	 * 
	 * @return データがない場合の例外
	 */
	public DataNotFoundException getDataNotFoundException() {
		return this.dataNotFoundExp;
	}

	/**
	 * 指定IDのSQL文を引数の条件で検索する。(期待値：1件)<br>
	 * 検索結果が1件より多く存在する場合、実行時例外がスローされる。
	 * 
	 * @param <E> 任意の型
	 * @param sqlID StatementID
	 * @param bindParams 条件
	 * @return 検索結果(1件)
	 * @throws DataNotFoundException 検索結果が0件の場合にスロー
	 * @see jp.co.takeda.a.dao.DataAccess#queryForObject(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <E> E queryForObject(final String sqlID, final Object bindParams) throws DataNotFoundException {

		if (LOG.isDebugEnabled()) {
			LOG.debug(buildDebugMessage(sqlID, bindParams));
		}

		final SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();
		final E result = (E) sqlMapTemp.queryForObject(sqlID, bindParams);

		if (result == null) {
			throw getDataNotFoundException();
		}
		return result;
	}

	/**
	 * 指定IDのSQL文を引数の条件で検索する。(期待値：1件以上)
	 * 
	 * @param <E> 任意の型
	 * @param sqlID StatementID
	 * @param bindParams 条件
	 * @return 検索結果(複数件)
	 * @throws DataNotFoundException 検索結果が0件の場合にスロー
	 * @see jp.co.takeda.a.dao.DataAccess#queryForList(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> queryForList(final String sqlID, final Object bindParams) throws DataNotFoundException {

		if (LOG.isDebugEnabled()) {
			LOG.debug(buildDebugMessage(sqlID, bindParams));
		}

		final SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();
		final List<E> list = sqlMapTemp.queryForList(sqlID, bindParams);

		if (list.size() < 1) {
			throw getDataNotFoundException();
		}
		return list;
	}

	/**
	 * 指定IDの[登録/更新/削除処理]を引数の条件で実行する。
	 * 
	 * @param sqlID StatementID
	 * @param bindParams 条件
	 * @return 影響行数
	 * @see jp.co.takeda.a.dao.DataAccess#execute(java.lang.String, java.lang.Object)
	 */
	public int execute(final String sqlID, final Object bindParams) {

		if (LOG.isDebugEnabled()) {
			LOG.debug(buildDebugMessage(sqlID, bindParams));
		}

		final SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();
		final int row = sqlMapTemp.update(sqlID, bindParams);

		if (LOG.isDebugEnabled()) {
			LOG.debug("execute End.success count:" + row);
		}
		return row;
	}

	/**
	 * PreparedStatementのバッチを実行する。<br>
	 * 
	 * <p>
	 * <code>Statement</code>のバッチではなく、<code>PreparedStatement</code>のバッチである。<br>
	 * 混同しないように注意が必要。<br>
	 * 同一SQL文(PreparedStatementのレベルでの同一)を複数回実行する場合に、<br>
	 * JDBCの実装次第ではパフォーマンスが向上する場合がある。<br>
	 * </p>
	 * 
	 * @param sqlHolders 複数のSQLを格納したホルダー
	 * @return 影響行数
	 * @see jp.co.takeda.a.dao.DataAccess#executeBatch(java.util.List)
	 */
	public int executeBatch(final List<SqlHolder> sqlHolders) {

		Integer result = 0;
		result = (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(final SqlMapExecutor executor) throws SQLException {

				if (sqlHolders == null) {
					final String errMsg = "実行するSQLがないためエラー";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug("executeBatch Start. SQL count:" + sqlHolders.size());
				}

				executor.startBatch();
				for (final SqlHolder sqlHolder : sqlHolders) {
					executor.update(sqlHolder.getId(), sqlHolder.getBindParams());

					if (LOG.isDebugEnabled()) {
						buildDebugMessage(sqlHolder.getId(), sqlHolder.getBindParams());
					}
				}
				return executor.executeBatch();
			}
		});

		if (LOG.isDebugEnabled()) {
			LOG.debug("executeBatch End. Result:" + result);
		}
		return result.intValue();
	}

	/**
	 * SQL Procedureを実行する。<br>
	 * 
	 * <pre>
	 * 引数のbindParamsでinパラメータとoutパラメータを兼ねる。
	 * Procedureの利用はロジックがアプリケーションから分離してしまう結果になるので、
	 * 極力避けるべきだが、パフォーマンス等の観点からやむを得ない場合は当関数からコールする。
	 * </pre>
	 * 
	 * @param sqlID StatementID
	 * @param bindParams 条件兼結果格納庫
	 * @see jp.co.takeda.a.dao.DataAccess#executeProcedure(java.lang.String, java.lang.Object)
	 */
	public void executeProcedure(final String sqlID, final Object bindParams) {

		if (LOG.isDebugEnabled()) {
			LOG.debug(buildDebugMessage(sqlID, bindParams));
		}

		final SqlMapClientTemplate sqlMapTemp = getSqlMapClientTemplate();
		sqlMapTemp.queryForObject(sqlID, bindParams);

	}

	/**
	 * デバック用メッセージを生成する。
	 * 
	 * @param sqlID SQLを特定するID
	 * @param bindParams バインド変数
	 * @return デバック用メッセージ
	 */
	@SuppressWarnings("unchecked")
	protected String buildDebugMessage(String sqlID, Object bindParams) {

		final String SEP = " ";
		StringBuilder sb = new StringBuilder();
		sb.append("sqlId=").append(sqlID).append(SEP);
		if (bindParams == null) {
			sb.append("bindParams:{null}");
		} else {
			sb.append("bindParams:{");

			// java.util.Map型の場合
			if (bindParams instanceof Map) {
				Map map = (Map) bindParams;
				for (Object obj : map.entrySet()) {
					Map.Entry entry = (Map.Entry) obj;
					sb.append(entry.getKey() + "=" + entry.getValue() + SEP);
				}
			}

			// java.util.Collection型の場合
			else if (bindParams instanceof Collection) {
				Collection col = (Collection) bindParams;
				int idx = 0;
				for (Object obj : col) {
					sb.append("[" + idx + "]=" + obj + SEP);
					idx++;
				}
			}

			// その他の場合
			else {
				sb.append(bindParams.toString());
			}
			sb.append("}");
		}

		String result = sb.toString();
		if (result.lastIndexOf(" }") != -1) {
			result = result.replace(" }", "}");
		}
		return result;
	}
}
