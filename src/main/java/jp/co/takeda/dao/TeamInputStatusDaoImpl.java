package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.TeamInputStatus;

import org.springframework.stereotype.Repository;

/**
 * チーム別入力状況にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("teamInputStatusDao")
public class TeamInputStatusDaoImpl extends AbstractDao implements TeamInputStatusDao {

	private static final String SQL_MAP = "DPS_I_TEAM_INPUT_STATUS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// チーム別入力状況を取得
	public TeamInputStatus search(Long seqKey) throws DataNotFoundException {
		return (TeamInputStatus) super.selectBySeqKey(seqKey);
	}

	// 組織コード、品目固定コードを元に、チーム別入力状況を取得
	public TeamInputStatus search(String sosCd, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		TeamInputStatus record = new TeamInputStatus();
		record.setSosCd(sosCd);
		record.setProdCode(prodCode);
		return (TeamInputStatus) super.selectByUniqueKey(record);
	}

	// チーム別入力状況を登録
	public void insert(TeamInputStatus record) throws DuplicateException {
		super.insert(record);
	}

	// チーム別入力状況を更新
	public int update(TeamInputStatus record) {
		return super.update(record);
	}

	// チーム別入力状況を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
