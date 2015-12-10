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
import dk.itu.jglyph.neat.TrainingSet;
import dk.itu.jglyph.features.FeatureExtractors;

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
			
			// NEAT needs at least one stim/target pair, so feed with dummy for starters
			List<StimulusTargetPair> temp = new ArrayList<>();
			double[] tempstim = new double[14];
			double[] temptarg = {1};
			temp.add(new StimulusTargetPair(tempstim, temptarg));			
			
			currentNetwork = NeatUtil.doEvolution(temp, properties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean evaluate(JGlyph glyph) {
		//TODO eval glyph using NN from ANJI
		double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph);
		
		double[] response =  currentNetwork.next(stimulus);
		
		if (response[0] > 0.8) { // TODO go from random threshold defined here to something better
			return true; 
		}
		else return false;
	}
	
	public void update(JGlyph glyph, boolean classification) {
		//TODO only re-evolve after X new classifications added, instead of every time
		
		// call feature extractor get double array
		double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph); 
		
		double[] target = { classification ? 1.0 : 0.0 };
		
		//  add to trainingdata
		trainingData.add(new StimulusTargetPair(stimulus, target));
		
		// Evolve new network
		currentNetwork = NeatUtil.doEvolution(trainingData, properties);
	}
}
