package jp.co.takeda.web.cmn.sso;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

public class AccessTokenParser {

	/**
	 * LOGGER
	 */
	private static final Log log = LogFactory.getLog(AccessTokenParser.class);

	private static final int IDX_JGI_NO = 0;

	private static final int IDX_ALT_JGI_NO = 1;

    /** コード：X509 */
    private final String TYPE_X509 = "X509";
    /** JWT発行者 */
    private final String jwtIssuer;
    /** タイムアウト値 */
    private final int timeoutMin;
    /** pemファイル格納場所 */
    private final String  pemFilePath  ;

    /**
     * コンストラクタ
     * @param keyFilePath 公開鍵ファイル格納場所
     * @param timeOutMinutes タイムアウト値
     * @param jwtIssuer JWT発行者
     */
	public AccessTokenParser(String keyFilePath,int timeOutMinutes,String jwtIssuer){
		this.pemFilePath = keyFilePath;
		this.timeoutMin = timeOutMinutes;
		this.jwtIssuer = jwtIssuer;
	}


	/**
	 * アクセストークンパーサー
	 * @param request
	 * @return アクセストークンが無ければnullを返す。
	 * @throws AccessTokenException アクセストークンが不正
	 * @throws AccessTokenFatalException アクセストークン処理異常
	 */
		public ParsedToken parseAccessToken(HttpServletRequest request) throws  AccessTokenException,AccessTokenFatalException {

        // Sessionの取得
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if(session == null){
            //セッションの新規生成
            session = ((HttpServletRequest) request).getSession(true);
        }


		try {
	        ((HttpServletRequest) request).setCharacterEncoding("UTF-8");
	        String accessToken = ((HttpServletRequest) request).getParameter("access_token");

	        if(accessToken == null) {
	        	log.info("アクセストークンなし:");
	        	return null;
	        }

	        // アクセストークンが取得できた場合
			FileInputStream fis = new FileInputStream(pemFilePath);
			BufferedInputStream bis = new BufferedInputStream(fis);

		    // get the public key
		    CertificateFactory certificateFactory;
			certificateFactory = CertificateFactory.getInstance(TYPE_X509);
		    Certificate certificate = certificateFactory.generateCertificate(bis);
		    PublicKey publicKey = certificate.getPublicKey();

	    	JwtParser parser = Jwts.parser().setSigningKey(publicKey);
	    	Jws<Claims> parseClaimsJws = parser.parseClaimsJws(accessToken);

	    	Claims jwsBody = parseClaimsJws.getBody();
			// iat (Issuad At) JWT発行日時取得
			java.util.Date iat = jwsBody.getIssuedAt();
			// sub (Subject) 取得
			String sub = jwsBody.getSubject();
			// iss (Issuer) JWT発行者取得
			String iss = jwsBody.getIssuer();

			// JWT発行日時 + 引数タイムアウト値（単位 分）
			Calendar calIat = Calendar.getInstance();
			calIat.setTime(iat);
			calIat.add(Calendar.MINUTE, timeoutMin);

			// 現在時刻取得
			Calendar calNow = Calendar.getInstance();

			// JWT期限切れチェック
			if (calIat.compareTo(calNow) < 0) {
				System.out.println("× Issuad At Error : " + iat);
	            log.info("ログイン処理＝＝＝＝＝＝Issued Atエラー:" + iat);
	            throw new AccessTokenException("Issued Atエラー");
			}

			// JWT発行者チェック
			if (!jwtIssuer.equals(iss)) {
	            log.info("ログイン処理＝＝＝＝＝＝Issuerエラー:" + iss);
	            throw new AccessTokenException("Issuerエラー");
			}

			// subjectを|で分割
			String[] subList = sub.split("\\|");

			// 従業員番号取得
			String hold_jgiNo =  getUnmaskedString(subList[IDX_JGI_NO]);

			// 従業員番号 カラチェック
			if( hold_jgiNo == null || hold_jgiNo.isEmpty()) {
	            throw new AccessTokenException("従業員番号がない");

			}
			// 代行従業員番号取得
			String hold_altJgiNo =  getUnmaskedString(subList[IDX_ALT_JGI_NO]);
			if( hold_altJgiNo == null) {
				hold_altJgiNo = "";
			}
			return new ParsedToken(hold_jgiNo, hold_altJgiNo);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("アクセストークン処理エラー 公開鍵ファイルがない:" + e.toString());
			throw new AccessTokenFatalException("アクセストークン処理エラー 公開鍵ファイルがない");

		} catch (CertificateException e) {
			e.printStackTrace();
			log.error("アクセストークン認証処理エラー 公開鍵:" + e.toString());
			throw new AccessTokenFatalException("アクセストークン処理エラー、公開鍵処理");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("アクセストークン処理エラー:" + e.toString());
			throw new AccessTokenFatalException("アクセストークン処理エラー");
		}
	}



	private String getUnmaskedString(String strEncodeUserIdMask) {
		// 先頭32文字を除去
		String strEncodeUserId = strEncodeUserIdMask.substring(32);

		// Base64デコード
		byte[] byteUserId = Base64.decodeBase64(strEncodeUserId);
		return  StringUtils.newStringUtf8(byteUserId);
	}



}
