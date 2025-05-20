package jp.co.takeda.bean;

import jp.co.takeda.security.DpMetaInfo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 非同期処理用ＤＴＯクラス
 * 
 * @author tkawabata
 */
public class DpAsyncDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * メタ情報
	 */
	private DpMetaInfo dpMetaInfo;

	/**
	 * コンストラクタ
	 * 
	 * @param dpMetaInfo メタ情報
	 */
	public DpAsyncDto(DpMetaInfo dpMetaInfo) {
		this.dpMetaInfo = dpMetaInfo;
	}

	/**
	 * ログ出力文字列を取得する。
	 * 
	 * @return ログ出力文字列
	 */
	public String getLogString() {
		return dpMetaInfo.getLogString().toString();
	}

	/**
	 * サーバ区分を取得する。
	 * 
	 * @return サーバ区分
	 */
	public String getAppServerKbn() {
		return dpMetaInfo.getAppServerKbn();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
