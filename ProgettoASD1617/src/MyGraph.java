import java.util.ArrayList;
import java.util.Set;
import org.jgrapht.graph.SimpleWeightedGraph;

public class MyGraph {

	SimpleWeightedGraph<Casella, MyDefaultWeightedEdge> grafo;
	Casella[][] spazio;
	
	public MyGraph(Casella[][] space){
		this.grafo = new SimpleWeightedGraph<Casella, MyDefaultWeightedEdge>(MyDefaultWeightedEdge.class);
		this.spazio = space;
	}
	
	public SimpleWeightedGraph<Casella, MyDefaultWeightedEdge> createMyGraph() {
		
		aggiuntaVertici();	//aggiungo tutte le caselle, che sono i miei vertici del grafo
		aggiuntaEdges();	//qui devo aggiungere tutti gli edge, ovvero le connessioni tra le caselle nel grafo
		
		return grafo;
	}
	
	public void printGraph(){ // voglio vedere se riesco da un NODO ad ottenere gli edge di cui è target : poi da lì recuperi la mossa inversa
		Set<Casella> vertici = grafo.vertexSet();
		Set<MyDefaultWeightedEdge> edges;
		ArrayList<Casella> vertexList = new ArrayList<>();
		ArrayList<MyDefaultWeightedEdge> edgeList = new ArrayList<>();
		vertexList.addAll(vertici);
		
		System.out.println("-------------------------------------------------------------------");
		
		for(Casella c: vertici){
			edges = grafo.edgesOf(c);
			edgeList.addAll(edges);
			
			System.out.println("***VERTEX: " + c.coordinata.toString() + " numero edges = " + edgeList.size() + " ***");
			
			for(MyDefaultWeightedEdge e: edgeList){
				System.out.println( "EDGE: " + e.toString() + "\n");
			}
			
			edgeList.removeAll(edgeList);
		}
	}
	
	/*
	 * Controllo se queste due caselle sono entrambe libere*/
	public boolean controlloCoppiaCaselleLibere(Casella pA, Casella pB) {
		if(pA.libera && pB.libera) {
			return true;
		}else {
			return false;
		}
	}
	
	public void aggiuntaEdges() {
		
		Casella p1, p2, p3, p4;
		//i -1 servono per far quadrare il kernel con i bordi
		/*
		 * Legenda posizioni kernel:
		 * [p1][p2]
		 * [p3][p4]
		 * 
		 * */
		for(int i = 0; i < spazio.length - 1; i++) {
			for(int j = 0; j < spazio[0].length - 1; j++) {
				
				p1 = spazio[i][j];
				p2 = spazio[i][j + 1];
				p3 = spazio[i + 1][j];
				p4 = spazio[i + 1][j + 1];
				
				//solo per due casi particolari su sei controllo anche se l'edge è già contenuto nel grafo
				addEdgeControl(p1, p2, false);
				
				addEdgeControl(p3, p4, true);
				
				//solo per due casi particolari su sei controllo anche se l'edge è già contenuto nel grafo
				addEdgeControl(p1, p3, false);
				
				addEdgeControl(p2, p4, true);
				
				addEdgeControl(p1, p4, true);
				
				addEdgeControl(p3, p2, true);
				
			}
		}
		
	}
	
	public void addEdgeControl(Casella p1, Casella p2, boolean noControl){
		MyDefaultWeightedEdge e;
		if(!noControl){ // se voglio un controllo
			noControl = !grafo.containsEdge(p1, p2);
		} else{
			noControl = true;
		}
		
		// se non voglio un controllo, la seconda parte del && è sempre true
		if(controlloCoppiaCaselleLibere(p1, p2) && noControl) {
			e = grafo.addEdge(p1, p2);
			e.addMossaToEdge(Direzione.getCurrespondentDirezione(p1.coordinata, p2.coordinata));
			grafo.setEdgeWeight(e, e.direzione.getDistanza());
			
		/*	System.out.println("p1 = " + p1.coordinata.toString() + " p2 = " + p2.coordinata.toString());
			System.out.println(e.toString());*/
		}
	}
	
	public void aggiuntaVertici() {
		
		for(int row = 0; row < spazio.length; row++) {
			for(int col = 0; col < spazio[0].length; col++) {
				
				if(spazio[row][col].libera) {
					
					grafo.addVertex(spazio[row][col]);
				}
			}
		}		
	}
}
