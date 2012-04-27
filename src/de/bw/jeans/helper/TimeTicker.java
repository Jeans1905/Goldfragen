package de.bw.jeans.helper;

public class TimeTicker implements Runnable {

	@Override
	public void run() {
		while(true) {
			// Während der Prüfung die Sekunden zählen...
			if(Datenspeicher.getPruefungLaeuft()) {
				
				if(Datenspeicher.getPruefungDauer() <= Datenspeicher.getMaxPruefdauer()) {
					Datenspeicher.setPruefungDauerAddSecond();
				}
				else {
					// TODO: Aktion nach Ablauf der Prüfungszeit erstellen!!
					
				}
			}
			
			try {
				Thread.sleep(1000);
			}
			catch(Exception ex) {}
		}
	}
}
