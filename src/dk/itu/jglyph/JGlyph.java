package dk.itu.jglyph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JGlyph implements Graph<Node>
{
	private final static Random RNG = new Random();
	
	private Nodes nodes;
	private AdjacencyMatrix adjMatrix;
	private int width;

	/** Constructs a Glyph with 'width * height' nodes. */
	public JGlyph(int width, int height) 
	{
		this.width = width;
		int size = width * height;
		
		Node[] nodeArr = new Node[size];
		for (int idx = 0; idx < size; ++idx) 
		{
			nodeArr[idx] = new Node(idx % width, idx / width);
		}
		
		nodes = new Nodes(nodeArr);
		
		adjMatrix = new AdjacencyMatrix(size);
	}

	@Override
	public List<Edge> getEdges(int nodeIdx) {
		List<Edge> edges = new ArrayList<Edge>();
		for (int idx = 0; idx < adjMatrix.size; ++idx) 
		{
			if(!adjMatrix.getValue(nodeIdx, idx))
			{
				continue;
			}
			
			Edge edge = nodes.getEdge(nodeIdx, idx);
			edges.add(edge);
		}
		return edges;
	}

	@Override
	public Iterable<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		
		int count = nodes.length;
		
		for (int idxA = 0; idxA < count; idxA++) 
		{
			for (int idxB = idxA; idxB < count; idxB++) 
			{
				if(!adjMatrix.getValue(idxA, idxB))
				{
					continue;
				}
				
				Edge edge = nodes.getEdge(idxA, idxB);
				edges.add(edge);
			}
		}
		return edges;
	}

	@Override
	public Iterable<Node> getNodes()
	{
		return(nodes);
	}

	@Override
	public int getNodeId(int x, int y) 
	{
		return x+width*y;
	}

	@Override
	public void makeEdge(int nodeIdxA, int nodeIdxB) 
	{
		// TODO: Code for making sure parallel overlapping edges get combined could go here
		adjMatrix.setValue(nodeIdxA, nodeIdxB, true);
	}

	@Override
	public void removeEdge(int nodeIdxA, int nodeIdxB) 
	{
		adjMatrix.setValue(nodeIdxA, nodeIdxB, false);
	}
	
	public void randomizeEdges()
	{
		for (int idxA = 0; idxA < nodes.length; ++idxA) 
		{
			for (int idxB = idxA; idxB < nodes.length; ++idxB) 
			{
				boolean value = RNG.nextInt() % 2 == 0;
				adjMatrix.setValue(idxA, idxB, value); 
			}
		}
	}
	
	public void mutate()
	{
		int length = nodes.length;
		int nodeIdxA = RNG.nextInt(length);
		int nodeIdxB = RNG.nextInt(length);
		boolean value = adjMatrix.getValue(nodeIdxA, nodeIdxB);
		adjMatrix.setValue(nodeIdxA, nodeIdxB, !value);
	}
	
	public int getDegree(int nodeIdx)
	{
		int degree = 0;
		for(int idx = 0; idx < nodes.length; ++idx)
		{
			boolean adjacent = adjMatrix.getValue(nodeIdx, idx);
			if(adjacent)
			{
				++degree;
			}
		}
		
		return(degree);
	}
	
	public boolean isEndOfStroke(int nodeIdx)
	{
		int degree = getDegree(nodeIdx);
		boolean result = degree % 2 != 0;
		return(result);
	}
	
	public List<Node> getEndsOfStrokes()
	{
		List<Node> ends = new ArrayList<Node>();
		
		for(int idx = 0; idx < nodes.length; ++idx)
		{
			if(!isEndOfStroke(idx))
			{
				continue;
			}
			
			Node end = nodes.getNode(idx);
			ends.add(end);
		}
		
		return(ends);
	}
	
	public Node getCentroid()
	{
		return nodes.centroid;
	}
}
