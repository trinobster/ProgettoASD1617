package MyUtilities;
import java.util.Random;


public class EstrazioniCasuali{
	
	private static Random rand;
	
	public static int estraiIntero(int min, int max){
		rand = new Random();
		return rand.nextInt(max - min) + min;
		//random.nextInt(max - min) + min; Note that the lower limit is inclusive, but the upper limit is exclusive.
	}
}