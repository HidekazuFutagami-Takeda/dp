package jp.co.takeda.bean;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * ロギングの情報を格納するクラス
 * 
 * @author tkawabata
 */
public class DpLoggingInfo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ログ[オンライン処理]
	 */
	public static final String LOG_ONLINE_STRING = "[ONLINE]";

	/**
	 * ログ[オンライン非同期処理]
	 */
	public static final String LOG_ASYNCS_STRING = "[ASYNCS]";

	/**
	 * ログ[初期起動]
	 */
	public static final String LOG_INIITIAL_STRING = "[INITIAL]";

	/**
	 * ログ[スケジュール処理]
	 */
	public static final String LOG_SCHEDULED_STRING = "[SCHEDULED]";

	/**
	 * ログファイルの絶対パスを特定するためのJNDI名
	 */
	public static final String LOG_FILE_PATH_JNDI_NAME = "java:comp/env/dp/logFilePath";

	/**
	 * ログファイルのデフォルトパス
	 */
	public static final String LOG_FILE_DEFAULT_PATH = "./dp";

	/**
	 * ログファイル名称
	 */
	public static final String LOG_FILE_NAME = "dp.log";

	/**
	 * ログファイル日付パターン
	 */
	public static final String LOG_FILE_DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * ログファイルgzip圧縮時の拡張子
	 */
	public static final String LOG_GZIP_SUFFIX = ".gz";

	/**
	 * ログファイル名の日付文字列を特定するための正規表現
	 */
	public static final String LOG_FILE_REGEX = "dp\\.log\\.\\d{4}-\\d{2}-\\d{2}.*";

	/**
	 * バックアップ数
	 */
	private Integer logMaxBackupIndex;

	/**
	 * ログファイルパス
	 */
	private String logFilePath;

	/**
	 * logFilePathを設定する。
	 * 
	 * @param logFilePath
	 */
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	/**
	 * バックアップ数を取得する。
	 * 
	 * @return logMaxBackupIndex バックアップ数
	 */
	public Integer getLogMaxBackupIndex() {
		return logMaxBackupIndex;
	}

	/**
	 * バックアップ数を設定する。
	 * 
	 * @param logMaxBackupIndex バックアップ数
	 */
	public void setLogMaxBackupIndex(Integer logMaxBackupIndex) {
		this.logMaxBackupIndex = logMaxBackupIndex;
	}

	/**
	 * ログファイル名込みのパスを取得する。
	 * 
	 * @return ログファイル名込みのパス
	 */
	public String getLogFilePathName() {
		return this.getLogFilePath() + "/" + LOG_FILE_NAME;
	}

	/**
	 * ログファイルパスを取得する。
	 * 
	 * @return ログファイルパス
	 */
	public String getLogFilePath() {
		if (this.logFilePath != null) {
			return this.logFilePath;
		}
		String filePath = null;
		try {
			InitialContext ic = new InitialContext();
			filePath = (String) ic.lookup(LOG_FILE_PATH_JNDI_NAME);
		} catch (NamingException e) {
			filePath = LOG_FILE_DEFAULT_PATH;
		}
		return filePath;
	}
}
