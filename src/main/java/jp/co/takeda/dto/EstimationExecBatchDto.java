package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlanStatus;
import jp.co.takeda.security.DpUser;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 試算実行（非同期処理）用DTO
 * 
 * @author khashimoto
 */
public class EstimationExecBatchDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd3;

	/**
	 * 実行者従業員情報
	 */
	private final DpUser dpUser;

	/**
	 * 更新前の担当者別計画ステータスリスト
	 */
	private final List<MrPlanStatus> orgMrPlanStatusList;

	/**
	 * 
	 * コンストラクタ
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param dpUser 実行者従業員情報
	 * @param orgMrPlanStatusList 更新前の担当者別計画ステータスリスト
	 */
	public EstimationExecBatchDto(String sosCd3, DpUser dpUser, List<MrPlanStatus> orgMrPlanStatusList) {
		this.sosCd3 = sosCd3;
		this.dpUser = dpUser;
		this.orgMrPlanStatusList = orgMrPlanStatusList;
	}

	//---------------------
	// Getter & Setter
	// --------------------

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * 組織コード(営業所)を取得する。
	 * 
	 * @return 組織コード(営業所)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 実行者従業員情報を取得する。
	 * 
	 * @return 実行者従業員情報
	 */
	public DpUser getDpUser() {
		return dpUser;
	}

	/**
	 * 更新前の担当者別計画ステータスリストを取得する。
	 * 
	 * @return 更新前の担当者別計画ステータスリスト
	 */
	public List<MrPlanStatus> getOrgMrPlanStatusList() {
		return orgMrPlanStatusList;
	}
}
