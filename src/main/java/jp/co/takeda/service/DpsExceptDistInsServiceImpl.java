package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.ExceptDistInsDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.PlannedProdDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SpecialInsPlanDao;
import jp.co.takeda.dto.ExceptDistInsDeleteDto;
import jp.co.takeda.dto.ExceptDistInsEntryDto;
import jp.co.takeda.dto.ExceptDistInsEntryProdListDto;
import jp.co.takeda.dto.ExceptDistInsUpdateDto;
import jp.co.takeda.dto.InsMrDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.div.InsDocStatusForCheck;
import jp.co.takeda.logic.div.InsWsStatusForCheck;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.ExceptDistIns;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.util.ExceptionUtil;

/**
 * 配分除外施設の登録・更新に関するサービス実装クラス
 *
 * @author siwamoto
 */
@Transactional
@Service("dpsExceptDistInsService")
public class DpsExceptDistInsServiceImpl implements DpsExceptDistInsService {

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

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
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("plannedProdDAO")
	protected PlannedProdDAO plannedProdDAO;

	/**
	 * 配分除外施設DAO
	 */
	@Autowired(required = true)
	@Qualifier("exceptDistInsDao")
	protected ExceptDistInsDao exceptDistInsDao;

	/**
	 * 特定施設個別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("specialInsPlanDao")
	protected SpecialInsPlanDao specialInsPlanDao;

	/**
	 * 担当者別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 施設医師別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsDocStatusCheckService")
	protected DpsInsDocStatusCheckService dpsInsDocStatusCheckService;

	/**
	 * 施設特約店別計画ステータスチェックサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsInsWsStatusCheckService")
	protected DpsInsWsStatusCheckService dpsInsWsStatusCheckService;

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	private DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	public void deleteExceptDistIns(String sosCd3, List<ExceptDistInsDeleteDto> deleteDtoList) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (deleteDtoList == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		//-----------------------
		// 施設・従業員リスト生成
		//-----------------------
		// 施設情報リスト
		List<DpsInsMst> insMstList = new ArrayList<DpsInsMst>();
		// 従業員情報リスト
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// チェック済み組織コード
		Set<String> sosCodeSet = new HashSet<String>();
		// チェック済み従業員格納リスト
		Set<Integer> jgiNoSet = new HashSet<Integer>();
		for (ExceptDistInsDeleteDto insMrDto : deleteDtoList) {
			try {
				// 施設・従業員の存在チェック、リストに追加
				DpsInsMst insMst = insMstDAO.search(insMrDto.getInsNo());
				insMstList.add(insMst);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//				// チェック済みの従業員以外の場合、リストに追加
//				if (insMst.getComJgiNo() != null && !jgiNoSet.contains(insMst.getComJgiNo())) {
//					JgiMst jgiMst = jgiMstDAO.search(insMst.getComJgiNo());
//					jgiMstList.add(jgiMst);
//					sosCodeSet.add(jgiMst.getSosCd3());
//					jgiNoSet.add(jgiMst.getJgiNo());
//				}
//				if (insMst.getCvmJgiNo() != null && !jgiNoSet.contains(insMst.getCvmJgiNo())) {
//					JgiMst jgiMst = jgiMstDAO.search(insMst.getCvmJgiNo());
//					jgiMstList.add(jgiMst);
//					sosCodeSet.add(jgiMst.getSosCd3());
//					jgiNoSet.add(jgiMst.getJgiNo());
//				}
//				if (insMst.getRsJgiNo() != null && !jgiNoSet.contains(insMst.getRsJgiNo())) {
//					JgiMst jgiMst = jgiMstDAO.search(insMst.getRsJgiNo());
//					jgiMstList.add(jgiMst);
//					sosCodeSet.add(jgiMst.getSosCd3());
//					jgiNoSet.add(jgiMst.getJgiNo());
//				}
//				if (insMst.getOncJgiNo() != null && !jgiNoSet.contains(insMst.getOncJgiNo())) {
//					JgiMst jgiMst = jgiMstDAO.search(insMst.getOncJgiNo());
//					jgiMstList.add(jgiMst);
//					sosCodeSet.add(jgiMst.getSosCd3());
//					jgiNoSet.add(jgiMst.getJgiNo());
//				}

//				//担当者の重複を除いてjgiMstListに入れていく
//				for(JgiMst jgi : insMst.getTantoList()){
//					if (!jgiNoSet.contains(jgi.getJgiNo())){
//						JgiMst jgiMst = jgiMstDAO.search(jgi.getJgiNo());
//						jgiMstList.add(jgiMst);
//						sosCodeSet.add(jgiMst.getSosCd3());
//						jgiNoSet.add(jgiMst.getJgiNo());
//					}
//				}
				JgiMst jgiMst = jgiMstDAO.search(insMst.getJgiNo());
				jgiMstList.add(jgiMst);
				sosCodeSet.add(jgiMst.getSosCd3());
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
			} catch (DataNotFoundException e) {
				final String errMsg = "施設・従業員情報が存在しない";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}

		// ------------------------
		// ステータスチェック
		// ------------------------
		for (String sosCd : sosCodeSet) {
	        checkMrStatus(sosCd, "DPS3231E");
		}
		checkInsWsStatus(jgiMstList, "DPS3232E");
		checkInsDocStatus(jgiMstList, "DPS3232E");

		// ----------------------
		// 検索処理実行
		// ----------------------
		for (ExceptDistInsDeleteDto deleteDto : deleteDtoList) {
			final String insNo = deleteDto.getInsNo();
			if (StringUtils.isEmpty(insNo)) {
				final String errMsg = "施設コードがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			final Date upDate = deleteDto.getUpDate();
			if (upDate == null) {
				final String errMsg = "最終更新日時がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
//			exceptDistInsDao.update(insNo, upDate, null);
			exceptDistInsDao.delete(insNo, upDate, null);
		}
	}

	public void entryExceptDistIns(ExceptDistInsEntryDto entryDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (entryDto == null) {
			final String errMsg = "試算・配分除外施設の登録用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (entryDto.getSosCd3() == null) {
			final String errMsg = "試算・配分除外施設の組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (entryDto.getInsMrList() == null || entryDto.getInsMrList().size() == 0) {
			final String errMsg = "試算・配分除外施設の施設/従業員のリストがnullまたは0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織コード(営業所)
		String sosCd3 = entryDto.getSosCd3();
		// 施設/従業員情報リスト
		List<InsMrDto> insMrList = entryDto.getInsMrList();
//		// 品目固定コードリスト
//		List<String> prodCodeList = entryDto.getProdCodeList();
		// 除外選択された品目固定コードリスト
		List<ExceptProd> exceptProdList = entryDto.getResultDtoList();
		//指定方法フラグ(配分除外施設：false、配分除外施設品目：true)
		boolean exceptProdSetFlg = true;
//		if (exceptProdList != null && exceptProdList.size() != 0) {
		if (exceptProdList == null) {
			exceptProdSetFlg = false;
		}

		//-------------------
		// 組織の存在チェック
		//-------------------
		try {
			sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "組織情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		//---------------------------------------------
		// 配分除外品目リスト・計画対象品目リストを生成
		//---------------------------------------------
//		ExceptDistInsEntryProdListDto prodListDto = createProdListDto(prodCodeList);
//		// 配分除外品目リスト
//		List<ExceptProd> exceptProdList = prodListDto.getExceptProdList();
//		// 計画対象品目リスト
//		List<PlannedProd> plannedProdList = prodListDto.getPlannedProdList();
//		// 配分除外施設フラグ(配分除外施設：true、配分除外施設品目：false)
//		boolean distInsFlg = prodListDto.getDistInsFlg();

		//-----------------------
		// 施設・従業員リスト生成
		//-----------------------
		// 施設情報リスト
		List<DpsInsMst> insMstList = new ArrayList<DpsInsMst>();
		// 従業員情報リスト
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// チェック済み組織コード
		Set<String> sosCodeSet = new HashSet<String>();
		// チェック済み従業員格納リスト
		Set<Integer> jgiNoSet = new HashSet<Integer>();
		// エラーメッセージ格納リスト
		List<MessageKey> messageList = new ArrayList<MessageKey>();
		MessageKey messageKey = null;
		for (InsMrDto insMrDto : insMrList) {
			try {
				// 施設・従業員の存在チェック、リストに追加
				DpsInsMst insMst = insMstDAO.search(insMrDto.getInsNo());
				insMstList.add(insMst);


//				//担当者の重複を除いてjgiMstListに入れていく
//				for(JgiMst jgi : insMst.getTantoList()){
//					if (!jgiNoSet.contains(jgi.getJgiNo())){
//						JgiMst jgiMst = jgiMstDAO.search(jgi.getJgiNo());
//						jgiMstList.add(jgiMst);
//						sosCodeSet.add(jgiMst.getSosCd3());
//						jgiNoSet.add(jgiMst.getJgiNo());
//					}
//				}
				JgiMst jgiMst = jgiMstDAO.search(insMst.getJgiNo());
				jgiMstList.add(jgiMst);
				sosCodeSet.add(jgiMst.getSosCd3());
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

				// ------------------------
				// 重複チェック
				// ------------------------
				try {
					exceptDistInsDao.searchByInsNo(insMst.getInsNo());
					messageList.add(new MessageKey("DPC3001E", insMst.getInsAbbrName()));
				} catch (DataNotFoundException e) {
				}
				// -------------------------------------
				// 施設の試算・配分除外フラグチェック
				// -------------------------------------
				if (exceptProdSetFlg) {
					if(exceptProdList.size() == 0) {
						messageList.add(new MessageKey("DPC1005E", "指定方法が試算・配分除外品目","品目の選択"));
					}
				}else {
					//試算・配分除外施設の場合
					if (insMrDto.getEstimationFlg() == null || insMrDto.getExceptFlg() == null) {
						messageList.add(new MessageKey("DPC1005E", "指定方法が試算・配分除外施設","施設の選択"));
					}
				}
//				// ------------------------
//				// 特定施設個別計画チェック
//				// ------------------------
//				MessageKey messageKey = checkSpecialInsPlan(distInsFlg, insMst, plannedProdList);
				messageKey = checkSpecialInsPlan(exceptProdSetFlg, insMst, exceptProdList);
				if (messageKey != null) {
					messageList.add(messageKey);
				}
			} catch (DataNotFoundException e) {
				final String errMsg = "施設・従業員情報が存在しない";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}
		if (messageList.size() != 0) {
			throw new LogicalException(new Conveyance(ExceptionUtil.removeDuplicateMessage(messageList)));
		}

		// ------------------------
		// ステータスチェック
		// ------------------------
		for (String sosCd : sosCodeSet) {
	        checkMrStatus(sosCd, "DPS3233E");
		}
		checkInsWsStatus(jgiMstList, "DPS3234E");
		checkInsDocStatus(jgiMstList, "DPS3234E");

		// ------------------------
		// 登録処理
		// ------------------------
		ExceptDistIns distIns = new ExceptDistIns();
		distIns.setExceptProd(exceptProdList);
		for (InsMrDto insMrDto : insMrList) {
			try {
				distIns.setInsNo(insMrDto.getInsNo());
				if(insMrDto.getEstimationFlg() != null) {
					if (insMrDto.getEstimationFlg().equals("1")) {
						distIns.setEstimationFlg(Boolean.TRUE);
					}else {
						distIns.setEstimationFlg(Boolean.FALSE);
					}
				}
				if(insMrDto.getExceptFlg() != null) {
					if (insMrDto.getExceptFlg().equals("1")) {
						distIns.setExceptFlg(Boolean.TRUE);
					}else {
						distIns.setExceptFlg(Boolean.FALSE);
					}
				}
				exceptDistInsDao.insert(distIns);
			} catch (DuplicateException e) {
				final String errMsg = "試算・配分除外施設の登録に失敗";
				throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
			}
		}
	}

	public void updateExceptDistIns(ExceptDistInsUpdateDto updateDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (updateDto == null) {
			final String errMsg = "試算・配分除外施設の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateDto.getInsNo() == null) {
			final String errMsg = "試算・配分除外施設の施設がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
//		if (updateDto.getJgiNo() == null) {
//			final String errMsg = "配分除外施設の従業員がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (updateDto.getUpDate() == null) {
			final String errMsg = "試算・配分除外施設の最終更新日時がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		//指定方法フラグ(配分除外施設：false、配分除外施設品目：true)
		boolean exceptProdSetFlg = updateDto.getExceptProdSetFlg();
		// 除外フラグ
		String[] exclusionFlg = updateDto.getExclusionFlg();
		// 除外選択された品目固定コードリスト
//		List<String> prodCodeList = updateDto.getProdCodeList();
		List<ExceptProd> exceptProdList = updateDto.getProdCodeList();
		// エラーメッセージ格納リスト
		List<MessageKey> messageList = new ArrayList<MessageKey>();
		MessageKey messageKey = null;
		if (exceptProdSetFlg) {
			//試算・配分除外品目の場合
			if(exceptProdList.size() == 0) {
				messageKey = new MessageKey("DPC1005E", "指定方法が試算・配分除外品目","品目の選択");
				messageList.add(messageKey);
				throw new LogicalException(new Conveyance(messageList));
			}
		}else {
			//試算・配分除外施設の場合
			if (updateDto.getExclusionFlg() == null) {
				final String errMsg = "試算・配分除外施設の除外フラグがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			if(exclusionFlg[0].toString().equals("0") && exclusionFlg[1].toString().equals("0")) {
				messageKey = new MessageKey("DPC1005E", "指定方法が試算・配分除外施設","指定区分の選択");
				messageList.add(messageKey);
				throw new LogicalException(new Conveyance(messageList));
			}
		}
		// 施設コード
		String insNo = updateDto.getInsNo();
		// 従業員番号
//		Integer jgiNo = updateDto.getJgiNo();
		// 最終更新日時
		Date upDate = updateDto.getUpDate();
		// 組織コード
		String sosCd3 = updateDto.getSosCd();

//		//---------------------------------------------
//		// 配分除外品目リスト・計画対象品目リストを生成
//		//---------------------------------------------
//		ExceptDistInsEntryProdListDto prodListDto = createProdListDto(prodCodeList);
//		// 配分除外品目リスト
//		List<ExceptProd> exceptProdList = prodListDto.getExceptProdList();
//		// 計画対象品目リスト
//		List<PlannedProd> plannedProdList = prodListDto.getPlannedProdList();
//		// 配分除外施設フラグ(配分除外施設：true、配分除外施設品目：false)
//		boolean distInsFlg = prodListDto.getDistInsFlg();

		//-----------------------
		// 施設・従業員リスト生成
		//-----------------------
		// 従業員情報リスト
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// チェック済み組織コード
		Set<String> sosCodeSet = new HashSet<String>();
		// チェック済み従業員格納リスト
		Set<Integer> jgiNoSet = new HashSet<Integer>();

		try {
			// 施設・従業員の存在チェック
			DpsInsMst insMst = insMstDAO.search(insNo);

//mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//			// チェック済みの従業員以外の場合、リストに追加
//			if (insMst.getComJgiNo() != null && !jgiNoSet.contains(insMst.getComJgiNo())) {
//				JgiMst jgiMst = jgiMstDAO.search(insMst.getComJgiNo());
//				jgiMstList.add(jgiMst);
//				sosCodeSet.add(jgiMst.getSosCd3());
//				jgiNoSet.add(jgiMst.getJgiNo());
//			}
//			if (insMst.getCvmJgiNo() != null && !jgiNoSet.contains(insMst.getCvmJgiNo())) {
//				JgiMst jgiMst = jgiMstDAO.search(insMst.getCvmJgiNo());
//				jgiMstList.add(jgiMst);
//				sosCodeSet.add(jgiMst.getSosCd3());
//				jgiNoSet.add(jgiMst.getJgiNo());
//			}
//			if (insMst.getRsJgiNo() != null && !jgiNoSet.contains(insMst.getRsJgiNo())) {
//				JgiMst jgiMst = jgiMstDAO.search(insMst.getRsJgiNo());
//				jgiMstList.add(jgiMst);
//				sosCodeSet.add(jgiMst.getSosCd3());
//				jgiNoSet.add(jgiMst.getJgiNo());
//			}
//			if (insMst.getOncJgiNo() != null && !jgiNoSet.contains(insMst.getOncJgiNo())) {
//				JgiMst jgiMst = jgiMstDAO.search(insMst.getOncJgiNo());
//				jgiMstList.add(jgiMst);
//				sosCodeSet.add(jgiMst.getSosCd3());
//				jgiNoSet.add(jgiMst.getJgiNo());
//			}

//			//担当者の重複を除いてjgiMstListに入れていく
//			for(JgiMst jgi : insMst.getTantoList()){
//				if (!jgiNoSet.contains(jgi.getJgiNo())){
//					JgiMst jgiMst = jgiMstDAO.search(jgi.getJgiNo());
//					jgiMstList.add(jgiMst);
//					sosCodeSet.add(jgiMst.getSosCd3());
//					jgiNoSet.add(jgiMst.getJgiNo());
//				}
//			}
			JgiMst jgiMst = jgiMstDAO.search(insMst.getJgiNo());
			jgiMstList.add(jgiMst);
			sosCodeSet.add(jgiMst.getSosCd3());
//mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// ------------------------
			// 特定施設個別計画チェック
			// ------------------------
//			MessageKey messageKey = checkSpecialInsPlan(distInsFlg, insMst, plannedProdList);
			messageKey = checkSpecialInsPlan(exceptProdSetFlg, insMst, exceptProdList);
			if (messageKey != null) {
				messageList.add(messageKey);
			}
		} catch (DataNotFoundException e) {
			final String errMsg = "施設・従業員情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		if (messageList.size() != 0) {
			throw new LogicalException(new Conveyance(messageList));
		}

		// ------------------------
		// ステータスチェック
		// ------------------------
		for (String sosCd : sosCodeSet) {
	        checkMrStatus(sosCd, "DPS3233E");
		}
		checkInsWsStatus(jgiMstList, "DPS3234E");
		checkInsDocStatus(jgiMstList, "DPS3234E");

		// ------------------------
		// 削除処理
		// ------------------------
		// 施設指定の場合：全件削除
		// 品目指定の場合：組織の対象カテゴリ品のみ削除

		if(exceptProdSetFlg) {

			// -------------------------------
			// 品目指定の場合
			// -------------------------------

//			// 対象組織取得
//			SosMst sosMst = sosMstDAO.search(sosCd3);

			// 1) 既に施設指定で登録されていた場合
			try {

				ExceptDistIns exceptDistIns = exceptDistInsDao.searchByInsNo(insNo);
				List<ExceptProd> prodList = exceptDistIns.getExceptProd();
				for(ExceptProd prod : prodList) {
					if (prod.getProdCode() == null) {
						// 施設指定レコード（品目NULLレコード）の削除
						exceptDistInsDao.deleteByProdNull(insNo, upDate);
					}
				}

//				if(prodList == null || prodList.size() == 0){
//
//					// 施設指定レコード（品目NULLレコード）の削除
//					exceptDistInsDao.delete(insNo, upDate, null);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　汎用的に書き換え
//					// 対象外カテゴリの品目を登録対象にする
//					if(BooleanUtils.isTrue(sosMst.getOncSosFlg())){
//
//						// ONC組織の場合は、JPBU品、仕入品を登録対象に追加
//						List<PlannedProd> _mmpProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.MMP, null);
//						for (PlannedProd plannedProd : _mmpProdList) {
//							exceptProdList.add(new ExceptProd(plannedProd.getProdCode()));
//						}
//
//						List<PlannedProd> _shireProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.SHIIRE, null);
//						for (PlannedProd plannedProd : _shireProdList) {
//							exceptProdList.add(new ExceptProd(plannedProd.getProdCode()));
//						}
//
//					} else {
//
//						// 営業所組織の場合は、ONC品を登録対象に追加
//						List<PlannedProd> _oncProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.ONC, null);
//						for (PlannedProd plannedProd : _oncProdList) {
//							exceptProdList.add(new ExceptProd(plannedProd.getProdCode()));
//						}
//					}

					//ProdCategoryに定義されている全カテゴリを対象にループ
//					for(ProdCategory c : ProdCategory.getCategoryList()){
//						//対象組織のカテゴリ・サブカテゴリの場合は登録対象にしない
//						if(c == ProdCategory.getInstance(sosMst.getSosCategory())
//						|| c == ProdCategory.getInstance(sosMst.getSosSubCategory())){
//							continue;
//						}
//
//						//対象外カテゴリの品目を登録対象にする
//						try{
//							List<PlannedProd> _prodList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, c, null);
//							for (PlannedProd plannedProd : _prodList) {
//								exceptProdList.add(new ExceptProd(plannedProd.getProdCode()));
//							}
//						} catch (DataNotFoundException e) {
//							//2018上期時点ではSPBUカテゴリの品目が存在しないということがありえます。　エラー処理は不要。
//						}
//					}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//				}

			} catch (DataNotFoundException e) {
			}

			// 2) 対象カテゴリの品目を削除
			List<String> deleteProdList = new ArrayList<String>();
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　汎用的に書き換え
//			if(BooleanUtils.isTrue(sosMst.getOncSosFlg())){
//
//				// ONC組織の場合は、ONC品のみ削除
//				List<PlannedProd> _oncProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.ONC, null);
//				for (PlannedProd plannedProd : _oncProdList) {
//					deleteProdList.add(plannedProd.getProdCode());
//				}
//
//			} else {
//
//				// 営業所組織の場合は、JPBU品、仕入品のみ削除
//				List<PlannedProd> _mmpProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.MMP, null);
//				for (PlannedProd plannedProd : _mmpProdList) {
//					deleteProdList.add(plannedProd.getProdCode());
//				}
//
//				List<PlannedProd> _shireProdList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, ProdCategory.SHIIRE, null);
//				for (PlannedProd plannedProd : _shireProdList) {
//					deleteProdList.add(plannedProd.getProdCode());
//				}
//			}

//			//メインカテゴリの品目
//			ProdCategory mainCategory = ProdCategory.getInstance(sosMst.getSosCategory());
//			if(mainCategory != null){
//				List<PlannedProd> _delList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, mainCategory, null);
//				for (PlannedProd plannedProd : _delList) {
//					deleteProdList.add(plannedProd.getProdCode());
//				}
//			}
//			//サブカテゴリの品目
//			ProdCategory subCategory = ProdCategory.getInstance(sosMst.getSosSubCategory());
//			if(subCategory != null){
//				List<PlannedProd> _delList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, Sales.IYAKU, subCategory, null);
//				for (PlannedProd plannedProd : _delList) {
//					deleteProdList.add(plannedProd.getProdCode());
//				}
//			}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			try {
				if(updateDto.getCategory() != null){
					// ----------------------------------
					// 削除する対象カテゴリの品目を取得
					// ----------------------------------
					Sales sales = null;
					for (String category : updateDto.getCategory()) {
						if(dpsCodeMasterSearchService.isVaccine(category)) {
							sales = Sales.VACCHIN;
						}else{
							sales = Sales.IYAKU;
						}
						List<PlannedProd> _delList = plannedProdDAO.searchList(PlannedProdDAO.SORT_STRING, sales, category, null);
						for (PlannedProd plannedProd : _delList) {
							deleteProdList.add(plannedProd.getProdCode());
						}
					}
				}
			} catch (DataNotFoundException e) {
			}
			// 施設品目削除
			exceptDistInsDao.delete(insNo, upDate, deleteProdList);

		} else {

			// -------------------------------
			// 施設指定の場合
			// -------------------------------
			// 全品削除（組織にかかわらず）
			exceptDistInsDao.delete(insNo, upDate, null);
		}

		// 品目指定で全部チェックはずされた場合、処理終了とする
		if (exceptProdSetFlg && exceptProdList.size() == 0) {
			return;
		}

		// ------------------------
		// 登録処理
		// ------------------------
		ExceptDistIns distIns = new ExceptDistIns();
		distIns.setExceptProd(exceptProdList);
		try {
			distIns.setInsNo(insNo);
			if (exclusionFlg != null) {
				if (exclusionFlg[0].toString().equals("1")) {
					distIns.setEstimationFlg(Boolean.TRUE);
				}else {
					distIns.setEstimationFlg(Boolean.FALSE);
				}
				if (exclusionFlg[1].toString().equals("1")) {
					distIns.setExceptFlg(Boolean.TRUE);
				}else {
					distIns.setExceptFlg(Boolean.FALSE);
				}
			}
			exceptDistInsDao.insert(distIns);
		} catch (DuplicateException e) {
			final String errMsg = "配分除外施設の登録に失敗";
			throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg), e);
		}
	}

	/**
	 * 配分除外施設の品目リストDTOを生成する。
	 *
	 *
	 * @param prodCodeList 品目固定コードのリスト
	 * @return 配分除外施設の品目リストDTO
	 */
	protected ExceptDistInsEntryProdListDto createProdListDto(List<String> prodCodeList) {
		//---------------------------------------------
		// 配分除外品目リスト・計画対象品目リストを生成
		//---------------------------------------------
		// 配分除外施設フラグ(配分除外施設：true、配分除外施設品目：false)
		boolean distInsFlg = true;
		// 配分除外品目リスト
		List<ExceptProd> exceptProdList = new ArrayList<ExceptProd>();
		// 計画対象品目リスト
		List<PlannedProd> plannedProdList = new ArrayList<PlannedProd>();
		// 品目固定コードリストから、配分除外品目リスト・計画対象品目リストを生成
		if (prodCodeList != null && prodCodeList.size() != 0) {
			// 配分除外施設品目
			distInsFlg = false;
//			for (String prodCode : prodCodeList) {
//				if(prodCode == null || "".equals(prodCode)) continue;
			for (int i =0 ; i< prodCodeList.size(); i++) {
				if(prodCodeList.get(i) == null || "".equals(prodCodeList.get(i))) continue;
				try {
					// 計画対象品目を取得
//					PlannedProd prod = plannedProdDAO.search(prodCode);
					PlannedProd prod = plannedProdDAO.search(prodCodeList.get(i));
					plannedProdList.add(prod);
					// 配分除外品目に追加
					ExceptProd exceptProd = new ExceptProd();
					exceptProd.setProdCode(prod.getProdCode());
					exceptProd.setCategory(prod.getCategory());
					exceptProd.setProdAbbrName(prod.getProdAbbrName());
					exceptProd.setProdName(prod.getProdName());
					exceptProdList.add(exceptProd);
				} catch (DataNotFoundException e) {
					final String errMsg = "配分除外施設品目の品目情報が存在しない";
					throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
				}
			}
		} else {
			// 配分除外施設の場合、全品目取得(ステータスチェックで使用)
			try {
				plannedProdList = plannedProdDAO.searchList(null, Sales.IYAKU, null, null);
			} catch (DataNotFoundException e) {
				final String errMsg = "品目情報が存在しない";
				throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
			}
		}
		return new ExceptDistInsEntryProdListDto(distInsFlg, exceptProdList, plannedProdList);
	}

	/**
	 * 特定施設個別計画に存在しないかチェックする。
	 *
	 *
	 * @param distInsFlg 配分除外施設フラグ
	 * @param insMst 施設情報
	 * @param plannedProdList チェック対象の計画対象品目情報のリスト
	 * @return メッセージキー(エラー無の場合、NULL)
	 */
//	protected MessageKey checkSpecialInsPlan(boolean distInsFlg, InsMst insMst, List<PlannedProd> plannedProdList) {
//		MessageKey messageKey = null;
//		if (distInsFlg) {
//			try {
//				specialInsPlanDao.searchByInsNo(null, insMst.getInsNo(), null, null);
//				messageKey = new MessageKey("DPS1023E", insMst.getInsAbbrName());
//			} catch (DataNotFoundException e) {
//			}
//		} else {
//			for (PlannedProd prod : plannedProdList) {
//				try {
//					specialInsPlanDao.searchByInsNo(null, insMst.getInsNo(), prod.getProdCode(), null);
//					messageKey = new MessageKey("DPS1024E", insMst.getInsAbbrName(), prod.getProdName());
//				} catch (DataNotFoundException e) {
//				}
//			}
//		}
//		return messageKey;
//	}
	protected MessageKey checkSpecialInsPlan(boolean exceptProdSetFlg, DpsInsMst insMst, List<ExceptProd> plannedProdList) {
		MessageKey messageKey = null;
		if (exceptProdSetFlg) {
			for (ExceptProd prod : plannedProdList) {
				try {
					specialInsPlanDao.searchByInsNo(null, insMst.getInsNo(), prod.getProdCode(), null);
					// 計画対象品目を取得
					PlannedProd prodGetName = plannedProdDAO.search(prod.getProdCode());
					messageKey = new MessageKey("DPS1024E", insMst.getInsAbbrName(), prodGetName.getProdName());
				} catch (DataNotFoundException e) {
				}
			}
		} else {
			try {
				specialInsPlanDao.searchByInsNo(null, insMst.getInsNo(), null, null);
				messageKey = new MessageKey("DPS1023E", insMst.getInsAbbrName());
			} catch (DataNotFoundException e) {
			}
		}
		return messageKey;
	}

	/**
	 * 配分除外施設における担当者別計画ステータスチェックを行う。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiMstList チェック対象の従業員情報リスト
	 */
	protected void checkMrStatus(String sosCd3, String msgCode) {
		// 全品目が対象
		String category = null;
		// 「試算中」は許可しない
		List<MrStatusForCheck> unallowableStatusList = new ArrayList<MrStatusForCheck>();
		unallowableStatusList.add(MrStatusForCheck.ESTIMATING);
		try {
			dpsMrStatusCheckService.execute(sosCd3, category, unallowableStatusList);
		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(msgCode));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

	}

	/**
	 * 配分除外施設における施設特約店別計画ステータスチェックを行う。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiMstList チェック対象の従業員情報リスト
	 */
	protected void checkInsWsStatus(List<JgiMst> jgiMstList, String msgCode) {

		// 全品目が対象
		String category = null;

		// 「配分中」は許可しない
		List<InsWsStatusForCheck> unallowableInsStatusList = new ArrayList<InsWsStatusForCheck>();
		unallowableInsStatusList.add(InsWsStatusForCheck.DISTING);
		try {
			dpsInsWsStatusCheckService.execute(jgiMstList, category, unallowableInsStatusList);
		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(msgCode));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}
	}

	/**
	 * 配分除外施設における施設医師別計画ステータスチェックを行う。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param jgiMstList チェック対象の従業員情報リスト
	 */
	protected void checkInsDocStatus(List<JgiMst> jgiMstList, String msgCode) {

		// 全品目が対象
		String category = null;

		// 「配分中」は許可しない
		List<InsDocStatusForCheck> unallowableInsStatusList = new ArrayList<InsDocStatusForCheck>();
		unallowableInsStatusList.add(InsDocStatusForCheck.DISTING);
		try {
			dpsInsDocStatusCheckService.execute(jgiMstList, category, unallowableInsStatusList);
		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey(msgCode));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}
	}

}
