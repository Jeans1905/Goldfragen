package de.bw.jeans.webtools;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import de.bw.jeans.helper.Datenspeicher;

public class Fragen {
	
	public static String getFragekatalog(String userName) {
		
		String METHODEN_NAME = "getFragenArray";
		String SOAP_ACTION = "urn:androidConnect#getFragen";
		String NAMESPACE = "urn:androidConnect";
		String URL = Datenspeicher.getFfwServer();
		String rC = "false";
		
		URL = WebTools.addWsdl2Url(URL);
		
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHODEN_NAME);
			request.addProperty("User", userName);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
			envelope.setOutputSoapObject(request);
			
			HttpTransportSE httpTransport = new HttpTransportSE(URL);
			httpTransport.call(SOAP_ACTION, envelope);
			
			SoapObject soapObject = (SoapObject) envelope.getResponse();
			
			if(soapObject.getPropertyAsString(0).equals("false"))
				return rC;

			String rcString = "";
			for(int i=0; i<soapObject.getPropertyCount(); i++) {
				rcString += soapObject.getPropertyAsString(i) + ",";
			}
			rcString = rcString.substring(0, rcString.length()-1);

			return rcString;
		}
		catch(Exception ex) {
			return rC;
		}
	}
}
