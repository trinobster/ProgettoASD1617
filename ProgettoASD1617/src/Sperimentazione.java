
public class Sperimentazione {
	
	public static final double OFFSET = 0.001;
	public Casella[][] spazio;
	public double counterUguaglianzeCAM;
	public double counterUguaglianzePM;
	public double counterLibere;
	
	public Sperimentazione(Casella[][] spazio){
		this.spazio = spazio;
		this.counterUguaglianzeCAM = 0;
		this.counterUguaglianzePM = 0;
	}

	public void stampaDifferenzeCAM(){
		System.out.println("\nSpazio che indica le differenze tra le lunghezze dei CAM di Rispref meno Risgraf:");
		
		double differenza;
		counterLibere = 0;
		
		for(int row = 0; row < spazio.length; row++) {
			
			for(int col = 0; col < spazio[0].length; col++) {
				
				if(spazio[row][col].isOrigine()) {
					System.out.print("[ *O* ]");
				
				}else if(spazio[row][col].isLibera() && !spazio[row][col].isOrigine()) {
					differenza = spazio[row][col].pesoCAMRispref - spazio[row][col].pesoCAMRisgraf;					
					System.out.printf("[%f]", differenza);
					
					counterLibere++;
					
					if( Math.abs(differenza) < OFFSET){
						counterUguaglianzeCAM++;
					}
				
				} else {
					System.out.print("[ occ ]");
				}
			}
			
			System.out.println();
		}
		System.out.println("uguali = " + counterUguaglianzeCAM );
		System.out.println("Percentuale di lunghezze CAM uguali = " + counterUguaglianzeCAM / counterLibere * 100 + "%");
	}
	
	public void stampaDifferenzePM(){
		System.out.println("\nSpazio che indica le differenze di PM tra Rispref e Risgraf:");
		
		String mossaRisgraf, mossaRispref, print;
		counterLibere = 0;
		
		for(int row = 0; row < spazio.length; row++) {
			
			for(int col = 0; col < spazio[0].length; col++) {
				
				if(spazio[row][col].isOrigine()) {
					System.out.print("[*O*]");
					
				}else if(spazio[row][col].isLibera() && !spazio[row][col].isOrigine()) {
					
					counterLibere++;
					
					mossaRispref = spazio[row][col].primaMossaRispref.name();
					mossaRisgraf = spazio[row][col].primaMossaRisgraf.name();
					
					if(mossaRispref.equalsIgnoreCase(mossaRisgraf)){
						print = "[ " + mossaRispref + " ]";
						counterUguaglianzePM++;
					} else{
						print = "[ " + mossaRispref + ", " + mossaRisgraf + " ]";
					}
					System.out.print(print);
					
				} else {
					System.out.print("[occ]");
				}
			}
			System.out.println();
		}
		
		System.out.println("uguali = " + counterUguaglianzePM );
		System.out.println("Percentuale di PM uguali = " + counterUguaglianzePM / counterLibere * 100 + "%");
	}
}
