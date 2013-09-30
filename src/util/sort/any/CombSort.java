package util.sort.any;

/**
 * Combo Sort algorithm
 *
 * @author Jan Cajthaml
 */
public class CombSort
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

    private static final float SHRINKFACTOR = 1.3f;
    
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private CombSort() {}

	// >-------[methods]---------------------------------------------------------------------------------------< //

    /**
	 * sort given collection 
	 * @param elements collection of elements
	 */
	public static <ELEMENTS extends Comparable<ELEMENTS>> void sort(ELEMENTS ... elements)
	{
		boolean flipped	= false;
		int gap			= elements.length;
		int top			= 0;
		int i			= 0;
		int j			= 0;
		
		do
		{
			gap = (int) ((float) gap / SHRINKFACTOR);
			switch (gap)
			{
				case 0: gap = 1; break;
				case 9: case 10: gap = 11; break;
				default: break;
			}
			
			flipped	= false;
			top		= elements.length - gap;
			
			for (i = 0; i < top; i++)
			{
				j = i + gap;
				if (elements[i].compareTo(elements[j])>0)
				{
					swap(elements,i,j);
					flipped = true;
				}    
			}
		}
		while (flipped || (gap > 1));
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