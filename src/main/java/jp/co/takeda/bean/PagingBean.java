package jp.co.takeda.bean;

import static jp.co.takeda.a.exp.ErrMessageKey.PARAMETER_ERROR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.SystemException;

/**
 * ページングを管理するヘルパークラス<br>
 * 
 * <pre>
 * 当クラスは変更不能(イミュータブル)が保障されており、生成後にデータを変更することが出来ない。
 * コンストラクタで渡されたデータを元にページング情報を生成する。
 * 利用する側は以下の公開メソッドを使ってページング情報にアクセスすることが可能である。
 * <li>現在ページ分のリストを取得する。{@link #getCrntPageData()}</li>
 * <li>1ページ目から現在ページまでのリストを取得する。{@link #getAlreadyDispData()}</li>
 * <li>現在ページ番号を取得する。{@link #getCrntPageNo()}</li>
 * <li>1ページ毎の表示件数を取得する。{@link #getDispCnt()}</li>
 * <li>ページサイズを取得する。{@link #getPageSize()}</li>
 * <li>全データ件数を取得する。{@link #getTotalSize()}</li>
 * <li>前のページが存在するかを取得する。{@link #isBefore()}</li>
 * <li>次のページが存在するかを取得する。{@link #isNext()}</li>
 * <li>前のページ番号を取得する。{@link #getNextPageNo()}</li>
 * <li>次のページ番号を取得する。{@link #getBeforePageNo()}</li>
 * <li>最後のページかを取得する。{@link #isLastPage()}</li>
 * <li>開始位置を取得する。{@link #getStartNo()}</li>
 * <li>終了位置を取得する。{@link #getEndNo()}</li>
 * <li>ページ番号情報を格納したリストを取得する。{@link #getPageList()}</li>
 * ※ 当クラスは画面に表示するページ情報だけではなく、全ページ情報を保持するのでWebアプリケーションでセッション中に当クラスを保持することは避けるべきである。
 * </pre>
 * 
 * @author tkawabata
 * @param <E> ページング対象のリストの要素を表す型
 * @see Page ページ番号を表す静的ネストクラス
 */
public class PagingBean<E> implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6927155413332767594L;

	/**
	 * ページング対象のリスト
	 */
	private final List<E> dataList;

	/**
	 * 1ページ毎の表示件数
	 */
	private final int dispCnt;

	/**
	 * 現在のページ番号
	 */
	private final int crntPageNo;

	/**
	 * ページ件数
	 */
	private final int pageSize;

	/**
	 * 現在ページのデータ(開始)インデックス値
	 */
	private final int firstIndex;

	/**
	 * 現在ページのデータ(終了)インデックス値
	 */
	private final int lastIndex;

	/**
	 * コンストラクタ<br>
	 * 
	 * <pre>
	 * 指定のパラメータを元にページング情報を生成する。
	 * ページング情報の生成は、コンストラクタ内で行う。
	 * 呼び出し側は当オブジェクトを生成し、公開されているメソッドにアクセスすることで、ページングに必要な情報を取得する。
	 * invalidPageNoErrFlgに真が指定されている場合、想定外のページ番号が指定された場合にシステムエラーとする。
	 * invalidPageNoErrFlgに偽が指定されている場合、想定外のページ番号が指定された場合に強制的に「1ページ」にする。
	 * </pre>
	 * 
	 * @param dataList ページング対象のリスト
	 * @param dispCnt 1ページに表示する件数
	 * @param crntPageNo 現在のページ番号
	 * @param invalidPageNoErrFlg 不正なページ番号が指定された場合にエラーとするか
	 */
	public PagingBean(final List<E> dataList, final int dispCnt, final int crntPageNo, final boolean invalidPageNoErrFlg) {

		if (dataList == null) {
			final String errMsg = "ページング対象のコレクションがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		if (dispCnt < 1) {
			final String errMsg = "1ページ当りの表示件数に１未満が指定されている";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		// ページング対象のコレクションの設定
		this.dataList = dataList;

		// 1ページ毎の表示件数の設定
		this.dispCnt = dispCnt;

		// ページ件数の[計算/設定]
		this.pageSize = this.findPageSize(dataList.size(), dispCnt);

		// 想定外のページ番号が指定された場合、強制的に「1ページ」にする。
		if ((crntPageNo < 1) || (crntPageNo > this.pageSize)) {

			if (invalidPageNoErrFlg) {
				final String errMsg = "不正なページ番号が指定されている。crntPageNo=";
				throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg + crntPageNo));
			}
			this.crntPageNo = 1;
		} else {
			this.crntPageNo = crntPageNo;
		}

		// データ開始位置の[計算/設定]
		this.firstIndex = this.findFirstIndex(this.crntPageNo, this.dispCnt);

		// データ終了位置の[計算/設定]
		this.lastIndex = this.findLastIndex(this.crntPageNo, this.dispCnt, this.dataList.size());
	}

	/**
	 * [データ件数]と[1ページあたりの表示件数]から[ページサイズ]を計算する。<br>
	 * 
	 * @param dataCnt データ件数
	 * @param dispCnt 1ページあたりの表示件数
	 * @return int ページサイズ
	 */
	protected int findPageSize(final int dataCnt, final int dispCnt) {

		if (dataCnt == 0) {
			return 0;
		}

		if (dispCnt == 0) {
			final String errMsg = "ページ数生成時に1ページあたりの表示件数が0のためエラー";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		int pageSize = 0;
		int amari = 0;
		pageSize = dataCnt / dispCnt;
		amari = dataCnt % dispCnt;

		if (amari > 0) {
			pageSize += 1;
		}

		return pageSize;
	}

	/**
	 * [ページ番号]と[1ページあたりの表示件数]から[データ取得開始位置]を取得する。<br>
	 * 
	 * @param crntPageNo ページ番号
	 * @param dispCnt 1ページあたりの表示件数
	 * @return int データ取得開始位置
	 */
	protected int findFirstIndex(final int crntPageNo, final int dispCnt) {
		return (crntPageNo * dispCnt - dispCnt);
	}

	/**
	 * [ページ番号]と[1ページあたりの表示件数]と[データ件数]から[データ取得終了始位置]を取得する。<br>
	 * 
	 * @param crntPageNo ページ番号
	 * @param dispCnt 1ページあたりの表示件数
	 * @param dataCnt データ件数
	 * @return int データ取得終了始位置
	 */
	protected int findLastIndex(final int crntPageNo, final int dispCnt, final int dataCnt) {

		int result = 0;
		result = crntPageNo * dispCnt - 1;

		if (result > (dataCnt - 1)) {
			result = dataCnt - 1;
		}

		return result;
	}

	/**
	 * 現在ページのデータをリストで返す。<br>
	 * 
	 * @return java.util.List<E> 現在ページのデータ
	 */
	public List<E> getCrntPageData() {
		/*
		 * javadocから抜粋
		 * List<E> subList(int fromIndex, int toIndex)
		 * このリストの、fromIndex (これを含む) から toIndex (これを含まない) の範囲の部分のビューを返します。
		 * fromIndex と toIndex が等しい場合は、空のリストが返されます。
		 */
		return this.dataList.subList(this.firstIndex, this.lastIndex + 1);
	}

	/**
	 * 1件目～現在ページのデータをリストで返す<br>
	 * 
	 * @return java.util.List<E> 1件目～現在ページのデータ
	 */
	public List<E> getAlreadyDispData() {
		/*
		 * javadocから抜粋
		 * List<E> subList(int fromIndex, int toIndex)
		 * このリストの、fromIndex (これを含む) から toIndex (これを含まない) の範囲の部分のビューを返します。
		 * fromIndex と toIndex が等しい場合は、空のリストが返されます。
		 */
		return this.dataList.subList(0, this.lastIndex + 1);
	}

	/**
	 * 現在ページ番号を取得する。<br>
	 * 
	 * @return int 現在ページ番号
	 */
	public int getCrntPageNo() {
		return this.crntPageNo;
	}

	/**
	 * 1ページ毎の表示件数を取得する。<br>
	 * 
	 * @return int 1ページ毎の表示件数
	 */
	public int getDispCnt() {
		return this.dispCnt;
	}

	/**
	 * ページサイズを取得する。<br>
	 * 
	 * @return int ページサイズ
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * 全データ件数を取得する。<br>
	 * 
	 * @return int 全データ件数
	 */
	public int getTotalSize() {
		return this.dataList.size();
	}

	/**
	 * 前のページが存在するかを取得する。<br>
	 * 
	 * @return boolean 前のページが存在する場合に真
	 */
	public boolean isBefore() {

		boolean result = false;
		if (this.crntPageNo != 1) {
			result = true;
		}
		return result;
	}

	/**
	 * 次のページが存在するかを取得する。<br>
	 * 
	 * @return boolean 次のページが存在する場合に真
	 */
	public boolean isNext() {

		boolean result = false;
		if (this.crntPageNo != this.pageSize) {
			result = true;
		}
		return result;
	}

	/**
	 * [次のページ]が存在する場合に、[次のページ番号]を返す。<br>
	 * 
	 * <pre>[次のページ]が存在しない場合は、nullを返す。</pre>
	 * 
	 * @return java.util.Integer 次のページ番号(存在しない場合はnull値)
	 */
	public Integer getNextPageNo() {

		Integer result = null;
		if (this.isNext()) {
			result = this.crntPageNo + 1;
		}

		return result;
	}

	/**
	 * [前のページ]が存在する場合に、[前のページ番号]を返す。<br>
	 * [前のページ]が存在しない場合は、nullを返す。
	 * 
	 * @return java.util.Integer 前のページ番号(存在しない場合はnull値)
	 */
	public Integer getBeforePageNo() {

		Integer result = null;
		if (this.isBefore()) {
			result = this.crntPageNo - 1;
		}

		return result;
	}

	/**
	 * 最後のページを表示中かを返す。<br>
	 * 
	 * @return boolean 最後のページを表示中ならば真
	 */
	public boolean isLastPage() {

		boolean result = false;
		if (this.crntPageNo == this.pageSize) {
			result = true;
		}
		return result;
	}

	/**
	 * ページング開始位置を取得する。<br>
	 * (ex. 総件数23件の5件表示の2ページ目の場合は10)
	 * 
	 * @return ページング開始位置
	 */
	public int getStartNo() {
		return this.firstIndex + 1;
	}

	/**
	 * ページング終了位置を取得する。<br>
	 * (ex. 総件数23件の5件表示の5ページ目の場合は23)
	 * 
	 * @return ページング終了位置
	 */
	public int getEndNo() {
		return this.lastIndex + 1;
	}

	/**
	 * ページ情報を格納したリストを取得する。<br>
	 * 
	 * @return ページング情報を格納したリスト
	 */
	public List<Page> getPageList() {

		final List<Page> result = new ArrayList<Page>();

		// ページ件数
		final int pageCnt = this.pageSize;

		// カレントページ番号
		final int crntPageNo = this.crntPageNo;

		// ページ件数分だけページ情報生成
		for (int i = 1; i <= pageCnt; i++) {
			final Page page = new Page("" + i);

			// 現在選択中のページ場合
			if (i == crntPageNo) {
				page.setCrntFlg(true);
			}
			// 結果リストに追加
			result.add(page);
		}
		return result;
	}

	/**
	 * ページングのための付加情報を表す静的ネストクラス<br>
	 * ページ番号を管理する。
	 * 
	 * @author tkawabata
	 */
	public static class Page {

		/**
		 * コンストラクタ
		 * 
		 * @param pageNo ページ番号
		 */
		public Page(final String pageNo) {
			this.pageNo = pageNo;
		}

		/**
		 * ページ番号
		 */
		private final String pageNo;

		/**
		 * 現在選択中かを表すフラグ
		 */
		private boolean crntFlg;

		/**
		 * 現在選択中かを表すフラグを取得する。
		 * 
		 * @return 現在選択中かを表すフラグ
		 */
		public boolean isCrntFlg() {
			return this.crntFlg;
		}

		/**
		 * 現在選択中かを表すフラグを設定する。
		 * 
		 * @param crntFlg 現在選択中かを表すフラグ
		 */
		private void setCrntFlg(final boolean crntFlg) {
			this.crntFlg = crntFlg;
		}

		/**
		 * ページ番号を取得する。
		 * 
		 * @return ページ番号
		 */
		public String getPageNo() {
			return this.pageNo;
		}
	}
}
