package dk.itu.jglyph;

import java.io.IOException;
import java.util.List;
import javax.swing.SwingWorker;

import com.anji.integration.Activator;
import com.anji.util.Properties;

import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.neat.NeatUtil;
import dk.itu.jglyph.neat.StimulusTargetPair;
import dk.itu.jglyph.user.Model;

public class Filter {
	
	private final static String PROPERTIES_FILE_NAME = "glyph.properties";
	private Properties properties;
	
	private Evaluator evaluator;
	
	// training data gathered so far
	private Model model;
	
	public Evaluator getEvaluator()
	{
		return(evaluator);
	}
	
	public Filter() {
		// TODO take care of everything (get it set up in default state)
		
		model = new Model();
		
		// properties file object
		try {
			properties = new Properties( PROPERTIES_FILE_NAME );
			
			int extractorCount = FeatureExtractors.getInstance().totalExtractors();
			
			properties.setProperty("stimulus.size", Integer.toString(extractorCount));
			
			evolveEvalutor();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void evolveEvalutor()
	{
		List<StimulusTargetPair> data = model.getTrainingSet();
		Activator network = NeatUtil.doEvolution(data, properties);
		evaluator = new Evaluator(network);
	}
	
	private boolean evolving = false;
	
	public void update(Glyph better, Glyph worse) {
		//TODO only re-evolve after X new classifications added, instead of every time
		
		model.addRelation(better, worse);
		
		if(!evolving)
		{
			evolving = true;
			SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

				@Override
				protected Object doInBackground() throws Exception {
					
					// Evolve new network
					evolveEvalutor();
					
					evolving = false;
					
					return null;
				}
			};
			
			worker.execute();			
		}
	}
}
