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
public class DeliveryResultInsListDto extends DpDto {

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
	 * 職種名
	 */
	public final String shokushuName;

    //add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	/**
	 * 品目名称（Ref[計画対象品目].〔品目名称〕）
	 */
	public final String prodName;
	//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する

	/**
	 * コンストラクタ
	 *
	 * @param resultDtoList 1行をあらわすDTOのリスト
	 * @param refDateList 参照期間リスト
	 * @param jgiName 従業員名
	 * @param shokushuName 職種名
	 */
	public DeliveryResultInsListDto(ArrayList<DeliveryResultInsDto> resultDtoList, List<Date> refDateList, String jgiName, String shokushuName, String prodName) {
		this.resultDtoList = resultDtoList;
		this.refDateList = refDateList;
		this.jgiName = jgiName;
		this.shokushuName = shokushuName;
		//add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
		this.prodName = prodName;
		//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	}

	/**
	 * 1行をあらわすDTOを取得する。
	 *
	 * @return 1行をあらわすDTO
	 */
	public ArrayList<DeliveryResultInsDto> getResultDtoList() {
		return resultDtoList;
	}

	/**
	 * 参照期間リストを取得する。
	 *
	 * @return 参照期間リスト
	 */
	public List<Date> getRefDateList() {
		return refDateList;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 職種名を取得する
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}

    //add Start 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する
	/**
	 * 品目名称を取得する。
	 *
	 * @return 品目名称
	 */
	public String getProdName() {
		return prodName;
	}
	//add End 2022/5/11 H.Futagami バックログNo.13　過去実績参照で選択している品目を一覧に表示する

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
