package util.sort.raw;

/**
 * Counting Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class CountingSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private CountingSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection
	 * 
	 * @param elements collection of integers
	 */
	public static void sort(int ... elements)
	{
	    int[] aux	= new int[elements.length];
	    int min		= elements[0];
	    int max		= elements[0];
	    
	    for (int i = 1; i < elements.length; i++)
	    {
	        if (elements[i] < min)		min = elements[i];
	        else if (elements[i] > max)	max = elements[i];
	    }
	    
	    int[] counts = new int[max - min + 1];
	    
	    for (int i = 0;  i < elements.length; i++)		counts[elements[i] - min]++;

	    counts[0]--;
	    
	    for (int i = 1; i < counts.length; i++)			counts[i] = counts[i] + counts[i-1];
	    for (int i = elements.length - 1; i >= 0; i--)	aux[counts[elements[i] - min]--] = elements[i];
	    for(int i=0; i<elements.length; i++)			elements[i]=aux[i];
	    
	}

}