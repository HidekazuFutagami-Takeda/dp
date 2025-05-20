package jp.co.takeda.security;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.security.Principal;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.Model;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.JknGrp;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.SosMstCtg;
import jp.co.takeda.model.div.BumonRank;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JknGrpId;
import jp.co.takeda.model.div.JokenKbn;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Shokusei;
import jp.co.takeda.model.div.Shokushu;
import jp.co.takeda.model.div.SosLvl;
import jp.co.takeda.model.div.SysClass;

/**
 * 納入計画システムのユーザを表すクラス
 *
 * @author tkawabata
 */
public final class DpUser extends Model<DpUser> implements Principal, Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5767743660674715400L;

	/**
	 * 従業員情報
	 */
	private final JgiMst jgiMst;

	/**
	 * 組織情報
	 */
	private final SosMst sosMst;

	/**
	 * ロール
	 */
	private final DpRole role;


	/**
	 * 副担当MR（主担当施設をもたないMR）
	 */
	private final boolean subMr;

	/**
	 * 画面権限条件リスト
	 */
	private final List<JknGrp> jknGrpList;

	private List<JokenSet> tokuyakuJknSetList;

	/**
	 * コンストラクタ
	 *
	 * @param user 従業員情報
	 * @param sosMst 組織情報
	 * @param roles ロール
	 */
	public DpUser(JgiMst jgiMst, SosMst sosMst, DpRole role, List<JknGrp> jknGrpList,List<JokenSet> tokuyakutenJokenSetCodes) {

		if (jgiMst == null) {
			final String errMsg = "従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMst == null) {
			final String errMsg = "組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (role == null) {
			final String errMsg = "ロールがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jknGrpList == null) {
			final String errMsg = "画面権限条件リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.jgiMst = jgiMst;
		this.sosMst = sosMst;
		this.role = role;
		this.jknGrpList = jknGrpList;
		this.subMr = false;
		this.tokuyakuJknSetList = tokuyakutenJokenSetCodes;

	}

	/**
	 * コンストラクタ
	 *
	 * @param user 従業員情報
	 * @param sosMst 組織情報
	 * @param roles ロール
	 * @param subMr 副担当MR（主担当施設をもたないMR）かどうか
	 */
	public DpUser(JgiMst jgiMst, SosMst sosMst, DpRole role, List<JknGrp> jknGrpList, boolean subMr,List<JokenSet> tokuyakutenJokenSetCodes) {

		if (jgiMst == null) {
			final String errMsg = "従業員情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosMst == null) {
			final String errMsg = "組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (role == null) {
			final String errMsg = "ロールがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jknGrpList == null) {
			final String errMsg = "画面権限条件リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.jgiMst = jgiMst;
		this.sosMst = sosMst;
		this.role = role;
		this.jknGrpList = jknGrpList;
		this.subMr = subMr;

		this.tokuyakuJknSetList = tokuyakutenJokenSetCodes;
	}

	/**
	 * 指定の条件セットに一致する場合に真を返す。
	 *
	 * @param jokenSet 条件セット
	 * @return 一致する場合に真
	 */
	public boolean isMatch(JokenSet jokenSet) {
		if (jokenSet == null) {
			return false;
		}
		return jokenSet.equals(this.getRole().getJokenSet());
	}

	/**
	 * 画面IDと参照可能組織レベルから、参照可能かどうかをチェックする
	 * @param screenId 画面ID文字列（Actionに設定されたSCREEN_IDを想定 例：DPM100C01）
	 * @param sosLvl 参照可能組織レベル
	 * @return
	 */
	public boolean isSosLvl(String screenId, SosLvl sosLvl) {
		if (screenId == null) {
			final String errMsg = "画面IDがnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		if (sosLvl == null) {
			final String errMsg = "参照可能組織レベルがnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}

		String displayID = "";
		JknGrpId jknGrpId = null;
		boolean allFlg = false;
		// 条件グループIDを特定
		if(screenId.equals("CMN")) {
			allFlg = true;
		}
		else {
			if(screenId.toUpperCase().substring(0, 3).
					equals(SysClass.DPM.getName())) {
				displayID = screenId.toUpperCase().substring(0, 5); // DPMの場合は先頭５桁で比較
			}else {
				displayID =trimDpsScreenId(screenId); // DPSの場合は先頭12桁で比較
			}
			jknGrpId = JknGrpId.getInstance(displayID);
		}

		// 所持している条件セットグループリストから、権限を確認
		for(JknGrp jknGrp:this.jknGrpList) {
			if(allFlg || jknGrp.getJknGrpId().equals(jknGrpId)) {
				if(jknGrp.getSosLvl().equals(sosLvl)) {
					return true;
				}
				break;
			}
		}

		return false;
	}

	/**
	 * 特約店ユーザか否かを判定する
	 * @return
	 */
	public boolean isTokuyakutenUser() {
		JokenSet myJokenSet = role.getJokenSet();
		for(JokenSet e:this.tokuyakuJknSetList) {
			if(e.equals(myJokenSet)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 画面IDから、ユーザの参照可能組織レベルを取得する
	 * @param screenId
	 * @return SosLvl 参照可能組織レベル
	 */
	public SosLvl getSosLvl(String screenId) {
		if (screenId == null) {
			final String errMsg = "画面IDがnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		String displayID = "";
		JknGrpId jknGrpId = null;
		boolean allFlg = false;
		// 条件グループIDを特定
		if(screenId.equals("CMN")) {
			allFlg = true;
		}
		else {
			if(screenId.toUpperCase().substring(0, 3).
					equals(SysClass.DPM.getName())) {
				displayID = screenId.toUpperCase().substring(0, 5); // DPMの場合は先頭５桁で比較
			}else {
				displayID =trimDpsScreenId(screenId); // DPSの場合は先頭12桁で比較
			}
			jknGrpId = JknGrpId.getInstance(displayID);
		}

		// 所持している条件セットグループリストから、権限を確認
		for(JknGrp jknGrp:this.jknGrpList) {
			if(allFlg || jknGrp.getJknGrpId().equals(jknGrpId)) {
				return jknGrp.getSosLvl();
			}
		}
		return SosLvl._ERROR;
	}

	/**
	 * 画面IDから、ユーザの条件区分を取得する
	 * @param screenId
	 * @return JokenKbn 参照可能組織レベル
	 */
	public JokenKbn getJokenKbn(String screenId) {
		if (screenId == null) {
			final String errMsg = "画面IDがnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		String displayID = "";
		JknGrpId jknGrpId = null;
		boolean allFlg = false;
		// 条件グループIDを特定
		if(screenId.equals("CMN")) {
			allFlg = true;
		}
		else {
			if(screenId.toUpperCase().substring(0, 3).
					equals(SysClass.DPM.getName())) {
				displayID = screenId.toUpperCase().substring(0, 5); // DPMの場合は先頭５桁で比較
			}else {
				displayID =trimDpsScreenId(screenId); // DPSの場合は先頭12桁で比較
			}
			jknGrpId = JknGrpId.getInstance(displayID);
		}

		// 所持している条件セットグループリストから、権限を確認
		for(JknGrp jknGrp:this.jknGrpList) {
			if(allFlg || jknGrp.getJknGrpId().equals(jknGrpId)) {
				return jknGrp.getJokenKbn();
			}
		}
		return JokenKbn.DO_NOT_USE;
	}

	public static String trimDpsScreenId(String screenId) {

		final String uCaseSID = screenId.toUpperCase();
		if(screenId.length() <= 12) {
			return screenId;
		}
		return  uCaseSID.substring(0, 12);

	}

	/**
	 * 指定の条件セット配列のいずれかに一致する場合に真を返す。
	 *
	 * @param jokenSetList 条件セット配列
	 * @return いずれかに一致する場合に真
	 */
	public boolean isMatch(JokenSet... jokenSetArray) {
		if (jokenSetArray == null) {
			final String errMsg = "検査対象の条件セット配列がnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		JokenSet jokenSet = this.getRole().getJokenSet();
		if (jokenSet == null) {
			final String errMsg = "対象ユーザが条件セットを保持していない";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		for (JokenSet entryJokenSet : jokenSetArray) {
			if (entryJokenSet.equals(jokenSet)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 指定の権限を保持している場合に真を返す。
	 *
	 * @param requiredAuth 必要な権限
	 * @return 指定の権限を保持している場合に真
	 */
	public boolean hasAuth(DpAuthority requiredAuth) {
		if (requiredAuth == null) {
			final String errMsg = "検査対象の権限がnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		List<DpAuthority> authList = dpUser.getRole().getAuthorityList();
		if (authList.contains(requiredAuth)) {
			return true;
		}
		return false;
	}

	/**
	 * 指定の権限配列の全てを保持している場合に真を返す。
	 *
	 * @param requiredAuthArray 必要な権限配列
	 * @return 全てを保持する場合に真
	 */
	public boolean hasAuth(DpAuthority... requiredAuthArray) {
		if (requiredAuthArray == null) {
			final String errMsg = "検査対象の権限配列がnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		List<DpAuthority> authList = dpUser.getRole().getAuthorityList();
		for (DpAuthority requiredAuth : requiredAuthArray) {
			if (!authList.contains(requiredAuth)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 指定の権限配列のいずれかを保持している場合に真を返す。
	 *
	 * @param requiredAuthArray 必要な権限配列
	 * @return いずれかを保持する場合に真
	 */
	public boolean hasAuthOr(DpAuthority... requiredAuthArray) {
		if (requiredAuthArray == null) {
			final String errMsg = "検査対象の権限配列がnull";
			throw new SystemException(new Conveyance(ErrMessageKey.PARAMETER_ERROR, errMsg));
		}
		DpUser dpUser = DpUserInfo.getDpUserInfo().getSettingUser();
		List<DpAuthority> authList = dpUser.getRole().getAuthorityList();
		for (DpAuthority requiredAuth : requiredAuthArray) {
			if (authList.contains(requiredAuth)) {
				return true;
			}
		}
		return false;
	}

	//---------------------
	// Protected Method
	// --------------------

	/**
	 * ロールを取得する。
	 *
	 * @return role ロール
	 */
	protected DpRole getRole() {
		return role;
	}

	/**
	 * 組織情報を取得する。
	 *
	 * @return 組織情報
	 */
	protected SosMst getSosMst() {
		return this.sosMst;
	}

	/**
	 * 従業員情報を取得する。
	 *
	 * @return 従業員情報
	 */
	protected JgiMst getJgiMst() {
		return this.jgiMst;
	}

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 従業員番号を取得する。
	 *
	 * @return 従業員番号
	 */
	public Integer getJgiNo() {
		return this.jgiMst.getJgiNo();
	}

	/**
	 * 氏名を取得する。
	 *
	 * @return 氏名
	 */
	public String getJgiName() {
		return this.jgiMst.getJgiName();
	}

	/**
	 * 職制を取得する。
	 *
	 * @return 職制
	 */
	public Shokusei getShokuseiCd() {
		return this.jgiMst.getShokuseiCd();
	}

	/**
	 * 職種を取得する。
	 *
	 * @return 職種
	 */
	public Shokushu getShokushuCd() {
		return this.jgiMst.getShokushuCd();
	}

	/**
	 * 標準組織背番号Ｃ2(支店レベル)を取得する。
	 *
	 * @return 標準組織背番号Ｃ2(支店レベル)
	 */
	public String getSosCd2() {
		return this.jgiMst.getSosCd2();
	}

	/**
	 * 標準組織背番号Ｃ3(営業所レベル)を取得する。
	 *
	 * @return 標準組織背番号Ｃ3(営業所レベル)
	 */
	public String getSosCd3() {
		return this.jgiMst.getSosCd3();
	}

	/**
	 * 標準組織背番号Ｃ4(チームレベル)を取得する。
	 *
	 * @return 標準組織背番号Ｃ4(チームレベル)
	 */
	public String getSosCd4() {
		return this.jgiMst.getSosCd4();
	}

	/**
	 * 標準組織背番号Ｃを取得する。
	 *
	 * @return 標準組織背番号Ｃ
	 */
	public String getSosCd() {
		return this.jgiMst.getSosCd();
	}

	/**
	 * 従業員区分を取得する。
	 *
	 * @return 従業員区分
	 */
	public JgiKb getJgiKb() {
		return this.jgiMst.getJgiKb();
	}

	/**
	 * 部門名正式を取得する。
	 *
	 * @return 部門名正式
	 */
	public String getBumonSeiName() {
		return this.sosMst.getBumonSeiName();
	}

	/**
	 * 部門名略式を取得する。
	 *
	 * @return 部門名略式
	 */
	public String getBumonRyakuName() {
		return this.sosMst.getBumonRyakuName();
	}

	/**
	 * 組織名称を取得する。
	 *
	 * @return 組織名称
	 */
	public String getSosName() {
		return this.sosMst.getSosName();
	}

	/**
	 * 上位組織コードを取得する。
	 *
	 * @return 上位組織コード
	 */
	public String getUpSosCd() {
		return this.sosMst.getUpSosCd();
	}

	/**
	 * 部門ランクを取得する。
	 *
	 * @return 部門ランク
	 */
	public BumonRank getBumonRank() {
		return this.sosMst.getBumonRank();
	}

	/**
	 * @return jknGrpList
	 */
	public List<JknGrp> getJknGrpList() {
		return jknGrpList;
	}

	/**
	 * 副担当MR（主担当施設をもたないMR）を取得する。
	 *
	 * @return 副担当MR（主担当施設をもたないMR）
	 */
	public boolean isSubMr() {
		return this.subMr;
	}

	/**
	 * ONC組織判定を取得する。
	 *
	 * @return ONC組織判定（true：ONC組織、false：ONC組織以外）
	 */
	public boolean isOncSos() {
		return BooleanUtils.isTrue(this.sosMst.getOncSosFlg());
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ判定
	 */
	public List<SosMstCtg> getSosCategoryList() {
		return this.sosMst.getSosCategoryList();
	}

	/**
	 * 【暫定対応】カテゴリリストのうち最初のカテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	@Deprecated
	public String getSosCategory() {
		if(this.sosMst.getSosCategoryList() != null) {
			return this.sosMst.getSosCategoryList().get(0).getCategoryName();
		}
		return null;
	}

	/**
	 * サブカテゴリを取得する。
	 *
	 * @return カテゴリコード
	 */
	public String getSosSubCategory() {
		return this.sosMst.getSosSubCategory();
	}

	/**
	 * 支店コードを取得する。
	 *
	 * @return 支店コード
	 */
	public String getBrCode() {
		return this.sosMst.getBrCode();
	}

	/**
	 * 営業所コードを取得する。
	 *
	 * @return 営業所コード
	 */
	public String getDistCode() {
		return this.sosMst.getDistCode();
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	public String getName() {
		return this.jgiMst.getJgiNo().toString();
	}

	@Override
	public int compareTo(DpUser obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.getJgiNo(), obj.getJgiNo()).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DpUser.class.isAssignableFrom(entry.getClass())) {
			DpUser obj = (DpUser) entry;
			return new EqualsBuilder().append(this.getJgiNo(), obj.getJgiNo()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.getJgiNo()).toHashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("従業員番号=" + this.jgiMst.getJgiNo() + ",");
		sb.append("氏名=" + this.jgiMst.getJgiName() + ",");
		sb.append("ロール=[");
		sb.append(this.role);
		sb.append("] , ");
		sb.append("画面権限条件リスト=[");
		for(JknGrp jknGrp:this.jknGrpList) {
			sb.append(jknGrp.toString());
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public DpUser clone() {
		return new DpUser(this.jgiMst.clone(), this.sosMst.clone(), this.role.clone(), this.jknGrpList, this.subMr,this.tokuyakuJknSetList);
	}
}
