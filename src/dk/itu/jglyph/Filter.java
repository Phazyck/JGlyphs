package dk.itu.jglyph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

import com.anji.integration.Activator;
import com.anji.util.Properties;

import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.neat.NeatUtil;
import dk.itu.jglyph.neat.StimulusTargetPair;

public class Filter {
	
	private final static String PROPERTIES_FILE_NAME = "glyph.properties";
	private Properties properties;
	private double threshhold = 1f;
	
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
	
	public double evaluate(JGlyph glyph) {
		//TODO eval glyph using NN from ANJI
		double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph);
		
		double[] response =  currentNetwork.next(stimulus);
		
		return response[0];
	}
	
	public boolean doesPass(JGlyph glyph) {
		
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
	
	private boolean evolving = false;
	
	public void update(JGlyph glyph, boolean classification) {
		//TODO only re-evolve after X new classifications added, instead of every time
		
		// call feature extractor get double array
		double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph); 
		
		double[] target = { classification ? 1.0 : 0.0 };
		
		//  add to trainingdata
		trainingData.add(new StimulusTargetPair(stimulus, target));
		
		if(!evolving)
		{
			evolving = true;
			SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

				@Override
				protected Object doInBackground() throws Exception {
					
					// Evolve new network
					currentNetwork = NeatUtil.doEvolution(trainingData, properties);
					
					evolving = false;
					
					return null;
				}
			};
			
			worker.execute();			
		}
		
		
		

	}
}
