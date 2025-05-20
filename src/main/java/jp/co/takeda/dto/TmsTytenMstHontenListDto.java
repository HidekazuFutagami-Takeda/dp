package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 特約店検索の結果格納DTO
 * 
 * @author khashimoto
 */
public class TmsTytenMstHontenListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店部名
	 */
	private final String tytenBuName;

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 特約店部名
	 */
	private final String tytenBuCode;

	/**
	 * 特約店基本統合のリスト
	 */
	private final List<TmsTytenMstHontenDto> tmsTytenMstHontenList;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param tytenBuName 特約店部名
	 * @param tmsTytenMstList 特約店基本統合のリスト
	 */
	public TmsTytenMstHontenListDto(String tytenBuName, String tytenBuCode, List<TmsTytenMstHontenDto> tmsTytenMstExtList) {
		this.tmsTytenMstHontenList = tmsTytenMstExtList;
		this.tytenBuName = tytenBuName;
		// add 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		this.tytenBuCode = tytenBuCode;
	}

	/**
	 * 特約店部名を取得する。
	 * 
	 * @return 特約店部名
	 */
	public String getTytenBuName() {
		return tytenBuName;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 特約店部コードを取得する。
	 *
	 * @return 特約店部コード
	 */
	public String getTytenBuCode() {
		return tytenBuCode;
	}

	/**
	 * 特約店基本統合のリストを取得する。
	 * 
	 * @return 特約店基本統合のリスト
	 */
	public List<TmsTytenMstHontenDto> getTmsTytenMstHontenList() {
		return tmsTytenMstHontenList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
