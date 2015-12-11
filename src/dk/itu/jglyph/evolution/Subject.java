package dk.itu.jglyph.evolution;

import java.util.Comparator;

import dk.itu.jglyph.Glyph;

public class Subject implements Comparable<Subject> 
{
	public final Glyph glyph;
	public final double fitness;
	
	public Subject(Glyph glyph, double fitness)
	{
		this.glyph = glyph;
		this.fitness = fitness;
	}

	@Override
	public int compareTo(Subject that) {
		int comparison = Double.compare(this.fitness, that.fitness);
		
		if(comparison == 0)
		{
			comparison = Integer.compare(this.hashCode(), that.hashCode());
		}
		
		return(comparison);
	}
	
	public final static Comparator<Subject> COMPARATOR_DESCENDING = new Comparator<Subject>() {

		@Override
		public int compare(Subject o1, Subject o2) {
			return -o1.compareTo(o2);
		}
	};
}
