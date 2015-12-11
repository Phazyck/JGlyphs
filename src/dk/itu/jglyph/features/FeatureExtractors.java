package dk.itu.jglyph.features;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dk.itu.jglyph.Edge;
import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.Node;

public class FeatureExtractors 
{
	private static FeatureExtractors instance = null;
	
	private List<IFeatureExtractor> extractors;
	private List<String> descriptions;
	
	private FeatureExtractors() 
	{
		extractors = new ArrayList<>();
		descriptions = new ArrayList<>();
		
		addExtractor((Glyph g) -> minAngle(g), "Minimum angle.");
		addExtractor((Glyph g) -> avgAngle(g), "Average angle.");
		addExtractor((Glyph g) -> maxAngle(g), "Maximum angle.");
		
		addExtractor((Glyph g) -> minDistanceToCenter(g), "Minimum distance to center.");
		addExtractor((Glyph g) -> avgDistanceToCenter(g), "Average distance to center");
		addExtractor((Glyph g) -> maxDistanceToCenter(g), "Maximum distance to center.");
		
		addExtractor((Glyph g) -> minEdgeLength(g), "Minimum edge length.");
		addExtractor((Glyph g) -> avgEdgeLength(g), "Average edge length.");
		addExtractor((Glyph g) -> maxEdgeLength(g), "Maximum edge length.");
		
		addExtractor((Glyph g) -> avgStrokeEndsX(g), "Average stroke ends X.");
		addExtractor((Glyph g) -> avgStrokeEndsY(g), "Average stroke ends Y.");
		
		addExtractor((Glyph g) -> unitCount(g), "Unit count.");
		addExtractor((Glyph g) -> edgeCount(g), "Edge count.");
		
		addExtractor((Glyph g) -> strokeEstimate(g), "Stroke count estimate.");
	}
	
	private void addExtractor(IFeatureExtractor extractor, String description)
	{
		extractors.add(extractor);
		descriptions.add(description);
	}
	
	public IFeatureExtractor getExtractor(int index)
	{
		return extractors.get(index);
	}
	
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
	
	public int totalExtractors() {
		return extractors.size();
	}
	
	public String getDescription(int index)
	{
		return descriptions.get(index);
	}
	
	public int count()
	{
		return extractors.size();
	}
	
	public static FeatureExtractors getInstance()
	{
		if(instance == null)
		{
			instance = new FeatureExtractors();
		}
		
		return(instance);
	}
	
	public static double minAngle(Glyph glyph)
	  {
		  List<Double> angles = getAngles(glyph);
		  if (angles.size() == 0) return Math.PI;
		  double min = Double.MAX_VALUE;
		  for (Double d : angles) {
			  if (d < min && d != 0) min = d; // TODO do we need to use epsilon for the last comparison?
		  }
		  return min;
	  }
	  
	  public static double avgAngle(Glyph glyph)
	  {
		  List<Double> angles = getAngles(glyph);
		  if (angles.size() == 0) return Math.PI;
		  double sum = 0;
//		  System.out.println(angles);
		  for (Double d : angles) {
			  sum += d;
		  }
		  return sum / angles.size();
	  }
	  
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
	  
	  private static List<Double> getAngles(Glyph g) {
		  List<Double> result = new ArrayList<Double>();
		  for (Node node : g.getNodes()) {
			  // TODO x/y cannot be double with our current creation scheme
			  int id = g.getNodeId((int)node.x, (int)node.y);
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

	/** Minimum distance from center for stroke start/end */
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

	  /** Average distance from center for stroke start/end */
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

	  /** Maximum distance from center for stroke start/end */
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

	  /** Number of edges */
	  public static double edgeCount(Glyph glyph) 
	  {
		  int count = glyph.getEdges().size();
		  
		  return(count);
	  }

	  /** Number of connected units */
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

	  /** An estimate for how many strokes would be needed to draw this */
	  public static double strokeEstimate(Glyph glyph) 
	  {
		  List<Node> ends = glyph.getEndsOfStrokes();
		  int count = ends.size();
		  
		  int estimate = count / 2;
		  
		  return(estimate);
	  }

	  /** Average X for all edges */
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

	  /** Average Y for all edges */
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

	  /** Average X for stroke ends */
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

	  /** Average Y for stroke ends */
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

	  /** Minimum edge length */
	  public static double minEdgeLength(Glyph glyph) 
	  {
		  double minLength = Double.MAX_VALUE;
		  for (Edge edge : glyph.getEdges()) {
			  double length = length(edge);
			  if (length < minLength) minLength = length;
		  }
		  return minLength;
	  }

	  /** Maximum edge length */
	  public static double maxEdgeLength(Glyph glyph) 
	  {
		  double maxLength = Double.MIN_VALUE;
		  for (Edge edge : glyph.getEdges()) {
			  double length = length(edge);
			  if (length > maxLength) maxLength = length;
		  }
		  return maxLength;
	  }

	  /** Average edge length */
	  public static double avgEdgeLength(Glyph glyph) 
	  {
		  double sum = 0;
		  int count = 0;
		  for (Edge edge : glyph.getEdges()) {
			  sum += length(edge);
			  count++;
		  }
		  return sum/count;
	  }
	  
	  private static double length(Edge edge) {
		  double dx = edge.from.x-edge.to.x;
		  double dy = edge.from.y-edge.to.y;
		  return Math.sqrt(dx*dx+dy*dy);
	  }
}
