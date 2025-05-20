package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.web.bean.CorrespondType.ASYNC;
import static jp.co.takeda.security.BusinessType.CMN;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dto.SosJgiDto;
import jp.co.takeda.dto.SosJgiListDto;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps910C00Form;

/**
 * Dps910C00(組織・従業員検索画面)のアクションクラス
 *
 * @author khashimoto
 */
@Controller("Dps910C00Action")
public class Dps910C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps910C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 組織・従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosJgiSearchService")
	protected DpsSosJgiSearchService dpsSosJgiSearchService;

    /**
     * コードマスタDAO
     */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterDao")
    protected DpsCodeMasterDao dpsCodeMasterDao;

	// -------------------------------
	// action method
	// -------------------------------

	/**
	 * 初期表示時に呼ばれるアクションメソッド<br>
	 * NOTE:2014下期向け支援改定にて、整形対応削除
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dps910C00F00(DpContext ctx, Dps910C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps910C00F00");
			LOG.debug("sosApplyFuncName:" + form.getSosApplyFuncName());
			LOG.debug("sosSrchPtnType:" + form.getSosSrchPtnType());
			LOG.debug("sosMinSrchValue:" + form.getSosMinSrchValue());
			LOG.debug("sosMaxSrchGetValue:" + form.getSosMaxSrchGetValue());
			LOG.debug("sosInitSosCodeValue:" + form.getSosInitSosCodeValue());
			LOG.debug("sosAllDispFlg:" + form.getSosAllDispFlg());
		}

		// 初期化処理
		form.formInit();

		// 検索サービス実行
		SosJgiListDto listDto = dpsSosJgiSearchService.search(form.getSosInitSosCodeValue(), form.getSosSrchPtnType(), form.getSosMaxSrchGetValue());

		// ログインユーザ情報/デフォルト組織情報取得
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		form.setDpUser(dpUser);

		// ログインユーザが全社表示可能の場合、全組織表示可能フラグをONに設定
		if(dpUser.isSosLvl("CMN", SosLvl.ALL)) {
			form.setSosAllDispFlg(true);
		}

		// 条件セットグループから組織参照レベルを抽出
		SosLvl sosLvl = null;
		for(JknGrp jknGrp : dpUser.getJknGrpList()) {
			if(sosLvl == null || (jknGrp.getSosLvl().getDbValue().compareTo(sosLvl.getDbValue()) < 0 )) {
				sosLvl = jknGrp.getSosLvl();
			}
		}

		form.setSosLvl(sosLvl);

		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dps910C00Form.SOS_JGI_LIST_DTO_KEY_R, listDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * 他画面からの非同期でデータ取得時に呼ばれるアクションメソッド<br>
	 * NOTE:2014下期向け支援改定にて、整形対応削除
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = CMN)
	public Result dps910C00F05(DpContext ctx, Dps910C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps910C00F05");
			LOG.debug("sosCd:" + form.getSosCd());
			LOG.debug("jgiNo:" + form.getJgiNo());
		}

		// 検索サービス実行
		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		SosJgiDto sosJgiDto = dpsSosJgiSearchService.search(form.getSosCd(), jgiNo, form.getSosMaxSrchGetValue());
		SosMst selectSosMst = sosJgiDto.getSosMstList().get(sosJgiDto.getSosMstList().size() - 1);
		JgiMst selectJgiMst = sosJgiDto.getJgiMst();
		if(selectJgiMst == null){
			form.setJgiNo(null);
		}

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		list.add(new CodeAndValue("name", sosJgiDto.getSosJgiName()));
		list.add(new CodeAndValue("sosCd2", sosJgiDto.getSosCd2()));
		list.add(new CodeAndValue("sosCd3", sosJgiDto.getSosCd3()));
		list.add(new CodeAndValue("sosCd4", sosJgiDto.getSosCd4()));
		list.add(new CodeAndValue("jgiNo", form.getJgiNo()));
		list.add(new CodeAndValue("initSosCodeValue", selectSosMst.getSosCd()));
		list.add(new CodeAndValue("brCode", selectSosMst.getBrCode()));
		list.add(new CodeAndValue("distCode", selectSosMst.getDistCode()));
		list.add(new CodeAndValue("oncSosFlg", Boolean.toString(BooleanUtils.isTrue(selectSosMst.getOncSosFlg()))));
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		//list.add(new CodeAndValue("sosCategory", selectSosMst.getSosCategory()));
		list.add(new CodeAndValue("sosSubCategory", selectSosMst.getSosSubCategory()));
		list.add(new CodeAndValue("underSosCnt", selectSosMst.getUnderSosCnt().toString()));
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		//ctx.getRequest().setAttribute("sosDataList", list);
	      // 従業員がワクチンのに参照可能な場合は、ワクチンのみに絞る
        DpUser user = DpUserInfo.getDpUserInfo().getSettingUser();
        // 条件グループの特定
        JknGrp jknGrp = user.getJknGrpList().get(0);
        String vacCtg = null;
        if(jknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) ||
                jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
            List<DpsCCdMst> codeList;
            try {
                codeList = dpsCodeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
            } catch (DataNotFoundException e) {
                final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが登録されていません。";
                throw new SystemException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, errMsg));
            }
            if(codeList.size() > 1) {
                final String errMsg = "計画管理汎用マスタにワクチンのカテゴリコードが複数登録されています。";
                throw new SystemException(new Conveyance(ErrMessageKey.DB_DUPLICATE_ERROR, errMsg));
            }
            vacCtg = codeList.get(0).getDataCd();
        }

		if (selectSosMst.getSosCategoryList() != null) {
			String sosCategoryTmp = "";
			for (int i = 0; i < selectSosMst.getSosCategoryList().size(); i++) {
			    // ワクチンのみ参照・更新の場合
			    if(vacCtg != null) {
			        if(selectSosMst.getSosCategoryList().get(i).getCategoryCd().equals(vacCtg)) {
			            sosCategoryTmp = selectSosMst.getSosCategoryList().get(i).getCategoryCd();
			            break;
			        }
			    }
			    //
			    else {
	                if(i != 0 ) {
	                    sosCategoryTmp += ",";
	                }
	                sosCategoryTmp += selectSosMst.getSosCategoryList().get(i).getCategoryCd();
			    }
			}
			list.add(new CodeAndValue("sosCategory", sosCategoryTmp));
		}
		ctx.getRequest().setAttribute("sosDataList", list);


		// デフォルト組織の変更
		if (form.isDefaultChangeFlg()) {
			BumonRank bumonRank = selectSosMst.getBumonRank();
			if (BumonRank.IEIHON.getDbValue() >= bumonRank.getDbValue()) {
				if (StringUtils.isNotBlank(form.getSosCd()) && StringUtils.isNotBlank(selectSosMst.getBrCode())) {
					DpUserInfo.getDpUserInfo().setDefaultSosJgi(null, null);
				}
			} else {
				if (StringUtils.isNotBlank(selectSosMst.getBrCode())) {
					DpUserInfo.getDpUserInfo().setDefaultSosJgi(selectSosMst, selectJgiMst);
				}
			}
		}
		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 起動時、入力パラメータを検証する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps910C00F00Validation(DpContext ctx, Dps910C00Form form) throws Exception {
		// 適用関数名 必須チェック
		if (StringUtils.isBlank(form.getSosApplyFuncName())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("適用関数名");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}
		// 検索パターン区分 必須チェック
		if (StringUtils.isBlank(form.getSosSrchPtnType())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("検索パターン区分");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}
		// 検索最小階層取得値 必須チェック
		if (StringUtils.isBlank(form.getSosMinSrchValue())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("検索最小階層取得値");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}
		// 検索最大階層取得値 必須チェック
		if (StringUtils.isBlank(form.getSosMaxSrchGetValue())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("検索最大階層取得値");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}
	}

	/**
	 * 非同期更新時、入力パラメータを検証する。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps910C00F05Validation(DpContext ctx, Dps910C00Form form) throws Exception {
		// 従業員番号 数値チェック
		if (StringUtils.isNotBlank(form.getJgiNo()) && !ValidationUtil.isInteger(form.getJgiNo())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("従業員番号");
			srcHolderList.add("半角数字");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1004E", srcHolderList)));
		}
		// 検索最大階層取得値 必須チェック
		if (StringUtils.isBlank(form.getSosMaxSrchGetValue())) {
			List<String> srcHolderList = new ArrayList<String>();
			srcHolderList.add("検索最大階層取得値");
			throw new ValidateException(new Conveyance(new MessageKey("DPC1003E", srcHolderList)));
		}
	}


}
