package dk.itu.jglyph.neat;

import java.util.List;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.neat.Evolver;
import com.anji.util.Properties;

import dk.itu.jglyph.user.Model;

public class NeatUtil {
	
	// Values for loading neural networks
	private final static String TRANSCRIBER_CLASS_KEY = "glyph.transcriber";
	
	public static Activator doEvolution(Model model, Properties properties) {
		TrainingSet.model = new Model(model);
		
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
