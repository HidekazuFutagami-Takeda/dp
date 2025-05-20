package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.TmsTytenPlanReferenceDetailDto;
import jp.co.takeda.model.WsPlanSummary2;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.util.ConvertUtil;

/**
 * 特約店別計画参照用データを生成するロジッククラス<br>
 *
 * <pre>
 * コンストラクタで受け取った詳細行には明細のみが格納されているため、
 * 明細を特約店毎に集計して返す。
 * 集計行である場合は、{@link TmsTytenPlanReferenceDetailDto#getIsSummary()}が真を返す設定を行う。
 * </pre>
 *
 * @author tkawabata
 */
public class CreateTmsTytenPlanReferenceLogic {

	/**
	 * 特約店別計画明細行
	 */
	private final List<WsPlanSummary2> detailList;

	/**
	 * コンストラクタ
	 *
	 * @param detailList 特約店別計画明細行
	 */
	public CreateTmsTytenPlanReferenceLogic(List<WsPlanSummary2> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 明細を特約店毎に集計する。
	 *
	 * @return 特約店毎に集計された明細リスト
	 */
	public List<TmsTytenPlanReferenceDetailDto> execute() {

		if (detailList == null) {
			final String errMsg = "特約店別計画明細行がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 特約店コードをキー、明細行リストを値とするマップを生成 + 同時に千円単位の変換を行う
		Map<String, List<TmsTytenPlanReferenceDetailDto>> resultListPerTytenMap = new LinkedHashMap<String, List<TmsTytenPlanReferenceDetailDto>>();
		for (WsPlanSummary2 wsPlanSummary : detailList) {
			String tytenCd = wsPlanSummary.getTmsTytenCd();
			List<TmsTytenPlanReferenceDetailDto> list = resultListPerTytenMap.get(tytenCd);
			if (list == null) {
				list = new ArrayList<TmsTytenPlanReferenceDetailDto>();
				resultListPerTytenMap.put(tytenCd, list);
			}

			// --------------------------
			// 千円単位の変換
			// --------------------------
			// 配分値UH
			Long distValueUh = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getDistValueUh());
			wsPlanSummary.setDistValueUh(distValueUh);

			// 積上値UH
			Long stackValueUh = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getStackValueUh());
			wsPlanSummary.setStackValueUh(stackValueUh);

			// 計画値UH
			Long plannedValueUh = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getPlannedValueUh());
			wsPlanSummary.setPlannedValueUh(plannedValueUh);

			// 配分値P
			Long distValueP = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getDistValueP());
			wsPlanSummary.setDistValueP(distValueP);

			// 積上値P
			Long stackValueP = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getStackValueP());
			wsPlanSummary.setStackValueP(stackValueP);

			// 計画値P
			Long plannedValueP = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getPlannedValueP());
			wsPlanSummary.setPlannedValueP(plannedValueP);

			// 組織コード
			wsPlanSummary.setSosCd(wsPlanSummary.getSosCd());

			// 更新日
			wsPlanSummary.setUpDate(wsPlanSummary.getUpDate());

			list.add(new TmsTytenPlanReferenceDetailDto(wsPlanSummary, false));
		}

		// -----------------------------------
		//  合計行生成処理開始
		// -----------------------------------
		List<TmsTytenPlanReferenceDetailDto> resultList = new ArrayList<TmsTytenPlanReferenceDetailDto>();
		for (String tytenCd : resultListPerTytenMap.keySet()) {
			List<TmsTytenPlanReferenceDetailDto> detailsPerTytenList = resultListPerTytenMap.get(tytenCd);

			// 明細行が続いて、最後に集計行が入るので、まずは明細行を全てリストに追加
			resultList.addAll(detailsPerTytenList);

			// ----------------------------
			// 合計行用の変数
			// ----------------------------
			// TMS特約店コード(NULL固定)
			String tmsTytenCd = null;
			// 価ベース区分(NULL固定)
			KaBaseKb kaBaseKb = null;
			// 配分値UH
			Long distValueUh = null;
			// 積上値UH
			Long stackValueUh = null;
			// 計画値UH
			Long plannedValueUh = null;
			// 配分値P
			Long distValueP = null;
			// 積上値P
			Long stackValueP = null;
			// 計画値P
			Long plannedValueP = null;
			// TMS特約店名称(漢字)(NULL固定)
			String tmsTytenMeiKj = null;
			// 統計品名コード(品目)(NULL固定)
			String statProdCode = null;
			// 品目名称(NULL固定)
			String prodName = null;

			// 集計のためのループ
			for (TmsTytenPlanReferenceDetailDto dto : detailsPerTytenList) {
				WsPlanSummary2 summary = dto.getWsPlanSummary();
				// 配分値UH
				distValueUh = addValue(distValueUh, summary.getDistValueUh());
				// 積上値UH
				stackValueUh = addValue(stackValueUh, summary.getStackValueUh());
				// 計画値UH
				plannedValueUh = addValue(plannedValueUh, summary.getPlannedValueUh());
				// 配分値P
				distValueP = addValue(distValueP, summary.getDistValueP());
				// 積上値P
				stackValueP = addValue(stackValueP, summary.getStackValueP());
				// 計画値P
				plannedValueP = addValue(plannedValueP, summary.getPlannedValueP());
			}
			WsPlanSummary2 summary = new WsPlanSummary2();
			summary.setTmsTytenCd(tmsTytenCd);
			summary.setKaBaseKb(kaBaseKb);
			summary.setDistValueUh(distValueUh);
			summary.setStackValueUh(stackValueUh);
			summary.setPlannedValueUh(plannedValueUh);
			summary.setDistValueP(distValueP);
			summary.setStackValueP(stackValueP);
			summary.setPlannedValueP(plannedValueP);
			summary.setTmsTytenMeiKj(tmsTytenMeiKj);
			summary.setStatProdCode(statProdCode);
			summary.setProdName(prodName);
			// 計画立案対象外フラグ(来期用)を設定する。(vmにて、明細行、合計行にかかわらず、これが立ってれば、無条件に色を変えれるようにする)
			summary.setPlanTaiGaiFlgRik(detailsPerTytenList.get(0).getWsPlanSummary().getPlanTaiGaiFlgRik());

			// 合計行フラグを真に設定して集計行を作成＋結果リストに追加
			resultList.add(new TmsTytenPlanReferenceDetailDto(summary, true));
		}
		return resultList;
	}

	/**
	 * 集計時の追加計算処理を行う。<br>
	 *
	 * <pre>
	 * <li>元の値がnullならば追加する値をそのまま返す。</li>
	 * <li>追加する値がnullならば元の値をそのまま返す。</li>
	 * <li>上記以外は元の値+追加する値を返す。</li>
	 * </pre>
	 *
	 * @param base 元の値
	 * @param value 追加する値
	 * @return 元値 + 追加する値
	 */
	private Long addValue(Long base, Long add) {
		if (base == null) {
			return add;
		}
		if (add == null) {
			return base;
		}
		return base + add;
	}
}
