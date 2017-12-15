import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import MyUtilities.MyFormulas;

public class Rispref {
	
	public HashMap<Point, ArrayList<Point>> hmap;
	public HashMap<Point, ArrayList<Point>> hmapTemp;	//hash temporanea per contenere gli aggiornamenti durante l'esecuzione
	public Casella[][] spazio;
	public int numR, numC;
	public SettaCaselle settaCaselle;
	
	public Rispref(Casella[][] spazio, Point origine){
		this.settaCaselle = new SettaCaselle(spazio, origine);
		settaCaselle.risolutore();
		settaCaselle.stampaSpazioVerdiBianche();
		settaCaselle.printHMap();
		
		this.spazio = settaCaselle.spazio;
		this.hmap = settaCaselle.hmap;
		this.numR = spazio.length;
		this.numC = spazio[0].length;
	}

	public void risolutore(){
		controllaHashmapAttuale();
		//avantiDopoCopertura();
		avantiDopoCopertura_V2();
	}
	
	/*  Ha in input una casella d'angolo e la casella bianca adiacente diagonalmente
	 *  A seconda della posizione relativa tra le due coordinate controlla il quadrante corripondente
	 *  S'intende "quadrante" relativamente alla casella d'angolo*/
	public void copertura(Point coordAngolo, Point coordBianca){
		// Ianna	
		
		Direzione posRelativa = Direzione.getCurrespondentDirezione(coordAngolo, coordBianca);
		
		if(posRelativa == Direzione.NE) {
			controlloIQuadrante(coordBianca, coordAngolo);
			
		}else if(posRelativa == Direzione.NW) {
			controlloIIQuadrante(coordBianca, coordAngolo);
			
		}else if(posRelativa == Direzione.SW) {
			controlloIIIQuadrante(coordBianca, coordAngolo);
			
		}else if(posRelativa == Direzione.SE) {
			controlloIVQuadrante(coordBianca, coordAngolo);
			
		}else {
			System.out.println("Errore: nessun controllo di copertura selezionato.");
		}
		
	}
	
	// Prende iterativamente tutte le righe della hash, e per tutti i value di una chiave assegna la copertura
	public void controllaHashmapAttuale() {
		hmapTemp = new HashMap<Point, ArrayList<Point>>();
		//hmapTemp = hmap;
		hmapTemp = copiaHashmap(hmap);
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()) {
			// Per ogni casella d'angolo adiacente avvio il controllo copertura
			for(Point a: kv.getValue()) {
				copertura(a, kv.getKey());
				
			}
		}
		
		hmap = copiaHashmap(hmapTemp);	// le modifiche vengono attuate definitivamente nella hash
		
	}
	
	public void controlloIQuadrante(Point biancaIniziale, Point angoloCorrispondente) {
		int i = biancaIniziale.x;
		int j = biancaIniziale.y;
		
		//controllo diagonale
		while(i > -1 && j < numC && spazio[i][j].libera){ 
			// diagonale NE
			
			aggiornaHash(i, j, angoloCorrispondente);
			
			i--;
			j++;
		}
		
		// Riassegno il punto di partenza
		i = biancaIniziale.x;
		j = biancaIniziale.y;
		
		//controllo L
		while(i > - 1 && j < numC && spazio[i][j].libera){ //quadrante NE
			controllaEst(i, j, angoloCorrispondente);
			controllaNord(i - 1, j, angoloCorrispondente);
			i--;
			j++;
		}
		
	}
	
	public void controlloIIQuadrante(Point biancaIniziale, Point angoloCorrispondente) {
		int i = biancaIniziale.x;
		int j = biancaIniziale.y;
		
		//controllo diagonale
		while(i > -1 && j > -1 && spazio[i][j].libera){ 
			// diagonale NW
			
			aggiornaHash(i, j, angoloCorrispondente);
			
			i--;
			j--;
		}
		
		// Riassegno il punto di partenza
		i = biancaIniziale.x;
		j = biancaIniziale.y;
		
		//controllo L
		while(i > -1 && j > -1 && spazio[i][j].libera){
			controllaOvest(i, j, angoloCorrispondente);
			controllaNord(i - 1, j, angoloCorrispondente);
			i--;
			j--;
		}
		
	}
	
	public void controlloIIIQuadrante(Point biancaIniziale, Point angoloCorrispondente) {
		int i = biancaIniziale.x;
		int j = biancaIniziale.y;
		
		//controllo diagonale
		while(i < numR && j > -1 && spazio[i][j].libera){ 
			// diagonale NW
			
			aggiornaHash(i, j, angoloCorrispondente);
			
			i++;
			j--;
		}
		
		// Riassegno il punto di partenza
		i = biancaIniziale.x;
		j = biancaIniziale.y;
		
		//controllo L
		while(i < numR && j > -1 && spazio[i][j].libera){
			controllaOvest(i, j, angoloCorrispondente);
			controllaSud(i + 1, j, angoloCorrispondente);
			i++;
			j--;
		}
	}
	
	public void controlloIVQuadrante(Point biancaIniziale, Point angoloCorrispondente) {
		int i = biancaIniziale.x;
		int j = biancaIniziale.y;
		
		//controllo diagonale
		while(i < numR && j < numC && spazio[i][j].libera){ 
			// diagonale NW
			
			aggiornaHash(i, j, angoloCorrispondente);
			
			i++;
			j++;
		}
		
		// Riassegno il punto di partenza
		i = biancaIniziale.x;
		j = biancaIniziale.y;
		
		//controllo L
		while(i < numR && j < numC && spazio[i][j].libera){
			controllaEst(i, j, angoloCorrispondente);
			controllaSud(i + 1, j, angoloCorrispondente);
			i++;
			j++;
		}
	}
	
	public void controllaSud(int startRow, int startCol, Point angoloCorrispondente){ 
		while(startRow < numR && spazio[startRow][startCol].libera){ // asse sud
			aggiornaHash(startRow, startCol, angoloCorrispondente);
			startRow++;
		}
		
	}
	
	public void controllaNord(int startRow, int startCol, Point angoloCorrispondente){
		while(startRow > -1 && spazio[startRow][startCol].libera){ // asse nord
			aggiornaHash(startRow, startCol, angoloCorrispondente);
			startRow--;
		}
		
	}
	
	public void controllaEst(int startRow, int startCol, Point angoloCorrispondente){
		while(startCol < numC && spazio[startRow][startCol].libera){ // asse est
			aggiornaHash(startRow, startCol, angoloCorrispondente);
			startCol++;
		}
		
	}
	
	public void controllaOvest(int startRow, int startCol, Point angoloCorrispondente){
		while(startCol > -1 && spazio[startRow][startCol].libera){ // asse ovest
			aggiornaHash(startRow, startCol, angoloCorrispondente);
			startCol--;
		}
		
	}
	
	// Viene aggiornata la hashmap temporanea, che alla fine di copertura diventerà la nuova hash per la copertura successiva
	public void aggiornaHash(int i, int j, Point angoloCorrispondente) {
		if(!hmapTemp.containsKey(spazio[i][j].coordinata)) {
			ArrayList<Point> caselleAngoloRelative = new ArrayList<>();
			caselleAngoloRelative.add(angoloCorrispondente);
			hmapTemp.put(spazio[i][j].coordinata, caselleAngoloRelative);
		}else {
			ArrayList<Point> caselleAngoloRelative = hmapTemp.get(spazio[i][j].coordinata);
			if(!caselleAngoloRelative.contains(angoloCorrispondente)){
				caselleAngoloRelative.add(angoloCorrispondente);
				hmapTemp.replace(spazio[i][j].coordinata, caselleAngoloRelative);
			}
		}
	}
	
	/* Metodo per copiare una hashmap in modo "non shallow"
	 * In Java a quanto ho letto non esiste un modo "nativo" per farlo, senza iterare */
	public HashMap<Point, ArrayList<Point>> copiaHashmap(HashMap<Point, ArrayList<Point>> hashmapOriginale) {
		HashMap<Point, ArrayList<Point>> hashmapCopia = new HashMap<Point, ArrayList<Point>>();
		
	    for (Map.Entry<Point, ArrayList<Point>> entry : hashmapOriginale.entrySet()){
	    	hashmapCopia.put(entry.getKey(), new ArrayList<Point>(entry.getValue()));
	    }
	    
	    return hashmapCopia;
	}
	
	public void printHashmap(){
		String print = "";
		System.out.println("\nPrint della hashmap:");
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()){
			print = print + "\nChiave B = (" + kv.getKey().x + ", " + kv.getKey().y + ")"
					+ " Lista Valori = [ ";
			for(Point a: kv.getValue()){
				print = print + "(" + a.x + ", " + a.y + "), ";
			}
			
			print = print + " ]";
		}
		
		System.out.println(print);
	}
	
	// Assegna le PMP alle caselle bianche che hanno caselle d'angolo con la stessa PMP oppure
	/* se le caselle d'angolo hanno PMP diverse controlla qual è il dlib più corto e assegna la PMP della casella
	 * d'angolo corrispondente
	 * 
	 * NB: devo sempre assegnare dlib, quindi potrei semplificare questo metodo e chiamare valutaPMPeDlib per ogni situazione
	 * */
	public void avantiDopoCopertura(){
		//Rossana
		Direzione pmp = Direzione.d;
		ArrayList<Point> caselleAngolo;
		Point a;
		int i;
		boolean daValutare;

		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()){
			
			caselleAngolo = kv.getValue();
			pmp = Direzione.d;
			daValutare = false; // Se  alla fine del metodo è true, sarà da valutare la PMP di B
			
			
			
			if(caselleAngolo.size() > 1){
				
				for(i = 0; i < caselleAngolo.size(); i++){
					
					a = caselleAngolo.get(i);
					
					if(pmp.name().equalsIgnoreCase(Direzione.d.name()) 
							|| spazio[a.x][a.y].primaMossaRispref.name().equalsIgnoreCase(pmp.name())){
						
						pmp = spazio[a.x][a.y].primaMossaRispref;
						/*System.out.println("pmp = " + pmp.name() + " di A = " + a.x + ", " + a.y
						+ " direzione A = " + spazio[a.x][a.y].primaMossaRispref.name());*/
					} else{
						// Se scorrendo le caselle angolo di b ne trovo una con PMP diversa dalle altre devo valutare la PMP di b
						valutaPMPeDlib(kv.getKey(), caselleAngolo);
						// ed interrompo lo scorrimento delle caselle d'angolo
						i = caselleAngolo.size();
						daValutare = true;
					}
				}
			} else {
				pmp = spazio[caselleAngolo.get(0).x][caselleAngolo.get(0).y].primaMossaRispref;
				i = 1;
			}
			
			// Se scorrendo tutte le caselle angolo di b arrivo fino qui con daValutare false,
			// vuol dire che hanno tutte PMP uguale e la assegno a b:
			if(!daValutare){
				spazio[kv.getKey().x][kv.getKey().y].primaMossaRispref = pmp;
				System.out.println(" Ho assegnato pmp = " + pmp.name() + " di b = " + kv.getKey().x + ", " + kv.getKey().y);
				// e poi rimuovo questa riga da hmap, in modo che restino in hmap solo
				//hmap.remove(kv.getKey(), kv.getValue());
			}
		}
	}
	
	public void avantiDopoCopertura_V2() {
		ArrayList<Point> caselleAngolo;
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()) {
			caselleAngolo = kv.getValue();
			
			valutaPMPeDlib(kv.getKey(), caselleAngolo);
		}
	}
	
	
	// Calcola la dlib minima tra la casella bianca e tutte le sue angolo, poi assegna essa e la PMP dell'angolo corrispondente alla bianca
	public void valutaPMPeDlib(Point b, ArrayList<Point> caselleAngolo){
		double dlibMin = 9999;
		int indexAngoloMin = 0;
		
		for(int i = 0; i < caselleAngolo.size(); i++) {
			double dlibTemp = MyFormulas.dlibComputation(caselleAngolo.get(i), b);
			if(dlibTemp < dlibMin) {
				dlibMin = dlibTemp;
				indexAngoloMin = i;
			}
		}
		
		// assegno PMP e dlib alla casella bianca
		spazio[b.x][b.y].primaMossaRispref = spazio[caselleAngolo.get(indexAngoloMin).x][caselleAngolo.get(indexAngoloMin).y].primaMossaRispref;
		spazio[b.x][b.y].pesoCAMRispref = spazio[caselleAngolo.get(indexAngoloMin).x][caselleAngolo.get(indexAngoloMin).y].pesoCAMRispref + dlibMin;
		
	}
	
	public void stampaSpazioPM(){
		System.out.println("\nSpazio delle prime mosse di Rispref:");
		
		String mossa, print;
		for(int row = 0; row < spazio.length; row++) {
			for(int col = 0; col < spazio[0].length; col++) {
				if(spazio[row][col].isOrigine()) {
					System.out.print("[*O*]");
				}else if(spazio[row][col].isLibera() && !spazio[row][col].isOrigine()) {
					mossa = spazio[row][col].primaMossaRispref.name();
					if(mossa.length() == 1){
						print = "[ " + mossa + " ]";
					} else{
						print = "[ " + mossa + "]";
					}
					System.out.print(print);
				} else {
					System.out.print("[occ]");
				}
			}
			System.out.println();
		}
	}
	
	public void stampaSpazioDlib(){
		System.out.println("\nSpazio delle dlib di Rispref:");
		
		double dlib;
		for(int row = 0; row < spazio.length; row++) {
			for(int col = 0; col < spazio[0].length; col++) {
				if(spazio[row][col].isOrigine()) {
					System.out.print("[ *O* ]");
				}else if(spazio[row][col].isLibera() && !spazio[row][col].isOrigine()) {
					dlib = spazio[row][col].pesoCAMRispref;
					System.out.printf("[%1.3f]", dlib);
				} else {
					System.out.print("[ occ ]");
				}
			}
			System.out.println();
		}
	}
	
	
}