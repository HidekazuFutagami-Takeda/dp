package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.InsType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * (医)施設特約店別計画編集画面を表すDTO
 * 
 * @author siwamoto
 */
public class ManageInsWsPlanHeaderDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設情報 検索結果DTO<br>
	 * 施設コードが入力されいない場合はNULLとなる。
	 */
	private final InsMstResultDto insMstResultDto;

	/**
	 * 対象施設担当従業員<br>
	 * 施設コードが入力されいない場合はNULLとなる。
	 */
	private final JgiMst targetMR;

	/**
	 * 特約店コード
	 */
	private final String tmsTytenCd;

	/**
	 * 特約店名称
	 */
	private final String tmsTytenName;

	/**
	 * 対象区分
	 */
	private final InsType insType;

	/**
	 * コンストラクタ
	 * 
	 * @param InsMstResultDto 施設情報
	 * @param tmsTytenCd 特約店コード
	 * @param tmsTytenName 特約店名称
	 * @param insType 対象区分
	 * @param targetMR 対象施設担当従業員
	 */
	public ManageInsWsPlanHeaderDto(InsMstResultDto insMstResultDto, String tmsTytenCd, String tmsTytenName, InsType insType, JgiMst targetMR) {
		this.insMstResultDto = insMstResultDto;
		this.tmsTytenCd = tmsTytenCd;
		this.tmsTytenName = tmsTytenName;
		this.insType = insType;
		this.targetMR = targetMR;
	}

	/**
	 * 施設情報を取得する。
	 * 
	 * @return insMstResultDto 施設情報
	 */
	public InsMstResultDto getInsMstResultDto() {
		return insMstResultDto;
	}

	/**
	 * 特約店コードを取得する。
	 * 
	 * @return tmsTytenCd 特約店コード
	 */
	public String getTmsTytenCd() {
		return tmsTytenCd;
	}

	/**
	 * 特約店名称を取得する。
	 * 
	 * @return tmsTytenName 特約店名称
	 */
	public String getTmsTytenName() {
		return tmsTytenName;
	}

	/**
	 * 対象区分を取得する。
	 * 
	 * @return 対象区分
	 */
	public InsType getInsType() {
		return insType;
	}

	/**
	 * 対象施設担当従業員を取得する。
	 * 
	 * @return 対象施設担当従業員
	 */
	public JgiMst getTargetMR() {
		return targetMR;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
