package dk.itu.jglyph.user;

import java.util.HashMap;

import dk.itu.jglyph.Glyph;

/**
 * A model of a user, build by observing the choice he or she have made.
 */
public class Model 
{
	/**
	 * A map for finding the node corresponding to a glyph.
	 */
	private HashMap<Glyph,Node> nodes;
	
	/**
	 * Constructs a new user model.
	 */
	public Model()
	{
		nodes = new HashMap<>();
	}
	
	/**
	 * Makes a copy of the model.
	 * @return The copy.
	 */
	public Model copy() {
		// TODO someone confirm this works, please <3 - Kasra
		
		Model result = new Model();
		
		for (Node parent : nodes.values()) {
			for (Node child : parent.getChildren()) {
				result.addRelation(parent.glyph, child.glyph);
			}
		}
		
		return result;
	}
	
	/**
	 * Adds a new relation to the user model.
	 * 
	 * @param better The glyph that is better than the one which is worse.
	 * @param worseThe glyph that is worse than the one which is better.
	 */
	public void addRelation(Glyph better, Glyph worse)
	{
		Node nodeBetter = nodes.get(better);
		if(nodeBetter == null)
		{
			nodeBetter = new Node(better);
			nodes.put(better, nodeBetter);
		}
		
		Node nodeWorse = nodes.get(worse);
		if(nodeWorse == null)
		{
			nodeWorse = new Node(worse);
			nodes.put(worse, nodeWorse);
		}
		
		nodeBetter.addChild(nodeWorse);
	}
	
	/**
	 * Gets all the nodes in the model.
	 * 
	 * @return The nodes.
	 */
	public Iterable<Node> getNodes() {
		return nodes.values();
	}
	
	/**
	 * Calculates the current amount of relations in the model.
	 * 
	 * @return The current amount of relations in the model.
	 */
	public int relationCount () {
		int count = 0;
		
		for (Node parent : getNodes()) {
			for (@SuppressWarnings("unused") Node child : parent.getChildren()) {
				count++;
			}
		}
		
		return count;
	}

	/**
	 * Determines whether or not the model is currently empty.
	 * 
	 * @return true if the model is empty, false if not.
	 */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

}
