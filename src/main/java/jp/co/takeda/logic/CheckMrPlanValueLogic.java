package jp.co.takeda.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.MrPlanDao;
import jp.co.takeda.model.MrPlan;
import jp.co.takeda.model.PlannedProd;
import jp.co.takeda.model.div.ProdCategory;

/**
 * 担当者別計画の値が妥当かを検証するロジッククラス<br>
 * 特定施設個別計画と比較して、妥当かを検証する。<br>
 * [担当者別計画 < 特定施設個別計画]ならばＮＧとする。
 *
 * @author tkawabata
 */
@Deprecated
public class CheckMrPlanValueLogic {

	/**
	 * ロガー
	 */
	private static final Log LOG = LogFactory.getLog(CheckMrPlanValueLogic.class);

	/**
	 * 営業所コード
	 */
	private final String sosCd3;

	/**
	 * 品目リスト
	 */
	private final List<PlannedProd> plannedProdList;

	/**
	 * 担当者別計画ＤＡＯ
	 */
	private final MrPlanDao mrPlanDao;

	/**
	 * 従業員マスタＤＡＯ
	 */
	private final JgiMstDAO jgiMstDAO;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd3 組織コード(営業所)
	 * @param plannedProdList 品目リスト
	 * @param mrPlanDao 担当者別計画ＤＡＯ
	 * @param jgiMstDAO 業員マスタＤＡＯ
	 */
	public CheckMrPlanValueLogic(String sosCd3, List<PlannedProd> plannedProdList, MrPlanDao mrPlanDao, JgiMstDAO jgiMstDAO) {
		this.sosCd3 = sosCd3;
		this.plannedProdList = plannedProdList;
		this.mrPlanDao = mrPlanDao;
		this.jgiMstDAO = jgiMstDAO;
	}

	/**
	 * 担当者別計画と特定施設の逆ザヤ検証を行う。
	 *
	 * @throws LogicalException 検証ＮＧの場合に論理例外をスロー
	 */
	public void execute() throws LogicalException {

		if (StringUtils.isBlank(sosCd3) || plannedProdList == null) {
			return;
		}

		// plannedProdListには品目固定コードと名称しかはいっていないので、要注意。
		List<MessageKey> errMsgKeyList = new ArrayList<MessageKey>();
		for (PlannedProd prod : plannedProdList) {
			String pCode = prod.getProdCode();
			List<MrPlan> mrPlanList = null;
			try {
				mrPlanList = mrPlanDao.searchBySosCd(MrPlanDao.SORT_STRING2, sosCd3, pCode, ProdCategory.MMP.toString());
			} catch (DataNotFoundException e) {
				// 担当者別計画がない場合はスキップ(ありえる)
				continue;
			}
			List<String> jgiNameList = new ArrayList<String>();
			for (MrPlan mrPlan : mrPlanList) {
				String jgiName = checkMrPlanAndSpecialInsPlan(mrPlan);
				if (jgiName != null) {
					jgiNameList.add(jgiName);
				}
			}
			if (jgiNameList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (String jgiName : jgiNameList) {
					sb.append("【" + jgiName + "】");
				}
				MessageKey messageKey = new MessageKey("DPS3255E", "【" + prod.getProdName() + "】", sb.toString());
				errMsgKeyList.add(messageKey);
			}
		}
		if (errMsgKeyList.size() > 0) {
			final String errMsg = "担当者別計画と特定施設個別計画の逆ザヤチェックで検証ＮＧ";
			throw new LogicalException(new Conveyance(errMsgKeyList, errMsg));
		}
	}

	/**
	 * 担当者別計画値と特定施設個別計画値を比較し、検証ＮＧならばＮＧの従業員名を返す。<br>
	 * (検証ＯＫの場合はNULLを返す)<br>
	 * [品目・担当者・UH/P毎の担当者計画] ＜ [品目・担当者・UH/P毎の特定施設個別計画]となっている場合に 従業員名称を返す。
	 *
	 * @param mrPlan 担当者別計画
	 * @return 従業員名
	 * @throw DataNotFoundException
	 */
	private String checkMrPlanAndSpecialInsPlan(MrPlan mrPlan) throws DataNotFoundException {

		boolean ngFlgUH = false;
		boolean ngFlgP = false;

		Long tUH = mrPlan.getPlannedValueUhY();
		Long sUH = mrPlan.getSpecialInsPlanValueUhY();

		Long tP = mrPlan.getPlannedValuePY();
		Long sP = mrPlan.getSpecialInsPlanValuePY();

		if (tUH == null) {
			tUH = 0L;
		}
		if (sUH == null) {
			sUH = 0L;
		}
		if (tP == null) {
			tP = 0L;
		}
		if (sP == null) {
			sP = 0L;
		}
		if (tUH < sUH) {
			ngFlgUH = true;
		}

		if (tP < sP) {
			ngFlgP = true;
		}

		if (ngFlgUH || ngFlgP) {
			if (LOG.isInfoEnabled()) {
				LOG.info("逆ザヤエラー");
				if (ngFlgUH) {
					LOG.info("UHで発生");
					LOG.info("tUH=" + tUH + ",sUH=" + sUH);
				}
				if (ngFlgP) {
					LOG.info("Pで発生");
					LOG.info("tP=" + tP + ",sP=" + sP);
				}
			}

			// 対象従業員名を取得＆返す
			return jgiMstDAO.search(mrPlan.getJgiNo()).getJgiName();
		}
		return null;
	}
}
