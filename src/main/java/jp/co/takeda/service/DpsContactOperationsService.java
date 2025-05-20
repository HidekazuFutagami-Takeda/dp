package jp.co.takeda.service;

import java.util.Date;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ContactOperationsEntryDto;

/**
 * 業務連絡に関するサービスインターフェイス
 * 
 * @author khashimoto
 */
public interface DpsContactOperationsService {

	/**
	 * 業務連絡(お知らせ)を登録する。<br>
	 * 
	 * @param entryDto 業務連絡の登録用DTO
	 * @throws LogicalException
	 */
	void entryAnnounce(ContactOperationsEntryDto entryDto) throws LogicalException;

	/**
	 * 業務連絡(アテンション)を登録する。<br>
	 * 
	 * @param entryDto 業務連絡の登録用DTO
	 * @throws LogicalException
	 */
	void entryAtt(ContactOperationsEntryDto entryDto) throws LogicalException;

	/**
	 * お知らせ情報を削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 最終更新日
	 */
	void deleteAnnounce(Long seqKey, Date upDate);

}
