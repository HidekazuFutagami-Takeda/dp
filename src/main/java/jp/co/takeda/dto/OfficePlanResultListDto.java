package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.div.CalcType;

/**
 * 営業所計画の検索結果用DTO
 *
 * @author khashimoto
 */
public class OfficePlanResultListDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード(営業所)
	 */
	private final String sosCd;

	/**
	 * カテゴリ
	 */
	private final String category;

	/**
	 * 試算タイプ
	 */
	private final CalcType calcType;

	/**
	 * 検索結果DTO
	 */
	private final List<OfficePlanResultDto> resultList;

	/**
	 * 調整金額更新日時
	 */
	private final Date choseiUpdate;

	/**
	 * ワクチン編集
	 */
	private final boolean vacEditFlg;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param sosCd 組織コード(営業所)
	 * @param category カテゴリ
	 * @param calcType 試算タイプ
	 * @param resultList 検索結果DTO
	 * @param choseiUpdate 調整金額更新日時
	 * @param vacEditFlg ワクチン編集
	 */
	public OfficePlanResultListDto(final String sosCd, final String category, final CalcType calcType,
			                        final List<OfficePlanResultDto> resultList, Date choseiUpdate, boolean vacEditFlg) {
		this.sosCd = sosCd;
		this.category = category;
		this.calcType = calcType;
		this.resultList = resultList;
		this.choseiUpdate = choseiUpdate;
		this.vacEditFlg = vacEditFlg;
	}

	/**
	 * 組織コード(営業所)を取得する。
	 *
	 * @return 組織コード(営業所)
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 試算タイプを取得する。
	 *
	 * @return 試算タイプ
	 */
	public CalcType getCalcType() {
		return calcType;
	}

	/**
	 * 検索結果DTOを取得する。
	 *
	 * @return 検索結果DTO
	 */
	public List<OfficePlanResultDto> getResultList() {
		return resultList;
	}

	/**
	 * 調整金額更新日時を取得する。
	 *
	 * @return 調整金額更新日時
	 */
	public Date getChoseiUpdate() {
		return choseiUpdate;
	}

	/**
	 * ワクチン編集を取得する。
	 *
	 * @return ワクチン編集
	 */
	public boolean getVacEditFlg() {
		return vacEditFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
