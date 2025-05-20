package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.SosMst;

/**
 * 営業所計画ステータスチェック用DTO
 *
 * @author nozaki
 */
public class OfficeStatusCheckDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * チェック対象の営業所の組織情報
	 */
	private final SosMst sosMst;

	/**
	 * チェック対象のカテゴリ
	 */
//	private final ProdCategory prodCategory;
	private final String prodCategory;

	/**
	 * 許可しない営業所計画ステータスのリスト
	 */
	private final List<OfficeStatusForCheck> statusForOfficeList;

	/**
	 * コンストラクタ <br>
	 * チェック対象の営業所の組織情報、カテゴリを指定する。
	 *
	 * @param sosMst チェック対象の営業所の組織情報
	 * @param prodCategory チェック対象のカテゴリ
	 * @param statusForOfficeList 許可しない営業所計画ステータスのリスト
	 * @param nothingStatusError ステータスなしエラーフラグ(true：エラーとする、false：エラーとしない)
	 */
//	public OfficeStatusCheckDto(SosMst sosMst, ProdCategory prodCategory, List<OfficeStatusForCheck> statusForOfficeList) {
	public OfficeStatusCheckDto(SosMst sosMst, String prodCategory, List<OfficeStatusForCheck> statusForOfficeList) {

		this.sosMst = sosMst;
		this.prodCategory = prodCategory;
		this.statusForOfficeList = statusForOfficeList;
	}

	/**
	 * チェック対象の営業所の組織情報を取得する。
	 *
	 * @return チェック対象の営業所の組織情報
	 */
	public SosMst getSosMst() {
		return sosMst;
	}

	/**
	 * チェック対象のカテゴリを取得する。
	 *
	 * @return チェック対象のカテゴリ
	 */
	public String getProdCategory() {
		return prodCategory;
	}

	/**
	 * 許可しない営業所計画ステータスのリストを取得する。
	 *
	 * @return 許可しない営業所計画ステータスのリスト
	 */
	public List<OfficeStatusForCheck> getStatusForOfficeList() {
		return statusForOfficeList;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
