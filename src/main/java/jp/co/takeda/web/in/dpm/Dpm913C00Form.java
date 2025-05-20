package jp.co.takeda.web.in.dpm;

import java.util.List;

import jp.co.takeda.a.bean.BoxKey;
import jp.co.takeda.a.bean.BoxKeyPerClassImpl;
import jp.co.takeda.a.web.bean.CodeAndValue;
import jp.co.takeda.dto.AddrScDto;
import jp.co.takeda.dto.AddrSearchResultListDto;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.web.cmn.action.DiaDpActionForm;

import org.apache.commons.lang.StringUtils;

/**
 * Dpm913C00(ワ)市区郡検索画面のフォームクラス
 * 
 * @author khashimoto
 */
public class Dpm913C00Form extends DiaDpActionForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果のキー値
	 */
	public static final BoxKey JIS_LIST_KEY_R = new BoxKeyPerClassImpl(Dpm913C00Form.class, AddrSearchResultListDto.class);



	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * 組織に紐づく都道府県
	 */
	public List<CodeAndValue> prefectures;

	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * 都道府県プルダウン リクエストデータ
	 */
	public static final BoxKey DPM913C00_DATA_PREFECTURE_LIST = new BoxKeyPerClassImpl(Dpm913C00Form.class, CodeAndValue.class);

	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * ワクチンの組織に紐づく都道府県を取得する
	 * @param prefectures ワクチンの組織に紐づく都道府県
	 * @return
	 */
	public List<CodeAndValue> getPrefectures() {
		return prefectures;
	}

	/**
	 * ワクチンの組織に紐づく都道府県を設定する
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * @param prefectures ワクチンの組織に紐づく都道府県
	 */
	public void setPrefectures(List<CodeAndValue> prefectures) {
		this.prefectures = prefectures;
	}

	/**
	 * ActionForm⇒ScDto 変換処理
	 * 
	 * @return AddrScDto 変換されたScDto
	 */
	public AddrScDto convertAddrScDto() throws Exception {

		// 府県コード
		Prefecture _addrCodePref = null;
		if (StringUtils.isNotEmpty(this.addrCodePref)) {
			_addrCodePref = Prefecture.getInstance(this.addrCodePref);
		}

		// 市区町村名
		String _sikuMeiKn = null;
		if (StringUtils.isNotEmpty(this.sikuMeiKn)) {
			_sikuMeiKn = this.sikuMeiKn;
		}

		return new AddrScDto(_addrCodePref, _sikuMeiKn);
	}

	// -------------------------------
	// field
	// -------------------------------
	// INパラメータ
	/**
	 * 適用関数名
	 */
	private String fukenApplyFuncName;

	/**
	 * 現在のページ番号
	 */
	private Integer crntPageNo;

	// 検索条件
	/**
	 * 都道府県
	 */
	private String addrCodePref;

	/**
	 * 市区町村
	 */
	private String sikuMeiKn;

	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * 初期表示組織コード
	 */
	private String sosInitSosCodeValue;

	// -------------------------------
	// getter & setter
	// -------------------------------

	/**
	 * 適用関数名を取得する。
	 * 
	 * @return 適用関数名
	 */
	public String getFukenApplyFuncName() {
		return fukenApplyFuncName;
	}

	/**
	 * 適用関数名を設定する。
	 * 
	 * @param fukenApplyFuncName 適用関数名
	 */
	public void setFukenApplyFuncName(String fukenApplyFuncName) {
		this.fukenApplyFuncName = fukenApplyFuncName;
	}

	/**
	 * 現在のページ番号を取得する。
	 * 
	 * @return 現在のページ番号
	 */
	public Integer getCrntPageNo() {
		return crntPageNo;
	}

	/**
	 * 現在のページ番号を設定する。
	 * 
	 * @param crntPageNo 現在のページ番号
	 */
	public void setCrntPageNo(Integer crntPageNo) {
		this.crntPageNo = crntPageNo;
	}

	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * 初期表示組織コードを取得する。
	 *
	 * @return 初期表示組織コード
	 */
	public String getSosInitSosCodeValue() {
		return sosInitSosCodeValue;
	}

	/**
	 * 2022/6/21 Y.Taniguchi バックログNo.21　組織に紐づく都道府県をプルダウンに表示する
	 * 初期表示組織コードを設定する。
	 *
	 * @param sosInitSosCodeValue 初期表示組織コード
	 */
	public void setSosInitSosCodeValue(String sosInitSosCodeValue) {
		this.sosInitSosCodeValue = sosInitSosCodeValue;
	}

	/**
	 * 都道府県を取得する。
	 * 
	 * @return addrCodePref 都道府県
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 都道府県を設定する。
	 * 
	 * @param addrCodePref 都道府県
	 */
	public void setAddrCodePref(String addrCodePref) {
		this.addrCodePref = addrCodePref;
	}

	/**
	 * 市区町村を取得する。
	 * 
	 * @return sikuMeiKn 市区町村
	 */
	public String getSikuMeiKn() {
		return sikuMeiKn;
	}

	/**
	 * 市区町村を設定する。
	 * 
	 * @param sikuMeiKn 市区町村
	 */
	public void setSikuMeiKn(String sikuMeiKn) {
		this.sikuMeiKn = sikuMeiKn;
	}

	// ----------------------------------------
	// INITIALIZE
	// ----------------------------------------
	@Override
	public void formInit() {
		crntPageNo = 1;
	}
}
