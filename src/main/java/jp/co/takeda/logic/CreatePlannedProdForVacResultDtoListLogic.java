package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanForVacDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.PlannedProdForVacResultDto;
import jp.co.takeda.dto.PlannedProdForVacResultProdInfoDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.MrPlanForVac;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.MathUtil;

/**
 * ワクチン用計画対象品目選択画面の検索結果用DTOのリストを生成する。
 *
 * @author khashimoto
 */
public class CreatePlannedProdForVacResultDtoListLogic {

	/**
	 * 組織情報取得DAO
	 */
	private final SosMstDAO sosMstDAO;

	/**
	 * 従業員情報取得DAO
	 */
	private final JgiMstDAO jgiMstDAO;

	/**
	 * ワクチン用担当者別計画取得DAO
	 */
	private final MrPlanForVacDao mrPlanForVacDao;

	/**
	 * 計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * コンストラクタ
	 *
	 * @param sosMstDAO 組織情報取得DAO
	 * @param jgiMstDAO 従業員情報取得DAO
	 * @param mrPlanForVacDao ワクチン用担当者別計画取得DAO
	 * @param plannedProdList 計画対象品目のリスト
	 */
	public CreatePlannedProdForVacResultDtoListLogic(SosMstDAO sosMstDAO, JgiMstDAO jgiMstDAO, MrPlanForVacDao mrPlanForVacDao, List<PlannedProd> plannedProdList) {
		this.sosMstDAO = sosMstDAO;
		this.jgiMstDAO = jgiMstDAO;
		this.mrPlanForVacDao = mrPlanForVacDao;
		this.plannedProdList = plannedProdList;
	}

	/**
	 * DTOリストを生成する。
	 *
	 * @return ワクチン用計画対象品目選択画面の検索結果用DTOのリスト
	 */
	public List<PlannedProdForVacResultDto> execute() {

		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		List<PlannedProdForVacResultDto> resultList = new ArrayList<PlannedProdForVacResultDto>();

		// ---------------------------------------
		// 本部、本部ワクチンＧの場合
		// ---------------------------------------
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G)) {

			// エリア特約店G一覧取得
			List<SosMst> tokuGSosMstList = new ArrayList<SosMst>();
			try {
				tokuGSosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.TOKUYAKUTEN_LIST, BumonRank.SITEN_TOKUYAKUTEN_BU, null);
			} catch (DataNotFoundException e) {
				final String errMsg = "エリア特約店G情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}

			// 担当者別計画取得
			List<MrPlanForVac> mrPlanList = new ArrayList<MrPlanForVac>();
			try {
				mrPlanList = mrPlanForVacDao.searchList(MrPlanForVacDao.SORT_STRING3, null, null, null, null);
			} catch (DataNotFoundException e) {
				// エラーにしない
			}
			int planIndex = 0;

			// 全社品目サマリ格納MAP生成
			Map<String, Long> planAllMap = new HashMap<String, Long>();
			Map<String, Long> advancePeriodAllMap = new HashMap<String, Long>();

			// エリア特約店G単位で繰り返し
			for (SosMst sosMst : tokuGSosMstList) {
				// 組織コード
				String sosCd = sosMst.getSosCd();
				// 組織名
				String sosName = sosMst.getBumonSeiName();
				// 組織品目サマリ格納MAP生成
				Map<String, Long> planMap = new HashMap<String, Long>();
				Map<String, Long> advancePeriodMap = new HashMap<String, Long>();

				// MRリスト取得
				List<JgiMst> mrList = new ArrayList<JgiMst>();
				try {
					mrList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.TOKUYAKUTEN_LIST, sosMst.getBumonRank());
				} catch (DataNotFoundException e) {
					// エラーにしない
				}

				// 小児科ＭＲのいない組織は省略 (J19-0010 対応・コメントのみ修正)
				if (mrList.isEmpty()) {
					continue;
				}

				// MR行生成
				// MR単位に実行
				for (JgiMst jgiMst : mrList) {
					// 品目DTO格納用リスト
					List<PlannedProdForVacResultProdInfoDto> prodDtoList = new ArrayList<PlannedProdForVacResultProdInfoDto>();
					// チーム名
					String teamName = null;
					if (!(jgiMst.getSosCd4().equals(JgiMst.BLANK_SOS_CD))) {
						try {
							SosMst team = sosMstDAO.search(jgiMst.getSosCd4());
							teamName = team.getBumonSeiName();
						} catch (DataNotFoundException e) {
							// エラーにしない
						}
					}

					// 従業員番号
					Integer jgiNo = jgiMst.getJgiNo();
					// 従業員名
					String jgiName = jgiMst.getJgiName();
					// 計画値(従業員サマリ)
					Long plannedValueJgiSum = 0L;
					// 前期実績値(従業員サマリ)
					Long advancePeriodValueJgiSum = 0L;
					// 品目単位に実行
					for (PlannedProd plannedProd : plannedProdList) {
						// 品目コード
						String prodCode = plannedProd.getProdCode();
						// 計画値
						Long plannedValue = null;
						// 前期実績値
						Long advancePeriodValue = null;
						// 担当者別計画
						if (planIndex < mrPlanList.size()) {
							MrPlanForVac mrPlanForVac = mrPlanList.get(planIndex);
							if (jgiNo.equals(mrPlanForVac.getJgiNo()) && prodCode.equals(mrPlanForVac.getProdCode())) {
								plannedValue = mrPlanForVac.getPlannedValueB();
								advancePeriodValue = mrPlanForVac.getMonNnuSummary().getAdvancePeriod();
								planIndex++;
							}
						}

						// 品目単位DTO生成
						prodDtoList.add(new PlannedProdForVacResultProdInfoDto(prodCode, plannedValue, advancePeriodValue, false));
						// 従業員サマリ加算
						plannedValueJgiSum = MathUtil.add(plannedValueJgiSum, plannedValue);
						advancePeriodValueJgiSum = MathUtil.add(advancePeriodValueJgiSum, advancePeriodValue);
						// 組織品目サマリ加算
						planMap.put(prodCode, MathUtil.add(planMap.get(prodCode), plannedValue));
						advancePeriodMap.put(prodCode, MathUtil.add(advancePeriodMap.get(prodCode), advancePeriodValue));
					}
					// 品目合計DTO生成
					prodDtoList.add(new PlannedProdForVacResultProdInfoDto(null, plannedValueJgiSum, advancePeriodValueJgiSum, true));
					// 従業員行生成
					resultList.add(new PlannedProdForVacResultDto(sosName, teamName, jgiName, prodDtoList, false, false, false));
				}

				// 品目DTO格納用リスト
				List<PlannedProdForVacResultProdInfoDto> prodDtoList = new ArrayList<PlannedProdForVacResultProdInfoDto>();
				// 計画値(組織サマリ)
				Long plannedValueSosSum = 0L;
				// 前期実績値(組織サマリ)
				Long advancePeriodSosSum = 0L;
				for (PlannedProd plannedProd : plannedProdList) {
					// 品目コード
					String prodCode = plannedProd.getProdCode();
					// 計画値
					Long plannedValue = MathUtil.add(0L, planMap.get(prodCode));
					// 前期実績値
					Long advancePeriodValue = MathUtil.add(0L, advancePeriodMap.get(prodCode));
					// 品目単位DTO生成
					prodDtoList.add(new PlannedProdForVacResultProdInfoDto(prodCode, plannedValue, advancePeriodValue, false));
					// 組織サマリ加算
					plannedValueSosSum = MathUtil.add(plannedValueSosSum, plannedValue);
					advancePeriodSosSum = MathUtil.add(advancePeriodSosSum, advancePeriodValue);
					// 全社品目サマリ加算
					planAllMap.put(prodCode, MathUtil.add(planAllMap.get(prodCode), plannedValue));
					advancePeriodAllMap.put(prodCode, MathUtil.add(advancePeriodAllMap.get(prodCode), advancePeriodValue));
				}
				// 品目合計DTO生成
				prodDtoList.add(new PlannedProdForVacResultProdInfoDto(null, plannedValueSosSum, advancePeriodSosSum, true));
				// 組織合計行生成
				resultList.add(new PlannedProdForVacResultDto(sosName, null, null, prodDtoList, false, true, false));
			}

			// 全社合計行生成
			// 品目DTO格納用リスト
			List<PlannedProdForVacResultProdInfoDto> prodDtoList = new ArrayList<PlannedProdForVacResultProdInfoDto>();
			// 計画値(全社サマリ)
			Long plannedValueAllSum = 0L;
			// 前期計画値(全社サマリ)
			Long advancePeriodValueAllSum = 0L;
			for (PlannedProd plannedProd : plannedProdList) {
				// 品目コード
				String prodCode = plannedProd.getProdCode();
				// 計画値
				Long plannedValue = MathUtil.add(0L, planAllMap.get(prodCode));
				// 前期計画値
				Long advancePeriodValue = MathUtil.add(0L, advancePeriodAllMap.get(prodCode));
				// 品目単位DTO生成
				prodDtoList.add(new PlannedProdForVacResultProdInfoDto(prodCode, plannedValue, advancePeriodValue, false));
				// 組織サマリ加算
				plannedValueAllSum = MathUtil.add(plannedValueAllSum, plannedValue);
				advancePeriodValueAllSum = MathUtil.add(advancePeriodValueAllSum, advancePeriodValue);
			}
			// 品目合計DTO生成
			prodDtoList.add(new PlannedProdForVacResultProdInfoDto(null, plannedValueAllSum, advancePeriodValueAllSum, true));
			// 全社合計行生成
			resultList.add(new PlannedProdForVacResultDto(null, null, null, prodDtoList, false, false, true));
		}

		// ---------------------------------------
		// 小児科ＡＣの場合 (J19-0010 対応・コメントのみ修正)
		// ---------------------------------------
		else if (dpUser.isMatch(JokenSet.WAKUTIN_AL)) {

			// エリア情報取得
			SosMst tokuGSosMst = null;
			try {
				tokuGSosMst = sosMstDAO.search(dpUser.getSosCd3());
			} catch (DataNotFoundException e) {
				final String errMsg = "エリア情報が存在しない：";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}


			// 組織配下の担当者別計画取得
			List<MrPlanForVac> mrPlanList = new ArrayList<MrPlanForVac>();
			try {
				mrPlanList = mrPlanForVacDao.searchList(MrPlanForVacDao.SORT_STRING3, null, dpUser.getSosCd3(), null, null);
			} catch (DataNotFoundException e) {
				// エラーにしない
			}

			int planIndex = 0;

			// 組織コード
			String sosCd = tokuGSosMst.getSosCd();

			// 組織名
			String sosName = tokuGSosMst.getBumonSeiName();

			// 組織品目サマリ格納MAP生成
			Map<String, Long> planMap = new HashMap<String, Long>();
			Map<String, Long> advancePeriodMap = new HashMap<String, Long>();

			// MRリスト取得
			List<JgiMst> mrList = new ArrayList<JgiMst>();
			try {
				mrList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosCd, SosListType.TOKUYAKUTEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G);
			} catch (DataNotFoundException e) {
				// エラーにしない
			}

			// MR行生成
			// MR単位に実行
			for (JgiMst jgiMst : mrList) {
				// 品目DTO格納用リスト
				List<PlannedProdForVacResultProdInfoDto> prodDtoList = new ArrayList<PlannedProdForVacResultProdInfoDto>();
				// 従業員番号
				Integer jgiNo = jgiMst.getJgiNo();
				// 従業員名
				String jgiName = jgiMst.getJgiName();
				// 計画値(従業員サマリ)
				Long plannedValueJgiSum = 0L;
				// 前期実績値(従業員サマリ)
				Long advancePeriodValueJgiSum = 0L;
				// 品目単位に実行
				for (PlannedProd plannedProd : plannedProdList) {
					// 品目コード
					String prodCode = plannedProd.getProdCode();
					// 計画値
					Long plannedValue = null;
					// 前期実績値
					Long advancePeriodValue = null;
					// 担当者別計画
					if (planIndex < mrPlanList.size()) {
						MrPlanForVac mrPlanForVac = mrPlanList.get(planIndex);
						if (jgiNo.equals(mrPlanForVac.getJgiNo()) && prodCode.equals(mrPlanForVac.getProdCode())) {
							plannedValue = mrPlanForVac.getPlannedValueB();
							advancePeriodValue = mrPlanForVac.getMonNnuSummary().getAdvancePeriod();
							planIndex++;
						}
					}
					// 品目単位DTO生成
					prodDtoList.add(new PlannedProdForVacResultProdInfoDto(prodCode, plannedValue, advancePeriodValue, false));
					// 従業員サマリ加算
					plannedValueJgiSum = MathUtil.add(plannedValueJgiSum, plannedValue);
					advancePeriodValueJgiSum = MathUtil.add(advancePeriodValueJgiSum, advancePeriodValue);
					// 組織品目サマリ加算
					planMap.put(prodCode, MathUtil.add(planMap.get(prodCode), plannedValue));
					advancePeriodMap.put(prodCode, MathUtil.add(advancePeriodMap.get(prodCode), advancePeriodValue));
				}

				// 品目合計DTO生成
				prodDtoList.add(new PlannedProdForVacResultProdInfoDto(null, plannedValueJgiSum, advancePeriodValueJgiSum, true));
				// 従業員行生成
				resultList.add(new PlannedProdForVacResultDto(sosName, null, jgiName, prodDtoList, false, false, false));
			}

			if (mrList.size() > 0) {
				// 品目DTO格納用リスト
				List<PlannedProdForVacResultProdInfoDto> prodDtoList = new ArrayList<PlannedProdForVacResultProdInfoDto>();
				// 計画値(組織サマリ)
				Long plannedValueSosSum = 0L;
				// 前期実績値(組織サマリ)
				Long advancePeriodValueSosSum = 0L;
				for (PlannedProd plannedProd : plannedProdList) {
					// 品目コード
					String prodCode = plannedProd.getProdCode();
					// 計画値
					Long plannedValue = MathUtil.add(0L, planMap.get(prodCode));
					// 前期実績値
					Long advancePeriodValue = MathUtil.add(0L, advancePeriodMap.get(prodCode));
					// 品目単位DTO生成
					prodDtoList.add(new PlannedProdForVacResultProdInfoDto(prodCode, plannedValue, advancePeriodValue, false));
					// 組織サマリ加算
					plannedValueSosSum = MathUtil.add(plannedValueSosSum, plannedValue);
					advancePeriodValueSosSum = MathUtil.add(advancePeriodValueSosSum, advancePeriodValue);
				}
				// 品目合計DTO生成
				prodDtoList.add(new PlannedProdForVacResultProdInfoDto(null, plannedValueSosSum, advancePeriodValueSosSum, true));
				// 組織合計行生成
				resultList.add(new PlannedProdForVacResultDto(sosName, null, null, prodDtoList, true, false, false));
			}
		}

		// ---------------------------------------
		// 小児科ＭＲの場合 (J19-0010 対応・コメントのみ修正)
		// ---------------------------------------
		else if (dpUser.isMatch(JokenSet.WAKUTIN_MR)) {

			// 従業員番号
			Integer jgiNo = dpUser.getJgiNo();

			// 従業員名
			String jgiName = dpUser.getJgiName();

			String sosName = dpUser.getBumonSeiName();
			String teamName = null;

			if (!(dpUser.getSosCd4().equals(JgiMst.BLANK_SOS_CD))) {
				try {
					SosMst tokuyakutenG = sosMstDAO.search(dpUser.getSosCd3());
					teamName = sosName;
					sosName = tokuyakutenG.getBumonSeiName();

				} catch (DataNotFoundException e) {
					// エラーにしない
				}
			}

			List<MrPlanForVac> mrPlanList = new ArrayList<MrPlanForVac>();
			try {
				mrPlanList = mrPlanForVacDao.searchList(MrPlanForVacDao.SORT_STRING3, dpUser.getJgiNo(), null, null, null);
			} catch (DataNotFoundException e) {
				// エラーにしない
			}

			int planIndex = 0;
			final int PLAN_MAX = mrPlanList.size();
			// MR行生成
			// 品目DTO格納用リスト
			List<PlannedProdForVacResultProdInfoDto> prodDtoList = new ArrayList<PlannedProdForVacResultProdInfoDto>();
			// 計画値(従業員サマリ)
			Long plannedValueJgiSum = 0L;
			// 前期実績値(従業員サマリ)
			Long advancePeriodValueJgiSum = 0L;
			// 品目単位に実行
			for (PlannedProd plannedProd : plannedProdList) {
				// 品目コード
				String prodCode = plannedProd.getProdCode();
				// 計画値
				Long plannedValue = null;
				// 前期実績値
				Long advancePeriodValue = null;
				// 担当者別計画
				if (planIndex != PLAN_MAX) {
					MrPlanForVac mrPlanForVac = mrPlanList.get(planIndex);
					if (jgiNo.equals(mrPlanForVac.getJgiNo()) && prodCode.equals(mrPlanForVac.getProdCode())) {
						plannedValue = mrPlanForVac.getPlannedValueB();
						advancePeriodValue = mrPlanForVac.getMonNnuSummary().getAdvancePeriod();
						planIndex++;
					}
				}
				// 品目単位DTO生成
				prodDtoList.add(new PlannedProdForVacResultProdInfoDto(prodCode, plannedValue, advancePeriodValue, false));
				// 従業員サマリ加算
				plannedValueJgiSum = MathUtil.add(plannedValueJgiSum, plannedValue);
				advancePeriodValueJgiSum = MathUtil.add(advancePeriodValueJgiSum, advancePeriodValue);
			}
			// 品目合計DTO生成
			prodDtoList.add(new PlannedProdForVacResultProdInfoDto(null, plannedValueJgiSum, advancePeriodValueJgiSum, true));
			// 従業員行生成
			resultList.add(new PlannedProdForVacResultDto(sosName, teamName, jgiName, prodDtoList, false, false, false));
		}

		// ---------------------------------------
		// 結果の返却
		// ---------------------------------------
		return resultList;
	}
}
