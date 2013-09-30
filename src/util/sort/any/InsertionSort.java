package util.sort.any;

/**
 * Insertion Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class InsertionSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private InsertionSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{
		int j		= 0;
		int i		= 0;
		ELEMENT key	= null;
		
		for (j = 1; j < elements.length; j++)
	    {
			key = elements[ j ];
			for(i = j - 1; (i >= 0) && (elements[ i ].compareTo(key)>0); i--)
				elements[ i+1 ] = elements[ i ];
			elements[ i+1 ] = key;
	    }
	}

}