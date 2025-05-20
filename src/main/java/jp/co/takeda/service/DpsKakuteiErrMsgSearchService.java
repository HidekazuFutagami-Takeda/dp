package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.DpsKakuteiErrMsgDto;

/**
 * 一括確定エラー情報サービスインターフェイス
 *
 */
public interface DpsKakuteiErrMsgSearchService {

	/**
	 * 一括確定エラー情報を取得する。
	 *
	 * @return 一括確定エラー情報
	 */
	public List<DpsKakuteiErrMsgDto> search() throws LogicalException;

	/**
	 * 一括確定エラー情報を登録する。
	 *
	 */
	public void insert(String sosCd, Integer jgiNo, String prodCode, String errMsg);

	/**
	 * 一括確定エラー情報を削除する。
	 *
	 */
	public void delete();
}
