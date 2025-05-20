package jp.co.takeda.web.cmn.action;

import static jp.co.takeda.a.exp.ErrMessageKey.FILE_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.IO_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.a.web.exp.ErrMessageKeyExt.BROWSER_CANCEL_ERROR;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.web.cmn.bean.Browser;
import jp.co.takeda.web.cmn.bean.ContentDispositionMode;
import jp.co.takeda.web.cmn.bean.Downloadable;
import jp.co.takeda.web.cmn.bean.ExportResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;

/**
 * アクションクラスのユーティリティ
 * 
 * @author tkawabata
 */
public class ActionUtil {

	/**
	 * StrutsのFormFileから入力ストリームを取得する。<br>
	 * 
	 * @param formFile StrutsFormFile
	 * @return 入力ストリーム
	 */
	public static InputStream getInputStream(FormFile formFile) {
		InputStream in = null;
		try {
			in = formFile.getInputStream();
		} catch (FileNotFoundException e) {
			IOUtils.closeQuietly(in);
			final String errMsg = "アップロードファイルが存在しない";
			throw new SystemException(new Conveyance(FILE_NOT_FOUND_ERROR, errMsg), e);
		} catch (IOException e) {
			IOUtils.closeQuietly(in);
			final String errMsg = "ファイルアップロード時のエラー";
			throw new SystemException(new Conveyance(IO_ERROR, errMsg), e);
		} finally {
			if (formFile != null) {
				formFile.destroy();
			}
		}
		return in;
	}

	/**
	 * レスポンスストリームにダウンロード対象の書き込み処理を行う。<br>
	 * 
	 * @param downLoadable ダウンロードインターフェイスを実装したBean
	 * @param response レスポンス情報
	 * @throws IOException IO例外
	 * @throws LogicalException ダウンロードキャンセル時にスロー
	 */
	public static void writeStream(final Downloadable downLoadable, final HttpServletResponse response) throws IOException, LogicalException {
		final OutputStream responceOutputStream = response.getOutputStream();
		ExportResult exportResult = null;
		try {
			final String downLoadFileName = downLoadable.getDownLoadFileName();
			final Browser browser = downLoadable.getBrowser();
			final int contentLength = downLoadable.getContentLength();
			response.setContentType(downLoadable.getContentType().getContentsType());
			if (contentLength != 0) {
				response.setContentLength(contentLength);
			}
			if (StringUtils.isBlank(downLoadFileName)) {
				final String errMsg = "ダウンロードファイル名称が設定されていない";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (browser == null) {
				final String errMsg = "ブラウザが指定されていない";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			final ContentDispositionMode mode = downLoadable.getContentDispositionMode();
			final String disposition = "Content-disposition";
			response.addHeader(disposition, browser.getContentDisposition(mode, downLoadFileName));
			exportResult = downLoadable.getExportResult();
			try {
				exportResult.export(responceOutputStream);
			}
			// ダウンロードキャンセル時対応1
			catch (IndexOutOfBoundsException e) {
				// ログにでてもしょうがないので、例外クラスは渡さない
				final String errMsg = "ダウンロード時のキャンセルボタン押下のエラー";
				throw new LogicalException(new Conveyance(BROWSER_CANCEL_ERROR, errMsg));
			}

			// ダウンロードキャンセル時対応2
			catch (IOException e) {
				// ログにでてもしょうがないので、例外クラスは渡さない
				final String errMsg = "ダウンロード時のキャンセルボタン押下のエラー";
				throw new LogicalException(new Conveyance(BROWSER_CANCEL_ERROR, errMsg));
			}
		} finally {
			if (exportResult != null) {
				exportResult.close();
			}

			// 全部吐き出す
			responceOutputStream.flush();

			// レスポンスのクローズ処理(本来、コンテナが判断するべきことだが）
			IOUtils.closeQuietly(responceOutputStream);
		}
	}
}
