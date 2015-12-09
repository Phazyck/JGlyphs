package dk.itu.jglyph;

public class Node
{
	public final double x;
	public final double y;
  
	public Node(double x, double y) 
	{
		super();
		this.x = x;
		this.y = y;
	}
	
	public double distanceSq(Node that)
	{
		double result = Node.distanceSq(this, that);
		
		return(result);
	}
	
	public double distance(Node that)
	{
		double result = Node.distance(this, that);
		
		return(result);
	}
	
	public static double distanceSq(Node a, Node b)
	{
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		
		double result = dx * dx + dy * dy;
		
		return(result);
	}
	
	public static double distance(Node a, Node b)
	{
		double distanceSq = distanceSq(a, b);
		
		double result = Math.sqrt(distanceSq);
		
		return(result);
	}
}