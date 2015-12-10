package dk.itu.jglyph.features;

import dk.itu.jglyph.JGlyph;

@FunctionalInterface
public interface IFeatureExtractor 
{
	public double extract(JGlyph g);
}