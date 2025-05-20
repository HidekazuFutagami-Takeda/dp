package jp.co.takeda.logic;

//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import static jp.co.takeda.a.exp.ErrMessageKey.*;
//import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.logic.div.InsWsStatusForCheck.*;
//import static jp.co.takeda.logic.div.InsWsStatusForCheck.NOTHING;
//mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.InsWsPlanStatusDao;
import jp.co.takeda.dto.InsWsStatusCheckDto;
import jp.co.takeda.dto.InsWsStatusCheckResultDto;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.logic.div.InsWsStatusForCheck;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsKakuteiErrMsg;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.StatusForInsWsPlan;

/**
 * 施設特約店別計画立案ステータスをチェックするロジッククラス
 *
 * @author nozaki
 */
public class InsWsPlanStatusCheckBySosLogic {

	/**
	 * 施設特約店別計画立案ステータスDAO
	 */
	private final InsWsPlanStatusDao insWsPlanStatusDao;

	/**
	 * チェック対象の従業員情報のリスト
	 */
	private final List<JgiMst> jgiMstList;

	/**
	 * チェック対象の組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * チェック対象の組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しない施設特約店別計画立案ステータスのリスト
	 */
	private final List<InsWsStatusForCheck> insWsStatusForCheckList;

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
	 * @param insWsPlanStatusDao 施設特約店別計画立案ステータスDAO
	 * @param unallowableStatusDto 施設特約店別計画立案ステータスチェック用DTO
	 */
	public InsWsPlanStatusCheckBySosLogic(InsWsPlanStatusDao insWsPlanStatusDao, InsWsStatusCheckDto insWsStatusCheckDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insWsPlanStatusDao == null) {
			final String errMsg = "施設特約店別計画立案ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckDto == null) {
			final String errMsg = "施設特約店別計画立案ステータスチェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckDto.getStatusCheckBumon() == null) {
			final String errMsg = "チェック対象の部門ランクがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckDto.getJgiMstList() == null || insWsStatusCheckDto.getJgiMstList().size() == 0) {
			final String errMsg = "チェック対象の従業員のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckDto.getPlannedProdList() == null || insWsStatusCheckDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckDto.getInsWsStatusForCheck() == null || insWsStatusCheckDto.getInsWsStatusForCheck().size() == 0) {
			final String errMsg = "許可しない施設特約店別計画立案ステータスのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : insWsStatusCheckDto.getPlannedProdList()) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		for (JgiMst jgiMst : insWsStatusCheckDto.getJgiMstList()) {
			if (jgiMst.getJgiNo() == null) {
				final String errMsg = "チェック対象の従業員の従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (jgiMst.getJgiName() == null) {
				final String errMsg = "チェック対象の従業員の氏名がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		this.insWsPlanStatusDao = insWsPlanStatusDao;
		this.plannedProdList = insWsStatusCheckDto.getPlannedProdList();
		this.insWsStatusForCheckList = insWsStatusCheckDto.getInsWsStatusForCheck();
		this.sosCd3 = insWsStatusCheckDto.getSosCd3();
		this.sosCd4 = insWsStatusCheckDto.getSosCd4();
		this.jgiMstList = insWsStatusCheckDto.getJgiMstList();

		// 許可しない施設特約店別計画立案ステータスに「ステータスなし」がある場合
		if (insWsStatusCheckDto.getInsWsStatusForCheck().contains(NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}
	}

	/**
	 * ステータスチェック実行
	 */
	public InsWsStatusCheckResultDto execute() {

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得した施設特約店別計画立案ステータスのリスト
		List<InsWsPlanStatus> insWsPlanStatusList = new ArrayList<InsWsPlanStatus>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 組織コード、品目固定コードを指定して施設特約店別計画立案ステータスを取得
			List<InsWsPlanStatus> allInsWsPlanStatusList = new ArrayList<InsWsPlanStatus>();
			try {
				allInsWsPlanStatusList = insWsPlanStatusDao.searchList(null, sosCd3, sosCd4, plannedProd.getProdCode());

			} catch (DataNotFoundException e) {

				if (nothingStatusError) {

					// ステータスなしをエラーとする場合は、エラーメッセージ追加
					messageKeyList.add(new MessageKey("DPS3108E", "対象全担当者", plannedProd.getProdName(), "配分前"));
					continue;
				}
			}

			Collections.sort(allInsWsPlanStatusList, InsWsPlanStatusComparator.getInstance());

			// 担当者ごとにチェック
			for (JgiMst jgiMst : jgiMstList) {

				// 検索対象の条件
				InsWsPlanStatus targetStatus = new InsWsPlanStatus();
				targetStatus.setProdCode(plannedProd.getProdCode());
				targetStatus.setJgiNo(jgiMst.getJgiNo());

				// 取得した施設特約店別計画立案ステータスから検索
				int index = Collections.binarySearch(allInsWsPlanStatusList, targetStatus, InsWsPlanStatusComparator.getInstance());

				// 指定した品目、担当者のステータスがない場合
				if (index < 0) {

					if (nothingStatusError) {

						// ステータスなしをエラーとする場合は、エラーメッセージ追加
						messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "配分前"));

					} else {

						// エラーにしない場合は、新規作成したステータスをリストに追加
						insWsPlanStatusList.add(targetStatus);
					}
					continue;
				}

				// 指定した品目、担当者のステータスをリストに追加
				insWsPlanStatusList.add(allInsWsPlanStatusList.get(index));

				// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
				StatusForInsWsPlan resultStatus = allInsWsPlanStatusList.get(index).getStatusForInsWsPlan();
				if (insWsStatusForCheckList.contains(InsWsStatusForCheck.getInstance(resultStatus))) {

					switch (resultStatus) {
						case DISTING:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画の配分中"));
							break;
						case DISTED:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画が配分済"));
							break;
						case PLAN_OPENED:
							messageKeyList.add(new MessageKey("DPS3109E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画"));
							break;
						case PLAN_COMPLETED:
							messageKeyList.add(new MessageKey("DPS3110E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画"));
							break;
					}
				}
			}
		}

		if (messageKeyList.size() > 0) {
//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
//			return new InsWsStatusCheckResultDto(messageKeyList, insWsPlanStatusList);
			return new InsWsStatusCheckResultDto(messageKeyList, insWsPlanStatusList, null);
//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

		} else {
			return new InsWsStatusCheckResultDto(insWsPlanStatusList);
		}
	}


//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	/**
	 * 一括確定ステータスチェック実行
	 */
	public InsWsStatusCheckResultDto executeKakutei() {

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		//一括確定エラー情報を登録する為のリスト
		List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList = new ArrayList<DpsKakuteiErrMsg>();

		// DBから取得した施設特約店別計画立案ステータスのリスト
		List<InsWsPlanStatus> insWsPlanStatusList = new ArrayList<InsWsPlanStatus>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 組織コード、品目固定コードを指定して施設特約店別計画立案ステータスを取得
			List<InsWsPlanStatus> allInsWsPlanStatusList = new ArrayList<InsWsPlanStatus>();
			try {
				allInsWsPlanStatusList = insWsPlanStatusDao.searchList(null, sosCd3, sosCd4, plannedProd.getProdCode());

			} catch (DataNotFoundException e) {

				if (nothingStatusError) {

					// ステータスなしをエラーとする場合は、エラーメッセージ追加
					messageKeyList.add(new MessageKey("DPS3108E", "対象全担当者", plannedProd.getProdName(), "配分前"));
					continue;
				}
			}

			Collections.sort(allInsWsPlanStatusList, InsWsPlanStatusComparator.getInstance());

			// 担当者ごとにチェック
			for (JgiMst jgiMst : jgiMstList) {

				// 検索対象の条件
				InsWsPlanStatus targetStatus = new InsWsPlanStatus();
				targetStatus.setProdCode(plannedProd.getProdCode());
				targetStatus.setJgiNo(jgiMst.getJgiNo());

				// 取得した施設特約店別計画立案ステータスから検索
				int index = Collections.binarySearch(allInsWsPlanStatusList, targetStatus, InsWsPlanStatusComparator.getInstance());

				// 指定した品目、担当者のステータスがない場合
				if (index < 0) {

					if (nothingStatusError) {

						// ステータスなしをエラーとする場合は、エラーメッセージ追加
						messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "配分前"));
						dpsKakuteiErrMsgList.add(makeErrDate(sosCd3, jgiMst.getJgiNo(), plannedProd.getProdCode(),new MessageKey("DPS3335E")));
					} else {

						// エラーにしない場合は、新規作成したステータスをリストに追加
						insWsPlanStatusList.add(targetStatus);
					}
					continue;
				}

				// 指定した品目、担当者のステータスをリストに追加
				insWsPlanStatusList.add(allInsWsPlanStatusList.get(index));

				// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
				StatusForInsWsPlan resultStatus = allInsWsPlanStatusList.get(index).getStatusForInsWsPlan();
				if (insWsStatusForCheckList.contains(InsWsStatusForCheck.getInstance(resultStatus))) {

					switch (resultStatus) {
						case DISTING:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画の配分中"));
							dpsKakuteiErrMsgList.add(makeErrDate(sosCd3, jgiMst.getJgiNo(), plannedProd.getProdCode(),new MessageKey("DPS3335E")));
							break;
						case DISTED:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画が配分済"));
							dpsKakuteiErrMsgList.add(makeErrDate(sosCd3, jgiMst.getJgiNo(), plannedProd.getProdCode(),new MessageKey("DPS3335E")));
							break;
						case PLAN_OPENED:
							messageKeyList.add(new MessageKey("DPS3109E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画"));
							dpsKakuteiErrMsgList.add(makeErrDate(sosCd3, jgiMst.getJgiNo(), plannedProd.getProdCode(),new MessageKey("DPS3335E")));
							break;
						case PLAN_COMPLETED:
							messageKeyList.add(new MessageKey("DPS3110E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設特約店別計画"));
							dpsKakuteiErrMsgList.add(makeErrDate(sosCd3, jgiMst.getJgiNo(), plannedProd.getProdCode(),new MessageKey("DPS3335E")));
							break;
					}
				}
			}
		}

		if (messageKeyList.size() > 0) {
			return new InsWsStatusCheckResultDto(messageKeyList, insWsPlanStatusList, dpsKakuteiErrMsgList);

		} else {
			return new InsWsStatusCheckResultDto(insWsPlanStatusList);
		}
	}

	//一括確定エラー情報テーブルに登録する為のエラー情報を作成する
	private DpsKakuteiErrMsg makeErrDate(String sosCd, Integer jgiNo, String prodCode, MessageKey messageKey) {
		DpsKakuteiErrMsg dpsKakuteiErrMsg = new DpsKakuteiErrMsg();
		dpsKakuteiErrMsg.setSosCd(sosCd);
		dpsKakuteiErrMsg.setProdCode(prodCode);
		dpsKakuteiErrMsg.setIsJgiNo(jgiNo);
		dpsKakuteiErrMsg.setMessageKey(messageKey);
		return dpsKakuteiErrMsg;
	}
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
}
