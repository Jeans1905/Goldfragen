package de.bw.jeans;

import java.security.PublicKey;
import java.util.ArrayList;

import de.bw.jeans.R;
import de.bw.jeans.helper.Datenspeicher;
import de.bw.jeans.helper.GoldKatalog;
import de.bw.jeans.helper.IntentIntegrator;
import de.bw.jeans.helper.IntentResult;
import de.bw.jeans.helper.UserEnvironmentTools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StartScreenOffline extends Activity {
	
	Spinner spiGruppe = null;
	EditText txtAnzahlFragen = null;
	LinearLayout linLay3 = null;
	LinearLayout linLay5 = null;
	ImageButton btnImg1 = null;
	ImageButton btnImg2 = null;
	TextView prfUser = null;
	TextView prfPw = null;
	Button btn2 = null;
	CheckBox cb1 = null;
	private ProgressDialog pgDialog; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen_offline);
		
		// Einlesen aller Felder
		spiGruppe = (Spinner) findViewById(R.id.spinner1);
		txtAnzahlFragen = (EditText) findViewById(R.id.editText1);
		linLay3 = (LinearLayout) findViewById(R.id.linearLayout3);
		linLay5 = (LinearLayout) findViewById(R.id.linearLayout5);
		btnImg1 = (ImageButton) findViewById(R.id.imageButton1);
		btnImg2 = (ImageButton) findViewById(R.id.imageButton2);
		prfUser = (TextView) findViewById(R.id.editText2);
		prfPw = (TextView) findViewById(R.id.editText3);
		btn2 = (Button) findViewById(R.id.button2);
		cb1 = (CheckBox) findViewById(R.id.checkBox1);
	}
	
	protected void onStart() {
		super.onStart();
		
		// Initialisieren der einzelnen Felder -- OFFLINE --
		TextView txtUeberschrift = (TextView) findViewById(R.id.textView2);
		txtUeberschrift.setText("Einstellungen - Übungsfragen");

		TextView lblGruppe = (TextView) findViewById(R.id.textView1);
		lblGruppe.setText("Gruppe: ");

		TextView lblAnzahlFragen = (TextView) findViewById(R.id.textView3);
		lblAnzahlFragen.setText("Anzahl Fragen: ");

		txtAnzahlFragen.setText(Datenspeicher.getAnzahlFragen());
		txtAnzahlFragen.setSelection(0, txtAnzahlFragen.getText().length());

		Button btnStart = (Button) findViewById(R.id.button1);
		btnStart.setWidth(300);
		btnStart.setText("Übung starten");

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Gruppen, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spiGruppe.setAdapter(adapter);
		ArrayAdapter myAdapter = (ArrayAdapter) spiGruppe.getAdapter();
		int spinnerPosition = myAdapter.getPosition(Datenspeicher.getBenutzerGruppe());
		spiGruppe.setSelection(spinnerPosition);

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Die eingegebenen Werte speichern
				datenSichernTest();

				// Den gewünschten Katalog erstellen
				GoldKatalog gKatalog = new GoldKatalog();
				gKatalog.fillFromLocal(Integer.valueOf(Datenspeicher.getAnzahlFragen()), Datenspeicher.getBenutzerGruppe());

				// Mit dem Fragen starten...
				Intent neueFragen = new Intent(StartScreenOffline.this, FragenControllerActivity.class);
				startActivity(neueFragen);
			}
		});
		
		btnImg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Der Server der Feuerwehr kann erfasse / geändert werden
				Intent changeServerData = new Intent(StartScreenOffline.this, FwServerErfassenActivity.class);
				startActivity(changeServerData);
			}
		});
		
		btnImg2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Die Daten eines Benutzers können eingescannt werden
				IntentIntegrator integrator = new IntentIntegrator(StartScreenOffline.this);
				integrator.initiateScan();
			}
		});
		
		showHideOnlineTools(Datenspeicher.isOnlineVersion());
		
		// Initialisieren der einzelnen Felder -- online
		if (Datenspeicher.isOnlineVersion()) {
			TextView tvUeberschriftTest = (TextView) findViewById(R.id.textView5);
			tvUeberschriftTest.setText("Einstellungen - Prüfungen");
			
			Button btnOnlStart = (Button) findViewById(R.id.button2);
			btnOnlStart.setWidth(300);
			btnOnlStart.setText("Prüfung starten");
			
			prfUser.setText(Datenspeicher.getBenutzerName());
			
			cb1.setChecked(Datenspeicher.getSavePassword());
			
			if(Datenspeicher.getSavePassword()) 
				prfPw.setText(Datenspeicher.getBenutzerKennwort());
			else
				prfPw.setText("");
			
			btn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// Die eingegebenen Werte speichern
					datenSichernPruefung();

					// Die Daten vom Server laden...
					pgDialog = new ProgressDialog(v.getContext(), ProgressDialog.STYLE_SPINNER);
					pgDialog.setTitle("Serverkommunikation");
					pgDialog.show();
					
					new AsyncTask<String, String, String>() {
						protected void onPostExecute(String result) {
							//super.onPostExecute(result);
							pgDialog.dismiss();
							if(result.equals("-1")) {
								showErrorDialog("Fehler bei der Anmeldung!\r\nEvtl. falsches Kennwort...");
							}
							else if (result.equals("-2")) {
								showErrorDialog("Fehler beim einlesen der Fragen!\r\nEvtl. später nochmals versuchen...");
							}
							else if (result.equals("-3")) {
								showErrorDialog("Server nicht erreichbar!\r\nEvtl. falsche URL verwendet...");
							}
							else if (result.equals("-4")) {
								showErrorDialog("Server nicht erreichbar!\r\nEvtl. kein Netz verfügbar...");
							}
							else {
								pruefungsfragenVorbereiten(result);
							}
						}

						protected void onProgressUpdate(String... progress) {
							updateWaitText(progress);
						}

						@Override
						protected String doInBackground(String... params) {
							String fragen = "";
							try {
								String user = params[0];
								String kennwort = params[1];
								
								// Das eingegebene Kennwort überprüfen
								publishProgress("prüfe Kennwort");
								String userHash = de.bw.jeans.webtools.Password.checkPassword(user, kennwort);
								if(userHash.equals("false")) {
									// User / Kennwort war falsch
									return "-1";
								}
								else if (userHash.equals("wrongURL")) {
									// Flasche URL angegeben
									return "-3";
								}
								else if (userHash.equals("noNet")) {
									// Keine Netzwerkverbindung vorhanden
									return "-4";
								}

								// Den benötigten Fragen laden
								publishProgress("lade Fragen");
								fragen = de.bw.jeans.webtools.Fragen.getFragekatalog(userHash);
								if(fragen.equals("false")) {
									// Fehler beim laden der Fragen
									return "-2";
								}
							}
							catch(Exception ex) {}
							
							return fragen;
						}
					}.execute(new String[] {prfUser.getText().toString(), prfPw.getText().toString()});
				}
			});
			
		} else {
			
		}
	}
	
	private void updateWaitText(String[] fortschritt) {
		pgDialog.setMessage(fortschritt[0]);
	}
	
	private void showErrorDialog(String errorText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(errorText);
		builder.setCancelable(true);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showHideOnlineTools(boolean show) {
		if(show) {
			linLay3.setVisibility(View.VISIBLE);
			linLay5.setVisibility(View.VISIBLE);
			btnImg1.setVisibility(View.VISIBLE);
			btn2.setVisibility(View.VISIBLE);		
			cb1.setVisibility(View.VISIBLE);
		}
		else {
			linLay3.setVisibility(View.GONE);
			linLay5.setVisibility(View.GONE);
			btnImg1.setVisibility(View.GONE);
			btn2.setVisibility(View.GONE);
			cb1.setVisibility(View.GONE);
		}
	}
	
	private void datenSichernTest() {
		Datenspeicher.setBenutzerGruppe(spiGruppe.getSelectedItem().toString());
		Datenspeicher.setAnzahlFragen(txtAnzahlFragen.getText().toString());
		UserEnvironmentTools.writeUserEnvironment();
	}
	
	private void datenSichernPruefung() {
		Datenspeicher.setBenutzerName(prfUser.getText().toString());
		Datenspeicher.setBenutzerKennwort(prfPw.getText().toString());
		Datenspeicher.setSavePassword(cb1.isChecked());
		
		UserEnvironmentTools.writeUserEnvironment();
	}

	private void pruefungsfragenVorbereiten(String webServiceErgebnis)
	{
		try 
		{
			String[] webWerte = webServiceErgebnis.split(",");
			
			// Die Daten in den Datenspeicher schreiben
			Datenspeicher.setPruefungsId(Integer.valueOf(webWerte[0]));
			Datenspeicher.setPruefungStartzeit(webWerte[1]);
			ArrayList<Integer> tmpListe = new ArrayList<Integer>();
			for(int i=2; i<webWerte.length; i++) {
				tmpListe.add(Integer.valueOf(webWerte[i]));
			}
			Datenspeicher.setPruefungsFragenIds(tmpListe);
			
			// Die Prüfung starten...
			Intent startePruefung = new Intent(StartScreenOffline.this, PruefungControllerActivity.class);
			startActivity(startePruefung);
		}
		catch(Exception ex)
		{
			String fehler = ex.getMessage();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
			  if(scanResult.getContents() != null && scanResult.getContents().toString().length() > 0) {
				  // Die eingelesenen Werte in den Datenspeicher schreiben
				  String[] webWerte = scanResult.getContents().split("~!~");
				  if(webWerte.length == 2) {
					  Datenspeicher.setBenutzerName(webWerte[0]);
					  Datenspeicher.setFfwServer(webWerte[1]);
					  prfUser.setText(webWerte[0]);
					  
					  Toast toast = Toast.makeText(StartScreenOffline.this, "Datenimport erfolgreich!", Toast.LENGTH_LONG);
					  toast.show();
				  }
				  else {
					  Toast toast = Toast.makeText(StartScreenOffline.this, "Fehler beim Einlesen des Codes...", Toast.LENGTH_LONG);
					  toast.show();
				  }
			  }
		  }
		  else {
			  // else continue with any other code you need in the method
			  Toast toast = Toast.makeText(StartScreenOffline.this, "Fehler beim Einlesen des Codes...", Toast.LENGTH_LONG);
			  toast.show();
		  }		  
	}
}
