package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.SosChoseiSummaryDao;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dto.ChoseiData;
import jp.co.takeda.dto.ChoseiDataParamsDto;
import jp.co.takeda.dto.ChoseiDataResultDetailDto;
import jp.co.takeda.dto.ChoseiDataResultDto;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.view.SosChoseiSummary;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 調整金額テーブルを検索するサービスクラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpmChoseiDataSearchService")
public class DpmChoseiDataSearchServiceImpl implements DpmChoseiDataSearchService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpmChoseiDataSearchServiceImpl.class);

	/**
	 * 納入計画管理DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosChoseiSummaryDao")
	protected SosChoseiSummaryDao sosChoseiSummaryDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstRealDao")
	protected SosMstRealDao sosMstRealDao;

	/**
	 * 組織・従業員検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmSosJgiSearchService")
	protected DpmSosJgiSearchService dpmSosJgiSearchService;

	// 調整金額情報の最終更新日時を検索
	public Calendar getChoseiLastUpDate() {

		// ------------------------------------
		// 調整金額情報の最終更新日時
		// ------------------------------------
		Date date = sosChoseiSummaryDao.getLastUpDate();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.getTime(); // 念のため

		return cal;
	}

	// 検索条件を生成
	public ChoseiDataParamsDto createParams(String targetSosCd, String targetBumonRank, ChoseiDataParamsDto beforeParams) {

		// ------------------------------------------------
		// 展開対象の組織コードが指定されていない場合
		// ------------------------------------------------
		if (StringUtils.isBlank(targetSosCd)) {

			if (beforeParams == null) {
				List<ChoseiData> openSosCdList = new ArrayList<ChoseiData>();
				return new ChoseiDataParamsDto(openSosCdList);
			} else {
				return beforeParams;
			}
		}

		// ------------------------------------------------
		// 展開対象の組織コードが指定されている場合
		// ------------------------------------------------
		else {
			ChoseiData value = new ChoseiData(targetSosCd, targetBumonRank);
			if (beforeParams == null) {
				List<ChoseiData> openSosCdList = new ArrayList<ChoseiData>();
				openSosCdList.add(value);
				return new ChoseiDataParamsDto(openSosCdList);
			} else {
				List<ChoseiData> openSosCdList = beforeParams.getOpenSosCdList();
				if (openSosCdList.contains(value)) {
					openSosCdList.remove(value);
				} else {
					openSosCdList.add(value);
				}
				return beforeParams;
			}
		}
	}

	// 調整金額テーブルを検索
	public ChoseiDataResultDto search(ChoseiDataParamsDto params) {

		// ------------------------------------
		// 引数チェック
		// ------------------------------------
		if (params == null) {
			final String errMsg = "調整金額テーブル検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ------------------------------------
		// 検索処理開始
		// ------------------------------------

		// 展開対象の組織(ユーザが操作して展開した組織コードのリスト)
		List<ChoseiData> openSosCdList = params.getOpenSosCdList();

		// 設定中のユーザ情報
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();

		// -----------------------------
		// 本部ユーザの場合(参照可能組織レベル 全項目参照可能）
		// -----------------------------
		if (dpUser.isSosLvl(JknGrpId.MENU.getDbValue(), SosLvl.ALL)) {

			// 全社の取得
			ChoseiDataResultDetailDto zensya = null;
			try {
				SosChoseiSummary tmp = sosChoseiSummaryDao.searchIyaku();
				zensya = new ChoseiDataResultDetailDto(tmp, 0);
			} catch (DataNotFoundException e) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "本部ユーザでログインしたが調整計画の全社計画が取得出来ない";
					LOG.error(errMsg);
				}
			}

			// 支店一覧
			List<ChoseiDataResultDetailDto> sitenList = null;
			try {
				sitenList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchShitenList(), 1);
			} catch (DataNotFoundException e) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "本部ユーザでログインしたが調整計画の支店一覧が取得出来ない";
					LOG.error(errMsg);
				}
			}

			// 展開する組織を取得
			if (openSosCdList == null || openSosCdList.size() < 1) {
				return new ChoseiDataResultDto(zensya, sitenList, null, null, null, null);
			}

			// 検索と格納
			return createChoseiDataResultDto(dpUser, zensya, sitenList, openSosCdList);
		}

		// -----------------------------
		// 支店の場合
		// -----------------------------
		else if (dpUser.isSosLvl(JknGrpId.MENU.getDbValue(), SosLvl.BRANCH)) {

			// 全社
			ChoseiDataResultDetailDto zensya = new ChoseiDataResultDetailDto(SosMst.SOS_CD1, "全社", 0, false);

			// 支店一覧
			List<ChoseiDataResultDetailDto> sitenList = null;
			try {
				String sosCd = dpUser.getSosCd2();
				List<ChoseiDataResultDetailDto> tmpList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchShitenList(), 1);
				for (ChoseiDataResultDetailDto entry : tmpList) {
					if (sosCd.equals(entry.getSosChoseiSummary().getSosCd())) {
						sitenList = new ArrayList<ChoseiDataResultDetailDto>();
						entry.setLinkFlg(false);
						sitenList.add(entry);
						break;
					}
				}
			} catch (DataNotFoundException e) {
			}

			if (sitenList == null || sitenList.isEmpty()) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "支店ユーザでログインしたが調整計画の対象支店が取得出来ない";
					LOG.error(errMsg);
				}
			}

			// 検索と格納
			return createChoseiDataResultDto(dpUser, zensya, sitenList, openSosCdList);
		}

		// -----------------------------
		// 営業所の場合
		// -----------------------------
		else if (dpUser.isSosLvl(JknGrpId.MENU.getDbValue(), SosLvl.OFFICE)) {

			// 全社
			ChoseiDataResultDetailDto zensya = new ChoseiDataResultDetailDto(SosMst.SOS_CD1, "全社", 0, false);

			// 支店
			SosMst sosMst = null;
			try {
				sosMst = sosMstRealDao.searchReal(dpUser.getSosCd2());
				try {
					sosMst.setSosCategoryList(sosMstRealDao.searchSosCategoryList(dpUser.getSosCd2()));
				}
				// データが見つからなくても、エラーにしない
				catch (DataNotFoundException e) {
				}

				ChoseiDataResultDetailDto siten = new ChoseiDataResultDetailDto(sosMst.getSosCd(), sosMst.getBumonSeiName(), 1, sosMst.getOncSosFlg());
				List<ChoseiDataResultDetailDto> sitenList = new ArrayList<ChoseiDataResultDetailDto>(1);
				sitenList.add(siten);

				// 検索と格納
				return createChoseiDataResultDto(dpUser, zensya, sitenList, openSosCdList);

			} catch (DataNotFoundException e) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "営業所ユーザでログインしたが調整計画の支店情報が取得出来ない";
					LOG.error(errMsg);
				}
			}
		}

		// -----------------------------
		// その他のロール
		// -----------------------------
		return null;
	}

	/**
	 * 調整計画データ結果明細を表すＤＴＯクラスのリストを生成する。
	 *
	 * @param entryList 候補のリスト
	 * @param indendSize インデント回数
	 * @return 調整計画データ結果明細を表すＤＴＯクラスのリスト
	 */
	private List<ChoseiDataResultDetailDto> createChoseiDataResultDetailDtoList(List<SosChoseiSummary> entryList, int indendSize) {
		List<ChoseiDataResultDetailDto> resultList = new ArrayList<ChoseiDataResultDetailDto>(entryList.size());
		for (SosChoseiSummary tmp : entryList) {
			resultList.add(new ChoseiDataResultDetailDto(tmp, indendSize));
		}
		return resultList;
	}

	/**
	 * 調整金額テーブルを生成する。
	 *
	 * @param dpUser ユーザ情報
	 * @param zensya 全社の調整計画
	 * @param sitenList 支店の調整計画リスト
	 * @param openSosCdList 展開対象の組織
	 * @return 調整金額テーブル
	 */
	private ChoseiDataResultDto createChoseiDataResultDto(DpUser dpUser, ChoseiDataResultDetailDto zensya, List<ChoseiDataResultDetailDto> sitenList, List<ChoseiData> openSosCdList) {

		// 営業所一覧マップ
		Map<String, List<ChoseiDataResultDetailDto>> officeListMap = null;

		// チーム一覧マップ
		Map<String, List<ChoseiDataResultDetailDto>> teamListMap = null;

		// （チーム所属）担当者一覧マップ
		Map<String, List<ChoseiDataResultDetailDto>> sos4MrListMap = null;

		// （営業所所属）担当者一覧マップ
		Map<String, List<ChoseiDataResultDetailDto>> sos3MrListMap = null;

		// 検索と格納
		List<ChoseiDataResultDetailDto> rList = null;

		// ---------------------------------
		// 初期検索の組織
		// ---------------------------------
		// 支店ロール ⇒ 営業所一覧
		if (dpUser.isMatch(JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF)) {
			try {
				rList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchOfficeList(dpUser.getSosCd2()), 2);
				if (officeListMap == null) {
					officeListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>();
				}
				officeListMap.put(dpUser.getSosCd2(), rList);
			} catch (DataNotFoundException e) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "支店ユーザでログインしたが調整計画の営業所一覧が取得出来ない";
					LOG.error(errMsg);
				}
			}
		}
		// 営業所ロール ⇒ MR一覧
		else if (dpUser.isMatch(JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF)) {

			try {
				List<ChoseiDataResultDetailDto> tmpList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchOfficeList(dpUser.getSosCd2()), 2);
				String sosCd = dpUser.getSosCd3();
				for (ChoseiDataResultDetailDto entry : tmpList) {
					if (sosCd.equals(entry.getSosChoseiSummary().getSosCd())) {
						rList = new ArrayList<ChoseiDataResultDetailDto>(1);
						entry.setLinkFlg(false);
						rList.add(entry);
						break;
					}
				}
				if (officeListMap == null) {
					officeListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>(1);
				}
				officeListMap.put(dpUser.getSosCd2(), rList);
			} catch (DataNotFoundException e) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "営業所ユーザでログインしたが調整計画の当該営業所が取得出来ない";
					LOG.error(errMsg);
				}
			}

			try {

//				if(dpUser.isOncSos()){

				// ONC組織の場合は、MR一覧 → ONC組織か否かによらず、、MR一覧を出す (2020/12/11 STEP2)
				rList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchMrList(dpUser.getSosCd3()), 3);
				if (sos3MrListMap == null) {
					sos3MrListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>();
				}
				sos3MrListMap.put(dpUser.getSosCd3(), rList); // 組織コードは営業所(3)コード

//				} else {
//
//					// 営業所組織の場合は、チーム一覧
//					rList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchTeamList(dpUser.getSosCd3()), 3);
//					if (teamListMap == null) {
//						teamListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>();
//					}
//					teamListMap.put(dpUser.getSosCd3(), rList);
//				}

			} catch (DataNotFoundException e) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "営業所ユーザでログインしたが調整計画のチーム一覧が取得出来ない";
					LOG.error(errMsg);
				}
			}
		}

		// ---------------------------------
		// 展開対象の組織
		// ---------------------------------
		for (ChoseiData value : openSosCdList) {
			try {
				BumonRank bumonRank = BumonRank.getInstance(Integer.valueOf(value.getBumonRank()));

				// 選択された組織
				SosMst sosMst = dpmSosJgiSearchService.searchSosMstWithEtcSos(value.getSosCd(), value.getBumonRank());

				// ユーザが支店をクリックした場合 ⇒ 営業所一覧の取得
				if (BumonRank.SITEN_TOKUYAKUTEN_BU == bumonRank) {
					rList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchOfficeList(value.getSosCd()), 2);
					if (officeListMap == null) {
						officeListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>();
					}
					officeListMap.put(value.getSosCd(), rList);
				}

				// ユーザが営業所をクリックした場合 ⇒ 担当者一覧の取得
				else if (BumonRank.OFFICE_TOKUYAKUTEN_G == bumonRank) {

//					if(sosMst.getOncSosFlg()){

					// 選択組織がONC組織の場合は、担当者一覧 → ONC組織か否かによらず、担当者一覧(2020/12/11 STEP2)
					rList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchMrList(value.getSosCd()), 3);
					if (sos3MrListMap == null) {
						sos3MrListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>();
					}
					sos3MrListMap.put(value.getSosCd(), rList); // 組織コードは営業所(3)コード

//					} else {
//
//						// 選択組織が営業所の場合は、チーム一覧
//						rList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchTeamList(value.getSosCd()), 3);
//						if (teamListMap == null) {
//							teamListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>();
//						}
//						teamListMap.put(value.getSosCd(), rList);
//					}
				}

				// ユーザがチームをクリックした場合 ⇒ 担当者一覧の取得
//				else if (BumonRank.TEAM == bumonRank) {
//					rList = createChoseiDataResultDetailDtoList(sosChoseiSummaryDao.searchMrList(value.getSosCd()), 4);
//					if (sos4MrListMap == null) {
//						sos4MrListMap = new HashMap<String, List<ChoseiDataResultDetailDto>>();
//					}
//					sos4MrListMap.put(value.getSosCd(), rList);
//				}

			} catch (DataNotFoundException e) {
				// 想定外だがトップ画面なので、エラーログのみとする
				if (LOG.isErrorEnabled()) {
					final String errMsg = "展開対象の組織・従業員の調整計画が取得出来ない";
					LOG.error(errMsg);
					LOG.error("sosCd=" + value.getSosCd() + ",bumonRank=" + value.getBumonRank());
				}
			}
		}
		return new ChoseiDataResultDto(zensya, sitenList, officeListMap, teamListMap, sos4MrListMap, sos3MrListMap);
	}
}
