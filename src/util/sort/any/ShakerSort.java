package util.sort.any;

/**
 * Shaker Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class ShakerSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private ShakerSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{
	    for (int i = 0; i < elements.length>>1; i++)
	    {
	        boolean swapped = false;
	        for (int j = i; j < elements.length - i - 1; j++)
	        {
	            if (elements[j].compareTo(elements[j+1])>0)
	            {
	            	swap(elements,j,j+1);
	                swapped = true;
	            }
	        }
	        for (int j = elements.length - 2 - i; j > i; j--)
	        {
	            if (elements[j].compareTo(elements[j-1])<0)
	            {
	            	swap(elements,j,j-1);
	                swapped = true;
	            }
	        }
	        if(!swapped) break;
	    }
	}

	/**
	 * 
	 * @param e
	 * @param a
	 * @param b
	 */
	private static <ELEMENT extends Comparable<ELEMENT>> void swap(ELEMENT[] e, int a, int b)
	{
		ELEMENT T	= e[a]; 
		e[a]		= e[b];
		e[b]		= T;
	}

}