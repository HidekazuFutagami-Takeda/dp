package jp.co.takeda.dto;

import java.util.List;

import jp.co.takeda.bean.DpDto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 配分除外施設の検索結果用DTO
 * 
 * @author siwamoto
 */
public class ExceptDistInsResultListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理はDpsExceptDistInsSearchServiceImplに実装のため不要となる
//	/**
//	 * MMP品目数
//	 */
//	private final Integer countMMP;
//
//	/**
//	 * 仕入れ品目数
//	 */
//	private final Integer countSHIIRE;
//
//	/**
//	 * ONC品目数
//	 */
//	private final Integer countONC;
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理はDpsExceptDistInsSearchServiceImplに実装のため不要となる

	/**
	 * 配分除外施設検索結果のList
	 */
	private final List<ExceptDistInsResultDto> exceptDistInsDtoList;

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理はDpsExceptDistInsSearchServiceImplに実装のため不要となる
	/**
	 * コンストラクタ
	 * 
	 * @param exceptDistInsList 配分除外施設検索結果のList
	 */
//	public ExceptDistInsResultListDto(Integer countMMP, Integer countSHIIRE, Integer countONC, List<ExceptDistInsResultDto> exceptDistInsDtoList) {
	public ExceptDistInsResultListDto(List<ExceptDistInsResultDto> exceptDistInsDtoList) {
		super();
//		this.countMMP = countMMP;
//		this.countSHIIRE = countSHIIRE;
//		this.countONC = countONC;
		this.exceptDistInsDtoList = exceptDistInsDtoList;
	}
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理はDpsExceptDistInsSearchServiceImplに実装のため不要となる

// del start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理はDpsExceptDistInsSearchServiceImplに実装のため不要となる
//	/**
//	 * MMP品目数を取得する。
//	 * 
//	 * @return countMMP
//	 */
//	public Integer getCountMMP() {
//		return countMMP;
//	}
//
//	/**
//	 * 仕入れ品目数を取得する。
//	 * 
//	 * @return countSHIIRE
//	 */
//	public Integer getCountSHIIRE() {
//		return countSHIIRE;
//	}
//
//	/**
//	 * ONC品目数を取得する。
//	 * 
//	 * @return countONC
//	 */
//	public Integer getCountONC() {
//		return countONC;
//	}
// del end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　品目カウント関連の処理はDpsExceptDistInsSearchServiceImplに実装のため不要となる

	/**
	 * 配分除外施設検索結果のListを取得する。
	 * 
	 * @return exceptDistInsDtoList
	 */
	public List<ExceptDistInsResultDto> getExceptDistInsDtoList() {
		return exceptDistInsDtoList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
