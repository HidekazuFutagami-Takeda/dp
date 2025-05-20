package jp.co.takeda.web.cmn.velocity;

import java.util.List;

import jp.co.takeda.bean.PagingBean;

/**
 * Pagingを行うためのVelocityTool
 * 
 * @author tkawabata
 */
public class PagingTool {

	/**
	 * Paging情報を返す。
	 * 
	 * @param <E> 任意の型
	 * @param dataList データリスト
	 * @param dispCnt データ件数
	 * @param crntPageNo ページ番号
	 * @return Paging情報
	 */
	public <E> PagingBean<E> getPagingBean(final List<E> dataList, final int dispCnt, final int crntPageNo) {
		return new PagingBean<E>(dataList, dispCnt, crntPageNo, false);
	}
}
