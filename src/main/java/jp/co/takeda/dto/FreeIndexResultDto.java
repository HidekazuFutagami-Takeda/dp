package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.EstimationParam;
import jp.co.takeda.model.ProdInfo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算パラメータ検索結果用DTO
 * 
 * @author nozaki
 */
public class FreeIndexResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 立案対象品目名称
	 */
	private final String prodName;

	/**
	 * 立案対象品目固定コード
	 */
	private final String prodCode;

	/**
	 * 立案対象品目の製品区分
	 */
	private final String prodType;

	/**
	 * 試算品目情報
	 */
	private final ProdInfo refProdInfo;

	/**
	 * 試算パラメータ情報
	 */
	private final EstimationParam estimationParam;

	/**
	 * フリー項目最終更新日
	 */
	private final Date lastUpdate;

	/**
	 * フリー項目最終更新者
	 */
	private final String lastUpdateUser;

	/**
	 * フリー項目検索結果詳細DTOのリスト(チーム単位情報)
	 */
	private final List<FreeIndexResultDetailDto> detailDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param prodCode 立案対象品目固定コード
	 * @param prodName 立案対象品目名称
	 * @param prodType 立案対象品目の製品区分
	 * @param refProdName 試算品目情報
	 * @param estimationParam 試算パラメータ情報
	 * @param lastUpdate フリー項目最終更新日
	 * @param detailDtoList フリー項目検索結果詳細DTOのリスト
	 */
	public FreeIndexResultDto(String prodCode, String prodName, String prodType, ProdInfo refProdInfo, EstimationParam estimationParam, Date lastUpdate, String lastUpdateUser,
		List<FreeIndexResultDetailDto> detailDtoList) {

		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodType = prodType;
		this.refProdInfo = refProdInfo;
		this.estimationParam = estimationParam;
		this.lastUpdate = lastUpdate;
		this.lastUpdateUser = lastUpdateUser;
		this.detailDtoList = detailDtoList;
	}

	/**
	 * 立案対象品目固定コードを取得する。
	 * 
	 * @return 立案対象品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 立案対象品目名称を取得する。
	 * 
	 * @return 立案対象品目名称
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 立案対象品目の製品区分を取得する。
	 * 
	 * @return 立案対象品目の製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 試算品目情報を取得する。
	 * 
	 * @return 試算品目情報
	 */
	public ProdInfo getRefProdInfo() {
		return refProdInfo;
	}

	/**
	 * 試算パラメータ情報を取得する。
	 * 
	 * @return 試算パラメータ情報
	 */
	public EstimationParam getEstimationParam() {
		return estimationParam;
	}

	/**
	 * フリー項目最終更新日を取得する。
	 * 
	 * @return フリー項目最終更新日
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * フリー項目最終更新者を取得する。
	 * 
	 * @return フリー項目最終更新者
	 */
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * フリー項目検索結果詳細DTOのリストを取得する。
	 * 
	 * @return フリー項目検索結果詳細DTOのリスト
	 */
	public List<FreeIndexResultDetailDto> getDetailDtoList() {
		return detailDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
