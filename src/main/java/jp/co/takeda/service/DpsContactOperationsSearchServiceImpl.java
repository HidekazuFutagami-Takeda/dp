package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.DataNotFoundException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.dao.AnnounceDao;
import jp.co.takeda.dao.OutputFileDao;
import jp.co.takeda.dto.AnnounceDto;
import jp.co.takeda.model.Announce;
import jp.co.takeda.model.OutputFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 業務連絡情報を取得するサービス実装クラス
 * 
 * @author tkawabata
 */
@Transactional
@Service("dpsContactOperationsSearchService")
public class DpsContactOperationsSearchServiceImpl implements DpsContactOperationsSearchService {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(DpsContactOperationsSearchServiceImpl.class);

	/**
	 * お知らせ情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("announceDao")
	protected AnnounceDao announceDao;

	/**
	 * 出力ファイル情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("outputFileDao")
	protected OutputFileDao outputFileDao;

	public List<AnnounceDto> searchAnnounceList(Integer jgiNo) throws DataNotFoundException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (jgiNo == null) {
			final String errMsg = "従業員番号がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		List<Announce> announceList = announceDao.searchByJgiNo(AnnounceDao.SORT_STRING, jgiNo);
		List<AnnounceDto> resultList = new ArrayList<AnnounceDto>(announceList.size());
		for (Announce announce : announceList) {
			if (announce.getOutputFileId() == null) {
				resultList.add(new AnnounceDto(announce, null));
			} else {
				try {
					OutputFile outputFile = outputFileDao.search(announce.getOutputFileId());
					resultList.add(new AnnounceDto(announce, outputFile));
				} catch (DataNotFoundException e) {
					if (LOG.isErrorEnabled()) {
						// データ不整合でお知らせの取得を取りやめるとお知らせが見られなくなるのでログをはいて処理を継続する
						final String errMsg = "お知らせ情報に出力ファイルが指定されているのに出力ファイル情報が取得出来ない";
						LOG.error(errMsg, e);
					}
				}
			}
		}
		return resultList;
	}
}
