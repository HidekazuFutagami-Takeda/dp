package jp.co.takeda.dto;

import java.util.List;
import java.util.Map;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 調整金額テーブルの取得結果を表すＤＴＯクラス
 * 
 * @author tkawabata
 */
public class ChoseiDataResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 調整計画明細[全社]
	 */
	private final ChoseiDataResultDetailDto zensha;

	/**
	 * 調整計画明細リスト[支店]
	 */
	private final List<ChoseiDataResultDetailDto> sitenList;

	/**
	 * 調整計画明細リスト[営業所]マップ<br>
	 * (支店組織コード(5桁)をキーとする)
	 */
	private final Map<String, List<ChoseiDataResultDetailDto>> officeListMap;

	/**
	 * 調整計画明細リスト[チーム]マップ<br>
	 * (営業所組織コード(5桁)をキーとする)
	 */
	private final Map<String, List<ChoseiDataResultDetailDto>> teamListMap;

	/**
	 * 調整計画明細リスト[（チーム所属）担当者]マップ<br>
	 * (チーム組織コード(5桁)をキーとする)
	 */
	private final Map<String, List<ChoseiDataResultDetailDto>> sos4MrListMap;

	/**
	 * 調整計画明細リスト[（営業所所属）担当者]マップ<br>
	 * (営業所組織コード(5桁)をキーとする)
	 */
	private final Map<String, List<ChoseiDataResultDetailDto>> sos3MrListMap;

	/**
	 * コンストラクタ
	 * 
	 * @param zensha 調整計画明細[全社]
	 * @param sitenListMap 調整計画明細リスト[支店]
	 * @param officeListMap 調整計画明細リスト[営業所]マップ
	 * @param teamListMap 調整計画明細リスト[チーム]マップ
	 * @param sos4MrListMap 調整計画明細リスト[（チーム所属）担当者]マップ
	 * @param sos3MrListMap 調整計画明細リスト[（営業所所属）担当者]マップ
	 */
	public ChoseiDataResultDto(ChoseiDataResultDetailDto zensha, List<ChoseiDataResultDetailDto> sitenList, Map<String, List<ChoseiDataResultDetailDto>> officeListMap,
		Map<String, List<ChoseiDataResultDetailDto>> teamListMap, Map<String, List<ChoseiDataResultDetailDto>> sos4MrListMap, Map<String, List<ChoseiDataResultDetailDto>> sos3MrListMap) {
		this.zensha = zensha;
		this.sitenList = sitenList;
		this.officeListMap = officeListMap;
		this.teamListMap = teamListMap;
		this.sos4MrListMap =sos4MrListMap;
		this.sos3MrListMap =sos3MrListMap;
	}

	/**
	 * 調整計画明細[全社]を取得する。
	 * 
	 * @return 調整計画明細[全社]
	 */
	public ChoseiDataResultDetailDto getZensha() {
		return zensha;
	}

	/**
	 * 調整計画明細[支店]を取得する。
	 * 
	 * @return 調整計画明細[支店]
	 */
	public List<ChoseiDataResultDetailDto> getSitenList() {
		return sitenList;
	}

	/**
	 * 調整計画明細リスト[営業所]マップを取得する。
	 * 
	 * @return 調整計画明細リスト[営業所]マップ
	 */
	public Map<String, List<ChoseiDataResultDetailDto>> getOfficeListMap() {
		return officeListMap;
	}

	/**
	 * 調整計画明細リスト[チーム]マップを取得する。
	 * 
	 * @return 調整計画明細リスト[チーム]マップ
	 */
	public Map<String, List<ChoseiDataResultDetailDto>> getTeamListMap() {
		return teamListMap;
	}

	/**
	 * 調整計画明細リスト[担当者]マップを取得する。
	 * 
	 * @return 調整計画明細リスト[担当者]マップ
	 */
	public Map<String, List<ChoseiDataResultDetailDto>> getSos4MrListMap() {
		return sos4MrListMap;
	}

	/**
	 * 調整計画明細リスト[（営業所所属）担当者]マップを取得する。
	 * 
	 * @return 調整計画明細リスト[（営業所所属）担当者]マップ
	 */
	public Map<String, List<ChoseiDataResultDetailDto>> getSos3MrListMap() {
		return sos3MrListMap;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
