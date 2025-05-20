package jp.co.takeda.web.in.dps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.TmsTytenPlanReferenceForVacResultDto;
import jp.co.takeda.dto.TmsTytenPlanReferenceHeaderForVacResultDto;
import jp.co.takeda.dto.WsPlanReferenceForVacScDto;
import jp.co.takeda.model.div.TytenKisLevel;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dps502C03((ワ)特約店別計画参照画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps502C03Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー(ヘッダ情報)
	 */
	public static final BoxKey DPS502C03_DATA_R_RESULT_HEADER = new BoxKeyPerClassImpl(Dps502C03Form.class, TmsTytenPlanReferenceHeaderForVacResultDto.class);

	/**
	 * 検索結果取得キー(一覧情報)
	 */
	public static final BoxKey DPS502C03_DATA_R_RESULT_DETAIL = new BoxKeyPerClassImpl(Dps502C03Form.class, TmsTytenPlanReferenceForVacResultDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPS502C03_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	// -------------------------------
	// Utility
	// -------------------------------
	/**
	 * 集約方法
	 */
	public static final List<CodeAndValue> tytenKisLevels;

	/**
	 * static initilizer
	 */
	static {
		List<CodeAndValue> tytenKisLevelList = new ArrayList<CodeAndValue>(5);
		// 集約方法にセット
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.HONTEN.getDbValue(), "本店(3桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.SHISHA.getDbValue(), "支社(5桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.SHITEN.getDbValue(), "支店(7桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.KA.getDbValue(), "課(9桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.BLK1.getDbValue(), "ブロック1(11桁)"));
		tytenKisLevelList.add(new CodeAndValue(TytenKisLevel.BLK2.getDbValue(), "ブロック2(13桁)"));
		tytenKisLevels = Collections.unmodifiableList(tytenKisLevelList);
	}

	/**
	 * [プルダウンリスト]集約方法を取得する。
	 *
	 * @return [プルダウンリスト]集約方法
	 */
	public List<CodeAndValue> getTytenKisLevels() {
		return tytenKisLevels;
	}

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
	 * 特約店コード(手入力)
	 */
	private String tmsTytenCdPart;

	/**
	 * 特約店名
	 */
	private String tmsTytenName;

	/**
	 * 集約方法
	 */
	private String tytenKisLevel = TytenKisLevel.BLK2.getDbValue();

	// -------------------------------
	// getter/setter
	// -------------------------------
	/**
	 * 特約店コード(手入力)を取得する
	 *
	 * @return 特約店コード(手入力)
	 */
	public String getTmsTytenCdPart() {
		return tmsTytenCdPart;
	}

	/**
	 * 特約店コード(手入力)を設定する
	 *
	 * @param 特約店コード(手入力)
	 */
	public void setTmsTytenCdPart(String tmsTytenCdPart) {
		this.tmsTytenCdPart = tmsTytenCdPart;
	}

	/**
	 * 特約店名を取得する
	 *
	 * @return 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 特約店名を設定する
	 *
	 * @param tmsTytenName 特約店名
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
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

	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * Formから特約店別計画参照画面検索条件を表すDTOを生成する。
	 *
	 * @return 特約店別計画参照画面検索条件を表すDTO
	 */
	public WsPlanReferenceForVacScDto convertWsPlanReferenceForVacScDto() {
		TytenKisLevel tytenKisLevel = TytenKisLevel.getInstance(this.tytenKisLevel);
		return new WsPlanReferenceForVacScDto(this.tmsTytenCdPart, tytenKisLevel,null,null); //このソースは使用しなくなるが、ワクチンの動作を見る用に、暫く保守。
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.tmsTytenCdPart = null;
		this.tmsTytenName = null;
		this.tytenKisLevel = TytenKisLevel.BLK2.getDbValue();
	}
}
