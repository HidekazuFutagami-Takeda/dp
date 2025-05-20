package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.WsPlanStatusDao;
import jp.co.takeda.dto.WsStatusCheckDto;
import jp.co.takeda.dto.WsStatusCheckResultDto;
import jp.co.takeda.logic.div.WsDistStatusForCheck;
import jp.co.takeda.logic.div.WsSlideStatusForCheck;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.WsPlanStatus;
import jp.co.takeda.model.div.StatusForWs;
import jp.co.takeda.model.div.StatusForWsSlide;

/**
 * 特約店別計画立案ステータスをチェックするロジック
 *
 * @author nozaki
 */
public class WsPlanStatusCheckByAllLogic {

	/**
	 * 特約店別計画立案ステータスDAO
	 */
	private final WsPlanStatusDao wsPlanStatusDao;

	/**
	 * チェック対象の支店組織情報のリスト
	 */
	private final List<SosMst> sosMstList;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * チェック対象のカテゴリ
	 */
	private final String prodCategory;
//	private final ProdCategory prodCategory;

	/**
	 * 許可しない特約店別計画立案配分ステータスのリスト
	 */
	private final List<WsDistStatusForCheck> wsDistStatusForCheck;

	/**
	 * 許可しない特約店別計画立案スライドステータスのリスト
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
	 * @param wsPlanStatusDao 特約店別計画立案ステータスDAO
	 * @param wsStatusCheckDto 特約店別計画立案ステータスチェック用DTO
	 */
	public WsPlanStatusCheckByAllLogic(WsPlanStatusDao wsPlanStatusDao, WsStatusCheckDto wsStatusCheckDto) {

		if (wsPlanStatusDao == null) {
			final String errMsg = "特約店別計画立案ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (wsStatusCheckDto == null) {
			final String errMsg = "特約店別計画立案ステータスチェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (wsStatusCheckDto.getSosMstList() == null) {
			final String errMsg = "チェック対象の支店組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (SosMst sosMst : wsStatusCheckDto.getSosMstList()) {
			if (sosMst.getSosCd() == null) {
				final String errMsg = "チェック対象の支店の組織コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (sosMst.getBumonSeiName() == null) {
				final String errMsg = "チェック対象の支店の部門名正式がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		if (wsStatusCheckDto.getPlannedProdList() == null || wsStatusCheckDto.getPlannedProdList().size() == 0) {
			final String errMsg = "チェック対象の計画対象品目のリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		for (PlannedProd plannedProd : wsStatusCheckDto.getPlannedProdList()) {
			if (plannedProd.getProdCode() == null) {
				final String errMsg = "チェック対象の品目の品目固定コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if (plannedProd.getProdName() == null) {
				final String errMsg = "チェック対象の品目の品目名称がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
		}
		this.wsPlanStatusDao = wsPlanStatusDao;
		this.sosMstList = wsStatusCheckDto.getSosMstList();
		this.plannedProdList = wsStatusCheckDto.getPlannedProdList();
		this.prodCategory = wsStatusCheckDto.getProdCategory();

		if (wsStatusCheckDto.getWsDistStatusForCheck() == null) {
			this.wsDistStatusForCheck = new ArrayList<WsDistStatusForCheck>();
		} else {
			this.wsDistStatusForCheck = wsStatusCheckDto.getWsDistStatusForCheck();
		}
		if (wsStatusCheckDto.getWsSlideStatusForCheck() == null) {
			this.wsSlideStatusForCheck = new ArrayList<WsSlideStatusForCheck>();
		} else {
			this.wsSlideStatusForCheck = wsStatusCheckDto.getWsSlideStatusForCheck();
		}

		// 許可しない特約店別計画立案ステータスに「ステータスなし」がある場合
		if (wsDistStatusForCheck.contains(WsDistStatusForCheck.NOTHING) || wsSlideStatusForCheck.contains(WsSlideStatusForCheck.NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}
	}

	/**
	 * ステータスチェック実行
	 */
	public WsStatusCheckResultDto execute() {

		// エラーメッセージキーのリスト
		ArrayList<MessageKey> messageKeyList = new ArrayList<MessageKey>();

		// DBから取得した特約店別計画立案ステータスのリスト
		List<WsPlanStatus> wsPlanStatusList = new ArrayList<WsPlanStatus>();

		// カテゴリを指定して特約店別計画立案ステータスを取得
		List<WsPlanStatus> allWsPlanStatusList = new ArrayList<WsPlanStatus>();
		try {
			allWsPlanStatusList = wsPlanStatusDao.searchList(null, prodCategory);

		} catch (DataNotFoundException e) {

			// ステータスなしをエラーとする場合は、エラーメッセージ追加
			if (nothingStatusError) {
				messageKeyList.add(new MessageKey("DPS3108E", "全支店", "対象全品目", "配分前"));
				return new WsStatusCheckResultDto(messageKeyList, wsPlanStatusList);
			}
		}

		Collections.sort(allWsPlanStatusList, WsPlanStatusComparator.getInstance());

		// 支店ごとにチェック
		for (SosMst sosMst : sosMstList) {

			// 品目ごとにチェック
			for (PlannedProd plannedProd : plannedProdList) {

				// 検索対象の条件
				WsPlanStatus targetStatus = new WsPlanStatus();
				targetStatus.setSosCd(sosMst.getSosCd());
				targetStatus.setProdCode(plannedProd.getProdCode());

				// 取得した特約店別計画立案ステータスから検索
				int index = Collections.binarySearch(allWsPlanStatusList, targetStatus, WsPlanStatusComparator.getInstance());

				// 指定した支店、担当者のステータスがない場合
				if (index < 0) {

					if (nothingStatusError) {

						// ステータスなしをエラーとする場合は、エラーメッセージ追加
						messageKeyList.add(new MessageKey("DPS3108E", sosMst.getSosName(), plannedProd.getProdName(), "配分前"));

					} else {

						// エラーにしない場合は、新規作成したステータスをリストに追加
						wsPlanStatusList.add(targetStatus);
					}
					continue;
				}

				wsPlanStatusList.add(allWsPlanStatusList.get(index));

				// 取得した配分ステータスが許可しない配分ステータスの場合は、エラーメッセージ追加
				StatusForWs resultDistStatus = allWsPlanStatusList.get(index).getStatusDistForWs();
				if (wsDistStatusForCheck.contains(WsDistStatusForCheck.getInstance(resultDistStatus))) {
					switch (resultDistStatus) {
						case NONE:
							messageKeyList.add(new MessageKey("DPS3108E", sosMst.getBumonSeiName(), plannedProd.getProdName(), "配分前"));
							break;
						case DISTING:
							messageKeyList.add(new MessageKey("DPS3108E", sosMst.getBumonSeiName(), plannedProd.getProdName(), "配分中"));
							break;
						case DISTED:
							messageKeyList.add(new MessageKey("DPS3108E", sosMst.getBumonSeiName(), plannedProd.getProdName(), "配分済"));
							break;
					}
				}
				// 取得した配分ステータスが許可しないスライドステータスの場合は、エラーメッセージ追加
				StatusForWsSlide resultSlideStatus = allWsPlanStatusList.get(index).getStatusSlideForWs();
				if (wsSlideStatusForCheck.contains(WsSlideStatusForCheck.getInstance(resultSlideStatus))) {
					switch (resultSlideStatus) {
						case NONE:
							messageKeyList.add(new MessageKey("DPS3108E", sosMst.getBumonSeiName(), plannedProd.getProdName(), "スライド前"));
							break;
						case SLIDING:
							messageKeyList.add(new MessageKey("DPS3108E", sosMst.getBumonSeiName(), plannedProd.getProdName(), "スライド中"));
							break;
						case SLIDED:
							messageKeyList.add(new MessageKey("DPS3108E", sosMst.getBumonSeiName(), plannedProd.getProdName(), "スライド済"));
							break;
					}
				}
			}
		}

		if (messageKeyList.size() > 0) {
			return new WsStatusCheckResultDto(messageKeyList, wsPlanStatusList);

		} else {
			return new WsStatusCheckResultDto(wsPlanStatusList);
		}

	}

}
