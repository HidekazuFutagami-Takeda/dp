package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;

/**
 *
 * (医)施設特約店別計画編集画面を表すDTO
 *
 * @author siwamoto
 */
public class ManageInsWsPlanDetailInsDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 施設名称
	 */
	private final String insName;

	/**
	 * 施設固定コード
	 */
	private final String insNo;

	/**
	 * 特約店リスト(表示する特約店明細)
	 */
	private final List<ManageInsWsPlanDetailWsDto> wsList;

	/**
	 * 特約店コードリスト(登録されている特約店明細の特約店コード)
	 */
	private final List<String> tmsTytenCdList;

	/**
	 * 削除予定施設フラグ
	 */
	private final boolean delInsFlg;

	/**
	 * 編集可能フラグ
	 * <ul>
	 * <li>TRUE = 編集可能</li>
	 * <li>FALSE = 編集可能でない</li>
	 * </ul>
	 */
	private final boolean enableEdit;

	/**
	 * コンストラクタ
	 *
	 * @param insName 施設名称
	 * @param insNo 施設固定コード
	 * @param wsList 特約店リスト(表示する特約店明細)
	 * @param tmsTytenCdList 特約店コードリスト(登録されている特約店明細の特約店コード)
	 * @param delInsFlg 削除予定施設フラグ
	 * @param enableEdit 編集可能フラグ
	 */
	public ManageInsWsPlanDetailInsDto(String insName, String insNo, List<ManageInsWsPlanDetailWsDto> wsList, List<String> tmsTytenCdList, boolean delInsFlg, boolean enableEdit) {
		this.insName = insName;
		this.insNo = insNo;
		this.wsList = wsList;
		this.tmsTytenCdList = tmsTytenCdList;
		this.delInsFlg = delInsFlg;
		this.enableEdit = enableEdit;
	}

	/**
	 * 施設名称を取得する。
	 *
	 * @return 施設名称
	 */
	public String getInsName() {
		return insName;
	}

	/**
	 * 施設固定コードを取得する。
	 *
	 * @return 施設固定コード
	 */
	public String getInsNo() {
		return insNo;
	}

	/**
	 * 特約店リストを取得する。
	 *
	 * @return 特約店リスト
	 */
	public List<ManageInsWsPlanDetailWsDto> getWsList() {
		return wsList;
	}

	/**
	 * 特約店コードリストを取得する。
	 *
	 * @return 特約店コードリスト
	 */
	public List<String> getTmsTytenCdList() {
		return tmsTytenCdList;
	}

	/**
	 * 削除予定施設フラグを取得する。
	 *
	 * @return 削除予定施設フラグ
	 */
	public boolean getDelInsFlg() {
		return delInsFlg;
	}

	/**
	 * 編集可能フラグ を取得する。
	 *
	 * @return 編集可能フラグ
	 */
	public boolean getEnableEdit() {
		return enableEdit;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
