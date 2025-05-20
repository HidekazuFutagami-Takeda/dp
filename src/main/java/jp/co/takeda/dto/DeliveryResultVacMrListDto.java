package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 過去実績参照（担当者別計画）DTOクラス
 * 
 * @author siwamoto
 */
public class DeliveryResultVacMrListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 納入実績リスト（サマリー）
	 */
	private final List<DeliveryResultVacMrDto> deliveryResultMrDtoSList;

	/**
	 * 納入実績リスト
	 */
	private final List<DeliveryResultVacMrDto> deliveryResultMrDtoList;

	/**
	 * 参照年月のリスト
	 */
	private final List<Date> refDateList;

	/**
	 * コンストラクタ
	 * 
	 * @param resultDtoSList
	 * @param resultDtoList
	 * @param refDateList
	 */
	public DeliveryResultVacMrListDto(List<DeliveryResultVacMrDto> resultDtoSList, List<DeliveryResultVacMrDto> resultDtoList, List<Date> refDateList) {
		super();
		this.deliveryResultMrDtoSList = resultDtoSList;
		this.deliveryResultMrDtoList = resultDtoList;
		this.refDateList = refDateList;
	}

	//---------------------
	// Getter
	// --------------------

	/**
	 * 納入実績リスト（サマリー）を取得する。
	 * 
	 * @return 納入実績リスト
	 */
	public List<DeliveryResultVacMrDto> getDeliveryResultMrDtoSList() {
		return deliveryResultMrDtoSList;
	}

	/**
	 * 納入実績リストを取得する。
	 * 
	 * @return 納入実績リスト
	 */
	public List<DeliveryResultVacMrDto> getDeliveryResultMrDtoList() {
		return deliveryResultMrDtoList;
	}

	/**
	 * 参照年月のリストを取得する。
	 * 
	 * @return refDateList
	 */
	public List<Date> getRefDateList() {
		return refDateList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
