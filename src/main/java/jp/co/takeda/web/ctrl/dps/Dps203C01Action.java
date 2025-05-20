package jp.co.takeda.web.ctrl.dps;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.security.DpAuthority.AuthType.*;

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
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.ValidateException;
import jp.co.takeda.a.web.bean.ActionMethod;
import jp.co.takeda.a.web.bean.MethodType;
import jp.co.takeda.a.web.bean.Result;
import jp.co.takeda.bean.ActionResult;
import jp.co.takeda.dto.MikakutokuSijouResultDto;
import jp.co.takeda.dto.MikakutokuSijouScDto;
import jp.co.takeda.dto.MikakutokuSijouUpdateDto;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.security.Permission;
import jp.co.takeda.security.RequestType;
import jp.co.takeda.service.DpsMikakutokuSijouSearchService;
import jp.co.takeda.service.DpsMikakutokuSijouService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.in.dps.Dps203C01Form;

/**
 * Dps203C01((医)未獲得市場編集画面)のアクションクラス
 *
 * @author stakeuchi
 */
@Controller("Dps203C01Action")
public class Dps203C01Action extends DpAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(Dps203C01Action.class);

	/**
	 * 画面ID
	 */
	private static final String SCREEN_ID = "DPS203C01";

	// -------------------------------
	// injection field
	// -------------------------------
	/**
	 * 未獲得市場 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMikakutokuSijouSearchService")
	protected DpsMikakutokuSijouSearchService dpsMikakutokuSijouSearchService;

	/**
	 * 未獲得市場 更新サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMikakutokuSijouService")
	protected DpsMikakutokuSijouService dpsMikakutokuSijouService;

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
	@RequestType
	@Permission(authType = REFER)
	public Result dps203C01F00(DpContext ctx, Dps203C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps203C01F00");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("YakkouSijouCode:" + form.getYakkouSijouCode());
		}

		// 初期化処理
		form.formInit();

		// ログインユーザの組織情報をセット
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				// 権限確認コード
				if (user.isSosLvl(SCREEN_ID, SosLvl.BRANCH)) {
					form.setSosCd2(user.getSosCd2());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.OFFICE)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
				}
				else if (user.isSosLvl(SCREEN_ID, SosLvl.MR)) {
					form.setSosCd2(user.getSosCd2());
					form.setSosCd3(user.getSosCd3());
					form.setJgiNo(String.valueOf(user.getJgiNo()));
				}
			}
		}

		// 検索実行
		searchMikakutokuSijouList(form);
		return ActionResult.SUCCESS;
	}

	/**
	 * 登録処理時に呼ばれるアクションメソッド
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@ActionMethod
	@RequestType
	@Permission(authType = EDIT)
	public Result dps203C01F05Execute(DpContext ctx, Dps203C01Form form) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("dps203C01F05Execute");
			LOG.debug("SosCd3:" + form.getSosCd3());
			LOG.debug("YakkouSijouCode:" + form.getYakkouSijouCode());
		}
		try {

			List<MikakutokuSijouUpdateDto> updateDtoList = form.convertMikakutokuSijouUpdateDto();
			if (updateDtoList != null && !updateDtoList.isEmpty()) {
				dpsMikakutokuSijouService.updateMikakutokuSijou(form.getSosCd3(), updateDtoList);
				addMessage(ctx, new MessageKey("DPC0003I", String.valueOf(updateDtoList.size())));
			}

		} finally {

			// 再検索実行
			searchMikakutokuSijouList(form);
		}
		return ActionResult.SUCCESS;
	}

	/**
	 * 検索処理を実行する。
	 *
	 * @param form アクションフォーム
	 * @throws Exception 例外
	 */
	private void searchMikakutokuSijouList(Dps203C01Form form) throws Exception {

		MikakutokuSijouScDto scDto = form.convertMikakutokuSijouScDto();
		List<MikakutokuSijouResultDto> resultDtoList;
		try {
			resultDtoList = dpsMikakutokuSijouSearchService.searchMikakutokuSijouList(scDto);
			form.setExistSearchDataFlag(true);
		} catch (LogicalException e) {
			form.setExistSearchDataFlag(false);
			throw e;
		}
		super.getRequestBox().put(Dps203C01Form.DPS203C01_DATA_R, (ArrayList<MikakutokuSijouResultDto>) resultDtoList);
	}

	// -------------------------------
	// validation method
	// -------------------------------
	/**
	 * 登録処理時の入力パラメータチェックを行う。
	 *
	 * @param ctx Context
	 * @param form ActionForm
	 * @throws ValidateException 例外
	 */
	@ActionMethod(methodType = MethodType.VALIDATION)
	public void dps203C01F05Validation(DpContext ctx, Dps203C01Form form) throws ValidateException {

		// 更新行のNullチェック
		String[] rowIdList = form.getRowIdList();
		if (rowIdList == null) {
			final String errMsg = "受信パラメータが存在しない";
			throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
		}

		for (int i = 0; i < rowIdList.length; i++) {

			// パラーメータのサイズチェック
			String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);
			if (rowId.length != 5) {
				final String errMsg = "受信パラメータが不正";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 増減金額に差異がない場合はチェックを行わない
			if (StringUtils.equals(rowId[3], rowId[4])) {
				continue;
			}
			// シーケンスキー
			if (StringUtils.isEmpty(rowId[0]) || !ValidationUtil.isLong(rowId[0])) {
				final String errMsg = "受信パラメータ(シーケンスキー)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 最終更新日
			if (StringUtils.isEmpty(rowId[1]) || !ValidationUtil.isLong(rowId[1])) {
				final String errMsg = "受信パラメータ(最終更新日)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
			// 検索時の未獲得市場
			if (StringUtils.isEmpty(rowId[2]) || !ValidationUtil.isLong(rowId[2])) {
				final String errMsg = "受信パラメータ(検索時の未獲得市場)が不正：";
				throw new ValidateException(new Conveyance(VALIDATE_ERROR, errMsg));
			}
		}
	}
}
