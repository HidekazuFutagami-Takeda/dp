package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.CalcType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 計画対象品目リストの検索結果用DTO
 * 
 * @author khashimoto
 */
public class PlannedProdResultListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索結果DTO
	 */
	private final List<PlannedProdResultDto> resultList;

	/**
	 * 営業所基準フラグ
	 */
	private final Boolean officeFlg;

	/**
	 * チーム基準フラグ
	 */
	private final Boolean teamFlg;

	/**
	 * 担当者基準フラグ
	 */
	private final Boolean mrFlg;

	/**
	 * 試算タイプ
	 */
	private final CalcType calcType;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param resultList 検索結果DTO
	 * @param officeFlg 営業所基準フラグ
	 * @param teamFlg チーム基準フラグ
	 * @param mrFlg 担当者基準フラグ
	 * @param calcType 試算タイプ
	 */
	public PlannedProdResultListDto(final List<PlannedProdResultDto> resultList, final boolean officeFlg, final boolean teamFlg, final boolean mrFlg, final CalcType calcType) {
		this.resultList = resultList;
		this.officeFlg = officeFlg;
		this.teamFlg = teamFlg;
		this.mrFlg = mrFlg;
		this.calcType = calcType;
	}

	/**
	 * 検索結果DTOを取得する。
	 * 
	 * @return 検索結果DTO
	 */
	public List<PlannedProdResultDto> getResultList() {
		return resultList;
	}

	/**
	 * 営業所基準フラグを取得する。
	 * 
	 * @return 営業所基準フラグ
	 */
	public Boolean getOfficeFlg() {
		return officeFlg;
	}

	/**
	 * チーム基準フラグを取得する。
	 * 
	 * @return チーム基準フラグ
	 */
	public Boolean getTeamFlg() {
		return teamFlg;
	}

	/**
	 * 担当者基準フラグを取得する。
	 * 
	 * @return 担当者基準フラグ
	 */
	public Boolean getMrFlg() {
		return mrFlg;
	}

	/**
	 * 試算タイプを取得する。
	 * 
	 * @return 試算タイプ
	 */
	public CalcType getCalcType() {
		return calcType;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
