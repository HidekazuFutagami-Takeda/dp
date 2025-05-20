package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * 過去実績参照(担当者別計画)DTOクラス
 * 
 * @author siwamoto
 */
public class DeliveryResultMrListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 納入実績リスト(サマリー)
	 */
	private final List<DeliveryResultMrDto> deliveryResultMrDtoSList;

	/**
	 * 納入実績リスト
	 */
	private final List<DeliveryResultMrDto> deliveryResultMrDtoList;

	/**
	 * 参照年月のリスト
	 */
	private final List<Date> refDateList;

	/**
	 * コンストラクタ
	 * 
	 * @param deliveryResultMrDtoSList 納入実績リスト(サマリー)
	 * @param deliveryResultMrDtoList 納入実績リスト
	 * @param refDateList 参照年月のリスト
	 */
	public DeliveryResultMrListDto(List<DeliveryResultMrDto> deliveryResultMrDtoSList, List<DeliveryResultMrDto> deliveryResultMrDtoList, List<Date> refDateList) {
		this.deliveryResultMrDtoSList = deliveryResultMrDtoSList;
		this.deliveryResultMrDtoList = deliveryResultMrDtoList;
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
	public List<DeliveryResultMrDto> getDeliveryResultMrDtoSList() {
		return deliveryResultMrDtoSList;
	}

	/**
	 * 納入実績リストを取得する。
	 * 
	 * @return 納入実績リスト
	 */
	public List<DeliveryResultMrDto> getDeliveryResultMrDtoList() {
		return deliveryResultMrDtoList;
	}

	/**
	 * 参照年月のリストを取得する。
	 * 
	 * @return 参照年月のリスト
	 */
	public List<Date> getRefDateList() {
		return refDateList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
