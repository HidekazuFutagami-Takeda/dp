package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DeliveryResultInsDto;
import jp.co.takeda.dto.DeliveryResultVacInsListDto;
import jp.co.takeda.model.DeliveryResultForVac;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.model.div.Term;
import jp.co.takeda.security.DpUserInfo;

/**
 * (ワ)過去実績参照(施設特約店別計画)ロジッククラス
 * 
 * @author siwamoto
 */
public class DeliveryResultVacInsResultDtoCreateLogic {

	/**
	 * 納入実績リスト
	 */
	List<DeliveryResultForVac> deliveryResultList;

	/**
	 * 従業員氏名
	 */
	String jgiName;

	/**
	 * コンストラクタ
	 * 
	 * @param deliveryResultList 納入実績リスト
	 * @param jgiName 従業員氏名
	 */
	public DeliveryResultVacInsResultDtoCreateLogic(List<DeliveryResultForVac> deliveryResultList, String jgiName) {
		this.deliveryResultList = deliveryResultList;
		this.jgiName = jgiName;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param deliveryResultList 納入実績リスト
	 * @param jgiName 従業員氏名
	 */
	public DeliveryResultVacInsResultDtoCreateLogic(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 検索結果DTO作成する。
	 * 
	 * @return 過去実績詳細検索結果
	 */
	public DeliveryResultVacInsListDto createResultDtoList() throws LogicalException {
		String sysYear = DpUserInfo.getDpUserInfo().getSysManage().getSysYear();
		Term sysTerm = DpUserInfo.getDpUserInfo().getSysManage().getSysTerm();
		List<Date> refDateList = createDateList(sysYear, sysTerm);
		ArrayList<DeliveryResultInsDto> resultDtoList = new ArrayList<DeliveryResultInsDto>();
		Long[] sumT = { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L };
		MonNnu monNnuT = new MonNnu();
		Long[] sumI = { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L };
		MonNnu monNnuI = new MonNnu();
		String oldInsNo = "";
		boolean isFirst = true;
		boolean isInsFast = true;
		for (DeliveryResultForVac deliveryResult : deliveryResultList) {
			if (!deliveryResult.getInsNo().equals(oldInsNo) && !isFirst) {
				//特約店計
				monNnuT = createMonNnu(sumT);
				List<Long> monNnuValueList = convertMonNnuToList(monNnuT);
				DeliveryResultInsDto resultDto = new DeliveryResultInsDto("", "計", monNnuValueList);
				resultDtoList.add(resultDto);
				sumI = createSumArray(sumI, monNnuT);
				Long[] temp = { 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L };
				sumT = temp;
				isInsFast = true;
			}
			String insName = "";
			if (isInsFast) {
				insName = deliveryResult.getInsAbbrName();
			}
			String tmsTytenName = deliveryResult.getTmsTytenMeiKj();
			List<Long> monNnuValueList = convertMonNnuToList(deliveryResult.getMonNnu());
			DeliveryResultInsDto resultDto = new DeliveryResultInsDto(insName, tmsTytenName, monNnuValueList);
			resultDtoList.add(resultDto);
			sumT = createSumArray(sumT, deliveryResult.getMonNnu());
			oldInsNo = deliveryResult.getInsNo();
			isFirst = false;
			isInsFast = false;
		}
		//特約店計
		monNnuT = createMonNnu(sumT);
		List<Long> monNnuValueList = convertMonNnuToList(monNnuT);
		DeliveryResultInsDto resultDto = new DeliveryResultInsDto("", "計", monNnuValueList);
		resultDtoList.add(resultDto);
		sumI = createSumArray(sumI, monNnuT);
		//施設計
		monNnuI = createMonNnu(sumI);
		monNnuValueList = convertMonNnuToList(monNnuI);
		resultDto = new DeliveryResultInsDto("施設計", "", monNnuValueList);
		resultDtoList.add(0, resultDto);
		DeliveryResultVacInsListDto result = new DeliveryResultVacInsListDto(resultDtoList, refDateList, jgiName);
		return result;
	}

	public DeliveryResultVacInsListDto createResultDtoList2() throws LogicalException {
		String sysYear = DpUserInfo.getDpUserInfo().getSysManage().getSysYear();
		Term sysTerm = DpUserInfo.getDpUserInfo().getSysManage().getSysTerm();
		List<Date> refDateList = createDateList(sysYear, sysTerm);
		DeliveryResultVacInsListDto result = new DeliveryResultVacInsListDto(null, refDateList, jgiName);
		return result;
	}

	/**
	 * 
	 * 計算用に引数がnullの場合に0を返す。
	 * 
	 * @param value 値
	 * @return 過去実績詳細検索結果
	 */
	private Long getLongValue(Long value) {
		if (value != null) {
			return value;
		} else {
			return 0L;
		}
	}

	/**
	 * 引数をMonNnuの各プロパティに設定したMonNnuを生成する。
	 * 
	 * @param Long[] value
	 * @return 生成されたMonNnu
	 */
	private MonNnu createMonNnu(Long[] value) {
		MonNnu obj = new MonNnu();
		obj.setDeliveryRecord01(value[0]);
		obj.setDeliveryRecord02(value[1]);
		obj.setDeliveryRecord03(value[2]);
		obj.setDeliveryRecord04(value[3]);
		obj.setDeliveryRecord05(value[4]);
		obj.setDeliveryRecord06(value[5]);
		obj.setDeliveryRecord07(value[6]);
		obj.setDeliveryRecord08(value[7]);
		obj.setDeliveryRecord09(value[8]);
		obj.setDeliveryRecord10(value[9]);
		obj.setDeliveryRecord11(value[10]);
		obj.setDeliveryRecord12(value[11]);
		obj.setDeliveryRecord13(value[12]);
		obj.setDeliveryRecord14(value[13]);
		obj.setDeliveryRecord15(value[14]);
		obj.setDeliveryRecord16(value[15]);
		obj.setDeliveryRecord17(value[16]);
		obj.setDeliveryRecord18(value[17]);
		obj.setDeliveryRecord19(value[18]);
		obj.setDeliveryRecord20(value[19]);
		obj.setDeliveryRecord21(value[20]);
		obj.setDeliveryRecord22(value[21]);
		obj.setDeliveryRecord23(value[22]);
		obj.setDeliveryRecord24(value[23]);
		obj.setPreFarAdvancePeriod(value[24]);
		obj.setFarAdvancePeriod(value[25]);
		obj.setAdvancePeriod(value[26]);
		obj.setCurrentPeriod(value[27]);
		obj.setCurrentPlanValue(value[28]);

		return obj;
	}

	/**
	 * 引数のLong[]に引数のMonNnuの各プロパティを加算し、計算結果のLong[]を返す。
	 * 
	 * @param Long[] value
	 * @param MonNnu obj
	 * @return 計算結果のLong[]
	 */
	private Long[] createSumArray(Long[] value, MonNnu obj) {
		value[0] += getLongValue(obj.getDeliveryRecord01());
		value[1] += getLongValue(obj.getDeliveryRecord02());
		value[2] += getLongValue(obj.getDeliveryRecord03());
		value[3] += getLongValue(obj.getDeliveryRecord04());
		value[4] += getLongValue(obj.getDeliveryRecord05());
		value[5] += getLongValue(obj.getDeliveryRecord06());
		value[6] += getLongValue(obj.getDeliveryRecord07());
		value[7] += getLongValue(obj.getDeliveryRecord08());
		value[8] += getLongValue(obj.getDeliveryRecord09());
		value[9] += getLongValue(obj.getDeliveryRecord10());
		value[10] += getLongValue(obj.getDeliveryRecord11());
		value[11] += getLongValue(obj.getDeliveryRecord12());
		value[12] += getLongValue(obj.getDeliveryRecord13());
		value[13] += getLongValue(obj.getDeliveryRecord14());
		value[14] += getLongValue(obj.getDeliveryRecord15());
		value[15] += getLongValue(obj.getDeliveryRecord16());
		value[16] += getLongValue(obj.getDeliveryRecord17());
		value[17] += getLongValue(obj.getDeliveryRecord18());
		value[18] += getLongValue(obj.getDeliveryRecord19());
		value[19] += getLongValue(obj.getDeliveryRecord20());
		value[20] += getLongValue(obj.getDeliveryRecord21());
		value[21] += getLongValue(obj.getDeliveryRecord22());
		value[22] += getLongValue(obj.getDeliveryRecord23());
		value[23] += getLongValue(obj.getDeliveryRecord24());
		value[24] += getLongValue(obj.getPreFarAdvancePeriod());
		value[25] += getLongValue(obj.getFarAdvancePeriod());
		value[26] += getLongValue(obj.getAdvancePeriod());
		value[27] += getLongValue(obj.getCurrentPeriod());
		value[28] += getLongValue(obj.getCurrentPlanValue());
		return value;
	}

	/**
	 * 過去実績期間をあらわすDate型のListを返す。
	 * 
	 * @param sysYear 年度
	 * @param sysTerm 期
	 * @return 過去実績期間のList<Date>
	 */
	private List<Date> createDateList(String sysYear, Term sysTerm) {
		List<Date> dateList = new ArrayList<Date>();
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_01, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_02, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_03, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_04, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_05, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_06, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_07, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_08, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_09, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_10, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_11, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_12, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_13, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_14, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_15, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_16, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_17, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_18, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_19, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_20, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_21, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_22, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_23, sysYear, sysTerm));
		dateList.add(RefPeriod.convertRefPeriod(RefPeriod.REF_24, sysYear, sysTerm));
		return dateList;
	}

	/**
	 * 引数のMonNnuの各プロパティを要素にもつLong[]のListを返す。
	 * 
	 * @param monNnu 納入実績
	 * @return Long配列のList
	 */
	public List<Long> convertMonNnuToList(MonNnu monNnu) {
		List<Long> resultList = new ArrayList<Long>();
		if (monNnu != null) {
			resultList.add(monNnu.getDeliveryRecord01());
			resultList.add(monNnu.getDeliveryRecord02());
			resultList.add(monNnu.getDeliveryRecord03());
			resultList.add(monNnu.getDeliveryRecord04());
			resultList.add(monNnu.getDeliveryRecord05());
			resultList.add(monNnu.getDeliveryRecord06());
			resultList.add(monNnu.getDeliveryRecord07());
			resultList.add(monNnu.getDeliveryRecord08());
			resultList.add(monNnu.getDeliveryRecord09());
			resultList.add(monNnu.getDeliveryRecord10());
			resultList.add(monNnu.getDeliveryRecord11());
			resultList.add(monNnu.getDeliveryRecord12());
			resultList.add(monNnu.getDeliveryRecord13());
			resultList.add(monNnu.getDeliveryRecord14());
			resultList.add(monNnu.getDeliveryRecord15());
			resultList.add(monNnu.getDeliveryRecord16());
			resultList.add(monNnu.getDeliveryRecord17());
			resultList.add(monNnu.getDeliveryRecord18());
			resultList.add(monNnu.getDeliveryRecord19());
			resultList.add(monNnu.getDeliveryRecord20());
			resultList.add(monNnu.getDeliveryRecord21());
			resultList.add(monNnu.getDeliveryRecord22());
			resultList.add(monNnu.getDeliveryRecord23());
			resultList.add(monNnu.getDeliveryRecord24());
			resultList.add(monNnu.getPreFarAdvancePeriod());
			resultList.add(monNnu.getFarAdvancePeriod());
			resultList.add(monNnu.getAdvancePeriod());
			resultList.add(monNnu.getCurrentPeriod());
			resultList.add(monNnu.getCurrentPlanValue());
			if (monNnu.getCurrentPeriod() != null && monNnu.getCurrentPlanValue() != null && monNnu.getCurrentPlanValue() > 0) {
				resultList.add((new Double(monNnu.getCurrentPeriod().doubleValue() / monNnu.getCurrentPlanValue().doubleValue() * 100d)).longValue());
			} else {
				resultList.add(0L);
			}
		} else {
			for (int i = 0; i < 30; i++) {
				resultList.add(0L);
			}
		}
		return resultList;
	}
}
