package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dto.SosListDto;
import jp.co.takeda.dto.TmsTytenMstHontenDto;
import jp.co.takeda.dto.TmsTytenMstHontenListDto;
import jp.co.takeda.dto.TmsTytenMstListDto;
import jp.co.takeda.dto.TmsTytenMstResultsListDto;
import jp.co.takeda.dto.TmsTytenMstResultsTenkaiListDto;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.dto.TmsTytenMstTenkaiDto;
import jp.co.takeda.dto.TmsTytenMstTenkaiListDto;
import jp.co.takeda.logic.TmsTytenTenkaiListSearchLogic;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.TytenKisLevel;

/**
 * 特約店情報に関するサービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsTmsTytenMstSearchService")
public class DpsTmsTytenMstSearchServiceImpl implements DpsTmsTytenMstSearchService {

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 特約店情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnDAO")
	protected TmsTytenMstUnDAO tmsTytenMstUnDAO;

	//
	public SosListDto search() throws LogicalException {

		// 検索実行
		List<SosMst> resultList;
		try {
			resultList = sosMstDAO.searchTokuyakutenbuList();
		} catch (DataNotFoundException e) {
			final String errMsg = "特約店部階層の情報が存在しない";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg), e);
		}
		return new SosListDto(resultList);
	}

	//
	public TmsTytenMstHontenListDto search(TmsTytenMstScDto scDto) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// add Start 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		// 組織名取得
		String sosName = null;
		String addrSosCode = null;

		if(StringUtils.isEmpty(scDto.getAddrCodePref())) {
			sosName = getSosName(scDto.getSosCd2());
			addrSosCode = scDto.getSosCd2();
		} else {
			addrSosCode =  getSosCode(scDto.getAddrCodePref());
			sosName = getSosName(addrSosCode);
			scDto.setAddrSosCode(addrSosCode);
		}
		// add End 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		// 検索実行
		List<TmsTytenMstHontenDto> resultList;
		try {
			resultList = tmsTytenMstUnDAO.searchHontenList(TmsTytenMstUnDAO.SORT_STRING, scDto);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		// mod 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		return new TmsTytenMstHontenListDto(sosName, addrSosCode, resultList);
	}

	//
	public TmsTytenMstListDto search(String tmsTytenCd, String sosCd2) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (tmsTytenCd == null) {
			final String errMsg = "特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織名取得
		String sosName = getSosName(sosCd2);

		// 選択した特約店情報取得
		TmsTytenMstUn tmsTytenMstUn = getTmsTyten(tmsTytenCd);

		// 検索条件生成
		String honten = tmsTytenMstUn.getTmsTytenCdHonten();
		TytenKisLevel level = TytenKisLevel.SHISHA;
		TmsTytenMstScDto tmsTytenMstScDto = new TmsTytenMstScDto(level, null, honten, null, null, null, null, null, null, null);

		// 検索実行
		List<TmsTytenMstUn> resultList = searchExecute(tmsTytenMstScDto);

		// リストの先頭に本店をプラス
		resultList.add(0, tmsTytenMstUn);
		return new TmsTytenMstListDto(sosName, tmsTytenMstUn, resultList);
	}

	//
	public TmsTytenMstTenkaiListDto search(String hontenTmsTytenCd, String shishaTmsTytenCd, TmsTytenMstScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (hontenTmsTytenCd == null) {
			final String errMsg = "特約店コード（本店）がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織情報取得
		String sosName = getSosName(scDto.getSosCd2());

		// 選択した特約店情報取得
		String tmsTytenCd = hontenTmsTytenCd;
		if (StringUtils.isNotEmpty(shishaTmsTytenCd)) {
			tmsTytenCd = shishaTmsTytenCd;
		}
		TmsTytenMstUn tmsTytenMstUn = getTmsTyten(tmsTytenCd);

		// 検索ロジック実行
		TmsTytenTenkaiListSearchLogic logic = new TmsTytenTenkaiListSearchLogic(tmsTytenMstUnDAO, tmsTytenMstUn, scDto);
		List<TmsTytenMstTenkaiDto> resultList = logic.execute();

		return new TmsTytenMstTenkaiListDto(sosName, tmsTytenMstUn, resultList);
	}

	//
	public TmsTytenMstResultsListDto searchResultsList(String sosCd, TmsTytenMstScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 検索実行
		List<TmsTytenMstUn> resultList;
		try {
			resultList = tmsTytenMstUnDAO.searchResultsList(TmsTytenMstUnDAO.SORT_STRING, sosCd, scDto);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		return new TmsTytenMstResultsListDto(resultList);
	}

	//
	public TmsTytenMstResultsTenkaiListDto searchResultsTytenList(String tmsTytenCd, TmsTytenMstScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (tmsTytenCd == null) {
			final String errMsg = "特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 選択した特約店情報取得
		TmsTytenMstUn tmsTytenMstUn = getTmsTyten(tmsTytenCd);

		// 検索ロジック実行
		TmsTytenTenkaiListSearchLogic logic = new TmsTytenTenkaiListSearchLogic(tmsTytenMstUnDAO, tmsTytenMstUn, scDto);
		List<TmsTytenMstTenkaiDto> resultList = logic.execute();
		return new TmsTytenMstResultsTenkaiListDto(tmsTytenMstUn, resultList);
	}

	/**
	 * 組織名を取得する。
	 *
	 * @param sosCd2 組織コード
	 * @return 組織名（コードがNULLの場合、NULL）
	 */
	private String getSosName(String sosCd2) {
		// 組織情報取得
		String sosName = null;
		if (StringUtils.isNotEmpty(sosCd2)) {
			try {
				SosMst sosMst = sosMstDAO.search(sosCd2);
				sosName = sosMst.getBumonSeiName();
			} catch (DataNotFoundException e) {
				final String errMsg = "特約店部情報が存在しない";
				throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg), e);
			}
		}
		return sosName;
	}

	/**
	 * 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
	 * 組織名を取得する。
	 *
	 * @param addrCodePref 組織コード
	 * @return 組織名（コードがNULLの場合、NULL）
	 */
	private String getSosCode(String addrCodePref) {
		// 組織情報取得
		String sosCode = null;
		try {
			SosMst sosMst = sosMstDAO.searchBumon(addrCodePref);
			sosCode = sosMst.getAddrCodePref();
		} catch (DataNotFoundException e) {
			final String errMsg = "特約店部情報が存在しない";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg), e);
		}
		return sosCode;
	}

	/**
	 * 特約店情報を取得する。
	 *
	 * @param tmsTytenCd TMS特約店コード
	 * @return 特約店情報
	 */
	private TmsTytenMstUn getTmsTyten(String tmsTytenCd) {
		try {
			return tmsTytenMstUnDAO.search(tmsTytenCd);
		} catch (DataNotFoundException e) {
			final String errMsg = "選択した特約店情報が存在しない";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg), e);
		}
	}

	/**
	 * 検索実行
	 *
	 * @param scDto 特約店情報の検索条件DTO
	 * @return 特約店情報のリスト
	 * @throws LogicalException
	 */
	private List<TmsTytenMstUn> searchExecute(TmsTytenMstScDto scDto) throws LogicalException {
		List<TmsTytenMstUn> resultList;
		try {
			resultList = tmsTytenMstUnDAO.searchList(TmsTytenMstUnDAO.SORT_STRING, scDto);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
		return resultList;
	}
}
