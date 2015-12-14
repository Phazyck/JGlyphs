package dk.itu.jglyph.neat;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;
import com.anji.util.Configurable;
import com.anji.util.Properties;

import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.neat.TrainingSet;
import dk.itu.jglyph.user.Model;
import dk.itu.jglyph.user.Node;

/**
 * Determines fitness based on if this network respects the relations in the user model
 * (Based on com.anji.integration.TargetFitnessFunction)
 */
public class ModelFitnessFunction implements BulkFitnessFunction, Configurable {
	/**
	 * Auto-generated serial version UID.
	 */
	private static final long serialVersionUID = -2295453770025929525L;

	/**
	 * The highest possible fitness value.
	 */
	private final static int MAX_FITNESS = 1000000;

	/**
	 * A utility object for logging.
	 */
	private static Logger logger = Logger.getLogger( ModelFitnessFunction.class );

	private final static String ADJUST_FOR_NETWORK_SIZE_FACTOR_KEY = "fitness.function.adjust.for.network.size.factor";
	private float adjustForNetworkSizeFactor = 0.0f;
	
	private final static String MINIMUM_DIFFERENCE_KEY = "glyph.fitness.minimum.difference";
	private double minimumDifference;
	
	/**
	 * The highest fitness value produced thus far.
	 */
	private int maxFitnessValue;

	/**
	 * The transcriber the produces neural networks from chromosomes.
	 */
	private ActivatorTranscriber activatorFactory;

	/**
	 * The user model.
	 */
	private Model model;

	/**
	 * The amount of stimuli.
	 */
	private int stimuliCount;
	
	/**
	 * The amount of responses.
	 */
	private int responseCount;

	
	/**
	 * See <a href=" {@docRoot}/params.htm" target="anji_params">Parameter Details </a> for
	 * specific property settings.
	 * 
	 * @param props configuration parameters
	 */
	public void init(Properties props) 
	{
		try {
			activatorFactory = (ActivatorTranscriber) props
					.singletonObjectProperty( ActivatorTranscriber.class );

			adjustForNetworkSizeFactor = props.getFloatProperty( ADJUST_FOR_NETWORK_SIZE_FACTOR_KEY,
					0.0f );
			
			minimumDifference = props.getDoubleProperty( MINIMUM_DIFFERENCE_KEY,
					0.1 );

			// Figure out sizes for stimlui/response layers
			stimuliCount = props.getIntProperty("stimulus.size");
			responseCount = props.getIntProperty("response.size");

			// Read in model to grab relations from 
			model = TrainingSet.model;
			
			setMaxFitnessValue(MAX_FITNESS);
		}
		catch ( Exception e ) {
			throw new IllegalArgumentException( "invalid properties: " + e.getClass().toString() + ": "
					+ e.getMessage() );
		}
	}

	/**
	 * @param aMaxFitnessValue maximum raw fitness this function will return
	 */
	protected void setMaxFitnessValue(int aMaxFitnessValue) 
	{
		int minGenes = stimuliCount + responseCount;
		maxFitnessValue = aMaxFitnessValue - (int) ( adjustForNetworkSizeFactor * minGenes );
	}

	/**
	 * Iterates through chromosomes. For each, transcribe it to an <code>Activator</code> and
	 * present the stimuli to the activator. The stimuli are presented in random order to ensure the
	 * underlying network is not memorizing the sequence of inputs. Calculation of the fitness based
	 * on error is delegated to the subclass. This method adjusts fitness for network size, based on
	 * configuration.
	 * 
	 * @param genotypes <code>List</code> contains <code>Chromosome</code> objects.
	 * @see ModelFitnessFunction#calculateErrorFitness(double[][], double, double)
	 */
	@SuppressWarnings("rawtypes")
	final public void evaluate(List genotypes) 
	{
		// screw you, old java! :<
		@SuppressWarnings("unchecked")
		List<Chromosome> chromosomes = (List<Chromosome>) genotypes;

		for (Chromosome chromosome : chromosomes) {
			try {
				Activator activator = activatorFactory.newActivator( chromosome );

				// TODO grab all edges and end with an array of response-diffs and targets
				List<Double> diffs = new ArrayList<>();
				List<Double> scores = new ArrayList<>();

				for (Node parent : model.getNodes()) {
					for (Node child : parent.getChildren()) {
						diffs.add(getErrorDifference(activator, parent.glyph, child.glyph));
					}
					double[] parentStimulus = FeatureExtractors.getInstance().extractFeatures(parent.glyph);
					scores.add(activator.next(parentStimulus)[0]);
				}
				
				double minResponse = Double.POSITIVE_INFINITY, maxResponse = Double.NEGATIVE_INFINITY; 
				
				for (Double d : scores) {
					minResponse = Math.min(minResponse, d);
					maxResponse = Math.max(maxResponse, d);
				}
				
				
				int fitness = calculateErrorFitness( diffs, minResponse,
						maxResponse )
						- (int) ( adjustForNetworkSizeFactor * chromosome.size() );
				
				chromosome.setFitnessValue( fitness );
			}
			catch ( TranscriberException e ) {
				logger.warn( "transcriber error: " + e.getMessage() );
				chromosome.setFitnessValue( 1 );
			}
		}
	}
	
	/**
	 * Calculates the error produced by the neural network, when evaluating a parent and child.
	 * @param activator The neural network.
	 * @param parent The parent.
	 * @param child The child.
	 * @return The error.
	 */
	private double getErrorDifference(Activator activator, Glyph parent, Glyph child) {
		double[] parentStimulus = FeatureExtractors.getInstance().extractFeatures(parent);
		double[] childStimulus = FeatureExtractors.getInstance().extractFeatures(child);
		
		double parentScore = activator.next(parentStimulus)[0];
		double childScore = activator.next(childStimulus)[0];
		
		double difference = (childScore + minimumDifference) - parentScore; // Add epsilon to make sure they aren't equal
		
		return Math.max(difference, 0.0);
	}
	

	/**
	 * Sum all differences, subtract from max
	 * fitness, and square result.
	 * 
	 * @param differences between a response and the ideal response (0 = response was good)
	 * @param minResponse
	 * @param maxResponse
	 * @return result of calculation
	 */
	private int calculateErrorFitness( List<Double> diffs, double minResponse, double maxResponse ) {

		double maxSumDiff = getMaxError(
				diffs.size(),
				( maxResponse - minResponse + minimumDifference ), 
				false );
		double maxRawFitnessValue = Math.pow( maxSumDiff, 2 );

		double sumDiff = 0;
		for (double d : diffs) {
			sumDiff += d;
		}

		if ( sumDiff > maxSumDiff + .00000001 )
			throw new IllegalStateException( "sum diff > max sum diff" );
		
		double rawFitnessValue = Math.pow( maxSumDiff - sumDiff, 2 );
		double skewedFitness = ( rawFitnessValue / maxRawFitnessValue ) * MAX_FITNESS;
		int result = (int) skewedFitness;
		return result;
	}

	/**
	 * @param responseCount
	 * @param minMaxRange
	 * @param sumOfSquares
	 * @return maximum error given the number and range of responses
	 */
	private double getMaxError( int responseCount, double minMaxRange, boolean sumOfSquares) {
		double maxDiffPerResponse = minMaxRange;
		if ( sumOfSquares )
			maxDiffPerResponse *= maxDiffPerResponse;
		return maxDiffPerResponse * responseCount;
	}


	/**
	 * @return maximum possible fitness value for this function
	 */
	public int getMaxFitnessValue() {
		return maxFitnessValue;
	}

}
