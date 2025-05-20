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
import jp.co.takeda.dao.DpsCExceptPlanDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.DistributionExecOrgDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.DistributionParamsDto;
import jp.co.takeda.dto.DocDistributionJgiDto;
import jp.co.takeda.dto.DocDistributionParamsDto;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpUser;

/**
 * 配分実行バッチサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsDistributionBatchMainService")
public class DpsDistributionBatchMainServiceImpl implements DpsDistributionBatchMainService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsDistributionBatchMainServiceImpl.class);

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
	 * 従業員情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCExceptPlanDao")
	protected DpsCExceptPlanDao dpsCExceptPlanDao;

	/**
	 * 施設医師別計画配分バッチ実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDocDistributionBatchService")
	protected DpsDocDistributionBatchService dpsDocDistributionBatchService;

	/**
	 * 施設特約店別計画配分バッチ実行サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsDistributionBatchService")
	protected DpsDistributionBatchService dpsDistributionBatchService;

	// 施設特約点別計画配分（バッチ起動）
	public void executeInsWsBatch(DistributionParamsDto paramsDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (paramsDto == null) {
			final String errMsg = "配分実行（非同期処理）用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String sosCd3 = paramsDto.getSosCd3();
		DpUser execDpUser = paramsDto.getDpUser();
		String category = paramsDto.getCategory();
		List<DistributionExecOrgDto> orgDistributionExecOrgDtoList = paramsDto.getDistributionExecOrgDtoList();
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (sosCd3 == null) {
//			throw new SystemException(new Conveyance(new MessageKey("DPC1006E", "営業所")));
//		}
		if (orgDistributionExecOrgDtoList == null || orgDistributionExecOrgDtoList.size() == 0) {
			throw new SystemException(new Conveyance(new MessageKey("DPC1012E", "品目")));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (execDpUser == null) {
			final String errMsg = "実行者従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 処理開始時間
		Date startTime = commonDao.getSystemTime();
		try {
			// 組織情報の取得
			SosMst sosMst = null;
			if (sosCd3 == null) {
				// ワクチンの場合、組織未選択時は全社対象とする
			}else {
				sosMst = sosMstDAO.search(sosCd3);
			}

			// ------------------------
			// 品目単位に配分処理を実行
			// ------------------------
			// お知らせ用の品目リスト
			List<String> errProdList = new ArrayList<String>();
			// 配分ミスリスト
			List<DistributionMissDto> missDtoAllList = new ArrayList<DistributionMissDto>();
			for (DistributionExecOrgDto distributionExecOrgDto : orgDistributionExecOrgDtoList) {
				try {
					List<DistributionMissDto> missDtoList = dpsDistributionBatchService.executeDistProd(sosMst, category, execDpUser, distributionExecOrgDto, paramsDto
						.isMrOpenCheck());
					if (missDtoList != null && missDtoList.size() != 0) {
						missDtoAllList.addAll(missDtoList);
					}
				} catch (Exception e) {
					// エラーが発生した場合、お知らせ用の品目リストに追加
					errProdList.add(distributionExecOrgDto.getProdName());
					LOG.error("【配分品目】「" + distributionExecOrgDto.getProdCode() + "」の配分処理でエラー発生", e);
					// ステータスを元に戻す
					try {
						dpsDistributionBatchService.resumeStatus(execDpUser, distributionExecOrgDto);
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

			// ----------------------------------------------
			// 配分処理後に計画対象外施設・特約店チェック
			// ----------------------------------------------
			try {
				dpsCExceptPlanDao.updateCheakDelflg();
			} catch (Exception e) {
				if (LOG.isErrorEnabled()) {
					LOG.error("削除（予定）施設に計画がされていないことの確認に失敗。", e);
				}
			}
			try {
				dpsCExceptPlanDao.updateCheakPlantaigaiflg();
			} catch (Exception e) {
				if (LOG.isErrorEnabled()) {
					LOG.error("計画立案対象外特約店に計画がされていないことの確認に失敗。", e);
				}
			}

			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
			if (errProdList.size() != 0) {
				// エラー登録
				if (sosCd3 == null) {
					dpsDistributionBatchService.entryContactOperations(SosMst.SOS_CD1, errProdList, startTime, ContactResultType.FAILURE, paramsDto.getDpUser(), category, null);
				}else {
					dpsDistributionBatchService.entryContactOperations(paramsDto.getSosCd3(), errProdList, startTime, ContactResultType.FAILURE, paramsDto.getDpUser(), category, null);
				}
			} else if (missDtoAllList != null && missDtoAllList.size() != 0) {
				// 配分ミス登録
				dpsDistributionBatchService.entryMissListContactOperations(sosMst, errProdList, startTime, ContactResultType.SUCCESS_DIST_MISS, paramsDto.getDpUser(), category,
					missDtoAllList);
			} else {
				// 正常終了登録
				if (sosCd3 == null) {
					dpsDistributionBatchService.entryContactOperations(SosMst.SOS_CD1, errProdList, startTime, ContactResultType.SUCCESS, execDpUser, category, null);
				}else {
					dpsDistributionBatchService.entryContactOperations(sosCd3, errProdList, startTime, ContactResultType.SUCCESS, execDpUser, category, null);
				}
			}

		} catch (Exception e) {
			LOG.error("【配分バッチ実行サービスで致命的エラー発生：アテンション登録開始】", e);
			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
			dpsDistributionBatchService.entryContactOperations(paramsDto.getSosCd3(), null, startTime, ContactResultType.FAILURE, paramsDto.getDpUser(), category, null);
			LOG.error("【配分バッチ実行サービスで致命的エラー発生：アテンション登録完了】");
		}
	}

	// 施設医師別計画配分
	public void executeInsDocBatch(DocDistributionParamsDto paramsDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (paramsDto == null) {
			final String errMsg = "配分実行（非同期処理）用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (paramsDto.getSosCd3() == null) {
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
			throw new SystemException(new Conveyance(new MessageKey("DPC1006E", "エリア")));
//			throw new SystemException(new Conveyance(new MessageKey("DPC1006E", "営業所")));
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		}
		if (paramsDto.getDpUser() == null) {
			final String errMsg = "実行者従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String sosCd = paramsDto.getSosCd3();
		DpUser execDpUser = paramsDto.getDpUser();
		boolean isMainHaibun = paramsDto.isMainHaibun();

		// 業務連絡区分（メイン配分 or 再配分）
		ContactOperationsType opeType = null;
		if(isMainHaibun){
			opeType = ContactOperationsType.INS_DOC_DIST_MMP;
		} else {
			opeType = ContactOperationsType.INS_DOC_RE_DIST_MMP;
		}

		// 処理開始時間
		Date startTime = commonDao.getSystemTime();
		try {

			// 組織情報の取得
			SosMst sosMst = sosMstDAO.search(sosCd);

			// ------------------------
			// 従業員単位に配分処理を実行
			// ------------------------
			List<DistributionMissDto> missDtoAllList = new ArrayList<DistributionMissDto>();
			boolean isError = false;
			for (DocDistributionJgiDto jgiDto : paramsDto.getJgiList()) {

				JgiMst jgiMst = jgiDto.getJgiMst();
				boolean doMrOpen = jgiDto.getDoMrOpen();
				boolean doPlanClear = jgiDto.getDoPlanClear();

				// 配分処理実行
				try {
					List<DistributionMissDto> missDtoList = dpsDocDistributionBatchService.executeDistProd(sosMst, jgiMst, doMrOpen, doPlanClear, isMainHaibun, execDpUser);
					if (missDtoList != null && missDtoList.size() != 0) {
						missDtoAllList.addAll(missDtoList);
					}

				} catch (Exception e) {

					isError = true;
					LOG.error("施設医師別計画配分処理でエラー発生", e);

					// ------------------------
					// ステータスを元に戻す
					// ------------------------
					try {
						dpsDocDistributionBatchService.resumeStatus(jgiMst, execDpUser);
					} catch (Exception e2) {
						// 品目単位ではエラーとしない
						LOG.error("施設医師別計画配分処理のステータス復元処理でエラー発生", e2);
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
			if (isError) {
				// エラー登録
				dpsDocDistributionBatchService.entryContactOperations(paramsDto.getSosCd3(), startTime, ContactResultType.FAILURE, paramsDto.getDpUser(), null, opeType);
			} else if (missDtoAllList != null && missDtoAllList.size() != 0) {
				// 配分ミス登録
				dpsDocDistributionBatchService.entryMissListContactOperations(sosMst, startTime, ContactResultType.SUCCESS_DIST_MISS, paramsDto.getDpUser(),missDtoAllList, opeType);
			} else {
				// 正常終了登録
				dpsDocDistributionBatchService.entryContactOperations(sosCd, startTime, ContactResultType.SUCCESS, execDpUser, null, opeType);
			}

		} catch (Exception e) {
			LOG.error("【配分バッチ実行サービスで致命的エラー発生：アテンション登録開始】", e);
			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
			dpsDocDistributionBatchService.entryContactOperations(paramsDto.getSosCd3(), startTime, ContactResultType.FAILURE, paramsDto.getDpUser(), null, opeType);
			LOG.error("【配分バッチ実行サービスで致命的エラー発生：アテンション登録完了】");
		}
	}

}
