package jp.co.takeda.model.view;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.takeda.a.bean.Model;

/**
 * 組織・従業員検索のラベル情報結果格納
 *
 * @author k.fukui
 */
public class SosJgiLblInfo extends Model<SosJgiLblInfo> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * name
	 */
	private String name;

	/**
	 * sosJgiName
	 */
	private String sosJgiName;

	/**
	 * sosCd2
	 */
	private String sosCd2;

	/**
	 * sosCd3
	 */
	private String sosCd3;

	/**
	 * sosCd4
	 */
	private String sosCd4;

	/**
	 * jgiNo
	 */
	private String jgiNo;

	/**
	 * initSosCodeValue
	 */
	private String initSosCodeValue;

	/**
	 * initSosCodeValue
	 */
	private String brCode;

	/**
	 * distCode
	 */
	private String distCode;

	/**
	 * etcSosFlg
	 */
	private String etcSosFlg;

	/**
	 * oncSosFlg
	 */
	private String oncSosFlg;

	/**
	 * sosCategory
	 */
	private String sosCategory;

	/**
	 * sosSubCategory
	 */
	private String sosSubCategory;

	/**
	 * underSosCnt
	 */
	private String underSosCnt;

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return sosJgiName
	 */
	public String getSosJgiName() {
		return sosJgiName;
	}

	/**
	 * @param sosJgiName セットする sosJgiName
	 */
	public void setSosJgiName(String sosJgiName) {
		this.sosJgiName = sosJgiName;
	}

	/**
	 * @return sosCd2
	 */
	public String getSosCd2() {
		return sosCd2;
	}

	/**
	 * @param sosCd2 セットする sosCd2
	 */
	public void setSosCd2(String sosCd2) {
		this.sosCd2 = sosCd2;
	}

	/**
	 * @return sosCd3
	 */
	public String getSosCd3() {
		return sosCd3;
	}

	/**
	 * @param sosCd3 セットする sosCd3
	 */
	public void setSosCd3(String sosCd3) {
		this.sosCd3 = sosCd3;
	}

	/**
	 * @return sosCd4
	 */
	public String getSosCd4() {
		return sosCd4;
	}

	/**
	 * @param sosCd4 セットする sosCd4
	 */
	public void setSosCd4(String sosCd4) {
		this.sosCd4 = sosCd4;
	}

	/**
	 * @return jgiNo
	 */
	public String getJgiNo() {
		return jgiNo;
	}

	/**
	 * @param jgiNo セットする jgiNo
	 */
	public void setJgiNo(String jgiNo) {
		this.jgiNo = jgiNo;
	}

	/**
	 * @return initSosCodeValue
	 */
	public String getInitSosCodeValue() {
		return initSosCodeValue;
	}

	/**
	 * @param initSosCodeValue セットする initSosCodeValue
	 */
	public void setInitSosCodeValue(String initSosCodeValue) {
		this.initSosCodeValue = initSosCodeValue;
	}

	/**
	 * @return brCode
	 */
	public String getBrCode() {
		return brCode;
	}

	/**
	 * @param brCode セットする brCode
	 */
	public void setBrCode(String brCode) {
		this.brCode = brCode;
	}

	/**
	 * @return distCode
	 */
	public String getDistCode() {
		return distCode;
	}

	/**
	 * @param distCode セットする distCode
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	/**
	 * @return etcSosFlg
	 */
	public String getEtcSosFlg() {
		return etcSosFlg;
	}

	/**
	 * @param etcSosFlg セットする etcSosFlg
	 */
	public void setEtcSosFlg(String etcSosFlg) {
		this.etcSosFlg = etcSosFlg;
	}

	/**
	 * @return oncSosFlg
	 */
	public String getOncSosFlg() {
		return oncSosFlg;
	}

	/**
	 * @param oncSosFlg セットする oncSosFlg
	 */
	public void setOncSosFlg(String oncSosFlg) {
		this.oncSosFlg = oncSosFlg;
	}

	/**
	 * @return sosCategory
	 */
	public String getSosCategory() {
		return sosCategory;
	}

	/**
	 * @param sosCategory セットする sosCategory
	 */
	public void setSosCategory(String sosCategory) {
		this.sosCategory = sosCategory;
	}

	/**
	 * @return sosSubCategory
	 */
	public String getSosSubCategory() {
		return sosSubCategory;
	}

	/**
	 * @param sosSubCategory セットする sosSubCategory
	 */
	public void setSosSubCategory(String sosSubCategory) {
		this.sosSubCategory = sosSubCategory;
	}

	/**
	 * @return underSosCnt
	 */
	public String getUnderSosCnt() {
		return underSosCnt;
	}

	/**
	 * @param underSosCnt セットする underSosCnt
	 */
	public void setUnderSosCnt(String underSosCnt) {
		this.underSosCnt = underSosCnt;
	}

	@Override
	public int compareTo(SosJgiLblInfo obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this, obj).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && SosJgiLblInfo.class.isAssignableFrom(entry.getClass())) {
			SosJgiLblInfo obj = (SosJgiLblInfo) entry;
			return new EqualsBuilder().append(this, obj).isEquals();
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

	/**
	 * <p>[概 要] 基本型（JavaBeans）⇒JSON文字列への変換処理</p>
	 * <p>[詳 細] </p>
	 * <p>[備 考] </p>
	 * @param  bean JavaBeansオブジェクト
	 * @return JSON変換後の文字列（パラメータがnullの場合はnullを返します。）
	 * @throws JsonProcessingException
	 */
	public String parseBeanToJson() throws JsonProcessingException{

		// Jacksonのマッパーを生成
		ObjectMapper mapper = new ObjectMapper();

		// JavaBeansオブジェクトをJSON文字列へ変換
		String jsonStr = mapper.writeValueAsString(this);

		return jsonStr;
	}
}
