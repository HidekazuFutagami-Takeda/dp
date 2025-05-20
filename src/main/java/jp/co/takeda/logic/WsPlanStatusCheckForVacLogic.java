package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.WsPlanStatusForVacDao;
import jp.co.takeda.dto.WsStatusCheckForVacDto;
import jp.co.takeda.dto.WsStatusCheckResultForVacDto;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.StatusForWsSlide;

/**
 * ワクチン用特約店別計画立案ステータスをチェックするロジック
 * 
 * @author nozaki
 */
public class WsPlanStatusCheckForVacLogic {

	/**
	 * ワクチン用特約店別計画立案ステータスDAO
	 */
	private final WsPlanStatusForVacDao wsPlanStatusForVacDao;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しないワクチン用特約店別計画立案スライドステータスのリスト
	 */
	private final List<WsSlideStatusForCheck> wsSlideStatusForCheck;

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
	 * @param wsPlanStatusForVacDao ワクチン用特約店別計画立案ステータスDAO
	 * @param wsStatusCheckForVacDto ワクチン用特約店別計画立案ステータスチェック用DTO
	 */
	public WsPlanStatusCheckForVacLogic(WsPlanStatusForVacDao wsPlanStatusForVacDao, WsStatusCheckForVacDto wsStatusCheckForVacDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (wsPlanStatusForVacDao == null) {
			final String errMsg = "ワクチン用特約店別計画立案ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (wsStatusCheckForVacDto == null) {
			final String errMsg = "ワクチン用特約店別計画立案ステータスチェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (wsStatusCheckForVacDto.getPlannedProdList() == null || wsStatusCheckForVacDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (wsStatusCheckForVacDto.getWsSlideStatusForCheck() == null || wsStatusCheckForVacDto.getWsSlideStatusForCheck().size() == 0) {
			final String errMsg = "許可しないワクチン用特約店別計画立案スライドステータスのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : wsStatusCheckForVacDto.getPlannedProdList()) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		this.wsPlanStatusForVacDao = wsPlanStatusForVacDao;
		this.plannedProdList = wsStatusCheckForVacDto.getPlannedProdList();
		this.wsSlideStatusForCheck = wsStatusCheckForVacDto.getWsSlideStatusForCheck();

		// 許可しないワクチン用特約店別計画立案ステータスに「ステータスなし」がある場合
		if (wsSlideStatusForCheck.contains(WsSlideStatusForCheck.NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}
	}

	/**
	 * ステータスチェック実行
	 */
	public WsStatusCheckResultForVacDto execute() {

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得したワクチン用特約店別計画立案ステータスのリスト
		List<WsPlanStatusForVac> wsPlanStatusForVacList = new ArrayList<WsPlanStatusForVac>();

		// 品目ごとにチェック
		for (PlannedProd plannedProd : plannedProdList) {

			// 担当者コード、品目固定コードを指定してステータスを取得
			WsPlanStatusForVac resultWsPlanStatusForVac = null;
			try {
				resultWsPlanStatusForVac = wsPlanStatusForVacDao.search(plannedProd.getProdCode());
				wsPlanStatusForVacList.add(resultWsPlanStatusForVac);

			} catch (DataNotFoundException e) {

				if (nothingStatusError) {

					// ステータスなしをエラーとする場合は、エラーメッセージ追加
					messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "スライド前"));

				} else {

					// エラーにしない場合は、新規作成してリストに追加
					resultWsPlanStatusForVac = new WsPlanStatusForVac();
					resultWsPlanStatusForVac.setProdCode(plannedProd.getProdCode());
					wsPlanStatusForVacList.add(resultWsPlanStatusForVac);
				}
				continue;
			}

			// 取得したスライドステータスが許可しないスライドステータスの場合は、エラーメッセージ追加
			StatusForWsSlide resultSlideStatus = resultWsPlanStatusForVac.getStatusSlideForWs();
			if (wsSlideStatusForCheck.contains(WsSlideStatusForCheck.getInstance(resultSlideStatus))) {
				switch (resultSlideStatus) {
					case NONE:
						messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "スライド前"));
						break;
					case SLIDING:
						messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "スライド中"));
						break;
					case SLIDED:
						messageKeyList.add(new MessageKey("DPS3104E", plannedProd.getProdName(), "スライド済"));
						break;
				}
			}
		}

		if (messageKeyList.size() > 0) {
			return new WsStatusCheckResultForVacDto(messageKeyList, wsPlanStatusForVacList);

		} else {
			return new WsStatusCheckResultForVacDto(wsPlanStatusForVacList);
		}
	}
}
