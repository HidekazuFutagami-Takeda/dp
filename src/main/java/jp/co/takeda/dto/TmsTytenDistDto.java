package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 * 特約店別計画配分DTO
 * 
 * @author yokokawa
 */
public class TmsTytenDistDto extends DpDto {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// -------------------------------
	// constructor
	// -------------------------------
	/**
	 * コンストラクタ
	 * 
	 * @param missListFileId 配分ミスリストファイルID
	 * @param errorPlannedProdList エラー品目リスト
	 */
	public TmsTytenDistDto(Long missListFileId, List<String> errorProdNameList) {
		this.missListFileId = missListFileId;
		this.errorProdNameList = errorProdNameList;
	}

	// -------------------------------
	// field
	// -------------------------------
	/**
	 * 配分ミスリストファイルID
	 */
	private Long missListFileId;

	/**
	 * エラー品目リスト
	 */
	private List<String> errorProdNameList;

	// -------------------------------
	// getter
	// -------------------------------
	/**
	 * 配分ミスリストファイルIDを取得する
	 * 
	 * @return 配分ミスリストファイルID
	 */
	public Long getMissListFileId() {
		return missListFileId;
	}

	/**
	 * エラー品目リストを取得する
	 * 
	 * @return エラー品目リスト
	 */
	public List<String> getErrorProdNameList() {
		return errorProdNameList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
