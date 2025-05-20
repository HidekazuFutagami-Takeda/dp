package jp.co.takeda.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.ExceptDistIns;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.div.InsClass;


/**
 * 配分除外施設の編集用DTO
 *
 * @author khashimoto
 */
public class ExceptDistInsUpdateInitDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード（営業所）
	 */
	private final String sosCd;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設名
	 */
	private final String insName;

	/**
	 * 対象
	 */
	private final String insType;

	/**
	 * 施設分類
	 */
	private final String insClass;

//	/**
//	 * 従業員番号
//	 */
//	private final Integer jgiNo;
//
//	/**
//	 * 担当者名
//	 */
//	private final String jgiName;

	/**
	 * 品目固定コードのリスト
	 */
	private final List<String> prodCodeList;

	/**
	 * 最終更新者名
	 */
	private final String upJgiName;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 職種名
	 */
	private final String dispJgiName;

	/**
	 * 計画対象品目の（MMP・仕入一般）の検索結果DTO
	 */
	private final List<ExceptDistInsPlannedProdResultDto> prodList;

	/**
	 * 施設にて選択された除外フラグ
	 * exclusionFlg[0] 試算除外フラグ
	 * exclusionFlg[1] 配分除外フラグ
	 * <pre>
	 * 0 選択なし
	 * 1 選択あり
	 * </pre>
	 */
	private final String[] exclusionFlg;

	/**
	 * コンストラクタ
	 *
	 * @param sosCd 組織コード(営業所）
	 * @param insMst 施設情報
	 * @param jgiMst 従業員情報
	 * @param distIns 配分除外施設
	 * @param prodList 配分除外施設品目リスト
	 */
	public ExceptDistInsUpdateInitDto(String sosCd, DpsInsMst insMst, ExceptDistIns distIns, List<ExceptDistInsPlannedProdResultDto> prodList, String dispJgiName, String insType) {
		this.sosCd = sosCd;
		this.insNo = distIns.getInsNo();
		this.insName = insMst.getInsAbbrName();
		//this.insType = insMst.getHoInsType().name();
		this.insType = insType;
		this.insClass = InsClass.getInsClassName(insMst.getInsClass(), insMst.getOldInsrFlg());
//		this.jgiNo = jgiMst.getJgiNo();
//		this.jgiName = jgiMst.getJgiName();
		//試算・配分除外品目の登録の場合、施設の指定区分を未チェックにする
		this.exclusionFlg = new String[] { "0", "0" };
		if (distIns.getExceptProd() != null) {
			List<String> tmpList = new ArrayList<String>();
			for (ExceptProd exceptProd : distIns.getExceptProd()) {
				tmpList.add(exceptProd.getProdCode());

				if (exceptProd.getProdCode() == null) {
					//品目コードがnullの場合、試算・配分除外施設の登録なので、施設の指定区分をセットする
					this.exclusionFlg[0] = exceptProd.getStrEstimationFlg();
					this.exclusionFlg[1] = exceptProd.getStrExceptFlg();

				}
			}
			this.prodCodeList = tmpList;

			// 現在設定されている品目取得
			List<String> tmpWork = new ArrayList<String>();
			for (ExceptDistInsPlannedProdResultDto targetProdList : prodList) {
				if(tmpList.contains(targetProdList.getProdCode())) tmpWork.add(targetProdList.getProdCode());
			}
		} else {
			this.prodCodeList = null;
		}
		this.upJgiName = distIns.getUpJgiName();
		this.upDate = distIns.getUpDate();
		this.prodList = prodList;
		this.dispJgiName = dispJgiName;
	}

	/**
	 * 組織コード（営業所）を取得する。
	 *
	 * @return sosCd 組織コード（営業所）
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 施設コードを取得する。
	 *
	 * @return insNo 施設コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 施設名を取得する。
	 *
	 * @return insName 施設名
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 対象を取得する。
	 *
	 * @return insType 対象
	 */
	public String getInsType() {
		return insType;
	}

	/**
	 * 施設分類を取得する。
	 *
	 * @return insClass 施設分類
	 */
	public String getInsClass() {
		return insClass;
	}

//	/**
//	 * 従業員番号を取得する。
//	 *
//	 * @return 従業員番号
//	 */
//	public Integer getJgiNo() {
//		return jgiNo;
//	}
//
//	/**
//	 * 担当者を取得する。
//	 *
//	 * @return jgiName 担当者
//	 */
//	public String getJgiName() {
//		return jgiName;
//	}

	/**
	 * 品目コードのリストを取得する。
	 *
	 * @return prodCodeList 品目コードのリスト
	 */
	public List<String> getProdCodeList() {
		return prodCodeList;
	}

	/**
	 * 最終更新者名を取得する。
	 *
	 * @return upJgiName 最終更新者名
	 */
	public String getUpJgiName() {
		return upJgiName;
	}

	/**
	 * 最終更新日時を取得する。
	 *
	 * @return upDate 最終更新日時
	 */
	public Date getUpDate() {
		return upDate;
	}

	/**
	 * 表示用担当者名を取得する
	 * @return
	 */
	public String getDispJgiName() {
		return dispJgiName;
	}

	/**
	 * 計画対象品目の（MMP・仕入一般）の検索結果DTOを取得する。
	 *
	 * @return prodList 計画対象品目の（MMP・仕入一般）の検索結果DTO
	 */
	public List<ExceptDistInsPlannedProdResultDto> getProdList() {
		return prodList;
	}

	/**
	 * 施設にて選択された除外フラグを取得する。
	 *
	 * @return exclusionFlg 除外フラグ
	 */
	public String[] getExclusionFlg() {
		return exclusionFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
