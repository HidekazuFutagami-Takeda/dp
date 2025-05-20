package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ManageInsPlanForVac;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画の検索結果 明細行を表すDTO
 * 
 * @author stakeuchi
 */
public class InsPlanForVacResultDetailAddrDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 都道府県
	 */
	private final Prefecture addrCodePref;

	/**
	 * JIS市区町村コード
	 */
	private final String addrCodeCity;

	/**
	 * 市区郡町村名（漢字）
	 */
	private final String shikuchosonMeiKj;

	/**
	 * 施設別計画のリスト
	 */
	private final List<InsPlanForVacResultDetailInsDto> InsPlanList;

	/**
	 * コンストラクタ
	 * 
	 * @param insPlan ワクチン用施設別計画
	 * @param InsPlanList 明細行を表すDTO
	 */
	public InsPlanForVacResultDetailAddrDto(ManageInsPlanForVac insPlan, List<InsPlanForVacResultDetailInsDto> InsPlanList) {
		this.addrCodePref = insPlan.getAddrCodePref();
		this.addrCodeCity = insPlan.getAddrCodeCity();
		this.shikuchosonMeiKj = insPlan.getShikuchosonMeiKj();
		this.InsPlanList = InsPlanList;
	}

	/**
	 * 都道府県を取得する。
	 * 
	 * @return 都道府県
	 */
	public Prefecture getAddrCodePref() {
		return addrCodePref;
	}

	/**
	 * JIS市区町村コードを取得する。
	 * 
	 * @return JIS市区町村コード
	 */
	public String getAddrCodeCity() {
		return addrCodeCity;
	}

	/**
	 * 市区郡町村名（漢字）を取得する。
	 * 
	 * @return 市区郡町村名（漢字）
	 */
	public String getShikuchosonMeiKj() {
		return shikuchosonMeiKj;
	}

	/**
	 * 施設別計画のリストを取得する。
	 * 
	 * @return insPlanList 施設別計画のリスト
	 */
	public List<InsPlanForVacResultDetailInsDto> getInsPlanList() {
		return InsPlanList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
