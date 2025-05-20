package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.view.SosEachKindInfo;

import org.springframework.stereotype.Repository;

/**
 * 組織単位の各種登録状況を取得するDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("sosEachKindInfoDao")
public class SosEachKindInfoDaoImpl extends AbstractDao implements SosEachKindInfoDao {

	private static final String SQL_MAP = "VIEW_SosEachKindInfo_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 組織コード(支店)を基に、各種登録状況を取得
	public SosEachKindInfo searchShiten(String sosCd2) throws DataNotFoundException {
		// ----------------------
		// 検索実行
		// ----------------------
		return search(sosCd2, sosCd2, null, null);
	}

	// 組織コード(営業所)を基に、各種登録状況を取得
	public SosEachKindInfo searchEigyo(String sosCd3) throws DataNotFoundException {
		// ----------------------
		// 検索実行
		// ----------------------
		return search(sosCd3, null, sosCd3, null);
	}

	// 組織コード(チーム)を基に、各種登録状況を取得
	public SosEachKindInfo searchTeam(String sosCd4) throws DataNotFoundException {
		// ----------------------
		// 検索実行
		// ----------------------
		return search(sosCd4, null, null, sosCd4);
	}

	/**
	 * 組織単位の各種登録状況取得処理を実行する。
	 * 
	 * @param sosCd 検索対象の組織コード
	 * @param sosCd2 組織コード(支店)
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @return 組織単位の各種登録状況
	 * @throws DataNotFoundException
	 */
	private SosEachKindInfo search(String sosCd, String sosCd2, String sosCd3, String sosCd4) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd", sosCd);
		paramMap.put("sosCd2", sosCd2);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}
}
