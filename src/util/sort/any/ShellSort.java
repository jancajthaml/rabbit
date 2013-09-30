package util.sort.any;

/**
 * Shell Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class ShellSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private ShellSort() {}
		
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
    public static <ELEMENT extends Comparable<ELEMENT>>void sort(ELEMENT ... elements)
    {
    	int h		= 1;
    	int pivot	= 0;
    	
        while ((pivot=(h * 3 + 1)) < elements.length) h = pivot;
        while( h > 0 )
        {
            for (int i = h - 1; i < elements.length; i++)
            {
            	ELEMENT B	= elements[i];
                int j		= i;
                
                for( j = i; (j >= h) && (elements[j-h].compareTo(B)>0); j -= h) elements[j] = elements[j-h];
                elements[j] = B;
            }
            h /= 3;
        }
    }
   
}