package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.TmsTytenPlanEditInputDto;
import jp.co.takeda.dto.TmsTytenPlanEditResultDto;
import jp.co.takeda.dto.TmsTytenPlanEditScDto;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps502C01((医)特約店別計画編集画面)のフォームクラス
 *
 * @author yokokawa
 */
public class Dps502C01Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey DPS502C01_DATA_R = new BoxKeyPerClassImpl(Dps502C01Form.class, TmsTytenPlanEditResultDto.class);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 集約方法
	 */
	private String tytenKisLevel;

	/**
	 * 価ベース区分
	 */
	private String kaBaseKb;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <pre>
	 * 選択行の識別に必要な文字列が[,]区切りで挿入される。
	 * Sprit文字列[0]=シーケンスキー
	 * Sprit文字列[1]=更新日時
	 * Sprit文字列[2]=営業所コード
	 * Sprit文字列[3]=計画値UH
	 * Sprit文字列[4]=計画値P
	 * </pre>
	 */
	private String[] rowIdList;

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 特約店コードを取得する
	 *
	 * @return 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店コードを取得する
	 *
	 * @param tmsTytenCd 特約店コード
	 */
	public void setTmsTytenCd(String tmsTytenCd) {
		this.tmsTytenCd = tmsTytenCd;
	}

	/**
	 * 品目固定コードを取得する
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 集約方法を取得する
	 *
	 * @return 集約方法
	 */
	public String getTytenKisLevel() {
		return tytenKisLevel;
	}

	/**
	 * 集約方法を設定する
	 *
	 * @param tytenKisLevel 集約方法
	 */
	public void setTytenKisLevel(String tytenKisLevel) {
		this.tytenKisLevel = tytenKisLevel;
	}

	/**
	 * 価ベース区分を取得する
	 *
	 * @return 価ベース区分
	 */
	public String getKaBaseKb() {
		return kaBaseKb;
	}

	/**
	 * 価ベース区分を設定する
	 *
	 * @param kaBaseKb 価ベース区分
	 */
	public void setKaBaseKb(String kaBaseKb) {
		this.kaBaseKb = kaBaseKb;
	}

	/**
	 * 追加・更新行の情報リストを取得する
	 *
	 * @return 選択行の識別IDリスト
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 追加・更新行の情報リストを設定する
	 *
	 * @param selectRowIdList 選択行の識別IDリスト
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
	}

	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * 特約店別計画編集 検索条件DTOに変換する
	 *
	 * @return 特約店別計画編集 検索条件DTO
	 */
	public TmsTytenPlanEditScDto convertTmsTytenPlanEditScDto() {
		return new TmsTytenPlanEditScDto(this.tmsTytenCd, this.prodCode, this.tytenKisLevel, KaBaseKb.getInstance(kaBaseKb));
	}

	/**
	 * 特約店別計画編集 入力値DTOに変換する
	 *
	 * @return 特約店別計画編集 入力値DTO
	 */
	public TmsTytenPlanEditInputDto convertTmsTytenPlanEditInputDto() {
		List<WsPlan> inputList = new ArrayList<WsPlan>();
		KaBaseKb baseKb = KaBaseKb.getInstance(this.kaBaseKb);
		if (this.rowIdList == null) {
			return new TmsTytenPlanEditInputDto(inputList, baseKb);
		}

		for (String rowData : this.rowIdList) {
			// 受信データ取得
			String[] data = ConvertUtil.splitConmma(rowData);
			Long seqKey = ConvertUtil.parseLong(data[0]);
			Date upDate = ConvertUtil.parseDate(data[1]);
			String sosCd = data[2];
			Long plannedValueUh = ConvertUtil.parseLong(data[3]);
			Long plannedValueP = ConvertUtil.parseLong(data[4]);

			// 特約店別計画入力値を設定
			WsPlan wsPlan = new WsPlan();
			wsPlan.setSeqKey(seqKey);
			wsPlan.setUpDate(upDate);
			wsPlan.setSosCd(sosCd);

			// 千円単位を1円単位に変換して設定
			wsPlan.setPlannedValueUh(ConvertUtil.parseMoneyToNormalUnit(plannedValueUh));
			wsPlan.setPlannedValueP(ConvertUtil.parseMoneyToNormalUnit(plannedValueP));

			wsPlan.setTmsTytenCd(this.tmsTytenCd);
			wsPlan.setProdCode(this.prodCode);
			wsPlan.setKaBaseKb(KaBaseKb.getInstance(this.kaBaseKb));

			inputList.add(wsPlan);
		}

		return new TmsTytenPlanEditInputDto(inputList, baseKb);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.rowIdList = null;
	}
}
