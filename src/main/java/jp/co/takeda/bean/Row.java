package jp.co.takeda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 行をあらわすクラス
 * 
 * @author tkawabata
 */
public class Row implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * カラムリスト
	 */
	private final List<Column> columnList = new ArrayList<Column>();

	/**
	 * カラムを追加する。
	 * 
	 * @param dataColumn カラム
	 */
	public void addDataColumn(final Column dataColumn) {
		columnList.add(dataColumn);
	}

	/**
	 * カラム数を取得する。
	 * 
	 * @return カラム数
	 */
	public int getColumnsCount() {
		return columnList.size();
	}

	/**
	 * カラムリストを取得する。
	 * 
	 * @return カラムリスト
	 */
	public List<Column> getColumnList() {
		return columnList;
	}

	/**
	 * このオブジェクトの文字列表現を取得する。
	 * 
	 * @return このオブジェクトの文字列表現
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < columnList.size(); i++) {
			Column column = columnList.get(i);
			sb.append("[").append(i).append("] ").append(column.toString());
		}
		return sb.toString();
	}
}
