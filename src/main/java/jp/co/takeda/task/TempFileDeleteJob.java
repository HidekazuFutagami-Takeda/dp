package jp.co.takeda.task;

import java.io.File;

import jp.co.takeda.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * 添付ファイルを削除するためのジョブクラス
 *
 * @author tkawabata
 */
@Controller("tempFileDeleteJob")
public class TempFileDeleteJob extends AbstractScheduledJob {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(TempFileDeleteJob.class);

	/**
	 * 関係会社帳票を特定するためのファイル名(正規表現)
	 */
	public static final String TEMP_FILE_SLIST_REGEX = "SList.*";

	/**
	 * ①担当者別品目別計画一覧を特定するためのファイル名(正規表現)
	 */
	public static final String TEMP_FILE_MMPLIST_REGEX = "ProdList.*";

	/**
	 * ②品目別担当者別計画検討表および③担当者別品目別計画検討表を特定するためのファイル名(正規表現)
	 */
	public static final String TEMP_FILE_REVIEW_REGEX = "Review.*";

	/**
	 * ④チーム担当者計画検討表帳票を特定するためのファイル名(正規表現)
	 */
	public static final String TEMP_FILE_TEAM_MR_REGEX = "TeamMR.*";

	/**
	 * ダウンロードファイルディレクトリ
	 */
	@Autowired(required = true)
	@Qualifier("downloadFileTempDir")
	protected String downloadFileTempDir;

	/**
	 * 添付ファイルを削除を行う。
	 */
	@Override
	protected void execute() {

		if (LOG.isDebugEnabled()) {
			LOG.debug("downloadFileTempDir=" + downloadFileTempDir);
		}

		// -----------------------------------------------
		// 引数チェック、状態チェック
		// -----------------------------------------------
		if (downloadFileTempDir == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("downloadFileTempDirがnull");
			}
			return;
		}

		File dir = new File(downloadFileTempDir);
		if (!dir.exists() || !dir.isDirectory()) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("対象パスを表すディレクトリが存在しないか、ディレクトリではない。downloadFileTempDir=" + downloadFileTempDir);
			}
			return;
		}

		if (!dir.canWrite()) {
			if (LOG.isWarnEnabled()) {
				LOG.warn(downloadFileTempDir + "の更新権限がないため、削除出来ない。");
			}
			return;
		}

		// -----------------------------------------------
		// 関係会社帳票
		// -----------------------------------------------

		File[] sListFileList = dir.listFiles(FileUtil.getFileRegexFilter(TEMP_FILE_SLIST_REGEX));
		if (sListFileList == null || sListFileList.length == 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info("正規表現に合致する関係会社帳票が存在しないため処理を中止。");
			}
		}

		// 対象ファイルが存在する場合
		else {
			for (File file : sListFileList) {
				String fileName = file.getName();
				if (StringUtils.isBlank(fileName)) {
					continue;
				}
				if (!fileName.matches(TEMP_FILE_SLIST_REGEX)) {
					continue;
				}
				boolean isDelete = file.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("関係会社帳票削除結果,File=" + file.getName() + ",isDelete=" + isDelete);
				}
			}
		}

		// ------------------------------------------------------------
		// ①担当者別品目別計画一覧
		// ------------------------------------------------------------

		File[] mmplistFileList = dir.listFiles(FileUtil.getFileRegexFilter(TEMP_FILE_MMPLIST_REGEX));
		if (mmplistFileList == null || mmplistFileList.length == 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info("正規表現に合致する担当者別品目別計画一覧が存在しないため処理を中止。");
			}
		}

		// 対象ファイルが存在する場合
		else {
			for (File file : mmplistFileList) {
				String fileName = file.getName();
				if (StringUtils.isBlank(fileName)) {
					continue;
				}
				if (!fileName.matches(TEMP_FILE_MMPLIST_REGEX)) {
					continue;
				}
				boolean isDelete = file.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("担当者別品目別計画一覧削除結果,File=" + file.getName() + ",isDelete=" + isDelete);
				}
			}
		}

		// ------------------------------------------------------------
		// ②品目別担当者別計画検討表および③担当者別品目別計画検討表
		// ------------------------------------------------------------

		File[] reviewFileList = dir.listFiles(FileUtil.getFileRegexFilter(TEMP_FILE_REVIEW_REGEX));
		if (reviewFileList == null || reviewFileList.length == 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info("正規表現に合致する品目別担当者別計画検討表および担当者別品目別計画検討表が存在しないため処理を中止。");
			}
		}

		// 対象ファイルが存在する場合
		else {
			for (File file : reviewFileList) {
				String fileName = file.getName();
				if (StringUtils.isBlank(fileName)) {
					continue;
				}
				if (!fileName.matches(TEMP_FILE_REVIEW_REGEX)) {
					continue;
				}
				boolean isDelete = file.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("品目別担当者別計画検討表および担当者別品目別計画検討表削除結果,File=" + file.getName() + ",isDelete=" + isDelete);
				}
			}
		}

		// -----------------------------------------------
		// ④チーム担当者計画検討表帳票
		// -----------------------------------------------

		File[] teamMrFileList = dir.listFiles(FileUtil.getFileRegexFilter(TEMP_FILE_TEAM_MR_REGEX));
		if (teamMrFileList == null || teamMrFileList.length == 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info("正規表現に合致するチーム担当者計画検討表帳票が存在しないため処理を中止。");
			}
		}

		// 対象ファイルが存在する場合
		else {
			for (File file : teamMrFileList) {
				String fileName = file.getName();
				if (StringUtils.isBlank(fileName)) {
					continue;
				}
				if (!fileName.matches(TEMP_FILE_TEAM_MR_REGEX)) {
					continue;
				}
				boolean isDelete = file.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("チーム担当者計画検討表帳票削除結果,File=" + file.getName() + ",isDelete=" + isDelete);
				}
			}
		}
	}
}
