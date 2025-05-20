package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.logic.div.InsDocStatusForCheck.NOTHING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.dto.InsDocStatusCheckDto;
import jp.co.takeda.dto.InsDocStatusCheckResultDto;
import jp.co.takeda.logic.div.InsDocStatusForCheck;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.StatusForInsDocPlan;

/**
 * 施設医師別計画立案ステータスをチェックするロジッククラス
 * 
 * @author nozaki
 */
public class InsDocPlanStatusCheckBySosLogic {

	/**
	 * 施設医師別計画立案ステータスDAO
	 */
	private final InsDocPlanStatusDao insDocPlanStatusDao;

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
	 * 許可しない施設医師別計画立案ステータスのリスト
	 */
	private final List<InsDocStatusForCheck> insDocStatusForCheckList;

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
	 * @param insDocPlanStatusDao 施設医師別計画立案ステータスDAO
	 * @param unallowableStatusDto 施設医師別計画立案ステータスチェック用DTO
	 */
	public InsDocPlanStatusCheckBySosLogic(InsDocPlanStatusDao insDocPlanStatusDao, InsDocStatusCheckDto insDocStatusCheckDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insDocPlanStatusDao == null) {
			final String errMsg = "施設医師別計画立案ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocStatusCheckDto == null) {
			final String errMsg = "施設医師別計画立案ステータスチェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocStatusCheckDto.getStatusCheckBumon() == null) {
			final String errMsg = "チェック対象の部門ランクがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocStatusCheckDto.getJgiMstList() == null || insDocStatusCheckDto.getJgiMstList().size() == 0) {
			final String errMsg = "チェック対象の従業員のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocStatusCheckDto.getPlannedProdList() == null || insDocStatusCheckDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocStatusCheckDto.getInsDocStatusForCheck() == null || insDocStatusCheckDto.getInsDocStatusForCheck().size() == 0) {
			final String errMsg = "許可しない施設医師別計画立案ステータスのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : insDocStatusCheckDto.getPlannedProdList()) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		for (JgiMst jgiMst : insDocStatusCheckDto.getJgiMstList()) {
			if (jgiMst.getJgiNo() == null) {
				final String errMsg = "チェック対象の従業員の従業員番号がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (jgiMst.getJgiName() == null) {
				final String errMsg = "チェック対象の従業員の氏名がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}

		this.insDocPlanStatusDao = insDocPlanStatusDao;
		this.plannedProdList = insDocStatusCheckDto.getPlannedProdList();
		this.insDocStatusForCheckList = insDocStatusCheckDto.getInsDocStatusForCheck();
		this.sosCd3 = insDocStatusCheckDto.getSosCd3();
		this.sosCd4 = insDocStatusCheckDto.getSosCd4();
		this.jgiMstList = insDocStatusCheckDto.getJgiMstList();

		// 許可しない施設医師別計画立案ステータスに「ステータスなし」がある場合
		if (insDocStatusCheckDto.getInsDocStatusForCheck().contains(NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}
	}

	/**
	 * ステータスチェック実行
	 */
	public InsDocStatusCheckResultDto execute() {

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得した施設医師別計画立案ステータスのリスト
		List<InsDocPlanStatus> insDocPlanStatusList = new ArrayList<InsDocPlanStatus>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 重点品目でない場合はスキップ
			if(BooleanUtils.isNotTrue(plannedProd.getPlanLevelInsDoc())){
				continue;
			}
			
			// 組織コード、品目固定コードを指定して施設医師別計画立案ステータスを取得
			List<InsDocPlanStatus> allInsDocPlanStatusList = new ArrayList<InsDocPlanStatus>();
			try {
				allInsDocPlanStatusList = insDocPlanStatusDao.searchList(null, sosCd3, sosCd4, plannedProd.getProdCode());

			} catch (DataNotFoundException e) {

				if (nothingStatusError) {

					// ステータスなしをエラーとする場合は、エラーメッセージ追加
					messageKeyList.add(new MessageKey("DPS3108E", "対象全担当者", plannedProd.getProdName(), "配分前"));
					continue;
				}
			}

			Collections.sort(allInsDocPlanStatusList, InsDocPlanStatusComparator.getInstance());

			// 担当者ごとにチェック
			for (JgiMst jgiMst : jgiMstList) {

				// 検索対象の条件
				InsDocPlanStatus targetStatus = new InsDocPlanStatus();
				targetStatus.setProdCode(plannedProd.getProdCode());
				targetStatus.setJgiNo(jgiMst.getJgiNo());

				// 取得した施設医師別計画立案ステータスから検索
				int index = Collections.binarySearch(allInsDocPlanStatusList, targetStatus, InsDocPlanStatusComparator.getInstance());

				// 指定した品目、担当者のステータスがない場合
				if (index < 0) {

					if (nothingStatusError) {

						// ステータスなしをエラーとする場合は、エラーメッセージ追加
						messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "配分前"));

					} else {

						// エラーにしない場合は、新規作成したステータスをリストに追加
						insDocPlanStatusList.add(targetStatus);
					}
					continue;
				}

				// 指定した品目、担当者のステータスをリストに追加
				insDocPlanStatusList.add(allInsDocPlanStatusList.get(index));

				// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
				StatusForInsDocPlan resultStatus = allInsDocPlanStatusList.get(index).getStatusForInsDocPlan();
				if (insDocStatusForCheckList.contains(InsDocStatusForCheck.getInstance(resultStatus))) {

					switch (resultStatus) {
						case DISTING:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設医師別計画の配分中"));
							break;
						case DISTED:
							messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設医師別計画が配分済"));
							break;
						case PLAN_OPENED:
							messageKeyList.add(new MessageKey("DPS3109E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設医師別計画"));
							break;
						case PLAN_COMPLETED:
							messageKeyList.add(new MessageKey("DPS3110E", jgiMst.getJgiName(), plannedProd.getProdName(), "施設医師別計画"));
							break;
					}
				}
			}
		}

		if (messageKeyList.size() > 0) {
			return new InsDocStatusCheckResultDto(messageKeyList, insDocPlanStatusList);

		} else {
			return new InsDocStatusCheckResultDto(insDocPlanStatusList);
		}
	}
}
