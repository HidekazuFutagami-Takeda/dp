package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.WsPlan;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * 特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("wsPlanDao")
public class WsPlanDaoImpl extends AbstractDao implements WsPlanDao {

	private static final String SQL_MAP = "DPS_I_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ユニークキーで特約店別計画を取得
	public WsPlan searchUk(String tmsTytenCd, String prodCode, String sosCd, KaBaseKb kaBaseKb) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (tmsTytenCd == null) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (kaBaseKb == null) {
			final String errMsg = "価ベース区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		WsPlan record = new WsPlan();
		record.setTmsTytenCd(tmsTytenCd);
		record.setProdCode(prodCode);
		record.setSosCd(sosCd);
		record.setKaBaseKb(kaBaseKb);
		return (WsPlan) super.selectByUniqueKey(record);
	}

	// 特約店別計画リストを取得
	public WsPlan search(Long seqKey) throws DataNotFoundException {
		return (WsPlan) super.selectBySeqKey(seqKey);
	}

	// 特約店別計画リストを取得
	public List<WsPlan> searchList(String sortString, String tmsTytenCd, String tmsTytenCdPart, String prodCode, String sosCd, String tytenKisLevel, KaBaseKb kaBaseKb) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if ((tmsTytenCd == null) && (tmsTytenCdPart == null)) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("tmsTytenCd", tmsTytenCd);
		paramMap.put("tmsTytenCdPart", tmsTytenCdPart);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd", sosCd);
		paramMap.put("tytenKisLevel", tytenKisLevel);
		paramMap.put("kaBaseKb", kaBaseKb);
		// 固定条件
		paramMap.put("planTargetFlg", true);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 組織(営業所)と品目固定コードを元に特約店別計画の積上値を計画値にコピーする
	public int updateCopyStackToPlanned(List<String> sosCd, List<String> prodCode) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (sosCd.size() == 0) {
			final String errMsg = "組織コード(営業所)が0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));

		} else if (prodCode.size() == 0) {
			final String errMsg = "品目固定コードが0件";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 更新条件生成
		// ----------------------
		DpUser dpUser = DpUserInfo.getDpUserInfo().getLoginUser();
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("sosCd", sosCd);
		paramMap.put("prodCode", prodCode);
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());

		// ----------------------
		// 更新実行
		// ----------------------
		int count = dataAccess.execute(getSqlMapName() + ".updateCopyStackToPlanned", paramMap);

		return count;
	}

	// 特約店別計画を登録
	public void insert(WsPlan record) throws DuplicateException {
		super.insert(record);
	}

	// 特約店別計画を更新
	public int update(WsPlan record) {
		return super.update(record);
	}

	// 特約店別計画を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}

	// 組織(営業所)と品目を元に特約店別計画を削除
	public int deleteSosProd(String prodCode, String sosCd) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 削除条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd", sosCd);
		paramMap.put("kaBaseKb", KaBaseKb.Y_KA_BASE);

		// ----------------------
		// 削除実行
		// ----------------------
		int count = dataAccess.execute(getSqlMapName() + ".deleteSosProd", paramMap);

		return count;
	}

	@Override
	public List<WsPlan> searchListFilterByCategory(String sortString, String tmsTytenCd, String sosCd,
			KaBaseKb kaBaseKb, String category) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if ((tmsTytenCd == null)) {
			final String errMsg = "TMS特約店コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("tmsTytenCd", tmsTytenCd);
		paramMap.put("sosCd", sosCd);
		paramMap.put("kaBaseKb", kaBaseKb);
		paramMap.put("category", category);
		// 固定条件
		paramMap.put("planTargetFlg", true);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}
}
