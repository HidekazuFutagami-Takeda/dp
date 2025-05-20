// del 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　未使用となるため全コメントアウト。カテゴリ個別ものを親クラスのProgressPlanStatusDtoに統一。
//package jp.co.takeda.dto;
//
//import java.util.List;
//
//import jp.co.takeda.model.div.BumonRank;
//import jp.co.takeda.model.view.JgiStatusSummary;
//import jp.co.takeda.model.view.SosStatusSummary;
//
//import org.apache.commons.lang.builder.ReflectionToStringBuilder;
//
///**
// * 業務進捗表(医)[営業所計画-施設特約店別計画(仕入)]を表すDTOクラス
// *
// * @author tkawabata
// */
//public class ProgressPlanShireStatusDto extends ProgressPlanStatusDto {
//
//	/**
//	 * serialVersionUID
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * コンストラクタ
//	 *
//	 * @param bumonRank 部門ランク(NULL可)
//	 * @param sosStatusSummaryList 組織単位のステータスのサマリリスト(NULL可)
//	 * @param jgiStatusSummaryList 担当者単位のステータスのサマリリスト(NULL可)
//	 */
//	public ProgressPlanShireStatusDto(BumonRank bumonRank, List<SosStatusSummary> sosStatusSummaryList, List<JgiStatusSummary> jgiStatusSummaryList) {
//		super(bumonRank, sosStatusSummaryList, jgiStatusSummaryList);
//	}
//
//	@Override
//	public String toString() {
//		return new ReflectionToStringBuilder(this).toString();
//	}
//}
