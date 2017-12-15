import java.awt.Point;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Risgraf {
	SimpleWeightedGraph<Casella, MyDefaultWeightedEdge> graph;
	MySpace mySpace;
	
	public Risgraf(SimpleWeightedGraph<Casella, MyDefaultWeightedEdge> graph, MySpace mySpace){
		this.graph = graph;
		this.mySpace = mySpace;
		System.out.println("\nRISOLUTORE RISGRAF creato");
	}

	public Casella[][] risolutore(){
		GraphPath<Casella, MyDefaultWeightedEdge> path;
		Casella destinazione; // false origine true libera
		Casella secondaCasella;
		List<Casella> vertexList; // lista di vertici del path
		Direzione primaMossa;
		
		// Per ogni casella libera dello spazio che non sia l'origine
		for(Point libera: mySpace.liberateGlob){
			
			//System.out.println("\n-----Libera = " + libera.toString() + "-----");
			
			if(!(libera.x == mySpace.xO && libera.y == mySpace.yO)){
				destinazione = mySpace.spazio[libera.x][libera.y];
		//		System.out.println("\n-----DESTINAZIONE = " + destinazione.toString() + "-----");
				
				// cerca un path di minima lunghezza tra origine e destinazione
				path = DijkstraShortestPath.findPathBetween(graph, mySpace.spazio[mySpace.xO][mySpace.yO], destinazione);
		//		System.out.println("Peso path = " + path.getWeight());
				// salva il peso del CAM nella matrice spazio, casella corrispondente destinazione
				mySpace.spazio[destinazione.numRiga][destinazione.numColonna].pesoCAMRisgraf = path.getWeight();
				
				// ottiene i vertici del percorso nell'ordine del percorso
				vertexList = path.getVertexList();
				
				// ottiene la seconda casella nel percorso per ricavare la PRIMA MOSSA dall'origine alla seconda casella
				secondaCasella = vertexList.get(1);
				primaMossa = Direzione.getCurrespondentDirezione
						(mySpace.spazio[mySpace.xO][mySpace.yO].coordinata, secondaCasella.coordinata);
				
		//		System.out.println("Prima mossa = " + primaMossa.toString());
				
				// salva la PRIMA MOSSA OTTIMALE nella matrice spazio
				mySpace.spazio[destinazione.numRiga][destinazione.numColonna].primaMossaRisgraf = primaMossa;
				
			}
		}
		
		return mySpace.spazio;
	}
	
	public void stampaSpazioPM(){
		System.out.println("Spazio delle prime mosse di Risgraf:");
		
		String mossa, print;
		Casella[][] spazio = mySpace.spazio;
		
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
	}
}
