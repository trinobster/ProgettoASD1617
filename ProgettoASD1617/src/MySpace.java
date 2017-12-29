import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class MySpace {
	
	public Casella[][] spazio;
	public int xO;
	public int yO;
	public int numR;
	public int numC;
	public int numOstacoliDaTogliere;
	public Random random;
	
	public ArrayList<Point> liberateGlob;
	public ArrayList<Point> liberateAtt;
	
	public MySpace(int xO, int yO, int numR, int numC, double percentualeOstacoliVoluta){
		this.xO = xO;
		this.yO = yO;
		this.numR = numR;
		this.numC = numC;
		
		this.random = new Random();
		
		liberateGlob = new ArrayList<>();
		liberateAtt = new ArrayList<>();
		
		spazio = new Casella[numR][numC];
		
		int numCaselleTot = numR * numC;
		int numOstacoliVoluti = (int) (numCaselleTot * percentualeOstacoliVoluta);
		numOstacoliDaTogliere = numCaselleTot - numOstacoliVoluti - 1;	//il -1 Ë per l'origine
		
		System.out.println("caselle totali: " + numCaselleTot+", numero ostacoli a cui arrivare: " + numOstacoliVoluti+"\n");
	}
	
	public Casella casellaLiberaRandom() {
		
		int rN = random.nextInt(liberateGlob.size());
		return spazio[(int)(liberateGlob.get(rN).getX())][(int)(liberateGlob.get(rN).getY())];
		
	}
	
	public Casella[][] createMySpace(){
		
		creaSpazioIniziale();
		//aggiungo l'origine alla lista globale di caselle libere e aggiorno anche la casella
		aggiungiOrigine();
		
		primoPercorso();
		percorsiLiberatori();
		
		System.out.println("Lista posizioni libere: " + liberateGlob);
		System.out.println("Dimensione lista liberate globali: " + liberateGlob.size());
		
		aggiornaCaselle();
		
		return spazio;
	}
	
	/*
	 * Inizializza lo spazio con origine
	 * */
	public void creaSpazioIniziale() {
		for(int i = 0; i < spazio.length; i++) {
			for(int j = 0; j < spazio[0].length; j++) {	
				Casella c = new Casella(false, false, i, j);
				spazio[i][j] = c;
			}
		}
		
		spazio[xO][yO].setOrigine(true);
	}
	
	public void aggiungiOrigine() {
		spazio[xO][yO].setOrigine(true);
		Point p = new Point(xO, yO);
		liberateGlob.add(p);
	}
	
	public void primoPercorso() {
		int rowProx, colProx, randomNumber;
		int rowPrec = xO;
		int colPrec = yO;
		Direzione dirProx;
		Point p;
		
		while(numOstacoliDaTogliere > 0) {
			randomNumber = random.nextInt(8);
			
			dirProx = Direzione.values()[randomNumber];
			rowProx = rowPrec + dirProx.getDirX();
			colProx = colPrec + dirProx.getDirY();
			
			p = new Point(rowProx,colProx);
			
			if(rowProx < 0 || rowProx >= spazio.length || colProx < 0 || colProx >= spazio[0].length) {
				//System.out.println("["+rowProx+","+colProx+"], fuori spazio.");
				break;
			} else {
				if(verificaCoordinateLiberaGlob(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione gi‡ libera.");
				} else {
					//System.out.println("["+rowProx+","+colProx+"], posizione liberata.");
					liberateGlob.add(p);
					numOstacoliDaTogliere--;
				}
			}
			rowPrec = rowProx;
			colPrec = colProx;
			
		}
				
	}

	/*
	 * Verifico se un punto a cui arrivo Ë gi‡ una casella liberata da altre ricerche*/
	public boolean verificaCoordinateLiberaGlob(Point2D point) {
		boolean gi‡Libera = false;
		for(Point2D coordLib : liberateGlob) {
			if(point.equals(coordLib)) {
				gi‡Libera = true;
				break;
			}
		}
		return gi‡Libera;
	}
	
	public void percorsiLiberatori() {
		
		boolean startChange = true;		//deve diventare false, ovvero deve arrivare a rappresentare un punto occupato
		
		while(numOstacoliDaTogliere > 0) {
			//System.out.println("Lista posizioni libere: "+liberateGlob);
			//System.out.println("numOstacoliDaTogliere: "+numOstacoliDaTogliere);
			int rX = 0;
			int rY = 0;
			Point2D newStart = null;
			
			//prendo una posizione random, libera o occupata che sia
			rX = random.nextInt(spazio.length);
			rY = random.nextInt(spazio[0].length);
			//System.out.println("Nuovo start: ["+rX+","+rY+"]");
			
			numOstacoliDaTogliere = secondiPercorsi(rX, rY);
		}
	}
	
	public int secondiPercorsi(int rowPrec, int colPrec ) {
		boolean terminazioneConnessa = false;
		
		while(numOstacoliDaTogliere > 0) {
			int randomNumber = random.nextInt(8);
			
			Direzione dirProx = Direzione.values()[randomNumber];
			int rowProx = rowPrec + dirProx.getDirX();
			int colProx = colPrec + dirProx.getDirY();
			
			Point p = new Point(rowProx,colProx);
			
			if(rowProx < 0 || rowProx >= spazio.length || colProx < 0 || colProx >= spazio[0].length) {
				//System.out.println("["+rowProx+","+colProx+"], fuori spazio.");
				
			} else {
				if(verificaCoordinateLiberaGlob(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione gi‡ liberata da ricerca precedente, fine ricerca.");
					terminazioneConnessa = true;
					
					rowPrec = rowProx;
					colPrec = colProx;
					break;
					
				}else if(verificaCoordinateLiberaAtt(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione gi‡ liberata da ricerca attuale, refresh della posizione in lista.");
					eliminaPosizioneDuplicata(p);
					liberateAtt.add(p);
					
					rowPrec = rowProx;
					colPrec = colProx;
					
				} else {
					//System.out.println("["+rowProx+","+colProx+"], posizione liberata");
					liberateAtt.add(p);
					numOstacoliDaTogliere--;
					
					rowPrec = rowProx;
					colPrec = colProx;
				}
			}
			
		}
		
		while(terminazioneConnessa == false) {
			int randomNumber = random.nextInt(8);
			
			Direzione dirProx = Direzione.values()[randomNumber];
			int rowProx = rowPrec + dirProx.getDirX();
			int colProx = colPrec + dirProx.getDirY();
			
			Point p = new Point(rowProx,colProx);
			
			if(rowProx < 0 || rowProx >= spazio.length || colProx < 0 || colProx >= spazio[0].length) {
				//System.out.println("["+rowProx+","+colProx+"], fuori spazio.");
			} else {
				if(verificaCoordinateLiberaGlob(p)) {
					//System.out.println("["+rowProx+","+colProx+"], terminato percorso connesso, fine ultima ricerca.");
					terminazioneConnessa = true;
					break;
				}else if(verificaCoordinateLiberaAtt(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione gi‡ liberata da ricerca attuale, refresh della posizione in lista.");
					eliminaPosizioneDuplicata(p);
					liberateAtt.add(p);
					
					rowPrec = rowProx;
					colPrec = colProx;
					
				} else {
					//System.out.println("["+rowProx+","+colProx+"], aggiunta e rimozione FIFO...");
					liberateAtt.remove(0);	//rimuovo il primo point2d della lista
					liberateAtt.add(p);
					//System.out.println("liberateAtt: "+liberateAtt);
					rowPrec = rowProx;
					colPrec = colProx;
				}
			}
			
		}
		
		for(Point nuovaL : liberateAtt) {
			liberateGlob.add(nuovaL);
		}
		liberateAtt.clear();
		
		return numOstacoliDaTogliere;
	}
	
	/*
	 * Funzione che serve prima del refresh della posizione*/
	public void eliminaPosizioneDuplicata(Point2D point) {
		
		for(Point2D coordLib : liberateAtt) {
			if(point.equals(coordLib)) {
				liberateAtt.remove(coordLib);
				break;
			}
		}
		
	}
	
	/*
	 * Verifico se un punto a cui arrivo Ë gi‡ una casella liberata da altre ricerche*/
	public boolean verificaCoordinateLiberaAtt(Point2D point) {
		boolean gi‡Libera = false;
		for(Point2D coordLib : liberateAtt) {
			if(point.equals(coordLib)) {
				gi‡Libera = true;
				break;
			}
		}
		return gi‡Libera;
	}
	
	/*
	 * Aggiorno le caselle da liberare basandomi sulla lista liberateGlob*/
	public void aggiornaCaselle() {
		for(int i = 0; i < liberateGlob.size(); i++) {
			Point2D posizione = liberateGlob.get(i);
			spazio[(int)(posizione.getX())][(int)(posizione.getY())].setLibera(true);
		}
	}
	
	/*
	 * Fai una rappresentazione grafica dello spazio*/
	public void stampaSpazio() {
		for(int row = 0; row < spazio.length; row++) {
			for(int col = 0; col < spazio[0].length; col++) {
				if(spazio[row][col].isOrigine()) {
					System.out.print("[ O ]");
				}else if(spazio[row][col].isLibera() && !spazio[row][col].isOrigine()) {
					System.out.print("[   ]");
				} else {
					System.out.print("[occ]");
				}
			}
			System.out.println();
			
		}
	}
	
/*	public void stampaSpazioPM(Casella[][] spazio){
		String mossa, print;
		
		for(int row = 0; row < spazio.length; row++) {
			for(int col = 0; col < spazio[0].length; col++) {
				if(spazio[row][col].isOrigine()) {
					System.out.print("[*O*]");
				}else if(spazio[row][col].isLibera() && !spazio[row][col].isOrigine()) {
					mossa = spazio[row][col].primaMossaRisgraf.name();
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
	}*/
}
