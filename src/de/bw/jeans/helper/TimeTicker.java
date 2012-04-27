package de.bw.jeans.helper;

public class TimeTicker implements Runnable {

	@Override
	public void run() {
		while(true) {
			// W�hrend der Pr�fung die Sekunden z�hlen...
			if(Datenspeicher.getPruefungLaeuft()) {
				
				if(Datenspeicher.getPruefungDauer() <= Datenspeicher.getMaxPruefdauer()) {
					Datenspeicher.setPruefungDauerAddSecond();
				}
				else {
					// TODO: Aktion nach Ablauf der Pr�fungszeit erstellen!!
					
				}
			}
			
			try {
				Thread.sleep(1000);
			}
			catch(Exception ex) {}
		}
	}
}
