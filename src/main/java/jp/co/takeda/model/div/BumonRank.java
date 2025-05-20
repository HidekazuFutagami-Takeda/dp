package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ResultGetter;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 部門ランクを表す列挙
 * 
 * @author tkawabata
 */
public enum BumonRank implements DbValue<Integer> {

	/**
	 * 武田薬品工業薬品
	 */
	TAKEDA(0),

	/**
	 * 医営本
	 */
	IEIHON(1),

	/**
	 * 支店、特約店部
	 */
	SITEN_TOKUYAKUTEN_BU(2),

	/**
	 * 営業所、特約店G
	 */
	OFFICE_TOKUYAKUTEN_G(3),

	/**
	 * チーム
	 */
	TEAM(4),

	/**
	 * 医薬では未使用
	 */
	OTHER(5);

	/**
	 * コンストラクタ
	 * 
	 * @param value 部門ランクを表す文字
	 */
	private BumonRank(Integer value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private Integer value;

	/**
	 * 部門ランクを取得する。
	 * 
	 * @return 部門ランクを表す数値
	 */
	public Integer getDbValue() {
		return value;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static BumonRank getInstance(Integer code) {

		if (code == null) {
			return null;
		}

		for (BumonRank entry : BumonRank.values()) {
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
	public static class BumonRankTypeHandler extends AbstractEnumTypeHandler {

		public BumonRankTypeHandler() {
			super(new EnumType(BumonRank.class, java.sql.Types.INTEGER));
		}

		@Override
		public Object getResult(ResultGetter resultGetter) throws SQLException {
			Object value = resultGetter.getInt();
			if (resultGetter.wasNull()) {
				return null;
			}
			return this.convertToEnumMap.get(value);
		}
	}
}
