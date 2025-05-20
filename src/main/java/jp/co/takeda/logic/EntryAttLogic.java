package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.AttAddrGrpIfDao;
import jp.co.takeda.dao.AttBaseIfDao;
import jp.co.takeda.dao.AttLinksIfDao;
import jp.co.takeda.dao.CalDao;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.model.AttAddrGrpIf;
import jp.co.takeda.model.AttBaseIf;
import jp.co.takeda.model.AttLinksIf;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.AttSystemKey;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.security.DpUser;

/**
 * アテンション登録ロジッククラス
 *
 * @author khashimoto
 */
public class EntryAttLogic {

	/**
	 * 件名
	 */
	private static final String SUBJECT = "納入計画支援システムより、お知らせがあります";

	/**
	 * 医薬支援
	 */
	private static final String DPS_IYAKU_PATH = "/dp/app/dps000C00F00";

	/**
	 * ワクチン支援
	 */
	private static final String DPS_VAC_PATH = "/dp/app/dps000C00F50";

	/**
	 * Attention基本I/FDAO
	 */
	private final AttBaseIfDao attBaseIfDao;

	/**
	 * Attention外部リンク情報I/FDAO
	 */
	private final AttLinksIfDao attLinksIfDao;

	/**
	 * Attention宛先グループ指定I/FDAO
	 */
	private final AttAddrGrpIfDao attAddrGrpIfDao;

	/**
	 * カレンダーDAO
	 */
	private final CalDao calDao;

	/**
	 * アテンション通知対象の従業員情報
	 */
	private final List<JgiMst> attJgiList;

	/**
	 * 実行者情報
	 */
	private final DpUser dpUser;

	/**
	 * メッセージ内容
	 */
	private final String message;

	/**
	 * 管理対象業務分類(支援/管理)
	 */
	private final SysClass sysClass;

	/**
	 * 外部システム一意キー
	 */
	private final AttSystemKey ifPKey;

	/**
	 * 外部システムID
	 */
	private final Integer ifSysKey;

	/**
	 * 外部リンクURL
	 */
	private final String url;

	/**
	 * 終了日時
	 */
	private final Date endTime;

	/**
	 * 掲載期間日数(各処理ともに4日)
	 */
	private final Integer naviDay = 4;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param attBaseIfDao Attention基本I/FDAO
	 * @param attLinksIfDao Attention外部リンク情報I/FDAO
	 * @param attAddrGrpIfDao Attention宛先グループ指定I/FDAO
	 * @param calDao カレンダーDAO
	 * @param attJgiList アテンション通知対象の従業員情報
	 * @param message メッセージ内容
	 * @param entryDto 業務連絡の登録用DTO
	 */
	public EntryAttLogic(AttBaseIfDao attBaseIfDao, AttLinksIfDao attLinksIfDao, AttAddrGrpIfDao attAddrGrpIfDao, CalDao calDao, List<JgiMst> attJgiList, String message,
		ContactOperationsEntryDto entryDto) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (attJgiList == null || attJgiList.size() == 0) {
			final String errMsg = "アテンション通知対象の従業員情報がなし";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (message == null || message.length() == 0) {
			final String errMsg = "メッセージ内容がなし";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (entryDto == null) {
			final String errMsg = "業務連絡の処理区分がなし";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.attBaseIfDao = attBaseIfDao;
		this.attLinksIfDao = attLinksIfDao;
		this.attAddrGrpIfDao = attAddrGrpIfDao;
		this.calDao = calDao;
		this.attJgiList = attJgiList;
		this.dpUser = entryDto.getDpUser();
		this.message = message;
		this.ifSysKey = attBaseIfDao.getSeqKey();
		// URLの設定
		switch (entryDto.getContactOperationsType()) {
			// 医薬支援
			case MR_PLAN_EST:
			case INS_WS_DIST:
			case INS_WS_DIST_SHIIRE:
			case WS_DIST:
			case WS_SLIDE_IYAKU:
			case INS_DOC_DIST_MMP:
			case INS_DOC_RE_DIST_MMP:
				this.url = DPS_IYAKU_PATH;
				this.sysClass = SysClass.DPS;
				break;
			// 医薬ワクチン
			case INS_WS_DIST_VAC:
			case MR_PLAN_EST_VAC:
			case WS_SLIDE_VAC:
				this.url = DPS_VAC_PATH;
				this.sysClass = SysClass.DPS;
				break;
			default:
				throw new SystemException(new Conveyance(PARAMETER_ERROR, "外部リンクURLの設定失敗"));
		}
		// 外部システム一意キーの設定
		switch (this.sysClass) {
			case DPS:
				this.ifPKey = AttSystemKey.DPS_KEY;
				break;
			case DPM:
				this.ifPKey = AttSystemKey.DPM_KEY;
				break;
			default:
				throw new SystemException(new Conveyance(PARAMETER_ERROR, "一意キーの設定失敗"));
		}
		this.endTime = entryDto.getEndTime();
	}

	/**
	 * 登録処理を実行する。
	 *
	 * @throws DuplicateException
	 */
	public void execute() throws DuplicateException {

		// ----------------------
		// Attention基本I/Fを生成
		// ----------------------
		AttBaseIf attBaseIf = new AttBaseIf();
		attBaseIf.setIfPKey(this.ifPKey);
		attBaseIf.setIfSysKey(this.ifSysKey);
		attBaseIf.setSubject(SUBJECT);
		attBaseIf.setConTxt(message);
		attBaseIf.setNaviStartDay(this.endTime);
		// 掲載終了日を取得する。
		try {
			Date date = calDao.searchBusinessDay(this.endTime, this.naviDay);
			attBaseIf.setNaviEndDay(date);
		} catch (DataNotFoundException e) {
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, "営業日の取得失敗"));
		}
		attBaseIf.setUpJgiNo(dpUser.getJgiNo());
		attBaseIf.setUpScrnId(this.sysClass.getName());
		attBaseIf.setUpFuncId(this.sysClass.getName());
		attBaseIf.setIsDate(this.endTime);
		attBaseIf.setUpDate(this.endTime);

		// ----------------------------------
		// Attention宛先グループ指定I/Fを生成
		// ----------------------------------
		AttAddrGrpIf attAddrGrpIf = new AttAddrGrpIf();
		attAddrGrpIf.setIfPKey(this.ifPKey);
		attAddrGrpIf.setIfSysKey(this.ifSysKey);
		attAddrGrpIf.setUpJgiNo(dpUser.getJgiNo());
		attAddrGrpIf.setUpScrnId(this.sysClass.getName());
		attAddrGrpIf.setUpFuncId(this.sysClass.getName());

		// --------------------------------
		// Attention外部リンク情報I/Fを生成
		// --------------------------------
		AttLinksIf attLinksIf = new AttLinksIf();
		attLinksIf.setIfPKey(this.ifPKey);
		attLinksIf.setIfSysKey(this.ifSysKey);
		attLinksIf.setExtLink(this.url);

		// Attention基本I/F登録
		attBaseIfDao.insert(attBaseIf);

		// Attention宛先グループ指定I/F登録
		int addrCnt = 1;
		for (JgiMst jgiMst : attJgiList) {
			attAddrGrpIf.setIfGropSeq(addrCnt);
			attAddrGrpIf.setAddrJgiNo(jgiMst.getJgiNo());
			attAddrGrpIfDao.insert(attAddrGrpIf);
			addrCnt++;
		}

		// Attention外部リンク情報I/F登録
		attLinksIfDao.insert(attLinksIf);
	}
}
