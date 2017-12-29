
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import MyUtilities.*;

public class Rispref {
	
	public static final double MAX_PESO_CAMP = Double.MAX_VALUE; //(1,7 * 10^308)
	
	public HashMap<Point, ArrayList<Point>> hmapCompleta;
	public HashMap<Point, ArrayList<Point>> hmap;
	public HashMap<Point, ArrayList<Point>> hmapTemp;	//hash temporanea per contenere gli aggiornamenti durante l'esecuzione
	public Casella[][] spazio;
	public int numR, numC, numCaselleBconPMP;
	public SettaCaselle settaCaselle;
	
	Stack<Point> biancheCoperte;
	
	public Rispref(Casella[][] spazio, Point origine){
		
		this.numR = spazio.length;
		this.numC = spazio[0].length;
		this.numCaselleBconPMP = 0;
		
		this.settaCaselle = new SettaCaselle(spazio, origine);
		
		this.hmap = null;
		
		
	}

	public void risolutore(){
		
		// Per prima cosa ricerca le caselle verdi/libere sugli assi, sulle diagonali e sulle L e le inserisce in caselleVerdi
		// oltre a riempire anche la pila di caselleBianche
		settaCaselle.scorriAssiOrigine();
		settaCaselle.scorriDiagonali();
		settaCaselle.scorriL();
		
		this.hmapCompleta = new HashMap<Point, ArrayList<Point>>(settaCaselle.caselleBianche.size(), 1.0f);
		
		// cerca le caselle d'angolo che soddisfano le 3 condizioni e le inserisce nella pila caselleAngolo
		hmap = settaCaselle.caselleAngolo(false, hmapCompleta, null); // passateSuccessive falso, hmap creato da settaCaselle ancora vuoto
		
		settaCaselle.settaDefaultPMP();
		settaCaselle.settaDefaultDlib();
		settaCaselle.stampaSpazioVerdiBianche();
			
		this.spazio = settaCaselle.spazio;// devo sovrascrivere spazio in Rispref perchè caselleAngolo aggiunge modifiche

		settaCaselle.printHMap();

		while(numCaselleBconPMP < settaCaselle.caselleBianche.size()){
			
			// per ogni casella bianca-angolo del mapping, aggiunge caselle bianche coperte da caselle d'angolo in hmap
			controllaHashmapAttuale_V2();
			//printhmap();
			//printhmapCompleta();
			
			settaCaselle.stampaSpazioVerdiBianche(); // utilizza hmap ricevuto da caselleAngolo
			// assegna la PMP alle caselle bianche che trova dentro hmap
			// utilizza hmap ricevuto da caselleAngolo e poi aggiorna hmapCompleta
			avantiDopoCopertura_V2();
			
			//calcola le caselle d'angolo partendo dalle casellebianche
			this.hmap = settaCaselle.caselleAngolo(true, hmapCompleta, this.spazio);
			this.spazio = settaCaselle.spazio; // devo sovrascrivere spazio in Rispref perchè caselleAngolo aggiunge modifiche

		}
		printhmapCompleta();
	}
	
	public void controllaHashmapAttuale_V2() {
		hmapTemp = new HashMap<Point, ArrayList<Point>>();
		hmapTemp = copiaHashmap(hmap);
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()) {
			// Per ogni casella d'angolo adiacente avvio il controllo copertura
			for(Point angolo: kv.getValue()) {
				//per ogni angolo devo considerare tutta la sua copertura
				this.biancheCoperte = new Stack<Point>();
				
				System.out.println("*** STO ESAMINANDO k = " + kv.getKey() + " a = " + angolo + " inserito in BIANCHECOP ***");
				biancheCoperte.push(new Point(kv.getKey().x, kv.getKey().y)); // inserisco la prima b coperta da questo angolo
				//printbiancheCoperte();
				
				copertura_V2(angolo, kv.getKey());
			}
		}
	//	printhmap();
	//	printhmaptemp();
		hmap = copiaHashmap(hmapTemp);	// le modifiche vengono attuate definitivamente nella hash
	}
	
	public void copertura_V2(Point coordAngolo, Point coordBianca){
		
		Direzione posRelativa = Direzione.fromAtob(coordAngolo, coordBianca);
		
		if(posRelativa.name().equalsIgnoreCase(Direzione.SE.name())) {
			System.out.println("\nMossa scelta tra a = " + coordAngolo + " b = " + coordBianca);
			funSE(coordBianca, coordAngolo, Direzione.SE);
			
		} else if(posRelativa.name().equalsIgnoreCase(Direzione.SW.name())){
			System.out.println("\nMossa scelta tra a = " + coordAngolo + " b = " + coordBianca);
			funSW(coordBianca, coordAngolo, Direzione.SW);
			
		} else if(posRelativa.name().equalsIgnoreCase(Direzione.NE.name())){
			System.out.println("\nMossa scelta tra a = " + coordAngolo + " b = " + coordBianca);
			funNE(coordBianca, coordAngolo, Direzione.NE);
			
		} else if(posRelativa.name().equalsIgnoreCase(Direzione.NW.name())){
			System.out.println("\nMossa scelta tra a = " + coordAngolo + " b = " + coordBianca);
			funNW(coordBianca, coordAngolo, Direzione.NW);
		} else {
			System.out.println("Errore: nessun controllo di copertura selezionato.");
		}
		
	}
	
	public void funSE(Point b, Point a, Direzione direzione){//SE : per ogni fun cambiano i check ed il prossimo temp
		boolean ok;
		
		int myX = spazio[b.x][b.y].coordinata.x;
		int myY = spazio[b.x][b.y].coordinata.y;
		Point temp = new Point();
		
		System.out.println("\n----funSE chiamata su " + b + " " + a + "----");
		
		while(biancheCoperte.size() > 0){
			checkEst(myX, myY, a);
			checkSud(myX, myY, a);
		
			myX++;
			myY++;
			temp.x = myX;
			temp.y = myY;
			System.out.println("New NEXT da esaminare = " + myX + ", " + myY);
			
			// se temp non è nello spazio/non è bianca, cerco un nuovo temp da biancheCoperte
			if(!isInsideSpace(myX, myY) || !spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
				ok = true;
				System.out.println("temp trovato AZZ.... ostacolo o fuori spazio!!!");
				
				do{
					temp = biancheCoperte.pop();
					myX = temp.x + 1;
					myY = temp.y + 1;
					//printbiancheCoperte();
					
					// se l'elemento considerato è nello spazio, è bianca e non già preso da hmapTemp con questo angolo
					if(isInsideSpace(myX, myY) && spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA) 
							&& !isInHtempwithA(myX, myY, a)){
						ok = false; // allora ho trovato il nuovo temp e posso interrompere questo do while
						insertIfNotExistbiancheCoperte(myX, myY);
						System.out.println("temp trovato da bianche coperte!!! " + myX + ", " + myY);
					}
				} while(ok && biancheCoperte.size() > 0);
			} else{
				System.out.println("temp trovato OK non ostacolo e dentro spazio");
				//insertIfNotExistbiancheCoperte(myX, myY);
				if(!isInHtempwithA(myX, myY, a)){
					biancheCoperte.push(new Point(myX, myY));
				}
			}
		}
		
		System.out.println("---- END funSE ----");
	}
	
	public void funSW(Point b, Point a, Direzione direzione){//SW
		boolean ok;
		
		int myX = spazio[b.x][b.y].coordinata.x;
		int myY = spazio[b.x][b.y].coordinata.y;
		Point temp = new Point();
		
		System.out.println("\n----funSW chiamata su " + b + " " + a + "----");
		
		while(biancheCoperte.size() > 0){
			checkWest(myX, myY, a);
			checkSud(myX, myY, a);
		
			myX++;
			myY--;
			temp.x = myX;
			temp.y = myY;
			System.out.println("New NEXT da esaminare = " + myX + ", " + myY);
			
			// se temp non è nello spazio/non è bianca, cerco un nuovo temp da biancheCoperte
			if(!isInsideSpace(myX, myY) || !spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
				ok = true;
				System.out.println("temp trovato AZZ.... ostacolo o fuori spazio!!!");
				
				do{
					temp = biancheCoperte.pop();
					myX = temp.x + 1;
					myY = temp.y - 1;
					//printbiancheCoperte();
					
					// se l'elemento considerato è nello spazio, è bianca e non già preso da hmapTemp con questo angolo
					if(isInsideSpace(myX, myY) && spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA) 
							&& !isInHtempwithA(myX, myY, a)){
						ok = false; // allora ho trovato il nuovo temp e posso interrompere questo do while
						insertIfNotExistbiancheCoperte(myX, myY);
						System.out.println("temp trovato da bianche coperte!!! " + myX + ", " + myY);
					}
				} while(ok && biancheCoperte.size() > 0);
			} else{
				System.out.println("temp trovato OK non ostacolo e dentro spazio");
				//insertIfNotExistbiancheCoperte(myX, myY);
				if(!isInHtempwithA(myX, myY, a)){
					biancheCoperte.push(new Point(myX, myY));
				}
			}
		}
		
		System.out.println("---- END funSW ----");
	}
	
	public void funNE(Point b, Point a, Direzione direzione){//NE
		boolean ok;
		
		int myX = spazio[b.x][b.y].coordinata.x;
		int myY = spazio[b.x][b.y].coordinata.y;
		Point temp = new Point();
		
		System.out.println("\n----funNE chiamata su " + b + " " + a + "----");
		
		while(biancheCoperte.size() > 0){
			checkEst(myX, myY, a);
			checkNord(myX, myY, a);
		
			myX--;
			myY++;
			temp.x = myX;
			temp.y = myY;
			System.out.println("New NEXT da esaminare = " + myX + ", " + myY);
			
			// se temp non è nello spazio/non è bianca, cerco un nuovo temp da biancheCoperte
			if(!isInsideSpace(myX, myY) || !spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
				ok = true;
				System.out.println("temp trovato AZZ.... ostacolo o fuori spazio!!!");
				
				do{
					temp = biancheCoperte.pop();
					myX = temp.x - 1;
					myY = temp.y + 1;
					//printbiancheCoperte();
					
					// se l'elemento considerato è nello spazio, è bianca e non già preso da hmapTemp con questo angolo
					if(isInsideSpace(myX, myY) && spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA) 
							&& !isInHtempwithA(myX, myY, a)){
						ok = false; // allora ho trovato il nuovo temp e posso interrompere questo do while
						insertIfNotExistbiancheCoperte(myX, myY);
						System.out.println("temp trovato da bianche coperte!!! " + myX + ", " + myY);
					}
				} while(ok && biancheCoperte.size() > 0);
			} else{
				System.out.println("temp trovato OK non ostacolo e dentro spazio");
				//insertIfNotExistbiancheCoperte(myX, myY);
				if(!isInHtempwithA(myX, myY, a)){
					biancheCoperte.push(new Point(myX, myY));
				}
			}
		}
		
		System.out.println("---- END funNE ----");
	}
	
	public void funNW(Point b, Point a, Direzione direzione){//NE
		boolean ok;
		
		int myX = spazio[b.x][b.y].coordinata.x;
		int myY = spazio[b.x][b.y].coordinata.y;
		Point temp = new Point();
		
		System.out.println("\n----funNW chiamata su " + b + " " + a + "----");
		
		while(biancheCoperte.size() > 0){
			checkWest(myX, myY, a);
			checkNord(myX, myY, a);
		
			myX--;
			myY--;
			temp.x = myX;
			temp.y = myY;
			System.out.println("New NEXT da esaminare = " + myX + ", " + myY);
			
			// se temp non è nello spazio/non è bianca, cerco un nuovo temp da biancheCoperte
			if(!isInsideSpace(myX, myY) || !spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
				ok = true;
				System.out.println("temp trovato AZZ.... ostacolo o fuori spazio!!!");
				
				do{
					temp = biancheCoperte.pop();
					myX = temp.x - 1;
					myY = temp.y - 1;
					//printbiancheCoperte();
					
					// se l'elemento considerato è nello spazio, non ostacolo e non già preso da hmapTemp con questo angolo
					if(isInsideSpace(myX, myY) && spazio[myX][myY].tipologia.equalsIgnoreCase(Casella.BIANCA) 
							&& !isInHtempwithA(myX, myY, a)){
						ok = false; // allora ho trovato il nuovo temp e posso interrompere questo do while
						insertIfNotExistbiancheCoperte(myX, myY);
						System.out.println("temp trovato da bianche coperte!!! " + myX + ", " + myY);
					}
				} while(ok && biancheCoperte.size() > 0);
			} else{
				System.out.println("temp trovato OK non ostacolo e dentro spazio");
				//insertIfNotExistbiancheCoperte(myX, myY);
				if(!isInHtempwithA(myX, myY, a)){
					biancheCoperte.push(new Point(myX, myY));
				}
			}
		}
		
		System.out.println("---- END funNW ----");
	}
	
	public void insertIfNotExistbiancheCoperte(int myX, int myY){
		if(!isInBiancheCoperte(myX, myY)){
			biancheCoperte.push(new Point(myX, myY));
			System.out.println("Inserito in biancheCoperte " + myX + ", " + myY);
			//printbiancheCoperte();
		}
	}
	
	public boolean isInHtempwithA(int myX, int myY, Point a){
		// Indica se in hmapTemp abbiamo già inserito la chiave con coordinate myX e myY con questo angolo associato a
		
		ArrayList<Point> lista;
		Point angolo;
		
		if(hmapTemp.containsKey(spazio[myX][myY].coordinata)){
			lista = hmapTemp.get(spazio[myX][myY].coordinata);
			
			for(int i = 0; i < lista.size(); i++){
				angolo = lista.get(i);
				if(angolo.x == a.x && angolo.y == a.y){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isInBiancheCoperte(int nextX, int nextY){
		// Indica se le coordinate passate sono attualmente presenti nello stack biancheCoperte
		Point biancaCoperta;
		
		for(int i = 0; i < biancheCoperte.size(); i++){
			biancaCoperta = biancheCoperte.get(i);
			if(biancaCoperta.x == nextX && biancaCoperta.y == nextY){
				return true;
			}
		}
		
		return false;
	}
	
	public void checkEst(int nextX, int nextY, Point a){
		// Continuo a scorrere verso est finchè trovo caselle nello spazio BIANCHE (non angolo, non verdi, non default)  
	/*	Point b = next;
		int x = b.x;
		int y = b.y;*/
		System.out.println("\ncheckEST called on b = " + nextX + ", " + nextY);
		
		while(nextY < numC && spazio[nextX][nextY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
			System.out.println("check est esamina " + spazio[nextX][nextY].toString());
			
		//	if(!isInBiancheCoperte(nextX, nextY)){ // se è in bianche coperte di sicuro è già in hTemp con questo angolo
		//		biancheCoperte.push(new Point(nextX, nextY));
//			insertIfNotExistbiancheCoperte(nextX, nextY);
			if(!isInHtempwithA(nextX, nextY, a)){
				insertIfNotExistbiancheCoperte(nextX, nextY);
			}
			System.out.println("check est ACCETTA HASH con b nuova est = " + nextX + ", " + nextY
						+ " E AGGIUNTA A BIANCHECOPERTE se non c'era già");
			aggiornaHash(nextX, nextY, a);
		//	}
			
			nextY++;
		}
	}
	
	public void checkSud(int nextX, int nextY, Point a){
		
		System.out.println("\ncheckSUD calledon b = " + nextX + ", " + nextY);
		
		while(nextX < numR && spazio[nextX][nextY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
			
			if(!isInHtempwithA(nextX, nextY, a)){
				insertIfNotExistbiancheCoperte(nextX, nextY);
			}
			System.out.println("check sud ACCETTA HASH con b nuova sud = " + nextX + ", " + nextY
						+ " E AGGIUNTA A BIANCHECOPERTE se non c'era già");
			aggiornaHash(nextX, nextY, a);
			nextX++;
		}
	}
	
	public void checkWest(int nextX, int nextY, Point a){
		System.out.println("\ncheckOVEST calledon b = " + nextX + ", " + nextY);
		
		while(nextY > -1 && spazio[nextX][nextY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
			
			if(!isInHtempwithA(nextX, nextY, a)){
				insertIfNotExistbiancheCoperte(nextX, nextY);
			}
			System.out.println("check ovest ACCETTA HASH con b nuova sud = " + nextX + ", " + nextY
						+ " E AGGIUNTA A BIANCHECOPERTE se non c'era già");
			aggiornaHash(nextX, nextY, a);
			nextY--;
		}
	}
	
	public void checkNord(int nextX, int nextY, Point a){
		System.out.println("\ncheckNORD calledon b = " + nextX + ", " + nextY);
		
		while(nextX > -1 && spazio[nextX][nextY].tipologia.equalsIgnoreCase(Casella.BIANCA)){
			
			if(!isInHtempwithA(nextX, nextY, a)){
				insertIfNotExistbiancheCoperte(nextX, nextY);
			}
			System.out.println("check nord ACCETTA HASH con b nuova sud = " + nextX + ", " + nextY
						+ " E AGGIUNTA A BIANCHECOPERTE se non c'era già");
			aggiornaHash(nextX, nextY, a);
			nextX--;
		}
	}
	
	// Viene aggiornata la hashmap temporanea, che alla fine di copertura diventerà la nuova hash per la copertura successiva
	public void aggiornaHash(int i, int j, Point angoloCorrispondente) {
		
		Point nuovo = spazio[i][j].coordinata;
		ArrayList<Point> lista;
		if(hmapTemp.containsKey(nuovo)){
			lista = hmapTemp.get(nuovo);
			if(!lista.contains(angoloCorrispondente)){
				lista.add(angoloCorrispondente);
				hmapTemp.replace(spazio[i][j].coordinata, lista);
				spazio[i][j].addAngolo(angoloCorrispondente);
			}
		} else{
			lista = new ArrayList<Point>();
			lista.add(angoloCorrispondente);
			hmapTemp.put(nuovo, lista);
			spazio[i][j].addAngolo(angoloCorrispondente);
		}
		
		lista = null;
		

	//	printhmaptemp();
	//	printhmap();
		
	}
	
	public boolean isInsideSpace(int x, int y){
		if(x > - 1 && y > - 1 && x < numR && y < numC){
			return true;
		} else{
			return false;
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
	
	public void printbiancheCoperte(){
		System.out.println("\nBIANCHE COPERTE: ");
		for(int i = 0; i < biancheCoperte.size(); i++){
			System.out.println(biancheCoperte.get(i));
		}
	}
	
	public void printhmaptemp(){
		String print = "\n\nTHIS IS TEMP\n";
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmapTemp.entrySet()){
			print = print + "\n Chiave B = (" + kv.getKey().x + ", " + kv.getKey().y + ")"
					+ " Lista Valori = [ ";
			for(Point a: kv.getValue()){
				print = print + "(" + a.x + ", " + a.y + "), ";
			}
			
			print = print + " ]";
		}
		
		System.out.println(print);
	}
	
	public void printhmapCompleta(){
		String print = "\n\nTHIS IS COMPLETTTTTTTTTTTTTTTTTE\n";
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmapCompleta.entrySet()){
			print = print + "\n Chiave B = (" + kv.getKey().x + ", " + kv.getKey().y + ")"
					+ " Lista Valori = [ ";
			for(Point a: kv.getValue()){
				print = print + "(" + a.x + ", " + a.y + "), ";
			}
			
			print = print + " ]";
		}
		
		System.out.println(print);
	}
	
	public void printhmap(){
		String print = "\n\nTHIS IS HMAP\n";
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()){
			print = print + "\nCOMPLETA Chiave B = (" + kv.getKey().x + ", " + kv.getKey().y + ")"
					+ " Lista Valori = [ ";
			for(Point a: kv.getValue()){
				print = print + "(" + a.x + ", " + a.y + "), ";
			}
			
			print = print + " ]";
		}
		
		System.out.println(print);
	}
	
	

	public void avantiDopoCopertura_V2() {
		ArrayList<Point> caselleAngolo;
		
		// Scorrere hmap va bene ma dovrebbe considerare le NUOVE caselle
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()) {
			caselleAngolo = kv.getValue();
			
			// se non hanno ancora assegnata una PMP, è la prima volta che trovo questa b e la analizzo
			if(spazio[kv.getKey().x][kv.getKey().y].primaMossaRispref.name().equalsIgnoreCase(Direzione.d.name())){
				
				valutaPMPeDlib_V2(kv.getKey(), caselleAngolo, MAX_PESO_CAMP);
			} else{
			/**
			 *  se ho già incontrato questa b, le ho assegnato già una PMP
			 *  MA se considerassi solamente hmap, che ogni volta che aggiungo caselle angolo si resetta,
			 *  potrei considerare SOLO le nuove caselle d'angolo dedicate a b che ho appena salvato in hmap
			 *  dimenticando però quelle vecchie!
			 *  
			 *  Quindi dovrei esaminare l'attuale dlib salvata in spazio[b.x][b.y].pesoCAMPRispref
			 *  se è minore dei dlib rispetto alle nuove caselle angolo trovate in hmap
			 */
				valutaPMPeDlib_V2(kv.getKey(), caselleAngolo, spazio[kv.getKey().x][kv.getKey().y].pesoCAMRispref);
			}
		}
		
		appendiHMap();
	}
	
	public void valutaPMPeDlib_V2(Point b, ArrayList<Point> caselleAngolo, double dlibMin){
		double dlibTemp;
		int indexAngoloMin = 0;
		Point angolo;
		boolean dlibMinCambiato = false;
				
		System.out.println("WOWOWOWOWOWOWOWO sto calcolando PMP per " + b.x + ", " + b.y + " con dlibINIZIALE = " + dlibMin
				+ " WOWOWOWOWOWOWOWO");
		
		for(int i = 0; i < caselleAngolo.size(); i++) {
			angolo = caselleAngolo.get(i);
				
			// dlib da A fino a b
			dlibTemp = MyFormulas.dlibComputation(caselleAngolo.get(i), b);
			// viene considerata anche la dlib da O ad A
			dlibTemp = dlibTemp + spazio[angolo.x][angolo.y].pesoCAMRispref;
			
			System.out.println("Ho trovato pesoCAMP = " + dlibTemp);
			
			if(dlibTemp < dlibMin) {
				System.out.println("YATTAAAAAAAAAAAAAA!!!! " + dlibTemp + " è minore di min = " + dlibMin);
				
				if(dlibMin == MAX_PESO_CAMP){
					numCaselleBconPMP++;
				}
				
				dlibMin = dlibTemp;
				indexAngoloMin = i;
				dlibMinCambiato = true;
			}
		}
		
		// assegno PMP e dlib alla casella bianca
		if(dlibMinCambiato){
			spazio[b.x][b.y].primaMossaRispref = spazio[caselleAngolo.get(indexAngoloMin).x][caselleAngolo.get(indexAngoloMin).y].primaMossaRispref;
			spazio[b.x][b.y].pesoCAMRispref = /*spazio[caselleAngolo.get(indexAngoloMin).x][caselleAngolo.get(indexAngoloMin).y].pesoCAMRispref +*/
					dlibMin;
		}
	}
	
	
	// Calcola la dlib minima tra la casella bianca e tutte le sue angolo, poi assegna essa e la PMP dell'angolo corrispondente alla bianca
	public void valutaPMPeDlib(Point b, ArrayList<Point> caselleAngolo){
		double dlibMin = 999999999;
		double dlibTemp;
		int indexAngoloMin = 0;
		Point angolo;
		
		//if(spazio[b.x][b.y].primaMossaRispref.name().equalsIgnoreCase(Direzione.d.name())){
		
			for(int i = 0; i < caselleAngolo.size(); i++) {
				angolo = caselleAngolo.get(i);
				
				// dlib da A fino a b
				dlibTemp = MyFormulas.dlibComputation(caselleAngolo.get(i), b);
				// viene considerata anche la dlib da O ad A
				dlibTemp = dlibTemp + spazio[angolo.x][angolo.y].pesoCAMRispref;
				
				if(dlibTemp < dlibMin) {
					dlibMin = dlibTemp;
					indexAngoloMin = i;
				}
			}
		
			// assegno PMP e dlib alla casella bianca
			spazio[b.x][b.y].primaMossaRispref = spazio[caselleAngolo.get(indexAngoloMin).x][caselleAngolo.get(indexAngoloMin).y].primaMossaRispref;
			spazio[b.x][b.y].pesoCAMRispref = /*spazio[caselleAngolo.get(indexAngoloMin).x][caselleAngolo.get(indexAngoloMin).y].pesoCAMRispref +*/
					dlibMin;
			numCaselleBconPMP++;
		//}
		
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
	
	public void appendiHMap(){
		// Appende in hmapCompleta tutto quello che era presente in hmap
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()){
			hmapCompleta.put(kv.getKey(), kv.getValue());
		}
		
	}


}