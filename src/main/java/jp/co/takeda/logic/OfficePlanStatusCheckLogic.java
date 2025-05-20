package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;
import static jp.co.takeda.logic.div.OfficeStatusForCheck.*;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsCodeMasterDao;
import jp.co.takeda.dao.OfficePlanStatusDao;
import jp.co.takeda.dto.OfficeStatusCheckDto;
import jp.co.takeda.dto.OfficeStatusCheckResultDto;
import jp.co.takeda.logic.div.OfficeStatusForCheck;
import jp.co.takeda.model.DpsCCdMst;
import jp.co.takeda.model.OfficePlanStatus;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.model.div.DpsCodeMaster;
import jp.co.takeda.model.div.StatusForOffice;

/**
 * 営業所計画ステータスをチェックするロジッククラス
 *
 * @author nozaki
 */
public class OfficePlanStatusCheckLogic {

	/**
	 * 営業所計画ステータスDAO
	 */
	private final OfficePlanStatusDao officePlanStatusDao;

	/**
	 * チェック対象の営業所の組織情報
	 */
	private final SosMst sosMst;

	/**
	 * チェック対象のカテゴリ
	 */
//	private ProdCategory prodCategory;
	private String prodCategory;

	/**
	 * 許可しない営業所計画ステータスのリスト
	 */
	private List<OfficeStatusForCheck> unallowableStatusList;

	/**
	 * ステータスなしエラーフラグ
	 * <ul>
	 * <li>true：ステータスなしをエラーとする</li>
	 * <li>false：ステータスなしエラーとしない</li>
	 * <ul>
	 */
	private final boolean nothingStatusError;

	/**
	 * 計画支援汎用マスタDAO
	 */
	private final DpsCodeMasterDao dpsCodeMasterDao;

	/**
	 * コンストラクタ
	 *
	 * @param officePlanStatusDao 営業所計画ステータスDAO
	 * @param unallowableStatusDto 営業所計画ステータスチェック用DTO
	 * @param dpsCodeMasterDao 計画支援汎用マスタDAO
	 */
	public OfficePlanStatusCheckLogic(OfficePlanStatusDao officePlanStatusDao, OfficeStatusCheckDto officeStatusCheckDto, DpsCodeMasterDao dpsCodeMasterDao) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (officePlanStatusDao == null) {
			final String errMsg = "営業所計画ステータスにアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (officeStatusCheckDto == null) {
			final String errMsg = "営業所計画ステータスチェック用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (officeStatusCheckDto.getSosMst() == null) {
			final String errMsg = "チェック対象の営業所の組織情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		// ワクチンの場合、組織未選択時は全社対象とする
//		if (officeStatusCheckDto.getSosMst().getSosCd() == null) {
//			final String errMsg = "チェック対象の営業所の組織コードがnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
//		if (officeStatusCheckDto.getSosMst().getBumonSeiName() == null) {
//			final String errMsg = "チェック対象の営業所の部門名正式がnull";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}
		if (officeStatusCheckDto.getProdCategory() == null) {
			final String errMsg = "チェック対象のカテゴリがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (officeStatusCheckDto.getStatusForOfficeList() == null || officeStatusCheckDto.getStatusForOfficeList().size() == 0) {
			final String errMsg = "許可しない営業所計画ステータスのリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		this.officePlanStatusDao = officePlanStatusDao;
		this.sosMst = officeStatusCheckDto.getSosMst();
		this.prodCategory = officeStatusCheckDto.getProdCategory();
		this.unallowableStatusList = officeStatusCheckDto.getStatusForOfficeList();

		// 許可しない営業所計画ステータスに「ステータスなし」がある場合
		if (officeStatusCheckDto.getStatusForOfficeList().contains(NOTHING)) {
			this.nothingStatusError = true;
		} else {
			this.nothingStatusError = false;
		}

		this.dpsCodeMasterDao = dpsCodeMasterDao;

	}

	/**
	 * ステータスチェック実行
	 */
	public OfficeStatusCheckResultDto execute() {

		// エラーメッセージのリスト
		List<MessageKey> errorMessageKeyList = new ArrayList<MessageKey>();

		// DBから取得した営業所別計画ステータスのリスト
		List<OfficePlanStatus> officePlanStatusList = new ArrayList<OfficePlanStatus>();

		// 組織コード、カテゴリを指定してステータスを取得
		OfficePlanStatus officePlanStatus = null;
		try {
//			officePlanStatus = officePlanStatusDao.search(sosMst.getSosCd(), prodCategory);
//			officePlanStatusList.add(officePlanStatus);
			officePlanStatusList = officePlanStatusDao.searchSosCdCategoryList(sosMst.getSosCd(), prodCategory);

		} catch (DataNotFoundException e) {

			if (nothingStatusError) {

				// ステータスなしをエラーとする場合は、エラーメッセージ追加
				errorMessageKeyList.add(new MessageKey("DPS3101E", sosMst.getBumonSeiName(), prodCategoryName(prodCategory)));
				return new OfficeStatusCheckResultDto(errorMessageKeyList, officePlanStatusList);

			} else {

				// エラーにしない場合は、新規作成してリストに追加
				officePlanStatus = new OfficePlanStatus();
				officePlanStatus.setSosCd(sosMst.getSosCd());
				officePlanStatus.setProdCategory(prodCategory);
				officePlanStatusList.add(officePlanStatus);

				return new OfficeStatusCheckResultDto(officePlanStatusList);
			}
		}

		// 取得したステータスが許可しないステータスの場合は、エラーメッセージ追加
		for (OfficePlanStatus officePlanStatusRow : officePlanStatusList) {
			StatusForOffice resultStatus = officePlanStatusRow.getStatusForOffice();
			if (unallowableStatusList.contains(OfficeStatusForCheck.getInstance(resultStatus))) {

				switch (resultStatus) {
					case DRAFT:
						errorMessageKeyList.add(new MessageKey("DPS3102E", sosMst.getBumonSeiName(), prodCategoryName(prodCategory)));
						return new OfficeStatusCheckResultDto(errorMessageKeyList, officePlanStatusList);
					case COMPLETED:
						errorMessageKeyList.add(new MessageKey("DPS3103E", sosMst.getBumonSeiName(), prodCategoryName(prodCategory)));
						return new OfficeStatusCheckResultDto(errorMessageKeyList, officePlanStatusList);
				}
			}
		}

		return new OfficeStatusCheckResultDto(officePlanStatusList);
	}

	/**
	 * カテゴリから名称を取得する。
	 *
	 * @param prodCategory
	 * @return カテゴリ名称
	 */
//	private String prodCategoryName(ProdCategory prodCategory) {
	private String prodCategoryName(String prodCategory) {
// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
//		switch (prodCategory) {
//			case MMP:
//				return "JPBU(STARS)";
//			case ONC:
//				return "ONC品";
//			case SHIIRE:
//				return "仕入品（一般・麻薬）";
//			default:
//				final String errMsg = "指定のコード値に対応する列挙が存在しない";
//				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

//		String categoryName = prodCategory.getProdCategoryName();

		// 検索結果一覧
		DpsCCdMst dpsCCdMst = new DpsCCdMst();
		try {
			// データ区分の検索
			dpsCCdMst = dpsCodeMasterDao.searchDataKbnAndCd(DpsCodeMaster.CAT.getDbValue(), prodCategory);
		} catch (DataNotFoundException e) {
			final String errMsg = "汎用マスタに、「DATA_KBN,DATA_CD=" + DpsCodeMaster.CAT.getDbValue() + ","  + prodCategory + "」が登録されていません。";
			throw new SystemException(new Conveyance(DATA_NOT_FOUND_ERROR, errMsg));
		}

		String categoryName = dpsCCdMst.getDataName();

//		if(categoryName.isEmpty()) {
//			final String errMsg = "指定のコード値に対応する列挙が存在しない";
//			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
//		}

		return categoryName;
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）
	}
}
