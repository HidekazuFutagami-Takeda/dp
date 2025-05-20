package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.InsMstScDto;
import jp.co.takeda.logic.ConvertInsKanaLogic;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.MrCat;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.StringUtil;

/**
 * 施設情報にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insMstRealDao")
public class InsMstRealDaoImpl extends AbstractManageDao implements InsMstRealDao {

	private static final String SQL_MAP = "DPM_C_VI_INS_MST_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * 共通ＤＡＯ
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	// 施設情報を取得
	public InsMst searchReal(String insNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("insNo", insNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return super.select(getSqlMapName() + ".selectReal", paramMap);
	}

	// 施設情報を取得
	public List<InsMst> searchRealList(InsMstScDto scDto) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getSosCd3() == null && scDto.getSosCd4() == null && scDto.getJgiNo() == null) {
			final String errMsg = "組織・従業員がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 施設名カナの特殊対応
		// ----------------------
		ConvertInsKanaLogic logic = new ConvertInsKanaLogic(commonDao);
		String searchKana = logic.execute(scDto.getInsKanaSrch());

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sosCd3", scDto.getSosCd3());
		paramMap.put("sosCd4", scDto.getSosCd4());
		paramMap.put("jgiNo", scDto.getJgiNo());
		paramMap.put("searchInsType", scDto.getSearchInsType() != null ? scDto.getSearchInsType().getDbValue() : null);
		if (StringUtils.isNotBlank(scDto.getInsFormalName()) || StringUtils.isNotBlank(searchKana)) {
			paramMap.put("likeSearchFlg", Boolean.valueOf(true));
		} else {
			paramMap.put("likeSearchFlg", null);
		}
		paramMap.put("insFormalName", StringUtil.toOracleTextEsc(scDto.getInsFormalName()));
		paramMap.put("insKanaSrch", StringUtil.toOracleTextEsc(searchKana));
		paramMap.put("activityType", scDto.getActivityType());
		paramMap.put("addrCodePref", scDto.getAddrCodePref());
		paramMap.put("addrCodeCity", scDto.getAddrCodeCity());
		paramMap.put("jgiKbList", new JgiKb[]{ JgiKb.JGI, JgiKb.CONTRACT_MR, JgiKb.EIGYOSHO_ZATU , JgiKb.DUMMY_MR});
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(scDto.getOyakoKb(),scDto.getOyakoKb2(),scDto.getOyakoKbProdCode()));

		// ----------------------
		// 検索実行
		// ----------------------
		return super.selectList(getSqlMapName() + ".selectRealList", paramMap);
	}


	// 施設情報を取得(施設を担当するMR情報も取得)
	public InsMst searchRealIncludeMr(String insNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// MR種別を指定して取得
		InsMst insMst;
		if(DpUserInfo.getDpUserInfo().getSysType() == SysType.IYAKU){
			insMst = searchRealIncludeMr(insNo,MrCat.MR);
		}else {
			insMst = searchRealIncludeMr(insNo,MrCat.VAC_MR);
		}

		// 担当者情報を削除
		insMst.setJgiNo(null);
		insMst.setJgiName(null);

		return insMst;
	}

	// 施設情報を取得(施設を担当するMR情報も取得)
	public InsMst searchRealIncludeMr(String insNo, MrCat mrCat) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (mrCat == null) {
			final String errMsg = "MR種類がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo", insNo);
		paramMap.put("mrCat", mrCat);

		// ----------------------
		// 検索実行
		// ----------------------
		return super.select(getSqlMapName() + ".selectRealByUnique", paramMap);
	}

	// 施設情報を取得(施設を担当するMR情報も取得)
	public InsMst searchRealIncludeMr(String insNo, Integer jgiNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo", insNo);
		paramMap.put("jgiNo", jgiNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return super.select(getSqlMapName() + ".selectRealByUnique", paramMap);
	}

	// 施設情報を取得(施設を担当するMR情報も取得)
	public InsMst searchRealIncludeMr(String insNo, String prodCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定Cがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo", insNo);
		paramMap.put("prodCode", prodCode);
		// ----------------------
		// 検索実行
		// ----------------------
		return super.select(getSqlMapName() + ".selectRealByUnique", paramMap);
	}

	// 施設情報を取得(施設を担当するMR情報も取得)
	public InsMst searchRealIncludeMrOyako(String insNo, String prodCode, String oyakoKb, String oyakoKb2, String oyakoKbProdCode) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo", insNo);
		if (prodCode == null) {
			// MR種別を指定して取得
			MrCat mrCat = null;
			if(DpUserInfo.getDpUserInfo().getSysType() == SysType.IYAKU){
				mrCat = MrCat.MR;
			}else {
				mrCat = MrCat.VAC_MR;
			}
			paramMap.put("mrCat", mrCat);
		} else {
			paramMap.put("prodCode", prodCode);
		}
		paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));

		// ----------------------
		// 検索実行
		// ----------------------
		return super.select(getSqlMapName() + ".selectRealByUniqueOyako", paramMap);
	}
}
