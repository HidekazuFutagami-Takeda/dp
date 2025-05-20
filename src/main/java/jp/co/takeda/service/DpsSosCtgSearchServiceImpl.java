package jp.co.takeda.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCViSosCtgDao;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.div.CodeMaster;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.view.SosCtg;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 計画支援の組織カテゴリコード検索サービス実装クラス
 * @author rna8405
 *
 */
@Transactional
@Service("dpsSosCtgSearchService")
public class DpsSosCtgSearchServiceImpl implements DpsSosCtgSearchService {

	/**
	 * 組織カテゴリDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCViSosCtgDao")
	protected DpsCViSosCtgDao dpsCViSosCtgDao;

    /**
     * コードマスタDAO
     */
    @Autowired(required = true)
    @Qualifier("dpsCodeMasterDao")
    protected DpsCodeMasterDao dpsCodeMasterDao;

	// 組織のカテゴリ一覧を取得する
	@Override
	public List<SosCtg> searchDpsSosCtgList(String sosCd, String screenId) {

		List<SosCtg> sosCtgList = new ArrayList<SosCtg>();

		try {
			sosCtgList = dpsCViSosCtgDao.search(sosCd);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		    return sosCtgList;
		}
		// 従業員がワクチンのみに参照可能な場合は、ワクチンのみに絞る
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
        DpUser user = userInfo.getSettingUser();
        // 条件グループの特定
        JknGrp jknGrp = null;
        for(JknGrp tmpJknGrp : user.getJknGrpList()) {
            if(screenId.toUpperCase().substring(0, 6).equals(tmpJknGrp.getJknGrpId().getDbValue())) {
                jknGrp = tmpJknGrp;
                break;
            }
        }
        if(jknGrp.getJokenKbn().equals(JokenKbn.VAC_REFER) ||
                jknGrp.getJokenKbn().equals(JokenKbn.VAC_EDIT)) {
            List<DpsCCdMst> codeList;
            try {
                codeList = dpsCodeMasterDao.searchCodeByDataKbn(CodeMaster.VAC.getDbValue());
            } catch (DataNotFoundException e) {
                final String errMsg = "計画支援汎用マスタにワクチンのカテゴリコードが登録されていません。";
                throw new SystemException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, errMsg));
            }
            if(codeList.size() > 1) {
                final String errMsg = "計画支援汎用マスタにワクチンのカテゴリコードが複数登録されています。";
                throw new SystemException(new Conveyance(ErrMessageKey.DB_DUPLICATE_ERROR, errMsg));
            }

            List<SosCtg> changeSosCtgList = new ArrayList<SosCtg>();
            SosCtg vacCtg = new SosCtg();
//            vacCtg.setCategory(codeList.get(0).getDataCd());
            for (SosCtg sosCtg :sosCtgList) {
            	// 組織に紐づくカテゴリの中で、ワクチンがある場合
            	if (sosCtg.getCategory().contains(codeList.get(0).getDataCd())){
                	vacCtg.setCategory(codeList.get(0).getDataCd());
                	break;
                }else {
                	// ない場合は、無し
                	vacCtg.setCategory(null);
                }
            }
            vacCtg.setSosCd(sosCd);
            changeSosCtgList.add(vacCtg);
            sosCtgList = changeSosCtgList;
        }

		return sosCtgList;
	}
}
