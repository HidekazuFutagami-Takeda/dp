package jp.co.takeda.model.div;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.AbstractEnumTypeHandler;
import jp.co.takeda.a.dao.DbValue;
import jp.co.takeda.a.dao.EnumType;
import jp.co.takeda.a.exp.SystemException;

/**
 * 品目カテゴリを表す列挙 STEP4にて廃止予定 使用不可（中島光 記）
 *
 * @author tkawabata
 */
@Deprecated
public enum ProdCategory implements DbValue<String> {

	/**
	 * JPBU品（旧：MMP品） STEP4にて廃止予定 使用不可（中島光 記）
	 */
	@Deprecated
	MMP("01"),

	/**
	 * 仕入品（一般・麻薬）STEP4にて廃止予定 使用不可（中島光 記）
	 */
	@Deprecated
	SHIIRE("02"),

	/**
	 * ONC品 STEP4にて廃止予定 使用不可（中島光 記）
	 */
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
//	ONC("3");
	@Deprecated
	ONC("03"),

	/**
	 * SPBU品 STEP4にて廃止予定 使用不可（中島光 記）
	 */
	@Deprecated
	SPBU("04");
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応

	/**
	 * コンストラクタ
	 *
	 * @param value 品目カテゴリを表す文字
	 */
	private ProdCategory(String value) {
		this.value = value;
	}

	/**
	 * RDBの値
	 */
	private String value;

	/**
	 * 品目カテゴリを表す文字を取得する。
	 *
	 * @return 品目カテゴリを表す文字
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
	public static ProdCategory getInstance(String code) {

		if (code == null) {
			return null;
		}

		for (ProdCategory entry : ProdCategory.values()) {
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
	public static class ProdCategoryTypeHandler extends AbstractEnumTypeHandler {

		public ProdCategoryTypeHandler() {
			super(new EnumType(ProdCategory.class, java.sql.Types.CHAR));
		}

	}

	/**
	 * 品目カテゴリ名称を取得する。
	 *
	 * @param category 品目カテゴリを表す列挙
	 * @return 品目カテゴリ名称
	 */
	public static String getProdCategoryName(ProdCategory category) {

		String prodCategoryName = "";

		// 引数がNullの場合は空文字をRETURN
		if (category == null) {
			return prodCategoryName;
		}

		switch (category) {
			case MMP:
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
//				prodCategoryName = "JPBU(STARS)";
				prodCategoryName = "GMBU(STARS)";
// add end 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
				break;
			case SHIIRE:
				prodCategoryName = "仕入品(一般・麻薬)";
				break;
			case ONC:
				prodCategoryName = "ONC品";
				break;
// add start 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
			case SPBU:
				prodCategoryName = "SPBU品";
				break;
// add end 2018/1/1 M.Wada J17-0010_2018年4月組織変更対応
		}
		return prodCategoryName;
	}

// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
	/**
	 * 品目カテゴリ名称を取得する。
	 *
	 * @param category 品目カテゴリを表す列挙
	 * @return 品目カテゴリ名称
	 */
	public static String getProdCategoryNoStarsName(ProdCategory category) {

		String prodCategoryName = "";

		// 引数がNullの場合は空文字をRETURN
		if (category == null) {
			return prodCategoryName;
		}

		switch (category) {
			case MMP:
				prodCategoryName = "GMBU品";
				break;
			case SHIIRE:
				prodCategoryName = "仕入品(一般・麻薬)";
				break;
			case ONC:
				prodCategoryName = "ONC品";
				break;
			case SPBU:
				prodCategoryName = "SPBU品";
				break;
		}
		return prodCategoryName;
	}
// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）

	/**
	 * MMP品（JPBU品）/ONC品 を組織のフラグから取得。
	 * 仕入品は対象外。
	 *
	 * @param isOncSos ONC組織か
	 * @return ONC組織の場合、ONC。それ以外の場合、MMP（JPBU）
	 */
	public static ProdCategory getSosCategory(Boolean isOncSos){
		if(BooleanUtils.isTrue(isOncSos)){
			return ONC;
		} else {
			return MMP;
		}
	}

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * カテゴリーの一覧リストを取得する
	 *  ※values()だと並び順がいまいち
	 *
	 * @return カテゴリーのリスト　※2018上期時点で、GMBU→SPBU→ONC→仕入品
	 */
	public static List<ProdCategory> getCategoryList(){
		return getCategoryListWithExclusion((List<ProdCategory>)null);
	}

	/**
	 * カテゴリーの一覧リストを取得する
	 *  ※values()だと並び順がいまいち
	 *
	 * @param excludeCategory リストから除外したいカテゴリを指定
	 * @return カテゴリーのリスト　※2018上期時点で、GMBU→SPBU→ONC→仕入品
	 */
	public static List<ProdCategory> getCategoryListWithExclusion(ProdCategory excludeCategory){
		return getCategoryListWithExclusion(Arrays.asList(excludeCategory));	}

	/**
	 * カテゴリーの一覧リストを取得する
	 *  ※values()だと並び順がいまいち
	 *
	 * @param excludeCategoryList リストから除外したいカテゴリを指定
	 * @return カテゴリーのリスト　※2018上期時点で、GMBU→SPBU→ONC→仕入品
	 */
	public static List<ProdCategory> getCategoryListWithExclusion(List<ProdCategory> excludeCategoryList){
		List<ProdCategory> list = new ArrayList<ProdCategory>();

		if(excludeCategoryList == null || !excludeCategoryList.contains(ProdCategory.MMP))  list.add(MMP);
		if(excludeCategoryList == null || !excludeCategoryList.contains(ProdCategory.SPBU)) list.add(SPBU);
		if(excludeCategoryList == null || !excludeCategoryList.contains(ProdCategory.ONC))  list.add(ONC);
		if(excludeCategoryList == null || !excludeCategoryList.contains(ProdCategory.SHIIRE))  list.add(SHIIRE);

		return list;
	}

	/**
	 * このインスタンスの品目カテゴリ名称を取得する。
	 *
	 * @return 品目カテゴリ名称
	 */
	public String getProdCategoryName(){
		return getProdCategoryName(this);
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

// add start 2018/05/21 S.Wada J18-0002_2018年4月組織変更対応（計画支援）
	public String getProdCategoryNoStarsName(){
		return getProdCategoryNoStarsName(this);
	}
// add end   2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）


}
