package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.a.bean.MessageKey;
//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.DpsKakuteiErrMsg;
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
import jp.co.takeda.model.InsWsPlanStatus;

/**
 * 施設特約店別計画立案ステータスチェック結果用DTO
 *
 * @author nozaki
 */
public class InsWsStatusCheckResultDto extends StatusCheckResultDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * DBから取得した施設特約店別計画立案ステータスのリスト
	 */
	private final List<InsWsPlanStatus> insWsPlanStatusList;

//mod Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 一括確定エラー情報のリスト
	 */
	private List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList;

	/**
	 * コンストラクタ
	 *
	 * @param errorMessageKeyList エラーメッセージキーのリスト
	 * @param insWsPlanStatusList DBから取得した施設特約店別計画立案ステータスのリスト
	 * @param dpsKakuteiErrMsgList 一括確定エラー情報のリスト
	 */
//	public InsWsStatusCheckResultDto(List<MessageKey> errorMessageKeyList, List<InsWsPlanStatus> insWsPlanStatusList) {
	public InsWsStatusCheckResultDto(List<MessageKey> errorMessageKeyList, List<InsWsPlanStatus> insWsPlanStatusList, List<DpsKakuteiErrMsg> dpsKakuteiErrMsgList) {
//		super(errorMessageKeyList);
		super(errorMessageKeyList);
//		this.insWsPlanStatusList = insWsPlanStatusList;
		this.insWsPlanStatusList = insWsPlanStatusList;
		this.dpsKakuteiErrMsgList = dpsKakuteiErrMsgList;
	}
//mod End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	/**
	 * コンストラクタ
	 *
	 * @param insWsPlanStatusList 施設特約店別計画立案ステータスのリスト
	 */
	public InsWsStatusCheckResultDto(List<InsWsPlanStatus> insWsPlanStatusList) {
		super();
		this.insWsPlanStatusList = insWsPlanStatusList;
	}

	/**
	 * DBから取得した施設特約店別計画立案ステータスのリストを取得。
	 *
	 * @return DBから取得した施設特約店別計画立案ステータスのリスト
	 */
	public List<InsWsPlanStatus> getInsWsPlanStatus() {
		return insWsPlanStatusList;
	}

//add Start 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応
	/**
	 * 一括確定エラー情報のリストを取得。
	 *
	 * @return 一括確定エラー情報のリスト
	 */
	public List<DpsKakuteiErrMsg> getDpsKakuteiErrMsg() {
		return dpsKakuteiErrMsgList;
	}
//add End 2022/7/13 R.takamoto ④No.6 一括確定のエラー表示対応

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
