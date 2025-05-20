package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.Prefecture;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設特約店別計画の検索結果 明細行を表すDTO
 * 
 * @author siwamoto
 */
public class ManageInsWsPlanForVacDetailAddrDto extends DpDto {

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
	private final List<ManageInsWsPlanForVacDetailInsDto> insWsPlanList;

	/**
	 * コンストラクタ
	 * 
	 * @param addrCodePref 都道府県
	 * @param addrCodeCity IS市区町村コード
	 * @param shikuchosonMeiKj 市区郡町村名（漢字）
	 * @param InsPlanList 施設DTOのリスト
	 */
	public ManageInsWsPlanForVacDetailAddrDto(Prefecture addrCodePref, String addrCodeCity, String shikuchosonMeiKj, List<ManageInsWsPlanForVacDetailInsDto> insWsPlanList) {
		this.addrCodePref = addrCodePref;
		this.addrCodeCity = addrCodeCity;
		this.shikuchosonMeiKj = shikuchosonMeiKj;
		this.insWsPlanList = insWsPlanList;
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
	public List<ManageInsWsPlanForVacDetailInsDto> getInsWsPlanList() {
		return insWsPlanList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
