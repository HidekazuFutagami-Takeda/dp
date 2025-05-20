package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 組織・従業員検索の結果格納DTO
 * 
 * @author khashimoto
 */
public class SosJgiDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 選択した組織情報リスト
	 */
	private final List<SosMst> sosMstList;

	/**
	 * 選択した組織・従業員名
	 */
	private final String sosJgiName;

	/**
	 * 選択した組織コード（支店階層）
	 */
	private final String sosCd2;

	/**
	 * 選択した組織コード（営業所階層）
	 */
	private final String sosCd3;

	/**
	 * 選択した組織コード（チーム階層）
	 */
	private final String sosCd4;

	/**
	 * 選択した従業員情報
	 */
	private final JgiMst jgiMst;

	/**
	 * コンストラクタ
	 * 
	 * @param sosMstList 選択した組織情報リスト
	 * @param sosJgiName 選択した組織・従業員名
	 * @param sosCd2 選択した組織コード（支店階層）
	 * @param sosCd3 選択した組織コード（営業所階層）
	 * @param sosCd4 選択した組織コード（チーム階層）
	 * @param jgiMst 選択した従業員情報
	 */
	public SosJgiDto(List<SosMst> sosMstList, String sosJgiName, String sosCd2, String sosCd3, String sosCd4, JgiMst jgiMst) {
		this.sosMstList = sosMstList;
		this.sosJgiName = sosJgiName;
		this.sosCd2 = sosCd2;
		this.sosCd3 = sosCd3;
		this.sosCd4 = sosCd4;
		this.jgiMst = jgiMst;
	}

	/**
	 * 選択した組織情報リストを取得する。
	 * 
	 * @return sosMstList 選択した組織情報リスト
	 */
	public List<SosMst> getSosMstList() {
		return sosMstList;
	}

	/**
	 * 選択した組織・従業員名を取得する。
	 * 
	 * @return sosJgiName 選択した組織・従業員名
	 */
	public String getSosJgiName() {
		return sosJgiName;
	}

	/**
	 * 選択した組織コード（支店階層）を取得する。
	 * 
	 * @return sosCd2 選択した組織コード（支店階層）
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 選択した組織コード（営業所階層）を取得する。
	 * 
	 * @return sosCd3 選択した組織コード（営業所階層）
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 選択した組織コード（チーム階層）を取得する。
	 * 
	 * @return sosCd4 選択した組織コード（チーム階層）
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 選択した従業員情報を取得する。
	 * 
	 * @return 選択した従業員情報
	 */
	public JgiMst getJgiMst() {
		return jgiMst;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
