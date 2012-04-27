package de.bw.jeans;

import de.bw.jeans.helper.Datenspeicher;
import de.bw.jeans.helper.GoldAntwort;
import de.bw.jeans.helper.GoldFrage;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StelleGoldfrageActivity extends Activity {

	private GoldFrage gFrage;
	private GoldAntwort[] gAntworten;
	private LinearLayout linLay;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stelle_goldfrage);

		Bundle daten = getIntent().getExtras();
		Integer frageNummer = daten.getInt("ID");
		Integer anzahlFragen = daten.getInt("AnzahlFragen");

		gFrage = Datenspeicher.getGoldFrage(frageNummer);
		gAntworten = gFrage.getAllGoldAntworten();

		TextView tvUeberschrift = (TextView) findViewById(R.id.textView1);
		tvUeberschrift.setText("Frage Nr. " + frageNummer);

		linLay = (LinearLayout) findViewById(R.id.linearLayout1);

		// Die Frage dem LinearenLayout hinzufügen
		TextView tvFrage = new TextView(getApplicationContext());
		tvFrage.setText(gFrage.getFrageText());
		tvFrage.setPadding(0, 0, 0, 20);
		linLay.addView(tvFrage);

		// Das evtl. Bild wird dem Layout hinzugefügt
		if (gFrage.getBildName().length() > 0) {
			ImageView myPic = new ImageView(getApplicationContext());
			if (gFrage.getBildName().toLowerCase().equals("gf14.png")) {
				Bitmap bMap = BitmapFactory.decodeResource(getResources(),
						R.drawable.gf14);
				myPic.setImageBitmap(bMap);
			} else if (gFrage.getBildName().toLowerCase().equals("gf38.png")) {
				Bitmap bMap = BitmapFactory.decodeResource(getResources(),
						R.drawable.gf38);
				myPic.setImageBitmap(bMap);
			}
			linLay.addView(myPic);
		}

		// Die einzelnen Antworten werden dem LinearenLayout hinzugefügt
		for (int i = 0; i < gAntworten.length; i++) {
			CheckBox cb = new CheckBox(getApplicationContext());
			cb.setText(gAntworten[i].getAntwortText());
			if(gAntworten[i].getKandidatAntwort())
				cb.setChecked(true);
			linLay.addView(cb);
		}

		// Der 'Weiter'-Button wird bereitgestellt
		Button btnWeiter = new Button(getApplicationContext());
		btnWeiter.setText("   Weiter...   ");
		
		if(anzahlFragen == frageNummer) {
			btnWeiter.setText("  A B G E B E N ! !  ");
			btnWeiter.setBackgroundColor(Color.RED);
		}
		
		LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		myParams.setMargins(50, 30, 50, 0);
		btnWeiter.setLayoutParams(myParams);

		btnWeiter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				saveUserData(true);

				setResult(Activity.RESULT_OK);
				finish();
			}
		});

		// Der 'Weiter'-Button wird den LinLay hinzugefügt
		linLay.addView(btnWeiter);
	}

	@Override
	public void onBackPressed() {
		saveUserData(false);
		super.onBackPressed();
	}
		
	private void saveUserData(boolean frageBeantwortet) {
		
		int anzahl = linLay.getChildCount();
		int checkBoxCounter = 0;
		for (int i = 0; i < anzahl; i++) {
			if (linLay.getChildAt(i) instanceof CheckBox) {
				CheckBox cb = (CheckBox) linLay.getChildAt(i);
				gAntworten[checkBoxCounter].setKandidatAntwort(cb.isChecked());
				checkBoxCounter++;
			}
		}
		
		gFrage.addAllGoldAntworten(gAntworten);
		gFrage.setFrageBeantwortet(frageBeantwortet);
	}
}
