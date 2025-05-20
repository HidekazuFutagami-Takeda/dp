package jp.co.takeda.web.cmn.delegate;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import jakarta.xml.bind.DatatypeConverter;
import jp.co.takeda.a.bean.Box;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.AccessDeniedException;
import jp.co.takeda.a.exp.CertificationException;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.bean.Constants;
import jp.co.takeda.bean.Constants.SpringBeanName;
import jp.co.takeda.exp.UserTimeoutException;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.service.DpcUserSearchService;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.DateUtil;
import jp.co.takeda.util.ValidationUtil;
import jp.co.takeda.web.cmn.action.DpDelegateAction;
import jp.co.takeda.web.cmn.bean.DpContext;
import jp.co.takeda.web.cmn.sso.AccessTokenParser;
import jp.co.takeda.web.cmn.sso.ParsedToken;

/**
 * 認証処理を実行するDelegate
 *
 * @author tkawabata
 */
@Controller("certificateDelegate")
public class CertificateDelegate extends DpDelegateAction {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(CertificateDelegate.class);

	/**
	 * ユーザIDを取得する際のキー文字列
	 */
	public static final String X_USER_KEY = "X_USER";

	/**
	 * 正規の従業員番号を取得する際のキー文字列
	 */
	public static final String OFFICIAL_USER_KEY = "employeeNumber";

	/**
	 * 検証用ハッシュ値を取得する際のキー文字列
	 */
	public static final String VERIFIER_KEY = "verifier";

	/**
	 * 代行ユーザの従業員番号を取得する際のキー文字列
	 */
	public static final String ALT_USER_COOKIE_KEY = "CookieActJgino";

	/**
	 * DB認証で許可されるＩＤリスト
	 */
	public static final String[] DB_CERTIFICATE_ID_LIST = { "dps000C00F00", "dps000C00F50", "dpm000C00F00", "dpm000C00F50", "dps502C03F05" };

	/**
	 * UserService
	 */
	@Autowired(required = true)
	@Qualifier("dpcUserSearchService")
	protected DpcUserSearchService dpcUserSearchService;

	@Autowired(required = true)
	@Qualifier("accessTokenParser")
	protected AccessTokenParser accessTokenParser;

	/**
	 * 運営ユーザID1
	 */
	@Autowired(required = true)
	@Qualifier("operatorUserId1")
	protected Integer operatorUserId1;

	/**
	 * 運営ユーザID2
	 */
	@Autowired(required = true)
	@Qualifier("operatorUserId2")
	protected Integer operatorUserId2;

	/**
	 * 認証処理を実行する。
	 *
	 * @param ctx Context
	 * @param action ActionMethod
	 * @throws Exception 例外
	 */
	@Override
	public void execute(DpContext ctx, Method action) throws Exception {

		Box requestBox = SpringUtil.getBean(SpringBeanName.REQUEST_BOX.value, super.servletContext);
		Box sessionBox = SpringUtil.getBean(SpringBeanName.SESSION_BOX.value, super.servletContext);
		DpUserInfo dpUserInfo = sessionBox.get(Constants.DP_LOGIN_USER_INFO_KEY_S);
		boolean sessionUserUseFlg = false;

		Integer jgiNo;
		Integer altJgiNo;
		// Accessトークン変換。なければ既存処理に遷移。アクセストークンが不正なら、例外を返す。（最終的に権限なし画面が出る。）

		ParsedToken accessToken;
		try {
			accessToken = accessTokenParser.parseAccessToken(ctx.getRequest());
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, "Access Token Processing Error"));
		}

		if (accessToken != null) {
			jgiNo = dpcUserSearchService.selectLoginJgiNoByUserId(accessToken.getJgiNo());
			altJgiNo = dpcUserSearchService.selectLoginJgiNoByUserId(accessToken.getAltJgiNo());

			final String loginUserIDStr = accessToken.getJgiNo();
			final String jgiNoStr = String.format("%07d", jgiNo);
			final String altJgiNoStr = String.format("%07d", altJgiNo);

			Cookie cookieJgiNo = new Cookie(OFFICIAL_USER_KEY, jgiNoStr);
			Cookie cookieAltJgiNo = new Cookie(ALT_USER_COOKIE_KEY, altJgiNoStr);
			Cookie cookieXUser= new Cookie(X_USER_KEY, loginUserIDStr);
			ctx.getResponse().addCookie(cookieJgiNo);
			ctx.getResponse().addCookie(cookieAltJgiNo);
			ctx.getResponse().addCookie(cookieXUser);
			try {
				// Cookieから取得した従業員番号を基に検証用ハッシュ値を算出する
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(("takeda" + loginUserIDStr + jgiNoStr).getBytes());
				byte[] digest = md.digest();

				String calcVerifier = DatatypeConverter.printHexBinary(digest);
				Cookie cookieVerifier = new Cookie(VERIFIER_KEY, calcVerifier);
				ctx.getResponse().addCookie(cookieVerifier);

			} catch (NoSuchAlgorithmException e) {
				// 算出時にエラーが発生した場合 (暗号アルゴリズムが使用できない)
				invalidate(ctx);
				final String errMsg = "verifier error.";
				throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg), e);
			}

		} else {
			// 従業員番号を取得(クッキーから）
			jgiNo = getJgiNo(ctx);
			altJgiNo = getAltJgiNo(ctx);
		}


		// Session × 従業員
		if (dpUserInfo != null) {

			// 代行設定されている場合
			// [正規の従業員番号]と[取得した代行の従業員番号]が等しい場合は設定されていないことになる仕様)
			if (altJgiNo != null && altJgiNo != jgiNo) {
				if (!dpUserInfo.isOfficialUser()) {
					if (dpUserInfo.getSettingUserJgiNo().equals(altJgiNo)) {
						sessionUserUseFlg = dpcUserSearchService.isAuthorized(dpUserInfo, ctx.getDpMetaInfo());
					}
				} else if (dpUserInfo.isOfficialUser() && dpUserInfo.getLoginUserJgiNo().equals(jgiNo)) {
					sessionUserUseFlg = dpcUserSearchService.isAuthorized(dpUserInfo, ctx.getDpMetaInfo());
					if (sessionUserUseFlg) {
						// ＤＢアクセスして、代行ユーザにしなくてよいかを確認
						// 代行でログイン可能ならばセッションを破棄(代行の従業員に切り替える)
						// 代行でログイン不可ならばセッションを継続(正規の従業員をそのまま利用する)
						sessionUserUseFlg = !(dpcUserSearchService.canAltLogin(altJgiNo, ctx.getDpMetaInfo()));
					}
				}
			}

			// 代行設定されていない場合
			else {
				if (dpUserInfo.isOfficialUser() && dpUserInfo.getLoginUserJgiNo().equals(jgiNo)) {
					sessionUserUseFlg = dpcUserSearchService.isAuthorized(dpUserInfo, ctx.getDpMetaInfo());
				}
			}
		}

		// Session上のデータが使えない場合の処理(DBアクセス)
		if (!sessionUserUseFlg) {

			invalidate(ctx); // SESSIONの無効化

			// 許可ID検証
			try {
				dpcUserSearchService.dataAccessPermitCheck(DB_CERTIFICATE_ID_LIST, ctx.getDpMetaInfo());
			} catch (AccessDeniedException e) {
				invalidate(ctx); // SESSIONの無効化
				throw new UserTimeoutException(e.getConveyance());
			}

			// DB認証
			try {
				dpUserInfo = dpcUserSearchService.searchDpUserInfo(jgiNo, altJgiNo, ctx.getDpMetaInfo());
			} catch (CertificationException e) {
				invalidate(ctx); // SESSIONの無効化
				throw e;
			} catch (DataNotFoundException e2) {
				invalidate(ctx); // SESSIONの無効化
				final String errMsg = "認証に関連する何れかのマスタに登録がない";
				throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg), e2);
			}

		}

		// 運用ユーザの場合はセッションタイムアウトを10分にする
		if(jgiNo.equals(operatorUserId1) || jgiNo.equals(operatorUserId2)){

			HttpSession session = ctx.getRequest().getSession(false);
			if(session != null && session.getMaxInactiveInterval() != 600){
				session.setMaxInactiveInterval(600);
			}
		}

		// REQUEST/SESSION/THREADに認証情報を格納
		requestBox = SpringUtil.getBean(SpringBeanName.REQUEST_BOX.value, super.servletContext);
		sessionBox = SpringUtil.getBean(SpringBeanName.SESSION_BOX.value, super.servletContext);
		requestBox.put(Constants.SESSION_ACCESS_PERMISSION_KEY_R, Constants.SESSION_ACCESS_PERMISSION_FLG_R);
		sessionBox.put(Constants.DP_LOGIN_USER_INFO_KEY_S, dpUserInfo);

		// アクセスログ
		accessLog(ctx, dpUserInfo);

		setDpuserInfo(servletContext, ctx.getRequest());
	}

	/**
	 * 正規の従業員の従業員番号を取得する。<br>
	 * 正規の従業員の従業員番号が見つからない場合はCertificationExceptionをスローする。
	 *
	 * @param ctx コンテキスト
	 * @return 従業員番号
	 * @throws CertificationException 従業員番号見つけられない場合
	 */
	protected Integer getJgiNo(DpContext ctx) throws CertificationException {
		// 従業員番号の取得先を、HTTPヘッダーからCookieに変更
		// また、Cookieが改竄されていないかも併せて確認
		String jgiNo = getJgiNoAfterVerifing(ctx);
		if (LOG.isDebugEnabled()) {
			LOG.debug("***JgiNo=" + jgiNo);
		}
		if (StringUtils.isEmpty(jgiNo)) {
			invalidate(ctx);
			final String errMsg = "jgiNo is null.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}
		if (!ValidationUtil.isInteger(jgiNo)) {
			invalidate(ctx);
			final String errMsg = "jgiNo is not integer.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}
		return ConvertUtil.parseInteger(jgiNo);
	}

	/**
	 * 従業員番号、代行ユーザの文字列を数値に変換する<br>
	 * 変換できない場合はCertificationExceptionをスローする。
	 *
	 * @param ctx コンテキスト
	 * @return 従業員番号
	 * @throws CertificationException 変換できない場合
	 */
	protected Integer strToIntJgiNo(String jgiNo) throws CertificationException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("***JgiNo=" + jgiNo);
		}
		if (StringUtils.isEmpty(jgiNo)) {
			final String errMsg = "jgiNo is null.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}
		if (!ValidationUtil.isInteger(jgiNo)) {
			final String errMsg = "jgiNo is not integer.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}
		return ConvertUtil.parseInteger(jgiNo);
	}

	/**
	 * 代行ユーザの従業員番号を取得する。<br>
	 * 代行ユーザの従業員番号が見つからない場合はnullを返す。
	 *
	 * @param ctx コンテキスト
	 * @return 従業員番号
	 */
	protected Integer getAltJgiNo(final DpContext ctx) {
		try {
			Cookie[] cookies = ctx.getRequest().getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (ALT_USER_COOKIE_KEY.equals(cookie.getName())) {
						String altJgiNo = cookie.getValue();
						if (StringUtils.isNotBlank(altJgiNo) && ValidationUtil.isInteger(altJgiNo)) {
							return ConvertUtil.parseInteger(altJgiNo);
						}
					}
				}
			}
		} catch (Exception e) {
			// さすがにここで落とすわけには行かない
			if (LOG.isErrorEnabled()) {
				LOG.error("代行ユーザの従業員番号取得時に致命的なエラーが発生", e);
			}
		}
		return null;
	}

	/**
	 * DpuserInfoをスレッド中に格納する。
	 *
	 * @param context サーブレットコンテキスト情報
	 * @param request リクエスト情報
	 */
	public static void setDpuserInfo(ServletContext context, HttpServletRequest request) {
		Box requestBox = SpringUtil.getBean(Constants.SpringBeanName.REQUEST_BOX.value, context);
		Boolean setThreadPoolFlg = requestBox.get(Constants.SESSION_ACCESS_PERMISSION_KEY_R);
		if (setThreadPoolFlg != null && setThreadPoolFlg) {
			Box sessionBox = SpringUtil.getBean(Constants.SpringBeanName.SESSION_BOX.value, context);
			DpUserInfo dpUserInfo = sessionBox.get(Constants.DP_LOGIN_USER_INFO_KEY_S);
			if (dpUserInfo != null) {
				DpUserInfo.setDpUserInfo(dpUserInfo);
				if (LOG.isDebugEnabled()) {
					LOG.debug("*** DpUserInfo --> Thread");
				}
			}
		}
	}

	/**
	 * DpuserInfoをスレッドからクリアする。
	 *
	 * @param context サーブレットコンテキスト情報
	 * @param request リクエスト情報
	 */
	public static void clearDpuserInfo(ServletContext context, HttpServletRequest request) {
		Box requestBox = SpringUtil.getBean(Constants.SpringBeanName.REQUEST_BOX.value, context);
		Boolean setThreadPoolFlg = requestBox.get(Constants.SESSION_ACCESS_PERMISSION_KEY_R);
		if (setThreadPoolFlg != null && setThreadPoolFlg) {
			DpUserInfo.clearDpUserInfo();
			if (LOG.isDebugEnabled()) {
				LOG.debug("*** DpUserInfo Clear");
			}
		}
	}

	/**
	 * セッションを無効化する。
	 *
	 * @param ctx コンテキスト情報
	 */
	public static void invalidate(DpContext ctx) {
		HttpSession session = ctx.getRequest().getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	/**
	 * アクセスログを出力する。
	 *
	 * @param ctx
	 * @param userInfo
	 */
	private void accessLog(DpContext ctx,DpUserInfo userInfo) {

		String jgiName = "";
		String sessionId = "";
		String sessionNew = "";
		String sessionCreationTime = "";
		String sId = "";
		String gCode = "";
		String fCode = "";

		// 従業員番号の取得先をHTTPヘッダーからログインユーザ情報に変更
		// ※従業員番号のNULL・数値チェックもこのタイミングでは実施しないよう変更
		//   (事前処理にて必ず実施されるため)
		Integer userId = null;
		Integer jgiNo = null;
		String path = ctx.getRequest().getRequestURI();

		DpMetaInfo  metaInfo = ctx.getDpMetaInfo();
		if(metaInfo != null){
			sId = metaInfo.getSId();
			gCode = metaInfo.getGCode();
			fCode = metaInfo.getFCode();
		}

		HttpSession session = ctx.getRequest().getSession(false);
		if (session != null) {
			sessionId = session.getId();
			sessionNew = Boolean.toString(session.isNew());
			sessionCreationTime = DateUtil.toString(new Date(session.getCreationTime()), "yyyy/MM/dd HH:mm:ss");
		}

		DpUser loginUser = userInfo.getLoginUser();
		if(loginUser != null){
			// ログインユーザ情報の従業員番号を設定
			userId = loginUser.getJgiNo();
			jgiNo = loginUser.getJgiNo();
			jgiName = loginUser.getJgiName();
		}

		StringBuffer logSb = new StringBuffer();
		logSb.append("I0100001");
		logSb.append(" ").append(userId);
		logSb.append(" ").append(jgiNo);
		logSb.append(" ").append("\"").append(jgiName).append("\"");
		logSb.append(" ").append(sessionId);
		logSb.append(" ").append(sessionNew);
		logSb.append(" ").append(sessionCreationTime);
		logSb.append(" ").append("-"); // portletId
		logSb.append(" ").append("-"); // portletMode
		logSb.append(" ").append(sId); // スクリーンID
		logSb.append(" ").append(gCode + fCode); // ファンクションID
		logSb.append(" ").append("-"); // ステータス（Normal,Bucket）
		logSb.append(" ").append(path); // path（jsp）
		logSb.append(" ").append("-"); // ???
		logSb.append(" ").append("-"); // システムID（ランキング用）
		logSb.append(" ").append("-"); // 情報ID（ランキング用）
		LOG.info(logSb.toString());
	}

	/**
	 * Cookieから正規の従業員の従業員番号を取得する。<br>
	 * また、Cookieが改竄されていないかも併せて確認する。<br>
	 * 正規の従業員の従業員番号が見つからない場合・Cookieの改竄を検知した場合はCertificationExceptionをスローする。
	 *
	 * @param ctx コンテキスト情報
	 * @return 従業員番号
	 * @throws CertificationException 認証失敗を表す例外
	 */
	protected String getJgiNoAfterVerifing(final DpContext ctx) throws CertificationException {

		// ユーザID
		String userId = "";
		// 従業員番号
		String jgiNo = "";
		// 検証用ハッシュ値(Cookie)
		String verifier = "";
		// 検証用ハッシュ値(算出結果)
		String calcVerifier = "";

		// Cookieから従業員番号・Cookieの改竄チェック用情報を取得する
		Cookie[] cookies = ctx.getRequest().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// ユーザID
				if (X_USER_KEY.equals(cookie.getName())) {
					userId = cookie.getValue();
				}
				// 従業員番号
				if (OFFICIAL_USER_KEY.equals(cookie.getName())) {
					jgiNo = cookie.getValue();
				}
				// 検証用ハッシュ値(Cookie)
				if (VERIFIER_KEY.equals(cookie.getName())) {
					verifier = cookie.getValue();
				}
			}
		}

		// CookieのユーザIDが存在しない場合
		if (StringUtils.isEmpty(userId)) {
			invalidate(ctx);
			final String errMsg = "userId is null.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}

		// Cookieの従業員番号が存在しない場合
		if (StringUtils.isEmpty(jgiNo)) {
			invalidate(ctx);
			final String errMsg = "jgiNo is null.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}

		// Cookieの検証用ハッシュ値が存在しない場合
		if (StringUtils.isEmpty(verifier)) {
			invalidate(ctx);
			final String errMsg = "verifier is null.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}

		try {
			// Cookieから取得したユーザID・従業員番号を基に検証用ハッシュ値を算出する
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(("takeda" + userId + jgiNo).getBytes());
			byte[] digest = md.digest();

			calcVerifier = DatatypeConverter.printHexBinary(digest);

		} catch (NoSuchAlgorithmException e) {
			// 算出時にエラーが発生した場合 (暗号アルゴリズムが使用できない)
			invalidate(ctx);
			final String errMsg = "verifier error.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg), e);
		}

		// Cookieから取得した検証用ハッシュ値と、算出した検証用ハッシュ値が一致しない場合
		// Cookieを改竄されたとみなす
		if (!StringUtils.equals(verifier, calcVerifier)) {
			invalidate(ctx);
			final String errMsg = "cookie tampered.";
			throw new CertificationException(new Conveyance(ErrMessageKey.CERTIFICATION_ERROR, errMsg));
		}

		// Cookieの従業員番号を返却
		return jgiNo;
	}
}
