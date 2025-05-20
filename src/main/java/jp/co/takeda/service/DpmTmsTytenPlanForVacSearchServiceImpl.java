package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageChangeParamBTDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.ManageWsPlanForVacDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.ManageWsPlanForVacDetailDto;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacProdHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacProdScDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.ChangeParamBT;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.ManageWsPlanForVac;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 【管理】(ワ)特約店別計画検索サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmTmsTytenPlanForVacSearchService")
public class DpmTmsTytenPlanForVacSearchServiceImpl implements DpmTmsTytenPlanForVacSearchService {

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	protected ManagePlannedProdDao managePlannedProdDao;

	/**
	 * 変換パラメータ(B→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamBTDao")
	protected ManageChangeParamBTDao manageChangeParamBTDao;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnRealDao")
	protected TmsTytenMstUnRealDao tmsTytenMstUnRealDao;

	/**
	 * 管理の(ワ)特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageWsPlanForVacDao")
	protected ManageWsPlanForVacDao manageWsPlanForVacDao;

	// ヘッダ検索
	public ManageWsPlanForVacHeaderDto searchHeader(ManageWsPlanForVacScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String tmsTytenCd = scDto.getTmsTytenCd();
		if (tmsTytenCd == null) {
			final String errMsg = "検索対象の特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 特約店情報取得
		// -----------------------------
		// 特約店コードを13桁に変換
		final String tmsTytenCd13 = new CreateTmsTytenCdLogic(tmsTytenCd).execute();
		TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd13);

		return new ManageWsPlanForVacHeaderDto(tmsTytenCd, tmsTytenMstUn.getTmsTytenMeiKj());
	}

	// 特約店別計画一覧検索
	public ManageWsPlanForVacDto searchList(ManageWsPlanForVacScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodCode = scDto.getProdCode();
		if (prodCode == null) {
			final String errMsg = "検索対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String tmsTytenCd = scDto.getTmsTytenCd();
		if (tmsTytenCd == null) {
			final String errMsg = "検索対象の特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final PlanData planData = scDto.getPlanData();
		if (planData == null) {
			final String errMsg = "計画の検索範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 特約店情報取得
		// -----------------------------
		final String tmsTytenCd13 = new CreateTmsTytenCdLogic(tmsTytenCd).execute();
		tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd13);

		// -----------------------------
		// 品目情報取得
		// -----------------------------
		ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

		// -----------------------------
		// T/Y変換率取得
		// -----------------------------
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		ChangeParamBT changeParamBT = manageChangeParamBTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);

		// -----------------------------
		// 特約店別計画取得
		// -----------------------------
		boolean allWsFlg = false;
		if (planData.equals(PlanData.ALL)) {
			allWsFlg = true;
		}
		List<ManageWsPlanForVac> wsPlanList = manageWsPlanForVacDao.searchListByProd(ManageWsPlanForVacDao.SORT_STRING, prodCode, tmsTytenCd, allWsFlg);

		// -----------------------------
		// 特約店別計画明細を生成
		// -----------------------------
		List<ManageWsPlanForVacDetailDto> detailList = new ArrayList<ManageWsPlanForVacDetailDto>();
		// 中間集計対象の桁数
		Integer sumLength = null;
		if (tmsTytenCd.length() < 13) {
			sumLength = tmsTytenCd.length() + 2;
		}

		// 中間リスト
		List<ManageWsPlanForVacDetailDto> tmpDetailList = new ArrayList<ManageWsPlanForVacDetailDto>();
		// 中間集計用計画値
		Long baseBSum = 0L;
		Long baseTSum = 0L;
		// 中間集計対象特約店コード
		String tmsTytenCdPart = null;
		for (int i = 0; i < wsPlanList.size(); i++) {
			// -----------
			// 明細行生成
			// -----------
			ManageWsPlanForVacDetailDto detailDto = null;
			ManageWsPlanForVac wsPlan = wsPlanList.get(i);
			// 特約店コード
			String tmpTmsCode = wsPlan.getTmsTytenCd();
			detailDto = createDetailDto(wsPlan, changeParamBT);

			// ------------------
			// 中間集計対象か判定
			// ------------------
			if (sumLength == null) {
				detailList.add(detailDto);
				continue;
			}
			if (tmsTytenCdPart != null) {
				if (tmpTmsCode.substring(0, sumLength).equals(tmsTytenCdPart)) {
					// 対象の場合、
				} else {
					// 対象外の場合、
					// リストに集計行を追加
					detailList.add(createDetailDto(tmsTytenCdPart, baseBSum, baseTSum, changeParamBT));
					// リストに中間リストを追加
					detailList.addAll(tmpDetailList);
					// 新規の集計行・中間リストを生成
					baseBSum = 0L;
					baseTSum = 0L;
					tmpDetailList = new ArrayList<ManageWsPlanForVacDetailDto>();
				}
			}
			// 集計行に加算
			baseBSum = MathUtil.add(baseBSum, detailDto.getBaseB());
			baseTSum = MathUtil.add(baseTSum, detailDto.getBaseT());
			// 中間リストに追加
			tmpDetailList.add(detailDto);
			// 中間集計対象特約店コード
			tmsTytenCdPart = tmpTmsCode.substring(0, sumLength);
		}
		// 中間リストに存在する場合、リストに追加
		if (tmpDetailList.size() != 0) {
			// リストに集計行を追加
			detailList.add(createDetailDto(tmsTytenCdPart, baseBSum, baseTSum, changeParamBT));
			// リストに中間リストを追加
			detailList.addAll(tmpDetailList);
		}

		// ------------
		// 結果DTO生成
		// ------------
		return new ManageWsPlanForVacDto(plannedProd.getProdName(), detailList);
	}

	/**
	 * 明細DTOを生成する。
	 *
	 *
	 * @param wsPlan 特約店別計画
	 * @param changeParamBT T/B変換パラメータ
	 * @return (医)特約店別計画編集画面を表すDTO
	 */
	private ManageWsPlanForVacDetailDto createDetailDto(ManageWsPlanForVac wsPlan, ChangeParamBT changeParamBT) {
		// 特約店名称
		String tmsTytemName = null;
		// ＴＭＳ特約店コード
		String tmsTytenCd = null;
		// B価ベース
		Long baseB = null;
		// T価ベース
		Long baseT = null;
		// シーケンスキー
		Long seqKey = null;
		// 最終更新者
		String upDateJgiName = null;
		// 最終更新日
		Date upDate = null;
		// 特約店中計フラグ
		boolean tytenSumRowFlg = false;

		// 特約店対象外フラグ
		boolean planTaiGaiFlgTok = false;

		// 計画
		if (wsPlan != null) {
			tmsTytemName = wsPlan.getTmsTytenName();
			tmsTytenCd = wsPlan.getTmsTytenCd();
			if (StringUtils.isNotBlank(tmsTytenCd)) {
				try {
					TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd);
					planTaiGaiFlgTok = tmsTytenMstUn.getPlanTaiGaiFlgTok();
				} catch (DataNotFoundException e) {
				}
			}
			baseB = ConvertUtil.parseMoneyToThousandUnit(wsPlan.getImplPlanForVac().getPlanned2ValueY());
			baseT = ConvertUtil.parseMoneyToThousandUnit(wsPlan.getImplPlanForVac().getPlanned2ValueT());
			seqKey = wsPlan.getSeqKey();
			if (wsPlan.getUpDate() != null) {
				upDateJgiName = wsPlan.getUpJgiName();
			}
			upDate = wsPlan.getUpDate();
		}

		return new ManageWsPlanForVacDetailDto(
			tmsTytemName, tmsTytenCd, baseB, baseT, seqKey, upDateJgiName, upDate, tytenSumRowFlg, changeParamBT.getChangeRate(), planTaiGaiFlgTok);
	}

	/**
	 * 中間集計行DTOを生成する。
	 *
	 *
	 * @param tmsTytenCdPart 特約店コード
	 * @param baseB B価ベース
	 * @param baseT T価ベース
	 * @param changeParamBT T/B変換パラメータ
	 * @return (ワ)特約店別計画編集画面を表すDTO
	 * @throws DataNotFoundException
	 */
	private ManageWsPlanForVacDetailDto createDetailDto(String tmsTytenCdPart, Long baseB, Long baseT, ChangeParamBT changeParamBT) throws DataNotFoundException {
		// 特約店コードを13桁に変換
		final String tmsTytenCd13 = new CreateTmsTytenCdLogic(tmsTytenCdPart).execute();
		TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd13);
		// 特約店名称
		String tmsTytemName = tmsTytenMstUn.getTmsTytenMeiKj();
		// 特約店コード
		String tmsTytenCd = tmsTytenCdPart;
		// 特約店中計フラグ
		boolean tytenSumRowFlg = true;

		return new ManageWsPlanForVacDetailDto(tmsTytemName, tmsTytenCd, baseB, baseT, null, null, null, tytenSumRowFlg, changeParamBT.getChangeRate(), false);
	}

	// ヘッダ検索(品目一覧)
	public ManageWsPlanForVacProdHeaderDto searchHeader(ManageWsPlanForVacProdScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String tmsTytenCd = scDto.getTmsTytenCd();
		if (tmsTytenCd == null) {
			final String errMsg = "検索対象の特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 特約店情報取得
		// -----------------------------
		// 特約店コードを13桁に変換
		final String tmsTytenCd13 = new CreateTmsTytenCdLogic(tmsTytenCd).execute();
		TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd13);
		boolean planTaiGaiFlgTok = tmsTytenMstUn.getPlanTaiGaiFlgTok();

		return new ManageWsPlanForVacProdHeaderDto(tmsTytenCd, tmsTytenMstUn.getTmsTytenMeiKj(), planTaiGaiFlgTok);
	}

}
