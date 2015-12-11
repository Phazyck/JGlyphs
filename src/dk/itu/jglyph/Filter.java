package dk.itu.jglyph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

import com.anji.integration.Activator;
import com.anji.util.Properties;

import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.neat.NeatUtil;
import dk.itu.jglyph.neat.StimulusTargetPair;

public class Filter {
	
	private final static String PROPERTIES_FILE_NAME = "glyph.properties";
	private Properties properties;
	
	private Evaluator evaluator;
	
	// training data gathered so far
	private List<StimulusTargetPair> trainingData;
	
	public Evaluator getEvaluator()
	{
		return(evaluator);
	}
	
	public Filter() {
		// TODO take care of everything (get it set up in default state)
		
		trainingData = new ArrayList<>();
		
		// properties file object
		try {
			properties = new Properties( PROPERTIES_FILE_NAME );
			
			int extractorCount = FeatureExtractors.getInstance().totalExtractors();
			
			properties.setProperty("stimulus.size", Integer.toString(extractorCount));
			
			// NEAT needs at least one stim/target pair, so feed with dummy for starters
			List<StimulusTargetPair> temp = new ArrayList<>();
			double[] tempstim = new double[extractorCount];
			double[] temptarg = {1};
			temp.add(new StimulusTargetPair(tempstim, temptarg));			
			
			updateEvalutor(temp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateEvalutor(List<StimulusTargetPair> data)
	{
		Activator network = NeatUtil.doEvolution(data, properties);
		evaluator = new Evaluator(network);
	}
	
	private boolean evolving = false;
	
	public void update(Glyph glyph, boolean classification) {
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
					updateEvalutor(trainingData);
					
					evolving = false;
					
					return null;
				}
			};
			
			worker.execute();			
		}
	}
}
