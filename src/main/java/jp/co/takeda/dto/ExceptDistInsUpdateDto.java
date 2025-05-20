package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ExceptProd;

/**
 * 配分除外施設の更新用DTO
 *
 * @author khashimoto
 */
public class ExceptDistInsUpdateDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード（営業所）
	 */
	private final String sosCd;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 組織のカテゴリー
	 */
	private String[] category;

	/**
	 * 品目固定コードのリスト
	 */
	private final List<ExceptProd> prodCodeList;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 配分除外品目設定フラグ
	 */
	private final Boolean exceptProdSetFlg;

	/**
	 * 除外フラグ
	 */
	private String[] exclusionFlg;

	/**
	 *
	 * コンストラクタ<br>
	 * 配分除外施設の場合、使用する。
	 *
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param upDate 最終更新日時
	 * @param exclusionFlg 試算・配分除外フラグ
	 */
	public ExceptDistInsUpdateDto(String sosCd, String insNo, Integer jgiNo, Date upDate, String[] category, String[] exclusionFlg) {
		this.sosCd = sosCd;
		this.insNo = insNo;
		this.jgiNo = jgiNo;
		this.prodCodeList = null;
		this.upDate = upDate;
		this.exceptProdSetFlg = false;
		this.category = category;
		this.exclusionFlg = exclusionFlg;
	}

	/**
	 *
	 * コンストラクタ<br>
	 * 配分除外施設品目の場合、使用する。
	 *
	 * @param insNo 施設コード
	 * @param prodCodeList 品目固定コードのリスト
	 * @param upDate 最終更新日時
	 */
	public ExceptDistInsUpdateDto(String sosCd, String insNo, Integer jgiNo, Date upDate, String[] category, List<ExceptProd> prodCodeList) {
		this.sosCd = sosCd;
		this.insNo = insNo;
		this.jgiNo = jgiNo;
		this.prodCodeList = prodCodeList;
		this.upDate = upDate;
		this.category = category;
		this.exceptProdSetFlg = true;
	}

	/**
	 * 組織コード（営業所）を取得する。
	 *
	 * @return sosCd 組織コード（営業所）
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
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
	 * 品目コードのリストを取得する。
	 *
	 * @return prodCodeList 品目コードのリスト
	 */
	public List<ExceptProd> getProdCodeList() {
		return prodCodeList;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 配分除外品目設定フラグを取得する
	 *
	 * @return exceptProdSetFlg
	 */
	public Boolean getExceptProdSetFlg() {
		return exceptProdSetFlg;
	}

	/**
	 * 組織のカテゴリーを取得する。
	 *
	 * @return 除外フラグ
	 */
	public String[] getCategory() {
		return category;
	}

	/**
	 * 組織のカテゴリーを設定する。
	 *
	 * @param category 組織のカテゴリー
	 */
	public void setCategory(String[] category) {
		this.category = category;
	}

	/**
	 * 除外フラグを取得する。
	 *
	 * @return 除外フラグ
	 */
	public String[] getExclusionFlg() {
		return exclusionFlg;
	}

	/**
	 * 除外フラグを設定する。
	 *
	 * @param exclusionFlg 除外フラグ
	 */
	public void setExclusionFlg(String[] exclusionFlg) {
		this.exclusionFlg = exclusionFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
