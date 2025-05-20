package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dto.ContactOperationsEntryDto;
import jp.co.takeda.dto.TmsTytenDistDto;
import jp.co.takeda.dto.TmsTytenDistParamDto;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.security.DpUser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 特約店別計画配分処理メインサービス実装クラス
 * 
 * @author yokokawa
 */
@Transactional
@Service("dpsTmsTytenDistMainService")
public class DpsTmsTytenDistMainServiceImpl implements DpsTmsTytenDistMainService {
	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsTmsTytenDistMainServiceImpl.class);

	/**
	 * DB共通情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 特約店別計画配分処理 サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsTmsTytenDistExecuteService")
	protected DpsTmsTytenDistExecuteService dpsTmsTytenDistExecuteService;

	// 特約店別計画配分処理を実行
	public void execute(TmsTytenDistParamDto tmsTytenDistParamDto) throws LogicalException {

		// 処理開始時間
		Date startTime = commonDao.getSystemTime();
		// ----------------------
		// 引数チェック
		// ----------------------
		// 特約店別計画配分パラメータ(非同期処理用)DTOチェック
		if (tmsTytenDistParamDto == null) {
			final String errMsg = "特約店別計画配分パラメータ(非同期処理用)DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		try {

			// 組織(支店)チェック
			if (tmsTytenDistParamDto.getSosMst2() == null) {
				final String errMsg = "組織(支店)がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 品目情報リストチェック
			if (tmsTytenDistParamDto.getPlannedProdList() == null) {
				final String errMsg = "品目情報リストがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

			} else if (tmsTytenDistParamDto.getPlannedProdList().size() == 0) {
				final String errMsg = "品目情報リストが0件";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 組織コード(営業所)リストチェック
			if (tmsTytenDistParamDto.getSosMst3List() == null) {
				final String errMsg = "組織コード(営業所)リストがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

			} else if (tmsTytenDistParamDto.getSosMst3List().size() == 0) {
				final String errMsg = "組織コード(営業所)リストが0件";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 実行者従業員情報チェック
			if (tmsTytenDistParamDto.getDpUser() == null) {
				final String errMsg = "実行者従業員情報がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 実行前ステータスチェック
			if (tmsTytenDistParamDto.getBeforeWsPlanStatusList() == null) {
				final String errMsg = "実行前ステータスがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

			} else if (tmsTytenDistParamDto.getBeforeWsPlanStatusList().size() == 0) {
				final String errMsg = "実行前ステータスが0件";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 配分基準(UH)チェック
			if (tmsTytenDistParamDto.getDistParamOfficeUHList() == null) {
				final String errMsg = "配分基準(UH)がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

			} else if (tmsTytenDistParamDto.getDistParamOfficeUHList().size() == 0) {
				final String errMsg = "配分基準(UH)が0件";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// 配分基準(P)チェック
			if (tmsTytenDistParamDto.getDistParamOfficePList() == null) {
				final String errMsg = "配分基準(P)がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

			} else if (tmsTytenDistParamDto.getDistParamOfficePList().size() == 0) {
				final String errMsg = "配分基準(P)が0件";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

			// ------------------------------------------
			// 特約店別計画配分(品目単位)
			// ※品目単位でトランザクションを生成
			// ------------------------------------------
			List<PlannedProd> plannedProdList = tmsTytenDistParamDto.getPlannedProdList();
			SosMst sosMst2 = tmsTytenDistParamDto.getSosMst2();
			List<SosMst> sosMst3List = tmsTytenDistParamDto.getSosMst3List();
			List<DistParamOffice> distParamOfficeUHList = tmsTytenDistParamDto.getDistParamOfficeUHList();
			List<DistParamOffice> distParamOfficePList = tmsTytenDistParamDto.getDistParamOfficePList();
			DpUser dpUser = tmsTytenDistParamDto.getDpUser();

			TmsTytenDistDto tmsTytenDistDto = new TmsTytenDistDto(null, new ArrayList<String>());
			// お知らせ用の品目リスト
			List<String> errProdList = new ArrayList<String>();
			for (PlannedProd plannedProd : plannedProdList) {
				try {
					// 品目単位の配分処理実行
					tmsTytenDistDto = dpsTmsTytenDistExecuteService.executeDistProd(sosMst2, plannedProd, sosMst3List, distParamOfficeUHList, distParamOfficePList, dpUser,
						startTime, tmsTytenDistDto);

				} catch (Exception e) {
					// エラーが発生した場合、お知らせ用の品目リストに追加
					errProdList.add(plannedProd.getProdName());
					// 配分処理でエラーが発生した場合、ステータスをロールバック
					try {
						LOG.error("【配分品目】「" + plannedProd.getProdCode() + "」の配分処理でエラー発生", e);

						List<WsPlanStatus> beforeWsPlanStatusList = tmsTytenDistParamDto.getBeforeWsPlanStatusList();
						dpsTmsTytenDistExecuteService.rollBackStatus(sosMst2, plannedProd, beforeWsPlanStatusList);

					} catch (Exception e2) {
						// ステータスロールバック時エラー
						LOG.error("【配分品目】「" + plannedProd.getProdCode() + "」のステータス復元処理でエラー発生", e2);
					}
				}
				
				// 接続維持のため問い合わせ
				try {
					commonDao.getSystemTime();
				} catch (Exception e) {
					LOG.error("接続維持のため問い合わせでエラー:" + e.getMessage());
				}
			}

			// ----------------------
			// 正常終了時
			// アテンション・お知らせ登録情報生成
			// ----------------------
			// 固定項目設定
			String sosCd = tmsTytenDistParamDto.getSosMst2().getSosCd();
			ContactOperationsType contactOperationsType = ContactOperationsType.WS_DIST;

			// 処理結果判定
			Long missListFileId = tmsTytenDistDto.getMissListFileId();

			ContactResultType contactResultType;
			if (errProdList.size() != 0) {
				// 異常終了を設定
				contactResultType = ContactResultType.FAILURE;
			} else if (missListFileId == null) {
				// 配分ミスが無い場合、正常終了を設定
				contactResultType = ContactResultType.SUCCESS;
			} else {
				// 配分ミスがある場合、正常終了（配分ミスあり）を設定
				contactResultType = ContactResultType.SUCCESS_DIST_MISS;
			}

			// 処理終了時間
			Date endTime = commonDao.getSystemTime();

			// アテンション・お知らせ登録情報生成
			ContactOperationsEntryDto contactOperationsEntryDto = new ContactOperationsEntryDto(sosCd, dpUser, contactOperationsType, contactResultType, startTime, endTime,
				errProdList, missListFileId);

			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
			dpsTmsTytenDistExecuteService.entryContactOperations(contactOperationsEntryDto);

		} catch (Exception e) {
			LOG.error("【特約店別計画配分処理 メインサービスで致命的エラー発生：アテンション登録開始】", e);
			// ----------------------
			// エラー終了時
			// アテンション・お知らせ登録情報生成
			// ----------------------
			// 固定項目設定
			String sosCd = tmsTytenDistParamDto.getSosMst2().getSosCd();
			DpUser dpUser = tmsTytenDistParamDto.getDpUser();
			ContactOperationsType contactOperationsType = ContactOperationsType.WS_DIST;
			ContactResultType contactResultType = ContactResultType.FAILURE;

			// 処理終了時間
			Date endTime = commonDao.getSystemTime();

			// アテンション・お知らせ登録情報生成
			ContactOperationsEntryDto contactOperationsEntryDto = new ContactOperationsEntryDto(sosCd, dpUser, contactOperationsType, contactResultType, startTime, endTime, null,
				null);

			// --------------------------
			// アテンション・お知らせ登録
			// --------------------------
			dpsTmsTytenDistExecuteService.entryContactOperations(contactOperationsEntryDto);

			LOG.error("【特約店別計画配分処理 メインサービスで致命的エラー発生：アテンション登録終了】", e);
		}
	}
}
