package jp.co.takeda.web.in.dpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.ManageInsWsPlanEntryDto;
import jp.co.takeda.dto.ManageInsWsPlanProdDto;
import jp.co.takeda.dto.ManageInsWsPlanProdHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdScDto;
import jp.co.takeda.logic.div.PlanData;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;
import jp.co.takeda.web.cmn.bean.ContentsType;
import jp.co.takeda.web.cmn.bean.Downloadable;

/**
 * Dpm401C00((医)施設特約店品目別計画編集画面)のフォームクラス
 *
 * @author siwamoto
 */
public class Dpm401C00Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DPM401C00_DATA_R
	 */
	public static final BoxKey DPM401C00_DATA_R = new BoxKeyPerClassImpl(Dpm401C00Form.class, ManageInsWsPlanProdDto.class);

	/**
	 * DPM401C00_DATA_R_HEADER
	 */
	public static final BoxKey DPM401C00_INPUT_DATA_R = new BoxKeyPerClassImpl(Dpm401C00Form.class, ManageInsWsPlanProdHeaderDto.class);

	/**
	 * 編集権限
	 */
	public static final DpAuthority DPM401C00_EDIT_AUTH = new DpAuthority(AuthType.EDIT);

	// -------------------------------
	// 部品
	// -------------------------------

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(支店)
	 */
	private String sosCd2;

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 従業員番号
	 */
	private String jgiNo;

	/**
	 * 雑組織フラグ
	 */
	private boolean etcSosFlg;

	/**
	 * 施設コード
	 */
	private String insNo;

	/**
	 * 施設名
	 */
	private String insName;

	/**
	 * 特約店コード
	 */
	private String tmsTytenCd;

	/**
	 * 特約店名
	 */
	private String tmsTytenName;

	/**
	 * 特約店コード(入力欄用)
	 */
	private String tmsTytenCdPart;

	/**
	 * 品目カテゴリ
	 */
	private String prodCategory;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

	/**
	 * 計画データ
	 */
	private String planData;

	// 処理用フィールド
	/**
	 * 処理用組織コード(支店)
	 */
	private String sosCd2Tran;

	/**
	 * 処理用組織コード(営業所)
	 */
	private String sosCd3Tran;

	/**
	 * 処理用組織コード(チーム)
	 */
	private String sosCd4Tran;

	/**
	 * 処理用従業員番号
	 */
	private String jgiNoTran;

	/**
	 * 処理用雑組織フラグ
	 */
	private boolean etcSosFlgTran;

	/**
	 * 処理用施設コード
	 */
	private String insNoTran;

	/**
	 * 処理用特約店コード
	 */
	private String tmsTytenCdTran;

	/**
	 * 処理用特約店コード(入力欄用)
	 */
	private String tmsTytenCdPartTran;

	/**
	 * 処理用品目カテゴリ
	 */
	private String prodCategoryTran;

	/**
	 * 処理用計画データ
	 */
	private String planDataTran;

	/**
	 * 処理用ワクチンのカテゴリコード
	 */
	private String vaccineCodeTran;

	/**
	 * 流通政策部であるか
	 */
	private boolean ryutsu;

	/**
	 * 全MRフラグ
	 */
	private boolean mrFlg;

	/**
	 * 追加・更新行の情報リスト
	 *
	 * <ul>
	 * <li>Sprit文字列[0]=品目固定コード</li>
	 * <li>Sprit文字列[1]=施設コード</li>
	 * <li>Sprit文字列[2]=特約店コード</li>
	 * <li>Sprit文字列[3]=対象施設</li>
	 * <li>Sprit文字列[4]=シーケンスキー</li>
	 * <li>Sprit文字列[5]=最終更新日時</li>
	 * <li>Sprit文字列[6]=T価ベース 更新前</li>
	 * <li>Sprit文字列[7]=T価ベース 更新後（入力値）</li>
	 * </ul>
	 */
	private String[] rowIdList;

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

	//---------------------
	// Getter & Setter
	// --------------------

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
	 * @param sosCd2 組織コード(支店)
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
	 * @param sosCd3 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 組織コード(チーム)を取得する。
	 *
	 * @return sosCd4 組織コード(チーム)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 組織コード(チーム)を設定する。
	 *
	 * @param sosCd4 組織コード(チーム)
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 *
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 全MRフラグを取得する。
	 *
	 * @return mrFlg 全MRフラグ
	 */
	public boolean getMrFlg() {
		return mrFlg;
	}

	/**
	 * 全MRフラグを設定する。
	 *
	 * @param mrFlg 全MRフラグ
	 */
	public void setMrFlg(boolean mrFlg) {
		this.mrFlg = mrFlg;
	}

	/**
	 * 雑組織フラグを取得する。
	 *
	 * @return etcSosFlg 雑組織フラグ
	 */
	public boolean isEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * 雑組織フラグを設定する。
	 *
	 * @param etcSosFlg 雑組織フラグ
	 */
	public void setEtcSosFlg(boolean etcSosFlg) {
		this.etcSosFlg = etcSosFlg;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設コードを設定する。
	 *
	 * @param insNo 施設コード
	 */
	public void setInsNo(String insNo) {
		this.insNo = insNo;
	}

	/**
	 * 施設名を取得する。
	 *
	 * @return insName 施設名
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 施設名を設定する。
	 *
	 * @param insName 施設名
	 */
	public void setInsName(String insName) {
		this.insName = insName;
	}

	/**
	 * 特約店コードを取得する。
	 *
	 * @return tmsTytenCd 特約店コード
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
	 * 特約店名を取得する。
	 *
	 * @return tmsTytenName 特約店名
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 特約店名を設定する。
	 *
	 * @param tmsTytenName　特約店名
	 */
	public void setTmsTytenName(String tmsTytenName) {
		this.tmsTytenName = tmsTytenName;
	}

	/**
	 * 特約店コード(入力欄用)を取得する。
	 *
	 * @return tmsTytenCdPart 特約店コード(入力欄用)
	 */
	public String getTmsTytenCdPart() {
		return tmsTytenCdPart;
	}

	/**
	 * 特約店コード(入力欄用)を設定する。
	 *
	 * @param tmsTytenCdPart 特約店コード(入力欄用)
	 */
	public void setTmsTytenCdPart(String tmsTytenCdPart) {
		this.tmsTytenCdPart = tmsTytenCdPart;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return prodCategory カテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param prodCategory カテゴリ
	 */
	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
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

	/**
	 * 計画を取得する。
	 *
	 * @return planData 計画
	 */
	public String getPlanData() {
		return planData;
	}

	/**
	 * 計画を設定する。
	 *
	 * @param planData 計画
	 */
	public void setPlanData(String planData) {
		this.planData = planData;
	}

	/**
	 * 送信データを取得する。
	 *
	 * @return rowIdList 送信データ
	 */
	public String[] getRowIdList() {
		return rowIdList;
	}

	/**
	 * 送信データを設定する。
	 *
	 * @param rowIdList 送信データ
	 */
	public void setRowIdList(String[] rowIdList) {
		this.rowIdList = rowIdList;
	}

	/**
	 * 処理用組織コード(支店)を取得する。
	 *
	 * @return 処理用組織コード(支店)
	 */
	public String getSosCd2Tran() {
		return sosCd2Tran;
	}

	/**
	 * 処理用組織コード(支店)を設定する。
	 *
	 * @param sosCd2Tran 処理用組織コード(支店)
	 */
	public void setSosCd2Tran(String sosCd2Tran) {
		this.sosCd2Tran = sosCd2Tran;
	}

	/**
	 * 処理用組織コード(営業所)を取得する。
	 *
	 * @return 処理用組織コード(営業所)
	 */
	public String getSosCd3Tran() {
		return sosCd3Tran;
	}

	/**
	 * 処理用組織コード(営業所)を設定する。
	 *
	 * @param sosCd3Tran 処理用組織コード(営業所)
	 */
	public void setSosCd3Tran(String sosCd3Tran) {
		this.sosCd3Tran = sosCd3Tran;
	}

	/**
	 * 処理用組織コード(チーム)を取得する。
	 *
	 * @return 処理用組織コード(チーム)
	 */
	public String getSosCd4Tran() {
		return sosCd4Tran;
	}

	/**
	 * 処理用組織コード(チーム)を設定する。
	 *
	 * @param sosCd4Tran 処理用組織コード(チーム)
	 */
	public void setSosCd4Tran(String sosCd4Tran) {
		this.sosCd4Tran = sosCd4Tran;
	}

	/**
	 * 処理用従業員番号を取得する。
	 *
	 * @return 処理用従業員番号
	 */
	public String getJgiNoTran() {
		return jgiNoTran;
	}

	/**
	 * 処理用従業員番号を設定する。
	 *
	 * @param jgiNoTran 処理用従業員番号
	 */
	public void setJgiNoTran(String jgiNoTran) {
		this.jgiNoTran = jgiNoTran;
	}

	/**
	 * 処理用雑組織フラグを取得する。
	 *
	 * @return 処理用雑組織フラグ
	 */
	public boolean isEtcSosFlgTran() {
		return etcSosFlgTran;
	}

	/**
	 * 処理用雑組織フラグを設定する。
	 *
	 * @param etcSosFlgTran 処理用雑組織フラグ
	 */
	public void setEtcSosFlgTran(boolean etcSosFlgTran) {
		this.etcSosFlgTran = etcSosFlgTran;
	}

	/**
	 * 処理用施設コードを取得する。
	 *
	 * @return 処理用施設コード
	 */
	public String getInsNoTran() {
		return insNoTran;
	}

	/**
	 * 処理用施設コードを設定する。
	 *
	 * @param insNoTran 処理用施設コード
	 */
	public void setInsNoTran(String insNoTran) {
		this.insNoTran = insNoTran;
	}

	/**
	 * 処理用特約店コードを取得する。
	 *
	 * @return 処理用特約店コード
	 */
	public String getTmsTytenCdTran() {
		return tmsTytenCdTran;
	}

	/**
	 * 処理用特約店コードを設定する。
	 *
	 * @param tmsTytenCdTran 処理用特約店コード
	 */
	public void setTmsTytenCdTran(String tmsTytenCdTran) {
		this.tmsTytenCdTran = tmsTytenCdTran;
	}

	/**
	 * 処理用特約店コード(入力欄用)を取得する。
	 *
	 * @return 処理用特約店コード(入力欄用)
	 */
	public String getTmsTytenCdPartTran() {
		return tmsTytenCdPartTran;
	}

	/**
	 * 処理用特約店コード(入力欄用)を設定する。
	 *
	 * @param tmsTytenCdPartTran 処理用特約店コード(入力欄用)
	 */
	public void setTmsTytenCdPartTran(String tmsTytenCdPartTran) {
		this.tmsTytenCdPartTran = tmsTytenCdPartTran;
	}

	/**
	 * 処理用品目カテゴリを取得する。
	 *
	 * @return 処理用品目カテゴリ
	 */
	public String getProdCategoryTran() {
		return prodCategoryTran;
	}

	/**
	 * 処理用品目カテゴリを設定する。
	 *
	 * @param prodCategoryTran 処理用品目カテゴリ
	 */
	public void setProdCategoryTran(String prodCategoryTran) {
		this.prodCategoryTran = prodCategoryTran;
	}

	/**
	 * 処理用計画データを取得する。
	 *
	 * @return 処理用計画データ
	 */
	public String getPlanDataTran() {
		return planDataTran;
	}

	/**
	 * 処理用計画データを設定する。
	 *
	 * @param planDataTran 処理用計画データ
	 */
	public void setPlanDataTran(String planDataTran) {
		this.planDataTran = planDataTran;
	}

	/**
	 * 処理用ワクチンのカテゴリコードを取得する。
	 *
	 * @return vaccineCodeTran 処理用ワクチンのカテゴリコード
	 */
	public String getVaccineCodeTran() {
		return vaccineCodeTran;
	}

	/**
	 * 処理用ワクチンのカテゴリコードをを設定する。
	 *
	 * @param vaccineCodeTran 処理用ワクチンのカテゴリコード
	 */
	public void setVaccineCodeTran(String vaccineCodeTran) {
		this.vaccineCodeTran = vaccineCodeTran;
	}

	/**
	 * 流通政策部であるかを取得する
	 *
	 * @return ryutsu 流通政策部であるか
	 */
	public boolean isRyutsu() {
		return ryutsu;
	}

	/**
	 * 流通政策部であるかを設定する。
	 *
	 * @param ryutsu 流通政策部であるか
	 */
	public void setRyutsu(boolean ryutsu) {
		this.ryutsu = ryutsu;
	}

	/**
	 * 画面表示用フィールドを処理用フィールドに設定
	 */
	public void setTranField() {
		this.sosCd2Tran = this.sosCd2;
		this.sosCd3Tran = this.sosCd3;
		this.sosCd4Tran = this.sosCd4;
		this.jgiNoTran = this.jgiNo;
		this.etcSosFlgTran = this.etcSosFlg;
		this.insNoTran = this.insNo;
		this.tmsTytenCdTran = this.tmsTytenCd;
		this.tmsTytenCdPartTran = this.tmsTytenCdPart;
		this.prodCategoryTran = this.prodCategory;
		this.planDataTran = this.planData;
	}

	/**
	 * 処理用フィールドを画面表示用フィールドに設定
	 */
	public void setViewField() {
		this.sosCd2 = this.sosCd2Tran;
		this.sosCd3 = this.sosCd3Tran;
		this.sosCd4 = this.sosCd4Tran;
		this.jgiNo = this.jgiNoTran;
		this.etcSosFlg = this.etcSosFlgTran;
		this.insNo = this.insNoTran;
		this.tmsTytenCd = this.tmsTytenCdTran;
		this.tmsTytenCdPart = this.tmsTytenCdPartTran;
		this.prodCategory = this.prodCategoryTran;
		this.planData = this.planDataTran;
	}

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return InsPlanScDto 変換されたScDto
	 */
	public ManageInsWsPlanProdScDto convertScDto() throws Exception {
		// 組織コード(営業所)
		if (StringUtils.isEmpty(this.sosCd3Tran)) {
			this.sosCd3Tran = null;
		}
		// 組織コード(チーム)
		if (StringUtils.isEmpty(this.sosCd4Tran)) {
			this.sosCd4Tran = null;
		}
		// 従業員番号
		Integer jgiNo = null;
		if (StringUtils.isNotEmpty(this.jgiNoTran)) {
			jgiNo = ConvertUtil.parseInteger(this.jgiNoTran);
		} else {
			this.jgiNoTran = null;
		}
		// 施設コード
		if (StringUtils.isEmpty(this.insNoTran)) {
			this.insNoTran = null;
		}
		// 品目カテゴリ
		if (StringUtils.isEmpty(prodCategoryTran)) {
			this.prodCategoryTran = null;
		}
		// 特約店コード
		if (StringUtils.isEmpty(tmsTytenCdPartTran)) {
			this.tmsTytenCdPartTran = null;
		}
		// 計画検索範囲
		PlanData planData = null;
		if (StringUtils.isNotEmpty(this.planDataTran)) {
			planData = PlanData.getInstance(this.planDataTran);
		} else {
			this.planDataTran = null;
		}
		// ワクチンのカテゴリコード
		if (StringUtils.isEmpty(vaccineCodeTran)) {
			this.vaccineCodeTran = null;
		}
		return new ManageInsWsPlanProdScDto(sosCd3Tran, sosCd4Tran, jgiNo, insNoTran, prodCategoryTran, tmsTytenCdPartTran, planData,vaccineCodeTran);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return ManageInsWsPlanProdEntryDto 変換されたUpdateDto
	 */
	public List<ManageInsWsPlanEntryDto> convertEntryDto() throws Exception {
		List<ManageInsWsPlanEntryDto> updateDtoList = new ArrayList<ManageInsWsPlanEntryDto>();
		if (updateDtoList != null) {
			for (int i = 0; i < rowIdList.length; i++) {
				String[] rowId = ConvertUtil.splitConmma(rowIdList[i]);

				final boolean isSame = StringUtils.equals(rowId[5], rowId[6]);

				// 品目固定コード
				final String prodCode = rowId[0];

				// 施設コード
				final String insNo = rowId[1];

				// 特約店コード
				final String tmsTytenCd = rowId[2];

				// 施設区分UHのDTO作成
				if (!isSame) {
					// シーケンスキー
					final Long seqKey = ConvertUtil.parseLong(rowId[3]);

					// 最終更新日時
					final Date upDate = ConvertUtil.parseDate(rowId[4]);

					// 更新前計画値
					final Long yBaseValueBefore = ConvertUtil.parseLong(rowId[5]);

					// 更新後計画値
					final Long yBaseValueAfter = ConvertUtil.parseLong(rowId[6]);

					final ManageInsWsPlanEntryDto updateDto = new ManageInsWsPlanEntryDto(prodCode, insNo, tmsTytenCd, seqKey, upDate, yBaseValueBefore, yBaseValueAfter);
					updateDtoList.add(updateDto);
				}
			}
		}
		return updateDtoList;
	}

	/**
	 * ログインユーザの組織情報をセット
	 *
	 *
	 */
	public void setUserInfo() {
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {
				setSosCd2(null);
				setSosCd3(null);
				setSosCd4(null);
				setJgiNo(null);
				if (user.isMatch(JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
					setSosCd2(user.getSosCd2());
				} else if (user.isMatch(JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF)) {
					setSosCd2(user.getSosCd2());
					setSosCd3(user.getSosCd3());
				}
			}
		}
	}

	/**
	 * headerDto ⇒ ActionForm 変換処理
	 *
	 * @param headerDto ヘッダ情報DTO
	 */
	public void convertHeaderDto(ManageInsWsPlanProdHeaderDto headerDto) {

		if (StringUtils.isNotEmpty(this.insNo)) {
			if (headerDto.getInsMstResultDto() == null) {
				insName = null;
				setUserInfo();
				etcSosFlg = false;
			} else {
				insNo = headerDto.getInsMstResultDto().getInsNo();
				insName = headerDto.getInsMstResultDto().getInsName();
				jgiNo = null;
				etcSosFlg = false;
				if (headerDto.getInsMstResultDto().getJgiNo() != null) {
					jgiNo = headerDto.getInsMstResultDto().getJgiNo().toString();
				}
			}
		}

		this.tmsTytenName = headerDto.getTmsTytenName();
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		setSosCd2(null);
		setSosCd3(null);
		setSosCd4(null);
		setJgiNo(null);
		setEtcSosFlg(false);
		setInsNo(null);
		setInsName(null);
		setTmsTytenCd(null);
		setTmsTytenCdPart(null);
		setTmsTytenName(null);
		setProdCategory(null);
		setPlanData("0");
	}
}
