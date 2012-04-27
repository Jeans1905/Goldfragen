package de.bw.jeans.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PruefungsDatenTools {

	private static final String fileName = "prfData.txt";
	
	private static String[] readFile() {
		File datei = Datenspeicher.getAppContext().getFileStreamPath(fileName);
		if(datei.canRead()) {
			try {
				FileInputStream iStream = new FileInputStream(datei);
				InputStreamReader iStreamReader = new InputStreamReader(iStream);
				BufferedReader bReader = new BufferedReader(iStreamReader);
				
				ArrayList<String> workList = new ArrayList<String>();
				String line;
				while ((line = bReader.readLine()) != null) {
					workList.add(line);
		        }
				
				iStream.close();
				
				String[] rcString = new String[workList.size()];
				for(int i=0; i<workList.size(); i++) {
					rcString[i] = workList.get(i);
				}
				return rcString;
			}
			catch (Exception ex) {
				return new String[0];
			}
			
		}
		else{
			return new String[0];
		}
	}

	public static boolean addToFile(String zeile) {
		String[] orgFile = readFile();
		File datei = Datenspeicher.getAppContext().getFileStreamPath(fileName);
		
		try {
			FileWriter fWriter = new FileWriter(datei);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			for(int i=0; i<orgFile.length; i++) {
				bWriter.write(orgFile[i]);
				bWriter.newLine();
			}
			bWriter.write(zeile);
			bWriter.close();
			
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}
	
}
