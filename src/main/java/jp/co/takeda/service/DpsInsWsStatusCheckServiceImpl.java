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
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCodeMasterDao;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.dao.DpsKakuteiErrMsgDao;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.InsWsStatusCheckDto;
import jp.co.takeda.dto.InsWsStatusCheckResultDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.InsWsPlanStatusCheckByMrLogic;
import jp.co.takeda.logic.InsWsPlanStatusCheckBySosLogic;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.Sales;

/**
 * 施設特約店別計画ステータスのステータスチェックを行なうサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsInsWsStatusCheckService")
public class DpsInsWsStatusCheckServiceImpl implements DpsInsWsStatusCheckService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsInsWsStatusCheckServiceImpl.class);

	/**
	 * 施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusDao")
	protected InsWsPlanStatusDao insWsPlanStatusDao;

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

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 一括確定エラー情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsKakuteiErrMsgDao")
	protected DpsKakuteiErrMsgDao dpsKakuteiErrMsgDao;

	/**
	 * 一括確定エラー情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsKakuteiErrMsgSearchService")
	protected DpsKakuteiErrMsgSearchService dpsKakuteiErrMsgSearchService;

//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	// 組織コード(営業所)、計画対象品目のリストを指定して、施設特約店別計画のチェックを行なう。
	public List<InsWsPlanStatus> execute(String sosCd3, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null) {
//			final String errMsg = "組織コード(営業所)がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
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
		// 営業所配下の従業員取得
		List<JgiMst> jgiMstList;
		try {

			// 2014下期向け支援改定にて、整形対応削除
//			jgiMstList = jgiMstDAO.searchOnlySeikeiBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			// ワクチンの場合、組織未選択時は全社対象とする
			if (sosCd3 == null) {
				jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, SosMst.SOS_CD1, SosListType.SHITEN_LIST, BumonRank.IEIHON);
			}else {
				jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			}

		} catch (DataNotFoundException e) {
			return new ArrayList<InsWsPlanStatus>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckDto insWsStatusCheckDto = new InsWsStatusCheckDto(sosCd3, jgiMstList, plannedProdList, unallowableStatusList);

		// 施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckBySosLogic statusCheckLogic = new InsWsPlanStatusCheckBySosLogic(insWsPlanStatusDao, insWsStatusCheckDto);
		InsWsStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatus();
	}

	// 組織コード(営業所)、計画対象品目を指定して、施設特約店別計画のチェックを行なう。
	public List<InsWsPlanStatus> execute(String sosCd3, PlannedProd plannedProd, List<InsWsStatusForCheck> unallowableStatusList) {
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		plannedProdList.add(plannedProd);
		return execute(sosCd3, plannedProdList, unallowableStatusList);
	}

//	public List<InsWsPlanStatus> execute(String sosCd3, ProdCategory category, List<InsWsStatusForCheck> unallowableStatusList) {
	public List<InsWsPlanStatus> execute(String sosCd3, String category, List<InsWsStatusForCheck> unallowableStatusList) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (unallowableStatusList == null || unallowableStatusList.size() == 0) {
			final String errMsg = "許可しないステータスのリストがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// チェック対象の従業員取得
		// ----------------------
		// 営業所配下の従業員取得
		List<JgiMst> jgiMstList;
		try {
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
		} catch (DataNotFoundException e) {
			return new ArrayList<InsWsPlanStatus>();
		}

		// -----------------------------
		// ワクチンのカテゴリコード取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		if(category.equals(vaccineCode)) {
			sales = Sales.VACCHIN;
		}else{
			sales = Sales.IYAKU;
		}

		// ----------------------
		// チェック対象の品目取得
		// ----------------------
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(MMP品)が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckDto insWsStatusCheckDto = new InsWsStatusCheckDto(sosCd3, jgiMstList, plannedProdList, unallowableStatusList);

		// 施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckBySosLogic statusCheckLogic = new InsWsPlanStatusCheckBySosLogic(insWsPlanStatusDao, insWsStatusCheckDto);
		InsWsStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatus();
	}

	public List<InsWsPlanStatus> execute(String sosCd3, String sosCd4, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
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
			// 2014下期向け支援改定にて、整形対応削除
//			jgiMstList = jgiMstDAO.searchOnlySeikeiBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, BumonRank.TEAM);
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, BumonRank.TEAM);

		} catch (DataNotFoundException e) {
			return new ArrayList<InsWsPlanStatus>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckDto insWsStatusCheckDto = new InsWsStatusCheckDto(sosCd3, sosCd4, jgiMstList, plannedProdList, unallowableStatusList);

		// 施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckBySosLogic statusCheckLogic = new InsWsPlanStatusCheckBySosLogic(insWsPlanStatusDao, insWsStatusCheckDto);
		InsWsStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatus();
	}

	public List<InsWsPlanStatus> execute(List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

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
		// 施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckDto insWsStatusCheckDto = new InsWsStatusCheckDto(jgiMstList, plannedProdList, unallowableStatusList);

		// 施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckByMrLogic statusCheckLogic = new InsWsPlanStatusCheckByMrLogic(insWsPlanStatusDao, insWsStatusCheckDto);
		InsWsStatusCheckResultDto resultDto = statusCheckLogic.execute();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatus();
	}

//	public List<InsWsPlanStatus> execute(List<JgiMst> jgiMstList, ProdCategory category, List<InsWsStatusForCheck> unallowableStatusList) {
	public List<InsWsPlanStatus> execute(List<JgiMst> jgiMstList, String category, List<InsWsStatusForCheck> unallowableStatusList) {

		// -----------------------------
		// ワクチンのカテゴリコード取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// -----------------------------
		// 売上所属の判定
		// -----------------------------
		Sales sales = null;
		if(category != null) {
			if(category.equals(vaccineCode)) {
				sales = Sales.VACCHIN;
			}else{
				sales = Sales.IYAKU;
			}
		}

		// ----------------------
		// チェック対象の品目取得
		// ----------------------
		List<PlannedProd> plannedProdList;
		try {
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category, null);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目(MMP品)が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// チェック実行
		return execute(jgiMstList, plannedProdList, unallowableStatusList);
	}


//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	//↑の共通部分から、一括確定用にステータスチェックを呼び出せす為に追加（execute　→　executeKakutei）
	public List<InsWsPlanStatus> executeKakutei(String sosCd3, List<JgiMst> jgiMstList, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

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
		// 施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckDto insWsStatusCheckDto = new InsWsStatusCheckDto(jgiMstList, plannedProdList, unallowableStatusList);

		// 施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckByMrLogic statusCheckLogic = new InsWsPlanStatusCheckByMrLogic(insWsPlanStatusDao, insWsStatusCheckDto);
		InsWsStatusCheckResultDto resultDto = statusCheckLogic.executeKakutei(sosCd3);

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getDpsKakuteiErrMsg(), resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatus();
	}
	public List<InsWsPlanStatus> executeKakutei(String sosCd3, String sosCd4, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
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
			// 2014下期向け支援改定にて、整形対応削除
//			jgiMstList = jgiMstDAO.searchOnlySeikeiBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, BumonRank.TEAM);
			jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd4, SosListType.SHITEN_LIST, BumonRank.TEAM);

		} catch (DataNotFoundException e) {
			return new ArrayList<InsWsPlanStatus>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckDto insWsStatusCheckDto = new InsWsStatusCheckDto(sosCd3, sosCd4, jgiMstList, plannedProdList, unallowableStatusList);

		// 施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckBySosLogic statusCheckLogic = new InsWsPlanStatusCheckBySosLogic(insWsPlanStatusDao, insWsStatusCheckDto);
		InsWsStatusCheckResultDto resultDto = statusCheckLogic.executeKakutei();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getDpsKakuteiErrMsg(), resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatus();
	}

	public List<InsWsPlanStatus> executeKakutei(String sosCd3, List<PlannedProd> plannedProdList, List<InsWsStatusForCheck> unallowableStatusList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null) {
//			final String errMsg = "組織コード(営業所)がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
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
		// 営業所配下の従業員取得
		List<JgiMst> jgiMstList;
		try {

			// 2014下期向け支援改定にて、整形対応削除
//			jgiMstList = jgiMstDAO.searchOnlySeikeiBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			// ワクチンの場合、組織未選択時は全社対象とする
			if (sosCd3 == null) {
				jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, SosMst.SOS_CD1, SosListType.SHITEN_LIST, BumonRank.IEIHON);
			}else {
				jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd3, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			}

		} catch (DataNotFoundException e) {
			return new ArrayList<InsWsPlanStatus>();
		}

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 施設特約店別計画ステータスチェックDTO作成
		InsWsStatusCheckDto insWsStatusCheckDto = new InsWsStatusCheckDto(sosCd3, jgiMstList, plannedProdList, unallowableStatusList);

		// 施設特約店別計画ステータスチェック実行
		InsWsPlanStatusCheckBySosLogic statusCheckLogic = new InsWsPlanStatusCheckBySosLogic(insWsPlanStatusDao, insWsStatusCheckDto);
		InsWsStatusCheckResultDto resultDto = statusCheckLogic.executeKakutei();

		// エラーがあった場合
		if (resultDto.isError()) {
			final String errMsg = "施設特約店別計画ステータスチェックでエラー";
			throw new UnallowableStatusException(new Conveyance(resultDto.getDpsKakuteiErrMsg(), resultDto.getErrorMessageKeyList(), errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("施設特約店別計画ステータスOK");
		}

		return resultDto.getInsWsPlanStatus();
	}
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

}
