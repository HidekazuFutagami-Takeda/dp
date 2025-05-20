package jp.co.takeda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.YakkouSijouDAO;
import jp.co.takeda.model.YakkouSijou;

/**
 * 薬効市場検索サービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpsYakkouSijouSearchService")
public class DpsYakkouSijouSearchServiceImpl implements DpsYakkouSijouSearchService {

	/**
	 * 薬効市場DAO
	 */
	@Autowired(required = true)
	@Qualifier("yakkouSijouDAO")
	protected YakkouSijouDAO yakkouSijouDAO;

	// 薬効市場リストを取得
	public List<YakkouSijou> searchList(String category) {
		try {
			return yakkouSijouDAO.searchList(YakkouSijouDAO.SORT_STRING, category);
		} catch (DataNotFoundException e) {
			final String errMsg = "薬効市場が登録されていないため、システムエラー";
			throw new SystemException(new Conveyance(ErrMessageKey.SYSTEM_ERROR, errMsg));
		}
	}
}
