package de.bw.jeans.helper;

public class GoldAntwort {

	private String antwortText = "";
	private String antwortId = "";
	private boolean antortKorrekt = false;
	private boolean kandidatAntwort = false;
	
	public String getAntwortText() {
		return this.antwortText;
	}
	
	public void setAntwortText(String Text) {
		this.antwortText = Text;
	}
	
	public boolean getAntwortKorrekt() {
		return this.antortKorrekt;
	}
	
	public void setAntwortKorrekt(boolean AntwortKorrekt) {
		this.antortKorrekt = AntwortKorrekt;
	}
	
	public boolean getKandidatAntwort() {
		return this.kandidatAntwort;
	}
	
	public void setKandidatAntwort(boolean AntwortKandidat) {
		this.kandidatAntwort = AntwortKandidat;
	}
	
	public boolean getFrageKorrektBeantwortet()  {
		return (this.antortKorrekt == this.kandidatAntwort) ? true : false;
	}
	
	public void setAntwortId(String id) {
		this.antwortId = id;
	}
	
	public String getAntwortId() {
		return this.antwortId;
	}
}
