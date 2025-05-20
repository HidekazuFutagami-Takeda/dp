package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.DateUtil;

/**
 * 業務連絡用メッセージを生成するロジッククラス
 *
 * @author khashimoto
 */
public class CreateOperationsMsgLogic {

	/**
	 * 日付フォーマット
	 */
	private static final String datePattern = "yyyy/MM/dd HH:mm";

	/**
	 * 正常メッセージ
	 */
	private static final String SUCCESS_MSG = "が正常に終了しました。<br>";

	/**
	 * 正常メッセージ(アテンション)
	 */
	private static final String SUCCESS_MSG_ATT = "が正常に終了しました。";

	/**
	 * 正常(配分ミス)メッセージ
	 */
	private static final String SUCCESS_DIST_MISS_MSG = "が正常に終了しましたが、配分ミスが発生しています。<br>";

	/**
	 * 正常(配分ミス)メッセージ(アテンション)
	 */
	private static final String SUCCESS_DIST_MISS_MSG_ATT = "が正常に終了しましたが、配分ミスが発生しています。";

	/**
	 * 異常メッセージ
	 */
	private static final String FAILURE_MSG = "が異常終了しました。<br>";

	/**
	 * 異常メッセージ(アテンション)
	 */
	private static final String FAILURE_MSG_ATT = "が異常終了しました。";

	/**
	 * お知らせ用メッセージ（異常時）
	 */
	private static final String FAILURE_ANNOUNCE_MSG = "「処理が異常終了しました。ヘルプディスクにお問い合わせください」";

	/**
	 * アテンション用メッセージ
	 */
	private static final String ATT_MSG = "詳細は納入計画システム支援トップのお知らせで確認ください。";

	/**
	 * 実行者情報
	 */
	private final DpUser dpUser;

	/**
	 * 業務連絡の処理区分
	 */
	private final ContactOperationsType contactOperationsType;

	/**
	 * 業務連絡の処理結果区分
	 */
	private final ContactResultType resultType;

	/**
	 * 時間通知メッセージ
	 */
	private final String timeMsg;

	/**
	 * 実行者・処理名
	 */
	private final String proName;

	/**
	 * 品目名称リスト
	 */
	private final List<String> prodNameList;

	/**
	 * コンストラクタ
	 *
	 * @param entryDto 業務連絡の登録用DTO
	 */
	public CreateOperationsMsgLogic(ContactOperationsEntryDto entryDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (entryDto == null) {
			final String errMsg = "業務連絡の登録用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (entryDto.getContactOperationsType() == null) {
			final String errMsg = "業務連絡の処理区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (entryDto.getContactResultType() == null) {
			final String errMsg = "業務連絡の処理結果がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 実行者名は、代行の場合は、代行ユーザ名(仕様)
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		if (dpUserInfo != null && dpUserInfo.isAltUserSetting()) {
			this.dpUser = dpUserInfo.getSettingUser();
		} else {
			this.dpUser = entryDto.getDpUser();
		}
		this.contactOperationsType = entryDto.getContactOperationsType();
		this.resultType = entryDto.getContactResultType();
		this.prodNameList = entryDto.getProdNameList();
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		if(entryDto.getCategory() == null){
			this.proName = dpUser.getJgiName() + "が実行した" + contactOperationsType.getValue();
		}else{	//カテゴリーがnullで無い場合(＝配分処理のお知らせ・アテンション)はカテゴリ名を付与する
			this.proName = dpUser.getJgiName() + "が実行した" + contactOperationsType.getValue() + "(" + entryDto.getCategory() + ")";
		}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		Date startTime = entryDto.getStartTime();
		Date endTime = entryDto.getEndTime();
		String start = startTime != null ? DateUtil.toString(startTime, datePattern) : "";
		String end = endTime != null ? DateUtil.toString(endTime, datePattern) : "";
		this.timeMsg = "開始時間:" + start + " - 終了時間:" + end;
	}

	/**
	 * お知らせメッセージを生成する。
	 *
	 * @return お知らせメッセージ
	 * @throws DataNotFoundException
	 */
	public String createAnnouceMsg() {
		String msg = null;
		switch (resultType) {
			case SUCCESS:
				msg = proName + SUCCESS_MSG + timeMsg;
				break;
			case SUCCESS_DIST_MISS:
				msg = proName + SUCCESS_DIST_MISS_MSG + timeMsg;
				break;
			case FAILURE:
				msg = proName + FAILURE_MSG + createErrMsg() + "<br>" + timeMsg;
				break;
		}

		if (msg == null) {
			final String errMsg = "お知らせメッセージ生成に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		return msg;
	}

	/**
	 * アテンションメッセージを生成する。
	 *
	 * @return アテンションメッセージ
	 * @throws DataNotFoundException
	 */
	public String createAttMsg() {
		String msg = null;
		switch (resultType) {
			case SUCCESS:
				msg = proName + SUCCESS_MSG_ATT + ATT_MSG;
				break;
			case SUCCESS_DIST_MISS:
				msg = proName + SUCCESS_DIST_MISS_MSG_ATT + ATT_MSG;
				break;
			case FAILURE:
				msg = proName + FAILURE_MSG_ATT + ATT_MSG;
				break;
		}

		if (msg == null) {
			final String errMsg = "アテンションメッセージ生成に失敗";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		return msg;
	}

	/**
	 * エラーメッセージを生成する。
	 *
	 * @return エラーメッセージ
	 */
	protected String createErrMsg() {
		// 品目名リストがある場合、品目名を表示
		if (prodNameList != null && prodNameList.size() != 0) {
			String msg = null;
			msg = "「下記品目の処理に失敗しました。<br>";
			for (int i = 0; i < prodNameList.size(); i++) {
				if (i == 0) {
					msg = msg + prodNameList.get(i);
				} else {
					msg = msg + "、" + prodNameList.get(i);
				}
			}
			return msg + "」";
		} else {
			return FAILURE_ANNOUNCE_MSG;
		}
	}
}
