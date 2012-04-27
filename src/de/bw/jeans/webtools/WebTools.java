package de.bw.jeans.webtools;

public class WebTools {
	
	static protected String addWsdl2Url(String url) {
		
		if(!url.substring(url.length()-1).equals("/"))
			url += "/";
		
		url += "android/lazWebConnect.php?wsdl";
		
		return url;
	}
}
