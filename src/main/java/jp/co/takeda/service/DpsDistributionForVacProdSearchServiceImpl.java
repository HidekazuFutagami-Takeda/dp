package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DistParamHonbuForVacDao;
import jp.co.takeda.dao.DistributionForVacProdDao;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.dto.InsWsDistForVacProdResultDto;
import jp.co.takeda.dto.InsWsDistProdUpdateDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DistParamHonbuForVac;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.model.view.DistributionForVacProd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ワクチン)配分機能の検索に関するサービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionForVacProdSearchService")
public class DpsDistributionForVacProdSearchServiceImpl implements DpsDistributionForVacProdSearchService {

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * ワクチン用配分対象品目一覧DAO
	 */
	@Autowired(required = true)
	@Qualifier("distributionForVacProdDao")
	protected DistributionForVacProdDao distributionForVacProdDao;

	/**
	 * ワクチン用配分基準（本部）DAO
	 */
	@Autowired(required = true)
	@Qualifier("distParamHonbuForVacDao")
	protected DistParamHonbuForVacDao distParamHonbuForVacDao;

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sysManageDao")
	protected SysManageDao sysManageDao;

	/**
	 * ワクチン用施設特約店別計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("insWsPlanStatusForVacDao")
	protected InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	// 配分対象品目一覧取得
	public List<InsWsDistForVacProdResultDto> searchDistributionProdList() throws DataNotFoundException {

		// ----------------------
		// 当期年度、期を取得
		// ----------------------
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPS, SysType.VACCINE);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画管理情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		String sysYear = sysManage.getSysYear();
		Term sysTerm = sysManage.getSysTerm();

		// ----------------------
		// 配分対象品目一覧取得
		// ----------------------
		List<DistributionForVacProd> distributionProdList = distributionForVacProdDao.searchList();

		// ----------------------
		// 配分基準取得
		// ----------------------
		List<InsWsDistForVacProdResultDto> resultDtoList = new ArrayList<InsWsDistForVacProdResultDto>();
		for (DistributionForVacProd distProd : distributionProdList) {

			BaseParam _baseParam = null;
			Date _refPeriodFrom = null;
			Date _refPeriodTo = null;
			Date _LastUpdate = null;

			// 営業所案が存在しない場合は、配分基準（本部案）を取得
			try {

				DistParamHonbuForVac distParamHonbuForVac = distParamHonbuForVacDao.search(distProd.getProdCode());
				_baseParam = distParamHonbuForVac.getBaseParam();
				_refPeriodFrom = RefPeriod.convertRefPeriod(distParamHonbuForVac.getBaseParam().getRefFrom(), sysYear, sysTerm);
				_refPeriodTo = RefPeriod.convertRefPeriod(distParamHonbuForVac.getBaseParam().getRefTo(), sysYear, sysTerm);
				_LastUpdate = distParamHonbuForVac.getUpDate();
			} catch (DataNotFoundException e1) {
				continue;
			}

			// 結果用DTO作成
			InsWsDistForVacProdResultDto resultDto = new InsWsDistForVacProdResultDto(distProd, _baseParam, _refPeriodFrom, _refPeriodTo, _LastUpdate);

			resultDtoList.add(resultDto);
		}

		return resultDtoList;
	}

	// 配分処理実行前のステータス取得
	public List<DistributionForVacExecOrgDto> searchDistributionPreparation(List<InsWsDistProdUpdateDto> insWsDistProdUpdateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
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
		List<DistributionForVacExecOrgDto> distributionExecOrgDtoList = new ArrayList<DistributionForVacExecOrgDto>();
		for (InsWsDistProdUpdateDto insWsDistProdUpdateDto : insWsDistProdUpdateDtoList) {

			String prodCode = insWsDistProdUpdateDto.getProdCode();
			String prodName = insWsDistProdUpdateDto.getProdName();
			Date lastUpdate = insWsDistProdUpdateDto.getStatusLastUpdate();

			List<InsWsPlanStatusForVac> insWsPlanStatusForVacList = null;
			try {
				insWsPlanStatusForVacList = insWsPlanStatusForVacDao.searchJgiBaseList(prodCode, null, null);
			} catch (DataNotFoundException e) {
				// ステータスが存在しない従業員も取得されるはずなので、DataNotFoundExceptionの場合はエラー
				final String errMsg = "施設特約店別計画立案ステータス取得失敗：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
			}
			DistributionForVacExecOrgDto distributionExecOrgDto = new DistributionForVacExecOrgDto(prodCode, prodName, insWsPlanStatusForVacList, lastUpdate);
			distributionExecOrgDtoList.add(distributionExecOrgDto);
		}

		return distributionExecOrgDtoList;
	}
}
