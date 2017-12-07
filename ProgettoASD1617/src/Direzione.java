import java.awt.Point;

public enum Direzione {
	
	N(0, -1, 0),
	S(1, 1, 0),
	E(2, 0, 1),
	W(3, 0, -1),
	NE(4, -1, 1),
	NW(5, -1, -1),
	SE(6, 1, 1),
	SW(7, 1, -1),
	d(0, 0, 0);
	
	private final int index;
	private final int dirX;
	private final int dirY;
	
	Direzione(int index, int dirX, int dirY){
		this.index = index;
		this.dirX = dirX;
		this.dirY = dirY;
	}

	public int getIndex() {
		return index;
	}

	public int getDirX() {
		return dirX;
	}

	public int getDirY() {
		return dirY;
	}
	
	public static Direzione getCurrespondentDirezione(Point c1, Point c2){
		
		Point c = new Point();
		c.setLocation(c2.getX() - c1.getX(), c2.getY() - c1.getY());
		// Ritorna la mossa corrispondente se da c1 voglio andare in c2, 
		// dove c è la coordinata è la differenza tra due coordinate c1 e c2
		
		if(c.x == -1 && c.y == 0){
			return N;
		} else if(c.x == 1 && c.y == 0){
			return S;
		} else if(c.x == 0 && c.y == -1){
			return W;
		} else if(c.x == 0 && c.y == 1){
			return E;
		} else if(c.x == -1 && c.y == -1){
			return NW;
		} else if(c.x == -1 && c.y == 1){
			return NE;
		} else if(c.x == 1 && c.y == -1){
			return SW;
		} else if(c.x == 1 && c.y == 1){
			return SE;
		} else{
			System.out.println("\n!!!ERRORE!!! vedi enum Direzione ritorna null getCurrespodentMossa() " + c.x + " " + c.y);
			return null;
		}
	}
	
	public double getDistanza(){
		switch(this){
			case N:
				return 1;
			case S:
				return 1;
			case W:
				return 1;
			case E:
				return 1;
			case NW:
				return Math.sqrt(2);
			case NE:
				return Math.sqrt(2);
			case SW:
				return Math.sqrt(2);
			case SE:
				return Math.sqrt(2);
			default:
				return 0;
		}
	}
	
	public Point getCurrespondentPosition(){
		
		switch(this){
			case N:
				return new Point(-1, 0);
			case S:
				return new Point(1, 0);
			case W:
				return new Point(0, -1);
			case E:
				return new Point(0, 1);
			case NW:
				return new Point(-1, -1);
			case NE:
				return new Point(-1, 1);
			case SW:
				return new Point(1, -1);
			case SE:
				return new Point(1, 1);
			default:
				System.out.println("\n!!!ERRORE!!! vedi enum Direzione ritorna null getCurrespodentPosition() ");
				return null;
		}
		
	}
	
	public String toString(){
		return Direzione.this.name() +" dist = " + this.getDistanza();
	}
	
	/*
	 * Ritorno l'indice della direzione opposta a quella dell'indice in input*/
	public static int direzioneOpposta(int dirIndex) {
		switch(dirIndex) {
		case 0:
			return 1;
		case 1:
			return 0;
		case 2:
			return 3;
		case 3:
			return 2;
		case 4:
			return 7;
		case 5:
			return 6;
		case 6:
			return 5;
		case 7:
			return 4;
		default:
			System.out.println("DEFAULT");
			return -1;
			
		}
		
	}
	
	/*
	 * Cambio la randomizzazione eliminando alcune possibili mosse successive che mi creerebbero agglomerati di caselle libere*/
	public static int[] direzioniPreferenziali(int dirIndex) {
		int[]preferenziali = null;
		switch(dirIndex) {
		case 0:
			preferenziali = new int[]{0,4,5};
			break;
		case 1:
			preferenziali = new int[]{1,6,7};
			break;
		case 2:
			preferenziali = new int[]{2,4,6};
			break;
		case 3:
			preferenziali = new int[]{3,5,7};
			break;
		case 4:
			preferenziali = new int[]{4,0,2};
			break;
		case 5:
			preferenziali = new int[]{5,0,3};
			break;
		case 6:
			preferenziali = new int[]{6,1,2};
			break;
		case 7:
			preferenziali = new int[]{7,1,3};
			break;
		default:
			break;
			
		}
		return preferenziali;
	}
	
	public static int[] direzioniPreferenziali2(int dirIndex) {
		int[]preferenziali = null;
		switch(dirIndex) {
		case 0:
			preferenziali = new int[]{0,4,5,2,3};
			break;
		case 1:
			preferenziali = new int[]{1,6,7,2,3};
			break;
		case 2:
			preferenziali = new int[]{2,4,6,0,1};
			break;
		case 3:
			preferenziali = new int[]{3,5,7,0,1};
			break;
		case 4:
			preferenziali = new int[]{4,0,2,5,6};
			break;
		case 5:
			preferenziali = new int[]{5,0,3,4,7};
			break;
		case 6:
			preferenziali = new int[]{6,1,2,4,7};
			break;
		case 7:
			preferenziali = new int[]{7,1,3,5,6};
			break;
		default:
			break;
			
		}
		return preferenziali;
	}
	
	
}
