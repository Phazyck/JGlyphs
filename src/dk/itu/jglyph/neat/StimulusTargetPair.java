package dk.itu.jglyph.neat;

@Deprecated
public class StimulusTargetPair {
	final double[] target;
	final double[] stimulus;
	
	public StimulusTargetPair(double[] stimulus, double[] target) {
		this.stimulus = stimulus;
		this.target = target;
	}
}
