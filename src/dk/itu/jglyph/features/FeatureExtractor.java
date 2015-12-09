package dk.itu.jglyph.features;

import java.util.List;

import dk.itu.jglyph.JGlyph;
import dk.itu.jglyph.Node;

public abstract class FeatureExtractor 
{
	public abstract double getFeature(JGlyph glyph);
	
	protected String name;
	
	public FeatureExtractor(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return(name);
	}
	
	/**
	 * An extractor for getting the shortest distance from any starting/ending-point of any stroke to the center of a glyph.
	 */
	public static final FeatureExtractor MINIMUM_DISTANCE_FROM_STROKE_ENDS_TO_CENTER = 
		new FeatureExtractor("Minimum distance from stroke ends to center.") 
		{
			
			@Override
			public double getFeature(JGlyph glyph) {
				  List<Node> ends = glyph.getEndsOfStrokes();
				  
				  if(ends.isEmpty())
				  {
					  return(0);
				  }
				  
				  Node centroid = glyph.getCentroid();
				  
				  double minSq = Double.MAX_VALUE;
				  
				  for(Node end : glyph.getEndsOfStrokes())
				  {
					  double distSq = end.distanceSq(centroid);
					  if(distSq < minSq)
					  {
						  minSq = distSq;
					  }
				  }
				  
				  double min = Math.sqrt(minSq);
				  
				  return(min);
			}
		};
	
	/**
	 * An extractor for getting the farthest distance from any starting/ending-point of any stroke to the center of a glyph.
	 */
	public static final FeatureExtractor MAXIMUM_DISTANCE_FROM_STROKE_ENDS_TO_CENTER = 
		new FeatureExtractor("Maximum distance from stroke ends to center.") 
		{
			@Override
			public double getFeature(JGlyph glyph) {
				List<Node> ends = glyph.getEndsOfStrokes();
				  
				  if(ends.isEmpty())
				  {
					  return(0);
				  }
				  
				  Node centroid = glyph.getCentroid();
				  
				  double maxSq = Double.MIN_VALUE;
				  
				  for(Node end : glyph.getEndsOfStrokes())
				  {
					  double distSq = end.distanceSq(centroid);
					  if(distSq > maxSq)
					  {
						  maxSq = distSq;
					  }
				  }
				  
				  double max = Math.sqrt(maxSq);
				  
				  return(max);
			}
		};
	
	/**
	 * An extractor for getting the average of all distances from any starting/ending-point of any stroke to the center of a glyph.
	 */
	public static final FeatureExtractor AVERAGE_DISTANCE_FROM_STROKE_ENDS_TO_CENTER = 
		new FeatureExtractor("Average distance from stroke ends to center.") 
		{
			@Override
			public double getFeature(JGlyph glyph) {
				List<Node> ends = glyph.getEndsOfStrokes();
				  int count = ends.size();
				  
				  if(count == 0)
				  {
					  return(0);
				  }
				  
				  Node centroid = glyph.getCentroid();
				  
				  double sum = 0;
				  
				  for(Node end : ends)
				  {
					  sum += end.distance(centroid);
				  }
				  
				  double average = sum / count;
				  
				  return(average);
			}
		};
	
	/**
	 * An extractor for getting an estimate of how many strokes there are in this glyph.
	 */
	public static final FeatureExtractor STROKE_COUNT_ESTIMATE = 
		new FeatureExtractor("Stroke count estimate.") 
		{
			@Override
			public double getFeature(JGlyph glyph) {
				List<Node> ends = glyph.getEndsOfStrokes();
				int count = ends.size();
	
				int estimate = count / 2;
	
				return(estimate);
			}
		};
	
	/**
	 * An extractor for getting the average X-coordinate of any starting/ending-point of any stroke.
	 */
	public static final FeatureExtractor AVERAGE_X_FOR_STROKE_ENDS = 
		new FeatureExtractor("Average X-coordinate for stroke ends.") 
		{
			@Override
			public double getFeature(JGlyph glyph) {
				List<Node> ends = glyph.getEndsOfStrokes();
				int count = ends.size();
	
				Node centroid = glyph.getCentroid();
	
				if(count == 0)
				{
					return(centroid.x);
				}
	
				double sum = 0;
	
				for(Node end : ends)
				{
					sum += end.x;
				}
	
				double average = sum / count;
	
				return(average);
			}
		};
	
	/**
	 * An extractor for getting the average Y-coordinate of any starting/ending-point of any stroke.
	 */
	public static final FeatureExtractor AVERAGE_Y_FOR_STROKE_ENDS = 
		new FeatureExtractor("Average Y-coordinate for stroke ends.")
		{
			@Override
			public double getFeature(JGlyph glyph) {
				List<Node> ends = glyph.getEndsOfStrokes();
				int count = ends.size();
	
				Node centroid = glyph.getCentroid();
	
				if(count == 0)
				{
					return(centroid.y);
				}
	
				double sum = 0;
	
				for(Node end : ends)
				{
					sum += end.y;
				}
	
				double average = sum / count;
	
				return(average);
			}
		};
	
	/**
	 * Description goes here.
	 */
	public static final FeatureExtractor TEMPLATE = new FeatureExtractor("Name goes here.") 
	{
		@Override
		public double getFeature(JGlyph glyph) {
			// TODO - 
			return 0;
		}
	};
	
	/**
	 * A collections of feature extractors.
	 */
	public static final FeatureExtractor[] FEATURE_EXTRACTORS = new FeatureExtractor[] {
			MINIMUM_DISTANCE_FROM_STROKE_ENDS_TO_CENTER,
			MAXIMUM_DISTANCE_FROM_STROKE_ENDS_TO_CENTER,
			AVERAGE_DISTANCE_FROM_STROKE_ENDS_TO_CENTER,
			STROKE_COUNT_ESTIMATE,
			AVERAGE_X_FOR_STROKE_ENDS,
			AVERAGE_Y_FOR_STROKE_ENDS	
	};
}
