package jp.co.takeda.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * 過去実績参照（施設特約店別計画）DTOクラス
 * 
 * @author siwamoto
 */
public class DeliveryResultVacInsListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 1行をあらわすDTOのリスト
	 */
	public final ArrayList<DeliveryResultInsDto> resultDtoList;

	/**
	 * 参照期間リスト
	 */
	public final List<Date> refDateList;

	/**
	 * 従業員名
	 */
	public final String jgiName;

	/**
	 * コンストラクタ
	 * 
	 * @param resultDtoList
	 * @param refDateList
	 * @param jgiName
	 */
	public DeliveryResultVacInsListDto(ArrayList<DeliveryResultInsDto> resultDtoList, List<Date> refDateList, String jgiName) {
		super();
		this.resultDtoList = resultDtoList;
		this.refDateList = refDateList;
		this.jgiName = jgiName;
	}

	/**
	 * 1行をあらわすDTOを取得する。
	 * 
	 * @return resultDtoList
	 */
	public ArrayList<DeliveryResultInsDto> getResultDtoList() {
		return resultDtoList;
	}

	/**
	 * 参照期間リストを取得する。
	 * 
	 * @return refDateList
	 */
	public List<Date> getRefDateList() {
		return refDateList;
	}

	/**
	 * 従業員名を取得する。
	 * 
	 * @return jgiName
	 */
	public String getJgiName() {
		return jgiName;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
