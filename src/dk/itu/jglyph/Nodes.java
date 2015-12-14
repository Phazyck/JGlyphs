package dk.itu.jglyph;

import java.util.Iterator;

/**
 * A collection of nodes.
 * 
 * Also provides cached meta-data about said nodes.
 */
public class Nodes implements Iterable<Node>
{
	/**
	 * The nodes.
	 */
	private final Node[] nodes;
	
	/**
	 * The possible edges between the nodes.
	 */
	private final Edge[][] edges;
	
	/**
	 * The amount of nodes.
	 */
	public final int length;
	
	/**
	 * The centroid node.
	 */
	public final Node centroid;
	
	/**
	 * Constructs a Nodes objects from an array of nodes.
	 * 
	 * @param nodes The nodes.
	 */
	public Nodes(Node[] nodes)
	{
		this.nodes = nodes;
		this.length = nodes.length;
		
		double sumX = 0;
		double sumY = 0;
		
		for(Node node : nodes)
		{
			sumX += node.x;
			sumY += node.y;
		}
		
		double avgX = sumX / length;
		double avgY = sumY / length;
		
		centroid = new Node(avgX, avgY);
		
		edges = new Edge[length][length];
	}
	
	/**
	 * Gets a node from the nodes.
	 * 
	 * @param nodeIdx The index of the node.
	 * @return The node.
	 */
	public Node getNode(int nodeIdx)
	{
		Node node = nodes[nodeIdx];
		return(node);
	}
	
	/**
	 * Gets an edge from one node to another.
	 * 
	 * @param nodeIdxA The index of the first node.
	 * @param nodeIdxB The index of the second node.
	 * @return The edge.
	 */
	public Edge getEdge(int nodeIdxA, int nodeIdxB)
	{
		Edge edge = edges[nodeIdxA][nodeIdxB];
		
		if(edge == null)
		{
			Node a = getNode(nodeIdxA);
			Node b = getNode(nodeIdxB);
			
			edge = edges[nodeIdxA][nodeIdxB] = new Edge(a, b);
		}
		
		return(edge);
	}

	@Override
	public Iterator<Node> iterator() {
		return new Iterator<Node>() {
			
			private int idx = 0;

			@Override
			public boolean hasNext() {
				return idx < length;
			}

			@Override
			public Node next() {
				
				return nodes[idx++];
			}
		};
	}
}
