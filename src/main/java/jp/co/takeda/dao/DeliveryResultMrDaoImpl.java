package jp.co.takeda.dao;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dto.DeliveryResultSosSummaryDto;
import jp.co.takeda.model.DeliveryResultMr;
import jp.co.takeda.model.MonNnu;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.model.div.JgiKb;
import jp.co.takeda.model.div.JokenSet;
import jp.co.takeda.model.div.Sales;
import jp.co.takeda.service.DpsCodeMasterSearchService;

/**
 * 担当者別納入実績にアクセスするDAO実装クラス
 *
 * @author khashimoto
 */
@Repository("deliveryResultMrDao")
public class DeliveryResultMrDaoImpl extends AbstractDao implements DeliveryResultMrDao {

	private static final String SQL_MAP = "DPS_I_DELIVERY_RESULT_MR_SqlMap";

	@Override
	protected String getSqlMapName() {
		return SQL_MAP;
	}

	/**
	 * カテゴリ 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsCodeMasterSearchService")
	protected DpsCodeMasterSearchService dpsCodeMasterSearchService;

	// 従業員番号、品目固定コード、施設出力対象区分を指定して納入実績を取得
	public DeliveryResultMr search(Integer jgiNo, String prodCode, InsType insType) throws DataNotFoundException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insType == null) {
			final String errMsg = "施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		DeliveryResultMr record = new DeliveryResultMr();
		record.setJgiNo(jgiNo);
		record.setProdCode(prodCode);
		record.setInsType(insType);
		return (DeliveryResultMr) super.selectByUniqueKey(record);
	}

	// 対象品目・営業所の納入実績を取得(雑担当を含む)
	public List<DeliveryResultMr> searchByProd(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException {

		// ----------------------
		// 検索実行
		// ----------------------
		return searchByProd(sortString, prodCode, sosCd3, sosCd4, insType, true, false, null, category);
	}

	// 対象品目・営業所の納入実績を取得する。(雑担当を含まない)
	public List<DeliveryResultMr> searchByProdNonZatu(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException {

		// ----------------------
		// 検索実行
		// ----------------------
		return searchByProd(sortString, prodCode, sosCd3, sosCd4, insType, false, false, null, category);
	}

	// 帳票用対象品目・営業所の納入実績を取得(雑担当を含む)
	public List<DeliveryResultMr> searchByProdReport(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, Sales sales, String category) throws DataNotFoundException {

		// ----------------------
		// 検索実行
		// ----------------------
		return searchByProd(sortString, prodCode, sosCd3, sosCd4, insType, true, true, sales, category);
	}
	/**
	 *
	 * 対象品目・営業所の納入実績を取得する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)(NULL可)
	 * @param insType 施設出力対象区分(NULL可:UHP合計)
	 * @param zatuFlg 雑担当フラグ(雑担当を含む場合、true)
	 * @param reportFlg 帳票用フラグ(帳票用の場合、true)
	 * @param sales 売上所属(NULL可)
	 * @param category カテゴリ(NULL可)
	 * @return 納入実績
	 * @throws DataNotFoundException
	 */
	private List<DeliveryResultMr> searchByProd(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, boolean zatuFlg, boolean reportFlg, Sales sales, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd3 == null && sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createParam(sortString, prodCode, sosCd3, sosCd4, insType, zatuFlg, category);
		if(reportFlg){
			paramMap.put("report", "TRUE");
			paramMap.put("sales", sales);
			paramMap.put("category", category);
			// 固定条件
			paramMap.put("planTargetFlg", true);
		}

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectByProd", paramMap);
	}

	/**
	 * 検索条件を生成する。
	 *
	 * @param sortString ソート条件
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param category カテゴリ
	 * @param zatuFlg 雑担当フラグ(雑担当を含む場合、true)
	 * @return 検索条件
	 */
	private Map<String, Object> createParam(String sortString, String prodCode, String sosCd3, String sosCd4, InsType insType, boolean zatuFlg, String category) {

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sortString", sortString);
		paramMap.put("prodCode", prodCode);
		paramMap.put("sosCd3", sosCd3);
		paramMap.put("sosCd4", sosCd4);
		// 施設出力対象区分
		List<InsType> insTypeList = new ArrayList<InsType>();
		if (insType != null) {
			//対象区分ごとの合計をもとめる(selectSummary)
			if (insType == InsType.P) {
				//対象区分がPの場合
				if (dpsCodeMasterSearchService.isVaccine(category)) {
					//ワクチンの場合、雑Pを含める
					insTypeList.add(InsType.P);
					insTypeList.add(InsType.ZATU);

					paramMap.put("insTypeList", insTypeList);
				}else {

					paramMap.put("insType", insType);
				}
			}
			else {
				paramMap.put("insType", insType);
			}
		} else {
			//対象区分がない時は、対象区分すべて
			if (dpsCodeMasterSearchService.isVaccine(category)) {
				//ワクチンの場合、雑Pを含める
				insTypeList.add(InsType.UH);
				insTypeList.add(InsType.P);
				insTypeList.add(InsType.ZATU);
			}else {
				insTypeList.add(InsType.UH);
				insTypeList.add(InsType.P);
			}
			paramMap.put("insTypeList", insTypeList);
		}

		paramMap.put("insTypeList", insTypeList);
		// 従業員区分
		List<JgiKb> jgiKbList = new ArrayList<JgiKb>();
		jgiKbList.add(JgiKb.JGI);
		jgiKbList.add(JgiKb.CONTRACT_MR);
		jgiKbList.add(JgiKb.DUMMY_MR);
		if (zatuFlg) {
			jgiKbList.add(JgiKb.EIGYOSHO_ZATU);
		}
		paramMap.put("jgiKbList", jgiKbList);
		// 固定条件
		paramMap.put("jokenSetList", new JokenSet[]{ JokenSet.IYAKU_MR });
		//add Start 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
		paramMap.put("category", category);
		//add End 2022/7/13 H.Futagami 2022年4月組織変更対応 検索条件にカテゴリ追加（結合条件：職種コード）
		return paramMap;
	}

	// 対象品目・営業所の納入実績サマリーを取得
	public DeliveryResultSosSummaryDto searchSosSummary(String prodCode, String sosCd3, boolean zatuFlg, InsType insType, String category) throws DataNotFoundException {
		return this.searchSummary(prodCode, sosCd3, null, insType, zatuFlg, category);
	}

	// 対象品目・チームの納入実績サマリーを取得
	public DeliveryResultSosSummaryDto searchTeamSummary(String prodCode, String sosCd4, InsType insType, String category) throws DataNotFoundException {
		return this.searchSummary(prodCode, null, sosCd4, insType, false, category);
	}

	/**
	 * 対象品目・組織の納入実績サマリーを取得する。<br>
	 * 施設出力対象区分がNULL指定の場合、合計を返す。
	 *
	 * @param prodCode 品目固定コード
	 * @param sosCd3 組織コード(営業所)
	 * @param sosCd4 組織コード(チーム)
	 * @param insType 施設出力対象区分(NULL可)
	 * @param zatuFlg 雑担当を含む場合、trueを指定
	 * @return 納入実績サマリー
	 * @throws DataNotFoundException
	 */
	private DeliveryResultSosSummaryDto searchSummary(String prodCode, String sosCd3, String sosCd4, InsType insType, boolean zatuFlg, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (prodCode == null) {
			final String errMsg = "品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (sosCd3 == null && sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createParam(null, prodCode, sosCd3, sosCd4, insType, zatuFlg, category);

		// ----------------------
		// 検索実行
		// ----------------------
		Map<String, Object> resultMap = dataAccess.queryForObject(getSqlMapName() + ".selectSummary", paramMap);
		String sosCd = (String) ObjectUtils.defaultIfNull(sosCd3, sosCd4);
		MonNnu _monNnu = (MonNnu) resultMap.get("monNnu");
		return new DeliveryResultSosSummaryDto(sosCd, prodCode, insType, _monNnu);
	}

	// 対象組織の納入実績を取得(雑担当を含まない)
	public List<DeliveryResultMr> searchBySosNonZatu(String sortString, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createParam(sortString, null, sosCd3, sosCd4, insType, false, category);
		paramMap.put("category", category);

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySos", paramMap);
	}

	// 帳票用対象組織の納入実績を取得(雑担当を含まない)
	public List<DeliveryResultMr> searchBySosNonZatuReport(String sortString, String sosCd3, String sosCd4, InsType insType, String category) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null && sosCd4 == null) {
			final String errMsg = "組織コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (category == null) {
			final String errMsg = "カテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 検索条件生成
		// ----------------------
		Map<String, Object> paramMap = createParam(sortString, null, sosCd3, sosCd4, insType, false, category);
		paramMap.put("category", category);
		paramMap.put("report", "TRUE");

		// ----------------------
		// 検索実行
		// ----------------------
		return dataAccess.queryForList(getSqlMapName() + ".selectBySos", paramMap);
	}
}
