package jp.co.takeda.web.cmn.sso;

public class ParsedToken {


	private final String jgiNo;
	private final String altJgiNo;

	public ParsedToken(String jgiNo, String altJgiNo) {
		this.jgiNo = jgiNo;
		this.altJgiNo = altJgiNo;
	}

	public String getJgiNo() {
		return jgiNo;
	}

	public String getAltJgiNo() {
		return altJgiNo;
	}

}
