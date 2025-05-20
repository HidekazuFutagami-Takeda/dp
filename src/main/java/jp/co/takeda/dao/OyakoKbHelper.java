package jp.co.takeda.dao;


import org.apache.commons.lang.StringUtils;

import jp.co.takeda.dao.div.OyakoKb;

class OyakoKbHelper {
	/**
	 * 親子区分として指定すべき値を返す
	 * @param oyakoKb 親子区分
	 * @param oyakoKb2 親子区分2
	 * @param prodCode 品目コード
	 * @return com_ins_oyakoテーブルの検索キー「oyako_kb」に指定される文字列
	 */
	public static String getOyakoKb(String oyakoKb, String oyakoKb2, String prodCode) {

		final String NO_EXISTS_VAL = "xxxxx";

		// 親子区分がとれていなければ、無効となるキーを返す。
		if(StringUtils.isBlank(oyakoKb)){
			return NO_EXISTS_VAL;// 存在し得ないコード
		}
		// 親子区分がZZZZZ(品目コードを検索キーとする）でない場合は、親子区分を設定し、検索キーとする
		if(!oyakoKb.equals(OyakoKb.EACH_PROD.getValue())) {
			return oyakoKb;
		}
		// 親子区分がZZZZZであれば、品目コードを返す。
		if(StringUtils.isNotBlank(prodCode)){
			return prodCode;
		}
		//親子区分がZZZZZであっても、品目コードがない場合は、
		//親子区分2(品目コードが取れない場合のデフォルト親子区分）を返す
		if(StringUtils.isNotBlank(oyakoKb2)){
			return oyakoKb2;
		}
		// 上記条件以外ならば、無効となるキーを返す
		return NO_EXISTS_VAL;// 存在し得ないコード
	}


}
