package jp.co.takeda.task;

import java.util.Map;

import javax.annotation.PostConstruct;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.bean.DpAsyncDto;
import jp.co.takeda.bean.DpExceptionDispatcher;
import jp.co.takeda.bean.DpLoggingInfo;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsAsyncProcessSearchService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * オンライン非同期処理を実行する抽象クラス<br>
 * 
 * <pre>
 * 画面から起動される非同期処理 = オンライン非同期処理とする。
 * 非同期に起動する機能自体は、<code>java.util.concurrent.ExecutorService</code>に委譲する。
 * 当クラスは抽象クラスであり、利用するためには、
 * メインのスレッドがオンライン非同期処理を起動するかを判定する<code>{@link #canFire(T)}</code>および
 * 非同期に起動される具体的な処理を記述した<code>{@link #process(T)}</code>を実装する必要がある。
 * </pre>
 * 
 * @see java.util.concurrent.ExecutorService
 * @see jp.co.takeda.bean.TaskExecutor
 * @author tkawabata
 */
public abstract class AbstractOnlineJob<T extends DpAsyncDto> {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(AbstractOnlineJob.class);

	/**
	 * サーバ起動時のログ文字列
	 */
	public static final String SERVER_START_UP_LOG_STRING = "*** ApplicationServer StartUp";

	@Autowired(required = true)
	@Qualifier("taskExecutor")
	protected TaskExecutor taskExecutor;

	/**
	 * 例外ディスパッチャ
	 */
	@Autowired(required = true)
	@Qualifier("exceptionDispatcher")
	protected DpExceptionDispatcher exceptionDispatcher;

	/**
	 * 非同期処理初期検索用
	 */
	@Autowired(required = true)
	@Qualifier("dpsAsyncProcessSearchService")
	protected DpsAsyncProcessSearchService dpsAsyncProcessSearchService;

	/**
	 * 初期化処理を定義する。
	 * 
	 * @throws LogicalException 論理例外
	 */
	@PostConstruct
	public final void init() {

		// 診断ログ開始
		NDC.push(DpLoggingInfo.LOG_INIITIAL_STRING);

		try {

			// サーバ区分を取得
			String appServerKbn = System.getProperty("hostname","1");

			// 初期化処理実行
			initilize(appServerKbn);

		} catch (final Exception e) {

			// 例外処理
			exceptionProcess(e);

		} finally {

			// 最終処理
			finalProcess();

			// 診断ログ終了
			NDC.remove();
		}
	}

	/**
	 * 初期化処理を定義する。
	 * 
	 * @param sKbn サーバ区分
	 * @throws Exception
	 */
	protected abstract void initilize(final String sKbn) throws Exception;

	/**
	 * メインスレッドが非同期処理を起動するかの判定を行う。<br>
	 * この抽象メソッドが<code>false</code>を返す場合、非同期処理は実行されない。
	 * 
	 * @param dto データの受け渡しを行うデータ格納庫
	 * @throws LogicalException 起動NGの場合に論理例外をスロー
	 */
	protected abstract void canFire(final T dto) throws LogicalException;

	/**
	 * 非同期に起動される具体的なバッチ処理を記述する。<br>
	 * 
	 * @param dto データの受け渡しを行うデータ格納庫
	 * @throws LogicalException 論理例外発生時
	 */
	protected abstract void process(final T dto) throws LogicalException;

	/**
	 * 起動条件をチェックし、問題がなければ「オンライン非同期処理」を起動する。<br>
	 * 
	 * <pre>
	 * 非同期処理自体は、<code>java.util.concurrent.ExecutorService</code>に委譲する。
	 *
	 * [処理フロー]
	 * {@link #canFire(Map)}が例外(LogicalException)をスローせずに終了した場合、非同期処理として起動する。
	 * {@link #canFire(Map)}が例外(LogicalException)をスローした場合、非同期処理を起動しない。(処理を中断する)
	 * </pre>
	 * 
	 * @param dto データの受け渡しを行うデータ格納庫
	 * @throws LogicalException 論理例外
	 */
	public final void execute(final T dto) throws LogicalException {

		// ユーザ情報のクローンを作成
		final DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo().clone();

		// 起動条件を確認
		this.canFire(dto);

		// 非同期処理機構にタスクを依頼
		this.taskExecutor.execute(new Runnable() {
			public void run() {
				try {

					DpUserInfo.setDpUserInfo(dpUserInfo);
					if (LOG.isDebugEnabled()) {
						LOG.debug("*** DpUserInfo --> Thread");
					}

					// 診断ログ開始
					NDC.push(DpLoggingInfo.LOG_ASYNCS_STRING);

					// ログ文字列追加
					NDC.push(dto.getLogString());

					// 処理
					process(dto);

				} catch (final Exception e) {

					// 例外処理
					exceptionProcess(dto, e);

				} finally {

					// 最終処理
					finalProcess(dto);

					// ユーザ情報の削除
					DpUserInfo.clearDpUserInfo();

					if (LOG.isDebugEnabled()) {
						LOG.debug("*** DpUserInfo Clear");
					}

					// 診断ログ終了
					NDC.remove();
				}
			}
		});
	}

	/**
	 * 必ず実行される最終処理<br>
	 * 実装は任意<br>
	 */
	protected void finalProcess() {
	}

	/**
	 * 必ず実行される最終処理<br>
	 * 実装は任意<br>
	 * 
	 * @param dto データの受け渡しを行うデータ格納庫
	 */
	protected void finalProcess(final T dto) {
	}

	/**
	 * 例外が発生した場合の挙動を規定する。<br>
	 * 
	 * @param cause 発生した例外
	 */
	protected void exceptionProcess(final Exception e) {
		this.exceptionDispatcher.dispatch(e);
	}

	/**
	 * 例外が発生した場合の挙動を規定する。<br>
	 * 
	 * @param dto 引渡オブジェクト
	 * @param cause 発生した例外
	 */
	protected void exceptionProcess(final T dto, final Exception e) {
		this.exceptionDispatcher.dispatch(e);
	}
}
