package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;
import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 出力ファイル情報出力区分を表す列挙
 * 
 * @author khashimoto
 */
public enum OutputType implements DbValue<String> {

	/**
	 * 営業所→施設特約店別計画配分
	 */
	OFFICE_INS_WS_PLAN_DIST("1"),

	/**
	 * 担当者→施設特約店別計画配分
	 */
	MR_INS_WS_PLAN_DIST("2"),

	/**
	 * 特約店別計画配分
	 */
	WS_PLAN_DIST("3"),

	/**
	 * ワクチン施設特約店別計画配分
	 */
	VAC_INS_WS_PLAN_DIST("4"),

	/**
	 * 施設医師別計画配分
	 */
	INS_DOC_PLAN_DIST("5");


	/**
	 * コンストラクタ
	 * 
	 * @param value 出力ファイル情報出力区分を表す文字
	 */
	private OutputType(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 出力ファイル情報出力区分を表す文字を取得する。
	 * 
	 * @return 出力ファイル情報出力区分を表す文字
	 */
	public String getDbValue() {
		return value;
	}

	/**
	 *ファイル名を取得する。
	 * 
	 * 
	 * @param sysType 管理対象業務区分
	 * @param outputType 出力ファイル情報出力区分
	 * @return ファイル名
	 */
	public static String convertFileName(SysType sysType, OutputType outputType) {

		if (sysType == null) {
			final String errMsg = "管理対象業務区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (outputType == null) {
			final String errMsg = "出力ファイル情報出力区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String fileName = null;

		switch (outputType) {
			case OFFICE_INS_WS_PLAN_DIST:
				fileName = "Haibun_MissList_";
				break;
			case MR_INS_WS_PLAN_DIST:
				fileName = "Haibun_MissList_";
				break;
			case WS_PLAN_DIST:
				fileName = "Haibun_MissList_Tyten";
				break;
			case VAC_INS_WS_PLAN_DIST:
				fileName = "Haibun_MissList_VAC";
				break;
			case INS_DOC_PLAN_DIST:
				fileName = "Haibun_MissList_DOC";
				break;
		}
		return fileName;
	}

	/**
	 * コード値から列挙を逆引きする。
	 * 
	 * @param code コード値
	 * @return 列挙
	 */
	public static OutputType getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (OutputType entry : OutputType.values()) {
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
	public static class OutputTypeTypeHandler extends AbstractEnumTypeHandler {

		public OutputTypeTypeHandler() {
			super(new EnumType(OutputType.class, java.sql.Types.BIGINT));
		}

	}
}
