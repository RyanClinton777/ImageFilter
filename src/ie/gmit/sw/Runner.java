package ie.gmit.sw;

/**@author Ryan Clinton
 * @version 2019-12 (4.14.0)
 * 
 * Simple Runner class
 * Just displays splash screen and creates a UI object.
 */
public class Runner {

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("----------------------");
		System.out.println("-----IMAGE-FILTER-----");
		System.out.println("----------------------");
		
		//run a new UI object.
		new UI();
	}
}
