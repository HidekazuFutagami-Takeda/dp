package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpAsyncDto;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUser;

/**
 * 配分処理パラメータクラス
 *
 * @author nozaki
 */
public class DistributionParamsDto extends DpAsyncDto {

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
	 * カテゴリ
	 */
	private final String category;

	/**
	 * 実行者従業員情報
	 */
	private final DpUser dpUser;

	/**
	 * 更新前の施設特約店別計画ステータスリスト
	 */
	private final List<DistributionExecOrgDto> distributionExecOrgDtoList;

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
	 * @param sosCd3 組織コード(営業所)
	 * @param category カテゴリ
	 * @param dpUser 実行者従業員情報
	 * @param distributionExecOrgDtoList 更新前の施設特約店別計画ステータスリスト
	 * @param isMrOpenCheck 配分処理と同時にMRに公開フラグ
	 */
	public DistributionParamsDto(DpMetaInfo metaInfo, String sosCd3, String category, DpUser dpUser,
		List<DistributionExecOrgDto> distributionExecOrgDtoList, boolean isMrOpenCheck) {
		super(metaInfo);
		this.sosCd3 = sosCd3;
		this.category = category;
		this.dpUser = dpUser;
		this.isMrOpenCheck = isMrOpenCheck; // 公開ステータス廃止によりfalse固定
		this.distributionExecOrgDtoList = distributionExecOrgDtoList;
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
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
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
	public List<DistributionExecOrgDto> getDistributionExecOrgDtoList() {
		return distributionExecOrgDtoList;
	}
}
