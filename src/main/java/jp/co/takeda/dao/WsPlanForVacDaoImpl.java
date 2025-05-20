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
import jp.co.takeda.model.WsPlanForVac;
import jp.co.takeda.model.div.KaBaseKb;
import jp.co.takeda.security.DpUser;
import jp.co.takeda.security.DpUserInfo;

/**
 * ワクチン用特約店別計画にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("wsPlanForVacDao")
public class WsPlanForVacDaoImpl extends AbstractDao implements WsPlanForVacDao {

	private static final String SQL_MAP = "DPS_V_WS_PLAN_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// ワクチン用特約店別計画を取得
	public WsPlanForVac search(Long seqKey) throws DataNotFoundException {
		return (WsPlanForVac) super.selectBySeqKey(seqKey);
	}

	// ユニークキーで特約店別計画を取得
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
	public WsPlanForVac searchUk(String tmsTytenCd, String prodCode, String sosCd, KaBaseKb kaBaseKb) throws DataNotFoundException {

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
		WsPlanForVac record = new WsPlanForVac();
		record.setTmsTytenCd(tmsTytenCd);
		record.setProdCode(prodCode);
		record.setSosCd(sosCd);
		record.setKaBaseKb(kaBaseKb);
		return (WsPlanForVac) super.selectByUniqueKey(record);
	}

	// 特約店別計画リストを取得
	public List<WsPlanForVac> searchList(String sortString, String sosCd2, String tmsTytenCd, String tmsTytenCdPart, String prodCode, String sosCd, KaBaseKb kaBaseKb) throws DataNotFoundException {

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
// add Start 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
//		paramMap.put("sosCd2", sosCd2);
		paramMap.put("sosCd2", null);
// add End 2022/7/20  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		paramMap.put("tmsTytenCd", tmsTytenCd);
		paramMap.put("tmsTytenCdPart", tmsTytenCdPart);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd", sosCd);
// mod 2022/7/19  Y.Taniguchi バックログNo.10　仕入特約店をＹベースでの計画修正を画面から行えるようにする
		paramMap.put("kaBaseKb", kaBaseKb);

		return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
	}

	// 品目固定コードを元に特約店別計画の積上値を計画値にコピー
	public int updateCopyStackToPlanned(List<String> prodCode) {
		// ----------------------
		// 引数チェック
		// ----------------------
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
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("prodCode", prodCode);
		paramMap.put("upJgiNo", dpUser.getJgiNo());
		paramMap.put("upJgiName", dpUser.getJgiName());

		// ----------------------
		// 更新実行
		// ----------------------
		int count = dataAccess.execute(getSqlMapName() + ".updateCopyStackToPlanned", paramMap);

		return count;
	}

	// ワクチン用特約店別計画を登録
	public void insert(WsPlanForVac record) throws DuplicateException {
		super.insert(record);
	}

	// ワクチン用特約店別計画を更新
	public int update(WsPlanForVac record) {
		return super.update(record);
	}

	// ワクチン用特約店別計画を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
