package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.PlannedProd;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 担当者別計画立案ステータスチェック用DTO
 * 
 * @author nozaki
 */
public class MrStatusCheckDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * チェック対象の組織コード(営業所)
	 */
	private final String sosCd;

	/**
	 * チェック対象の計画対象品目のリスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 許可しない担当者別計画立案ステータスのリスト
	 */
	private final List<MrStatusForCheck> mrStatusForCheckList;

	/**
	 * コンストラクタ
	 * 
	 * チェック対象の組織コード(営業所)、計画対象品目のリストを指定する。
	 * 
	 * @param sosCd チェック対象の組織コード(営業所)
	 * @param plannedProdList チェック対象の計画対象品目のリスト
	 * @param statusForOffice 許可しない担当者別計画立案ステータス
	 */
	public MrStatusCheckDto(String sosCd, List<PlannedProd> plannedProdList, List<MrStatusForCheck> mrStatusForCheckList) {

		this.sosCd = sosCd;
		this.plannedProdList = plannedProdList;
		this.mrStatusForCheckList = mrStatusForCheckList;
	}

	/**
	 * チェック対象の組織コード(営業所)を取得する。
	 * 
	 * @return チェック対象の組織コード(営業所)
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * チェック対象の計画対象品目のリストを取得する。
	 * 
	 * @return チェック対象の計画対象品目のリスト
	 */
	public List<PlannedProd> getPlannedProdList() {
		return plannedProdList;
	}

	/**
	 * 許可しない担当者別計画立案ステータスのリストを取得する。
	 * 
	 * @return 許可しない担当者別計画立案ステータスのリスト
	 */
	public List<MrStatusForCheck> getMrStatusForCheck() {
		return mrStatusForCheckList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
