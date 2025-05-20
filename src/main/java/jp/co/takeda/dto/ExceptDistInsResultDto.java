package jp.co.takeda.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ExceptProd;
import jp.co.takeda.model.div.RegistType;

/**
 * 配分除外施設の検索結果用DTO
 *
 * @author siwamoto
 */
public class ExceptDistInsResultDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

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

	/**
	 * 配分除外施設
	 */
	private final String exceptDistIns;

	/**
	 * 担当者・従業員番号
	 */
	private final Integer jgiNo;

	/**
	 * 担当者名
	 */
	private final String jgiName;

	/**
	 * 職種名
	 */
	private final String dispJgiName;

	/**
	 * 最終更新者名
	 */
	private final String upJgiName;

	/**
	 * 配分除外品目
	 */
	private final List<ExceptProd> exceptDistInsProd;

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * 表示用配分除外品目名
	 */
	private final String dispExceptDistInsProd;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * 表示用試算除外品目名
	 */
	private final String dispExceptEstiInsProd;

	/**
	 * 最終更新日時
	 */
	private final Date upDate;

	/**
	 * 施設コード
	 */
	private final String insNo;

	/**
	 * 登録区分
	 */
	private final RegistType registType;

	/**
	 * 試算除外フラグ
	 */
	private Boolean estimationFlg;

	/**
	 * 配分除外フラグ
	 */
	private Boolean exceptFlg;

	/**
	 * コンストラクタ
	 *
	 * @param insName 施設名
	 * @param insType 対象
	 * @param insClass 施設分類
	 * @param exceptDistIns 配分除外施設
	 * @param jgiNo 担当者・従業員番号
	 * @param jgiName 担当者名
	 * @param upJgiName 最終更新者名
	 * @param exceptDistInsProd 配分除外品目
	 * @param upDate 最終更新日時
	 * @param insNo 施設コード
	 * @param jgiNo 従業員番号
	 * @param registType 登録区分
	 * @param shokushuName 職種名
	 */
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//	public ExceptDistInsResultDto(String insName, String insType, String insClass, String exceptDistIns, Integer jgiNo, String jgiName, String upJgiName,
//		List<ExceptProd> exceptDistInsProd, Date upDate, String insNo, RegistType registType, String dispJgiName) {
	public ExceptDistInsResultDto(String insName, String insType, String insClass, String exceptDistIns, Integer jgiNo, String jgiName, String upJgiName,
			List<ExceptProd> exceptDistInsProd, String dispExceptDistInsProd, String dispExceptEstiInsProd,Date upDate, String insNo, RegistType registType, String dispJgiName,
			Boolean estimationFlg, Boolean exceptFlg) {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		super();
		this.insName = insName;
		this.insType = insType;
		this.insClass = insClass;
		this.exceptDistIns = exceptDistIns;
		this.jgiNo = jgiNo;
		this.jgiName = jgiName;
		this.upJgiName = upJgiName;
		this.exceptDistInsProd = exceptDistInsProd;
// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.dispExceptDistInsProd = dispExceptDistInsProd;
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		this.dispExceptEstiInsProd = dispExceptEstiInsProd;
		this.upDate = upDate;
		this.insNo = insNo;
		this.registType = registType;
		this.dispJgiName = dispJgiName;
		this.estimationFlg = estimationFlg;
		this.exceptFlg = exceptFlg;
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

	/**
	 * 配分除外施設を取得する。
	 *
	 * @return 配分除外施設
	 */
	public String getExceptDistIns() {
		return exceptDistIns;
	}

	/**
	 * 担当者・従業員番号を取得する。
	 *
	 * @return 担当者・従業員番号
	 */
	public Integer getJgiNo() {
		return jgiNo;
	}

	/**
	 * 担当者名を取得する。
	 *
	 * @return jgiName 担当者名
	 */
	public String getJgiName() {
		return jgiName;
	}

	/**
	 * 表示用担当者名を取得する
	 * @return
	 */
	public String getDispJgiName() {
		return dispJgiName;
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
	 * 配分除外品目を取得する。
	 *
	 * @return 配分除外品目
	 */
	public List<ExceptProd> getExceptDistInsProd() {
		return exceptDistInsProd;
	}

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	/**
	 * 表示用配分除外品目名を取得する。
	 *
	 * @return 表示用配分除外品目名
	 */
	public String getDispExceptDistInsProd() {
		return dispExceptDistInsProd;
	}
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

	/**
	 * 表示用試算除外品目名を取得する。
	 *
	 * @return 表示用試算除外品目名
	 */
	public String getDispExceptEstiInsProd() {
		return dispExceptEstiInsProd;
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
	 * 登録区分を取得する。
	 *
	 * @return String 登録区分
	 */
	public String getRegistType() {
		return registType.getDbValue();
	}

	/**
	 * 試算除外フラグを取得する。
	 *
	 * @return 試算除外フラグ
	 */
	public Boolean getEstimationFlg() {
		return estimationFlg;
	}

	/**
	 * 配分除外フラグを取得する。
	 *
	 * @return 配分除外フラグ
	 */
	public Boolean getExceptFlg() {
		return exceptFlg;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
