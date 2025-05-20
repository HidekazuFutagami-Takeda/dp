package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JgiMstRealDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dao.div.SosListType;
import jp.co.takeda.dto.SosJgiDto;
import jp.co.takeda.dto.SosJgiListDto;
import jp.co.takeda.logic.div.SosSrchValue;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;

/**
 * 組織情報検索サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmSosJgiSearchService")
public class DpmSosJgiSearchServiceImpl implements DpmSosJgiSearchService {

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstRealDao")
	protected SosMstRealDao sosMstRealDao;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstRealDao")
	protected JgiMstRealDao jgiMstRealDao;

	public String searchUpSosCode(String sosCd) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		SosMst sosMst = sosMstRealDao.searchReal(sosCd);
		return sosMst.getUpSosCd();
	}

	public SosJgiListDto search(String sosCd, String sosListType, String sosMaxSrchGetValue, boolean etcSosFlg) {
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
		SosListType sosType = SosListType.getInstance(sosListType);
		SosSrchValue sosSrchValue = SosSrchValue.getInstance(sosMaxSrchGetValue);
		try {

			// ---------------------
			// 選択した組織取得
			// ---------------------
			SosMst sosMst = null;
			if (etcSosFlg) {
				sosMst = sosMstRealDao.searchRealEtcSos(sosCd);
			} else {
				sosMst = sosMstRealDao.searchReal(sosCd);
			}
			try {
				sosMst.setSosCategoryList(sosMstRealDao.searchSosCategoryList(sosCd));
			}
			// データが見つからなくても、エラーにしない
			catch (DataNotFoundException e) {
			}

			// ---------------------
			// 選択済み組織一覧取得
			// ---------------------
			List<SosMst> sosList = selectList(sosMst);

			// 検索範囲に合わせて、選択組織を入れ帰る
			BumonRank curRank = null;
			switch (sosSrchValue) {
				case SHITEN:
					curRank = BumonRank.IEIHON;
					break;
				case OFFICE:
					curRank = BumonRank.SITEN_TOKUYAKUTEN_BU;
					break;
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
			sosMst = selectSosList.get(selectSosList.size() - 1);
			BumonRank bumonRank = sosMst.getBumonRank();

			// ---------------------
			// 下位組織一覧取得
			// ---------------------
			List<SosMst> sosMstList = new ArrayList<SosMst>();
			BumonRank searchRank = bumonRank;
			String searhSosCd = sosMst.getSosCd();
			SosMst addSosMst = sosMst;
			switch (bumonRank) {
				case IEIHON:
				case SITEN_TOKUYAKUTEN_BU:
					break;
				case OFFICE_TOKUYAKUTEN_G:
					if (sosType.equals(SosListType.SHITEN_LIST)) {
						break;
					}
					// 特約店Gの場合、特約店G一覧を取得
					// チームの場合、チーム一覧を取得
				case TEAM:
					if (sosType.equals(SosListType.SHITEN_LIST)) {
						searchRank = BumonRank.OFFICE_TOKUYAKUTEN_G;
					} else {
						searchRank = BumonRank.SITEN_TOKUYAKUTEN_BU;
					}
					searhSosCd = sosMst.getUpSosCd();
					addSosMst = selectSosList.get(selectSosList.size() - 2);
					break;
			}
			try {
				List<SosMst> tmpList = sosMstRealDao.searchRealList(SosMstRealDao.SORT_STRING, sosType, searchRank, searhSosCd);
				// 特約店Gの場合、自組織系以外は除外
				if (sosType.equals(SosListType.TOKUYAKUTEN_LIST)) {
					String targetSosCd = sosMst.getSosCd();
					for (SosMst entry : tmpList) {
						if (entry.getSosCd().equals(targetSosCd) || entry.getUpSosCd().equals(targetSosCd)) {
							sosMstList.add(entry);
						}
					}
				} else {
					sosMstList = tmpList;
				}
			} catch (DataNotFoundException e) {
				// 下位組織一覧無しでもエラーにしない
			}
			sosMstList.add(0, addSosMst);

			// ---------------------
			// 従業員（担当者）一覧取得
			// ---------------------
			List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
			if (bumonRank.equals(BumonRank.OFFICE_TOKUYAKUTEN_G) || bumonRank.equals(BumonRank.TEAM)) {
				try {
					jgiMstList = jgiMstRealDao.searchRealBySosCd(sosMst.getSosCd(), sosType, bumonRank);
				} catch (DataNotFoundException e) {
					// 組織配下に従業員無しでもエラーにしない
				}
			}

			// ---------------------
			// 結果情報生成
			// ---------------------
			return new SosJgiListDto(selectSosList, sosMstList, jgiMstList);
		} catch (DataNotFoundException e) {
			final String errMsg = "組織・従業員検索で組織情報が存在しない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}

	public SosJgiDto search(String sosCd, Integer jgiNo, String sosMaxSrchGetValue, boolean etcSosFlg) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosMaxSrchGetValue == null) {
			final String errMsg = "最大検索範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		try {
			// ---------------------
			// 選択した従業員取得
			// ---------------------
			JgiMst jgiMst = null;
			if (jgiNo != null) {
				jgiMst = jgiMstRealDao.searchReal(jgiNo);

				// 営業所雑担当の場合、雑組織フラグをONにする。（ONCは含まない）
				etcSosFlg = BooleanUtils.isTrue(jgiMst.getEtcSosFlg());
			}

			if (StringUtils.isEmpty(sosCd)) {
				if (jgiNo == null) {
					// 組織コード・従業員が未設定の場合、本部で検索
					sosCd = SosMst.SOS_CD1;
				} else {
					// 組織コードが未設定・従業員が設定ありの場合、従業員の所属組織まで検索
					sosCd = jgiMst.getSosCd();
				}
			}
			// ---------------------
			// 選択した組織取得
			// ---------------------
			SosMst selSosMst = null;
			if (etcSosFlg) {
				selSosMst = sosMstRealDao.searchRealEtcSos(sosCd);
			} else {
				selSosMst = sosMstRealDao.searchReal(sosCd);
			}
			try {
				selSosMst.setSosCategoryList(sosMstRealDao.searchSosCategoryList(sosCd));
			}
			// データが見つからなくても、エラーにしない
			catch (DataNotFoundException e) {
			}

			// ---------------------
			// 選択済み組織一覧取得
			// ---------------------
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

			if (sosJgiName.equals("")) {
				sosJgiName = selSosMst.getBumonSeiName();
			}

			return new SosJgiDto(selectSosList, sosJgiName, sosCd2, sosCd3, sosCd4, jgiMst);
		} catch (DataNotFoundException e) {
			final String errMsg = "組織・従業員検索で組織情報が存在しない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg), e);
		}
	}

	// 組織検索
	public SosMst searchSosMstWithEtcSos(String sosCd, String bumonRank) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (StringUtils.isBlank(sosCd)) {
			final String errMsg = "組織コードがnullまたは空";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		SosMst sosMst = null;
		try {
			sosMst = sosMstRealDao.searchReal(sosCd);
			// チームの場合、雑組織かを検査
			if (bumonRank != null && bumonRank.equals(BumonRank.TEAM.getDbValue().toString())) {
				if (BumonRank.OFFICE_TOKUYAKUTEN_G.equals(sosMst.getBumonRank())) {
					sosMst = sosMstRealDao.searchRealEtcSos(sosCd);
				}
			}
			sosMst.setSosCategoryList(sosMstRealDao.searchSosCategoryList(sosCd));
		} catch (DataNotFoundException e) {
			// 何もしない
		}
		return sosMst;
	}

	// 従業員検索
	public JgiMst searchJgiMst(Integer jgiNo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		return jgiMstRealDao.searchReal(jgiNo);
	}

	/**
	 * 引数の組織情報を元に、上位の組織リストを取得する。
	 *
	 * @param sosMst 組織情報
	 * @return 上位組織一覧
	 * @throws DataNotFoundException
	 */
	private List<SosMst> selectList(SosMst sosMst) throws DataNotFoundException {
		// ---------------------
		// 上位組織一覧取得
		// ---------------------
		List<SosMst> selectSosList = new ArrayList<SosMst>();
		selectSosList.add(sosMst);
		String upSoscd = sosMst.getUpSosCd();
		BumonRank upBumonRank = sosMst.getBumonRank();
		while (!upBumonRank.equals(BumonRank.IEIHON)) {
			SosMst tmpSosMst = sosMstRealDao.searchReal(upSoscd);
			selectSosList.add(0, tmpSosMst);
			upSoscd = tmpSosMst.getUpSosCd();
			upBumonRank = tmpSosMst.getBumonRank();
		}
		return selectSosList;
	}
}
