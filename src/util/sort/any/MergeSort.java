package util.sort.any;

/**
 * Merge Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class MergeSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private MergeSort() {}
	
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
	 * @param l low index
	 * @param h high index
	 */
	private static <ELEMENT extends Comparable<ELEMENT>>  void sort(ELEMENT elements[], int l, int h)
	{
		int lo			= l;
		int hi			= h;
		
		if (lo >= hi) return;
		
		int mid = (lo + hi) >> 1;
		
		sort(elements, lo, mid);
		sort(elements, mid + 1, hi);
		
		int end_lo		= mid;
		int start_hi	= mid + 1;
		
		while ((lo <= end_lo) && (start_hi <= hi))
		{
			if (elements[lo].compareTo(elements[start_hi])<0) lo++;
			else
			{
				ELEMENT T = elements[start_hi];
				
				for (int k = start_hi - 1; k >= lo; k--) elements[k+1] = elements[k];
				
				elements[lo++] = T;
				end_lo++;
				start_hi++;
			}
		}
	}

}