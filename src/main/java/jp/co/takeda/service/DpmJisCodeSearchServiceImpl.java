package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JisCodeMstDao;
import jp.co.takeda.dto.AddrScDto;
import jp.co.takeda.dto.AddrSearchResultDto;
import jp.co.takeda.dto.AddrSearchResultListDto;
import jp.co.takeda.model.JisCodeMst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JIS府県・市区町村に関するサービス実装クラス
 * 
 * @author khashimoto
 */
@Transactional
@Service("dpmJisCodeSearchService")
public class DpmJisCodeSearchServiceImpl implements DpmJisCodeSearchService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * JIS府県・市区町村DAO
	 */
	@Autowired(required = true)
	@Qualifier("jisCodeMstDao")
	protected JisCodeMstDao jisCodeMstDao;

	// JIS府県・市区町村検索
	public AddrSearchResultListDto search(AddrScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "JIS府県・市区町村検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// JIS府県・市区町村検索実行
		List<JisCodeMst> jisCodeMstList = null;
		jisCodeMstList = jisCodeMstDao.searchList(scDto);

		// 結果格納DTO生成
		List<AddrSearchResultDto> addrSearchResultList = new ArrayList<AddrSearchResultDto>();
		for (JisCodeMst mst : jisCodeMstList) {
			addrSearchResultList.add(new AddrSearchResultDto(mst.getTodoufukenCd(), mst.getFukenMeiKj(), mst.getShikuchosonCd(), mst.getShikuchosonMeiKj()));
		}

		return new AddrSearchResultListDto(addrSearchResultList);
	}

}
