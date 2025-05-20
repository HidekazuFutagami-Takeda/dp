package jp.co.takeda.bean;

import org.apache.log4j.DailyRollingFileAppender;

/**
 * 納入計画システム用日ローテファイルアペンダークラス
 * 
 * @author tkawabata
 */
public class DpRollingFileAppender extends DailyRollingFileAppender {

	/**
	 * コンストラクタ
	 */
	public DpRollingFileAppender() {
		super();
		setFile(new DpLoggingInfo().getLogFilePathName());
	}

	@Override
	public String getFile() {
		return new DpLoggingInfo().getLogFilePathName();
	}
}
