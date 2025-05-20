package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.InsWsStatusCheckForVacDto;
import jp.co.takeda.dto.InsWsStatusCheckResultForVacDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.InsWsPlanStatusCheckForVacByMrLogic;
import jp.co.takeda.logic.InsWsPlanStatusCheckForVacBySosLogic;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.BumonRank;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ワクチン用施設特約店別計画ステータスのステータスチェックを行なうサービス実装クラス
 * 
 * @author nozaki
 */
@Transactional
@Service("dpsInsWsStatusCheckForVacService")
public class DpsInsWsStatusCheckForVacServiceImpl implements DpsInsWsStatusCheckForVacService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsInsWsStatusCheckForVacServiceImpl.class);

	/**
	 * ワクチン用施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusForVacDao")
	protected InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 従業員DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	public List<InsWsPlanStatusForVac> execute(String sosCd3, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(特約店G)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : plannedProdList) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の従業員取得
		// ----------------------
		// 特約店G配下の従業員取得
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.TOKUYAKUTEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
		} catch (DataNotFoundException e) {
			return new ArrayList<InsWsPlanStatusForVac>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// ワクチン用施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckForVacDto insWsStatusCheckForVacDto = new InsWsStatusCheckForVacDto(sosCd3, jgiMstList, plannedProdList, unallowableStatusList);

		// ワクチン用施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckForVacByMrLogic statusCheckLogic = new InsWsPlanStatusCheckForVacByMrLogic(insWsPlanStatusForVacDao, insWsStatusCheckForVacDto);
		InsWsStatusCheckResultForVacDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "ワクチン用施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ワクチン用施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatusForVac();
	}

	public List<InsWsPlanStatusForVac> execute(String sosCd3, String sosCd4, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(特約店G)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd4 == null) {
			final String errMsg = "組織コード(チーム)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : plannedProdList) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の従業員取得
		// ----------------------
		// チーム配下の従業員取得
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.TOKUYAKUTEN_LIST, BumonRank.TEAM);
		} catch (DataNotFoundException e) {
			return new ArrayList<InsWsPlanStatusForVac>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// ワクチン用施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckForVacDto insWsStatusCheckForVacDto = new InsWsStatusCheckForVacDto(sosCd3, sosCd4, jgiMstList, plannedProdList, unallowableStatusList);

		// ワクチン用施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckForVacBySosLogic statusCheckLogic = new InsWsPlanStatusCheckForVacBySosLogic(insWsPlanStatusForVacDao, insWsStatusCheckForVacDto);
		InsWsStatusCheckResultForVacDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "ワクチン用施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ワクチン用施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatusForVac();
	}

	public List<InsWsPlanStatusForVac> execute(List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiMstList == null || jgiMstList.size() == 0) {
			final String errMsg = "従業員情報のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (JgiMst jgiMst : jgiMstList) {
			if (jgiMst.getJgiNo() == null) {
				final String errMsg = "チェック対象の従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (jgiMst.getJgiName() == null) {
				final String errMsg = "チェック対象の氏名がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		for (PlannedProd plannedProd : plannedProdList) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// ワクチン用施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckForVacDto insWsStatusCheckForVacDto = new InsWsStatusCheckForVacDto(jgiMstList, plannedProdList, unallowableStatusList);

		// ワクチン用施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckForVacByMrLogic statusCheckLogic = new InsWsPlanStatusCheckForVacByMrLogic(insWsPlanStatusForVacDao, insWsStatusCheckForVacDto);
		InsWsStatusCheckResultForVacDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "ワクチン用施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ワクチン用施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatusForVac();
	}

	public List<InsWsPlanStatusForVac> execute(List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (plannedProdList == null || plannedProdList.size() == 0) {
			final String errMsg = "計画対象品目のリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : plannedProdList) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// ----------------------
		// チェック対象の従業員取得
		// ----------------------
		// 全小児科MR取得 (J19-0010 対応・コメントのみ修正)
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, null, SosListType.TOKUYAKUTEN_LIST, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "ワクチンMRがいない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		return execute(jgiMstList, plannedProdList, unallowableStatusList);
	}
}
