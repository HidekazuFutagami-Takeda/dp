package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 施設別計画の検索結果 ヘッダ情報を表すDTO
 * 
 * @author stakeuchi
 */
public class InsPlanResultHeaderDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設情報 検索結果DTO
	 * 
	 * <pre>
	 * 検索時、施設コードが入力されいない場合はNull値となる。
	 * </pre>
	 */
	private final InsMstResultDto insMstResultDto;

	/**
	 * コンストラクタ
	 * 
	 * @param insMstResultDto 施設情報 検索結果DTO
	 */
	public InsPlanResultHeaderDto(InsMstResultDto insMstResultDto) {
		this.insMstResultDto = insMstResultDto;
	}

	/**
	 * 施設情報 検索結果DTOを取得する。
	 * 
	 * @return insMstResultDto 施設情報 検索結果DTO
	 */
	public InsMstResultDto getInsMstResultDto() {
		return insMstResultDto;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
