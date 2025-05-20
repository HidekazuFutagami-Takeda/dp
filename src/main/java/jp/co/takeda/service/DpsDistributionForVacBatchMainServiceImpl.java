package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.DistributionForVacExecOrgDto;
import jp.co.takeda.dto.DistributionForVacParamsDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.security.DpUser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (ワクチン)配分実行バッチサービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionForVacBatchMainService")
public class DpsDistributionForVacBatchMainServiceImpl implements DpsDistributionForVacBatchMainService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsDistributionForVacBatchMainServiceImpl.class);

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
	 * (ワクチン)配分バッチ実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionForVacBatchService")
	protected DpsDistributionForVacBatchService dpsDistributionForVacBatchService;

	public void executeBatch(DistributionForVacParamsDto paramsDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (paramsDto == null) {
			final String errMsg = "配分実行（非同期処理）用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		DpUser execDpUser = paramsDto.getDpUser();
		List<DistributionForVacExecOrgDto> orgDistributionExecOrgDtoList = paramsDto.getDistributionForVacExecOrgDtoList();
		if (orgDistributionExecOrgDtoList == null || orgDistributionExecOrgDtoList.size() == 0) {
			throw new SystemException(new Conveyance(new MessageKey("DPC1012E", "品目")));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 処理開始時間
		Date startTime = commonDao.getSystemTime();
		try {

			// ------------------------
			// 品目単位に配分処理を実行
			// ------------------------
			// お知らせ用の品目リスト
			List<String> errProdList = new ArrayList<String>();
			// 配分ミスリスト
			List<DistributionMissDto> missDtoAllList = new ArrayList<DistributionMissDto>();
			for (DistributionForVacExecOrgDto distributionExecOrgDto : orgDistributionExecOrgDtoList) {
				try {
					List<DistributionMissDto> missDtoList = dpsDistributionForVacBatchService.executeDistProd(execDpUser, distributionExecOrgDto, paramsDto.isMrOpenCheck());
					if (missDtoList != null && missDtoList.size() != 0) {
						missDtoAllList.addAll(missDtoList);
					}
				} catch (Exception e) {
					// エラーが発生した場合、お知らせ用の品目リストに追加
					errProdList.add(distributionExecOrgDto.getProdName());
					LOG.error("【配分品目】「" + distributionExecOrgDto.getProdCode() + "」の配分処理でエラー発生", e);
					// ステータスを元に戻す
					try {
						dpsDistributionForVacBatchService.resumeStatus(execDpUser, distributionExecOrgDto);
					} catch (Exception e2) {
						// 品目単位ではエラーとしない
						LOG.error("【配分品目】「" + distributionExecOrgDto.getProdCode() + "」のステータス復元処理でエラー発生", e2);
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
			if (errProdList.size() != 0) {
				// エラー登録
				dpsDistributionForVacBatchService.entryContactOperations(errProdList, startTime, ContactResultType.FAILURE, paramsDto.getDpUser(), null);
			} else if (missDtoAllList != null && missDtoAllList.size() != 0) {
				// 配分ミス登録
				dpsDistributionForVacBatchService
					.entryMissListContactOperations(errProdList, startTime, ContactResultType.SUCCESS_DIST_MISS, paramsDto.getDpUser(), missDtoAllList);
			} else {
				// 正常終了登録
				dpsDistributionForVacBatchService.entryContactOperations(errProdList, startTime, ContactResultType.SUCCESS, execDpUser, null);
			}

		} catch (Exception e) {
			LOG.error("【配分バッチ実行サービスで致命的エラー発生：アテンション登録開始】", e);
			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
			dpsDistributionForVacBatchService.entryContactOperations(null, startTime, ContactResultType.FAILURE, paramsDto.getDpUser(), null);
			LOG.error("【配分バッチ実行サービスで致命的エラー発生：アテンション登録完了】");
		}
	}
}
