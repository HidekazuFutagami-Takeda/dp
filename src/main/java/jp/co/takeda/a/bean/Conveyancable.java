package jp.co.takeda.a.bean;

import java.io.Serializable;

/**
 * 伝達可能であることを示すインターフェイス
 * 
 * @author tkawabata
 */
public interface Conveyancable extends Serializable {

	/**
	 * 伝達オブジェクトを取得する。
	 * 
	 * @return 伝達オブジェクト
	 */
	Conveyance getConveyance();
}
