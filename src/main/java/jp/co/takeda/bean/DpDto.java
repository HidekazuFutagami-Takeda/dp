package jp.co.takeda.bean;

import java.io.Serializable;

/**
 * 納入計画システム向け抽象DTOクラス
 * 
 * <pre>
 * 抽象クラスであり、当クラスを継承する場合、以下のメソッドを実装する必要がある。
 * <li>{@link #toString()}</li>
 * </pre>
 * 
 * @author khashimoto
 */
public abstract class DpDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * このオブジェクトの文字列表現を取得する。
	 * 
	 * @return このオブジェクトの文字列表現
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
