import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rispref {
	
	public HashMap<Point, ArrayList<Point>> hmap;
	public HashMap<Point, ArrayList<Point>> hmapTemp;	//hash temporanea per contenere gli aggiornamenti durante l'esecuzione
	public Casella[][] spazio;
	public int numR, numC;
	
	public Rispref( HashMap<Point, ArrayList<Point>> hmap, Casella[][] spazio){
		this.hmap = hmap;
		this.spazio = spazio;
		this.numR = spazio.length;
		this.numC = spazio[0].length;
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
	
	
	
	
	
	
	
	
	public void avantiDopoCopertura(){
		//Rossana
	}
	
	
	
}
