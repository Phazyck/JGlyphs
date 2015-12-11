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
		//TODO eval glyph using NN from ANJI
		double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph);
		
		double[] response =  network.next(stimulus);
		
		return response[0];
	}
	
	public boolean doesPass(Glyph glyph) {
		
		double fitness = evaluate(glyph);
		
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
	}
}
