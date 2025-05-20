package jp.co.takeda.service;

import jp.co.takeda.web.cmn.bean.ExportResult;

/**
 * ファイル作成サービスインターフェイス
 * 
 * @author nozaki
 */
public interface DpcFileCreateService {

	/**
	 * ファイルを作成する。
	 * 
	 * @param outputDir 出力先ディレクトリ
	 * @param exportResult 出力結果
	 */
	void createFile(String outputDir, ExportResult exportResult);
}
