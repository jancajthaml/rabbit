package util.sort.any;

/**
 * Gnome Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class GnomeSort
{
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private GnomeSort() {}
		
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{
		int i	= 1;
		int j	= 2;
	 
		while(i < elements.length)
		{
			if ( elements[i-1].compareTo(elements[i]) <= 0 ) i = j++;
			else
			{
				swap(elements,i-1,i--);
				i = (i==0) ? j++ : i;
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