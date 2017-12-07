import org.jgrapht.graph.DefaultWeightedEdge;

public class MyDefaultWeightedEdge extends DefaultWeightedEdge{
	
	/**
	 * Classe estesa per poter aggiungere la mossa
	 */
	private static final long serialVersionUID = 4919881627944123741L;
	
	Direzione direzione;
	
	public MyDefaultWeightedEdge(){
		super();
	}
	
	public void addMossaToEdge(Direzione m){
		this.direzione = m;
	}
	
	public String toString(){
		
		StringBuffer result = new StringBuffer();
		result.append("\nSOURCE = " + this.getSource() + ", TARGET = " + this.getTarget() + ", " 
				+ "peso edge = " + this.getWeight() + ", mossa = " + this.direzione.toString());
		return result.toString();
		
	}
}
