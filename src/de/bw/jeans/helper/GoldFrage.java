package de.bw.jeans.helper;

import java.util.ArrayList;
import java.util.Random;

import android.R.integer;

public class GoldFrage {

	private int frageId = 0;
	private String frageText = "";
	private String bildName = "";
	private ArrayList<GoldAntwort> antworten = new ArrayList<GoldAntwort>();
	private ArrayList<Integer> antwortenReihenfolge = new ArrayList<Integer>();
	private boolean frageBeantwortet = false;
	
	public void setFrageId(int Id) {
		this.frageId = Id;
	}
	
	public int getFrageId() {
		return this.frageId;
	}
	
	public String getFrageText() {
		return this.frageText;
	}
	
	public void setFrageText(String Text) {
		this.frageText = Text;
	}
	
	public String getBildName() {
		return this.bildName;
	}
	
	public void setBildName(String Name) {
		this.bildName = Name;
	}
	
	public void setAntwortenReihenfolge(ArrayList<Integer> dieReihenfolge) {
		this.antwortenReihenfolge = dieReihenfolge;
	}
	
	public void addGoldAntwort(GoldAntwort Antwort) {
		antworten.add(Antwort);
	}
	
	public void addAllGoldAntworten(GoldAntwort[] goldAntworten) {
		this.antworten.clear();
		for(int i=0; i<goldAntworten.length; i++) {
			antworten.add(goldAntworten[i]);
		}
	}
	
	public GoldAntwort getGoldAntwort(int AntwortNummer) {
		int queryNummer = AntwortNummer - 1;
		
		if(queryNummer < 0 || queryNummer > antworten.size())
			return null;
		
		return antworten.get(queryNummer);
	}
	
	public GoldAntwort[] getAllGoldAntworten() {
		return getAllGoldAntworten(false);
	}
	
	public GoldAntwort[] getAllGoldAntworten(boolean mischen) {
		GoldAntwort[] myAntworten = new GoldAntwort[antworten.size()];
		
		if(mischen) {			
			for(int i=0; i<antworten.size() ; i++) {
				if(antwortenReihenfolge.size() > 0) {
					Integer x = Integer.valueOf(antwortenReihenfolge.get(i));
					myAntworten[i] = antworten.get(Integer.valueOf(antwortenReihenfolge.get(i)));
				}
			}
		}
		else
		{
			for(int i=0; i<antworten.size(); i++) {
				myAntworten[i] = antworten.get(i);
			}
		}
		
		return myAntworten;
	}
	
	public ArrayList<Integer> getAntwortenReihenfolge() {
		return this.antwortenReihenfolge;
	}
	
 	public boolean getFrageKorrekt() {
		boolean rC = true;
		
		for(int i=0; i<antworten.size(); i++) {
			if(!antworten.get(i).getFrageKorrektBeantwortet()) {
				rC = false;
				break;
			}
		}
		
		return rC;
	}
	
	public boolean getFrageBeantwortet() {
		return this.frageBeantwortet;
	}
	
	public void setFrageBeantwortet(boolean fAntwort) {
		this.frageBeantwortet = fAntwort;
	}
}
