package jp.co.takeda.web.cmn.bean;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import jp.co.takeda.a.exp.LogicalException;

/**
 * 出力結果を表すインターフェイス<br>
 * 
 * @author tkawabata
 */
public interface ExportResult extends Closeable {

	/**
	 * 名称を取得する。
	 * 
	 * @return 名称
	 */
	public String getName();

	/**
	 * 結果を出力する。
	 * 
	 * @param outputStream 出力対象のストリーム
	 * @throws IOException IO例外
	 * @throws LogicalException 論理例外
	 */
	public void export(OutputStream outputStream) throws IOException, LogicalException;
}
