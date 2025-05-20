package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenPlanAddForVacDetailDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacInputDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanAddForVacScDto;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps502C05((ワ)特約店別計画追加画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps502C05Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS502C05_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps502C05Form.class, TmsTytenPlanAddForVacResultDto.class);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分
	 */
	private String kaBaseKb;

	/**
	 * 特約店コード
	 */
	private String tytenCd;

	/**
	 * 特約店名
	 */
	private String tytenName;

	/**
	 * 特約店部コード
	 */
	private String brCd;

	/**
	 * エリア特約店Gコード
	 */
	private String distCd;

	/**
	 * エリア特約店G名
	 */
	private String areaName;

	/**
	 * 特約店コード(検索時)
	 */
	private String searchTytenCd;

	/**
	 * 特約店名(検索時)
	 */
	private String searchTytenName;

	/**
	 * 特約店部コード(検索時)
	 */
	private String searchBrCd;

	/**
	 * エリア特約店Gコード(検索時)
	 */
	private String searchDistCd;

	/**
	 * エリア特約店G名(検索時)
	 */
	private String searchAreaName;

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * カテゴリ
	 */
	private String category;

	/**
	 * 更新行の識別IDリスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=品目固定コード
	 * Sprit文字列[1]=計画値
	 * </pre>
	 */
	private String[] rowIdList;

	/**
	 * 施設特約店別計画〆フラグ
	 */
	private Boolean wsEndFlg;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * 価ベース区分を取得する。
	 *
	 * @return 価ベース区分
	 */
	public String getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
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
	 * @return tytenCd 特約店コード
	 */
	public String getTytenCd() {
		return tytenCd;
	}

	/**
	 * 特約店コードを設定する。
	 *
	 * @param tytenCd 特約店コード
	 */
	public void setTytenCd(String tytenCd) {
		this.tytenCd = tytenCd;
	}

	/**
	 * 特約店名を取得する。
	 *
	 * @return tytenName 特約店名
	 */
	public String getTytenName() {
		return tytenName;
	}

	/**
	 * 特約店名を設定する。
	 *
	 * @param tytenName 特約店名
	 */
	public void setTytenName(String tytenName) {
		this.tytenName = tytenName;
	}

	/**
	 * 特約店部コードを取得する。
	 *
	 * @return brCd 特約店部コード
	 */
	public String getBrCd() {
		return brCd;
	}

	/**
	 * 特約店部コードを設定する。
	 *
	 * @param brCd 特約店部コード
	 */
	public void setBrCd(String brCd) {
		this.brCd = brCd;
	}

	/**
	 * エリア特約店Gコードを取得する。
	 *
	 * @return distCd エリア特約店Gコード
	 */
	public String getDistCd() {
		return distCd;
	}

	/**
	 * エリア特約店Gコードを設定する。
	 *
	 * @param distCd エリア特約店Gコード
	 */
	public void setDistCd(String distCd) {
		this.distCd = distCd;
	}

	/**
	 * エリア特約店G名を取得する。
	 *
	 * @return areaName エリア特約店G名
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * エリア特約店G名を設定する。
	 *
	 * @param areaName エリア特約店G名
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 特約店コード(検索時)を取得する。
	 *
	 * @return searchTytenCd 特約店コード(検索時)
	 */
	public String getSearchTytenCd() {
		return searchTytenCd;
	}

	/**
	 * 特約店コード(検索時)を設定する。
	 *
	 * @param searchTytenCd 特約店コード(検索時)
	 */
	public void setSearchTytenCd(String searchTytenCd) {
		this.searchTytenCd = searchTytenCd;
	}

	/**
	 * 特約店名(検索時)を取得する。
	 *
	 * @return searchTytenName 特約店名(検索時)
	 */
	public String getSearchTytenName() {
		return searchTytenName;
	}

	/**
	 * 特約店名(検索時)を設定する。
	 *
	 * @param searchTytenName 特約店名(検索時)
	 */
	public void setSearchTytenName(String searchTytenName) {
		this.searchTytenName = searchTytenName;
	}

	/**
	 * 特約店部コード(検索時)を取得する。
	 *
	 * @return searchBrCd 特約店部コード(検索時)
	 */
	public String getSearchBrCd() {
		return searchBrCd;
	}

	/**
	 * 特約店部コード(検索時)を設定する。
	 *
	 * @param searchBrCd 特約店部コード(検索時)
	 */
	public void setSearchBrCd(String searchBrCd) {
		this.searchBrCd = searchBrCd;
	}

	/**
	 * エリア特約店Gコード(検索時)を取得する。
	 *
	 * @return searchDistCd エリア特約店Gコード(検索時)
	 */
	public String getSearchDistCd() {
		return searchDistCd;
	}

	/**
	 * エリア特約店Gコード(検索時)を設定する。
	 *
	 * @param searchDistCd エリア特約店Gコード(検索時)
	 */
	public void setSearchDistCd(String searchDistCd) {
		this.searchDistCd = searchDistCd;
	}

	/**
	 * エリア特約店G名(検索時)を取得する。
	 *
	 * @return searchAreaName エリア特約店G名(検索時)
	 */
	public String getSearchAreaName() {
		return searchAreaName;
	}

	/**
	 * エリア特約店G名(検索時)を設定する。
	 *
	 * @param searchAreaName エリア特約店G名(検索時)
	 */
	public void setSearchAreaName(String searchAreaName) {
		this.searchAreaName = searchAreaName;
	}

	/**
	 * 更新行の識別IDリストを取得する。
	 *
	 * @return rowIdList 更新行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return (String[]) rowIdList.clone();
	}

	/**
	 * 更新行の識別IDリストを設定する。
	 *
	 * @param rowIdList 更新行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList.clone();
	}

	/**
	 * 施設特約店別計画〆フラグを取得する。
	 *
	 * @return 施設特約店別計画〆フラグ
	 */
	public Boolean getWsEndFlg() {
		return wsEndFlg;
	}

	/**
	 * 施設特約店別計画〆フラグを設定する。
	 *
	 * @param wsEndFlg 施設特約店別計画〆フラグ
	 */
	public void setWsEndFlg(Boolean wsEndFlg) {
		this.wsEndFlg = wsEndFlg;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * カテゴリを取得する。
	 *
	 * @return category カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * ワクチン用特約店別計画追加 検索条件DTOに変換する。
	 *
	 * @return ワクチン用特約店別計画追加 検索条件DTO
	 */
	public TmsTytenPlanAddForVacScDto convertTmsTytenPlanAddForVacScDto() {
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		return new TmsTytenPlanAddForVacScDto(tytenCd, brCd, distCd, KaBaseKb.getInstance(this.kaBaseKb));
	}

	/**
	 * ワクチン用特約店別計画追加 入力値DTOに変換する。
	 *
	 * @return ワクチン用特約店別計画追加 入力値DTO
	 */
	public TmsTytenPlanAddForVacInputDto convertTmsTytenPlanAddForVacInputDto() {
		List<TmsTytenPlanAddForVacDetailDto> detailDtoList = new ArrayList<TmsTytenPlanAddForVacDetailDto>();
		if (this.rowIdList == null) {
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
			return new TmsTytenPlanAddForVacInputDto(searchTytenCd, searchBrCd, searchDistCd, KaBaseKb.getInstance(this.kaBaseKb), detailDtoList);
		}
		for (String rowData : this.rowIdList) {
			final String[] data = ConvertUtil.splitConmma(rowData);
			final String seqKey = data[0];
			final String prodCode = data[1];
			final String plannedValue = data[2];
			// 登録済みの場合は保存処理を行わない
			if (StringUtils.isNotEmpty(seqKey)) {
				continue;
			}
			// 計画値が未入力の場合は保存処理を行わない
			if (StringUtils.isEmpty(plannedValue)) {
				continue;
			}
			// 品目コード
			PlannedProd plannedProd = new PlannedProd();
			plannedProd.setProdCode(prodCode);
			// 計画値
			WsPlanForVac wsPlanForVac = new WsPlanForVac();
			wsPlanForVac.setPlannedValue(ConvertUtil.parseMoneyToNormalUnit(plannedValue));
			// 明細DTOの生成
			TmsTytenPlanAddForVacDetailDto detailDto = new TmsTytenPlanAddForVacDetailDto(plannedProd, wsPlanForVac);
			detailDtoList.add(detailDto);
		}
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		return new TmsTytenPlanAddForVacInputDto(searchTytenCd, searchBrCd, searchDistCd, KaBaseKb.getInstance(this.kaBaseKb), detailDtoList);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.tytenCd = null;
		this.tytenName = null;
		this.brCd = null;
		this.distCd = null;
		this.areaName = null;
		this.searchTytenCd = null;
		this.searchTytenName = null;
		this.searchBrCd = null;
		this.searchDistCd = null;
		this.searchAreaName = null;
		this.rowIdList = null;
	}
}
