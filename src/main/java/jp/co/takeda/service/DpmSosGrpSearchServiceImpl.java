package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.SosGrpDao;
import jp.co.takeda.model.SosGrp;

/**
 * 組織グループ検索サービス実装クラス
 *
 * @author rna8405
 *
 */
@Transactional
@Service("dpmSosGrpSearchService")
public class DpmSosGrpSearchServiceImpl implements DpmSosGrpSearchService {

	/**
	 * 組織グループDAO
	 */
	@Autowired(required = true)
	@Qualifier("sosGrpDao")
	protected SosGrpDao sosGrpDao;

	@Override
	public List<SosGrp> searchSosGrp(String sosCd) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<SosGrp> sosGrp = new ArrayList<SosGrp>();

		try {
			sosGrp = sosGrpDao.search(sosCd);
		} catch (DataNotFoundException e) {
			// 何もしない
		}
		return sosGrp;
	}
}
