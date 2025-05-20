package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * ステータスチェック結果用DTO
 * 
 * @author nozaki
 */
public class StatusCheckResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * エラーメッセージキーのリスト
	 */
	private final List<MessageKey> errorMessageKeyList;

	/**
	 * チェック結果 (true：エラーあり、false：エラーなし)
	 */
	private final boolean error;

	/**
	 * コンストラクタ
	 * 
	 * @param error チェック結果(true：エラーあり、false：エラーなし)
	 * @param errorMessageKeyList errorMessageKeyList
	 */
	public StatusCheckResultDto(List<MessageKey> errorMessageKeyList) {
		this.error = true;
		this.errorMessageKeyList = errorMessageKeyList;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param errorMessageKeyList errorMessageKeyList
	 */
	public StatusCheckResultDto() {

		this.error = false;
		this.errorMessageKeyList = null;
	}

	/**
	 * エラーメッセージキーのリストを取得。
	 * 
	 * @return エラーメッセージキーのリスト
	 */
	public List<MessageKey> getErrorMessageKeyList() {
		return errorMessageKeyList;
	}

	/**
	 * チェック結果を取得。
	 * <ul>
	 * <li>true：エラーあり</li>
	 * <li>false：エラーなし</li>
	 * <ul>
	 * 
	 * @return チェック結果(true：エラーあり、false：エラーなし)
	 */
	public boolean isError() {
		return error;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
