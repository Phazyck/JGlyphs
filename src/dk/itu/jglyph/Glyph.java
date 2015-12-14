package dk.itu.jglyph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Glyph
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adjMatrix == null) ? 0 : adjMatrix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Glyph other = (Glyph) obj;
		if (adjMatrix == null) {
			if (other.adjMatrix != null)
				return false;
		} else if (!adjMatrix.equals(other.adjMatrix))
			return false;
		return true;
	}

	private final static Random RNG = new Random();
	
	private Nodes nodes;
	private AdjacencyMatrix adjMatrix;
	private int width;

	/** Constructs a Glyph with 'width * height' nodes. */
	public Glyph(int width, int height) 
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
	
	private Glyph(Nodes nodes, AdjacencyMatrix adjMatrix, int width)
	{
		this.nodes = nodes;
		this.adjMatrix = adjMatrix;
		this.width = width;
	}

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

	public List<Edge> getEdges() {
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

	public Iterable<Node> getNodes()
	{
		return(nodes);
	}

	public int getNodeId(int x, int y) 
	{
		return x+width*y;
	}

	public void makeEdge(int nodeIdxA, int nodeIdxB) 
	{
		// TODO: Code for making sure parallel overlapping edges get combined could go here
		adjMatrix.setValue(nodeIdxA, nodeIdxB, true);
	}

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
				adjMatrix.setValue(idxA, idxB, RNG.nextBoolean()); 
			}
		}
	}
	
	public void mutate()
	{
		int length = nodes.length;
		int nodeIdxA = RNG.nextInt(length);
		int nodeIdxB = RNG.nextInt(length);
		
		boolean remove = RNG.nextBoolean();
		boolean value = !remove;
		
		for(int i = 0; i < 100; ++i)
		{
			 nodeIdxA = RNG.nextInt(length);
			 nodeIdxB = RNG.nextInt(length);
			 value = adjMatrix.getValue(nodeIdxA, nodeIdxB);
			 if(value == remove)
			 {
				 break;
			 }
		}	
		
		adjMatrix.setValue(nodeIdxA, nodeIdxB, !value);
	}
	
	public void crossWith(Glyph that)
	{
		AdjacencyMatrix thisMatrix = this.adjMatrix;
		AdjacencyMatrix thatMatrix = that.adjMatrix;
		
		for(int i = 0; i < nodes.length; ++i)
		{
			for(int j = i; j < nodes.length; ++j)
			{
				if(RNG.nextBoolean())
				{
					continue;
				}
				
				boolean thisBool = thisMatrix.getValue(i, j);
				boolean thatBool = thatMatrix.getValue(i, j);
				thisMatrix.setValue(i, j, thatBool);
				thatMatrix.setValue(i, j, thisBool);
			}
		}
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
	
	public Glyph clone()
	{
		Nodes cloneNodes = nodes;
		AdjacencyMatrix cloneMatrix = adjMatrix.clone();
		int cloneWidth = width;
		
		Glyph clone = new Glyph(cloneNodes, cloneMatrix, cloneWidth);
		
		return(clone);
	}
}
