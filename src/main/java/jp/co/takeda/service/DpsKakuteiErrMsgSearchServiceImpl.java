package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsKakuteiErrMsgDao;
import jp.co.takeda.dto.DpsKakuteiErrMsgDto;
import jp.co.takeda.model.DpsKakuteiErrMsg;


/**
 *納入計画管理検索サービス実装クラス
 *
 */
@Transactional
@Service("dpsKakuteiErrMsgSearchService")
public class DpsKakuteiErrMsgSearchServiceImpl implements DpsKakuteiErrMsgSearchService {


	/**
	 * 一括確定エラー情報ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("dpsKakuteiErrMsgDao")
	protected DpsKakuteiErrMsgDao dpsKakuteiErrMsgDao;


	public List<DpsKakuteiErrMsgDto> search() throws LogicalException{

		// -----------------------------
		// 引数チェック
		// -----------------------------


		// -----------------------------
		// 検索処理
		// -----------------------------
		List<DpsKakuteiErrMsg> dpsKakuteiErrMsg = new ArrayList<DpsKakuteiErrMsg>();
		List<DpsKakuteiErrMsgDto> dpsKakuteiErrMsgDtoList = new ArrayList<DpsKakuteiErrMsgDto>();

		try {
			dpsKakuteiErrMsg = dpsKakuteiErrMsgDao.search();
		} catch (DataNotFoundException e) {
			final String errMsg = "一括確定エラー情報が存在しない";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		for(DpsKakuteiErrMsg errMsg :dpsKakuteiErrMsg){
			dpsKakuteiErrMsgDtoList.add(
				new DpsKakuteiErrMsgDto(errMsg.getSosCd2(), errMsg.getBumonRyakuName2(),
						errMsg.getSosCd3(), errMsg.getBumonRyakuName3(),
						errMsg.getJgiNo(), errMsg.getJgiName(),
						errMsg.getErrMessage(), errMsg.getProdMessage(),
						null));
		}

		return dpsKakuteiErrMsgDtoList;
	}

	public void insert(String sosCd, Integer jgiNo, String prodCode, String errMsg) {
		dpsKakuteiErrMsgDao.insert(sosCd, jgiNo, prodCode, errMsg);
	}

	public void delete() {
		dpsKakuteiErrMsgDao.delete();
	}


}
