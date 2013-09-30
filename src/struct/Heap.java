package struct;

import java.util.Iterator;

/**
 * Lightweight Generic Heap implementation
 *
 * @author Jan Cajthaml
 * 
 */
public class Heap<ELEMENT extends Comparable<ELEMENT>> implements Iterable<ELEMENT>
{

	// >-------[attrs]---------------------------------------------------------------------------------------< //

    private int capacity	= 0;
    private ELEMENT[] array	= null;
    
	// >-------[ctor]---------------------------------------------------------------------------------------< //

    /**
     * Creates empty Heap for 100 elements
     */
    @SuppressWarnings("unchecked") public Heap( )
    { array = (ELEMENT[]) new Comparable[ 101 ]; }
    
    /**
     * Creates Heap from given array of elements
     * 
     * @param items source array
     */
    @SuppressWarnings("unchecked") public Heap( ELEMENT ... items )
    {
        capacity	= items.length;
        array		= (ELEMENT[]) new Comparable[ items.length + 1 ];
        
        for( int i = 0; i < items.length; i++ )	array[ i + 1 ] = items[ i ];
        for( int i = capacity>>1; i > 0; i-- )	heapify( i );
    }
    
	// >-------[methods]---------------------------------------------------------------------------------------< //

    /**
     * Doubles the capacity of internal array
     */
    @SuppressWarnings("unchecked") private void doubleArray( )
    {
        ELEMENT[] copy = (ELEMENT[]) new Comparable[ array.length<<1 ];
        System.arraycopy(array, 0, copy, 0, array.length);
        array = copy;
    }
    
    /**
     * Insert element
     * 
     * @param value element value
     */
    public void push( ELEMENT value )
    {
        if(capacity==array.length-1)
            doubleArray( );

        int hole	= ++capacity;
        array[ 0 ]	= value;
        
        for( ; value.compareTo( array[ hole>>1 ] ) < 0; hole >>= 1 )
            array[ hole ] = array[ hole>>1 ];
        
        array[ hole ] = value;
    }
   
    /**
     * Removes element from heap
     * 
     * @return ELEMENT element value
     */
    public ELEMENT pop()
    {
        ELEMENT min	= array[1];
        array[1]	= array[capacity--];
        heapify( 1 );    
        return min;
    }
    
    /**
     * Is Heap empty?
     * 
     * @return true if heap is empty 
     */
    public boolean isEmpty()
    { return capacity == 0;	}
    
    /**
     * Clears heap
     */
    public void clear()
    { capacity = 0;	}
    
    /**
     * Heapify Heap
     * 
     * @param hole index
     */
    private void heapify( int hole )
    {
        int child	= 0;
        ELEMENT tmp	= array[ hole ];
        
        for( ; hole<<1 <= capacity; hole = child )
        {
            child = hole<<1;
            if( child != capacity && array[ child + 1 ].compareTo( array[ child ] ) < 0 )
                child++;
            if( array[ child ].compareTo( tmp ) < 0 )
            	array[ hole ] = array[ child ];
            else break;
        }
        array[ hole ] = tmp;
    }
   
    /**
     * thread-safe iterator
     */
	public Iterator<ELEMENT> iterator()
	{
		return new Iterator<ELEMENT>()
		{
			public boolean hasNext()	{ return !isEmpty();	}
			public ELEMENT next()		{ return pop();			}
			public void remove()		{						}
		};
	}
	
}