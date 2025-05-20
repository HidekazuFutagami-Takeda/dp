package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.model.EstParamOffice;

import org.springframework.stereotype.Repository;

/**
 * 試算パラメータ(営業所)にアクセスするDAO実装クラス
 * 
 * @author khashimoto
 */
@Repository("estParamOfficeDao")
public class EstParamOfficeDaoImpl extends AbstractDao implements EstParamOfficeDao {

	private static final String SQL_MAP = "DPS_I_EST_PARAM_OFFICE_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	// 試算パラメータ(営業所)を取得
	public EstParamOffice search(Long seqKey) throws DataNotFoundException {
		return (EstParamOffice) super.selectBySeqKey(seqKey);
	}

	// 組織コード、品目固定コードを指定して、試算パラメータ(営業所)を取得
	public EstParamOffice search(String sosCd, String prodCode) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		EstParamOffice record = new EstParamOffice();
		record.setSosCd(sosCd);
		record.setProdCode(prodCode);
		return (EstParamOffice) super.selectByUniqueKey(record);
	}

	// 試算パラメータ(営業所)を登録
	public void insert(EstParamOffice record) throws DuplicateException {
		super.insert(record);
	}

	// 試算パラメータ(営業所)を更新
	public int update(EstParamOffice record) {
		return super.update(record);
	}

	// 試算パラメータ(営業所)を削除
	public int delete(Long seqKey, Date upDate) {
		return super.deleteBySeqKey(seqKey, upDate);
	}
}
