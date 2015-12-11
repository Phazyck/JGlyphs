package dk.itu.jglyph.user;

import java.util.ArrayList;
import java.util.HashMap;
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
		}
		
		Node nodeWorse = nodes.get(worse);
		if(nodeWorse == null)
		{
			nodeWorse = new Node(worse);
		}
		
		nodeBetter.addChild(nodeWorse);
	}
	
	public List<StimulusTargetPair> getTrainingSet()
	{
		int count = nodes.size();
		
		FeatureExtractors extractors = FeatureExtractors.getInstance();
		
		if(count < 1)
		{
			// NEAT needs at least one stim/target pair, so feed with dummy for starters
			List<StimulusTargetPair> data = new LinkedList<>();
			
			double[] stimulus = new double[extractors.count()];
			double[] target = new double[]{0};
			StimulusTargetPair pair = new StimulusTargetPair(stimulus, target);
			data.add(pair);

			return(data);	
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

}
