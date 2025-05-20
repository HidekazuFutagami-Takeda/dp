package jp.co.takeda.model;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpManageModel;
import jp.co.takeda.model.div.DistBasisType;
import jp.co.takeda.model.div.Sales;

/**
 * 管理時の計画対象品目を表すモデルクラス
 *
 * @author khashimoto
 */
public class ManagePlannedProd extends DpManageModel<ManagePlannedProd> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------
	// Data Field
	// --------------------
	/**
	 * 品目固定コード
	 */
	private String prodCode;

	/**
	 * グループコード
	 */
	private String groupCode;

	/**
	 * 統計品名コード(品目)
	 */
	private String statProdCode;

	/**
	 * カテゴリ
	 */
	private String category;

	/**
	 * 売上所属
	 */
	private Sales sales;

	/**
	 * 製品区分
	 */
	private String prodType;

	/**
	 * 品目名称
	 */
	private String prodName;

	/**
	 * 品目略称
	 */
	private String prodAbbrName;

	/**
	 * 薬効市場コード
	 */
	private String yakkouSijouCode;

	/**
	 * 施設特約店別計画配分元
	 */
	private DistBasisType distBasisType;

	/**
	 * 計画立案レベル・全社
	 */
	private Boolean planLevelZensha;

	/**
	 * 計画立案レベル・支店
	 */
	private Boolean planLevelSiten;

	/**
	 * 計画立案レベル・営業所
	 */
	private Boolean planLevelOffice;

	/**
	 * 計画立案レベル・チーム
	 */
	private Boolean planLevelTeam;

	/**
	 * 計画立案レベル・担当者
	 */
	private Boolean planLevelMr;

	/**
	 * 計画立案レベル・施設
	 */
	private Boolean planLevelIns;

	/**
	 * 計画立案レベル・施設特約店
	 */
	private Boolean planLevelInsWs;

	/**
	 * 計画立案レベル・特約店
	 */
	private Boolean planLevelWs;

	/**
	 * 仮品目かを示すフラグ
	 */
	private Boolean tempProdFlg;

	/**
	 * 計画対象かを示すフラグ
	 */
	private Boolean planTargetFlg;

	/**
	 * 他システムＩＦ許可フラグ
	 */
	private Boolean othSysIfFlg;

	/**
	 * 表示順
	 */
	private Long dataSeq;

	//---------------------
	// Getter & Setter
	// --------------------

	/**
	 * 品目固定コードを取得する。
	 *
	 * @return 品目固定コード
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * 品目固定コードを設定する。
	 *
	 * @param prodCode 品目固定コード
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * グループコードを取得する。
	 *
	 * @return グループコード
	 */
	public String getGroupCode() {
		return groupCode;
	}

	/**
	 * グループコードを設定する。
	 *
	 * @param groupCode グループコード
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**
	 * 統計品名コード(品目)を取得する。
	 *
	 * @return 統計品名コード(品目)
	 */
	public String getStatProdCode() {
		return statProdCode;
	}

	/**
	 * 統計品名コード(品目)を設定する。
	 *
	 * @param statProdCode 統計品名コード(品目)
	 */
	public void setStatProdCode(String statProdCode) {
		this.statProdCode = statProdCode;
	}

	/**
	 * カテゴリを取得する。
	 *
	 * @return カテゴリ
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * カテゴリを設定する。
	 *
	 * @param category カテゴリ
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 売上所属を取得する。
	 *
	 * @return 売上所属
	 */
	public Sales getSales() {
		return sales;
	}

	/**
	 * 売上所属を設定する。
	 *
	 * @param sales 売上所属
	 */
	public void setSales(Sales sales) {
		this.sales = sales;
	}

	/**
	 * 製品区分を取得する。
	 *
	 * @return 製品区分
	 */
	public String getProdType() {
		return prodType;
	}

	/**
	 * 製品区分を設定する。
	 *
	 * @param prodType 製品区分
	 */
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	/**
	 * 品目名称(漢字)を取得する。
	 *
	 * @return 品目名称(漢字)
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * 品目名称(漢字)を設定する。
	 *
	 * @param prodName 品目名称(漢字)
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * 品目略称を取得する。
	 *
	 * @return 品目略称
	 */
	public String getProdAbbrName() {
		return prodAbbrName;
	}

	/**
	 * 品目略称を設定する。
	 *
	 * @param prodAbbrName 品目略称
	 */
	public void setProdAbbrName(String prodAbbrName) {
		this.prodAbbrName = prodAbbrName;
	}

	/**
	 * 薬効市場コードを取得する。
	 *
	 * @return 薬効市場コード
	 */
	public String getYakkouSijouCode() {
		return yakkouSijouCode;
	}

	/**
	 * 薬効市場コードを設定する。
	 *
	 * @param yakkouSijouCode 薬効市場コード
	 */
	public void setYakkouSijouCode(String yakkouSijouCode) {
		this.yakkouSijouCode = yakkouSijouCode;
	}

	/**
	 * 施設特約店別計画配分元を取得する。
	 *
	 * @return 施設特約店別計画配分元
	 */
	public DistBasisType getDistBasisType() {
		return distBasisType;
	}

	/**
	 * 施設特約店別計画配分元を設定する。
	 *
	 * @param distBasisType 施設特約店別計画配分元
	 */
	public void setDistBasisType(DistBasisType distBasisType) {
		this.distBasisType = distBasisType;
	}

	/**
	 * 計画立案レベル・全社を取得する。
	 *
	 * @return 計画立案レベル・全社
	 */
	public Boolean getPlanLevelZensha() {
		return planLevelZensha;
	}

	/**
	 * 計画立案レベル・全社を設定する。
	 *
	 * @param planLevelZensha 計画立案レベル・全社
	 */
	public void setPlanLevelZensha(Boolean planLevelZensha) {
		this.planLevelZensha = planLevelZensha;
	}

	/**
	 * 計画立案レベル・支店を取得する。
	 *
	 * @return 計画立案レベル・支店
	 */
	public Boolean getPlanLevelSiten() {
		return planLevelSiten;
	}

	/**
	 * 計画立案レベル・支店を設定する。
	 *
	 * @param planLevelSiten 計画立案レベル・支店
	 */
	public void setPlanLevelSiten(Boolean planLevelSiten) {
		this.planLevelSiten = planLevelSiten;
	}

	/**
	 * 計画立案レベル・営業所を取得する。
	 *
	 * @return 計画立案レベル・営業所
	 */
	public Boolean getPlanLevelOffice() {
		return planLevelOffice;
	}

	/**
	 * 計画立案レベル・営業所を設定する。
	 *
	 * @param planLevelOffice 計画立案レベル・営業所
	 */
	public void setPlanLevelOffice(Boolean planLevelOffice) {
		this.planLevelOffice = planLevelOffice;
	}

	/**
	 * 計画立案レベル・チームを取得する。
	 *
	 * @return 計画立案レベル・チーム
	 */
	public Boolean getPlanLevelTeam() {
		return planLevelTeam;
	}

	/**
	 * 計画立案レベル・チームを設定する。
	 *
	 * @param planLevelTeam 計画立案レベル・チーム
	 */
	public void setPlanLevelTeam(Boolean planLevelTeam) {
		this.planLevelTeam = planLevelTeam;
	}

	/**
	 * 計画立案レベル・担当者を取得する。
	 *
	 * @return 計画立案レベル・担当者
	 */
	public Boolean getPlanLevelMr() {
		return planLevelMr;
	}

	/**
	 * 計画立案レベル・担当者を設定する。
	 *
	 * @param planLevelMr 計画立案レベル・担当者
	 */
	public void setPlanLevelMr(Boolean planLevelMr) {
		this.planLevelMr = planLevelMr;
	}

	/**
	 * 計画立案レベル・施設を取得する。
	 *
	 * @return 計画立案レベル・施設
	 */
	public Boolean getPlanLevelIns() {
		return planLevelIns;
	}

	/**
	 * 計画立案レベル・施設を設定する。
	 *
	 * @param planLevelIns 計画立案レベル・施設
	 */
	public void setPlanLevelIns(Boolean planLevelIns) {
		this.planLevelIns = planLevelIns;
	}

	/**
	 * 計画立案レベル・施設特約店を取得する。
	 *
	 * @return 計画立案レベル・施設特約店
	 */
	public Boolean getPlanLevelInsWs() {
		return planLevelInsWs;
	}

	/**
	 * 計画立案レベル・施設特約店を設定する。
	 *
	 * @param planLevelInsWs 計画立案レベル・施設特約店
	 */
	public void setPlanLevelInsWs(Boolean planLevelInsWs) {
		this.planLevelInsWs = planLevelInsWs;
	}

	/**
	 * 計画立案レベル・特約店を取得する。
	 *
	 * @return 計画立案レベル・特約店
	 */
	public Boolean getPlanLevelWs() {
		return planLevelWs;
	}

	/**
	 * 計画立案レベル・特約店を設定する。
	 *
	 * @param planLevelWs 計画立案レベル・特約店
	 */
	public void setPlanLevelWs(Boolean planLevelWs) {
		this.planLevelWs = planLevelWs;
	}

	/**
	 * 仮品目かを示すフラグを取得する。
	 *
	 * @return 仮品目かを示すフラグ
	 */
	public Boolean getTempProdFlg() {
		return tempProdFlg;
	}

	/**
	 * 仮品目かを示すフラグを設定する。
	 *
	 * @param tempProdFlg 仮品目かを示すフラグ
	 */
	public void setTempProdFlg(Boolean tempProdFlg) {
		this.tempProdFlg = tempProdFlg;
	}

	/**
	 * 計画対象かを示すフラグを取得する。
	 *
	 * @return 計画対象かを示すフラグ
	 */
	public Boolean getPlanTargetFlg() {
		return planTargetFlg;
	}

	/**
	 * 計画対象かを示すフラグを設定する。
	 *
	 * @param planTargetFlg 計画対象かを示すフラグ
	 */
	public void setPlanTargetFlg(Boolean planTargetFlg) {
		this.planTargetFlg = planTargetFlg;
	}

	/**
	 * 他システムＩＦ許可フラグを取得する。
	 *
	 * @return 他システムＩＦ許可フラグ
	 */
	public Boolean getOthSysIfFlg() {
		return othSysIfFlg;
	}

	/**
	 * 他システムＩＦ許可フラグを設定する。
	 *
	 * @param othSysIfFlg 他システムＩＦ許可フラグ
	 */
	public void setOthSysIfFlg(Boolean othSysIfFlg) {
		this.othSysIfFlg = othSysIfFlg;
	}

	/**
	 * 表示順を取得する。
	 *
	 * @return 表示順
	 */
	public Long getDataSeq() {
		return dataSeq;
	}

	/**
	 * 表示順を設定する。
	 *
	 * @param dataSeq 表示順
	 */
	public void setDataSeq(Long dataSeq) {
		this.dataSeq = dataSeq;
	}

	@Override
	public int compareTo(ManagePlannedProd obj) {
		if (obj != null) {
			return new CompareToBuilder().append(this.prodCode, obj.prodCode).toComparison();
		}
		return -1;
	}

	@Override
	public boolean equals(Object entry) {
		if (entry != null && ManagePlannedProd.class.isAssignableFrom(entry.getClass())) {
			ManagePlannedProd obj = (ManagePlannedProd) entry;
			return new EqualsBuilder().append(this.prodCode, obj.prodCode).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.prodCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
