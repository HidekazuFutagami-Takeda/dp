package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.ErrMessageKey;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.MikakutokuSijou;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;

/**
 * 未獲得市場にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("mikakutokuSijouDao")
public class MikakutokuSijouDaoImpl extends AbstractDao implements MikakutokuSijouDao {

	private static final String SQL_MAP = "DPS_I_MIKAKUTOKU_SIJOU_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsSyComInsOyakoDao")
	protected DpsSyComInsOyakoDao dpsSyComInsOyakoDao;

	// 未獲得市場を取得
	public MikakutokuSijou search(Long seqKey) throws DataNotFoundException {
		return (MikakutokuSijou) super.selectBySeqKey(seqKey);
	}

	// 未獲得市場を更新
	public int update(MikakutokuSijou record) {
		return super.update(record);
	}

	// 組織コード・薬効市場コードより、未獲得市場のサマリーデータを取得
	public List<MikakutokuSijou> searchSumList(String sortString, String sosCd3, String yakkouSijouCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createMap(sortString, sosCd3, yakkouSijouCode);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		PlannedCtg plannedCtg = new PlannedCtg();
//		plannedCtg = plannedCtgDao.search(scDto.getProdCategory());

		// ----------------------
		// 検索実行
		// ----------------------
		List<MikakutokuSijou> resultList = new ArrayList<MikakutokuSijou>();
		List<MikakutokuSijou> resultUHList = new ArrayList<MikakutokuSijou>();
		List<MikakutokuSijou> resultPList = new ArrayList<MikakutokuSijou>();
		try {
			paramMap.put("insType", InsType.UH);
			resultUHList = dataAccess.queryForList(getSqlMapName() + ".selectSumList", paramMap);
		} catch (DataNotFoundException e) {
		}
		try {
			paramMap.put("insType", InsType.P);
			resultPList = dataAccess.queryForList(getSqlMapName() + ".selectSumList", paramMap);
		} catch (DataNotFoundException e) {
		}
		resultList.addAll(resultUHList);
		resultList.addAll(resultPList);
		if (resultList.size() == 0) {
			throw new DataNotFoundException(new Conveyance(ErrMessageKey.DATA_NOT_FOUND_ERROR, "検索結果0件"));
		}
		return resultList;
	}

	// 組織コード・薬効市場コードより、未獲得市場の全リストを取得
	public List<MikakutokuSijou> searchList(String sortString, String sosCd3, String yakkouSijouCode, String oyakoKb,String oyakoKb2, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		int oyakoCount = 0;
		String oyakoKbProdCode = prodCode;
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpsSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createMap(sortString, sosCd3, yakkouSijouCode);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
		paramMap.put("sortString", sortString);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	/**
	 * 引数チェック・検索条件作成を行う。
	 *
	 * @param sortString ソート順
	 * @param sosCd3 組織コード(営業所)
	 * @param yakkouSijouCode 薬効市場コード
	 * @return 検索条件マップ
	 */
	private Map<String, Object> createMap(String sortString, String sosCd3, String yakkouSijouCode) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (yakkouSijouCode == null) {
			final String errMsg = "薬効市場コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("yakkouSijouCode", yakkouSijouCode);
		// 固定条件
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		paramMap.put("jgiKbList", jgiKbList);
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		return paramMap;
	}
}
