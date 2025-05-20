package jp.co.takeda.logic;

import jp.co.takeda.service.DpsCodeMasterSearchService;

/**
 * 仕入品(一般)を判定するロジッククラス
 *
 * @author siwamoto
 */
public class GetGeneralFlg {


	/**
	 * 汎用マスタ検索サービス
	 */
	private DpsCodeMasterSearchService dpsCodeMasterSearchService;

	/**
	 * 麻薬コード
	 */
	private static final String MAYAKU = "0157";

	/**
	 * グループコード
	 */
	private String groupCode;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * コンストラクタ
	 *
	 * @param prodCode グループコード
	 * @param category 品目カテゴリ
	 * @param dpsCodeMasterSearchService 汎用マスタ検索サービス
	 */
	public GetGeneralFlg(String groupCode, String category, DpsCodeMasterSearchService dpsCodeMasterSearchService) {
		this.groupCode = groupCode;
		this.category = category;
		this.dpsCodeMasterSearchService = dpsCodeMasterSearchService;
	}

	/**
	 * カテゴリが仕入品、かつ麻薬コード以外の場合、trueとなる。
	 *
	 * @param groupCode グループコード
	 * @return 納入計画システム用の削除フラグ
	 */
	public Boolean generalFlg() {
		if ((dpsCodeMasterSearchService.isSiire(category)) && (!groupCode.equals(MAYAKU))) {
			return true;
		}
		return false;
	}
}
