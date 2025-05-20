package jp.co.takeda.web.cmn.velocity;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * MapのためのVelocityツール
 *
 * @author siwamoto
 */
public class MapTool {

	/**
	 * マップから指定キーがさす値を取り出す。
	 *
	 * @param map Mapオブジェクト
	 * @param key キー
	 * @return String
	 */
	public String getMapValue(final Map<String, Object> map, final String key) {

		if (map == null) {
			return "";
		}
		if (map.get(key) == null) {
			return "";
		} else {
			String value = map.get(key).toString();
			return value;
		}
	}

	/**
	 * チーム名称を省略化して返す。
	 *
	 * @param entry チーム名称(完全)
	 * @return 省略化したチーム名称
	 */
	public String transformTeamName(String entry) {
		if (entry == null) {
			return entry;
		}
		final String target1 = "エリアチーム";
		if (entry.contains(target1)) {
			return (entry.replaceFirst(target1, ""));
		}
		final String target2 = "チーム";
		if (entry.contains(target2)) {
			return (entry.replaceFirst(target2, ""));
		}
		final String target3 = "エリア";
		if (entry.contains(target3)) {
			return (entry.replaceFirst(target3, ""));
		}
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
// mod Start 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		final String target4 = "エリア";
//		final String target4 = "営業所";
// mod End 2022/5/20 R.Yoshida 2022年4月組織変更対応（支店→リージョン、営業所→エリア）
		if (entry.contains(target4)) {
			return (entry.replaceFirst(target4, ""));
		}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		return entry;
	}

	/**
	 * 第一引数の数値文字列から第二引数の数値文字列を引いた数字文字列を返す。
	 *
	 * @param numberStr 第一引数の数値文字列
	 * @param minusStr 第二引数の数値文字列
	 * @return 差分の数値文字列。計算できない場合は、デフォルト値を返す。
	 */
	public String getLength(String numberStr, String minusStr, String defaultStr) {
		try {
			if (StringUtils.isBlank(numberStr)) {
				int number = Integer.parseInt(defaultStr);
				int minus = Integer.parseInt(minusStr);
				Integer result = number - minus;
				return result.toString();
			}
			int number = Integer.parseInt(numberStr);
			int minus = Integer.parseInt(minusStr);
			Integer result = number - minus;
			return result.toString();
		} catch (Exception e) {
			return defaultStr;
		}
	}

	/**
	 * デフォルトスクリーンサイズ[幅]
	 */
	public static final String DEF_SCREEN_WIDTH = "1600";

	/**
	 * デフォルトスクリーンサイズ[高さ]
	 */
	public static final String DEF_SCREEN_HEIGHT = "900";

	/**
	 * デフォルトスクリーンサイズ[幅]を返す。
	 *
	 * @return デフォルトスクリーンサイズ[幅]
	 */
	public String getDefaultScreenSizeW() {
		return DEF_SCREEN_WIDTH;
	}

	/**
	 * デフォルトスクリーンサイズ[高さ]を返す。
	 *
	 * @return デフォルトスクリーンサイズ[高さ]
	 */
	public String getDefaultScreenSizeH() {
		return DEF_SCREEN_HEIGHT;
	}
}
