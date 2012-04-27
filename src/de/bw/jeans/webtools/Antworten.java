package de.bw.jeans.webtools;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import de.bw.jeans.helper.Datenspeicher;
import de.bw.jeans.helper.GoldAntwort;
import de.bw.jeans.helper.GoldFrage;
import de.bw.jeans.helper.PruefungsDatenTools;

public class Antworten {
	
	public static boolean sendAntworten() {
		
		String METHODEN_NAME = "setAntworten";
		String SOAP_ACTION = "urn:androidConnect#setAntworten";
		String NAMESPACE = "urn:androidConnect";
		String URL = Datenspeicher.getFfwServer();
		boolean rC = false;
		
		URL = WebTools.addWsdl2Url(URL);
		
		// Den Antwortstring erzeugen
		String prfAntwort = createReturnString();
		
		try {
			// Die Daten an den Server der Feuerwehr übertragen
			SoapObject request = new SoapObject(NAMESPACE, METHODEN_NAME);
			request.addProperty("antwortString", prfAntwort);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
			envelope.setOutputSoapObject(request);
			
			HttpTransportSE httpTransport = new HttpTransportSE(URL);
			httpTransport.call(SOAP_ACTION, envelope);
			
			SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
			
			if(soapPrimitive.toString().equals("true"))
				rC = true;
		}
		catch(Exception ex) {
			// Die Daten in eine lokale Datei schreiben um sie später zu übertragen
			PruefungsDatenTools.addToFile(prfAntwort);
		}
		
		// Die Antwort zurückgeben
		return rC;
	}
	
	private static String createReturnString() {
		
		// Den Returnstring bereitstellen
		String rC = "";
		
		// Die Daten aus den Datenspeicher lesen
		String userName = Datenspeicher.getBenutzerName();
		String startTimestramp = Datenspeicher.getPruefungStartzeit();
		Integer prfDauer = Datenspeicher.getPruefungDauer();
		String endeTimestamp = String.valueOf((Integer.valueOf(startTimestramp) + prfDauer));
		Integer prfId = Datenspeicher.getPruefungsId();
		ArrayList<GoldFrage> prfFragen = Datenspeicher.getAllGoldfragen();
		
		// Den Returnstring aufbauen...
		rC += prfId.toString() + ",";	// Hinzufügen der PrüfungsId
		rC += userName + ",";			// Hinzufügen des Benutzernamens
		rC += endeTimestamp + ",";		// Hinzufügen des 'Ende-Timestamps'
		
		// Alle Antworten durchgehen und, sollte eine Antwort angehakt sein, diese in den Returnstring mit aufnehmen
		for(GoldFrage gFrage : prfFragen) {
			GoldAntwort[] gAntworten = gFrage.getAllGoldAntworten();
			String gAtwortString = "";

			for(int i=0; i<gAntworten.length; i++) {
				if(gAntworten[i].getKandidatAntwort()) {
					gAtwortString += gAntworten[i].getAntwortId() + ";";
				}
			}
			
			if(gAtwortString.length() > 0) {
				gAtwortString = gAtwortString.substring(0, (gAtwortString.length() - 1));
				rC += String.valueOf(gFrage.getFrageId()) + ";" + gAtwortString + ",";
			}
		}
		
		// Das letzte Komma antfernen
		rC = rC.substring(0, rC.length()-1);
		
		// Den Returnstring zurückgeben
		return rC;
	}
}
