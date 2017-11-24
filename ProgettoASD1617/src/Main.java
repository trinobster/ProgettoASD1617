import java.awt.Dimension;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import org.jgraph.JGraph;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.awt.geom.Point2D;
import java.lang.reflect.GenericArrayType;


public class Main {
	
	public static ArrayList<Point2D> liberateAtt = new ArrayList<Point2D>();
	public static ArrayList<Point2D> liberateGlob = new ArrayList<Point2D>();
	
	public static void main(String[] args) {
		
		// Questi saranno gli input dell'utente
		System.out.println("Ciaone");
		final int numR = 100;
		final int numC = 200;
		final int xO = 90;
		final int yO = 190;
		final double percentualeOstacoliVoluta = 0.50;
		
		int numCaselleTot = numR*numC;
		int numOstacoliVoluti = (int) (numCaselleTot*percentualeOstacoliVoluta);
		int numOstacoliDaTogliere = numCaselleTot - numOstacoliVoluti - 1;	//il -1 è per l'origine

		System.out.println("caselle totali: "+numCaselleTot+", numero ostacoli a cui arrivare: "+numOstacoliVoluti+"\n");
		
		// Inizializzo l'array bidimensionale dello caselle
		Casella[][]spazio = new Casella[numR][numC];
		creaSpazioIniziale(spazio, xO, yO);
		
		//aggiungo l'origine alla lista globale di caselle libere e aggiorno anche la casella
		aggiungiOrigine(xO, yO, spazio);
		
		numOstacoliDaTogliere = primoPercorso(xO, yO, numOstacoliDaTogliere, spazio);
		percorsiLiberatori(numOstacoliDaTogliere, spazio);
		
		System.out.println("Lista posizioni libere: "+liberateGlob);
		System.out.println("Dimensione lista liberate globali: "+liberateGlob.size());
		aggiornaCaselle(spazio);
		stampaSpazio(spazio);
		
		//fase di creazione del grafo
		SimpleWeightedGraph<Casella, DefaultWeightedEdge> grafoSpazio = new SimpleWeightedGraph<Casella, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		grafoSpazio = creaGrafo(grafoSpazio, spazio);
		
		// Prints the shortest path from vertex i to vertex c. This certainly
        // exists for our particular directed graph.
		
		Casella d = casellaLiberaRandom(spazio);
		Casella origine = spazio[xO][yO];
		
		System.out.println();
        System.out.println("Shortest path from "+origine.toString()+" to "+d.toString()+": ");
        DijkstraShortestPath<Casella, DefaultWeightedEdge> dijkstraAlg =
            new DijkstraShortestPath<>(grafoSpazio);
        SingleSourcePaths<Casella, DefaultWeightedEdge> iPaths = dijkstraAlg.getPaths(origine);
        System.out.println(iPaths.getPath(d) + "\n");
        System.out.println("Peso del percorso trovato: "+dijkstraAlg.getPathWeight(origine, d));
				
				
	}
	
	public static void percorsiLiberatori(int numOstacoliDaTogliere, Casella[][]spazio) {
		Random random = new Random();
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
			
			numOstacoliDaTogliere = secondiPercorsi(rX, rY, numOstacoliDaTogliere, spazio);
			
		}
		
	}
	
	public static int primoPercorso(int rowPrec, int colPrec, int numOstacoliDaTogliere, Casella[][]spazio) {
		Random random = new Random();
		
		while(numOstacoliDaTogliere > 0) {
			int randomNumber = random.nextInt(8);
			
			Direzione dirProx = Direzione.values()[randomNumber];
			int rowProx = rowPrec + dirProx.getDirX();
			int colProx = colPrec + dirProx.getDirY();
			
			Point2D p = new Point2D.Double(rowProx,colProx);
			
			if(rowProx < 0 || rowProx >= spazio.length || colProx < 0 || colProx >= spazio[0].length) {
				//System.out.println("["+rowProx+","+colProx+"], fuori spazio.");
				break;
			} else {
				if(verificaCoordinateLiberaGlob(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione già libera.");
				} else {
					//System.out.println("["+rowProx+","+colProx+"], posizione liberata.");
					liberateGlob.add(p);
					numOstacoliDaTogliere--;
				}
			}
			rowPrec = rowProx;
			colPrec = colProx;
			
		}
		
		return numOstacoliDaTogliere;
		
	}
	
	public static int secondiPercorsi(int rowPrec, int colPrec, int numOstacoliDaTogliere, Casella[][]spazio) {
		Random random = new Random();
		boolean terminazioneConnessa = false;
		
		while(numOstacoliDaTogliere > 0) {
			int randomNumber = random.nextInt(8);
			
			Direzione dirProx = Direzione.values()[randomNumber];
			int rowProx = rowPrec + dirProx.getDirX();
			int colProx = colPrec + dirProx.getDirY();
			
			Point2D p = new Point2D.Double(rowProx,colProx);
			
			if(rowProx < 0 || rowProx >= spazio.length || colProx < 0 || colProx >= spazio[0].length) {
				//System.out.println("["+rowProx+","+colProx+"], fuori spazio.");
				
			} else {
				if(verificaCoordinateLiberaGlob(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione già liberata da ricerca precedente, fine ricerca.");
					terminazioneConnessa = true;
					
					rowPrec = rowProx;
					colPrec = colProx;
					break;
				}else if(verificaCoordinateLiberaAtt(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione già liberata da ricerca attuale, refresh della posizione in lista.");
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
			
			Point2D p = new Point2D.Double(rowProx,colProx);
			
			if(rowProx < 0 || rowProx >= spazio.length || colProx < 0 || colProx >= spazio[0].length) {
				//System.out.println("["+rowProx+","+colProx+"], fuori spazio.");
			} else {
				if(verificaCoordinateLiberaGlob(p)) {
					//System.out.println("["+rowProx+","+colProx+"], terminato percorso connesso, fine ultima ricerca.");
					terminazioneConnessa = true;
					break;
				}else if(verificaCoordinateLiberaAtt(p)) {
					//System.out.println("["+rowProx+","+colProx+"], posizione già liberata da ricerca attuale, refresh della posizione in lista.");
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
		
		for(Point2D nuovaL : liberateAtt) {
			liberateGlob.add(nuovaL);
		}
		liberateAtt.clear();
		
		return numOstacoliDaTogliere;
	}
	
	public static void aggiungiOrigine(int x, int y, Casella[][]spazio) {
		spazio[x][y].setOrigine(true);
		Point2D p = new Point2D.Double(x,y);
		liberateGlob.add(p);
	}
	
	/*
	 * Inizializza lo spazio con origine
	 * */
	public static void creaSpazioIniziale(Casella[][]spazioBid, int xO, int yO) {
		for(int i = 0; i < spazioBid.length; i++) {
			for(int j = 0; j < spazioBid[0].length; j++) {	
				Casella c = new Casella(false, false, i, j);
				spazioBid[i][j] = c;
			}
		}
		
		spazioBid[xO][yO].setOrigine(true);
	}
	/*
	 * Aggiorno le caselle da liberare basandomi sulla lista liberateGlob*/
	public static void aggiornaCaselle(Casella[][]spazio) {
		for(int i=0; i<liberateGlob.size(); i++) {
			Point2D posizione = liberateGlob.get(i);
			spazio[(int)(posizione.getX())][(int)(posizione.getY())].setLibera(true);
		}
	}
	
	
	/*
	 * Fai una rappresentazione grafica dello spazio*/
	public static void stampaSpazio(Casella[][]spazio) {
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
	
	/*
	 * Verifico se un punto a cui arrivo è già una casella liberata da altre ricerche*/
	public static boolean verificaCoordinateLiberaGlob(Point2D point) {
		boolean giàLibera = false;
		for(Point2D coordLib : liberateGlob) {
			if(point.equals(coordLib)) {
				giàLibera = true;
				break;
			}
		}
		return giàLibera;
	}
	
	/*
	 * Verifico se un punto a cui arrivo è già una casella liberata da altre ricerche*/
	public static boolean verificaCoordinateLiberaAtt(Point2D point) {
		boolean giàLibera = false;
		for(Point2D coordLib : liberateAtt) {
			if(point.equals(coordLib)) {
				giàLibera = true;
				break;
			}
		}
		return giàLibera;
	}
	
	/*
	 * Funzione che serve prima del refresh della posizione*/
	public static void eliminaPosizioneDuplicata(Point2D point) {
		
		for(Point2D coordLib : liberateAtt) {
			if(point.equals(coordLib)) {
				liberateAtt.remove(coordLib);
				break;
			}
		}
		
	}
	
	public static Casella casellaLiberaRandom(Casella[][]spazio) {
		Random random = new Random();
		
		int rN = random.nextInt(liberateGlob.size());
		return spazio[(int)(liberateGlob.get(rN).getX())][(int)(liberateGlob.get(rN).getY())];
		
	}
	
	/*
	 * Inizializzo il mio grafo*/
	public static SimpleWeightedGraph<Casella, DefaultWeightedEdge> creaGrafo(SimpleWeightedGraph<Casella, DefaultWeightedEdge> grafo, Casella[][]spazio) {
		
		grafo = aggiuntaVertici(grafo, spazio);		//aggiungo tutte le caselle, che sono i miei vertici del grafo
		
		grafo = aggiuntaEdges(grafo, spazio);		//qui devo aggiungere tutti gli edge, ovvero le connessioni tra le caselle nel grafo
		
		return grafo;
		
	}
	
	/*
	 * Aggiungo tutti i vertici al mio grafo*/
	public static SimpleWeightedGraph<Casella, DefaultWeightedEdge> aggiuntaVertici(SimpleWeightedGraph<Casella, DefaultWeightedEdge> grafo, Casella[][]spazio) {
		for(int row=0; row<spazio.length; row++) {
			for(int col=0; col<spazio[0].length; col++) {
				if(spazio[row][col].isLibera()) {
					grafo.addVertex(spazio[row][col]);
				}
			}
		}
		return grafo;
		
	}
	
	public static SimpleWeightedGraph<Casella, DefaultWeightedEdge> aggiuntaEdges(SimpleWeightedGraph<Casella, DefaultWeightedEdge> grafo, Casella[][]spazio) {
		
		//i -1 servono per far quadrare il kernel con i bordi
		/*
		 * Legenda posizioni kernel:
		 * [p1][p2]
		 * [p3][p4]
		 * 
		 * */
		for(int i=0; i<spazio.length-1; i++) {
			for(int j=0; j<spazio[0].length-1; j++) {
				Casella p1 = spazio[i][j];
				Casella p2 = spazio[i][j+1];
				Casella p3 = spazio[i+1][j];
				Casella p4 = spazio[i+1][j+1];
				
				
				//solo per due casi particolari su sei controllo anche se l'edge è già contenuto nel grafo
				if(controlloCoppiaCaselleLibere(p1, p2) && !grafo.containsEdge(p1, p2)) {
					DefaultWeightedEdge e = grafo.addEdge(p1, p2);
					grafo.setEdgeWeight(e, 1);
				}
				if(controlloCoppiaCaselleLibere(p3, p4)) {
					DefaultWeightedEdge e = grafo.addEdge(p3, p4);
					grafo.setEdgeWeight(e, 1);
				}
				if(controlloCoppiaCaselleLibere(p1, p3) && !grafo.containsEdge(p1, p3)) {
					DefaultWeightedEdge e = grafo.addEdge(p1, p3);
					grafo.setEdgeWeight(e, 1);
				}
				if(controlloCoppiaCaselleLibere(p2, p4)) {
					DefaultWeightedEdge e = grafo.addEdge(p2, p4);
					grafo.setEdgeWeight(e, 1);
				}
				if(controlloCoppiaCaselleLibere(p1, p4)) {
					DefaultWeightedEdge e = grafo.addEdge(p1, p4);
					grafo.setEdgeWeight(e, Math.sqrt(2));
				}
				if(controlloCoppiaCaselleLibere(p3, p2)) {
					DefaultWeightedEdge e = grafo.addEdge(p3, p2);
					grafo.setEdgeWeight(e, Math.sqrt(2));
				}
				
			}
		}
		
		return grafo;
	}
	
	/*
	 * Controllo se queste due caselle sono entrambe libere*/
	public static boolean controlloCoppiaCaselleLibere(Casella pA, Casella pB) {
		if(pA.isLibera() && pB.isLibera()) {
			return true;
		}else {
			return false;
		}
	}
	
	
}




