#set( $layout = "nothingLayout.vm" )
##----------------------------------------------
##Velocity制御。
##ただし権限にはアクセス出来ない
##----------------------------------------------
window.onbeforeunload = function(event) {
	if (document.getElementById("closeWindow")) {
		closeWindowFlg = (document.getElementById("closeWindow").value == "true");
		if (closeWindowFlg && editFlg) {
			var closeMsg = "$text.get('DPC999C99.000','dialogue')";
			event = (event || window.event);
			flg = (event.clientX > 0 && event.clientY < 0);
			if (flg) {
				event.returnValue = closeMsg;
			}
		}
	}
};
##----------------------------------------------
##ダブルクリックを防止する。
##----------------------------------------------
function blockIt() {
	alert('$text.get("DPC999C99.002","dialogue")');
	return false;
}
## 組織・従業員ダイアログを起動する。
##
## @param sosApplyFuncName 適用関数名
## @param sosSrchPtnType 検索パターン
## @param sosMinSrchValue 最小検索範囲
## @param sosMaxSrchGetValue 最大検索範囲
## @param sosAllDispFlg 全表示フラグ
## @return false
function openSosSearchDialog(sosApplyFuncName, sosSrchPtnType, sosMinSrchValue, sosMaxSrchGetValue, sosAllDispFlg) {
	var formName = document.forms[0].name;
	var dpx = formName.substring(0,3);
	var initSosCodeValue = document.getElementById('initSosCodeValue');
	var url ="$link.setAction('" + dpx + "910C00F00')";
	url = url.concat("?sosApplyFuncName=").concat(sosApplyFuncName);
	url = url.concat("&sosSrchPtnType=").concat(sosSrchPtnType);
	url = url.concat("&sosMinSrchValue=").concat(sosMinSrchValue);
	url = url.concat("&sosMaxSrchGetValue=").concat(sosMaxSrchGetValue);
	url = url.concat("&sosInitSosCodeValue=").concat(initSosCodeValue.value);
	url = url.concat("&sosAllDispFlg=").concat(sosAllDispFlg);
	url = url.concat("&includeSeikei=false");
	## openModalWindow(url, sosApplyFuncName, null);
	openModalWindowW(url);
	return false;
}
## 組織・従業員ダイアログを起動する（整形を含む）。
##
## @param sosApplyFuncName 適用関数名
## @param sosSrchPtnType 検索パターン
## @param sosMinSrchValue 最小検索範囲
## @param sosMaxSrchGetValue 最大検索範囲
## @param sosAllDispFlg 全表示フラグ
## @return false
function openSosSearchDialog_Seikei(sosApplyFuncName, sosSrchPtnType, sosMinSrchValue, sosMaxSrchGetValue, sosAllDispFlg) {
	var formName = document.forms[0].name;
	var dpx = formName.substring(0,3);
	var initSosCodeValue = document.getElementById('initSosCodeValue');
	var url ="$link.setAction('" + dpx + "910C00F00')";
	url = url.concat("?sosApplyFuncName=").concat(sosApplyFuncName);
	url = url.concat("&sosSrchPtnType=").concat(sosSrchPtnType);
	url = url.concat("&sosMinSrchValue=").concat(sosMinSrchValue);
	url = url.concat("&sosMaxSrchGetValue=").concat(sosMaxSrchGetValue);
	url = url.concat("&sosInitSosCodeValue=").concat(initSosCodeValue.value);
	url = url.concat("&sosAllDispFlg=").concat(sosAllDispFlg);
	url = url.concat("&includeSeikei=true");
	## openModalWindow(url, sosApplyFuncName, null);
	openModalWindowW(url);
	return false;
}
## 主担当従業員ダイアログを起動する。
##
## @param sosApplyFuncName 適用関数名
## @param jgiNo 従業員番号
## @return false
function openSosSearchDialog_MainMr(sosApplyFuncName,jgiNo) {
	var formName = document.forms[0].name;
	var dpx = formName.substring(0,3);
	var url ="$link.setAction('" + dpx + "914C00F00')";
	url = url.concat("?sosApplyFuncName=").concat(sosApplyFuncName);
	url = url.concat("&jgiNo=").concat(jgiNo);
	##openModalWindow(url, sosApplyFuncName, null);
	openModalWindowW2(url,"350px","400px");
	return false;
}
## TL検索ダイアログを起動する。
##
## @param sosApplyFuncName 適用関数名
## @param jgiNo 従業員番号
## @return false
function openSosSearchDialog_TLSearch(sosApplyFuncName,jgiNo) {
	var formName = document.forms[0].name;
	var dpx = formName.substring(0,3);
	var url ="$link.setAction('" + dpx + "915C00F00')";
	url = url.concat("?sosApplyFuncName=").concat(sosApplyFuncName);
	url = url.concat("&jgiNo=").concat(jgiNo);
	##openModalWindow(url, sosApplyFuncName, null);
	openModalWindowW2(url,"350px","400px");
	return false;
}
## 組織・従業員の詳細情報を非同期に取得する。
## 詳細情報(JSONオブジェクト)には以下の名前で値が格納されている。
## [name]:選択した組織従業員の名前
## [sos2]:組織コード2
## [sos3]:組織コード3
## [sos4]:組織コード4
## [jgiNo]:従業員番号
## [initSosCodeValue]:初期表示組織コード
## 例)
## <code>
## var obj = getDetailSosInfo(sosCd, jgiNo);
## alert("name=" + obj.name);
## alert("sosCd2=" + obj.sosCd2);
## alert("sosCd3=" + obj.sosCd3);
## alert("sosCd4=" + obj.sosCd4);
## alert("jgiNo=" + obj.jgiNo);
## alert("initSosCodeValue=" + obj.initSosCodeValue);
## </code>
##
## @param sosCd 組織コード
## @param jgiNo 従業員番号
## @return 詳細情報(JSONオブジェクト)
function getDetailSosInfo(sosCd, jgiNo, sosMaxSrchGetValue, defaultChangeFlg, etcSosFlg) {
	var formName = document.forms[0].name;
	var dpx = formName.substring(0,3);
	var url ="$link.setAction('" + dpx + "910C00F05')?sFlg=true" + "&sosCd=" + sosCd + "&jgiNo=" + jgiNo + "&sosMaxSrchGetValue=" + sosMaxSrchGetValue + "&defaultChangeFlg=" + defaultChangeFlg + "&etcSosFlg=" + etcSosFlg + "&includeSeikei=false";
	try {
		var text = getPage(url);
		return JSON.parse(text);
	} catch(e) {
	}
	return false;
}
## 組織・従業員の詳細情報を非同期に取得する（整形を含む）。
## 詳細情報(JSONオブジェクト)には以下の名前で値が格納されている。
## [name]:選択した組織従業員の名前
## [sos2]:組織コード2
## [sos3]:組織コード3
## [sos4]:組織コード4
## [jgiNo]:従業員番号
## [initSosCodeValue]:初期表示組織コード
## 例)
## <code>
## var obj = getDetailSosInfo(sosCd, jgiNo);
## alert("name=" + obj.name);
## alert("sosCd2=" + obj.sosCd2);
## alert("sosCd3=" + obj.sosCd3);
## alert("sosCd4=" + obj.sosCd4);
## alert("jgiNo=" + obj.jgiNo);
## alert("initSosCodeValue=" + obj.initSosCodeValue);
## </code>
##
## @param sosCd 組織コード
## @param jgiNo 従業員番号
## @return 詳細情報(JSONオブジェクト)
function getDetailSosInfo_Seikei(sosCd, jgiNo, sosMaxSrchGetValue, defaultChangeFlg, etcSosFlg) {
	var formName = document.forms[0].name;
	var dpx = formName.substring(0,3);
	var url ="$link.setAction('" + dpx + "910C00F05')?sFlg=true" + "&sosCd=" + sosCd + "&jgiNo=" + jgiNo + "&sosMaxSrchGetValue=" + sosMaxSrchGetValue + "&defaultChangeFlg=" + defaultChangeFlg + "&etcSosFlg=" + etcSosFlg + "&includeSeikei=true";
	try {
		var text = getPage(url);
		return JSON.parse(text);
	} catch(e) {
	}
	return false;
}
##
## 金額チェック
## <br>
## 以下のチェックを行なう<br>
## ・数値チェック<br>
## ・整数チェック<br>
## ・桁数チェック<br>
## ・0以上チェック<br>
## ・下一桁チェック<br>
##
## @param name 項目名
## @param value 入力値
## @param lenght 桁数
## @param plusOnly true：0以上のみ、false：負数許可
## @param msgHeader メッセージヘッダー
##
function amountValidation(name,value,lenght,plusOnly,msgHeader) {
	if(value == null || value == ""){
		return;
	}
	if(msgHeader == null){
		msgHeader = "";
	}
	isAdded = false;
	## [数値]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1004E",["' + name + '","半角数値"])';
		isAdded = addErrorInfo(!isDecimal(value), errorMsg);
	}
	## [整数]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1016E",["' + name + '"])';
		isAdded = addErrorInfo(!isInteger(value), errorMsg);
	}
	## [バイト数(lenght)]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1020E",["' + name + '","' + lenght + '"])';
		isAdded = addErrorInfo(!isByteInRange(value + "", lenght), errorMsg);
	}
	## [0以上]
	if(plusOnly){
		if(!isAdded){
			errorMsg = msgHeader + '$text.get("DPC1008E",["' + name + '","0"])';
			isAdded = addErrorInfo(!isPlus(value), errorMsg);
		}
	}
	## [下一桁が0]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1026E",["' + name + '"])';
	isAdded = addErrorInfo(!isTailZero(value), errorMsg);
	}
	return isAdded;
}
##
## 金額チェック
## <br>
## 以下のチェックを行なう<br>
## ・数値チェック<br>
## ・整数チェック<br>
## ・桁数チェック<br>
## ・0以上チェック<br>
##
## @param name 項目名
## @param value 入力値
## @param lenght 桁数
## @param plusOnly true：0以上のみ、false：負数許可
## @param msgHeader メッセージヘッダー
##
function amountValidationNotTailCheck(name,value,lenght,plusOnly,msgHeader) {
	if(value == null || value == ""){
		return;
	}
	if(msgHeader == null){
		msgHeader = "";
	}
	isAdded = false;
	## [数値]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1004E",["' + name + '","半角数値"])';
		isAdded = addErrorInfo(!isDecimal(value), errorMsg);
	}
	## [整数]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1016E",["' + name + '"])';
		isAdded = addErrorInfo(!isInteger(value), errorMsg);
	}
	## [バイト数(lenght)]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1020E",["' + name + '","' + lenght + '"])';
		isAdded = addErrorInfo(!isByteInRange(value + "", lenght), errorMsg);
	}
	## [0以上]
	if(plusOnly){
		if(!isAdded){
			errorMsg = msgHeader + '$text.get("DPC1008E",["' + name + '","0"])';
			isAdded = addErrorInfo(!isPlus(value), errorMsg);
		}
	}
	return isAdded;
}
##
## 金額チェック
## <br>
## 入力項目ではない項目をチェックする場合
## 以下のチェックを行なう<br>
## ・数値チェック<br>
## ・整数チェック<br>
## ・桁数チェック<br>
## ・0以上チェック<br>
##
## @param name 項目名
## @param value 入力値
## @param lenght 桁数
## @param plusOnly true：0以上のみ、false：負数許可
## @param msgHeader メッセージヘッダー
##
function amountValidationNotTailCheckNotInput(name,value,lenght,plusOnly,msgHeader) {
	if(value == null || value == ""){
		return;
	}
	if(msgHeader == null){
		msgHeader = "";
	}
	isAdded = false;
	## [数値]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1030E",["' + name + '","半角数値"])';
		isAdded = addErrorInfo(!isDecimal(value), errorMsg);
	}
	## [整数]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1031E",["' + name + '"])';
		isAdded = addErrorInfo(!isInteger(value), errorMsg);
	}
	## [バイト数(lenght)]
	if(!isAdded){
		errorMsg = msgHeader + '$text.get("DPC1032E",["' + name + '","' + lenght + '"])';
		isAdded = addErrorInfo(!isByteInRange(value + "", lenght), errorMsg);
	}
	## [0以上]
	if(plusOnly){
		if(!isAdded){
			errorMsg = msgHeader + '$text.get("DPC1033E",["' + name + '","0"])';
			isAdded = addErrorInfo(!isPlus(value), errorMsg);
		}
	}
	return isAdded;
}
## 金額チェック
## <br>
## 入力項目ではない項目をチェックする場合
## 以下のチェックを行なう<br>
## ・桁数チェック<br>
##
## @param name 項目名
## @param value 入力値
## @param lenght 桁数
## @param plusOnly true：0以上のみ、false：負数許可
## @param msgHeader Y価/B価（未設定の場合、Y価）
##
function amountValidationNotTailCheckNotInputNotDisp(name,value,lenght,plusOnly,msgHeader) {
	if(value == null || value == ""){
		return;
	}
	if(msgHeader == null  || msgHeader == ""){
		msgHeader = "Y価";
	}
	isAdded = false;
	## [バイト数(lenght)]
	if(!isAdded){
		errorMsg = '$text.get("DPM1002E",["' + name + '","' + msgHeader + '"])';
		isAdded = addErrorInfo(!isByteInRange(value + "", lenght), errorMsg);
	}
    return isAdded;
}
##----------------------------------------------
##画面サイズを設定する
##----------------------------------------------
function setPageStyle() {
	var gh = screen.height;
	if (window.ActiveXObject) {
		try {
			try {
				httpRequest = new ActiveXObject('Msxml2.XMLHTTP');
			} catch (e) {
				httpRequest = new ActiveXObject('Microsoft.XMLHTTP');
			}
			httpRequest.open('GET', '$link.getContextPath()/pageServlet?gHeight=' + gh, true);
			httpRequest.send(null);
		} catch (e) {}
	}
	return false;
}
##----------------------------------------------
##Grid縦幅を取得する
##----------------------------------------------
function getGridHeight(pageId, defaultHeight) {
	if (pageId == 'undefined' || pageId == null || pageId == '') {
		return defaultHeight;
	}
	var aH = document.documentElement.clientHeight;
	switch (pageId) {
	## ------------------------------
	## 支援
	## ------------------------------
	## 施設選択画面
	case 'dps911C00':
		aH = aH - 240;
		break;
	## (医)過去実績参照画面(担当者別)
	case 'dps913C00':
		aH = aH - 200;
		break;
	## (医)過去実績参照画面(施設特約店別)
	case 'dps913C01':
		## mod Start 2021/7/2 H.Kaneko Step4UAT課題151
		##aH = aH - 155;
		aH = aH - 90;
		## mod Start 2021/7/2 H.Kaneko Step4UAT課題151
		break;
	## 管理トップ画面
	case 'dpm000C00s25':
		aH = aH - 170 - 30; ##トップページのヘッダ部アイコンの縦幅分の調整
		break;
	## (ワ)過去実績参照画面(担当者別)
	case 'dps913C02':
		aH = aH - 200;
		break;
	## (ワ)過去実績参照画面(施設特約店別)
	case 'dps913C03':
		aH = aH - 155;
		break;
	## (医)配分除外施設一覧画面
	case 'dps201C00':
		aH = aH - 250;
		break;
	## (医)配分除外施設登録画面
	## ⇒施設一覧が上にあるので、ここは従来通りとするが、対応しやすいように呼び出しだけはする。
	case 'dps201C01':
		aH = defaultHeight;
		break;
	## (医)配分除外施設編集画面 200
	case 'dps201C02':
		aH = aH - 300;
		break;
	## (医)特定施設個別計画施設一覧画面(営業所案)
	case 'dps202C00':
		aH = aH - 300;
		break;
	## (医)特定施設個別計画立案画面(営業所案)
	case 'dps202C01':
		aH = aH - 180;
		break;
	## (医)特定施設個別計画施設一覧画面(担当者立案)
	case 'dps202C02':
		aH = aH - 280;
		break;
	## (医)特定施設個別計画立案画面(担当者立案)
	case 'dps202C03':
		aH = aH - 180;
		break;
	## (ワ)特定施設個別計画施設一覧画面(担当者立案)
	case 'dps202C04':
		aH = aH - 320;
		break;
	## (ワ)特定施設個別計画立案画面(担当者立案)
	case 'dps202C05':
		##aH = aH - 180;
		aH = aH - 150;
		break;
	## (医)未獲得市場一覧画面
	case 'dps203C00':
		aH = aH - 250;
		break;
	## (医)未獲得市場編集画面
	case 'dps203C01':
		aH = aH - 160;
		break;
	## (医)営業所計画編集画面
	case 'dps300C00':
		##aH = aH - 260;
		aH = aH - 360;
		break;
	## (医)試算対象品目一覧画面
	case 'dps301C00':
		aH = aH - 300;
		break;
	## (医)フリー項目編集画面
	case 'dps301C02':
		aH = aH - 310;
		break;
	## (医)計画対象品目選択画面(営業所まで特定した場合)
	case 'dps302C00s01':
		aH = aH - 340;
		break;
	## (医)計画対象品目選択画面(チームまで特定した場合)
	case 'dps302C00s02':
		aH = aH - 310;
		break;
	## (医)計画対象品目選択画面(担当者まで特定した場合)
	case 'dps302C00s03':
		aH = aH - 240;
		break;
	## (医)試算結果詳細画面(営業所まで特定した場合)
	case 'dps302C01_1':
		aH = aH - 379;
		break;
	## (医)試算結果詳細画面(チームまで特定した場合)
	case 'dps302C01_2':
		aH = aH - 220;
		break;
	## (医)担当者別計画編集画面(チーム別計画)
	case 'dps302C02':
		aH = aH - 330;
		break;
	## (医)担当者別計画編集画面(担当者別計画)
	case 'dps302C03':
		## mod Start 2021/7/8 H.Kaneko Step4UAT課題13
		##aH = aH - 330;
		aH = aH - 280;
		## mod End 2021/7/8 H.Kaneko Step4UAT課題13
		break;
	## (ワ)計画対象品目選択画面
	case 'dps302C04':
		aH = aH - 210;
		break;
	## (ワ)担当者別計画編集画面(担当者別入力)
	case 'dps302C05':
		aH = aH - 150;
		break;
	## (医)施設医師別計画配分対象品目一覧画面
	case 'dps600C00':
		aH = aH - 240;
		break;
	## (医)施設医師別計画担当者一覧画面(利用者が営業所以上)
	case 'dps601C00':
		aH = aH - 310;
		break;
	## (医)施設医師別計画品目一覧画面(担当者まで特定した場合)
	case 'dps601C01_1':
		aH = aH - 300;
		break;
	## (医)施設医師別計画品目一覧画面(担当者まで特定していない場合)
	case 'dps601C01_2':
		aH = aH - 270;
		break;
	## (医)施設医師別計画編集画面
	case 'dps601C02':
		aH = aH - 245;
		break;
	## (医)施設特約店別計画配分対象品目一覧画面
	case 'dps400C00':
		##aH = aH - 300;
		## mod Start 2021/7/5 H.Kaneko Step4UAT課題137
		##aH = aH - 285;
		aH = aH - 300;
		## mod End 2021/7/5 H.Kaneko Step4UAT課題137
		break;
	## (ワ)施設特約店別計画配分対象品目一覧画面
	case 'dps400C03':
		aH = aH - 250;
		break;
	## (医)施設特約店別計画担当者一覧画面(利用者が営業所以上)
	case 'dps401C00_1':
		## mod Start 2021/7/1 H.Kaneko Step4UAT課題151
		##aH = aH - 340;
		aH = aH - 300;
		## mod End 2021/7/1 H.Kaneko Step4UAT課題151
		break;
	## (医)施設特約店別計画担当者一覧画面(利用者がAL以下)
	case 'dps401C00_2':
		aH = aH - 340;
		break;
	## (医)施設特約店別計画品目一覧画面(担当者まで特定した場合)
	case 'dps401C01_1':
		##aH = aH - 340;
		aH = aH - 410;
		break;
	## (医)施設特約店別計画品目一覧画面(担当者まで特定していない場合)
	case 'dps401C01_2':
		aH = aH - 310;
		break;
	## (医)施設特約店別計画編集画面
	case 'dps401C02':
		##aH = aH - 300;
		## mod Start 2021/7/2 H.Kaneko Step4UAT課題191
		##aH = aH - 310;
		aH = aH -275;
		## mod End 2021/7/2 H.Kaneko Step4UAT課題191
		break;
	## (医)施設特約店別計画編集画面(重点品)
	case 'dps401C02_2':
		aH = aH - 300;
		break;
	## (ワ)施設特約店別計画担当者一覧画面(利用者が本部・本部ワクチンG)
	case 'dps401C03_1':
		##aH = aH - 330;
		## 暫定ヘッダ縦幅解消後、見直し。以下の分岐も
		aH = aH - 380;
		defaultHeight = defaultHeight - 30;
		break;
	## (ワ)施設特約店別計画担当者一覧画面(利用者が特約店部長 - 特約店業務G)
	case 'dps401C03_2':
		aH = aH - 260;
		break;
	## (ワ)施設特約店別計画担当者一覧画面(利用者が小児科AC) (J19-0010 対応・コメントのみ修正)
	case 'dps401C03_3':
		aH = aH - 300;
		break;
	## (ワ)施設特約店別計画品目一覧画面
	case 'dps401C04':
		##aH = aH - 330;
		aH = aH - 340;
		break;
	## (ワ)施設特約店別計画編集画面
	case 'dps401C05':
		aH = aH - 330;
		break;
	## (医)特約店別計画配分対象品目一覧画面
	case 'dps500C00':
		## mod Start 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		##aH = aH - 350;
		aH = aH - 250;
		## mod End 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		break;
	## (医)特約店別計画参照画面
	case 'dps502C00':
		##aH = aH - 290;
		## mod Start 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		##aH = aH - 320;
		aH = aH - 260;
		## mod End 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		defaultHeight = defaultHeight - 30;
		break;
	## (医)特約店別計画編集画面
	case 'dps502C01':
		aH = aH - 150;
		break;
	## (医)特約店別計画編集画面
	case 'dps502C02':
		## mod Start 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		##aH = aH - 250;
		aH = aH - 245;
		## mod End 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		break;
	## (ワ)特約店別計画参照画面
	case 'dps502C03':
		## mod Start 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		##aH = aH - 280;
		aH = aH - 230;
		## mod End 2021/7/5 H.Kaneko Step4UAT課題特約店関連
		break;
	## (ワ)特約店別計画編集画面
	case 'dps502C04':
		aH = aH - 120;
		break;
	## (ワ)特約店別計画追加画面
	case 'dps502C05':
		aH = aH - 220;
		break;
	## ------------------------------
	## 管理
	## ------------------------------
	## 施設選択画面
	case 'dpm911C00':
		aH = aH - 240;
		break;
	## (医)組織別計画編集画面
	case 'dpm100C00':
		aH = aH - 325 - 20;
		break;
	## (医)品目別計画編集画面
	case 'dpm101C00':
		aH = aH - 270 - 20;
		break;
	## (医)積上結果表示ダイアログ
	case 'dpm101C01':
		aH = aH - 180;
		break;
	## (医)施設別計画編集画面
	case 'dpm200C00':
		aH = aH - 370;
		break;
	## (ワ)施設別計画編集画面
	case 'dpm200C01':
		aH = aH - 330;
		break;
	## (医)施設品目別計画編集画面
	case 'dpm201C00':
		aH = aH - 310;
		break;
	## (ワ)施設品目別計画編集画面
	case 'dpm201C01':
		aH = aH - 310;
		break;
	## (医)特約店別計画編集画面
	case 'dpm300C00':
		aH = aH - 310;
		break;
	## (ワ)特約店別計画編集画面
	case 'dpm300C01':
		aH = aH - 290;
		break;
	## (医)特約店品目別計画編集画面
	case 'dpm301C00':
		aH = aH - 290;
		break;
	## (ワ)特約店品目別計画編集画面
	case 'dpm301C01':
		aH = aH - 290;
		break;
	## (医)施設特約店別計画編集画面
	case 'dpm400C00':
		aH = aH - 310;
		break;
	## (ワ)施設特約店別計画編集画面
	case 'dpm400C01':
		aH = aH - 310;
		break;
	## (医)施設特約店品目別計画編集画面
	case 'dpm401C00':
		aH = aH - 330;
		break;
	## (ワ)施設特約店品目別計画編集画面
	case 'dpm401C01':
		aH = aH - 330;
		break;
	## 月別 組織別計画編集画面
	case 'dpm500C00':
		aH = aH - 330;
		break;
	## 月別 組織品目別計画編集画面
	case 'dpm501C00':
		aH = aH - 330;
		break;
		## 月別 施設別計画編集画面
	case 'dpm600C00':
		aH = aH - 370;
		break;
	## 月別 施設品目別計画編集画面
	case 'dpm601C00':
		aH = aH - 330;
		break;
	default :
		defaultHeight;
		break;
	}
	## alert("pageId=" + pageId + "\nclientWidth=" + document.documentElement.clientWidth + "\nclientHeight=" + document.documentElement.clientHeight + "\naH=" + aH + "\ndefaultHeight=" + defaultHeight);
	if (aH < defaultHeight) {
		aH = defaultHeight;
	}
	return aH - 20;
}
