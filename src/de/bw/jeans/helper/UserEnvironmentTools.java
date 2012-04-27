package de.bw.jeans.helper;

import java.io.*;

import android.content.Context;

public class UserEnvironmentTools {
	
	private static final String fileName = "GoldFragenData.ini";
	
	// Definieren der initialen Werte
	private static final String stdUserName = "";
	private static final String stdUserPW = "";
	private static final boolean savePw = false;
	private static final String stdUserGruppe = "Mannschaft";
	private static final String stdAnzahlFragen = "20";
	private static final String stdFfwServer = "";
	
	public static boolean readUserEnvironment() {
		if (!checkForFile()) {
			// Die Datei ist nicht vorhanden und wird nun mit den Standardwerten gefüllt
			try {
				FileOutputStream out = Datenspeicher.getAppContext().openFileOutput(fileName, Context.MODE_PRIVATE);
				OutputStreamWriter wrt = new OutputStreamWriter(out);
				wrt.write("UserName=" + stdUserName + "\r\n");
				wrt.write("UserPw=" + stdUserPW + "\r\n");
				wrt.write("SavePW=" + savePw + "\r\n");
				wrt.write("UserGruppe=" + stdUserGruppe + "\r\n");
				wrt.write("FfwServer=" + stdFfwServer + "\r\n");
				wrt.write("AnzahlFragen=" + stdAnzahlFragen + "\r\n");
				wrt.close();
			} catch (Exception ex) {
				// Fehler beim Schreiben der Datei
				return false;
			}
			finally {
				// Die Standardwerte werden noch gesetzt
				setDefaultValues();
			}
		} else {
			// Die Datei existiert, die Werte daraus werden eingelesen
			try
			{
				// Zuvor werden auf jeden Fall die Satndardwerte gesetzt!
				setDefaultValues();
				
				// Einlesen der Werte aus der Datei
				String zeile = null;
				FileInputStream inStream = Datenspeicher.getAppContext().openFileInput(fileName);
				BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
				try{
					while((zeile = in.readLine()) != null) {
						String[] workArray = zeile.split("=", 2);
						
						if(workArray[0].equals("UserName"))
							Datenspeicher.setBenutzerName(workArray[1].toString());
						
						if(workArray[0].equals("UserPw"))
							Datenspeicher.setBenutzerKennwort(workArray[1].toString());
						
						if(workArray[0].equals("UserGruppe"))
							Datenspeicher.setBenutzerGruppe(workArray[1].toString());
						
						if(workArray[0].equals("FfwServer"))
							Datenspeicher.setFfwServer(workArray[1].toString());
						
						if(workArray[0].equals("AnzahlFragen"))
							Datenspeicher.setAnzahlFragen(workArray[1].toString());
						
						if(workArray[0].equals("SavePW")) {
							if(workArray[1].toString().equals("true"))
								Datenspeicher.setSavePassword(true);
							else
								Datenspeicher.setSavePassword(false);
						}
					}
				}
				finally {
					in.close();
				}
			}
			catch (Exception ex) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean writeUserEnvironment() {
		
		try {
			// Löschen der bisherigen INI-Datei
			if(checkForFile()) {
				File datei = Datenspeicher.getAppContext().getFileStreamPath(fileName);
				datei.delete();
			}
			
			// Schreiben der neuen INI-Datei
			FileOutputStream out = Datenspeicher.getAppContext().openFileOutput(fileName, Context.MODE_PRIVATE);
			OutputStreamWriter wrt = new OutputStreamWriter(out);
			wrt.write("UserName=" + Datenspeicher.getBenutzerName() + "\r\n");
			wrt.write("UserPw=" + Datenspeicher.getBenutzerKennwort() + "\r\n");
			wrt.write("SavePW=" + Datenspeicher.getSavePassword() + "\r\n");
			wrt.write("UserGruppe=" + Datenspeicher.getBenutzerGruppe() + "\r\n");
			wrt.write("FfwServer=" + Datenspeicher.getFfwServer() + "\r\n");
			wrt.write("AnzahlFragen=" + Datenspeicher.getAnzahlFragen() + "\r\n");
			wrt.close();
		}
		catch(Exception ex) {
			return false;
		}
		
		return true;
	}
	
	private static boolean checkForFile() {
		File datei = Datenspeicher.getAppContext().getFileStreamPath(fileName);
		return (datei.exists()) ? true : false;
	}
	
	private static void setDefaultValues() {
		// Die Standardwerte werden auch in den Datenspeicher geschrieben
		Datenspeicher.setBenutzerName(stdUserName);
		Datenspeicher.setBenutzerKennwort(stdUserPW);
		Datenspeicher.setBenutzerGruppe(stdUserGruppe);
		Datenspeicher.setFfwServer(stdFfwServer);
		Datenspeicher.setAnzahlFragen(stdAnzahlFragen);
		Datenspeicher.setSavePassword(savePw);
	}
}
