package dk.itu.jglyph.evolution;

import java.io.IOException;

import com.anji.util.Properties;

/**
 * A utility class for loading properties of the glyph evolver.
 */
public class GlyphEvolverProperties 
{
	/**
	 * The properties object of loading the properties.
	 */
	private Properties properties;
	
	/**
	 * Constructs a new properties object for a given resource.
	 * 
	 * @param resource The resource.
	 */
	public GlyphEvolverProperties(String resource)
	{
		try {
			properties = new Properties(resource);
		} catch (IOException e) {
			properties = null;
		}
	}
	
	/**
	 * Adds a required prefix to a key string.
	 * 
	 * @param key The key.
	 * @return The prefixed key.
	 */
	private String prefixKey(String key)
	{
		String result = "glyph.evolver." + key;
		return(result);
	}
	
	/**
	 * Finds out whether or not a key is present in the properties.
	 * 
	 * @param key The key.
	 * @return true if the key exists, false if not.
	 */
	private boolean keyExists(String key)
	{
		boolean exists = properties != null && properties.containsKey(key);
		
		return(exists);
	}
	
	/**
	 * Tries to get an integer value from the properties with a given key.
	 * If not possible, the fall-back value is returned.
	 * 
	 * @param key The key.
	 * @param fallback The fall-back value.
	 * @return The integer.
	 */
	public int tryGetInt(String key, int fallback)
	{
		if(properties == null)
		{
			// TODO wtf just happened here? - Kas
		}
		
		key = prefixKey(key);
		int result = fallback;
		if(keyExists(key))
		{
			result = properties.getIntProperty(key);
		}
		return(result);
	}
	
	/**
	 * Tries to get a double value from the properties with a given key.
	 * If not possible, the fall-back value is returned.
	 * 
	 * @param key The key.
	 * @param fallback The fall-back value.
	 * @return The double.
	 */
	public double tryGetDouble(String key, double fallback)
	{
		key = prefixKey(key);
		double result = fallback;
		if(keyExists(key))
		{
			result = properties.getDoubleProperty(key);
		}
		return(result);
	}

}
