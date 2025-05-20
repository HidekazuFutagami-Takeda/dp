package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.TmsTytenMstUn;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 特約店実績検索の結果格納DTO
 * 
 * @author khashimoto
 */
public class TmsTytenMstResultsTenkaiListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

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
	 * @param tmsTytenMstList 特約店基本統合のリスト
	 */
	public TmsTytenMstResultsTenkaiListDto(List<TmsTytenMstTenkaiDto> tmsTytenMstTenkaiList) {
		this.tmsTytenMstTenkaiList = tmsTytenMstTenkaiList;
		this.hontenMeiKj = null;
		this.shishaMeiKj = null;
		this.shitenMeiKj = null;
	}

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param tmsTytenMstUn 選択中の特約店基本統合
	 * @param tmsTytenMstList 特約店基本統合のリスト
	 */
	public TmsTytenMstResultsTenkaiListDto(TmsTytenMstUn tmsTytenMstUn, List<TmsTytenMstTenkaiDto> tmsTytenMstTenkaiList) {
		this.tmsTytenMstTenkaiList = tmsTytenMstTenkaiList;
		this.hontenMeiKj = tmsTytenMstUn.getHontenMeiKj();
		this.shishaMeiKj = tmsTytenMstUn.getShishaMeiKj();
		this.shitenMeiKj = tmsTytenMstUn.getShitenMeiKj();
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
