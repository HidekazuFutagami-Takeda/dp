package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.takeda.dto.DistributionLogicDto;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsWsPlanDto;
import jp.co.takeda.logic.div.InsWsDistType;
import jp.co.takeda.logic.div.InsWsPlanType;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DistParam;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.view.DistributionElement;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 配分ロジッククラス<br>
 *
 * <pre>
 * 増分値配分を行なうクラス。
 * ただし、UP/DOWN判定の結果がDOWNの場合は、構成比配分を行なう。
 * </pre>
 *
 * @see DistributionByRateLogic
 * @author nozaki
 */
public class DistributionByIncrementLogic extends DistributionBaseLogic {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(DistributionByIncrementLogic.class);

	/**
	 * 配分要素のリスト
	 */
	private final List<DistributionElement> elementList;

	/**
	 * 配分パラメータ
	 */
	private final DistParam distParam;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * 施設出力対象区分
	 */
	private final InsType insType;

	/**
	 * 配分母数
	 */
	private final Long paramValueBase;

	/**
	 * 特定施設個別計画サマリー
	 */
	private final Long specialSum;

	/**
	 * 配分用計画値（配分母数 - 特定施設個別計画サマリー）
	 */
	private final Long distValue;

	/**
	 * 対象品目の配分要素リスト
	 */
	private final List<DistributionElement> prodElementList;

	/**
	 * コンストラクタ
	 *
	 * @param elementList 配分要素のリスト
	 * @param baseParam 試算/配分の共通パラメータ
	 * @param distParam 配分パラメータ
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param insType 施設出力対象区分
	 * @param paramValue 配分母数
	 * @param specialSum 特定施設個別計画サマリー
	 * @param distType 施設特約店別計画の配分種類
	 * @param prodElementList 計画対象品目の配分要素リスト
	 */
	public DistributionByIncrementLogic(List<DistributionElement> elementList, BaseParam baseParam, DistParam distParam, String sosCd, Integer jgiNo, String prodCode,
		InsType insType, Long paramValue, Long specialSum, InsWsDistType distType, List<DistributionElement> prodElementList) {
		super(baseParam, distType);
		this.elementList = elementList;
		this.distParam = distParam;
		this.sosCd = sosCd;
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insType = insType;
		this.paramValueBase = paramValue;
		this.specialSum = specialSum;
		this.prodElementList = prodElementList;
		// ------------------------------
		// 配分母数の保存
		// ------------------------------
		// 配分母数 = 営業所計画 - 特定施設個別計画(担当者立案)集計
		this.distValue = MathUtil.sub(this.paramValueBase, this.specialSum, true);
	}

	/**
	 * 配分を実行する。
	 *
	 * @return 配分ロジッククラスの結果格納DTO
	 */
	public DistributionLogicDto execute() {

		// 配分ミス情報
		DistributionMissDto distMissDto = null;

		// --------------------------------------
		// 納入実績・特定施設個別計画の集計値生成
		// --------------------------------------

		// 過去実績合計(配分品目)
		Long kakoJissekiSum = null;
		// 重点先過去実績合計(配分品目)
		Long kakoJissekiJtnSum = null;
		// 一般先過去実績合計(配分品目)
		Long kakoJissekiIppanSum = null;
		// 削除施設リスト
		List<DistributionElement> delInsList = new ArrayList<DistributionElement>();
		// 配分除外施設リスト
		List<DistributionElement> exceptList = new ArrayList<DistributionElement>();
		// 特定施設個別計画施設リスト
		List<DistributionElement> specialList = new ArrayList<DistributionElement>();
		// 重点先施設リスト
		List<DistributionElement> jtnList = new ArrayList<DistributionElement>();
		// 一般先先施設リスト
		List<DistributionElement> ippanList = new ArrayList<DistributionElement>();
		// 配分対象リスト
		List<DistributionElement> distList = new ArrayList<DistributionElement>();

		// 配分品目の納入実績合計を算出
		for (DistributionElement element : elementList) {

			// 参照期間内で集計した値がマイナスになる場合は０に置き換える
			Long sumResult = element.getSumResult();
			if (sumResult != null) {
				if (sumResult < 0) {
					sumResult = 0L;
				}
				element.setSumResult(sumResult);
			}

// mod start 2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応
//			if (distType == InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getDelInsFlg()) {
//				// (ワ)施設特約店配分 かつ 削除施設 は、配分対象外
//				delInsList.add(element);

//			if (   (distType == InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getDelInsFlg())
//				|| (distType != InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getInsType() == InsType.P && element.getDelInsFlg())
//				|| (distType != InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getInsType() == InsType.P && StringUtils.strip(element.getInsFormalName(), "　 ").startsWith("●"))
//			) {
				//　削除施設の配分除外
				//　　・ワクチンのDelInsFlg=1　※改訂前の仕様はこれのみ
				//　　・医薬のP施設のDelInsFlg=1
				//　　・医薬のP施設の施設名に'●'が付与されたもの
				//delInsList.add(element);

// mod end   2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応
			if (element.getExceptDistInsFlg()) {
				// 配分除外施設 は、配分対象外
				exceptList.add(element);

			} else if (element.getSpecialInsPlanFlg()) {
				// 特定施設個別計画施設 は、配分対象外
				specialList.add(element);

			} else {
				if (element.getWeightingFlg()) {
					// 重点先施設
					jtnList.add(element);
					// 重点先実績合計
					kakoJissekiJtnSum = MathUtil.add(kakoJissekiJtnSum, element.getSumResult());
				} else {
					// 一般先施設
					ippanList.add(element);
					// 一般先実績合計
					kakoJissekiIppanSum = MathUtil.add(kakoJissekiIppanSum, element.getSumResult());
				}
				// 配分対象
				distList.add(element);
			}
		}

		// 配分品目全実績合計
		kakoJissekiSum = MathUtil.add(kakoJissekiJtnSum, kakoJissekiIppanSum);

		if (LOG.isDebugEnabled()) {
			LOG.debug("【組織】sosCd：" + sosCd);
			LOG.debug("【従業員】jgiNo：" + jgiNo);
			LOG.debug("【対象】insType：" + insType);
			LOG.debug("【品目】prodCode：" + prodCode);
			LOG.debug("【配分母数】paramValue：" + paramValueBase);
			LOG.debug("【特定施設】specialSum：" + specialSum);
			LOG.debug("【配分母数 - 特定施設】distValue：" + distValue);
			LOG.debug("【過去実績合計】kakoJissekiSum：" + kakoJissekiSum);
			LOG.debug("【重点先過去実績合計】kakoJissekiJtnSum：" + kakoJissekiJtnSum);
		}

		// ------------------------------
		// 施設特約店別計画の生成
		// ------------------------------

		List<InsWsPlanDto> insWsPlanList = new ArrayList<InsWsPlanDto>();

		// (1)	配分基準に設定されているゼロ配分フラグを取得
		if (distParam.getZeroDistFlg()) {

			// (2)	ゼロ配分フラグが真(「ゼロ配分を実行する」に設定されている)の場合、ゼロ配分モードで配分する
			if (LOG.isDebugEnabled()) {
				LOG.debug("ゼロ配分処理実行");
			}
			createInsWsPlan(insWsPlanList, prodCode, distList, InsWsPlanType.ZERO, 0L, 0L, null);

		} else {

			// (3)	ゼロ配分フラグが偽(「ゼロ配分を実行しない」に設定されている)の場合、UP/DOWN判定を行なう
			if (LOG.isDebugEnabled()) {
				LOG.debug("通常配分処理実行");
			}

			// -------------------------------------
			// UP/DOWN判定
			// -------------------------------------

			// 対象品目の納入実績合計を算出し、ベースとなる納入実績をMAPに格納する。
			Map<DistKey, DistributionElement> planProdMap = new HashMap<DistKey, DistributionElement>();
			Long convertkakoJissekiSum = null;
			for (DistributionElement element : prodElementList) {

				// 参照期間内で集計した値がマイナスになる場合は０に置き換える
				Long sumResult = element.getSumResult();
				if (sumResult != null) {
					if (sumResult < 0) {
						sumResult = 0L;
					}
					element.setSumResult(sumResult);
				}

// mod start 2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応
//				if (distType == InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getDelInsFlg()) {
//					// (ワ)施設特約店配分 かつ 削除施設 は、配分対象外
//				if (   (distType == InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getDelInsFlg())
//					|| (distType != InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getInsType() == InsType.P && element.getDelInsFlg())
//					|| (distType != InsWsDistType.MR_PLAN_DIST_FOR_VAC && element.getInsType() == InsType.P && StringUtils.strip(element.getInsFormalName(), "　 ").startsWith("●"))
//				) {
					//　削除施設は配分除外
					//　　・ワクチンのDelInsFlg=1　※改訂前の仕様はこれのみ
					//　　・医薬のP施設のDelInsFlg=1
					//　　・医薬のP施設の施設名に'●'が付与されたもの
// mod end   2019/01/30 S.Hayashi B19-0013_DPX_削除施設配分除外対応
				if (element.getExceptDistInsFlg()) {
					// 配分除外施設 は、配分対象外
				} else if (element.getSpecialInsPlanFlg()) {
					// 特定施設個別計画施設 は、配分対象外
				} else {
					// 実績合計
					convertkakoJissekiSum = MathUtil.add(convertkakoJissekiSum, element.getSumResult());
					// 配分対象
					planProdMap.put(new DistKey(element.getInsNo(), element.getTmsTytenCd()), element);
				}
			}

			// (4)	計画値増分値を以下の式で算出する

			// 対象品目の納入実績合計を6ヶ月分(1期)分にする
			convertkakoJissekiSum = ConvertUtil.parseTermValue(convertkakoJissekiSum, baseParam.getRefFrom(), baseParam.getRefTo());

			// 計画増分値 ＝ 配分母数 － 1期あたりの納入実績
			Long paramIncValue = MathUtil.sub(distValue, convertkakoJissekiSum, true);

			// (5)	以下の場合にUPと判定する。
			// 【計画増分値 != NULL】かつ【計画増分値 > 0】かつ【重点先施設集計 != NULL】かつ【重点先施設集計 > 0】
			boolean upFlg = false;
			if (paramIncValue != null && kakoJissekiJtnSum != null && paramIncValue > 0 && kakoJissekiJtnSum > 0) {
				upFlg = true;
			}

			Long distValueSum = 0L;
			if (upFlg) {

				// -------------------------------------
				// UP時の配分結果の算出
				// -------------------------------------

				// (1)(2)(3)	重点先施設への配分処理を行なう･･･①
				Long jtnValueSum = createInsWsPlan(insWsPlanList, prodCode, jtnList, InsWsPlanType.JTN, paramIncValue, kakoJissekiJtnSum, planProdMap);

				// (4)(5)	一般先施設への配分値を算出する･･･②
				Long ippanValueSum = createInsWsPlan(insWsPlanList, prodCode, ippanList, InsWsPlanType.IPPAN, null, null, planProdMap);

				// (6)(7)	納入実績(配分品目)にはなく、納入実績(計画対象品目)には実績がある施設特約店に対し、配分値を算出する･･･③
				Long planProdSum = createInsWsPlan(insWsPlanList, planProdMap);

				// (8)	配分値を合計する(① + ② + ③)
				distValueSum = MathUtil.add(jtnValueSum, ippanValueSum);
				distValueSum = MathUtil.add(distValueSum, planProdSum);

				// (9)	丸めによる誤差をもっとも理論値が大きい配分結果に足しこむ
				if (kakoJissekiSum != null && kakoJissekiSum > 0) {

					// 誤差 = 配分母数 - 配分値合計
					Long diffValue = MathUtil.sub(distValue, distValueSum);
					if (LOG.isDebugEnabled()) {
						LOG.debug("【丸め誤差】diffValue：" + diffValue);
					}

					// もっとも値が大きい配分値(Y) = もっとも値が大きい配分値(Y) + 誤差
					if (diffValue != null && distList.size() != 0 && diffValue != 0) {

						Collections.sort(insWsPlanList, InsWsPlanDistValueComparator.getInstance());

						InsWsPlanDto plan = insWsPlanList.get(0);
						Long updateValue = MathUtil.add(plan.getDistValue(), diffValue);
						InsWsPlanDto updatePlan = new InsWsPlanDto(plan, updateValue);
						insWsPlanList.set(0, updatePlan);
					}
				}

				// (11)	上位計画と計画値との差額を算出する
				Long allPlannedValue = 0L;
				for (InsWsPlanDto insWsPlanDto : insWsPlanList) {

					// 配分値が0より大きい場合は計画値合計に加算する
					Long insWsDistValue = insWsPlanDto.getDistValue();
					if (insWsDistValue != null && insWsDistValue > 0) {
						allPlannedValue = MathUtil.add(allPlannedValue, insWsDistValue);
					}
				}

				// 差額 ＝ 担当者別計画 - (特定施設個別計画集計 + 計画値集計)
				Long totalDiffValue = MathUtil.sub(paramValueBase, MathUtil.add(specialSum, allPlannedValue));

				// (12)	差額が0でない場合は配分ミスとする。
				if (totalDiffValue == null || totalDiffValue != 0) {
					distMissDto = new DistributionMissDto(sosCd, jgiNo, prodCode, insType, paramValueBase, totalDiffValue, "DPS2015W");
				}

			} else {

				// -------------------------------------
				// DOWN時の配分結果の算出
				// -------------------------------------

				// DOWNの場合、構成比率配分処理を実行
				DistributionByRateLogic distributionByRateLogic = new DistributionByRateLogic(elementList, distParam, sosCd, jgiNo, prodCode, insType, paramValueBase, specialSum,
					distType);
				return distributionByRateLogic.execute();
			}
		}

		// ------------------------------
		// 配分除外施設登録
		// ------------------------------
		createInsWsPlan(insWsPlanList, prodCode, exceptList, InsWsPlanType.EXCEPT, 0L, 0L, null);

		// ------------------------------
		// 削除施設登録
		// ------------------------------
		createInsWsPlan(insWsPlanList, prodCode, delInsList, InsWsPlanType.DELETE, 0L, 0L, null);

		return new DistributionLogicDto(distMissDto, insWsPlanList);
	}

	/**
	 * 施設特約店別計画の登録情報を生成する。
	 *
	 * @param insWsPlan 登録対象の施設特約店別計画
	 * @param planProdMap 対象品目の過去実績リスト
	 * @return 配分値合計
	 */
	protected Long createInsWsPlan(List<InsWsPlanDto> insWsPlanList, Map<DistKey, DistributionElement> planProdMap) {
		// 配分値合計
		Long distValueSum = 0L;
		// 施設特約店別計画の登録
		for (DistributionElement element : planProdMap.values()) {
			// 6ヶ月換算した実績値を取得
			Long value = ConvertUtil.parseTermValue(element.getSumResult(), baseParam.getRefFrom(), baseParam.getRefTo());

			// 万単位に丸める
			value = calcTheory(value);
			distValueSum = MathUtil.add(distValueSum, value);
			InsWsPlanDto insWsPlan = new InsWsPlanDto(element.getJgiNo(), prodCode, element.getInsNo(), element.getTmsTytenCd(), value, false, false, false);
			insWsPlanList.add(insWsPlan);
		}
		return distValueSum;
	}
}
