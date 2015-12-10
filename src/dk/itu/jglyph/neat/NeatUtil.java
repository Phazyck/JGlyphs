package dk.itu.jglyph.neat;

import java.util.List;
import java.util.Map;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.neat.Evolver;
import com.anji.util.Properties;

import dk.itu.jglyph.JGlyph;

public class NeatUtil {
	
	// Values for loading neural networks
	private final static String TRANSCRIBER_CLASS_KEY = "glyph.transcriber";
	
	public static Activator doEvolution(List<StimulusTargetPair> trainingData, Properties properties) {
		// set TrainingSet.stimuli/values
		int count = trainingData.size();
		
		System.out.println(count);
		
		double[][] stimuli = new double[count][];
		double[][] targets = new double[count][];
		
		for (int i = 0; i < count; i++) {
			StimulusTargetPair stp = trainingData.get(i);
			
			stimuli[i] = stp.stimulus;
			targets[i] = stp.target;
		}
		
		TrainingSet.stimuli = stimuli;
		TrainingSet.targets = targets;
		
		System.out.println(TrainingSet.stimuli.length);
		System.out.println(TrainingSet.targets.length);
		
		
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