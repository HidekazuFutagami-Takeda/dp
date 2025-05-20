package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.bean.DpModel;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 管理用拡張ＤＡＯクラス
 * 
 * @author khashimoto
 */
public abstract class AbstractManageDao extends AbstractDao {

	/**
	 * シーケンスキーを元に取得する。<br>
	 * sqlMapのselectを実行する。<br>
	 * 検索条件に年度・期の設定を追加して、検索を実行する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 取得データ
	 * @throws DataNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected DpModel selectBySeqKey(Long seqKey) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (seqKey == null) {
			final String errMsg = "シーケンスキーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("seqKey", seqKey);
		// 年度・期の設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		paramMap.put("calYear", sysManage.getSysYear());
		paramMap.put("calTerm", sysManage.getSysTerm());
		SysType sysType = sysManage.getSysType();
		if (sysType != null && SysType.IYAKU == sysType) {
			paramMap.put("isIyaku", Boolean.valueOf(true));
		}

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".select", paramMap);
	}

	/**
	 * ユニークキーを元に取得する。<br>
	 * sqlMapのselectUkを実行する。
	 * 
	 * @param record 取得対象データ
	 * @return 取得データ
	 * @throws DataNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected <E, T> E selectByUniqueKey(DpManageModel<T> record) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 年度・期の設定
		record = setCal(record);

		// ----------------------
		// 検索実行
		// ----------------------
		Object result = dataAccess.queryForObject(getSqlMapName() + ".selectUk", record);
		return (E) result;
	}

	/**
	 * 全カラムを指定して登録する。<br>
	 * ユニークキーで重複チェックを行い、<br>
	 * sqlMapのinsertを実行する。
	 * 
	 * @param record 登録対象データ
	 * @param pgId 更新PGID
	 * @throws DuplicateException
	 */
	protected <T> void insert(DpManageModel<T> record, String pgId) throws DuplicateException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "登録情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 年度・期設定
		record = setCal(record);

		// -------------
		// 重複チェック
		// -------------
		this.duplicateCheck(record);

		// -------------
		// 登録実行
		// -------------
		// 登録者・更新者情報設定
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		record.setIsJgiNo(dpUser.getJgiNo());
		record.setIsJgiName(dpUser.getJgiName());
		record.setIsPgId(pgId);
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		record.setUpPgId(pgId);
		record.setDelFlg(false);
		dataAccess.execute(getSqlMapName() + ".insert", record);
	}

	/**
	 * 登録時の重複チェック
	 * 
	 * @param record 登録対象データ
	 * @throws DuplicateException
	 */
	protected <T> void duplicateCheck(DpManageModel<T> record) throws DuplicateException {
		try {
			this.selectByUniqueKey(record);
			this.insertDataDuplicate();
		} catch (DataNotFoundException e) {
		}
	}

	/**
	 * seqKeyで指定して更新する。<br>
	 * seqKeyで楽観的ロックチェックを行い、<br>
	 * sqlMapのupdateを実行する。
	 * 
	 * @param record 更新対象データ
	 * @param pgId 更新PGID
	 * @return 処理件数
	 */
	protected <T> int update(DpManageModel<T> record, String pgId) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "更新情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 年度・期設定
		record = setCal(record);

		// -------------
		// 更新実行
		// -------------
		// 更新者情報設定
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		record.setUpPgId(pgId);
		int count = dataAccess.execute(getSqlMapName() + ".update", record);
		if (count == 0) {
			optimisticLock();
		}
		return count;
	}

	/**
	 * 年度・期の設定を行う。
	 * 
	 * @param record
	 * @return モデル
	 */
	protected <T> DpManageModel<T> setCal(DpManageModel<T> record) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		DpManageModel<T> model = record;

		// 年度・期の設定
		model.setCalYear(DpUserInfo.getDpUserInfo().getSysManage().getSysYear());
		model.setCalTerm(DpUserInfo.getDpUserInfo().getSysManage().getSysTerm());

		return model;
	}

	/**
	 * 検索条件に年度・期の設定を追加して、検索を実行する。
	 * 
	 * @param sqlId sqlID
	 * @param map 検索条件を格納したMAP
	 * @return 検索結果オブジェクト
	 * @throws DataNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected <E> E select(String sqlID, Map<String, Object> map) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sqlID == null) {
			final String errMsg = "sqlIDがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (map == null) {
			final String errMsg = "検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 年度・期の設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		map.put("calYear", sysManage.getSysYear());
		map.put("calTerm", sysManage.getSysTerm());
		SysType sysType = sysManage.getSysType();
		if (sysType != null && SysType.IYAKU == sysType) {
			map.put("isIyaku", Boolean.valueOf(true));
		}

		// ----------------------
		// 検索実行
		// ----------------------
		Object result = dataAccess.queryForObject(sqlID, map);
		return (E) result;
	}

	/**
	 * 検索条件に年度・期の設定を追加して、検索を実行する。
	 * 
	 * @param sqlID sqlID
	 * @param map 検索条件を格納したMAP
	 * @return 検索結果オブジェクトリスト
	 * @throws DataNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected <E> List<E> selectList(String sqlID, Map<String, Object> paramMap) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sqlID == null) {
			final String errMsg = "sqlIDがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (paramMap == null) {
			final String errMsg = "検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 年度・期の設定
		SysManage sysManage = DpUserInfo.getDpUserInfo().getSysManage();
		paramMap.put("calYear", sysManage.getSysYear());
		paramMap.put("calTerm", sysManage.getSysTerm());
		SysType sysType = sysManage.getSysType();
		if (sysType != null && SysType.IYAKU == sysType) {
			paramMap.put("isIyaku", Boolean.valueOf(true));
		}

		// ----------------------
		// 検索実行
		// ----------------------
		Object result = dataAccess.queryForList(sqlID, paramMap);
		return (List<E>) result;
	}
}
