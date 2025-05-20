package jp.co.takeda.task;

import java.io.File;
import java.util.Calendar;

import jp.co.takeda.bean.Compress;
import jp.co.takeda.bean.DpLoggingInfo;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * ログファイルをリストア(削除 OR 圧縮)するためのジョブクラス
 * 
 * @author tkawabata
 */
@Controller("logRestoreJob")
public class LogRestoreJob extends AbstractScheduledJob {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(LogRestoreJob.class);

	/**
	 * ロギング情報
	 */
	@Autowired(required = true)
	@Qualifier("loggingInfo")
	protected DpLoggingInfo loggingInfo;

	/**
	 * ログのリストア(削除 OR 圧縮)を行う。<br>
	 * 
	 * <pre>
	 * 対象のログファイルのパスは、{@link DpLoggingInfo#getLogFilePath()}より取得する。
	 * {@link DpLoggingInfo#getLogMaxBackupIndex()}で指定されている日数以上経過したログファイルは削除対象となる。
	 * 規定の書式で定義しているファイルのうち、圧縮されていないファイルは
	 * 削除対象のファイルは、{@link DpLoggingInfo#LOG_FILE_DATE_STRING}をプリフィックスに持つファイル名で構成されている。
	 * このファイル名を解析し、[処理日付 + getLogMaxBackupIndex()]の日付以上経過しているログファイルを削除する。
	 * </pre>
	 */
	@Override
	protected void execute() {

		if (LOG.isDebugEnabled()) {
			LOG.debug("logFilePath=" + loggingInfo.getLogFilePath());
			LOG.debug("logMaxBackupIndex=" + loggingInfo.getLogMaxBackupIndex());
		}

		// -----------------------------------------------
		// 引数チェック、状態チェック
		// -----------------------------------------------
		if (loggingInfo == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("loggingInfoがnull");
			}
			return;
		}
		String filePath = loggingInfo.getLogFilePath();
		Integer logBackUpIndex = loggingInfo.getLogMaxBackupIndex();

		if (filePath == null || logBackUpIndex == null) {
			if (LOG.isInfoEnabled()) {
				LOG.info("ファイルパス/バックアップ日数の指定がないため処理中止。filePath=" + filePath + "logBackUpIndex=" + logBackUpIndex);
			}
			return;
		}

		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("対象パスを表すディレクトリが存在しないか、ディレクトリではない。filePath=" + filePath);
			}
			return;
		}

		if (!dir.canWrite()) {
			if (LOG.isWarnEnabled()) {
				LOG.warn(filePath + "の更新権限がないため、リストア出来ない。");
			}
			return;
		}

		File[] fileList = dir.listFiles(FileUtil.getFileRegexFilter(DpLoggingInfo.LOG_FILE_REGEX));
		if (fileList == null || fileList.length == 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info("正規表現に合致するファイルが存在しないため処理を中止。");
			}
			return;
		}

		// -----------------------------------------------
		// ここからリストア処理
		// -----------------------------------------------

		Calendar today = Calendar.getInstance();
		Calendar standDay = DateUtil.getXDay(today, -logBackUpIndex);
		standDay = DateUtil.truncateTime(standDay);
		if (LOG.isDebugEnabled()) {
			LOG.debug("standDay=" + DateUtil.toString(standDay, "yyyy-MM-dd hh:mm:ss"));
		}

		for (File file : fileList) {
			String fileName = file.getName();
			if (StringUtils.isBlank(fileName)) {
				continue;
			}
			if (!fileName.matches(DpLoggingInfo.LOG_FILE_REGEX)) {
				continue;
			}

			String[] targetList = fileName.split("\\.");
			if (targetList == null || !(targetList.length == 3 || targetList.length == 4)) {
				continue;
			}
			String targetString = targetList[2];
			Calendar fileDate = DateUtil.toCalendar(targetString, DpLoggingInfo.LOG_FILE_DATE_PATTERN);
			if (LOG.isDebugEnabled()) {
				LOG.debug("fileDate=" + DateUtil.toString(fileDate, "yyyy-MM-dd hh:mm:ss"));
			}

			if (fileDate.before(standDay)) {
				if (LOG.isInfoEnabled()) {
					LOG.info("削除ファイル。fileName=" + fileName);
				}
				boolean isDelete = file.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("削除結果。isDelete=" + isDelete);
				}
			} else if (!fileName.endsWith(DpLoggingInfo.LOG_GZIP_SUFFIX)) {
				if (LOG.isInfoEnabled()) {
					LOG.info("圧縮対象ファイル。fileName=" + fileName);
				}
				FileUtil.compress(file, Compress.GZIP, true);
			}
		}
	}
}
