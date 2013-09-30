package util.sort.raw;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Radix Sort algorithm
 * 
 * @author Jan Cajthaml
 *
 */
public class RadixSort
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
    private static int numberOfBuckets												= 10;
    @SuppressWarnings("unchecked") private static final Queue<Integer>[] buckets	= new ArrayDeque[numberOfBuckets];
    private static int[] unsorted													= null;

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private RadixSort() {}

    // >-------[post-process]---------------------------------------------------------------------------------------< //
    
    static
    {
        for (int i=0; i<numberOfBuckets; i++)
            buckets[i] = new ArrayDeque<Integer>();
    }
    
    // >-------[methods]---------------------------------------------------------------------------------------< //
    
    /**
	 * sort given collection
	 * 
	 * @param elements collection of integers
	 * @return sorted collection
	 */
    public static int[] sort(int ... elements)
    {
        RadixSort.unsorted	= elements;
        int numberOfDigits	= getMaxNumberOfDigits();
        int divisor			= 1;
        
        for (int n=0; n<numberOfDigits; n++)
        {
            for (int d : RadixSort.unsorted) buckets[getDigit(d,divisor)].add(d);
            
            int index = 0;
            
            for (Queue<Integer> bucket : buckets)
            {
                while (!bucket.isEmpty()) RadixSort.unsorted[index++] = bucket.remove();
            }
            divisor *= 10;
        }

        try { return RadixSort.unsorted; }
        
        finally
        {
            for (int i=0; i<numberOfBuckets; i++)
                buckets[i].clear();
            RadixSort.unsorted = null;
        }
    }
    
    /**
     * 
     * @return
     */
    private static int getMaxNumberOfDigits()
    {
        int max		= Integer.MIN_VALUE;
        int temp	= 0;
        
        for (int i : RadixSort.unsorted)
        {
            temp = (int)Math.log10(i)+1;
            if (temp>max) max = temp;
        }
        return max;
    }
    
    /**
     * 
     * @param integer
     * @param divisor
     * @return
     */
    private static int getDigit(int integer, int divisor)
    { return (integer / divisor) % 10; }

}