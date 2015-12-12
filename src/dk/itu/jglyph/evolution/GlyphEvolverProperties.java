package dk.itu.jglyph.evolution;

import java.io.IOException;

import com.anji.util.Properties;

public class GlyphEvolverProperties 
{
	private Properties properties;
	
	public GlyphEvolverProperties(String resource)
	{
		try {
			properties = new Properties(resource);
		} catch (IOException e) {
			properties = null;
		}
	}
	
	private String fixKey(String key)
	{
		String result = "glyph.evolver." + key;
		return(result);
	}
	
	private boolean keyExists(String key)
	{
		boolean exists = properties != null && properties.containsKey(key);
		
		return(exists);
	}
	
	public int tryGetInt(String key, int fallback)
	{
		if(properties == null)
		{
			// TODO wtf just happened here? - Kas
		}
		
		key = fixKey(key);
		int result = fallback;
		if(keyExists(key))
		{
			result = properties.getIntProperty(key);
		}
		return(result);
	}
	
	public double tryGetDouble(String key, double fallback)
	{
		key = fixKey(key);
		double result = fallback;
		if(keyExists(key))
		{
			result = properties.getDoubleProperty(key);
		}
		return(result);
	}

}
