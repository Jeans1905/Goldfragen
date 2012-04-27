package de.bw.jeans;

import java.util.ArrayList;

import de.bw.jeans.helper.Datenspeicher;
import de.bw.jeans.helper.GoldAntwort;
import de.bw.jeans.helper.GoldFrage;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ZeigeFehlerActivity extends Activity {

	ArrayList<GoldFrage> alleFalschenGoldFragen = new ArrayList<GoldFrage>();
	int aktFrageNummer = 0;
	int anzFalscheFragen = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stelle_goldfrage);

		// Suchen der nicht korrekt beantworteten Fragen
		for (GoldFrage gFrage : Datenspeicher.getAllGoldfragen()) {
			if (!gFrage.getFrageBeantwortet() || !gFrage.getFrageKorrekt()) {
				alleFalschenGoldFragen.add(gFrage);
			}
			else if (Datenspeicher.getShowCorrectAlso()) {
				alleFalschenGoldFragen.add(gFrage);
			}
		}

		// Die Werte in die Klasse schreiben
		aktFrageNummer = 0;
		anzFalscheFragen = alleFalschenGoldFragen.size();

		// Die erste, falsche Frage anzeigen
		this.schreibeFrage();
		// schreibeFrage();
	}

	private void schreibeFrage() {
		// Einlesen der Daten
		GoldFrage gFrage = alleFalschenGoldFragen.get(aktFrageNummer);

		// Ausgabe der Überschrift
		TextView tvUeberschrift = (TextView) findViewById(R.id.textView1);
		tvUeberschrift.setText("Fehler Nr. " + (aktFrageNummer + 1));

		// Das LinearLayout einlesen
		final LinearLayout linLay = (LinearLayout) findViewById(R.id.linearLayout1);
		linLay.removeAllViews();

		// Die Frage ausgeben
		TextView gFrageText = new TextView(getApplicationContext());
		gFrageText.setText(gFrage.getFrageText());
		gFrageText.setPadding(0, 0, 0, 20);
		linLay.addView(gFrageText);

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

		// Die gegebenen Antworten ausgeben
		for (int i = 0; i < gFrage.getAllGoldAntworten().length; i++) {
			CheckBox cb = new CheckBox(getApplicationContext());
			cb.setEnabled(false);
			GoldAntwort gAntwort = gFrage.getGoldAntwort(i + 1);
			cb.setText(gAntwort.getAntwortText());

			if (gAntwort.getKandidatAntwort()) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}

			if (gAntwort.getAntwortKorrekt() == gAntwort.getKandidatAntwort()) {
				cb.setTextColor(Color.WHITE);
			} else {
				cb.setTextColor(Color.RED);
			}

			linLay.addView(cb);
		}
		
		RelativeLayout relLay = new RelativeLayout(this);
		
		ImageButton iButtonLeft = new ImageButton(this);
		iButtonLeft.setImageResource(R.drawable.pfeil_links);
		if (aktFrageNummer == 0) {
			//iButtonLeft.setEnabled(false);
			iButtonLeft.setVisibility(View.GONE);
		} else {
			//iButtonLeft.setEnabled(true);
			iButtonLeft.setVisibility(View.VISIBLE);
		}
		iButtonLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				aktFrageNummer--;
				schreibeFrage();
			}
		});
		RelativeLayout.LayoutParams iButtonLeftParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iButtonLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		iButtonLeftParams.leftMargin = 40;
		relLay.addView(iButtonLeft, iButtonLeftParams);
		
		ImageButton iButtonRight = new ImageButton(this);
		iButtonRight.setImageResource(R.drawable.pfeil_rechts);
		if ((aktFrageNummer + 1) == alleFalschenGoldFragen.size()) {
			//iButtonRight.setEnabled(false);
			iButtonRight.setVisibility(View.GONE);
		} else {
			//iButtonRight.setEnabled(true);
			iButtonRight.setVisibility(View.VISIBLE);
		}
		iButtonRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				aktFrageNummer++;
				schreibeFrage();
			}
		});
		RelativeLayout.LayoutParams iButtonRightParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iButtonRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		iButtonRightParams.rightMargin = 40;
		relLay.addView(iButtonRight, iButtonRightParams);
		
		
		linLay.addView(relLay);
	}
}
