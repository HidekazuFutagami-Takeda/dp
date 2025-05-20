package jp.co.takeda.web.in.dps;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.EstimationParamUpdateDto;
import jp.co.takeda.dto.EstimationResultDetailExecDto;
import jp.co.takeda.dto.EstimationResultDetailResultInfoDto;
import jp.co.takeda.dto.EstimationResultDetailResultRowDto;
import jp.co.takeda.dto.EstimationResultDetailScDto;
import jp.co.takeda.model.EstParamOffice;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

/**
 * Dps302C01((医)試算結果詳細画面)のフォームクラス
 *
 * @author stakeuchi
 */
public class Dps302C01Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果(共通情報)取得キー
	 */
	public static final BoxKey DPS302C01_DATA_R_INFO_RESULT = new BoxKeyPerClassImpl(Dps302C01Form.class, EstimationResultDetailResultInfoDto.class);

	/**
	 * 検索結果(テーブル行)取得キー
	 */
	public static final BoxKey DPS302C01_DATA_R_LIST_RESULT = new BoxKeyPerClassImpl(Dps302C01Form.class, EstimationResultDetailResultRowDto.class);

	/**
	 * チーム部門フラグ取得キー
	 */
	public static final BoxKey DPS302C01_DATA_R_TEAM_FLAG = new BoxKeyPerClassImpl(Dps302C01Form.class, Boolean.class);

	/**
	 * ActionForm ⇒ ScDto 変換処理
	 *
	 * @return EstimationResultDetailScDto 変換されたScDto
	 */
	public EstimationResultDetailScDto convertEstimationResultDetailScDto() throws Exception {
		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3)) {
			sosCd3 = null;
		}
		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd4)) {
			sosCd4 = null;
		}
		// 品目コード
		if (StringUtils.isEmpty(prodCode)) {
			prodCode = null;
		}
		// 対象区分
		InsType _insType = null;
		if (StringUtils.equals(insType, "1")) {
			_insType = InsType.UH;
		} else if (StringUtils.equals(insType, "2")) {
			_insType = InsType.P;
		}
		// カテゴリ
		if (StringUtils.isEmpty(category)) {
			category = null;
		}

//		return new EstimationResultDetailScDto(sosCd3, sosCd4, prodCode, _insType);
		return new EstimationResultDetailScDto(sosCd3, sosCd4, prodCode, _insType, category);
	}

	/**
	 * ActionForm ⇒ UpdateDto 変換処理
	 *
	 * @return EstimationResultDetailScDto 変換されたScDto
	 */
	public EstimationResultDetailExecDto convertEstimationResultDetailUpdateDto() throws Exception {
		// 組織コード(営業所)
		if (StringUtils.isEmpty(sosCd3)) {
			sosCd3 = null;
		}
		// 品目固定コード
		if (StringUtils.isEmpty(prodCode)) {
			prodCode = null;
		}
		// 品目名称
		if (StringUtils.isEmpty(prodName)) {
			prodName = null;
		}
		// シーケンスキー
		Long lParamSeqKey = ConvertUtil.parseLong(paramSeqKey);
		// 最終更新日時
		Date paramUpDate = ConvertUtil.parseDate(paramUpDateTime);
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		// 留保率
		Integer iIndexRyhRtsu = ConvertUtil.parseInteger(indexRyhRtsu);
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		// 指数(未獲得市場)
		Integer iIndexMikakutoku = ConvertUtil.parseInteger(indexMikakutoku);
		// 指数(納入実績)
		Integer iIndexDelivery = ConvertUtil.parseInteger(indexDelivery);
		// 指数(フリー項目1)
		Integer iIndexFree1 = ConvertUtil.parseInteger(indexFree1);
		// 指数(フリー項目2)
		Integer iIndexFree2 = ConvertUtil.parseInteger(indexFree2);
		// 指数(フリー項目3)
		Integer iIndexFree3 = ConvertUtil.parseInteger(indexFree3);
		// カテゴリ
		if (StringUtils.isEmpty(category)) {
			category = null;
		}
// mod start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
//		return new EstimationResultDetailExecDto(sosCd3, prodCode, prodName, lParamSeqKey, paramUpDate, iIndexMikakutoku, iIndexDelivery, iIndexFree1, iIndexFree2, iIndexFree3);
		return new EstimationResultDetailExecDto(sosCd3, prodCode, prodName, lParamSeqKey, paramUpDate, iIndexRyhRtsu, iIndexMikakutoku, iIndexDelivery, iIndexFree1, iIndexFree2, iIndexFree3, category);
// mod end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	}

	/**
	 * 画面から入力されたデータを、事前チェック用DTOに変換する。
	 *
	 * @return 事前チェック用DTO
	 */
	public EstimationParamUpdateDto getEstimationParamUpdateData() throws Exception {

		EstParamOffice estParamOffice = new EstParamOffice();
		estParamOffice.setSosCd(sosCd3);
		estParamOffice.setProdCode(prodCode);

		// 試算パラメータ
		EstimationParam estimationParam = new EstimationParam();
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		estimationParam.setIndexRyhRtsu(ConvertUtil.parseInteger(indexRyhRtsu));
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		estimationParam.setIndexMikakutoku(ConvertUtil.parseInteger(indexMikakutoku));
		estimationParam.setIndexDelivery(ConvertUtil.parseInteger(indexDelivery));
		estimationParam.setIndexFree1(ConvertUtil.parseInteger(indexFree1));
		estimationParam.setIndexFree2(ConvertUtil.parseInteger(indexFree2));
		estimationParam.setIndexFree3(ConvertUtil.parseInteger(indexFree3));
		estimationParam.setIndexFreeName1(formIndexFreeName1);
		estimationParam.setIndexFreeName2(formIndexFreeName2);
		estimationParam.setIndexFreeName3(formIndexFreeName3);
		estParamOffice.setEstimationParam(estimationParam);
		return new EstimationParamUpdateDto(estParamOffice, prodName);
	}

	public void convertActionForm(EstimationResultDetailResultInfoDto dto) {
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexRyhRtsu = ConvertUtil.toString(dto.getIndexRyhRtsu());
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexMikakutoku = ConvertUtil.toString(dto.getIndexMikakutoku());
		this.indexDelivery = ConvertUtil.toString(dto.getIndexDelivery());
		this.indexFree1 = ConvertUtil.toString(dto.getIndexFree1());
		this.indexFree2 = ConvertUtil.toString(dto.getIndexFree2());
		this.indexFree3 = ConvertUtil.toString(dto.getIndexFree3());
		this.formIndexFreeName1 = dto.getIndexFreeName1();
		this.formIndexFreeName2 = dto.getIndexFreeName2();
		this.formIndexFreeName3 = dto.getIndexFreeName3();
	}

	// -------------------------------
	// 部品
	// -------------------------------
//	/**
//	 * 対象区分
//	 */
//	public static final List<CodeAndValue> INS_TYPES;
//
//	/**
//	 * static initilizer
//	 */
//	static {
//		List<CodeAndValue> tmp = null;
//		// 対象区分にセット
//		tmp = new ArrayList<CodeAndValue>(3);
//		tmp.add(new CodeAndValue(InsType.UH.getDbValue(), "UH"));
//		tmp.add(new CodeAndValue(InsType.P.getDbValue(), "P"));
//		tmp.add(new CodeAndValue("9", "合計"));
//		INS_TYPES = Collections.unmodifiableList(tmp);
//	}

//	/**
//	 * [プルダウンリスト]対象区分を取得する。
//	 *
//	 * @return [プルダウンリスト]対象区分
//	 */
//	public List<CodeAndValue> getInsTypes() {
//		return INS_TYPES;
//	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 組織コード(営業所)
	 */
	private String sosCd3;

	/**
	 * 組織コード(チーム)
	 */
	private String sosCd4;

	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * 対象区分コード
	 */
	private String insType;

	// NOT INIT PARAMS
	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * [試算パラメータ] シーケンスキー
	 */
	private String paramSeqKey;

	/**
	 * [試算パラメータ] 最終更新日(getTime()の値)
	 */
	private String paramUpDateTime;

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * [試算パラメータ] 留保率
	 */
	private String indexRyhRtsu;
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * [試算パラメータ] 指数(未獲得市場)
	 */
	private String indexMikakutoku;

	/**
	 * [試算パラメータ] 指数(納入実績)
	 */
	private String indexDelivery;

	/**
	 * [試算パラメータ] 指数(フリー項目1)
	 */
	private String indexFree1;

	/**
	 * [試算パラメータ] 指数(フリー項目2)
	 */
	private String indexFree2;

	/**
	 * [試算パラメータ] 指数(フリー項目3)
	 */
	private String indexFree3;

	/**
	 * [試算パラメータ] 指数名称(フリー項目1)
	 */
	private String indexFreeName1;

	/**
	 * [試算パラメータ] 指数名称(フリー項目2)
	 */
	private String indexFreeName2;

	/**
	 * [試算パラメータ] 指数名称(フリー項目3)
	 */
	private String indexFreeName3;

	/**
	 * [試算パラメータ] 指数名称(フリー項目1)文字化け対策
	 */
	private String formIndexFreeName1;

	/**
	 * [試算パラメータ] 指数名称(フリー項目2)文字化け対策
	 */
	private String formIndexFreeName2;

	/**
	 * [試算パラメータ] 指数名称(フリー項目3)文字化け対策
	 */
	private String formIndexFreeName3;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * ヘッダ（UH）
	 */
	private String headerUh;

	/**
	 * ヘッダ（P）
	 */
	private String headerP;

	/**
	 * 対象区分
	 */
	private List<CodeAndValue> insTypes;

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
	 * 品目固定コードを取得する。
	 *
	 * @return prodCode 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * 品目名称を取得する。
	 *
	 * @return prodName 品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称を設定する。
	 *
	 * @param prodName 品目名称
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 対象区分を取得する。
	 *
	 * @return insType 対象区分
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param insType 対象区分
	 */
	public void setInsType(String insType) {
		this.insType = insType;
	}

	/**
	 * [試算パラメータ] シーケンスキーを取得する。
	 *
	 * @return paramSeqKey [試算パラメータ] シーケンスキー
	 */
	public String getParamSeqKey() {
		return paramSeqKey;
	}

	/**
	 * [試算パラメータ] シーケンスキーを設定する。
	 *
	 * @param paramSeqKey [試算パラメータ] シーケンスキー
	 */
	public void setParamSeqKey(String paramSeqKey) {
		this.paramSeqKey = paramSeqKey;
	}

	/**
	 * [試算パラメータ] 最終更新日(getTime()の値)を取得する。
	 *
	 * @return paramUpDateTime [試算パラメータ] 最終更新日(getTime()の値)
	 */
	public String getParamUpDateTime() {
		return paramUpDateTime;
	}

	/**
	 * [試算パラメータ] 最終更新日(getTime()の値)を設定する。
	 *
	 * @param paramUpDateTime [試算パラメータ] 最終更新日(getTime()の値)
	 */
	public void setParamUpDateTime(String paramUpDateTime) {
		this.paramUpDateTime = paramUpDateTime;
	}

// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
	/**
	 * [試算パラメータ] 留保率を取得する。
	 *
	 * @return indexRyhRtsu [試算パラメータ] 留保率
	 */
	public String getIndexRyhRtsu() {
		return indexRyhRtsu;
	}

	/**
	 * [試算パラメータ] 留保率を設定する。
	 *
	 * @param indexRyhRtsu [試算パラメータ] 留保率
	 */
	public void setIndexRyhRtsu(String indexRyhRtsu) {
		this.indexRyhRtsu = indexRyhRtsu;
	}
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）

	/**
	 * [試算パラメータ] 指数(未獲得市場)を取得する。
	 *
	 * @return indexMikakutoku [試算パラメータ] 指数(未獲得市場)
	 */
	public String getIndexMikakutoku() {
		return indexMikakutoku;
	}

	/**
	 * [試算パラメータ] 指数(納入実績)を設定する。
	 *
	 * @param indexMikakutoku [試算パラメータ] 指数(納入実績)
	 */
	public void setIndexMikakutoku(String indexMikakutoku) {
		this.indexMikakutoku = indexMikakutoku;
	}

	/**
	 * [試算パラメータ] 指数(納入実績)を取得する。
	 *
	 * @return indexDelivery [試算パラメータ] 指数(納入実績)
	 */
	public String getIndexDelivery() {
		return indexDelivery;
	}

	/**
	 * [試算パラメータ] 指数(納入実績)を設定する。
	 *
	 * @param indexDelivery [試算パラメータ] 指数(納入実績)
	 */
	public void setIndexDelivery(String indexDelivery) {
		this.indexDelivery = indexDelivery;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目1)を取得する。
	 *
	 * @return indexFree1 [試算パラメータ] 指数(フリー項目1)
	 */
	public String getIndexFree1() {
		return indexFree1;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目1)を設定する。
	 *
	 * @param indexFree1 [試算パラメータ] 指数(フリー項目1)
	 */
	public void setIndexFree1(String indexFree1) {
		this.indexFree1 = indexFree1;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目2)を取得する。
	 *
	 * @return indexFree2 [試算パラメータ] 指数(フリー項目2)
	 */
	public String getIndexFree2() {
		return indexFree2;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目2)を設定する。
	 *
	 * @param indexFree2 [試算パラメータ] 指数(フリー項目2)
	 */
	public void setIndexFree2(String indexFree2) {
		this.indexFree2 = indexFree2;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目3)を取得する。
	 *
	 * @return indexFree3 [試算パラメータ] 指数(フリー項目3)
	 */
	public String getIndexFree3() {
		return indexFree3;
	}

	/**
	 * [試算パラメータ] 指数(フリー項目3)を設定する。
	 *
	 * @param indexFree3 [試算パラメータ] 指数(フリー項目3)
	 */
	public void setIndexFree3(String indexFree3) {
		this.indexFree3 = indexFree3;
	}

	/**
	 * [試算パラメータ] 指数名称(フリー項目1)を取得する。
	 *
	 * @return indexFreeName1 [試算パラメータ] 指数名称(フリー項目1)
	 */
	public String getIndexFreeName1() {
		return indexFreeName1;
	}

	/**
	 * [試算パラメータ] 指数名称(フリー項目1)を設定する。
	 *
	 * @param indexFreeName1 [試算パラメータ] 指数名称(フリー項目1)
	 */
	public void setIndexFreeName1(String indexFreeName1) {
		this.indexFreeName1 = indexFreeName1;
	}

	/**
	 * [試算パラメータ] 指数名称(フリー項目2)を取得する。
	 *
	 * @return indexFreeName2 [試算パラメータ] 指数名称(フリー項目2)
	 */
	public String getIndexFreeName2() {
		return indexFreeName2;
	}

	/**
	 * [試算パラメータ] 指数名称(フリー項目2)を設定する。
	 *
	 * @param indexFreeName2 [試算パラメータ] 指数名称(フリー項目2)
	 */
	public void setIndexFreeName2(String indexFreeName2) {
		this.indexFreeName2 = indexFreeName2;
	}

	/**
	 * [試算パラメータ] 指数名称(フリー項目3)を取得する。
	 *
	 * @return indexFreeName3 [試算パラメータ] 指数名称(フリー項目3)
	 */
	public String getIndexFreeName3() {
		return indexFreeName3;
	}

	/**
	 * [試算パラメータ] 指数名称(フリー項目3)を設定する。
	 *
	 * @param indexFreeName3 [試算パラメータ] 指数名称(フリー項目3)
	 */
	public void setIndexFreeName3(String indexFreeName3) {
		this.indexFreeName3 = indexFreeName3;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
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
	 * ヘッダ(UH)を取得する。
	 *
	 * @return headerUh  ヘッダ(UH)
	 */
	public String getHeaderUh() {
		return headerUh;
	}

	/**
	 * ヘッダ(UH)を設定する。
	 *
	 * @param headerUh  ヘッダ(UH)
	 */
	public void setHeaderUh(String headerUh) {
		this.headerUh = headerUh;
	}

	/**
	 * ヘッダ(P)を取得する。
	 *
	 * @return headerP  ヘッダ(P)
	 */
	public String getHeaderP() {
		return headerP;
	}

	/**
	 * ヘッダ(P)を設定する。
	 *
	 * @param headerP  ヘッダ(P)
	 */
	public void setHeaderP(String headerP) {
		this.headerP = headerP;
	}

	/**
	 * 対象区分を取得する。
	 *
	 * @return insTypes 対象区分
	 */
	public List<CodeAndValue> getInsTypes() {
		return insTypes;
	}

	/**
	 * 対象区分を設定する。
	 *
	 * @param insTypes 対象区分
	 */
	public void setInsTypes(List<CodeAndValue> insTypes) {
		this.insTypes = insTypes;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		this.prodName = null;
		this.paramSeqKey = null;
		this.paramUpDateTime = null;
// add start 2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexRyhRtsu = null;
// add end   2016/07/11 S.Hayashi B16-0087_試算ロジック変更対応（留保率）
		this.indexMikakutoku = null;
		this.indexDelivery = null;
		this.indexFree1 = null;
		this.indexFree2 = null;
		this.indexFree3 = null;
		this.indexFreeName1 = null;
		this.indexFreeName2 = null;
		this.indexFreeName3 = null;
		this.formIndexFreeName1 = null;
		this.formIndexFreeName2 = null;
		this.formIndexFreeName3 = null;
	}
}
