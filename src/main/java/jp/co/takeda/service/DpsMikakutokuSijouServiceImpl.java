package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.MikakutokuSijouDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dto.MikakutokuSijouUpdateDto;
import jp.co.takeda.exp.UnallowableStatusException;
import jp.co.takeda.logic.MikakutokuSijouUpdateLogic;
import jp.co.takeda.logic.div.MrStatusForCheck;
import jp.co.takeda.model.SosMst;

/**
 * 未獲得市場の登録/更新に関するサービス実装クラス
 *
 * @author stakeuchi
 */
@Transactional
@Service("dpsMikakutokuSijouService")
public class DpsMikakutokuSijouServiceImpl implements DpsMikakutokuSijouService {

	/**
	 * 未獲得市場DAO
	 */
	@Autowired(required = true)
	@Qualifier("mikakutokuSijouDao")
	protected MikakutokuSijouDao mikakutokuSijouDao;

	/**
	 * 担当者別計画ステータスサービス
	 */
	@Autowired(required = true)
	@Qualifier("dpsMrStatusCheckService")
	protected DpsMrStatusCheckService dpsMrStatusCheckService;

	/**
	 * 組織情報(取込)DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	public void updateMikakutokuSijou(String sosCd3, List<MikakutokuSijouUpdateDto> updateDtoList) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (sosCd3 == null) {
			final String errMsg = "組織コード(営業所)がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (updateDtoList == null) {
			final String errMsg = "未獲得市場の更新用DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ----------------------
		// 組織情報
		// ----------------------
		SosMst sosMst;
		try {
			sosMst = sosMstDAO.search(sosCd3);
		} catch (DataNotFoundException e) {
			final String errMsg = "対象営業所が取得できない：" + sosCd3;
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

// mod start 2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　組織マスタ.OncSosFlgでのカテゴリー判定を、組織マスタ.category、subCategoryを参照に変更
		// 組織からカテゴリ判定（MMP or ONC）
//		ProdCategory category = ProdCategory.getSosCategory(sosMst.getOncSosFlg());
//		ProdCategory category = ProdCategory.getInstance(sosMst.getSosCategory());
		String category = sosMst.getSosCategory();
// mod end   2018/05/21 S.Hayashi J18-0002_2018年4月組織変更対応（計画支援）　組織マスタ.OncSosFlgでのカテゴリー判定を、組織マスタ.category、subCategoryを参照に変更

		// ----------------------
		// ステータスチェック
		// ----------------------
		// 「試算中」は許可しない
		List<MrStatusForCheck> unallowableStatusList = new ArrayList<MrStatusForCheck>();
		unallowableStatusList.add(MrStatusForCheck.ESTIMATING);
		try {
			dpsMrStatusCheckService.execute(sosCd3, category, unallowableStatusList);
		} catch (UnallowableStatusException e) {

			// メッセージ作成
			List<MessageKey> messageKeyList = new ArrayList<MessageKey>();
			messageKeyList.add(new MessageKey("DPS3235E"));
			messageKeyList.addAll(e.getConveyance().getMessageKeyList());
			throw new UnallowableStatusException(new Conveyance(messageKeyList));
		}

		// ----------------------
		// 更新処理
		// ----------------------
		MikakutokuSijouUpdateLogic updateLogic = new MikakutokuSijouUpdateLogic(mikakutokuSijouDao, updateDtoList);
		updateLogic.update();
	}
}
