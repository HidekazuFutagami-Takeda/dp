package jp.co.takeda.a.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

/**
 * メッセージキーを表すクラス<br>
 * 
 * @author tkawabata
 */
public class MessageKey implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7480750018760286961L;

	/**
	 * メッセージキーが指定されていない。
	 */
	public static final String ERROR_MSG = "メッセージキーが指定されていない。";

	/**
	 * キー値
	 */
	private final String key;

	/**
	 * キー値に対応したプレースホルダー(null可,変更不可)
	 */
	private final List<String> placeFolderList;

	/**
	 * コンストラクタ
	 * 
	 * @param key キー値
	 * @param srcHolderList プレースホルダーリスト
	 */
	public MessageKey(final String key, final List<String> srcHolderList) {
		Assert.notNull(key, ERROR_MSG);
		this.key = key;
		if (srcHolderList != null) {
			this.placeFolderList = Collections.unmodifiableList(srcHolderList);
		} else {
			this.placeFolderList = null;
		}
	}

	/**
	 * コンストラクタ
	 * 
	 * @param key キー値
	 * @param srcHolders プレースホルダー配列
	 */
	public MessageKey(final String key, final String... srcHolders) {
		Assert.notNull(key, ERROR_MSG);
		this.key = key;
		List<String> list = null;
		if ((srcHolders != null) && (srcHolders.length > 0)) {
			final int len = srcHolders.length;
			list = new ArrayList<String>(len);
			for (int i = 0; i < len; i++) {
				list.add(srcHolders[i]);
			}
			this.placeFolderList = Collections.unmodifiableList(list);
		} else {
			this.placeFolderList = null;
		}
	}

	/**
	 * キー値を取得する。
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * プレースホルダーを取得する。
	 * 
	 * @return プレースホルダー
	 */
	public List<String> getPlaceFolderList() {
		return placeFolderList;
	}
}
