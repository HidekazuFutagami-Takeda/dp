package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.DATA_NOT_FOUND_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageChangeParamBTDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.ManageInsWsPlanForVacDetailAddrDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDetailInsDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDetailWsDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.model.ChangeParamBT;
import jp.co.takeda.model.ManageInsWsPlanForVac;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.Prefecture;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 管理 ワクチン施設特約店別計画明細を作成するロジッククラス
 *
 * @author nozaki
 */
public class CreateInsWsPlanForVacResultLogic {

	/**
	 * 変換パラメータ(B→T価)DAO
	 */
	private final ManageChangeParamBTDao manageChangeParamBTDao;

	/**
	 * 納入計画管理DAO
	 */
	private final SysManageDao sysManageDao;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnRealDao")
	protected final TmsTytenMstUnRealDao tmsTytenMstUnRealDao;

	/**
	 * コンストラクタ
	 *
	 * @param sysManageDao 納入計画管理DAO
	 * @param manageChangeParamBTDao 変換パラメータ(B→T価)DAO
	 * @param manageInsPlanForVacDao ワクチン用施設特約店別計画DAO
	 * @param tmsTytenMstUnRealDao TMS特約店基本統合DAO
	 */
	public CreateInsWsPlanForVacResultLogic(SysManageDao sysManageDao, ManageChangeParamBTDao manageChangeParamBTDao, TmsTytenMstUnRealDao tmsTytenMstUnRealDao) {
		this.manageChangeParamBTDao = manageChangeParamBTDao;
		this.sysManageDao = sysManageDao;
		this.tmsTytenMstUnRealDao = tmsTytenMstUnRealDao;
	}

	/**
	 * ワクチンの施設特約店別計画から施設特約店別計画明細DTOを作成する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insPlanList 表示対象の施設特約店別計画リスト
	 * @return 施設特約店別計画明細DTO
	 * @throws DataNotFoundException
	 */
	public ManageInsWsPlanForVacDto execute(String prodCode, String tmsTytenCd, List<ManageInsWsPlanForVac> insWsPlanList) throws DataNotFoundException {

		// ----------------------
		// T/B変換率取得
		// ----------------------

		// 納入計画システム管理取得
		SysManage sysManage;
		try {
			sysManage = sysManageDao.search(SysClass.DPM, SysType.VACCINE);
		} catch (DataNotFoundException e) {
			final String errMsg = "納入計画システム管理の取得に失敗。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg), e);
		}

		// T/B変換率取得
		Double changeRate;
		try {
			ChangeParamBT changeParamBT = manageChangeParamBTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);
			changeRate = changeParamBT.getChangeRate();

		} catch (DataNotFoundException e) {
			final String errMsg = "T/B変換パラメータの取得に失敗。prodCode=";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// -----------------------------
		// 明細行作成
		// -----------------------------

		// 市区町村リスト
		List<ManageInsWsPlanForVacDetailAddrDto> addrList = new ArrayList<ManageInsWsPlanForVacDetailAddrDto>();
		// 登録可否フラグ(明細全件編集不可の場合は、登録不可能とする)
		boolean enableEntry = false;

		// 施設リスト
		List<ManageInsWsPlanForVacDetailInsDto> insList = null;

		// 特約店リスト
		List<ManageInsWsPlanForVacDetailWsDto> wsList = null;
		// 施設に登録されている特約店コードリスト(非表示分含む)
		List<String> tmsTytenCdList = null;

		// 前行の施設特約店別計画(作業用)
		ManageInsWsPlanForVac tmpInsWsPlan = null;

		for (ManageInsWsPlanForVac curInsWsPlan : insWsPlanList) {

			// 施設コードが変わった場合
			if (isChangeIns(tmpInsWsPlan, curInsWsPlan)) {

				// 前明細が存在する、かつ、特約店リストが1件以上ある場合
				if (tmpInsWsPlan != null && wsList.size() > 0) {

					// 前行までの特約店リストで施設DTOを作成、施設リストに追加
					ManageInsWsPlanForVacDetailInsDto insDto = createDetailInsDto(tmpInsWsPlan, wsList, tmsTytenCdList);
					insList.add(insDto);
				}

				// 特約店リスト、特約店コードリスト初期化
				wsList = new ArrayList<ManageInsWsPlanForVacDetailWsDto>();
				tmsTytenCdList = new ArrayList<String>();
			}

			// 府県コードまたは市区町村コードが変わった場合
			if (isChangeShiku(tmpInsWsPlan, curInsWsPlan)) {

				// 前明細が存在する、かつ、施設リストが1件以上ある場合
				if (tmpInsWsPlan != null && insList.size() > 0) {

					// 前行までの施設リストで市区町村DTOを作成、市区町村リストに追加
					ManageInsWsPlanForVacDetailAddrDto addrDto = createDetailAddrDto(tmpInsWsPlan, insList);
					addrList.add(addrDto);
				}

				// 施設リスト初期化
				insList = new ArrayList<ManageInsWsPlanForVacDetailInsDto>();
			}

			// 特約店コードリストに特約店コードを追加
			tmsTytenCdList.add(curInsWsPlan.getTmsTytenCd());

			// 表示対象の特約店の場合は、特約店リストに追加
			if (tmsTytenCd == null || curInsWsPlan.getTmsTytenCd().startsWith(tmsTytenCd)) {

				// (特約店別のTB変換率を取得することになった場合はここで取得する)
				// 明細作成、特約店リストに追加

				// 特約店対象外フラグの取得
				boolean planTaiGaiFlgTok = false;
				if (StringUtils.isNotBlank(curInsWsPlan.getTmsTytenCd())) {
					try {
						TmsTytenMstUn tytenMstUn = tmsTytenMstUnRealDao.searchRealRef(curInsWsPlan.getTmsTytenCd());
						planTaiGaiFlgTok = tytenMstUn.getPlanTaiGaiFlgTok();
					} catch (DataNotFoundException e) {
					}
				}
				ManageInsWsPlanForVacDetailWsDto insDto = new ManageInsWsPlanForVacDetailWsDto(curInsWsPlan, planTaiGaiFlgTok);
				wsList.add(insDto);
			}

			// 前行に設定
			tmpInsWsPlan = curInsWsPlan;
		}

		// 最終の明細を、施設リストに追加
		if (wsList.size() > 0) {
			ManageInsWsPlanForVacDetailInsDto insDto = createDetailInsDto(tmpInsWsPlan, wsList, tmsTytenCdList);
			insList.add(insDto);
		}

		// 最終の施設リストを、市区町村リストに追加
		if (insList.size() > 0) {
			ManageInsWsPlanForVacDetailAddrDto addrDto = createDetailAddrDto(tmpInsWsPlan, insList);
			addrList.add(addrDto);
		}

		if (addrList.size() == 0) {
			throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR));
		}

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new ManageInsWsPlanForVacDto(changeRate, addrList, enableEntry);
	}

	/**
	 * 明細リストの市区町村が変わったか判断する<br>
	 * 府県コード、または、市区町村コードが変わった場合は、明細リストの市区町村が変わったとする。
	 *
	 * @param tmpInsPlan 前行の明細
	 * @param curInsPlan 現在の明細
	 * @return true：明細の市区町村が変わった場合、false：明細の市区町村が変わっていない場合
	 */
	private boolean isChangeShiku(ManageInsWsPlanForVac tmpInsWsPlan, ManageInsWsPlanForVac curInsWsPlan) {

		if (tmpInsWsPlan == null) {
			return true;
		}
		if (tmpInsWsPlan.getAddrCodePref() != curInsWsPlan.getAddrCodePref()) {
			return true;
		}
		if (!StringUtils.equals(tmpInsWsPlan.getAddrCodeCity(), curInsWsPlan.getAddrCodeCity())) {
			return true;
		}

		return false;
	}

	/**
	 * 明細リストの施設が変わったか判断する<br>
	 * 施設コードが変わった場合は、明細リストの施設が変わったとする。
	 *
	 * @param tmpInsPlan 前行の明細
	 * @param curInsPlan 現在の明細
	 * @return true：明細の市区町村が変わった場合、false：明細の市区町村が変わっていない場合
	 */
	private boolean isChangeIns(ManageInsWsPlanForVac tmpInsWsPlan, ManageInsWsPlanForVac curInsWsPlan) {

		if (tmpInsWsPlan == null) {
			return true;
		}
		if (!StringUtils.equals(tmpInsWsPlan.getInsNo(), curInsWsPlan.getInsNo())) {
			return true;
		}

		return false;
	}

	/**
	 * 市区町村DTOを作成する。
	 *
	 * @param tmpInsWsPlan 施設特約店別計画
	 * @param wsList 施設DTOのリスト
	 * @return 市区町村DTO
	 */
	private ManageInsWsPlanForVacDetailAddrDto createDetailAddrDto(ManageInsWsPlanForVac tmpInsWsPlan, List<ManageInsWsPlanForVacDetailInsDto> insList) {

		Prefecture addrCodePref = tmpInsWsPlan.getAddrCodePref();
		String addrCodeCity = tmpInsWsPlan.getAddrCodeCity();
		String shikuchosonMeiKj = tmpInsWsPlan.getShikuchosonMeiKj();

		return new ManageInsWsPlanForVacDetailAddrDto(addrCodePref, addrCodeCity, shikuchosonMeiKj, insList);
	}

	/**
	 * 施設DTOを作成する。
	 *
	 * @param tmpInsWsPlan 施設特約店別計画
	 * @param wsList 表示特約店DTOのリスト
	 * @param tmsTytenCdList 特約店コードリスト(非表示分含む)
	 * @return 施設DTO
	 */
	private ManageInsWsPlanForVacDetailInsDto createDetailInsDto(ManageInsWsPlanForVac tmpInsWsPlan, List<ManageInsWsPlanForVacDetailWsDto> wsList, List<String> tmsTytenCdList) {

		String insNo = tmpInsWsPlan.getInsNo();
		String insName = tmpInsWsPlan.getInsAbbrName();

		// 削除施設フラグ取得 (依頼中または削除済は削除施設)
		DelInsLogic delInsLogic = new DelInsLogic(tmpInsWsPlan.getReqFlg(), tmpInsWsPlan.getInsDelFlg());
		boolean delFlg = delInsLogic.getDelFlg();

		// 登録済の計画があるか
		boolean existPlan = false;
		for (ManageInsWsPlanForVacDetailWsDto manageInsWsPlanForVacDetailWsDto : wsList) {
			if (manageInsWsPlanForVacDetailWsDto.getSeqKey() != null) {
				existPlan = true;
				break;
			}
		}

		// 編集可能か (削除施設 かつ 未登録の場合は編集不可)
		boolean enableEdit = true;
		if (delFlg && !existPlan) {
			enableEdit = false;
		}

		return new ManageInsWsPlanForVacDetailInsDto(insName, insNo, delFlg, enableEdit, wsList, tmsTytenCdList);
	}

}
