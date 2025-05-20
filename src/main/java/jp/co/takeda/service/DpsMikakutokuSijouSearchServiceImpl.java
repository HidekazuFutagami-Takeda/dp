package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.MikakutokuSijouDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.YakkouSijouDAO;
import jp.co.takeda.dto.MikakutokuSijouResultDto;
import jp.co.takeda.dto.MikakutokuSijouScDto;
import jp.co.takeda.logic.MikakutokuSijouResultDtoCreateLogic;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.MikakutokuSijou;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.YakkouSijou;

/**
 * 未獲得市場の検索に関するサービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsMikakutokuSijouSearchService")
public class DpsMikakutokuSijouSearchServiceImpl implements DpsMikakutokuSijouSearchService {

	/**
	 * 未獲得市場DAO
	 */
	@Autowired(required = true)
	@Qualifier("mikakutokuSijouDao")
	protected MikakutokuSijouDao mikakutokuSijouDao;

	/**
	 * 計画領域カテゴリ取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	/**
	 * 薬効市場DAO
	 */
	@Autowired(required = true)
	@Qualifier("yakkouSijouDAO")
	protected YakkouSijouDAO yakkouSijouDAO;

	/**
	 * 計画品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	public List<MikakutokuSijouResultDto> searchMikakutokuSijouList(MikakutokuSijouScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sosCd3 = scDto.getSosCd3();
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String yakkouSijouCode = scDto.getYakkouSijouCode();
		if (yakkouSijouCode == null) {
			final String errMsg = "薬効市場コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sortString = MikakutokuSijouDao.SORT_STRING3;

		// -----------------------------
		// 薬効市場からカテゴリを取得
		// -----------------------------
		YakkouSijou  yakkouSijou;
		try {
			yakkouSijou = yakkouSijouDAO.search( yakkouSijouCode);
		} catch (DataNotFoundException e) {
			final String errMsg = "薬効市場が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
		}
		String category = yakkouSijou.getCategory() ;

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		plannedCtg = dpsPlannedCtgDao.search(category);

		// ------------------------------------
		// 薬効市場コードから品目コードを取得
		// ------------------------------------
		PlannedProd plannedProd = null;
		plannedProd = plannedProdDAO.searchByYakkouSijouCode(yakkouSijouCode);

		// ----------------------
		// 検索処理実行
		// ----------------------
		List<MikakutokuSijou> searchResultList = null;
		try {
			searchResultList = mikakutokuSijouDao.searchList(sortString, sosCd3, yakkouSijouCode, plannedCtg.getOyakoKb(), plannedCtg.getOyakoKb2(),  plannedProd.getProdCode());
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		MikakutokuSijouResultDtoCreateLogic logic = new MikakutokuSijouResultDtoCreateLogic(searchResultList);
		return logic.createResultDtoList();
	}

	public List<MikakutokuSijouResultDto> searchMikakutokuSijouSumList(MikakutokuSijouScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sosCd3 = scDto.getSosCd3();
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String yakkouSijouCode = scDto.getYakkouSijouCode();
		if (yakkouSijouCode == null) {
			final String errMsg = "薬効市場コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String sortString = MikakutokuSijouDao.SORT_STRING2;

		// ----------------------
		// 検索処理実行
		// ----------------------
		List<MikakutokuSijou> searchResultList = null;
		try {
			searchResultList = mikakutokuSijouDao.searchSumList(sortString, sosCd3, yakkouSijouCode);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		MikakutokuSijouResultDtoCreateLogic logic = new MikakutokuSijouResultDtoCreateLogic(searchResultList);
		return logic.createResultDtoSumList();
	}
}
