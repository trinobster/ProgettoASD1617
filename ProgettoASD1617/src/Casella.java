import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class Casella{
	public static final String DEFAULT = "default";
	public static final String VERDE = "verde";
	public static final String BIANCA = "bianca";
	public static final String ANGOLO = "angolo";
	
	public boolean origine;
	public boolean libera;
	public int numRiga, numColonna;
	public Point coordinata;
	public Direzione primaMossaRisgraf;
	public Direzione primaMossaRispref;
	public double pesoCAMRisgraf;
	public String tipologia;
	public ArrayList<Point> coordinateAngoliAssociati = null; // serve solo alle caselle bianche
	//public ArrayList<MyUtils.Direzione> direzioniDisponibili;
	
	public Casella(boolean origine, boolean libera, int numRiga, int numColonna) {
		this.origine = origine;
		this.libera = libera;
		this.numRiga = numRiga;
		this.numColonna = numColonna;
		this.coordinata = new Point(numRiga, numColonna);
		
		this.tipologia = DEFAULT;
		this.primaMossaRispref = Direzione.d;
		
	}
	
	public Casella(){
		this.origine = false;
		this.libera = true;
	}

	public void creaCoordinateAngoli(){
		this.coordinateAngoliAssociati = new ArrayList<>();
	}
	
	public void addAngolo(Point a){
		if(coordinateAngoliAssociati == null){
			creaCoordinateAngoli();
		}
		coordinateAngoliAssociati.add(a);
	}
	
	public boolean isOrigine() {
		return origine;
	}

	public void setOrigine(boolean origine) {
		this.origine = origine;
	}

	public boolean isLibera() {
		return libera;
	}

	public void setLibera(boolean libera) {
		this.libera = libera;
	}

	public int getNumRiga() {
		return numRiga;
	}

	public void setNumRiga(int numRiga) {
		this.numRiga = numRiga;
	}

	public int getNumColonna() {
		return numColonna;
	}

	public void setNumColonna(int numColonna) {
		this.numColonna = numColonna;
	}
	
	public String toString() {
		return "[" + this.getNumRiga() + "," + this.getNumColonna() + "]" + " libera = " + libera;
	}

	public void muoviti() {
		
	}
}
