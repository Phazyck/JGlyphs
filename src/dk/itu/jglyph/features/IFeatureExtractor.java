package dk.itu.jglyph.features;

import dk.itu.jglyph.Glyph;

/**
 * A function interface for feature extractors.
 */
@FunctionalInterface
public interface IFeatureExtractor 
{
	/**
	 * Extracts a feature from a glyph.
	 * @param g The glyph.
	 * @return The feature.
	 */
	public double extract(Glyph g);
}