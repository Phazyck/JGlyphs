package dk.itu.jglyph.features;

import dk.itu.jglyph.Glyph;

@FunctionalInterface
public interface IFeatureExtractor 
{
	public double extract(Glyph g);
}