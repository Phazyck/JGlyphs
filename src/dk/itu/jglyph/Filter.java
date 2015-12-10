package dk.itu.jglyph;

import com.anji.integration.Activator;

public class Filter {
	
	// Neural Network activation object
	private Activator currentNetwork;
	
	public Filter() {
		// TODO take care of everything (get it set up in default state)
	}
	
	public boolean evaluate(JGlyph glyph) {
		//TODO eval glyph using NN from ANJI
		return true;
	}
	
	public void update(JGlyph glyph, boolean classification) {
		//TODO update filter (reevolve), adding the params to the training set
		//TODO only re-evolve after X new classifications added
	}

}
