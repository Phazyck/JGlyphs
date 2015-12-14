package dk.itu.jglyph.user;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import dk.itu.jglyph.Glyph;

/**
 * A node in a relation graph used to make a user model.
 */
public class Node 
{
	/**
	 * The glyph which is tied to the node.
	 */
	public final Glyph glyph;
	
	/**
	 * The children of this node.
	 */
	private HashSet<Node> children;
	
	/**
	 * The parents of this node.
	 */
	public HashSet<Node> parents;
	
	/**
	 * Constructs a new Node from a given glyph.
	 * 
	 * @param glyph The glyph.
	 */
	public Node(Glyph glyph)
	{
		this.glyph = glyph;
		this.children = new HashSet<>();
		this.parents = new HashSet<>();
	}
	
	/**
	 * Adds a given node as a child to this node.
	 * 
	 * @param node The child-to-be.
	 */
	public void addChild(Node node)
	{
		if(node.hasChild(this))
		{
			throw new RuntimeException("Oh shit! Cycles!!! T_T");
		}
		
		children.add(node);
		node.parents.add(this);
	}
	
	/**
	 * Determines if a given node is a child of this node.
	 * 
	 * @param node The node.
	 * @return true if the node is a child of this node, false if not.
	 */
	public boolean hasChild(Node node)
	{
		for(Node child : children)
		{
			if(child.equals(node) || child.hasChild(node))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates the amount of unique children of this node.
	 * 
	 * @return The amount of unique children of this node.
	 */
	public int countUniqueChildren()
	{
		HashSet<Node> uniques = new HashSet<>();
		
		Queue<Node> queue = new LinkedList<Node>(children);
		
		while(!queue.isEmpty())
		{
			Node child = queue.poll();
			
			if(uniques.contains(child))
			{
				continue;
			}
			
			uniques.add(child);
			
			queue.addAll(child.children);
		}
		
		int count = uniques.size();
		
		return(count);
	}
	
	/**
	 * Gets all the immediate children of this node.
	 * @return The children.
	 */
	public Iterable<Node> getChildren() {
		return children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((glyph == null) ? 0 : glyph.hashCode());
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
		Node other = (Node) obj;
		if (glyph == null) {
			if (other.glyph != null)
				return false;
		} else if (!glyph.equals(other.glyph))
			return false;
		return true;
	}
}
