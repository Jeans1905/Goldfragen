package de.bw.jeans.webtools;

import java.net.UnknownHostException;
import java.security.MessageDigest;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import de.bw.jeans.helper.Datenspeicher;

public class Password {
	public static String checkPassword(String user, String password) {
		
		String METHODEN_NAME = "checkUserArray";
		String SOAP_ACTION = "urn:androidConnect#checkUserArray";
		String NAMESPACE = "urn:androidConnect";
		String URL = Datenspeicher.getFfwServer();
		String rC = "false";
		
		URL = WebTools.addWsdl2Url(URL);
		
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHODEN_NAME);
			request.addProperty("User", user);
			request.addProperty("Password", md5(password));
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
			envelope.setOutputSoapObject(request);
			
			HttpTransportSE httpTransport = new HttpTransportSE(URL);
			httpTransport.call(SOAP_ACTION, envelope);
			
			SoapObject soapObject = (SoapObject) envelope.getResponse();
			
			if(soapObject.getPropertyAsString(0).equals("true")) {
				rC = soapObject.getPropertyAsString(1);
			}
		}
		catch(XmlPullParserException ex) {
			return "wrongURL";
		}
		catch(UnknownHostException ex) {
			return "noNet";
		}
		catch(Exception ex) {			
			return rC;
		}
		
		return rC;
	}

	private static String md5(String s) {
		try {
			// Den MD5-Hash erzeugen
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Einen HEX-String erzeugen
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while(h.length() < 2) {
					h = "0" + h;
				}
				hexString.append(h);
			}
			return hexString.toString();
		} catch (Exception ex) {
			return "";
		}
	}
	
}
