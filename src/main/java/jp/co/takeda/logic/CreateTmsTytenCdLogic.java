package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

import org.apache.commons.lang.StringUtils;

/**
 * 特約店コードを生成する。
 * 
 * @author tkawabata
 */
public class CreateTmsTytenCdLogic {

	/**
	 * コンストラクタ
	 * 
	 * @param tmsTytenCdPart 特約店コード(部分)
	 */
	public CreateTmsTytenCdLogic(String tmsTytenCdPart) {
		this.tmsTytenCdPart = tmsTytenCdPart;
	}

	/**
	 * 特約店コード(部分)
	 */
	private final String tmsTytenCdPart;

	/**
	 * 特約店コードゼロ
	 */
	private static final String ZERO = "0";

	/**
	 * ＴＭＳ特約店コード(13桁)を生成する。
	 * 
	 * @return ＴＭＳ特約店コード(13桁)
	 */
	public String execute() {

		// --------------------------------------
		// 引数チェック
		// --------------------------------------
		if (StringUtils.isBlank(this.tmsTytenCdPart)) {
			return null;
		}
		int length = this.tmsTytenCdPart.length();
		if (length < 3 || length > 13) {
			final String errMsg = "特約店コード(部分)の桁数が不正。length=" + length;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (length == 13) {
			return this.tmsTytenCdPart;
		}

		// --------------------------------------
		// 補完処理
		// --------------------------------------
		StringBuilder sb = new StringBuilder(this.tmsTytenCdPart);
		while (sb.length() < 13) {
			sb.append(ZERO);
		}
		return sb.toString();
	}
}
