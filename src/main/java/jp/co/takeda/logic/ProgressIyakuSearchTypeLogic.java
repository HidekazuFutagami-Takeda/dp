package jp.co.takeda.logic;

import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * (医)業務進捗の検索タイプを取得するロジッククラス
 *
 * @author tkawabata
 */
public class ProgressIyakuSearchTypeLogic {

	/**
	 * 当サービスクラスは、トップ画面でしか使用しない前提
	 */
	private static final String SCREEN_ID = "DPS000";

	/**
	 * 業務進捗の検索タイプを表す列挙
	 *
	 * @author tkawabata
	 */
	public enum ProgressIyakuSearchType {

		/**
		 * 支店一覧
		 */
		SITEN_LIST(ProgressIyakuRank.SITEN),

		/**
		 * 支店
		 */
		SITEN(ProgressIyakuRank.SITEN),

		/**
		 * 営業所一覧
		 */
		OFFICE_LIST(ProgressIyakuRank.OFFICE),

		/**
		 * 営業所
		 */
		OFFICE(ProgressIyakuRank.OFFICE),

//		/**
//		 * チームリスト
//		 */
//		TEAM_LIST(ProgressIyakuRank.TEAM),
//
//		/**
//		 * チーム
//		 */
//		TEAM(ProgressIyakuRank.TEAM),

		/**
		 * 担当者リスト
		 */
		TANTOU_LIST(ProgressIyakuRank.TANTOU),

		/**
		 * 担当者
		 */
		TANTOU(ProgressIyakuRank.TANTOU),

		/**
		 * NONE
		 */
		NONE(ProgressIyakuRank.NONE);

		/**
		 * 階層
		 */
		private final ProgressIyakuRank progressIyakuRank;

		/**
		 * コンストラクタ
		 *
		 * @param progressIyakuRank 階層
		 */
		private ProgressIyakuSearchType(ProgressIyakuRank progressIyakuRank) {
			this.progressIyakuRank = progressIyakuRank;
		}

		/**
		 * 階層を取得する。
		 *
		 * @return progressIyakuRank 階層
		 */
		public ProgressIyakuRank getProgressIyakuRank() {
			return progressIyakuRank;
		}

		/**
		 * 業務進捗の取得階層を表す列挙
		 *
		 * @author tkawabata
		 */
		public enum ProgressIyakuRank {

			/**
			 * 支店
			 */
			SITEN,

			/**
			 * 営業所
			 */
			OFFICE,

			/**
			 * チーム
			 */
			TEAM,

			/**
			 * 担当者
			 */
			TANTOU,

			/**
			 * 無し
			 */
			NONE;
		}
	}

//	/**
//	 * 特約店系
//	 */
//	private static JokenSet[] TOKUYAKU = {
//		JokenSet.TOKUYAKUTEN_G_MASTER,
//		JokenSet.TOKUYAKUTEN_G_STAFF,
//		JokenSet.TOKUYAKUTEN_G_TANTOU,
//		JokenSet.TOKUYAKUTEN_GYOUMU_G,
//		JokenSet.TOKUYAKUTEN_MASTER };

	/**
	 * ログイン情報に設定されている組織従業員や初期表示フラグを元に、医薬の検索タイプを取得する。
	 *
	 * @return 検索タイプ
	 */
	public static ProgressIyakuSearchType getIyakuSearchType() {
		DpUser settingDpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		SosMst dSosMst = DpUserInfo.getDpUserInfo().getDefaultSosMst();
		boolean initDispFlg = DpUserInfo.getDpUserInfo().getInitDispFlg();
		SosLvl sosLvl = settingDpUser.getSosLvl(SCREEN_ID);

		if (dSosMst == null) {

			switch(sosLvl){
			case ALL:
				return ProgressIyakuSearchType.SITEN_LIST;
			default:
				return ProgressIyakuSearchType.NONE;
			}
		}

		// 部門ランクの取得
		BumonRank bRank = dSosMst.getBumonRank();


		switch(sosLvl){
		case ALL:// 本部の場合
			switch(bRank){
			case IEIHON:
				return ProgressIyakuSearchType.SITEN_LIST;
			case SITEN_TOKUYAKUTEN_BU:
				if(initDispFlg) {
					return ProgressIyakuSearchType.SITEN_LIST;
				}
				return ProgressIyakuSearchType.OFFICE_LIST;
			case OFFICE_TOKUYAKUTEN_G:
				return ProgressIyakuSearchType.TANTOU_LIST;
			default:
				return ProgressIyakuSearchType.SITEN;
			}

		case BRANCH:// 支店の場合
			switch( bRank){
			case SITEN_TOKUYAKUTEN_BU:
				if(initDispFlg) {
					return ProgressIyakuSearchType.SITEN;
				}
				return ProgressIyakuSearchType.OFFICE_LIST;
			case OFFICE_TOKUYAKUTEN_G:
				return ProgressIyakuSearchType.TANTOU_LIST;
			default:
				return ProgressIyakuSearchType.SITEN;
			}
		case OFFICE:// 営業所の場合
			switch( bRank){
			case OFFICE_TOKUYAKUTEN_G:
				if(initDispFlg) {
					return ProgressIyakuSearchType.OFFICE;
				}
				return ProgressIyakuSearchType.TANTOU_LIST;
			default:
				return ProgressIyakuSearchType.OFFICE;
			}
		case MR:
			// 副担当MRの場合はなし
			if(settingDpUser.isSubMr()){
				return ProgressIyakuSearchType.NONE;
			} else {
				return ProgressIyakuSearchType.TANTOU;
			}

		default:
			return ProgressIyakuSearchType.NONE;

		}

/*
		// 本部の場合
		if (settingDpUser.isMatch(JokenSet.HONBU)) {
			if (BumonRank.IEIHON == bRank) {
				return ProgressIyakuSearchType.SITEN_LIST;
			} else if (initDispFlg && BumonRank.SITEN_TOKUYAKUTEN_BU == bRank) {
				return ProgressIyakuSearchType.SITEN_LIST;
			} else if (BumonRank.SITEN_TOKUYAKUTEN_BU == bRank) {
				return ProgressIyakuSearchType.OFFICE_LIST;
			} else if (BumonRank.OFFICE_TOKUYAKUTEN_G == bRank) {
//				return ProgressIyakuSearchType.TEAM_LIST;
//			} else if (BumonRank.TEAM == bRank) {
				return ProgressIyakuSearchType.TANTOU_LIST;
			} else {
				return ProgressIyakuSearchType.SITEN;
			}
		}

		// 支店の場合
		if (settingDpUser.isMatch(JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
			if (initDispFlg && BumonRank.SITEN_TOKUYAKUTEN_BU == bRank) {
				return ProgressIyakuSearchType.SITEN;
			} else if (BumonRank.SITEN_TOKUYAKUTEN_BU == bRank) {
				return ProgressIyakuSearchType.OFFICE_LIST;
			} else if (BumonRank.OFFICE_TOKUYAKUTEN_G == bRank) {
				return ProgressIyakuSearchType.TEAM_LIST;
			} else if (BumonRank.TEAM == bRank) {
				return ProgressIyakuSearchType.TANTOU_LIST;
			} else {
				return ProgressIyakuSearchType.SITEN;
			}
		}

		// 営業所の場合
		if (settingDpUser.isMatch(JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF)) {
			if (initDispFlg && BumonRank.OFFICE_TOKUYAKUTEN_G == bRank) {
				return ProgressIyakuSearchType.OFFICE;
			} else if (BumonRank.OFFICE_TOKUYAKUTEN_G == bRank) {
				return ProgressIyakuSearchType.TEAM_LIST;
			} else if (BumonRank.TEAM == bRank) {
				return ProgressIyakuSearchType.TANTOU_LIST;
			} else {
				return ProgressIyakuSearchType.OFFICE;
			}
		}

//		// ＡＬの場合
//		if (settingDpUser.isMatch(JokenSet.IYAKU_AL)) {
//			if (initDispFlg && BumonRank.TEAM == bRank) {
//				return ProgressIyakuSearchType.TEAM;
//			} else if (BumonRank.TEAM == bRank) {
//				return ProgressIyakuSearchType.TANTOU_LIST;
//			} else {
//				return ProgressIyakuSearchType.TEAM;
//			}
//		}

		// ＭＲの場合
		if (settingDpUser.isMatch(JokenSet.IYAKU_MR)) {

			// 副担当MRの場合はなし
			if(settingDpUser.isSubMr()){
				return ProgressIyakuSearchType.NONE;
			} else {
				return ProgressIyakuSearchType.TANTOU;
			}
		}

		// 特約店系の場合
		if (settingDpUser.isMatch(TOKUYAKU)) {
			return ProgressIyakuSearchType.SITEN_LIST;
		}

		// ここまで到達したらNONEを返す
		return ProgressIyakuSearchType.NONE;
		*/
	}
}
