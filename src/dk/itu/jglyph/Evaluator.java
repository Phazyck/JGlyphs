package dk.itu.jglyph;

import com.anji.integration.Activator;

import dk.itu.jglyph.features.FeatureExtractors;

public class Evaluator 
{
	// Neural Network activation object
	private Activator network;
	
	public Evaluator(Activator network)
	{
		this.network = network;
	}
	
	private double threshhold = 1f;
	
	public double evaluate(Glyph glyph) {
		double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph);
		
		double[] response =  network.next(stimulus);
		
		return response[0];
	}
	
	/* pass is always false so this doesnt work as is
	public boolean doesPass(Glyph glyph) {
		
		double fitness = evaluate(glyph);
		
		// WTF IS THIS???
		boolean pass = fitness + Double.MIN_VALUE >= threshhold;
		
		if(pass)
		{
			threshhold = 1;
		}
		else
		{
			threshhold *= 0.9;
		}
		
		return(pass);
	}*/
}
