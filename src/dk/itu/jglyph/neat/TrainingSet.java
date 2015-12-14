package dk.itu.jglyph.neat;

import dk.itu.jglyph.user.Model;

/**
 * A class for storing global values for exposure to ANJI fitness functions.
 */
public class TrainingSet 
{
	/**
	 * The user model.
	 */
	public static Model model;

	// NOTE: Legacy values, consider removing
	
	/**
	 * The stimuli of the training set.
	 */
	public static double[][] stimuli;
	/**
	 * The target output of the training set.
	 */
	public static double[][] targets;

	/**
	 * This class is not supposed to be instantiated.
	 */
	private TrainingSet() {};

}
