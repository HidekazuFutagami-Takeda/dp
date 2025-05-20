package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 試算元計画値を表す列挙
 * 
 * @author tkawabata
 */
public enum EstimationBase implements DbValue<String> {

	/**
	 * 計画値全体
	 */
	PLAN_FOR_ALL("1"),

	/**
	 * 増分値
	 */
	PLAN_FOR_UP("2");

	/**
	 * コンストラクタ
	 * 
	 * @param value 配分先区分を表す文字
	 */
	private EstimationBase(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 試算元計画値を表す文字を取得する。
	 * 
	 * @return 試算元計画値を表す文字
	 */
	public String getDbValue() {
		return value;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static EstimationBase getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (EstimationBase entry : EstimationBase.values()) {
			if (entry.getDbValue().equals(code)) {
				return entry;
			}
		}
		final String errMsg = "指定のコード値に対応する列挙が存在しない。code=" + code;
		throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
	}

	/**
	 * DBとのマッピングを行うタイプハンドラークラス
	 * 
	 * @author khashimoto
	 */
	public static class EstimationBaseTypeHandler extends AbstractEnumTypeHandler {

		public EstimationBaseTypeHandler() {
			super(new EnumType(EstimationBase.class, java.sql.Types.CHAR));
		}

	}
}
