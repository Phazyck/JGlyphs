package dk.itu.jglyph;

import com.anji.integration.Activator;

import dk.itu.jglyph.features.FeatureExtractors;

/**
 * A class for evaluating glyphs.
 */
public class Evaluator 
{
	/**
	 * The neural network that implements the actual evaluation.
	 */
	private final Activator network;
	
	/**
	 * Constructs a new evaluator with a given neural network.
	 * 
	 * @param network The neural network.
	 */
	public Evaluator(Activator network)
	{
		this.network = network;
	}
	
	/**
	 * Evaluates a given glyph.
	 * 
	 * @param glyph The glyph.
	 * @return A fitness score of the given glyph.
	 */
	public double evaluate(Glyph glyph) {
		if (network == null) return 1.0; // TODO dunno if this is a good idea
		
		double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph);
		
		double[] response =  network.next(stimulus);
		
		return response[0];
	}
}
