package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.logic.div.ContactOperationsType;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpUser;

/**
 * アテンション通知対象の従業員を取得するロジッククラス
 *
 * @author khashimoto
 */
public class SearchAttJgiListLogic {

	/**
	 * 組織情報DAO
	 */
	private final SosMstDAO sosMstDAO;

	/**
	 * 従業員情報DAO
	 */
	private final JgiMstDAO jgiMstDAO;

	/**
	 * 実行者情報
	 */
	private final DpUser dpUser;

	/**
	 * 業務連絡の処理区分
	 */
	private final ContactOperationsType operationsType;

	/**
	 * 実行対象の組織コード
	 */
	private final String sosCd;

	/**
	 * コンストラクタ
	 *
	 * @param sosMstDAO 組織情報DAO
	 * @param jgiMstDAO 従業員情報DAO
	 * @param dpUser 実行者情報
	 * @param operationsType 業務連絡の処理区分
	 * @param sosCd 実行対象の組織コード
	 */
	public SearchAttJgiListLogic(SosMstDAO sosMstDAO, JgiMstDAO jgiMstDAO, DpUser dpUser, ContactOperationsType operationsType, String sosCd) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosMstDAO == null) {
			final String errMsg = "組織情報DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiMstDAO == null) {
			final String errMsg = "従業員情報DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (dpUser == null) {
			final String errMsg = "実行者情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (operationsType == null) {
			final String errMsg = "チェック対象の組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.sosMstDAO = sosMstDAO;
		this.jgiMstDAO = jgiMstDAO;
		this.dpUser = dpUser;
		this.operationsType = operationsType;
		this.sosCd = sosCd;
	}

	/**
	 * アテンション通知対象の従業員を取得する。
	 *
	 * @return アテンション通知対象の従業員リスト
	 * @throws DataNotFoundException
	 */
	public List<JgiMst> execute() throws DataNotFoundException {

		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();

		// -----------------------------
		// 通常の設定
		// -----------------------------
		switch (operationsType) {
			case MR_PLAN_EST:
			case INS_WS_DIST:
				jgiMstList = searchMMPJgiList();
				break;
			case INS_WS_DIST_SHIIRE:
				jgiMstList = searchDistShiireJgiList();
				break;
			case INS_DOC_DIST_MMP:
			case INS_DOC_RE_DIST_MMP:
				jgiMstList = searchDistDocJgiList();
				break;
			case WS_DIST:
			case WS_SLIDE_IYAKU:
				jgiMstList = searchWsJgiList();
				break;
			case INS_WS_DIST_VAC:
			case MR_PLAN_EST_VAC:
			case WS_SLIDE_VAC:
				jgiMstList = searchForVacJgiList();
				break;
		}

		// -----------------------------
		// 代行ユーザの設定
		// -----------------------------
		try {
			JgiMst altJgi = jgiMstDAO.searchAltJgi();
			boolean addFlg = true;
			for (JgiMst jgiMst : jgiMstList) {
				// 既にリストに追加済みの場合は、追加しない
				if (jgiMst.getJgiNo().equals(altJgi.getJgiNo())) {
					addFlg = false;
					break;
				}
			}
			if (addFlg) {
				jgiMstList.add(altJgi);
			}
		} catch (DataNotFoundException e) {
			// エラーとしない
		}
		return jgiMstList;
	}

	/**
	 * (医)試算処理/配分処理(MMP品)のアテンション通知先従業員を取得する。
	 *
	 * @return 従業員リスト
	 * @throws DataNotFoundException
	 */
	protected List<JgiMst> searchMMPJgiList() throws DataNotFoundException {
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// 本部、支店長、支店スタッフ、リージョナルGM・小児科ＡＣは実行者に通知
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF, JokenSet.WAKUTIN_AL)) {
			JgiMst jgiMst = jgiMstDAO.searchReal(dpUser.getJgiNo());
			jgiMstList.add(jgiMst);
		}

		// 営業所長の場合、営業所長(実行者含む)・営業所スタッフに通知
		if (dpUser.isMatch(JokenSet.OFFICE_MASTER)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosCd, JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF));
			} catch (DataNotFoundException e) {
				// 営業所スタッフがいない場合もエラーにしない
			}
		}

		// 本部、支店長、支店スタッフ、営業所スタッフの場合、営業所スタッフ(実行者含む)に通知
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF, JokenSet.OFFICE_STAFF)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosCd, JokenSet.OFFICE_STAFF));
			} catch (DataNotFoundException e) {
				// 営業所スタッフがいない場合もエラーにしない
			}
		}

		return jgiMstList;
	}

	/**
	 * (医)施設特約店別配分処理(仕入品)のアテンション通知先従業員を取得する。
	 *
	 * @return 従業員リスト
	 * @throws DataNotFoundException
	 */
	protected List<JgiMst> searchDistShiireJgiList() throws DataNotFoundException {
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// 本部、営業所長、営業所スタッフは実行者に通知
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF)) {
			JgiMst jgiMst = jgiMstDAO.searchReal(dpUser.getJgiNo());
			jgiMstList.add(jgiMst);
		}

		// 営業所情報を取得
		SosMst sosMst = sosMstDAO.search(sosCd);

		// 支店長の場合、支店長・支店スタッフに通知
		if (dpUser.isMatch(JokenSet.SITEN_MASTER)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosMst.getUpSosCd(), JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF));
			} catch (DataNotFoundException e) {
				// 支店スタッフがいない場合もエラーにしない
			}
		}

		// 本部、支店スタッフ、営業所長、営業所スタッフの場合、支店スタッフに通知
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_STAFF, JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosMst.getUpSosCd(), JokenSet.SITEN_STAFF));
			} catch (DataNotFoundException e) {
				// 支店スタッフがいない場合もエラーにしない
			}
		}

		return jgiMstList;
	}

	/**
	 * (医)施設医師別配分処理のアテンション通知先従業員を取得する。
	 *
	 * @return 従業員リスト
	 * @throws DataNotFoundException
	 */
	protected List<JgiMst> searchDistDocJgiList() throws DataNotFoundException {
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// 本部、支店長、支店スタッフ、AL、MRは実行者に通知
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF, JokenSet.IYAKU_AL, JokenSet.IYAKU_MR)) {
			JgiMst jgiMst = jgiMstDAO.searchReal(dpUser.getJgiNo());
			jgiMstList.add(jgiMst);
		}

		// 営業所長の場合、営業所長・営業所スタッフに通知
		if (dpUser.isMatch(JokenSet.OFFICE_MASTER)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosCd, JokenSet.OFFICE_MASTER, JokenSet.OFFICE_STAFF));
			} catch (DataNotFoundException e) {
				// 営業所スタッフがいない場合もエラーにしない
			}
		}

		// 本部、支店長、支店スタッフ、営業所スタッフの場合、営業所スタッフに通知
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF, JokenSet.OFFICE_STAFF)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosCd, JokenSet.OFFICE_STAFF));
			} catch (DataNotFoundException e) {
				// 営業所スタッフがいない場合もエラーにしない
			}
		}

		return jgiMstList;
	}

	/**
	 * (医)特約店配分/スライド処理のアテンション通知先従業員を取得する。
	 *
	 * @return 従業員リスト
	 * @throws DataNotFoundException
	 */
	protected List<JgiMst> searchWsJgiList() throws DataNotFoundException {
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// 本部は実行者に通知
		if (dpUser.isMatch(JokenSet.HONBU)) {
			JgiMst jgiMst = jgiMstDAO.searchReal(dpUser.getJgiNo());
			jgiMstList.add(jgiMst);
		}

		// 支店長の場合、支店長(実行者含む)・支店スタッフに通知
		if (dpUser.isMatch(JokenSet.SITEN_MASTER)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosCd, JokenSet.SITEN_MASTER, JokenSet.SITEN_STAFF));
			} catch (DataNotFoundException e) {
				// 支店スタッフがいない場合もエラーにしない
			}
		}

		// 本部、支店スタッフの場合、支店スタッフ(実行者含む)に通知
		if (dpUser.isMatch(JokenSet.HONBU, JokenSet.SITEN_STAFF)) {
			try {
				jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, sosCd, JokenSet.SITEN_STAFF));
			} catch (DataNotFoundException e) {
				// 支店スタッフがいない場合もエラーにしない
			}
		}

		return jgiMstList;
	}

	/**
	 * (ワ)施設特約店別配分/スライド処理のアテンション通知先従業員を取得する。
	 *
	 * @return 従業員リスト
	 * @throws DataNotFoundException
	 */
	protected List<JgiMst> searchForVacJgiList() throws DataNotFoundException {
		List<JgiMst> jgiMstList = new ArrayList<JgiMst>();
		// 本部は実行者に通知
		if (dpUser.isMatch(JokenSet.HONBU)) {
			JgiMst jgiMst = jgiMstDAO.searchReal(dpUser.getJgiNo());
			jgiMstList.add(jgiMst);
		}
		// ワクチンGに通知
		jgiMstList.addAll(jgiMstDAO.searchBySosCd(null, null, JokenSet.HONBU_WAKUTIN_G));

		return jgiMstList;
	}
}
