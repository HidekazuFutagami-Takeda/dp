package jp.co.takeda.dao;

/**
 * ユーザIDから従業員番号を引き当てる共有プロシージャへのDAOインターフェース
 * @author UJU3389
 *
 */
public interface ComUserInfoDao {

	int selectLoginJgiNoByUserId(String userId);
}
