package dk.itu.jglyph;

import java.util.Iterator;

public class Nodes implements Iterable<Node>
{
	private final Node[] nodes;
	
	private final Edge[][] edges;
	
	public final int length;
	
	public final Node centroid;
	
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
	
	public Node getNode(int nodeIdx)
	{
		Node node = nodes[nodeIdx];
		return(node);
	}
	
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
