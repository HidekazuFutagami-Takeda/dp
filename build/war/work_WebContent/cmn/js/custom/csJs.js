/**
 * [特約店コード]と[集約方法]の関係性検証
 *
 * @return 検証ＯＫの場合に真を返す
 */
function checkTmsTytenCdAndKisLevel(tmsTytenCdPart, tytenKisLevel) {

	if (!tmsTytenCdPart || !tytenKisLevel) {
		return true;
	}

	// 特約店コード桁数
	var entry = tmsTytenCdPart.length;
	var isKisLevelErrFlg = false;

	// 3桁入力
	if (entry <= 3) {
		return true;
	}

	// 5桁入力
	else if (entry <= 5) {
		if (tytenKisLevel == "01") {
			return false;
		}
	}

	// 7桁入力
	else if (entry <= 7) {
		if (tytenKisLevel == "01" || tytenKisLevel == "02") {
			return false;
		}
	}

	// 9桁入力
	else if (entry <= 9) {
		if (tytenKisLevel == "01" || tytenKisLevel == "02" || tytenKisLevel == "03") {
			return false;
		}
	}

	// 11桁入力
	else if (entry <= 11) {
		if (tytenKisLevel == "01" || tytenKisLevel == "02" || tytenKisLevel == "03" || tytenKisLevel == "04") {
			return false;
		}
	}

	// 13桁入力
	else if (entry <= 13) {
		if (tytenKisLevel == "01" || tytenKisLevel == "02" || tytenKisLevel == "03" || tytenKisLevel == "04" || tytenKisLevel == "05") {
			return false;
		}
	}

	return true;
}

/**
 * 末尾のゼロと空白を除去する。 末尾から走査し、ゼロと空白以外の文字列が出た時点で処理を中止する。
 * ただし奇数桁(例：3/5/7/9/11/13桁)が入力された場合、左隣の偶数桁(例:2/4/6/8/10/12桁)が '0'の場合のみゼロと空白を除去する。
 *
 * @param 対象文字列
 * @return 末尾のゼロと空白を除去した文字列
 */
function removeZeroBlankFromEnd(entry) {
	if (entry == null || entry == "") {
		return entry;
	}
	var target = entry;
	var idx = entry.length-1;
	while (idx > 0) {
		testString1 = entry.charAt(idx);
		if (idx != 0) {
		testString2 = entry.charAt(idx-1);
		}
		if (testString1 != 0 && testString1 != " ") {
			return target;
		} else if (idx % 2 == 0) {
			if (testString2 == 0) {
				target = target.substring(0, idx);
			}
		} else {
			target = target.substring(0, idx);
		}
		idx = idx - 1;
	}
	return "";
}

/**
 * モーダルダイアログを開く。
 *
 * @param url URL
 * @param func モーダルウィンドウから実行される関数
 * @param args モーダルウィンドウに渡される引数
 * @return false
 */
function openModalWindow(url, func, args) {
	var width = 990;
	if(screen.availWidth > 990){
		width = screen.availWidth;
	}
	var height = dialogHeight=screen.availHeight - 40;
	window.open(url,'window',"width=" + width +"px,height=" + height +"px");
	return false;
}
/**
 * ダイアログを開く。
 *
 * @param url URL
 * @return ウィンドウオブジェクト
 */
function openWindow(url) {
	var width = 990;
	if(screen.availWidth > 990){
		width = screen.availWidth;
	}
	var height = screen.availHeight - 40;
	return window.open(url,'window',"width=" + width +"px,height=" + height +"px");
}
/**
 * モーダルダイアログを開く。検索系ダイアログ用の幅。
 *
 * @param url URL
 * @return モーダルウィンドウ
 */
function openModalWindowW(url) {
	window.open(url,'_blank',"width=750px,height=680px");
	return false;
}

/**
 * モーダルダイアログを開く。検索系ダイアログ用の幅。
 *
 * @param url URL
 * @return モーダルウィンドウ
 */
function openModalWindowW2(url,w,h) {
	window.open(url,'window',"width=" + w +"px,height=" + h +"px");
	return false;
}

/**
 * モーダルダイアログを開く。検索系ダイアログ用の幅。
 *
 * @param url URL
 * @return モーダルウィンドウ
 */
function openModalWindowW3(url, windowname, func, args) {
	window.open(url,windowname,"width=750px,height=650px");
	return false;
}

/**
 * モーダルダイアログの親画面からの引数を取得する。 (ブラウザ判定はしない)
 *
 * @return dialogArguments
 */
function dialogArgs(){
	if (window.dialogArguments) {
		return window.dialogArguments;
	} else if (window.opener) {
		return window.opener;
	} else {
		return null;
	}
}

/**
 * 配列に格納されたメッセージをメッセージエリアに表示する。
 *
 * @param msgs メッセージが格納された配列
 * @return false
 */
var msgText = "";
function displayMessage(msgs) {
	if (msgText != "") {
		// 処理をしている様に見せるためメッセージを一定期間非表示とする
		var blank = "";
		for (i=0; i < msgs.length; i++) {
			blank = blank.concat("<span class='errorMessage'>　</span><br>");
		}
		document.getElementById("messageArea").innerHTML = blank;
	}
	msgText = "";
	if (msgs.length > 5) { // メッセージが5件以上の場合にスクロールバー表示
		msgText = msgText.concat('<div style="height:98px; overflow-y:scroll;">');
	}
	msgText = msgText.concat('<table>');
	for (i=0; i < msgs.length; i++) {
		msgText = msgText.concat('<tr><td class="errorMessage">');
		if (msgs[i] != null) {
			msgText = msgText.concat(msgs[i]);
		}
		msgText = msgText.concat('</tr></td>');
	}
	msgText = msgText.concat('</table>');
	if (msgs.length > 5) { // メッセージが5件以上の場合にスクロールバー表示(閉じタグ)
		msgText = msgText.concat('</div>');
	}
	setTimeout("document.getElementById('messageArea').innerHTML = msgText", 100);
	return false;
}

/**
 * 組織従業員のラベル・パラメータを書き換える。
 *
 * @param sosCd 組織コード
 * @param jgiNo 従業員番号
 * @param sosMaxSrchGetValue 最大検索範囲
 * @param etcSosFlg 雑組織フラグ
 * @return false
 */
function sosApply(sosCd, jgiNo, sosMaxSrchGetValue, etcSosFlg) {
	return sosApplyExt(sosCd, jgiNo, sosMaxSrchGetValue, true, etcSosFlg);
}
/**
 * 組織従業員のラベル・パラメータを書き換える。
 *
 * @param sosCd 組織コード
 * @param jgiNo 従業員番号
 * @param sosMaxSrchGetValue 最大検索範囲
 * @param defaultChangeFlg デフォルト組織変更フラグ
 * @param etcSosFlg 雑組織フラグ
 * @return false
 */
function sosApplyExt(sosCd, jgiNo, sosMaxSrchGetValue, defaultChangeFlg, etcSosFlg) {
	var obj = getDetailSosInfo(sosCd, jgiNo, sosMaxSrchGetValue, defaultChangeFlg, etcSosFlg);
	return sosApplyExecute(obj);
}
/**
 * 組織従業員のラベル・パラメータを書き換える（整形を含む）。
 *
 * @param sosCd 組織コード
 * @param jgiNo 従業員番号
 * @param sosMaxSrchGetValue 最大検索範囲
 * @param defaultChangeFlg デフォルト組織変更フラグ
 * @param etcSosFlg 雑組織フラグ
 * @return false
 */
function sosApplySeikeiExt(sosCd, jgiNo, sosMaxSrchGetValue, defaultChangeFlg, etcSosFlg) {
	var obj = getDetailSosInfo_Seikei(sosCd, jgiNo, sosMaxSrchGetValue, defaultChangeFlg, etcSosFlg);
	return sosApplyExecute(obj);
}

/**
 * 組織従業員のラベル・パラメータを書き換える。
 *
 * @param sosCd 組織コード
 * @param jgiNo 従業員番号
 * @param sosMaxSrchGetValue 最大検索範囲
 * @param defaultChangeFlg デフォルト組織変更フラグ
 * @param etcSosFlg 雑組織フラグ
 * @return false
 */
function sosApplyExecute(obj) {
	var labelTxt = document.getElementById("sos");
	if (obj.name) {
		labelTxt.childNodes[0].nodeValue = obj.name;
	}
	var sosCd2 = document.getElementById('sosCd2');
	sosCd2.value = obj.sosCd2;
	var sosCd3 = document.getElementById('sosCd3');
	sosCd3.value = obj.sosCd3;
	var sosCd4 = document.getElementById('sosCd4');
	sosCd4.value = obj.sosCd4;
	var jgiNo = document.getElementById('jgiNo');
	jgiNo.value = obj.jgiNo;
	var initSosCodeValue = document.getElementById('initSosCodeValue');
	initSosCodeValue.value = obj.initSosCodeValue;
	if(document.getElementById('brCode')){
		var brCode = document.getElementById('brCode');
		brCode.value = obj.brCode;
	}
	if(document.getElementById('distCode')){
		var distCode = document.getElementById('distCode');
		distCode.value = obj.distCode;
	}
	if(document.getElementById('etcSosFlg')){
		var etcFlg = document.getElementById('etcSosFlg');
		etcFlg.value = obj.etcSosFlg;
	}
	if(document.getElementById('oncSosFlg')){
		var oncFlg = document.getElementById('oncSosFlg');
		oncFlg.value = obj.oncSosFlg;
	}
	if(document.getElementById('sosCategory')){
		var sosCategory = document.getElementById('sosCategory');
		sosCategory.value = obj.sosCategory;
	}
	if(document.getElementById('sosSubCategory')
	&& obj.sosSubCategory !== undefined){		// 計画管理側ではSubCategoryは存在しない
		var sosSubCategory = document.getElementById('sosSubCategory');
		sosSubCategory.value = obj.sosSubCategory;
	}
	if(document.getElementById('underSosCnt')
	&& obj.underSosCnt !== undefined){
		var underSosCnt = document.getElementById('underSosCnt');
		underSosCnt.value = obj.underSosCnt;
	}
	return false;
}

/**
 * 同期通信を行い、データを取得する。
 *
 * @param pageURL リクエスト先のURL
 * @return xmlhttp.responseText
 */
function getPage(pageURL) {
	try {
		xmlhttp = getXMLHttpRequest();
		if (xmlhttp) {
			xmlhttp.open('GET', pageURL, false);
			xmlhttp.send(null);
			return xmlhttp.responseText;
		} else {
			return null;
		}
	} catch (e) {
		return null;
	}
}

/**
 * XMLHttpRequestを取得する。
 *
 * @return XMLHttpRequest
 */
function getXMLHttpRequest() {
	var xmlhttp = false;
	try {
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	} catch (e) {
		xmlhttp = false;
	}
	if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
		try {
			xmlhttp = new XMLHttpRequest();
		} catch (e) {
			xmlhttp = false;
		}
	}
	return xmlhttp;
}

/**
 * setSum(grid,columnId)の機能に加え他グリッドの集計行に値をセットする。
 * 尚、rowTypeが「subTotal」のものは明細行グリッド側にあるものとし、 「total」のみが集計行グリッドにあることを前提とする。
 *
 * @param detailGrid 明細行のグリッド
 * @param sumGrid 集計行のグリッド
 * @param columnId 対象の列
 */
function setSumOtherGrid(detailGrid,sumGrid,columnId){
	var map = new Array(); // サブ合計MAP(subTotalIdごとに合計)
	// 入力行のデータを、サブグループごとに合計
	for (rowId = 1; rowId <= detailGrid.getRowsNum(); rowId++) {
		var rowType = detailGrid.getUserData(rowId, 'rowType');
		if(rowType == "input"){
			var subTotalId = detailGrid.getUserData(rowId, 'subTotalId');
			var value = detailGrid.cells(rowId,columnId).getValue();
			if(value == null || value.toString() == ""){
				continue;
			}
			if(map[subTotalId] != null && map[subTotalId] !=""){
				map[subTotalId] = map[subTotalId] + convertToInteger(detailGrid,rowId,columnId);
			} else {
				map[subTotalId] = convertToInteger(detailGrid,rowId,columnId);
			}
			minusStyle(detailGrid,rowId, columnId);
		}
	}
	// サブ合計行に設定
	for (rowId = 1; rowId <= detailGrid.getRowsNum(); rowId++) {
		var rowType = detailGrid.getUserData(rowId, 'rowType');
		if(rowType == "subTotal"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(detailGrid.cells(rowId, columnId).getAttribute("sum") != "false"){
				var subTotalId = detailGrid.getUserData(rowId, 'subTotalId');
				detailGrid.cells(rowId, columnId).setValue(map[subTotalId]);
				minusStyle(detailGrid,rowId, columnId);
			}
		}
	}
	// 全合計取得
	var allSum = 0;
	for (var k in map) {
		allSum = allSum + map[k];
	}
	if(map.length == 0){
		allSum = "";
	}
	// 全合計行に設定
	for (rowId = 1; rowId <= sumGrid.getRowsNum(); rowId++) {
		var rowType = sumGrid.getUserData(rowId, 'rowType');
		if(rowType == "total"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(sumGrid.cells(rowId, columnId).getAttribute("sum") != "false"){
				sumGrid.cells(rowId, columnId).setValue(allSum);
				minusStyle(sumGrid,rowId, columnId);
			}
		}
	}
}

/**
 * 他グリッドの集計行に明細行の総計値をセットする。
 *
 * @param detailGrid 明細行のグリッド
 * @param sumGrid 集計行のグリッド
 * @param columnId 対象の列
 */
function setAllRowSumOtherGrid(detailGrid,sumGrid,columnId){
	var sum = 0;
	var sumFlg = false;
	detailGrid.forEachRow(function(id){
		sum = sum + convertToInteger(detailGrid,id,columnId);
		minusStyle(detailGrid,id, columnId);
		var detailValue = detailGrid.cells(id,columnId).getValue();
		if(detailValue != null && detailValue != ""){
			sumFlg = true;
		} else if (detailValue == "0") {
			sumFlg = true;
		}
	});
	if(!sumFlg){
		sum = "";
	}
	sumGrid.cells("1",columnId).setValue(sum);
	minusStyle(sumGrid,"1", columnId);
}

/**
 * 任意の2列の合計値を任意の列にセットする。
 *
 * @param grid 計算対象のグリッド
 * @param rowId 計算対象のRowId
 * @param col1 計算対象の列1
 * @param col2 計算対象の列2
 * @param col3 計算結果をセットする列
 */
function doCalcSumCol(grid, rowId ,col1, col2, col3){
	sumValue = null;
	value1 = grid.cells(rowId,col1).getValue();
	value2 = grid.cells(rowId,col2).getValue();
	if(value1 != null && value1.toString() != ""){
		sumValue = convertToInteger(grid,rowId,col1);
	}
	if(value2 != null && value2.toString() != ""){
		sumValue = sumValue + convertToInteger(grid,rowId,col2);
	}
	cell = grid.cells(rowId, col3);
	if (sumValue != null) {
		cell.setValue(sumValue);
		minusStyle(grid,rowId, col3);
	}
	else {
		cell.setValue(null);
	}
}

/**
 * 任意の3列の合計値を任意の列にセットする。
 *
 * @param grid 計算対象のグリッド
 * @param rowId 計算対象のRowId
 * @param col1 計算対象の列1
 * @param col2 計算対象の列2
 * @param col3 計算対象の列2
 * @param col4 計算結果をセットする列
 */
function doCalcSumColExt(grid, rowId ,col1, col2, col3, col4){
	sumValue = null;
	value1 = grid.cells(rowId,col1).getValue();
	value2 = grid.cells(rowId,col2).getValue();
	value3 = grid.cells(rowId,col3).getValue();
	if(value1 != null && value1.toString() != ""){
		sumValue = convertToInteger(grid,rowId,col1);
	}
	if(value2 != null && value2.toString() != ""){
		sumValue = sumValue + convertToInteger(grid,rowId,col2);
	}
	if(value3 != null && value3.toString() != ""){
		sumValue = sumValue + convertToInteger(grid,rowId,col3);
	}
	cell = grid.cells(rowId, col4);
	if (sumValue != null) {
		cell.setValue(sumValue);
		minusStyle(grid,rowId, col4);
	}
	else {
		cell.setValue(null);
	}
}

/**
 * T/Y変換率からT価ベースを算出する。
 *
 * @param grid 対象グリッド
 * @param yRowId Y価ベース行インデックス(計算元セル)
 * @param yColId Y価ベース列インデックス(計算元セル)
 * @param tRowId T価ベース行インデックス(挿入セル)
 * @param tColId T価ベース列インデックス(挿入セル)
 * @param tyChangeRate T/Y変換率
 * @return
 */
function calcTBaseValue(grid, yRowId, yColId, tRowId, tColId, tyChangeRate) {
	var tBaseValue = null;
	var yBaseValue = grid.cells(yRowId,yColId).getValue();
	if(yBaseValue != null && yBaseValue.toString() != ""){
		var iYBaseValue = convertToInteger(grid,yRowId,yColId);
		if(tyChangeRate != null && tyChangeRate.toString() != ""){
			tBaseValue = iYBaseValue * tyChangeRate / 100;
		}
	}
	var cell = grid.cells(tRowId, tColId);
	if (tBaseValue != null) {
		cell.setValue(tBaseValue);
		minusStyle(grid,tRowId, tColId);
		minusStyle(grid,yRowId, yColId);
	} else {
		cell.setValue(null);
	}
}

/**
 * T/Y変換率からY価ベースを算出する。
 *
 * @param grid 対象グリッド
 * @param tRowId T価ベース行インデックス(計算元セル)
 * @param tColId T価ベース列インデックス(計算元セル)
 * @param yRowId Y価ベース行インデックス(挿入セル)
 * @param yColId Y価ベース列インデックス(挿入セル)
 * @param tyChangeRate T/Y変換率
 * @return
 */
function calcYBaseValue(grid, tRowId, tColId, yRowId, yColId, tyChangeRate) {
	var yBaseValue = null;
	var tBaseValue = grid.cells(tRowId,tColId).getValue();
	if(tBaseValue != null && tBaseValue.toString() != ""){
		var itBaseValue = convertToInteger(grid,tRowId,tColId);
		if(tyChangeRate != null && tyChangeRate.toString() != ""){
			yBaseValue = itBaseValue / tyChangeRate * 100;
		}
	}
	var cell = grid.cells(yRowId, yColId);
	if (yBaseValue != null) {
		cell.setValue(yBaseValue);
		minusStyle(grid,tRowId, tColId);
		minusStyle(grid,yRowId, yColId);
	} else {
		cell.setValue(null);
	}
}
/**
 * T/Y変換率からY価ベースを算出する。(グリッドへの表示を行わない)
 *
 * @param tBaseValue T価ベース
 * @param tyChangeRate T/Y変換率
 * @return Y価ベース
 */
function getYBaseValue(tBaseValue, tyChangeRate) {
	var yBaseValue = 0;
	if(tBaseValue != null && tBaseValue.toString() != "" && tyChangeRate != null && tyChangeRate.toString() != ""){
		yBaseValue = tBaseValue / tyChangeRate * 100;
		yBaseValue = Math.round(yBaseValue);
	}
	return yBaseValue;
}
/**
 * 入力行に対応する中計行のIdを返す。(明細の下に中計行がある場合)
 *
 * @param grid 対象グリッド
 * @param rowId 検索始点の行インデックス
 * @return 中計行の行インデックス
 */
function getSubTotalIdDown(grid,rowId){
	var subTotalId = "";
	var i = grid.getRowIndex(rowId);
	while(grid.getUserData(grid.getRowId(i),"rowType") != "subTotal"){
		subTotalId = i;
		i++;
	}
	return grid.getRowId(++subTotalId);
}

var Class = {
	create: function() {
		return function() {
			this.initialize.apply(this, arguments);
		}
	}
}

function getValue(s){return document.getElementById(s).value}

var Validate = Class.create();
Validate.prototype = {
	/*--------------------------------------------------------------------------*/
	initialize:function(){
		this.error_array = []
		this.rules_array = [];
		this.e = true;
	},
	/*--------------------------------------------------------------------------*/
	isEqual:function(string1, string2){
		if(string1 == string2) return true;
		else return false;
	},
	/*--------------------------------------------------------------------------*/
	hasValidChars:function(s, characters, caseSensitive){
		function escapeSpecials(s){
			return s.replace(new RegExp("([\\\\-])", "g"), "\\$1");
		}
		return new RegExp("^[" + escapeSpecials(characters) + "]+$",(!caseSensitive ? "i" : "")).test(s);
	},
	/*--------------------------------------------------------------------------*/
	isSimpleIP:function(ip){
		ipRegExp = /^(([0-2]*[0-9]+[0-9]+)\.([0-2]*[0-9]+[0-9]+)\.([0-2]*[0-9]+[0-9]+)\.([0-2]*[0-9]+[0-9]+))$/
		return ipRegExp.test(ip);
	},
	/*--------------------------------------------------------------------------*/
	isAlphaLatin:function(string){
		alphaRegExp = /^[0-9a-z]+$/i
		return alphaRegExp.test(string);
	},
	/*--------------------------------------------------------------------------*/
	isNotEmpty:function (string){
		return /\S/.test(string);
	},
	/*--------------------------------------------------------------------------*/
	isEmpty:function(s){
		return !/\S/.test(s);
	},
	/*--------------------------------------------------------------------------*/
	isIntegerInRange:function(n,Nmin,Nmax){
		var num = Number(n);
		if(isNaN(num)){
			return false;
		}
		if(num != Math.round(num)){
			return false;
		}
		return (num >= Nmin && num <= Nmax);
	},
	/*--------------------------------------------------------------------------*/
	isNum:function(number){
		numRegExp = /^[0-9]+$/
		return numRegExp.test(number);
	},
	/*--------------------------------------------------------------------------*/
	isEMailAddr:function(string){
		emailRegExp = /^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.([a-z]){2,4})$/
		return emailRegExp.test(string);
	},
	/*--------------------------------------------------------------------------*/
	isZipCode:function(zipcode,country){
		if(!zipcode) return false;
		if(!country) format = 'US';
		switch(country){
			case'US': zpcRegExp = /^\d{5}$|^\d{5}-\d{4}$/; break;
			case'MA': zpcRegExp = /^\d{5}$/; break;
			case'CA': zpcRegExp = /^[A-Z]\d[A-Z] \d[A-Z]\d$/; break;
			case'DU': zpcRegExp = /^[1-9][0-9]{3}\s?[a-zA-Z]{2}$/; break;
			case'FR': zpcRegExp = /^\d{5}$/; break;
			case'Monaco':zpcRegExp = /^(MC-)\d{5}$/; break;
		}
		return zpcRegExp.test(zipcode);
	},
	/*--------------------------------------------------------------------------*/
	isDate:function(date,format){
		if(!date) return false;
		if(!format) format = 'FR';

		switch(format){
			case'FR': RegExpformat = /^(([0-2]\d|[3][0-1])\/([0]\d|[1][0-2])\/([2][0]|[1][9])\d{2})$/; break;
			case'US': RegExpformat = /^([2][0]|[1][9])\d{2}\-([0]\d|[1][0-2])\-([0-2]\d|[3][0-1])$/; break;
			case'SHORTFR': RegExpformat = /^([0-2]\d|[3][0-1])\/([0]\d|[1][0-2])\/\d{2}$/; break;
			case'SHORTUS': RegExpformat = /^\d{2}\-([0]\d|[1][0-2])\-([0-2]\d|[3][0-1])$/; break;
			case'dd MMM yyyy':RegExpformat = /^([0-2]\d|[3][0-1])\s(Jan(vier)?|F竏堋ｩv(rier)?|Mars|Avr(il)?|Mai|Juin|Juil(let)?|Aout|Sep(tembre)?|Oct(obre)?|Nov(ember)?|Dec(embre)?)\s([2][0]|[1][19])\d{2}$/; break;
			case'MMM dd, yyyy':RegExpformat = /^(J(anuary|u(ne|ly))|February|Ma(rch|y)|A(pril|ugust)|(((Sept|Nov|Dec)em)|Octo)ber)\s([0-2]\d|[3][0-1])\,\s([2][0]|[1][9])\d{2}$/; break;
		}

		return RegExpformat.test(date);
	},
	/*--------------------------------------------------------------------------*/
	isMD5:function(string){
		if(!string) return false;
		md5RegExp = /^[a-f0-9]{32}$/;
		return md5RegExp.test(string);
	},
	/*--------------------------------------------------------------------------*/
	isURL:function(string){
		if(!string) return false;
		string = string.toLowerCase();
		urlRegExp = /^(((ht|f)tp(s?))\:\/\/)([0-9a-zA-Z\-]+\.)+[a-zA-Z]{2,6}(\:[0-9]+)?(\/\S*)?$/
		return urlRegExp.test(string);
	},
	/*--------------------------------------------------------------------------*/
	isGuid:function(guid){// guid format : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx or
						// xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
		if(!guid) return false;
		GuidRegExp = /^[{|\(]?[0-9a-fA-F]{8}[-]?([0-9a-fA-F]{4}[-]?){3}[0-9a-fA-F]{12}[\)|}]?$/
		return GuidRegExp.test(guid);
	},
	/*--------------------------------------------------------------------------*/
	isISBN:function(number){
		if(!number) return false;
		ISBNRegExp = /ISBN\x20(?=.{13}$)\d{1,5}([- ])\d{1,7}\1\d{1,6}\1(\d|X)$/
		return ISBNRegExp.test(number);
	},
	/*--------------------------------------------------------------------------*/
	isSSN:function(number){// Social Security Number format : NNN-NN-NNNN
		if(!number) return false;
		ssnRegExp = /^\d{3}-\d{2}-\d{4}$/
		return ssnRegExp.test(number);
	},
	/*--------------------------------------------------------------------------*/
	isDecimal:function(number){// positive or negative decimal
		if(!number) return false;
		decimalRegExp = /^-?(0|[1-9]{1}\d{0,})(\.(\d{1}\d{0,}))?$/
		return decimalRegExp.test(number);
	},
	/*--------------------------------------------------------------------------*/
	isplatform:function(platform){
		// win, mac, nix
		if(!platform) return false;
		var os;
		winRegExp = /\win/i
		if(winRegExp.test(window.navigator.platform)) os = 'win';

		macRegExp = /\mac/i
		if(macRegExp.test(window.navigator.platform)) os = 'mac';

		nixRegExp = /\unix|\linux|\sun/i
		if(nixRegExp.test(window.navigator.platform)) os = 'nix';

		if(platform == os) return true;
		else return false;
	},
	/*--------------------------------------------------------------------------*/
	getValue:function(id){
		document.getElementById(id).value;
	},
	/*--------------------------------------------------------------------------*/
	addRules:function(rules){
		this.rules_array.push(rules);
	},
	/*--------------------------------------------------------------------------*/
	check:function(){
		this.error_array = [];
		this.e = true;
		for(var i=0;i<this.rules_array.length;i++){
			switch(this.rules_array[i].option){
				/*--------------------------------------------------------------------------*/
				case'ValidChars':
					if(!this.hasValidChars(getValue(this.rules_array[i].id),this.rules_array[i].chars,false)){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'AlphaLatin':
					if (this.isAlphaLatin(getValue(this.rules_array[i].id))){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'required':
					if (this.isEmpty(getValue(this.rules_array[i].id))){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'integerRange':
					if (!this.isIntegerInRange(getValue(this.rules_array[i].id),this.rules_array[i].Min,this.rules_array[i].Max)){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'Number':
					if (!this.isNum(getValue(this.rules_array[i].id))){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'email':
					if (!this.isEMailAddr(getValue(this.rules_array[i].id))){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'zipCode':
					if (!this.isZipCode(getValue(this.rules_array[i].id),this.rules_array[i].country)){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'date':
					if(!this.isDate(getValue(this.rules_array[i].id),this.rules_array[i].format)){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'url':
					if(!this.isURL(getValue(this.rules_array[i].id))){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'Decimal':
					if(!this.isDecimal(getValue(this.rules_array[i].id))){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
				case'isEqual':
					if(!this.isEqual(getValue(this.rules_array[i].id),getValue(this.rules_array[i].to))){
						this.error_array.push(this.rules_array[i].error);
						this.e = false;
					}
				break;
				/*--------------------------------------------------------------------------*/
			}
		}
	},
	/*--------------------------------------------------------------------------*/
	Apply:function(el){
		this.check();
		if(this.e){
			return true;
		}else{
			var endMsg = this.error_array;
			if(!el){
				alert(this.error_array.toString().replace(/\,/gi,"\n"));
			}else{
				document.getElementById(el).innerHTML = this.error_array.toString().replace(/\,/gi,"<br/>");
			}
			return false;
		}
	}
	/*--------------------------------------------------------------------------*/
}

/*----------------------------------------------------------------------------
 * エラーチェック制御
 *----------------------------------------------------------------------------/
/** エラーメッセージを格納する配列 */
var errorMsgs;

/**
 * エラー情報を追加 (エラーありの場合に配列にメッセージが格納されます)
 *
 * @param isError エラーがあるかどうか(エラーチェック関数の結果)
 * @param errorMsg エラーの場合に出力するエラーメッセージ
 * @return エラー情報追加=TRUE, エラーなし=FALSE
 */
function addErrorInfo(isError, errorMsg) {
	if (isError) {
		return addErrorMessage(errorMsg);
	}
	return false;
}

/**
 * 配列にエラーメッセージを格納
 *
 * @param errorMsg 追加するエラーメッセージ
 * @return true
 */
function addErrorMessage(errorMsg) {
	if (errorMsgs == null) {
		errorMsgs = new Array();
	}
	errorMsgs.push(errorMsg);
	return true;
}

/**
 * 入力データの検証実行
 *
 * @return エラーなし=TRUE, エラーあり=FALSE
 */
function validation() {
	if (errorMsgs == null) {
		return true;
	}
	if (errorMsgs.length == 0) {
		addErrorMessage(null);
		displayMessage(errorMsgs);
		errorMsgs = new Array(); // 配列の初期化
		return true;
	}
	displayMessage(errorMsgs);
	errorMsgs = new Array(); // 配列の初期化
	return false;
}

/*----------------------------------------------------------------------------
 * エラーチェック関数
 *----------------------------------------------------------------------------/
/** ライブラリオブジェクト */
var vld = new Validate();

/**
 * 「値入力済み」かどうかを検証する。
 *
 * @param value チェック値
 * @return 入力済み=TRUE, 未入力=FALSE
 */
function isEntered(value) {
	return vld.isNotEmpty(value);
}

/**
 * 「数値」かどうかを検証する。
 *
 * @param value チェック値
 * @return 数値=TRUE, 数値でない=FALSE
 */
function isNumber(value) {
	return vld.isNum(value);
}

/**
 * 「数値」かどうかを検証する。 マイナス、小数点を含む。
 *
 * @param value チェック値
 * @return 数値=TRUE, 数値でない=FALSE
 */
function isDecimal(value) {
	return vld.isDecimal(value);
}

/**
 * 「半角カナ」かどうかを検証する。
 *
 * @param value チェック値
 * @return 半角カナ=TRUE, 半角カナ以外=FALSE
 */
function isHankakuKana(value) {
	hankakukana = /^[ -~｡-ﾟ]*$/
	return hankakukana.test(value);
}

/**
 * 「全角かな」かどうかを検証する。
 *
 * @param value チェック値
 * @return 全角かな=TRUE, 全角かな以外=FALSE
 */
function isZenkakuKana(value) {
	for ( var i = 0; i < value.length; ++i) {
		var c = value.charCodeAt(i);
		if (c < 256 || (c >= 0xff61 && c <= 0xff9f)) {
			return false;
		}
	}
	return true;
}

/**
 * 「バイト数」が範囲内かどうかを検証する。
 *
 * @param value チェック値
 * @param limitByte 制限バイト数
 * @return 範囲内=TRUE, 範囲外=FALSE
 */
function isByteInRange(value, limitByte) {
	value = value + "";
	var count = 0;
	for ( var i = 0; i < value.length; ++i) {
		var sub = value.substring(i, i + 1);
		if (isZenkakuKana(sub)) {
			count += 2;
		} else {
			count += 1;
		}
	}
	if (limitByte < count) {
		return false;
	}
	return true;
}

/**
 * 「バイト数」が範囲内かどうかを検証する。
 *
 * @param value チェック値
 * @param limitByte 制限バイト数
 * @return 範囲内=TRUE, 範囲外=FALSE
 */
function isByteInRangeMin(value, limitByte) {
	value = value + "";
	var count = 0;
	for ( var i = 0; i < value.length; ++i) {
		var sub = value.substring(i, i + 1);
		if (isZenkakuKana(sub)) {
			count += 2;
		} else {
			count += 1;
		}
	}
	if (limitByte > count) {
		return false;
	}
	return true;
}

/**
 * 「文字列サイズ」が指定したサイズと同様かどうかを検証する
 *
 * @param value
 * @param length
 * @return
 */
function isFixLength(value, length) {
	value = value + "";
	if (value.length != length) {
		return false;
	}
	return true;
}

/**
 * 「0以上」かどうかを検証する。
 *
 * @param value チェック値
 * @return 0以上=TRUE, 0以上でない=FALSE
 */
function isPlus(value) {
	if (value >= 0) {
		return true;
	} else {
		return false;
	}
}

/**
 * 「整数」かどうかを検証する。
 *
 * @param value チェック値
 * @return 整数=TRUE, 整数でない=FALSE
 */
function isInteger(value) {
	var integer = /^[-]?[0-9]+$/
	return integer.test(value);
}

/**
 * 「末尾が"0"」かどうかを検証する。
 *
 * @param value チェック値
 * @return 末尾が"0"=TRUE, 末尾が"0"でない=FALSE
 */
function isTailZero(value) {
	var tailValue = value.charAt(value.length - 1);
	if (tailValue == "0") {
		return true;
	} else {
		return false;
	}
}

/**
 * 数値が指定した範囲内かどうかを検証する。
 *
 * @param value チェック値
 * @param minValue 最小値
 * @param maxValue 最大値
 * @return 範囲内=TRUE, 範囲外=FALSE
 */
function isIntegerInRange(value, minValue, maxValue) {
	return vld.isIntegerInRange(value, minValue, maxValue);
}

/**
 * 文字列が特約店コードの桁数かどうかを検証する。
 *
 * @param value チェック値
 * @return 特約店コードの桁数=TRUE, 特約店コードの桁数ではない=FALSE
 */
function isTmsTytenCdLength(value) {
	var length = value.length;
	var result = false;
	switch (length) {
		case 3:
			result = true;
			break;
		case 5:
			result = true;
			break;
		case 7:
			result = true;
			break;
		case 9:
			result = true;
			break;
		case 11:
			result = true;
			break;
		case 13:
			result = true;
			break;
		default:
			result = false;
	}
	return result;
}

/**
 * DhtmlXGridを生成する。
 *
 * @param id DhtmlXを特定するId
 * @return DhtmlXGrid
 */
function createDhtmlXGrid(id) {
	var grid = new dhtmlXGridObject(id);
	grid.setImagePath(contextPath + "/cmn/dhtmlx/codebase/imgs/");
	grid.setSkin("light");
	grid.preventIECaching(true);
	return grid;
}

/**
 * ダウンロード中の表示を伴うDhtmlXGridを生成する。
 *
 * @param id DhtmlXを特定するId
 * @param coverId ダウンロード中レイヤーを特定するためのId
 * @return DhtmlXGrid
 */
function createDhtmlXGridLodingExt(id, coverId) {
	var grid = new dhtmlXGridObject(id);
	grid.setImagePath(contextPath + "/cmn/dhtmlx/codebase/imgs/");
	grid.setSkin("light");
	grid.attachEvent("onXLS", function() {
		document.getElementById(coverId).style.display = 'block';
	});
	grid.attachEvent("onXLE", function() {
		document.getElementById(coverId).style.display = 'none';
	});
	return grid;
}

/**
 * ダウンロード中の表示を伴うDhtmlXGridを生成する。
 * グリッド全体を消しておき、ロードが終わったタイミングで復元する方式
 *
 * @param id DhtmlXを特定するId
 * @param coverId ダウンロード中レイヤーを特定するためのId
 * @return DhtmlXGrid
 */
function createDhtmlXGridLodingExt2(id, coverId) {
	var grid = new dhtmlXGridObject(id);
	grid.setImagePath(contextPath + "/cmn/dhtmlx/codebase/imgs/");
	grid.setSkin("light");
	grid.attachEvent("onXLS", function() {
		document.getElementById(coverId).style.display = 'block';
	});
	grid.attachEvent("onXLE", function() {
		document.getElementById(coverId).style.display = 'none';
		document.getElementById(id).style.display = 'block';
	});
	return grid;
}

/**
 * ダウンロード中の表示を削除する。
 *
 * @param coverId ダウンロード中レイヤーを特定するためのId
 * @return なし
 */
function createDhtmlXGridLodingDelete(coverId) {
	document.getElementById(coverId).style.display = 'none';
}

/**
 * ブロックを選択＆コピーする。 フォーマット付き入力欄対応。
 * データXMLにおいて、userdataにrowType=inputが宣言されている行のみ対象とする。
 *
 * @param grid グリッド
 * @param code 押下されたコード値
 * @param ctrl CTRL
 * @param columnIdArr 入力セルの列の配列(ex：new Array("2","5"))
 * @param shift SHIFT
 * @return false
 */
function copyBlock(grid, code, ctrl, shift,columnIdArr) {

	// コピー
	if (code == 67 && ctrl) {
		// 範囲選択されていない場合は、カレントセルの値をクリップボードにコピー
	 	if(grid._selectionArea == null){
			var _rowId = grid.getSelectedRowId();
			var _colId = grid.getSelectedCellIndex();
			if(_rowId == null || _colId == null){return true;}
			if(_rowId < 1 || _colId < 0){return true;}
			clipboardData.setData("Text", grid.cells(_rowId,_colId).getValue().toString());
	 	}
		grid.copyBlockToClipboard();
	}

	if (code == 86 && ctrl) {

		if(columnIdArr == undefined || columnIdArr == null || columnIdArr.length == 0){
			return true;
		}

		// カレントセルが編集状態の場合は、未編集状態に変更
		var isEdit = false;
		if(grid.editor){
			grid.editStop();
			isEdit = true;
		}

		// ペースト前に、指定された入力セルのカラムタイプがednの場合のみedに変更する
		for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
			for(colIndex=0; colIndex<columnIdArr.length; colIndex++){
				var type = grid.cells3(grid.rowsAr[rowId], columnIdArr[colIndex]).cell._cellType;
				if(type == "edn"){
					grid.setCellExcellType(rowId, columnIdArr[colIndex], "ed");
				}
			}
		}
		// ペースト
		grid.pasteBlockFromClipboard();
		// ペースト後に、指定された入力セルのカラムタイプがed、かつ、データが入力されている場合のみednに変更する
		for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
			var rowType = grid.getUserData(rowId, 'rowType');
			if(rowType == "input"){
				for(colIndex=0; colIndex<columnIdArr.length; colIndex++){
					var columnId = columnIdArr[colIndex];
					if(grid.cells(rowId,columnId).getValue().toString() != ""){
						var type = grid.cells3(grid.rowsAr[rowId], columnId).cell._cellType;
						if(type == "ed"){
							grid.cells(rowId, columnId).setValue(convertToInteger(grid,rowId,columnId));
							grid.setCellExcellType(rowId, columnId, "edn");
							minusStyle(grid,rowId, columnId);
						}
					} else {
						grid.cells(rowId, columnId).setValue(" ");
					}
				}
			}
		}

		// 編集状態だった場合は、カレントセルを編集状態に戻す
		if(isEdit){
			grid.editCell();
		}
	}
	return true;
}
/**
 * Grid行のユーザデータをフォームの配列フィールドに格納する。
 *
 * @param form 格納対象のフォーム(document.XXXForm)
 * @param grid 取得対象のグリッド
 * @param fieldName フォームの格納先変数名
 * @param userDataName ユーザデータ名
 */
function addRowIdList(form, grid, fieldName, userDataName) {
	var rowNum = grid.getRowsNum();
	for (i = 1; i <= rowNum; i++) {
		var userData = grid.getUserData(i, userDataName);
		if(userData == null || userData == ""){continue;}
		var element = document.createElement('input');
		element.type	= 'hidden';
		element.name	= fieldName;
		element.value = userData;
		form.appendChild(element);
	}
}

/**
 * Grid選択行のユーザデータをフォームの配列フィールドに格納する。
 *
 * @param form 格納対象のフォーム(document.XXXForm)
 * @param grid 取得対象のグリッド
 * @param fieldName フォームの格納先変数名
 * @param userDataName ユーザデータ名
 * @param checkBoxColumnIdx 取得対象のグリッドのチェックボックス表示列インデックス
 * @return 追加要素あり=TRUE, 追加要素なし=FALSE
 */
function addCheckRowIdList(form, grid, fieldName, userDataName, checkBoxColumnIdx) {
	var isDataExist = false;
	var checkedIdList = [];
	var checkedRows = grid.getCheckedRows(checkBoxColumnIdx);
	if (checkedRows != "") {
		/* すでにfieldNameが存在していた場合、formから削除する */
		var elements = form.childNodes;
		var removeids = new Array();
		for ( var i = 0; i < elements.length; i++) {
			if(elements[i].name == fieldName){
				removeids.push(i);
			}
		}
		for ( var i = 0; i < removeids.length; i++) {
			form.removeChild(elements[removeids[0]]);
		}
		/* formにfieldNameを追加 */
		var rowIdList = checkedRows.split(",");
		for ( var i = 0; i < rowIdList.length; i++) {
			var userData = grid.getUserData(rowIdList[i], userDataName);
			if(userData == null || userData == ""){continue;}
			var element = document.createElement('input');
			element.type = 'hidden';
			element.name = fieldName;
			element.value = userData;
			form.appendChild(element);
			isDataExist = true;
		}
	}
	return isDataExist;
}

/**
 * 入力フィールド制御
 *
 * @param grid 対象のグリッド
 * @param rowId 制御を行うセルId
 * @param cellInd 制御を行う項目Id
 * @param imeMode IME制御。半角英数-inactive 全角-active
 * @param maxLength 入力上限桁数
 */
function setIme(grid,rowId,cellInd,imeMode,maxLength){
	try {
		cell = grid.cells(rowId,cellInd);
		cell.cell.firstChild.style.imeMode = imeMode;
		cell.cell.firstChild.maxLength = maxLength;
		cell.cell.firstChild.select();
	}catch(e) {
	}
}

/**
 * 入力された値によって項目タイプを変更
 *
 * @param rowId 制御を行うセルId
 * @param cellInd 制御を行う項目Id
 * @param newValue 入力された値
 */
function intCheck(grid,rowId,cellInd,newvalue){
	try{
		cell = grid.cells(rowId,cellInd);
		if(newvalue.toString() == "NaN"){
			grid.setCellExcellType(rowId, cellInd, "ed");
			cell.setValue("");
		}
		if(newvalue.toString() == "0"){
			grid.setCellExcellType(rowId, cellInd, "ed");
		}else if( ( newvalue.toString() != "" ) && isInteger(newvalue) ){
			grid.setCellExcellType(rowId, cellInd, "edn");
		}else{
			cell.setValue("");
			grid.setCellExcellType(rowId, cellInd, "ed");
		}
	}catch(e){}
}

/**
 * 入力された値によって項目タイプを変更
 *
 * @param rowId 制御を行うセルId
 * @param cellInd 制御を行う項目Id
 * @param newValue 入力された値
 */
function intCheckRo(grid,rowId,cellInd,newvalue){
	try{
	cell = grid.cells(rowId,cellInd);
	if(newvalue.toString() == ""){
		grid.setCellExcellType(rowId, cellInd, "ro");
		cell.setValue("");
	}
	if(newvalue.toString() == "0"){
		grid.setCellExcellType(rowId, cellInd, "ro");
	}else if( ( newvalue.toString() != "" ) && isInteger(newvalue) ){
		grid.setCellExcellType(rowId, cellInd, "ron");
	}else{
		cell.setValue("");
		grid.setCellExcellType(rowId, cellInd, "ro");
	}
	}catch(e){}
}

/**
 * 差額・マイナス値のスタイル設定
 *
 * @param rowId 制御を行うセルId
 * @param cellInd 制御を行う項目Id
 */
function sagakuStyle(grid,rowId,cellInd){
	value = grid.cells(rowId,cellInd).getValue();
	if(isInteger(value) && value < 0 ){
		grid.cells(rowId,cellInd).cell.style.color = "#FF0000";
	}else{
		grid.cells(rowId,cellInd).cell.style.color = "";
	}
	if(isInteger(value) && value != 0 ){
		grid.cells(rowId,cellInd).cell.style.backgroundColor = "#F4A460";
	}else{
		grid.cells(rowId,cellInd).cell.style.backgroundColor = "";
	}
}

/**
 * マイナス値のスタイル設定
 * 【注意】下に定義されたfunctionが実行されます。
 *
 * @param rowId 制御を行うセルId
 * @param cellInd 制御を行う項目Id
 */
function minusStyle(grid,rowId,cellInd,newvalue){
	cell = grid.cells(rowId,cellInd);
	if(isInteger(newvalue) && newvalue < 0){
		cell.cell.style.color = "#FF0000";
	}else{
		cell.cell.style.color = "";
	}
}

/**
 * マイナス値のスタイル設定
 *
 * @param rowId 制御を行うセルId
 * @param cellInd 制御を行う項目Id
 */
function minusStyle(grid,rowId,cellInd){
	value = grid.cells(rowId,cellInd).getValue();
	if(isInteger(value) && value < 0 ){
		grid.cells(rowId,cellInd).cell.style.color = "#FF0000";
	}else{
		grid.cells(rowId,cellInd).cell.style.color = "";
	}
}

/**
 * チェックボックス、ALL ON、ALL OFF
 *
 * @param grid 対象のグリッド
 * @param columnId チェックボックスのカラムID
 * @param setOn TRUE：ONにする、FALSE：OFFにする
 */
function allCheckboxChange(grid,columnId,setOn){
	rowNum = grid.getRowsNum();
	for ( var i = 0; i < rowNum; i++) {
		var rowId = grid.getRowId(i);
		if(grid.cells(rowId, columnId).isDisabled()) {
			continue;
		}
		var value = grid.cells(rowId, columnId).getValue();
		if(value == 0 || value == 1){
			if(setOn){
				grid.cells(rowId, columnId).setValue(1);
			} else {
				grid.cells(rowId, columnId).setValue(0);
			}
		}
	}
}

/**
 * チェックボックス、品目カテゴリ別ALL ON
 *
 * @param grid 対象のグリッド
 * @param columnId チェックボックスのカラムID
 * @param category ONにする品目カテゴリ(1:MMP 2:仕入一般)
 */
function categoryCheckboxChange(grid,columnId,category){
	rowNum = grid.getRowsNum();
	for ( var i = 0; i < rowNum; i++) {
		var rowId = grid.getRowId(i);
		var value = grid.cells(rowId, columnId).getValue();
		var rowcategory = grid.getUserData(rowId,"category");
		if(value == 0 || value == 1){
			if(rowcategory == category){
				grid.cells(rowId, columnId).setValue(1);
			}
		}
	}
}

/**
 * チェックボックス、計画対象品目カテゴリ別ALL ON
 *
 * @param grid 対象のグリッド
 * @param columnId チェックボックスのカラムID
 * @param category ONにする品目カテゴリ(01:営業所 02:仕入一般 04:ワクチン 05:CV)
 */
function prodcategoryCheckboxChange(grid,columnId,category){
	rowNum = grid.getRowsNum();
	for ( var i = 0; i < rowNum; i++) {
		var rowId = grid.getRowId(i);
		var value = grid.cells(rowId, columnId).getValue();
		var rowcategory = grid.getUserData(rowId,"category");
		if(value == 0 || value == 1){
			if(rowcategory == category){
				grid.cells(rowId, columnId).setValue(1);
			}
		}
	}
}
/**
 * カーソル移動・桁数チェック
 *
 * @param grid 対象のグリッド
 * @param keycode 入力されたキーコード
 */
function currentCellMove(grid,keycode){
	try{
	var selectInd = grid.getRowIndex(grid.getSelectedRowId());
	var selectIndE = grid.getRowIndex(grid.getSelectedRowId()) + 1;
	var isSplit = grid._split_event;
	if(keycode == 13){
		grid.selectCell(selectIndE , grid.getSelectedCellIndex() , true, false, false, true);
	}
	else if(keycode == 38 && isSplit){
		grid.selectCell(selectInd , grid.getSelectedCellIndex() , true, false, false, true);
	}
	else if(keycode == 40 && isSplit){
		grid.selectCell(selectInd , grid.getSelectedCellIndex() , true, false, false, true);
	}
	else if(keycode == 37){
		if (grid.cell.firstChild.tagName == "TEXTAREA"){return true;}
		grid.selectCell(selectInd , grid.getSelectedCellIndex()-1 , true, false, false, true);
	}else if(keycode == 39){
		if (grid.cell.firstChild.tagName == "TEXTAREA"){return true;}
		grid.selectCell(selectInd , grid.getSelectedCellIndex()+1 , true, false, false, true);
	}else if(keycode == 8 || keycode == 46 || keycode == 13 || keycode == 38 || keycode == 40 || keycode == 37 || keycode == 39 || keycode == 9) {
		return true;
	}else{
		try {
			max = grid.cell.firstChild.maxLength;
			if (grid.cell.firstChild.tagName == "TEXTAREA" && (grid.cell.firstChild.value).length >= max){
				if(document.selection.createRange().text.length > 0 ){
					return true;
				}else{
					return false;
				}
			}
		}catch(e) {}
	}
	return true;
	}catch(e) {
	}
}

/**
 * 列の集計。 データXMLにおいて、userdataのrowType、subTotalIdによって判断を行なう。
 * rowType=inputで宣言される行は、入力行とみなし、入力された値がsubTotalId単位で合計される
 * rowType=subTotalで宣言される行は、サブグループ行とみなし、中間合計を出力する。中間合計はsubTotalIdで合計される。
 * ただし、セルに対し属性sumにfalseが設定されている場合は、入力行合計値を出力しない。
 * rowType=totalで宣言される行は、全入力の合計行とみなし、全subTotalの合計値を出力する。
 * ただし、セルに対し属性sumにfalseが設定されている場合は、全subTotalの合計値を出力しない。
 *
 * @param grid
 *			対象のグリッド
 * @param columnId
 *			対象の列
 */
function setSum(grid,columnId){
	var map = new Array(); // サブ合計MAP(subTotalIdごとに合計)
	// 入力行のデータを、サブグループごとに合計
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "input"){
			var subTotalId = grid.getUserData(rowId, 'subTotalId');
			var value = grid.cells(rowId,columnId).getValue();
			if(value == null || value.toString() == ""){
				continue;
			}
			if(map[subTotalId] != null && map[subTotalId] !=""){
				map[subTotalId] = map[subTotalId] + convertToInteger(grid,rowId,columnId);
			} else {
				map[subTotalId] = convertToInteger(grid,rowId,columnId);
			}
			minusStyle(grid,rowId, columnId);
		}
	}
	// サブ合計行に設定
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "subTotal"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(grid.cells(rowId, columnId).getAttribute("sum") != "false"){
				var subTotalId = grid.getUserData(rowId, 'subTotalId');
				if(map[subTotalId] != null && map[subTotalId] !=""){
					grid.cells(rowId, columnId).setValue(map[subTotalId]);
				} else {
					grid.cells(rowId, columnId).setValue(0);
				}
				minusStyle(grid,rowId, columnId);
			}
		}
	}
	// 全合計取得
	var allSum = 0;
	for (var k in map) {
		allSum = allSum + map[k];
	}
	if(map.length == 0){
		allSum = 0;
	}
	// 全合計行に設定
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "total"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(grid.cells(rowId, columnId).getAttribute("sum") != "false"){
				grid.cells(rowId, columnId).setValue(allSum);
				minusStyle(grid,rowId, columnId);
			}
		}
	}
}

/**
 * setSum2
 * setSumのバグ修正版（インデックスがグリッドの最終行まで到達しないバグの修正版）
 * setSumで問題なく動いている個所については、あえて古い版を使うための措置。
 * 列の集計。 データXMLにおいて、userdataのrowType、subTotalIdによって判断を行なう。
 * rowType=inputで宣言される行は、入力行とみなし、入力された値がsubTotalId単位で合計される
 * rowType=subTotalで宣言される行は、サブグループ行とみなし、中間合計を出力する。中間合計はsubTotalIdで合計される。
 * ただし、セルに対し属性sumにfalseが設定されている場合は、入力行合計値を出力しない。
 * rowType=totalで宣言される行は、全入力の合計行とみなし、全subTotalの合計値を出力する。
 * ただし、セルに対し属性sumにfalseが設定されている場合は、全subTotalの合計値を出力しない。
 *
 * @param grid
 *			対象のグリッド
 * @param columnId
 *			対象の列
 */
function setSum2(grid,columnId){
	let lastRowId = grid.getRowsNum() + 1;
	var map = new Array(); // サブ合計MAP(subTotalIdごとに合計)
	// 入力行のデータを、サブグループごとに合計
	for (rowId = 1; rowId <= lastRowId; rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "input"){
			var subTotalId = grid.getUserData(rowId, 'subTotalId');
			var value = grid.cells(rowId,columnId).getValue();
			if(value == null || value.toString() == ""){
				continue;
			}
			if(map[subTotalId] != null && map[subTotalId] !=""){
				map[subTotalId] = map[subTotalId] + convertToInteger(grid,rowId,columnId);
			} else {
				map[subTotalId] = convertToInteger(grid,rowId,columnId);
			}
			minusStyle(grid,rowId, columnId);
		}
	}
	// サブ合計行に設定
	for (rowId = 1; rowId <= lastRowId; rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "subTotal"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(grid.cells(rowId, columnId).getAttribute("sum") != "false"){
				var subTotalId = grid.getUserData(rowId, 'subTotalId');
				if(map[subTotalId] != null && map[subTotalId] !=""){
					grid.cells(rowId, columnId).setValue(map[subTotalId]);
				} else {
					grid.cells(rowId, columnId).setValue(0);
				}
				minusStyle(grid,rowId, columnId);
			}
		}
	}
	// 全合計取得
	var allSum = 0;
	for (var k in map) {
		allSum = allSum + map[k];
	}
	if(map.length == 0){
		allSum = 0;
	}
	// 全合計行に設定
	for (rowId = 1; rowId <= lastRowId; rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "total"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(grid.cells(rowId, columnId).getAttribute("sum") != "false"){
				grid.cells(rowId, columnId).setValue(allSum);
				minusStyle(grid,rowId, columnId);
			}
		}
	}
}

/**
 * （中間集計行が２つ存在するパターン total -> subTotal -> detTotal の順に細かい集計）
 * 列の集計。 データXMLにおいて、userdataのrowType、subTotalId、subTotalId、detTotalIdによって判断を行なう。
 * rowType=inputで宣言される行は、入力行とみなし、入力された値がsubTotalId、detTotalId単位で合計される
 * rowType=subTotalで宣言される行は、サブグループ行とみなし、中間合計を出力する。中間合計はsubTotalIdで合計される。
 * rowType=detTotalで宣言される行は、サブ（小）グループ行とみなし、中間合計を出力する。中間合計はdetTotalIdで合計される。
 * ただし、セルに対し属性sumにfalseが設定されている場合は、入力行合計値を出力しない。
 * rowType=totalで宣言される行は、全入力の合計行とみなし、全subTotalの合計値を出力する。
 * ただし、セルに対し属性sumにfalseが設定されている場合は、全subTotalの合計値を出力しない。
 *
 * @param grid
 *			対象のグリッド
 * @param columnId
 *			対象の列
 */
function setSumExt(grid,columnId){
	var map = new Array(); // サブ合計MAP(subTotalIdごとに合計)
	var detMap = new Array(); // サブ合計MAP(detTotalIdごとに合計)
	// 入力行のデータを、サブグループごとに合計
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "input"){
			var subTotalId = grid.getUserData(rowId, 'subTotalId');
			var detTotalId = grid.getUserData(rowId, 'detTotalId');
			var value = grid.cells(rowId,columnId).getValue();
			if(value == null || value.toString() == ""){
				continue;
			}
			// subTotal
			value = convertToInteger(grid,rowId,columnId);
			if(map[subTotalId] != null && map[subTotalId] !=""){
				map[subTotalId] = map[subTotalId] + value;
			} else {
				map[subTotalId] = value;
			}
			// detTotalId
			if(detMap[detTotalId] != null && detMap[detTotalId] !=""){
				detMap[detTotalId] = detMap[detTotalId] + value;
			} else {
				detMap[detTotalId] = value;
			}
			minusStyle(grid,rowId, columnId);
		}
	}
	// サブ合計行に設定
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "subTotal"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(grid.cells(rowId, columnId).getAttribute("sum") != "false"){
				var subTotalId = grid.getUserData(rowId, 'subTotalId');
				if(map[subTotalId] != null && map[subTotalId] !=""){
					grid.cells(rowId, columnId).setValue(map[subTotalId]);
				} else {
					grid.cells(rowId, columnId).setValue(0);
				}
				minusStyle(grid,rowId, columnId);
			}
		}
		if(rowType == "detTotal"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(grid.cells(rowId, columnId).getAttribute("sum") != "false"){
				var detTotalId = grid.getUserData(rowId, 'detTotalId');
				if(detMap[detTotalId] != null && detMap[detTotalId] !=""){
					grid.cells(rowId, columnId).setValue(detMap[detTotalId]);
				} else {
					grid.cells(rowId, columnId).setValue(0);
				}
				minusStyle(grid,rowId, columnId);
			}
		}
	}
	// 全合計取得
	var allSum = 0;
	for (var k in map) {
		allSum = allSum + map[k];
	}
	if(map.length == 0){
		allSum = 0;
	}
	// 全合計行に設定
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var rowType = grid.getUserData(rowId, 'rowType');
		if(rowType == "total"){
			// セルの属性"sum"がfalseの場合は、合計値を設定しない
			if(grid.cells(rowId, columnId).getAttribute("sum") != "false"){
				grid.cells(rowId, columnId).setValue(allSum);
				minusStyle(grid,rowId, columnId);
			}
		}
	}
}

/**
 * 差額表示設定。 データXMLにおいて、csllの属性によって判断を行なう。
 * セルの属性「sub」の値："s1"は引かれる対象、"s2"は引く対象、"target"は"s1"-"s2"。
 * セルの属性「subId」の値：同じsubIdで指定された"s1""s2""s3"によって差額を表示する。
 *
 * @param grid 対象のグリッド
 * @param columnId 対象の列
 */
function setSagaku(grid,columnId){
	var s1map = new Array();
	var s2map = new Array();
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var cell = grid.cells(rowId, columnId);
		if(cell.getAttribute("sub") == "s1"){
			var value = grid.cells(rowId,columnId).getValue();
			if(value != null && value.toString() != ""){
				var subId = cell.getAttribute("subId")
				if(s1map[subId] != null && s1map[subId] !=""){
					s1map[subId] = s1map[subId] + convertToInteger(grid,rowId,columnId);
				} else {
					s1map[subId] = convertToInteger(grid,rowId,columnId);
				}
			}
		}
		if(cell.getAttribute("sub") == "s2"){
			var value = grid.cells(rowId,columnId).getValue();
			if(value != null && value.toString() != ""){
				var subId = cell.getAttribute("subId")
				if(s2map[subId] != null && s2map[subId] !=""){
					s2map[subId] = s2map[subId] + convertToInteger(grid,rowId,columnId);
				} else {
					s2map[subId] = convertToInteger(grid,rowId,columnId);
				}
			}
		}
	}
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var cell = grid.cells(rowId, columnId);
		if(cell.getAttribute("sub") == "target"){
			var subId = cell.getAttribute("subId");
			var s1value = "";
			var s2value = "";
			if(s1map[subId] != null && s1map[subId] != ""){
				s1value = s1map[subId];
			}
			if(s2map[subId] != null && s2map[subId] != ""){
				s2value = s2map[subId];
			}
			if(s1value == "" && s2value == ""){
				cell.setValue(0);
			} else {
				cell.setValue((s1value - 0) - (s2value - 0));
			}
			sagakuStyle(grid,rowId, columnId);
//add start 2016/12/08 M.Matsuoka B16-0164_【DPX】ユーザ要望改訂
			if(rowId != 2){
				adjustPlanStyle(grid,rowId, columnId);
			}
//add end 2016/12/08 M.Matsuoka B16-0164_【DPX】ユーザ要望改訂
		}
	}
}

/**
 * 差額表示設定。 データXMLにおいて、csllの属性によって判断を行なう。
 * セルの属性「sub」の値："s1"は引かれる対象、"s2"は引く対象、"target"は"s1"-"s2"。
 * セルの属性「subId」の値：同じsubIdで指定された"s1""s2""s3"によって差額を表示する。
 *
 * @param grid 対象のグリッド
 * @param s1ColumnId s1対象列
 * @param s2ColumnId s2対象列
 * @param targetColumnId target対象列
 * @param columnId 対象の列
 */
function setSagakuByColumn(grid,s1ColumnId,s2ColumnId,targetColumnId){
	var s1map = new Array();
	var s2map = new Array();
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var cell = grid.cells(rowId, s1ColumnId);
		if(cell.getAttribute("sub") == "s1"){
			var value = grid.cells(rowId,s1ColumnId).getValue();
			if(value != null && value.toString() != ""){
				var subId = cell.getAttribute("subId")
				if(s1map[subId] != null && s1map[subId] !=""){
					s1map[subId] = s1map[subId] + convertToInteger(grid,rowId,s1ColumnId);
				} else {
					s1map[subId] = convertToInteger(grid,rowId,s1ColumnId);
				}
			}
		}
		if(cell.getAttribute("sub") == "s2"){
			var value = grid.cells(rowId,s2ColumnId).getValue();
			if(value != null && value.toString() != ""){
				var subId = cell.getAttribute("subId")
				if(s2map[subId] != null && s2map[subId] !=""){
					s2map[subId] = s2map[subId] + convertToInteger(grid,rowId,s2ColumnId);
				} else {
					s2map[subId] = convertToInteger(grid,rowId,s2ColumnId);
				}
			}
		}
	}
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var cell = grid.cells(rowId, targetColumnId);
		if(cell.getAttribute("sub") == "target"){
			var subId = cell.getAttribute("subId");
			var s1value = "";
			var s2value = "";
			if(s1map[subId] != null && s1map[subId] != ""){
				s1value = s1map[subId];
			}
			if(s2map[subId] != null && s2map[subId] != ""){
				s2value = s2map[subId];
			}
			if(s1value == "" && s2value == ""){
				cell.setValue(0);
			} else {
				cell.setValue((s1value - 0) - (s2value - 0));
			}
			sagakuStyle(grid,rowId, targetColumnId);
//add start 2016/12/08 M.Matsuoka B16-0164_【DPX】ユーザ要望改訂
			if(rowId != 2){
				adjustPlanStyle(grid, rowId, targetColumnId);
			}
//add end 2016/12/08 M.Matsuoka B16-0164_【DPX】ユーザ要望改訂
		}
	}
}

/**
 * 比率の計算。
 * 分母、分子が数値以外、または0の場合は""。
 *
 * @param numerator 分子
 * @param denominator 分母
 */
function calcRate(numerator ,denominator){
	if(denominator == null || !isInteger(denominator)){
		return "";
	}
	if(numerator == null || !isInteger(numerator)){
		return "";
	}
	if((denominator - 0) == 0){
		return "";
	} else if ((numerator - 0) == 0){
		return "0.0";
	} else {
		return ((numerator - 0) / (denominator - 0) * 100);
	}
}

/**
 * 比率の計算。 分母、分子が数値以外、または0の場合は""。
 *
 * @param columnId 設定列
 * @param numeratorId 分子列
 * @param denominatorId 分母列
 */
function setRate(grid,columnId,numeratorId ,denominatorId){
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var numerator = grid.cells(rowId, numeratorId).getValue();
		var denominator = grid.cells(rowId, denominatorId).getValue();
		grid.cells(rowId, columnId).setValue(calcRate(numerator,denominator));
	}
}

/**
 * 前同比を計算する。
 * 分母が数値以外、または0の場合は""。
 * 分子が数値以外の場合は""。
 * 第一候補列(決定欄)がnull又は０ならば第二候補列(営業所欄)を利用する。
 *
 * @param columnId 設定列
 * @param numeratorId1 分子列1(第一候補列(決定欄))
 * @param numeratorId2 分子列2(第二候補列(営業所欄))
 */
function setRate2(grid, columnId, numeratorId1, numeratorId2){
	for (rowId = 1; rowId <= grid.getRowsNum(); rowId++) {
		var denominator = grid.getUserData(rowId, "hiddenAdvancePeriodUnitYen");
		if (denominator == 'undefined' || denominator == null || denominator == '' || denominator == '0' || denominator == 0) {
			grid.cells(rowId, columnId).setValue("");
			continue;
		}
		var numerator = null;
		var numerator1 = grid.cells(rowId, numeratorId1).getValue();
		if (numerator1 != 'undefined' && numerator1 != null && numerator1 != '') {
			if ((numerator1 - 0) == 0){
				//第一候補列(決定欄)がzeroならば第二候補列(営業所欄)を利用する。
				numerator = grid.cells(rowId, numeratorId2).getValue();
			} else {
				numerator = numerator1;
			}
		} else {
			//第一候補列(決定欄)がnullならば第二候補列(営業所欄)を利用する。
			numerator = grid.cells(rowId, numeratorId2).getValue();
		}
		if (numerator != 'undefined' && numerator != null && numerator != '' && isInteger(numerator)) {
			numerator = numerator * 1000;
		}
		grid.cells(rowId, columnId).setValue(calcRate(numerator,denominator));
	}
}

/**
 * 数値の変換。
 *
 * @param grid 対象のグリッド
 * @param rowId 対象の行
 * @param columnId 対象の列
 */
function convertToInteger(grid,rowId,columnId){
	var value = grid.cells(rowId,columnId).getValue().toString();
	value = value.replace(/,/g,"");

	/* add Start 2022/10/26 H.futagami No.8　計画管理の月別計画に納入実績の値を表示*/
	if(value.indexOf('.') > -1 ){
		return value - 0;
	}
	/* add End 2022/10/26 H.futagami No.8　計画管理の月別計画に納入実績の値を表示*/

	if(!isInteger(value)){
		return 0;
	} else {
		return value - 0;
	}
}

/**
 * 列のデータをコピーする。
 * コピー先セルのTypeがedもしくはednの場合のみコピーします。
 *
 * @param grid 対象のグリッド
 * @param fromCol コピー元の列番号
 * @param toCol コピー先の列番号
 */
function copyColData(grid, fromCol, toCol){
	for (rowIndex = 1; rowIndex <= grid.getRowsNum(); rowIndex++) {
		var cellType = grid.cells(rowIndex, toCol).getAttribute("type");
		if(cellType == 'ed' || cellType == 'edn'){
			var fromValue = grid.cells(rowIndex, fromCol).getValue();
			grid.setCellExcellType(rowIndex, toCol, "ed");
			grid.cells(rowIndex, toCol).setValue(fromValue);
			grid.setCellExcellType(rowIndex, toCol, "edn");
		}
	}
}

/**
 * ウインドウ有無確認関数
 * 閉じられている場合、trueを返します。
 *
 * @param winVar 確認対象のウィンドウオブジェクト
 */
function win_closed(winVar) {

	var ua = navigator.userAgent
	if( !!winVar )
		if( ( ua.indexOf('Gecko')!=-1 || ua.indexOf('MSIE 4')!=-1 )
			 && ua.indexOf('Win')!=-1 )
			 return winVar.closed
		else return typeof winVar.document != 'object'
	else return true
}
//add start 2016/12/08 M.Matsuoka B16-0164_【DPX】ユーザ要望改訂
/**
 * チーム別の設定
 *
 * @param rowId 制御を行うセルId
 * @param cellId 制御を行う項目Id
 */
function adjustPlanStyle(grid, rowId, cellId){
	value = grid.cells(rowId, cellId).getValue();
	if(isInteger(value) && value != 0 ){
		grid.cells(2, cellId ).cell.style.backgroundColor = "#F4A460";
	}
}
//add start 2016/12/08 M.Matsuoka B16-0164_【DPX】ユーザ要望改訂

/**
 * 登録ボタン押下時の編集中のセル値チェッカー
 * @param pGridObj dhtmlxGridオブジェクト
 * @param pErrorDialogString チェックエラー時、ポップアップに出す文字列
 * @returns
 */
function EditingValueChecker(pGridObj, pErrorDialogString){

	const gridObj = pGridObj;
	const errorDialogString = pErrorDialogString;

	// --- 編集中セル値の登録時チェック用定数 ---
	const BEFORE_EDIT = 0;
	const EDITOR_IS_OPENING = 1;
	const EDITOR_IS_CLOSED = 2;

	// --- 状態保持変数
	let cellEditStage = BEFORE_EDIT;
	let editedValue = 0;

	// -- Escキーが押された際のEditor Closeにて、ステータスをリセットする。
	// -- onEditCell イベントではEscでの Editor Closeが検知されないための措置
	gridObj.attachEvent("onEditCancel", function(id, value){
		cellEditStage = BEFORE_EDIT;
		editedValue = 0;
	});

	// -- 状態の保持 --
	this.saveState = function(pStage,pEditedValue){
		cellEditStage = pStage;
		editedValue = pEditedValue;
	}

	// --- 編集中セル値の登録時チェック ---
	this.isValid = function(){
		if (cellEditStage == EDITOR_IS_OPENING ) {
			gridObj.editStop();

			if(isNaN(editedValue)){
				window.alert(errorDialogString);
				return false;
			}
		}
		return true;
	}
}

/**
 * 品目コードプルダウンを前に選択していたものに変更する。
 * @param tranVal form input prodCodeTran の値
 * @returns
 */
function setProdCodePullDown(tranVal){

	if(!tranVal){
		return;
	}

	for (i = 0; i < document.getElementById("prodCode").length; ++i){
		if (document.getElementById("prodCode").options[i].value == tranVal){
			document.getElementById("prodCode").value = tranVal;
			return;
		}
	}

}


/**
 * スクロールバーの幅を取得
 * https://stackoverflow.com/questions/13382516/getting-scroll-bar-width-using-javascript
 *
 * @returns スクロールバーの幅
 */
function getScrollbarWidth() {

	// Creating invisible container
	const outer = document.createElement('div');
	outer.style.visibility = 'hidden';
	outer.style.overflow = 'scroll'; // forcing scrollbar to appear
	outer.style.msOverflowStyle = 'scrollbar'; // needed for WinJS apps
	document.body.appendChild(outer);

	// Creating inner element and placing it in the container
	const inner = document.createElement('div');
	outer.appendChild(inner);

	// Calculating difference between container's full width and the child width
	const scrollbarWidth = (outer.offsetWidth - inner.offsetWidth);

	// Removing temporary elements from the DOM
	outer.parentNode.removeChild(outer);

	return scrollbarWidth;

}

//計画立案対象外特約店のチェック
function cs_planTaiGaiFlgRikCheck(pGridObj,pColIdx) {
	var rowNum = pGridObj.getRowsNum();
	for(i=0;i<rowNum;i++){
		//i番目の列のrowIdを取得
		var rowId = pGridObj.getRowId(i);
		//集計行はチェックしない
		var inputRow = pGridObj.getUserData(rowId,"rowType") == "input";
		if(inputRow){
			//空白行は許可
			let value = pGridObj.cells2(i, pColIdx).getValue();
			if(value == ""){
			}else{
				value = Number(value);
				if(pGridObj.getUserData(rowId,"planTaiGaiFlgRik") == "1" && value >= 1){
					return true;
				}
			}
		}
	}
	return false;
}


/**
 * カテゴリ・品目を組織選択前のものに戻す
 * @returns
 */
function CategoryAndProdCodeMemorizer(){

	const categorySelect = document.getElementById("prodCategory");
	const prodSelect = document.getElementById("prodCode");

	// --- 状態保持変数
	let categoryCodeVal = "-----";
	if (categorySelect != null){
		categoryCodeVal = categorySelect.value;
	}
	let prodCodeVal = "-----";
	if (prodSelect != null){
		prodCodeVal = prodSelect.value;
	}


	// -- 値を戻す --
	this.undoCategoryCodeValue = function(){
		if(categoryCodeVal !=  "-----" && categorySelect != null){
			for (let i = 0 ; i < categorySelect.length ; i++){
				if(categoryCodeVal == categorySelect[i].value){
					categorySelect[i].selected = true;
					break;
				}
			}
		}
	}

	this.undoProdCodeValue = function(){
		if(prodCodeVal != "-----" && prodSelect != null){
			for (let i = 0 ; i < prodSelect.length ; i++){
				if(prodCodeVal == prodSelect[i].value){
					prodSelect[i].selected = true;
					break;
				}
			}
		}
	}

}


/**
 * カテゴリ・品目を組織選択前のものに戻す
 * @returns
 */
function CategoryAndProdCodeMemorizerForDps(){

	const categorySelect = document.getElementById("category");
	const prodSelect = document.getElementById("prodCode");

	// --- 状態保持変数
	let categoryCodeVal = "-----";
	if (categorySelect != null){
		categoryCodeVal = categorySelect.value;
	}
	let prodCodeVal = "-----";
	if (prodSelect != null){
		prodCodeVal = prodSelect.value;
	}


	// -- 値を戻す --
	this.undoCategoryCodeValue = function(){
		if(categoryCodeVal != "-----" && categorySelect != null){
			for (let i = 0 ; i < categorySelect.length ; i++){
				if(categoryCodeVal == categorySelect[i].value){
					 categorySelect[i].selected = true;
					 break;
				}
			}
		}
	}

	this.undoProdCodeValue = function(){
		if(prodCodeVal != "-----" && prodSelect != null){
			for (let i = 0 ; i < prodSelect.length ; i++){
				if(prodCodeVal == prodSelect[i].value){
					prodSelect[i].selected = true;
					break;
				}
			}
		}
	}

}

//編集ウィンドウを開き、閉じたらafterClosedを実行する
function cs_openEditDialog(url,afterClosed){
	var childWindow = openWindow(url);
	interval = setInterval(function(){
		if(childWindow.closed){
			clearInterval(interval);
			afterClosed();
		}
	}, 200);
}
