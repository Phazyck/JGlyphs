package dk.itu.jglyph;

/**
 * A graph node with a position in 2-dimension space.
 * 
 * Used for glyphs.
 */
public class Node
{
	/**
	 * The x-coordinate of the node.
	 */
	public final double x;
	
	/**
	 * The y-coordinate of the node.
	 */
	public final double y;
  
	/**
	 * Constructs a node with the given x and y coordinates.
	 * 
	 * @param x The x-coordinate of the node.
	 * @param y The y-coordinate of the node.
	 */
	public Node(double x, double y) 
	{
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates the squared distance between this node and another.
	 * 
	 * @param that The other node.
	 * @return The squared distance.
	 */
	public double distanceSq(Node that)
	{
		double result = Node.distanceSq(this, that);
		
		return(result);
	}
	
	/**
	 * Calculates the distance between this node and another.
	 * 
	 * @param that The other node.
	 * @return The distance.
	 */
	public double distance(Node that)
	{
		double result = Node.distance(this, that);
		
		return(result);
	}
	
	/**
	 * Calculates the squared distance between one node and another.
	 * 
	 * @param a The first node.
	 * @param b The second node.
	 * @return The squared distance.
	 */
	public static double distanceSq(Node a, Node b)
	{
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		
		double result = dx * dx + dy * dy;
		
		return(result);
	}
	
	/**
	 * Calculates the distance between one node and another.
	 * 
	 * @param a The first node.
	 * @param b The second node.
	 * @return The distance.
	 */
	public static double distance(Node a, Node b)
	{
		double distanceSq = distanceSq(a, b);
		
		double result = Math.sqrt(distanceSq);
		
		return(result);
	}
}