package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.IO_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.web.cmn.bean.ExportResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ファイル作成サービス実装クラス
 * 
 * @author nozaki
 */
@Transactional
@Service("dpcFileCreateService")
public class DpcFileCreateServiceImpl implements DpcFileCreateService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpcFileCreateServiceImpl.class);

	// ファイル作成
	public void createFile(String outputDir, ExportResult exportResult) {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (outputDir == null) {
			final String errMsg = "出力先ディレクトリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (exportResult == null) {
			final String errMsg = "出力結果がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		final String fileName = exportResult.getName();
		BufferedOutputStream stream = null;
		try {
			stream = new BufferedOutputStream(new FileOutputStream(new File(outputDir, fileName)));
			exportResult.export(stream);

		} catch (Exception e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("ファイル作成に失敗、ファイル=" + fileName);
			}
			throw new SystemException(new Conveyance(new MessageKey("DPS1035E")), e);

		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					if (LOG.isWarnEnabled()) {
						LOG.warn("ストリームのクローズに失敗");
					}
					throw new SystemException(new Conveyance(IO_ERROR), e);
				}
			}
		}
	}
}
