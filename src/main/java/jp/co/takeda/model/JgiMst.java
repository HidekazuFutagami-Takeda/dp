package jp.co.takeda.model;

import jp.co.takeda.a.bean.Model;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.Shokusei;
import jp.co.takeda.model.div.Shokushu;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 従業員基本を表すモデルクラス
 *
 * @author tkawabata
 */
public final class JgiMst extends Model<JgiMst> implements Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 兼務区分([0]で固定)
	 */
	public static final Integer KENMU_KB = Integer.valueOf(0);

	/**
	 * 組織コード1～4のブランク時の値
	 */
	public static final String BLANK_SOS_CD = "00000";

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 従業員番号
	 */
	private Integer jgiNo;

	/**
	 * 氏名
	 */
	private String jgiName;

	/**
	 * 職制名C
	 */
	private Shokusei shokuseiCd;

	/**
	 * 職種C
	 */
	private Shokushu shokushuCd;

	/**
	 * 職種名
	 */
	private String shokushuName;


	/**
	 * 標準組織背番号Ｃ2(支店レベル)<br>
	 * 支店内の従業員検索時に使用
	 */
	private String sosCd2;

	/**
	 * 標準組織背番号Ｃ3(営業所レベル)<br>
	 * 営業所内の従業員検索時に使用
	 */
	private String sosCd3;

	/**
	 * 標準組織背番号Ｃ4(チームレベル)<br>
	 * チーム内の従業員検索時に使用
	 */
	private String sosCd4;

	/**
	 * 標準組織背番号Ｃ<br>
	 * 所属する組織取得時に使用
	 */
	private String sosCd;

	/**
	 * 従業員区分
	 */
	private JgiKb jgiKb;

	/**
	 * 雑組織フラグ
	 * TODO:2015年上期組織変更対応（管理のみ対応済み）
	 */
	private Boolean etcSosFlg;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員番号を設定する。
	 *
	 * @param jgiNo 従業員番号
	 */
	public void setJgiNo(Integer jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * 氏名を取得する。
	 *
	 * @return 氏名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 氏名を設定する。
	 *
	 * @param jgiName 氏名
	 */
	public void setJgiName(String jgiName) {
		this.jgiName = jgiName;
	}

	/**
	 * 職制を取得する。
	 *
	 * @return 職制
	 */
	public Shokusei getShokuseiCd() {
		return shokuseiCd;
	}

	/**
	 * 職制を設定する。
	 *
	 * @param shokuseiCd 職制
	 */
	public void setShokuseiCd(Shokusei shokuseiCd) {
		this.shokuseiCd = shokuseiCd;
	}

	/**
	 * 職種を取得する。
	 *
	 * @return 職種
	 */
	public Shokushu getShokushuCd() {
		return shokushuCd;
	}

	/**
	 * 職種を設定する。
	 *
	 * @param shokushuCd 職種
	 */
	public void setShokushuCd(Shokushu shokushuCd) {
		this.shokushuCd = shokushuCd;
	}

	/**
	 * 標準組織背番号Ｃ2(支店レベル)を取得する。
	 *
	 * @return 標準組織背番号Ｃ2(支店レベル)
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * 標準組織背番号Ｃ2(支店レベル)を設定する。
	 *
	 * @param sosCd2 標準組織背番号Ｃ2(支店レベル)
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	/**
	 * 標準組織背番号Ｃ3(営業所レベル)を取得する。
	 *
	 * @return 標準組織背番号Ｃ3(営業所レベル)
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * 標準組織背番号Ｃ3(営業所レベル)を設定する。
	 *
	 * @param sosCd3 標準組織背番号Ｃ3(営業所レベル)
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * 標準組織背番号Ｃ4(チームレベル)を取得する。
	 *
	 * @return 標準組織背番号Ｃ4(チームレベル)
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * 標準組織背番号Ｃ4(チームレベル)を設定する。
	 *
	 * @param sosCd4 標準組織背番号Ｃ4(チームレベル)
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

	/**
	 * 標準組織背番号Ｃを取得する。
	 *
	 * @return 標準組織背番号Ｃ
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 標準組織背番号Ｃを設定する。
	 *
	 * @param sosCd 標準組織背番号Ｃ
	 */
	public void setSosCd(String sosCd) {
		this.sosCd = sosCd;
	}

	/**
	 * 従業員区分を取得する。
	 *
	 * @return 従業員区分
	 */
	public JgiKb getJgiKb() {
		return jgiKb;
	}

	/**
	 * 従業員区分を設定する。
	 *
	 * @param jgiKb 従業員区分
	 */
	public void setJgiKb(JgiKb jgiKb) {
		this.jgiKb = jgiKb;
	}

	/**
	 * 職種名を取得する
	 *
	 * @return
	 */
	public String getShokushuName() {
		return shokushuName;
	}

	/**
	 * 職種名を設定する
	 *
	 * @param shokushuName
	 */
	public void setShokushuName(String shokushuName) {
		this.shokushuName = shokushuName;
	}

	/**
	 * 雑組織フラグを取得する。
	 * 
	 * @return 雑組織フラグ
	 */
	public Boolean getEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * 雑組織フラグを設定する。
	 * 
	 * @param etcSosFlg 雑組織フラグ
	 */
	public void setEtcSosFlg(Boolean etcSosFlg) {
		this.etcSosFlg = etcSosFlg;
	}

	@Override
	public int compareTo(JgiMst obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jgiNo, obj.jgiNo).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && JgiMst.class.isAssignableFrom(entry.getClass())) {
			JgiMst obj = (JgiMst) entry;
			return new EqualsBuilder().append(this.jgiNo, obj.jgiNo).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jgiNo).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	@Override
	public JgiMst clone() {
		JgiMst jgiMst = new JgiMst();
		jgiMst.setJgiNo(this.jgiNo);
		jgiMst.setJgiKb(this.jgiKb);
		jgiMst.setJgiName(this.jgiName);
		jgiMst.setShokuseiCd(this.shokuseiCd);
		jgiMst.setShokushuCd(this.shokushuCd);
		jgiMst.setShokushuName(this.shokushuName);
		jgiMst.setSosCd(this.sosCd);
		jgiMst.setSosCd2(this.sosCd2);
		jgiMst.setSosCd3(this.sosCd3);
		jgiMst.setSosCd4(this.sosCd4);
		return jgiMst;
	}
}
