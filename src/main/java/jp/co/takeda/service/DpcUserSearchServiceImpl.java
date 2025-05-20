package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.CertificationException;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.ComUserInfoDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.DpsJknGrpDao;
import jp.co.takeda.dao.JgiJokenDAO;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.JknGrpDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.logic.TargetJokenSetSelectLogic;
import jp.co.takeda.logic.UserAuthorizedCheckLogic;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.BusinessTarget;
import jp.co.takeda.security.BusinessType;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpRole;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;

/**
 * 納入計画システムのユーザを扱うサービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpcUserSearchService")
public class DpcUserSearchServiceImpl implements DpcUserSearchService {

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 条件セットDAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiJokenDAO")
	protected JgiJokenDAO jgiJokenDAO;

	/**
	 * 組織情報(取込)DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 組織情報(直接参照)DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstRealDao")
	protected SosMstRealDao sosMstRealDao;

	/**
	 * ユーザIDから従業員番号を取り出すDAO
	 */
	@Autowired(required = true)
	@Qualifier("comUserInfoDao")
	protected ComUserInfoDao comUserInfoDao;

	/**
	 * 計画管理条件セットグループDAO
	 */
	@Autowired(required = true)
	@Qualifier("jknGrpDao")
	private JknGrpDao jknGrpDao;

	/**
	 * 計画支援条件セットグループDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsJknGrpDao")
	private DpsJknGrpDao dpsJknGrpDao;

    @Autowired(required = true)
    @Qualifier("codeMasterDao")
    protected CodeMasterDao codeMasterDao;

    @Autowired(required = true)
    @Qualifier("dpsCodeMasterDao")
    protected DpsCodeMasterDao dpsCodeMasterDao;

    @Autowired(required = true)
    @Qualifier("dpsCodeMasterSearchService")
    protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// modify start 2019/12/02 M.Wada As-Is支援運用ユーザ追加
	/**
	 * 運営ユーザID1
	 */
	@Autowired(required = true)
	@Qualifier("operatorUserId1")
	protected Integer operatorUserId1;
	// modify end 2019/12/02 M.Wada As-Is支援運用ユーザ追加

	// add start 2019/12/02 M.Wada As-Is支援運用ユーザ追加
	/**
	 * 運営ユーザID2
	 */
	@Autowired(required = true)
	@Qualifier("operatorUserId2")
	protected Integer operatorUserId2;
	// add end 2019/12/02 M.Wada As-Is支援運用ユーザ追加

	// デフォルト組織従業員の再設定を行う
	public void changeDefaultSosJgi(String defaultFlg, String initDispFlg, String sosCd, String jgiNo) {

		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();

		// -------------------------
		// デフォルトに戻す場合
		// -------------------------
		if (StringUtils.isNotBlank(defaultFlg) && Boolean.parseBoolean(defaultFlg)) {
			dpUserInfo.reverseDefaultSosJgi();
		}
		// -------------------------
		// 従業員指定の場合
		// -------------------------
		else if (StringUtils.isNotBlank(jgiNo) && ValidationUtil.isInteger(jgiNo)) {
			try {
				JgiMst jgiMst = jgiMstDAO.search(ConvertUtil.parseInteger(jgiNo));
				SosMst sosMst = sosMstDAO.search(jgiMst.getSosCd());
				dpUserInfo.setDefaultSosJgi(sosMst, jgiMst);
			} catch (DataNotFoundException e) {
				// 未検査
			}
		}
		// -------------------------
		// 組織指定の場合
		// -------------------------
		else if (StringUtils.isNotBlank(sosCd)) {
			try {
				SosMst sosMst = sosMstDAO.search(sosCd);
				if (BumonRank.IEIHON.getDbValue() >= sosMst.getBumonRank().getDbValue()) {
					dpUserInfo.setDefaultSosJgi(null, null);
				} else {
					dpUserInfo.setDefaultSosJgi(sosMst, null);
				}
			} catch (DataNotFoundException e) {
				// 未検査
			}
		}
		// -------------------------
		// 初期表示設定に戻す場合
		// -------------------------
		if (StringUtils.isNotBlank(initDispFlg)) {
			dpUserInfo.setInitDispFlg(Boolean.valueOf(initDispFlg));
		}
	}

	// 設定ユーザを指定の従業員番号の従業員に変更する
	public void switchUser(Integer jgiNo, DpMetaInfo dpMetaInfo) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "設定従業員変更時に変更対象の従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpMetaInfo == null) {
			final String errMsg = "メタ情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		Integer settingJgiNo = userInfo.getSettingUser().getJgiNo();

		// 設定中の従業員番号と指定の従業員番号が一致する場合は何もしない
		if (jgiNo.equals(settingJgiNo)) {
			return;
		}

		// 正規の従業員に戻したい場合
		Integer loginUserJgiNo = userInfo.getLoginUserJgiNo();
		if (jgiNo.equals(loginUserJgiNo)) {
			userInfo.switchOfficialUserInfo();
			return;
		}

		// 代行ユーザに設定したい場合、従業員情報を取得する
		try {

			BusinessTarget requestBusinessTarget = dpMetaInfo.getBusinessTarget();
			BusinessType requestBusinessType = dpMetaInfo.getBusinessType();

			TargetJokenSetSelectLogic logic = new TargetJokenSetSelectLogic(requestBusinessTarget, requestBusinessType);
			JokenSet[] targetJokenSetList = logic.execute();

            // ------------------------------------
            // ユーザの権限情報取得
            // ------------------------------------
			SysClass sysClass = BusinessTarget.getSysType(requestBusinessTarget);
			List<JknGrp> jknGrpList = new ArrayList<JknGrp>();
			JknGrp menuJknGrp = null;
			if (sysClass == SysClass.DPM) {
				// 管理の場合
				jknGrpList = jknGrpDao.searchListByJgiNo(jgiNo);
	            for(JknGrp JknGrp:jknGrpList) {
	                if(JknGrp.getJknGrpId().equals(JknGrpId.MENU)) {
	                    menuJknGrp = JknGrp;
	                    break;
	                }
	            }
			} else {
				// 支援の場合
				jknGrpList = dpsJknGrpDao.searchListByJgiNo(jgiNo);
	            for(JknGrp JknGrp:jknGrpList) {
	                if(JknGrp.getJknGrpId().equals(JknGrpId.MENU_DPS)) {
	                    menuJknGrp = JknGrp;
	                    break;
	                }
	            }
			}
			JgiMst altJgiMst = jgiMstDAO.searchReal(jgiNo);
			SosMst altSosMst = getSosMst(sysClass, altJgiMst, menuJknGrp);
			DpRole altDpRole = new DpRole(jgiJokenDAO.searchJokenSet(jgiNo, targetJokenSetList));

			List<JokenSet> tokuyakutenJokenSetCodes = dpsCodeMasterSearchService.searchTokuyakuJokenSetCd();
			DpUser altDpUser = new DpUser(jgiMstDAO.searchByJgiKb(jgiNo, ALT_JGI_KB_ARRAY), altSosMst, altDpRole, jknGrpList,tokuyakutenJokenSetCodes);
			userInfo.switchAltUserInfo(altDpUser);
		} catch (DataNotFoundException e) {
			final String errMsg = "代行ユーザに切り替えようとしたが、対象の代行ユーザがDBに見つからないためエラー jgiNo=" + jgiNo;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		} catch (LogicalException e) {
			final String errMsg = "代行ユーザに切り替えようとしたが、対象の代行ユーザがログイン情報に見つからないためエラー jgiNo=" + jgiNo;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}

	// データアクセス認証が許可されているＩＤかを検証
	public void dataAccessPermitCheck(String[] permitIdList, DpMetaInfo dpMetaInfo) throws AccessDeniedException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (permitIdList == null) {
			final String errMsg = "ユーザ管理情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (permitIdList.length == 0) {
			return;
		}
		if (dpMetaInfo == null) {
			final String errMsg = "メタ情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検証開始
		// ----------------------
		boolean permitFlg = false;
		final String path = dpMetaInfo.getPath();
		for (String id : permitIdList) {
			if (path.indexOf(id) != -1) {
				permitFlg = true;
				break;
			}
		}
		if (permitFlg == false) {
			final String errMsg = "ＤＢ認証出来ない不許可ＩＤが指定されているためエラー。path=" + path;
			throw new AccessDeniedException(new Conveyance(ErrMessageKey.ACCESS_DENIED_ERROR, errMsg));
		}
	}

	// 認証されているかを検証
	public boolean isAuthorized(DpUserInfo dpUserInfo, DpMetaInfo dpMetaInfo) throws CertificationException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (dpUserInfo == null) {
			final String errMsg = "ユーザ管理情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpMetaInfo == null) {
			final String errMsg = "メタ情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 要求情報を参照して認証されているかを検証
		SysClass sysClass = dpUserInfo.getSysClass();
		SysType sysType = dpUserInfo.getSysType();
		BusinessTarget bTarget = dpMetaInfo.getBusinessTarget();
		BusinessType bType = dpMetaInfo.getBusinessType();
		return new UserAuthorizedCheckLogic(sysClass, sysType, bTarget, bType).isAuthrized();
	}

	// 代行ユーザとしてログイン可能かを判定
	public boolean canAltLogin(Integer altJgiNo, DpMetaInfo dpMetaInfo) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (altJgiNo == null) {
			final String errMsg = "従業員コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpMetaInfo == null) {
			final String errMsg = "メタ情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 共通系の要求はＯＫにする
		BusinessTarget requestBusinessTarget = dpMetaInfo.getBusinessTarget();
		BusinessType requestBusinessType = dpMetaInfo.getBusinessType();

		// 共通系の要求でログイン判定は出来ないので、falseを返す
		if (BusinessTarget.DPC == requestBusinessTarget || BusinessType.CMN == requestBusinessType) {
			return false;
		}

		// 要求情報(管理対象業務と業務種別)に対応した必要な条件セット配列を取得
		TargetJokenSetSelectLogic logic = new TargetJokenSetSelectLogic(requestBusinessTarget, requestBusinessType);
		JokenSet[] targetJokenSetList = logic.execute();

		try {
			// 代行ユーザの存在チェック
			jgiMstDAO.searchReal(altJgiNo);

			// 条件セットの確認
			jgiJokenDAO.searchJokenSet(altJgiNo, targetJokenSetList);

			// 従業員区分の確認
			jgiMstDAO.searchByJgiKb(altJgiNo, ALT_JGI_KB_ARRAY);

			// ここまでくれば認証可能
			return true;

		} catch (DataNotFoundException e) {
			return false;
		}
	}

	// 納入計画システムのユーザ情報を取得
	public DpUserInfo searchDpUserInfo(Integer jgiNo, Integer altJgiNo, DpMetaInfo dpMetaInfo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpMetaInfo == null) {
			final String errMsg = "メタ情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// リクエストタイプの判定
		BusinessTarget requestBusinessTarget = dpMetaInfo.getBusinessTarget();
		BusinessType requestBusinessType = dpMetaInfo.getBusinessType();

		SysClass sysClass = BusinessTarget.getSysType(requestBusinessTarget);
		SysType sysType = BusinessType.getSysType(requestBusinessType);

		// ------------------------------------
		// ユーザの権限情報取得
		// ------------------------------------
		JknGrp menuJknGrp = null;
		boolean jknCheckFlg = false;
		List<JknGrp> jknGrpList = new ArrayList<JknGrp>();
		if (sysClass == SysClass.DPM) {
			// 管理の場合
			jknGrpList = jknGrpDao.searchListByJgiNo(jgiNo);
			for(JknGrp JknGrp : jknGrpList) {
				if(JknGrp.getJknGrpId().equals(JknGrpId.MENU)) {
					jknCheckFlg = true;
					menuJknGrp = JknGrp;
					break;
				}
			}
		} else {
			// 支援の場合
			jknGrpList = dpsJknGrpDao.searchListByJgiNo(jgiNo);
			for(JknGrp JknGrp : jknGrpList) {
				if(JknGrp.getJknGrpId().equals(JknGrpId.MENU_DPS)) {
					jknCheckFlg = true;
					menuJknGrp = JknGrp;
					break;
				}
			}
		}
		if(jknCheckFlg == false) {
			final String errMsg = "jknSet not found";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}

		// ------------------------------------
		// 正規の従業員認証 ＆ 取得処理
		// ------------------------------------

		// 要求情報(管理対象業務と業務種別)に対応した必要な条件セット配列を取得
		TargetJokenSetSelectLogic logic = new TargetJokenSetSelectLogic(requestBusinessTarget, requestBusinessType);
		JokenSet[] targetJokenSetList = logic.execute();

		JgiMst jgiMst = null;
		SosMst sosMst = null;
		DpUser dpUser = null;

		List<JokenSet> tokuyakutenJokenSetCodes = dpsCodeMasterSearchService.searchTokuyakuJokenSetCd();


		try {

			// 従業員番号が運営ユーザかどうか
			if(jgiNo.equals(operatorUserId1) || jgiNo.equals(operatorUserId2)){
			// modify end 2019/12/02 M.Wada As-Is支援運用ユーザ追加
				// 運営ユーザの場合、従業員情報の組織コードを本部に変更する
				jgiMst = jgiMstDAO.searchByJgiKb(jgiNo, OPERATOR_JGI_KB_ARRAY);

				// 企画Ｇの組織を取得
				List<SosMst> sosList = sosMstRealDao.searchRealList(SosMstRealDao.SORT_STRING, SosMst.HONBU_KIKAKU_GROUP);

				// 1件目の組織の組織コードを設定
				jgiMst.setSosCd(sosList.get(0).getSosCd());
				jgiMst.setSosCd2(JgiMst.BLANK_SOS_CD);

				sosMst = getSosMst(sysClass, jgiMst, menuJknGrp);
				DpRole dpRole = null;
				try {
					dpRole = new DpRole(jgiJokenDAO.searchJokenSet(jgiNo, targetJokenSetList));
				} catch (DataNotFoundException e) {
				}
				dpUser = new DpUser(jgiMstDAO.searchByJgiKb(jgiNo, OPERATOR_JGI_KB_ARRAY), sosMst, dpRole, jknGrpList,tokuyakutenJokenSetCodes);

			} else {

				// 運営ユーザ以外の場合
				jgiMst = jgiMstDAO.searchByJgiKb(jgiNo, OFFICIAL_JGI_KB_ARRAY);
				sosMst = getSosMst(sysClass, jgiMst, menuJknGrp);
				DpRole dpRole = null;
				try {
					dpRole = new DpRole(jgiJokenDAO.searchJokenSet(jgiNo, targetJokenSetList));
				} catch (DataNotFoundException e) {
				}
				dpUser = new DpUser(jgiMstDAO.searchByJgiKb(jgiNo, OFFICIAL_JGI_KB_ARRAY), sosMst, dpRole, jknGrpList,tokuyakutenJokenSetCodes);

				// 副担当MR(主担当施設を持たないMR）の場合は権限を再設定
				try {
					jgiMstDAO.searchSubJgi(jgiNo);
					dpRole = new DpRole(dpRole.getJokenSet(), dpRole.getRoleName(), DpRole.AUTH_LIST_IYAKU_SUB_MR);
					dpUser = new DpUser(jgiMstDAO.searchByJgiKb(jgiNo, OFFICIAL_JGI_KB_ARRAY), sosMst, dpRole, jknGrpList, true,tokuyakutenJokenSetCodes);
				} catch (DataNotFoundException e) {
				}
			}


		} catch (DataNotFoundException e) {
			final String errMsg = "user not found";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg), e);
		}

		// ------------------------------------
		// 代行ユーザ認証 ＆ 取得処理
		// ------------------------------------
		if (altJgiNo != null) {
			try {
				JgiMst altJgiMst = jgiMstDAO.searchReal(altJgiNo);
				SosMst altSosMst = getSosMst(sysClass, altJgiMst, menuJknGrp);
				DpRole altDpRole = new DpRole(jgiJokenDAO.searchJokenSet(altJgiNo, targetJokenSetList));
				if (sysClass == SysClass.DPM) {// 管理の場合
					jknGrpList = jknGrpDao.searchListByJgiNo(altJgiNo);
				} else {// 支援の場合
					jknGrpList = dpsJknGrpDao.searchListByJgiNo(altJgiNo);
				}
				DpUser altDpUser = new DpUser(jgiMstDAO.searchByJgiKb(altJgiNo, ALT_JGI_KB_ARRAY), altSosMst, altDpRole, jknGrpList,tokuyakutenJokenSetCodes);
				return new DpUserInfo(dpUser, altDpUser, sysClass, sysType, altSosMst.clone(), altJgiMst.clone());
			} catch (DataNotFoundException e) {
			}
		}

		// ------------------------------------
		// 正規の従業員認証開始
		// ------------------------------------
		List<CodeAndValue> altJgiList = null;
		try {
			altJgiList = jgiMstDAO.searchAltJgi(jgiNo, targetJokenSetList);
		} catch (DataNotFoundException e) {
		}
		if (altJgiList != null) {
			// 切替対象従業員の一番上に正規の従業員を設定
			altJgiList.add(0, new CodeAndValue(jgiMst.getJgiNo().toString(), jgiMst.getJgiName()));
		}
		return new DpUserInfo(dpUser, altJgiList, sysClass, sysType, sosMst.clone(), jgiMst.clone());
	}

	// ログインした従業員に関連するデフォルトの品目カテゴリを取得
	public String searchDefaultProdCategoryCode() {

		DpUser loginUser = DpUserInfo.getDpUserInfo().getSettingUser();

		//ログインユーザが支店長・支店スタッフ権限保有、
		//且つ組織マスタのカテゴリーorサブカテゴリーに仕入品が設定されている場合、仕入品をデフォルトカテゴリとする。
		String siireCode = dpsCodeMasterSearchService.searchCodeByDataKbn(DpsCodeMaster.SII.getDbValue()).get(0).getDataCd();
		List<String> sosCategoryList = new ArrayList<String>();
		for (SosMstCtg sosMstCtg : loginUser.getSosCategoryList()) {
			sosCategoryList.add(sosMstCtg.getCategoryCd());
		}
		if (loginUser.isMatch(JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)
			&&
			(sosCategoryList.contains(siireCode))
			)
		 {
			return siireCode;
		 }

		// カテゴリーリストの最初のカテゴリをデフォルトカテゴリとする。
		if (loginUser.getSosCategoryList().isEmpty()) {
			return "";
		} else {
			return loginUser.getSosCategoryList().get(0).getCategoryCd();
		}
	}

	/**
	 * ユーザＩＤから従業員番号を取り出す
	 * @param userId 画面のユーザＩＤ
	 * @return 従業員番号
	 * @throws DataNotFoundException  従業員番号が見つからない場合にスロー
	 */
	@Override
	public int selectLoginJgiNoByUserId(String userId) throws DataNotFoundException {

		return comUserInfoDao.selectLoginJgiNoByUserId(userId);
	}
	// ------------------------------------------
	// PRIVATE METHOD
	// ------------------------------------------

	/**
	 * 管理対象業務に応じた組織情報を取得する。
	 *
	 * @param sysClass 管理対象区分
	 * @param menuJknGrp
	 * @param sosCd 組織コード
	 * @return 組織情報
	 * @throws DataNotFoundException データが見つからない場合にスロー
	 */
	private SosMst getSosMst(SysClass sysClass, JgiMst jgiMst, JknGrp menuJknGrp) throws DataNotFoundException {

		// ------------------------
		// 引数チェック
		// ------------------------
		String sosCd = jgiMst.getSosCd();
		if (StringUtils.isBlank(sosCd)) {
			final String errMsg = "組織コードが指定されていないため、エラー";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sysClass == null) {
			final String errMsg = "管理対象業務が指定されていないため、エラー";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		SosMst sosMst = null;

		// 支援の場合
		if (SysClass.DPS == sysClass) {
			sosMst = sosMstDAO.search(sosCd);
		}

		// 管理の場合
		else if (SysClass.DPM == sysClass) {
			sosMst = sosMstRealDao.searchReal(sosCd);
		}

		// その他の場合
		else {
			final String errMsg = "管理対象業務に支援または管理が指定されているためエラー sysClass=" + sysClass;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// カテゴリの取得
		List<SosMstCtg> sosCtgList = new ArrayList<SosMstCtg>();
		// 管理の場合
		if (SysClass.DPM == sysClass) {
			// 組織カテゴリビュー（DPM_C_VI_SOS_CTG）より該当するカテゴリ取得
			try {
			    sosCtgList = sosMstRealDao.searchSosCategoryList(sosCd);
			}
			// 取得できなかった場合
			// SOS_CD='01050'（JPBUのSOS_CD1）でカテゴリを再取得←全カテゴリが取れる
			catch (DataNotFoundException e) {
	            sosCtgList = sosMstRealDao.searchSosCategoryList(SosMst.SOS_CD1);
			}
			// 計画管理条件セットグループ（DPM_C_JKN_GRP）の条件区分（JOKEN_KBN）が
	        // 2：ワクチンのみ参照可能、3：ワクチンのみ更新可能の場合
	        // ①②よりワクチン以外を削除
	        if(menuJknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) ||
	                menuJknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
	            List<DpmCCdMst> codeList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
	            if(codeList.size() > 1) {
	                final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが複数登録されています。";
	                throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
	            }
	            String vacCtg = codeList.get(0).getDataCd();
	            List<SosMstCtg> changeSosCtgList = new ArrayList<SosMstCtg>();
	            for(SosMstCtg sosCtg: sosCtgList) {
	                if(sosCtg.getCategoryCd().equals(vacCtg)) {
	                    changeSosCtgList.add(sosCtg);
	                    break;
	                }
	            }
	            sosCtgList = changeSosCtgList;
	        }
		} else {
			// 支援の場合
			// 組織カテゴリビュー（DPS_C_VI_SOS_CTG）より該当するカテゴリ取得
			try {
			    sosCtgList = sosMstRealDao.searchDpsSosCategoryList(sosCd);
			}
			// 取得できなかった場合
			// SOS_CD='01050'（JPBUのSOS_CD1）でカテゴリを再取得←全カテゴリが取れる
			catch (DataNotFoundException e) {
	            sosCtgList = sosMstRealDao.searchDpsSosCategoryList(SosMst.SOS_CD1);
			}
			// 計画支援条件セットグループ（DPS_C_JKN_GRP）の条件区分（JOKEN_KBN）が
	        // 2：ワクチンのみ参照可能、3：ワクチンのみ更新可能の場合
	        // ①②よりワクチン以外を削除
	        if(menuJknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) ||
	                menuJknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
	            List<DpsCCdMst> codeList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
	            if(codeList.size() > 1) {
	                final String errMsg = "計画支援汎用マスタにワクチンのカテゴリコードが複数登録されています。";
	                throw new SystemException(new Conveyance(DB_DUPLICATE_ERROR, errMsg));
	            }
	            String vacCtg = codeList.get(0).getDataCd();
	            List<SosMstCtg> changeSosCtgList = new ArrayList<SosMstCtg>();
	            for(SosMstCtg sosCtg: sosCtgList) {
	                if(sosCtg.getCategoryCd().equals(vacCtg)) {
	                    changeSosCtgList.add(sosCtg);
	                    break;
	                }
	            }
	            sosCtgList = changeSosCtgList;
	        }
		}

		sosMst.setSosCategoryList(sosCtgList);

		return sosMst;
	}


}
