package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

//add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import java.util.ArrayList;
import java.util.List;
//add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応

import org.apache.commons.lang.StringUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
//add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.dao.DpmInsJgiSosDao;
//add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.JgiMstRealDao;
// add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.model.InsJgiSos;
// add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.MrCat;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 施設コードから妥当性をチェックするロジッククラス
 *
 * @author nozaki
 */
public class CheckInsNoLogic {

	/**
	 * 従業員DAO
	 */
	private final JgiMstRealDao jgiMstRealDao;

	/**
	 * 施設情報DAO
	 */
	private final InsMstRealDao insMstRealDao;

//add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
	/**
	 * 同じ組織配下の施設担当者DAO
	 */
	private final DpmInsJgiSosDao dpmInsJgiSosDao;
//add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応

	/**
	 * コンストラクタ
	 *
	 * @param jgiMstRealDao 従業員DAO
	 */
//mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
//	public CheckInsNoLogic(JgiMstRealDao jgiMstRealDao, InsMstRealDao insMstRealDao) {
	public CheckInsNoLogic(JgiMstRealDao jgiMstRealDao, InsMstRealDao insMstRealDao, DpmInsJgiSosDao dpmInsJgiSosDao) {
//mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
		this.jgiMstRealDao = jgiMstRealDao;
		this.insMstRealDao = insMstRealDao;
//add Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
		this.dpmInsJgiSosDao = dpmInsJgiSosDao;
//add End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応

	}

	/**
	 * 施設コード、品目固定コードから、以下のチェックを行なう。<br>
	 * 問題がない場合は、該当する施設情報を取得する。<br>
	 * <br>
	 * ・施設コードに該当する施設が存在するか<br>
	 * ・施設コードに該当する施設担当者が存在するか<br>
	 * ・施設コードに該当する施設担当者が利用者配下の従業員か<br>
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード（NULL可）
	 * @return 施設情報
	 * @throws DataNotFoundException 施設コードに該当する施設が存在しない場合
	 * @throws LogicalException 施設コードに該当する施設担当者が不正の場合
	 */
	public InsMst executeInsProd(String insNo, String prodCode) throws LogicalException {

		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 施設情報を検索
		InsMst insMst;
		if(StringUtils.isEmpty(prodCode)){
			insMst = insMstRealDao.searchRealIncludeMr(insNo);
		} else {
			insMst = insMstRealDao.searchRealIncludeMr(insNo, prodCode);
			if (insMst.getJgiNo() == null) {
				throw new LogicalException(new Conveyance(new MessageKey("DPM1004E")));
			}
		}

/* del Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
		// COM担当者
		// 施設のCOM担当者の従業員情報取得
		JgiMst comJgi = new JgiMst();
		if(insMst.getComJgiNo() != null){
			try {
				comJgi = jgiMstRealDao.searchReal(insMst.getComJgiNo());
			} catch (DataNotFoundException e) {
			}
		}

// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		// SPECIALTY担当者
		// 施設のSPECIALTY担当者の従業員情報取得
		JgiMst spJgi = new JgiMst();
		if(insMst.getSpJgiNo() != null){
			try {
				spJgi = jgiMstRealDao.searchReal(insMst.getSpJgiNo());
			} catch (DataNotFoundException e) {
			}
		}
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

		// ONC担当者
		// 施設のONC担当者の従業員情報取得
		JgiMst oncJgi = new JgiMst();
		if(insMst.getOncJgiNo() != null){
			try {
				oncJgi = jgiMstRealDao.searchReal(insMst.getOncJgiNo());
			} catch (DataNotFoundException e) {
			}
		}

// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
		// 固形腫瘍担当者
		// 施設の固形腫瘍担当者の従業員情報取得
		JgiMst kokeiJgi = new JgiMst();
		if(insMst.getKokeiJgiNo() != null){
			try {
				kokeiJgi = jgiMstRealDao.searchReal(insMst.getKokeiJgiNo());
			} catch (DataNotFoundException e) {
			}
		}

		JgiMst vacJgi = new JgiMst();
		if(insMst.getVacJgiNo() != null){
			try {
				vacJgi = jgiMstRealDao.searchReal(insMst.getVacJgiNo());
			} catch (DataNotFoundException e) {
			}
		}
// del End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応 */


// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）

		// 設定中の従業員の配下担当者かどうか
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {

				/**
				 * ※ user.getSosLvl(メニュー画面の画面ID ) にて、
				 * ユーザの組織レベルを取得する。
				 * メニュー画面ならば、
				 * 全ての条件セットコードのレコードが確実に存在している前提のもと、
				 * メニュー画面のIDをキーとしている。
				 */
				switch (user.getSosLvl(JknGrpId.MENU.getDbValue())) {
				case ALL:
					// OK
					break;

				case BRANCH:
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					// 下記をすべて満たす場合はエラー
					// ① 設定中従業員の組織コード2と、対象施設のCOM担当者の組織コード2が異なる
					// ② 設定中従業員の組織コード2と、対象施設のSP担当者の組織コード2が異なる
					// ③ 設定中従業員の組織コード2と、対象施設のONC担当者の組織コード2が異なる
					// ④ 設定中従業員の組織コード2と、対象施設の固形腫瘍担当者の組織コード2が異なる
					// ⑤ 設定中従業員の組織コード2と、対象施設のワクチン担当者の組織コード3が異なる
//					if (!StringUtils.equals(user.getSosCd2(), comJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), spJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), kokeiJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), oncJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), vacJgi.getSosCd2())
//					){
					List<InsJgiSos> branchSos = new ArrayList<InsJgiSos>();
					try {
						branchSos = dpmInsJgiSosDao.searchBanch(insMst.getInsNo(), user.getSosCd2());
					} catch (DataNotFoundException e) {
//						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
//					}
					}
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					break;

				case OFFICE:
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					// 下記をすべて満たす場合はエラー
					// ① 設定中従業員の組織コード3と、対象施設のCOM担当者の組織コード3が異なる
					// ② 設定中従業員の組織コード3と、対象施設のSP担当者の組織コード3が異なる
					// ③ 設定中従業員の組織コード3と、対象施設のONC担当者の組織コード3が異なる
					// ④ 設定中従業員の組織コード3と、対象施設の固形腫瘍担当者の組織コード3が異なる
					// ⑤ 設定中従業員の組織コード3と、対象施設のワクチン担当者の組織コード3が異なる
//					if (!StringUtils.equals(user.getSosCd3(), comJgi.getSosCd3()) &&
//						!StringUtils.equals(user.getSosCd3(), spJgi.getSosCd3()) &&
//					!StringUtils.equals(user.getSosCd3(), kokeiJgi.getSosCd3()) &&
//					!StringUtils.equals(user.getSosCd3(), oncJgi.getSosCd3()) &&
//					!StringUtils.equals(user.getSosCd3(), vacJgi.getSosCd3())
//							) {
					List<InsJgiSos> officeSos = new ArrayList<InsJgiSos>();
					try {
						officeSos = dpmInsJgiSosDao.searchOffice(insMst.getInsNo(), user.getSosCd3());

					} catch (DataNotFoundException e) {
//						if (user.isMatch(JokenSet.TOKUYAKUTEN_G_MASTER, JokenSet.TOKUYAKUTEN_GYOUMU_G, JokenSet.WAKUTIN_AL)) {
						if (user.isMatch(JokenSet.TOKUYAKUTEN_G_MASTER, JokenSet.TOKUYAKUTEN_GYOUMU_G, JokenSet.WAKUTIN_AL)) {
//							throw new LogicalException(new Conveyance(new MessageKey("DPM1006E")));
							throw new LogicalException(new Conveyance(new MessageKey("DPM1006E")));
//						}
						}
//						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
//					}
					}
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					break;

				case MR:
					// MR担当者の場合、施設コードから取得したマスタの事業所番号と異なっていた場合エラーとする
					try {
						InsMst insMstMr = insMstRealDao.searchRealIncludeMr(insNo,user.getJgiNo());
					} catch (DataNotFoundException e) {
						//データがない場合の処理
						throw new LogicalException(new Conveyance(new MessageKey("ErrMessageKey.DATA_NOT_FOUND_ERROR")));
					}
					break;

				default:

					final String errMsg = "設定中従業員のロールが想定外:";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + user));
				}
			}
		}
		return insMst;
	}

	/**
	 * 施設コード、品目固定コードから、以下のチェックを行なう。(親子紐づけ対応)<br>
	 * 問題がない場合は、該当する施設情報を取得する。<br>
	 * <br>
	 * ・施設コードに該当する施設が存在するか<br>
	 * ・施設コードに該当する施設担当者が存在するか<br>
	 * ・施設コードに該当する施設担当者が利用者配下の従業員か<br>
	 *
	 * @param insNo 施設コード
	 * @param prodCode 品目固定コード（NULL可）
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分２
	 * @param oyakoKbProdCode 親子区分品目コード
	 * @return 施設情報
	 * @throws DataNotFoundException 施設コードに該当する施設が存在しない場合
	 * @throws LogicalException 施設コードに該当する施設担当者が不正の場合
	 */
	public InsMst executeInsProdOyako(String insNo, String prodCode, String oyakoKb,String oyakoKb2, String oyakoKbProdCode) throws LogicalException {

		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 施設情報を検索
		InsMst insMst;
		if(StringUtils.isEmpty(prodCode)){
			insMst = insMstRealDao.searchRealIncludeMrOyako(insNo, null, oyakoKb, oyakoKb2, oyakoKbProdCode);
		} else {
			insMst = insMstRealDao.searchRealIncludeMrOyako(insNo, prodCode, oyakoKb, oyakoKb2, oyakoKbProdCode);
			if (insMst.getJgiNo() == null) {
				throw new LogicalException(new Conveyance(new MessageKey("DPM1004E")));
			}
		}

/* del Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
		// COM担当者
		// 施設のCOM担当者の従業員情報取得
		JgiMst comJgi = new JgiMst();
		if(insMst.getComJgiNo() != null){
			try {
				comJgi = jgiMstRealDao.searchReal(insMst.getComJgiNo());
			} catch (DataNotFoundException e) {
			}
		}

		// SPECIALTY担当者
		// 施設のSPECIALTY担当者の従業員情報取得
		JgiMst spJgi = new JgiMst();
		if(insMst.getSpJgiNo() != null){
			try {
				spJgi = jgiMstRealDao.searchReal(insMst.getSpJgiNo());
			} catch (DataNotFoundException e) {
			}
		}

		// ONC担当者
		// 施設のONC担当者の従業員情報取得
		JgiMst oncJgi = new JgiMst();
		if(insMst.getOncJgiNo() != null){
			try {
				oncJgi = jgiMstRealDao.searchReal(insMst.getOncJgiNo());
			} catch (DataNotFoundException e) {
			}
		}

		// 固形腫瘍担当者
		// 施設の固形腫瘍担当者の従業員情報取得
		JgiMst kokeiJgi = new JgiMst();
		if(insMst.getKokeiJgiNo() != null){
			try {
				kokeiJgi = jgiMstRealDao.searchReal(insMst.getKokeiJgiNo());
			} catch (DataNotFoundException e) {
			}
		}

		JgiMst vacJgi = new JgiMst();
		if(insMst.getVacJgiNo() != null){
			try {
				vacJgi = jgiMstRealDao.searchReal(insMst.getVacJgiNo());
			} catch (DataNotFoundException e) {
			}
		}
// del End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応 */

		// 設定中の従業員の配下担当者かどうか
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {

				/**
				 * ※ user.getSosLvl(メニュー画面の画面ID ) にて、
				 * ユーザの組織レベルを取得する。
				 * メニュー画面ならば、
				 * 全ての条件セットコードのレコードが確実に存在している前提のもと、
				 * メニュー画面のIDをキーとしている。
				 */
				switch (user.getSosLvl(JknGrpId.MENU.getDbValue())) {
				case ALL:
					// OK
					break;

				case BRANCH:
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					// 下記をすべて満たす場合はエラー
					// ① 設定中従業員の組織コード2と、対象施設のCOM担当者の組織コード2が異なる
					// ② 設定中従業員の組織コード2と、対象施設のSP担当者の組織コード2が異なる
					// ③ 設定中従業員の組織コード2と、対象施設のONC担当者の組織コード2が異なる
					// ④ 設定中従業員の組織コード2と、対象施設の固形腫瘍担当者の組織コード2が異なる
					// ⑤ 設定中従業員の組織コード2と、対象施設のワクチン担当者の組織コード3が異なる
//					if (!StringUtils.equals(user.getSosCd2(), comJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), spJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), kokeiJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), oncJgi.getSosCd2()) &&
//						!StringUtils.equals(user.getSosCd2(), vacJgi.getSosCd2())
//						) {
					List<InsJgiSos> branchSos = new ArrayList<InsJgiSos>();
					try {
							branchSos = dpmInsJgiSosDao.searchBanch(insMst.getInsNo(), user.getSosCd2());
					} catch (DataNotFoundException e) {
//						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
//					}
					}

// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					break;

				case OFFICE:
// mod Start 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					// 下記をすべて満たす場合はエラー
					// ① 設定中従業員の組織コード3と、対象施設のCOM担当者の組織コード3が異なる
					// ② 設定中従業員の組織コード3と、対象施設のSP担当者の組織コード3が異なる
					// ③ 設定中従業員の組織コード3と、対象施設のONC担当者の組織コード3が異なる
					// ④ 設定中従業員の組織コード3と、対象施設の固形腫瘍担当者の組織コード3が異なる
					// ⑤ 設定中従業員の組織コード3と、対象施設のワクチン担当者の組織コード3が異なる
//					if (!StringUtils.equals(user.getSosCd3(), comJgi.getSosCd3()) &&
//						!StringUtils.equals(user.getSosCd3(), spJgi.getSosCd3()) &&
//						!StringUtils.equals(user.getSosCd3(), kokeiJgi.getSosCd3()) &&
//						!StringUtils.equals(user.getSosCd3(), oncJgi.getSosCd3()) &&
//						!StringUtils.equals(user.getSosCd3(), vacJgi.getSosCd3())
//							) {
					List<InsJgiSos> officeSos = new ArrayList<InsJgiSos>();
					try {
						officeSos = dpmInsJgiSosDao.searchOffice(insMst.getInsNo(), user.getSosCd3());
					} catch (DataNotFoundException e) {
//						if (user.isMatch(JokenSet.TOKUYAKUTEN_G_MASTER, JokenSet.TOKUYAKUTEN_GYOUMU_G, JokenSet.WAKUTIN_AL)) {
						if (user.isMatch(JokenSet.TOKUYAKUTEN_G_MASTER, JokenSet.TOKUYAKUTEN_GYOUMU_G, JokenSet.WAKUTIN_AL)) {
//							throw new LogicalException(new Conveyance(new MessageKey("DPM1006E")));
							throw new LogicalException(new Conveyance(new MessageKey("DPM1006E")));
//						}
						}
//						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
//					}
					}
// mod End 2022/6/15 R.takamoto INC1796156　施設品目別計画編集画面の担当外エラー対応
					break;

				case MR:
					// MR担当者の場合、施設コードから取得したマスタの事業所番号と異なっていた場合エラーとする
					try {
						InsMst insMstMr = insMstRealDao.searchRealIncludeMr(insNo,user.getJgiNo());
					} catch (DataNotFoundException e) {
						//データがない場合の処理
						throw new LogicalException(new Conveyance(new MessageKey("ErrMessageKey.DATA_NOT_FOUND_ERROR")));
					}
					break;

				default:

					final String errMsg = "設定中従業員のロールが想定外:";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + user));
				}
			}
		}
		return insMst;
	}

	/**
	 * 施設コードから、以下のチェックを行なう。<br>
	 * 問題がない場合は、該当する施設情報を取得する。<br>
	 * <br>
	 * ・施設コードに該当する施設が存在するか<br>
	 * ・施設コードに該当する施設担当者が存在するか<br>
	 * ・施設コードに該当する施設担当者が利用者配下の従業員か<br>
	 *
	 * @param insNo 施設コード
	 * @return 施設情報
	 * @throws DataNotFoundException 施設コードに該当する施設が存在しない場合
	 * @throws LogicalException 施設コードに該当する施設担当者が不正の場合
	 */
	public InsMst execute(String insNo, MrCat mrCat) throws LogicalException {

		if (insNo == null) {
			final String errMsg = "検索対象の施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// 施設コードが設定されている場合、施設情報を検索
		InsMst insMst = insMstRealDao.searchRealIncludeMr(insNo, mrCat);

		// 担当者が取得できなかった場合
		if (insMst.getJgiNo() == null) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1004E")));
		}

		// 従業員情報取得
		JgiMst jgiMst;
		try {
			jgiMst = jgiMstRealDao.searchReal(insMst.getJgiNo());
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPM1004E")));
		}

		// 設定中の従業員の配下担当者かどうか
		validateMr(jgiMst);

		return insMst;
	}

	/**
	 * 指定された従業員が、設定中のユーザの配下かチェックする。<br>
	 * 配下ではない場合は例外(LogicalException)をスローする。<br>
	 *
	 * @param jgiMst 対象の従業員
	 * @throws LogicalException
	 */
	public void validateMr(JgiMst jgiMst) throws LogicalException {

		// 設定中の従業員の配下担当者かどうか
		DpUserInfo userInfo = DpUserInfo.getDpUserInfo();
		if (userInfo != null) {
			DpUser user = userInfo.getSettingUser();
			if (user != null) {

				/**
				 * ※ user.getSosLvl(メニュー画面の画面ID ) にて、
				 * ユーザの組織レベルを取得する。
				 * メニュー画面ならば、
				 * 全ての条件セットコードのレコードが確実に存在している前提のもと、
				 * メニュー画面のIDをキーとしている。
				 */
				switch (user.getSosLvl(JknGrpId.MENU.getDbValue())) {
				case ALL:
					// OK
					break;
				case BRANCH:
					// 設定中従業員の組織コード2と、対象担当者の組織コード2が異なる場合はエラー
					if (!StringUtils.equals(user.getSosCd2(), jgiMst.getSosCd2())) {
						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
					}
					break;
				case OFFICE:
					// 設定中従業員の組織コード3と、対象担当者の組織コード3が異なる場合はエラー
					if (!StringUtils.equals(user.getSosCd3(), jgiMst.getSosCd3())) {
						if (user.isMatch(JokenSet.TOKUYAKUTEN_G_MASTER, JokenSet.TOKUYAKUTEN_GYOUMU_G, JokenSet.WAKUTIN_AL)) {
							throw new LogicalException(new Conveyance(new MessageKey("DPM1006E")));
						}
						throw new LogicalException(new Conveyance(new MessageKey("DPM1003E")));
					}
					break;
				case MR:
					// OK
					break;
				default:
					final String errMsg = "設定中従業員のロールが想定外:";
					throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + user));

				}
			}
		}
	}
}
