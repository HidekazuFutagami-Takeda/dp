package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.view.JgiEachKindInfoForVac;
import jp.co.takeda.model.view.SosEachKindInfoForVac;

/**
 * 業務進捗表(ワ)[各種登録状況]を表すDTOクラス
 * 
 * @author tkawabata
 */
public class ProgressEachKindInfoForVacDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織別各種登録状況
	 */
	private final SosEachKindInfoForVac sosEachKindInfoForVac;

	/**
	 * 担当者別各種登録状況
	 */
	private final List<JgiEachKindInfoForVac> jgiEachKindInfoForVac;

	/**
	 * コンストラクタ
	 * 
	 * @param sosEachKindInfoForVac 組織別各種登録状況(NULL可)
	 * @param jgiEachKindInfoForVac 担当者別各種登録状況(NULL可)
	 */
	public ProgressEachKindInfoForVacDto(SosEachKindInfoForVac sosEachKindInfoForVac, List<JgiEachKindInfoForVac> jgiEachKindInfoForVac) {
		this.sosEachKindInfoForVac = sosEachKindInfoForVac;
		this.jgiEachKindInfoForVac = jgiEachKindInfoForVac;
	}

	/**
	 * 組織別各種登録状況を取得する。
	 * 
	 * @return sosEachKindInfoForVac 組織別各種登録状況
	 */
	public SosEachKindInfoForVac getSosEachKindInfoForVac() {
		return sosEachKindInfoForVac;
	}

	/**
	 * 担当者別各種登録状況を取得する。
	 * 
	 * @return jgiEachKindInfoForVac 担当者別各種登録状況
	 */
	public List<JgiEachKindInfoForVac> getJgiEachKindInfoForVac() {
		return jgiEachKindInfoForVac;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
