package jp.co.takeda.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 納入計画システム支援のタスク実行機構
 * 
 * @author tkawabata
 */
public class TaskExecutor extends ThreadPoolTaskExecutor {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(TaskExecutor.class);

	@Override
	public final void initialize() {

		if (LOG.isDebugEnabled()) {
			LOG.debug("taskExecutor initialized...");
		}

		// 親クラスの初期化処理
		super.initialize();
	}
}
