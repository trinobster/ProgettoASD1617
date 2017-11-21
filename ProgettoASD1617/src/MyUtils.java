
public class MyUtils {

	public enum Direzione{
		N(0,-1,0),
		S(1,1,0),
		E(2,0,1),
		O(3,0,-1),
		NE(4,-1,1),
		NW(5,-1,-1),
		SE(6,1,1),
		SW(7,1,-1);
		
		private final int index;
		private final int dirX;
		private final int dirY;
		
		Direzione(int index, int dirX, int dirY){
			this.index=index;
			this.dirX=dirX;
			this.dirY=dirY;
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
		
		
		
		
	}
	
}
