package ie.gmit.sw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter; //Used to filter out non-images
import java.io.IOException;

import javax.imageio.ImageIO;

/**@author Ryan Clinton
 * @version 2019-12 (4.14.0)
 * 
 * Parses files from a directory and passes them to the FilterApplicator to be filtered.
 * Generally responsible for File IO and parsing.
 * Implements Runnable, so this class can be threaded.
 */
public class Parser implements Runnable {
	/**
	 * Input directory
	 */
	String inputDir;
	/**
	 * Output directory
	 */
	String outputDir;
	/**
	 * Kernel object for filter to be applied - passed in to FilterApplicator
	 */
	Kernel filter;
	/**
	 * Array of images, instantiated when the input directory is parsed for images.
	 */
	File[] images;

	/**
	 * @param filter - Kernel object. The filter to be applied
	 * @param input - input directory
	 * @param output - output directory
	 */
	public Parser(Kernel filter, String input, String output) {
		this.inputDir = input;
		this.outputDir = output;
		this.filter =  filter;
	}

	/**
	 * Runs the operations of this class on a thread (implements Runnable)
	 * 
	 * Parses images (jpg, png, gif) in input dir
	 * Passes them each to FilterApplicator to be filtered
	 * Outputs the result to output dir
	 */
	public void run() {
		//Make sure directory exists
		File dir = new File(inputDir);
		if (!dir.exists()) {
			System.out.println("Directory does not exist.");
			return;
		}

		//Get a list of images from the input directory
		//We use a FilenamFilter to specify which file types to use
		//basically the file only gets listed if the filter returns true - if it's one of the allowed formats
		File[] images = new File(inputDir).listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return ((name.toLowerCase().endsWith(".png")) || (name.toLowerCase().endsWith(".jpg")) || (name.toLowerCase().endsWith(".gif")) || (name.toLowerCase().endsWith(".gif")));
			}}
				);

		//Return if no valid images
		if (images.length < 1) {
			System.out.println("No valid files found");
			return;
		}

		System.out.println(images.length+" images found.");

		//Loop through the images, call FilterApplicator for each.
		BufferedImage image; //Association
		for (File f: images) {
			try {
			//Read in an image and convert to a BufferedImage
			image = ImageIO.read(f);
			
			System.out.println("Filtering image: \""+f.getName()+"\"...");
			
			//Apply filter with new FilterApplicator
			image = new FilterApplicator(filter).getFiltered(image);
			
			//Output file
			//GET FILE EXTENSION LATER IF TIME, ASSUME PNG FOR NOW
			ImageIO.write(image, "png", new File(outputDir+"/"+f.getName()+"_FILTERED.png"));
			System.out.println("Image: \""+f.getName()+"\" Filtered sucessfuly.");
			}
			catch(IOException e) {
				//e.printStackTrace();
				System.out.println("Operation failed for image: "+f.getName());
				continue; //Ignore in case the next ones work
			}
		}
	}
}
