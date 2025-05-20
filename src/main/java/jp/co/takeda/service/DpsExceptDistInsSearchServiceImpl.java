package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.ExceptDistInsDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.ExceptDistInsPlannedProdResultDto;
import jp.co.takeda.dto.ExceptDistInsResultDto;
import jp.co.takeda.dto.ExceptDistInsResultListDto;
import jp.co.takeda.dto.ExceptDistInsScDto;
import jp.co.takeda.dto.ExceptDistInsUpdateInitDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.DpsPlannedCtg;
import jp.co.takeda.model.ExceptDistIns;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.RegistType;
import jp.co.takeda.model.div.Sales;

/**
 * 配分除外施設の検索に関するサービス実装クラス
 *
 * @author siwamoto
 */
@Transactional
@Service("dpsExceptDistInsSearchService")
public class DpsExceptDistInsSearchServiceImpl implements DpsExceptDistInsSearchService {

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 配分除外施設DAO
	 */
	@Autowired(required = true)
	@Qualifier("exceptDistInsDao")
	protected ExceptDistInsDao exceptDistInsDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 組織情報取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	private DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 計画対象カテゴリ領域取得DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	public ExceptDistInsResultListDto searchExceptDistIns(ExceptDistInsScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg dpsPlannedCtg = new DpsPlannedCtg();
		if (scDto.getCategory() != null) {
			dpsPlannedCtg = dpsPlannedCtgDao.search(scDto.getCategory());
		}

		// ----------------------
		// 検索処理実行
		// ----------------------
		List<ExceptDistIns> searchResultList = null;
		try {
			searchResultList = exceptDistInsDao.searchList(ExceptDistInsDao.SORT_STRING_OYAKO, scDto.getSosCd3(), scDto.getSosCd4(), scDto.getJgiNo(), dpsPlannedCtg.getOyakoKb(),dpsPlannedCtg.getOyakoKb2());
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")));
		}

		// ----------------------
		// 検索処理実行
		// ----------------------
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　カテゴリーごとの品目リスト取得を一括取得に変更
//		List<PlannedProd> countMMPList = null;
//		List<PlannedProd> countSHIIREList = null;
//		List<PlannedProd> countONCList = null;
		List<PlannedProd> plannedProdList = null;
//		Integer countMMP    = 0;
//		Integer countSHIIRE = 0;
//		Integer countONC = 0;
//		HashMap<ProdCategory, Integer> countProdMstMap = new HashMap<ProdCategory, Integer>();
		HashMap<String, Integer> countProdMstMap = new HashMap<String, Integer>();
//
		try {
//			countMMPList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.MMP,  null);
//			countSHIIREList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.SHIIRE,  null);
//			countONCList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.ONC,  null);
//			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, null,  null);
			plannedProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, null, null,  null);

		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
//		countMMP    = countMMPList.size();
//		countSHIIRE = countSHIIREList.size();
//		countONC = countONCList.size();
		//カテゴリーごとの品目件数をmapに作成する
		for(PlannedProd plannedProd : plannedProdList){
			if(!countProdMstMap.containsKey(plannedProd.getCategory())){
				countProdMstMap.put(plannedProd.getCategory(), 1);
			} else {
				countProdMstMap.put(plannedProd.getCategory(), countProdMstMap.get(plannedProd.getCategory()) + 1);
			}
		}


//		List<ExceptDistInsResultDto> resultDto = convertExceptDistInsResultDto(searchResultList);
		List<ExceptDistInsResultDto> resultDto = convertExceptDistInsResultDto(searchResultList, countProdMstMap);
//		ExceptDistInsResultListDto resultList = new ExceptDistInsResultListDto(countMMP, countSHIIRE, countONC, resultDto);
		ExceptDistInsResultListDto resultList = new ExceptDistInsResultListDto(resultDto);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　カテゴリーごとの品目リスト取得を一括取得に変更
		return resultList;
	}

//	public List<ExceptDistInsPlannedProdResultDto> searchExceptDistInsProd(String sosCd,String category) throws LogicalException {
//
//		// ----------------------
//		// 検索処理実行
//		// ----------------------
//		return searchExceptDistInsProdExe(sosCd,category);
//	}

	public ExceptDistInsUpdateInitDto searchExceptDistInsCategoryMulti(String insNo, String dispJgiName, String sosCd, String[] category, List<DpsCCdMst> cdMstCategoryList, String insType) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 施設情報取得
		DpsInsMst insMst = null;
		try {
			insMst = insMstDAO.search(insNo);
		} catch (DataNotFoundException e) {
			final String errMsg = "施設情報の取得失敗";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// 配分除外施設取得
		ExceptDistIns exceptDistIns = null;
		try {
			exceptDistIns = exceptDistInsDao.searchByInsNo(insNo);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPS2031W")), e);
		}

		// 品目の一覧を取得
		//TODO:shi
		List<ExceptDistInsPlannedProdResultDto> prodList = searchExceptDistInsProdExeCategoryMulti(sosCd,category,cdMstCategoryList,exceptDistIns);

		// DTO生成して返す
		return new ExceptDistInsUpdateInitDto(sosCd, insMst, exceptDistIns, prodList, dispJgiName, insType);
	}

	// 施設コード、品目固定コードで配分除外施設かどうかを取得（施設指定の場合でも判定）
	public boolean isExceptDistIns(String insNo, String prodCode) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		return exceptDistInsDao.isExceptDistIns(insNo, prodCode);
	}

	/**
	 * exceptDistInsオブジェクトリスト⇒ExceptDistInsResultDtoオブジェクトリスト 変換処理
	 *
	 * @param exceptDistInsList 変換元exceptDistInsオブジェクトのリスト
	 * @return ExceptDistInsResultDtoオブジェクトリスト
	 */
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理をServiceTool.javaからここに移動＆修正
//	protected List<ExceptDistInsResultDto> convertExceptDistInsResultDto(List<ExceptDistIns> exceptDistInsList) {
//	protected List<ExceptDistInsResultDto> convertExceptDistInsResultDto(List<ExceptDistIns> exceptDistInsList, HashMap<ProdCategory, Integer> countProdMstMap) {
	protected List<ExceptDistInsResultDto> convertExceptDistInsResultDto(List<ExceptDistIns> exceptDistInsList, HashMap<String, Integer> countProdMstMap) {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理はServiceTool.javaからここに移動＆修正
		List<ExceptDistInsResultDto> exceptDistInsResultDtoList = new ArrayList<ExceptDistInsResultDto>();

		// 変換元がNullの場合は配列0のリストを返却
		if (exceptDistInsList == null || exceptDistInsList.isEmpty()) {
			return exceptDistInsResultDtoList;
		}

		for (ExceptDistIns exceptDistIns : exceptDistInsList) {

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理をServiceTool.javaからここに移動＆修正
//			exceptDistInsResultDtoList.add(convertExceptDistInsResultDto(exceptDistIns));
			exceptDistInsResultDtoList.add(convertExceptDistInsResultDto(exceptDistIns, countProdMstMap));
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理をServiceTool.javaからここに移動＆修正
		}

		return exceptDistInsResultDtoList;
	}

	/**
	 * exceptDistInsオブジェクト⇒SpecialInsPlanResultDtoオブジェクト 変換処理
	 *
	 *
	 * @param exceptDistIns 変換元exceptDistInsオブジェクト
	 * @return ExceptDistInsResultDtoオブジェクト
	 */
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理をServiceTool.javaからここに移動＆修正
//	protected ExceptDistInsResultDto convertExceptDistInsResultDto(ExceptDistIns exceptDistIns) {
//	protected ExceptDistInsResultDto convertExceptDistInsResultDto(ExceptDistIns exceptDistIns, HashMap<ProdCategory, Integer> countProdMstMap) {
	protected ExceptDistInsResultDto convertExceptDistInsResultDto(ExceptDistIns exceptDistIns, HashMap<String, Integer> countProdMstMap) {
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理をServiceTool.javaからここに移動＆修正
		// 施設名
		final String insName = exceptDistIns.getInsAbbrName();

		// 対象
		final String insType = exceptDistIns.getHoInsType().name();

		// 施設分類
		InsClass insClass = exceptDistIns.getInsClass();
		OldInsrFlg oldInsFlg = exceptDistIns.getOldInsrFlg();
		final String insClazz = InsClass.getInsClassName(insClass, oldInsFlg);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		// 担当者
		final Integer jgiNo = exceptDistIns.getJgiNo();
		final String jgiName = exceptDistIns.getJgiName();
//		final String dispJgiName = exceptDistIns.getDispJgiName();
		StringBuilder sbTanto = new StringBuilder();
		for(JgiMst dispJgi : exceptDistIns.getDispJgi()){
			if(sbTanto.length() != 0){
				sbTanto.append("、");
			}
			sbTanto.append(dispJgi.getJgiName()).append("（").append(dispJgi.getShokushuName()).append("）");
		}
		final String dispJgiName = sbTanto.toString();
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

		// 最終更新者
		final String upJgiName = exceptDistIns.getUpJgiName();

		// 最終更新日
		final Date upDate = exceptDistIns.getUpDate();

		// 施設コード
		final String insNo = exceptDistIns.getInsNo();
		final String exceptDistInsName = exceptDistIns.getInsAbbrName();
		final List<ExceptProd> exceptDistInsProd = exceptDistIns.getExceptProd();

		// 登録区分
		final RegistType registType = exceptDistIns.getRegistType();

		// 施設の試算除外フラグ・配分除外フラグ
		Boolean estimationFlg = exceptDistIns.getEstimationFlg();
		Boolean exceptFlg =exceptDistIns.getExceptFlg();

		// 品目
		String dispExceptDistInsProd = "";
		String dispExceptEstiInsProd = "";

		//  試算除外フラグまたは配分除外フラグのどちらかが立っている場合に以下の処理を行う。
		// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理をServiceTool.javaからここに移動＆修正
		//配分除外品目名
		//　　カテゴリーごとに除外品目数を振り分け
//		HashMap<ProdCategory, List<ExceptProd>> exceptProdListMap = new HashMap<ProdCategory, List<ExceptProd>>();
		HashMap<String, List<ExceptProd>> exceptProdListMap = new HashMap<String, List<ExceptProd>>();
		for(ExceptProd prod : exceptDistIns.getExceptProd()){
			if(!exceptProdListMap.containsKey(prod.getCategory())){
				exceptProdListMap.put(prod.getCategory(), new ArrayList<ExceptProd>());
			}
			exceptProdListMap.get(prod.getCategory()).add(prod);
		}
		//　　配分除外品目リストをカンマ区切り文字列にする
//		StringBuilder sbProd = new StringBuilder();
		StringBuilder sbProdEsti = new StringBuilder();
		StringBuilder sbProdDist = new StringBuilder();
//		HashMap<ProdCategory, Boolean> categoryNameFlgMap = new HashMap<ProdCategory, Boolean>(); //"品目カテゴリ名"を出力したかどうかのフラグマップ

		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.CAT.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.CAT.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		//　　getCategoryList(0)のカテゴリー順に品目出力する
		//for(ProdCategory category : ProdCategory.getCategoryList()){
		for(DpsCCdMst dpsCCdMst : searchList){
			String category = dpsCCdMst.getDataCd();
			if(!exceptProdListMap.containsKey(category))
				continue;

			List<ExceptProd> list = exceptProdListMap.get(category);
			Integer estiCnt = 0;
			Integer distCnt = 0;
			for(ExceptProd listRow :list) {
				if(listRow.getStrEstimationFlg().equals("1")) {
					estiCnt = estiCnt + 1;
				}
				if(listRow.getStrExceptFlg().equals("1")) {
					distCnt = distCnt + 1;
				}
			}
			//除外品目数がマスタの品目数と同じ場合、品目名の変わりに"品目カテゴリ名"を出力する
			if(list.size() == countProdMstMap.get(category).intValue()
					&& (list.size() == estiCnt || list.size() == distCnt)){

				if(list.size() == estiCnt ) {
					if(sbProdEsti.length() > 0) {
						sbProdEsti.append("，");
					}
					sbProdEsti.append(dpsCCdMst.getDataName());
				}
				if(list.size() == distCnt) {
					if(sbProdDist.length() > 0) {
						sbProdDist.append("，");
					}
					sbProdDist.append(dpsCCdMst.getDataName());
				}
				//除外品目数がマスタの品目数と違う場合、個別の品目名を出力
				for(ExceptProd prod : list){
					if (list.size() != estiCnt) {
						if (prod.getStrEstimationFlg().equals("1")) {
							if(sbProdEsti.length() > 0)
								sbProdEsti.append("，");

							if(StringUtils.isNotBlank(prod.getProdAbbrName())){
								sbProdEsti.append(prod.getProdAbbrName());
							}else{
								sbProdEsti.append(prod.getProdName());
							}
						}
					}
					if(list.size() != distCnt) {
						if (prod.getStrExceptFlg().equals("1")) {
							if(sbProdDist.length() > 0)
								sbProdDist.append("，");

							if(StringUtils.isNotBlank(prod.getProdAbbrName())){
								sbProdDist.append(prod.getProdAbbrName());
							}else{
								sbProdDist.append(prod.getProdName());
							}
						}
					}
				}
//				if(sbProd.length() > 0) {
//					sbProd.append("，");
//				}
////				sbProd.append(category.getProdCategoryName());
//				sbProd.append(dpsCCdMst.getDataName());

			//除外品目数がマスタの品目数と違う場合、個別の品目名を出力
			} else {
				for(ExceptProd prod : list){
					if (prod.getStrEstimationFlg().equals("1")) {
						if(sbProdEsti.length() > 0)
							sbProdEsti.append("，");

						if(StringUtils.isNotBlank(prod.getProdAbbrName())){
							sbProdEsti.append(prod.getProdAbbrName());
						}else{
							sbProdEsti.append(prod.getProdName());
						}
					}
					if (prod.getStrExceptFlg().equals("1")) {
						if(sbProdDist.length() > 0)
							sbProdDist.append("，");

						if(StringUtils.isNotBlank(prod.getProdAbbrName())){
							sbProdDist.append(prod.getProdAbbrName());
						}else{
							sbProdDist.append(prod.getProdName());
						}
					}
//					if(sbProd.length() > 0)
//						sbProd.append("，");
//
//					if(StringUtils.isNotBlank(prod.getProdAbbrName())){
//						sbProd.append(prod.getProdAbbrName());
//					}else{
//						sbProd.append(prod.getProdName());
//					}
				}
			}
		}
		dispExceptEstiInsProd = sbProdEsti.toString();
		dispExceptDistInsProd = sbProdDist.toString();
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理をServiceTool.javaからここに移動＆修正

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		// オブジェクト生成
//		return new ExceptDistInsResultDto(insName, insType, insClazz, exceptDistInsName, jgiNo, jgiName, upJgiName, exceptDistInsProd, upDate, insNo, registType, dispJgiName);
		return new ExceptDistInsResultDto(insName, insType, insClazz, exceptDistInsName, jgiNo, jgiName, upJgiName, exceptDistInsProd,
				dispExceptDistInsProd, dispExceptEstiInsProd, upDate, insNo, registType, dispJgiName, estimationFlg, exceptFlg);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	}

	/**
	 * 計画対象品目の（MMP・仕入一般）の検索結果DTOを取得する。
	 *
	 * @return 計画対象品目DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	public List<ExceptDistInsPlannedProdResultDto> searchExceptDistInsProdExe(String sosCd, String[] categoryList, List<DpsCCdMst> cdMstCategoryList) throws LogicalException {

		// 組織コードから組織情報取得
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象組織がない：" + sosCd;
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		// ----------------------
		// 検索処理実行
		// ----------------------
		List<PlannedProd> plannedPlodList = new ArrayList<PlannedProd>();

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		//組織に属するCategoryごとの品目リストを取得
		try {
			// -----------------------------
			// 売上所属の判定
			// -----------------------------
			if (categoryList != null) {
				Sales sales = null;
				for (String category : categoryList) {
					if(category == null) {
						sales = null;
					}else {
						if(category.equals(vaccineCode)) {
							sales = Sales.VACCHIN;
						}else{
							sales = Sales.IYAKU;
						}
					}
					plannedPlodList.addAll(plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category, null));
				}
			}
		} catch (DataNotFoundException e) {
		}

		// 表示対象の品目がない
		if (plannedPlodList.size() == 0) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")));
		}

		//配分除外施設から試算除外フラグと配分除外フラグを取得
		String strEstimationFlg = "0";
		String strExceptFlg = "0";
		// 選択可能の品目を設定する
		List<ExceptDistInsPlannedProdResultDto> resultDtoList = new ArrayList<ExceptDistInsPlannedProdResultDto>();
		for (PlannedProd planedPlod : plannedPlodList) {
			final String prodCode = planedPlod.getProdCode();
			final String prodName = planedPlod.getProdName();
			final String category = planedPlod.getCategory();
			String categoryName = null;
			for (DpsCCdMst categoryRow : cdMstCategoryList) {
				if (planedPlod.getCategory().equals(categoryRow.getDataCd())) {
					categoryName = categoryRow.getDataName();
					strEstimationFlg = "0";
					strExceptFlg = "0";
					break;
				}
			}

			ExceptDistInsPlannedProdResultDto resultDto = new ExceptDistInsPlannedProdResultDto(prodCode, prodName, category, categoryName,strEstimationFlg,strExceptFlg);

			resultDtoList.add(resultDto);
		}
		return resultDtoList;
	}

	/**
	 * 計画対象品目の（MMP・仕入一般）の検索結果DTOを取得する。
	 *
	 * @return 計画対象品目DTOのリスト
	 * @throws LogicalException 検索結果が0件だった場合
	 */
	protected List<ExceptDistInsPlannedProdResultDto> searchExceptDistInsProdExeCategoryMulti(String sosCd, String[] categoryList, List<DpsCCdMst> cdMstCategoryList, ExceptDistIns exceptDistIns) throws LogicalException {
		// ----------------------
		// 検索処理実行
		// ----------------------
		List<PlannedProd> plannedPlodList = new ArrayList<PlannedProd>();

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		//組織に属するCategoryごとの品目リストを取得
		try {
			if(categoryList != null){
				// -----------------------------
				// 売上所属の判定
				// -----------------------------
				Sales sales = null;
				for (String category : categoryList) {
					if(category == null) {
						sales = null;
					}else {
						if(category.equals(vaccineCode)) {
							sales = Sales.VACCHIN;
						}else{
							sales = Sales.IYAKU;
						}
					}
					plannedPlodList.addAll(plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category,  null));
				}
			}
		} catch (DataNotFoundException e) {
		}

		// 表示対象の品目がない
		if (plannedPlodList.size() == 0) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")));
		}

		List<ExceptDistInsPlannedProdResultDto> resultDtoList = new ArrayList<ExceptDistInsPlannedProdResultDto>();
		for (PlannedProd planedPlod : plannedPlodList) {
			final String prodCode = planedPlod.getProdCode();
			final String prodName = planedPlod.getProdName();
			final String category = planedPlod.getCategory();
			//汎用マスタからカテゴリー名称を取得
			String categoryName = null;
			for (DpsCCdMst categoryRow : cdMstCategoryList) {
				if (planedPlod.getCategory().equals(categoryRow.getDataCd())) {
					categoryName = categoryRow.getDataName();
					break;
				}
			}
			//配分除外施設から試算除外フラグと配分除外フラグを取得
			String strEstimationFlg = "0";
			String strExceptFlg = "0";
			if(exceptDistIns.getExceptProd() != null) {
				for (ExceptProd exceptProd : exceptDistIns.getExceptProd()) {
					if(prodCode.equals(exceptProd.getProdCode())) {
						strEstimationFlg = exceptProd.getStrEstimationFlg();
						strExceptFlg = exceptProd.getStrExceptFlg();
						break;
					}
				}
			}
			ExceptDistInsPlannedProdResultDto resultDto = new ExceptDistInsPlannedProdResultDto(prodCode, prodName, category, categoryName, strEstimationFlg, strExceptFlg);
			resultDtoList.add(resultDto);
		}
		return resultDtoList;
	}
}
