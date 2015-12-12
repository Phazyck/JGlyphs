package dk.itu.jglyph.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
	
	public List<StimulusTargetPair> getTrainingSetLevel()
	{
		if (nodes.isEmpty()) return getDummyTrainingSet();
		
		HashSet<Node> used = new HashSet<Node>();
		List<List<Node>> levels = new ArrayList<List<Node>>();
		while (used.size() != nodes.size()) {
			List<Node> currentLevel = new ArrayList<Node>();
			levels.add(currentLevel);
			for (Node node : nodes.values()) {
				// any parents that are not in used is a parent we need to label skip it so we can take the parent first
				if (used.contains(node) || !used.containsAll(node.parents)) continue;
				currentLevel.add(node);
			}
			used.addAll(currentLevel);
		}
		
		List<StimulusTargetPair> data = new ArrayList<>();
		
		double maxLvl = levels.size() - 1;
		int lvl = levels.size() - 1;
		for (List<Node> level : levels) {
			for (Node node : level) {
				double[] stimulus = FeatureExtractors.getInstance().extractFeatures(node.glyph);
				double[] target = new double[]{lvl/maxLvl};
				data.add(new StimulusTargetPair(stimulus, target));
			}
			lvl--;
		}
		
		return data;
	}
	
	public List<StimulusTargetPair> getTrainingSetChildCount()
	{
		int count = nodes.size();
		
		FeatureExtractors extractors = FeatureExtractors.getInstance();
		
		if(count < 1)
		{
			// NEAT needs at least one stim/target pair, so feed with dummy for starters
			return getDummyTrainingSet();
		}
		
		Node[] values = new Node[count]; 
		values = nodes.values().toArray(values);
		
		double[] childCounts = new double[count];
		
		double maxChildCount = -1;
		
		for(int i = 0; i < count; ++i)
		{
			Node value = values[i];
			int childCount = value.countUniqueChildren();
			if(childCount > maxChildCount)
			{
				maxChildCount = childCount;
			}
			
			childCounts[i] = childCount;
		}
		
		List<StimulusTargetPair> data = new LinkedList<>();
		
		for(int i = 0; i < count; ++i)
		{
			Node value = values[i];
			Glyph glyph = value.glyph;
			double[] stimulus = FeatureExtractors.getInstance().extractFeatures(glyph);
			double[] target = new double[] {childCounts[i] / maxChildCount };
			StimulusTargetPair pair = new StimulusTargetPair(stimulus, target);
			data.add(pair);
		}

		return(data);	
	}
	
	private static List<StimulusTargetPair> getDummyTrainingSet()
	{
		List<StimulusTargetPair> data = new LinkedList<>();
		
		double[] stimulus = new double[FeatureExtractors.getInstance().count()];
		double[] target = new double[]{0};
		StimulusTargetPair pair = new StimulusTargetPair(stimulus, target);
		data.add(pair);
		return data;
	}

	public Iterable<Node> getNodes() {
		return nodes.values();
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

}
