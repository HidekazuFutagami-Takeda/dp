package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import jp.co.takeda.bean.DpAsyncDto;
import jp.co.takeda.security.DpMetaInfo;
import jp.co.takeda.security.DpUser;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 医師別計画配分処理パラメータクラス
 * 
 * @author nozaki
 */
public class DocDistributionParamsDto extends DpAsyncDto {

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
	 * ステータス最終更新日時
	 */
	private final Date statusLastUpdates;

	/**
	 * 対象従業員リスト
	 */
	private final List<DocDistributionJgiDto> jgiList; 

	/**
	 * メイン配分（営業所指定） or 再配分（担当者指定）
	 */
	private final boolean isMainHaibun; 
	
	/**
	 * コンストラクタ
	 * 
	 * @param metaInfo メタ情報
	 * @param sosCd3 組織コード(営業所)
	 * @param dpUser 実行者従業員情報
	 * @param statusLastUpdates ステータス最終更新日時
	 * @param jgiList 対象従業員リスト
	 * @param isMainHaibun メイン配分（営業所指定）か再配分（担当者指定）か
	 */
	public DocDistributionParamsDto(DpMetaInfo metaInfo, String sosCd3, DpUser dpUser,Date statusLastUpdates, List<DocDistributionJgiDto> jgiList, boolean isMainHaibun) {
		super(metaInfo);
		this.sosCd3 = sosCd3;
		this.dpUser = dpUser;
		this.statusLastUpdates = statusLastUpdates;
		this.jgiList = jgiList;
		this.isMainHaibun = isMainHaibun;
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
	 * ステータス最終更新日時を取得する。
	 * 
	 * @return ステータス最終更新日時
	 */
	public Date getStatusLastUpdates() {
		return statusLastUpdates;
	}

	/**
	 * 対象従業員リストを取得する。
	 * 
	 * @return 対象従業員リスト
	 */
	public List<DocDistributionJgiDto> getJgiList() {
		return jgiList;
	}

	/**
	 * メイン配分（営業所指定） or 再配分（担当者指定）
	 * 
	 * @return true：メイン配分
	 */
	public boolean isMainHaibun() {
		return isMainHaibun;
	}
}
