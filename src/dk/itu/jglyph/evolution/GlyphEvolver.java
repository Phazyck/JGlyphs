package dk.itu.jglyph.evolution;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import dk.itu.jglyph.Evaluator;
import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.util.Random;

public class GlyphEvolver 
{
	private final static int DEFAULT_POPULATION_SIZE = 256;
	private final static int DEFAULT_NUMBER_OF_GENERATIONS = 1024;
	
	private final static double DEFAULT_FITNESS_TARGET = 0.8;
	
	private final static double DEFAULT_BEST_SURVIVOR_RATE = 0.3;
	private final static double DEFAULT_TOTAL_SURVIVOR_RATE = 0.5;
	
	private final static double DEFAULT_PARENT_PICK_CHANCE = 0.5;
	private final static double DEFAULT_PARENT_PICK_CHANCE_INCREMENT = -0.01;
	
	private final static double DEFAULT_MUTATION_CHANCE = 0.2;
	private final static double DEFAULT_CROSSOVER_CHANCE = 0.95;
	
	private final static int DEFAULT_GLYPH_WIDTH = 3;
	private final static int DEFAULT_GLYPH_HEIGHT = 3;
	
	private int populationSize;
	private int numberOfGenerations;
	
	private double fitnessTarget;
	
	private double bestSurvivorRate;
	private double totalSuvirvorRate;
	
	private double parentPickChance;
	private double parentPickChanceIncrement;
	
	private double mutationChance;
	private double crossoverChance;
	
	private int glyphWidth;
	private int glyphHeight;
	
	private Evaluator evaluator;
	
	private TreeSet<Subject> population;
	
	public GlyphEvolver(Evaluator evaluator)
	{
		GlyphEvolverProperties properties = new GlyphEvolverProperties("glyph.properties");
		
		populationSize = properties.tryGetInt("popul.size", DEFAULT_POPULATION_SIZE);
		numberOfGenerations = properties.tryGetInt("num.generations", DEFAULT_NUMBER_OF_GENERATIONS);
		fitnessTarget = properties.tryGetDouble("fitness.target", DEFAULT_FITNESS_TARGET);
		bestSurvivorRate = properties.tryGetDouble("survivor.rate.best", DEFAULT_BEST_SURVIVOR_RATE);
		totalSuvirvorRate = properties.tryGetDouble("survivor.rate.total", DEFAULT_TOTAL_SURVIVOR_RATE);
		parentPickChance = properties.tryGetDouble("parent.pick.chance", DEFAULT_PARENT_PICK_CHANCE);
		parentPickChanceIncrement = properties.tryGetDouble("parent.pick.chance.increment", DEFAULT_PARENT_PICK_CHANCE_INCREMENT);
		mutationChance = properties.tryGetDouble("mutation.chance", DEFAULT_MUTATION_CHANCE);
		crossoverChance = properties.tryGetDouble("crossover.chance", DEFAULT_CROSSOVER_CHANCE);
		glyphWidth = properties.tryGetInt("glyph.width", DEFAULT_GLYPH_WIDTH);
		glyphHeight= properties.tryGetInt("glyph.height", DEFAULT_GLYPH_HEIGHT);
				
		init(evaluator);
	}
	
	public Iterable<Subject> getPopulation()
	{
//		TreeSet<Subject> clone = new TreeSet<Subject>(population); // NOTE this is not a deep copy, if that's what you've assumed
//		
//		return(clone);
		
		return population;
	}
	
	public void init(Evaluator evaluator)
	{
		this.evaluator = evaluator;
		
		population = new TreeSet<Subject>(Subject.COMPARATOR_DESCENDING);
		
		while(population.size() < populationSize)
		{
			Subject subject = makeSubject(randomGlyph());
			population.add(subject);
		}
	}
	
	public void setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	public Glyph randomGlyph()
	{
		Glyph glyph = new Glyph(glyphWidth, glyphHeight);
		glyph.randomizeEdges();
		return glyph;
	}
	
	private double getFitness(Glyph glyph)
	{
		double fitness = evaluator.evaluate(glyph);
		
		return(fitness);
	}
	
	private Subject makeSubject(Glyph glyph)
	{
		double fitness = getFitness(glyph);
		Subject subject = new Subject(glyph, fitness);
		return(subject);
	}
	
	public Subject getChampion()
	{
		Subject champion = population.first();
		
		return(champion);
	}

	public void evolve()
	{
		for(int generation = 0; generation < numberOfGenerations; ++generation)
		{
			if(getChampion().fitness >= fitnessTarget)
			{
				break;
			}
			
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
			
			Subject champion = getChampion();
			
			double bestFitness = champion.fitness;
			double worstFitness = population.last().fitness;
			
			System.out.printf("Generation #%d\n", generation);
			System.out.printf("Best fitness %f\n", bestFitness);
			System.out.printf("Worst fitness %f\n", worstFitness);
			System.out.println();
		}
	}
	
	private void breedChildren(Subject[] subjects, int survivors)
	{
		int count = subjects.length - survivors;
		
		for(int idx = 0;
			idx < count;
			idx += 2)
		{
			Subject gene1 = Random.pickChanceDescending(subjects, survivors, count, parentPickChance, parentPickChanceIncrement);
			
			Glyph data1 = gene1.glyph.clone();
			
			if((count - idx) == 1)
			{
				data1.mutate();
				subjects[survivors + idx] = makeSubject(data1);
				break;
			}
			
			Subject gene2 = Random.pickRandom(subjects, survivors, count, gene1);
			Glyph data2 = gene2.glyph.clone();
			
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
