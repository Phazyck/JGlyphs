package dk.itu.jglyph;

import java.util.Arrays;

/**
 * A sparse adjacency matrix for undirected graphs. 
 */
public class AdjacencyMatrix 
{
	/**
	 * The matrix.
	 */
	private final boolean[][] matrix;
	
	/**
	 * The width/height of the matrix.
	 */
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

	/**
	 * Constructs an adjacency matrix of a given size.
	 * 
	 * @param size The size.
	 */
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
	
	/**
	 * Constructs an adjacency matrix.
	 * 
	 * @param matrix The matrix.
	 */
	private AdjacencyMatrix(boolean[][] matrix)
	{
		this.size = matrix.length;
		this.matrix = matrix;
	}
	
	/**
	 * Gets a value from the adjacency matrix.
	 * 
	 * @param row The row.
	 * @param col The column.
	 * @return true if connected, false if not.
	 */
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
	
	/**
	 * Sets a value in the adjacency matrix.
	 * 
	 * @param row The row.
	 * @param col The column.
	 * @param value true if connected, false if not.
	 */
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
	
	/**
	 * Makes a copy of the adjacency matrix.
	 * 
	 * @return The copy.
	 */
	public AdjacencyMatrix copy()
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