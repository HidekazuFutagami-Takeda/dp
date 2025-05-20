package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.dto.SpecialInsPlanSearchForVacResultDto;
import jp.co.takeda.model.DeliveryResultForVac;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SpecialInsPlanForVac;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * ワクチン用特定施設個別計画立案画面の検索結果用DTO生成ロジック
 *
 * @author khashimoto
 */
public class CreateSpecialInsPlanSearchForVacResultDtoLogic {

	/** 品目リスト */
	private final List<PlannedProd> plannedProdList;

	/** 施設情報リスト */
	private final List<DpsInsMst> insMstList;

	/** 特定施設個別計画リスト */
	private final List<SpecialInsPlanForVac> specialInsPlans;

	/** 過去実績リスト */
	private final List<DeliveryResultForVac> deliveryResults;

	/** 配分除外施設リスト */
	private final List<String> exceptDistInsList;

	/** 配分除外品目リスト */
	private final Map<String, List<String>> exceptDistProdList;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param plannedProdList
	 * @param insMstList
	 * @param specialInsPlans
	 * @param deliveryResults
	 * @param exceptDistInsList
	 * @param exceptDistProdList
	 */
	public CreateSpecialInsPlanSearchForVacResultDtoLogic(List<PlannedProd> plannedProdList, List<DpsInsMst> insMstList, List<SpecialInsPlanForVac> specialInsPlans,
		List<DeliveryResultForVac> deliveryResults, List<String> exceptDistInsList, Map<String, List<String>> exceptDistProdList) {
		this.plannedProdList = plannedProdList;
		this.insMstList = insMstList;
		this.specialInsPlans = specialInsPlans;
		this.deliveryResults = deliveryResults;
		this.exceptDistInsList = exceptDistInsList;
		this.exceptDistProdList = exceptDistProdList;
	}

	/**
	 * SpecialInsPlanProdListResultDtoオブジェクトリスト 変換処理
	 *
	 * @return SpecialInsPlanProdListResultDtoオブジェクトリスト
	 */
	public List<SpecialInsPlanSearchForVacResultDto> convertSpecialInsPlanProdListResultDto() {
		List<SpecialInsPlanSearchForVacResultDto> specialInsPlanProdListResultDto = new ArrayList<SpecialInsPlanSearchForVacResultDto>();
		// 特定施設インデックス
		int specialIndex = 0;
		// 過去実績インデックス
		int deliveryIndex = 0;
		// 全品目サマリー(営業所案/担当者案/納入実績
		Long msumY = 0L;
		Long[] msumD = { 0L, 0L, 0L, 0L };

		//対象品目分繰り返す
		for (PlannedProd plannedProd : plannedProdList) {
			//結果DTOコンストラクタ引数
			//品目名称
			String prodName = plannedProd.getProdName();
			//品目固定コード
			String prodCode = plannedProd.getProdCode();
			//製品区分
			String prodType = plannedProd.getProdType();

			// 品目計
			Long hsumY = 0L;
			Long[] hsumD = { 0L, 0L, 0L, 0L };
			// 施設分繰り返す
			for (DpsInsMst insMst : insMstList) {
				//施設分類
				String insClazz = InsClass.getInsClassName(insMst.getInsClass(), insMst.getOldInsrFlg());
				//施設コード
				String insNo = insMst.getInsNo();
				//施設名
				String insName = insMst.getInsAbbrName();
				//削除フラグ
				boolean delFlg = new DelInsLogic(insMst.getReqFlg(), insMst.getDelFlg()).getDelFlg();
				//配分除外フラグ
				boolean exDistFlg = getExDistFlg(insNo, prodCode);
				//施設計
				Long ssumY = 0L;
				Long[] temp = { 0L, 0L, 0L, 0L };
				Long[] ssumD = temp;

				// 明細有無フラグ
				boolean meisaiFlg = false;
				boolean continueFlg = true;
				while (continueFlg) {
					//
					Long plannedValueY = null;
					String tmsTytenCd = null;
					String tmsTytenName = null;
					Boolean planTaiGaiFlgTok = null;
					Boolean planTaiGaiFlgRik = null;
					Date upDate = null;
					String upJgiName = null;
					MonNnu monNnu0 = null;

					//品目と施設が一致する場合、特定施設個別計画・実績を設定する
					AddCondition addCondition = judgment(specialIndex, deliveryIndex, prodCode, insNo);
					SpecialInsPlanForVac specialInsPlan = null;
					DeliveryResultForVac deliveryResult = null;
					switch (addCondition) {
						case BOTH_ADD:
							specialInsPlan = specialInsPlans.get(specialIndex);
							deliveryResult = deliveryResults.get(deliveryIndex);
							specialIndex++;
							deliveryIndex++;
							meisaiFlg = true;
							break;
						case SPECIAL_ADD:
							specialInsPlan = specialInsPlans.get(specialIndex);
							specialIndex++;
							meisaiFlg = true;
							break;
						case DELI_ADD:
							deliveryResult = deliveryResults.get(deliveryIndex);
							deliveryIndex++;
							meisaiFlg = true;
							break;
						case BOTH_NONE:
							continueFlg = false;
							// 明細がある場合、ループ終了
							if (meisaiFlg) {
								continue;
							}
							// 明細が無い場合、空行作成
							break;
					}
					// 特定施設の設定
					if (specialInsPlan != null) {
						tmsTytenCd = specialInsPlan.getTmsTytenCd();
						tmsTytenName = specialInsPlan.getTmsTytenName();
						plannedValueY = ConvertUtil.parseMoneyToThousandUnit(specialInsPlan.getPlannedValueB());
						upDate = specialInsPlan.getUpDate();
						upJgiName = specialInsPlan.getUpJgiName();
						planTaiGaiFlgTok = specialInsPlan.getPlanTaiGaiFlgTok();
						planTaiGaiFlgRik = specialInsPlan.getPlanTaiGaiFlgRik();
						//施設計
						ssumY += getLongValue(plannedValueY);
					}
					// 過去実績の設定
					if (deliveryResult != null) {
						tmsTytenCd = deliveryResult.getTmsTytenCd();
						tmsTytenName = deliveryResult.getTmsTytenMeiKj();
						monNnu0 = deliveryResult.getMonNnu();
						monNnu0.setPreFarAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getPreFarAdvancePeriod()));
						monNnu0.setFarAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getFarAdvancePeriod()));
						monNnu0.setAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getAdvancePeriod()));
						monNnu0.setCurrentPeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getCurrentPeriod()));
						planTaiGaiFlgTok = deliveryResult.getPlanTaiGaiFlgTok();
						planTaiGaiFlgRik = deliveryResult.getPlanTaiGaiFlgRik();
						//施設計
						ssumD = createSumArray(ssumD, monNnu0);
					}
					// DTO生成
					SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(insNo, insName, insClazz, plannedValueY, null, prodCode, prodName, prodType,
						tmsTytenCd, tmsTytenName, upDate, upJgiName, monNnu0, delFlg, exDistFlg, planTaiGaiFlgTok, planTaiGaiFlgRik);
					specialInsPlanProdListResultDto.add(resultDto);
				}
				//施設計
				MonNnu monNnuSsumD = createMonNnu(ssumD);
				SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(insNo, "sSum", insClazz, ssumY, null, prodCode, prodName, prodType, null, null,
					null, null, monNnuSsumD, delFlg, exDistFlg, false, false);
				specialInsPlanProdListResultDto.add(resultDto);
				hsumY += ssumY;
				hsumD = createSumArray(hsumD, monNnuSsumD);
			}
			//品目計
			MonNnu monNnuHsumD = createMonNnu(hsumD);
			SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(null, "hSum", null, hsumY, null, prodCode, prodName, prodType, null, null, null,
				null, monNnuHsumD, null, null, false, false);
			specialInsPlanProdListResultDto.add(resultDto);
			msumY = MathUtil.add(msumY, hsumY);
			msumD = createSumArray(msumD, monNnuHsumD);
		}

		//計
		MonNnu monNnuMsumD = createMonNnu(msumD);
		SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(null, "mSum", null, msumY, null, null, "ワクチン計", null, null, null, null, null,
			monNnuMsumD, null, null, false, false);
		specialInsPlanProdListResultDto.add(resultDto);
		return specialInsPlanProdListResultDto;

	}

	/**
	 * SpecialInsMrPlanProdListResultDtoオブジェクトリスト 変換処理
	 *
	 * @return SpecialInsMrPlanProdListResultDtoオブジェクトリスト
	 */
	public List<SpecialInsPlanSearchForVacResultDto> convertSpecialInsMrPlanProdListResultDto() {
		List<SpecialInsPlanSearchForVacResultDto> specialInsPlanProdListResultDto = new ArrayList<SpecialInsPlanSearchForVacResultDto>();
		// 特定施設インデックス
		int specialIndex = 0;
		// 過去実績インデックス
		int deliveryIndex = 0;
		// 全品目サマリー(営業所案/担当者案/納入実績
		Long msumY = 0L;
		Long msumY2 = 0L;
		Long[] msumD = { 0L, 0L, 0L, 0L };

		//対象品目分繰り返す
		for (PlannedProd plannedProd : plannedProdList) {
			//結果DTOコンストラクタ引数
			//品目名称
			String prodName = plannedProd.getProdName();
			//品目固定コード
			String prodCode = plannedProd.getProdCode();
			//製品区分
			String prodType = plannedProd.getProdType();

			// 品目計
			Long hsumY = 0L;
			Long hsumY2 = 0L;
			Long[] hsumD = { 0L, 0L, 0L, 0L };
			// 施設分繰り返す
			for (DpsInsMst insMst : insMstList) {
				//施設分類
				String insClazz = InsClass.getInsClassName(insMst.getInsClass(), insMst.getOldInsrFlg());

//				//対象区分
//				String insType = insMst.getHoInsType().getDbValue();
				//施設コード
				String insNo = insMst.getInsNo();
				//施設名
				String insName = insMst.getInsAbbrName();
				//削除フラグ
				boolean delFlg = new DelInsLogic(insMst.getReqFlg(), insMst.getDelFlg()).getDelFlg();
				//配分除外フラグ
				boolean exDistFlg = getExDistFlg(insNo, prodCode);
				//施設計
				Long ssumY = 0L;
				Long ssumY2 = 0L;
				Long[] temp = { 0L, 0L, 0L, 0L };
				Long[] ssumD = temp;

				// 品目施設情報取得
//				ProdInsInfoKanri prodIns = new ProdInsInfoKanri();
//				try {
//					prodIns = prodInsInfoKanriDao.searchByInsNo(prodCode, insNo);
//				} catch (DataNotFoundException e) {
//				}
//
//				// 表示対象かどうか判定
//				boolean isNotDisp = ProdInsInfoScanDispKbn.NOT == prodIns.getProdInsInfoScanDispKbn();


				// 明細有無フラグ
				boolean meisaiFlg = false;
				boolean continueFlg = true;
//				List<SpecialInsPlanProdListResultDto> resultTmsList = new ArrayList<SpecialInsPlanProdListResultDto>();
				while (continueFlg) {
					// 特定施設個別計画情報初期化
					Long seqKey = null;
					Long plannedValueY = null;
					Long plannedValueY2 = null;
					String tmsTytenCd = null;
					String tmsTytenName = null;
					Boolean planTaiGaiFlgTok = null;
					Boolean planTaiGaiFlgRik = null;
					Date upDate = null;
					String upJgiName = null;
					MonNnu monNnu0 = null;

					//品目と施設が一致する場合、特定施設個別計画・実績を設定する
					AddCondition addCondition = judgment(specialIndex, deliveryIndex, prodCode, insNo);
					SpecialInsPlanForVac specialInsPlan = null;
					DeliveryResultForVac deliveryResult = null;
					switch (addCondition) {
						case BOTH_ADD:
							specialInsPlan = specialInsPlans.get(specialIndex);
							deliveryResult = deliveryResults.get(deliveryIndex);
							specialIndex++;
							deliveryIndex++;
							meisaiFlg = true;
							break;
						case SPECIAL_ADD:
							specialInsPlan = specialInsPlans.get(specialIndex);
							specialIndex++;
							meisaiFlg = true;
							break;
						case DELI_ADD:
							deliveryResult = deliveryResults.get(deliveryIndex);
							deliveryIndex++;
							meisaiFlg = true;
							break;
						case BOTH_NONE:
							continueFlg = false;
							// 明細がある場合、ループ終了
							if (meisaiFlg) {
								continue;
							}
							// 明細が無い場合、空行作成
							break;
					}
					// 特定施設の設定
					if (specialInsPlan != null) {
						tmsTytenCd = specialInsPlan.getTmsTytenCd();
						tmsTytenName = specialInsPlan.getTmsTytenName();
						planTaiGaiFlgTok = specialInsPlan.getPlanTaiGaiFlgTok();
						planTaiGaiFlgRik = specialInsPlan.getPlanTaiGaiFlgRik();
						// 特定施設計画の計画立案区分が営業所案の場合
						if (specialInsPlan.getPlanType().equals(PlanType.OFFICE)) {
							plannedValueY = ConvertUtil.parseMoneyToThousandUnit(specialInsPlan.getPlannedValueB());
							// 担当者立案画面の場合
							if ((specialIndex < specialInsPlans.size())) {
								// 特定施設計画リストから営業所案の次行を取得
								SpecialInsPlanForVac specialInsPlan2 = specialInsPlans.get(specialIndex);
								// 取得した特定施設計画の計画立案区分が担当者立案の場合
								if (specialInsPlan2.getPlanType().equals(PlanType.MR) && specialInsPlan2.getProdCode().equals(prodCode)
									&& specialInsPlan2.getInsNo().equals(insNo) && specialInsPlan2.getTmsTytenCd().equals(tmsTytenCd)) {
									// 担当者立案計画のデータを特定施設計画に追加
									seqKey = specialInsPlan2.getSeqKey();
									plannedValueY2 = ConvertUtil.parseMoneyToThousandUnit(specialInsPlan2.getPlannedValueB());
									upDate = specialInsPlan2.getUpDate();
									upJgiName = specialInsPlan2.getUpJgiName();
									// 特定施設計画リストのカウンタを増やす
									specialIndex++;
								}
							}
						}
						// 担当者立案計画のみ登録されている場合
						else {
//							if (mrFlg) {
								// 担当者立案計画のデータを特定施設計画に追加
								seqKey = specialInsPlan.getSeqKey();
								plannedValueY2 = ConvertUtil.parseMoneyToThousandUnit(specialInsPlan.getPlannedValueB());
								upDate = specialInsPlan.getUpDate();
								upJgiName = specialInsPlan.getUpJgiName();
//							}
						}

						//施設計
						ssumY += getLongValue(plannedValueY);
						ssumY2 += getLongValue(plannedValueY2);
					}
					// 過去実績の設定
					if (deliveryResult != null) {
						tmsTytenCd = deliveryResult.getTmsTytenCd();
						tmsTytenName = deliveryResult.getTmsTytenMeiKj();
						monNnu0 = deliveryResult.getMonNnu();
						monNnu0.setPreFarAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getPreFarAdvancePeriod()));
						monNnu0.setFarAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getFarAdvancePeriod()));
						monNnu0.setAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getAdvancePeriod()));
						monNnu0.setCurrentPeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getCurrentPeriod()));
						planTaiGaiFlgTok = deliveryResult.getPlanTaiGaiFlgTok();
						planTaiGaiFlgRik = deliveryResult.getPlanTaiGaiFlgRik();
						//施設計
						ssumD = createSumArray(ssumD, monNnu0);
					}
					// DTO生成
					SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(insNo, insName, insClazz, plannedValueY, plannedValueY2, prodCode, prodName, prodType,
						tmsTytenCd, tmsTytenName, upDate, upJgiName, monNnu0, delFlg, exDistFlg, planTaiGaiFlgTok, planTaiGaiFlgRik);
					specialInsPlanProdListResultDto.add(resultDto);
				}
				//施設計
				MonNnu monNnuSsumD = createMonNnu(ssumD);
				SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(insNo, "sSum", insClazz, ssumY, ssumY2, prodCode, prodName, prodType, null, null,
					null, null, monNnuSsumD, delFlg, exDistFlg, false, false);
				specialInsPlanProdListResultDto.add(resultDto);
				hsumY += ssumY;
				hsumY2 += ssumY2;
				hsumD = createSumArray(hsumD, monNnuSsumD);
			}
			//品目計
			MonNnu monNnuHsumD = createMonNnu(hsumD);
			SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(null, "hSum", null, hsumY, hsumY2, prodCode, prodName, prodType, null, null, null,
				null, monNnuHsumD, null, null, false, false);
			specialInsPlanProdListResultDto.add(resultDto);
			msumY = MathUtil.add(msumY, hsumY);
			msumY2 = MathUtil.add(hsumY2, hsumY2);
			msumD = createSumArray(msumD, monNnuHsumD);
		}

		//計
		MonNnu monNnuMsumD = createMonNnu(msumD);
		SpecialInsPlanSearchForVacResultDto resultDto = new SpecialInsPlanSearchForVacResultDto(null, "mSum", null, msumY, msumY2, null, "ワクチン計", null, null, null, null, null,
			monNnuMsumD, null, null, false, false);
		specialInsPlanProdListResultDto.add(resultDto);
		return specialInsPlanProdListResultDto;

	}

	/**
	 * 実績の配列をモデルMonNnuの各プロパティに設定する
	 *
	 * @param value 計画値
	 * @return MonNnu
	 */
	private MonNnu createMonNnu(Long[] value) {
		MonNnu obj = new MonNnu();
		obj.setPreFarAdvancePeriod(value[0]);
		obj.setFarAdvancePeriod(value[1]);
		obj.setAdvancePeriod(value[2]);
		obj.setCurrentPeriod(value[3]);
		return obj;
	}

	/**
	 * 実績の配列とモデルMonNnuの各値を加算し、計算結果を返す。
	 *
	 * @param value 実績値配列
	 * @param value MonNnu
	 * @return 加算後の実績値配列
	 */
	private Long[] createSumArray(Long[] value, MonNnu obj) {
		value[0] += getLongValue(obj.getPreFarAdvancePeriod());
		value[1] += getLongValue(obj.getFarAdvancePeriod());
		value[2] += getLongValue(obj.getAdvancePeriod());
		value[3] += getLongValue(obj.getCurrentPeriod());
		return value;
	}

	/**
	 * 計画値を集計用にする
	 *
	 * @param value 計画値
	 * @return 計画値
	 */
	private Long getLongValue(Long value) {
		if (value != null) {
			return value;
		} else {
			return 0L;
		}
	}

	/**
	 * 配分除外かどうかを返す。
	 *
	 * @param insNo
	 * @param prodCode
	 * @return 配分除外の場合、TRUE
	 */
	private Boolean getExDistFlg(String insNo, String prodCode) {
		if (exceptDistInsList.contains(insNo)) {
			return true;
		}
		if (exceptDistProdList.containsKey(insNo)) {
			List<String> prodList = exceptDistProdList.get(insNo);
			if (prodList.contains(prodCode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 特定施設、過去実績を参照し、どれを追加するか判定する。
	 *
	 * @author khashimoto
	 */
	private AddCondition judgment(int specialIndex, int deliveryIndex, String prodCode, String insNo) {
		SpecialInsPlanForVac specialInsPlan = isSpecialAdd(specialIndex, prodCode, insNo);
		DeliveryResultForVac deliveryResult = isDeliveryAdd(deliveryIndex, prodCode, insNo);
		if (specialInsPlan == null && deliveryResult == null) {
			return AddCondition.BOTH_NONE;
		}
		if (specialInsPlan == null) {
			return AddCondition.DELI_ADD;
		}
		if (deliveryResult == null) {
			return AddCondition.SPECIAL_ADD;
		}
		String specialInsPlanTms = specialInsPlan.getTmsTytenCd();
		String deliveryResultTms = deliveryResult.getTmsTytenCd();
		if (specialInsPlanTms.equals(deliveryResultTms)) {
			return AddCondition.BOTH_ADD;
		}
		if (specialInsPlanTms.compareTo(deliveryResultTms) < 0) {
			return AddCondition.SPECIAL_ADD;
		} else {
			return AddCondition.DELI_ADD;
		}
	}

	private SpecialInsPlanForVac isSpecialAdd(int specialIndex, String prodCode, String insNo) {
		if (specialInsPlans.size() <= specialIndex) {
			return null;
		}
		SpecialInsPlanForVac specialInsPlan = specialInsPlans.get(specialIndex);
		if (specialInsPlan.getProdCode().equals(prodCode) && specialInsPlan.getInsNo().equals(insNo)) {
			return specialInsPlan;
		}
		return null;
	}

	private DeliveryResultForVac isDeliveryAdd(int deliveryIndex, String prodCode, String insNo) {
		if (deliveryResults.size() <= deliveryIndex) {
			return null;
		}
		DeliveryResultForVac deliveryResult = deliveryResults.get(deliveryIndex);
		if (deliveryResult.getProdCode().equals(prodCode) && deliveryResult.getInsNo().equals(insNo)) {
			return deliveryResult;
		}
		return null;
	}

	private enum AddCondition {
		BOTH_ADD, SPECIAL_ADD, DELI_ADD, BOTH_NONE;
	}

}
