package dk.itu.jglyph.evolution;

import java.util.TreeSet;

import dk.itu.jglyph.Evaluator;
import dk.itu.jglyph.Glyph;

public class GlyphEvolver 
{
	private final static int DEFAULT_POPULATION_SIZE = 256;
	private int populationSize = DEFAULT_POPULATION_SIZE;
	
	private final static int DEFAULT_ITERATION_LIMIT = 1024;
	private int iterationLimit = DEFAULT_ITERATION_LIMIT;
	
	private final static double DEFAULT_TARGET_FITNESS = 0.8;
	private double targetFitness = DEFAULT_TARGET_FITNESS;
	
	private final static double DEFAULT_BEST_SURVIVOR_RATE = 0.3;
	private double bestSurvivorRate = DEFAULT_BEST_SURVIVOR_RATE;
	
	private final static double DEFAULT_TOTAL_SURVIVOR_RATE = 0.5;
	private double totalSuvirvorRate = DEFAULT_TOTAL_SURVIVOR_RATE;
	
	private final static double DEFAULT_PARENT_PICK_CHANCE = 0.5;
	private double parentPickChance = DEFAULT_PARENT_PICK_CHANCE;
	
	private final static double DEFAULT_PARENT_PICK_CHANCE_INCREMENT = -0.01;
	private double parentPickChanceIncrement = DEFAULT_PARENT_PICK_CHANCE_INCREMENT;
	
	private static final double DEFAULT_MUTATION_CHANCE = 0.2;
	private double mutationChance = DEFAULT_MUTATION_CHANCE;
	
	private static final double DEFAULT_CROSSOVER_CHANCE = 0.95;
	private double crossoverChance = DEFAULT_CROSSOVER_CHANCE;
	
	private int glyphWidth;
	private int glyphHeight;
	
	private Evaluator evaluator;
	
	private TreeSet<Subject> population;

	public GlyphEvolver(int glyphWidth, int glyphHeight, Evaluator evaluator)
	{
		this.glyphWidth = glyphWidth;
		this.glyphHeight = glyphHeight;
		init(evaluator);
	}
	
	public void init(Evaluator evaluator)
	{
		this.evaluator = evaluator;
		
		population = new TreeSet<Subject>(Subject.COMPARATOR_DESCENDING);
		
		while(population.size() < populationSize)
		{
			Glyph glyph = new Glyph(glyphWidth, glyphHeight);
			glyph.randomizeEdges();
			Subject subject = makeSubject(glyph);
			population.add(subject);
		}
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
		for(int iteration = 0; iteration < iterationLimit; ++iteration)
		{
			Subject champion = getChampion();
			
			double bestFitness = champion.fitness;
			double worstFitness = population.last().fitness;
			
			System.out.printf("Generation #%d\n", iteration);
			System.out.printf("Best fitness %f\n", bestFitness);
			System.out.printf("Worst fitness %f\n", worstFitness);
			System.out.println();
			
			if(champion.fitness >= targetFitness)
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
			
			int top = (int)(length * bestSurvivorRate);
			int bottom = subjects.length - top;
			
			Random.shufflePortion(subjects, top, bottom);
			
			int survivors = (int)(length * totalSuvirvorRate);
			
			breedChildren(subjects, survivors);
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
