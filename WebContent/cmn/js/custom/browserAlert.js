/**
 * chrome対応までの暫定アラート
 */

var userAgent = window.navigator.userAgent.toLowerCase();

if(
//		userAgent.indexOf('msie') != -1 || /* IE10以下 */
        userAgent.indexOf('trident') != -1) {  /* IE11 */
	/* do nothing */
} else {
      alert('お使いのブラウザはサービス非対応です。\nInternet Explorer 11をご利用ください。\n(このダイアログを閉じるとタブが閉じます。)');
      window.close();

}
