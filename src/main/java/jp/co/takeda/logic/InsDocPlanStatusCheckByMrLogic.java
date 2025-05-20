package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import static jp.co.takeda.logic.div.InsDocStatusForCheck.NOTHING;

import java.util.ArrayList;
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
public class InsDocPlanStatusCheckByMrLogic {

	/**
	 * 施設医師別計画立案ステータスDAO
	 */
	private final InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * チェック対象の従業員情報のリスト
	 */
	private final List<JgiMst> jgiMstList;

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
	public InsDocPlanStatusCheckByMrLogic(InsDocPlanStatusDao insDocPlanStatusDao, InsDocStatusCheckDto insDocStatusCheckDto) {

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
		if (insDocStatusCheckDto.getPlannedProdList() == null || insDocStatusCheckDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocStatusCheckDto.getInsDocStatusForCheck() == null || insDocStatusCheckDto.getInsDocStatusForCheck().size() == 0) {
			final String errMsg = "許可しない施設医師別計画立案ステータスのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insDocStatusCheckDto.getJgiMstList() == null || insDocStatusCheckDto.getJgiMstList().size() == 0) {
			final String errMsg = "チェック対象の担従業員情報のリストがnull、またはサイズ0";
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
		List<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得した施設医師別計画立案ステータスのリスト
		List<InsDocPlanStatus> insDocPlanStatusList = new ArrayList<InsDocPlanStatus>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 重点品目でない場合はスキップ
			if(BooleanUtils.isNotTrue(plannedProd.getPlanLevelInsDoc())){
				continue;
			}

			// 担当者ごとにチェック
			for (JgiMst jgiMst : jgiMstList) {

				// 担当者コード、品目固定コードを指定してステータスを取得
				InsDocPlanStatus resultInsDocPlanStatus = null;
				try {
					resultInsDocPlanStatus = insDocPlanStatusDao.search(jgiMst.getJgiNo(), plannedProd.getProdCode());
					insDocPlanStatusList.add(resultInsDocPlanStatus);

				} catch (DataNotFoundException e) {

					if (nothingStatusError) {

						// ステータスなしをエラーとする場合は、エラーメッセージ追加
						messageKeyList.add(new MessageKey("DPS3108E", jgiMst.getJgiName(), plannedProd.getProdName(), "配分前"));

					} else {

						// エラーにしない場合は、新規作成してリストに追加
						resultInsDocPlanStatus = new InsDocPlanStatus();
						resultInsDocPlanStatus.setJgiNo(jgiMst.getJgiNo());
						resultInsDocPlanStatus.setProdCode(plannedProd.getProdCode());
						insDocPlanStatusList.add(resultInsDocPlanStatus);
					}
					continue;
				}

				// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
				StatusForInsDocPlan resultStatus = resultInsDocPlanStatus.getStatusForInsDocPlan();
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
