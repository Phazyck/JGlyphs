package dk.itu.jglyph.util;

import dk.itu.jglyph.evolution.Subject;

public class Random 
{
	private final static java.util.Random RNG = new java.util.Random();

	public  static void shufflePortion(Subject[] array, int offset, int length)
	{
		for(int idx = length - 1; idx > 0; --idx)
		{
			  
			int indexA = offset + (RNG.nextInt(idx  + 1));
			int indexB = offset + idx;
			
			Subject tmp = array[indexA];
			array[indexA] = array[indexB];
			array[indexB] = tmp; 
		}
	}
	
	public static boolean getBoolean(double probability)
	{
		boolean result = RNG.nextDouble() < probability;
		return(result);
	}
	
	public static <T> T pickChanceDescending(T[] items, int offset, int length, double initialChance, double chanceIncrement)
	{
		double chance = initialChance;
		
		T result = null;
		
		for(int i = offset; i < offset + length; ++i)
		{
			T item = items[i];
			
			result = item;
			if(RNG.nextDouble() < chance)
			{
				return(result);
			}
			chance += chanceIncrement;
		}
		
		return(result);
	}
	
	public static <T> int pickIndex(T[] items, int offset, int length)
	{
		int index = offset + RNG.nextInt(length);
		return(index);
	}
	
	public static <T> T pickRandom(T[] items, int offset, int length, T ignore)
	{
		while(true)
		{
			int index = pickIndex(items, offset, length);
			T item = items[index];
			if((ignore == null) || (item != ignore)) 
			{
				return(item);
			}
		}
	}
	
	public static int getInt(int max)
	{
		int result = RNG.nextInt(max);
		return(result);
	}
}
