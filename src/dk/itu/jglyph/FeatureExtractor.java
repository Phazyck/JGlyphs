package dk.itu.jglyph;

import java.util.List;

public class FeatureExtractor 
{
  private FeatureExtractor() {}

  public static double minimumAngle(JGlyph glyph)
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }
  
  public static double avgAngle(JGlyph glyph)
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }
  
  public static double maxAngle(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }
  
  /** Minimum distance from center for stroke start/end */
  public static double minDistanceToCenter(JGlyph glyph) 
  {
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

  /** Average distance from center for stroke start/end */
  public static double avgDistanceToCenter(JGlyph glyph) 
  {
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

  /** Maximum distance from center for stroke start/end */
  public static double maxDistanceToCenter(JGlyph glyph) 
  {
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

  /** Number of edges */
  public static double edgeCount(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }

  /** Number of connected units */
  public static double unitCount(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }

  /** An estimate for how many strokes would be needed to draw this */
  public static double strokeEstimate(JGlyph glyph) 
  {
	  List<Node> ends = glyph.getEndsOfStrokes();
	  int count = ends.size();
	  
	  int estimate = count / 2;
	  
	  return(estimate);
  }

  /** Average X for all edges */
  public static double avgX(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }

  /** Average Y for all edges */
  public static double avgY(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }

  /** Average X for stroke ends */
  public static double avgStrokeEndsX(JGlyph glyph) 
  {
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

  /** Average Y for stroke ends */
  public static double avgStrokeEndsY(JGlyph glyph) 
  {
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

  /** Minimum edge length */
  public static double minEdgeLength(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }

  /** Maximum edge length */
  public static double maxEdgeLength(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }

  /** Average edge length */
  public static double avgEdgeLength(JGlyph glyph) 
  {
	  // Søren
	  // TODO - Implement this method.
	  throw new RuntimeException("Method not implemented.");
  }
}