package de.bw.jeans;

import de.bw.jeans.helper.Datenspeicher;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FwServerErfassenActivity extends Activity {

	EditText eText;
	Button btnAbbrechen;
	Button btnSpeichern;
	Button btnScan;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fw_server_erfassen);
	    
	    btnAbbrechen = (Button) findViewById(R.id.button2);
	    btnSpeichern = (Button) findViewById(R.id.button1);
	    
	    eText = (EditText) findViewById(R.id.editText1);
	    eText.setText(Datenspeicher.getFfwServer_Short());
	    
	    TextView tv1 = (TextView) findViewById(R.id.textView1);
	    tv1.setText("FFW-Server erfassen");
	    
	    
	    // Der Event-Listener für 'Abbrechen'
	    btnAbbrechen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	    
	    // Der Event-Listener für 'Speichern'
	    btnSpeichern.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Datenspeicher.setFfwServer(eText.getText().toString());
				finish();
			}
		});
	}
}
