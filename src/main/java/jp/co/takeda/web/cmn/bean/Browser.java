package jp.co.takeda.web.cmn.bean;

import java.io.Serializable;

/**
 * ブラウザを表すインターフェイス
 * 
 * @author tkawabata
 */
public interface Browser extends Serializable {

	/**
	 * ブラウザ名称を返す。
	 * 
	 * @return ブラウザ名称
	 */
	String getBrowserName();

	/**
	 * Httpレスポンスヘッダフィールド[content-disposition]を取得する。
	 * 
	 * @param mode content-dispositionモード
	 * @param fileName ファイル名称
	 * @return [content-disposition]に書き込むべき文字列
	 */
	String getContentDisposition(ContentDispositionMode mode, String fileName);
}
