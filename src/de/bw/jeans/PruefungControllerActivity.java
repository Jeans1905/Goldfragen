package de.bw.jeans;

import java.util.ArrayList;

import de.bw.jeans.helper.DatenbankHilfen;
import de.bw.jeans.helper.Datenspeicher;
import de.bw.jeans.helper.GoldFrage;
import de.bw.jeans.webtools.Antworten;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class PruefungControllerActivity extends Activity {

	int mainCounter = 0;
	ArrayList<GoldFrage> mainList = new ArrayList<GoldFrage>();
	ArrayList<GoldFrage> gFragen = new ArrayList<GoldFrage>();
	private ProgressDialog pgDialog;
	private AlertDialog alertDialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pruefung_controller);
	
	    // Die gespeicherten Prüfungsfragen aus der Datenapeicher einlesen
	    ArrayList<Integer> fragenIds = Datenspeicher.getPruefungsFragenIds();
	    
	    // Die Goldfragen erstellen
	    Integer[] gFragenId = new Integer[fragenIds.size()];
	    for(int i=0; i<fragenIds.size(); i++) {
	    	gFragenId[i] = fragenIds.get(i);
	    }
	    DatenbankHilfen dbHilfen = new DatenbankHilfen(Datenspeicher.getAppContext());
	    dbHilfen.writeToDatenspeicher(gFragenId);
	    
	    // Die generierten Goldfragen aus dem Datenspeichern auslesen
	    gFragen = Datenspeicher.getAllGoldfragen();
	    mainList = gFragen;
	    
	    // Den Datenspeicher auf die Prüfung vorbereiten
	    Datenspeicher.setPruefungDauerToZero();
	    Datenspeicher.setPruefungLaeuft(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		int counter = 0;
		boolean nextFrage = false;
		for(GoldFrage gFrage : gFragen) {
			counter++;
			if(!gFrage.getFrageBeantwortet()) {
				nextFrage = true;
				mainCounter = counter;
				mainList = gFragen;
				break;
			}
		}
		
		if(nextFrage) {
			// Es wurde eine neue, bisher nicht gestellte Frage gefunden...
			Intent newGoldFrage = new Intent(PruefungControllerActivity.this, StelleGoldfrageActivity.class);
			newGoldFrage.putExtra("ID", counter);
			newGoldFrage.putExtra("AnzahlFragen", mainList.size());
			startActivityForResult(newGoldFrage, 1);
		}
		else {
			// Alle Prüfungsfragen wurden beantwortet...
			Datenspeicher.setPruefungLaeuft(false);
			
			// Die benötigten Controls einlesen
			TextView tvZeit = (TextView) findViewById(R.id.textView3);
			TextView tvZeile = (TextView) findViewById(R.id.textView4);
			TextView tvProzent = (TextView) findViewById(R.id.textView5);
			TextView tvInfo = (TextView) findViewById(R.id.textView7);
			
			// Die Prüfungswerte ermitteln
			double prozKorrekt = getProzentKorrekt();
			String dauer = getZeitString();
			
			// Prüfen, ob bestanden oder nicht...
			boolean bestanden = true;
			if(prozKorrekt < 75)
				bestanden = false;
			if(Datenspeicher.getPruefungDauer() > Datenspeicher.getMaxPruefdauer())
				bestanden = false;
			
			// Texte übergeben
			tvZeit.setText(dauer);
			tvZeile.setText(getAnzahlKorrekt() + " Fragen korrekt beantwortet, das sind");
			tvProzent.setText(prozKorrekt + " %");
			if(bestanden) {
				tvInfo.setText("B E S T A N D E N !");
				tvInfo.setTextColor(Color.GREEN);
			}
			else {
				tvInfo.setText("nicht bestanden...");
				tvInfo.setTextColor(Color.RED);
			}
			
			// Die Dialogbox anzeigen
			pgDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
			pgDialog.setTitle("Serverkommunikation");
			pgDialog.show();
			
			alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Info");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.dismiss();
				}
			});
			
			new AsyncTask<Void, String, Boolean>() {
				
				protected void onPostExecute(Boolean Value) {
					pgDialog.dismiss();

					if(Value) {
						// Die Daten wurden erfolgreich an den Server übertragen
						alertDialog.setMessage("Daten erfolgreich übertragen!");
						alertDialog.show();
					}
					else {
						// Das Übertragen der Daten hat leider nicht funktioniert...
						alertDialog.setMessage("Fehler beim übertragen der Daten...\r\nDie Daten wurden gespeichert und\r\nwerden später übertragen...");
						alertDialog.show();
					}
				}
				
				protected void onProgressUpdate(String... progress) {
					updateWaitText(progress);
				}
				
				@Override
				protected Boolean doInBackground(Void... params) {

					publishProgress("Ergebnisse werden an den\r\nServer übertragen...");
					return Antworten.sendAntworten();
				}
			}.execute();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				
			}
		} else if (resultCode == RESULT_CANCELED) {
			if (requestCode == 1) {
				if(mainCounter >= 2)
					mainList.get(mainCounter - 2).setFrageBeantwortet(false);
				else {
					// Ein Abbrechen der Prüfung ist NICHT möglich...
					//finish();
				}
			}
		}
	}

	private void updateWaitText(String[] fortschritt) {
		pgDialog.setMessage(fortschritt[0]);
	}
	
	private int getAnzahlKorrekt() {
		int rC = 0;
		
		for(int i=0; i<mainList.size(); i++) {
			if(mainList.get(i).getFrageKorrekt())
				rC++;
		}
		
		return rC;
	}
	
	private double getProzentKorrekt() {
		int alleFragen = mainList.size();
		int anzahlKorrekt = getAnzahlKorrekt();
				
		double rC = (100*anzahlKorrekt) / alleFragen;
		return Math.round(rC * 100.) / 100.;
	}

	private String getZeitString() {
		String rC = "";
		int zeitRest = Datenspeicher.getPruefungDauer();
		
		// Die Anzahl der Stunden ermitteln
		if((zeitRest / 3600) > 0) {
			int stdAnzahl = Math.abs(zeitRest / 3600);
			zeitRest = zeitRest - (3600*stdAnzahl);
			rC += stdAnzahl + ":";
		}
		
		// Die Anzahl der Minuten ermitteln
		if((zeitRest / 60) > 0) {
			int anzMinuten = Math.abs(zeitRest / 60);
			zeitRest = zeitRest - (60*anzMinuten);
			if(anzMinuten < 10)
				rC += "0" + anzMinuten + ":";
			else
				rC += anzMinuten + ":";
		}
		
		// Wenn unter einer Minute, dann eine 0 bereitstellen
		if(rC.length() == 0)
			rC = "0:";
		
		// Der Rest sind die verbleibenden Sekunden
		if(zeitRest < 10)
			rC += "0" + zeitRest;
		else
			rC += zeitRest;
		
		return rC;
	}
}
