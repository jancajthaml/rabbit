package util.sort.any;

/**
 * Selection Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class SelectionSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private SelectionSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{
		for (int i = 0; i < elements.length - 1; i++)
		{
			int maxIndex = i;
			for (int j = i + 1; j < elements.length; j++)
				if (elements[j].compareTo(elements[maxIndex])<0) maxIndex = j;
			swap(elements,i,maxIndex);
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