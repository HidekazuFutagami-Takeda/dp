package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.ArrayList;
import java.util.List;

// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
import org.apache.commons.lang.StringUtils;
// add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.bean.MessageKey;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.DpsPlannedCtgDao;
import jp.co.takeda.dao.InsMstDAO;
import jp.co.takeda.dto.DpsInsMstScDto;
import jp.co.takeda.dto.InsMstResultDto;
import jp.co.takeda.logic.DpsCreateInsMstScResultDtoLogic;
import jp.co.takeda.model.DpsInsMst;
import jp.co.takeda.model.DpsPlannedCtg;
// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
import jp.co.takeda.model.JgiMst;
// add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

/**
 * 施設検索サービス実装クラス
 *
 * @author khashimoto
 */
@Transactional
@Service("dpsInsMstSearchService")
public class DpsInsMstSearchServiceImpl implements DpsInsMstSearchService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstDAO")
	protected InsMstDAO insMstDAO;

	/**
	 * 計画対象カテゴリ領域DAO
	 */
	@Autowired(required = true)
	@Qualifier("dpsPlannedCtgDao")
	protected DpsPlannedCtgDao dpsPlannedCtgDao;

	public InsMstResultDto search(String insNo, Integer jgiNo, String prodCode, String category) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		plannedCtg = dpsPlannedCtgDao.search(category);
// mod Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
		List<DpsInsMst> insMstList = null;

//		DpsInsMst insMst = insMstDAO.search(insNo, jgiNo, prodCode, plannedCtg.getOyakoKb(),
		insMstList = insMstDAO.search(insNo, jgiNo, prodCode, plannedCtg.getOyakoKb(),
//			plannedCtg.getOyakoKb2(), category);
			plannedCtg.getOyakoKb2(), category);

		List<JgiMst> tantoList = new ArrayList<JgiMst>();
		JgiMst tanto = new JgiMst();
		DpsInsMst outInsMst = new DpsInsMst();

		//prodCodeがある場合は、tantolistが無いので、エラー回避の為分岐
		if(StringUtils.isEmpty(prodCode)) {
			//施設番号ありなのでするので1施設のみ設定される
			for (DpsInsMst insMst : insMstList) {
				if(noTargetCheck(insMst)) {
					tanto = new JgiMst();
					tanto.setJgiNo(insMst.getListJgiNo());
					tanto.setJgiName(insMst.getListJgiName());
					tanto.setShokushuName(insMst.getListShokushuName());
					tantoList.add(tanto);
					//forを抜けるとフォーカスが外れるので出力対象に詰めておく
					outInsMst = insMst;
				}
			}
			//集約したtantolisutを出力対象に設定する
			outInsMst.setTantoList(tantoList);
		}else {
			for (DpsInsMst insMst : insMstList) {
				outInsMst = insMst;
			}
		}
		//出力対象外になるので、for終了後に出力対象となるように設定する
//		return convertInsMstScResultDto(insMst);
		return convertInsMstScResultDto(outInsMst);

// mod End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
	}

	public List<InsMstResultDto> search(DpsInsMstScDto scDto, String category) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "施設情報検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// -----------------------------
		// 計画対象カテゴリ領域取得
		// -----------------------------
		DpsPlannedCtg plannedCtg = new DpsPlannedCtg();
		plannedCtg = dpsPlannedCtgDao.search(category);

		List<DpsInsMst> insMstList = null;
		try {
			insMstList = insMstDAO.searchList(InsMstDAO.SORT_STRING2, scDto, plannedCtg.getOyakoKb(),
					plannedCtg.getOyakoKb2(), category);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}
// mod Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

		if((StringUtils.isEmpty(scDto.getProdCode()))) {
			return convertInsMstScResultTantoDto(insMstList);
		}else {
//		return convertInsMstScResultDto(insMstList);
			return convertInsMstScResultDto(insMstList);
		}
// mod End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

	}

	public List<InsMstResultDto> searchFamilyIns(String insNo, Integer jgiNo, String prodCode) throws LogicalException {
		// ----------------------
		// 引数チェック
		// ----------------------
		if (insNo == null) {
			final String errMsg = "施設コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodCode == null) {
			final String errMsg = "品目コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		List<DpsInsMst> insMstList = null;
		try {
			insMstList = insMstDAO.searchFamilyInsList(insNo, jgiNo, prodCode);
		} catch (DataNotFoundException e) {
			throw new LogicalException(new Conveyance(new MessageKey("DPC1023E")), e);
		}

		return convertInsMstScResultDto(insMstList);
	}

	/**
	 * InsMstオブジェクトリスト⇒InsMstScResultDtoオブジェクトリスト 変換処理
	 *
	 * @param insMstList 変換元InsMstオブジェクトのリスト
	 * @return InsMstScResultDtoオブジェクトリスト
	 */
	private List<InsMstResultDto> convertInsMstScResultDto(List<DpsInsMst> insMstList) {

		List<InsMstResultDto> insMstScResultDtoList = new ArrayList<InsMstResultDto>();

		// 変換元がNullの場合は配列0のリストを返却
		if (insMstList == null || insMstList.isEmpty()) {
			return insMstScResultDtoList;
		}

		for (DpsInsMst insMst : insMstList) {
			// オブジェクト生成
			insMstScResultDtoList.add(convertInsMstScResultDto(insMst));
		}
		return insMstScResultDtoList;
	}

// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
	private List<InsMstResultDto> convertInsMstScResultTantoDto(List<DpsInsMst> insMstList) {

		List<InsMstResultDto> insMstScResultDtoList = new ArrayList<InsMstResultDto>();

		// 変換元がNullの場合は配列0のリストを返却
		if (insMstList == null || insMstList.isEmpty()) {
			return insMstScResultDtoList;
		} else {

			String insBackChek = null;
			List<JgiMst> tantoList = new ArrayList<JgiMst>();
			JgiMst tanto = new JgiMst();
			DpsInsMst outInsMst = new DpsInsMst();

			for (DpsInsMst insMst : insMstList) {
				//汎用化の為、１レコードから複数レコードになったので施設毎に集約する
				//SQLは汎用化を行ったが組織毎に行われる判定はできない為、javaで明示的に制御をする
				if(StringUtils.isEmpty(insMst.getListJgiKb())){
					insMstScResultDtoList.add(convertInsMstScResultDto(insMst));
				}
				else {
					if(noTargetCheck(insMst)) {
					//１件前と施設が異なるなら、Listと合わせて出力対象にする
						if(insBackChek == null || insBackChek.equals(insMst.getInsNo())) {
						} else {
							outInsMst.setTantoList(tantoList);
							insMstScResultDtoList.add(convertInsMstScResultDto(outInsMst));
							tantoList = new ArrayList<JgiMst>();
							outInsMst = new DpsInsMst();
						}
						//同じ施設の担当者を、List用をListに累積する
						tanto.setJgiNo(insMst.getListJgiNo());
						tanto.setJgiName(insMst.getListJgiName());
						tanto.setShokushuName(insMst.getListShokushuName());
						tantoList.add(tanto);
						tanto = new JgiMst();
						//比較用に施設番号を記録
						insBackChek = insMst.getInsNo();
						//出力対象を記憶
						outInsMst = insMst;
					}
				}
			}
			//最後の１件は出力対象外になるので、for終了後に出力対象となるように設定する
			outInsMst.setTantoList(tantoList);
			insMstScResultDtoList.add(convertInsMstScResultDto(outInsMst));
		}
		return insMstScResultDtoList;
	}
// add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応

// add Start 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応
	//出力しない判定を記載
	private boolean noTargetCheck(DpsInsMst insMst) {
		if(insMst.getListJgiKb().equals("8") && (insMst.getListMrCat().equals("05")||insMst.getListMrCat().equals("03")||insMst.getListMrCat().equals("12"))) {
			return false;
		}
		return true;
	}
// add End 2022/06/30 R.takamoto 施設選択画面の担当者にNSBU、RDBUが含まれない対応


	/**
	 * InsMstオブジェクト⇒InsMstScResultDtoオブジェクト 変換処理
	 *
	 * @param insMst 変換元InsMstオブジェクト
	 * @return InsMstScResultDtoオブジェクト
	 */
	private InsMstResultDto convertInsMstScResultDto(DpsInsMst insMst) {
		// オブジェクト生成
		return new DpsCreateInsMstScResultDtoLogic(insMst).convert();
	}
}
