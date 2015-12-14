package dk.itu.jglyph;

import java.io.IOException;
import javax.swing.SwingWorker;

import com.anji.integration.Activator;
import com.anji.util.Properties;

import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.neat.NeatUtil;
import dk.itu.jglyph.user.Model;

/**
 * A class for filtering nodes using an evolved fitness function.
 */
public class Filter {
	
	/**
	 * The file that contains the properties tied to this class.
	 */
	private final static String PROPERTIES_FILE_NAME = "glyph.properties";
	
	/**
	 * The properties object for accessing properties related to this class. 
	 */
	private Properties properties;
	
	/**
	 * The glyph evaluator.
	 */
	private Evaluator evaluator;
	
	/**
	 * The constructed user model, based on recorded choices.
	 */
	private Model model;
	
	/**
	 * Gets the current evaluator which is used for filtering.
	 * 
	 * @return The evaluator.
	 */
	public Evaluator getEvaluator()
	{
		return(evaluator);
	}
	
	/**
	 * Constructs new filter.
	 */
	public Filter() {
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
	
	/**
	 * Evolves the internal evaluator.
	 */
	private void evolveEvalutor()
	{
		if (model.isEmpty()) {
			evaluator = new Evaluator(null); // TODO get this hack out of the way
			return; // Nothing to evolve upon yet
		}
		Activator network = NeatUtil.doEvolution(model, properties);
		evaluator = new Evaluator(network);
	}
	
	/**
	 * Indicates whether or not the filter is currently doing evolution.
	 */
	private boolean evolving = false;
	
	/**
	 * Updates the filter, by providing additional information about glyphs.
	 * 
	 * @param better The glyph that is better than the worse glyph.
	 * @param worse The glyph that is worse than the better glyph.
	 */
	public void update(Glyph better, Glyph worse) {
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

	/**
	 * Gets the recorded user model.
	 * @return The user model
	 */
	public Model getModel() {
		return model;
	}
}
