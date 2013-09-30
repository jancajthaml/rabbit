package util.sort.any;

/**
 * Bubble Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class BubbleSort
{
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private BubbleSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort( ELEMENT ... elements )
	{
	     int j			= 0;
	     boolean flag	= true;
	     
	     while ( flag )
	     {
	    	 flag= false;
	    	 for( j=0;  j < elements.length -1;  j++ )
	    	 {
	    		 if ( elements[ j ].compareTo(elements[j+1])>0 )
	    		 {
	    			 swap(elements,j,j+1);
	    			 flag = true;  
	    		 } 
	    	 }
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