package jp.co.takeda.web.cmn.bean;

/**
 * コンテンツタイプを表す列挙
 * 
 * @author tkawabata
 */
public enum ContentsType {

	ABS("audio/x-mpeg"),

	AI("application/postscript"),

	AIF("audio/x-aiff"),

	AIFC("audio/x-aiff"),

	AIFF("audio/x-aiff"),

	AIM("application/x-aim"),

	ART("image/x-jg"),

	ASF("video/x-ms-asf"),

	ASX("video/x-ms-asf"),

	AU("audio/basic"),

	AVI("video/x-msvideo"),

	AVX("video/x-rad-screenplay"),

	BCPIO("application/x-bcpio"),

	BIN("application/octet-stream"),

	BMP("image/bmp"),

	BODY("text/html"),

	CDF("application/x-netcdf"),

	CER("application/x-x509-ca-cert"),

	CLASS("application/java"),

	CPIO("application/x-cpio"),

	CSH("application/x-csh"),

	CSS("text/css"),

	CSV("text/csv"),

	DIB("image/bmp"),

	DOC("application/msword"),

	DTD("application/xml-dtd"),

	DV("video/x-dv"),

	DVI("application/x-dvi"),

	EPS("application/postscript"),

	ETX("text/x-setext"),

	EXE("application/octet-stream"),

	GIF("image/gif"),

	GTAR("application/x-gtar"),

	GZ("application/x-gzip"),

	HDF("application/x-hdf"),

	HTC("text/x-component"),

	HTM("text/html"),

	HTML("text/html"),

	HQX("application/mac-binhex40"),

	ICO("image/x-icon"),

	IEF("image/ief"),

	JAD("text/vnd.sun.j2me.app-descriptor"),

	JAR("application/java-archive"),

	JAVA("text/plain"),

	JNLP("application/x-java-jnlp-file"),

	JPE("image/jpeg"),

	JPEG("image/jpeg"),

	JPG("image/jpeg"),

	JS("text/javascript"),

	JSF("text/plain"),

	JSPF("text/plain"),

	KAR("audio/midi"),

	LATEX("application/x-latex"),

	M3U("audio/x-mpegurl"),

	MAC("image/x-macpaint"),

	MAN("application/x-troff-man"),

	MATHML("application/mathml+xml"),

	ME("application/x-troff-me"),

	MID("audio/midi"),

	MIDI("audio/midi"),

	MIF("application/vnd.mif"),

	MOV("video/quicktime"),

	MOVIE("video/x-sgi-movie"),

	MP1("audio/x-mpeg"),

	MP2("audio/mpeg"),

	MP3("audio/mpeg"),

	MPA("audio/x-mpeg"),

	MPE("video/mpeg"),

	MPEG("video/mpeg"),

	MPEGA("audio/x-mpeg"),

	MPG("video/mpeg"),

	MPV2("video/mpeg2"),

	MS("application/x-troff-ms"),

	NC("application/x-netcdf"),

	ODA("application/oda"),

	ODB("application/vnd.oasis.opendocument.database"),

	ODC("application/vnd.oasis.opendocument.chart"),

	ODF("application/vnd.oasis.opendocument.formula"),

	ODG("application/vnd.oasis.opendocument.graphics"),

	ODI("application/vnd.oasis.opendocument.image"),

	ODM("application/vnd.oasis.opendocument.text-master"),

	ODP("application/vnd.oasis.opendocument.presentation"),

	ODS("application/vnd.oasis.opendocument.spreadsheet"),

	ODT("application/vnd.oasis.opendocument.text"),

	OGG("application/ogg"),

	OTG("application/vnd.oasis.opendocument.graphics-template"),

	OTH("application/vnd.oasis.opendocument.text-web"),

	OTP("application/vnd.oasis.opendocument.presentation-template"),

	OTS("application/vnd.oasis.opendocument.spreadsheet-template"),

	OTT("application/vnd.oasis.opendocument.text-template"),

	PBM("image/x-portable-bitmap"),

	PTC("image/pict"),

	PDF("application/pdf"),

	PGM("image/x-portable-graymap"),

	PIC("image/pict"),

	PICT("image/pict"),

	PLS("audio/x-scpls"),

	PNG("image/png"),

	PRM("image/x-portable-anymap"),

	PNT("image/x-macpaint"),

	PPM("image/x-portable-pixmap"),

	PPS("application/vnd.ms-powerpoint"),

	PPT("application/vnd.ms-powerpoint"),

	PS("application/postscript"),

	PSD("image/x-photoshop"),

	QT("video/quicktime"),

	QTI("image/x-quicktime"),

	QTIF("image/x-quicktime"),

	RSA("image/x-cmu-raster"),

	RDF("application/rdf+xml"),

	RGB("image/x-rgb"),

	RM("application/vnd.rn-realmedia"),

	ROFF("application/x-troff"),

	RTF("text/rtf"),

	RTX("text/richtext"),

	SH("application/x-sh"),

	SHAR("application/x-shar"),

	SMF("audio/x-midi"),

	SIT("application/x-stuffit"),

	SND("audio/basic"),

	SRC("application/x-wais-source"),

	SV4CPIO("application/x-sv4cpio"),

	SV4CRC("application/x-sv4crc"),

	SVG("image/svg+xml"),

	SVGZ("image/svg+xml"),

	SWF("application/x-shockwave-flash"),

	T("application/x-troff"),

	TAR("application/x-tar"),

	TCL("application/x-tcl"),

	TEX("application/x-tex"),

	TEXI("application/x-texinfo"),

	TEXINFO("application/x-texinfo"),

	TIF("image/tiff"),

	TIFF("image/tiff"),

	TR("application/x-troff"),

	TSV("text/tab-separated-values"),

	TXT("text/plain"),

	ULW("audio/basic"),

	USTAR("application/x-ustar"),

	VRML("model/vrml"),

	VSD("application/x-visio"),

	VXML("application/voicexml+xml"),

	WAV("audio/x-wav"),

	WBMP("image/vnd.wap.wbmp"),

	WML("text/vnd.wap.wml"),

	WMLC("application/vnd.wap.wmlc"),

	WMLS("text/vnd.wap.wmlscript"),

	WMLSCRIPTS("application/vnd.wap.wmlscriptc"),

	WRL("model/vrml"),

	XBM("image/x-xbitmap"),

	XHT("application/xhtml+xml"),

	XHTML("application/xhtml+xml"),

	// mod Start 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応
//	XLS("application/vnd.ms-excel"),
	XLS("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	// mod End 2022/9/12  Y.Taniguchi バックログNo.13　POIバージョンアップ対応

	XML("application/xml"),

	XPM("image/x-xpixmap"),

	XSL("application/xml"),

	XSLT("application/xslt+xml"),

	XUL("application/vnd.mozilla.xul+xml"),

	XWD("image/x-xwindowdump"),

	Z("application/x-compress"),

	ZIP("application/zip");

	/**
	 * コンテンツタイプを示す文字列
	 */
	private final String contentsType;

	/**
	 * コンストラクタ
	 * 
	 * @param contentsType MIMETYPE
	 */
	private ContentsType(final String contentsType) {
		this.contentsType = contentsType;
	}

	/**
	 * コンテンツタイプを表す文字列取得する。
	 * 
	 * @return コンテンツタイプを示す文字列
	 */
	public String getContentsType() {
		return this.contentsType;
	}
}
