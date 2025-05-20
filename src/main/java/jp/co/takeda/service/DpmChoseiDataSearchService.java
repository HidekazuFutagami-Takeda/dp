package jp.co.takeda.service;

import java.util.Calendar;

import jp.co.takeda.dto.ChoseiDataParamsDto;
import jp.co.takeda.dto.ChoseiDataResultDto;

/**
 * 調整金額テーブルを検索するサービスインターフェイス
 * 
 * @author tkawabata
 */
public interface DpmChoseiDataSearchService {

	/**
	 * 調整金額情報の最終更新日時を検索
	 * 
	 * @return 調整金額情報の最終更新日時
	 */
	Calendar getChoseiLastUpDate();

	/**
	 * 検索条件を生成する。
	 * 
	 * @param targetSosCd 今回新たに展開/非展開する対象の組織コード(NULL可)
	 * @param targetBumonRank 今回新たに展開/非展開する対象の部門ランク(NULL可)
	 * @param beforeParams 今までの検索条件(NULL可)
	 * @return 検索条件
	 */
	ChoseiDataParamsDto createParams(String targetSosCd, String targetBumonRank, ChoseiDataParamsDto beforeParams);

	/**
	 * 調整金額テーブルを検索する。
	 * 
	 * @param params 検索条件
	 * @return 調整金額テーブル
	 */
	ChoseiDataResultDto search(ChoseiDataParamsDto params);
}
