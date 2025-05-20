package jp.co.takeda.web.cmn.bean;

/**
 * ダウンロード可能であることを示すインターフェイス
 * 
 * @author tkawabata
 */
public interface Downloadable {

	/**
	 * デフォルトのファイル名称
	 */
	static final String DEF_DOWNLOAD_FILE_NAME = "file";

	/**
	 * デフォルトのコンテンツタイプ
	 */
	static final int DEF_LENGTH = 0;

	/**
	 * デフォルトのコンテンツタイプ
	 */
	static final ContentsType DEF_CONTENTS_TYPE = ContentsType.TXT;

	/**
	 * デフォルトのブラウザ
	 */
	static final Browser DEF_BROWSER = new InternetExplorerBrowser();

	/**
	 * UserAgentのヘッダ名称
	 */
	static final String USER_AGENT_HEADER_NAME = "user-agent";

	/**
	 * InternetExplorer UserAgent
	 */
	static final String INTERNETEXPLORE_USER_AGENT = "MSIE";

	/**
	 * content-dispositionモード
	 */
	static final ContentDispositionMode DEF_CONTENT_DISPOSITION_MODE = ContentDispositionMode.ATTACHMENT;

	/**
	 * content-dispositionモードを取得する。<br>
	 * 
	 * @return content-dispositionモード
	 */
	ContentDispositionMode getContentDispositionMode();

	/**
	 * ダウンロードファイル名称を取得する。<br>
	 * 
	 * @return ダウンロードファイル名称
	 */
	String getDownLoadFileName();

	/**
	 * ブラウザを取得する。<br>
	 * 
	 * @return ブラウザ
	 */
	Browser getBrowser();

	/**
	 * コンテンツレングスを取得する。<br>
	 * 
	 * @return コンテンツレングス
	 */
	int getContentLength();

	/**
	 * コンテンツタイプを取得する。<br>
	 * 
	 * @return コンテンツタイプ
	 */
	ContentsType getContentType();

	/**
	 * 出力対象を取得する。<br>
	 * 
	 * @return 出力対象
	 */
	ExportResult getExportResult();
}
