package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.CalcType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算対象品目一覧の検索結果用DTO
 * 
 * @author khashimoto
 */
public class EstimationProdListResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 試算タイプ
	 */
	private final CalcType calcType;

	/**
	 * 試算対象品目一覧
	 */
	private final List<EstimationProdResultDto> estimationProdResultDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param calcType 試算対象品目情報
	 * @param estimationProdResultDtoList 試算対象品目一覧
	 */
	public EstimationProdListResultDto(CalcType calcType, List<EstimationProdResultDto> estimationProdResultDtoList) {

		this.calcType = calcType;
		this.estimationProdResultDtoList = estimationProdResultDtoList;
	}

	/**
	 * 試算タイプを取得する。
	 * 
	 * @return 試算タイプ
	 */
	public CalcType getCalcType() {
		return calcType;
	}

	/**
	 * 試算対象品目一覧を取得する。
	 * 
	 * @return 試算対象品目一覧
	 */
	public List<EstimationProdResultDto> getEstimationProdResultDtoList() {
		return estimationProdResultDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
