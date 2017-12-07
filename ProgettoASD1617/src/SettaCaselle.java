import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import MyUtilities.MyUtil;

public class SettaCaselle {
	
	public Stack<Casella> caselleVerdi;
	public Stack<Casella> caselleBianche;
	public Stack<Casella> caselleAngolo;
	public Casella[][] spazio;
	
	public HashMap<Point, ArrayList<Point>> hmap; // mappa chiave(posizioni caselle biache) e valore(posizioni caselle angolo relative)
	
	public Point origine;
	public int numR, numC, i, j;
	
	public SettaCaselle(Casella[][] spazio, Point origine){
		this.caselleVerdi = new Stack<>();
		this.caselleBianche = new Stack<>();
		this.caselleAngolo = new Stack<>();
		this.spazio = spazio;
		
		this.origine = origine;
		this.numR = spazio.length;
		this.numC = spazio[1].length;
		
		this.hmap = new HashMap<Point, ArrayList<Point>>();
	}
	
	//NB: scorrere indica che controlla se è libera, set non controlla
	public void risolutore(){
		// Per prima cosa ricerca le caselle verdi/libere sugli assi, sulle diagonali e sulle L e le inserisce in caselleVerdi
		// oltre a riempire anche la pila di caselleBianche
		scorriAssiOrigine();
		scorriDiagonali();
		scorriL();
		
		// cerca le caselle d'angolo che soddisfano le 3 condizioni e le inserisce nella pila caselleAngolo
		caselleAngolo();
		//printHMap();
		settaDefaultPMP(caselleVerdi);
	}
	
	public void stampaSpazioPMP(){
		String mossa, print;
		System.out.println();
		
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
	
	public void settaDefaultPMP(Stack<Casella> caselle){
		Casella c;
		// Assegna PMP di default a tutte le caselle verdi e bianche
		for(int i = 0; i < caselle.size(); i++){
			c = caselle.get(i);
			Direzione defaultPMP = getDefaultPMP(c.coordinata);
			spazio[c.coordinata.x][c.coordinata.y].primaMossaRispref = defaultPMP;
		}
	}
	
	public Direzione getDefaultPMP(Point p){
		if(p.x - origine.x > 0){ // righe sotto l'origine
			if(p.y - origine.y > 0){// quadrante SE
				return Direzione.SE;
			} else if(p.y - origine.y < 0){
				return Direzione.SW;
			} else{
				return Direzione.S;
			}
		} else if(p.x - origine.x < 0){ // righe sopra origine
			if(p.y - origine.y > 0){
				return Direzione.NE;
			} else if(p.y - origine.y < 0){
				return Direzione.NW;
			} else{
				return Direzione.N;
			}
		} else{// stessa riga dell'origine
			if(p.y - origine.y > 0){
				return Direzione.E;
			} else{
				return Direzione.W;
			}
		}
	}
	
	public void printHMap(){
		String print = "";
		
		for(Map.Entry<Point, ArrayList<Point>> kv: hmap.entrySet()){
			print = print + "\nChiave B = (" + kv.getKey().x + ", " + kv.getKey().y + ")"
					+ " Lista Valori = [ ";
			for(Point a: kv.getValue()){
				print = print + "(" + a.x + ", " + a.y + "), ";
			}
			
			print = print + " ]";
		}
		
		System.out.println(print);
	}
	
	public void caselleAngolo(){
		Casella b, isA; //isA è la possibile casella angolo
		Point posizione = new Point();
		int h, k;
		
		ArrayList<Point> caselleAngoloRelative;
		
		// Prendo in esame ogni casella bianca B
		for(int i = 0; i < caselleBianche.size(); i++){
			b = caselleBianche.get(i);
			
		//	System.out.println("b = (" + b.coordinata.x + ", " + b.coordinata.y +")");
			
			// prendo in esame tutte le caselle vicine con distanza sqrt(2) da B/b una alla volta per ciclo
			for(h = -1; h < 2; h = h + 2){
				for(k = -1; k < 2; k = k + 2){
					
					posizione.x = b.coordinata.x + h;
					posizione.y = b.coordinata.y + k;
					
					// 1° Controllo: b si trova a distanza sqrt(2) da almeno una casella verde. 
					if(firstControl(posizione)){
							isA = spazio[posizione.x][posizione.y];
							
						//	System.out.println("isA= (" + isA.coordinata.x + ", " + isA.coordinata.y + ")");
							// 2° Controllo: isA dista 1 da una casella ostacolo, la quale dista 1 da B
							if(secondControl(isA.coordinata, b.coordinata)){
								
								//3° Controllo: isA dista 1 da una terza casella non bianca, la quale dista 1 da B
								if(thirdControl(isA.coordinata, b.coordinata)){
									spazio[isA.coordinata.x][isA.coordinata.y].tipologia = Casella.ANGOLO;
									caselleAngolo.push(spazio[isA.coordinata.x][isA.coordinata.y]);
									
									if(!hmap.containsKey(b.coordinata)){
										caselleAngoloRelative = new ArrayList<>();
										caselleAngoloRelative.add(isA.coordinata);
										hmap.put(b.coordinata, caselleAngoloRelative);
									} else{
										caselleAngoloRelative = hmap.get(b.coordinata);
										if(!caselleAngoloRelative.contains(isA.coordinata)){
											caselleAngoloRelative.add(isA.coordinata);
											hmap.replace(b.coordinata, caselleAngoloRelative);
										}
										
									}
									
								}
							}// Se il 2° controllo fallisce, passo alla prox b
							
						} //Se il 1° controllo fallisce, passa alla prox b
				} // end for interno
			} // end for esterno
		}
	}
	
	public boolean isDistantOne(Point p1, Point p2){
	//	System.out.println("p1 = " + p1.x + " " + p1.y + ", b = " + p2.x + " " + p2.y );
		if(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)) == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean thirdInsideControl(Point temp, Point b){
		if(isInsideSpace(temp) && spazio[temp.x][temp.y].tipologia.equalsIgnoreCase(Casella.VERDE) && isDistantOne(temp, b)){
		//	System.out.println("terza = (" + temp.x + ", " + temp.y + ")");
			return true;
		} else{
			return false;
		}
	}
	
	public boolean thirdControl(Point isA, Point b){
		// ritorna T se c'è una casella non bianca (VERDE) che dista 1 da isA, la quale dista 1 da B
		Point temp = new Point();
		
		temp.x = isA.x + 1;
		temp.y = isA.y;
		
		if(thirdInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && spazio[temp.x][temp.y].tipologia.equalsIgnoreCase(Casella.VERDE) && isDistantOne(temp, b)){
			System.out.println("terza = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		temp.x = isA.x - 1;
		temp.y = isA.y;
		if(thirdInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && spazio[temp.x][temp.y].tipologia.equalsIgnoreCase(Casella.VERDE) && isDistantOne(temp, b)){
			System.out.println("terza = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		temp.x = isA.x;
		temp.y = isA.y + 1;
		if(thirdInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && spazio[temp.x][temp.y].tipologia.equalsIgnoreCase(Casella.VERDE) && isDistantOne(temp, b)){
			System.out.println("terza = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		temp.x = isA.x;
		temp.y = isA.y - 1;
		if(thirdInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && spazio[temp.x][temp.y].tipologia.equalsIgnoreCase(Casella.VERDE) && isDistantOne(temp, b)){
			System.out.println("terza = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		return false;
	}
	
	public boolean firstControl(Point isA){
		// ritorna T se isA è una casella verde
		if(isInsideSpace(isA) && (spazio[isA.x][isA.y].tipologia.equalsIgnoreCase(Casella.VERDE))){
			return true;
		} else{
			return false;
		}
	}
	
	public boolean secondInsideControl(Point temp, Point b){
		if(isInsideSpace(temp) && !spazio[temp.x][temp.y].libera && isDistantOne(temp, b)){
		//	System.out.println("ostacolo = (" + temp.x + ", " + temp.y + ")");
			return true;
		} else{
			return false;
		}
	}
	
	public boolean secondControl(Point isA, Point b){ 
		// ritorna T se c'è una casella vicina ad isA con distanza 1, che è un ostacolo e che dista 1 da b
		Point temp = new Point();
		
		temp.x = isA.x + 1;
		temp.y = isA.y;
		if(secondInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && !spazio[temp.x][temp.y].libera && isDistantOne(temp, b)){
			System.out.println("ostacolo = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		temp.x = isA.x - 1;
		temp.y = isA.y;
		if(secondInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && !spazio[temp.x][temp.y].libera && isDistantOne(temp, b)){
			System.out.println("ostacolo = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		temp.x = isA.x;
		temp.y = isA.y + 1;
		if(secondInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && !spazio[temp.x][temp.y].libera && isDistantOne(temp, b)){
			System.out.println("ostacolo = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		temp.x = isA.x;
		temp.y = isA.y - 1;
		if(secondInsideControl(temp, b)){
			return true;
		}
	/*	if(isInsideSpace(temp) && !spazio[temp.x][temp.y].libera && isDistantOne(temp, b)){
			System.out.println("ostacolo = (" + temp.x + ", " + temp.y + ")");
			return true;
		}*/
		
		return false;
	}
	
	public boolean isInsideSpace(Point p){
		if(p.x > - 1 && p.y > - 1 && p.x < numR && p.y < numC){
			return true;
		} else{
			return false;
		}
	}
	
	public void scorriL(){
		int rowIncr = 0;
		int colIncr = 0;
		
		int i = origine.x - 1;
		int j = origine.y + 1;
		
		while(i > - 1 && j < numC && spazio[i][j].libera){ //quadrante NE
			scorriEst(i, j);
			scorriNord(i - 1, j);
			i--;
			j++;
		}
		
		i = origine.x + 1;
		j = origine.y + 1;
		while(i < numR && j < numC && spazio[i][j].libera){ // quadrante SE
			scorriEst(i, j);
			scorriSud(i + 1, j);
			i++;
			j++;
		}
		
		i = origine.x + 1;
		j = origine.y - 1;
		while(i < numR && j > -1 && spazio[i][j].libera){
			scorriOvest(i, j);
			scorriSud(i + 1, j);
			i++;
			j--;
		}
		
		i = origine.x - 1;
		j = origine.y - 1;
		while(i > -1 && j > -1 && spazio[i][j].libera){
			scorriOvest(i, j);
			scorriNord(i - 1, j);
			i--;
			j--;
		}
	}
	
	public void setQuadratoBianco(int startRow, int startCol, int endRow, int endCol){
		for(int i = startRow; i < endRow; i++){
			for(int j = startCol; j < endCol; j++){
				if(spazio[i][j].libera){
					setTipologiaBianca(i, j);
				}
			}
		}
	}
	
	public void setColonnaBianca(int start, int end, int col){
		for(int i = start; i < end; i++){
			setTipologiaBianca(i, col);
		}
	}
	
	public void setRigaBianca(int start, int end, int row){
		for(int i = start; i < end; i++){
			setTipologiaBianca(row, i);
		}
	}
	
	public void scorriDiagonali(){
		
		i = origine.x - 1;
		j = origine.y + 1;
		while(i > -1 && j < numC && spazio[i][j].libera){ 
			// diagonale NE
			setTipologiaVerde(i, j);
			i--;
			j++;
		}
		setQuadratoBianco(0, j, i + 1, numC);
		
		
		i = origine.x + 1;
		j = origine.y + 1;
		while(i < numR && j < numC && spazio[i][j].libera){ 
			// diagonale SW
			setTipologiaVerde(i, j);
			i++;
			j++;
		}
		setQuadratoBianco(i, j, numR, numC);
		
		
		i = origine.x + 1;
		j = origine.y - 1;
		while(i < numR && j > -1 && spazio[i][j].libera){ 
			// diagonale SE
			setTipologiaVerde(i, j);
			i++;
			j--;
		}
		setQuadratoBianco(i, 0, numR, j + 1);
		
		
		i = origine.x - 1;
		j = origine.y - 1;
		while(i > -1 && j > -1 && spazio[i][j].libera){ 
			// diagonale NW
			setTipologiaVerde(i, j);
			i--;
			j--;
		}
		setQuadratoBianco(0, 0, i + 1, j + 1);
	}
	
	public void scorriSud(int startRow, int startCol){ 
		while(startRow < numR && spazio[startRow][startCol].libera){ // asse sud
			setTipologiaVerde(startRow, startCol);
			startRow++;
		}
		setColonnaBianca(startRow, numR, startCol);
	}
	
	public void scorriNord(int startRow, int startCol){
		while(startRow > -1 && spazio[startRow][startCol].libera){ // asse nord
			setTipologiaVerde(startRow, startCol);
			startRow--;
		}
		setColonnaBianca(0, startRow + 1, startCol);
	}
	
	public void scorriEst(int startRow, int startCol){
		while(startCol < numC && spazio[startRow][startCol].libera){ // asse est
			setTipologiaVerde(startRow, startCol);
			startCol++;
		}
		setRigaBianca(startCol, numC, startRow);
	}
	
	public void scorriOvest(int startRow, int startCol){
		while(startCol > -1 && spazio[startRow][startCol].libera){ // asse ovest
			setTipologiaVerde(startRow, startCol);
			startCol--;
		}
		setRigaBianca(0, startCol + 1, startRow);
	}
	
	public void scorriAssiOrigine(){
		i = origine.x + 1;
		scorriSud(i, origine.y);
	/*	while(i < numR && spazio[i][origine.y].libera){ // asse sud
			setTipologiaVerde(i, origine.y);
			i++;
		}
		setColonnaBianca(i, numR, origine.y);*/
		
		
		i = origine.x - 1;
		scorriNord(i, origine.y);
	/*	while(i > -1 && spazio[i][origine.y].libera){ // asse nord
			setTipologiaVerde(i, origine.y);
			i--;
		}
		setColonnaBianca(0, i + 1, origine.y);*/
		
		
		i = origine.y + 1;
		scorriEst(origine.x, i);
		/*while(i < numC && spazio[origine.x][i].libera){ // asse est
			setTipologiaVerde(origine.x, i);
			i++;
		}
		setRigaBianca(i, numC, origine.x);*/
		
		
		i = origine.y - 1;
		scorriOvest(origine.x, i);
	/*	while(i > -1 && spazio[origine.x][i].libera){ // asse ovest
			setTipologiaVerde(origine.x, i);
			i--;
		}
		setRigaBianca(0, i + 1, origine.x);*/
	}
	
	
	public void setTipologiaVerde(int row, int col){
		if(spazio[row][col].libera){
			spazio[row][col].tipologia = Casella.VERDE;
			caselleVerdi.push(spazio[row][col]);
		}
	}
	
	public void setTipologiaBianca(int row, int col){
		if(spazio[row][col].libera){
			spazio[row][col].tipologia = Casella.BIANCA;
			caselleBianche.push(spazio[row][col]);
		}
	}
	
	public void stampaSpazioVerdiBianche(){
		String print = "";
		
		System.out.println();
		
		for(int row = 0; row < spazio.length; row++) {
			for(int col = 0; col < spazio[0].length; col++) {
				if(spazio[row][col].isOrigine()) {
					System.out.print("[ O ]");
				}else if(spazio[row][col].isLibera()) {

					if(spazio[row][col].tipologia.equalsIgnoreCase(Casella.BIANCA)){
						print = "[ b ]";
					} else if(spazio[row][col].tipologia.equalsIgnoreCase(Casella.ANGOLO)){
						print = "[ A ]";
				/*	} else if(spazio[row][col].tipologia.equalsIgnoreCase(Casella.VERDE)){
						print = "[ . ]";*/
					} else{
						print = "[   ]";
					}
					System.out.print(print);
				} else if(!spazio[row][col].libera) {
					System.out.print("[occ]");
				}
			}
			System.out.println();
		}
	}
}
