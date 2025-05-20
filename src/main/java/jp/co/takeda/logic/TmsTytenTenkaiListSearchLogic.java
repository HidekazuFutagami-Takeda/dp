package jp.co.takeda.logic;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.DuplicateException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.TmsTytenMstUnDAO;
import jp.co.takeda.dto.TmsTytenMstScDto;
import jp.co.takeda.dto.TmsTytenMstTenkaiDto;
import jp.co.takeda.model.TmsTytenMstUn;
import jp.co.takeda.model.div.TytenKisLevel;

/**
 * 特約店検索の展開ロジッククラス
 *
 * @author khashimoto
 */
public class TmsTytenTenkaiListSearchLogic {

	/**
	 * 特約店情報DAO
	 */
	private final TmsTytenMstUnDAO tmsTytenMstUnDAO;

	/**
	 * 特約店情報
	 */
	private final TmsTytenMstUn tmsTytenMstUn;

	/**
	 * 特約店情報の検索条件
	 */
	private final TmsTytenMstScDto tmsTytenMstScDto;

	/**
	 * 従業員番号
	 */
	private final String jgiNo;

	/**
	 * 対象区分
	 */
	private final String insType;

	/**
	 * コンストラクタ
	 *
	 * @param tmsTytenMstUnDAO 特約店情報DAO
	 * @param tmsTytenMstUn 特約店情報
	 * @param tmsTytenMstScDto 特約店情報の検索条件
	 */
	public TmsTytenTenkaiListSearchLogic(TmsTytenMstUnDAO tmsTytenMstUnDAO, TmsTytenMstUn tmsTytenMstUn, TmsTytenMstScDto tmsTytenMstScDto) {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (tmsTytenMstUnDAO == null) {
			final String errMsg = "特約店情報DAOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenMstUn == null) {
			final String errMsg = "特約店情報がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (tmsTytenMstScDto == null) {
			final String errMsg = "特約店情報の検索条件がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		this.tmsTytenMstUnDAO = tmsTytenMstUnDAO;
		this.tmsTytenMstUn = tmsTytenMstUn;
		this.tmsTytenMstScDto = tmsTytenMstScDto;
		this.jgiNo = tmsTytenMstScDto.getJgiNo();
		this.insType = tmsTytenMstScDto.getInsType();
	}

	/**
	 * 特定施設個別計画の登録/更新ロジックを実行する。
	 *
	 * @throws DuplicateException
	 */
	public List<TmsTytenMstTenkaiDto> execute() throws LogicalException {

		// 検索条件生成
		String honten = tmsTytenMstUn.getTmsTytenCdHonten();
		String shisha = tmsTytenMstUn.getTmsTytenCdShisha();
		String shiten = tmsTytenMstUn.getTmsTytenCdShiten();
		switch (tmsTytenMstUn.getTytenKisLvll()) {
			case HONTEN:
				shisha = convertTytenCd(tmsTytenMstUn.getTmsTytenCdShisha());
				shiten = convertTytenCd(tmsTytenMstUn.getTmsTytenCdShiten());
				break;
			case SHISHA:
				shiten = convertTytenCd(tmsTytenMstUn.getTmsTytenCdShiten());
				break;
		}
		TytenKisLevel level = tmsTytenMstScDto.getTytenKisLevel();
		String tytenCd = tmsTytenMstScDto.getTmsTytenCd();
		// 本店階層以外の特約店検索時は、組織コード（特約店部）は紐付けない
		String takedaSsk1Cd = null;
		String hontenMeiKn = tmsTytenMstScDto.getHontenMeiKn();
		// mod 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		TmsTytenMstScDto scDto = new TmsTytenMstScDto(level, tytenCd, honten, shisha, shiten, takedaSsk1Cd, hontenMeiKn, jgiNo, insType, null);

		// ----------------------
		// 検索実行
		// ----------------------
		// 検索結果取得
		List<TmsTytenMstUn> searchlist;
		try {
			searchlist = tmsTytenMstUnDAO.searchList(TmsTytenMstUnDAO.SORT_STRING, scDto);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")));
		}

		// 検索対象特約店の本店以下全リストを取得
		// mod 2022/6/27  Y.Taniguchi バックログNo.15　ワクチンの場合、選択した都道府県に紐づく“特約店（本店）”のみ表示
		TmsTytenMstScDto allScDto = new TmsTytenMstScDto(null, null, honten, null, null, null, null, null, null, null);
		List<TmsTytenMstUn> allList;
		try {
			allList = tmsTytenMstUnDAO.searchList(TmsTytenMstUnDAO.SORT_STRING, allScDto);
		} catch (DataNotFoundException e) {
			final String errMsg = "特約店情報が取得不可";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
		}

		// 末端フラグの判定を行い、結果をDTOに変換
		List<TmsTytenMstTenkaiDto> resultList = new ArrayList<TmsTytenMstTenkaiDto>();
		for (TmsTytenMstUn tmsTytenMstUn : searchlist) {
			boolean endFlg = false;
			boolean deliveryFlg = false;
			int index = allList.indexOf(tmsTytenMstUn);
			// 全リスト最後の場合、TRUE
			if ((index + 1) == allList.size()) {
				endFlg = true;
				// 担当者実績がある場合
				if(BooleanUtils.isTrue(tmsTytenMstUn.getDeliveryFlg())){
					deliveryFlg = true;
				}
			} else {
				// 次レコードの階層レベル以上の場合、TRUE
				TmsTytenMstUn nextTmsMst = allList.get(index + 1);
				if ((tmsTytenMstUn.getTytenKisLvll().compareTo(nextTmsMst.getTytenKisLvll())) >= 0) {
					endFlg = true;
					// 担当者実績がある場合
					if(BooleanUtils.isTrue(tmsTytenMstUn.getDeliveryFlg())){
						deliveryFlg = true;
					}
				}
			}
			resultList.add(new TmsTytenMstTenkaiDto(tmsTytenMstUn, endFlg, deliveryFlg));
		}
		return resultList;
	}

	/**
	 * 特約店コードを変換する。
	 *
	 * <pre>
	 * "00"の場合、nullに変換。
	 * </pre>
	 *
	 * @param code
	 * @return 変換後の特約店コード
	 */
	private String convertTytenCd(String code) {
		if (code == null) {
			return null;
		}
		if (code.equals("00")) {
			return null;
		}
		return code;
	}
}
