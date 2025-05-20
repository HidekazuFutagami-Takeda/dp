package jp.co.takeda.logic;

import java.util.List;

import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.div.ActivityType;
import jp.co.takeda.model.div.DpsHoInsType;
import jp.co.takeda.model.div.InsClass;
import jp.co.takeda.model.div.OldInsrFlg;

/**
 * 支援・施設情報の検索結果用DTOを生成するロジッククラス
 *
 * @author khashimoto
 */
public class DpsCreateInsMstScResultDtoLogic {

	/**
	 * 支援・施設情報
	 */
	private final DpsInsMst insMst;

	/**
	 * コンストラクタ
	 *
	 * @param insMst 施設情報
	 */
	public DpsCreateInsMstScResultDtoLogic(DpsInsMst insMst) {
		this.insMst = insMst;
	}

	/**
	 * 検索結果用DTOを生成する
	 *
	 * @return 検索結果用DTO
	 */
	public InsMstResultDto convert() {
		// 施設名
		final String insName = insMst.getInsAbbrName();

		// 対象
		final String insType = DpsHoInsType.getHoInsTypeName(insMst.getHoInsType());

		// 施設分類
		InsClass insClass = insMst.getInsClass();
		OldInsrFlg oldInsFlg = insMst.getOldInsrFlg();
		final String insClazz = InsClass.getInsClassName(insClass, oldInsFlg);

		// 施設コード
		final String insNo = insMst.getInsNo();

		// 従業員番号
		final Integer jgiNo = insMst.getJgiNo();

		// 担当者
		final String jgiName = insMst.getJgiName();

		// 職種名
		final String shokushuName = insMst.getShokushuName();

		// 削除フラグ
		final Boolean delFlg = new DelInsLogic(insMst.getReqFlg(),insMst.getDelFlg()).getDelFlg();

		// 配分除外フラグ
		final Boolean exceptDistFlg = insMst.getExceptDistFlg();

		// JIS府県コード
		final String addrCodePref = insMst.getAddrCodePref().getDbValue();

		// JIS市区町村コード
		final String addrCodeCity = insMst.getAddrCodeCity();

		// 府県名
		final String fukenMeiKj = insMst.getFukenMeiKj();

		// 市区町村名
		final String shikuchosonMeiKj = insMst.getShikuchosonMeiKj();

		// 活動区分
		final ActivityType activityType = insMst.getActivityType();

		// モールフラグ
		final Boolean mallFlg = (new MallLogic().isMall(insMst));

		// 担当者(COM)
		final String comJgiName = insMst.getComJgiName();

		// 従業員番号(COM)
		final Integer comJgiNo = insMst.getComJgiNo();

		// 職種名(COM)
		final String comShokushuName = insMst.getComShokushuName();

		// 担当者(CVM)
		final String cvmJgiName = insMst.getCvmJgiName();

		// 従業員番号(CVM)
		final Integer cvmJgiNo = insMst.getCvmJgiNo();

		// 職種名(CVM)
		final String cvmShokushuName = insMst.getCvmShokushuName();

		// 担当者(RS)
		final String rsJgiName = insMst.getRsJgiName();

		// 従業員番号(RS)
		final Integer rsJgiNo = insMst.getRsJgiNo();

		// 職種名(RS)
		final String rsShokushuName = insMst.getRsShokushuName();

// add Start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
		// 担当者(SPBU)
		final String spJgiName = insMst.getSpJgiName();

		// 従業員番号(SPBU)
		final Integer spJgiNo = insMst.getSpJgiNo();
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		// 職種名(SPBU)
		final String spShokushuName = insMst.getSpShokushuName();
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

		// 担当者(ONC)
		final String oncJgiName = insMst.getOncJgiName();

		// 従業員番号(ONC)
		final Integer oncJgiNo = insMst.getOncJgiNo();

		// 職種名(ONC)
		final String oncShokushuName = insMst.getOncShokushuName();

// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
		// 担当者(固形腫瘍)
		final String kokeiJgiName = insMst.getKokeiJgiName();

		// 従業員番号(固形腫瘍)
		final Integer kokeiJgiNo = insMst.getKokeiJgiNo();

		// 職種名(固形腫瘍)
		final String kokeiShokushuName = insMst.getKokeiShokushuName();
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）

		// 担当者(VAC)
		final String vacJgiName = insMst.getVacJgiName();

		// 従業員番号(VAC)
		final Integer vacJgiNo = insMst.getVacJgiNo();

		// 職種名(VAC)
		final String vacShokushuName = insMst.getVacShokushuName();

// add start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
		// SYU0003の担当者のリスト
		final List<JgiMst> tantoList = insMst.getTantoList();
// add end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）

//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
		// 従業員番号(BU2CSMR)
		final Integer bu2csMrJgiNo = insMst.getBu2csMrJgiNo();

		// 担当者(BU2CSMR)
		final String bu2csMrJgiName = insMst.getBu2csMrJgiName();

		// 従業員番号(JOHLC)
		final Integer johlcMrJgiNo = insMst.getJohlcMrJgiNo();

		// 担当者(JOHLC)
		final String johlcMrJgiName = insMst.getJohlcMrJgiName();

		// 従業員番号(BU2BLO)
		final Integer bu2bloMrJgiNo = insMst.getBu2bloMrJgiNo();

		// 担当者(BU2BLO)
		final String bu2bloMrJgiName = insMst.getBu2bloMrJgiName();

		// 従業員番号(BU2HER)
		final Integer bu2herMrJgiNo = insMst.getBu2herMrJgiNo();

		// 担当者(BU2HER)
		final String bu2herMrJgiName = insMst.getBu2herMrJgiName();

		// 従業員番号(JOGUH)
		final Integer joguhMrJgiNo = insMst.getJoguhMrJgiNo();

		// 担当者(JOGUH)
		final String joguhMrJgiName = insMst.getJoguhMrJgiName();

		// 従業員番号(BU1LAM)
		final Integer bu1lamMrJgiNo = insMst.getBu1lamMrJgiNo();

		// 担当者(BU1LAM)
		final String bu1lamMrJgiName = insMst.getBu1lamMrJgiName();
//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更

				// オブジェクト生成
		return new InsMstResultDto(insName, insType, insClazz, insNo, jgiNo,
				jgiName, shokushuName, delFlg, addrCodePref, addrCodeCity,
				fukenMeiKj, shikuchosonMeiKj, activityType, mallFlg,
				insMst.getProdIns(), comJgiNo, comJgiName, comShokushuName,
				cvmJgiNo, cvmJgiName, cvmShokushuName, rsJgiNo, rsJgiName,
				rsShokushuName, oncJgiNo, oncJgiName, oncShokushuName,
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
// add start 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//				spJgiNo, spJgiName,
// add start 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
				kokeiJgiNo, kokeiJgiName, kokeiShokushuName,
// add end 2020/03/25 趙 2020年4月組織変更対応（各種アプリ）
				spJgiNo, spJgiName, spShokushuName,
// add end 2018/1/18 M.Wada J17-0010_2018年4月組織変更対応（各種アプリ）
//add Start 2025/04/07 H.Futagami 施設検索の担当者表示の変更
				bu2csMrJgiNo, bu2csMrJgiName,
				johlcMrJgiNo, johlcMrJgiName,
				bu2bloMrJgiNo, bu2bloMrJgiName,
				bu2herMrJgiNo, bu2herMrJgiName,
				joguhMrJgiNo, joguhMrJgiName,
				bu1lamMrJgiNo, bu1lamMrJgiName,
//add End 2025/04/07 H.Futagami 施設検索の担当者表示の変更
//				vacJgiNo, vacJgiName, vacShokushuName, exceptDistFlg);
				vacJgiNo, vacJgiName, vacShokushuName, tantoList, exceptDistFlg);
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	}

}
