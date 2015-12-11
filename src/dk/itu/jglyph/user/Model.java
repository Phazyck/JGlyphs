package dk.itu.jglyph.user;

import java.util.HashMap;

import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.features.FeatureExtractors;
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
		}
		
		Node nodeWorse = nodes.get(worse);
		if(nodeWorse == null)
		{
			nodeWorse = new Node(worse);
		}
		
		nodeBetter.addChild(nodeWorse);
	}
	
	public void updateTrainingSet()
	{
		int count = nodes.size();
		
		FeatureExtractors extractors = FeatureExtractors.getInstance();
		
		if(count < 1)
		{
			TrainingSet.stimuli = new double[1][extractors.count()];
			TrainingSet.targets = new double[][]{{0}};
			return;
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
		
		double[][] stimuli = new double[count][];
		double[][] targets = new double[count][];
		
		for(int i = 0; i < count; ++i)
		{
			Node value = values[i];
			Glyph glyph = value.glyph;
			stimuli[i] = FeatureExtractors.getInstance().extractFeatures(glyph);
			double target = childCounts[i] / maxChildCount;
			targets[i] = new double[] { target };
		}
		
		TrainingSet.stimuli = stimuli;
		TrainingSet.targets = targets;
	}

}
