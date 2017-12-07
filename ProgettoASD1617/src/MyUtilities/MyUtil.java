package MyUtilities;

import java.awt.Point;
import java.util.ArrayList;

public class MyUtil {
	
	public static ArrayList<Point> figliPossibiliTot(Point c, int numR, int numC){

		ArrayList<Point> figliPosstot = new ArrayList<Point>();
		Point f;

		if(c.x == 0 && c.y == 0){
			Point f1 = new Point(c.x + 1, c.y + 1);
			Point f2 = new Point(c.x, c.y + 1);
			Point f3 = new Point(c.x + 1, c.y);

			figliPosstot.add(f1);
			figliPosstot.add(f2);
			figliPosstot.add(f3);
			return figliPosstot;
		}

		
		if(c.x == 0 && c.y == numC - 1){
			Point f1 = new Point(c.x, c.y -1);
			Point f2 = new Point(c.x + 1, c.y);
			Point f3 = new Point(c.x + 1, c.y - 1);
			
			figliPosstot.add(f1);
			figliPosstot.add(f2);
			figliPosstot.add(f3);

			return figliPosstot;
		}
		

		if(c.x == numR - 1 && c.y == 0){
			Point f1 = new Point(c.x - 1, c.y);
			Point f2 = new Point(c.x, c.y + 1);
			Point f3 = new Point(c.x - 1, c.y + 1);
			
			figliPosstot.add(f1);
			figliPosstot.add(f2);
			figliPosstot.add(f3);

			return figliPosstot;
		}

		

		if(c.x == numR - 1 && c.y == numC - 1){
			Point f1 = new Point(c.x - 1, c.y);
			Point f2 = new Point(c.x, c.y - 1);
			Point f3 = new Point(c.x - 1, c.y - 1);

				figliPosstot.add(f1);
				figliPosstot.add(f2);
				figliPosstot.add(f3);
				
			return figliPosstot;
		}

		if(c.x == 0){
			for(int i = c.y - 1; i < c.y + 2; i++){
				for(int j = c.x; j < c.x + 2; j++){
					if(!(i == c.y && j == c.x)){
						f = new Point(j, i);
						figliPosstot.add(f);
					}
				}
			}
			return figliPosstot;
		}

		if(c.x == numR - 1){
			for(int i = c.y - 1; i < c.y + 2; i++){
				for(int j = c.x - 1; j < c.x + 1; j++){
					if(!(i == c.y && j == c.x)){
						f = new Point(j, i);
						figliPosstot.add(f);

					}
				}
			}
			return figliPosstot;
		}

		if(c.y == 0){
			for(int i = c.x - 1; i < c.x + 2; i++){
				for(int j = c.y; j < c.y + 2; j++){
					if(!(i == c.x && j == c.y)){
						f = new Point(i, j);
						figliPosstot.add(f);

					}
				}
			}
			return figliPosstot;
		}

		if(c.y == numC - 1){
			for(int i = c.x - 1; i < c.x + 2; i++){
				for(int j = c.y - 1; j < c.y + 1; j++){
					if(!(i == c.x && j == c.y)){
						f = new Point(i, j);
						figliPosstot.add(f);

					}
				}
			}
			return figliPosstot;
		}

		for(int i = c.x - 1; i < c.x + 2; i++){
			for(int j = c.y - 1; j < c.y + 2; j++){
				if(!(i == c.x && j == c.y)){
					f = new Point(i, j);
					figliPosstot.add(f);
				}
			}
		}
		return figliPosstot;
	}
}
