package dk.itu.jglyph;

public class AdjacencyMatrix 
{
	private final boolean[][] matrix;
	public final int size;
	
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
}