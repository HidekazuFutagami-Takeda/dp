package jp.co.takeda.web.in.dps;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.EstimationParamResultDto;
import jp.co.takeda.dto.EstimationParamUpdateDto;
import jp.co.takeda.model.BaseParam;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.div.EstimationBase;
import jp.co.takeda.model.div.RefPeriod;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DpActionForm;

/**
 * Dps301C01((医)試算パラメータ編集画面)のフォームクラス
 *
 * @author nozaki
 */
public class Dps301C01Form extends DpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * Dps301C01_DATA_R
	 */
	public static final BoxKey DPS301C01_DATA_R = new BoxKeyPerClassImpl(Dps301C01Form.class, EstimationParamResultDto.class);

	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 営業所案 試算元基準値
	 */
	private String officeEstimationBase;

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * 営業所案 留保率
	 */
	private String officeIndexRyhRtsu;
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * 営業所案 指数（未獲得市場）
	 */
	private String officeIndexMikakutoku;

	/**
	 * 営業所案 指数（納入実績）
	 */
	private String officeIndexDelivery;

	/**
	 * 営業所案 フリー項目1
	 */
	private String officeIndexFree1;

	/**
	 * 営業所案 フリー項目2
	 */
	private String officeIndexFree2;

	/**
	 * 営業所案 フリー項目3
	 */
	private String officeIndexFree3;

	/**
	 * 営業所案 フリー項目名称1
	 */
	private String officeIndexFreeName1;

	/**
	 * 営業所案 フリー項目名称2
	 */
	private String officeIndexFreeName2;

	/**
	 * 営業所案 フリー項目名称3
	 */
	private String officeIndexFreeName3;

	/**
	 * 営業所案 試算品目コード
	 */
	private String officeRefProdCode;

	/**
	 * 営業所案 参照期間(FROM)
	 */
	private String officeRefFrom;

	/**
	 * 営業所案 参照期間(TO)
	 */
	private String officeRefTo;

	/**
	 * 営業所案 実績参照品目コード1
	 */
	private String officeResultRefProdCode1;

	/**
	 * 営業所案 実績参照品目コード2
	 */
	private String officeResultRefProdCode2;

	/**
	 * 営業所案 実績参照品目コード3
	 */
	private String officeResultRefProdCode3;

	/**
	 * 営業所案 実績参照品目コード4
	 */
	private String officeResultRefProdCode4;

	/**
	 * 営業所案 シーケンスキー
	 */
	private String officeSeqKey;

	/**
	 * 営業所案 最終更新日
	 */
	private String officeLastUpdateTime;

	/**
	 * 品目カテゴリリスト
	 */
	private List<CodeAndValue> prodCategoryList;

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
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 *
	 * @param 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 営業所案 試算品目コードを取得する。
	 *
	 * @return 営業所案 試算品目コード
	 */
	public String getOfficeRefProdCode() {
		return officeRefProdCode;
	}

	/**
	 * 営業所案 試算品目コードを設定する。
	 *
	 * @param officeRefProdCode 営業所案 試算品目コード
	 */
	public void setOfficeRefProdCode(String officeRefProdCode) {
		this.officeRefProdCode = officeRefProdCode;
	}

	/**
	 * 営業所案 参照期間(FROM)を取得する。
	 *
	 * @return 営業所案 参照期間(FROM)
	 */
	public String getOfficeRefFrom() {
		return officeRefFrom;
	}

	/**
	 * 営業所案 参照期間(FROM)を設定する。
	 *
	 * @param officeRefFrom 営業所案 参照期間(FROM)
	 */
	public void setOfficeRefFrom(String officeRefFrom) {
		this.officeRefFrom = officeRefFrom;
	}

	/**
	 * 営業所案 参照期間(TO)を取得する。
	 *
	 * @return 営業所案 参照期間(TO)
	 */
	public String getOfficeRefTo() {
		return officeRefTo;
	}

	/**
	 * 営業所案 参照期間(TO)を設定する。
	 *
	 * @param officeRefTo 営業所案 参照期間(TO)
	 */
	public void setOfficeRefTo(String officeRefTo) {
		this.officeRefTo = officeRefTo;
	}

	/**
	 * 営業所案 実績参照品目コード1を取得する。
	 *
	 * @return 営業所案 実績参照品目コード1
	 */
	public String getOfficeResultRefProdCode1() {
		return officeResultRefProdCode1;
	}

	/**
	 * 営業所案 実績参照品目コード1を設定する。
	 *
	 * @param officeResultRefProdCode1 営業所案 実績参照品目コード1
	 */
	public void setOfficeResultRefProdCode1(String officeResultRefProdCode1) {
		this.officeResultRefProdCode1 = officeResultRefProdCode1;
	}

	/**
	 * 営業所案 実績参照品目コード2を取得する。
	 *
	 * @return 営業所案 実績参照品目コード2
	 */
	public String getOfficeResultRefProdCode2() {
		return officeResultRefProdCode2;
	}

	/**
	 * 営業所案 実績参照品目コード2を設定する。
	 *
	 * @param officeResultRefProdCode2 営業所案 実績参照品目コード2
	 */
	public void setOfficeResultRefProdCode2(String officeResultRefProdCode2) {
		this.officeResultRefProdCode2 = officeResultRefProdCode2;
	}

	/**
	 * 営業所案 実績参照品目コード3を取得する。
	 *
	 * @return 営業所案 実績参照品目コード3
	 */
	public String getOfficeResultRefProdCode3() {
		return officeResultRefProdCode3;
	}

	/**
	 * 営業所案 実績参照品目コード3を設定する。
	 *
	 * @param officeResultRefProdCode3 営業所案 実績参照品目コード3
	 */
	public void setOfficeResultRefProdCode3(String officeResultRefProdCode3) {
		this.officeResultRefProdCode3 = officeResultRefProdCode3;
	}

	/**
	 * 営業所案 実績参照品目コード4を取得する。
	 *
	 * @return 営業所案 実績参照品目コード4
	 */
	public String getOfficeResultRefProdCode4() {
		return officeResultRefProdCode4;
	}

	/**
	 * 営業所案 実績参照品目コード4を設定する。
	 *
	 * @param officeResultRefProdCode4 営業所案 実績参照品目コード4
	 */
	public void setOfficeResultRefProdCode4(String officeResultRefProdCode4) {
		this.officeResultRefProdCode4 = officeResultRefProdCode4;
	}

	/**
	 * 営業所案 試算元基準値を取得する
	 *
	 * @return 営業所案 試算元基準値
	 */
	public String getOfficeEstimationBase() {
		return officeEstimationBase;
	}

	/**
	 * 営業所案 試算元基準値を設定する。
	 *
	 * @param officeEstimationBase 営業所案 試算元基準値
	 */
	public void setOfficeEstimationBase(String officeEstimationBase) {
		this.officeEstimationBase = officeEstimationBase;
	}

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * 営業所案 留保率を取得する
	 *
	 * @return 営業所案 留保率
	 */
	public String getOfficeIndexRyhRtsu() {
		return officeIndexRyhRtsu;
	}

	/**
	 * 営業所案 留保率を設定する。
	 *
	 * @param officeIndexMikakutoku 営業所案 留保率
	 */
	public void setOfficeIndexRyhRtsu(String officeIndexRyhRtsu) {
		this.officeIndexRyhRtsu = officeIndexRyhRtsu;
	}
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * 営業所案 指数（未獲得市場）を取得する
	 *
	 * @return 営業所案 指数（未獲得市場）
	 */
	public String getOfficeIndexMikakutoku() {
		return officeIndexMikakutoku;
	}

	/**
	 * 営業所案 指数（未獲得市場）を設定する。
	 *
	 * @param officeIndexMikakutoku 営業所案 指数（未獲得市場）
	 */
	public void setOfficeIndexMikakutoku(String officeIndexMikakutoku) {
		this.officeIndexMikakutoku = officeIndexMikakutoku;
	}

	/**
	 * 営業所案 指数（納入実績）を取得する
	 *
	 * @return 営業所案 指数（納入実績）
	 */
	public String getOfficeIndexDelivery() {
		return officeIndexDelivery;
	}

	/**
	 * 営業所案 指数（納入実績）を設定する。
	 *
	 * @param officeIndexDelivery 営業所案 指数（納入実績）
	 */
	public void setOfficeIndexDelivery(String officeIndexDelivery) {
		this.officeIndexDelivery = officeIndexDelivery;
	}

	/**
	 * 営業所案 フリー項目1を取得する
	 *
	 * @return 営業所案 フリー項目1
	 */
	public String getOfficeIndexFree1() {
		return officeIndexFree1;
	}

	/**
	 * 営業所案 フリー項目1を設定する。
	 *
	 * @param officeIndexFree1 営業所案 フリー項目1
	 */
	public void setOfficeIndexFree1(String officeIndexFree1) {
		this.officeIndexFree1 = officeIndexFree1;
	}

	/**
	 * 営業所案 フリー項目2を取得する
	 *
	 * @return 営業所案 フリー項目2
	 */
	public String getOfficeIndexFree2() {
		return officeIndexFree2;
	}

	/**
	 * 営業所案 フリー項目2を設定する。
	 *
	 * @param officeIndexFree2 営業所案 フリー項目2
	 */
	public void setOfficeIndexFree2(String officeIndexFree2) {
		this.officeIndexFree2 = officeIndexFree2;
	}

	/**
	 * 営業所案 フリー項目3を取得する
	 *
	 * @return 営業所案 フリー項目3
	 */
	public String getOfficeIndexFree3() {
		return officeIndexFree3;
	}

	/**
	 * 営業所案 フリー項目3を設定する。
	 *
	 * @param officeIndexFree3 営業所案 フリー項目3
	 */
	public void setOfficeIndexFree3(String officeIndexFree3) {
		this.officeIndexFree3 = officeIndexFree3;
	}

	/**
	 * 営業所案 フリー項目名称1を取得する
	 *
	 * @return 営業所案 フリー項目名称1
	 */
	public String getOfficeIndexFreeName1() {
		return officeIndexFreeName1;
	}

	/**
	 * 営業所案 フリー項目名称1を設定する。
	 *
	 * @param officeIndexFreeName1 営業所案 フリー項目名称1
	 */
	public void setOfficeIndexFreeName1(String officeIndexFreeName1) {
		this.officeIndexFreeName1 = officeIndexFreeName1;
	}

	/**
	 * フリー項目名称2を取得する
	 *
	 * @return フリー項目名称2
	 */
	public String getOfficeIndexFreeName2() {
		return officeIndexFreeName2;
	}

	/**
	 * フリー項目名称2を設定する。
	 *
	 * @param officeIndexFreeName2 フリー項目名称2
	 */
	public void setOfficeIndexFreeName2(String officeIndexFreeName2) {
		this.officeIndexFreeName2 = officeIndexFreeName2;
	}

	/**
	 * フリー項目名称3を取得する
	 *
	 * @return フリー項目名称3
	 */
	public String getOfficeIndexFreeName3() {
		return officeIndexFreeName3;
	}

	/**
	 * フリー項目名称3を設定する。
	 *
	 * @param officeIndexFreeName3 フリー項目名称3
	 */
	public void setOfficeIndexFreeName3(String officeIndexFreeName3) {
		this.officeIndexFreeName3 = officeIndexFreeName3;
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return シーケンスキー
	 */
	public String getOfficeSeqKey() {
		return officeSeqKey;
	}

	/**
	 * シーケンスキーを設定する。
	 *
	 * @param officeSeqKey シーケンスキー
	 */
	public void setOfficeSeqKey(String officeSeqKey) {
		this.officeSeqKey = officeSeqKey;
	}

	/**
	 * 最終更新日(ミリ秒)を取得する。
	 *
	 * @return 最終更新日(ミリ秒)
	 */
	public String getOfficeLastUpdateTime() {
		return officeLastUpdateTime;
	}

	/**
	 * 最終更新日(ミリ秒)を設定する。
	 *
	 * @param officeLastUpdateTime 最終更新日(ミリ秒)
	 */
	public void setOfficeLastUpdateTime(String officeLastUpdateTime) {
		this.officeLastUpdateTime = officeLastUpdateTime;
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
	 * ActionForm変換処理 <br>
	 * 画面から入力されたデータを、試算実行用DTOに変換する。
	 *
	 * @return 試算実行用DTOのリスト
	 */
	public EstimationParamUpdateDto getEstimationParamUpdateData() throws Exception {

		EstParamOffice estParamOffice = new EstParamOffice();

		estParamOffice.setSosCd(sosCd3);
		estParamOffice.setProdCode(prodCode);
		if (!StringUtils.isEmpty(officeSeqKey)) {
			estParamOffice.setSeqKey(ConvertUtil.parseLong(officeSeqKey));
		}
		if (!StringUtils.isEmpty(officeLastUpdateTime)) {
			estParamOffice.setUpDate(new Date((ConvertUtil.parseLong(officeLastUpdateTime))));
		}

		// 試算パラメータ
		EstimationParam estimationParam = new EstimationParam();
		estimationParam.setEstimationBase(EstimationBase.getInstance(officeEstimationBase));
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		estimationParam.setIndexRyhRtsu(ConvertUtil.parseInteger(officeIndexRyhRtsu));
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		estimationParam.setIndexMikakutoku(ConvertUtil.parseInteger(officeIndexMikakutoku));
		estimationParam.setIndexDelivery(ConvertUtil.parseInteger(officeIndexDelivery));
		estimationParam.setIndexFree1(ConvertUtil.parseInteger(officeIndexFree1));
		estimationParam.setIndexFree2(ConvertUtil.parseInteger(officeIndexFree2));
		estimationParam.setIndexFree3(ConvertUtil.parseInteger(officeIndexFree3));
		estimationParam.setIndexFreeName1(officeIndexFreeName1);
		estimationParam.setIndexFreeName2(officeIndexFreeName2);
		estimationParam.setIndexFreeName3(officeIndexFreeName3);
		estParamOffice.setEstimationParam(estimationParam);

		// 試算配分共通パラメータ
		BaseParam baseParam = new BaseParam();
		baseParam.setRefProdCode(officeRefProdCode);
		baseParam.setRefFrom(RefPeriod.getInstance(officeRefFrom));
		baseParam.setRefTo(RefPeriod.getInstance(officeRefTo));
		baseParam.setResultRefProdCode1(officeResultRefProdCode1);
		baseParam.setResultRefProdCode2(officeResultRefProdCode2);
		baseParam.setResultRefProdCode3(officeResultRefProdCode3);
		baseParam.setResultRefProdCode4(officeResultRefProdCode4);
		estParamOffice.setBaseParam(baseParam);

		return new EstimationParamUpdateDto(estParamOffice, prodName);
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.prodName = null;
		this.officeEstimationBase = null;
		this.officeIndexMikakutoku = null;
		this.officeIndexDelivery = null;
		this.officeIndexFree1 = null;
		this.officeIndexFree2 = null;
		this.officeIndexFree3 = null;
		this.officeIndexFreeName1 = null;
		this.officeIndexFreeName2 = null;
		this.officeIndexFreeName3 = null;
		this.officeRefProdCode = null;
		this.officeRefFrom = null;
		this.officeRefTo = null;
		this.officeResultRefProdCode1 = null;
		this.officeResultRefProdCode2 = null;
		this.officeResultRefProdCode3 = null;
		this.officeResultRefProdCode4 = null;
		this.officeSeqKey = null;
		this.officeLastUpdateTime = null;
	}
}
