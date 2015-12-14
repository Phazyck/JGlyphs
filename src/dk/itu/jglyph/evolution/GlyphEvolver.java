package dk.itu.jglyph.evolution;

import java.util.ArrayList;
import java.util.TreeSet;

import dk.itu.jglyph.Evaluator;
import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.util.Random;

/**
 * A class for evolving glyphs using a given fitness function.
 */
public class GlyphEvolver 
{
	/**
	 * The default population size.
	 */
	private final static int DEFAULT_POPULATION_SIZE = 256;
	/**
	 * The default maximum number of generations an evolution run should go through.
	 */
	private final static int DEFAULT_NUMBER_OF_GENERATIONS = 1024;

	/**
	 * The default percentage of best individuals that should be picked out for survival. 
	 */
	private final static double DEFAULT_BEST_SURVIVOR_RATE = 0.3;
	/**
	 * The default percentage of total individuals that should be picked out for survival. 
	 */
	private final static double DEFAULT_TOTAL_SURVIVOR_RATE = 0.5;
	
	/**
	 * The default chance that a mutation will be performed. 
	 */
	private final static double DEFAULT_MUTATION_CHANCE = 0.2;
	/**
	 * The default chance that a crossover will be performed. 
	 */
	private final static double DEFAULT_CROSSOVER_CHANCE = 0.95;
	
	/**
	 * The default width (amount of nodes) of a glyph.
	 */
	private final static int DEFAULT_GLYPH_WIDTH = 3;
	/**
	 * The default height (amount of nodes) of a glyph.
	 */
	private final static int DEFAULT_GLYPH_HEIGHT = 3;
	
	/**
	 * The current population size.
	 */
	private int populationSize;
	/**
	 * The current maximum number of generations an evolution run should go through.
	 */
	private int numberOfGenerations;
	
	/**
	 * The current percentage of best individuals that should be picked out for survival. 
	 */
	private double bestSurvivorRate;
	/**
	 * The current percentage of total individuals that should be picked out for survival. 
	 */
	private double totalSuvirvorRate;
	
	/**
	 * The current chance that a mutation will be performed. 
	 */
	private double mutationChance;
	/**
	 * The current chance that a crossover will be performed. 
	 */
	private double crossoverChance;
	
	/**
	 * The current width (amount of nodes) of a glyph.
	 */
	private int glyphWidth;
	/**
	 * The current height (amount of nodes) of a glyph.
	 */
	private int glyphHeight;
	
	/**
	 * The current evaluator.
	 */
	private Evaluator evaluator;
	
	/**
	 * The current population.
	 */
	private TreeSet<Subject> population;
	
	/**
	 * Constructs a new glyph evolver with a given evaluator.
	 * 
	 * @param evaluator The evaluator.
	 */
	public GlyphEvolver(Evaluator evaluator)
	{
		GlyphEvolverProperties properties = new GlyphEvolverProperties("glyph.properties");
		
		populationSize = properties.tryGetInt("popul.size", DEFAULT_POPULATION_SIZE);
		numberOfGenerations = properties.tryGetInt("num.generations", DEFAULT_NUMBER_OF_GENERATIONS);
		bestSurvivorRate = properties.tryGetDouble("survivor.rate.best", DEFAULT_BEST_SURVIVOR_RATE);
		totalSuvirvorRate = properties.tryGetDouble("survivor.rate.total", DEFAULT_TOTAL_SURVIVOR_RATE);
		mutationChance = properties.tryGetDouble("mutation.chance", DEFAULT_MUTATION_CHANCE);
		crossoverChance = properties.tryGetDouble("crossover.chance", DEFAULT_CROSSOVER_CHANCE);
		glyphWidth = properties.tryGetInt("glyph.width", DEFAULT_GLYPH_WIDTH);
		glyphHeight= properties.tryGetInt("glyph.height", DEFAULT_GLYPH_HEIGHT);
				
		init(evaluator);
	}
	
	/**
	 * Gets the current population.
	 * 
	 * @return The population.
	 */
	public Iterable<Subject> getPopulation()
	{
		return population;
	}
	
	/**
	 * (Re)initializes the evolver with a given evaluator.
	 * 
	 * @param evaluator The evaluator.
	 */
	public void init(Evaluator evaluator)
	{
		this.evaluator = evaluator;
		
		population = new TreeSet<Subject>(Subject.COMPARATOR_DESCENDING);
		
		Glyph glyph = new Glyph(glyphWidth, glyphHeight);
		
		while(population.size() < populationSize)
		{
			glyph.randomizeEdges();
			
			population.add(makeSubject(glyph.copy()));
		}
	}
	
	/**
	 * Generates a population of glyphs with a wide variety of features.
	 * 
	 * NOTE(oliver): This doesn't really belong here.
	 * NOTE(oliver): This only makes variety in regards to edge counts.
	 * 
	 * @param size The amount of desired glyphs.
	 * @return The generated glyphs.
	 */
	public ArrayList<Glyph> getDistributedPopulation(int size)
	{
		ArrayList<Glyph> result = new ArrayList<>();
		
		Glyph glyph = new Glyph(glyphWidth, glyphHeight);
		
		for(int i = 0; i < size; ++i)
		{
			while(glyph.getEdges().size() < i)
			{	
				Glyph clone = glyph.copy();
				clone.mutate();
				if(clone.getEdges().size() > glyph.getEdges().size())
				{
					glyph = clone;
				}				
			}
			
			while(glyph.getEdges().size() > i)
			{	
				Glyph clone = glyph.copy();
				clone.mutate();
				if(clone.getEdges().size() < glyph.getEdges().size())
				{
					glyph = clone;
				}				
			}
			
			result.add(glyph.copy());
		}
		
		return(result);
	}
	
	/**
	 * Assigns a new evaluator to the evolver.
	 * @param evaluator
	 */
	public void setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	/**
	 * Constructs a random glyph.
	 * 
	 * @return The random glyph.
	 */
	public Glyph randomGlyph()
	{
		Glyph glyph = new Glyph(glyphWidth, glyphHeight);
		glyph.randomizeEdges();
		return glyph;
	}
	
	/**
	 * Calculates the fitness of a glyph.
	 * 
	 * @param glyph The glyph.
	 * @return The fitness.
	 */
	private double getFitness(Glyph glyph)
	{
		double fitness = evaluator.evaluate(glyph);
		
		return(fitness);
	}
	
	/**
	 * Makes a population subject from a glyph.
	 * 
	 * @param glyph The glyph.
	 * @return The subject.
	 */
	private Subject makeSubject(Glyph glyph)
	{
		double fitness = getFitness(glyph);
		Subject subject = new Subject(glyph, fitness);
		return(subject);
	}
	
	/**
	 * Gets the best subject of the entire current population.
	 * 
	 * @return The champion.
	 */
	public Subject getChampion()
	{
		Subject champion = population.first();
		
		return(champion);
	}

	/**
	 * Evolves the population of glyphs.
	 */
	public void evolve()
	{
		for(int generation = 0; generation < numberOfGenerations; ++generation)
		{
			int length = population.size();
			
			Subject[] subjects = new Subject[length];
			
			int idx = 0;
			for(Subject subject : population)
			{
				subjects[idx++] = subject;
			}
			
			// TODO We could add new random nodes to keep diversity as we update the fitness function
			
			int top = (int)(length * bestSurvivorRate);
			int bottom = subjects.length - top;
			
			Random.shufflePortion(subjects, top, bottom);
			
			int survivors = (int)(length * totalSuvirvorRate);
			
			breedChildren(subjects, survivors);
			
			// We need to remember to add the new subjects to population
			population.clear();
			for (Subject subject : subjects) {
				population.add(subject);
			}
			
			
		}
		Subject champion = getChampion();

		double bestFitness = champion.fitness;
		double worstFitness = population.last().fitness;

		System.out.printf("Best fitness %f\n", bestFitness);
		System.out.printf("Worst fitness %f\n", worstFitness);
		System.out.println();
	}
	
	/**
	 * Breeds new children in a population of subjects with some amount of survivors.
	 * 
	 * @param subjects The subjects.
	 * @param survivors The survivors.
	 */
	private void breedChildren(Subject[] subjects, int survivors)
	{
		int count = subjects.length - survivors;
		
		for(int idx = 0;
			idx < count;
			idx += 2)
		{
			Subject gene1 = 
					Random.pickRandom(subjects, 0, survivors);
			
			Glyph data1 = gene1.glyph.copy();
			
			if((count - idx) == 1)
			{
				data1.mutate();
				subjects[survivors + idx] = makeSubject(data1);
				break;
			}
			
			Subject gene2 = Random.pickRandom(subjects, 0, survivors, gene1);
			Glyph data2 = gene2.glyph.copy();
			
			boolean doMutation = Random.getBoolean(mutationChance);
			boolean doCrossover = !doMutation || Random.getBoolean(crossoverChance);
			
			if(doCrossover)
			{
				data1.crossWith(data2);
			}
			
			if(doMutation)
			{
				data1.mutate();
				data2.mutate();
			}
			
			subjects[survivors + idx + 0] = makeSubject(data1);
			subjects[survivors + idx + 1] = makeSubject(data2);
		}
	}
}
