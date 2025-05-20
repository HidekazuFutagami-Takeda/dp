package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

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
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.ManageWsPlanDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.ManageWsPlanDetailDto;
import jp.co.takeda.dto.ManageWsPlanDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuProdHeaderDto;
import jp.co.takeda.dto.ManageWsPlanProdDetailDto;
import jp.co.takeda.dto.ManageWsPlanProdDto;
import jp.co.takeda.dto.ManageWsPlanProdScDto;
import jp.co.takeda.dto.ManageWsPlanScDto;
import jp.co.takeda.logic.CreateTmsTytenCdLogic;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.ManagePlannedProd;
import jp.co.takeda.model.ManageWsPlan;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 【管理】特約店別計画検索サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmTmsTytenPlanSearchService")
public class DpmTmsTytenPlanSearchServiceImpl implements DpmTmsTytenPlanSearchService {

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	protected ManagePlannedProdDao managePlannedProdDao;

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamYTDao")
	protected ManageChangeParamYTDao manageChangeParamYTDao;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnRealDao")
	protected TmsTytenMstUnRealDao tmsTytenMstUnRealDao;

	/**
	 * 管理の特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageWsPlanDao")
	protected ManageWsPlanDao manageWsPlanDao;

	/**
	 * 計画管理汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("codeMasterDao")
	protected CodeMasterDao codeMasterDao;

	/**
	 * コードマスタデータ取得サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmCodeMasterSearchService")
	protected DpmCodeMasterSearchService dpmCodeMasterSearchService;

	// ヘッダ検索
	public ManageWsPlanForIyakuHeaderDto searchHeader(ManageWsPlanScDto scDto) throws LogicalException {

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

		return new ManageWsPlanForIyakuHeaderDto(tmsTytenCd, tmsTytenMstUn.getTmsTytenMeiKj());
	}

	// 特約店別計画一覧検索
	public ManageWsPlanDto searchList(ManageWsPlanScDto scDto) throws LogicalException {

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
		// 品目情報取得
		// -----------------------------
		ManagePlannedProd plannedProd = managePlannedProdDao.search(prodCode);

		// -----------------------------
		// T/Y変換率取得
		// -----------------------------
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		ChangeParamYT changeParamYT = manageChangeParamYTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);

		// -----------------------------
		// 特約店別計画取得
		// -----------------------------
		boolean allWsFlg = false;
		if (planData.equals(PlanData.ALL)) {
			allWsFlg = true;
		}
		List<ManageWsPlan> wsPlanList = manageWsPlanDao.searchListByProd(ManageWsPlanDao.SORT_STRING, prodCode, tmsTytenCd, allWsFlg);

		// -----------------------------
		// 特約店別計画明細を生成
		// -----------------------------
		List<ManageWsPlanDetailDto> detailList = new ArrayList<ManageWsPlanDetailDto>();
		// 中間集計対象の桁数
		Integer sumLength = null;
		if (tmsTytenCd.length() < 13) {
			sumLength = tmsTytenCd.length() + 2;
		}

		// 中間リスト
		List<ManageWsPlanDetailDto> tmpDetailList = new ArrayList<ManageWsPlanDetailDto>();
		// 中間集計用計画値
		Long baseTUhSum = 0L;
		Long baseTPSum = 0L;
		Long baseTZSum = 0L;
		Long baseYUhSum = 0L;
		Long baseYPSum = 0L;
		Long baseYZSum = 0L;
		// 中間集計対象特約店コード
		String tmsTytenCdPart = null;
		for (int i = 0; i < wsPlanList.size(); i++) {
			// -----------
			// 明細行生成
			// -----------
			ManageWsPlanDetailDto detailDto = null;
			ManageWsPlan wsPlan = wsPlanList.get(i);
			String tmpTmsCode = wsPlan.getTmsTytenCd();

			// UHの場合
			if (wsPlan.getInsType().equals(InsType.UH)) {
				// 次があり、次の特約店コードが等しい場合(P)
				if (i + 1 < wsPlanList.size() && tmpTmsCode.equals(wsPlanList.get(i + 1).getTmsTytenCd())) {
					i++;
					ManageWsPlan wsPlanP = wsPlanList.get(i);
					if (!allWsFlg && wsPlan.getDelFlg() && wsPlanP.getDelFlg()) {
						continue;
					}
					detailDto = createDetailDto(wsPlan, wsPlanP, null, changeParamYT);
					// 次があり、次の特約店コードが等しい場合(Z)
					if (i + 1 < wsPlanList.size() && tmpTmsCode.equals(wsPlanList.get(i + 1).getTmsTytenCd())) {
						i++;
						ManageWsPlan wsPlanZ = wsPlanList.get(i);
						if (!allWsFlg && wsPlan.getDelFlg() && wsPlanZ.getDelFlg()) {
							continue;
						}
						detailDto = createDetailDto(wsPlan, wsPlanP, wsPlanZ, changeParamYT);
					}
					// その他
					else {
						if (!allWsFlg && wsPlan.getDelFlg()) {
							continue;
						}
						detailDto = createDetailDto(wsPlan, wsPlanP, null, changeParamYT);
					}
				}
				// その他
				else {
					if (!allWsFlg && wsPlan.getDelFlg()) {
						continue;
					}
					detailDto = createDetailDto(wsPlan, null, null, changeParamYT);
				}
			}
			// Pの場合
			else if (wsPlan.getInsType().equals(InsType.P)) {
				// 次があり、次の特約店コードが等しい場合(Z)
				if (i + 1 < wsPlanList.size() && tmpTmsCode.equals(wsPlanList.get(i + 1).getTmsTytenCd())) {
					i++;
					ManageWsPlan wsPlanZ = wsPlanList.get(i);
					if (!allWsFlg && wsPlan.getDelFlg() && wsPlanZ.getDelFlg()) {
						continue;
					}
				detailDto = createDetailDto(null, wsPlan, wsPlanZ, changeParamYT);
				}
				// その他
				else {
					if (!allWsFlg && wsPlan.getDelFlg()) {
						continue;
					}
					detailDto = createDetailDto(null, wsPlan, null, changeParamYT);
				}
			}
			// Zの場合
			else {
				ManageWsPlan wsPlanZ = wsPlanList.get(i);
				if (!allWsFlg && wsPlan.getDelFlg() && wsPlanZ.getDelFlg()) {
					continue;
				}
				detailDto = createDetailDto(null, null, wsPlanZ, changeParamYT);
			}
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
					detailList.add(createDetailDto(tmsTytenCdPart, baseYUhSum, baseYPSum, baseYZSum, baseTUhSum, baseTPSum, baseTZSum, changeParamYT));
					// リストに中間リストを追加
					detailList.addAll(tmpDetailList);
					// 新規の集計行・中間リストを生成
					baseTUhSum = 0L;
					baseTPSum = 0L;
					baseTZSum = 0L;
					baseYUhSum = 0L;
					baseYPSum = 0L;
					baseYZSum = 0L;
					tmpDetailList = new ArrayList<ManageWsPlanDetailDto>();
				}
			}
			// 集計行に加算
			baseTUhSum = MathUtil.add(baseTUhSum, detailDto.getBaseTUH());
			baseTPSum = MathUtil.add(baseTPSum, detailDto.getBaseTP());
			baseTZSum = MathUtil.add(baseTZSum, detailDto.getBaseTZ());
			baseYUhSum = MathUtil.add(baseYUhSum, detailDto.getBaseYUH());
			baseYPSum = MathUtil.add(baseYPSum, detailDto.getBaseYP());
			baseYZSum = MathUtil.add(baseYZSum, detailDto.getBaseYZ());
			// 中間リストに追加
			tmpDetailList.add(detailDto);
			// 中間集計対象特約店コード
			tmsTytenCdPart = tmpTmsCode.substring(0, sumLength);
		}
		// 中間リストに存在する場合、リストに追加
		if (tmpDetailList.size() != 0) {
			// リストに集計行を追加
			detailList.add(createDetailDto(tmsTytenCdPart, baseYUhSum, baseYPSum, baseYZSum, baseTUhSum, baseTPSum, baseTZSum, changeParamYT));
			// リストに中間リストを追加
			detailList.addAll(tmpDetailList);
		}

		int actualDataCnt = 0;
		for (ManageWsPlanDetailDto dto : detailList) {
			if (!dto.isTytenSumRowFlg()) {
				actualDataCnt++;
			}
		}
		if (actualDataCnt < 1) {
			final String errMsg = "UH/Pともに論理削除されたデータしかないためエラー";
			throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ------------
		// 結果DTO生成
		// ------------
		return new ManageWsPlanDto(plannedProd.getProdName(), detailList);
	}

	/**
	 * 明細DTOを生成する。
	 * @param wsPlanUH 特約店別計画（UH）
	 * @param wsPlanP 特約店別計画（P）
	 * @param wsPlanZ 特約店別計画（Z）
	 * @return (医)特約店別計画編集画面を表すDTO
	 */
	private ManageWsPlanDetailDto createDetailDto(ManageWsPlan wsPlanUH, ManageWsPlan wsPlanP, ManageWsPlan wsPlanZ, ChangeParamYT changeParamYT) {
		// 特約店名称
		String tmsTytemName = null;
		// ＴＭＳ特約店コード
		String tmsTytenCd = null;
		// [UH] Y価ベース
		Long baseYUH = null;
		// [UH] S価ベース
		Long baseTUH = null;
		// [UH] シーケンスキー
		Long seqKeyUH = null;
		// [UH] 最終更新者
		String upDateJgiNameUH = null;
		// [UH] 最終更新日
		Date upDateUH = null;
		// [P] Y価ベース
		Long baseYP = null;
		// [P] S価ベース
		Long baseTP = null;
		// [P] シーケンスキー
		Long seqKeyP = null;
		// [P] 最終更新者
		String upDateJgiNameP = null;
		// [P] 最終更新日
		Date upDateP = null;
		// [Z] Y価ベース
		Long baseYZ = null;
		// [Z] S価ベース
		Long baseTZ = null;
		// [Z] シーケンスキー
		Long seqKeyZ = null;
		// [Z] 最終更新者
		String upDateJgiNameZ = null;
		// [Z] 最終更新日
		Date upDateZ = null;
		// 特約店中計フラグ
		boolean tytenSumRowFlg = false;

		// UH計画
		if (wsPlanUH != null) {
			tmsTytemName = wsPlanUH.getTmsTytenName();
			tmsTytenCd = wsPlanUH.getTmsTytenCd();
			baseYUH = ConvertUtil.parseMoneyToThousandUnit(wsPlanUH.getImplPlan().getPlanned2ValueY());
			baseTUH = ConvertUtil.parseMoneyToThousandUnit(wsPlanUH.getImplPlan().getPlanned2ValueT());
			seqKeyUH = wsPlanUH.getSeqKey();
			if (wsPlanUH.getUpDate() != null) {
				upDateJgiNameUH = wsPlanUH.getUpJgiName();
			}
			upDateUH = wsPlanUH.getUpDate();
		}

		// P計画
		if (wsPlanP != null) {
			tmsTytemName = wsPlanP.getTmsTytenName();
			tmsTytenCd = wsPlanP.getTmsTytenCd();
			baseYP = ConvertUtil.parseMoneyToThousandUnit(wsPlanP.getImplPlan().getPlanned2ValueY());
			baseTP = ConvertUtil.parseMoneyToThousandUnit(wsPlanP.getImplPlan().getPlanned2ValueT());
			seqKeyP = wsPlanP.getSeqKey();
			if (wsPlanP.getUpDate() != null) {
				upDateJgiNameP = wsPlanP.getUpJgiName();
			}
			upDateP = wsPlanP.getUpDate();
		}

		// Z計画
		if (wsPlanZ != null) {
			tmsTytemName = wsPlanZ.getTmsTytenName();
			tmsTytenCd = wsPlanZ.getTmsTytenCd();
			baseYZ = ConvertUtil.parseMoneyToThousandUnit(wsPlanZ.getImplPlan().getPlanned2ValueY());
			baseTZ = ConvertUtil.parseMoneyToThousandUnit(wsPlanZ.getImplPlan().getPlanned2ValueT());
			seqKeyZ = wsPlanZ.getSeqKey();
			if (wsPlanZ.getUpDate() != null) {
				upDateJgiNameZ = wsPlanZ.getUpJgiName();
			}
			upDateZ = wsPlanZ.getUpDate();
		}

		// 特約店対象外フラグ
		boolean planTaiGaiFlgTok = false;
		if (StringUtils.isNotBlank(tmsTytenCd)) {
			try {
				TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd);
				planTaiGaiFlgTok = tmsTytenMstUn.getPlanTaiGaiFlgTok();
			} catch (DataNotFoundException e) {
			}
		}
		return new ManageWsPlanDetailDto(tmsTytemName, tmsTytenCd,
				baseYUH, baseTUH, baseTUH, seqKeyUH, upDateJgiNameUH, upDateUH,
				baseYP, baseTP, baseTP, seqKeyP, upDateJgiNameP, upDateP,
				baseYZ, baseTZ, baseTZ, seqKeyZ, upDateJgiNameZ, upDateZ,
			tytenSumRowFlg, changeParamYT.getChangeRateUh(), changeParamYT.getChangeRateP(), changeParamYT.getChangeRateZ(), planTaiGaiFlgTok);
	}

	/**
	 * 中間集計行DTOを生成する。
	 *
	 *
	 * @param tmsTytenCdPart 特約店コード
	 * @param baseYUhSum [UH] Y価ベース(合計)
	 * @param baseYPSum [P] Y価ベース(合計)
	 * @param baseYZSum [Z] Y価ベース(合計)
	 * @param baseTUhSum [UH] T価ベース(合計)
	 * @param baseTPSum [P] T価ベース(合計)
	 * @param baseTZSum [Z] T価ベース(合計)
	 * @return (医)特約店別計画編集画面を表すDTO
	 * @throws DataNotFoundException
	 */
	private ManageWsPlanDetailDto createDetailDto(String tmsTytenCdPart,
			Long baseYUhSum, Long baseYPSum, Long baseYZSum, Long baseTUhSum, Long baseTPSum, Long baseTZSum, ChangeParamYT changeParamYT) throws DataNotFoundException {
		// 特約店コードを13桁に変換
		final String tmsTytenCd13 = new CreateTmsTytenCdLogic(tmsTytenCdPart).execute();
		TmsTytenMstUn tmsTytenMstUn = tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd13);
		// 特約店名称
		String tmsTytemName = tmsTytenMstUn.getTmsTytenMeiKj();
		// 特約店コード
		String tmsTytenCd = tmsTytenCdPart;
		// 特約店中計フラグ
		boolean tytenSumRowFlg = true;
		// 特約店対象外フラグ
		boolean planTaiGaiFlgTok = false;

		return new ManageWsPlanDetailDto(tmsTytemName, tmsTytenCd,
				baseYUhSum, baseTUhSum, baseTUhSum, null, null, null,
				baseYPSum, baseTPSum, baseTPSum, null, null, null,
				baseYZSum, baseTZSum, baseTZSum, null, null, null,
				tytenSumRowFlg, changeParamYT.getChangeRateUh(), changeParamYT.getChangeRateP(), changeParamYT.getChangeRateZ(), planTaiGaiFlgTok);
	}

	// ヘッダ検索(品目一覧)
	public ManageWsPlanForIyakuProdHeaderDto searchHeader(ManageWsPlanProdScDto scDto) throws LogicalException {

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

		// 品目カテゴリ名称
		String prodCategoryName = null;
		if (StringUtils.isNotBlank(scDto.getProdCategory())) {

			// 検索結果一覧
			try {
				// カテゴリの検索
				prodCategoryName = codeMasterDao.searchOneCategory(CodeMaster.CAT.getDbValue(), scDto.getProdCategory()).getDataName();
			} catch (DataNotFoundException e) {
				// 検索結果0件の場合は何もしない
			}

		}
		return new ManageWsPlanForIyakuProdHeaderDto(tmsTytenCd, tmsTytenMstUn.getTmsTytenMeiKj(), prodCategoryName, planTaiGaiFlgTok);
	}

	// 特約店別計画一覧検索(品目一覧)
	public ManageWsPlanProdDto searchList(ManageWsPlanProdScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodCategory = scDto.getProdCategory();
		if (prodCategory == null) {
			final String errMsg = "検索対象の品目カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String tmsTytenCd = new CreateTmsTytenCdLogic(scDto.getTmsTytenCd()).execute();
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
		tmsTytenMstUnRealDao.searchRealRef(tmsTytenCd);

		// -----------------------------
		// 特約店別計画取得
		// -----------------------------
		boolean allProdFlg = false;
		if (planData.equals(PlanData.ALL)) {
			allProdFlg = true;
		}

		List<ManageWsPlan> wsPlanList = manageWsPlanDao.searchListByWs(tmsTytenCd, prodCategory, allProdFlg, scDto.isVaccine());

		// -----------------------------
		// 特約店別計画明細を生成
		// -----------------------------
		List<ManageWsPlanProdDetailDto> detailList = new ArrayList<ManageWsPlanProdDetailDto>();
		ManageWsPlan wsPlanUH = null;
		ManageWsPlan wsPlanP = null;
		ManageWsPlan wsPlanZ = null;
		for (int i = 0; i < wsPlanList.size(); i++) {
			// -----------
			// 明細行生成
			// -----------
			ManageWsPlan wsPlan = wsPlanList.get(i);

			// UH、P、雑の判断
			switch(wsPlan.getInsType()) {
				case UH:
					wsPlanUH = wsPlan;
					break;
				case P:
					wsPlanP = wsPlan;
					break;
				case ZATU:
					wsPlanZ = wsPlan;
					break;
			}

			// 次があり、次の品目固定コードが等しい場合は次の要素を判断する
			if (i + 1 < wsPlanList.size() && wsPlan.getProdCode().equals(wsPlanList.get(i + 1).getProdCode())) {
				continue;
			}
			// 全品目の場合、およびUH、P、雑のいずれかが有効な場合は画面表示に追加する
			if (!allProdFlg
					&& (wsPlanUH != null && wsPlanUH.getDelFlg())
					&& (wsPlanP != null && wsPlanP.getDelFlg())
					&& (wsPlanZ != null && wsPlanZ.getDelFlg())
					) {
				continue;
			}

			detailList.add(createProdDetailDto(wsPlanUH, wsPlanP, wsPlanZ, scDto.isRyutsu()));
			wsPlanUH = null;
			wsPlanP = null;
			wsPlanZ = null;

		}

		if (detailList.size() < 1) {
			final String errMsg = "UH/P/Zともに論理削除されたデータしかないためエラー";
			throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ------------
		// 結果DTO生成
		// ------------
		// ヘッダ部生成
		return new ManageWsPlanProdDto(detailList);
	}

	/**
	 * 明細DTOを生成する。
	 *
	 * @param wsPlanUH 特約店別計画（UH）
	 * @param wsPlanP 特約店別計画（P）
	 * @param wsPlanZ 特約店別計画（Z）
	 * @param ryutsu 流通フラグ
	 * @return 特約店別計画編集画面を表すDTO
	 */
	private ManageWsPlanProdDetailDto createProdDetailDto(ManageWsPlan wsPlanUH, ManageWsPlan wsPlanP, ManageWsPlan wsPlanZ, boolean ryutsu) {
		// 品目名称
		String prodName = null;
		// 品目固定コード
		String prodCode = null;
		// [UH] Y価ベース
		Long baseYUH = null;
		// [UH] S価ベース
		Long baseSUH = null;
		// [UH] シーケンスキー
		Long seqKeyUH = null;
		// [UH] 最終更新者
		String upDateJgiNameUH = null;
		// [UH] 最終更新日
		Date upDateUH = null;
		// [P] Y価ベース
		Long baseYP = null;
		// [P] S価ベース
		Long baseSP = null;
		// [P] シーケンスキー
		Long seqKeyP = null;
		// [P] 最終更新者
		String upDateJgiNameP = null;
		// [P] 最終更新日
		Date upDateP = null;

		// [Z] Y価ベース
		Long baseYZ = null;
		// [Z] S価ベース
		Long baseSZ = null;
		// [Z] シーケンスキー
		Long seqKeyZ = null;
		// [Z] 最終更新者
		String upDateJgiNameZ = null;
		// [Z] 最終更新日
		Date upDateZ = null;

		// [UH] T/Y変換率
		Double tyChangeRateUH = null;
		// [P] T/Y変換率
		Double tyChangeRateP = null;
		// [Z] T/Y変換率
		Double tyChangeRateZ = null;

		// UH計画
		if (wsPlanUH != null) {
			prodName = wsPlanUH.getProdName();
			prodCode = wsPlanUH.getProdCode();
			baseYUH = ConvertUtil.parseMoneyToThousandUnit(wsPlanUH.getImplPlan().getPlanned2ValueY());
			if(ryutsu) {
	            baseSUH = ConvertUtil.parseMoneyToThousandUnit(wsPlanUH.getImplPlan().getPlanned2ValueT());
			}
			seqKeyUH = wsPlanUH.getSeqKey();
			if (wsPlanUH.getUpDate() != null) {
				upDateJgiNameUH = wsPlanUH.getUpJgiName();
			}
			upDateUH = wsPlanUH.getUpDate();
		}

		// P計画
		if (wsPlanP != null) {
			prodName = wsPlanP.getProdName();
			prodCode = wsPlanP.getProdCode();
			baseYP = ConvertUtil.parseMoneyToThousandUnit(wsPlanP.getImplPlan().getPlanned2ValueY());
            if(ryutsu) {
                baseSP = ConvertUtil.parseMoneyToThousandUnit(wsPlanP.getImplPlan().getPlanned2ValueT());
            }
			seqKeyP = wsPlanP.getSeqKey();
			if (wsPlanP.getUpDate() != null) {
				upDateJgiNameP = wsPlanP.getUpJgiName();
			}
			upDateP = wsPlanP.getUpDate();
		}

		// Z計画
		if (wsPlanZ != null) {
			prodName = wsPlanZ.getProdName();
			prodCode = wsPlanZ.getProdCode();
			baseYZ = ConvertUtil.parseMoneyToThousandUnit(wsPlanZ.getImplPlan().getPlanned2ValueY());
            if(ryutsu) {
                baseSZ = ConvertUtil.parseMoneyToThousandUnit(wsPlanZ.getImplPlan().getPlanned2ValueT());
            }
			seqKeyZ = wsPlanZ.getSeqKey();
			if (wsPlanZ.getUpDate() != null) {
				upDateJgiNameZ = wsPlanZ.getUpJgiName();
			}
			upDateZ = wsPlanZ.getUpDate();
		}

		// -----------------------------
		// T/Y変換率取得
		// -----------------------------
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		try {
			ChangeParamYT changeParamYT = manageChangeParamYTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
			tyChangeRateUH = changeParamYT.getChangeRateUh();
			tyChangeRateP = changeParamYT.getChangeRateP();
            tyChangeRateZ = changeParamYT.getChangeRateZ();
		} catch (DataNotFoundException e) {
			final String errMsg = "T/Y変換率取得に失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		return new ManageWsPlanProdDetailDto(prodName, prodCode, baseSUH, baseYUH, seqKeyUH, upDateJgiNameUH, upDateUH, baseSP, baseYP, seqKeyP, upDateJgiNameP, upDateP,
			baseSZ, baseYZ, seqKeyZ, upDateJgiNameZ, upDateZ, tyChangeRateUH, tyChangeRateP, tyChangeRateZ);
	}
}
