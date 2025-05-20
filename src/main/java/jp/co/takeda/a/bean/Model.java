package jp.co.takeda.a.bean;

import java.io.Serializable;

/**
 * 抽象Modelクラス<br>
 * 
 * <p>
 * 抽象クラスであり、当クラスを継承する場合、以下のメソッドを実装する必要がある。<br>
 * <ul>
 * <li>{@link #equals(Object)}</li>
 * <li>{@link #hashCode()}</li>
 * <li>{@link #compareTo(Object)}</li>
 * <li>{@link #toString()}</li>
 * </ul>
 * </p>
 * 
 * @author tkawabata
 */
public abstract class Model<T> implements Serializable, Comparable<T> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * このオブジェクトと指定されたオブジェクトの大小関係を返す。
	 * 
	 * @param obj 比較対象のオブジェクト(任意の型)
	 * @return 大小関係(-1、0、1)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public abstract int compareTo(final T obj);

	/**
	 * オブジェクトの等価性を判定する。
	 * 
	 * @param obj 比較対象のオブジェクト
	 * @return 等価の場合に真
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(final Object obj);

	/**
	 * ハッシュ値を算出する。
	 * 
	 * @return ハッシュ値
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();

	/**
	 * このオブジェクトの文字列表現を取得する。
	 * 
	 * @return このオブジェクトの文字列表現
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();
}
