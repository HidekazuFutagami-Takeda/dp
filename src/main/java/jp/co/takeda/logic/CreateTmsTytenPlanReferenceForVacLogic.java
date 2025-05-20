package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.TmsTytenPlanReferenceDetailDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceForVacDetailDto;
import jp.co.takeda.model.WsPlanForVacSummary2;
import jp.co.takeda.model.div.KaBaseKbForVac;
import jp.co.takeda.util.ConvertUtil;

/**
 * ワクチン特約店別計画参照用データを生成するロジッククラス<br>
 *
 * <pre>
 * コンストラクタで受け取った詳細行には明細のみが格納されているため、
 * 明細を特約店毎に集計して返す。
 * 集計行である場合は、{@link TmsTytenPlanReferenceDetailDto#getIsSummary()}が真を返す設定を行う。
 * </pre>
 *
 * @author tkawabata
 */
public class CreateTmsTytenPlanReferenceForVacLogic {

	/**
	 * 特約店別計画明細行
	 */
	private final List<WsPlanForVacSummary2> detailList;

	/**
	 * コンストラクタ
	 *
	 * @param detailList 特約店別計画明細行
	 */
	public CreateTmsTytenPlanReferenceForVacLogic(List<WsPlanForVacSummary2> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 明細を特約店毎に集計する。
	 *
	 * @return 特約店毎に集計された明細リスト
	 */
	public List<TmsTytenPlanReferenceForVacDetailDto> execute() {

		if (detailList == null) {
			final String errMsg = "特約店別計画明細行がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 特約店コードをキー、明細行リストを値とするマップを生成 + 同時に千円単位の変換を行う
		Map<String, List<TmsTytenPlanReferenceForVacDetailDto>> resultListPerTytenMap = new LinkedHashMap<String, List<TmsTytenPlanReferenceForVacDetailDto>>();
		for (WsPlanForVacSummary2 wsPlanSummary : detailList) {
			String tytenCd = wsPlanSummary.getTmsTytenCd();
			List<TmsTytenPlanReferenceForVacDetailDto> list = resultListPerTytenMap.get(tytenCd);
			if (list == null) {
				list = new ArrayList<TmsTytenPlanReferenceForVacDetailDto>();
				resultListPerTytenMap.put(tytenCd, list);
			}

			// --------------------------
			// 千円単位の変換
			// --------------------------
			// 計画値
			Long plannedValue = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getPlannedValue());
			wsPlanSummary.setPlannedValue(plannedValue);

			// 積上値P
			Long insStackValue = ConvertUtil.parseMoneyToThousandUnit(wsPlanSummary.getInsStackValue());
			wsPlanSummary.setInsStackValue(insStackValue);

			list.add(new TmsTytenPlanReferenceForVacDetailDto(wsPlanSummary, false, false));
		}

		// -----------------------------------
		//  合計行生成処理開始
		// -----------------------------------
		// 合計行用の変数
		// TMS特約店コード(NULL固定)
		String tmsTytenCd = null;
		// 価ベース区分(NULL固定)
		KaBaseKbForVac kaBaseKb = null;
		// TMS特約店名称(漢字)(NULL固定)
		String tmsTytenMeiKj = null;
		// 統計品名コード(品目)(NULL固定)
		String statProdCode = null;
		// 品目名称(NULL固定)
		String prodName = null;
		// 全合計計画値
		Long plannedValueAll = null;
		// 全合計積上値
		Long insStackValueAll = null;

		List<TmsTytenPlanReferenceForVacDetailDto> resultList = new ArrayList<TmsTytenPlanReferenceForVacDetailDto>();
		for (String tytenCd : resultListPerTytenMap.keySet()) {
			List<TmsTytenPlanReferenceForVacDetailDto> detailsPerTytenList = resultListPerTytenMap.get(tytenCd);

			// 明細行が続いて、最後に集計行が入るので、まずは明細行を全てリストに追加
			resultList.addAll(detailsPerTytenList);

			// 計画値UH
			Long plannedValue = null;
			// 積上値UH
			Long insStackValue = null;

			// 集計のためのループ
			for (TmsTytenPlanReferenceForVacDetailDto dto : detailsPerTytenList) {
				WsPlanForVacSummary2 summary = dto.getWsPlanForVacSummary();
				// 計画値
				plannedValue = addValue(plannedValue, summary.getPlannedValue());
				// 積上値
				insStackValue = addValue(insStackValue, summary.getInsStackValue());
			}
			WsPlanForVacSummary2 summary = new WsPlanForVacSummary2();
			summary.setTmsTytenCd(tmsTytenCd);
			summary.setKaBaseKb(kaBaseKb);
			summary.setPlannedValue(plannedValue);
			summary.setInsStackValue(insStackValue);
			summary.setTmsTytenMeiKj(tmsTytenMeiKj);
			summary.setStatProdCode(statProdCode);
			summary.setProdName(prodName);
			// 計画立案対象外フラグ(来期用)を設定する。(vmにて、明細行、合計行にかかわらず、これが立ってれば、無条件に色を変えれるようにする)
			summary.setPlanTaiGaiFlgRik(detailsPerTytenList.get(0).getWsPlanForVacSummary().getPlanTaiGaiFlgRik());

			// 合計行フラグを真に設定して集計行を作成＋結果リストに追加
			resultList.add(new TmsTytenPlanReferenceForVacDetailDto(summary, true, false));

			// 全合計に加算
			// 計画値
			plannedValueAll = addValue(plannedValueAll, plannedValue);
			// 積上値
			insStackValueAll = addValue(insStackValueAll, insStackValue);
		}
		// 全合計生成
		WsPlanForVacSummary2 summary = new WsPlanForVacSummary2();
		summary.setTmsTytenCd(tmsTytenCd);
		summary.setKaBaseKb(kaBaseKb);
		summary.setPlannedValue(plannedValueAll);
		summary.setInsStackValue(insStackValueAll);
		summary.setTmsTytenMeiKj(tmsTytenMeiKj);
		summary.setStatProdCode(statProdCode);
		summary.setProdName(prodName);

		// 全合計行を結果リストの先頭に追加
		resultList.add(0, new TmsTytenPlanReferenceForVacDetailDto(summary, false, true));
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
