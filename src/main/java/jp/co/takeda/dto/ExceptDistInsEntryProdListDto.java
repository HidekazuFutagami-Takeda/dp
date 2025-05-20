package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.PlannedProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分除外施設の品目リストDTO
 * 
 * @author khashimoto
 */
public class ExceptDistInsEntryProdListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 配分除外施設フラグ(配分除外施設：true、配分除外施設品目：false)
	 */
	private final Boolean distInsFlg;

	/**
	 * 配分除外品目リスト
	 */
	private final List<ExceptProd> exceptProdList;

	/**
	 * 計画対象品目リスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * コンストラクタ<br>
	 * 配分除外施設の場合、使用する。
	 * 
	 * @param distInsFlg 配分除外施設フラグ(配分除外施設：true、配分除外施設品目：false)
	 * @param exceptProdList 配分除外品目リスト
	 * @param plannedProdList 計画対象品目リスト
	 */
	public ExceptDistInsEntryProdListDto(Boolean distInsFlg, List<ExceptProd> exceptProdList, List<PlannedProd> plannedProdList) {
		this.distInsFlg = distInsFlg;
		this.exceptProdList = exceptProdList;
		this.plannedProdList = plannedProdList;
	}

	/**
	 * 配分除外施設フラグ(配分除外施設：true、配分除外施設品目：false)を取得する。
	 * 
	 * @return sosCd3 配分除外施設フラグ(配分除外施設：true、配分除外施設品目：false)
	 */
	public Boolean getDistInsFlg() {
		return distInsFlg;
	}

	/**
	 * 配分除外品目リストを取得する。
	 * 
	 * @return insMrList 配分除外品目リスト
	 */
	public List<ExceptProd> getExceptProdList() {
		return exceptProdList;
	}

	/**
	 * 計画対象品目リストを取得する。
	 * 
	 * @return prodCodeList 計画対象品目リスト
	 */
	public List<PlannedProd> getPlannedProdList() {
		return plannedProdList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
