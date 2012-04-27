package de.bw.jeans.helper;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class TmpFileHilfen {

	// Filename der Datei, in der die nicht übertragenen Prüfungen gepuffert werden
	private static String TMP_FILE_NAME = "/data/data/de.bw.jeans/databases/prfPuffer.txt";
	
	public static void addPruefung(String prfDaten) {
		try {
			FileWriter fw = new FileWriter(new File(TMP_FILE_NAME), true);
			fw.append(prfDaten);
			fw.close();
		}
		catch (Exception ex) {
			
		}
	}
	
	public static Integer getAnzahlPruefungen() {
		ArrayList<String> inhalt = readFile();
		return inhalt.size();
	}
	
	public static ArrayList<String> readFile() {
		File file = new File(TMP_FILE_NAME);
		ArrayList<String> inhalt = new ArrayList<String>();
		
		try {
			if(file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(fin);
				String line;
				
				while ((line = dis.readLine()) != null) {
					inhalt.add(line);
				}
				
				dis.close();
				fin.close();
			}
		}
		catch(Exception ex) {}
		
		return inhalt;
	}
	
}
