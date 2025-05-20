package jp.co.takeda.dto;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.MikakutokuSijou;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 未獲得市場の検索結果用DTO
 *
 * @author stakeuchi
 */
public class MikakutokuSijouResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 組織コード
	 */
	private final String sosCd;

	/**
	 * 組織名
	 */
	private final String sosName;

	/**
	 * 従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 従業員名
	 */
	private final String jgiName;

	/**
	 * 職種名
	 */
	private final String shokushuName;

	/**
	 * 対象区分(「UH」or「P」or「UHP」)
	 */
	private final String insType;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 施設名
	 */
	private final String insName;

	/**
	 * 薬効市場全体
	 */
	private final Long yakkouSijouZentai;

	/**
	 * 薬効市場タケダ品
	 */
	private final Long yakkouSijouTakeda;

	/**
	 * 薬効市場未獲得市場
	 */
	private final Long yakkouSijouMikakutoku;

	/**
	 * 薬効市場構成比
	 */
	private final Double yakkouSijouRatio;

	/**
	 * 増減金額
	 */
	private final Long modifyAmount;

	/**
	 * 営業所案 未獲得市場
	 */
	private final Long distPlanMikakutoku;

	/**
	 * 営業所案 構成比
	 */
	private final Double distPlanRatio;

	/**
	 * 組織毎合計行フラグ<br>
	 * TRUE=合計行, FALSE=合計行でない
	 */
	private final Boolean isSosSumRow;

	/**
	 * 施設区分毎合計行フラグ<br>
	 * TRUE=合計行, FALSE=合計行でない
	 */
	private final Boolean isInsTypeSumRow;

	/**
	 * 変換元の未獲得市場
	 */
	private final MikakutokuSijou mikakutokuSijou;

	/**
	 *
	 * コンストラクタ
	 *
	 * @param sosCd 組織コード
	 * @param sosName 組織名
	 * @param jgiNo 従業員番号
	 * @param jgiName 従業員名
	 * @param insType 対象区分(「UH」or「P」or「UHP」)
	 * @param insNo 施設コード
	 * @param insName 施設名
	 * @param yakkouSijouZentai 薬効市場全体
	 * @param yakkouSijouTakeda 薬効市場タケダ品
	 * @param yakkouSijouMikakutoku 薬効市場未獲得市場
	 * @param yakkouSijouRatio 薬効市場構成比
	 * @param modifyAmount 増減金額
	 * @param distPlanMikakutoku 営業所案 未獲得市場
	 * @param distPlanRatio 営業所案 構成比
	 * @param isSosSumRow 組織毎合計行フラグ
	 * @param isInsTypeSumRow 施設区分毎合計行フラグ
	 * @param mikakutokuSijou 変換元の未獲得市場
	 * @param shokushuName 職種名
	 */
	public MikakutokuSijouResultDto(String sosCd, String sosName, Integer jgiNo, String jgiName, String insType, String insNo, String insName, Long yakkouSijouZentai,
		Long yakkouSijouTakeda, Long yakkouSijouMikakutoku, Double yakkouSijouRatio, Long modifyAmount, Long distPlanMikakutoku, Double distPlanRatio, Boolean isSosSumRow,
		Boolean isInsTypeSumRow, MikakutokuSijou mikakutokuSijou, String shokushuName) {
		this.sosCd = sosCd;
		this.sosName = sosName;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.insType = insType;
		this.insNo = insNo;
		this.insName = insName;
		this.yakkouSijouZentai = yakkouSijouZentai;
		this.yakkouSijouTakeda = yakkouSijouTakeda;
		this.yakkouSijouMikakutoku = yakkouSijouMikakutoku;
		this.yakkouSijouRatio = yakkouSijouRatio;
		this.modifyAmount = modifyAmount;
		this.distPlanMikakutoku = distPlanMikakutoku;
		this.distPlanRatio = distPlanRatio;
		this.isSosSumRow = isSosSumRow;
		this.isInsTypeSumRow = isInsTypeSumRow;
		this.mikakutokuSijou = mikakutokuSijou;
		this.shokushuName = shokushuName;
	}

	/**
	 * 組織コードを取得する。
	 *
	 * @return sosCd 組織コード
	 */
	public String getSosCd() {
		return sosCd;
	}

	/**
	 * 組織名を取得する。
	 *
	 * @return sosName 組織名
	 */
	public String getSosName() {
		return sosName;
	}

	/**
	 * 従業員番号を取得する。
	 *
	 * @return jgiNo 従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 従業員名を取得する。
	 *
	 * @return jgiName 従業員名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 対象区分
	 *
	 * @return insType 対象区分
	 */
	public String getInsType() {
		return insType;
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
	 * 薬効市場全体を取得する。
	 *
	 * @return yakkouSijouZentai 薬効市場全体
	 */
	public Long getYakkouSijouZentai() {
		return yakkouSijouZentai;
	}

	/**
	 * 薬効市場タケダ品を取得する。
	 *
	 * @return yakkouSijouTakeda 薬効市場タケダ品
	 */
	public Long getYakkouSijouTakeda() {
		return yakkouSijouTakeda;
	}

	/**
	 * 薬効市場未獲得市場を取得する。
	 *
	 * @return yakkouSijouMikakutoku 薬効市場未獲得市場
	 */
	public Long getYakkouSijouMikakutoku() {
		return yakkouSijouMikakutoku;
	}

	/**
	 * 薬効市場構成比を取得する。
	 *
	 * @return yakkouSijouRatio 薬効市場構成比
	 */
	public Double getYakkouSijouRatio() {
		return yakkouSijouRatio;
	}

	/**
	 * 増減金額を取得する。
	 *
	 * @return modifyAmount 増減金額
	 */
	public Long getModifyAmount() {
		return modifyAmount;
	}

	/**
	 * 営業所案 未獲得市場を取得する。
	 *
	 * @return distPlanMikakutoku 営業所案 未獲得市場
	 */
	public Long getDistPlanMikakutoku() {
		return distPlanMikakutoku;
	}

	/**
	 * 営業所案 構成比を取得する。
	 *
	 * @return distPlanRatio 営業所案 構成比
	 */
	public Double getDistPlanRatio() {
		return distPlanRatio;
	}

	/**
	 * 組織毎合計行フラグを取得する。
	 *
	 * @return isSosSumRow 組織毎合計行フラグ
	 */
	public Boolean getIsSosSumRow() {
		return isSosSumRow;
	}

	/**
	 * 施設区分毎合計行フラグを取得する。
	 *
	 * @return isInsTypeSumRow 施設区分毎合計行フラグ
	 */
	public Boolean getIsInsTypeSumRow() {
		return isInsTypeSumRow;
	}

	/**
	 * 変換元の未獲得市場を取得する。
	 *
	 * @return mikakutokuSijou 変換元の未獲得市場
	 */
	public MikakutokuSijou getMikakutokuSijou() {
		return mikakutokuSijou;
	}

	/**
	 * 職種名を取得する
	 * @return
	 */
	public String getShokushuName() {
		return shokushuName;
	}
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
