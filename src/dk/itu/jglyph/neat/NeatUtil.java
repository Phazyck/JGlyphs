package dk.itu.jglyph.neat;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.neat.Evolver;
import com.anji.util.Properties;

import dk.itu.jglyph.user.Model;

/**
 * A utility class for doing NeuroEvolution of Augmenting Topologies.
 */
public class NeatUtil 
{
	/**
	 * Values for loading neural networks.
	 */
	private final static String TRANSCRIBER_CLASS_KEY = "glyph.transcriber";
	
	/**
	 * Evolves a neural network using the given user model and properties.
	 * 
	 * @param model The user model.
	 * @param properties The properties.
	 * @return The evolved neural network.
	 */
	public static Activator doEvolution(Model model, Properties properties) {
		TrainingSet.model = model.copy();
		System.out.println("Current # of relations:\t" + model.relationCount());
		
		// Evolve new network
		
		Activator result = null;
		
		try {
			Evolver evolver = new Evolver();
			evolver.init(properties);
			evolver.run();
			
			ActivatorTranscriber transcriber = (ActivatorTranscriber) properties.newObjectProperty( TRANSCRIBER_CLASS_KEY );

			result = transcriber.newActivator( evolver.getChamp() );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
