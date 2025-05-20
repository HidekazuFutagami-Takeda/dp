package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpAsyncDto;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUser;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (ワクチン)配分処理パラメータクラス
 * 
 * @author khashimoto
 */
public class DistributionForVacParamsDto extends DpAsyncDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 実行者従業員情報
	 */
	private final DpUser dpUser;

	/**
	 * 更新前の(ワクチン)施設特約店別計画ステータスリスト
	 */
	private final List<DistributionForVacExecOrgDto> distributionForVacExecOrgDtoList;

	/**
	 * 配分処理と同時にMRに公開フラグ
	 * <ul>
	 * <li>TRUE =公開する</li>
	 * <li>FALSE=公開しない</li>
	 * </ul>
	 */
	private final boolean isMrOpenCheck;

	/**
	 * コンストラクタ
	 * 
	 * @param metaInfo メタ情報
	 * @param dpUser 実行者従業員情報
	 * @param isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 * @param distributionForVacExecOrgDtoList 更新前の(ワクチン)施設特約店別計画ステータスリスト
	 */
	public DistributionForVacParamsDto(DpMetaInfo metaInfo, DpUser dpUser, boolean isMrOpenCheck, List<DistributionForVacExecOrgDto> distributionForVacExecOrgDtoList) {
		super(metaInfo);
		this.dpUser = dpUser;
		this.isMrOpenCheck = isMrOpenCheck;
		this.distributionForVacExecOrgDtoList = distributionForVacExecOrgDtoList;
	}

	//---------------------
	// Getter & Setter
	// --------------------

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
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
	 * 配分処理と同時にMRに公開フラグを取得する。
	 * 
	 * @return isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public boolean isMrOpenCheck() {
		return isMrOpenCheck;
	}

	/**
	 * 更新前の施設特約店別計画ステータスリストを取得する。
	 * 
	 * @return 更新前の施設特約店別計画ステータスリスト
	 */
	public List<DistributionForVacExecOrgDto> getDistributionForVacExecOrgDtoList() {
		return distributionForVacExecOrgDtoList;
	}
}
