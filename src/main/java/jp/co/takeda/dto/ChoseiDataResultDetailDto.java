package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.view.SosChoseiSummary;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 調整計画データ結果明細を表すＤＴＯクラス
 * 
 * @author tkawabata
 */
public class ChoseiDataResultDetailDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ラベル用文字列
	 */
	private final String name;

	/**
	 * インデント回数
	 */
	private final int indendSize;

	/**
	 * 組織/従業員単位の調整金額有無のサマリ
	 */
	private final SosChoseiSummary sosChoseiSummary;

	/**
	 * データ検索フラグ
	 */
	private final boolean dataSearchFlg;

	/**
	 * 組織リンク有無のフラグ
	 */
	private boolean linkFlg;

	/**
	 * ONC組織フラグ
	 */
	private final boolean oncSosFlg;

	/**
	 * コンストラクタ
	 * 
	 * @param sosChoseiSummary 組織/従業員単位の調整金額有無のサマリ
	 * @param indendSize インデント回数
	 */
	public ChoseiDataResultDetailDto(SosChoseiSummary sosChoseiSummary, int indendSize) {
		this.sosChoseiSummary = sosChoseiSummary;
		this.indendSize = indendSize;
		this.linkFlg = true;
		this.dataSearchFlg = true;
		this.name = createSosName(sosChoseiSummary.getSosJgiName(), sosChoseiSummary.getJgiNo());
		this.oncSosFlg = BooleanUtils.isTrue(sosChoseiSummary.getOncSosFlg());
	}

	/**
	 * コンストラクタ
	 * 
	 * @param sosCd 組織コード
	 * @param name 組織名称
	 * @param indendSize インデント回数
	 */
	public ChoseiDataResultDetailDto(String sosCd, String name, int indendSize, boolean oncSosFlg) {
		this.sosChoseiSummary = new SosChoseiSummary();
		this.sosChoseiSummary.setSosCd(sosCd);
		this.sosChoseiSummary.setSosJgiName(name);
		this.sosChoseiSummary.setOncSosFlg(oncSosFlg);
		this.indendSize = indendSize;
		this.linkFlg = false;
		this.dataSearchFlg = false;
		this.name = createSosName(name, null);
		this.oncSosFlg = oncSosFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * リンク有無のフラグを取得する。
	 * 
	 * @return リンク有無のフラグ
	 */
	public boolean isLinkFlg() {
		return linkFlg;
	}

	/**
	 * リンク有無のフラグを設定する。
	 * 
	 * @param linkFlg リンク有無のフラグ
	 */
	public void setLinkFlg(boolean linkFlg) {
		this.linkFlg = linkFlg;
	}

	/**
	 * データ検索フラグを取得する。
	 * 
	 * @return データ検索フラグ
	 */
	public boolean isDataSearchFlg() {
		return dataSearchFlg;
	}

	/**
	 * ラベル用文字列を取得する。
	 * 
	 * @return ラベル用文字列
	 */
	public String getName() {
		return name;
	}

	/**
	 * 組織/従業員単位の調整金額有無のサマリを取得する。
	 * 
	 * @return 組織/従業員単位の調整金額有無のサマリ
	 */
	public SosChoseiSummary getSosChoseiSummary() {
		return sosChoseiSummary;
	}

	/**
	 * インデント回数を取得する。
	 * 
	 * @return インデント回数
	 */
	public int getIndendSize() {
		return indendSize;
	}

	/**
	 * ONC組織判定を取得する。
	 * 
	 * @return ONC組織判定（true：ONC組織、false：ONC組織以外）
	 */
	public boolean isOncSos() {
		return oncSosFlg;
	}

	/**
	 * 組織名称用の文字列を生成する。
	 * 
	 * @param jgiNo 従業員番号
	 * @return 組織名称用の文字列
	 */
	private String createSosName(String name, Integer jgiNo) {
		if (jgiNo == null) {
			final String PREFIX = "▼";
			return PREFIX + name;
		} else {
			return name;
		}
	}
}
