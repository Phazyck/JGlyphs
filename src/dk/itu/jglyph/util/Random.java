package dk.itu.jglyph.util;

import dk.itu.jglyph.evolution.Subject;

/**
 * A utility class for doing operations that facilitate randomness.
 */
public class Random 
{
	/**
	 * The Random Number Generator.
	 */
	private final static java.util.Random RNG = new java.util.Random();

	/**
	 * Shuffles a portion of an array.
	 * 
	 * @param array The array.
	 * @param offset An offset into the portion that should be shuffled.
	 * @param length The amount of elements that should be shuffled.
	 */
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
	
	/**
	 * Performs a biased coinflip.
	 * 
	 * @param probability The probability of getting false.
	 * @return The result of the coinflip.
	 */
	public static boolean getBoolean(double probability)
	{
		boolean result = RNG.nextDouble() < probability;
		return(result);
	}

	/**
	 * Picks a random index into a portion of an array.
	 * 
	 * @param offset An offset into the array.
	 * @param length The size of the portion.
	 * @return The random index.
	 */
	private static int pickIndex(int offset, int length)
	{
		int index = offset + RNG.nextInt(length);
		return(index);
	}
	
	/**
	 * Picks a random element from a portion of an array.
	 * @param items The array.
	 * @param offset An offset into the array.
	 * @param length The size of the portion.
	 * @return The randomly picked element.
	 */
	public static <T> T pickRandom(T[] items, int offset, int length)
	{
		while(true)
		{
			int index = pickIndex(offset, length);
			T item = items[index];

			return(item);
			
		}
	}
	
	/**
	 * Picks a random element from a portion of an array, while ignoring a given element.
	 * 
	 * @param items The array.
	 * @param offset An offset into the array.
	 * @param length The size of the portion.
	 * @param ignore The element to ignore.
	 * @return The randomly picked element.
	 */
	public static <T> T pickRandom(T[] items, int offset, int length, T ignore)
	{
		while(true)
		{
			int index = pickIndex(offset, length);
			T item = items[index];
			if((ignore == null) || (item != ignore)) 
			{
				return(item);
			}
		}
	}
	
	/**
	 * Gets a random integer between 0 (include) and max (exclusive).
	 * @param max The max value.
	 * @return The random integer.
	 */
	public static int getInt(int max)
	{
		int result = RNG.nextInt(max);
		return(result);
	}
}
