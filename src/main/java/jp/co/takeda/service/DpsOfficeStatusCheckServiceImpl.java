package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCViSosCtgDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.OfficeStatusCheckDto;
import jp.co.takeda.dto.OfficeStatusCheckResultDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.OfficePlanStatusCheckLogic;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;

/**
 * 営業所計画ステータスのステータスチェックを行なうサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsOfficeStatusCheckService")
public class DpsOfficeStatusCheckServiceImpl implements DpsOfficeStatusCheckService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsOfficeStatusCheckServiceImpl.class);

	/**
	 * 営業所計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 組織カテゴリDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCViSosCtgDao")
	protected DpsCViSosCtgDao dpsCViSosCtgDao;

	public List<OfficePlanStatus> executeForShiten(SosMst sosMst, String prodCategory, List<OfficeStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosMst == null) {
			final String errMsg = "支店の組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCategory == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMst.getSosCd() == null) {
			final String errMsg = "支店の組織情報コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 営業所一覧取得
		// ----------------------
		List<SosMst> sosMstList = null;
		try {
			sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, sosMst.getSosCd());
		} catch (DataNotFoundException e) {
			final String errMsg = "支店配下の営業所が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		List<MessageKey> errorMessageKeyList = new ArrayList<MessageKey>();
		List<OfficePlanStatus> officePlanStatusList = new ArrayList<OfficePlanStatus>();

		// 営業所分繰り返す
		for (SosMst officeSosMst : sosMstList) {
			// カテゴリが組織に属してなければ、ステータスチェックしない
			try {
				dpsCViSosCtgDao.searchBySosCdAndCategory(officeSosMst.getSosCd(),prodCategory);
			} catch (DataNotFoundException e) {
				continue;
			}


			// 営業所計画ステータスチェックDTO作成
			OfficeStatusCheckDto officeStatusCheckDto = new OfficeStatusCheckDto(officeSosMst, prodCategory, unallowableStatusList);

			// 営業所計画ステータスチェック実行
			OfficePlanStatusCheckLogic statusCheckLogic = new OfficePlanStatusCheckLogic(officePlanStatusDao, officeStatusCheckDto, dpsCodeMasterDao);
			OfficeStatusCheckResultDto resultDto = statusCheckLogic.execute();

			// エラーがあった場合、エラーメッセージ追加
			if (resultDto.isError()) {
				errorMessageKeyList.addAll(resultDto.getErrorMessageKeyList());
			}
			officePlanStatusList.addAll(resultDto.getOfficePlanStatus());
		}

		// エラーがあった場合
		if (errorMessageKeyList.size() > 0) {
			final String errMsg = "営業所計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(errorMessageKeyList, errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("営業所計画ステータスOK");
		}

		return officePlanStatusList;
	}


	public List<OfficePlanStatus> executeForOffice(SosMst sosMst, String prodCategory, List<OfficeStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
		if (prodCategory == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 営業所取得(営業所名称が設定されていない場合)
		// ----------------------
		if (sosMst.getSosCd() != null) {
			if (sosMst.getBumonSeiName() == null) {
				try {
					sosMst = sosMstDAO.search(sosMst.getSosCd());
				} catch (DataNotFoundException e) {
					final String errMsg = "指定された営業所が存在しない";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
				}
			}
		}

		// ----------------------
		// ステータスチェック
		// ----------------------

		// 営業所計画ステータスチェックDTO作成
		OfficeStatusCheckDto officeStatusCheckDto = new OfficeStatusCheckDto(sosMst, prodCategory, unallowableStatusList);

		// 営業所計画ステータスチェック実行
		OfficePlanStatusCheckLogic statusCheckLogic = new OfficePlanStatusCheckLogic(officePlanStatusDao, officeStatusCheckDto, dpsCodeMasterDao);
		OfficeStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "営業所計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("営業所計画ステータスOK");
		}
		return resultDto.getOfficePlanStatus();
	}
}
