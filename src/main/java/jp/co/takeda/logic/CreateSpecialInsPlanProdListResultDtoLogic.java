package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dao.ProdInsInfoKanriDao;
import jp.co.takeda.dto.SpecialInsPlanProdListResultDto;
import jp.co.takeda.model.DeliveryResult;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.ProdInsInfoKanri;
import jp.co.takeda.model.SpecialInsPlan;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.PlanType;
import jp.co.takeda.model.div.ProdInsInfoScanDispKbn;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

/**
 * 特定施設個別計画立案画面（担当者案）の検索結果用DTO生成ロジック
 *
 * @author khashimoto
 */
public class CreateSpecialInsPlanProdListResultDtoLogic {

	/** 品目リスト */
	private final List<PlannedProd> plannedProdList;

	/** 施設情報リスト */
	private final List<DpsInsMst> insMstList;

	/** 特定施設個別計画リスト */
	private final List<SpecialInsPlan> specialInsPlans;

	/** 過去実績リスト */
	private final List<DeliveryResult> deliveryResults;

	/** （担当者案）を返すか判断するフラグ */
	private final boolean mrFlg;

	/** 配分除外施設リスト */
	private final List<String> exceptDistInsList;

	/** 配分除外品目リスト */
	private final Map<String, List<String>> exceptDistProdList;

	/** カテゴリ */;
	private final String prodCategory;

	/** 品目施設情報Dao */
	private final ProdInsInfoKanriDao prodInsInfoKanriDao;

	/** カテゴリ・品目検索サービス */
	private final DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param plannedProdList
	 * @param insMstList
	 * @param specialInsPlans
	 * @param deliveryResults
	 * @param mrFlg
	 * @param exceptDistInsList
	 * @param exceptDistProdList
	 * @param prodCategory
	 * @param prodInsInfoKanriDao
	 * @param dpsCodeMasterSearchService
	 */
	public CreateSpecialInsPlanProdListResultDtoLogic(List<PlannedProd> plannedProdList, List<DpsInsMst> insMstList, List<SpecialInsPlan> specialInsPlans,
		List<DeliveryResult> deliveryResults, boolean mrFlg, List<String> exceptDistInsList, Map<String, List<String>> exceptDistProdList, String prodCategory,
		ProdInsInfoKanriDao prodInsInfoKanriDao, DpsCodeMasterSearchService dpsCodeMasterSearchService) {
		this.plannedProdList = plannedProdList;
		this.insMstList = insMstList;
		this.specialInsPlans = specialInsPlans;
		this.deliveryResults = deliveryResults;
		this.mrFlg = mrFlg;
		this.exceptDistInsList = exceptDistInsList;
		this.exceptDistProdList = exceptDistProdList;
		this.prodCategory = prodCategory;
		this.prodInsInfoKanriDao = prodInsInfoKanriDao;
		this.dpsCodeMasterSearchService = dpsCodeMasterSearchService;
	}

	/**
	 * SpecialInsPlanProdListResultDtoオブジェクトリスト 変換処理
	 *
	 * @return SpecialInsPlanProdListResultDtoオブジェクトリスト
	 */
	public List<SpecialInsPlanProdListResultDto> convertSpecialInsPlanProdListResultDto() {
		List<SpecialInsPlanProdListResultDto> resultList = new ArrayList<SpecialInsPlanProdListResultDto>();

		// -----------------------------------
		// 準備
		// -----------------------------------

		// 特定施設インデックス
		int specialIndex = 0;
		// 過去実績インデックス
		int deliveryIndex = 0;
		// 全品目サマリー(営業所案/担当者案/納入実績
		Long msumY = 0L;
		Long msumY2 = 0L;
		Long[] msumD = { 0L, 0L, 0L, 0L };

		// -----------------------------------
		// 品目毎に行を作成
		// -----------------------------------
		//対象品目分繰り返す
		for (PlannedProd plannedProd : plannedProdList) {

			// ----------------------
			// 初期化
			// ----------------------
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

//			// ----------------------
//			// 仕入(一般)品計作成
//			// ----------------------
//			//仕入(一般)品計
//			GetGeneralFlg logic = new GetGeneralFlg(plannedProd.getGroupCode(), prodCategory, dpsCodeMasterSearchService);
//			boolean isGeneral = logic.generalFlg();
//			//最初の麻薬品だったら、一般計行を追加する
//			if ((!isGeneral) && generalFlg) {
//				MonNnu monNnuMsumD = createMonNnu(msumD);
//				SpecialInsPlanProdListResultDto resultDto = new SpecialInsPlanProdListResultDto(null, null, null, "mSum", null, msumY, msumY2, null, "仕入品(一般)計",
//					shiireCd, null, null, null, null, null, monNnuMsumD, null, null, false, false);
//				resultList.add(resultDto);
//				generalFlg = false;
//			}

			// ----------------------
			// 施設特約店別明細行
			// ----------------------

			// 施設分繰り返す
			for (DpsInsMst insMst : insMstList) {
				// 施設情報初期化
				//施設分類
				String insClazz = InsClass.getInsClassName(insMst.getInsClass(), insMst.getOldInsrFlg());
				//対象区分
				String insType = insMst.getHoInsType().getDbValue();
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
				ProdInsInfoKanri prodIns = new ProdInsInfoKanri();
				try {
					prodIns = prodInsInfoKanriDao.searchByInsNo(prodCode, insNo);
				} catch (DataNotFoundException e) {
				}

				// 表示対象かどうか判定
				boolean isNotDisp = ProdInsInfoScanDispKbn.NOT == prodIns.getProdInsInfoScanDispKbn();

				// 明細有無フラグ
				boolean meisaiFlg = false;
				boolean continueFlg = true;
				List<SpecialInsPlanProdListResultDto> resultTmsList = new ArrayList<SpecialInsPlanProdListResultDto>();
				while (continueFlg) {
					// 特定施設個別計画情報初期化
					Long seqKey = null;
					Long plannedValueY = null;
					Long plannedValueY2 = null;
					String tmsTytenCd = null;
					String tmsTytenName = null;
					Date upDate = null;
					String upJgiName = null;
					MonNnu monNnu0 = null;
					boolean planTaiGaiFlgTok = false;
					boolean planTaiGaiFlgRik = false;

					//品目と施設が一致する場合、特定施設個別計画・実績を設定する
					AddCondition addCondition = judgment(specialIndex, deliveryIndex, prodCode, insNo);
					SpecialInsPlan specialInsPlan = null;
					DeliveryResult deliveryResult = null;
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
							plannedValueY = ConvertUtil.parseMoneyToThousandUnit(specialInsPlan.getPlannedValueY());
							// 担当者立案画面の場合
							if (mrFlg) {
								if ((specialIndex < specialInsPlans.size())) {
									// 特定施設計画リストから営業所案の次行を取得
									SpecialInsPlan specialInsPlan2 = specialInsPlans.get(specialIndex);
									// 取得した特定施設計画の計画立案区分が担当者立案の場合
									if (specialInsPlan2.getPlanType().equals(PlanType.MR) && specialInsPlan2.getProdCode().equals(prodCode)
										&& specialInsPlan2.getInsNo().equals(insNo) && specialInsPlan2.getTmsTytenCd().equals(tmsTytenCd)) {
										// 担当者立案計画のデータを特定施設計画に追加
										seqKey = specialInsPlan2.getSeqKey();
										plannedValueY2 = ConvertUtil.parseMoneyToThousandUnit(specialInsPlan2.getPlannedValueY());
										upDate = specialInsPlan2.getUpDate();
										upJgiName = specialInsPlan2.getUpJgiName();
										// 特定施設計画リストのカウンタを増やす
										specialIndex++;
									}
								}
							}
							// 営業所案画面の場合
							else {
								// 営業所案計画のデータを特定施設計画に追加
								seqKey = specialInsPlan.getSeqKey();
								upDate = null;
								upJgiName = null;
							}
						}
						// 担当者立案計画のみ登録されている場合
						else {
							if (mrFlg) {
								// 担当者立案計画のデータを特定施設計画に追加
								seqKey = specialInsPlan.getSeqKey();
								plannedValueY2 = ConvertUtil.parseMoneyToThousandUnit(specialInsPlan.getPlannedValueY());
								upDate = specialInsPlan.getUpDate();
								upJgiName = specialInsPlan.getUpJgiName();
							}
						}
						// 表示対象外施設の場合でも、計画値があれば表示対象
						if(plannedValueY != null || plannedValueY2 != null){
							isNotDisp = false;
						}
						//施設計
						ssumY += getLongValue(plannedValueY);
						ssumY2 += getLongValue(plannedValueY2);
					}
					// 過去実績の設定
					if (deliveryResult != null) {
						tmsTytenCd = deliveryResult.getTmsTytenCd();
						tmsTytenName = deliveryResult.getTmsTytenMeiKj();
						planTaiGaiFlgTok = deliveryResult.getPlanTaiGaiFlgTok();
						planTaiGaiFlgRik = deliveryResult.getPlanTaiGaiFlgRik();
						monNnu0 = deliveryResult.getMonNnu();
						monNnu0.setPreFarAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getPreFarAdvancePeriod()));
						monNnu0.setFarAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getFarAdvancePeriod()));
						monNnu0.setAdvancePeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getAdvancePeriod()));
						monNnu0.setCurrentPeriod(ConvertUtil.parseMoneyToThousandUnit(monNnu0.getCurrentPeriod()));
						//施設計
						ssumD = createSumArray(ssumD, monNnu0);
					}
					// DTO生成
					SpecialInsPlanProdListResultDto resultDto = new SpecialInsPlanProdListResultDto(seqKey, insNo, insName, insType, insClazz, plannedValueY, plannedValueY2,
						prodCode, prodName, prodCategory, prodType, tmsTytenCd, tmsTytenName, upDate, upJgiName, monNnu0, delFlg, exDistFlg, planTaiGaiFlgTok, planTaiGaiFlgRik,
						prodIns);
					resultTmsList.add(resultDto);
				}
				if(isNotDisp){
					// 表示対象外施設の場合、追加しない
					continue;
				}
				//施設計
				MonNnu monNnuSsumD = createMonNnu(ssumD);
				SpecialInsPlanProdListResultDto resultDto = new SpecialInsPlanProdListResultDto(null, insNo, insName, "sSum", insClazz, ssumY, ssumY2, prodCode, prodName,
					prodCategory, prodType, null, null, null, null, monNnuSsumD, delFlg, exDistFlg, false, false, prodIns);
				resultList.addAll(resultTmsList);
				resultList.add(resultDto);
				hsumY += ssumY;
				hsumY2 += ssumY2;
				hsumD = createSumArray(hsumD, monNnuSsumD);
			}
			//品目計
			MonNnu monNnuHsumD = createMonNnu(hsumD);
			SpecialInsPlanProdListResultDto resultDto = new SpecialInsPlanProdListResultDto(null, null, null, "hSum", null, hsumY, hsumY2, prodCode, prodName, prodCategory,
				prodType, null, null, null, null, monNnuHsumD, null, null, false, false);
			resultList.add(resultDto);
			msumY = MathUtil.add(msumY, hsumY);
			msumY2 = MathUtil.add(hsumY2, hsumY2);
			msumD = createSumArray(msumD, monNnuHsumD);
		}

		//カテゴリ計
		String sumName = dpsCodeMasterSearchService.searchDataName(CodeMaster.CAT.getDbValue(), prodCategory) + "計";
		//switch (prodCategory) {
		//	case MMP:
		//		sumName = "MMP品計";
		//		break;
		//	case SHIIRE:
		//		sumName = "仕入品(一般・麻薬)計";
		//		break;
		//}
		MonNnu monNnuMsumD = createMonNnu(msumD);
		SpecialInsPlanProdListResultDto resultDto = new SpecialInsPlanProdListResultDto(null, null, null, "mSum", null, msumY, msumY2, null, sumName, prodCategory, null, null,
			null, null, null, monNnuMsumD, null, null, false, false);
		resultList.add(resultDto);
		return resultList;

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
		SpecialInsPlan specialInsPlan = isSpecialAdd(specialIndex, prodCode, insNo);
		DeliveryResult deliveryResult = isDeliveryAdd(deliveryIndex, prodCode, insNo);
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

	private SpecialInsPlan isSpecialAdd(int specialIndex, String prodCode, String insNo) {
		if (specialInsPlans.size() <= specialIndex) {
			return null;
		}
		SpecialInsPlan specialInsPlan = specialInsPlans.get(specialIndex);
		if (specialInsPlan.getProdCode().equals(prodCode) && specialInsPlan.getInsNo().equals(insNo)) {
			return specialInsPlan;
		}
		return null;
	}

	private DeliveryResult isDeliveryAdd(int deliveryIndex, String prodCode, String insNo) {
		if (deliveryResults.size() <= deliveryIndex) {
			return null;
		}
		DeliveryResult deliveryResult = deliveryResults.get(deliveryIndex);
		if (deliveryResult.getProdCode().equals(prodCode) && deliveryResult.getInsNo().equals(insNo)) {
			return deliveryResult;
		}
		return null;
	}

	private enum AddCondition {
		BOTH_ADD, SPECIAL_ADD, DELI_ADD, BOTH_NONE;
	}

}
