package jp.co.takeda.task;

import jp.co.takeda.bean.DpExceptionDispatcher;
import jp.co.takeda.bean.DpLoggingInfo;

import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * スケジュール化されたジョブを実行するクラス
 * 
 * @author tkawabata
 */
public abstract class AbstractScheduledJob {

	/**
	 * 例外ディスパッチャ
	 */
	@Autowired(required = true)
	@Qualifier("exceptionDispatcher")
	protected DpExceptionDispatcher exceptionDispatcher;

	/**
	 * バッチ処理を実行する。
	 */
	public final void executeInternal() {

		try {

			// 診断ログ開始
			NDC.push(DpLoggingInfo.LOG_SCHEDULED_STRING);

			// バッチ処理を実行
			this.execute();

		} catch (Exception e) {
			this.exceptionDispatcher.dispatch(e);
		} finally {
			// 診断ログ終了
			NDC.remove();
		}
	}

	/**
	 * バッチ処理を実行する。
	 */
	protected abstract void execute();
}
