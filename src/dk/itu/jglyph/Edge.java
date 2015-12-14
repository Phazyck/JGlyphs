package dk.itu.jglyph;

/**
 * An edge from one node to another.
 */
public class Edge 
{
	/**
	 * The node the edge goes from.
	 */
	public final Node from;
	
	/**
	 * The node the edge goes to.
	 */
	public final Node to;
	
	/**
	 * Constructs an edge.
	 * 
	 * @param from The node the edge goes from.
	 * @param to The node the edge goes to.
	 */
	public Edge(Node from, Node to) {
		super();
		this.from = from;
		this.to = to;
	}
}
