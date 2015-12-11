package dk.itu.jglyph.user;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import dk.itu.jglyph.Glyph;

public class Node 
{
	public final Glyph glyph;
	private HashSet<Node> children;
	
	public Node(Glyph glyph)
	{
		this.glyph = glyph;
		this.children = new HashSet<>();
	}
	
	public void addChild(Node node)
	{
		if(node.hasChild(this))
		{
			throw new RuntimeException("Oh shit! Cycles!!! T_T");
		}
		
		children.add(node);
	}
	
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
