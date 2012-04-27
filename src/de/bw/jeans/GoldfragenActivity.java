package de.bw.jeans;

import de.bw.jeans.R;
import de.bw.jeans.helper.Datenspeicher;
import de.bw.jeans.helper.TimeTicker;
import de.bw.jeans.helper.UserEnvironmentTools;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GoldfragenActivity extends Activity {
	
	private Thread secondCounter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Den Context der App ablegen
        Datenspeicher.setAppContext(getApplicationContext());
        
        // Den 'START'-Button definieren
        Button myStartButton = (Button) findViewById(R.id.button1);
        myStartButton.setText("S T A R T");
        myStartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserEnvironmentTools.readUserEnvironment();
				
				// Den Startschirm anzeigen
				Intent newStart = new Intent(GoldfragenActivity.this, StartScreenOffline.class);
				startActivity(newStart);
			}
		});
    }
    
    protected void onStart() {
    	super.onStart();
    	

        // Den Thread für das zählen der Sekunden starten
        secondCounter = new Thread(new TimeTicker());
        secondCounter.start();
    }
    
    protected void onStop() {
    	super.onStop();
    	
    	// Den Zählthread wieder anhalten...
    	try {
    		secondCounter.stop();
    	}
    	catch (Exception ex) {}
    }
}