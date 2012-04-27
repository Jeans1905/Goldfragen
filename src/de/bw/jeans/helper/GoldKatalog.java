package de.bw.jeans.helper;

import java.util.ArrayList;
import java.util.Random;

public class GoldKatalog {

	public void fillFromLocal(int Anzahl, String Gruppe) {
		// Die ID der gewünschten Gruppe ermitteln
		int gruppenId = getGruppenId(Gruppe);

		// Die Anzahl der gewünschten Fragen überprüfen
		int anzahlFragen = checkAnzahl(Anzahl, gruppenId);

		// Alle vorhandenen Fragen in eine ArrayList schreiben
		DatenbankHilfen dbHilfe = new DatenbankHilfen(
				Datenspeicher.getAppContext());
		ArrayList<Integer> alleFragenIds = dbHilfe.getAllFrageIds(gruppenId);

		// Die gewünschte Fragenmenge aus den vorhandenen Fragen (zufällig) rauspicken
		Integer[] fragen = new Integer[anzahlFragen];
		Random ra = new Random();
		for (int i = 0; i < anzahlFragen; i++) {
			int zufallszahl = ra.nextInt(alleFragenIds.size());
			fragen[i] = alleFragenIds.get(zufallszahl);
			alleFragenIds.remove(zufallszahl);
		}

		// Die ermittelten Fragen in den Datenspeicher schreiben
		dbHilfe.writeToDatenspeicher(fragen);
	}

	private int getGruppenId(String Gruppe) {

		int rC = 0;

		if (Gruppe.equals("Gruppenführer"))
			rC = 1;
		if (Gruppe.equals("Mannschaft"))
			rC = 3;
		if (Gruppe.equals("Maschinist"))
			rC = 2;

		return rC;
	}

	private int checkAnzahl(int Anzahl, int GruppenId) {

		int rC = Anzahl;

		if (Anzahl < 1)
			rC = 1;

		if (GruppenId == 1 && Anzahl > 60) {
			rC = 60;
		}

		if (GruppenId == 2 && Anzahl > 60) {
			rC = 60;
		}

		if (GruppenId == 3 && Anzahl > 90) {
			rC = 90;
		}

		return rC;
	}

}
