package dk.itu.jglyph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JGlyph implements Graph
{
	private final static Random RNG = new Random();
	
	private Node[] nodes;
	private boolean[][] adjMatrix;

	/** Constructs a Glyph with width*height nodes. */
	public JGlyph(int width, int height) 
	{
		nodes = new Node[width*height];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node(i%width, i/width);
		}
		adjMatrix = new boolean[width*height][width*height];
	}

	@Override
	public Iterable<Edge> getEdges(int nodeIndex) {
		List<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < adjMatrix.length; i++) {
			// Isn't it stupid to create new edges every time?
			// And do we care for the order of the nodes in an edge?
			if (adjMatrix[i][nodeIndex]) edges.add(new Edge(nodes[i], nodes[nodeIndex]));
		}
		return edges;
	}

	@Override
	public Iterable<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = i; j < adjMatrix.length; j++) {
				if (adjMatrix[i][j]) edges.add(new Edge(nodes[i], nodes[j]));
			}
		}
		return edges;
	}

	@Override
	public Iterable<Node> getNodes() {
		// This is rather ineffective
		return Arrays.asList(nodes);
	}

	@Override
	public int getNodeId(int x, int y) {
		return x+x*y;
	}

	@Override
	public void makeEdge(int nodeIdA, int nodeIdB) {
		//TODO: Code for making sure parallel overlapping edges get combined could go here
		adjMatrix[nodeIdA][nodeIdB] = adjMatrix[nodeIdB][nodeIdA] = true;
	}

	@Override
	public void removeEdge(int nodeIdA, int nodeIdB) {
		adjMatrix[nodeIdA][nodeIdB] = adjMatrix[nodeIdB][nodeIdA] = false;
	}
	
	public void randomizeEdges()
	{
		for (int i = 0; i < nodes.length; i++) {
			for (int j = i; j < nodes.length; j++) {
				adjMatrix[i][j] = adjMatrix[j][i] = RNG.nextInt() % 2 == 0;
			}
		}
	}
}
