package de.bw.jeans.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatenbankHilfen extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/de.bw.jeans/databases/";
    private static String DB_NAME = "goldfragen.db";
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
    
	public DatenbankHilfen(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		
		try {
			createDataBase();
		}
		catch(Exception ex) {
			
		}
		
	}

	public void createDataBase() throws IOException {

    	boolean dbExist = checkDataBase();

    	if(dbExist){
    		//do nothing - database already exist
    	}else{
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();

        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
	
    private boolean checkDataBase() {

    	SQLiteDatabase checkDB = null;

    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}

    	if(checkDB != null){
    		checkDB.close();
    	}

    	return checkDB != null ? true : false;
    }
    
    private void copyDataBase() throws IOException {

    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);

    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;

    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);

    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}

    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
    
    public void openDataBase() throws SQLException{
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    
    @Override
 	public synchronized void close() {
     	    if(myDataBase != null)
     		    myDataBase.close();
     	    super.close();
 	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	
	public ArrayList<Integer> getAllFrageIds(int GruppenId) {
		
		ArrayList<Integer> tmpListe = new ArrayList<Integer>();
		
		// Öffnen der Datenbank
		openDataBase();

		Cursor zeiger = myDataBase.rawQuery("SELECT _id FROM fragen WHERE gruppe=?", new String[] {String.valueOf(GruppenId)});
		while(zeiger.moveToNext()) {
			tmpListe.add(zeiger.getInt(0));
		}
		
		// Schließen der Datenbank
		close();
		
		return tmpListe;
	}
	
	public void writeToDatenspeicher(Integer[] fragenIds) {
		
		// Die bisherigen Daten aus dem Speicher löschen
		Datenspeicher.removeAllGoldfragen();
		
		// Öffnen der Datenbank
		openDataBase();
		
		// Schreiben der Daten in den Datenspeicher
		for (Integer fragenId : fragenIds) {
			GoldFrage gFrage = new GoldFrage();
			
			// Die Daten zur Frage ermitteln
			Cursor zeiger = myDataBase.rawQuery("SELECT text, bildid FROM fragen WHERE _id=?", new String[] {String.valueOf(fragenId)});
			zeiger.moveToFirst();
			gFrage.setFrageId(fragenId);
			gFrage.setFrageText(zeiger.getString(0));
			gFrage.setBildName(zeiger.getString(1));
			
			// Die Antworten zur Frage raussuchen
			ArrayList<GoldAntwort> gAntwortenArray = new ArrayList<GoldAntwort>();
			zeiger = myDataBase.rawQuery("SELECT _id, antwort, antwort_korrekt FROM antworten WHERE frage=? ORDER BY _id", new String[] {String.valueOf(fragenId)});
			while(zeiger.moveToNext()) {
				GoldAntwort gAntwort = new GoldAntwort();
				gAntwort.setAntwortText(zeiger.getString(1));
				gAntwort.setAntwortKorrekt(getBooleanValue(zeiger.getInt(2)));
				gAntwort.setAntwortId(zeiger.getString(0));
				gAntwortenArray.add(gAntwort);
			}
			
			// Die Reihenfolge der Fragen festlegen
			ArrayList<Integer> newReihenfolge = new ArrayList<Integer>();
			
			Random ra = new Random();
			
			// Alle Werte in einen Pool schreiben
			int anzahlAntwort = gAntwortenArray.size();
			ArrayList<Integer> antwortPool = new ArrayList<Integer>();
			for(int i=0; i<anzahlAntwort; i++) {
				antwortPool.add(i);
			}
			
			// Die Reihenfolge festsetzen
			for(int i=0; i<anzahlAntwort; i++) {
				int zufallszahl = ra.nextInt(antwortPool.size());
				newReihenfolge.add(antwortPool.get(zufallszahl));
				antwortPool.remove(zufallszahl);
			}
			
			gFrage.setAntwortenReihenfolge(newReihenfolge);
			
			// Die Antworten in der Reihenfolge neuen Reihenfolge sortieren
			for(int i=0; i<newReihenfolge.size(); i++) {
				gFrage.addGoldAntwort(gAntwortenArray.get(newReihenfolge.get(i)));
			}
			
			Datenspeicher.addGoldfrage(gFrage);
		}
		
		// Schließen der Datenbank
		close();
	}
	
	private static boolean getBooleanValue(int wert) {
		return (wert == 0) ? false : true;
	}
}
