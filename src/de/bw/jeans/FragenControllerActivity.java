package de.bw.jeans;

import java.util.ArrayList;

import de.bw.jeans.helper.Datenspeicher;
import de.bw.jeans.helper.FragenAuswerten;
import de.bw.jeans.helper.GoldFrage;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class FragenControllerActivity extends Activity {

	int mainCounter = 0;
	FragenAuswerten myAuswertung;
	CheckBox showCorrektAlso;
	Button showErrors;
	ArrayList<GoldFrage> mainList = new ArrayList<GoldFrage>();
	ArrayList<GoldFrage> gFragen = new ArrayList<GoldFrage>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragen_controller);
		
		if(mainList.size() == 0) {
			gFragen = Datenspeicher.getAllGoldfragen();
			mainList = gFragen;
		}
		else {
			gFragen = mainList;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
				
		int counter = 0;
		boolean nextFrage = false;
		for (GoldFrage goldFrage : gFragen) {
			counter++;
			if (!goldFrage.getFrageBeantwortet()) {
				nextFrage = true;
				mainCounter = counter;
				mainList = gFragen;
				break;
			}
		}

		if (nextFrage) {
			// Es wurde eine neue, bisher nicht gestellte Frage gefunden...
			Intent newGoldFrage = new Intent(FragenControllerActivity.this, StelleGoldfrageActivity.class);
			newGoldFrage.putExtra("ID", counter);
			newGoldFrage.putExtra("AnzahlFragen", mainList.size());
			startActivityForResult(newGoldFrage, 1);
		} else {
			// Es wurden alle Fragen gestellt...
			
			
					
			// Zeit, das Ergebnis auszuwerten
			showCorrektAlso = (CheckBox) findViewById(R.id.checkBox1);
			showCorrektAlso.setChecked(Datenspeicher.getShowCorrectAlso());
			
			myAuswertung = new FragenAuswerten();
			TextView prozAngabe = (TextView) findViewById(R.id.textView3);
			prozAngabe.setText(myAuswertung.getProzentKorrekt() + " %");
			prozAngabe.setTextSize(70);
			TextView infoText = (TextView) findViewById(R.id.textView4);
			if (myAuswertung.getTestBestanden()) {
				// Der Test war erfolgreich!
				infoText.setText("erreicht und somit die Prüfung bestanden!");
				prozAngabe.setTextColor(Color.GREEN);
			} else {
				// Der Test war NICHT erfolgreich...
				infoText.setText("erreicht und somit die Prüfung nicht bestanden!");
				prozAngabe.setTextColor(Color.RED);
			}

			showErrors = (Button) findViewById(R.id.button2);
			showErrors.setWidth(350);
			showErrors.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Die falsch beantworteten Fragen sollen angezeigt werden
					Intent myIntent = new Intent(FragenControllerActivity.this, ZeigeFehlerActivity.class);
					startActivity(myIntent);
				}
			});

			switchButtonShowErrors();

			Button tryAgain = (Button) findViewById(R.id.button1);
			tryAgain.setWidth(350);
			tryAgain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			
			showCorrektAlso.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Datenspeicher.setShowCorrectAlso(showCorrektAlso.isChecked());
					switchButtonShowErrors();
				}
			});
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
				else
					finish();
			}
		}
	}

	private void switchButtonShowErrors() {
		if (myAuswertung.getProzentKorrekt() < 100 || showCorrektAlso.isChecked()) {
			//showErrors.setVisibility(View.VISIBLE);
			showErrors.setEnabled(true);
		} else {
			//showErrors.setVisibility(View.GONE);
			showErrors.setEnabled(false);
		}
	}

}
