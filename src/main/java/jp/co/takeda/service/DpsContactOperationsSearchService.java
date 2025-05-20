package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.dto.AnnounceDto;

/**
 * 業務連絡情報を取得するサービスインターフェイス
 * 
 * @author tkawabata
 */
public interface DpsContactOperationsSearchService {

	/**
	 * お知らせ情報を取得する。<br>
	 * 
	 * @param jgiNo 従業員番号
	 * @throws DataNotFoundException お知らせ情報が見つからない場合にスロー
	 */
	List<AnnounceDto> searchAnnounceList(Integer jgiNo) throws DataNotFoundException;

}
