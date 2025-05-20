package jp.co.takeda.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyancable;
import jp.co.takeda.a.bean.MessageKey;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;

/**
 * 例外関連のユーティリティクラス
 * 
 * @author tkawabata
 */
public abstract class ExceptionUtil {

	/**
	 * DataAccessExceptionから発生元の例外クラスを抽出する。<br>
	 * (SQLExceptionを抽出する)
	 * 
	 * @param e DataAccessException
	 * @return CauseされたExceptionのうち、欲しい例外
	 */
	public static Exception findException(DataAccessException e) {

		// 楽観的ロック例外等、ユーザが生成したDataAccessException
		Throwable t1 = e.getCause();
		if (t1 == null) {
			return e;
		}

		// iBatisレベルでの例外、その他
		Throwable t2 = t1.getCause();
		if (t2 == null) {
			if (t1 instanceof Exception) {
				return (Exception) t1;
			} else {
				return e;
			}
		}

		// SQLException
		if (t2 instanceof Exception) {
			return (Exception) t2;
		} else {
			if (t1 instanceof Exception) {
				return (Exception) t1;
			} else {
				return e;
			}
		}
	}

	/**
	 * 例外メッセージを生成する。
	 * 
	 * @param e 例外
	 * @return メッセージ
	 */
	public static String buildLogMessage(final Exception e) {
		StringBuilder sb = new StringBuilder();

		final String delim = " ";
		if (e instanceof Conveyancable) {
			sb.append(((Conveyancable) e).getConveyance().getMessage() + delim);
		}
		sb.append(delim + ExceptionUtils.getStackTrace(e));
		return sb.toString();
	}

	/**
	 * 重複メッセージを除去したメッセージリストを返す。
	 * 
	 * @param entryList 検査対象のメッセージリスト
	 * @return 重複メッセージを除去したメッセージリスト
	 */
	public static List<MessageKey> removeDuplicateMessage(List<MessageKey> entryList) {

		if (entryList == null || entryList.isEmpty() || entryList.size() == 1) {
			return entryList;
		}

		Map<String, MessageKey> entryMap = new LinkedHashMap<String, MessageKey>();
		for (MessageKey messageKey : entryList) {
			List<String> pfList = messageKey.getPlaceFolderList();
			if (pfList == null) {
				entryMap.put(messageKey.getKey(), messageKey);
			} else {
				StringBuffer sb = new StringBuffer(messageKey.getKey());
				for (String value : pfList) {
					sb.append(value);
				}
				entryMap.put(sb.toString(), messageKey);
			}
		}

		List<MessageKey> returnList = new ArrayList<MessageKey>();
		Collection<MessageKey> col = entryMap.values();
		for (MessageKey key : col) {
			returnList.add(key);
		}
		return returnList;
	}
}
