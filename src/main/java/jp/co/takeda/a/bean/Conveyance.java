package jp.co.takeda.a.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsKakuteiErrMsg;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

/**
 * 伝達を表すクラス
 *
 * @author tkawabata
 */
public class Conveyance implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7442936525209103126L;

	/**
	 * メッセージキーが指定されていないことを示すエラーメッセージ
	 */
	public static final String ERROR_MSG = "メッセージキーが指定されていない";

	/**
	 * メッセージキー(null不可,変更不可)
	 */
	private final List<MessageKey> messageKeyList;

	/**
	 * メッセージ(null可)
	 */
	private final String message;

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 一括確定エラー情報のリスト
	 */
	private List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList;

//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	/**
	 * コンストラクタ
	 *
	 * @param messageKey メッセージキー
	 */
	public Conveyance(final MessageKey messageKey) {
		this(messageKey, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param messageKey メッセージキー
	 * @param message メッセージ
	 */
	public Conveyance(final MessageKey messageKey, final String message) {
		Assert.notNull(messageKey, ERROR_MSG);
		List<MessageKey> tmpList = new ArrayList<MessageKey>(1);
		tmpList.add(messageKey);
		messageKeyList = Collections.unmodifiableList(tmpList);
		this.message = message;
	}

	/**
	 * コンストラクタ
	 *
	 * @param messageKeyList メッセージキーリスト
	 */
	public Conveyance(final List<MessageKey> messageKeyList) {
		this(messageKeyList, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param messageKeyList メッセージキーリスト
	 * @param message メッセージ
	 */
	public Conveyance(final List<MessageKey> messageKeyList, final String message) {
		Assert.notEmpty(messageKeyList, ERROR_MSG);
		this.messageKeyList = Collections.unmodifiableList(messageKeyList);
		this.message = message;
	}

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * コンストラクタ
	 *
	 * @param messageKeyList メッセージキーリスト
	 * @param dpsKakuteiErrMsgList 一括確定エラー情報
	 */
	public Conveyance(final List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList, final List<MessageKey> messageKeyList) {
		this(dpsKakuteiErrMsgList, messageKeyList, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param messageKeyList メッセージキーリスト
	 * @param dpsKakuteiErrMsgList 一括確定エラー情報
	 * @param message メッセージ
	 */
	public Conveyance(final List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList, final List<MessageKey> messageKeyList, final String message) {
		Assert.notEmpty(messageKeyList, ERROR_MSG);
		this.messageKeyList = Collections.unmodifiableList(messageKeyList);
		this.dpsKakuteiErrMsgList = dpsKakuteiErrMsgList;
		this.message = message;
	}
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	/**
	 * メッセージキーリストを取得する。
	 *
	 * @return メッセージキーリスト
	 */
	public List<MessageKey> getMessageKeyList() {
		return messageKeyList;
	}

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 一括確定エラー情報のリストを取得する。
	 *
	 * @return 一括確定エラー情報リスト
	 */
	public List<DpsKakuteiErrMsg> getDpsKakuteiErrMsgList() {
		return dpsKakuteiErrMsgList;
	}
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	/**
	 * メッセージを取得する。
	 *
	 * @return メッセージ
	 */
	public String getMessage() {
		return this.message;
	}
}
