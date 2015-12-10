package dk.itu.jglyph;

import com.anji.integration.Activator;

import dk.itu.jglyph.features.FeatureExtractors;

public class Filter {
	
	private final static double THRESHHOLD = 0.5f;
	
	// Neural Network activation object
	private Activator currentNetwork;
	
	public Filter() {
		// TODO take care of everything (get it set up in default state)
	}
	
	public double evaluate(JGlyph glyph) {
		//TODO eval glyph using NN from ANJI
		
		double edgeCount = FeatureExtractors.edgeCount(glyph);
		
//		System.out.println(edgeCount);
		
		double score = 1;
		
		if(edgeCount < 2)
		{
			score -= (2 - edgeCount);
		}
		else if(edgeCount > 5)
		{
			score -= (edgeCount - 5);
		}
		
		return(score);
	}
	
	public boolean doesPass(JGlyph glyph) {
		
		double fitness = evaluate(glyph);
		boolean pass = fitness >= THRESHHOLD;
		return(pass);
	}
	
	public void update(JGlyph glyph, boolean classification) {
		//TODO update filter (reevolve), adding the params to the training set
		//TODO only re-evolve after X new classifications added
	}

}
