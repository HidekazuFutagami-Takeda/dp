package jp.co.takeda.dto;

import java.util.Date;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MrPlanFreeIndex;
import jp.co.takeda.util.ConvertUtil;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * フリー項目　検索、更新用DTO
 *
 * @author nozaki
 */
public class FreeIndexDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * シーケンスキー
	 */
	private final Long seqKey;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * 職種名
	 */
	private final String shokushuName;

	/**
	 * 品目固定コード
	 */
	private final String prodCode;

	/**
	 * フリー項目値UH1
	 */
	private final Long indexFreeValueUh1;

	/**
	 * フリー項目値UH2
	 */
	private final Long indexFreeValueUh2;

	/**
	 * フリー項目値UH3
	 */
	private final Long indexFreeValueUh3;

	/**
	 * フリー項目値P1
	 */
	private final Long indexFreeValueP1;

	/**
	 * フリー項目値P2
	 */
	private final Long indexFreeValueP2;

	/**
	 * フリー項目値P3
	 */
	private final Long indexFreeValueP3;

	/**
	 * 更新日
	 */
	private final Date upDate;

	/**
	 * コンストラクタ
	 *
	 * @param seqKey シーケンスキー
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param prodCode 品目固定コード
	 * @param indexFreeValueUh1 フリー項目UH1
	 * @param indexFreeValueUh2 フリー項目UH2
	 * @param indexFreeValueUh3 フリー項目UH3
	 * @param indexFreeValueP1 フリー項目P1
	 * @param indexFreeValueP2 フリー項目P2
	 * @param indexFreeValueP3 フリー項目P3
	 * @param shokushuName 職種名
	 * @param upDate
	 */
	public FreeIndexDto(Long seqKey, Integer jgiNo, String jgiName, String prodCode, Long indexFreeValueUh1, Long indexFreeValueUh2, Long indexFreeValueUh3, Long indexFreeValueP1,
		Long indexFreeValueP2, Long indexFreeValueP3, Date upDate, String shokushuName) {

		this.seqKey = seqKey;
		this.jgiNo = jgiNo;
		this.prodCode = prodCode;
		this.indexFreeValueUh1 = indexFreeValueUh1;
		this.indexFreeValueUh2 = indexFreeValueUh2;
		this.indexFreeValueUh3 = indexFreeValueUh3;
		this.indexFreeValueP1 = indexFreeValueP1;
		this.indexFreeValueP2 = indexFreeValueP2;
		this.indexFreeValueP3 = indexFreeValueP3;
		this.upDate = upDate;
		this.jgiName = jgiName;
		this.shokushuName = shokushuName;
	}

	/**
	 * コンストラクタ
	 *
	 * @param mrPlanFreeIndex フリー項目情報
	 */
	public FreeIndexDto(MrPlanFreeIndex mrPlanFreeIndex) {

		this.seqKey = mrPlanFreeIndex.getSeqKey();
		this.jgiNo = mrPlanFreeIndex.getJgiNo();
		this.jgiName = mrPlanFreeIndex.getJgiName();
		this.prodCode = mrPlanFreeIndex.getProdCode();
		this.indexFreeValueUh1 = ConvertUtil.parseMoneyToThousandUnit(mrPlanFreeIndex.getIndexFreeValueUh1());
		this.indexFreeValueUh2 = ConvertUtil.parseMoneyToThousandUnit(mrPlanFreeIndex.getIndexFreeValueUh2());
		this.indexFreeValueUh3 = ConvertUtil.parseMoneyToThousandUnit(mrPlanFreeIndex.getIndexFreeValueUh3());
		this.indexFreeValueP1 = ConvertUtil.parseMoneyToThousandUnit(mrPlanFreeIndex.getIndexFreeValueP1());
		this.indexFreeValueP2 = ConvertUtil.parseMoneyToThousandUnit(mrPlanFreeIndex.getIndexFreeValueP2());
		this.indexFreeValueP3 = ConvertUtil.parseMoneyToThousandUnit(mrPlanFreeIndex.getIndexFreeValueP3());
		this.upDate = mrPlanFreeIndex.getUpDate();
		this.shokushuName = mrPlanFreeIndex.getShokushuName();
	}

	/**
	 * シーケンスキーを取得する。
	 *
	 * @return シーケンスキー
	 */
	public Long getSeqKey() {
		return seqKey;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * フリー項目値UH1を取得する。
	 *
	 * @return フリー項目値UH1
	 */
	public Long getIndexFreeValueUh1() {
		return indexFreeValueUh1;
	}

	/**
	 * フリー項目値UH2を取得する。
	 *
	 * @return フリー項目値UH2
	 */
	public Long getIndexFreeValueUh2() {
		return indexFreeValueUh2;
	}

	/**
	 * フリー項目値UH3を取得する。
	 *
	 * @return フリー項目値UH3
	 */
	public Long getIndexFreeValueUh3() {
		return indexFreeValueUh3;
	}

	/**
	 * フリー項目値P1を取得する。
	 *
	 * @return フリー項目値P1
	 */
	public Long getIndexFreeValueP1() {
		return indexFreeValueP1;
	}

	/**
	 * フリー項目値P2を取得する。
	 *
	 * @return フリー項目値P2
	 */
	public Long getIndexFreeValueP2() {
		return indexFreeValueP2;
	}

	/**
	 * フリー項目値P3を取得する。
	 *
	 * @return フリー項目値P3
	 */
	public Long getIndexFreeValueP3() {
		return indexFreeValueP3;
	}

	/**
	 * 更新日を取得する。
	 *
	 * @return 更新日
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 職種名
	 * @return shokushuName
	 */
	public String getShokushuName() {
		return shokushuName;
	}
	/**
	 * フリー項目情報を取得する。
	 *
	 * @return フリー項目情報
	 */
	public MrPlanFreeIndex getMrPlanFreeIndex() {

		MrPlanFreeIndex mrPlanFreeIndex = new MrPlanFreeIndex();
		mrPlanFreeIndex.setSeqKey(seqKey);
		mrPlanFreeIndex.setJgiNo(jgiNo);
		mrPlanFreeIndex.setProdCode(prodCode);
		mrPlanFreeIndex.setIndexFreeValueUh1(ConvertUtil.parseMoneyToNormalUnit(indexFreeValueUh1));
		mrPlanFreeIndex.setIndexFreeValueUh2(ConvertUtil.parseMoneyToNormalUnit(indexFreeValueUh2));
		mrPlanFreeIndex.setIndexFreeValueUh3(ConvertUtil.parseMoneyToNormalUnit(indexFreeValueUh3));
		mrPlanFreeIndex.setIndexFreeValueP1(ConvertUtil.parseMoneyToNormalUnit(indexFreeValueP1));
		mrPlanFreeIndex.setIndexFreeValueP2(ConvertUtil.parseMoneyToNormalUnit(indexFreeValueP2));
		mrPlanFreeIndex.setIndexFreeValueP3(ConvertUtil.parseMoneyToNormalUnit(indexFreeValueP3));
		mrPlanFreeIndex.setUpDate(upDate);
		return mrPlanFreeIndex;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
