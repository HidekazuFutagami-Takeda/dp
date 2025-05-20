package jp.co.takeda.web.in.dps;

import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.DistPlanningListSummaryScDto;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dps930C00((医)計画立案準備ダウンロード画面)のフォームクラス
 *
 * @author takahashi
 */
public class Dps920C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * アップロード用ファイルのダウンロード取得キー
	 */
	public static final BoxKey DPS920C00_DATA_EXIST = new BoxKeyPerClassImpl(Dps920C00Form.class, Boolean.class);

	// -------------------------------
	// report
	// -------------------------------
	/**
	 * ファイル名
	 */
	protected String downloadFileName;

	/**
	 * コンテンツレングス
	 */
	protected int contentsLength;

	@Override
	public ContentsType getContentType() {
		return ContentsType.XLS;
	}

	@Override
	public String getDownLoadFileName() {
		return downloadFileName;
	}

	@Override
	public int getContentLength() {
		return Downloadable.DEF_LENGTH;
	}

	// -------------------------------
	// Utility
	// -------------------------------
	/**
	 * ダウンロードファイル名を設定する。
	 *
	 * @param downloadFileName ダウンロードファイル名
	 */
	public void setDownLoadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * ファイル種類リスト
	 */
	private List<CodeAndValue> fileTypeList;

	/**
	 * 組織コード(本部)
	 */
	private String sosCd1;

	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 品目のカテゴリ
	 */
	private String category;

	/**
	 * 品目のカテゴリ
	 */
	private String siiCtg;

	/**
	 * 品目のカテゴリ
	 */
	private String vacCtg;

	// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 選択されたカテゴリ
	 */
	private String selectCategory;

	/**
	 * ファイル種類
	 */
	private String fileType;
	// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	/**
	 * ファイル名
	 */
	private String fileName;
	// add End 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * ファイル種類リストを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public List<CodeAndValue> getFileTypeList() {
		return fileTypeList;
	}

	/**
	 * ファイル種類リストを設定する。
	 *
	 * @param fileType ファイル種類
	 */
	public void setFileTypeList(List<CodeAndValue> fileTypeList) {
		this.fileTypeList = fileTypeList;
	}

	/**
	 * 組織コード(本部)を取得する。
	 *
	 * @return sosCd1 組織コード(本部)
	 */
	public String getSosCd1() {
		return sosCd1;
	}

	/**
	 * 組織コード(本部)を設定する。
	 *
	 * param sosCd1 組織コード(本部)
	 */
	public void setSosCd1(String sosCd1) {
		this.sosCd1 = sosCd1;
	}

	/**
	 * 組織コード(支店)を取得する。
	 *
	 * @return sosCd2 組織コード(支店)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 組織コード(支店)を設定する。
	 *
	 * param sosCd2 組織コード(支店)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * param category カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 品目カテゴリリストを取得する。
	 *
	 * @return prodCategoryList 品目カテゴリリスト
	 */
	public List<CodeAndValue> getProdCategoryList() {
		return prodCategoryList;
	}

	/**
	 * 選択されたカテゴリリストを取得する。
	 *
	 * @return selectCategory 選択されたカテゴリ
	 */
	public String getSelectCategory() {
		return selectCategory;
	}

	/**
	 * ファイル種類を取得する。
	 *
	 * @return fileType ファイル種類
	 */
	public String getFileType() {
		return fileType;
	}
	// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	/**
	 * カテゴリを設定する。
	 *
	 * param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 仕入のカテゴリコードを取得する。
	 *
	 * param siiCtg カテゴリ:仕入
	 */
	public String getSiiCtg() {
		return siiCtg;
	}

	/**
	 * 仕入のカテゴリコードを設定する。
	 *
	 * param siiCtg カテゴリ：仕入
	 */
	public void setSiiCtg(String siiCtg) {
		this.siiCtg = siiCtg;
	}

	/**
	 * ワクチンのカテゴリコードを取得する。
	 *
	 * param vacCtg カテゴリ:ワクチン
	 */
	public String getvacCtg() {
		return vacCtg;
	}

	/**
	 * ワクチンのカテゴリコードを設定する。
	 *
	 * param vacCtg カテゴリ：ワクチン
	 */
	public void setVacCtg(String vacCtg) {
		this.vacCtg = vacCtg;
	}

	// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	/**
	 * 品目カテゴリリストを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	/**
	 * 選択されたカテゴリを設定する。
	 *
	 * @param selectCategory 選択されたカテゴリ
	 */
	public void setSelectCategory(String selectCategory) {
		this.selectCategory = selectCategory;
	}

	/**
	 * ファイル種類を設定する。
	 *
	 * @param fileType ファイル種類
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能

	// add Start 2024/2/26 K.Suzuki 2024年2月　チェックツール追加
	/**
	 * ファイル名を設定する。
	 *
	 * @param fileName ファイル名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * ファイル名を取得する。
	 *
	 * param fileName ファイル名
	 */
	public String getFileName() {
		return fileName;
	}
	// add end 2024/2/26 K.Suzuki 2024年2月　チェックツール追加

	// -------------------------------
	// convert
	// -------------------------------

	/**
	 * Formから営業所計画アップロード用計画対象品目検索条件を表すDTOを生成する。
	 *
	 * @return 営業所計画アップロード用計画対象品目検索条件を表すDTO
	 */
	public DistPlanningListSummaryScDto convertDistPlanningListSummaryScDto() {

		// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		//return new DistPlanningListSummaryScDto(this.sosCd1, this.sosCd2, this.sosCd3, this.category);
		return new DistPlanningListSummaryScDto(this.sosCd1, this.sosCd2, this.sosCd3, this.category, this.selectCategory);
		// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.fileTypeList = null;
		this.sosCd1 = null;
		this.sosCd2 = null;
		this.sosCd3 = null;
		this.category = null;
		// add Start 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
		this.prodCategoryList = null;
		this.selectCategory = null;
		this.fileType = null;
		// add End 2022/6/08 H.Futagami バックログNo.4　削除施設・調整あり計画データのダウンロード機能
	}

}
