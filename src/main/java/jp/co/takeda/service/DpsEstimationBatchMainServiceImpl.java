package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.EstimationExecOrgDto;
import jp.co.takeda.dto.EstimationParamsDto;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.security.DpUser;

/**
 * 試算実行バッチサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsEstimationBatchMainService")
public class DpsEstimationBatchMainServiceImpl implements DpsEstimationBatchMainService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsEstimationBatchMainServiceImpl.class);

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
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 計画支援コードマスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	protected DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 試算バッチ実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsEstimationBatchService")
	protected DpsEstimationBatchService dpsEstimationBatchService;

	public void executeBatch(EstimationParamsDto iyakuEstimationDto) throws LogicalException {

		// 処理開始時間取得
		Date startTime = commonDao.getSystemTime();
		if (iyakuEstimationDto == null) {
			final String errMsg = "試算実行（非同期処理）用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		try {
			// ----------------------
			// 引数チェック
			// ----------------------
			String sosCd3 = iyakuEstimationDto.getSosCd3();
// add Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
			String category = iyakuEstimationDto.getCategory();
// add End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
			DpUser execDpUser = iyakuEstimationDto.getDpUser();
			List<EstimationExecOrgDto> orgEstimationExecOrgDtoList = iyakuEstimationDto.getOrgMrPlanStatusList();
			if (sosCd3 == null) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
				throw new SystemException(new Conveyance(new MessageKey("DPC1006E", "エリア")));
//				throw new SystemException(new Conveyance(new MessageKey("DPC1006E", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			}
			if (orgEstimationExecOrgDtoList == null || orgEstimationExecOrgDtoList.size() == 0) {
				throw new SystemException(new Conveyance(new MessageKey("DPC1012E", "品目")));
			}
			if (execDpUser == null) {
				final String errMsg = "実行者従業員情報がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// ------------------------
			// 組織情報の取得
			// ------------------------
			SosMst sosMst = sosMstDAO.search(sosCd3);

			// ------------------------
			// 品目毎に試算処理実行
			// ------------------------

			// お知らせ用の品目リスト
			List<String> errProdList = new ArrayList<String>();
			for (EstimationExecOrgDto estimationExecOrgDto : orgEstimationExecOrgDtoList) {
				try {
					dpsEstimationBatchService.executeEstProd(sosMst, execDpUser, estimationExecOrgDto.getMrPlanStatus());
				} catch (Exception e) {
					// エラーが発生した場合、お知らせ用の品目リストに追加
					errProdList.add(estimationExecOrgDto.getProdName());
					if (LOG.isErrorEnabled()) {
						LOG.error("【試算品目】「" + estimationExecOrgDto.getProdCode() + "」の試算処理でエラー発生", e);
					}
					// ステータスを元に戻す
					try {
						dpsEstimationBatchService.resumeStatus(sosCd3, estimationExecOrgDto, execDpUser);
					} catch (Exception e2) {
						// 品目単位ではエラーとしない
						if (LOG.isErrorEnabled()) {
							LOG.error("【試算品目】「" + estimationExecOrgDto.getProdCode() + "」のステータス復元処理でエラー発生", e);
						}
					}
				}

				// 接続維持のため問い合わせ
				try {
					commonDao.getSystemTime();
				} catch (Exception e) {
					LOG.error("接続維持のため問い合わせでエラー:" + e.getMessage());
				}
			}

			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
// mod Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
//			if(isVaccine(iyakuEstimationDto)) {
//				if (errProdList.size() == 0) {
//					dpsEstimationBatchService.entryContactOperationsForVac(sosCd3, errProdList, startTime, ContactResultType.SUCCESS, execDpUser);
//				} else {
//					dpsEstimationBatchService.entryContactOperationsForVac(iyakuEstimationDto.getSosCd3(), errProdList, startTime, ContactResultType.FAILURE, iyakuEstimationDto.getDpUser());
//				}
//			}else {
// del End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
//				if (errProdList.size() == 0) {
				if (errProdList.size() == 0) {
//					dpsEstimationBatchService.entryContactOperations(sosCd3, errProdList, startTime, ContactResultType.SUCCESS, execDpUser);
					dpsEstimationBatchService.entryContactOperations(sosCd3, errProdList, startTime, ContactResultType.SUCCESS, execDpUser, category);
//				} else {
				} else {
//					dpsEstimationBatchService.entryContactOperations(iyakuEstimationDto.getSosCd3(), errProdList, startTime, ContactResultType.FAILURE, iyakuEstimationDto.getDpUser());
					dpsEstimationBatchService.entryContactOperations(iyakuEstimationDto.getSosCd3(), errProdList, startTime, ContactResultType.FAILURE, iyakuEstimationDto.getDpUser(), category);
//				}
				}
//			}
// mod End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更

		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("【試算バッチ実行サービスで致命的エラー発生：アテンション登録開始】", e);
			}
			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
// mos Start 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更
// 			if(isVaccine(iyakuEstimationDto)) {
//				dpsEstimationBatchService.entryContactOperationsForVac(iyakuEstimationDto.getSosCd3(), null, startTime, ContactResultType.FAILURE, iyakuEstimationDto.getDpUser());
//			}else {
//				dpsEstimationBatchService.entryContactOperations(iyakuEstimationDto.getSosCd3(), null, startTime, ContactResultType.FAILURE, iyakuEstimationDto.getDpUser());
				dpsEstimationBatchService.entryContactOperations(iyakuEstimationDto.getSosCd3(), null, startTime, ContactResultType.FAILURE, iyakuEstimationDto.getDpUser(), iyakuEstimationDto.getCategory());
//			}
// mos End 2022/6/3 R.takamoto バックログNo.11　試算処理における「お知らせ」の表示内容変更 */
			if (LOG.isErrorEnabled()) {
				LOG.error("【試算バッチ実行サービスで致命的エラー発生：アテンション登録完了】");
			}
		}
	}

	/**
	 * 試算対象のカテゴリがワクチンか否か
	 * @param iyakuEstimationDto
	 */
	private boolean isVaccine(EstimationParamsDto iyakuEstimationDto) {
		try {
			List<EstimationExecOrgDto> orgMrPlanStatusList = iyakuEstimationDto.getOrgMrPlanStatusList();
			String prodCode = orgMrPlanStatusList.get(0).getProdCode();
			PlannedProd plannedProd = plannedProdDAO.search(prodCode);
			String category = plannedProd.getCategory();
			String dataCd = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue()).get(0).getDataCd();
			if(category.equals(dataCd)) {
				return true;
			}
			return false;

		} catch (Exception e) {
			final String errMsg = "カテゴリを特定できない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
	}
}
