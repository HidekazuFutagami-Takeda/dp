package jp.co.takeda.web.in.dps;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.dto.DistParamResultDto;
import jp.co.takeda.dto.DistParamUpdateDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.DistParam;
import jp.co.takeda.model.DistParamOffice;
import jp.co.takeda.model.div.DistributionType;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.security.DpAuthority;
import jp.co.takeda.security.DpAuthority.AuthType;

import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps400C01((医)施設特約店別計画配分基準編集画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps400C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果取得キー
	 */
	public static final BoxKey Dps400C01_DATA_R_SEARCH_RESULT = new BoxKeyPerClassImpl(Dps400C01Form.class, DistParamResultDto.class);

	/**
	 * 編集権限((医)施設特約店別計画(自)配分処理)
	 */
	public static final DpAuthority DPS400C01_MMP_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	/**
	 * 編集権限((医)施設特約店別計画(仕)配分処理)
	 */
	public static final DpAuthority DPS400C01_SIRE_EDIT_AUTH = new DpAuthority( AuthType.EDIT);

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 営業所案 ゼロ配分フラグ
	 */
	private boolean officeZeroDistFlg;

	/**
	 * [UH]営業所案 配分品目コード
	 */
	private String officeRefProdCodeUH;

	/**
	 * [UH]営業所案 実績参照期間(FROM)
	 */
	private String officeRefFromUH;

	/**
	 * [UH]営業所案 実績参照期間(TO)
	 */
	private String officeRefToUH;

	/**
	 * [UH]営業所案 実績参照品目コード1
	 */
	private String officeResultRefProdCode1UH;

	/**
	 * [UH]営業所案 実績参照品目コード2
	 */
	private String officeResultRefProdCode2UH;

	/**
	 * [UH]営業所案 実績参照品目コード3
	 */
	private String officeResultRefProdCode3UH;

	/**
	 * [P]営業所案 配分品目コード
	 */
	private String officeRefProdCodeP;

	/**
	 * [P]営業所案 実績参照期間(FROM)
	 */
	private String officeRefFromP;

	/**
	 * [P]営業所案 実績参照期間(TO)
	 */
	private String officeRefToP;

	/**
	 * [P]営業所案 実績参照品目コード1
	 */
	private String officeResultRefProdCode1P;

	/**
	 * [P]営業所案 実績参照品目コード2
	 */
	private String officeResultRefProdCode2P;

	/**
	 * [P]営業所案 実績参照品目コード3
	 */
	private String officeResultRefProdCode3P;

	/**
	 * (Hidden項目)品目名
	 */
	private String prodName;

	/**
	 * (Hidden項目)[UH]営業所案 シーケンスキー
	 */
	private String officeSeqKeyUH;

	/**
	 * (Hidden項目)[UH]営業所案 最終更新日時
	 */
	private String officeUpDateUH;

	/**
	 * (Hidden項目)[UH]営業所案 配布先区分
	 */
	private String officeDistributionTypeUH;

	/**
	 * (Hidden項目)[UH]営業所案 実績参照品目コード4
	 */
	private String officeResultRefProdCode4UH;

	/**
	 * (Hidden項目)[P]営業所案 シーケンスキー
	 */
	private String officeSeqKeyP;

	/**
	 * (Hidden項目)[P]営業所案 最終更新日時
	 */
	private String officeUpDateP;

	/**
	 * (Hidden項目)[P]営業所案 配布先区分
	 */
	private String officeDistributionTypeP;

	/**
	 * (Hidden項目)[P]営業所案 実績参照品目コード4
	 */
	private String officeResultRefProdCode4P;

	/**
	 * 品目カテゴリ
	 */
	private String category;

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 組織コード(営業所)を設定する。
	 *
	 * @param 組織コード(営業所)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 営業所案 ゼロ配分フラグを取得する。
	 *
	 * @return officeZeroDistFlg 営業所案 ゼロ配分フラグ
	 */
	public boolean getOfficeZeroDistFlg() {
		return officeZeroDistFlg;
	}

	/**
	 * 営業所案 ゼロ配分フラグを設定する。
	 *
	 * @param officeZeroDistFlg 営業所案 ゼロ配分フラグ
	 */
	public void setOfficeZeroDistFlg(boolean officeZeroDistFlg) {
		this.officeZeroDistFlg = officeZeroDistFlg;
	}

	/**
	 * [UH]営業所案 配分品目コードを取得する。
	 *
	 * @return officeRefProdCodeUH [UH]営業所案 配分品目コード
	 */
	public String getOfficeRefProdCodeUH() {
		return officeRefProdCodeUH;
	}

	/**
	 * [UH]営業所案 配分品目コードを設定する。
	 *
	 * @param officeRefProdCodeUH [UH]営業所案 配分品目コード
	 */
	public void setOfficeRefProdCodeUH(String officeRefProdCodeUH) {
		this.officeRefProdCodeUH = officeRefProdCodeUH;
	}

	/**
	 * [UH]営業所案 実績参照期間(FROM)を取得する。
	 *
	 * @return officeRefFromUH [UH]営業所案 実績参照期間(FROM)
	 */
	public String getOfficeRefFromUH() {
		return officeRefFromUH;
	}

	/**
	 * [UH]営業所案 実績参照期間(FROM)を設定する。
	 *
	 * @param officeRefFromUH [UH]営業所案 実績参照期間(FROM)
	 */
	public void setOfficeRefFromUH(String officeRefFromUH) {
		this.officeRefFromUH = officeRefFromUH;
	}

	/**
	 * [UH]営業所案 実績参照期間(TO)を取得する。
	 *
	 * @return officeRefToUH [UH]営業所案 実績参照期間(TO)
	 */
	public String getOfficeRefToUH() {
		return officeRefToUH;
	}

	/**
	 * [UH]営業所案 実績参照期間(TO)を設定する。
	 *
	 * @param officeRefToUH [UH]営業所案 実績参照期間(TO)
	 */
	public void setOfficeRefToUH(String officeRefToUH) {
		this.officeRefToUH = officeRefToUH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード1を取得する。
	 *
	 * @return officeResultRefProdCode1UH [UH]営業所案 実績参照品目コード1
	 */
	public String getOfficeResultRefProdCode1UH() {
		return officeResultRefProdCode1UH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード1を設定する。
	 *
	 * @param officeResultRefProdCode1UH [UH]営業所案 実績参照品目コード1
	 */
	public void setOfficeResultRefProdCode1UH(String officeResultRefProdCode1UH) {
		this.officeResultRefProdCode1UH = officeResultRefProdCode1UH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード2を取得する。
	 *
	 * @return officeResultRefProdCode2UH [UH]営業所案 実績参照品目コード2
	 */
	public String getOfficeResultRefProdCode2UH() {
		return officeResultRefProdCode2UH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード2を設定する。
	 *
	 * @param officeResultRefProdCode2UH [UH]営業所案 実績参照品目コード2
	 */
	public void setOfficeResultRefProdCode2UH(String officeResultRefProdCode2UH) {
		this.officeResultRefProdCode2UH = officeResultRefProdCode2UH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード3を取得する。
	 *
	 * @return officeResultRefProdCode3UH [UH]営業所案 実績参照品目コード3
	 */
	public String getOfficeResultRefProdCode3UH() {
		return officeResultRefProdCode3UH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード3を設定する。
	 *
	 * @param officeResultRefProdCode3UH [UH]営業所案 実績参照品目コード3
	 */
	public void setOfficeResultRefProdCode3UH(String officeResultRefProdCode3UH) {
		this.officeResultRefProdCode3UH = officeResultRefProdCode3UH;
	}

	/**
	 * [P]営業所案 配分品目コードを取得する。
	 *
	 * @return officeRefProdCodeP [P]営業所案 配分品目コード
	 */
	public String getOfficeRefProdCodeP() {
		return officeRefProdCodeP;
	}

	/**
	 * [P]営業所案 配分品目コードを設定する。
	 *
	 * @param officeRefProdCodeP [P]営業所案 配分品目コード
	 */
	public void setOfficeRefProdCodeP(String officeRefProdCodeP) {
		this.officeRefProdCodeP = officeRefProdCodeP;
	}

	/**
	 * [P]営業所案 実績参照期間(FROM)を取得する。
	 *
	 * @return officeRefFromP [P]営業所案 実績参照期間(FROM)
	 */
	public String getOfficeRefFromP() {
		return officeRefFromP;
	}

	/**
	 * [P]営業所案 実績参照期間(FROM)を設定する。
	 *
	 * @param officeRefFromP [P]営業所案 実績参照期間(FROM)
	 */
	public void setOfficeRefFromP(String officeRefFromP) {
		this.officeRefFromP = officeRefFromP;
	}

	/**
	 * [P]営業所案 実績参照期間(TO)を取得する。
	 *
	 * @return officeRefToP [P]営業所案 実績参照期間(TO)
	 */
	public String getOfficeRefToP() {
		return officeRefToP;
	}

	/**
	 * [P]営業所案 実績参照期間(TO)を設定する。
	 *
	 * @param officeRefToP [P]営業所案 実績参照期間(TO)
	 */
	public void setOfficeRefToP(String officeRefToP) {
		this.officeRefToP = officeRefToP;
	}

	/**
	 * [P]営業所案 実績参照品目コード1を取得する。
	 *
	 * @return officeResultRefProdCode1P [P]営業所案 実績参照品目コード1
	 */
	public String getOfficeResultRefProdCode1P() {
		return officeResultRefProdCode1P;
	}

	/**
	 * [P]営業所案 実績参照品目コード1を設定する。
	 *
	 * @param officeResultRefProdCode1P [P]営業所案 実績参照品目コード1
	 */
	public void setOfficeResultRefProdCode1P(String officeResultRefProdCode1P) {
		this.officeResultRefProdCode1P = officeResultRefProdCode1P;
	}

	/**
	 * [P]営業所案 実績参照品目コード2を取得する。
	 *
	 * @return officeResultRefProdCode2P [P]営業所案 実績参照品目コード2
	 */
	public String getOfficeResultRefProdCode2P() {
		return officeResultRefProdCode2P;
	}

	/**
	 * [P]営業所案 実績参照品目コード2を設定する。
	 *
	 * @param officeResultRefProdCode2P [P]営業所案 実績参照品目コード2
	 */
	public void setOfficeResultRefProdCode2P(String officeResultRefProdCode2P) {
		this.officeResultRefProdCode2P = officeResultRefProdCode2P;
	}

	/**
	 * [P]営業所案 実績参照品目コード3を取得する。
	 *
	 * @return officeResultRefProdCode3P [P]営業所案 実績参照品目コード3
	 */
	public String getOfficeResultRefProdCode3P() {
		return officeResultRefProdCode3P;
	}

	/**
	 * [P]営業所案 実績参照品目コード3を設定する。
	 *
	 * @param officeResultRefProdCode3P [P]営業所案 実績参照品目コード3
	 */
	public void setOfficeResultRefProdCode3P(String officeResultRefProdCode3P) {
		this.officeResultRefProdCode3P = officeResultRefProdCode3P;
	}

	/**
	 * 品目名を取得する。
	 *
	 * @return prodName 品目名
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名を設定する。
	 *
	 * @param prodName 品目名
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * [UH]営業所案 シーケンスキーを取得する。
	 *
	 * @return officeSeqKeyUH [UH]営業所案 シーケンスキー
	 */
	public String getOfficeSeqKeyUH() {
		return officeSeqKeyUH;
	}

	/**
	 * [UH]営業所案 シーケンスキーを設定する。
	 *
	 * @param officeSeqKeyUH [UH]営業所案 シーケンスキー
	 */
	public void setOfficeSeqKeyUH(String officeSeqKeyUH) {
		this.officeSeqKeyUH = officeSeqKeyUH;
	}

	/**
	 * [UH]営業所案 最終更新日時を取得する。
	 *
	 * @return officeUpDateUH [UH]営業所案 最終更新日時
	 */
	public String getOfficeUpDateUH() {
		return officeUpDateUH;
	}

	/**
	 * [UH]営業所案 最終更新日時を設定する。
	 *
	 * @param officeUpDateUH [UH]営業所案 最終更新日時
	 */
	public void setOfficeUpDateUH(String officeUpDateUH) {
		this.officeUpDateUH = officeUpDateUH;
	}

	/**
	 * [UH]営業所案 配布先区分を取得する。
	 *
	 * @return officeDistributionTypeUH [UH]営業所案 配布先区分
	 */
	public String getOfficeDistributionTypeUH() {
		return officeDistributionTypeUH;
	}

	/**
	 * [UH]営業所案 配布先区分を設定する。
	 *
	 * @param officeDistributionTypeUH [UH]営業所案 配布先区分
	 */
	public void setOfficeDistributionTypeUH(String officeDistributionTypeUH) {
		this.officeDistributionTypeUH = officeDistributionTypeUH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード4を取得する。
	 *
	 * @return officeResultRefProdCode4UH [UH]営業所案 実績参照品目コード4
	 */
	public String getOfficeResultRefProdCode4UH() {
		return officeResultRefProdCode4UH;
	}

	/**
	 * [UH]営業所案 実績参照品目コード4を設定する。
	 *
	 * @param officeResultRefProdCode4UH [UH]営業所案 実績参照品目コード4
	 */
	public void setOfficeResultRefProdCode4UH(String officeResultRefProdCode4UH) {
		this.officeResultRefProdCode4UH = officeResultRefProdCode4UH;
	}

	/**
	 * [P]営業所案 シーケンスキーを取得する。
	 *
	 * @return officeSeqKeyP [P]営業所案 シーケンスキー
	 */
	public String getOfficeSeqKeyP() {
		return officeSeqKeyP;
	}

	/**
	 * [P]営業所案 シーケンスキーを設定する。
	 *
	 * @param officeSeqKeyP [P]営業所案 シーケンスキー
	 */
	public void setOfficeSeqKeyP(String officeSeqKeyP) {
		this.officeSeqKeyP = officeSeqKeyP;
	}

	/**
	 * [P]営業所案 最終更新日時を取得する。
	 *
	 * @return officeUpDateP [P]営業所案 最終更新日時
	 */
	public String getOfficeUpDateP() {
		return officeUpDateP;
	}

	/**
	 * [P]営業所案 最終更新日時を設定する。
	 *
	 * @param officeUpDateP [P]営業所案 最終更新日時
	 */
	public void setOfficeUpDateP(String officeUpDateP) {
		this.officeUpDateP = officeUpDateP;
	}

	/**
	 * [P]営業所案 配布先区分を取得する。
	 *
	 * @return officeDistributionTypeP [P]営業所案 配布先区分
	 */
	public String getOfficeDistributionTypeP() {
		return officeDistributionTypeP;
	}

	/**
	 * [P]営業所案 配布先区分を設定する。
	 *
	 * @param officeDistributionTypeP [P]営業所案 配布先区分
	 */
	public void setOfficeDistributionTypeP(String officeDistributionTypeP) {
		this.officeDistributionTypeP = officeDistributionTypeP;
	}

	/**
	 * [P]営業所案 実績参照品目コード4を取得する。
	 *
	 * @return officeResultRefProdCode4P [P]営業所案 実績参照品目コード4
	 */
	public String getOfficeResultRefProdCode4P() {
		return officeResultRefProdCode4P;
	}

	/**
	 * [P]営業所案 実績参照品目コード4を設定する。
	 *
	 * @param officeResultRefProdCode4P [P]営業所案 実績参照品目コード4
	 */
	public void setOfficeResultRefProdCode4P(String officeResultRefProdCode4P) {
		this.officeResultRefProdCode4P = officeResultRefProdCode4P;
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


	@Override
	public void reset() {
		this.officeZeroDistFlg = false;
	}

	// -------------------------------
	// convert
	// -------------------------------
	/**
	 * ActionForm変換処理 <br>
	 * 画面から入力されたデータを、配分基準登録用DTOに変換する。
	 *
	 * @return 配分基準登録用DTOのリスト
	 */
	public DistParamUpdateDto convertDistParamUpdateDto() throws Exception {
		// UHの配分基準(営業所)を生成
		DistParamOffice distParamOfficeUH = new DistParamOffice();
		if (!StringUtils.isEmpty(officeSeqKeyUH)) {
			distParamOfficeUH.setSeqKey(ConvertUtil.parseLong(officeSeqKeyUH));
		}
		if (!StringUtils.isEmpty(officeUpDateUH)) {
			distParamOfficeUH.setUpDate(new Date((ConvertUtil.parseLong(officeUpDateUH))));
		}
		distParamOfficeUH.setSosCd(sosCd3);
		distParamOfficeUH.setProdCode(prodCode);
		distParamOfficeUH.setInsType(InsType.UH);
		BaseParam baseParamUH = new BaseParam();
		baseParamUH.setRefProdCode(officeRefProdCodeUH);
		baseParamUH.setRefFrom(RefPeriod.getInstance(officeRefFromUH));
		baseParamUH.setRefTo(RefPeriod.getInstance(officeRefToUH));
		baseParamUH.setResultRefProdCode1(officeResultRefProdCode1UH);
		baseParamUH.setResultRefProdCode2(officeResultRefProdCode2UH);
		baseParamUH.setResultRefProdCode3(officeResultRefProdCode3UH);
		baseParamUH.setResultRefProdCode4(officeResultRefProdCode4UH);
		distParamOfficeUH.setBaseParam(baseParamUH);
		DistParam distParamUH = new DistParam();
		distParamUH.setDistributionType(DistributionType.getInstance(officeDistributionTypeUH));
		distParamUH.setZeroDistFlg(officeZeroDistFlg);
		distParamOfficeUH.setDistParam(distParamUH);

		// Pの配分基準(営業所)を生成
		DistParamOffice distParamOfficeP = new DistParamOffice();
		if (!StringUtils.isEmpty(officeSeqKeyP)) {
			distParamOfficeP.setSeqKey(ConvertUtil.parseLong(officeSeqKeyP));
		}
		if (!StringUtils.isEmpty(officeUpDateP)) {
			distParamOfficeP.setUpDate(new Date((ConvertUtil.parseLong(officeUpDateP))));
		}
		distParamOfficeP.setSosCd(sosCd3);
		distParamOfficeP.setProdCode(prodCode);
		distParamOfficeP.setInsType(InsType.P);
		BaseParam baseParamP = new BaseParam();
		baseParamP.setRefProdCode(officeRefProdCodeP);
		baseParamP.setRefFrom(RefPeriod.getInstance(officeRefFromP));
		baseParamP.setRefTo(RefPeriod.getInstance(officeRefToP));
		baseParamP.setResultRefProdCode1(officeResultRefProdCode1P);
		baseParamP.setResultRefProdCode2(officeResultRefProdCode2P);
		baseParamP.setResultRefProdCode3(officeResultRefProdCode3P);
		baseParamP.setResultRefProdCode4(officeResultRefProdCode4P);
		distParamOfficeP.setBaseParam(baseParamP);
		DistParam distParamP = new DistParam();
		distParamP.setDistributionType(DistributionType.getInstance(officeDistributionTypeP));
		distParamP.setZeroDistFlg(officeZeroDistFlg);
		distParamOfficeP.setDistParam(distParamP);

		return new DistParamUpdateDto(distParamOfficeUH, distParamOfficeP, prodName);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.officeZeroDistFlg = false;
		this.officeRefProdCodeUH = null;
		this.officeRefProdCodeP = null;
		this.officeRefFromP = null;
		this.officeRefToP = null;
		this.officeResultRefProdCode1P = null;
		this.officeResultRefProdCode2P = null;
		this.officeResultRefProdCode3P = null;
		this.prodName = null;
		this.officeSeqKeyUH = null;
		this.officeUpDateUH = null;
		this.officeDistributionTypeUH = null;
		this.officeResultRefProdCode4UH = null;
		this.officeSeqKeyP = null;
		this.officeUpDateP = null;
		this.officeDistributionTypeP = null;
		this.officeResultRefProdCode4P = null;
	}
}
