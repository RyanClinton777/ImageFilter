package ie.gmit.sw;
import java.awt.image.BufferedImage;

/**@author Ryan Clinton
 * @version 2019-12 (4.14.0)
 * 
 * This class is used to apply a given filter kernel to a given image.
 */
public class FilterApplicator{
	/**
	 * Kernel filter to be applied
	 */
	Kernel filter;
	/**
	 * Buffer the image in memory
	 */
	BufferedImage imageBuffer;

	/**
	 * @param filter - Kernel object, filter to be applied
	 */
	public FilterApplicator(Kernel filter) {
		super();
		this.filter = filter;
	}

	//takes in buffer for the image, pass on to other methods to be filtered.
	//Returns the resultant filtered image buffer.
	/**
	 * @param imageBuffer
	 * @return
	 */
	public BufferedImage getFiltered(BufferedImage imageBuffer) {
		int pixel, newRGB;

		//For each pixel in the image
		for (int y = 0; y < imageBuffer.getHeight(); y++) {
			for (int x = 0; x < imageBuffer.getWidth(); x++) {
				//Get the pixel at this (x, y) coordinate
				pixel = imageBuffer.getRGB(x, y); 

				//Apply filter to this pixel
				newRGB = applyFilter(pixel, x, y);
				imageBuffer.setRGB(x, y, newRGB);
			}
		} 

		//Return filtered Image.
		return imageBuffer;
	}

	/**
	 * Apply the kernel convolution matrix to a given pixel and position
	 * Does so in full colour - splits the pixel into r,g,b, filters them individually, them combines them.
	 * 
	 * @param pixel - The RBG value of the pixel to be filtered
	 * @param x - X Coordinate
	 * @param y - Y Coordinate
	 * @return filtered RGB value for pixel
	 */
	private int applyFilter(int pixel, int x, int y) {
		int element = pixel;
		int rgb, red, green, blue;

		//System.out.println("Before Change:"+element);//DEBUG

		//Loop through convolution matrix
		for (int row = 0; row < filter.getMatrix().length; row++) {
			for (int col = 0; col < filter.getMatrix()[row].length; col++) {
				try {
					//This will cause an exception if we overrun the edges of the image (These are ignored)
					//Get RGB for the corresponding pixel for our current position in the filter matrix
					//Because we are applying to a 3x3 grid of pixels based on the current one in the loop, we add matrix position to the coordinates
					//E.g. x+2 would be 2 down, 2 right from the current pixel

					//We can get the RGB colour channels out of a 32-bit int as follows:
					//& 0xff leaves us with only the relevant 8 bits of binary of the variable
					red = (element >> 16) & 0xff;
					green = (element >> 8) & 0xff;
					blue = element & 0xff;

					//Now we apply the filter (multiply corresponding number in matrix by this pixel) for each colour channel
					red = (int) (red * filter.getMatrix()[row][col]);
					green = (int) (green * filter.getMatrix()[row][col]);
					blue = (int) (blue * filter.getMatrix()[row][col]);

					//Re-create a 32-bit RG``B pixel from the channels by bit-shifting values into their proper place;
					rgb = 0;
					rgb = rgb | (red << 16);
					rgb = rgb | (green << 8);
					rgb = rgb | blue;

					element += rgb;
					//System.out.println("["+x+"]["+y+"] * "+"["+filter.getMatrix()[row][col]+"] = "+rgb);//DEBUG
				} 
				catch (Exception e) {
					continue; //Ignore exceptions
				}
			}
		}
		//Set RGB for this pixel to comination of R,G and B channels, resulting in full colour again.
		//image.setRGB(x, y, rgb); //Set the pixel colour at (x, y) to red
		//System.out.println("After Change:"+element);//DEBUG

		return element;
	}
}
