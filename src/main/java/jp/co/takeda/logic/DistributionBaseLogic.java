package jp.co.takeda.logic;

import java.util.List;
import java.util.Map;

import jp.co.takeda.bean.Scale;
import jp.co.takeda.dto.InsWsPlanDto;
import jp.co.takeda.logic.div.InsWsDistType;
import jp.co.takeda.logic.div.InsWsPlanType;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.view.DistributionElement;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 配分ロジッククラスのベースクラス<br>
 *
 * @author nozaki
 */
public class DistributionBaseLogic {

	/**
	 * ロガー
	 */
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(DistributionBaseLogic.class);

	/**
	 * 試算/配分の共通パラメータ
	 */
	protected final BaseParam baseParam;

	/**
	 * 施設特約店別計画の配分種類
	 */
	protected final InsWsDistType distType;

	/**
	 * コンストラクタ
	 *
	 * @param baseParam 試算/配分の共通パラメータ
	 * @param distType 施設特約店別計画の配分種類
	 */
	public DistributionBaseLogic(BaseParam baseParam, InsWsDistType distType) {
		this.baseParam = baseParam;
		this.distType = distType;
	}

	/**
	 * コンストラクタ
	 *
	 * @param distType 施設特約店別計画の配分種類
	 */
	public DistributionBaseLogic(InsWsDistType distType) {
		this.baseParam = null;
		this.distType = distType;
	}

	/**
	 * 施設特約店別計画の登録情報を生成する。
	 *
	 * @param insWsPlan 登録対象の施設特約店別計画
	 * @param prodCode 品目固定コード(計画対象品目)
	 * @param list 登録対象のリスト
	 * @param type 施設特約店別計画の種類
	 * @param paramValue 配分母数
	 * @param kakoJissekiSum 配分品目過去実績計
	 * @param planProdMap 計画対象品目の過去実績リスト
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

					// -----------------------
					// 通常配分
					// -----------------------
					// 【配分値】配分母数の構成比配分(配分品目)
					value = MathUtil.calcDistValue(paramValue, element.getSumResult(), kakoJissekiSum);
					break;

				case JTN:

					// -----------------------
					// 重点先配分
					// -----------------------
					if (element.getSumResult() == null) {
						value = null;
					} else {

						// 6ヶ月換算した実績値(計画対象品目)
						Long pastValue = getTermDistValue(element, planProdMap);

						// 増分値の構成比配分(配分品目)
						Long incrementValue = MathUtil.calcDistValue(paramValue, element.getSumResult(), kakoJissekiSum);

						// 【配分値】6ヶ月換算した実績値(計画対象品目) ＋ 増分値の構成比配分(配分品目)
						value = MathUtil.add(pastValue, incrementValue);
					}
					break;

				case IPPAN:

					// -----------------------
					// 一般先配分
					// -----------------------
					// 【配分値】6ヶ月換算した実績値(計画対象品目)
					value = getTermDistValue(element, planProdMap);
					break;

				case EXCEPT:

					// 配分除外施設
					expectDistInsFlg = true;
					break;

				case DELETE:

					// 削除施設
					delInsFlg = true;
					break;
			}

			// 削除施設の場合は、delInsFlgを設定
			if (element.getDelInsFlg()) {
				delInsFlg = true;
			}

			// 指定単位に丸める
			value = calcTheory(value);
			distValueSum = MathUtil.add(distValueSum, value);
			InsWsPlanDto insWsPlan = new InsWsPlanDto(element.getJgiNo(), prodCode, element.getInsNo(), element.getTmsTytenCd(), value, false, expectDistInsFlg, delInsFlg);
			insWsPlanList.add(insWsPlan);
		}
		return distValueSum;
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
			result = ConvertUtil.parseTermValue(distributionElement.getSumResult(), baseParam.getRefFrom(), baseParam.getRefTo());
			planProdMap.remove(key);
		}
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
			case MR_PLAN_DIST:
			case MR_PLAN_RE_DIST:
			case OFFICE_PLAN_DIST:
			case OFFICE_PLAN_RE_DIST:
				result = MathUtil.calcTheory(value, Scale.TEN_THOUSAND);
				break;
			case MR_PLAN_DIST_FOR_VAC:
				result = MathUtil.calcTheory(value, Scale.THOUSAND);
				break;
		}
		return result;
	}

	/**
	 * 対象品目の配分要素リストのキーオブジェクト
	 *
	 * @author khashimoto
	 */
	protected class DistKey {
		/** 施設コード */
		private final String insNo;
		/** 特約店コード */
		private final String tmsTytenCd;

		/**
		 *
		 * コンストラクタ
		 *
		 * @param insNo 施設コード
		 * @param tmsTytenCd 特約店コード
		 */
		protected DistKey(String insNo, String tmsTytenCd) {
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
