package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.SosMst;

/**
 * 営業所計画立案ステータスの検索に関するサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsOfficePlanStatusSearchService")
public class DpsOfficePlanStatusSearchServiceImpl implements DpsOfficePlanStatusSearchService {

	/**
	 * 営業所計画立案ステータスDAO
	 */
	@Autowired(required = true)
	@Qualifier("officePlanStatusDao")
	protected OfficePlanStatusDao officePlanStatusDao;

	/**
	 * 組織情報(取込)DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	// 営業所計画立案ステータスを取得する。
	public OfficePlanStatus searchMMP(String sosCd3) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 組織情報
		// ----------------------
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);

		} catch (DataNotFoundException e) {
			final String errMsg = "対象営業所が取得できない：" + sosCd3;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　組織マスタ.OncSosFlgでのカテゴリー判定を、組織マスタ.category、subCategoryを参照に変更
//		// 組織からカテゴリ判定（MMP or ONC）
//		ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
		// 組織からカテゴリ判定
//		ProdCategory category = ProdCategory.getInstance(sosMst.getSosCategory());
		String category = sosMst.getSosCategory();
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　組織マスタ.OncSosFlgでのカテゴリー判定を、組織マスタ.category、subCategoryを参照に変更

		return officePlanStatusDao.search(sosCd3, category);
	}

}
