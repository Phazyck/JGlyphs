package dk.itu.jglyph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents the glyphs of a possible alphabet.
 */
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

	/**
	 * The internal Random Number Generator.
	 */
	private final static Random RNG = new Random();
	
	/**
	 * The nodes of the Glyph.
	 */
	private Nodes nodes;
	
	/**
	 * The adjacency matrix.
	 */
	private AdjacencyMatrix adjMatrix;
	
	/**
	 * The width of the glyph in node-units.
	 */
	private int width;

	/**
	 * Constructs a Glyph with 'width * height' nodes.
	 * 
	 * @param width The width.
	 * @param height The height.
	 */
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
	
	/**
	 * Constructs a Glyph with the given nodes, adjacency matrix and width.
	 * @param nodes The nodes.
	 * @param adjMatrix The adjacency matrix.
	 * @param width The width.
	 */
	private Glyph(Nodes nodes, AdjacencyMatrix adjMatrix, int width)
	{
		this.nodes = nodes;
		this.adjMatrix = adjMatrix;
		this.width = width;
	}

	/**
	 * Gets all the current edges going from a given node.
	 *  
	 * @param nodeIdx The index of the node.
	 * @return The edges going from the node.
	 */
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

	/**
	 * Gets all edges going from any node to any node.
	 * 
	 * @return The edges.
	 */
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

	/**
	 * Gets all the nodes.
	 * 
	 * @return The nodes.
	 */
	public Iterable<Node> getNodes()
	{
		return(nodes);
	}

	/**
	 * Gets the node index for a node with the given coordinates.
	 * 
	 * @param x The x-coordinate of the node. 
	 * @param y The y-coordinate of the node.
	 * @return The index of the node.
	 */
	public int getNodeIdx(int x, int y) 
	{
		// NOTE(oliver): This seems very bug-prone now...
		return x+width*y;
	}

	/**
	 * Makes an edge between two nodes.
	 * 
	 * @param nodeIdxA The index of the first node.
	 * @param nodeIdxB The index of the second node.
	 */
	public void makeEdge(int nodeIdxA, int nodeIdxB) 
	{
		// TODO: Code for making sure parallel overlapping edges get combined could go here
		adjMatrix.setValue(nodeIdxA, nodeIdxB, true);
	}

	/**
	 * Removes an edge between two nodes.
	 * 
	 * @param nodeIdxA The index of the first node.
	 * @param nodeIdxB The index of the second node.
	 */
	public void removeEdge(int nodeIdxA, int nodeIdxB) 
	{
		adjMatrix.setValue(nodeIdxA, nodeIdxB, false);
	}
	
	/**
	 * Randomizes the edges from any node to any other node.
	 */
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
	
	/**
	 * Mutates the glyph by removing or adding a single edge chosen at random.
	 */
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
	
	/**
	 * Makes a cross-mutation, in place, with another glyph by crossing their adjacency matrices.
	 * 
	 * @param that The other glyph.
	 */
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
	
	/**
	 * Gets the number of edges connected to the given node.
	 * 
	 * @param nodeIdx The index of the node.
	 * @return The number of edges connected to the node.
	 */
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
	
	/**
	 * Computes whether or not the given node is at the end of a stroke.
	 * 
	 * @param nodeIdx The index of the node.
	 * @return true if the node is at the end of a stroke, false if not.
	 */
	public boolean isEndOfStroke(int nodeIdx)
	{
		int degree = getDegree(nodeIdx);
		boolean result = degree % 2 != 0;
		return(result);
	}
	
	/**
	 * Gets all the nodes that are at the ends of strokes.
	 * 
	 * @return The nodes that are at the ends of strokes.
	 */
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
	
	/**
	 * Gets a node that represents the center of the glyph.
	 * 
	 * @return The node that represents the center of the glyph. 
	 */
	public Node getCentroid()
	{
		return nodes.centroid;
	}
	
	/**
	 * Makes a copy if this glyph.
	 * 
	 * @return The copy.
	 */
	public Glyph copy()
	{
		Nodes cloneNodes = nodes;
		AdjacencyMatrix cloneMatrix = adjMatrix.copy();
		int cloneWidth = width;
		
		Glyph clone = new Glyph(cloneNodes, cloneMatrix, cloneWidth);
		
		return(clone);
	}
}
