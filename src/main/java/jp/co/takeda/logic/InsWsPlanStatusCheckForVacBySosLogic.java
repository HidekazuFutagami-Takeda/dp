package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.logic.div.InsWsStatusForCheck.NOTHING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.InsWsPlanStatusForVacDao;
import jp.co.takeda.dto.InsWsStatusCheckForVacDto;
import jp.co.takeda.dto.InsWsStatusCheckResultForVacDto;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.StatusForInsWsPlan;

/**
 * ワクチン用施設特約店別計画立案ステータスをチェックするロジッククラス
 * 
 * @author nozaki
 */
public class InsWsPlanStatusCheckForVacBySosLogic {

	/**
	 * ワクチン用施設特約店別計画立案ステータスDAO
	 */
	private final InsWsPlanStatusForVacDao insWsPlanStatusForVacDao;

	/**
	 * チェック対象の従業員情報のリスト
	 */
	private final List<JgiMst> jgiMstList;

	/**
	 * チェック対象の組織コード(特約店G)
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
	 * 許可しないワクチン用施設特約店別計画立案ステータスのリスト
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
	 * @param insWsPlanStatusForVacDao ワクチン用施設特約店別計画立案ステータスDAO
	 * @param unallowableStatusDto ワクチン用施設特約店別計画立案ステータスチェック用DTO
	 */
	public InsWsPlanStatusCheckForVacBySosLogic(InsWsPlanStatusForVacDao insWsPlanStatusForVacDao, InsWsStatusCheckForVacDto insWsStatusCheckForVacDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insWsPlanStatusForVacDao == null) {
			final String errMsg = "ワクチン用施設特約店別計画立案ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckForVacDto == null) {
			final String errMsg = "ワクチン用施設特約店別計画立案ステータスチェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckForVacDto.getStatusCheckBumon() == null) {
			final String errMsg = "チェック対象の部門ランクがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckForVacDto.getJgiMstList() == null || insWsStatusCheckForVacDto.getJgiMstList().size() == 0) {
			final String errMsg = "チェック対象の従業員のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckForVacDto.getPlannedProdList() == null || insWsStatusCheckForVacDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsStatusCheckForVacDto.getInsWsStatusForCheck() == null || insWsStatusCheckForVacDto.getInsWsStatusForCheck().size() == 0) {
			final String errMsg = "許可しないワクチン用施設特約店別計画立案ステータスのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : insWsStatusCheckForVacDto.getPlannedProdList()) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		for (JgiMst jgiMst : insWsStatusCheckForVacDto.getJgiMstList()) {
			if (jgiMst.getJgiNo() == null) {
				final String errMsg = "チェック対象の従業員の従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (jgiMst.getJgiName() == null) {
				final String errMsg = "チェック対象の従業員の氏名がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		this.insWsPlanStatusForVacDao = insWsPlanStatusForVacDao;
		this.plannedProdList = insWsStatusCheckForVacDto.getPlannedProdList();
		this.insWsStatusForCheckList = insWsStatusCheckForVacDto.getInsWsStatusForCheck();
		this.sosCd3 = insWsStatusCheckForVacDto.getSosCd3();
		this.sosCd4 = insWsStatusCheckForVacDto.getSosCd4();
		this.jgiMstList = insWsStatusCheckForVacDto.getJgiMstList();

		// 許可しないワクチン用施設特約店別計画立案ステータスに「ステータスなし」がある場合
		if (insWsStatusCheckForVacDto.getInsWsStatusForCheck().contains(NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}
	}

	/**
	 * ステータスチェック実行
	 */
	public InsWsStatusCheckResultForVacDto execute() {

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得したワクチン用施設特約店別計画立案ステータスのリスト
		List<InsWsPlanStatusForVac> insWsPlanStatusForVacList = new ArrayList<InsWsPlanStatusForVac>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 組織コード、品目固定コードを指定してワクチン用施設特約店別計画立案ステータスを取得
			List<InsWsPlanStatusForVac> allInsWsPlanStatusForVacList = new ArrayList<InsWsPlanStatusForVac>();
			try {
				allInsWsPlanStatusForVacList = insWsPlanStatusForVacDao.searchList(null, sosCd3, sosCd4, plannedProd.getProdCode());

			} catch (DataNotFoundException e) {

				if (nothingStatusError) {

					// ステータスなしをエラーとする場合は、エラーメッセージ追加
					messageKeyList.add(new MessageKey("DPS3108E", "対象全担当者", plannedProd.getProdName(), "配分前"));
					continue;
				}
			}

			Collections.sort(allInsWsPlanStatusForVacList, InsWsPlanStatusForVacComparator.getInstance());

			// 担当者ごとにチェック
			for (JgiMst jgiMst : jgiMstList) {

				// 検索対象の条件
				InsWsPlanStatusForVac targetStatus = new InsWsPlanStatusForVac();
				targetStatus.setProdCode(plannedProd.getProdCode());
				targetStatus.setJgiNo(jgiMst.getJgiNo());

				// 取得したワクチン用施設特約店別計画立案ステータスから検索
				int index = Collections.binarySearch(allInsWsPlanStatusForVacList, targetStatus, InsWsPlanStatusForVacComparator.getInstance());

				// 指定した品目、担当者のステータスがない場合
				if (index < 0) {
					if (nothingStatusError) {

						// ステータスなしをエラーとする場合は、エラーメッセージ追加
						messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "配分前"));

					} else {

						// エラーにしない場合は、新規作成したステータスをリストに追加
						insWsPlanStatusForVacList.add(targetStatus);
					}
					continue;
				}

				// 指定した品目、担当者のステータスをリストに追加
				insWsPlanStatusForVacList.add(allInsWsPlanStatusForVacList.get(index));

				// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
				StatusForInsWsPlan resultStatus = allInsWsPlanStatusForVacList.get(index).getStatusForInsWsPlan();
				if (insWsStatusForCheckList.contains(InsWsStatusForCheck.getInstance(resultStatus))) {

					switch (resultStatus) {
						case DISTING:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "配分中"));
							break;
						case DISTED:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "配分済"));
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
			return new InsWsStatusCheckResultForVacDto(messageKeyList, insWsPlanStatusForVacList);

		} else {
			return new InsWsStatusCheckResultForVacDto(insWsPlanStatusForVacList);
		}

	}

}
