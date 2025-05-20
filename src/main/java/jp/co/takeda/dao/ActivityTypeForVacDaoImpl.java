package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.ActivityTypeForVac;

/**
 * 活動区分情報にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("activityTypeForVacDao")
public class ActivityTypeForVacDaoImpl extends AbstractDao implements ActivityTypeForVacDao {

	private static final String SQL_MAP = "DPS_V_ACTIVITY_TYPE_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 施設コードも元に、活動区分情報を取得
	public ActivityTypeForVac search(String insNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		ActivityTypeForVac record = new ActivityTypeForVac();
		record.setInsNo(insNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectUk", record);
	}
}
