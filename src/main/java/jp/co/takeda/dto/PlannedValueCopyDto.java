package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.CopyTarget;
import jp.co.takeda.logic.div.CopyWay;
import jp.co.takeda.model.PlannedProd;

/**
 * 計画値の一括コピー処理実行用DTO
 *
 * @author khashimoto
 */
public class PlannedValueCopyDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード（営業所）
	 */
	private final String sosCd3;

	/**
	 * 品目リスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * コピー対象
	 */
	private final CopyTarget copyTarget;

	/**
	 * コピー方法
	 */
	private final CopyWay copyWay;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param sosCd3 組織コード（営業所）
	 * @param plannedProdList 品目リスト
	 * @param copyTarget コピー対象
	 * @param copyWay コピー方法
	 */
	public PlannedValueCopyDto(String sosCd3, List<PlannedProd> plannedProdList, CopyTarget copyTarget, CopyWay copyWay, String category) {
		this.sosCd3 = sosCd3;
		this.plannedProdList = plannedProdList;
		this.copyTarget = copyTarget;
		this.copyWay = copyWay;
		this.category = category;
	}

	/**
	 * 組織コード（営業所）を取得する。
	 *
	 * @return 組織コード（営業所）
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 品目リストを取得する。
	 *
	 * @return 品目リスト
	 */
	public List<PlannedProd> getPlannedProdList() {
		return plannedProdList;
	}

	/**
	 * コピー対象を取得する。
	 *
	 * @return コピー対象
	 */
	public CopyTarget getCopyTarget() {
		return copyTarget;
	}

	/**
	 * コピー方法を取得する。
	 *
	 * @return コピー方法
	 */
	public CopyWay getCopyWay() {
		return copyWay;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
