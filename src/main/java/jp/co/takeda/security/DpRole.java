package jp.co.takeda.security;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.Model;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.security.DpAuthority.AuthType;

/**
 * 納入計画システムのロールを表すクラス
 *
 * @author tkawabata
 */
public final class DpRole extends Model<DpRole> implements Cloneable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 条件セットのプライマリーマップ
	 */
	private static final Map<JokenSet, Integer> JOKENSET_PRIMARY_MAP;

	/**
	 * 本部権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_HONBU;

	/**
	 * 本部ワクチンＧ権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_HONBU_WAKUTIN_G;

	/**
	 * 支店長権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_SITEN_MASTER;

	/**
	 * 支店スタッフ権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_SITEN_STAFF;

	/**
	 * 営業所長権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_OFFICE_MASTER;

	/**
	 * 営業所スタッフ権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_OFFICE_STAFF;

	/**
	 * ＡＬ権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_IYAKU_AL;

	/**
	 * ＭＲ権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_IYAKU_MR;

	/**
	 * ＭＲ（副担当）権限リスト
	 */
	public static final List<DpAuthority> AUTH_LIST_IYAKU_SUB_MR;

	/**
	 * 整形ＭＲ権限リスト
	 */
	@Deprecated
	private static final List<DpAuthority> AUTH_LIST_IYAKU_SEIKEI_MR;

	/**
	 * 小児科ＡＣ権限リスト (J19-0010 対応・コメントのみ修正)
	 */
	private static final List<DpAuthority> AUTH_LIST_WAKUTIN_AL;

	/**
	 * 小児科ＭＲ権限リスト (J19-0010 対応・コメントのみ修正)
	 */
	private static final List<DpAuthority> AUTH_LIST_WAKUTIN_MR;

	/**
	 * 特約店部長権限リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_TOKUYAKUTEN_MASTER;

	/**
	 * 特約店ＧＭリスト
	 */
	@Deprecated
	private static final List<DpAuthority> AUTH_LIST_TOKUYAKUTEN_G_MASTER;

	/**
	 * 特約店Ｇスタッフリスト
	 */
	private static final List<DpAuthority> AUTH_LIST_TOKUYAKUTEN_G_STAFF;

	/**
	 * 特約店業務Ｇリスト
	 */
	@Deprecated
	private static final List<DpAuthority> AUTH_LIST_TOKUYAKUTEN_GYOUMU_G;

	/**
	 * 特約店担当リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_TOKUYAKUTEN_TANTOU;


	/**
	 * ワクチンACリスト
	 */
	private static final List<DpAuthority> AUTH_LIST_VACCINE_AC;

	/**
	 * 全営業副所長リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_OFFICE_SUB_MASTER;

	/**
	 * 特約店GM（兼務あり）リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_TOKUYAKUTEN_GM_KENMU_ARI;

	/**
	 * 特約店GM（兼務なし）リスト
	 */
	private static final List<DpAuthority> AUTH_LIST_TOKUYAKUTEN_GM_KENMU_NASHI;


	/**
	 * STATICイニシャライザ
	 */
	static {

		// 条件セットの優先順
		Map<JokenSet, Integer> tmpMap = new HashMap<JokenSet, Integer>();
		tmpMap.put(JokenSet.HONBU, Integer.valueOf(1));           // JKN0255: 1
		tmpMap.put(JokenSet.HONBU_WAKUTIN_G, Integer.valueOf(2)); // JKN0256: 2
		tmpMap.put(JokenSet.SITEN_MASTER, Integer.valueOf(10));   // JKN0011:10
		tmpMap.put(JokenSet.SITEN_STAFF, Integer.valueOf(15));    // JKN0013:15
		tmpMap.put(JokenSet.OFFICE_MASTER, Integer.valueOf(20));  // JKN0015:20
		tmpMap.put(JokenSet.OFFICE_STAFF, Integer.valueOf(25));   // JKN0016:25
		tmpMap.put(JokenSet.IYAKU_AL, Integer.valueOf(30));
		tmpMap.put(JokenSet.IYAKU_MR, Integer.valueOf(35));
		tmpMap.put(JokenSet.IYAKU_SEIKEI_MR, Integer.valueOf(35));
		tmpMap.put(JokenSet.TOKUYAKUTEN_MASTER, Integer.valueOf(10));  // JKN0036:10
		tmpMap.put(JokenSet.TOKUYAKUTEN_G_MASTER, Integer.valueOf(15));
		tmpMap.put(JokenSet.TOKUYAKUTEN_GM_KENMU_NASHI, Integer.valueOf(22)); // JKN0101:22
		tmpMap.put(JokenSet.TOKUYAKUTEN_GM_KENMU_ARI, Integer.valueOf(24)); // JKN0101:22
		tmpMap.put(JokenSet.TOKUYAKUTEN_G_STAFF, Integer.valueOf(20)); // JKN0254:20
		tmpMap.put(JokenSet.TOKUYAKUTEN_GYOUMU_G, Integer.valueOf(25));
		tmpMap.put(JokenSet.TOKUYAKUTEN_G_TANTOU, Integer.valueOf(30)); // JKN0040:30
		tmpMap.put(JokenSet.WAKUTIN_AL, Integer.valueOf(33));
		tmpMap.put(JokenSet.WAKUTIN_MR, Integer.valueOf(35));
		JOKENSET_PRIMARY_MAP = Collections.unmodifiableMap(tmpMap);


		// ------------------------------------------------------------------------------------------------------------
		// 本部
		// ------------------------------------------------------------------------------------------------------------
		List<DpAuthority> tmpList = new ArrayList<DpAuthority>();
		tmpList.add(new DpAuthority(AuthType.REFER));
		tmpList.add(new DpAuthority(AuthType.EDIT));
		tmpList.add(new DpAuthority(AuthType.OUTPUT));
		AUTH_LIST_HONBU = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 本部ワクチンＧ
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_HONBU_WAKUTIN_G = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 支店長、支店Ｓ
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_SITEN_MASTER = Collections.unmodifiableList(tmpList);
		AUTH_LIST_SITEN_STAFF = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 営業所長、営業所Ｓ
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_OFFICE_MASTER = Collections.unmodifiableList(tmpList);
		AUTH_LIST_OFFICE_STAFF = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// ＡＬ
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_IYAKU_AL = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// ＭＲ
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_IYAKU_MR = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// ＭＲ（副担当）
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_IYAKU_SUB_MR = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 整形ＭＲ
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_IYAKU_SEIKEI_MR = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 小児科ＡＣ (J19-0010 対応・コメントのみ修正)
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_WAKUTIN_AL = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 小児科ＭＲ (J19-0010 対応・コメントのみ修正)
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_WAKUTIN_MR = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 特約店部長、特約店ＧＭ、特約店Ｇスタッフ、特約店業務Ｇ
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_TOKUYAKUTEN_MASTER = Collections.unmodifiableList(tmpList);
		AUTH_LIST_TOKUYAKUTEN_G_MASTER = Collections.unmodifiableList(tmpList);
		AUTH_LIST_TOKUYAKUTEN_G_STAFF = Collections.unmodifiableList(tmpList);
		AUTH_LIST_TOKUYAKUTEN_GYOUMU_G = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 特約店担当者
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_TOKUYAKUTEN_TANTOU = Collections.unmodifiableList(tmpList);


		// ------------------------------------------------------------------------------------------------------------
		// ワクチンAC
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_VACCINE_AC = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 全営業副所長
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_OFFICE_SUB_MASTER = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 特約店GM（兼務あり）
		// ------------------------------------------------------------------------------------------------------------
		AUTH_LIST_TOKUYAKUTEN_GM_KENMU_ARI = Collections.unmodifiableList(tmpList);

		// ------------------------------------------------------------------------------------------------------------
		// 特約店GM（兼務なし）
		// ------------------------------------------------------------------------------------------------------------
AUTH_LIST_TOKUYAKUTEN_GM_KENMU_NASHI = Collections.unmodifiableList(tmpList);
	}

	/**
	 * コンストラクタ
	 *
	 * @param jokenSet 条件セット
	 */
	public DpRole(List<JokenSet> jokenSetList) {

		JokenSet jSet = null;
		if (jokenSetList == null) {
			final String errMsg = "条件セットリストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		} else if (jokenSetList.size() == 1) {
			jSet = jokenSetList.get(0);
		} else {
			Integer primaryKey = null;
			Integer key = null;
			for (JokenSet joken : jokenSetList) {
				key = JOKENSET_PRIMARY_MAP.get(joken);

				if ((primaryKey == null) || (key != null && primaryKey.compareTo(key) > 0)) {
					primaryKey = key;
					jSet = joken;
				}
			}
		}
		if (jSet == null) {
			final String errMsg = "納入計画システムで許容されている条件セットが見つからない";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		String rName = null;
		List<DpAuthority> aList = null;

		switch (jSet) {

			// 本部
			case HONBU:
				rName = "本部";
				aList = AUTH_LIST_HONBU;
				break;

			// 本部（ワクチン）
			case HONBU_WAKUTIN_G:
				aList = AUTH_LIST_HONBU_WAKUTIN_G;
				break;

			// 支店長
			case SITEN_MASTER:
				rName = "支店長";
				aList = AUTH_LIST_SITEN_MASTER;
				break;

			// 支店Ｓ
			case SITEN_STAFF:
				rName = "支店Ｓ";
				aList = AUTH_LIST_SITEN_STAFF;
				break;

			// 営業所長
			case OFFICE_MASTER:
				rName = "営業所長";
				aList = AUTH_LIST_OFFICE_MASTER;
				break;

			// 営業所Ｓ
			case OFFICE_STAFF:
				rName = "営業所Ｓ";
				aList = AUTH_LIST_OFFICE_STAFF;
				break;

			// ＡＬ
			case IYAKU_AL:
				rName = "ＴＬ";
				aList = AUTH_LIST_IYAKU_AL;
				break;

			// ＭＲ
			case IYAKU_MR:
				rName = "ＭＲ";
				aList = AUTH_LIST_IYAKU_MR;
				break;

			// 整形ＭＲ
			case IYAKU_SEIKEI_MR:
				rName = "整形ＭＲ";
				aList = AUTH_LIST_IYAKU_SEIKEI_MR;
				break;

			// 小児科ＡＣ (J19-0010 対応・コメントのみ修正)
			case WAKUTIN_AL:
				rName = "ワクチンＴＬ";
				aList = AUTH_LIST_WAKUTIN_AL;
				break;

			// 小児科ＭＲ (J19-0010 対応・コメントのみ修正)
			case WAKUTIN_MR:
				rName = "ワクチンＭＲ";
				aList = AUTH_LIST_WAKUTIN_MR;
				break;

			// 特約店部長
			case TOKUYAKUTEN_MASTER:
				rName = "特約店部長";
				aList = AUTH_LIST_TOKUYAKUTEN_MASTER;
				break;

			// 特約店ＧＭ
			case TOKUYAKUTEN_G_MASTER:
				rName = "特約店ＧＭ";
				aList = AUTH_LIST_TOKUYAKUTEN_G_MASTER;
				break;

			// 特約店Ｇスタッフ
			case TOKUYAKUTEN_G_STAFF:
				rName = "特約店Ｇスタッフ";
				aList = AUTH_LIST_TOKUYAKUTEN_G_STAFF;
				break;

			// 特約店業務Ｇ
			case TOKUYAKUTEN_GYOUMU_G:
				rName = "特約店業務Ｇ";
				aList = AUTH_LIST_TOKUYAKUTEN_GYOUMU_G;
				break;

			// 特約店Ｇ担当
			case TOKUYAKUTEN_G_TANTOU:
				rName = "特約店担当";
				aList = AUTH_LIST_TOKUYAKUTEN_TANTOU;
				break;

			// 特約店Ｇ担当
			case VACCINE_AC:
				rName = "ワクチンAC";
				aList = AUTH_LIST_VACCINE_AC;
				break;

			// 特約店Ｇ担当
			case OFFICE_SUB_MASTER:
				rName = "全営業副所長";
				aList = AUTH_LIST_OFFICE_SUB_MASTER;
				break;

			case TOKUYAKUTEN_GM_KENMU_ARI:
				rName = "特約店GM（兼務あり）";
				aList = AUTH_LIST_TOKUYAKUTEN_GM_KENMU_ARI;
				break;

			case TOKUYAKUTEN_GM_KENMU_NASHI:
				rName = "特約店GM（兼務なし）";
				aList = AUTH_LIST_TOKUYAKUTEN_GM_KENMU_NASHI;
				break;



			// 想定外
			default:
				final String errMsg = "想定外の条件セット";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.jokenSet = jSet;
		this.roleName = rName;
		this.authorityList = aList;
	}

	/**
	 * コンストラクタ<br>
	 *
	 * @param jokenSet 条件セット
	 */
	public DpRole(JokenSet jokenSet, String roleName, List<DpAuthority> authorityList) {
		this.jokenSet = jokenSet;
		this.roleName = roleName;
		this.authorityList = authorityList;
	}

	/**
	 * 新しい権限リストを生成する。<br>
	 * 権限を追加する場合は、本メソッドでリストを再生成した上で追加しないと全体に適用されてしまう。
	 *
	 * @return 新しい権限リスト
	 */
	protected List<DpAuthority> createNewAuthorityList(final List<DpAuthority> entryList) {
		if (entryList == null || entryList.isEmpty()) {
			return null;
		}
		List<DpAuthority> dpAuthorityList = new ArrayList<DpAuthority>();
		for (DpAuthority authority : entryList) {
			dpAuthorityList.add(authority);
		}
		return dpAuthorityList;
	}

	/**
	 * 条件セット
	 */
	private final JokenSet jokenSet;

	/**
	 * ロール名
	 */
	private final String roleName;

	/**
	 * 権限
	 */
	private final List<DpAuthority> authorityList;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 条件セットを取得する。
	 *
	 * @return 条件セット
	 */
	public JokenSet getJokenSet() {
		return jokenSet;
	}

	/**
	 * ロール名を取得する。
	 *
	 * @return ロール名
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * 権限リストを取得する。
	 *
	 * @return authorityList 権限リスト
	 */
	public List<DpAuthority> getAuthorityList() {
		return authorityList;
	}

	@Override
	public String toString() {
		return this.roleName;
	}

	@Override
	public int compareTo(DpRole obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.jokenSet, obj.jokenSet).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && DpRole.class.isAssignableFrom(entry.getClass())) {
			DpRole obj = (DpRole) entry;
			return new EqualsBuilder().append(this.jokenSet, obj.jokenSet).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.jokenSet).toHashCode();
	}

	@Override
	public DpRole clone() {
		return new DpRole(jokenSet, roleName, authorityList);
	}
}
