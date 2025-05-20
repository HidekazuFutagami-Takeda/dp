package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dao.div.ProdPlanLevel;
import jp.co.takeda.dto.TmsTytenPlanAddDetailDto;
import jp.co.takeda.dto.TmsTytenPlanAddInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddScDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps502C02((医)特約店別計画追加画面)のフォームクラス
 *
 * @author yokokawa
 */
public class Dps502C02Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS502C02_DATA_R = new BoxKeyPerClassImpl(Dps502C02Form.class,
			TmsTytenPlanAddResultDto.class);

	/**
	 * 計画立案レベル・営業所
	 */
	public static final ProdPlanLevel DPS502C02_PLANLEVEL_OFFICE = ProdPlanLevel.OFFICE;

	/**
	 * 計画立案レベル・特約店
	 */
	public static final ProdPlanLevel DPS502C02_PLANLEVEL_WS = ProdPlanLevel.WS;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 価ベース区分
	 */
	private String kaBaseKb;

	/**
	 * 特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 特約店名
	 */
	private String tmsTytenName;

	/**
	 * 支店コード
	 */
	private String brCodeInput;

	/**
	 * 営業所コード
	 */
	private String distCodeInput;

	/**
	 * 営業所名
	 */
	private String officeName;

	/**
	 * 特約店コード(検索時)
	 */
	private String searchTmsTytenCd;

	/**
	 * 特約店名(検索時)
	 */
	private String searchTmsTytenName;

	/**
	 * 支店コード(検索時)
	 */
	private String searchBrCodeInput;

	/**
	 * 営業所コード(検索時)
	 */
	private String searchDistCodeInput;

	/**
	 * 営業所名(検索時)
	 */
	private String searchOfficeName;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=品目固定コード
	 * Sprit文字列[2]=計画値UH
	 * Sprit文字列[3]=計画値P
	 * </pre>
	 */
	private String[] rowIdList;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 価ベース区分を取得する。
	 *
	 * @return 価ベース区分
	 */
	public String getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 価ベース区分を設定する。
	 *
	 * @param kaBaseKb 価ベース区分
	 */
	public void setKaBaseKb(String kaBaseKb) {
		this.kaBaseKb = kaBaseKb;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店コードを設定する。
	 *
	 * @param tmsTytenCd 特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 特約店名称を取得する。
	 *
	 * @return 特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 特約店名称を設定する。
	 *
	 * @param tmsTytenName 特約店名称
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
	}

	/**
	 * 支店コードを設定する。
	 *
	 * @return 支店コード
	 */
	public String getBrCodeInput() {
		return brCodeInput;
	}

	/**
	 * 支店コードを取得する。
	 *
	 * @param brCode 支店コード
	 */
	public void setBrCodeInput(String brCodeInput) {
		this.brCodeInput = brCodeInput;
	}

	/**
	 * 営業所コードを取得する。
	 *
	 * @return 営業所コード
	 */
	public String getDistCodeInput() {
		return distCodeInput;
	}

	/**
	 * 営業所コードを設定する。
	 *
	 * @param distCode 営業所コード
	 */
	public void setDistCodeInput(String distCodeInput) {
		this.distCodeInput = distCodeInput;
	}

	/**
	 * 営業所名称を取得する。
	 *
	 * @return 営業所名称
	 */
	public String getOfficeName() {
		return officeName;
	}

	/**
	 * 営業所名称を設定する。
	 *
	 * @param officeName 営業所名称
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	/**
	 * 特約店コード(検索時)を取得する。
	 *
	 * @return 特約店コード(検索時)
	 */
	public String getSearchTmsTytenCd() {
		return searchTmsTytenCd;
	}

	/**
	 * 特約店コード(検索時)を設定する。
	 *
	 * @param searchTmsTytenCd 特約店コード(検索時)
	 */
	public void setSearchTmsTytenCd(String searchTmsTytenCd) {
		this.searchTmsTytenCd = searchTmsTytenCd;
	}

	/**
	 * 特約店名(検索時)を取得する。
	 *
	 * @return searchTmsTytenName 特約店名(検索時)
	 */
	public String getSearchTmsTytenName() {
		return searchTmsTytenName;
	}

	/**
	 * 特約店名(検索時)を設定する。
	 *
	 * @param searchTmsTytenName 特約店名(検索時)
	 */
	public void setSearchTmsTytenName(String searchTmsTytenName) {
		this.searchTmsTytenName = searchTmsTytenName;
	}

	/**
	 * 支店コード(検索時)を取得する。
	 *
	 * @return 支店コード(検索時)
	 */
	public String getSearchBrCodeInput() {
		return searchBrCodeInput;
	}

	/**
	 * 支店コード(検索時)を設定する。
	 *
	 * @param searchBrCodeInput 支店コード(検索時)
	 */
	public void setSearchBrCodeInput(String searchBrCodeInput) {
		this.searchBrCodeInput = searchBrCodeInput;
	}

	/**
	 * 営業所コード(検索時)を取得する。
	 *
	 * @return 営業所コード(検索時)
	 */
	public String getSearchDistCodeInput() {
		return searchDistCodeInput;
	}

	/**
	 * 営業所コード(検索時)を設定する。
	 *
	 * @param searchDistCodeInput 営業所コード(検索時)
	 */
	public void setSearchDistCodeInput(String searchDistCodeInput) {
		this.searchDistCodeInput = searchDistCodeInput;
	}

	/**
	 * 営業所名(検索時)を取得する。
	 *
	 * @return searchOfficeName 営業所名(検索時)
	 */
	public String getSearchOfficeName() {
		return searchOfficeName;
	}

	/**
	 * 営業所名(検索時)を設定する。
	 *
	 * @param searchOfficeName 営業所名(検索時)
	 */
	public void setSearchOfficeName(String searchOfficeName) {
		this.searchOfficeName = searchOfficeName;
	}

	/**
	 * 追加・更新行の情報リストを取得する。
	 *
	 * @return 選択行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 追加・更新行の情報リストを設定する。
	 *
	 * @param selectRowIdList 選択行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
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
	 * @param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return category カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 品目カテゴリリストを取得する。
	 *
	 * @return prodCategory 品目カテゴリ
	 */
	public List<CodeAndValue> getProdCategoryList() {
		return prodCategoryList;
	}

	/**
	 * 品目カテゴリリストを設定する。
	 *
	 * @param prodCategory 品目カテゴリ
	 */
	public void setProdCategoryList(List<CodeAndValue> prodCategoryList) {
		this.prodCategoryList = prodCategoryList;
	}

	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * 特約店別計画追加 検索条件DTOに変換する。
	 *
	 * @return 特約店別計画追加 検索条件DTO
	 */
	public TmsTytenPlanAddScDto convertTmsTytenPlanAddScDto() {
		return new TmsTytenPlanAddScDto(this.tmsTytenCd, this.brCodeInput, this.distCodeInput,
				KaBaseKb.getInstance(this.kaBaseKb));
	}

	/**
	 * 特約店別計画追加 入力値DTOに変換する。
	 *
	 * @return 特約店別計画追加 入力値DTO
	 */
	public TmsTytenPlanAddInputDto convertTmsTytenPlanAddInputDto() {
		List<TmsTytenPlanAddDetailDto> inputList = new ArrayList<TmsTytenPlanAddDetailDto>();
		if (this.rowIdList == null) {
			return new TmsTytenPlanAddInputDto(this.searchTmsTytenCd, this.searchBrCodeInput, this.searchDistCodeInput,
					KaBaseKb.getInstance(this.kaBaseKb), inputList);
		}
		for (String rowData : this.rowIdList) {
			String[] data = ConvertUtil.splitConmma(rowData);

			// CSVから入力値を取得
			String prodCode = data[0];
			String plannedValueUh = data[1];
			String plannedValueP = data[2];

			// 入力されているか判定
			if (StringUtils.isEmpty(plannedValueUh) && StringUtils.isEmpty(plannedValueP)) {
				// 入力が無い行は読み飛ばす
				continue;
			}

			// 入力値をDTOにつめる
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(prodCode);

			WsPlan wsPlan = new WsPlan();

			// 千円単位を1円単位に変換して設定
			wsPlan.setPlannedValueUh(ConvertUtil.parseMoneyToNormalUnit(plannedValueUh));
			wsPlan.setPlannedValueP(ConvertUtil.parseMoneyToNormalUnit(plannedValueP));

			TmsTytenPlanAddDetailDto detailData = new TmsTytenPlanAddDetailDto(plannedProd, wsPlan);
			inputList.add(detailData);
		}

		return new TmsTytenPlanAddInputDto(this.searchTmsTytenCd, this.searchBrCodeInput, this.searchDistCodeInput,
				KaBaseKb.getInstance(this.kaBaseKb), inputList);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// 元画面から送信される情報以外はクリア
		//this.kaBaseKb = null;
		this.tmsTytenCd = null;
		this.tmsTytenName = null;
		this.brCodeInput = null;
		this.distCodeInput = null;
		this.officeName = null;
		this.searchTmsTytenCd = null;
		this.searchTmsTytenName = null;
		this.searchBrCodeInput = null;
		this.searchDistCodeInput = null;
		this.searchOfficeName = null;
		this.rowIdList = null;
	}
}
