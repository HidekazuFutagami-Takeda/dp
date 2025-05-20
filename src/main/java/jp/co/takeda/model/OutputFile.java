package jp.co.takeda.model;

import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.div.OutputType;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 出力ファイル情報を表すモデルクラス
 * 
 * @author khashimoto
 */
public class OutputFile extends DpModel<OutputFile> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------

	/**
	 * 出力区分
	 */
	private OutputType outputType;

	/**
	 * 出力ファイル名
	 */
	private String outputFileName;

	/**
	 * 汎用項目
	 */
	private String freeData;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 出力区分を取得する。
	 * 
	 * @return 出力区分
	 */
	public OutputType getOutputType() {
		return outputType;
	}

	/**
	 * 出力区分を設定する。
	 * 
	 * @param outputFileId 出力区分
	 */
	public void setOutputType(OutputType outputType) {
		this.outputType = outputType;
	}

	/**
	 * 出力ファイル名を取得する。
	 * 
	 * @return 出力ファイル名
	 */
	public String getOutputFileName() {
		return outputFileName;
	}

	/**
	 * 出力ファイル名を設定する。
	 * 
	 * @param outputFileName 出力ファイル名
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	/**
	 * 汎用項目を取得する。
	 * 
	 * @return 汎用項目
	 */
	public String getFreeData() {
		return freeData;
	}

	/**
	 * 汎用項目を設定する。
	 * 
	 * @param freeData 汎用項目
	 */
	public void setFreeData(String freeData) {
		this.freeData = freeData;
	}

	@Override
	public int compareTo(OutputFile obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.seqKey, obj.seqKey).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && OutputFile.class.isAssignableFrom(entry.getClass())) {
			OutputFile obj = (OutputFile) entry;
			return new EqualsBuilder().append(this.seqKey, obj.seqKey).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.seqKey).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
