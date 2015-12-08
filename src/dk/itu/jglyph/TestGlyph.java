package dk.itu.jglyph;

import java.util.ArrayList;

public class TestGlyph implements Graph
{
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	public TestGlyph()
	{
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		
		for(int y = 0; y < 3; ++y)
		{
			for(int x = 0; x < 3; ++x)
			{
				nodes.add(new Node(x,y));
			}
		}
		
		Node[] ns = new Node[] {
			nodes.get(6),
			nodes.get(0),
			nodes.get(4),
			nodes.get(2),
			nodes.get(8)	
		};
		
		for(int i = 0; i + 1 < ns.length; ++i)
		{
			Node a = ns[i];
			Node b = ns[i+1];
			Edge e = new Edge(a, b);
			edges.add(e);
		}
	}

	@Override
	public Iterable<Edge> getEdges(int nodeIndex) {
		throw new RuntimeException("Method not supported.");
	}

	@Override
	public Iterable<Edge> getEdges() {
		return(edges);
	}

	@Override
	public Iterable<Node> getNodes() {
		return(nodes);
	}

	@Override
	public int getNodeId(int x, int y) {
		throw new RuntimeException("Method not supported.");
	}

	@Override
	public void makeEdge(int nodeIdA, int nodeIdB) {
		throw new RuntimeException("Method not supported.");
	}

	@Override
	public void removeEdge(int nodeIdA, int nodeIdB) {
		throw new RuntimeException("Method not supported.");
	}
}
