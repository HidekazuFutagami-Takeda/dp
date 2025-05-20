package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.model.ChangeParamYT;
import jp.co.takeda.model.SysManage;
import jp.co.takeda.model.div.InsType;

/**
 * 対象区分、品目固定コードからT/Y変換率を取得ロジッククラス。
 *
 * @author nozaki
 */
public class GetChangeParamYTLogic {

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	protected ManageChangeParamYTDao manageChangeParamYTDao;

	/**
	 * 納入計画管理
	 */
	protected SysManage sysManage;

	/**
	 * コンストラクタ
	 *
	 * @param sysManage 納入計画管理
	 * @param manageChangeParamYTDao 変換パラメータ(Y→T価)DAO
	 */
	public GetChangeParamYTLogic(SysManage sysManage, ManageChangeParamYTDao manageChangeParamYTDao) {
		this.sysManage = sysManage;
		this.manageChangeParamYTDao = manageChangeParamYTDao;
	}

	/**
	 * 対象区分、品目固定コードからT/Y変換率を取得する。
	 *
	 * @param insType 対象区分
	 * @param prodCode 品目固定コード
	 * @return 変換パラメータ
	 * @throws DataNotFoundException UH/P以外の施設の場合
	 */
	public Double execute(InsType insType, String prodCode) throws DataNotFoundException {

		if (prodCode == null) {
			final String errMsg = "検索対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (insType == null) {
			final String errMsg = "検索対象の対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// T/Y変換率取得
		// ----------------------
		ChangeParamYT changeParamYT;
		try {
			changeParamYT = manageChangeParamYTDao.searchUk(sysManage.getSysYear(), sysManage.getSysTerm(), prodCode, null);

		} catch (DataNotFoundException e) {
			final String errMsg = "T/Y変換パラメータの取得に失敗。prodCode=";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg + prodCode), e);
		}

		// 変換率取得
		Double changeRate;
		switch (insType) {
			case UH:
				changeRate = changeParamYT.getChangeRateUh();
				break;
			case P:
				changeRate = changeParamYT.getChangeRateP();
			case ZATU:
				changeRate = changeParamYT.getChangeRateZ();
				break;
			default:
				// MRが主担当の雑施設が指定された場合、データが見つからないエラーとする(医薬の場合のみ)
				final String errMsg = "検索対象の対象区分が不正(UH・P以外)";
				throw new DataNotFoundException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		return changeRate;
	}
}
