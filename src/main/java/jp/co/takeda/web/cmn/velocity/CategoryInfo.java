package jp.co.takeda.web.cmn.velocity;

public class CategoryInfo {
	/*
	 * カテゴリ番号
	 */
	final private String dbValue;
	/*
	 * カテゴリ名称
	 */
	final private String prodCategoryName;
	/**
	 * @return dbValue
	 */
	public String getDbValue() {
		return dbValue;
	}
	/**
	 * @return prodCategoryName
	 */
	public String getProdCategoryName() {
		return prodCategoryName;
	}

	public CategoryInfo(String dbValue, String prodCategoryName) {
		super();
		this.dbValue = dbValue;
		this.prodCategoryName = prodCategoryName;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CategoryInfo [dbValue=" + dbValue + ", prodCategoryName=" + prodCategoryName + "]";
	}



}
