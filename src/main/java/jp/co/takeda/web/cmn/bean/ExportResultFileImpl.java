package jp.co.takeda.web.cmn.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * ExportResultのファイル出力実装
 * 
 * @author tkawabata
 */
public class ExportResultFileImpl implements ExportResult {

	/**
	 * ファイル名称
	 */
	private final String fileName;

	/**
	 * ファイル
	 */
	private final File file;

	/**
	 * コンストラクタ
	 * 
	 * @param fileName 出力するファイル名称
	 * @param exportFile 出力する入力ストリーム
	 */
	public ExportResultFileImpl(File file) {
		this.fileName = file.getName();
		this.file = file;
	}

	/**
	 * ファイル名称を返す。
	 * 
	 * @return ファイル名称
	 * @see jp.co.takeda.web.cmn.bean.ExportResult#getName()
	 */
	public String getName() {
		return this.fileName;
	}

	/**
	 * 出力対象のストリームをファイルに出力する。<br>
	 * 
	 * @param outputStream 出力対象のストリーム
	 * @throws IOException IO例外
	 */
	public void export(final OutputStream outputStream) throws IOException {
		BufferedInputStream fis = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(file));
			final byte[] oBuff = new byte[2048];
			int iSize;

			while ((-1 != (iSize = fis.read(oBuff))) && (iSize != 0)) {
				outputStream.write(oBuff, 0, iSize);
			}
			outputStream.flush();
		} finally {
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 * リソースの開放処理を行う。<br>
	 * 
	 * @throws IOException IO例外
	 * @see java.io.Closeable#close()
	 */
	public void close() throws IOException {
	}
}
