package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.OfficePlanStatus;

/**
 * 営業所計画ステータスにアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("officePlanStatusDao")
public class OfficePlanStatusDaoImpl extends AbstractDao implements OfficePlanStatusDao {

	private static final String SQL_MAP = "DPS_I_OFFICE_PLAN_STATUS_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 営業所計画ステータスを取得
	public OfficePlanStatus search(Long seqKey) throws DataNotFoundException {
		return (OfficePlanStatus) super.selectBySeqKey(seqKey);
	}

	// 組織コード、カテゴリをもとに、営業所計画ステータスを取得
	public OfficePlanStatus search(String sosCd, String prodCategory) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCategory == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		OfficePlanStatus record = new OfficePlanStatus();
		record.setSosCd(sosCd);
		record.setProdCategory(prodCategory);
		return (OfficePlanStatus) super.selectByUniqueKey(record);
	}

	// 組織コード、カテゴリをもとに、営業所計画ステータスを取得
	public List<OfficePlanStatus> searchSosCdCategoryList(String sosCd, String prodCategory) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCategory == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		OfficePlanStatus record = new OfficePlanStatus();
		record.setSosCd(sosCd);
		record.setProdCategory(prodCategory);
		return dataAccess.queryForList(getSqlMapName() + ".selectUk", record);
	}

	// 営業所計画ステータスを登録
	public void insert(OfficePlanStatus record) throws DuplicateException {
		super.insert(record);
	}

	// 営業所計画ステータスを更新
	public int update(OfficePlanStatus record) {
		return super.update(record);
	}

	// 営業所計画ステータスを削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
