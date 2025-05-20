package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ExceptProd;

/**
 * 配分除外施設の登録用DTO
 *
 * @author khashimoto
 */
public class ExceptDistInsEntryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 施設/従業員情報のリスト
	 */
	private final List<InsMrDto> insMrList;

	/**
	 * 品目固定コードのリスト
	 */
	private final List<String> prodCodeList;

	/**
	 * 結果保持用DtoList
	 */
	private  List<ExceptProd> resultDtoList;

	/**
	 *
	 * コンストラクタ<br>
	 * 配分除外施設の場合、使用する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param insMrList 施設/従業員情報のリスト
	 */
	public ExceptDistInsEntryDto(String sosCd3, List<InsMrDto> insMrList) {
		this.sosCd3 = sosCd3;
		this.insMrList = insMrList;
		this.prodCodeList = null;
		this.resultDtoList = null;
	}

	/**
	 * コンストラクタ<br>
	 * 配分除外施設品目の場合、使用する。
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param insMrList 施設/従業員情報のリスト
	 * @param prodCodeList 品目固定コードのリスト
	 */
	public ExceptDistInsEntryDto(String sosCd3, List<InsMrDto> insMrList, List<ExceptProd> resultDtoList) {
		this.sosCd3 = sosCd3;
		this.insMrList = insMrList;
		this.prodCodeList = null;
		this.resultDtoList = resultDtoList;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return sosCd3 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 施設/従業員情報のリストのリストを取得する。
	 *
	 * @return insMrList 施設/従業員情報のリスト
	 */
	public List<InsMrDto> getInsMrList() {
		return insMrList;
	}

	/**
	 * 品目コードのリストを取得する。
	 *
	 * @return prodCodeList 品目コードのリスト
	 */
	public List<String> getProdCodeList() {
		return prodCodeList;
	}


	/**
	 * 結果リスト
	 * @return resultDtoList
	 */
	public List<ExceptProd> getResultDtoList() {
		return resultDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
