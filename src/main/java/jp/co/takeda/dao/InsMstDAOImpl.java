package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

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
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.div.DpsSearchInsType;
import jp.co.takeda.dto.DpsInsMstScDto;
import jp.co.takeda.logic.ConvertInsKanaLogic;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.OldInsrFlg;
import jp.co.takeda.model.div.SysType;
import jp.co.takeda.security.DpUserInfo;
import jp.co.takeda.util.StringUtil;

/**
 * 施設情報にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("insMstDAO")
public class InsMstDAOImpl extends AbstractDao implements InsMstDAO {

	private static final String SQL_MAP = "DPS_C_INS_MST_SqlMap";

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

	/**
	 * 計画支援汎用マスタDAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterDao")
	private DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsSyComInsOyakoDao")
	protected DpsSyComInsOyakoDao dpsSyComInsOyakoDao;

	// 施設情報を取得
	public DpsInsMst search(String insNo) throws DataNotFoundException {
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
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("insNo", insNo);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForObject(getSqlMapName() + ".selectMR01ByInsNo", paramMap);
//		return search(insNo,null,null);
	}

	// 施設情報を取得
// mod Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
//	public DpsInsMst search(String insNo, Integer jgiNo, String prodCode, String oyakoKb, String oyakoKb2 ,String category) throws DataNotFoundException {
	public List<DpsInsMst> search(String insNo, Integer jgiNo, String prodCode, String oyakoKb, String oyakoKb2 ,String category) throws DataNotFoundException {
// mod End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo", insNo);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("prodCode", prodCode);

//		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
//		SysType sysType = DpUserInfo.getDpUserInfo().getSysManage().getSysType();
//		if (sysType != null && SysType.IYAKU == sysType) {
//			paramMap.put("isIyaku", Boolean.valueOf(true));
//		}
		if (vaccineCode.equals(category)) {
			paramMap.put("isIyaku", Boolean.valueOf(false));
// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
			paramMap.put("sortMrCat", "02");
// add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
		}else {
// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
			paramMap.put("sortMrCat", "01");
//add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
			paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
			SysType sysType = DpUserInfo.getDpUserInfo().getSysManage().getSysType();
			if (sysType != null && SysType.IYAKU == sysType) {
				paramMap.put("isIyaku", Boolean.valueOf(true));
			}
		}
        paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));

		// ----------------------
		// 検索実行
		// ----------------------
// mod Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
//		if(StringUtils.isEmpty(prodCode)){
//			return dataAccess.queryForObject(getSqlMapName() + ".selectList", paramMap);
//		} else {
//			return dataAccess.queryForObject(getSqlMapName() + ".selectListByProd", paramMap);
//		}
		if(StringUtils.isEmpty(prodCode)){
			return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		} else {
			return dataAccess.queryForList(getSqlMapName() + ".selectListByProd", paramMap);
		}
// mod End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
	}

	// 施設情報を取得
	public List<DpsInsMst> searchList(String sortString, DpsInsMstScDto scDto, String oyakoKb, String oyakoKb2 ,String category) throws DataNotFoundException {
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

		// -----------------------------
		// ワクチンコードの取得
		// -----------------------------
		List<DpsCCdMst> searchList = new ArrayList<DpsCCdMst>();
		try {
			// カテゴリの検索
			searchList = dpsCodeMasterDao.searchCodeByDataKbn(DpsCodeMaster.VAC.getDbValue());
		} catch (DataNotFoundException e) {
			final String errMsg = "計画管理汎用マスタに、「" + DpsCodeMaster.VAC.getDbValue() + "」コードが登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}
		String vaccineCode = searchList.get(0).getDataCd();

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		int oyakoCount = 0;
		String oyakoKbProdCode = scDto.getProdCode();
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("sosCd3", scDto.getSosCd3());
		paramMap.put("sosCd4", scDto.getSosCd4());
		paramMap.put("jgiNo", scDto.getJgiNo());
		paramMap.put("searchInsType", scDto.getSearchInsType() != null ? scDto.getSearchInsType().getDbValue().split(",") : null);
		paramMap.put("insFormalName", StringUtil.toOracleTextEsc(scDto.getInsFormalName()));
		paramMap.put("insKanaSrch", StringUtil.toOracleTextEsc(searchKana));
		paramMap.put("activityType", scDto.getActivityType());
		paramMap.put("prodCode", scDto.getProdCode());
		if (vaccineCode.equals(category)) {
			paramMap.put("isIyaku", Boolean.valueOf(false));
// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
			paramMap.put("sortMrCat", "02");
//add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
		}else {
// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
			paramMap.put("sortMrCat", "01");
//add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
			paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);
			SysType sysType = DpUserInfo.getDpUserInfo().getSysManage().getSysType();
			if (sysType != null && SysType.IYAKU == sysType) {
				paramMap.put("isIyaku", Boolean.valueOf(true));
			}
		}

//        paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,scDto.getProdCode()));
        paramMap.put("oyakoKb", OyakoKbHelper.getOyakoKb(oyakoKb,oyakoKb2,oyakoKbProdCode));
        paramMap.put("vaccineSelect", DpsSearchInsType.VAC.getDbValue());

		// ----------------------
		// 検索実行
		// ----------------------
		if(StringUtils.isEmpty(scDto.getProdCode())){
			return dataAccess.queryForList(getSqlMapName() + ".selectList", paramMap);
		} else {
			return dataAccess.queryForList(getSqlMapName() + ".selectListByProd", paramMap);
		}
	}

	// 施設情報を取得（指定された施設コードの親子施設を取得）
	public List<DpsInsMst> searchFamilyInsList(String insNo, Integer jgiNo, String prodCode) throws DataNotFoundException {
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
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insNo",insNo);
		paramMap.put("jgiNo",jgiNo);
		paramMap.put("prodCode",prodCode);
		paramMap.put("isFamily",true);
		paramMap.put("TLJokenSet", JokenSet.IYAKU_AL);

		return dataAccess.queryForList(getSqlMapName() + ".selectFamilyInsList", paramMap);
	}
	// 施設情報を取得(Ａ調が紐付く場合、Ａ調も取得)：ワクチンのみ使用（特定施設個別計画）
	public List<DpsInsMst> searchIncludeA(String sortString, Integer jgiNo, String insNo) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		paramMap.put("sortString", sortString);
		paramMap.put("jgiNo", jgiNo);
		paramMap.put("insNo", insNo);
		// 固定条件
		paramMap.put("insClass", InsClass.THOUZAI);
		paramMap.put("oldInsrFlg", OldInsrFlg.A_THOUZAI);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectIncludeA", paramMap);
	}
}
