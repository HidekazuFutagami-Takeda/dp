package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.model.div.JokenSet.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.InsDocPlanStatusDao;
import jp.co.takeda.model.InsDocPlanStatus;
import jp.co.takeda.model.InsWsPlanStatus;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.StatusForInsDocPlan;
import jp.co.takeda.model.div.StatusForInsWsPlan;
import jp.co.takeda.model.div.StatusForMrPlan;
import jp.co.takeda.model.view.InsDocPlanStatSum;
import jp.co.takeda.model.view.InsWsPlanStatSum;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;

/**
 * 施設特約店別計画　表示判定ロジック
 *
 * @author nozaki
 */
public class CheckInsWsDispLogic {

	/**
	 * 施設医師別計画ステータスDAO
	 */
	private final InsDocPlanStatusDao insDocPlanStatusDao;

	/**
	 * 汎用マスタ検索サービス
	 */
	private final DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 計画対象カテゴリ領域検索サービス
	 */
	private final DpsPlannedCtgSearchService dpsPlannedCtgSearchService;

	/**
	 * 利用者のランク
	 */
	private final Integer rank;

	/**
	 * ランク（営業所長・スタッフ以上）
	 */
	private final Integer RANK_OFFICE = 1;

	/**
	 * AL
	 */
	private final Integer RANK_TEAM = 2;

	/**
	 * MR
	 */
	private final Integer RANK_MR = 3;

	/**
	 * コンストラクタ
	 *
	 * @param jgiMstRealDao 従業員DAO
	 */
	public CheckInsWsDispLogic(InsDocPlanStatusDao insDocPlanStatusDao, DpsCodeMasterSearchService dpsCodeMasterSearchService, DpsPlannedCtgSearchService dpsPlannedCtgSearchService) {

		if (insDocPlanStatusDao == null) {
			final String errMsg = "施設医師別計画DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.insDocPlanStatusDao = insDocPlanStatusDao;
		this.dpsCodeMasterSearchService = dpsCodeMasterSearchService;
		this.dpsPlannedCtgSearchService = dpsPlannedCtgSearchService;

		// ログインユーザ情報
		DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();

		// ランク取得
		if (settingUser.isMatch(HONBU, SITEN_MASTER, SITEN_STAFF, OFFICE_MASTER, OFFICE_STAFF)) {
			rank = RANK_OFFICE;
		} else if (settingUser.isMatch(IYAKU_AL)) {
			rank = RANK_TEAM;
		} else if (settingUser.isMatch(IYAKU_MR,IYAKU_SEIKEI_MR)) {
			rank = RANK_MR;
		} else {
			rank = null;
		}
	}

	/**
	 * 施設特約店別計画の計画値表示可能かどうか。
	 * <br>
	 * ・所長以上は、常にOK
	 * ・【仕入品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品】の場合は、担当者別計画 確定 ならばOK
	 * @param rank
	 * @param plannedProd
	 * @param mrPlanStatus
	 * @param insWsPlanStatus
	 * @return
	 */
	public boolean dipsPlan(PlannedProd plannedProd, MrPlanStatus mrPlanStatus, InsWsPlanStatSum insWsPlanStatSum ){

		// 施設特約店別計画すべてが「配分済」の場合ステータス作成
		InsWsPlanStatus insWsPlanStatus = null;
		if(insWsPlanStatSum.getDisted().equals(insWsPlanStatSum.getTotalCount())){
			insWsPlanStatus = new InsWsPlanStatus();
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
		}

		return dipsPlan(plannedProd,mrPlanStatus,insWsPlanStatus);
	}

	/**
	 * 施設特約店別計画の計画値表示可能かどうか。
	 * <br>
	 * （2018年上期向け改定）
	 * ・【SPBU品】を追加する意図で『仕入品以外』という判定方法に変更
	 * （2015年下期向け改定）
	 * ・所長以上は、常にOK
	 * ・【仕入品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品】【ONC品】の場合は、担当者別計画 確定 ならばOK
	 * <br>
	 * （2014年下期向け改定）
	 * ・所長以上は、常にOK
	 * ・【仕入品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品】の場合は、担当者別計画 確定 ならばOK
	 * ・【整形】は削除。
	 * <br>
	 * （改定前）
	 * ・所長以上は、常にOK
	 * ・【仕入品】【整形品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品】の場合は、
	 * 　　　　ALの場合、担当者別計画 公開済み以降 ならばOK
	 * 　　　　MRの場合、担当者別計画 確定 ならばOK
	 * @param rank
	 * @param plannedProd
	 * @param mrPlanStatus
	 * @param insWsPlanStatus
	 * @return
	 */
	public boolean dipsPlan(PlannedProd plannedProd, MrPlanStatus mrPlanStatus, InsWsPlanStatus insWsPlanStatus){

		// ステータスが取得できていない場合はNG
		if(insWsPlanStatus == null){
			return false;
		}

		// 所長以上の場合はOK
		if (rank == RANK_OFFICE) {
			return true;
		}

		// MRは、施設特約店別計画 公開中以降ならばOK
		if(insWsPlanStatus.getStatusForInsWsPlan() == StatusForInsWsPlan.PLAN_OPENED ||
		   insWsPlanStatus.getStatusForInsWsPlan() == StatusForInsWsPlan.PLAN_COMPLETED){
			return true;
		}

//#### DEL start 2021/07/12
//		// 計画対象カテゴリ領域を取得
//		DpsPlannedCtg plannedCtg = dpsPlannedCtgSearchService.searchPlannedCtg(plannedProd.getCategory());
//
//		// 計画対象カテゴリ領域.担当=0の場合は、施設特約店別計画 配分済み以降ならばOK
//		if(plannedCtg.getPlanLevelMr().equals("0")){
//
//			if(insWsPlanStatus == null){
//				return false;
//			}
//
//			if(insWsPlanStatus.getStatusForInsWsPlan() == StatusForInsWsPlan.DISTED ||
//			   insWsPlanStatus.getStatusForInsWsPlan() == StatusForInsWsPlan.PLAN_COMPLETED){
//				return true;
//			}
//		}
//
//// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
////		// MMP品の場合は、担当者別計画 確定 ならばOK
////		if(plannedProd.getCategory() == ProdCategory.MMP || plannedProd.getCategory() == ProdCategory.ONC){
//		// 計画対象カテゴリ領域.担当=1の場合は、担当者別計画 確定 ならばOK
//		else if(plannedCtg.getPlanLevelMr().equals("1")){ //nullがありえるか分からないが修正前のコードの動きに合わせる
//// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//
//			if(mrPlanStatus == null){
//				return false;
//			}
//
//			if( mrPlanStatus.getStatusForMrPlan() == StatusForMrPlan.MR_PLAN_COMPLETED){
//				return true;
//			}
//		}
//#### DEL end 2021/07/12
		return false;
	}

	/**
	 * 施設特約店別計画の編集リンク表示可能かどうか。
	 * （2018年上期向け改定）
	 * ・【SPBU品】を追加する意図で『仕入品以外』という判定方法に変更
	 * （2014年下期向け改定）
	 * ・所長以上は、常にOK
	 * ・【仕入品】【整形品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品（重点品目）】の場合は、担当者別計画 確定、かつ、施設医師別計画 確定ならばOK
	 * ・【MMP品（非重点品目）】の場合は、担当者別計画 確定 ならばOK
	 * <br>
	 * （改定前）
	 * ・所長以上は、常にOK
	 * ・【仕入品】【整形品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品（重点品目）】の場合は、施設医師別計画 公開済み以降ならばOK
	 * ・【MMP品（非重点品目）】の場合は、
	 * 　　　　ALの場合、担当者別計画 公開済み以降 ならばOK
	 * 　　　　MRの場合、担当者別計画 確定 ならばOK
	 * @param rank
	 * @param plannedProd
	 * @param mrPlanStatus
	 * @param insWsPlanStatus
	 * @return
	 */
	public boolean dipsEditLink(Integer jgiNo, PlannedProd plannedProd, MrPlanStatus mrPlanStatus, InsWsPlanStatSum insWsPlanStatSum){

		// 施設特約店別計画すべてが「配分済」の場合ステータス作成
		InsWsPlanStatus insWsPlanStatus = null;
		if(insWsPlanStatSum.getDisted().equals(insWsPlanStatSum.getTotalCount())){
			insWsPlanStatus = new InsWsPlanStatus();
			insWsPlanStatus.setStatusForInsWsPlan(StatusForInsWsPlan.DISTED);
		}

		return dipsEditLink(jgiNo,plannedProd,mrPlanStatus,insWsPlanStatus);
	}

	/**
	 * 施設特約店別計画の編集リンク表示可能かどうか。
	 * <br>
	 * （2018年上期向け改定）
	 * ・【SPBU品】を追加する意図で『仕入品以外』という判定方法に変更
	 * （2015年下期向け改定）
	 * ・所長以上は、常にOK
	 * ・【仕入品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品またはONC品（重点品目）】の場合は、担当者別計画 確定、かつ、施設医師別計画 確定ならばOK
	 * ・【MMP品またはONC品（非重点品目）】の場合は、担当者別計画 確定 ならばOK
	 * <br>
	 * （2014年下期向け改定）
	 * ・所長以上は、常にOK
	 * ・【仕入品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品（重点品目）】の場合は、担当者別計画 確定、かつ、施設医師別計画 確定ならばOK
	 * ・【MMP品（非重点品目）】の場合は、担当者別計画 確定 ならばOK
	 * <br>
	 * （改定前）
	 * ・所長以上は、常にOK
	 * ・【仕入品】【整形品】の場合は、施設特約店別計画 配分済み以降ならばOK
	 * ・【MMP品（重点品目）】の場合は、施設医師別計画 公開済み以降ならばOK
	 * ・【MMP品（非重点品目）】の場合は、
	 * 　　　　ALの場合、担当者別計画 公開済み以降 ならばOK
	 * 　　　　MRの場合、担当者別計画 確定 ならばOK
	 * @param rank
	 * @param plannedProd
	 * @param mrPlanStatus
	 * @param insWsPlanStatus
	 * @return
	 */
	public boolean dipsEditLink(Integer jgiNo, PlannedProd plannedProd, MrPlanStatus mrPlanStatus, InsWsPlanStatus insWsPlanStatus){

		// 従業員未指定はNG
		if(jgiNo == null){
			return false;
		}

		// 所長以上の場合はOK
		if (rank == RANK_OFFICE) {
			return true;
		}

		//add Start 2022/8/19 H.Futagami MRが「一部配布済」をクリックするとシステムエラー（INC1937706）
		//insWsPlanStatusが取得できない品目の場合、編集リンク不要なのでfalseを返却する
		if(insWsPlanStatus == null) {
			return false;
		}
		//add End 2022/8/19 H.Futagami MRが「一部配布済」をクリックするとシステムエラー（INC1937706）

		// MRは施設特約店別計画ステータスが「公開」「確定」の時OK
		if(insWsPlanStatus.getStatusForInsWsPlan() == StatusForInsWsPlan.PLAN_OPENED ||
			insWsPlanStatus.getStatusForInsWsPlan() == StatusForInsWsPlan.PLAN_COMPLETED){
				return true;
		}

//### del 2021/07/02 MRは施設特約店別計画ステータスが「公開」「確定」の時に計画編集リンク表示のため、削除 ###
//		// 仕入品・整形品の場合は、施設特約店別計画 配分済み以降ならばOK
//		if(dpsCodeMasterSearchService.isSiire(plannedProd.getCategory())){
//
//			// 計画値表示の場合はOK
//			if(dipsPlan(plannedProd,mrPlanStatus,insWsPlanStatus)){
//				return true;
//			}
//		}
//
//// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
////		// MMP品ONC品（非重点）の場合は、担当者別計画「確定」ならばOK
////		if((plannedProd.getCategory() == ProdCategory.MMP || plannedProd.getCategory() == ProdCategory.ONC) && !plannedProd.getPlanLevelInsDoc()){
//		// 仕入品以外（2018上期時点でGMBU、SPBU、ONC品）の（非重点）の場合は、担当者別計画「確定」ならばOK
//		else if((plannedProd.getCategory() != null) && !plannedProd.getPlanLevelInsDoc()){ //nullがありえるか分からないが修正前のコードの動きに合わせる
//// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//
//			// 計画値表示の場合はOK
//			if(dipsPlan(plannedProd,mrPlanStatus,insWsPlanStatus)){
//				return true;
//			}
//		}
//
//// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
////		// MMP品ONC品（重点品目）の場合は、施設医師別計画 確定、かつ、担当者別計画 確定ならばOK
////		if((plannedProd.getCategory() == ProdCategory.MMP || plannedProd.getCategory() == ProdCategory.ONC) && plannedProd.getPlanLevelInsDoc()){
//		// 仕入品以外（2018上期時点でGMBU、SPBU、ONC品）の（重点品目）の場合は、施設医師別計画 確定、かつ、担当者別計画 確定ならばOK
//		else if((plannedProd.getCategory() != null) && plannedProd.getPlanLevelInsDoc()){ //nullがありえるか分からないが修正前のコードの動きに合わせる
//// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//
//			// 計画値非表示の場合はNG
//			if(!dipsPlan(plannedProd,mrPlanStatus,insWsPlanStatus)){
//				return false;
//			}
//
//			InsDocPlanStatus insDocPlanStatus = null;
//			try {
//				insDocPlanStatus = insDocPlanStatusDao.search(jgiNo, plannedProd.getProdCode());
//			} catch (DataNotFoundException e) {
//			}
//
//			if(insDocPlanStatus == null){
//				return false;
//			}
//
//			if(insDocPlanStatus.getStatusForInsDocPlan() == StatusForInsDocPlan.PLAN_OPENED ||
//			   insDocPlanStatus.getStatusForInsDocPlan() == StatusForInsDocPlan.PLAN_COMPLETED){
//				return true;
//
//			} else {
//				return false;
//			}
//		}

		return false;
	}

	/**
	 * 施設特約店別計画の個別配分可能かどうか。
	 * <br>
	 * （2018年上期向け改定）
	 * ・【SPBU品】を追加する意図で『仕入品以外』という判定方法に変更
	 * （2015年下期向け改定）
	 * ・【仕入品】の場合は、施設特約店別計画「配分済」ならばOK
	 * ・【MMP品またはONC品（非重点品目）】の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * ・【MMP品またはONC品（重点品目）】の場合は、担当者別計画「確定」、かつ、施設医師別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * <br>
	 * （2014年下期向け改定）
	 * ・【仕入品】の場合は、施設特約店別計画「配分済」ならばOK
	 * ・【MMP品（非重点品目）】の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * ・【MMP品（重点品目）】の場合は、担当者別計画「確定」、かつ、施設医師別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * <br>
	 * （改定前）
	 * ・【仕入品】【整形品】の場合は、施設特約店別計画「配分済」ならばOK
	 * ・【MMP品（非重点品目）】の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * ・【MMP品（重点品目）】の場合は、施設医師別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 *
	 * @param rank
	 * @param plannedProd
	 * @param mrPlanStatus
	 * @param insDocPlanStatus
	 * @param insWsPlanStatus
	 * @return
	 */
	public boolean enableRehaibun(Integer jgiNo, PlannedProd plannedProd, MrPlanStatus mrPlanStatus, InsDocPlanStatSum insDocPlanStatSum, InsWsPlanStatSum insWsPlanStatSum){


		// 仕入品・整形品の場合は、施設特約店別計画「配分済」 ～ 施設特約店別計画「確定前」 ならばOK
		if(dpsCodeMasterSearchService.isSiire(plannedProd.getCategory())){

			// 配分権限がない場合はNG
			DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();
			if(!settingUser.hasAuth(new DpAuthority( AuthType.EDIT))){
				return false;
			}

			int insWsTotal = insWsPlanStatSum.getTotalCount();
			int insWsDisted = insWsPlanStatSum.getDisted();
			int insWsCompleted = insWsPlanStatSum.getPlanCompleted();

			// 施設特約店別計画「確定」がいない、かつ、全員「配分済」
			if(insWsCompleted == 0 && insWsTotal == insWsDisted){
				return true;
			}

			return false;
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//		// MMP品（非重点）の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
//		// MMP品（重点）の場合は、さらに施設医師別計画が「確定」ならばOK
//		if(plannedProd.getCategory() == ProdCategory.MMP || plannedProd.getCategory() == ProdCategory.ONC){
		// 仕入品以外（2018上期時点でGMBU、SPBU、ONC品）の（非重点）の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
		// 仕入品以外（2018上期時点でGMBU、SPBU、ONC品）の（重点）の場合は、さらに施設医師別計画が「確定」ならばOK
		else if(plannedProd.getCategory() != null){ //nullがありえるか分からないが修正前のコードの動きに合わせる
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// 配分権限がない場合はNG
			DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();
			if(!settingUser.hasAuth(new DpAuthority( AuthType.EDIT))){
				return false;
			}

			// 担当者別計画が確定していない
			if(mrPlanStatus == null || mrPlanStatus.getStatusForMrPlan() != StatusForMrPlan.MR_PLAN_COMPLETED){
				return false;
			}

			int insWsTotal = insWsPlanStatSum.getTotalCount();
//			int insWsNon = insWsPlanStatSum.getNone();
			int insWsDisted = insWsPlanStatSum.getDisted();
			int insWsCompleted = insWsPlanStatSum.getPlanCompleted();

			// 施設特約店別計画「確定」がいる、または、全員「配分済」ではない
			if(insWsCompleted != 0 || insWsTotal != insWsDisted){
				return false;
			}

			// 重点品の場合、施設医師別計画が確定していない
			if(plannedProd.getPlanLevelInsDoc()){

				int insDocTotal = insDocPlanStatSum.getTotalCount();
				int insDocCompleted = insDocPlanStatSum.getPlanCompleted();

				// 施設医師別計画が全員確定していない
				if(insDocTotal != insDocCompleted){
					return false;
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * 施設特約店別計画の個別配分可能かどうか。
	 * <br>
	 * （2018年上期向け改定）
	 * ・【SPBU品】を追加する意図で『仕入品以外』という判定方法に変更
	 * （2015年下期向け改定）
	 * ・【仕入品】の場合は、施設特約店別計画「配分済」ならばOK
	 * ・【MMP品またはONC品（非重点品目）】の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * ・【MMP品またはONC品（重点品目）】の場合は、担当者別計画「確定」、かつ、施設医師別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * <br>
	 * （2014年下期向け改定）
	 * ・【仕入品】の場合は、施設特約店別計画「配分済」ならばOK
	 * ・【MMP品（非重点品目）】の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * ・【MMP品（重点品目）】の場合は、担当者別計画「確定」、かつ、施設医師別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * ・【整形】は削除。
	 * <br>
	 * （改定前）
	 * ・【仕入品】【整形品】の場合は、施設特約店別計画「配分済」ならばOK
	 * ・【MMP品（非重点品目）】の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 * ・【MMP品（重点品目）】の場合は、施設医師別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
	 *
	 * @param rank
	 * @param plannedProd
	 * @param mrPlanStatus
	 * @param insWsPlanStatus
	 * @return
	 */
	public boolean enableRehaibun(Integer jgiNo, PlannedProd plannedProd, MrPlanStatus mrPlanStatus, InsWsPlanStatus insWsPlanStatus){

		// 従業員未指定はNG
		if(jgiNo == null){
			return false;
		}

		// 仕入品・整形品の場合は、施設特約店別計画「配分済」 ～ 施設特約店別計画「確定前」 ならばOK
		if(dpsCodeMasterSearchService.isSiire(plannedProd.getCategory())){

			// 配分権限がない場合はNG
			DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();
			if(!settingUser.hasAuth(new DpAuthority( AuthType.EDIT))){
				return false;
			}

			if(insWsPlanStatus == null){
				return false;
			}

			if(insWsPlanStatus.getStatusForInsWsPlan() == StatusForInsWsPlan.DISTED){
				return true;
			}

			return false ;
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//		// MMP品（非重点）の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
//		if(plannedProd.getCategory() == ProdCategory.MMP || plannedProd.getCategory() == ProdCategory.ONC){
		// 仕入品以外（2018上期時点でGMBU、SPBU、ONC品）の（非重点）の場合は、担当者別計画「確定」 ～ 施設特約店別計画「確定前」ならばOK
		else if(plannedProd.getCategory() != null){ //nullがありえるか分からないが修正前のコードの動きに合わせる
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

			// 配分権限がない場合はNG
			DpUser settingUser =  DpUserInfo.getDpUserInfo().getSettingUser();
			if(!settingUser.hasAuth(new DpAuthority( AuthType.EDIT))){
				return false;
			}

			// 担当者別計画が確定していない
			if(mrPlanStatus == null || mrPlanStatus.getStatusForMrPlan() != StatusForMrPlan.MR_PLAN_COMPLETED){
				return false;
			}

			// 施設特約店別計画「配分済」ではない
			if(insWsPlanStatus == null || insWsPlanStatus.getStatusForInsWsPlan() == null || insWsPlanStatus.getStatusForInsWsPlan() != StatusForInsWsPlan.DISTED){
				return false;
			}

			// 重点品の場合、施設医師別計画が確定していない
			if(plannedProd.getPlanLevelInsDoc()){

				InsDocPlanStatus insDocPlanStatus = null;
				try {
					insDocPlanStatus = insDocPlanStatusDao.search(jgiNo, plannedProd.getProdCode());
				} catch (DataNotFoundException e) {
					return false;
				}

				if(insDocPlanStatus.getStatusForInsDocPlan() != StatusForInsDocPlan.PLAN_COMPLETED){
					return false;
				}
			}

			return true;
		}

		return false ;
	}

}
