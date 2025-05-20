package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JisCodeMstDao;
import jp.co.takeda.dto.JisCodeMstResultDto;
import jp.co.takeda.dto.JisCodeMstScDto;
import jp.co.takeda.model.JisCodeMst;
import jp.co.takeda.model.div.Prefecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JIS府県・市区町村の検索に関するサービス実装クラス
 * 
 * @author stakeuchi
 */
@Transactional
@Service("dpsJisCodeMstSearchService")
public class DpsJisCodeMstSearchServiceImpl implements DpsJisCodeMstSearchService {

	/**
	 * JIS府県・市区町村DAO
	 */
	@Autowired(required = true)
	@Qualifier("jisCodeMstDao")
	protected JisCodeMstDao jisCodeMstDao;

	public List<JisCodeMstResultDto> searchJisCodeMstList(JisCodeMstScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件オブジェクトがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final Prefecture addrCodePref = scDto.getAddrCodePref();
		if (addrCodePref == null) {
			final String errMsg = "都道府県コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索処理実行
		// ----------------------
		List<JisCodeMst> searchResultList = null;
		try {
			searchResultList = jisCodeMstDao.searchByTodoufukenCd(addrCodePref);
		} catch (DataNotFoundException e) {
			// 検索結果0件の場合は何もしない
		}

		return convertJisCodeMstResultDto(searchResultList);
	}

	/**
	 * JisCodeMst ⇒ JisCodeMstResultDto 変換処理
	 * 
	 * @param 変換元JisCodeMstオブジェクトリスト
	 * @return JisCodeMstResultDtoオブジェクトリスト
	 */
	private List<JisCodeMstResultDto> convertJisCodeMstResultDto(List<JisCodeMst> jisCodeMstList) {

		List<JisCodeMstResultDto> jisCodeMstResultDto = new ArrayList<JisCodeMstResultDto>();

		// 変換元がNullの場合は配列0のリストを返却
		if (jisCodeMstList == null || jisCodeMstList.isEmpty()) {
			return jisCodeMstResultDto;
		}

		for (JisCodeMst jisCodeMst : jisCodeMstList) {

			// 市区郡町村コード
			final String addrCodeCity = jisCodeMst.getShikuchosonCd();

			// 市区郡町村名
			final String addrNameCity = jisCodeMst.getShikuchosonMeiKj();

			// オブジェクト生成
			JisCodeMstResultDto resultDto = new JisCodeMstResultDto(addrCodeCity, addrNameCity, jisCodeMst);

			jisCodeMstResultDto.add(resultDto);
		}
		return jisCodeMstResultDto;
	}
}
