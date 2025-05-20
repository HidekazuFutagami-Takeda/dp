package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.view.JgiEachKindInfo;
import jp.co.takeda.model.view.SosEachKindInfo;

/**
 * 業務進捗表(医)[各種登録状況]を表すDTOクラス
 * 
 * @author tkawabata
 */
public class ProgressEachKindInfoDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 部門ランク
	 */
	private final BumonRank bumonRank;

	/**
	 * 組織別各種登録状況を格納したリスト
	 */
	private final List<SosEachKindInfo> sosEachKindInfoList;

	/**
	 * 担当者別各種登録状況を格納したリスト
	 */
	private final List<JgiEachKindInfo> jgiEachKindInfoList;

	/**
	 * コンストラクタ
	 * 
	 * @param bumonRank 部門ランク(NULL可)
	 * @param sosEachKindInfoList 組織別各種登録状況を格納したリスト(NULL可)
	 * @param jgiEachKindInfoList 担当者別各種登録状況を格納したリスト(NULL可)
	 */
	public ProgressEachKindInfoDto(BumonRank bumonRank, List<SosEachKindInfo> sosEachKindInfoList, List<JgiEachKindInfo> jgiEachKindInfoList) {
		this.bumonRank = bumonRank;
		this.sosEachKindInfoList = sosEachKindInfoList;
		this.jgiEachKindInfoList = jgiEachKindInfoList;
	}

	/**
	 * 部門ランクを取得する。
	 * 
	 * @return bumonRank 部門ランク
	 */
	public BumonRank getBumonRank() {
		return bumonRank;
	}

	/**
	 * 組織別各種登録状況を格納したリストを取得する。
	 * 
	 * @return sosEachKindInfoList 組織別各種登録状況を格納したリスト
	 */
	public List<SosEachKindInfo> getSosEachKindInfoList() {
		return sosEachKindInfoList;
	}

	/**
	 * 担当者別各種登録状況を格納したリストを取得する。
	 * 
	 * @return jgiEachKindInfoList 担当者別各種登録状況を格納したリスト
	 */
	public List<JgiEachKindInfo> getJgiEachKindInfoList() {
		return jgiEachKindInfoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
