package jp.co.takeda.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.takeda.dto.MikakutokuSijouResultDto;
import jp.co.takeda.model.MikakutokuSijou;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.util.ConvertUtil;
import jp.co.takeda.util.MathUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 未獲得市場の検索結果用DTOを生成するロジッククラス
 *
 * <pre>
 * DAOの検索結果からはUHとPの集計行のみが取得される。
 * そのためUHP行・各合計行の生成をこのロジックにて行う。
 * また各合計行の構成(%)の値も同時に算出する。
 *
 * 以下がこのクラスの役割
 * ・UHP行の生成
 * ・各合計行の生成
 * ・構成(%)の算出
 * ・検索結果用DTOを生成し呼び出し元に返す。
 *
 * このロジッククラスは呼び出し元に依存せず、引数の値がNullだったらサイズ0のオブジェクトを返す。
 * サイズ0のオブジェクトリストを例外とする場合は、呼び出し元で例外処理を行うものとする。
 * </pre>
 *
 * @author stakeuchi
 */
public class MikakutokuSijouResultDtoCreateLogic {

	/**
	 * DAO検索結果の未獲得市場リスト
	 */
	private final List<MikakutokuSijou> searchResultList;

	/**
	 * 構成(%)算出用<br>
	 * 薬効市場未獲得市場値 集計MAP<br>
	 * (Key=対象区分コード Value=合計金額)
	 */
	private final Map<String, Long> yakkouSijouMikakutokuSumMap;

	/**
	 * 構成(%)算出用<br>
	 * 営業所案未獲得市場値 集計MAP<br>
	 * (Key=対象区分コード Value=合計金額)
	 */
	private final Map<String, Long> distPlanMikakutokuSumMap;

	/**
	 * 施設対象区分「UHP」を表す定数
	 */
	private static final String INS_TYPE_UHP = InsType.UH.name() + InsType.P.name();

	/**
	 * コンストラクタ
	 *
	 * @param searchResultList DAO検索結果の未獲得市場リスト
	 */
	public MikakutokuSijouResultDtoCreateLogic(List<MikakutokuSijou> searchResultList) {
		this.searchResultList = searchResultList;
		// 構成(%)算出用の集計MAPを生成
		this.yakkouSijouMikakutokuSumMap = createYakkouSijouMikakutokuSumMap();
		this.distPlanMikakutokuSumMap = createDistPlanMikakutokuSumMap();
	}

	/**
	 * 検索結果用DTOリストの生成<br>
	 * DAO検索結果の未獲得市場リストから検索結果用DTOを生成する。
	 */
	public List<MikakutokuSijouResultDto> createResultDtoList() {
		// 検索結果行の生成
		List<MikakutokuSijouResultDto> searchResultList = createSearchResultList();
		return searchResultList;
	}

	/**
	 * 検索結果用DTOリストの生成<br>
	 * DAO検索結果の未獲得市場リストから「UHP行」・「チーム合計行」・「対象区分合計行」を含んだ検索結果用DTOを生成する。
	 */
	public List<MikakutokuSijouResultDto> createResultDtoSumList() {
		// 検索結果リストがNull・Emptyの場合は配列0のリストを返却
		if (searchResultList == null || searchResultList.isEmpty()) {
			return new ArrayList<MikakutokuSijouResultDto>();
		}

		// UHP行の生成
		List<MikakutokuSijouResultDto> uhpList = createUHPList();

		// 検索結果行の生成
		List<MikakutokuSijouResultDto> searchResultList = createSearchResultList();

		// UHP行と検索結果行を結合したリストを生成
		List<MikakutokuSijouResultDto> resultDtoList = new ArrayList<MikakutokuSijouResultDto>();
		resultDtoList.addAll(uhpList);
		resultDtoList.addAll(searchResultList);

		// 未獲得市場一覧の全情報を搭載したリストを生成
		List<MikakutokuSijouResultDto> allIncludeList = createAllIncludeList(resultDtoList);

		return allIncludeList;
	}

	/**
	 * フィールドに格納された検索結果リストからUHP行の検索結果用DTOリストを生成する。
	 *
	 * @return UHP行の検索結果用DTOリスト
	 */
	protected List<MikakutokuSijouResultDto> createUHPList() {
		// 検索結果リストがNull・Emptyの場合は配列0のリストを返却
		if (searchResultList == null || searchResultList.isEmpty()) {
			return new ArrayList<MikakutokuSijouResultDto>();
		}
		// 集計MAPに「UH+P」の未獲得市場を格納
		// ( Key=[従業員番号], Value=[集計された未獲得市場オブジェクト] )
		Map<Integer, MikakutokuSijou> uhpMap = new LinkedHashMap<Integer, MikakutokuSijou>();
		for (MikakutokuSijou mikakutokuSijou : searchResultList) {
			final Integer jgiNo = mikakutokuSijou.getJgiNo();
			if (jgiNo != null) {
				final InsType insType = mikakutokuSijou.getInsType();
				final boolean isInsTypeUH = insType.equals(InsType.UH);
				final boolean isInsTypeP = insType.equals(InsType.P);
				if (isInsTypeUH || isInsTypeP) {
					MikakutokuSijou uhpData = null;
					if (uhpMap.containsKey(jgiNo)) {
						uhpData = uhpMap.get(jgiNo);
					} else {
						uhpData = new MikakutokuSijou();
						// 集計元の基本情報をセット
						uhpMap.put(jgiNo, uhpData);
						uhpData.setSosCd(mikakutokuSijou.getSosCd());
						uhpData.setBumonRyakuName(mikakutokuSijou.getBumonRyakuName());
						uhpData.setJgiNo(mikakutokuSijou.getJgiNo());
						uhpData.setJgiName(mikakutokuSijou.getJgiName());
						uhpData.setShokushuName(mikakutokuSijou.getShokushuName());
					}
					// 値の加算処理
					addValueForSumRowData(uhpData, mikakutokuSijou);
				}
			}
		}
		// MAPからLISTに変換
		List<MikakutokuSijou> uhpList = new ArrayList<MikakutokuSijou>();
		for (Entry<Integer, MikakutokuSijou> entry : uhpMap.entrySet()) {
			uhpList.add(entry.getValue());
		}
		// 構成比(%)の算出
		Long yakkouSijouMikakutokuUHP = yakkouSijouMikakutokuSumMap.get(INS_TYPE_UHP);
		Long distPlanMikakutokuUHP = distPlanMikakutokuSumMap.get(INS_TYPE_UHP);
		for (MikakutokuSijou mikakutokuSijou : uhpList) {
			calcRatio(mikakutokuSijou, yakkouSijouMikakutokuUHP, distPlanMikakutokuUHP);
		}
		// 検索結果用DTOの生成
		List<MikakutokuSijouResultDto> uhpDtoList = new ArrayList<MikakutokuSijouResultDto>();
		for (MikakutokuSijou mikakutokuSijou : uhpList) {
			uhpDtoList.add(convertResultDto(INS_TYPE_UHP, false, false, mikakutokuSijou));
		}
		return uhpDtoList;
	}

	/**
	 * フィールドに格納された検索結果リストから検索結果用DTOリストを生成する。<br>
	 *
	 * @return DAO検索結果を変換した検索結果用DTOリスト
	 */
	protected List<MikakutokuSijouResultDto> createSearchResultList() {
		// 検索結果リストがNull・Emptyの場合は配列0のリストを返却
		if (searchResultList == null || searchResultList.isEmpty()) {
			return new ArrayList<MikakutokuSijouResultDto>();
		}
		// 検索結果リストを検索結果用DTOに変換
		List<MikakutokuSijouResultDto> resultDtoList = new ArrayList<MikakutokuSijouResultDto>();
		for (MikakutokuSijou mikakutokuSijou : searchResultList) {
			final InsType insType = mikakutokuSijou.getInsType();
			if (insType != null) {
				resultDtoList.add(convertResultDto(insType.name(), false, false, mikakutokuSijou));
			}
		}
		return resultDtoList;
	}

	/**
	 * 検索結果から生成された検索結果用DTOを元に、組織毎の合計行を生成する。
	 *
	 *
	 * @param dtoList 検索結果用DTOリスト
	 * @return 集計MAP ( Key=[組織コード+対象区分コード], Value=[検索結果用DTO(合計行)] )
	 */
	protected Map<String, MikakutokuSijouResultDto> createSosSumMap(List<MikakutokuSijouResultDto> dtoList) {
		// 引数リストがNull・Emptyの場合はサイズ0のMAPを返却
		if (dtoList == null || dtoList.isEmpty()) {
			return new HashMap<String, MikakutokuSijouResultDto>();
		}
		Map<String, MikakutokuSijou> sosSumMap = new HashMap<String, MikakutokuSijou>();
		Map<String, String> insTypeMap = new HashMap<String, String>();
		for (MikakutokuSijouResultDto dto : dtoList) {
			final String sosCd = dto.getSosCd();
			final String insType = dto.getInsType();
			final String keyStr = sosCd + insType;
			MikakutokuSijou mikakutokuSijou = dto.getMikakutokuSijou();
			MikakutokuSijou sumRowData = null;
			if (sosSumMap.containsKey(keyStr)) {
				sumRowData = sosSumMap.get(keyStr);
			} else {
				sumRowData = new MikakutokuSijou();
				sumRowData.setBumonRyakuName("");
				sumRowData.setJgiName("");
				sosSumMap.put(keyStr, sumRowData);
				insTypeMap.put(keyStr, insType);
			}
			// 値の加算処理
			addValueForSumRowData(sumRowData, mikakutokuSijou);
		}
		Map<String, MikakutokuSijouResultDto> sosSumDtoMap = new HashMap<String, MikakutokuSijouResultDto>();
		for (Entry<String, MikakutokuSijou> entry : sosSumMap.entrySet()) {
			final String insType = insTypeMap.get(entry.getKey());
			// 構成比(%)の算出
			Long yakkouSijouMikakutoku = yakkouSijouMikakutokuSumMap.get(insType);
			Long distPlanMikakutoku = distPlanMikakutokuSumMap.get(insType);
			calcRatio(entry.getValue(), yakkouSijouMikakutoku, distPlanMikakutoku);
			// 検索用DTOに変換してMAPに格納
			sosSumDtoMap.put(entry.getKey(), convertResultDto(insType, true, false, entry.getValue()));
		}
		return sosSumDtoMap;
	}

	/**
	 * 検索結果から生成された検索結果用DTOを元に、対象区分毎の合計行を生成する。
	 *
	 * @param dtoList 検索結果用DTOリスト
	 * @return 集計MAP ( Key=[対象区分コード], Value=[検索結果用DTO(合計行)] )
	 */
	protected Map<String, MikakutokuSijouResultDto> createInsTypeSumMap(List<MikakutokuSijouResultDto> dtoList) {
		// 引数リストがNull・Emptyの場合はサイズ0のMAPを返却
		if (dtoList == null || dtoList.isEmpty()) {
			return new HashMap<String, MikakutokuSijouResultDto>();
		}
		Map<String, MikakutokuSijou> insTypeSumMap = new HashMap<String, MikakutokuSijou>();
		for (MikakutokuSijouResultDto dto : dtoList) {
			final String insType = dto.getInsType();
			MikakutokuSijou mikakutokuSijou = dto.getMikakutokuSijou();
			MikakutokuSijou sumRowData = null;
			if (insTypeSumMap.containsKey(insType)) {
				sumRowData = insTypeSumMap.get(insType);
			} else {
				sumRowData = new MikakutokuSijou();
				sumRowData.setBumonRyakuName("");
				sumRowData.setJgiName("");
				insTypeSumMap.put(insType, sumRowData);
			}
			// 値の加算処理
			addValueForSumRowData(sumRowData, mikakutokuSijou);
		}
		Map<String, MikakutokuSijouResultDto> insTypeSumDtoMap = new HashMap<String, MikakutokuSijouResultDto>();
		for (Entry<String, MikakutokuSijou> entry : insTypeSumMap.entrySet()) {
			// 構成比(%)の算出
			Long yakkouSijouMikakutoku = yakkouSijouMikakutokuSumMap.get(entry.getKey());
			Long distPlanMikakutoku = distPlanMikakutokuSumMap.get(entry.getKey());
			calcRatio(entry.getValue(), yakkouSijouMikakutoku, distPlanMikakutoku);
			// 検索用DTOに変換してMAPに格納
			insTypeSumDtoMap.put(entry.getKey(), convertResultDto(entry.getKey(), false, true, entry.getValue()));
		}
		return insTypeSumDtoMap;
	}

	/**
	 * 加算先(集計行の未獲得市場)に加算元(集計元となる未獲得市場)の値を追加する。
	 *
	 * @param targetObj 加算先の未獲得市場オブジェクト
	 * @param sourceData 加算元の未獲得市場オブジェクト
	 */
	protected void addValueForSumRowData(MikakutokuSijou targetObj, MikakutokuSijou sourceObj) {
		if (targetObj == null || sourceObj == null) {
			return;
		}
		// 薬効市場全体
		addValueForSumRowDataYakkouSijouZentai(targetObj, sourceObj);
		// 薬効市場タケダ品
		addValueForSumRowDataYakkouSijouTakeda(targetObj, sourceObj);
		// 薬効市場未獲得市場
		addValueForSumRowDataYakkouSijouMikakutoku(targetObj, sourceObj);
		// 増減金額
		addValueForSumRowDataYakkouSijouModifyAmount(targetObj, sourceObj);
		// 営業所案 未獲得市場
		addValueForSumRowDataDistPlanMikakutoku(targetObj, sourceObj);
	}

	/**
	 * 加算先(集計行の未獲得市場)に加算元(集計元となる未獲得市場)の薬効市場全体を追加する。
	 *
	 * @param targetObj 加算先の未獲得市場オブジェクト
	 * @param sourceData 加算元の未獲得市場オブジェクト
	 */
	protected void addValueForSumRowDataYakkouSijouZentai(MikakutokuSijou targetObj, MikakutokuSijou sourceObj) {
		if (targetObj == null || sourceObj == null) {
			return;
		}
		Long targetYakkouSijouZentai = targetObj.getYakkouSijouZentai();
		Long sourceYakkouSijouZentai = sourceObj.getYakkouSijouZentai();
		if (sourceYakkouSijouZentai != null) {
			if (targetYakkouSijouZentai == null) {
				targetYakkouSijouZentai = sourceYakkouSijouZentai;
			} else {
				targetYakkouSijouZentai += sourceYakkouSijouZentai;
			}
		}
		targetObj.setYakkouSijouZentai(targetYakkouSijouZentai);
	}

	/**
	 * 加算先(集計行の未獲得市場)に加算元(集計元となる未獲得市場)の薬効市場タケダ品を追加する。
	 *
	 * @param targetObj 加算先の未獲得市場オブジェクト
	 * @param sourceData 加算元の未獲得市場オブジェクト
	 */
	protected void addValueForSumRowDataYakkouSijouTakeda(MikakutokuSijou targetObj, MikakutokuSijou sourceObj) {
		if (targetObj == null || sourceObj == null) {
			return;
		}
		Long targetYakkouSijouTakeda = targetObj.getYakkouSijouTakeda();
		Long sourceYakkouSijouTakeda = sourceObj.getYakkouSijouTakeda();
		if (sourceYakkouSijouTakeda != null) {
			if (targetYakkouSijouTakeda == null) {
				targetYakkouSijouTakeda = sourceYakkouSijouTakeda;
			} else {
				targetYakkouSijouTakeda += sourceYakkouSijouTakeda;
			}
		}
		targetObj.setYakkouSijouTakeda(targetYakkouSijouTakeda);
	}

	/**
	 * 加算先(集計行の未獲得市場)に加算元(集計元となる未獲得市場)の薬効市場未獲得市場を追加する。
	 *
	 * @param targetObj 加算先の未獲得市場オブジェクト
	 * @param sourceData 加算元の未獲得市場オブジェクト
	 */
	protected void addValueForSumRowDataYakkouSijouMikakutoku(MikakutokuSijou targetObj, MikakutokuSijou sourceObj) {
		if (targetObj == null || sourceObj == null) {
			return;
		}
		Long targetYakkouSijouMikakutoku = targetObj.getYakkouSijouMikakutoku();
		Long sourceYakkouSijouMikakutoku = sourceObj.getYakkouSijouMikakutoku();
		if (sourceYakkouSijouMikakutoku != null) {
			if (targetYakkouSijouMikakutoku == null) {
				targetYakkouSijouMikakutoku = sourceYakkouSijouMikakutoku;
			} else {
				targetYakkouSijouMikakutoku += sourceYakkouSijouMikakutoku;
			}
		}
		targetObj.setYakkouSijouMikakutoku(targetYakkouSijouMikakutoku);
	}

	/**
	 * 加算先(集計行の未獲得市場)に加算元(集計元となる未獲得市場)の増減金額を追加する。
	 *
	 * @param targetObj 加算先の未獲得市場オブジェクト
	 * @param sourceData 加算元の未獲得市場オブジェクト
	 */
	protected void addValueForSumRowDataYakkouSijouModifyAmount(MikakutokuSijou targetObj, MikakutokuSijou sourceObj) {
		if (targetObj == null || sourceObj == null) {
			return;
		}
		Long targetModifyAmountY = targetObj.getModifyAmountY();
		Long sourceModifyAmountY = sourceObj.getModifyAmountY();
		if (sourceModifyAmountY != null) {
			if (targetModifyAmountY == null) {
				targetModifyAmountY = sourceModifyAmountY;
			} else {
				targetModifyAmountY += sourceModifyAmountY;
			}
		}
		targetObj.setModifyAmountY(targetModifyAmountY);
	}

	/**
	 * 加算先(集計行の未獲得市場)に加算元(集計元となる未獲得市場)の営業所案 未獲得市場を追加する。
	 *
	 * @param targetObj 加算先の未獲得市場オブジェクト
	 * @param sourceData 加算元の未獲得市場オブジェクト
	 */
	protected void addValueForSumRowDataDistPlanMikakutoku(MikakutokuSijou targetObj, MikakutokuSijou sourceObj) {
		if (targetObj == null || sourceObj == null) {
			return;
		}
		Long targetDistPlanMikakutoku = targetObj.getDistPlanMikakutoku();
		Long sourceDistPlanMikakutoku = sourceObj.getDistPlanMikakutoku();
		if (sourceDistPlanMikakutoku != null) {
			if (targetDistPlanMikakutoku == null) {
				targetDistPlanMikakutoku = sourceDistPlanMikakutoku;
			} else {
				targetDistPlanMikakutoku += sourceDistPlanMikakutoku;
			}
		}
		targetObj.setDistPlanMikakutoku(targetDistPlanMikakutoku);
	}

	/**
	 * 検索結果一覧における全情報を保持するリストを生成する。
	 *
	 * @param resultDtoList 検索結果用DTOリスト
	 * @return 検索結果一覧における全情報を保持するリスト
	 */
	protected ArrayList<MikakutokuSijouResultDto> createAllIncludeList(List<MikakutokuSijouResultDto> resultDtoList) {
		// 引数リストがNull・Emptyの場合は配列0のリストを返却
		if (resultDtoList == null || resultDtoList.isEmpty()) {
			return new ArrayList<MikakutokuSijouResultDto>();
		}
		// 組織合計行の集計MAP
		Map<String, MikakutokuSijouResultDto> sosSumMap = createSosSumMap(resultDtoList);
		// 対象区分合計行の集計MAP
		Map<String, MikakutokuSijouResultDto> insTypeSumMap = createInsTypeSumMap(resultDtoList);

		// 新規データリストに各合計行を画面の表示順番で挿入する
		ArrayList<MikakutokuSijouResultDto> allIncludeList = new ArrayList<MikakutokuSijouResultDto>();
		String workSosCd = resultDtoList.get(0).getSosCd();
		String workInsType = resultDtoList.get(0).getInsType();
		for (MikakutokuSijouResultDto dto : resultDtoList) {
			final String sosCd = dto.getSosCd();
			final String insType = dto.getInsType();
			final boolean isSameSosCd = StringUtils.equals(workSosCd, sosCd);
			final boolean isSameInsType = StringUtils.equals(workInsType, insType);
			// 組織が前回と異なり、対象区分が前回と同様の場合は組織合計行を挿入
			if (!isSameSosCd && isSameInsType) {
				allIncludeList.add(sosSumMap.get(workSosCd + workInsType));
			}
			// 対象区分が前回と異なる場合は組織合計行と対象区分合計行を挿入
			else if (!isSameInsType) {
				allIncludeList.add(sosSumMap.get(workSosCd + workInsType));
				allIncludeList.add(insTypeSumMap.get(workInsType));
			}
			// 通常の検索結果を挿入
			allIncludeList.add(dto);
			// 現ループの情報を保持
			workSosCd = dto.getSosCd();
			workInsType = dto.getInsType();
		}
		// 最終行に残りの組織合計行と対象区分合計行を挿入
		allIncludeList.add(sosSumMap.get(workSosCd + workInsType));
		allIncludeList.add(insTypeSumMap.get(workInsType));
		return allIncludeList;
	}

	/**
	 * 未獲得市場モデルから未獲得市場検索結果用DTOへの変換処理を行う。<br>
	 * ( MikakutokuSijou ⇒ MikakutokuSijouResultDto )
	 *
	 * @param insType 施設対象区分
	 * @param isSosSumRow 組織合計行=TRUE, 組織合計行でない=FALSE
	 * @param isInsTypeSumRow 対象区分合計行=TRUE, 対象区分合計行でない=FALSE
	 * @param mikakutokuSijou 変換元となる未獲得市場オブジェクト
	 * @return
	 */
	protected MikakutokuSijouResultDto convertResultDto(String insType, boolean isSosSumRow, boolean isInsTypeSumRow, MikakutokuSijou mikakutokuSijou) {
		if (mikakutokuSijou == null) {
			return null;
		}
		final String sosCode = mikakutokuSijou.getSosCd();
		final String sosName = mikakutokuSijou.getBumonRyakuName();
		final Integer jgiNo = mikakutokuSijou.getJgiNo();
		final String jgiName = mikakutokuSijou.getJgiName();
		final String insNo = mikakutokuSijou.getInsNo();
		final String insName = mikakutokuSijou.getInsAbbrName();
		final String shokushuName = mikakutokuSijou.getShokushuName();
		final Long yakkouSijouZentai = ConvertUtil.parseMoneyToThousandUnit(mikakutokuSijou.getYakkouSijouZentai());
		final Long yakkouSijouTakeda = ConvertUtil.parseMoneyToThousandUnit(mikakutokuSijou.getYakkouSijouTakeda());
		final Long yakkouSijouMikakutoku = ConvertUtil.parseMoneyToThousandUnit(mikakutokuSijou.getYakkouSijouMikakutoku());
		final Double yakkouSijouRatio = mikakutokuSijou.getYakkouSijouRatio();
		final Long modifyAmount = ConvertUtil.parseMoneyToThousandUnit(mikakutokuSijou.getModifyAmountY());
		final Long distPlanMikakutoku = ConvertUtil.parseMoneyToThousandUnit(mikakutokuSijou.getDistPlanMikakutoku());
		final Double distPlanRatio = mikakutokuSijou.getDistPlanRatio();
		MikakutokuSijouResultDto resultDto = new MikakutokuSijouResultDto(sosCode, sosName, jgiNo, jgiName, insType, insNo, insName, yakkouSijouZentai, yakkouSijouTakeda,
			yakkouSijouMikakutoku, yakkouSijouRatio, modifyAmount, distPlanMikakutoku, distPlanRatio, isSosSumRow, isInsTypeSumRow, mikakutokuSijou, shokushuName);
		return resultDto;
	}

	/**
	 * 構成(%)を算出し未獲得市場オブジェクトに挿入する。
	 *
	 * @param mikakutokuSijou 算出した構成比(%)を挿入する未獲得市場オブジェクト
	 * @param sumYakkouSijouMikakutoku 構成比のdenominator(割る値) 薬効市場の未獲得市場値
	 * @param sumDistPlanMikakutoku 構成比のdenominator(割る値) 営業所の未獲得市場値
	 */
	protected void calcRatio(MikakutokuSijou mikakutokuSijou, Long sumYakkouSijouMikakutoku, Long sumDistPlanMikakutoku) {
		if (mikakutokuSijou == null) {
			return;
		}
		// 薬効市場未獲得市場
		Long yakkouSijouMikakutoku = mikakutokuSijou.getYakkouSijouMikakutoku();
		mikakutokuSijou.setYakkouSijouRatio(getCalcRatioValue(yakkouSijouMikakutoku, sumYakkouSijouMikakutoku));
		// 営業所案未獲得市場
		Long distPlanMikakutoku = mikakutokuSijou.getDistPlanMikakutoku();
		mikakutokuSijou.setDistPlanRatio(getCalcRatioValue(distPlanMikakutoku, sumDistPlanMikakutoku));
	}

	/**
	 * 構成(%)の値を計算し取得する。
	 *
	 * <pre>
	 * 計算式
	 *  (割れられる値÷割る値)×100
	 *  (除算は小数点第三位以下を四捨五入とする)
	 * </pre>
	 *
	 * @param numerator 割れられる値
	 * @param denominator 割る値
	 * @return 構成(%)
	 */
	protected Double getCalcRatioValue(final Number numerator, final Number denominator) {
		if (numerator == null || denominator == null) {
			return null;
		} else if (denominator.doubleValue() == 0d) {
			return null;
		}
		Double doubleValue = MathUtil.divide(numerator, denominator, 3, RoundingMode.HALF_UP);
		BigDecimal bdValue = new BigDecimal(doubleValue.toString());
		bdValue = bdValue.multiply(new BigDecimal(100));
		return bdValue.doubleValue();
	}

	/**
	 * 薬効市場の未獲得市場値の集計MAPを生成する。
	 *
	 * @return 集計MAP( Key=対象区分コード Value=合計金額)
	 */
	protected Map<String, Long> createYakkouSijouMikakutokuSumMap() {
		if (searchResultList == null || searchResultList.isEmpty()) {
			return new HashMap<String, Long>();
		}
		Map<String, Long> yakkouSijouMikakutokuSumMap = new HashMap<String, Long>();
		for (MikakutokuSijou mikakutokuSijou : searchResultList) {
			final String insType = mikakutokuSijou.getInsType().name();
			final Long yakkouSijouMikakutoku = mikakutokuSijou.getYakkouSijouMikakutoku();
			if (yakkouSijouMikakutoku != null) {
				Long amount = yakkouSijouMikakutokuSumMap.get(insType);
				if (amount == null) {
					yakkouSijouMikakutokuSumMap.put(insType, yakkouSijouMikakutoku);
				} else {
					amount += yakkouSijouMikakutoku;
					yakkouSijouMikakutokuSumMap.put(insType, amount);
				}
			}
		}
		// UHPの集計MAPを追加
		addUHPSumMap(yakkouSijouMikakutokuSumMap);
		return yakkouSijouMikakutokuSumMap;
	}

	/**
	 * 営業所案の未獲得市場値の集計MAPを生成する。
	 *
	 * @return 集計MAP( Key=対象区分コード Value=合計金額)
	 */
	protected Map<String, Long> createDistPlanMikakutokuSumMap() {
		if (searchResultList == null || searchResultList.isEmpty()) {
			return new HashMap<String, Long>();
		}
		Map<String, Long> distPlanMikakutokuSumMap = new HashMap<String, Long>();
		for (MikakutokuSijou mikakutokuSijou : searchResultList) {
			final String insType = mikakutokuSijou.getInsType().name();
			final Long distPlanMikakutoku = mikakutokuSijou.getDistPlanMikakutoku();
			if (distPlanMikakutoku != null) {
				Long mapAmount = distPlanMikakutokuSumMap.get(insType);
				if (mapAmount == null) {
					distPlanMikakutokuSumMap.put(insType, distPlanMikakutoku);
				} else {
					mapAmount += distPlanMikakutoku;
					distPlanMikakutokuSumMap.put(insType, mapAmount);
				}
			}
		}
		// UHPの集計MAPを追加
		addUHPSumMap(distPlanMikakutokuSumMap);
		return distPlanMikakutokuSumMap;
	}

	/**
	 * UHPをキーにUHとPの合計金額を引数のMAPに追加する。
	 *
	 * @param sourceMap UHとPがキーとなり未獲得市場値が集計されたMAP
	 */
	protected void addUHPSumMap(Map<String, Long> sourceMap) {
		if (sourceMap == null) {
			return;
		}
		Long uhpAmount = 0L;
		for (Entry<String, Long> entry : sourceMap.entrySet()) {
			final boolean isUH = StringUtils.equals(InsType.UH.name(), entry.getKey());
			final boolean isP = StringUtils.equals(InsType.P.name(), entry.getKey());
			if (isUH || isP) {
				uhpAmount += entry.getValue();
			}
		}
		sourceMap.put(INS_TYPE_UHP, uhpAmount);
	}
}
