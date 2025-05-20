package jp.co.takeda.service;

/**
 * 特約店別計画スライドサービスインターフェイス
 * 
 * @author yokokawa
 */
public interface DpsTmsTytenSlideService {

	/**
	 * 特約店別計画スライド処理を実行する。
	 * 
	 * @param sosCd2 組織コード(支店)
	 * @param category カテゴリー
	 */
	public void tmsTytenSlide(String sosCd2, String category);
}
