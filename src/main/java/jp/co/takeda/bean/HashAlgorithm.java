package jp.co.takeda.bean;

/**
 * ハッシュのアルゴリズムを表す列挙
 * 
 * @author tkawabata
 */
public enum HashAlgorithm {

	/**
	 * MD5
	 */
	MD5("MD5"),

	/**
	 * SHA1
	 */
	SHA1("SHA1");

	/**
	 * アルゴリズムを識別するID
	 */
	private final String id;

	/**
	 * コンストラクタ
	 * 
	 * @param id ID
	 */
	HashAlgorithm(final String id) {
		this.id = id;
	}

	/**
	 * ハッシュアルゴリズムを識別するキーを取得する。
	 * 
	 * @return ハッシュアルゴリズムを識別するキー
	 */
	public String getAlgorithmKey() {
		return this.id;
	}
}
