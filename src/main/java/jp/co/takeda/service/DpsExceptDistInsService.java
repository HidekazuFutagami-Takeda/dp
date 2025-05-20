package jp.co.takeda.service;

import java.util.List;

import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dto.ExceptDistInsDeleteDto;
import jp.co.takeda.dto.ExceptDistInsEntryDto;
import jp.co.takeda.dto.ExceptDistInsUpdateDto;

/**
 * 配分除外施設の登録・更新に関するサービスインターフェイス
 * 
 * @author siwamoto
 */
public interface DpsExceptDistInsService {

	/**
	 * 配分除外施設を削除する
	 * 
	 * @param sosCd3 組織コード(営業所)
	 * @param deleteDtoList 配分除外施設の削除用DTO
	 */
	void deleteExceptDistIns(String sosCd3, List<ExceptDistInsDeleteDto> deleteDtoList);

	/**
	 * 配分除外施設を登録する
	 * 
	 * @param entryDto 配分除外施設の登録用DTO
	 * @throws LogicalException
	 */
	void entryExceptDistIns(ExceptDistInsEntryDto entryDto) throws LogicalException;

	/**
	 * 配分除外施設を更新する
	 * 
	 * @param updateDto 配分除外施設の更新用DTO
	 * @throws LogicalException
	 */
	void updateExceptDistIns(ExceptDistInsUpdateDto updateDto) throws LogicalException;

}
