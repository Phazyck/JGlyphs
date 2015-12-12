package dk.itu.jglyph.neat;

import dk.itu.jglyph.user.Model;

/**
 * Storing values for exposure to ANJI fitness functions
 */
public class TrainingSet {

	public static Model model;

	// Legacy values, consider removing
	public static double[][] stimuli;
	public static double[][] targets;

	// Static class
	private TrainingSet() {};

}
