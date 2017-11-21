import java.util.ArrayList;

public class Casella {
	
	public boolean origine;
	public boolean libera;
	public int numRiga, numColonna;
	//public ArrayList<MyUtils.Direzione> direzioniDisponibili;
	

	public Casella(boolean origine, boolean libera, int numRiga, int numColonna) {
		this.origine = origine;
		this.libera = libera;
		this.numRiga = numRiga;
		this.numColonna = numColonna;
	}
	
	public void muoviti() {
		
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
	
	/*
	 * Override di toString, così mi stampa bene le caselle*/
	public String toString() {
		return "["+this.getNumRiga()+","+this.getNumColonna()+"]";
	}
	
	
	

}
