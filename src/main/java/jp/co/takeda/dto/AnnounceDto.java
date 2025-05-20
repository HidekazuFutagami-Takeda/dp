package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.Announce;
import jp.co.takeda.model.OutputFile;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * お知らせ情報を表すDTO
 * 
 * @author tkawabata
 */
public class AnnounceDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * お知らせ情報
	 */
	protected final Announce announce;

	/**
	 * 出力ファイル情報
	 */
	protected final OutputFile outputFile;

	/**
	 * コンストラクタ
	 * 
	 * @param announce お知らせ情報
	 * @param outputFile 添付ファイル(null可)
	 */
	public AnnounceDto(final Announce announce, final OutputFile outputFile) {
		this.announce = announce;
		this.outputFile = outputFile;
	}

	/**
	 * お知らせ情報を取得する。
	 * 
	 * @return お知らせ情報
	 */
	public Announce getAnnounce() {
		return announce;
	}

	/**
	 * 出力ファイル情報を取得する。
	 * 
	 * @return 出力ファイル情報
	 */
	public OutputFile getOutputFile() {
		return outputFile;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
