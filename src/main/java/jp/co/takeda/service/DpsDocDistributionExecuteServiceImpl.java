package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.SYSTEM_ERROR;

import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.InsDocHaibunDao;
import jp.co.takeda.dto.DistributionMissDto;
import jp.co.takeda.dto.InsDocHaibunDto;
import jp.co.takeda.model.JgiMst;
import jp.co.takeda.model.SosMst;
import jp.co.takeda.security.DpUser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 施設医師別計画・配分実行サービス実装クラス
 *
 * @author nozaki
 */
@Transactional
@Service("dpsDocDistributionExecuteService")
public class DpsDocDistributionExecuteServiceImpl implements DpsDocDistributionExecuteService {

	/**
	 * ロガー
	 */
	protected static final Log LOG = LogFactory.getLog(DpsDocDistributionExecuteServiceImpl.class);

	/**
	 * 施設医師別配分DAO
	 */
	@Autowired(required = true)
	@Qualifier("insDocHaibunDao")
	protected InsDocHaibunDao insDocHaibunDao;

	public List<DistributionMissDto> execute(InsDocHaibunDto insDocHaibunDto) {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (insDocHaibunDto == null) {
			final String errMsg = "施設医師配分パラメータが未設定";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
		}
		SosMst sosMst = insDocHaibunDto.getSosMst();
		if (sosMst == null) {
			final String errMsg = "組織情報が未設定";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
		}
		JgiMst jgiMst = insDocHaibunDto.getJgiMst();
		if (jgiMst == null) {
			final String errMsg = "従業員情報が未設定";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
		}
		DpUser execDpUser = insDocHaibunDto.getExecDpUser();
		if (execDpUser == null) {
			final String errMsg = "配分実行者情報が未設定";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg));
		}
		// ----------------------
		// 配分処理
		// ----------------------
		try {
			return insDocHaibunDao.executeHaibun(insDocHaibunDto);
		} catch (Exception e) {
			final String errMsg = "施設医師別配分処理に失敗";
			throw new SystemException(new Conveyance(SYSTEM_ERROR, errMsg), e);
		}
	}
}
