package de.bw.jeans.helper;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;

public class Datenspeicher {

	// Ist "TRUE", wenn es sich für die St. Leon interne Version handelt
	private static final boolean onlineVersion = true;

	// Den aktuellen Context der App speichern
	private static Context appContext = null;

	// Die Benutzerdaten des aktuellen Users
	private static String userName = "";
	private static String userPW = "";
	private static boolean savePW = false;
	private static String userGruppe = "";
	private static String ffwServer = "";
	
	// Informationen über eine evtl. laufende Prüfung
	private static boolean pruefungLauft = false;
	private static String pruefungStartzeit = "";
	private static int pruefungDauer = 0;
	private static int maxPruefdauer = 2700;
	private static int pruefungId = 0;
	private static ArrayList<Integer> pruefungsFragenId = new ArrayList<Integer>();
	
	// Die 'Standard'-Anzahl an Fragen
	private static String anzahlFragen = "";
	
	// Die Goldfragen 
	private static ArrayList<GoldFrage> gFragen = new ArrayList<GoldFrage>();
	
	// Auch richtig beantwortete Fragen sollen angezeigt werden
	private static boolean showCorrectAlso = false;

	public static boolean isOnlineVersion() {
		return (onlineVersion) ? true : false;
	}
	
	public static void setShowCorrectAlso(boolean value) {
		showCorrectAlso = value;
	}
	
	public static boolean getShowCorrectAlso() {
		return showCorrectAlso;
	}

	public static void setAppContext(Context appContext) {
		Datenspeicher.appContext = appContext;
	}

	public static Context getAppContext() {
		return Datenspeicher.appContext;
	}

	public static String getBenutzerName() {
		return Datenspeicher.userName;
	}

	public static void setBenutzerName(String benutzerName) {
		Datenspeicher.userName = benutzerName;
	}

	public static String getBenutzerKennwort() {
		return Datenspeicher.userPW;
	}

	public static void setBenutzerKennwort(String benutzerKennwort) {
		Datenspeicher.userPW = benutzerKennwort;
	}

	public static String getBenutzerGruppe() {
		return Datenspeicher.userGruppe;
	}

	public static void setBenutzerGruppe(String benutzerGruppe) {
		Datenspeicher.userGruppe = benutzerGruppe;
	}

	public static String getFfwServer() {		
		return Datenspeicher.ffwServer;
	}
	
	public static String getFfwServer_Short() {
		return Datenspeicher.ffwServer;
	}

	public static void setFfwServer(String feuerwehrServer) {
		Datenspeicher.ffwServer = feuerwehrServer;
	}
	
	public static String getAnzahlFragen() {
		return Datenspeicher.anzahlFragen;
	}
	
	public static void setAnzahlFragen(String anzahlFragen) {
		Datenspeicher.anzahlFragen = anzahlFragen;
	}
	
	public static ArrayList<GoldFrage> getAllGoldfragen() {
		return Datenspeicher.gFragen;
	}
	
	public static GoldFrage getGoldFrage(Integer fragenNummer) {
		
		fragenNummer = fragenNummer - 1;
		
		if(Datenspeicher.gFragen.isEmpty())
			return null;
		
		if(fragenNummer > Datenspeicher.gFragen.size())
			return null;
		
		return Datenspeicher.gFragen.get(fragenNummer);
	}
	
	public static void addGoldfrage(GoldFrage gFrage) {
		Datenspeicher.gFragen.add(gFrage);
	}
	
	public static void removeAllGoldfragen() {
		gFragen.clear();
	}
	
	public static boolean getPruefungLaeuft() {
		return Datenspeicher.pruefungLauft;
	}
	
	public static void setPruefungLaeuft(boolean pruefungLaeuft) {
		Datenspeicher.pruefungLauft = pruefungLaeuft;
	}
	
	public static String getPruefungStartzeit() {
		return Datenspeicher.pruefungStartzeit;
	}
	
	public static void setPruefungStartzeit(String startzeit) {
		Datenspeicher.pruefungStartzeit = startzeit;
	}
	
	public static int getPruefungDauer() {
		return Datenspeicher.pruefungDauer;
	}
	
	public static void setPruefungDauerToZero() {
		Datenspeicher.pruefungDauer = 0;
	}
	
	public static void setPruefungDauerAddSecond() {
		Datenspeicher.pruefungDauer ++;
	}
	
	public static int getMaxPruefdauer() {
		return maxPruefdauer;
	}
	
	public static void setPruefungsId(int pruefungsId) {
		Datenspeicher.pruefungId = pruefungsId;
	}
	
	public static int getPruefungsId() {
		return Datenspeicher.pruefungId;
	}
	
	public static void setPruefungsFragenIds(ArrayList<Integer> fragenIds) {
		Datenspeicher.pruefungsFragenId = fragenIds;
	}
	
	public static ArrayList<Integer> getPruefungsFragenIds() {
		return Datenspeicher.pruefungsFragenId;
	}
	
	public static boolean getSavePassword() {
		return savePW;
	}
	
	public static void setSavePassword(boolean savePw) {
		savePW = savePw;
	}
}
