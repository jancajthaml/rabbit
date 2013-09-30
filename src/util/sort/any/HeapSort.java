package util.sort.any;

/**
 * Heap Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class HeapSort
{

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private HeapSort() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT ... elements)
	{
		for(int i=elements.length; i>1; i--) sort(elements, i - 1);
	}
	
	/**
	 * 
	 * @param array
	 * @param arr_ubound
	 */
	public static <ELEMENT extends Comparable<ELEMENT>> void sort(ELEMENT[] array, int arr_ubound)
	{
		int i			= 0;
		int o			= 0;
		int lChild		= 0;
		int rChild		= 0;
		int mChild		= 0;
		int root		= (arr_ubound-1)/2;
		ELEMENT temp	= null;

		for(o = root; o >= 0; o--)
		{
			for(i=root;i>=0;i--)
			{
				lChild = (2*i)+1;
				rChild = (2*i)+2;
				if((lChild <= arr_ubound) && (rChild <= arr_ubound))
				{
					if(array[rChild].compareTo(array[lChild])>=0)
						mChild = rChild;
					else
						mChild = lChild;
				}
				else mChild = (rChild > arr_ubound)?lChild:rChild;

				if(array[i].compareTo(array[mChild])<0)
				{
					temp = array[i];
					array[i] = array[mChild];
					array[mChild] = temp;
				}
			}
		}
		temp				= array[0];
		array[0]			= array[arr_ubound];
		array[arr_ubound]	= temp;
	}

}