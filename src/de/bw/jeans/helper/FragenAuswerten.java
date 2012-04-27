package de.bw.jeans.helper;

import java.util.ArrayList;

public class FragenAuswerten {
	
	private int anzahlFragen = 0;
	private int beantworteteFragen = 0;
	private int korrektBeantworteteFragen = 0;
	
	private double prozentRichtig = 0;
	private double prozentFalsch = 0;
	
	private boolean testBestanden = false;
	
	public FragenAuswerten() {
		ArrayList<GoldFrage> workList = Datenspeicher.getAllGoldfragen();
		for(int i=0; i<workList.size(); i++) {
			GoldFrage gFrage = workList.get(i);
			this.anzahlFragen++;
			if(gFrage.getFrageBeantwortet())
				this.beantworteteFragen++;
			if(gFrage.getFrageKorrekt())
				this.korrektBeantworteteFragen++;
		}
		ergebnisErrechnen();
	}
	
	private void ergebnisErrechnen() {
		// Errechnen, wieviel Prozent der Fragen korrekt beantwortet wurden
		double prozKorrekt = (100 * this.korrektBeantworteteFragen) / this.anzahlFragen;
		prozKorrekt = Math.round(prozKorrekt * 100.) / 100.;
		double prozFalsch = 100 - prozKorrekt;
		
		this.prozentRichtig = prozKorrekt;
		this.prozentFalsch = prozFalsch;
		
		if(prozKorrekt > 75)
			this.testBestanden = true;
		else
			this.testBestanden = false;
	}
	
	public boolean getTestBestanden() {
		return this.testBestanden;
	}
	
	public double getProzentKorrekt() {
		return this.prozentRichtig;
	}
	
	public double getProzentFalsch() {
		return this.prozentFalsch;
	}
	
	public int getAnzahlFragen() {
		return this.anzahlFragen;
	}
	
	public int getBeantworteteFragen() {
		return this.beantworteteFragen;
	}
	
	public int getKorrektBeantworteteFragen() {
		return this.korrektBeantworteteFragen;
	}
}
