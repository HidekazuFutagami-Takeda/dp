package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MainSubRelationDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.SosJgiDto;
import jp.co.takeda.dto.SosJgiListDto;
import jp.co.takeda.logic.div.SosSrchValue;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JokenSet;

/**
 * 組織情報検索サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsSosJgiSearchService")
public class DpsSosJgiSearchServiceImpl implements DpsSosJgiSearchService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 施設主副担当関連DAO
	 */
	@Autowired(required = true)
	@Qualifier("mainSubRelationDAO")
	protected MainSubRelationDAO mainSubRelationDAO;

	/**
	 * 組織カテゴリコード検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsSosCtgSearchService")
	protected DpsSosCtgSearchService dpsSosCtgSearchService;

	// 組織情報を取得する。
	public SosMst searchSos(String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "下位組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織コードから組織情報取得
		return sosMstDAO.search(sosCd);
	}

	// 配下組織の一覧を取得する。
	public List<SosMst> searchUnderSosList(String sosCd, SosListType sosListType) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "下位組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		SosMst sosMst = sosMstDAO.search(sosCd);

		// ----------------------
		// 処理実行
		// ----------------------
		List<SosMst> sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, sosListType, sosMst.getBumonRank(), sosCd);
		return sosMstList;
	}

	// 上位組織の組織コードを取得する。
	public String searchUpSosCode(String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "下位組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 処理実行
		// ----------------------
		SosMst sosMst = sosMstDAO.search(sosCd);
		return sosMst.getUpSosCd();
	}

	// 組織階層・従業員一覧の検索処理
	public SosJgiListDto search(String sosCd, String sosListType, String sosMaxSrchGetValue) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosListType == null) {
			final String errMsg = "組織検索パターンがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMaxSrchGetValue == null) {
			final String errMsg = "最大検索範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織コードが未設定の場合、本部で検索
		if (StringUtils.isEmpty(sosCd)) {
			sosCd = SosMst.SOS_CD1;
		}

		// 医薬かワクチンか
		SosListType sosType = SosListType.getInstance(sosListType);

		// 検索する階層(支店～従業員)
		SosSrchValue sosSrchValue = SosSrchValue.getInstance(sosMaxSrchGetValue);

		try {

			// --------------------------------------------
			// selectSosList := 画面上部のパンくずリスト
			// --------------------------------------------

			// 選択した組織取得
			SosMst sosMst;

			// 2014下期向け支援改定にて、整形対応削除
//			sosMst = sosMstDAO.searchIncludeSeikei(sosCd);
			sosMst = sosMstDAO.search(sosCd);

			try {
				sosMst.setSosCategoryList(sosMstDAO.searchSosCategoryList(sosCd));
			}
			// データが見つからなくても、エラーにしない
			catch (DataNotFoundException e) {
			}

			// 選択済み組織一覧取得
			List<SosMst> sosList = selectList(sosMst);

			// 検索範囲に合わせて、選択組織を入れ帰る
			BumonRank curRank = null;
			switch (sosSrchValue) {
				case SHITEN:
					curRank = BumonRank.IEIHON;
					break;
				case OFFICE:
					// 管理側はOFFICEの場合でもTEAMの動きを指定しているので、それに合わせる
					//curRank = BumonRank.SITEN_TOKUYAKUTEN_BU;
					//break;
				case TEAM:
					curRank = BumonRank.OFFICE_TOKUYAKUTEN_G;
					break;
				case JGI:
					curRank = sosMst.getBumonRank();
					break;
			}
			List<SosMst> selectSosList = new ArrayList<SosMst>();
			for (SosMst mst : sosList) {
				selectSosList.add(mst);
				if (mst.getBumonRank().equals(curRank)) {
					break;
				}
			}

			// --------------------------------------------
			// sosMstList := 組織選択部分のリスト
			// --------------------------------------------
			sosMst = selectSosList.get(selectSosList.size() - 1);
			BumonRank bumonRank = sosMst.getBumonRank();

			// 下位組織一覧取得
			List<SosMst> sosMstList = new ArrayList<SosMst>();
			BumonRank searchRank = bumonRank;
			String searhSosCd = sosMst.getSosCd();
			SosMst addSosMst = sosMst;

			// [選択した組織]の部門ランクがチームの場合、同一階層の組織を取得する。
			// これは、この階層までは、自組織＋下位組織一覧なのに対して、
			// この階層以降は、上位組織＋自チームを表示する仕様のため。
			switch (bumonRank) {
				case IEIHON:
				case SITEN_TOKUYAKUTEN_BU:
				case OFFICE_TOKUYAKUTEN_G:
					break;
				case TEAM:
					searchRank = BumonRank.OFFICE_TOKUYAKUTEN_G;
					searhSosCd = sosMst.getUpSosCd();
					addSosMst = selectSosList.get(selectSosList.size() - 2);
					break;
			}
			try {

				// 2014下期向け支援改定にて、整形対応削除
//				sosMstList = sosMstDAO.searchListIncludeSeikei(SosMstDAO.SORT_STRING, sosType, searchRank, searhSosCd);
				sosMstList = sosMstDAO.searchList(SosMstDAO.SORT_STRING, sosType, searchRank, searhSosCd);

			} catch (DataNotFoundException e) {
				// 下位組織一覧無しでもエラーにしない
			}
			sosMstList.add(0, addSosMst);

			// --------------------------------------------
			// jgiMstList := 従業員選択部分のリスト
			// --------------------------------------------

			// 従業員一覧取得
			List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
			if (bumonRank.equals(BumonRank.OFFICE_TOKUYAKUTEN_G) || bumonRank.equals(BumonRank.TEAM)) {
				try {

					// 2014下期向け支援改定にて、整形対応削除
//					jgiMstList = jgiMstDAO.searchIncludeSeikeiBySosCd(JgiMstDAO.SORT_STRING, sosMst.getSosCd(), sosType, bumonRank);
					jgiMstList = jgiMstDAO.searchBySosCd(JgiMstDAO.SORT_STRING, sosMst.getSosCd(), sosType, bumonRank);

				} catch (DataNotFoundException e) {
					// 組織配下に従業員無しでもエラーにしない
				}
			}

			// 結果情報生成
			return new SosJgiListDto(selectSosList, sosMstList, jgiMstList);
		} catch (DataNotFoundException e) {
			final String errMsg = "組織・従業員検索で組織情報が存在しない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}

	// 組織・従業員の検索処理
	public SosJgiDto search(String sosCd, Integer jgiNo, String sosMaxSrchGetValue) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosMaxSrchGetValue == null) {
			final String errMsg = "最大検索範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 組織コードが未設定の場合、本部で検索
		if (StringUtils.isEmpty(sosCd)) {
			sosCd = SosMst.SOS_CD1;
		}
		try {

			// 選択した従業員取得
			JgiMst jgiMst = null;
			if (jgiNo != null) {
				try {
					jgiMst = jgiMstDAO.search(jgiNo);
				} catch (DataNotFoundException e) {
					jgiMst = jgiMstDAO.searchSubJgi(jgiNo);
				}
				sosCd = jgiMst.getSosCd();
			}

			// 選択した組織取得（整形含みで検索）
			// NOTE:2014下期向け支援改定にて、整形対応削除
//			SosMst selSosMst = sosMstDAO.searchIncludeSeikei(sosCd);
			SosMst selSosMst = sosMstDAO.search(sosCd);

			try {
				selSosMst.setSosCategoryList(sosMstDAO.searchSosCategoryList(sosCd));
			}
			// データが見つからなくても、エラーにしない
			catch (DataNotFoundException e) {
			}

			// 整形を除く場合、上位の組織（営業所）を取得
//			if(!includeSeikei && selSosMst.getSeikeiSosFlg()){
//				selSosMst = sosMstDAO.searchIncludeSeikei(selSosMst.getUpSosCd());
//				jgiMst = null;
//			}

			// 選択済み組織一覧取得
			List<SosMst> selectSosList = selectList(selSosMst);

			// 検索結果を格納
			SosSrchValue sosMax = SosSrchValue.getInstance(sosMaxSrchGetValue);
			String sosCd2 = null;
			String sosCd3 = null;
			String sosCd4 = null;
			String sosJgiName = "";
			for (SosMst sosMst : selectSosList) {
				switch (sosMst.getBumonRank()) {
					case SITEN_TOKUYAKUTEN_BU:
						sosCd2 = sosMst.getSosCd();
						sosJgiName = sosMst.getBumonSeiName();
						break;
					case OFFICE_TOKUYAKUTEN_G:
						sosCd3 = sosMst.getSosCd();
						switch (sosMax) {
							case OFFICE:
							case TEAM:
							case JGI:
								sosJgiName = sosJgiName + " " + sosMst.getBumonSeiName();
								break;
						}
						break;
					case TEAM:
						sosCd4 = sosMst.getSosCd();
						switch (sosMax) {
							case TEAM:
							case JGI:
								sosJgiName = sosJgiName + " " + sosMst.getBumonSeiName();
								break;
						}
						break;
				}
			}

			if (jgiMst != null && SosSrchValue.JGI.equals(sosMax)) {
				sosJgiName = sosJgiName + " " + jgiMst.getJgiName();
			}
			return new SosJgiDto(selectSosList, sosJgiName, sosCd2, sosCd3, sosCd4, jgiMst);

		} catch (DataNotFoundException e) {
			final String errMsg = "組織・従業員検索で組織情報が存在しない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}

	/**
	 * 引数の組織情報を元に、上位の組織リストを取得する。
	 * NOTE:2014下期向け支援改定にて、整形対応削除
	 *
	 * @param sosMst 組織情報
	 * @return 上位組織一覧
	 * @throws DataNotFoundException
	 */
//	private List<SosMst> selectList(SosMst sosMst,boolean includeSeikei) throws DataNotFoundException {
	private List<SosMst> selectList(SosMst sosMst) throws DataNotFoundException {

		// 上位組織一覧取得
		List<SosMst> selectSosList = new ArrayList<SosMst>();
		selectSosList.add(sosMst);
		String upSoscd = sosMst.getUpSosCd();
		BumonRank upBumonRank = sosMst.getBumonRank();
		while (!upBumonRank.equals(BumonRank.IEIHON)) {
			SosMst tmpSosMst;
			// NOTE:2014下期向け支援改定にて、整形対応削除
//			tmpSosMst = sosMstDAO.searchIncludeSeikei(upSoscd);
			tmpSosMst = sosMstDAO.search(upSoscd);
			selectSosList.add(0, tmpSosMst);
			upSoscd = tmpSosMst.getUpSosCd();
			upBumonRank = tmpSosMst.getBumonRank();
		}
		return selectSosList;
	}

	// 施設主担当検索
	public List<JgiMst> searchMainMrList(Integer jgiNo) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 処理実行
		// ----------------------
		List<JgiMst> resultList = new ArrayList<JgiMst>();

		try {
			resultList = mainSubRelationDAO.searchList(jgiNo);
		} catch (DataNotFoundException e) {
		}

		// ----------------------
		// 先頭に自身を追加する
		// ----------------------

		// 従業員情報検索
		JgiMst jgiMst = null;
		try {
			jgiMst = jgiMstDAO.search(jgiNo);
		} catch (DataNotFoundException e) {
		}

		// 従業員情報にいない場合は、副担当情報を検索
		if(jgiMst == null){
			try {
				jgiMst = jgiMstDAO.searchSubJgi(jgiNo);
			} catch (DataNotFoundException e1) {
			}
		}

		// 1件目に追加
		if(jgiMst != null){
			resultList.add(0,jgiMst);
		}

		return resultList;
	}

	/**
	 * 指定された従業員と所属のチームリーダを取得する。
	 * @param jgiNo 従業員番号
	 * @return 従業員情報リスト（MR、チームリーダ）
	 */
	public List<JgiMst> searchTlMrList(Integer jgiNo) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ----------------------
		// 処理実行
		// ----------------------
		List<JgiMst> resultList = new ArrayList<JgiMst>();
		try {
			resultList = jgiMstDAO.searchByJokenSetList(jgiNo, JokenSet.IYAKU_AL, JokenSet.IYAKU_ONC_AL);
		} catch (DataNotFoundException e) {
		}

		return resultList;
	}
}
