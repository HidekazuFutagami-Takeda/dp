package jp.co.takeda.web.in.dps;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.ProgressEachKindInfoDto;
import jp.co.takeda.dto.ProgressEachKindInfoForVacDto;
import jp.co.takeda.dto.ProgressParamDto;
import jp.co.takeda.dto.ProgressParamForVacDto;
import jp.co.takeda.dto.ProgressPlanStatusDto;
import jp.co.takeda.dto.ProgressPlanStatusForVacDto;
import jp.co.takeda.dto.ProgressPlanWsStatusDto;
import jp.co.takeda.dto.ProgressPlanWsStatusForVacDto;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dps000C00(トップ画面)のフォームクラス
 *
 * @author tkawabata
 */
public class Dps000C00Form extends DpActionForm {

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
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPS000C00F25_DATA_R(業務進捗表(医)[薬価改定][T/Y変換指定率])
	 */
	public static final BoxKey DPS000C00F25_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressParamDto.class);

	/**
	 * DPS000C00F26_DATA_R(業務進捗表(医)[各種登録状況])
	 */
	public static final BoxKey DPS000C00F26_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressEachKindInfoDto.class);

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　カテゴリ別に分ける必要は無いため統一
//	/**
//	 * DPS000C00F27_DATA_R(業務進捗表(医)[営業所計画-施設特約店別計画(MMP)])
//	 */
//	public static final BoxKey DPS000C00F27_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanMMPStatusDto.class);
//
//	/**
//	 * DPS000C00F28_DATA_R(業務進捗表(医)[営業所計画-施設特約店別計画(仕入)])
//	 */
//	public static final BoxKey DPS000C00F28_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanShireStatusDto.class);
//
//	/**
//	 * DPS000C00F30_DATA_R(業務進捗表(医)[営業所計画-施設特約店別計画(ONC)])
//	 */
//	public static final BoxKey DPS000C00F30_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanONCStatusDto.class);
//
//// add start 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
//	/**
//	 * DPS000C00F31_DATA_R(業務進捗表(医)[営業所計画-施設特約店別計画(SPBU)])
//	 */
//	public static final BoxKey DPS000C00F31_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanSPBUStatusDto.class);
//
//// add end 2018/6/5 M.Wada J18-0002_【DPX】2018年4月組織変更対応（計画支援）
	/**
	 * DPS000C00F20_DATA_R(業務進捗表(医)[営業所計画-施設特約店別計画(カテゴリ共通)])
	 */
	public static final BoxKey DPS000C00F20_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanStatusDto.class);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　カテゴリ別に分ける必要は無いため統一

	/**
	 * DPS000C00F29_DATA_R(業務進捗表(医)[特約店別計画(MMP品・仕入品(一般・麻薬))])
	 */
	public static final BoxKey DPS000C00F29_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanWsStatusDto.class);

	/**
	 * DPS000C00F75_DATA_R(業務進捗表(ワ)[T/B変換指定率])
	 */
	public static final BoxKey DPS000C00F75_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressParamForVacDto.class);

	/**
	 * DPS000C00F76_DATA_R(業務進捗表(ワ)[各種登録状況])
	 */
	public static final BoxKey DPS000C00F76_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressEachKindInfoForVacDto.class);

	/**
	 * DPS000C00F77_DATA_R(業務進捗表(ワ)[施設特約店別計画])
	 */
	public static final BoxKey DPS000C00F77_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanStatusForVacDto.class);

	/**
	 * DPS000C00F78_DATA_R(業務進捗表(ワ)[特約店別計画])
	 */
	public static final BoxKey DPS000C00F78_DATA_R = new BoxKeyPerClassImpl(Dps000C00Form.class, ProgressPlanWsStatusForVacDto.class);

	/**
	 * (医)一般MR計画参照表出力（FOCUS接続）。整形MR用メニュー追加対応。
	 */
	public static final DpAuthority AUTH_DPS_I_FOCUS_MR_PLAN_INTERFACE = new DpAuthority( AuthType.REFER);

	// -------------------------------
	// field
	// -------------------------------

	/**
	 * 切替従業員番号
	 */
	private String applyJgiNo;

	/**
	 * お知らせシーケンス番号
	 */
	private String announceSeqKey;

	/**
	 * お知らせ更新日時
	 */
	private String announceUpDate;

	/**
	 * お知らせ出力ファイル情報ID
	 */
	private String announceOutputFileId;

	/**
	 * 担当者別計画帳票出力時の組織コード
	 */
	private String tanOutputSosCd;

	/**
	 * 関係会社別施設特約店別一覧出力時の組織コード
	 */
	private String insWsOutputSosCd;

	/**
	 * 施設計画市区郡町村別集計結果出力時の従業員番号
	 */
	private String insWsVacJgiNo;

	/**
	 * デフォルトフラグ
	 */
	private String defFlg;

	/**
	 * 初期表示フラグ
	 */
	private String initDispFlg;

	/**
	 * 設定組織コード
	 */
	private String settingSosCd;

	/**
	 * 設定従業員番号
	 */
	private String settingJgiNo;

	/**
	 * リダイレクト先パス
	 */
	private String redirectPath;

	/**
	 * カテゴリー　※(医)営業所計画－施設特約店別計画(カテゴリ共通)テーブルの取得用
	 */
	private String category;


	/**
	 * 仕入完了通知フラグ
	 */
	private boolean shiireFixAllFinished;

	/**
	 * 仕入完了通知フラグ エラーあり
	 */
	private boolean shiireFixAllFinishedWithError;


	// -------------------------------
	// Utility
	// -------------------------------

	/**
	 * フォームを更新する。
	 */
	public void reflash() {
		DpUserInfo dpUserInfo = DpUserInfo.getDpUserInfo();
		Integer jgiNo = dpUserInfo.getSettingUserJgiNo();
		if (jgiNo != null) {
			this.applyJgiNo = jgiNo.toString();
		} else {
			this.applyJgiNo = null;
		}
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
	// Getter & Setter
	// -------------------------------

	/**
	 * 切替従業員番号を取得する。
	 *
	 * @return 切替従業員番号
	 */
	public String getApplyJgiNo() {
		return applyJgiNo;
	}

	/**
	 * 切替従業員番号を設定する。
	 *
	 * @param applyJgiNo 切替従業員番号
	 */
	public void setApplyJgiNo(String applyJgiNo) {
		this.applyJgiNo = applyJgiNo;
	}

	/**
	 * お知らせシーケンス番号を取得する。
	 *
	 * @return お知らせシーケンス番号
	 */
	public String getAnnounceSeqKey() {
		return announceSeqKey;
	}

	/**
	 * お知らせシーケンス番号を設定する。
	 *
	 * @param announceSeqKey お知らせシーケンス番号
	 */
	public void setAnnounceSeqKey(String announceSeqKey) {
		this.announceSeqKey = announceSeqKey;
	}

	/**
	 * お知らせ更新日時を取得する。
	 *
	 * @return announceUpDate お知らせ更新日時
	 */
	public String getAnnounceUpDate() {
		return announceUpDate;
	}

	/**
	 * お知らせ更新日時を設定する。
	 *
	 * @param announceUpDate お知らせ更新日時
	 */
	public void setAnnounceUpDate(String announceUpDate) {
		this.announceUpDate = announceUpDate;
	}

	/**
	 * お知らせ出力ファイル情報IDを取得する。
	 *
	 * @return announceOutputFileId お知らせ出力ファイル情報ID
	 */
	public String getAnnounceOutputFileId() {
		return announceOutputFileId;
	}

	/**
	 * お知らせ出力ファイル情報IDを設定する。
	 *
	 * @param announceOutputFileId お知らせ出力ファイル情報ID
	 */
	public void setAnnounceOutputFileId(String announceOutputFileId) {
		this.announceOutputFileId = announceOutputFileId;
	}

	/**
	 * 担当者別計画帳票出力時の組織コードを取得する。
	 *
	 * @return tanOutputSosCd 担当者別計画帳票出力時の組織コード
	 */
	public String getTanOutputSosCd() {
		return tanOutputSosCd;
	}

	/**
	 * 担当者別計画帳票出力時の組織コードを設定する。
	 *
	 * @param tanOutputSosCd 担当者別計画帳票出力時の組織コード
	 */
	public void setTanOutputSosCd(String tanOutputSosCd) {
		this.tanOutputSosCd = tanOutputSosCd;
	}

	/**
	 * 関係会社別施設特約店別一覧出力時の組織コードを取得する。
	 *
	 * @return insWsOutputSosCd 関係会社別施設特約店別一覧出力時の組織コード
	 */
	public String getInsWsOutputSosCd() {
		return insWsOutputSosCd;
	}

	/**
	 * 関係会社別施設特約店別一覧出力時の組織コードを設定する。
	 *
	 * @param insWsOutputSosCd 関係会社別施設特約店別一覧出力時の組織コード
	 */
	public void setInsWsOutputSosCd(String insWsOutputSosCd) {
		this.insWsOutputSosCd = insWsOutputSosCd;
	}

	/**
	 * 施設計画市区郡町村別集計結果出力時の従業員番号を取得する。
	 *
	 * @return 施設計画市区郡町村別集計結果出力時の従業員番号
	 */
	public String getInsWsVacJgiNo() {
		return insWsVacJgiNo;
	}

	/**
	 * 施設計画市区郡町村別集計結果出力時の従業員番号を設定する。
	 *
	 * @param insWsVacJgiNo 施設計画市区郡町村別集計結果出力時の従業員番号
	 */
	public void setInsWsVacJgiNo(String insWsVacJgiNo) {
		this.insWsVacJgiNo = insWsVacJgiNo;
	}

	/**
	 * デフォルトフラグを取得する。
	 *
	 * @return defFlg デフォルトフラグ
	 */
	public String getDefFlg() {
		return defFlg;
	}

	/**
	 * デフォルトフラグを設定する。
	 *
	 * @param defFlg デフォルトフラグ
	 */
	public void setDefFlg(String defFlg) {
		this.defFlg = defFlg;
	}

	/**
	 * 初期表示フラグを取得する。
	 *
	 * @return initDispFlg 初期表示フラグ
	 */
	public String getInitDispFlg() {
		return initDispFlg;
	}

	/**
	 * 初期表示フラグを設定する。
	 *
	 * @param initDispFlg 初期表示フラグ
	 */
	public void setInitDispFlg(String initDispFlg) {
		this.initDispFlg = initDispFlg;
	}

	/**
	 * 設定組織コードを取得する。
	 *
	 * @return settingSosCd 設定組織コード
	 */
	public String getSettingSosCd() {
		return settingSosCd;
	}

	/**
	 * 設定組織コードを設定する。
	 *
	 * @param settingSosCd 設定組織コード
	 */
	public void setSettingSosCd(String settingSosCd) {
		this.settingSosCd = settingSosCd;
	}

	/**
	 * 設定従業員番号を取得する。
	 *
	 * @return settingJgiNo 設定従業員番号
	 */
	public String getSettingJgiNo() {
		return settingJgiNo;
	}

	/**
	 * 設定従業員番号を設定する。
	 *
	 * @param settingJgiNo 設定従業員番号
	 */
	public void setSettingJgiNo(String settingJgiNo) {
		this.settingJgiNo = settingJgiNo;
	}

	/**
	 * リダイレクト先パスを取得する。
	 *
	 * @return redirectPath リダイレクト先パス
	 */
	public String getRedirectPath() {
		return redirectPath;
	}

	/**
	 * リダイレクト先パスを設定する。
	 *
	 * @param redirectPath リダイレクト先パス
	 */
	public void setRedirectPath(String redirectPath) {
		this.redirectPath = redirectPath;
	}

	/**
	 * カテゴリーを取得する。　※(医)営業所計画－施設特約店別計画(カテゴリ共通)テーブルの取得用
	 *
	 * @return category カテゴリー
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリーを設定する。　※(医)営業所計画－施設特約店別計画(カテゴリ共通)テーブルの取得用
	 *
	 * @param category カテゴリー
	 */
	public void setCategory(String category) {
		this.category = category;
	}


	/**
	 * 仕入完了通知フラグ を取得する
	 * @return shiirFixAllFinished
	 */
	public boolean isShiireFixAllFinished() {
		return shiireFixAllFinished;
	}

	/**
	 * 仕入完了通知フラグ をセットする
	 * @param shiirFixAllFinished セットする shiirFixAllFinished
	 */
	public void setShiireFixAllFinished(boolean shiirFixAllFinished) {
		shiireFixAllFinished = shiirFixAllFinished;
	}

	/**
	 * 仕入完了通知フラグ エラーあり を取得する
	 * @return shiirFixAllFinishedWithError
	 */
	public boolean isShiireFixAllFinishedWithError() {
		return shiireFixAllFinishedWithError;
	}

	/**
	 * 仕入完了通知フラグ エラーあり をセットする
	 * @param shiirFixAllFinishedWithError セットする shiirFixAllFinishedWithError
	 */
	public void setShiireFixAllFinishedWithError(boolean shiirFixAllFinishedWithError) {
		shiireFixAllFinishedWithError = shiirFixAllFinishedWithError;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		// REQUEST管理のため初期化不要
	}
}
