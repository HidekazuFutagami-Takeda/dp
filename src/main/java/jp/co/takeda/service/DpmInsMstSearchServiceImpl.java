package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpmCViSosCtgDao;
import jp.co.takeda.dao.DpmSyComInsOyakoDao;
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dto.InsMrDto;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.dto.InsMstScDto;
import jp.co.takeda.logic.CreateInsMstScResultDtoLogic;
import jp.co.takeda.model.InsMst;
import jp.co.takeda.model.PlannedCtg;
import jp.co.takeda.model.view.SosCtg;

/**
 * 施設検索サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpmInsMstSearchService")
public class DpmInsMstSearchServiceImpl implements DpmInsMstSearchService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstRealDao")
	protected InsMstRealDao insMstRealDao;

	/**
	 * 組織カテゴリDAO
	 */
	@Autowired(required = true)
    @Qualifier("dpmCViSosCtgDao")
    protected DpmCViSosCtgDao dpmCViSosCtgDao;

	/**
	 * プランカテゴリDAO
	 */
    @Autowired(required = true)
    @Qualifier("plannedCtgDao")
    protected PlannedCtgDao plannedCtgDao;

	/**
	 * 親子関連情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpmSyComInsOyakoDao")
	protected DpmSyComInsOyakoDao dpmSyComInsOyakoDao;


    public List<InsMstResultDto> search(InsMstScDto scDto) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設情報検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		String sosCd = scDto.getSosCd3();
		if(StringUtils.isNotEmpty(scDto.getSosCd4())) {
		    sosCd = scDto.getSosCd4();
		}
        // 施設からカテゴリ抽出
		List<SosCtg> sosCtgList = dpmCViSosCtgDao.search(sosCd);
		List<PlannedCtg> plannedCtgList = null;
        try {
            plannedCtgList = plannedCtgDao.searchByCtgList(sosCtgList);
        } catch (DataNotFoundException e) {
            throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
        }

        // 親子区分が複数あった場合は、1件目を正とする（暫定対応）
        scDto.setOyakoKb(plannedCtgList.get(0).getOyakoKb());
        scDto.setOyakoKb2(plannedCtgList.get(0).getOyakoKb2());

		// ---------------------------------
		// /親子関連情報のカウント取得
		// ---------------------------------
		int oyakoCount = 0;
		String oyakoKbProdCode = scDto.getProdCode();
		if (StringUtils.isNotEmpty(oyakoKbProdCode)) {
			oyakoCount = dpmSyComInsOyakoDao.searchCount(oyakoKbProdCode);
			if(oyakoCount == 0) {
				//親子関連情報が存在しない
				oyakoKbProdCode = null;
			}
		}
		//親子区分1が'ZZZZZ'の時、セットする親子区分1（品目コード）を設定する。
		scDto.setOyakoKbProdCode(oyakoKbProdCode);

		List<InsMst> insMstList = null;
		try {
			insMstList = insMstRealDao.searchRealList(scDto);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		return convertInsMstScResultDto(insMstList);
	}

	public InsMstResultDto search(String insNo, Integer jgiNo) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		InsMst insMst = null;
		try {
			insMst = insMstRealDao.searchRealIncludeMr(insNo, jgiNo);
		} catch (DataNotFoundException e) {
			insMst = insMstRealDao.searchReal(insNo);
		}
		return convertInsMstScResultDto(insMst);
	}

	public InsMstResultDto search(String insNo) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		InsMst insMst = insMstRealDao.searchReal(insNo);
		return convertInsMstScResultDto(insMst);
	}

	public List<InsMstResultDto> search(List<InsMrDto> insMrDtoList) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insMrDtoList == null) {
			final String errMsg = "施設コード/従業員番号リストがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		List<InsMstResultDto> insMstScResultDtoList = new ArrayList<InsMstResultDto>();
		for (InsMrDto insMrDto : insMrDtoList) {
			insMstScResultDtoList.add(convertInsMstScResultDto(insMstRealDao.searchRealIncludeMr(insMrDto.getInsNo(), insMrDto.getJgiNo())));
		}
		return insMstScResultDtoList;
	}

	/**
	 * InsMstオブジェクトリスト⇒InsMstScResultDtoオブジェクトリスト 変換処理
	 *
	 * @param insMstList 変換元InsMstオブジェクトのリスト
	 * @return InsMstScResultDtoオブジェクトリスト
	 */
	private List<InsMstResultDto> convertInsMstScResultDto(List<InsMst> insMstList) {

		List<InsMstResultDto> insMstScResultDtoList = new ArrayList<InsMstResultDto>();

		// 変換元がNullの場合は配列0のリストを返却
		if (insMstList == null || insMstList.isEmpty()) {
			return insMstScResultDtoList;
		}

		for (InsMst insMst : insMstList) {
			// オブジェクト生成
			insMstScResultDtoList.add(convertInsMstScResultDto(insMst));
		}
		return insMstScResultDtoList;
	}

	/**
	 * InsMstオブジェクト⇒InsMstScResultDtoオブジェクト 変換処理
	 *
	 * @param insMst 変換元InsMstオブジェクト
	 * @return InsMstScResultDtoオブジェクト
	 */
	private InsMstResultDto convertInsMstScResultDto(InsMst insMst) {
		// オブジェクト生成
		return new CreateInsMstScResultDtoLogic(insMst).convert();
	}
}
