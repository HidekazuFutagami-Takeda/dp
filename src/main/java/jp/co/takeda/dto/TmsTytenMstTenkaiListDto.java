package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.TmsTytenMstUn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店(展開)検索の結果格納DTO
 * 
 * @author khashimoto
 */
public class TmsTytenMstTenkaiListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 特約店部名
	 */
	private final String tytenBuName;

	/**
	 * 本店名漢字
	 */
	private final String hontenMeiKj;

	/**
	 * 支社名漢字
	 */
	private final String shishaMeiKj;

	/**
	 * 支店名漢字
	 */
	private final String shitenMeiKj;

	/**
	 * 特約店基本統合のリスト
	 */
	private final List<TmsTytenMstTenkaiDto> tmsTytenMstTenkaiList;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param tmsTytenMstUn 選択中の特約店基本統合
	 * @param tmsTytenMstTenkaiList 特約店基本統合のリスト
	 */
	public TmsTytenMstTenkaiListDto(List<TmsTytenMstTenkaiDto> tmsTytenMstTenkaiList) {
		this.tmsTytenMstTenkaiList = tmsTytenMstTenkaiList;
		this.tytenBuName = null;
		this.hontenMeiKj = null;
		this.shishaMeiKj = null;
		this.shitenMeiKj = null;
	}

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param tytenBuName 特約店部名
	 * @param tmsTytenMstUn 選択中の特約店基本統合
	 * @param tmsTytenMstList 特約店基本統合のリスト
	 */
	public TmsTytenMstTenkaiListDto(String tytenBuName, TmsTytenMstUn tmsTytenMstUn, List<TmsTytenMstTenkaiDto> tmsTytenMstTenkaiList) {
		this.tmsTytenMstTenkaiList = tmsTytenMstTenkaiList;
		this.tytenBuName = tytenBuName;
		this.hontenMeiKj = tmsTytenMstUn.getHontenMeiKj();
		this.shishaMeiKj = tmsTytenMstUn.getShishaMeiKj();
		this.shitenMeiKj = tmsTytenMstUn.getShitenMeiKj();
	}

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param tytenBuName 特約店部名
	 * @param tmsTytenMstList 特約店基本統合のリスト
	 */
	public TmsTytenMstTenkaiListDto(String tytenBuName, List<TmsTytenMstTenkaiDto> tmsTytenMstTenkaiList) {
		this.tmsTytenMstTenkaiList = tmsTytenMstTenkaiList;
		this.tytenBuName = tytenBuName;
		this.hontenMeiKj = null;
		this.shishaMeiKj = null;
		this.shitenMeiKj = null;
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
	 * 本店名漢字を取得する。
	 * 
	 * @return 本店名漢字
	 */
	public String getHontenMeiKj() {
		return hontenMeiKj;
	}

	/**
	 * 支社名漢字を取得する。
	 * 
	 * @return 支社名漢字
	 */
	public String getShishaMeiKj() {
		return shishaMeiKj;
	}

	/**
	 * 支店名漢字を取得する。
	 * 
	 * @return 支店名漢字
	 */
	public String getShitenMeiKj() {
		return shitenMeiKj;
	}

	/**
	 * 特約店基本統合のリストを取得する。
	 * 
	 * @return 特約店基本統合のリスト
	 */
	public List<TmsTytenMstTenkaiDto> getTmsTytenMstTenkaiList() {
		return tmsTytenMstTenkaiList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
