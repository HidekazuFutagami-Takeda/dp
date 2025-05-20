package jp.co.takeda.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.TytenKisLevel;

/**
 * 特約店情報の検索条件(Search Condition)を表すDTO
 *
 * @author khashimoto
 */
public class TmsTytenMstScDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 抽出対象の特約店階層レベル
	 */
	private final TytenKisLevel tytenKisLevel;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 本店
	 */
	private final String tmsTytenCdHonten;

	/**
	 * 支社
	 */
	private final String tmsTytenCdShisha;

	/**
	 * 支店
	 */
	private final String tmsTytenCdShiten;

	/**
	 * 組織コード（特約店部)
	 */
	private final String sosCd2;

	/**
	 * 本店名（半角カナ）
	 */
	private final String hontenMeiKn;

	/**
	 * 従業員番号
	 */
	private final String jgiNo;

	/**
	 * 対象施設
	 */
	private final String insType;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * t都道府県コード
	 */
	private final String addrCodePref;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 組織コード
	 */
	private String addrSosCode;

	/**
	 * コンストラクタ
	 *
	 * @param tytenKisLevel 抽出対象の特約店階層レベル
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenCdHonten 本店
	 * @param tmsTytenCdShisha 支社
	 * @param tmsTytenCdShiten 支店
	 * @param sosCd2 組織コード（特約店部)
	 * @param hontenMeiKn 本店名（半角カナ）
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * @param addrCodePref 都道府県コード
	 */
	public TmsTytenMstScDto(TytenKisLevel tytenKisLevel, String tmsTytenCd, String tmsTytenCdHonten, String tmsTytenCdShisha, String tmsTytenCdShiten, String sosCd2,
		String hontenMeiKn, String jgiNo, String insType, String addrCodePref) {
		this.tytenKisLevel = tytenKisLevel;
		this.tmsTytenCd = tmsTytenCd;
		this.tmsTytenCdHonten = tmsTytenCdHonten;
		this.tmsTytenCdShisha = tmsTytenCdShisha;
		this.tmsTytenCdShiten = tmsTytenCdShiten;
		this.sosCd2 = sosCd2;
		this.hontenMeiKn = hontenMeiKn;
		this.jgiNo = jgiNo;
		this.insType = insType;
		this.addrCodePref = addrCodePref;
	}

	/**
	 * 抽出対象の特約店階層レベルを取得する。
	 *
	 * @return tytenKisLevel 抽出対象の特約店階層レベル
	 */
	public TytenKisLevel getTytenKisLevel() {
		return tytenKisLevel;
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
	 * 本店を取得する。
	 *
	 * @return tmsTytenCdHonten 本店
	 */
	public String getTmsTytenCdHonten() {
		return tmsTytenCdHonten;
	}

	/**
	 * 支社を取得する。
	 *
	 * @return tmsTytenCdShisha 支社
	 */
	public String getTmsTytenCdShisha() {
		return tmsTytenCdShisha;
	}

	/**
	 * 支店を取得する。
	 *
	 * @return tmsTytenCdShiten 支店
	 */
	public String getTmsTytenCdShiten() {
		return tmsTytenCdShiten;
	}

	/**
	 * 組織コード（特約店部)を取得する。
	 *
	 * @return sosCd2 組織コード（特約店部)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 本店名（半角カナ）を取得する。
	 *
	 * @return hontenMeiKn 本店名（半角カナ）
	 */
	public String getHontenMeiKn() {
		return hontenMeiKn;
	}

	/**
	 * 従業員番号を取得する
	 *
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * 対象施設を取得する
	 *
	 * @return insType
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 組織コードを取得する。
	 *
	 * @return 都道府県コード
	 */
	public String getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 組織コードを取得する。
	 *
	 * @return 都道府県コード
	 */
	public String getAddrSosCode() {
		return addrSosCode;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 組織コードをセットする。
	 * @param addrSosCode
	 * @return 都道府県コード
	 */
	public void setAddrSosCode(String addrSosCode) {
		this.addrSosCode = addrSosCode;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
