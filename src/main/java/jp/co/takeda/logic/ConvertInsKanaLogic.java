package jp.co.takeda.logic;

import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.model.ConvertInsKana;

/**
 * 施設名カナを変換するためのコンバートロジッククラス
 * 
 * @author tkawabata
 */
public class ConvertInsKanaLogic {

	/**
	 * 空白文字列
	 */
	public static final String BLANK = "";

	/**
	 * 変換マップを取得するＤＡＯインターフェイス
	 */
	private final CommonDao commonDao;

	/**
	 * コンストラクタ
	 * 
	 * @param commonDao ＤＡＯインターフェイス
	 */
	public ConvertInsKanaLogic(CommonDao commonDao) {
		if (commonDao == null) {
			final String errMsg = "変換マップを取得するためのDAOが指定されていない。";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		this.commonDao = commonDao;
	}

	/**
	 * 入力値を登録されている変換マップで変換する。
	 * 
	 * @param input 変換対象の文字列
	 * @return 変換後の文字列
	 */
	public String execute(String input) {
		if (input == null || input.equals(BLANK)) {
			return input;
		}
		List<ConvertInsKana> dictionaryList = null;
		try {
			dictionaryList = commonDao.findAll();
		} catch (DataNotFoundException e) {
			return input;
		}
		if (dictionaryList == null || dictionaryList.size() < 1) {
			return input;
		}
		for (ConvertInsKana conv : dictionaryList) {
			String entryString = conv.getEntryString();
			if (input.contains(entryString)) {
				String replaceString = conv.getReplaceString();
				if (replaceString == null) {
					replaceString = BLANK;
				}
				input = input.replace(entryString, replaceString);
			}
		}
		return input;
	}
}
