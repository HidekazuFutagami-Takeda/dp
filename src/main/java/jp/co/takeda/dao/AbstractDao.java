package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.DB_DUPLICATE_ERROR;
import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.dao.DataAccess;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.DpModel;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * DAO共通処理を定義する抽象クラス
 * 
 * @author tkawabata
 */
public abstract class AbstractDao {

	@Autowired
	@Qualifier("dataAccess")
	protected DataAccess dataAccess;

	/**
	 * sqlMap名称を取得する。
	 * 
	 * @return sqlMap名称
	 */
	protected abstract String getSqlMapName();

	/**
	 * 登録時の重複エラー時の挙動を規定する。
	 * 
	 * @throws DuplicateException 重複例外
	 */
	protected void insertDataDuplicate() throws DuplicateException {
		final String errMsg = "：登録時に重複を発見";
		throw new DuplicateException(new Conveyance(DB_DUPLICATE_ERROR, getSqlMapName() + errMsg));
	}

	/**
	 * 再検索時にデータが見つからない場合の挙動を規定する。
	 * 
	 * @throws SystemException システム例外をスロー
	 */
	protected void researchDataNotFound() throws SystemException {
		final String errMsg = "：登録更新後に対象データが見つからない";
		throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, getSqlMapName() + errMsg));
	}

	/**
	 * 更新対象が見つからない場合に楽観的ロック例外をスローする。
	 * 
	 * @param e DataNotFoundException
	 * @throws OptimisticLockingFailureException 楽観的ロック例外
	 */
	protected void optimisticLock(DataNotFoundException e) throws OptimisticLockingFailureException {
		final String errMsg = "：更新対象が見つからないため、楽観的ロックエラーとする";
		throw new OptimisticLockingFailureException(getSqlMapName() + errMsg, e);
	}

	/**
	 * 更新日時が異なり、更新・削除失敗時に楽観的ロック例外をスローする。
	 * 
	 * @throws OptimisticLockingFailureException 楽観的ロック例外
	 */
	protected void optimisticLock() throws OptimisticLockingFailureException {
		final String errMsg = "：更新日時が異なるため、楽観的ロックエラーとする";
		throw new OptimisticLockingFailureException(getSqlMapName() + errMsg);
	}

	/**
	 * シーケンスキーを元に取得する。<br>
	 * sqlMapのselectを実行する。
	 * 
	 * @param seqKey シーケンスキー
	 * @return 取得データ
	 * @throws DataNotFoundException
	 */
	@SuppressWarnings("unchecked")
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
		Map<String, Long> paramMap = new HashMap<String, Long>(1);
		paramMap.put("seqKey", seqKey);

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
	protected DpModel selectByUniqueKey(DpModel record) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectUk", record);
	}

	/**
	 * 全カラムを指定して登録する。<br>
	 * ユニークキーで重複チェックを行い、<br>
	 * sqlMapのinsertを実行する。
	 * 
	 * @param record 登録対象データ
	 * @throws DuplicateException
	 */
	@SuppressWarnings("unchecked")
	protected void insert(DpModel record) throws DuplicateException {
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		insert(record, dpUser);
	}

	/**
	 * 全カラムを指定して登録する。<br>
	 * ユニークキーで重複チェックを行い、<br>
	 * sqlMapのinsertを実行する。
	 * 
	 * @param record 登録対象データ
	 * @param dpUser 更新者情報
	 * @throws DuplicateException
	 */
	@SuppressWarnings("unchecked")
	protected void insert(DpModel record, DpUser dpUser) throws DuplicateException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "登録情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "更新者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------
		// 重複チェック
		// -------------
		this.duplicateCheck(record);

		insertNonCheck(record, dpUser);
	}

	/**
	 * 全カラムを指定して登録する。<br>
	 * sqlMapのinsertを実行する。
	 * 
	 * @param record 登録対象データ
	 * @throws DuplicateException
	 */
	@SuppressWarnings("unchecked")
	protected void insertNonCheck(DpModel record) throws DuplicateException {
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		insert(record, dpUser);
	}

	/**
	 * 全カラムを指定して登録する。<br>
	 * sqlMapのinsertを実行する。
	 * 
	 * @param record 登録対象データ
	 * @param dpUser 更新者情報
	 * @throws DuplicateException
	 */
	@SuppressWarnings("unchecked")
	protected void insertNonCheck(DpModel record, DpUser dpUser) throws DuplicateException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "登録情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "更新者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------
		// 登録実行
		// -------------
		// 登録者・更新者情報設定
		record.setIsJgiNo(dpUser.getJgiNo());
		record.setIsJgiName(dpUser.getJgiName());
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		dataAccess.execute(getSqlMapName() + ".insert", record);
	}

	/**
	 * seqKeyで指定して更新する。<br>
	 * seqKeyで楽観的ロックチェックを行い、<br>
	 * sqlMapのupdateを実行する。
	 * 
	 * @param record 更新対象データ
	 * @return 処理件数
	 */
	@SuppressWarnings("unchecked")
	protected int update(DpModel record) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (record == null) {
			final String errMsg = "更新情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -------------
		// 更新実行
		// -------------
		// 更新者情報設定
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		record.setUpJgiNo(dpUser.getJgiNo());
		record.setUpJgiName(dpUser.getJgiName());
		int count = dataAccess.execute(getSqlMapName() + ".update", record);
		if (count == 0) {
			optimisticLock();
		}
		return count;
	}

	/**
	 * シーケンスキーを元に削除する。
	 * 
	 * @param seqKey シーケンスキー
	 * @param upDate 更新日付
	 * @return 処理件数
	 * @throws DataNotFoundException
	 */
	protected int deleteBySeqKey(Long seqKey, Date upDate) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (seqKey == null) {
			final String errMsg = "シーケンスキーがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (upDate == null) {
			final String errMsg = "更新日がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("seqKey", seqKey);
		paramMap.put("upDate", upDate);

		// -------------
		// 削除実行
		// -------------
		int count = dataAccess.execute(getSqlMapName() + ".delete", paramMap);
		if (count == 0) {
			optimisticLock();
		}
		return count;
	}

	/**
	 * 登録時の重複チェック
	 * 
	 * @param record 登録対象データ
	 * @throws DuplicateException
	 */
	@SuppressWarnings("unchecked")
	protected void duplicateCheck(DpModel record) throws DuplicateException {
		try {
			this.selectByUniqueKey(record);
			this.insertDataDuplicate();
		} catch (DataNotFoundException e) {
		}
	}
}
