package jp.co.takeda.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Compress;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ファイルに関するユーティリティクラス
 * 
 * @author tkawabata
 */
public abstract class FileUtil {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(FileUtil.class);

	/**
	 * 圧縮処理のバッファーサイズ
	 */
	private static int COMPRESS_BUFFER_SIZE = 1024;

	/**
	 * EOF
	 */
	protected static final int EOF = -1;

	/**
	 * 指定のファイルを圧縮する。
	 * 
	 * @param fileOrDir ファイル
	 * @param compress 圧縮形式
	 * @param delFlg 圧縮後ファイルを削除するか
	 */
	public static void compress(final File fileOrDir, final Compress compress, final boolean delFlg) {

		// -----------------------
		// 引数チェック
		// -----------------------
		if (fileOrDir == null) {
			final String errMsg = "指定ファイルまたはディレクトリがnull";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
		}
		if (!fileOrDir.canRead()) {
			final String errMsg = "指定ファイルまたはディレクトリの読取権限がない";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
		}
		if (compress == null) {
			final String errMsg = "圧縮形式がnull";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
		}
		if (!fileOrDir.isFile()) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("ファイル以外が指定されているため、圧縮不可");
			}
			return;
		}

		// -----------------------
		// 圧縮方式の決定
		// -----------------------
		FileOutputStream fos = null;
		DeflaterOutputStream dos = null;
		try {

			File comp = new File(fileOrDir.getAbsoluteFile() + compress.suffix);
			fos = new FileOutputStream(comp);
			switch (compress) {
				case ZIP:
					dos = new ZipOutputStream(fos);
					break;
				case GZIP:
					dos = new GZIPOutputStream(fos);
					break;
				default:
					String errMsg = "想定外の圧縮形式が指定されている。compress=" + compress;
					throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
			}

			// -----------------------
			// 圧縮処理
			// -----------------------
			boolean result = compressFile(fileOrDir, dos, compress);
			if (result && delFlg && fileOrDir.isFile()) {
				boolean successFlg = fileOrDir.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("compress result..." + successFlg);
				}
			}

		} catch (IOException e) {
			final String errMsg = "圧縮中にエラーが発生";
			throw new SystemException(new Conveyance(ErrMessageKey.IO_ERROR, errMsg), e);
		} finally {
			IOUtils.closeQuietly(dos);
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * 指定のファイルまたはディレクトリをZIP圧縮する。
	 * 
	 * @param fileOrDir ファイルまたはディレクトリ
	 * @param compress 圧縮形式
	 * @param fileDeleteFlg 圧縮後ファイルを削除するか
	 * @param dirDeleteFlg ディレクトリを削除するか
	 */
	public static void compressZip(final File fileOrDir, final boolean dirDeleteFlg) {

		// -----------------------
		// 引数チェック
		// -----------------------
		if (fileOrDir == null) {
			final String errMsg = "指定ファイルまたはディレクトリがnull";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
		}
		if (!fileOrDir.canRead()) {
			final String errMsg = "指定ファイルまたはディレクトリの読取権限がない";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
		}

		// -----------------------
		// 圧縮方式の決定
		// -----------------------
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		Compress compress = Compress.ZIP;
		try {

			File comp = new File(fileOrDir.getAbsoluteFile() + Compress.ZIP.suffix);
			fos = new FileOutputStream(comp);
			zos = new ZipOutputStream(fos);

			// -----------------------
			// 圧縮処理
			// -----------------------
			boolean result = false;
			if (fileOrDir.isFile()) {
				result = compressFile(fileOrDir, zos, compress);
			} else {
				result = compressFile(fileOrDir, null, zos, compress);
			}

			// ファイル削除
			if (result && dirDeleteFlg && fileOrDir.isFile()) {
				boolean successFlg = fileOrDir.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("compress result..." + successFlg);
				}
			}
			// ディレクトリ削除
			if (result && dirDeleteFlg && fileOrDir.isDirectory()) {
				// ディレクトリ内ファイル削除
				File[] fileList = fileOrDir.listFiles();
				for (File file : fileList) {
					file.delete();
				}
				boolean successFlg = fileOrDir.delete();
				if (LOG.isInfoEnabled()) {
					LOG.info("compress result..." + successFlg);
				}
			}

		} catch (IOException e) {
			final String errMsg = "圧縮中にエラーが発生";
			throw new SystemException(new Conveyance(ErrMessageKey.IO_ERROR, errMsg), e);
		} finally {
			IOUtils.closeQuietly(zos);
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * ファイルを圧縮する。
	 * 
	 * @param fileOrDir ファイルまたはディレクトリ
	 * @param dos 圧縮形式の出力ストリーム
	 * @param compress 圧縮形式
	 * @return 圧縮成功の場合に真
	 */
	private static boolean compressFile(File fileOrDir, DeflaterOutputStream dos, Compress compress) {
		boolean result = false;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(fileOrDir);
			bis = new BufferedInputStream(fis, COMPRESS_BUFFER_SIZE);
			int c;
			while ((c = bis.read()) != EOF) {
				dos.write((byte) c);
			}
			result = true;
		} catch (IOException e) {
			final String errMsg = "圧縮中にエラーが発生";
			throw new SystemException(new Conveyance(ErrMessageKey.IO_ERROR, errMsg), e);
		} finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
		return result;
	}

	/**
	 * ディレクトリを圧縮する。
	 * 
	 * @param fileOrDir ファイルまたはディレクトリ
	 * @param zos ZIP圧縮形式の出力ストリーム
	 * @param compress 圧縮形式
	 * @return 圧縮成功の場合に真
	 */
	private static boolean compressFile(File fileOrDir, String directoryPath, ZipOutputStream zos, Compress compress) {
		boolean result = false;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		// ファイルか判定
		if (fileOrDir.isFile()) {
			// ファイルを圧縮
			try {
				zos.putNextEntry(new ZipEntry(makePath(directoryPath, fileOrDir.getName())));
				result = compressFile(fileOrDir, zos, compress);
			} catch (IOException e) {
				final String errMsg = "ディレクトリ圧縮中にエラーが発生";
				throw new SystemException(new Conveyance(ErrMessageKey.IO_ERROR, errMsg), e);
			} finally {
				IOUtils.closeQuietly(bis);
				IOUtils.closeQuietly(fis);
			}
			// ディレクトリか判定
		} else if (fileOrDir.isDirectory()) {
			// ディレクトリを圧縮
			File[] fileArray = fileOrDir.listFiles();
			for (File file : fileArray) {
				result = compressFile(file, directoryPath, zos, compress);
			}
		}
		return result;
	}

	/**
	 * パスを生成する
	 * 
	 * @param directoryPath
	 * @param fileName
	 * @return ディレクトリパス
	 */
	private static String makePath(String directoryPath, String fileName) {
		return StringUtils.isEmpty(directoryPath) ? fileName : directoryPath + File.separator + fileName;
	}

	/**
	 * 正規表現のパターンにマッチするファイルを取得する。
	 * 
	 * @param regex 正規表現
	 * @return 正規表現にマッチするファイルかを判定するファイル名フィルター
	 */
	public static FilenameFilter getFileRegexFilter(String regex) {
		final String regex_ = regex;
		return new FilenameFilter() {
			public boolean accept(File file, String name) {
				boolean ret = name.matches(regex_);
				return ret;
			}
		};
	}
}
