package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DistParamHonbuDao;
import jp.co.takeda.dao.DistParamOfficeDao;
import jp.co.takeda.dao.DistributionProdDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.DistParamResultDto;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.InsWsDistProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DistParamHonbu;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.model.view.DistributionProd;

/**
 * 配分機能の検索に関するサービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsDistributionProdSearchService")
public class DpsDistributionProdSearchServiceImpl implements DpsDistributionProdSearchService {

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 配分対象品目一覧DAO
	 */
	@Autowired(required = true)
	@Qualifier("distributionProdDao")
	protected DistributionProdDao distributionProdDao;

	/**
	 * 配分基準（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamHonbuDao")
	protected DistParamHonbuDao distParamHonbuDao;

	/**
	 * 配分基準（営業所）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamOfficeDao")
	protected DistParamOfficeDao distParamOfficeDao;

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * 施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusDao")
	protected InsWsPlanStatusDao insWsPlanStatusDao;

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
	private DpsCodeMasterDao dpsCodeMasterDao;

	// 配分対象品目一覧取得
	public List<InsWsDistProdResultDto> searchDistributionProdList(String category, String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

		// 営業所コードから組織情報取得
		if (sosCd != null) {
			SosMst sosMst;
			try {
				sosMst = sosMstDAO.search(sosCd);
			} catch (DataNotFoundException e) {
				final String errMsg = "対象組織がない：" + sosCd;
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
		}

		// -----------------------------
		// ワクチンコードの取得
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
		SysType stype = null;
		Sales sales = null;
		if(category.equals(vaccineCode)) {
			stype = SysType.VACCINE;
			sales = Sales.VACCHIN;
		}else{
			stype = SysType.IYAKU;
			sales = Sales.IYAKU;
		}

		// ----------------------
		// 当期年度、期を取得
		// ----------------------
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, stype);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画管理情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		String sysYear = sysManage.getSysYear();
		Term sysTerm = sysManage.getSysTerm();

		// ----------------------
		// 配分対象品目一覧取得
		// ----------------------
		List<DistributionProd> distributionProdList = distributionProdDao.searchInsWsProdList(sales, category, sosCd);

		// ----------------------
		// 配分基準取得
		// ----------------------
		List<InsWsDistProdResultDto> resultDtoList = new ArrayList<InsWsDistProdResultDto>();
		for (DistributionProd distProd : distributionProdList) {

			DistributionProd _distProd;
			boolean _isHonbu;
			BaseParam _baseParamUH = null;
			Date _refPeriodFromUH = null;
			Date _refPeriodToUH = null;
			BaseParam _baseParamP = null;
			Date _refPeriodFromP = null;
			Date _refPeriodToP = null;
			Date _LastUpdate = null;

			try {
				// 配分基準（営業所案）取得
				List<DistParamOffice> distParamOfficeList = distParamOfficeDao.searchInsWsParamList(DistParamOfficeDao.SORT_STRING, sosCd, distProd.getProdCode(), null, category, sales);

				_distProd = distProd;
				_isHonbu = false;
				for (DistParamOffice distParamOffice : distParamOfficeList) {
					switch (distParamOffice.getInsType()) {
						case UH:
							_baseParamUH = distParamOffice.getBaseParam();
							_refPeriodFromUH = RefPeriod.convertRefPeriod(distParamOffice.getBaseParam().getRefFrom(), sysYear, sysTerm);
							_refPeriodToUH = RefPeriod.convertRefPeriod(distParamOffice.getBaseParam().getRefTo(), sysYear, sysTerm);
							_LastUpdate = distParamOffice.getUpDate();
							break;
						case P:
							_baseParamP = distParamOffice.getBaseParam();
							_refPeriodFromP = RefPeriod.convertRefPeriod(distParamOffice.getBaseParam().getRefFrom(), sysYear, sysTerm);
							_refPeriodToP = RefPeriod.convertRefPeriod(distParamOffice.getBaseParam().getRefTo(), sysYear, sysTerm);
							break;
						default:
							break;
					}
				}

				if (_baseParamUH == null) {
					final String errMsg = "営業所案から、配分基準UHの取得に失敗:";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + distProd.getProdCode()));
				}
				if (_baseParamP == null) {
					final String errMsg = "営業所案から、配分基準Pの取得に失敗:";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + distProd.getProdCode()));
				}

			} catch (DataNotFoundException e) {

				// 営業所案が存在しない場合は、配分基準（本部案）を取得
				try {
					List<DistParamHonbu> distParamHonbuList = distParamHonbuDao.searchInsWsParamList(DistParamHonbuDao.SORT_STRING, distProd.getProdCode(), null, category);

					_distProd = distProd;
					_isHonbu = true;
					for (DistParamHonbu distParamHonbu : distParamHonbuList) {
						switch (distParamHonbu.getInsType()) {
							case UH:
								_baseParamUH = distParamHonbu.getBaseParam();
								_refPeriodFromUH = RefPeriod.convertRefPeriod(distParamHonbu.getBaseParam().getRefFrom(), sysYear, sysTerm);
								_refPeriodToUH = RefPeriod.convertRefPeriod(distParamHonbu.getBaseParam().getRefTo(), sysYear, sysTerm);
								_LastUpdate = distParamHonbu.getUpDate();
								break;
							case P:
								_baseParamP = distParamHonbu.getBaseParam();
								_refPeriodFromP = RefPeriod.convertRefPeriod(distParamHonbu.getBaseParam().getRefFrom(), sysYear, sysTerm);
								_refPeriodToP = RefPeriod.convertRefPeriod(distParamHonbu.getBaseParam().getRefTo(), sysYear, sysTerm);
								break;
							default:
								break;
						}
					}

					if (_baseParamUH == null) {
						final String errMsg = "本部案から、配分基準UHの取得に失敗:";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + distProd.getProdCode()));
					}
					if (_baseParamP == null) {
						final String errMsg = "本部案から、配分基準Pの取得に失敗:";
						throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + distProd.getProdCode()));
					}

				} catch (DataNotFoundException e1) {
					final String errMsg = "配分基準(本部案、営業所案)が存在しない：";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + distProd.getProdCode()), e1);
				}
			}

			// 結果用DTO作成
			InsWsDistProdResultDto resultDto = new InsWsDistProdResultDto(_distProd, _isHonbu, _baseParamUH, _refPeriodFromUH, _refPeriodToUH, _baseParamP, _refPeriodFromP,
				_refPeriodToP, _LastUpdate);

			resultDtoList.add(resultDto);
		}

		return resultDtoList;
	}

	// 配分基準取得
	public DistParamResultDto searchDistributionParam(String sosCd, String prodCode, String category) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 計画対象品目取得
		// ----------------------
		PlannedProd plannedProd = null;
		try {
			plannedProd = plannedProdDAO.search(prodCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "計画対象品目が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// ----------------------
		// （本部案）配分基準取得
		// ----------------------
		DistParamHonbu distParamHonbuUH;
		DistParamHonbu distParamHonbuP;
		try {
			distParamHonbuUH = distParamHonbuDao.search(prodCode, InsType.UH, DistributionType.INS_WS_PLAN);
			distParamHonbuP = distParamHonbuDao.search(prodCode, InsType.P, DistributionType.INS_WS_PLAN);
		} catch (DataNotFoundException e) {
			final String errMsg = "配分基準UHまたはP（本部案）が存在しない：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}
		// ゼロ配分フラグがUHとPで異なる場合は、システムエラーとする
		if (distParamHonbuUH.getDistParam().getZeroDistFlg() != distParamHonbuP.getDistParam().getZeroDistFlg()) {
			final String errMsg = "配分基準（本部案）のゼロ配分フラグがUHとPで異なる：";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode));
		}

		// ----------------------
		// （営業所案）配分基準取得
		// ----------------------
		DistParamOffice distParamOfficeUH = null;
		DistParamOffice distParamOfficeP = null;
		try {
			distParamOfficeUH = distParamOfficeDao.search(sosCd, prodCode, InsType.UH, DistributionType.INS_WS_PLAN);
			distParamOfficeP = distParamOfficeDao.search(sosCd, prodCode, InsType.P, DistributionType.INS_WS_PLAN);
		} catch (DataNotFoundException e) {

			// 営業所案が存在しない場合は、本部案を営業所案に設定する
			distParamOfficeUH = new DistParamOffice();
			distParamOfficeUH.setSosCd(sosCd);
			distParamOfficeUH.setProdCode(prodCode);
			distParamOfficeUH.setInsType(InsType.UH);
			distParamOfficeUH.setBaseParam(distParamHonbuUH.getBaseParam());
			distParamOfficeUH.setDistParam(distParamHonbuUH.getDistParam());

			distParamOfficeP = new DistParamOffice();
			distParamOfficeP.setSosCd(sosCd);
			distParamOfficeP.setProdCode(prodCode);
			distParamOfficeP.setInsType(InsType.P);
			distParamOfficeP.setBaseParam(distParamHonbuP.getBaseParam());
			distParamOfficeP.setDistParam(distParamHonbuP.getDistParam());
		}

		// カテゴリ名取得
		DpsCCdMst dpsCCdMst = new DpsCCdMst();
		try {
			// データ区分の検索
			dpsCCdMst = dpsCodeMasterDao.searchDataKbnAndCd(DpsCodeMaster.CAT.getDbValue(), category);
		} catch (DataNotFoundException e) {
			final String errMsg = "汎用マスタに、「DATA_KBN,DATA_CD=" + DpsCodeMaster.CAT.getDbValue() + ","  + category + "」が登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ----------------------
		// 結果DTO作成
		// ----------------------
		return new DistParamResultDto(plannedProd, distParamHonbuUH, distParamHonbuP, distParamOfficeUH, distParamOfficeP, dpsCCdMst.getDataName());
	}

	// 配分処理実行前のステータス取得
	public List<DistributionExecOrgDto> searchDistributionPreparation(String sosCd, List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd == null) {
//			final String errMsg = "組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (insWsDistProdUpdateDtoList == null || insWsDistProdUpdateDtoList.size() == 0) {
			final String errMsg = "施設特約店別計画配分対象品目更新用DTOがnullまたはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (InsWsDistProdUpdateDto insWsDistProdUpdateDto : insWsDistProdUpdateDtoList) {
			if (insWsDistProdUpdateDto.getProdCode() == null) {
				final String errMsg = "配分実行対象の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (insWsDistProdUpdateDto.getProdName() == null) {
				final String errMsg = "配分実行対象の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		// 品目ごとに施設特約店別計画立案ステータスを取得する
		List<DistributionExecOrgDto> distributionExecOrgDtoList = new ArrayList<DistributionExecOrgDto>();
		for (InsWsDistProdUpdateDto insWsDistProdUpdateDto : insWsDistProdUpdateDtoList) {

			String prodCode = insWsDistProdUpdateDto.getProdCode();
			String prodName = insWsDistProdUpdateDto.getProdName();
			Date lastUpdate = insWsDistProdUpdateDto.getStatusLastUpdate();

			List<InsWsPlanStatus> insWsPlanStatusList = null;
			try {
				insWsPlanStatusList = insWsPlanStatusDao.searchJgiBaseList(sosCd, null, prodCode);
			} catch (DataNotFoundException e) {
				// ステータスが存在しない従業員も取得されるはずなので、DataNotFoundExceptionの場合はエラー
				final String errMsg = "施設特約店別計画立案ステータス取得失敗：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + sosCd));
			}
			DistributionExecOrgDto distributionExecOrgDto = new DistributionExecOrgDto(prodCode, prodName, insWsPlanStatusList, lastUpdate);
			distributionExecOrgDtoList.add(distributionExecOrgDto);
		}

		return distributionExecOrgDtoList;
	}
}
