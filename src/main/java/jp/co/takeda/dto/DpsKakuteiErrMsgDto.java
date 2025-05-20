package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DpsKakuteiErrMsg;

/**
 * 一括確定エラー情報の検索結果用DTO
 *
 * @author khashimoto
 */
public class DpsKakuteiErrMsgDto extends DpDto {

	/**
	*リュージョンコード
	*/
	private final String sosCd2;

	/**
	*リュージョン名
	*/
	private final String bumonRyakuName2;

	/**
	*エリアコード
	*/
	private final String sosCd3;

	/**
	*エリア名
	*/
	private final String bumonRyakuName3;

	/**
	*従業員番号
	*/
	private final Integer jgiNo;

	/**
	*従業員名前
	*/
	private final String jgiName;

	/**
	*エラーメッセージ
	*/
	private final String errMessage;

	/**
	*品目メッセージ
	*/
	private final String prodMessage;

	/**
	*一括確定エラーメッセージリスト
	*/
	private final List<DpsKakuteiErrMsg>  errMsgList;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param sosCd2 リュージョンコード
	 * @param bumonRyakuName2 リュージョン名
	 * @param sosCd3 エリアコード
	 * @param bumonRyakuName3 エリア名
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名前
	 * @param errMessage エラーメッセージ
	 * @param prodMessage 品目メッセージ
	 * @param List<DpsKakuteiErrMsg> errMsgList 一括確定エラーメッセージリスト
	 */
	public DpsKakuteiErrMsgDto(String sosCd2, String bumonRyakuName2, String sosCd3, String bumonRyakuName3,
			Integer jgiNo,  String jgiName, String errMessage, String prodMessage,
			List<DpsKakuteiErrMsg> errMsgList) {
		this.sosCd2 = sosCd2;
		this.bumonRyakuName2 = bumonRyakuName2;
		this.sosCd3 = sosCd3;
		this.bumonRyakuName3 = bumonRyakuName3;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.errMessage = errMessage;
		this.prodMessage = prodMessage;
		this.errMsgList = errMsgList;

	}

	/**
	*リュージョンコードを取得する
	*/
	public String getSosCd2() {
	return sosCd2;
	}

	/**
	*リュージョン名を取得する
	*/
	public String getBumonRyakuName2() {
	return bumonRyakuName2;
	}

	/**
	*エリアコードを取得する
	*/
	public String getSosCd3() {
		return sosCd3;
	}


	/**
	*エリア名を取得する
	*/
	public String getBumonRyakuName3() {
		return bumonRyakuName3;
	}


	/**
	*従業員番号を取得する
	*/
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	*従業員名前を取得する
	*/
	public String getJgiName() {
		return jgiName;
	}

	/**
	*エラーメッセージを取得する
	*/
	public String getErrMessage() {
		return errMessage;
	}

	/**
	*品目メッセージを取得する
	*/
	public String getProdMessage() {
		return prodMessage;
	}

	/**
	*一括確定エラーメッセージリストを取得する
	*/
	public List<DpsKakuteiErrMsg> getDpsKakuteiErrMsg(){
		return errMsgList;
	}


	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
