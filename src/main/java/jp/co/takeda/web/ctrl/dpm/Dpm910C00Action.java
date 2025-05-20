package jp.co.takeda.web.ctrl.dpm;

import static jp.co.takeda.a.web.bean.CorrespondType.ASYNC;
import static jp.co.takeda.security.BusinessType.CMN;

import java.util.ArrayList;
import java.util.List;

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
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dto.SosJgiDto;
import jp.co.takeda.dto.SosJgiListDto;
import jp.co.takeda.model.DpmCCdMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpmSosJgiSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dpm.Dpm910C00Form;

/**
 * Dpm910C00(組織・従業員検索画面)のアクションクラス
 *
 * @author khashimoto
 */
@Controller("Dpm910C00Action")
public class Dpm910C00Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dpm910C00Action.class);

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 組織・従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmSosJgiSearchService")
	protected DpmSosJgiSearchService dpmSosJgiSearchService;

    /**
     * コードマスタDAO
     */
    @Autowired(required = true)
    @Qualifier("codeMasterDao")
    protected CodeMasterDao codeMasterDao;

	// -------------------------------
	// action method
	// -------------------------------

	/**
	 * 初期表示時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType(businessType = CMN)
	public Result dpm910C00F00(DpContext ctx, Dpm910C00Form form) throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm910C00F00");
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
		SosJgiListDto listDto = dpmSosJgiSearchService.search(form.getSosInitSosCodeValue(), form.getSosSrchPtnType(), form.getSosMaxSrchGetValue(), form.isEtcSosFlg());

		// ログインユーザ情報/デフォルト組織情報取得
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		form.setDpUser(dpUser);
		// ログインユーザが全社表示可能かどうかを確認
		if(dpUser.isSosLvl("CMN", SosLvl.ALL)) {
			form.setSosAllDispFlg(true);
		}

		// 条件セットグループから組織参照レベルを抽出
		SosLvl sosLvl = null;
		for(JknGrp jknGrp:dpUser.getJknGrpList()) {
			if(sosLvl == null || (jknGrp.getSosLvl().getDbValue().compareTo(sosLvl.getDbValue()) < 0 )) {
				sosLvl = jknGrp.getSosLvl();
			}
		}
		form.setSosLvl(sosLvl);


		// 結果格納DTOをリクエストに格納
		super.getRequestBox().put(Dpm910C00Form.SOS_JGI_LIST_DTO_KEY_R, listDto);
		return ActionResult.SUCCESS;
	}

	/**
	 * 他画面からの非同期でデータ取得時に呼ばれるアクションメソッド<br>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod(correspondType = ASYNC)
	@RequestType(businessType = CMN)
	public Result dpm910C00F05(DpContext ctx, Dpm910C00Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dpm910C00F05");
			LOG.debug("sosCd:" + form.getSosCd());
			LOG.debug("jgiNo:" + form.getJgiNo());
		}

		// 検索サービス実行
		Integer jgiNo = ConvertUtil.parseInteger(form.getJgiNo());
		SosJgiDto sosJgiDto = dpmSosJgiSearchService.search(form.getSosCd(), jgiNo, form.getSosMaxSrchGetValue(), form.isEtcSosFlg());
		SosMst selectSosMst = sosJgiDto.getSosMstList().get(sosJgiDto.getSosMstList().size() - 1);

		List<CodeAndValue> list = new ArrayList<CodeAndValue>();
		list.add(new CodeAndValue("name", sosJgiDto.getSosJgiName()));
		list.add(new CodeAndValue("sosCd2", sosJgiDto.getSosCd2()));
		list.add(new CodeAndValue("sosCd3", sosJgiDto.getSosCd3()));
		list.add(new CodeAndValue("sosCd4", sosJgiDto.getSosCd4()));
		list.add(new CodeAndValue("jgiNo", form.getJgiNo()));
		list.add(new CodeAndValue("initSosCodeValue", selectSosMst.getSosCd()));
		list.add(new CodeAndValue("brCode", selectSosMst.getBrCode()));
		list.add(new CodeAndValue("distCode", selectSosMst.getDistCode()));
		list.add(new CodeAndValue("etcSosFlg", selectSosMst.getEtcSosFlg().toString()));
		list.add(new CodeAndValue("oncSosFlg", selectSosMst.getOncSosFlg().toString()));

	      // 従業員がワクチンのに参照可能な場合は、ワクチンのみに絞る
        DpUser user = DpUserInfo.getDpUserInfo().getSettingUser();
        // 条件グループの特定
        JknGrp jknGrp = user.getJknGrpList().get(0);
        String vacCtg = null;
        if(jknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) ||
                jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
            List<DpmCCdMst> codeList;
            try {
                codeList = codeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
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

		return ActionResult.SUCCESS;
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 起動時、入力パラメータチェック
	 *
	 * <pre>
	 * 画面入力にエラーがある場合に例外をスローする。
	 * </pre>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm910C00F00Validation(DpContext ctx, Dpm910C00Form form) throws Exception {
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
	 * 非同期更新時、入力パラメータチェック
	 *
	 * <pre>
	 * 画面入力にエラーがある場合に例外をスローする。
	 * </pre>
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws Exception 例外(ValidateException)
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dpm910C00F05Validation(DpContext ctx, Dpm910C00Form form) throws Exception {
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
