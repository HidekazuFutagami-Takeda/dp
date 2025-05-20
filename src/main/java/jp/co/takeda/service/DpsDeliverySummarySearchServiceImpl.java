package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DeliveryResultMrDao;
import jp.co.takeda.dao.ProdSummaryDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.DeliveryResultSosSummaryDto;
import jp.co.takeda.dto.DeliveryResultSummaryDetailDto;
import jp.co.takeda.dto.DeliveryResultSummaryDto;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.ProdSummary;
import jp.co.takeda.util.MathUtil;

/**
 * 過去実績参照(担当者別計画モード)の検索に関するサービスクラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsDeliverySummarySearchService")
public class DpsDeliverySummarySearchServiceImpl implements DpsDeliverySummarySearchService {

	/**
	 * 簡易版品目を取得するためのDAO
	 */
	@Autowired(required = true)
	@Qualifier("prodSummaryDao")
	protected ProdSummaryDao prodSummaryDao;

	/**
	 * 担当者別納入実績にアクセスするDAO
	 */
	@Autowired(required = true)
	@Qualifier("deliveryResultMrDao")
	DeliveryResultMrDao deliveryResultMrDao;

	/**
	 * 組織DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	// 担当者別の期集約サマリー情報を取得
	public DeliveryResultSummaryDto searchDeliveryResultSummaryDto(InsType insType, String prodCode, String sosCd3, String sosCd4, String category) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isBlank(prodCode)) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(sosCd3) && StringUtils.isBlank(sosCd4)) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (StringUtils.isBlank(category)) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// --------------------------------
		// 簡易版品目情報
		// --------------------------------
		ProdSummary prodSummary = prodSummaryDao.search(prodCode);

		// --------------------------------
		// 営業所サマリ情報
		// --------------------------------
		DeliveryResultSummaryDetailDto officeSummary = null;
		if (StringUtils.isNotBlank(sosCd3) && StringUtils.isBlank(sosCd4)) {
			DeliveryResultSosSummaryDto officeDto = deliveryResultMrDao.searchSosSummary(prodCode, sosCd3, false, insType, category);
			officeSummary = createDeliveryResultSummaryDetailDto(officeDto.getMonNnu());
		}

		// --------------------------------
		// チームサマリマップ情報
		// --------------------------------
		DeliveryResultSummaryDetailDto allTeamSummary = null;
		Map<String, DeliveryResultSummaryDetailDto> teamSummaryMap = null;
		if (StringUtils.isNotBlank(sosCd4)) {
			DeliveryResultSosSummaryDto teamDto = deliveryResultMrDao.searchTeamSummary(prodCode, sosCd4, insType, category);
			allTeamSummary = createDeliveryResultSummaryDetailDto(teamDto.getMonNnu());
			teamSummaryMap = new HashMap<String, DeliveryResultSummaryDetailDto>();
			teamSummaryMap.put(sosCd4, allTeamSummary);
		} else if (StringUtils.isNotBlank(sosCd3)) {
			List<SosMst> teamSosList = null;
			try {
				teamSosList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, SosListType.SHITEN_LIST, BumonRank.OFFICE_TOKUYAKUTEN_G, sosCd3);
			} catch (DataNotFoundException e) {
				// チームがない場合は、営業所組織
				teamSosList = new ArrayList<SosMst>();
				teamSosList.add(sosMstDAO.search(sosCd3));
			}
			if (teamSosList != null && teamSosList.size() > 0) {
				teamSummaryMap = new HashMap<String, DeliveryResultSummaryDetailDto>();
				for (SosMst sos : teamSosList) {

					DeliveryResultSosSummaryDto teamDto = null;
					try {

						if(sos.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){
							// 組織が営業所・ONCエリアの場合
							teamDto = deliveryResultMrDao.searchSosSummary(prodCode, sosCd3, false, insType, category);
						} else {
							// 組織がチームの場合
							teamDto = deliveryResultMrDao.searchTeamSummary(prodCode, sos.getSosCd(), insType, category);
						}

					} catch (DataNotFoundException e) {
						teamDto = new DeliveryResultSosSummaryDto(null, null, null, null);
					}
					teamSummaryMap.put(teamDto.getSosCde(), createDeliveryResultSummaryDetailDto(teamDto.getMonNnu()));
				}

				// チーム合計を作成
				Long preFarAdvancePeriod = null;
				Long farAdvancePeriod = null;
				Long advancePeriod = null;
				Long currentPeriod = null;
				Long currentPlanValue = null;
				for (DeliveryResultSummaryDetailDto dto : teamSummaryMap.values()) {
					preFarAdvancePeriod = MathUtil.add(preFarAdvancePeriod, dto.getPreFarAdvancePeriod());
					farAdvancePeriod = MathUtil.add(farAdvancePeriod, dto.getFarAdvancePeriod());
					advancePeriod = MathUtil.add(advancePeriod, dto.getAdvancePeriod());
					currentPeriod = MathUtil.add(currentPeriod, dto.getCurrentPeriod());
					currentPlanValue = MathUtil.add(currentPlanValue, dto.getCurrentPlanValue());
				}
				allTeamSummary = new DeliveryResultSummaryDetailDto(preFarAdvancePeriod, farAdvancePeriod, advancePeriod, currentPeriod, currentPlanValue);
			}
		}

		// --------------------------------
		// ＭＲサマリマップ情報
		// --------------------------------
		Map<Integer, DeliveryResultSummaryDetailDto> mrSummaryMap = new HashMap<Integer, DeliveryResultSummaryDetailDto>();
		List<DeliveryResultMr> jList = null;
		try {
			jList = deliveryResultMrDao.searchByProdNonZatu(DeliveryResultMrDao.SORT_STRING, prodCode, sosCd3, sosCd4, insType, category);
		} catch (DataNotFoundException e) {
			// 何もしない
		}
		if (jList != null && jList.size() > 0) {
			mrSummaryMap = new HashMap<Integer, DeliveryResultSummaryDetailDto>();
			for (DeliveryResultMr jgiDto : jList) {

				Integer jgiNo = jgiDto.getJgiNo();
				DeliveryResultSummaryDetailDto dto = mrSummaryMap.get(jgiNo);
				if (dto == null) {
					mrSummaryMap.put(jgiNo, createDeliveryResultSummaryDetailDto(jgiDto.getMonNnu()));
				} else {
					mrSummaryMap.put(jgiNo, addDeliveryResultSummaryDetailDto(dto, jgiDto.getMonNnu()));
				}
			}
		}
		// 結果の生成とリターン
		return new DeliveryResultSummaryDto(prodSummary, officeSummary, teamSummaryMap, allTeamSummary, mrSummaryMap);
	}

	/**
	 * 納入実績の組織単位のサマリの明細を表すＤＴＯを生成する。
	 *
	 * @param m 納入実績
	 * @return 納入実績の組織単位のサマリの明細を表すＤＴＯ
	 */
	private DeliveryResultSummaryDetailDto createDeliveryResultSummaryDetailDto(MonNnu m) {

		if (m == null) {
			return new DeliveryResultSummaryDetailDto(null, null, null, null, null);
		}
		return new DeliveryResultSummaryDetailDto(m.getPreFarAdvancePeriod(), m.getFarAdvancePeriod(), m.getAdvancePeriod(), m.getCurrentPeriod(), m.getCurrentPlanValue());
	}

	/**
	 * 納入実績の組織単位のサマリの明細を表すＤＴＯに実績を加算する。
	 *
	 * @param d 納入実績の組織単位のサマリの明細を表すＤＴＯ
	 * @param m 納入実績
	 * @return 加算した納入実績の組織単位のサマリの明細を表すＤＴＯ
	 */
	private DeliveryResultSummaryDetailDto addDeliveryResultSummaryDetailDto(DeliveryResultSummaryDetailDto d, MonNnu m) {

		Long preFarAdvancePeriod = d.getPreFarAdvancePeriod();
		Long farAdvancePeriod = d.getFarAdvancePeriod();
		Long advancePeriod = d.getAdvancePeriod();
		Long currentPeriod = d.getCurrentPeriod();
		Long currentPlanValue = d.getCurrentPlanValue();

		Long preFarAdvancePeriod_ = MathUtil.add(preFarAdvancePeriod, m.getPreFarAdvancePeriod());
		Long farAdvancePeriod_ = MathUtil.add(farAdvancePeriod, m.getFarAdvancePeriod());
		Long advancePeriod_ = MathUtil.add(advancePeriod, m.getAdvancePeriod());
		Long currentPeriod_ = MathUtil.add(currentPeriod, m.getCurrentPeriod());
		Long currentPlanValue_ = MathUtil.add(currentPlanValue, m.getCurrentPlanValue());

		return new DeliveryResultSummaryDetailDto(preFarAdvancePeriod_, farAdvancePeriod_, advancePeriod_, currentPeriod_, currentPlanValue_);

	}

}
