package dk.itu.jglyph;

public interface Graph<T> {
	/** Get all lines to/from a specific node.*/
	public Iterable<Edge> getEdges(int nodeIndex);

	/** Get all lines to/from all nodes. */
	public Iterable<Edge> getEdges();

	/** Get all nodes in the graph. */  
	public Iterable<T> getNodes();

	/** 
	 * Get a node index of a given coordinate.
	 * throw an exception if coordinate is invalid - I guess…
	 */
	public int getNodeId(int x, int y);

	/** Make an edge between nodes A and B. */
	public void makeEdge(int nodeIdA, int nodeIdB);

	/** Remove an edge between nodes A and B. */
	public void removeEdge(int nodeIdA, int nodeIdB);
}
