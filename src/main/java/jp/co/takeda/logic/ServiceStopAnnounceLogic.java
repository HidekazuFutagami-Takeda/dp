package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Calendar;
import java.util.Locale;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.model.SysManage;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

/**
 * サービス停止を通知するアナウンスを出すかを判定するロジッククラス
 * 
 * @author tkawabata
 */
public class ServiceStopAnnounceLogic {

	/**
	 * サービス停止までのリミットミリ分
	 */
	private static final int BEF_TIME_MINIUTE = 60;

	/**
	 * サービス停止までのリミットミリ秒
	 */
	private static final long BEF_TIME_MILI_SECOND = BEF_TIME_MINIUTE * 60 * 1000;

	/**
	 * 納入計画システム管理
	 */
	private final SysManage sysManage;

	/**
	 * メッセージソース
	 */
	private MessageSource messageSource;

	/**
	 * コンストラクタ
	 * 
	 * @param sysManage 納入計画システム管理情報
	 * @param messageSource メッセージソース
	 */
	public ServiceStopAnnounceLogic(SysManage sysManage, MessageSource messageSource) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sysManage == null) {
			final String errMsg = "納入計画システム管理がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (messageSource == null) {
			final String errMsg = "メッセージソースがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.sysManage = sysManage;
		this.messageSource = messageSource;
	}

	/**
	 * アナウンスメッセージを取得する。
	 * 
	 * @return アナウンスメッセージ。アナウンスメッセージがない場合はNULLを返す。
	 */
	public String getAnnounceMessage() {

		String stopTime1 = sysManage.getServiceStopTime1();
		String stopTime2 = sysManage.getServiceStopTime2();

		if (StringUtils.isNotBlank(stopTime1) && stopTime1.length() == 5) {
			boolean announceFlg = isAnnounce(stopTime1);
			if (announceFlg) {
				String[] placeHolders = new String[] { stopTime1 };
				return messageSource.getMessage(Constants.SERVICE_STOP_MSG_KEY, placeHolders, Locale.getDefault());
			}
		}

		if (StringUtils.isNotBlank(stopTime2) && stopTime2.length() == 5) {
			boolean announceFlg = isAnnounce(stopTime2);
			if (announceFlg) {
				String[] placeHolders = new String[] { stopTime2 };
				return messageSource.getMessage(Constants.SERVICE_STOP_MSG_KEY, placeHolders, Locale.getDefault());
			}
		}

		// 必要ない場合はNULLを返す
		return null;
	}

	/**
	 * アナウンスが必要かを判定する。
	 * 
	 * @param stopTime 現在日時に設定する時分値([XX:XX]形式)
	 * @return アナウンスが必要な場合に真
	 */
	private boolean isAnnounce(String stopTime) {
		if (StringUtils.isNotBlank(stopTime) && stopTime.length() == 5) {
			Calendar now = Calendar.getInstance();
			Calendar set = Calendar.getInstance();
			int hh = Integer.parseInt(stopTime.substring(0, 2));
			int mm = Integer.parseInt(stopTime.substring(3));
			set.set(Calendar.HOUR_OF_DAY, hh);
			set.set(Calendar.MINUTE, mm);
			set.set(Calendar.SECOND, 0);
			set.set(Calendar.MILLISECOND, 0);
			set.getTime(); // 念のため

			long value = (set.getTimeInMillis() - now.getTimeInMillis());
			if (value >= 0 && value <= BEF_TIME_MILI_SECOND) {
				return true;
			}
		}
		return false;
	}
}
