package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.logic.div.MrStatusForCheck.*;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.MrPlanStatusDao;
import jp.co.takeda.dto.MrStatusCheckDto;
import jp.co.takeda.dto.MrStatusCheckResultDto;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 担当者別計画立案ステータスをチェックするロジッククラス
 *
 * @author nozaki
 */
public class MrPlanStatusCheckLogic {

	/**
	 * 担当者別計画立案ステータスDAO
	 */
	private final MrPlanStatusDao mrPlanStatusDao;

	/**
	 * チェック対象の組織コード(営業所)
	 */
	private String sosCd;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しない担当者別計画立案ステータスのリスト
	 */
	private final List<MrStatusForCheck> mrStatusForCheckList;

	/**
	 * ステータスなしエラーフラグ
	 * <ul>
	 * <li>true：ステータスなしをエラーとする</li>
	 * <li>false：ステータスなしエラーとしない</li>
	 * <ul>
	 */
	private final boolean nothingStatusError;

	/**
	 * コンストラクタ
	 *
	 * @param mrPlanStatusDao 担当者別計画立案ステータスDAO
	 * @param unallowableStatusDto 担当者別計画立案ステータスチェック用DTO
	 */
	public MrPlanStatusCheckLogic(MrPlanStatusDao mrPlanStatusDao, MrStatusCheckDto mrStatusCheckDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (mrPlanStatusDao == null) {
			final String errMsg = "担当者別計画立案ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrStatusCheckDto == null) {
			final String errMsg = "担当者別計画立案ステータスチェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (mrStatusCheckDto.getSosCd() == null) {
//			final String errMsg = "チェック対象の組織コード(営業所)がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (mrStatusCheckDto.getPlannedProdList() == null || mrStatusCheckDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrStatusCheckDto.getMrStatusForCheck() == null || mrStatusCheckDto.getMrStatusForCheck().size() == 0) {
			final String errMsg = "許可しない担当者別計画立案ステータスのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : mrStatusCheckDto.getPlannedProdList()) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		this.mrPlanStatusDao = mrPlanStatusDao;
		this.sosCd = mrStatusCheckDto.getSosCd();
		this.plannedProdList = mrStatusCheckDto.getPlannedProdList();
		this.mrStatusForCheckList = mrStatusCheckDto.getMrStatusForCheck();

		// 許可しない担当者別計画立案ステータスに「ステータスなし」がある場合
		if (mrStatusCheckDto.getMrStatusForCheck().contains(NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}
	}

	/**
	 * ステータスチェック実行
	 */
	public MrStatusCheckResultDto execute() {

		//add Start 2023/6/19  Y.Taniguchi バックログNo.35 編集可能権限の追加
		//ユーザ情報を取得する。
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		//従業員を取得する。
		DpUser settingUser = userInfo.getSettingUser();
		//add END 2023/6/19  Y.Taniguchi バックログNo.35 編集可能権限の追加

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得した担当者別計画立案ステータスのリスト
		List<MrPlanStatus> mrPlanStatusList = new ArrayList<MrPlanStatus>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 組織コード、品目固定コードを指定してステータスを取得
			MrPlanStatus resultMrPlanStatus = null;
			try {
//				resultMrPlanStatus = mrPlanStatusDao.search(sosCd, plannedProd.getProdCode());
//				mrPlanStatusList.add(resultMrPlanStatus);
				mrPlanStatusList = mrPlanStatusDao.searchSosCdProdList(sosCd, plannedProd.getProdCode());

			} catch (DataNotFoundException e) {

				if (nothingStatusError) {

					// ステータスなしをエラーとする場合は、エラーメッセージ追加
					messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "試算前"));

				} else {

					// エラーにしない場合は、新規作成してリストに追加
					resultMrPlanStatus = new MrPlanStatus();
					resultMrPlanStatus.setSosCd(sosCd);
					resultMrPlanStatus.setProdCode(plannedProd.getProdCode());
					mrPlanStatusList.add(resultMrPlanStatus);
				}
				continue;
			}

			// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
			for (MrPlanStatus resultMrPlanStatusRow : mrPlanStatusList) {
				StatusForMrPlan resultStatus = resultMrPlanStatusRow.getStatusForMrPlan();
				if (mrStatusForCheckList.contains(MrStatusForCheck.getInstance(resultStatus))) {

					switch (resultStatus) {
						case ESTIMATING:
							messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "試算中"));
							break;
						case ESTIMATED:
							messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "試算済"));
							break;
						case TEAM_PLAN_OPENED:
							messageKeyList.add(new MessageKey("DPS3105E", plannedProd.getProdName(), "チーム別計画"));
							break;
						case TEAM_PLAN_COMPLETED:
							//add Start 2023/6/19  Y.Taniguchi バックログNo.35 編集可能権限の追加
							//本部：JKN0255,本部（ワクチン）：JKN0256,全支店長：JKN0011,全支店S：JKN0013はエラー対象外とする。
							if(!settingUser.isMatch(JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G,  JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
							//add END 2023/6/19  Y.Taniguchi バックログNo.35 編集可能権限の追加
	// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　エラーメッセージ変更
//							messageKeyList.add(new MessageKey("DPS3106E", plannedProd.getProdName(), "チーム別計画"));
							messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "担当者別計画が公開前"));
	// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　エラーメッセージ変更
							//add Start 2023/6/19  Y.Taniguchi バックログNo.35 編集可能権限の追加
							}
							//add END 2023/6/19  Y.Taniguchi バックログNo.35 編集可能権限の追加
							break;
						case MR_PLAN_OPENED:
							messageKeyList.add(new MessageKey("DPS3105E", plannedProd.getProdName(), "担当者別計画"));
							break;
						case MR_PLAN_INPUT_FINISHED:
							messageKeyList.add(new MessageKey("DPS3107E", plannedProd.getProdName(), "ALによる担当者別計画入力"));
							break;
						case MR_PLAN_COMPLETED:
							messageKeyList.add(new MessageKey("DPS3106E", plannedProd.getProdName(), "担当者別計画"));
							break;
					}
				}
			}

		}

		if (messageKeyList.size() > 0) {
			return new MrStatusCheckResultDto(messageKeyList, mrPlanStatusList);

		} else {
			return new MrStatusCheckResultDto(mrPlanStatusList);
		}
	}
}
