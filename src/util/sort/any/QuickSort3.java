package util.sort.any;

/**
 * Quick Sort (3-pivot) algorithm
 *
 * @author Jan Cajthaml
 */
public class QuickSort3
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private QuickSort3() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{ sort(elements,0,elements.length-1); }
	
	/**
	 * sort given collection 
	 * @param num collection of elements
	 * @param left left bounds
	 * @param right right bounds
	 */
	private static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT[] elements, int left, int right)
	{
		int j = 0;
		
		if( left + 32 > right )
		{
		    for( int p = left + 1; p <= right; p++ )
	        {
	            ELEMENT tmp = elements[ p ];

	            for( j = p; j > left && tmp.compareTo( elements[ j - 1 ] ) < 0; j-- )
	            	elements[ j ] = elements[ j - 1 ];
	            
	            elements[ j ] = tmp;
	        }
		}
        else
        {
    		int i		= (right+left)>>1;
    		ELEMENT v	= null;
    		
    		if (elements[left].compareTo(elements[i])>0)		swap(elements,left,i);
    		if (elements[left].compareTo(elements[right])>0)	swap(elements,left,right);
    		if (elements[i].compareTo(elements[right])>0)		swap(elements,i,right);

    		j = right-1;
    			
    		swap(elements,i,j);
    			
    		i = left;
    		v = elements[j];
    			
    		for(;;)
    		{
    			while(elements[++i].compareTo(v)<0);
    			while(elements[--j].compareTo(v)>0);
    			if (j<i) break;
    			swap (elements,i,j);
    		}
    		swap(elements,i,right-1);
    		sort(elements,left,j);
    		sort(elements,i+1,right);
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