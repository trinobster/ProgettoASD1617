package MyUtilities;

import java.awt.Point;

public class MyFormulas {
	
	/*
	 * Prende in input le coordinate di due caselle e ritorna la dlib tra esse*/
	public static double dlibComputation(Point source, Point destination) {
		double deltaX = Math.abs(destination.getX()-source.getX());
		double deltaY = Math.abs(destination.getY()-source.getY());
		double deltaMin = Math.min(deltaX, deltaY);
		double deltaMax = Math.max(deltaX, deltaY);
		
		double dlib = Math.sqrt(2)*deltaMin + deltaMax - deltaMin;
		return dlib;
	}
	
}
