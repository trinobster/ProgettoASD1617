import java.util.ArrayList;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import MyUtilities.InputDati;

import java.util.List;
import java.awt.Point;
import java.awt.geom.Point2D;


public class Main {
	
	public static final String MSG_INSRT_MAX_R = "\nInserire massimo numero righe ";
	public static final String MSG_INSRT_MAX_C = "\nInserire massimo numero colonne ";
	public static final String MSG_INSRT_X_ORIGIN = "\nInserire x origine ";
	public static final String MSG_INSRT_Y_ORIGIN = "\nInserire y origine ";
	public static final String MSG_INSRT_PERC_OSTACOLI = "\nInserire percentuale ostacoli ";
	
	public static ArrayList<Point2D> liberateAtt = new ArrayList<Point2D>();
	public static ArrayList<Point2D> liberateGlob = new ArrayList<Point2D>();
	
	// Questi saranno gli input dell'utente
	public static int numR = 8;
	public static int numC = 10;
	public static final int xO = 3;
	public static final int yO = 3;
	public static final double percentualeOstacoliVoluta = 0.5;
	
	public static void main(String[] args) {
		
	/**	0. Questo è per quando l'utente inserirà da console i parametri	
	 * numR = InputDati.leggiIntero(MSG_INSRT_MAX_R);
		numC = InputDati.leggiIntero(MSG_INSRT_MAX_C);*/
		
		// 1. Creazione dello spazio
		MySpace mySpace = new MySpace(xO, yO, numR, numC, percentualeOstacoliVoluta);
		Casella[][] space = mySpace.createMySpace();
		//mySpace.stampaSpazio();
		
		// 2. Creazione del grafo
		MyGraph myGraph = new MyGraph(space);
		SimpleWeightedGraph<Casella, MyDefaultWeightedEdge> graph = myGraph.createMyGraph();
	//	myGraph.printGraph();
		
		// 3. Utilizzo di Risgraf per determinare PM e pesi minimi associati a tutte le caselle dello spazio
		Risgraf risgraf = new Risgraf(graph, mySpace);
		risgraf.risolutore();
		risgraf.stampaSpazioPM();
		
		
		// 4. Parte di Rispref
		SettaCaselle settaCaselle = new SettaCaselle(space, new Point(xO, yO));
		settaCaselle.risolutore();
		//rispref.stampaSpazioVerdiBianche();
		settaCaselle.stampaSpazioVerdiBianche();
		settaCaselle.printHMap();
		settaCaselle.stampaSpazioPMP();
		
		// Assegnamento della prima generazione di coperture
		Rispref rispref = new Rispref(settaCaselle.hmap, space);
		rispref.controllaHashmapAttuale();
		rispref.printHashmap();
		
		/* Qui bisogna trovare le caselle d'angolo di seconda, terza ecc. generazione, intervallando ogni volta
		 * l'assegnamento della n-esima generazione di coperture, fino a quando ogni casella bianca fa parte 
		 * di almeno una copertura
		*/
		
				
	}
}




