package jp.co.takeda.web.cmn.velocity;

import static jp.co.takeda.model.div.JokenSet.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.logic.ProgressIyakuSearchTypeLogic;
import jp.co.takeda.logic.ProgressIyakuSearchTypeLogic.ProgressIyakuSearchType;
import jp.co.takeda.logic.ProgressIyakuSearchTypeLogic.ProgressIyakuSearchType.ProgressIyakuRank;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.InsWsPlanStatusForVac;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.WsPlanStatusForVac;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CalcType;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.StatusForInsDocPlan;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.StatusForWsSlide;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.model.view.InsDocPlanStatSum;
import jp.co.takeda.model.view.InsWsPlanStatSum;
import jp.co.takeda.model.view.MrPlanStatSum;
import jp.co.takeda.model.view.OfficePlanStatSum;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.model.view.WsPlanStatusSummary;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.DateUtil;

/**
 * 業務進捗状況を管理するVelocityツールクラス
 *
 * @author tkawabata
 */
public class ProgressTool extends SpringRegistTool {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(ProgressTool.class);

	/**
	 * 業務進捗のリンクフォントサイズ
	 */
	public static final String PROGRESS_LINK_FONT = "style='font-size:10px;' ";

	/**
	 * 業務進捗のリンクフォントサイズを返す。
	 *
	 * @return 業務進捗のリンクフォントサイズ
	 */
	public static String getStyle() {
		return PROGRESS_LINK_FONT;
	}

	/**
	 * (自)施設特約店別計画担当者再配分権限
	 */
	public static final DpAuthority EDIT_I_MMP_AUTH_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (仕)施設特約店別計画担当者再配分権限
	 */
	public static final DpAuthority EDIT_I_SIRE_AUTH_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (自)施設特約店別計画担当者確定権限
	 */
	public static final DpAuthority EDIT_I_MMP_AUTH_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (仕)施設特約店別計画担当者確定権限
	 */
	public static final DpAuthority EDIT_I_SIRE_AUTH_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画担当者再配分権限
	 */
	public static final DpAuthority EDIT_W_AUTH_REHAIBUN = new DpAuthority( AuthType.EDIT);

	/**
	 * (ワ)施設特約店別計画担当者確定権限
	 */
	public static final DpAuthority EDIT_W_AUTH_FIX = new DpAuthority( AuthType.EDIT);

	/**
	 * 営業所長、営業所スタッフ
	 */
	public static final JokenSet[] OFFICE_AUTH = { JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF };

	/**
	 * (自)営業所計画参照権限
	 */
	public static final DpAuthority REFFER_DPS_I_MMP_OFFICE_PLAN = new DpAuthority( AuthType.REFER);

	/**
	 * (仕)営業所計画参照権限
	 */
	public static final DpAuthority REFFER_DPS_I_SIRE_OFFICE_PLAN = new DpAuthority( AuthType.REFER);

	// ------------------------------------
	// テーブルの表示制御用関数
	// ------------------------------------
	/**
	 * (医)ＴＹ変換指定率薬価改定率を表示するかを示すフラグ
	 *
	 * @return 表示する場合に真
	 */
	public boolean isTYYakkaDispFlg() {
		if (canDpsIyaku() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		if(settingUser.isTokuyakutenUser()) {
			return false;
		}
		switch(settingUser.getSosLvl(JknGrpId.MENU_DPS.getDbValue())){
		case ALL:
			ProgressIyakuSearchType searchType = getProgressIyakuSearchType();
			switch (searchType) {
			case SITEN_LIST:
				return true;
			default:
				return false;
			}
		default:
			return false;
		}
	}

	/**
	 * (医)各種登録状況を表示するかを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isIyakuRegistDataDispFlg() {
		if (canDpsIyaku() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		if(settingUser.isTokuyakutenUser()) {
			return false;
		}

		switch(settingUser.getSosLvl(JknGrpId.MENU_DPS.getDbValue())){
		case ALL:
		case BRANCH:
		case OFFICE:
		case MR:
		case TEAM:
			ProgressIyakuRank rank = getProgressIyakuRank();
			switch (rank) {
				case SITEN:
				case OFFICE:
				case TEAM:
				case TANTOU:
					return true;
				default:
					return false;
			}
		default:
			return false;

		}

	}

	/**
	 * 特約店系ユーザか否か
	 * @return
	 */
	public boolean isTokuyakutenUser() {
		return DpUserInfo.getDpUserInfo().getSettingUser().isTokuyakutenUser();
	}

	/**
	 * (医)営業所計画-担当者別計画を表示するかを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isBusinessProgressTableDispFlg(String category) {
		if (canDpsIyaku() == false) {
			return false;
		}
		List<String> categories = getSosCategories();
		if(!categories.contains(category)) {
			return false;
		}

		ProgressIyakuRank rank = getProgressIyakuRank();
		switch (rank) {
			case SITEN:
			case OFFICE:
			case TEAM:
			case TANTOU:
				return true;
			default:
				return false;
		}
	}

	/**
	 * ユーザの組織（現在選択している組織）の組織カテゴリのリストを返す
	 * @return
	 */
	private List<String> getSosCategories() {
		List<String> categories = new ArrayList<String>();
		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		String sosCd = null;
		if(dSosMst == null) {
			// NULL は本社従業員 ログイン時
			sosCd = SosMst.SOS_CD1;

		}else {
			sosCd = dSosMst.getSosCd();
		}
		List<SosCtg> sosCtgs = getDpsSosCtgSearchService().searchDpsSosCtgList(sosCd, "DPS000");
		for(SosCtg e:sosCtgs) {
			categories.add(e.getCategory());
		}
		return categories;
	}

	/**
	 * (医)営業所計画-担当者別計画に表示する参照権限のあるカテゴリーリストを取得する。
	 *DATA_VALUE順
	 * @return カテゴリーリスト
	 */
	public List<CategoryInfo> getDispBusinessProgressCategoryList(){

		List<CategoryInfo> categories = new ArrayList<CategoryInfo>();
		List<DpsCCdMst> list = getDpsCodeMasterSearchService().getCategoriesSortByProgressOrder();
		for (DpsCCdMst e : list) {
			if(isBusinessProgressTableDispFlg(e.getDataCd())) {
				CategoryInfo info = new CategoryInfo(e.getDataCd(),e.getDataName());
				categories.add(info);
			}
		}
		return categories;
	}

	/**
	 * (医)営業所計画-担当者別計画に表示する参照権限のあるカテゴリーリストを取得する。
	 * DATA_SEQ順
	 * @return カテゴリーリスト
	 */
	public List<CategoryInfo> getDispBusinessProgressCategoryListBySeq(){

		List<CategoryInfo> categories = new ArrayList<CategoryInfo>();
		List<DpsCCdMst> list = getDpsCodeMasterSearchService().searchCodeByDataKbn("category");
		for (DpsCCdMst e : list) {
			if(isBusinessProgressTableDispFlg(e.getDataCd())) {
				CategoryInfo info = new CategoryInfo(e.getDataCd(),e.getDataName());
				categories.add(info);
			}
		}
		return categories;
	}

	/**
	 * 業務進捗状況_特約店別計画(MMP品・仕入品(一般・麻薬)・ONC)を表示するかを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isWsPlanDispFlg() {
		if (canDpsIyaku() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		switch(settingUser.getSosLvl(JknGrpId.MENU_DPS.getDbValue())){
		case ALL:
		case BRANCH:
			ProgressIyakuRank rank = getProgressIyakuRank();
			switch (rank) {
				case SITEN:
					return true;
				default:
					return false;
			}
		default:
			return false;

		}
	}

	/**
	 * (ワ)ＴＢ変換指定率薬価改定率を表示するかを示すフラグ
	 *
	 * @return 表示する場合に真
	 */
	public boolean isTBDispFlg() {
		if (canDpsWakutin() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		final JokenSet[] PERMIT_JOKEN = { JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G };
		if (settingUser.isMatch(PERMIT_JOKEN)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * (ワ)各種登録状況を表示するかを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isWakutinRegistDataDispFlg() {
		if (canDpsWakutin() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		final JokenSet[] PERMIT_JOKEN = { JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G, JokenSet.WAKUTIN_AL, JokenSet.WAKUTIN_MR };
		if (settingUser.isMatch(PERMIT_JOKEN)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * (ワ)本部(本部または本部ワクチンＧ）かを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isWakutinHonbu() {
		if (canDpsWakutin() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		final JokenSet[] PERMIT_JOKEN = { JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G };
		if (settingUser.isMatch(PERMIT_JOKEN)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * (ワ)担当者別計画-施設特約店別計画を表示するかを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isWakutinTableDispFlg() {
		if (canDpsWakutin() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		final JokenSet[] PERMIT_JOKEN = { JokenSet.HONBU, JokenSet.HONBU_WAKUTIN_G, JokenSet.WAKUTIN_AL, JokenSet.WAKUTIN_MR };
		if (settingUser.isMatch(PERMIT_JOKEN)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 業務進捗状況_特約店別計画(ワクチン品)を表示するかを示すフラグを取得する。
	 *
	 * @return 表示する場合に真
	 */
	public boolean isWakutinWsPlanDispFlg() {
		if (canDpsWakutin() == false) {
			return false;
		}
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		final JokenSet[] PERMIT_JOKEN = { HONBU, HONBU_WAKUTIN_G, TOKUYAKUTEN_G_MASTER, TOKUYAKUTEN_G_STAFF, TOKUYAKUTEN_G_TANTOU, TOKUYAKUTEN_GYOUMU_G, TOKUYAKUTEN_MASTER };
		if (settingUser.isMatch(PERMIT_JOKEN)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * パンくずリストを取得する。
	 *
	 * @return パンくずリスト
	 */
	public List<String> createPanList() {

		List<String> panList = new ArrayList<String>();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		ProgressIyakuRank rank = getProgressIyakuRank();


		if(DpUserInfo.getDpUserInfo().getLoginUser().isTokuyakutenUser()) {
			return panList;
		}

		// チームなしの場合
//		boolean existsTeam = true;
//		if(dSosMst != null && dSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){
//			try {
//				getDpsSosJgiSearchService().searchUnderSosList(dSosMst.getSosCd(), SosListType.SHITEN_LIST);
//			} catch (DataNotFoundException e) {
//				existsTeam = false;
//			}
//		}

		// 組織名
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		final String S_NAME = "リージョン";
		final String O_NAME = "エリア";
//		final String S_NAME = "支店";
//		final String O_NAME = "営業所";
// mod End   2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		final String T_NAME = "チーム";
		final String M_NAME = "担当者";

		// 関数名
		final String FUNC_NAME = "dps000C00F00Change";

		switch(settingUser.getSosLvl(JknGrpId.MENU_DPS.getDbValue())) {
		case ALL:

			switch (rank) {
				case SITEN:
					panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
					break;
				case OFFICE:
					panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "false", "", "", "", ""));
					panList.add(createChangeLink(O_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
					break;
				case TEAM:
					panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "false", "", "", "", ""));
					panList.add(createChangeLink(O_NAME, FUNC_NAME, "false", "false", dSosMst.getUpSosCd(), "", "", ""));
					panList.add(createChangeLink(T_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
					break;
				case TANTOU:

					// 支店コード取得
					String sitenSosCode = "";
					try {
						if(dSosMst != null && dSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){
							sitenSosCode = dSosMst.getUpSosCd();
						} else {
							sitenSosCode = getDpsSosJgiSearchService().searchUpSosCode(dSosMst.getUpSosCd());
						}
					} catch (DataNotFoundException e) {
						if (LOG.isErrorEnabled()) {
							final String errMsg = "本部ユーザがデフォルト組織で指定している支店コードを取得出来ない";
							LOG.error(errMsg, e);
						}
					}
					panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
					panList.add(createChangeLink(O_NAME, FUNC_NAME, "false", "false", sitenSosCode, "", "", ""));
//					if(existsTeam){
//						panList.add(createChangeLink(T_NAME, FUNC_NAME, "false", "false", dSosMst.getUpSosCd(), "", "", ""));
//					}
					panList.add(createChangeLink(M_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
					break;
			default:
				break;
			}
			break;
		case BRANCH:
				switch (rank) {
					case SITEN:
						panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
						break;
					case OFFICE:
						panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
						panList.add(createChangeLink(O_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
						break;
					case TEAM:
						panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
						panList.add(createChangeLink(O_NAME, FUNC_NAME, "false", "false", dSosMst.getUpSosCd(), "", "", ""));
						panList.add(createChangeLink(T_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
						break;
					case TANTOU:
						panList.add(createChangeLink(S_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
						panList.add(createChangeLink(O_NAME, FUNC_NAME, "false", "false", settingUser.getSosCd(), "", "", ""));
//						if(existsTeam){
//							panList.add(createChangeLink(T_NAME, FUNC_NAME, "false", "false", dSosMst.getUpSosCd(), "", "", ""));
//						}
						panList.add(createChangeLink(M_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
						break;
				default:
					break;
				}
			break;
		case OFFICE:
				switch (rank) {
					case OFFICE:
						panList.add(createChangeLink(O_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
						break;
					case TEAM:
						panList.add(createChangeLink(O_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
						panList.add(createChangeLink(T_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
						break;
					case TANTOU:
						panList.add(createChangeLink(O_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
//						if(existsTeam){
//							panList.add(createChangeLink(T_NAME, FUNC_NAME, "false", "false", dSosMst.getUpSosCd(), "", "", ""));
//						}
						panList.add(createChangeLink(M_NAME, FUNC_NAME, "false", "false", "", "", "", ""));
						break;
				default:
					break;
				}
			break;
		case MR:
				switch (rank) {
					case TANTOU:
						panList.add(createChangeLink(M_NAME, FUNC_NAME, "true", "true", "", "", "", ""));
						break;
				default:
					break;
				}
			break;

		default:
			break;

		}


		return panList;
	}

	// ------------------------------------
	// テーブル内のロジック制御用関数
	// ------------------------------------
	/**
	 * (医)業務進捗の検索タイプを取得する。
	 *
	 * @return 業務進捗の検索タイプ
	 */
	public ProgressIyakuSearchType getProgressIyakuSearchType() {

		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		ProgressIyakuSearchType iyakuType = ProgressIyakuSearchTypeLogic.getIyakuSearchType();

		// 部門ランク３の場合、配下チームが存在するか
		boolean existsTeam = true;
		if(dSosMst != null && dSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){
			try {
				getDpsSosJgiSearchService().searchUnderSosList(dSosMst.getSosCd(), SosListType.SHITEN_LIST);
			} catch (DataNotFoundException e) {
				existsTeam = false;
			}
		}

//		// チーム一覧表示、かつ、チームが存在しない場合は、担当者表示に変更
//		if(iyakuType == ProgressIyakuSearchType.TEAM_LIST && existsTeam == false){
//			iyakuType = ProgressIyakuSearchType.TANTOU_LIST;
//		}

		return iyakuType;
	}

	/**
	 * (医)業務進捗の階層タイプを取得する。
	 *
	 * @return 業務進捗の階層タイプ
	 */
	public ProgressIyakuRank getProgressIyakuRank() {

		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		ProgressIyakuRank rank = ProgressIyakuSearchTypeLogic.getIyakuSearchType().getProgressIyakuRank();

		// 部門ランク３の場合、配下チームが存在するか
		boolean existsTeam = true;
		if(dSosMst != null && dSosMst.getBumonRank() == BumonRank.OFFICE_TOKUYAKUTEN_G){
			try {
				getDpsSosJgiSearchService().searchUnderSosList(dSosMst.getSosCd(), SosListType.SHITEN_LIST);
			} catch (DataNotFoundException e) {
				existsTeam = false;
			}
		}

		// チーム表示、かつ、チームが存在しない場合は、担当者表示に変更
		if(rank == ProgressIyakuRank.TEAM && existsTeam == false){
			rank = ProgressIyakuRank.TANTOU;
		}

		return rank;
	}

	/**
	 * 組織従業員変更リンクを生成する。<br>
	 *
	 * @param linkName リンク名称(NULL不可) リンクのラベル
	 * @param funcName 関数名(NULL不可) リンクのJavaScript関数名
	 * @param defFlg デフォルト設定フラグ(NULL可) 組織従業員をデフォルトの状態に戻すか
	 * @param initFlg 初期表示フラグ(NULL可) 初期表示設定にするか
	 * @param sosCd 組織コード(NULL可)
	 * @param jgiNo 従業員コード(NULL可)
	 * @param redirectPath リダイレクト先パス(NULL可)
	 * @param style CSSスタイル(NULL可)
	 * @return 変更リンク
	 */
	public String createChangeLink(String linkName, String funcName, String defFlg, String initFlg, String sosCd, String jgiNo, String redirectPath, String style) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href='#' ");
		if (StringUtils.isNotBlank(style)) {
			sb.append(style);
		}
		sb.append("onclick='return ");
		sb.append(funcName + "(\"" + defFlg + "\",\"" + initFlg + "\",\"" + sosCd + "\",\"" + jgiNo + "\",\"" + redirectPath + "\")'>");
		sb.append(linkName);
		sb.append("</a>");
		return sb.toString();
	}

	/**
	 * (医)営業所計画のステータスを取得する。
	 *
	 * @param officePlanStatSum 営業所計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @param category カテゴリ
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressOfficePlanStatus(OfficePlanStatSum officePlanStatSum, ProgressIyakuRank rank, String sosCd, String category) {
		int totalCount = officePlanStatSum.getTotalCount();
		int draftCount = officePlanStatSum.getDraft();
		int completed = officePlanStatSum.getCompleted();

		String urlParams = "";
		if (StringUtils.isNotBlank(category)) {
			urlParams = "?category=" + category;
		}

		//add Start 2022/7/25 H.Futagami 2022年4月組織変更対応  トップ画面 エリア計画の表示修正
		if (officePlanStatSum.getPlanLevelOffice().equals("0")) {
			return new CodeAndValue(ProgressStatus.NOTHING.toString(), "");
		}
		//add End 2022/7/25 H.Futagami 2022年4月組織変更対応  トップ画面 エリア計画の表示修正

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == completed) {
			return new CodeAndValue(ProgressStatus.END.toString(), "確定");
		}

		// ------------------------
		// !一部確定
		// ------------------------
		if (completed > 0) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", sosCd, "", "dps300C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部確定");
			}
		}

		// ------------------------
		// 下書
		// ------------------------
		if (draftCount > 0) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps300C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "下書");
			}
		}

		// ------------------------
		// 未登録
		// ------------------------
		if (rank == ProgressIyakuRank.OFFICE) {
			String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps300C00F00" + urlParams, PROGRESS_LINK_FONT);
			return new CodeAndValue(ProgressStatus.ING.toString(), link);
		}
		return new CodeAndValue(ProgressStatus.ING.toString(), "下書");
	}

	/**
	 * (医)担当者別計画試算状況のステータスを取得する。
	 *
	 * @param officePlanStatSum 営業所計画ステータスのサマリ
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressEstimationStatus(OfficePlanStatSum officePlanStatSum, MrPlanStatSum mrPlanStatSum, ProgressIyakuRank rank, String sosCd) {
		int estimated = mrPlanStatSum.getEstimated();
		int estimating = mrPlanStatSum.getEstimating();
		int totalCount = mrPlanStatSum.getTotalCount();

		// ------------------------
		// 試算済
		// ------------------------
		if (totalCount == estimated) {
			return new CodeAndValue(ProgressStatus.END.toString(), "試算済");
		}

		// ------------------------
		// !一部試算済
		// ------------------------
		if (estimated > 0) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("一部<br>試算済", "dps000C00F00Change", "", "", sosCd, "", "dps301C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>試算済");
			}
		}

		// ------------------------
		// 試算中
		// ------------------------
		if (estimating == totalCount) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("試算中", "dps000C00F00Change", "", "", sosCd, "", "dps301C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "試算中");
			}
		}

		// ------------------------
		// !一部試算中
		// ------------------------
		if (estimating > 0) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("一部<br>試算中", "dps000C00F00Change", "", "", sosCd, "", "dps301C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>試算中");
			}
		}

		// ------------------------
		// 試算前
		// ------------------------
		int oTotalCount = officePlanStatSum.getTotalCount();
		int oCompleted = officePlanStatSum.getCompleted();
		if (oTotalCount == oCompleted) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("試算前", "dps000C00F00Change", "", "", sosCd, "", "dps301C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "試算前");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)チーム別計画ＡＬ公開のステータスを取得する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressTeamPlanAlOpenStatus(MrPlanStatSum mrPlanStatSum, ProgressIyakuRank rank, String sosCd) {
		int estimated = mrPlanStatSum.getEstimated();
		int teamPlanOpened = mrPlanStatSum.getTeamPlanOpened();
		int totalCount = mrPlanStatSum.getTotalCount();

		// ------------------------
		// 公開中
		// ------------------------
		if (totalCount == teamPlanOpened) {
			return new CodeAndValue(ProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// !一部公開中
		// ------------------------
		if (teamPlanOpened > 0) {
			if ((rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM)) {
				String link = createChangeLink("一部<br>公開中", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>公開中");
			}
		}

		// ------------------------
		// 公開前
		// ------------------------
		if (totalCount == estimated) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("公開前", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "公開前");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)チーム別計画所長確定のステータスを取得する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressTeamPlanMasterFixStatus(MrPlanStatSum mrPlanStatSum, ProgressIyakuRank rank, String sosCd) {
		int teamPlanOpened = mrPlanStatSum.getTeamPlanOpened();
		int teamPlanCompleted = mrPlanStatSum.getTeamPlanCompleted();
		int totalCount = mrPlanStatSum.getTotalCount();

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == teamPlanCompleted) {
			return new CodeAndValue(ProgressStatus.END.toString(), "確定");
		}

		// ------------------------
		// !一部確定
		// ------------------------
		if (teamPlanCompleted > 0) {
			if ((rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM)) {
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部確定");
			}
		}

		// ------------------------
		// 下書
		// ------------------------
		if (totalCount == teamPlanOpened) {
			if ((rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM)) {
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "下書");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)担当者別計画ＡＬ公開のステータスを取得する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressMrPlanAlOpenStatus(MrPlanStatSum mrPlanStatSum, ProgressIyakuRank rank, String sosCd) {
		int teamPlanCompleted = mrPlanStatSum.getTeamPlanCompleted();
		int mrPlanOpened = mrPlanStatSum.getMrPlanOpened();
		int totalCount = mrPlanStatSum.getTotalCount();

		// ------------------------
		// 公開中
		// ------------------------
		if (totalCount == mrPlanOpened) {
			return new CodeAndValue(ProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// !一部公開中
		// ------------------------
		if (mrPlanOpened > 0) {
			if ((rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM)) {
				String link = createChangeLink("一部<br>公開中", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>公開中");
			}
		}

		// ------------------------
		// 公開前
		// ------------------------
		if (totalCount == teamPlanCompleted) {
			if ((rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM)) {
				String link = createChangeLink("公開前", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "公開前");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)担当者別計画ＡＬ修正のステータスを取得する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressMrPlanAlUpdateStatus(MrPlanStatSum mrPlanStatSum, ProgressIyakuRank rank, String sosCd) {
		int mrPlanOpened = mrPlanStatSum.getMrPlanOpened();
		int mrPlanInputFinished = mrPlanStatSum.getMrPlanInputFinished();
		int totalCount = mrPlanStatSum.getTotalCount();

		// ------------------------
		// 入力完了
		// ------------------------
		if (totalCount == mrPlanInputFinished) {
			return new CodeAndValue(ProgressStatus.END.toString(), "入力完了");
		}

		// ------------------------
		// !一部完了
		// ------------------------
		if (mrPlanInputFinished > 0) {
			if ((rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM)) {
				String link = createChangeLink("一部完了", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部完了");
			}
		}

		// ------------------------
		// 下書
		// ------------------------
		if (totalCount == mrPlanOpened) {
			if ((rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM)) {
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "下書");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)担当者別計画所長確定のステータスを取得する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressMrPlanMasterFixStatus(MrPlanStatSum mrPlanStatSum, ProgressIyakuRank rank, String sosCd, Integer jgiNo) {
		int mrPlanOpened = mrPlanStatSum.getMrPlanOpened();
		int mrPlanCompleted = mrPlanStatSum.getMrPlanCompleted();
		int totalCount = mrPlanStatSum.getTotalCount();

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == mrPlanCompleted) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("確定", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.END.toString(), "確定");
			}
		}

		// ------------------------
		// !一部確定
		// ------------------------
		if (mrPlanCompleted > 0) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部確定");
			}
		}

		// ------------------------
		// 下書
		// ------------------------
		if (totalCount == mrPlanOpened) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps302C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "下書");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設医師別計画配分状況のステータスを取得する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsDocPlanDistStatus(MrPlanStatSum mrPlanStatSum, InsDocPlanStatSum insDocPlanStatSum, ProgressIyakuRank rank, String sosCd, Integer jgiNo) {

		// 医師別計画立案対象の品目がない場合
		if(insDocPlanStatSum == null){
			return new CodeAndValue(ProgressStatus.NOTHING.toString(), "－");
		}

		int docPlanStatDisted = insDocPlanStatSum.getDisted();
		int docPlanStatDisting = insDocPlanStatSum.getDisting();
		int totalCount = insDocPlanStatSum.getTotalCount();

		// ------------------------
		// 配分済
		// ------------------------
		if (totalCount == docPlanStatDisted) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("配分済", "dps000C00F00Change", "", "", sosCd, "", "dps601C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("配分済", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps601C01F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.END.toString(), "配分済");
			}
		}

		// ------------------------
		// !一部配分済
		// ------------------------
		if (docPlanStatDisted > 0) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("一部<br>配分済", "dps000C00F00Change", "", "", sosCd, "", "dps601C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("一部<br>配分済", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps601C01F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>配分済");
			}
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (docPlanStatDisting == totalCount) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("配分中", "dps000C00F00Change", "", "", sosCd, "", "dps600C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "配分中");
			}
		}

		// ------------------------
		// !一部配分中
		// ------------------------
		if (docPlanStatDisting > 0) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("一部<br>配分中", "dps000C00F00Change", "", "", sosCd, "", "dps600C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>配分中");
			}
		}

		// ------------------------
		// 配分前
		// ------------------------
		if (mrPlanStatSum != null) {
			int tTotalCount = mrPlanStatSum.getTotalCount();
			int tCompleted = mrPlanStatSum.getMrPlanCompleted();
			if (tTotalCount == tCompleted) {
				if (rank == ProgressIyakuRank.OFFICE) {
					String link = createChangeLink("配分前", "dps000C00F00Change", "", "", sosCd, "", "dps600C00F00", PROGRESS_LINK_FONT);
					return new CodeAndValue(ProgressStatus.ING.toString(), link);
				} else {
					return new CodeAndValue(ProgressStatus.ING.toString(), "配分前");
				}
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * NOTE:2014下期向け改定により、未使用（配分→確定になるため）
	 * (医)施設医師別計画ＭＲ公開のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @return CodeAndValue 業務進捗を表すステータス
	 * @deprecated
	 */
	public CodeAndValue progressInsDocPlanMrOpenStatus(InsDocPlanStatSum insDocPlanStatSum, ProgressIyakuRank rank, String sosCd, Integer jgiNo) {

		if(insDocPlanStatSum == null){
			return new CodeAndValue(ProgressStatus.NOTHING.toString(), "－");
		}

		int docPlanOpened = insDocPlanStatSum.getPlanOpened();
		int totalCount = insDocPlanStatSum.getTotalCount();
		int docPlanStatDisted = insDocPlanStatSum.getDisted();

		// ------------------------
		// 公開中
		// ------------------------
		if (totalCount == docPlanOpened) {
			return new CodeAndValue(ProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// !一部公開中
		// ------------------------
		if (docPlanOpened > 0) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("一部<br>公開中", "dps000C00F00Change", "", "", sosCd, "", "dps601C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("一部<br>公開中", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps601C01F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>公開中");
			}
		}

		// ------------------------
		// 公開前
		// ------------------------
		if (totalCount == docPlanStatDisted) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("公開前", "dps000C00F00Change", "", "", sosCd, "", "dps601C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "公開前");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * NOTE:2014下期向け改定により、未使用（配分→確定になるため）
	 * (医)施設医師別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 * @deprecated
	 */
	public CodeAndValue progressInsDocPlanMrFixStatus(InsDocPlanStatSum insDocPlanStatSum, ProgressIyakuRank rank, String sosCd, Integer jgiNo) {

		if(insDocPlanStatSum == null){
			return new CodeAndValue(ProgressStatus.NOTHING.toString(), "－");
		}

		int insDocPlanCompleted = insDocPlanStatSum.getPlanCompleted();
		int totalCount = insDocPlanStatSum.getTotalCount();
		int docPlanOpened = insDocPlanStatSum.getPlanOpened();

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == insDocPlanCompleted) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("確定", "dps000C00F00Change", "", "", sosCd, "", "dps601C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps601C01F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.END.toString(), "確定");
			}
		}

		// ------------------------
		// ！一部確定
		// ------------------------
		if (insDocPlanCompleted > 0) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", sosCd, "", "dps601C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps601C01F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部確定");
			}
		}

		// ------------------------
		// 下書
		// ------------------------
		if (totalCount == docPlanOpened) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps601C00F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps601C01F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "下書");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設特約店別計画配分状況(ＭＭＰ品)のステータスを取得する。
	 *
	 * @param mrPlanStatSum 担当者別計画ステータスのサマリ
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリ
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param category カテゴリ
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanDistStatus(MrPlanStatSum mrPlanStatSum, InsDocPlanStatSum insDocPlanStatSum, InsWsPlanStatSum insWsPlanStatSum, ProgressIyakuRank rank, String sosCd, Integer jgiNo, String category) {
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();
		int wsPlanStatDisting = insWsPlanStatSum.getDisting();
		int totalCount = insWsPlanStatSum.getTotalCount();
		String urlParams = "";
		if (StringUtils.isNotBlank(category)) {
			urlParams = "?category=" + category;
		}

		// ------------------------
		// 配分済
		// ------------------------
		if (totalCount == wsPlanStatDisted) {
			return new CodeAndValue(ProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// !一部配分済
		// ------------------------
		if (wsPlanStatDisted > 0) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
//				String link = createChangeLink("一部<br>配分済", "dps000C00F00Change", "", "", sosCd, "", "dps401C00F00" + urlParams, PROGRESS_LINK_FONT);
				String link = createChangeLink("一部<br>配分済", "dps000C00F00Change", "", "", sosCd, "", "dps400C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("一部<br>配分済", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>配分済");
			}
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (wsPlanStatDisting == totalCount) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("配分中", "dps000C00F00Change", "", "", sosCd, "", "dps401C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("配分中", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "配分中");
			}
		}

		// ------------------------
		// !一部配分中
		// ------------------------
		if (wsPlanStatDisting > 0) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				String link = createChangeLink("一部<br>配分中", "dps000C00F00Change", "", "", sosCd, "", "dps401C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				String link = createChangeLink("一部<br>配分中", "dps000C00F00Change", "", "", "",  jgiNo.toString(), "dps401C01F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>配分中");
			}
		}

		// ------------------------
		// 配分前
		// ------------------------
		if (mrPlanStatSum != null) {
			int tTotalCount = mrPlanStatSum.getTotalCount();
			int tCompleted = mrPlanStatSum.getMrPlanCompleted();

			// 施設医師別計画の立案品目がない場合
			if(insDocPlanStatSum == null) {
				insDocPlanStatSum = new InsDocPlanStatSum();
				insDocPlanStatSum.setTotalCount(0);
				insDocPlanStatSum.setPlanCompleted(0);
			}

			int dTotalCount = insDocPlanStatSum.getTotalCount();
			int dCompleted = insDocPlanStatSum.getPlanCompleted();
			if (tTotalCount == tCompleted && dTotalCount == dCompleted) {
				if (rank == ProgressIyakuRank.OFFICE ) {
					String link = createChangeLink("配分前", "dps000C00F00Change", "", "", sosCd, "", "dps400C00F00" + urlParams, PROGRESS_LINK_FONT);
					return new CodeAndValue(ProgressStatus.ING.toString(), link);
				} else {
					return new CodeAndValue(ProgressStatus.ING.toString(), "配分前");
				}
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設特約店別計画配分状況(仕入品)のステータスを取得する。
	 *
	 * @param officePlanStatSum 営業所別計画ステータスのサマリ
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanDistStatus(OfficePlanStatSum officePlanStatSum, InsWsPlanStatSum insWsPlanStatSum, ProgressIyakuRank rank, String sosCd) {
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();
		int wsPlanStatDisting = insWsPlanStatSum.getDisting();
		int totalCount = insWsPlanStatSum.getTotalCount();
		final String urlParams = "?category=2";

		// ------------------------
		// 配分済
		// ------------------------
		if (totalCount == wsPlanStatDisted) {
			return new CodeAndValue(ProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// !一部配分済
		// ------------------------
		if (wsPlanStatDisted > 0) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("一部<br>配分済", "dps000C00F00Change", "", "", sosCd, "", "dps400C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>配分済");
			}
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (wsPlanStatDisting == totalCount) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("配分中", "dps000C00F00Change", "", "", sosCd, "", "dps400C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "配分中");
			}
		}

		// ------------------------
		// !一部配分中
		// ------------------------
		if (wsPlanStatDisting > 0) {
			if (rank == ProgressIyakuRank.OFFICE) {
				String link = createChangeLink("一部<br>配分中", "dps000C00F00Change", "", "", sosCd, "", "dps400C00F00" + urlParams, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部<br>配分中");
			}
		}

		// ------------------------
		// 配分前
		// ------------------------
		if (officePlanStatSum != null) {
			int oTotalCount = officePlanStatSum.getTotalCount();
			int oCompleted = officePlanStatSum.getCompleted();
			if (oTotalCount == oCompleted) {
				if (rank == ProgressIyakuRank.OFFICE) {
					String link = createChangeLink("配分前", "dps000C00F00Change", "", "", sosCd, "", "dps400C00F00" + urlParams, PROGRESS_LINK_FONT);
					return new CodeAndValue(ProgressStatus.ING.toString(), link);
				} else {
					return new CodeAndValue(ProgressStatus.ING.toString(), "配分前");
				}
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設特約店別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	//mod Start 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
	//public CodeAndValue progressInsWsPlanMrFixStatus(InsWsPlanStatSum insWsPlanStatSum, ProgressIyakuRank rank, String sosCd, Integer jgiNo) {
	public CodeAndValue progressInsWsPlanMrFixStatus(InsWsPlanStatSum insWsPlanStatSum, ProgressIyakuRank rank, String sosCd, Integer jgiNo, String category) {
	//mod End 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
		int insWsPlanCompleted = insWsPlanStatSum.getPlanCompleted();
		int totalCount = insWsPlanStatSum.getTotalCount();
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == insWsPlanCompleted) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				//mod Start 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				//String link = createChangeLink("確定", "dps000C00F00Change", "", "", sosCd, "", "dps401C00F00", PROGRESS_LINK_FONT);
				String link = createChangeLink("確定", "dps000C00F00Change", "", "", sosCd, "", "dps401C01F00?category=" + category, PROGRESS_LINK_FONT);
				//mod End 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				//mod Start 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				//String link = createChangeLink("確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00", PROGRESS_LINK_FONT);
				String link = createChangeLink("確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00?category=" + category, PROGRESS_LINK_FONT);
				//mod End 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.END.toString(), "確定");
			}
		}

		// ------------------------
		// ！一部確定
		// ------------------------
		if (insWsPlanCompleted > 0) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				//mod Start 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				//String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", sosCd, "", "dps401C00F00", PROGRESS_LINK_FONT);
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", sosCd, "", "dps401C01F00?category=" + category, PROGRESS_LINK_FONT);
				//mod End 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				//mod Start 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				//String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00", PROGRESS_LINK_FONT);
				String link = createChangeLink("一部確定", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00?category=" + category, PROGRESS_LINK_FONT);
				//mod End 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部確定");
			}
		}

		// ------------------------
		// 下書
		// ------------------------
		if (totalCount == wsPlanStatDisted) {
			if (rank == ProgressIyakuRank.OFFICE || rank == ProgressIyakuRank.TEAM) {
				//mod Start 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				//String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps401C00F00", PROGRESS_LINK_FONT);
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", sosCd, "", "dps401C01F00?category=" + category, PROGRESS_LINK_FONT);
				//mod End 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (rank == ProgressIyakuRank.TANTOU) {
				//mod Start 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				//String link = createChangeLink("下書", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00", PROGRESS_LINK_FONT);
				String link = createChangeLink("下書", "dps000C00F00Change", "", "", "", jgiNo.toString(), "dps401C01F00?category=" + category, PROGRESS_LINK_FONT);
				//mod End 2022/8/18 H.Futagami 2022年4月組織変更対応 top画面から施設特約店別計画品目一覧画面へ遷移時のカテゴリ条件変更
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "下書");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)特約店別計画配分状況のステータスを取得する。
	 *
	 * @param wsPlanStatusSummary 特約店別計画サマリ
	 * @param rank 業務進捗の取得階層
	 * @param prodCategoryString 品目カテゴリを示す文字列
	 * @return 特約店別計画配分状況
	 */
	public CodeAndValue progressWsPlanDistStatus(WsPlanStatusSummary wsPlanStatusSummary, ProgressIyakuRank rank, String prodCategoryString) {
		int wsPlanStatDisted = wsPlanStatusSummary.getDisted();
		int wsPlanStatDisting = wsPlanStatusSummary.getDisting();
		int totalCount = wsPlanStatusSummary.getTotalCount();
		String linkString = "dps500C00F00?category=" + prodCategoryString;
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();

		if (totalCount == 0) {
			//画面表示の背景色選択という目的で、本来「配分前」を意味する"NOTを流用。
			return new CodeAndValue(ProgressStatus.NOT.toString(), "対象品目なし");
		}

		// ------------------------
		// 配分済
		// ------------------------
		if (totalCount == wsPlanStatDisted) {
			return new CodeAndValue(ProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// !一部配分済
		// ------------------------
		if (wsPlanStatDisted > 0) {
			if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
				String link = createChangeLink("一部配分済", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部配分済");
			}
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (wsPlanStatDisting == totalCount) {
			if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
				String link = createChangeLink("配分中", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "配分中");
			}
		}

		// ------------------------
		// !一部配分中
		// ------------------------
		if (wsPlanStatDisting > 0) {
			if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
				String link = createChangeLink("一部配分中", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部配分中");
			}
		}

		// ------------------------
		// 配分前
		// ------------------------
		if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
			String link = createChangeLink("配分前", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
			return new CodeAndValue(ProgressStatus.NOT.toString(), link);
		} else {
			return new CodeAndValue(ProgressStatus.NOT.toString(), "配分前");
		}
	}

	/**
	 * (医)特約店別計画スライド状況のステータスを取得する。
	 *
	 * @param wsPlanStatusSummary 特約店別計画サマリ
	 * @param rank 業務進捗の取得階層
	 * @param prodCategoryString 品目カテゴリを示す文字列
	 * @return 特約店別計画配分状況
	 */
	public CodeAndValue progressWsPlanSlideStatus(WsPlanStatusSummary wsPlanStatusSummary, ProgressIyakuRank rank, String prodCategoryString) {
		int wsPlanStatSlided = wsPlanStatusSummary.getSlided();
		int wsPlanStatSliding = wsPlanStatusSummary.getSliding();
		int totalCount = wsPlanStatusSummary.getTotalCount();
		String linkString = "dps501C00F00?category=" + prodCategoryString;
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// カテゴリの品目が０件　※2018上期はSPBUカテゴリの品目が０件
		// ------------------------
		if (totalCount == 0) {
			//画面表示の背景色選択という目的で、本来「スライド前」を意味する"NOTを流用。
			return new CodeAndValue(ProgressStatus.NOT.toString(), "対象品目なし");
		}

		// ------------------------
		// スライド済
		// ------------------------
		if (totalCount == wsPlanStatSlided) {
			return new CodeAndValue(ProgressStatus.END.toString(), "スライド済");
		}

		// ------------------------
		// !一部スライド済
		// ------------------------
		if (wsPlanStatSlided > 0) {
			if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
				String link = createChangeLink("一部スライド済", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部スライド済");
			}
		}

		// ------------------------
		// スライド中
		// ------------------------
		if (wsPlanStatSliding == totalCount) {
			if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
				String link = createChangeLink("スライド中", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "スライド中");
			}
		}

		// ------------------------
		// !一部スライド中
		// ------------------------
		if (wsPlanStatSliding > 0) {
			if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
				String link = createChangeLink("一部スライド中", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "一部スライド中");
			}
		}

		// ------------------------
		// スライド前
		// ------------------------
		if (rank == ProgressIyakuRank.SITEN && dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
			String link = createChangeLink("スライド前", "dps000C00F00Change", "", "", "", "", linkString, PROGRESS_LINK_FONT);
			return new CodeAndValue(ProgressStatus.NOT.toString(), link);
		} else {
			return new CodeAndValue(ProgressStatus.NOT.toString(), "スライド前");
		}
	}

	/**
	 * (ワ)施設特約店別計画配分状況のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanDistStatusForVac(InsWsPlanStatSum insWsPlanStatSum) {
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();
		int wsPlanStatDisting = insWsPlanStatSum.getDisting();
		int totalCount = insWsPlanStatSum.getTotalCount();

		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 配分済
		// ------------------------
		if (totalCount == wsPlanStatDisted) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				return new CodeAndValue(ProgressStatus.END.toString(), "配分済");
			}
		}

		// ------------------------
		// !一部配分済
		// ------------------------
		if (wsPlanStatDisted > 0) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("一部<br>配分済", "dps000C00F50Change", "", "", "", "", "dps400C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (wsPlanStatDisting == totalCount) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("配分中", "dps000C00F50Change", "", "", "", "", "dps400C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}
		}

		// ------------------------
		// !一部配分中
		// ------------------------
		if (wsPlanStatDisting > 0) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("一部<br>配分中", "dps000C00F50Change", "", "", "", "", "dps400C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}
		}

		// ------------------------
		// 配分前
		// ------------------------
		if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
			String link = createChangeLink("配分前", "dps000C00F50Change", "", "", "", "", "dps400C03F00", PROGRESS_LINK_FONT);
			return new CodeAndValue(ProgressStatus.NOT.toString(), link);
		} else {
			return new CodeAndValue(ProgressStatus.NOT.toString(), "配分前");
		}
	}

	/**
	 * (ワ)施設特約店別計画ＭＲ公開のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrOpenStatusForVac(InsWsPlanStatSum insWsPlanStatSum) {
		int wsPlanOpened = insWsPlanStatSum.getPlanOpened();
		int totalCount = insWsPlanStatSum.getTotalCount();
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();

		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 公開中
		// ------------------------
		if (totalCount == wsPlanOpened) {
			return new CodeAndValue(ProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// !一部公開中
		// ------------------------
		if (wsPlanOpened > 0) {

			// 本部、本部ワクチンＧ
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("一部<br>公開中", "dps000C00F50Change", "", "", "", "", "dps401C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}

			// 小児科ＡＣ (J19-0010 対応・コメントのみ修正)
			else if (settingUser.isMatch(WAKUTIN_AL)) {
				String link = createChangeLink("一部<br>公開中", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}

			// 小児科ＭＲ (J19-0010 対応・コメントのみ修正)
			else if (settingUser.isMatch(WAKUTIN_MR)) {
				String link = createChangeLink("一部<br>公開中", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}
		}

		// ------------------------
		// 公開前
		// ------------------------

		if (totalCount == wsPlanStatDisted) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("公開前", "dps000C00F50Change", "", "", "", "", "dps401C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else if (settingUser.isMatch(WAKUTIN_AL,WAKUTIN_MR)) {
				return new CodeAndValue(ProgressStatus.NOT.toString(), "公開前");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (ワ)施設特約店別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrFixStatusForVac(InsWsPlanStatSum insWsPlanStatSum) {
		int insWsPlanCompleted = insWsPlanStatSum.getPlanCompleted();
		int totalCount = insWsPlanStatSum.getTotalCount();
		int wsPlanOpened = insWsPlanStatSum.getPlanOpened();

		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == insWsPlanCompleted) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("確定", "dps000C00F50Change", "", "", "", "", "dps401C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);

			} else if (settingUser.isMatch(WAKUTIN_AL)) {
				String link = createChangeLink("確定", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);

			} else if (settingUser.isMatch(WAKUTIN_MR)) {
				String link = createChangeLink("確定", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.END.toString(), link);
			}
		}

		// ------------------------
		// ！一部確定
		// ------------------------
		if (insWsPlanCompleted > 0) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("一部確定", "dps000C00F50Change", "", "", "", "", "dps401C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);

			} else if (settingUser.isMatch(WAKUTIN_AL)) {
				String link = createChangeLink("一部確定", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);

			} else if (settingUser.isMatch(WAKUTIN_MR)) {
				String link = createChangeLink("一部確定", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}
		}

		// ------------------------
		// 下書
		// ------------------------
		if (totalCount == wsPlanOpened) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("下書", "dps000C00F50Change", "", "", "", "", "dps401C03F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);

			} else if (settingUser.isMatch(WAKUTIN_AL)) {
				String link = createChangeLink("下書", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);

			} else if (settingUser.isMatch(WAKUTIN_MR)) {
				String link = createChangeLink("下書", "dps000C00F50Change", "", "", "", "", "dps401C04F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(ProgressStatus.NOT.toString(), "");
	}

	/**
	 * (ワ)特約店別計画スライド状況のステータスを取得する。
	 *
	 * @param wsPlanStatusSummary 特約店別計画サマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @param prodCategoryString 品目カテゴリを示す文字列
	 * @return 特約店別計画配分状況
	 */
	public CodeAndValue progressWsPlanSlideStatusForVac(WsPlanStatusForVac wsPlanStatusSummary) {
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// スライド済
		// ------------------------
		if (wsPlanStatusSummary.getStatusSlideForWs() == StatusForWsSlide.SLIDED) {
			return new CodeAndValue(ProgressStatus.END.toString(), "スライド済");
		}

		// ------------------------
		// !一部スライド済
		// ------------------------
		if (wsPlanStatusSummary.getStatusSlideForWs() == StatusForWsSlide.SLIDING) {
			if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
				String link = createChangeLink("スライド中", "dps000C00F50Change", "", "", "", "", "dps501C02F00", PROGRESS_LINK_FONT);
				return new CodeAndValue(ProgressStatus.ING.toString(), link);
			} else {
				return new CodeAndValue(ProgressStatus.ING.toString(), "スライド中");
			}

		}

		if (settingUser.isMatch(HONBU, HONBU_WAKUTIN_G)) {
			String link = createChangeLink("スライド前", "dps000C00F50Change", "", "", "", "", "dps501C02F00", PROGRESS_LINK_FONT);
			return new CodeAndValue(ProgressStatus.NOT.toString(), link);
		} else {
			return new CodeAndValue(ProgressStatus.NOT.toString(), "スライド前");
		}
	}

	/**
	 * [支援×医薬]を使用可能かを返す。
	 *
	 * @return [支援×医薬]を使用可能な場合に真
	 */
	private static boolean canDpsIyaku() {
		SysManage sysManage = SecurityTool.getSysManage();
		SysClass sysClass = sysManage.getSysClass();
		if (sysClass == SysClass.DPM) {
			return false;
		}
		SysType sysType = sysManage.getSysType();
		if (sysType == SysType.VACCINE) {
			return false;
		}
		return true;
	}

	/**
	 * [支援×ワクチン]を使用可能かを返す。
	 *
	 * @return [支援×ワクチン]を使用可能な場合に真
	 */
	private static boolean canDpsWakutin() {
		SysManage sysManage = SecurityTool.getSysManage();
		SysClass sysClass = sysManage.getSysClass();
		if (sysClass == SysClass.DPM) {
			return false;
		}
		SysType sysType = sysManage.getSysType();
		if (sysType == SysType.IYAKU) {
			return false;
		}
		return true;
	}

	// --------------------------------------------------------------------------------------
	// 施設医師別計画 品目一覧画面表示用
	// --------------------------------------------------------------------------------------

	/**
	 * (医)施設医師別計画配分状況のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリ
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsDocPlanDistStatus(InsDocPlanStatSum insDocPlanStatSum) {
		int docPlanStatDisted = insDocPlanStatSum.getDisted();
		int docPlanStatDisting = insDocPlanStatSum.getDisting();
		int totalCount = insDocPlanStatSum.getTotalCount();

		// ------------------------
		// 配分済(配分実行可能)
		// ------------------------
		if (totalCount == docPlanStatDisted) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// !一部配分済
		// ------------------------
		if (docPlanStatDisted > 0) {
			return new CodeAndValue(InsDocPlanProgressStatus.PARTLY_END.toString(), "一部配分済");
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (docPlanStatDisting == totalCount) {
			return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "配分中");
		}

		// ------------------------
		// !一部配分中
		// ------------------------
		if (docPlanStatDisting > 0) {
			return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "一部配分中");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設医師別計画配分状況のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータス
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsDocPlanDistStatus(InsDocPlanStatus insDocPlanStatus) {

		StatusForInsDocPlan statusForInsDocPlan = insDocPlanStatus.getStatusForInsDocPlan();

		// ------------------------
		// 配分済(配分実行可能)
		// ------------------------
		if (statusForInsDocPlan == StatusForInsDocPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "配分済");
		}
		if (statusForInsDocPlan == StatusForInsDocPlan.PLAN_OPENED) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "配分済");
		}
		if (statusForInsDocPlan == StatusForInsDocPlan.DISTED) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (statusForInsDocPlan == StatusForInsDocPlan.DISTING) {
			return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "配分中");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設医師別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsDocPlanMrFixStatus(InsDocPlanStatSum insDocPlanStatSum) {
		int insDocPlanCompleted = insDocPlanStatSum.getPlanCompleted();
		int totalCount = insDocPlanStatSum.getTotalCount();
		int docPlanOpened = insDocPlanStatSum.getPlanOpened();

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == insDocPlanCompleted) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "確定");
		}

		// ------------------------
		// ！一部確定
		// ------------------------
		if (insDocPlanCompleted > 0) {
			return new CodeAndValue(InsDocPlanProgressStatus.PARTLY_END.toString(), "一部確定");
		}

		// ------------------------
		// 確定前
		// ------------------------
		if (totalCount == docPlanOpened) {
			return new CodeAndValue(InsDocPlanProgressStatus.ING.toString(), "");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設医師別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータス
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsDocPlanMrFixStatus(InsDocPlanStatus insDocPlanStatus) {

		StatusForInsDocPlan statusForInsDocPlan = insDocPlanStatus.getStatusForInsDocPlan();

		// ------------------------
		// 確定
		// ------------------------
		if (statusForInsDocPlan == StatusForInsDocPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "確定");
		}

		// ------------------------
		// 確定前
		// ------------------------
		if (statusForInsDocPlan == StatusForInsDocPlan.PLAN_OPENED) {
			return new CodeAndValue(InsDocPlanProgressStatus.ING.toString(), "");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設医師別計画ＭＲ公開のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータスのサマリ
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsDocPlanMrOpenStatus(InsDocPlanStatSum insDocPlanStatSum) {
		int docPlanOpened = insDocPlanStatSum.getPlanOpened();
		int totalCount = insDocPlanStatSum.getTotalCount();
		int docPlanStatDisted = insDocPlanStatSum.getDisted();

		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 公開中
		// ------------------------
		if (totalCount == docPlanOpened) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// !一部公開中
		// ------------------------
		if (docPlanOpened > 0) {
			return new CodeAndValue(InsDocPlanProgressStatus.PARTLY_END.toString(), "一部公開中");
		}

		// ------------------------
		// 公開前
		// ------------------------
		if (totalCount == docPlanStatDisted) {
			if (settingUser.hasAuthOr(EDIT_I_MMP_AUTH_REHAIBUN, EDIT_I_SIRE_AUTH_REHAIBUN, EDIT_W_AUTH_REHAIBUN)) {
				return new CodeAndValue(InsDocPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設医師別計画ＭＲ公開のステータスを取得する。
	 *
	 * @param insDocPlanStatSum 施設医師別計画ステータス
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsDocPlanMrOpenStatus(InsDocPlanStatus insDocPlanStatus) {

		StatusForInsDocPlan statusForInsDocPlan = insDocPlanStatus.getStatusForInsDocPlan();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 公開中
		// ------------------------
		if (statusForInsDocPlan == StatusForInsDocPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "公開中");
		}
		if (statusForInsDocPlan == StatusForInsDocPlan.PLAN_OPENED) {
			return new CodeAndValue(InsDocPlanProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// 公開前(試算済)
		// ------------------------
		if (statusForInsDocPlan == StatusForInsDocPlan.DISTED) {
			if (settingUser.hasAuth(EDIT_I_MMP_AUTH_REHAIBUN, EDIT_I_SIRE_AUTH_REHAIBUN)) {
				return new CodeAndValue(InsDocPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsDocPlanProgressStatus.NOT.toString(), "");
	}

	// --------------------------------------------------------------------------------------
	// 施設特約店別計画 品目一覧画面表示用
	// --------------------------------------------------------------------------------------

	/**
	 * (医・ワ)施設特約店別計画配分状況のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanDistStatus(InsWsPlanStatSum insWsPlanStatSum) {
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();
		int wsPlanStatDisting = insWsPlanStatSum.getDisting();
		int totalCount = insWsPlanStatSum.getTotalCount();

		// ------------------------
		// 配分済(配分実行可能)
		// ------------------------
		if (totalCount == wsPlanStatDisted) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// !一部配分済
		// ------------------------
		if (wsPlanStatDisted > 0) {
			return new CodeAndValue(InsWsPlanProgressStatus.PARTLY_END.toString(), "一部配分済");
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (wsPlanStatDisting == totalCount) {
			return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "配分中");
		}

		// ------------------------
		// !一部配分中
		// ------------------------
		if (wsPlanStatDisting > 0) {
			return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "一部配分中");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}


	/**
	 * (ワのみ)施設特約店別計画ＭＲ公開のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrOpenStatus(InsWsPlanStatSum insWsPlanStatSum) {
		int wsPlanOpened = insWsPlanStatSum.getPlanOpened();
		int totalCount = insWsPlanStatSum.getTotalCount();
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();

		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 公開中
		// ------------------------
		if (totalCount == wsPlanOpened) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// !一部公開中
		// ------------------------
		if (wsPlanOpened > 0) {
			return new CodeAndValue(InsWsPlanProgressStatus.PARTLY_END.toString(), "一部公開中");
		}

		// ------------------------
		// 公開前
		// ------------------------
		if (totalCount == wsPlanStatDisted) {
			if (settingUser.hasAuthOr(EDIT_I_MMP_AUTH_REHAIBUN, EDIT_I_SIRE_AUTH_REHAIBUN, EDIT_W_AUTH_REHAIBUN)) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}
	/**
	 * (医・ワ)施設特約店別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータスのサマリ
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrFixStatus(InsWsPlanStatSum insWsPlanStatSum) {
		int insWsPlanCompleted = insWsPlanStatSum.getPlanCompleted();
		int totalCount = insWsPlanStatSum.getTotalCount();
		int wsPlanOpened = insWsPlanStatSum.getPlanOpened();
		int wsPlanStatDisted = insWsPlanStatSum.getDisted();

		// ------------------------
		// 確定
		// ------------------------
		if (totalCount == insWsPlanCompleted) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "確定");
		}

		// ------------------------
		// ！一部確定
		// ------------------------
		if (insWsPlanCompleted > 0) {
			return new CodeAndValue(InsWsPlanProgressStatus.PARTLY_END.toString(), "一部確定");
		}

		// ------------------------
		// 確定前
		// ------------------------
		SysType sysType = DpUserInfo.getDpUserInfo().getSysType();
		if(sysType == SysType.IYAKU) {
			if (totalCount == wsPlanStatDisted) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		} else if(sysType == SysType.VACCINE) {
			if (totalCount == wsPlanOpened) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		} else {
			return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設特約店別計画配分状況のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータス
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanDistStatus(InsWsPlanStatus insWsPlanStatus) {

		StatusForInsWsPlan statusForInsWsPlan = insWsPlanStatus.getStatusForInsWsPlan();

		// ------------------------
		// 配分済(配分実行可能)
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "配分済");
		}
		if (statusForInsWsPlan == StatusForInsWsPlan.DISTED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "配分済");
		}
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_OPENED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.DISTING) {
			return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "配分中");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医)施設特約店別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータス
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrFixStatus(InsWsPlanStatus insWsPlanStatus) {

		StatusForInsWsPlan statusForInsWsPlan = insWsPlanStatus.getStatusForInsWsPlan();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 確定
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "確定");
		}

		// ------------------------
		// 確定前(配分済)
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.DISTED) {
			if (settingUser.hasAuthOr(EDIT_I_MMP_AUTH_FIX, EDIT_I_SIRE_AUTH_FIX)) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医・ワ)施設特約店別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insWsPlanStatus 施設特約店別計画ステータス
	 * @param category カテゴリ
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrFixStatus(InsWsPlanStatus insWsPlanStatus, String category) {

		StatusForInsWsPlan statusForInsWsPlan = insWsPlanStatus.getStatusForInsWsPlan();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 確定
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "確定");
		}

		//if (!isVaccine(category)) {
			// ------------------------
			// 確定前(配分済)
			// ------------------------
			if (statusForInsWsPlan == StatusForInsWsPlan.DISTED) {
				if (settingUser.hasAuthOr(EDIT_I_MMP_AUTH_FIX, EDIT_I_SIRE_AUTH_FIX)) {
					return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
				}
			}
		//}

		// ------------------------
		// 確定前
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_OPENED) {
			if (settingUser.hasAuth(EDIT_W_AUTH_FIX)) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	// --------------------------------------------------------------------------------------
	// ワクチン 施設特約店別計画 品目一覧画面表示用
	// --------------------------------------------------------------------------------------

	/**
	 * (ワ)施設特約店別計画配分状況のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータス
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanDistStatus(InsWsPlanStatusForVac insWsPlanStatus) {

		StatusForInsWsPlan statusForInsWsPlan = insWsPlanStatus.getStatusForInsWsPlan();

		// ------------------------
		// 配分済(配分実行可能)
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "配分済");
		}
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_OPENED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "配分済");
		}
		if (statusForInsWsPlan == StatusForInsWsPlan.DISTED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "配分済");
		}

		// ------------------------
		// 配分中
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.DISTING) {
			return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "配分中");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (医・ワ)施設特約店別計画ＭＲ公開のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータス
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrOpenStatus(InsWsPlanStatus insWsPlanStatus) {

		StatusForInsWsPlan statusForInsWsPlan = insWsPlanStatus.getStatusForInsWsPlan();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 公開中
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "公開中");
		}
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_OPENED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// 公開前(試算済)
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.DISTED) {
			if (settingUser.hasAuth(EDIT_W_AUTH_REHAIBUN)) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (ワ)施設特約店別計画ＭＲ公開のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータス
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrOpenStatus(InsWsPlanStatusForVac insWsPlanStatus) {

		StatusForInsWsPlan statusForInsWsPlan = insWsPlanStatus.getStatusForInsWsPlan();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 公開中
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "公開中");
		}
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_OPENED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "公開中");
		}

		// ------------------------
		// 公開前(試算済)
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.DISTED) {
			if (settingUser.hasAuth(EDIT_W_AUTH_REHAIBUN)) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * (ワ)施設特約店別計画ＭＲ確定のステータスを取得する。
	 *
	 * @param insWsPlanStatSum 施設特約店別計画ステータス
	 * @param rank 業務進捗の取得階層
	 * @param sosCd 組織コード
	 * @return ProgressStatus 業務進捗を表すステータス
	 */
	public CodeAndValue progressInsWsPlanMrFixStatus(InsWsPlanStatusForVac insWsPlanStatus) {

		StatusForInsWsPlan statusForInsWsPlan = insWsPlanStatus.getStatusForInsWsPlan();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// ------------------------
		// 確定
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_COMPLETED) {
			return new CodeAndValue(InsWsPlanProgressStatus.END.toString(), "確定");
		}

		// ------------------------
		// 確定前
		// ------------------------
		if (statusForInsWsPlan == StatusForInsWsPlan.PLAN_OPENED) {
			if (settingUser.hasAuth(EDIT_W_AUTH_FIX)) {
				return new CodeAndValue(InsWsPlanProgressStatus.ING.toString(), "");
			}
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	// --------------------------------------------------------------------------------------
	// 調整リンク
	// --------------------------------------------------------------------------------------

	/**
	 * 営業所編集画面へ遷移する調整金額表示文字列を生成する。<br>
	 * ※営業所編集画面への参照権限がない場合はリンクではなく、ラベルのみ表示
	 *
	 * @param choseiFlg 調整金額有無を示すフラグ
	 * @param update 更新日
	 * @param sosCd 組織コード
	 * @param prodCategory 品目カテゴリ
	 * @return 調整金額表示文字列
	 */
	public String createOfficeChoseiString(Boolean choseiFlg, Date update, String sosCd, String prodCategory) {
		StringBuilder sb = new StringBuilder();
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		if (settingUser.hasAuth(REFFER_DPS_I_MMP_OFFICE_PLAN, REFFER_DPS_I_SIRE_OFFICE_PLAN)) {
			sb.append("<a href=\"#\" onclick=\"return officeG('");
			sb.append(sosCd);
			sb.append("','");
			sb.append(prodCategory);
			sb.append("');\">");
			if (choseiFlg != null && choseiFlg) {
				sb.append("あり</a>");
			} else {
				sb.append("なし</a>");
			}
		} else {
			if (choseiFlg != null && choseiFlg) {
				sb.append("<font color=\"#FF0000\">あり</font>");
			} else {
				sb.append("なし");
			}
		}
		if (update != null) {
			sb.append("<br>");
			sb.append(DateUtil.toString(update, "MM/dd HH:mm"));
		} else {
			sb.append("<br>");
			sb.append("－");
		}
		return sb.toString();
	}

	/**
	 * 施設特約店別計画品目一覧画面へ遷移する調整金額表示文字列を生成する。<br>
	 *
	 * @param choseiFlg 調整金額有無を示すフラグ
	 * @param update 更新日
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCategory 品目カテゴリ
	 * @return 調整金額表示文字列
	 */
	public String createInswsChoseiString(Boolean choseiFlg, Date update, String sosCd, Integer jgiNo, String prodCategory) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"#\" onclick=\"return inswsG('");
		sb.append(sosCd);
		sb.append("','");
		sb.append(jgiNo);
		sb.append("','");
		sb.append(prodCategory);
		sb.append("');\">");
		if (choseiFlg != null && choseiFlg) {
			sb.append("あり</a>");
		} else {
			sb.append("なし</a>");
		}
		if (update != null) {
			sb.append("<br>");
			sb.append(DateUtil.toString(update, "MM/dd HH:mm"));
		} else {
			sb.append("<br>");
			sb.append("－");
		}
		return sb.toString();
	}

	/**
	 * 施設医師別計画品目一覧画面へ遷移する調整金額表示文字列を生成する。<br>
	 *
	 * @param choseiFlg 調整金額有無を示すフラグ
	 * @param update 更新日
	 * @param sosCd 組織コード
	 * @param jgiNo 従業員番号
	 * @param prodCategory 品目カテゴリ
	 * @return 調整金額表示文字列
	 */
	public String createInsdocChoseiString(Boolean choseiFlg, Date update, String sosCd, Integer jgiNo, String prodCategory) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"#\" onclick=\"return insdocG('");
		sb.append(sosCd);
		sb.append("','");
		sb.append(jgiNo);
		sb.append("');\">");
		if (choseiFlg != null && choseiFlg) {
			sb.append("あり</a>");
		} else {
			sb.append("なし</a>");
		}
		if (update != null) {
			sb.append("<br>");
			sb.append(DateUtil.toString(update, "MM/dd HH:mm"));
		} else {
			sb.append("<br>");
			sb.append("－");
		}
		return sb.toString();
	}


	// --------------------------------------------------------------------------------------
	// 計画立案対象外あり/なしの取得
	// --------------------------------------------------------------------------------------
	/**
	 * 計画立案対象外あり/なしの取得
	 * @param count
	 * @param sosCd
	 * @return
	 */
	public CodeAndValue progressExceptPlanStatus(Integer count,String sosCd,String category) {

		//boolean hasExPlan = getDpsCExceptPlanSearchService().hasExceptPlan(sosCd);
		if(count > 0) {
			return new CodeAndValue(ProgressStatus.ING.toString(), "<a href=\"#\" onclick=\"return dps940C00F00('" + sosCd + "','" + category + "')\">あり</a>");
		}
		return new CodeAndValue(ProgressStatus.NOT.toString(), "なし");


	}


	// --------------------------------------------------------------------------------------
	// その他
	// --------------------------------------------------------------------------------------

	/**
	 * (医)試算タイプを取得する。
	 *
	 * @param calcType 試算タイプ
	 * @return CodeAndValue 業務進捗を表すステータス
	 */
	public CodeAndValue progressCalcType(CalcType calcType) {

		if (calcType == null) {
			return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
		}

		switch (calcType) {
			case OFFICE_TEAM_MR:
				return new CodeAndValue(CalcType.OFFICE_TEAM_MR.toString(), "（営→チ→担）");
			case OFFICE_MR:
				return new CodeAndValue(CalcType.OFFICE_MR.toString(), "（営→担）");
		}

		// ------------------------
		// 空白
		// ------------------------
		return new CodeAndValue(InsWsPlanProgressStatus.NOT.toString(), "");
	}

	/**
	 * 営業所長（営業所スタッフ）ユーザかどうかを返す。
	 *
	 * @return 営業所長（営業所スタッフ）ユーザの場合、真
	 */
	public boolean isOfficeAuth() {
		boolean isOfficeAuthFlg = false;
		DpUser settingUser = DpUserInfo.getDpUserInfo().getSettingUser();
		if (settingUser.isMatch(OFFICE_AUTH)) {
			isOfficeAuthFlg = true;
		}
		return isOfficeAuthFlg;
	}

	/**
	 * 業務進捗を表すステータス
	 *
	 * @author tkawabata
	 */
	public enum ProgressStatus {

		/**
		 * 終了
		 */
		END,

		/**
		 * 処理中
		 */
		ING,

		/**
		 * 処理前
		 */
		NOT,

		/**
		 * 対象なし
		 */
		NOTHING;
	}

	/**
	 * 業務進捗を表すステータス
	 *
	 * @author tkawabata
	 */
	public enum InsWsPlanProgressStatus {

		/**
		 * 終了
		 */
		END,

		/**
		 * 一部終了
		 */
		PARTLY_END,

		/**
		 * 処理中
		 */
		ING,

		/**
		 * 処理前
		 */
		NOT;
	}

	/**
	 * 業務進捗を表すステータス
	 *
	 * @author tkawabata
	 */
	public enum InsDocPlanProgressStatus {

		/**
		 * 終了
		 */
		END,

		/**
		 * 一部終了
		 */
		PARTLY_END,

		/**
		 * 処理中
		 */
		ING,

		/**
		 * 処理前
		 */
		NOT;
	}

	/**
	 * 組織で表示可能なカテゴリーリストを取得する。
	 * DATA_VALUE順
	 * @return カテゴリーリスト
	 */
	public List<CategoryInfo> getProdCategoryList(){
		return  getDispBusinessProgressCategoryList();
	}

	/**
	 * 組織で表示可能なカテゴリーリストを取得する。
	 * DATA_SEQ順
	 * @return カテゴリーリスト
	 */
	public List<CategoryInfo> getProdCategoryListByDataSeq(){
		return  getDispBusinessProgressCategoryListBySeq();
	}

	/**
	 * ワクチンであるか
	 * @param category カテゴリ
	 * @return ワクチンであればtrue
	 */
	public boolean isVaccine(String category) {
		return getDpsCodeMasterSearchService().isVaccine(category);
	}
}
