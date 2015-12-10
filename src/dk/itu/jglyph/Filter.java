package dk.itu.jglyph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anji.integration.Activator;
import com.anji.util.Properties;

import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.neat.NeatUtil;
import dk.itu.jglyph.neat.StimulusTargetPair;

public class Filter {
	
	private final static String PROPERTIES_FILE_NAME = "glyph.properties";
	private Properties properties;
	
	// Neural Network activation object
	private Activator currentNetwork;
	
	// training data gathered so far
	private List<StimulusTargetPair> trainingData;
	
	public Filter() {
		// TODO take care of everything (get it set up in default state)
		
		trainingData = new ArrayList<>();
		
		// properties file object
		try {
			properties = new Properties( PROPERTIES_FILE_NAME );
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean evaluate(JGlyph glyph) {
		//TODO eval glyph using NN from ANJI
		return true;
	}
	
	public void update(JGlyph glyph, boolean classification) {
		//TODO only re-evolve after X new classifications added
		
		// call feature extractor get double array
		double[] stimulus = null; // TODO call new feature extractor aggregate
		
		double[] target = { classification ? 1.0 : 0.0 };
		
		//  add to trainingdata
		trainingData.add(new StimulusTargetPair(stimulus, target));
		
		NeatUtil.setTrainingSetValues(trainingData);
		
		currentNetwork = NeatUtil.doEvolution(properties);
	}
}
