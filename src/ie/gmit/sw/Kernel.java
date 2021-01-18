package ie.gmit.sw;

//Holds data for each filter kernel/ convo matrix
/**@author Ryan Clinton
 * @version 2019-12 (4.14.0)
 * 
 * Kernel / Filter / Convolution Matrix class
 */
public class Kernel {
	/**
	 * Name of this filter
	 */
	private String name;
	/**
	 * The convolution matrix for this filter
	 */
	private double[][] matrix;
	
	
	/**
	 * @param name - The name of this filter
	 * @param matrix - A 2d matrix of doubles. The convolution matrix for this filter.
	 */
	public Kernel(String name, double[][] matrix) {
		super();
		
		this.name = name;
		this.matrix = matrix;
	}
	
	/**
	 * @return name - name of filter
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return matrix - convolution matrix for this filter
	 */
	public double[][] getMatrix() {
		return matrix;
	}

	/**
	 * @param matrix
	 */
	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}
}
