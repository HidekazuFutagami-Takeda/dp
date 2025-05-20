package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.logic.div.ContactResultType;
import jp.co.takeda.security.DpUser;

/**
 * 業務連絡の登録用DTO
 *
 * @author khashimoto
 */
public class ContactOperationsEntryDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 実行対象の組織コード(営業所/支店)
	 */
	private final String sosCd;

	/**
	 * 実行者情報
	 */
	private final DpUser dpUser;

	/**
	 * 業務連絡の処理区分
	 */
	private final ContactOperationsType contactOperationsType;

	/**
	 * 業務連絡の処理結果区分
	 */
	private final ContactResultType contactResultType;

	/**
	 * 処理開始時間
	 */
	private final Date startTime;

	/**
	 * 処理終了時間
	 */
	private final Date endTime;

	/**
	 * 品目名称リスト
	 */
	private final List<String> prodNameList;

	/**
	 * 出力ファイルID
	 */
	private final Long outputFileId;

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する
	/**
	 * カテゴリー
	 */
	private final String category;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する

	/**
	 * コンストラクタ(処理失敗時)
	 *
	 * @param sosCd 実行対象の組織コード(営業所/支店)
	 * @param dpUser 実行者情報
	 * @param contactOperationsType 業務連絡の処理区分
	 * @param contactResultType 業務連絡の処理結果区分
	 * @param startTime 処理開始時間
	 * @param endTime 処理終了時間
	 * @param prodNameList 品目名称リスト
	 * @param outputFileId 出力ファイルID
	 */
	public ContactOperationsEntryDto(String sosCd, DpUser dpUser, ContactOperationsType contactOperationsType, ContactResultType contactResultType, Date startTime, Date endTime,
		List<String> prodNameList, Long outputFileId) {
		this.sosCd = sosCd;
		this.dpUser = dpUser;
		this.contactOperationsType = contactOperationsType;
		this.contactResultType = contactResultType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.prodNameList = prodNameList;
		this.outputFileId = outputFileId;
		this.category = null; //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する。　配分処理以外ではnullとしておく。
	}

	/**
	 * コンストラクタ(処理成功時)
	 *
	 * @param sosCd 実行対象の組織コード(営業所/支店)
	 * @param dpUser 実行者情報
	 * @param contactOperationsType 業務連絡の処理区分
	 * @param startTime 処理開始時間
	 * @param endTime 処理終了時間
	 */
	public ContactOperationsEntryDto(String sosCd, DpUser dpUser, ContactOperationsType contactOperationsType, Date startTime, Date endTime) {
		this.sosCd = sosCd;
		this.dpUser = dpUser;
		this.contactOperationsType = contactOperationsType;
		this.contactResultType = ContactResultType.SUCCESS;
		this.startTime = startTime;
		this.endTime = endTime;
		this.prodNameList = null;
		this.outputFileId = null;
		this.category = null; //add 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する。　配分処理以外ではnullとしておく。
	}

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する
	/**
	 * コンストラクタ(処理失敗時)　※配分処理用
	 *
	 * @param sosCd 実行対象の組織コード(営業所/支店)
	 * @param dpUser 実行者情報
	 * @param contactOperationsType 業務連絡の処理区分
	 * @param contactResultType 業務連絡の処理結果区分
	 * @param startTime 処理開始時間
	 * @param endTime 処理終了時間
	 * @param prodNameList 品目名称リスト
	 * @param outputFileId 出力ファイルID
	 * @param category カテゴリー
	 */
	public ContactOperationsEntryDto(String sosCd, DpUser dpUser, ContactOperationsType contactOperationsType, ContactResultType contactResultType, Date startTime, Date endTime,
		List<String> prodNameList, Long outputFileId, String category) {
		this.sosCd = sosCd;
		this.dpUser = dpUser;
		this.contactOperationsType = contactOperationsType;
		this.contactResultType = contactResultType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.prodNameList = prodNameList;
		this.outputFileId = outputFileId;
		this.category = category;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する

	/**
	 * 実行対象の組織コード(営業所/支店)を取得する。
	 *
	 * @return 実行対象の組織コード(営業所/支店)
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 実行者情報を取得する。
	 *
	 * @return 実行者情報
	 */
	public DpUser getDpUser() {
		return dpUser;
	}

	/**
	 * 業務連絡の処理区分を取得する。
	 *
	 * @return 業務連絡の処理区分
	 */
	public ContactOperationsType getContactOperationsType() {
		return contactOperationsType;
	}

	/**
	 * 業務連絡の処理結果区分を取得する。
	 *
	 * @return 業務連絡の処理結果区分
	 */
	public ContactResultType getContactResultType() {
		return contactResultType;
	}

	/**
	 * 処理開始時間を取得する。
	 *
	 * @return 処理開始時間
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 処理終了時間を取得する。
	 *
	 * @return 処理終了時間
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 品目名称リストを取得する。
	 *
	 * @return 品目名称リスト
	 */
	public List<String> getProdNameList() {
		return prodNameList;
	}

	/**
	 * 出力ファイルIDを取得する。
	 *
	 * @return 出力ファイルID
	 */
	public Long getOutputFileId() {
		return outputFileId;
	}

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する
	/**
	 * カテゴリーを取得する。
	 *
	 * @return カテゴリー
	 */
	public String getCategory() {
		return category;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　配分処理の"お知らせ"内容の"カテゴリ名"のべた書き回避のため、カテゴリ名も保持する

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
