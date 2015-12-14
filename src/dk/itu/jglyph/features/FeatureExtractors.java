package dk.itu.jglyph.features;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dk.itu.jglyph.Edge;
import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.Node;

/**
 * A utility class with methods and functions for extracting features of a glyph.
 */
public class FeatureExtractors 
{
	/**
	 * The singleton instance.
	 */
	private static FeatureExtractors instance = null;
	
	/**
	 * The list of available feature extractor functions.
	 */
	private List<IFeatureExtractor> extractors;
	
	/**
	 * A list of descriptions of the available  feature extractors.
	 */
	private List<String> descriptions;
	
	/**
	 * Private singleton constructor.
	 */
	private FeatureExtractors() 
	{
		extractors = new ArrayList<>();
		descriptions = new ArrayList<>();
		
//		addExtractor((Glyph g) -> minAngle(g), "Minimum angle.");
//		addExtractor((Glyph g) -> avgAngle(g), "Average angle.");
//		addExtractor((Glyph g) -> maxAngle(g), "Maximum angle.");
		
//		addExtractor((Glyph g) -> minDistanceToCenter(g), "Minimum distance to center.");
//		addExtractor((Glyph g) -> avgDistanceToCenter(g), "Average distance to center");
//		addExtractor((Glyph g) -> maxDistanceToCenter(g), "Maximum distance to center.");
//		
//		addExtractor((Glyph g) -> minEdgeLength(g), "Minimum edge length.");
//		addExtractor((Glyph g) -> avgEdgeLength(g), "Average edge length.");
//		addExtractor((Glyph g) -> maxEdgeLength(g), "Maximum edge length.");
		
//		addExtractor((Glyph g) -> avgStrokeEndsX(g), "Average stroke ends X.");
//		addExtractor((Glyph g) -> avgStrokeEndsY(g), "Average stroke ends Y.");
		
//		addExtractor((Glyph g) -> unitCount(g), "Unit count.");
		addExtractor((Glyph g) -> edgeCount(g), "Edge count.");

//		addExtractor((Glyph g) -> strokeEstimate(g), "Stroke count estimate.");
		
//		addExtractor((Glyph g) -> dotCount(g), "Dot count.");
	}
	
	/**
	 * Adds a feature extractor to the list of available feature extractors.
	 * 
	 * @param extractor The feature extractor.
	 * @param description A description of the feature extractor.
	 */
	private void addExtractor(IFeatureExtractor extractor, String description)
	{
		extractors.add(extractor);
		descriptions.add(description);
	}
	
	/**
	 * Gets a feature extractor.
	 * @param index The index of the feature extractor.
	 * @return The feature extractor.
	 */
	public IFeatureExtractor getExtractor(int index)
	{
		return extractors.get(index);
	}
	
	/**
	 * Extracts all features of a glyph, using the currently available feature extractors.
	 * 
	 * @param glyph The glyph.
	 * @return The features.
	 */
	public double[] extractFeatures(Glyph glyph)
	{
		int length = extractors.size();
		
		double[] features = new double[length];
		
		for(int i = 0; i < length; ++i)
		{
			IFeatureExtractor extractor = extractors.get(i);
			features[i] = extractor.extract(glyph);
		}
		
		return(features);
	}
	
	/**
	 * A count of the amount of available feature extractors.
	 * @return The total amount of available feature extractors. 
	 */
	public int totalExtractors() {
		return extractors.size();
	}
	
	/**
	 * Gets the description of a feature extractor.
	 * @param index The index of the feature extractor.
	 * @return The description of the feature extractor.
	 */
	public String getDescription(int index)
	{
		return descriptions.get(index);
	}
	
	/**
	 * Gets the singleton instance of the feature extractors class.
	 * 
	 * @return The singleton instance.
	 */
	public static FeatureExtractors getInstance()
	{
		if(instance == null)
		{
			instance = new FeatureExtractors();
		}
		
		return(instance);
	}
	
	/**
	 * Calculates the smallest angle found in the glyph.
	 * @param glyph The glyph.
	 * @return The smallest angle found in the glyph.
	 */
	public static double minAngle(Glyph glyph)
	  {
		  List<Double> angles = getAngles(glyph);
		  if (angles.size() == 0) return Math.PI; // Why return non-zero if there's no angles? - Kas
		  double min = Double.MAX_VALUE;
		  for (Double d : angles) {
			  if (d < min && d != 0) min = d; // TODO do we need to use epsilon for the last comparison?
		  }
		  return min;
	  }
	  
	/**
	 * Calculates the average of all angles found in the glyph.
	 * @param glyph The glyph.
	 * @return The average of all angles found in the glyph.
	 */
	  public static double avgAngle(Glyph glyph)
	  {
		  List<Double> angles = getAngles(glyph);
		  if (angles.size() == 0) return Math.PI;
		  double sum = 0;
		  for (Double d : angles) {
			  sum += d;
		  }
		  return sum / angles.size();
	  }
	  
		/**
		 * Calculates the largest angle found in the glyph.
		 * @param glyph The glyph.
		 * @return The largest angle found in the glyph.
		 */
	  public static double maxAngle(Glyph glyph) 
	  {
		  List<Double> angles = getAngles(glyph);
		  if (angles.size() == 0) return Math.PI;
		  double max = Double.MIN_VALUE;
		  for (Double d : angles) {
			  if (d > max) max = d;
		  }
		  return max;
	  }
	  
	  /**
	   * Finds all angles in a glyph.
	   * @param g The glyph.
	   * @return The angles.
	   */
	  private static List<Double> getAngles(Glyph g) {
		  List<Double> result = new ArrayList<Double>();
		  for (Node node : g.getNodes()) {
			  // TODO x/y cannot be double with our current creation scheme
			  int id = g.getNodeIdx((int)node.x, (int)node.y);
			  List<Edge> edges = g.getEdges(id);
			  for (int i = 0; i < edges.size(); i++) {
				  for (int j = i+1; j < edges.size(); j++) {
					  Edge e1 = edges.get(i);
					  Edge e2 = edges.get(j);
					  Node n1 = node.equals(e1.from) ? e1.to : e1.from;
					  Node n2 = node.equals(e2.from) ? e2.to : e2.from;
					  
					  //Skip dots
					  if (node.equals(n1) || node.equals(n2)) continue;
					  
					  // TODO 0/180 could be sorted our here but we might need that for other metrics
					  
					  result.add(getAngle(node, n1, n2));
				  }
			  }
		  }
		  return result;
	  }
	  
	  /**
	   * Calculates the angle between three nodes.
	   * @param center The center of the angle.
	   * @param n1 The first leg of the angle.
	   * @param n2 The second leg of the angle.
	   * @return The angle.
	   */
	  private static Double getAngle(Node center, Node n1, Node n2) {
		  double x1 = n1.x - center.x;
		  double x2 = n2.x - center.x;
		  double y1 = n1.y - center.y;
		  double y2 = n2.y - center.y;
		  
		  double dot = x1*x2 + y1*y2;
		  double l1 = Math.sqrt(x1*x1 + y1*y1);
		  double l2 = Math.sqrt(x2*x2 + y2*y2);
		  
		  return Math.acos(dot/(l1*l2));
	  }

	  /**
	   * Calculates the minimum distance from center for stroke start/ends.
	   * 
	   * @param glyph The glyph.
	   * @return The minimum distance from center for stroke start/ends.
	   */
	  public static double minDistanceToCenter(Glyph glyph) 
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

	  /**
	   * Calculates the average of all distances from center for stroke start/ends.
	   * 
	   * @param glyph The glyph.
	   * @return The average of all distances from center for stroke start/ends.
	   */
	  public static double avgDistanceToCenter(Glyph glyph) 
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

	  /**
	   * Calculates the maximum distance from center for stroke start/ends.
	   * 
	   * @param glyph The glyph.
	   * @return The maximum distance from center for stroke start/ends.
	   */
	  public static double maxDistanceToCenter(Glyph glyph) 
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

	  /**
	   * Calculates the amount of edges in a glyph.
	   * @param glyph The glyph.
	   * @return The amount of edges.
	   */
	  public static double edgeCount(Glyph glyph) 
	  {
		  int count = glyph.getEdges().size();
		  
		  return(count);
	  }

	  /**
	   * Calculates the amount of connected units in a glyph.
	   * @param glyph The glyph.
	   * @return The amount of connected units in a glyph.
	   */
	  public static double unitCount(Glyph glyph) 
	  {
		  Iterable<Edge> edges = glyph.getEdges();
		  // List of sets found
		  List<List<Node>> sets = new ArrayList<List<Node>>();
		  for (Edge edge : edges) {
			  // Set of nodes that are connected to this edge
			  List<Node> currentSet = new ArrayList<Node>();
			  currentSet.add(edge.from);
			  currentSet.add(edge.to);
			  if (sets.size() > 0) {
				  for (Iterator<List<Node>> iterator = sets.iterator(); iterator.hasNext();) {
					  List<Node> set = iterator.next();
					  if (set.contains(edge.from) || set.contains(edge.to)) {
						  currentSet.addAll(set);
						  iterator.remove();
					  }
					  // TODO test if the edge crosses the set
				  }
			  }
			  sets.add(currentSet);
		  }
		  return sets.size();
	  }

	  /**
	   * Calculates an estimate for how many strokes would be needed to draw this glyph.
	   * @param glyph The glyph.
	   * @return The estimate for how many strokes would be needed to draw this glyph.
	   */
	  public static double strokeEstimate(Glyph glyph) 
	  {
		  List<Node> ends = glyph.getEndsOfStrokes();
		  int count = ends.size();
		  
		  int estimate = count / 2;
		  
		  return(estimate);
	  }

	  /**
	   * Calculates the average X-coordinate for all edges in the glyph.
	   * @param glyph The glyph.
	   * @return The average X-coordinate for all edges in the glyph.
	   */
	  public static double avgX(Glyph glyph) 
	  {
		  double sum = 0;
		  int count = 0;
		  for (Edge edge : glyph.getEdges()) {
			  sum += edge.from.x + edge.to.x;
			  count++;
		  }
		  return sum/(count*2);
	  }

	  /**
	   * Calculates the average Y-coordinate for all edges in the glyph.
	   * @param glyph The glyph.
	   * @return The average Y-coordinate for all edges in the glyph.
	   */
	  public static double avgY(Glyph glyph) 
	  {
		  double sum = 0;
		  int count = 0;
		  for (Edge edge : glyph.getEdges()) {
			  sum += edge.from.y + edge.to.y;
			  count++;
		  }
		  return sum/(count*2);
	  }

	  /**
	   * Calculates the average X-coordinate for all stroke ends in the glyph.
	   * @param glyph The glyph.
	   * @return The average X-coordinate for all stroke ends in the glyph.
	   */
	  public static double avgStrokeEndsX(Glyph glyph) 
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

	  /**
	   * Calculates the average Y-coordinate for all stroke ends in the glyph.
	   * @param glyph The glyph.
	   * @return The average Y-coordinate for all stroke ends in the glyph.
	   */
	  public static double avgStrokeEndsY(Glyph glyph) 
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

	  /**
	   * Calculates the smallest length of all edges in the glyph.
	   * @param glyph The glyph.
	   * @return The smallest length of all edges in the glyph.
	   */
	  public static double minEdgeLength(Glyph glyph) 
	  {
		  double minLength = Double.MAX_VALUE;
		  for (Edge edge : glyph.getEdges()) {
			  double length = length(edge);
			  if (length < minLength) minLength = length;
		  }
		  return minLength;
	  }

	  /**
	   * Calculates the largest length of all edges in the glyph.
	   * @param glyph The glyph.
	   * @return The largest length of all edges in the glyph.
	   */
	  public static double maxEdgeLength(Glyph glyph) 
	  {
		  double maxLength = Double.MIN_VALUE;
		  for (Edge edge : glyph.getEdges()) {
			  double length = length(edge);
			  if (length > maxLength) maxLength = length;
		  }
		  return maxLength;
	  }

	  /**
	   * Calculates the average of the lengths of all edges in the glyph.
	   * @param glyph The glyph.
	   * @return The average of the lengths of all edges in the glyph.
	   */
	  public static double avgEdgeLength(Glyph glyph) 
	  {
		  double sum = 0;
		  int count = 0;
		  for (Edge edge : glyph.getEdges()) {
			  sum += length(edge);
			  count++;
		  }
		  
		  if (count == 0) {
			return 0.0;
		}
		  
		  return sum/count;
	  }
	  
	  /**
	   * Calculates the amount of "dots" in the glyph.
	   * 
	   * @param glyph The glyph.
	   * @return The amount of dots in the glyph.
	   */
	  public static double dotCount(Glyph glyph) 
	  {
		  int sum = 0;
		  for (Edge edge : glyph.getEdges()) {
			if (edge.from.equals(edge.to)) {
				sum++;
			}
		  }
		  return sum;
	  }
	  
	  /**
	   * Calculates the length of an edge.
	   * 
	   * @param edge The edge.
	   * @return The length of the edge.
	   */
	  private static double length(Edge edge) {
		  double dx = edge.from.x-edge.to.x;
		  double dy = edge.from.y-edge.to.y;
		  return Math.sqrt(dx*dx+dy*dy);
	  }
}
