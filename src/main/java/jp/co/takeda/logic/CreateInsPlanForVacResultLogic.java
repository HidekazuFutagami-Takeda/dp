package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageChangeParamBTDao;
import jp.co.takeda.dao.SysManageDao;
import jp.co.takeda.dto.InsPlanForVacResultDetailAddrDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailInsDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.model.ChangeParamBT;
import jp.co.takeda.model.ManageInsPlanForVac;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.SysClass;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.util.MathUtil;

/**
 * 管理 ワクチン施設別計画明細を作成するロジッククラス
 *
 * @author nozaki
 */
public class CreateInsPlanForVacResultLogic {

	/**
	 * 変換パラメータ(B→T価)DAO
	 */
	private final ManageChangeParamBTDao manageChangeParamBTDao;

	/**
	 * 納入計画管理DAO
	 */
	private final SysManageDao sysManageDao;

	/**
	 * コンストラクタ
	 *
	 * @param sysManageDao 納入計画管理DAO
	 * @param manageChangeParamBTDao 変換パラメータ(B→T価)DAO
	 * @param manageInsPlanForVacDao ワクチン用施設別計画DAO
	 */
	public CreateInsPlanForVacResultLogic(SysManageDao sysManageDao, ManageChangeParamBTDao manageChangeParamBTDao) {

		this.manageChangeParamBTDao = manageChangeParamBTDao;
		this.sysManageDao = sysManageDao;
	}

	/**
	 * ワクチンの施設別計画から施設別計画明細DTOを作成する。
	 *
	 * @param prodCode 品目固定コード
	 * @param insPlanList 表示対象の施設別計画リスト
	 * @return 施設別計画明細DTO
	 */
	public InsPlanForVacResultDto execute(String prodCode, List<ManageInsPlanForVac> insPlanList) {

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

		// 表示対象施設の計画値合計
		Long searchPlanTotal = null;

		// 前行の施設別計画(作業用)
		ManageInsPlanForVac tmpInsPlan = null;

		// 市区町村リスト
		List<InsPlanForVacResultDetailAddrDto> addrList = new ArrayList<InsPlanForVacResultDetailAddrDto>();

		// 明細行リスト
		List<InsPlanForVacResultDetailInsDto> detailList = null;

		for (ManageInsPlanForVac curInsPlan : insPlanList) {

			// 府県コードまたは市区町村コードが変わった場合
			if (isChange(tmpInsPlan, curInsPlan)) {

				// 前行までの明細で市区町村DTOを作成、リストに追加
				if (tmpInsPlan != null) {
					InsPlanForVacResultDetailAddrDto detailAddr = new InsPlanForVacResultDetailAddrDto(tmpInsPlan, detailList);
					addrList.add(detailAddr);
				}

				// 明細行リスト初期化
				detailList = new ArrayList<InsPlanForVacResultDetailInsDto>();
			}

			// 明細行作成、リストに追加
			InsPlanForVacResultDetailInsDto detail = new InsPlanForVacResultDetailInsDto(curInsPlan, changeRate);
			detailList.add(detail);

			// 表示対象施設の計画値を加算する
			searchPlanTotal = MathUtil.add(searchPlanTotal, curInsPlan.getImplPlanForVac().getPlanned2ValueY());

			// 前行に設定
			tmpInsPlan = curInsPlan;
		}

		// 最終の市区町村DTOを作成、リストに追加
		InsPlanForVacResultDetailAddrDto detailAddr = new InsPlanForVacResultDetailAddrDto(tmpInsPlan, detailList);
		addrList.add(detailAddr);

		// -------------------------------------------
		// 検索結果作成
		// -------------------------------------------
		return new InsPlanForVacResultDto(addrList, searchPlanTotal);
	}

	/**
	 * 明細リストの市区町村が変わったか判断する<br>
	 * 府県コード、または、市区町村コードが変わった場合は、明細リストの市区町村が変わったとする。
	 *
	 * @param tmpInsPlan 前行の明細
	 * @param curInsPlan 現在の明細
	 * @return true：明細の市区町村が変わった場合、false：明細の市区町村が変わっていない場合
	 */
	private boolean isChange(ManageInsPlanForVac tmpInsPlan, ManageInsPlanForVac curInsPlan) {

		if (tmpInsPlan == null) {
			return true;
		}
		if (tmpInsPlan.getAddrCodePref() != curInsPlan.getAddrCodePref()) {
			return true;
		}
		if (!StringUtils.equals(tmpInsPlan.getAddrCodeCity(), curInsPlan.getAddrCodeCity())) {
			return true;
		}

		return false;
	}
}
