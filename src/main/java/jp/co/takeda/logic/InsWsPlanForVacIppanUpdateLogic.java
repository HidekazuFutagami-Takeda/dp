package jp.co.takeda.logic;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.bean.Scale;
import jp.co.takeda.dao.InsWsPlanDao;
import jp.co.takeda.dao.InsWsPlanForVacDao;
import jp.co.takeda.dto.InsWsPlanForVacProdSummaryDto;
import jp.co.takeda.model.InsWsPlan;
import jp.co.takeda.model.InsWsPlanForVac;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.util.MathUtil;

/**
 * (ワ)施設特約店別計画の一般先の更新を行うロジッククラス
 *
 * @author khashimoto
 */
public class InsWsPlanForVacIppanUpdateLogic {

	/**
	 * 施設特約店別計画取得DAO
	 */
	private final InsWsPlanDao insWsPlanDao;

	/**
	 * ワクチン用施設特約店別計画取得DAO
	 */
	private final InsWsPlanForVacDao insWsPlanForVacDao;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * JIS府県コード
	 */
	private final Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 一般先計の計画値
	 */
	private final Long ippanValue;

	/**
	 * 一般先計の更新日時
	 */
	private final Date upDate;


	/**
	 * コンストラクタ
	 *
	 * @param InsWsPlanDao 施設特約店別計画取得DAO
	 * @param insWsPlanForVacDao ワクチン用施設特約店別計画取得DAO
	 * @param jgiNo 従業員番号
	 * @param prodCode 品目固定コード
	 * @param addrCodePref JIS府県コード
	 * @param addrCodeCity JIS市区町村コード
	 * @param ippanValue 一般先計の計画値
	 * @param upDate 一般先計の更新日時
	 */
	public InsWsPlanForVacIppanUpdateLogic(InsWsPlanDao insWsPlanDao, InsWsPlanForVacDao insWsPlanForVacDao, Integer jgiNo, String prodCode, Prefecture addrCodePref, String addrCodeCity, Long ippanValue,
		Date upDate) {
		this.insWsPlanDao = insWsPlanDao;
		this.insWsPlanForVacDao = insWsPlanForVacDao;
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.addrCodePref = addrCodePref;
		this.addrCodeCity = addrCodeCity;
		this.ippanValue = ippanValue;
		this.upDate = upDate;
	}

	/**
	 * 一般先の更新を行う。
	 */
	public void execute() {
		if (upDate == null) {
			return;
		}
		List<InsWsPlanForVac> ippanList;
		InsWsPlanForVacProdSummaryDto summaryDto;
		try {
			// 一般先リストの取得
			ippanList = insWsPlanForVacDao.searchList(InsWsPlanForVacDao.SORT_STRING, jgiNo, prodCode, ActivityType.IPPAN, addrCodePref, addrCodeCity);
			// 一般先サマリの取得
			summaryDto = insWsPlanForVacDao.searchProdSummary(prodCode, null, jgiNo, ActivityType.IPPAN, addrCodePref, addrCodeCity);
		} catch (DataNotFoundException e) {
			final String errMsg = "一般先更新対象が見つからないため、楽観的ロックエラーとする";
			throw new OptimisticLockingFailureException(errMsg, e);
		}

		// 更新値サマリ
		Long updateValueSum = 0L;
		for (InsWsPlanForVac insWsPlanForVac : ippanList) {
			// 計画値 = 理論値 * 計画値合計/理論値合計
			Long updateValue = MathUtil.calcDistValue(insWsPlanForVac.getDistValueB(), ippanValue, summaryDto.getDistValueB());
			// 千円単位で丸める
			updateValue = MathUtil.calcTheory(updateValue, Scale.THOUSAND);
			insWsPlanForVac.setPlannedValueB(updateValue);
			updateValueSum = MathUtil.add(updateValueSum, updateValue);
		}

		// 更新値サマリ(理論値)が0以外の場合、丸め誤差を上位の計画に加算する。
		if (updateValueSum != 0) {
			Long diffValue = MathUtil.sub(ippanValue, updateValueSum);
			if (diffValue != null && diffValue != 0) {
				Collections.sort(ippanList, InsWsPlanForVacValueComparator.getInstance());

				InsWsPlanForVac plan = ippanList.get(0);
				Long updateValue = MathUtil.add(plan.getPlannedValueB(), diffValue);
				plan.setPlannedValueB(updateValue);
				ippanList.set(0, plan);
			}
		}

		for (InsWsPlanForVac insWsPlanForVac : ippanList) {
			//VMからActionに渡している更新日時が１件目だけ。２件目が１件目と比較してコンマ数秒だけ未来と判断してチェックにひっかかるため。
//			// 更新対象の日付が、新しいレコードがある場合排他エラー
//			if (insWsPlanForVac.getUpDate().after(upDate)) {
//				final String errMsg = "施設特約店別計画が更新されているため、楽観的ロックエラーとする";
//				throw new OptimisticLockingFailureException(errMsg);
//			}

			InsWsPlan insWsPlan = new InsWsPlan();
			insWsPlan.setSeqKey(insWsPlanForVac.getSeqKey());
			insWsPlan.setJgiNo(insWsPlanForVac.getJgiNo());
			insWsPlan.setProdCode(insWsPlanForVac.getProdCode());
			insWsPlan.setInsNo(insWsPlanForVac.getInsNo());
			insWsPlan.setTmsTytenCd(insWsPlanForVac.getTmsTytenCd());
			insWsPlan.setDistValueY(insWsPlanForVac.getDistValueB());
			// add Start 2023/8/08 H.Futagami 不具合対応No.04　施設特約店別計画：参照、更新処理修正
		    insWsPlan.setModifyValueY(insWsPlanForVac.getPlannedValueB());
			// add End 2023/8/08 H.Futagami 不具合対応No.04　施設特約店別計画：参照、更新処理修正
			// mod Start 2023/8/08 H.Futagami 不具合対応No.04　施設特約店別計画：参照、更新処理修正
			//insWsPlan.setPlannedValueY(insWsPlanForVac.getPlannedValueB());
			if (insWsPlanForVac.getPlannedValueB() == null) {
				insWsPlan.setPlannedValueY(insWsPlanForVac.getDistValueB());
			}else {
				insWsPlan.setPlannedValueY(insWsPlanForVac.getPlannedValueB());
			}
			// mod End 2023/8/08 H.Futagami 不具合対応No.04　施設特約店別計画：参照、更新処理修正
			insWsPlan.setBefPlannedValueY(insWsPlanForVac.getBefPlannedValueB());
			insWsPlan.setPlannedValueT(insWsPlanForVac.getPlannedValueT());
			insWsPlan.setSpecialInsPlanFlg(insWsPlanForVac.getSpecialInsPlanFlg());
			insWsPlan.setExpectDistInsFlg(insWsPlanForVac.getExceptDistInsFlg());
			insWsPlan.setDelInsFlg(insWsPlanForVac.getDelInsFlg());
			insWsPlan.setUpJgiNo(insWsPlanForVac.getUpJgiNo());
			insWsPlan.setUpJgiName(insWsPlanForVac.getUpJgiName());
			insWsPlan.setUpDate(insWsPlanForVac.getUpDate());

			// 更新実行
//			insWsPlanForVacDao.update(insWsPlanForVac);
			// 更新
			insWsPlanDao.update(insWsPlan);
		}
	}
}
