package ie.gmit.sw;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/**@author Ryan Clinton
 * @version 2019-12 (4.14.0)
 * 
 * Console User Interface (UI) class
 * Uses Scanner class for inputs
 */
public class UI{
	/**
	 * Input directory
	 * Desktop by default.
	 */
	private String inputDir = (System.getProperty("user.home") + "/Desktop");
	/**
	 * output directory.
	 * Desktop by default
	 */
	private String outputDir = (System.getProperty("user.home") + "/Desktop");
	/**
	 * Holds proposed directories while they are confirmed to be valid.
	 */
	private String tempDir;

	/**
	 * holds user selection
	 */
	private String select = "default"; //User selection
	/**
	 * Scanner object for inputs
	 */
	Scanner scanner;

	/**
	 * Array of Kernel objects for our filters. 
	 * Instantiated at start of constructor.
	 * I used array instead of ArrayList or something because I only added custom filters right at the end. 
	 * I figure it's a rare enough to add them that it's not egregious; but I would have changed it if I had time.
	 */
	Kernel[] filters;

	
	/**
	 * Constructor - Loads in filters and handles main menu
	 */
	public UI() {
		//---Set up
		filters = getDefaultFilters(); //Load in filters
		scanner = new Scanner(System.in); // Create a Scanner object		

		//---Menu loop - loop until user inputs "exit"
		do {
			//Display menu
			displayMainMenu();

			select = scanner.nextLine();
			switch (select) {
			//---CHANGE INPUT DIRECTORY
			case "1": {
				System.out.println("Enter path to new input directory.");
				tempDir = scanner.nextLine();

				//Confirm that it is valid
				File dir = new File(tempDir);
				//If exists
				if (dir.exists()) {
					System.out.println("Input directory changed.");
					inputDir = tempDir;
				}
				//Doesn't exist
				else {
					System.out.println("Invalid directory.");
				}

				break;
			}
			case "2": {
				//---CHANGE OUTPUT DIRECTORY
				System.out.println("Enter path to new output directory.");
				tempDir = scanner.nextLine();

				//Confirm that it is valid
				File dir = new File(tempDir);
				//If exists
				if (dir.exists()) {
					System.out.println("Output directory changed.");
					outputDir = tempDir;
				}
				//Doesn't exist
				else {
					System.out.println("Invalid directory.");
				}
				break;
			}
			case "3": {
				//---APPLY FILTER
				displayFilterMenu();
				break;
			}
			case "4": {
				//---ADD CUSTOM FILTER
				addCustomFilter();
				break;
			}
			}
		}
		while(!select.equalsIgnoreCase("exit"));
		//Exit
		System.out.println("\nThank you. Goodbye.");
	}

	/**
	 * This method gets inputs from the user for a custom filter, then instantiates it and adds it to the array
	 * supports 3x3 and 5x5
	 * Code is a bit of a mess because I was running out of time and making silly mistakes, which I covered up with messy code
	 */
	private void addCustomFilter() {
		Kernel newKernel;
		String name;
		double[][] matrix;

		System.out.println("\nAdd new Filter - Select size:");
		System.out.println("[1]: 3x3");
		System.out.println("[2]: 5x5");
		System.out.println("[-1] Cancel");

		//---Get size input
		int select = Integer.parseInt(scanner.nextLine());

		//Cancel
		if (select == -1) {
			System.out.println("Cancelled.");
			return;
		}

		//If selection invalid
		if (!(select == 1) && !(select == 2)) {
			System.out.println("Invalid selection");
			return;
		}

		//---get name
		System.out.println("Enter new filter name:");
		name = scanner.nextLine();

		//---Create matrix
		//3x3 Matrix:
		if (select == 2) matrix = new double[5][5];
		//5x5
		else matrix = new double[3][3];

		//---Get values
		try {
			for (int row = 0; row < matrix.length; row++) {
				for (int col = 0; col < matrix[row].length; col++) {
					System.out.println("Enter value for ["+row+","+col+"]");
					matrix[row][col] = Integer.valueOf(scanner.nextLine());
				}
			}
			
			//Print final result
			System.out.print("New Filter - "+name+":");
			for (int row = 0; row < matrix.length; row++) {
				System.out.println();
				for (int col = 0; col < matrix[row].length; col++) {
					System.out.print(" "+matrix[row][col]+" ");
				}
			}
			System.out.println("");//blank line
		}
		catch (Exception e) {
			System.out.println("Invalid input. Cancelling Operation.");
			return;
		}

		//Create kernel object
		newKernel = new Kernel(name, matrix);
		//add to array
		//I know this is bad code and should use A different collection like ArrayList, amortized time etc, did it last minute
		filters = Arrays.copyOf(filters, (filters.length+1));
		filters[filters.length-1] = newKernel;
		
		System.out.println("New Filter Created. It should now be listed when selecting a filter.");
	}

	/**
	 * Prints the main menu options.
	 */
	private void displayMainMenu() {
		System.out.println("\nMAIN MENU");
		System.out.println("Please select an option:");
		System.out.println("[1] Set Input Directory - Currently "+inputDir);
		System.out.println("[2] Set Output Directory - Currently "+outputDir);
		System.out.println("[3] Filter images in input directory");
		System.out.println("[4] Add a custom filter");
		System.out.println("Enter \"exit\" to exit.");
	}

	/**
	 * Returns a list of pre-set kernels from the brief.
	 * If more are added here later, the filter selection menu will dynamically present them and allow them to be selected.
	 * Called on start-up
	 * @return array of pre-set kernels
	 */
	private Kernel[] getDefaultFilters() {
		//Create default filters
		Kernel[] filters = 
			{
					new Kernel("Identity", new double[][]{{0, 0, 0}, {0, 1, 0},{0, 0, 0}}),
					new Kernel("Edge Detection", new double[][]{{-1, -1, -1},{-1, 8, -1},{-1, -1, -1}}),
					new Kernel("Edge Detection 2", new double[][]{{1, 0, -1},{0, 0, 0},{-1, 0, 1}}),
					new Kernel("Laplacian", new double[][]{{0, -1, 0},{-1, 4, -1},{0, -1, 0}}),
					new Kernel("Sharpen", new double[][]{{0, -1, 0},{-1, 5, -1},{0, -1, 0}}),
					new Kernel("Vertical Lines", new double[][]{{-1, 2, -1},{-1, 2, -1},{-1, 2, -1}}),
					new Kernel("Horizontal Lines", new double[][]{{-1, -1, -1},{2, 2, 2},{-1, -1, -1}}),
					new Kernel("Diagonal 45 Lines", new double[][]{{-1, -1, 2},{-1, 2, -1},{2, -1, -1}}),
					new Kernel("Sobel Horizontal", new double[][]{{-1, -2, -1},{0, 0, 0},{1, 2, 1}}),
					new Kernel("Sobel Vertical", new double[][]{{-1, 0, 1},{-2, 0, 2},{-1, 0, 1}}),
					new Kernel("Box Blur", new double[][]{{0.111, 0.111, 0.111},{0.111, 0.111, 0.111},{0.111, 0.111, 0.111}}),
			};
		return filters;
	}

	/**
	 * Dynamically lists filters and allows user to select one.
	 * This includes the presets, and any custom filters that the user has created.
	 */
	private void displayFilterMenu() {
		//Display all filters
		System.out.println("Select a filter:");
		for (int i = 0; i<filters.length; i++) {
			System.out.println("["+i+"] "+filters[i].getName());
		}
		System.out.println("[-1] Cancel");

		//get input
		int select = Integer.parseInt(scanner.nextLine());

		//-1 to exit
		if (select == -1) {
			System.out.println("Canceled.");
			return;
		}
		//else selection within range - valid input
		else if (select > -1 || select < filters.length) {
			//File parser class, gets the images in inputDir and filters them
			System.out.println("\n\""+filters[select].getName()+"\" filter selected.");

			//Create and run new parser object.
			//Parser is Runnable.
			//We create a new thread and pass it in as an argument to run it on a new thread
			//start() calls run() under the hood, so the run method of Parser class will be called
			new Thread(new Parser(filters[select], inputDir, outputDir)).start();
		}
		//anything else
		else System.out.println("Invalid selection");
	}

}