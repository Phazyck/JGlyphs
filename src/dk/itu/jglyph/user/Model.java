package dk.itu.jglyph.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.neat.StimulusTargetPair;
import dk.itu.jglyph.neat.TrainingSet;

public class Model 
{
	private HashMap<Glyph,Node> nodes;
	
	public Model()
	{
		nodes = new HashMap<>();
	}
	
	// Makes a deep copy
	public Model clone() {
		// TODO someone confirm this works, please <3 - Kasra
		
		Model result = new Model();
		
		for (Node parent : nodes.values()) {
			for (Node child : parent.getChildren()) {
				result.addRelation(parent.glyph, child.glyph);
			}
		}
		
		return result;
	}
	
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
	
	public Iterable<Node> getNodes() {
		return nodes.values();
	}
	
	public int relationCount () {
		int count = 0;
		
		for (Node parent : getNodes()) {
			for (Node child : parent.getChildren()) {
				count++;
			}
		}
		
		return count;
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

}
