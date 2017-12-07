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
	public static int numR = 100;
	public static int numC = 50;
	public static final int xO = 50;
	public static final int yO = 25;
	public static final double percentualeOstacoliVoluta = 0.3;
	
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
	//	risgraf.stampaSpazioPM();
		
		SettaCaselle rispref = new SettaCaselle(space, new Point(xO, yO));
		rispref.risolutore();
		rispref.stampaSpazioVerdiBianche();
	//	rispref.stampaSpazioPMP();
		
		System.out.println(rispref.spazio[xO + 1][yO].tipologia);
		
		System.out.println(rispref.spazio[xO + 1][yO - 1].tipologia);
		
		System.out.println(rispref.spazio[xO + 1][yO + 1].tipologia);


	/**	Casella d = new Casella(false, true, 3, 3);
		Casella origine = space[xO][yO];
		
		System.out.println();
        System.out.println("Shortest path from " + origine.toString() + " to " + d.toString() + ": ");
        // questo metodo è deprecato..
        DijkstraShortestPath<Casella, MyDefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<>(grafo);
        
        SingleSourcePaths<Casella, DefaultWeightedEdge> iPaths = dijkstraAlg.getPaths(origine);
        
       System.out.println(iPaths.getPath(d) + "\n");
        System.out.println("Peso del percorso trovato: " + dijkstraAlg.getPathWeight(origine, d));*/
				
				
	}
}




