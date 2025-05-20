package jp.co.takeda.logic;

/**
 * 調整金額メッセージを生成するロジッククラス
 * 
 * @author tkawabata
 */
public class CreateChoseiMsgLogic {

	/** 調整金額固定文言 */
	private static final String CHOSEI_START = "(調整金額";

	/** 調整金額固定文言 */
	private static final String CHOSEI_START_PRIORITY = "(調整施設";

	/** 調整金額固定文言 */
	private static final String CHOSEI_UH = " UH：";

	/** 調整金額固定文言 */
	private static final String CHOSEI_P = " P：";

	/** 調整金額固定文言 */
	private static final String CHOSEI_ARI = "あり";

	/** 調整金額固定文言 */
	private static final String CHOSEI_NASI = "なし";

	/** 調整金額固定文言 */
	private static final String CHOSEI_END = ")";

	/**
	 * UH調整フラグ
	 */
	private boolean uhChoseiFlg;

	/**
	 * P調整フラグ
	 */
	private boolean pChoseiFlg;

	/**
	 * コンストラクタ
	 * 
	 * @param uhChoseiFlg UH調整フラグ
	 * @param pChoseiFlg P調整フラグ
	 */
	public CreateChoseiMsgLogic(boolean uhChoseiFlg, boolean pChoseiFlg) {
		this.uhChoseiFlg = uhChoseiFlg;
		this.pChoseiFlg = pChoseiFlg;
	}

	public String createMsg() {
		return createMsg(false);
	}
	
	public String createMsg(boolean isPriority) {
		
		// 画面表示用メッセージ生成
		StringBuilder sb = new StringBuilder();
		
		// 冒頭文字
		if(isPriority){
			sb.append(CHOSEI_START_PRIORITY);
		} else {
			sb.append(CHOSEI_START);
		}
		
		sb.append(CHOSEI_UH);
		if (uhChoseiFlg) {
			sb.append(CHOSEI_ARI);
		} else {
			sb.append(CHOSEI_NASI);
		}
		sb.append(CHOSEI_P);
		if (pChoseiFlg) {
			sb.append(CHOSEI_ARI);
		} else {
			sb.append(CHOSEI_NASI);
		}
		sb.append(CHOSEI_END);
		return sb.toString();
	}
}
