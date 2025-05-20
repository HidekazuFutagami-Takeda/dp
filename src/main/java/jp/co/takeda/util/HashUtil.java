package jp.co.takeda.util;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.HashAlgorithm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ハッシュに関連するユーティリティクラス<br>
 * 
 * <pre>
 * java.security.MessageDigestを用いて、文字列のハッシュ値を取得する。
 * 
 * <strong>使用例</strong>
 * <code>
 * // DBに登録するユーザパスワードのハッシュ値を取得
 * byte[] hash = HashUtil.hash(HashAlgorithm.MD5, userPassword);
 * </code> 
 * </pre>
 * 
 * * @author tkawabata
 */
public abstract class HashUtil {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(HashUtil.class);

	/**
	 * 指定されたアルゴリズムで文字列をハッシュ値を取得する。<br>
	 * 
	 * @param str ハッシュ値の取得対象の文字列
	 * @param algorithm ハッシュアルゴリズム
	 * @return ハッシュ値
	 */
	public static byte[] hash(final String str, final HashAlgorithm algorithm) {

		if (str == null) {
			return null;
		}

		if (algorithm == null) {
			final String errMsg = "ハッシュアルゴリズムが指定されていない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ハッシュ値取得処理 str=" + str + ",algorithm=" + algorithm);
		}

		MessageDigest md = null;
		try {
			switch (algorithm) {

				case MD5:
				case SHA1:
					md = MessageDigest.getInstance(algorithm.getAlgorithmKey());
					break;
				default:
					final String errMsg = "想定外のアルゴリズムが指定された";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}

		} catch (final NoSuchAlgorithmException e) {
			final String errMsg = "ハッシュアルゴリズムが存在しない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}

		return md.digest(str.getBytes());
	}
}
