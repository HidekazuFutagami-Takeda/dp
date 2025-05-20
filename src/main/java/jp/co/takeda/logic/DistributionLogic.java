package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.bean.Scale;
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 【09改訂前】配分ロジッククラス<br>
 *
 * <pre>
 * 09下期改訂により、配分ロジックを変更。
 * 当クラスをベースに、別途クラス定義を行ったため、当クラスは、非推奨とする。
 * </pre>
 *
 * @author khashimoto
 * @deprecated
 */
@Deprecated
public class DistributionLogic {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(DistributionLogic.class);

	/**
	 * 配分要素のリスト
	 */
	private final List<DistributionElement> elementList;

	/**
	 * 試算/配分の共通パラメータ
	 */
	private final BaseParam baseParam;

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
	 * 施設特約店別計画の配分種類
	 */
	private final InsWsDistType distType;

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
	 * @param prodElementList 対象品目の配分要素リスト
	 */
	public DistributionLogic(List<DistributionElement> elementList, BaseParam baseParam, DistParam distParam, String sosCd, Integer jgiNo, String prodCode, InsType insType,
		Long paramValue, Long specialSum, InsWsDistType distType, List<DistributionElement> prodElementList) {
		this.elementList = elementList;
		this.baseParam = baseParam;
		this.distParam = distParam;
		this.sosCd = sosCd;
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.insType = insType;
		this.paramValueBase = paramValue;
		this.specialSum = specialSum;
		// ------------------------------
		// 配分母数の保存
		// ------------------------------
		// 配分母数から、特定施設個別計画を引く
		this.distValue = MathUtil.sub(this.paramValueBase, this.specialSum, true);
		this.distType = distType;
		this.prodElementList = prodElementList;
	}

	/**
	 * 配分を実行する。
	 *
	 * @return 配分ロジッククラスの結果格納DTO
	 */
	public DistributionLogicDto execute() {

		// 配分ミス情報
		DistributionMissDto distMissDto = null;

		// ゼロ配分フラグ
		boolean zeroFlg = distParam.getZeroDistFlg();

		// --------------------------------------
		// 納入実績・特定施設個別計画の集計値生成
		// --------------------------------------
		Long kakoJissekiSum = null;
		Long kakoJissekiJtnSum = null;
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
		for (DistributionElement element : elementList) {
			Long sumResult = element.getSumResult();
			// 実績値の変換
			if (sumResult != null) {
				// 実績値がマイナスの場合、0とする
				if (sumResult < 0) {
					sumResult = 0L;
				}
				element.setSumResult(sumResult);
			}
			// 施設の属性で振り分ける
			if (element.getDelInsFlg()) {
				// 削除施設
				delInsList.add(element);
			} else if (element.getExceptDistInsFlg()) {
				// 配分除外施設
				exceptList.add(element);
			} else if (element.getSpecialInsPlanFlg()) {
				// 特定施設個別計画施設
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
		// 全実績合計
		kakoJissekiSum = MathUtil.add(kakoJissekiJtnSum, kakoJissekiIppanSum);

		// ------------------------------
		// 施設特約店別計画の生成
		// ------------------------------
		List<InsWsPlanDto> insWsPlanList = new ArrayList<InsWsPlanDto>();
		if (kakoJissekiSum == null) {
			// 納入実績が無の場合、通常配分を実施しない
			// 配分ミス生成

			// コンパイルエラー回避のためコメントアウト
			//			distMissDto = new DistributionMissDto(sosCd, jgiNo, prodCode, insType, paramValueBase, "DPS2013W");
		} else if (kakoJissekiSum <= 0 || zeroFlg) {
			// 納入実績が0以下の場合、またはゼロ配分の場合、ゼロ配分を実施
			createInsWsPlan(insWsPlanList, prodCode, distList, InsWsPlanType.ZERO);
			// 配分ミス生成(納入実績が0以下の場合)
			if (kakoJissekiSum <= 0) {

				// コンパイルエラー回避のためコメントアウト
				//				distMissDto = new DistributionMissDto(sosCd, jgiNo, prodCode, insType, paramValueBase, "DPS2014W");
			}
		} else {
			// 通常配分の実施
			executeDist(jtnList, ippanList, insWsPlanList, kakoJissekiSum, kakoJissekiJtnSum);
		}

		// ------------------------------
		// 配分除外施設登録
		// ------------------------------
		createInsWsPlan(insWsPlanList, prodCode, exceptList, InsWsPlanType.EXCEPT);

		// ------------------------------
		// 削除施設登録
		// ------------------------------
		createInsWsPlan(insWsPlanList, prodCode, delInsList, InsWsPlanType.DELETE);

		// 配分ミス除外
		// 配分用計画値（配分母数 - 特定施設個別計画サマリー）がnullまたは、0以下の場合配分ミスとしない。
		if (distValue == null || distValue <= 0) {
			distMissDto = null;
		}

		return new DistributionLogicDto(distMissDto, insWsPlanList);

	}

	/**
	 * 通常の配分更新を実施する。
	 *
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param insType 施設出力対象区分
	 * @param distType 配分種類
	 * @param jtnList 重点先施設のリスト
	 * @param ippanList 一般先施設のリスト
	 * @param insWsPlanList 登録対象の施設特約店別計画リスト
	 * @param kakoJissekiSum 実績値合計
	 * @param kakoJissekiJtnSum 重点先実績値合計
	 * @param specialSum 特定施設個別計画の合計
	 * @param prodCode 品目固定コード
	 * @param paramValue 配分母数
	 */
	protected void executeDist(List<DistributionElement> jtnList, List<DistributionElement> ippanList, List<InsWsPlanDto> insWsPlanList, Long kakoJissekiSum, Long kakoJissekiJtnSum) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("【組織】sosCd：" + sosCd);
			LOG.debug("【従業員】jgiNo：" + jgiNo);
			LOG.debug("【対象】insType：" + insType);
			LOG.debug("【品目】prodCode：" + prodCode);
			LOG.debug("【配分母数】paramValue：" + paramValueBase);
			LOG.debug("【過去実績】kakoJissekiSum：" + kakoJissekiSum);
			LOG.debug("【過去実績】kakoJissekiJtnSum：" + kakoJissekiJtnSum);
			LOG.debug("【特定施設】specialSum：" + specialSum);
		}

		// 対象品目の納入実績合計を算出し、ベースとなる納入実績をMAPに格納する。
		Map<DistKey, DistributionElement> planProdMap = new HashMap<DistKey, DistributionElement>();
		Long convertkakoJissekiSum = null;
		for (DistributionElement element : prodElementList) {
			Long sumResult = element.getSumResult();
			// 実績値の変換
			if (sumResult != null) {
				// 実績値がマイナスの場合、0とする
				if (sumResult < 0) {
					sumResult = 0L;
				}
				element.setSumResult(sumResult);
			}
			// 施設の属性で振り分ける
			if (element.getDelInsFlg()) {
				// 削除施設
			} else if (element.getExceptDistInsFlg()) {
				// 配分除外施設
			} else if (element.getSpecialInsPlanFlg()) {
				// 特定施設個別計画施設
			} else {
				// 実績合計
				convertkakoJissekiSum = MathUtil.add(convertkakoJissekiSum, element.getSumResult());
				// 配分対象
				planProdMap.put(new DistKey(element.getInsNo(), element.getTmsTytenCd()), element);
			}
		}
		// 対象品目の納入実績合計を6ヶ月分(1期)分にする
		convertkakoJissekiSum = ConvertUtil.parseTermValue(convertkakoJissekiSum, baseParam.getRefFrom(), baseParam.getRefTo());

		// 増分値の取得(計画値 - 対象品目の実績)
		Long paramIncValue = MathUtil.sub(distValue, convertkakoJissekiSum, true);
		boolean upFlg = false;

		// 増分>0かつ、重点先実績>0の場合、UP処理を行う
		if (paramIncValue != null && kakoJissekiJtnSum != null && paramIncValue > 0 && kakoJissekiJtnSum > 0) {
			upFlg = true;
		}

		// ------------------------------
		// 通常配分更新
		// ------------------------------
		// 配分値合計取得
		Long distValueSum = 0L;
		List<DistributionElement> distList = new ArrayList<DistributionElement>();
		distList.addAll(jtnList);
		distList.addAll(ippanList);
		// UP配分
		if (upFlg) {

			// 重点先配分
			Long jtnValueSum = createInsWsPlan(insWsPlanList, prodCode, jtnList, InsWsPlanType.JTN, paramIncValue, kakoJissekiJtnSum, planProdMap);

			// 一般先配分
			Long ippanValueSum = createInsWsPlan(insWsPlanList, prodCode, ippanList, InsWsPlanType.IPPAN, null, null, planProdMap);

			// 計画立案品目の実績のみ存在する明細(順序変更不可！)
			Long planProdSum = createInsWsPlan(insWsPlanList, planProdMap);

			// 配分合計値 = 重点先合計 + 一般先合計
			distValueSum = MathUtil.add(jtnValueSum, ippanValueSum);

			// 配分合計値 = 配分合計値 +  計画立案品目の実績のみ存在する明細
			distValueSum = MathUtil.add(distValueSum, planProdSum);

		} else {

			// DOWN配分(通常配分)
			distValueSum = createInsWsPlan(insWsPlanList, prodCode, distList, InsWsPlanType.NORMAL, distValue, kakoJissekiSum, planProdMap);
		}

		// 丸め誤差の再配分/配分値が上位の計画に加算
		Long diffValue = MathUtil.sub(distValue, distValueSum);
		if (LOG.isDebugEnabled()) {
			LOG.debug("【丸め誤差】diffValue：" + diffValue);
		}

		if (diffValue != null && distList.size() != 0 && diffValue != 0) {

			Collections.sort(insWsPlanList, InsWsPlanDistValueComparator.getInstance());

			InsWsPlanDto plan = insWsPlanList.get(0);
			Long updateValue = MathUtil.add(plan.getDistValue(), diffValue);
			InsWsPlanDto updatePlan = new InsWsPlanDto(plan, updateValue);
			insWsPlanList.set(0, updatePlan);
		}
	}

	/**
	 * 施設特約店別計画の登録情報を生成する。
	 *
	 * @param insWsPlan 登録対象の施設特約店別計画
	 * @param list 登録対象のリスト
	 * @param type 施設特約店別計画の種類
	 * @param paramValue 配分母数
	 * @param kakoJissekiSum 過去実績計
	 * @param planProdMap 対象品目の過去実績リスト
	 * @return 配分値合計
	 */
	protected Long createInsWsPlan(List<InsWsPlanDto> insWsPlanList, String prodCode, List<DistributionElement> list, InsWsPlanType type, Long paramValue, Long kakoJissekiSum,
		Map<DistKey, DistributionElement> planProdMap) {
		// 配分値合計
		Long distValueSum = 0L;
		// 施設特約店別計画の登録
		for (DistributionElement element : list) {
			boolean expectDistInsFlg = false;
			boolean delInsFlg = false;
			Long value = 0L;
			switch (type) {
				case NORMAL:
					value = calcDistValue(paramValue, element.getSumResult(), kakoJissekiSum);
					break;
				case JTN:
					value = calcUpDistValue(paramValue, element.getSumResult(), kakoJissekiSum, getTermDistValue(element, planProdMap));
					break;
				// 一般先の場合、6ヶ月換算した実績値をそのまま設定
				case IPPAN:
					// 納入実績を6ヶ月分(1期)分にする
					value = getTermDistValue(element, planProdMap);
					break;
				case EXCEPT:
					expectDistInsFlg = true;
					break;
				case DELETE:
					delInsFlg = true;
					break;
			}
			// 万単位に丸める
			value = calcTheory(value);
			distValueSum = MathUtil.add(distValueSum, value);
			InsWsPlanDto insWsPlan = new InsWsPlanDto(element.getJgiNo(), prodCode, element.getInsNo(), element.getTmsTytenCd(), value, false, expectDistInsFlg, delInsFlg);
			insWsPlanList.add(insWsPlan);
		}
		return distValueSum;
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
			Long value = getTermDistValue(element);
			// 万単位に丸める
			value = calcTheory(value);
			distValueSum = MathUtil.add(distValueSum, value);
			InsWsPlanDto insWsPlan = new InsWsPlanDto(element.getJgiNo(), prodCode, element.getInsNo(), element.getTmsTytenCd(), value, false, false, false);
			insWsPlanList.add(insWsPlan);
		}
		return distValueSum;
	}

	/**
	 * 施設特約店別計画の登録情報(配分値0)を生成する。
	 *
	 * @param insWsPlan 登録対象の施設特約店別計画
	 * @param list 登録対象のリスト
	 * @param type 施設特約店別計画の種類
	 * @return 配分値
	 */
	protected Long createInsWsPlan(List<InsWsPlanDto> insWsPlanList, String prodCode, List<DistributionElement> list, InsWsPlanType type) {
		return createInsWsPlan(insWsPlanList, prodCode, list, type, 0L, 0L, null);
	}

	/**
	 * 配分結果を算出する。
	 *
	 * @param paramValue 配分母数
	 * @param jisseki 納入実績
	 * @param allJisseki 納入実績合計
	 * @return 配分値
	 */
	protected Long calcDistValue(Long paramValue, Long jisseki, Long allJisseki) {
		return MathUtil.calcDistValue(paramValue, jisseki, allJisseki);
	}

	/**
	 * UP時の重点先配分結果を算出する。
	 *
	 * @param paramValue 増分値
	 * @param jisseki 納入実績
	 * @param allJisseki 重点先納入実績合計
	 * @param convertResult 対象品目の過去実績値
	 * @return 配分値
	 */
	protected Long calcUpDistValue(Long paramValue, Long jisseki, Long allJisseki, Long convertResult) {
		if (jisseki == null) {
			return null;
		}
		Long result = 0L;
		result = MathUtil.add(convertResult, calcDistValue(paramValue, jisseki, allJisseki));
		return result;
	}

	/**
	 * 理論値を丸める。<br>
	 * MMP品の場合、万単位。<br>
	 * 仕入品の場合、千円単位<br>
	 * ワクチン品の場合、千円単位<br>
	 *
	 * @param value 理論値
	 * @return 理論値
	 */
	protected Long calcTheory(Long value) {
		Long result = null;
		switch (distType) {
			case OFFICE_PLAN_DIST:
			case OFFICE_PLAN_RE_DIST:
			case MR_PLAN_DIST:
			case MR_PLAN_RE_DIST:
				result = MathUtil.calcTheory(value, Scale.TEN_THOUSAND);
				break;
			case MR_PLAN_DIST_FOR_VAC:
				result = MathUtil.calcTheory(value, Scale.THOUSAND);
				break;
		}
		return result;
	}

	/**
	 * 自品目の実績値を取得する。<br>
	 * 対象品目の過去実績リストに対象実績がある場合、6ヶ月換算する。<br>
	 * 換算後、対象実績をリストから削除する。<br>
	 * 対象実績がない場合、実績値を0として返す。
	 *
	 * @param element 配分要素
	 * @param planProdMap 対象品目の過去実績リスト
	 * @return 6ヶ月換算した自品目の実績値
	 */
	protected Long getTermDistValue(DistributionElement element, Map<DistKey, DistributionElement> planProdMap) {
		Long result = 0L;
		DistKey key = new DistKey(element.getInsNo(), element.getTmsTytenCd());
		if (planProdMap.containsKey(key)) {
			DistributionElement distributionElement = planProdMap.get(key);
			result = getTermDistValue(distributionElement);
			planProdMap.remove(key);
		}
		return result;
	}

	/**
	 * 6ヶ月換算した実績値を取得する。
	 *
	 *
	 * @param element 配分要素
	 * @return 6ヶ月換算した実績値
	 */
	protected Long getTermDistValue(DistributionElement element) {
		return ConvertUtil.parseTermValue(element.getSumResult(), baseParam.getRefFrom(), baseParam.getRefTo());
	}

	/**
	 * 対象品目の配分要素リストのキーオブジェクト
	 *
	 * @author khashimoto
	 */
	private class DistKey {
		/** 施設コード */
		private final String insNo;
		/** 特約店コード */
		private final String tmsTytenCd;

		/**
		 * コンストラクタ
		 *
		 * @param insNo 施設コード
		 * @param tmsTytenCd 特約店コード
		 */
		private DistKey(String insNo, String tmsTytenCd) {
			this.insNo = insNo;
			this.tmsTytenCd = tmsTytenCd;
		}

		@Override
		public boolean equals(Object entry) {
			if (entry != null && DistKey.class.isAssignableFrom(entry.getClass())) {
				DistKey obj = (DistKey) entry;
				return new EqualsBuilder().append(this.insNo, obj.insNo).append(this.tmsTytenCd, obj.tmsTytenCd).isEquals();
			}
			return false;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(this.insNo).append(this.tmsTytenCd).toHashCode();
		}
	}
}
