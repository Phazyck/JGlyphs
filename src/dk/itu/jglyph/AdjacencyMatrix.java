package dk.itu.jglyph;

import java.util.Arrays;

public class AdjacencyMatrix 
{
	private final boolean[][] matrix;
	public final int size;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(matrix);
		result = prime * result + size;
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
		AdjacencyMatrix other = (AdjacencyMatrix) obj;
		if (!Arrays.deepEquals(matrix, other.matrix))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	public AdjacencyMatrix(int size)
	{
		this.size = size;
		matrix = new boolean[size][];
		
		for(int row = 0; row < size; ++row)
		{
			int cols = size - row;
			matrix[row] = new boolean[cols];
		}
	}
	
	private AdjacencyMatrix(boolean[][] matrix)
	{
		this.size = matrix.length;
		this.matrix = matrix;
	}
	
	public boolean getValue(int row, int col)
	{
		boolean element;
		
		if(row > col)
		{
			element = getValue(col, row);
		}
		else
		{
			element = matrix[row][col-row];
		}
		
		return(element);
	}
	
	public void setValue(int row, int col, boolean value)
	{
		if(row > col)
		{
			setValue(col, row, value);
		}
		else
		{
			matrix[row][col-row] = value;
		}
	}
	
	public AdjacencyMatrix clone()
	{
		boolean[][] cloneMatrix = new boolean[size][];
		
		for(int i = 0; i < size; ++i)
		{
			cloneMatrix[i] = matrix[i].clone();
		}
		
		AdjacencyMatrix clone = new AdjacencyMatrix(cloneMatrix);
		
		return(clone);
	}
}