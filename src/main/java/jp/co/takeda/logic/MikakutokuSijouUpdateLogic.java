package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.Date;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.MikakutokuSijouDao;
import jp.co.takeda.dto.MikakutokuSijouUpdateDto;
import jp.co.takeda.model.MikakutokuSijou;

/**
 * 未獲得市場の更新ロジッククラス
 * 
 * @author stakeuchi
 */
public class MikakutokuSijouUpdateLogic {

	/**
	 * 未獲得市場DAO
	 */
	private final MikakutokuSijouDao mikakutokuSijouDao;

	/**
	 * 未獲得市場更新用DTOリスト
	 */
	private final List<MikakutokuSijouUpdateDto> modifyDtoList;

	/**
	 * コンストラクタ
	 * 
	 * @param mikakutokuSijouDao 未獲得市場DAO
	 * @param modifyDtoList 未獲得市場更新用DTOリスト
	 */
	public MikakutokuSijouUpdateLogic(MikakutokuSijouDao mikakutokuSijouDao, List<MikakutokuSijouUpdateDto> modifyDtoList) {
		// ----------------------
		// パラメータチェック
		// ----------------------
		if (mikakutokuSijouDao == null) {
			final String errMsg = "未獲得市場にアクセスするDAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (modifyDtoList == null || modifyDtoList.size() == 0) {
			final String errMsg = "未獲得市場の更新用DTOリストがnull、またはサイズ0";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.mikakutokuSijouDao = mikakutokuSijouDao;
		this.modifyDtoList = modifyDtoList;
	}

	/**
	 * 未獲得市場の更新ロジック実行
	 */
	public void update() {
		for (MikakutokuSijouUpdateDto dto : modifyDtoList) {
			// ----------------------
			// DTOパラメータチェック
			// ----------------------
			final Long seqKey = dto.getSeqKey();
			if (seqKey == null) {
				final String errMsg = "シーケンスキーがnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			final Date upDate = dto.getUpDate();
			if (upDate == null) {
				final String errMsg = "最終更新日時がnull";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
			}
			final Long yakkouSijouMikakutoku = dto.getYakkouSijouMikakutoku();
			final Long modifyAmount = dto.getModifyAmount();

			// ----------------------
			// 更新オブジェクト生成
			// ----------------------
			MikakutokuSijou mikakutokuSijou = new MikakutokuSijou();
			// シーケンスキーをセット
			mikakutokuSijou.setSeqKey(seqKey);
			// 最終更新日時をセット
			mikakutokuSijou.setUpDate(upDate);
			// 増減金額をセット
			mikakutokuSijou.setModifyAmountY(modifyAmount);

			if (yakkouSijouMikakutoku != null) {
				if (modifyAmount != null) {
					// 営業所案未獲得市場をセット
					mikakutokuSijou.setDistPlanMikakutoku(yakkouSijouMikakutoku + modifyAmount);
				} else {
					mikakutokuSijou.setDistPlanMikakutoku(yakkouSijouMikakutoku);
				}
			} else {
				if (modifyAmount != null) {
					// 営業所案未獲得市場をセット
					mikakutokuSijou.setDistPlanMikakutoku(modifyAmount);
				}
			}
			// ----------------------
			// 更新処理実行
			// ----------------------
			mikakutokuSijouDao.update(mikakutokuSijou);
		}
	}
}
