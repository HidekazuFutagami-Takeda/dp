package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.AnnounceDao;
import jp.co.takeda.dao.AttAddrGrpIfDao;
import jp.co.takeda.dao.AttBaseIfDao;
import jp.co.takeda.dao.AttLinksIfDao;
import jp.co.takeda.dao.CalDao;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.logic.CreateOperationsMsgLogic;
import jp.co.takeda.logic.EntryAttLogic;
import jp.co.takeda.logic.SearchAnnounceJgiListLogic;
import jp.co.takeda.logic.SearchAttJgiListLogic;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.model.Announce;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 業務連絡に関するサービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpsContactOperationsService")
public class DpsContactOperationsServiceImpl implements DpsContactOperationsService {

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * お知らせ情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("announceDao")
	protected AnnounceDao announceDao;

	/**
	 * 出力ファイル情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("outputFileDao")
	protected OutputFileDao outputFileDao;

	/**
	 * Attention基本I/FDAO
	 */
	@Autowired(required = true)
	@Qualifier("attBaseIfDao")
	protected AttBaseIfDao attBaseIfDao;

	/**
	 * Attention外部リンク情報I/FDAO
	 */
	@Autowired(required = true)
	@Qualifier("attLinksIfDao")
	protected AttLinksIfDao attLinksIfDao;

	/**
	 * Attention宛先グループ指定I/FDAO
	 */
	@Autowired(required = true)
	@Qualifier("attAddrGrpIfDao")
	protected AttAddrGrpIfDao attAddrGrpIfDao;

	/**
	 * カレンダーDAO
	 */
	@Autowired(required = true)
	@Qualifier("calDao")
	protected CalDao calDao;

	// 業務連絡(お知らせ)を登録
	public void entryAnnounce(ContactOperationsEntryDto entryDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (entryDto == null) {
			final String errMsg = "業務連絡の登録用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		ContactOperationsType operationsType = entryDto.getContactOperationsType();
		DpUser dpUser = entryDto.getDpUser();
		if (operationsType == null) {
			final String errMsg = "処理区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ------------------------
		// お知らせ対象の従業員取得
		// ------------------------
		DpUser target = DpUserInfo.getDpUserInfo().getSettingUser();
		if (target == null) {
			target = dpUser;
		}
		SearchAnnounceJgiListLogic searchAnnounceJgiListLogic = new SearchAnnounceJgiListLogic(sosMstDAO, jgiMstDAO, target, operationsType, entryDto.getSosCd());
		List<JgiMst> annouceJgiList = searchAnnounceJgiListLogic.execute();

		// ------------------------
		// お知らせメッセージ作成
		// ------------------------
		CreateOperationsMsgLogic createAnnounceMsgLogic = new CreateOperationsMsgLogic(entryDto);
		String message = createAnnounceMsgLogic.createAnnouceMsg();

		// ------------------------
		// お知らせ情報登録
		// ------------------------
		for (JgiMst jgiMst : annouceJgiList) {
			Announce announce = new Announce();
			announce.setJgiNo(jgiMst.getJgiNo());
			announce.setMessage(message);
			announce.setOutputFileId(entryDto.getOutputFileId());
			announceDao.insert(announce, dpUser);
		}
	}

	// 業務連絡(アテンション)を登録
	public void entryAtt(ContactOperationsEntryDto entryDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (entryDto == null) {
			final String errMsg = "業務連絡の登録用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		ContactOperationsType operationsType = entryDto.getContactOperationsType();
		DpUser dpUser = entryDto.getDpUser();
		if (operationsType == null) {
			final String errMsg = "処理区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------------
		// アテンション通知対象の従業員取得
		// --------------------------------
		DpUser target = DpUserInfo.getDpUserInfo().getSettingUser();
		if (target == null) {
			target = dpUser;
		}

		SearchAttJgiListLogic searchAttJgiListLogic = new SearchAttJgiListLogic(sosMstDAO, jgiMstDAO, target, operationsType, entryDto.getSosCd());
		List<JgiMst> attJgiList = searchAttJgiListLogic.execute();

		// ------------------------
		// アテンションメッセージ作成
		// ------------------------
		CreateOperationsMsgLogic createAnnounceMsgLogic = new CreateOperationsMsgLogic(entryDto);
		String message = createAnnounceMsgLogic.createAttMsg();

		// ------------------------
		// アテンション登録
		// ------------------------
		EntryAttLogic entryAttLogic = new EntryAttLogic(attBaseIfDao, attLinksIfDao, attAddrGrpIfDao, calDao, attJgiList, message, entryDto);
		entryAttLogic.execute();
	}

	// お知らせ情報を削除
	public void deleteAnnounce(Long seqKey, Date upDate) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (seqKey == null) {
			final String errMsg = "お知らせ情報削除時にシーケンスキーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (upDate == null) {
			final String errMsg = "お知らせ情報削除時に最終更新日がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 出力ファイルを消さずにお知らせのみ削除して終了
		announceDao.delete(seqKey, upDate);
	}
}
