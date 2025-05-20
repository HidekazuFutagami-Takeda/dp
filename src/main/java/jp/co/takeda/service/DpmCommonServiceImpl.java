package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.CalDao;
import jp.co.takeda.model.Cal;

/**
 * 管理の共通サービス実装クラス
 *
 * @author rna8405
 */
@Transactional
@Service("dpmCommonService")
public class DpmCommonServiceImpl implements DpmCommonService {

	/**
	 * カレンダーDAO
	 */
	@Autowired(required = true)
	@Qualifier("calDao")
	protected CalDao calDao;

	@Override
	public Cal searchToday() {
		Cal today = new Cal();
		try {
			today = calDao.searchToday();
		} catch (DataNotFoundException e) {
			final String errMsg = "本日のカレンダーが存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		return today;
	}

    //add Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	@Override
	public Cal searchBizDays(String calYear, String calMonth, Integer bizDays) {
		Cal today = new Cal();
		try {
			today = calDao.searchBizDays(calYear,calMonth,bizDays);
		} catch (DataNotFoundException e) {
			final String errMsg = "月内営業日順位で検索した日のカレンダーが存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}
		return today;
	}
    //add End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示

}
