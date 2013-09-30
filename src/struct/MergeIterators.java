package struct;

import java.util.Iterator;

/**
 * Iterator that unifies the group of iterators into one
 *
 * @author Jan Cajthaml
 */
public class MergeIterators<ELEMENT> implements Iterator<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

    private Iterator<ELEMENT> iterator[]	= null;
    private int pointer						= 0;
    
	// >-------[ctor]---------------------------------------------------------------------------------------< //

    /**
     * Creates union of iterators
     * @param iterators
     */
    public MergeIterators(Iterator<ELEMENT> ... iterators)
    { iterator = iterators; }

    // >-------[methods]---------------------------------------------------------------------------------------< //

    /**
     * has next element?
     */
    public boolean hasNext()
    {
    	while ( pointer < iterator.length && !iterator[pointer].hasNext() )	pointer++;
    	return pointer < iterator.length;
    }

    /**
     * get next element
     */
    public ELEMENT next()
    {
    	while ( pointer < iterator.length && !iterator[pointer].hasNext() ) pointer++;
    	return iterator[pointer].next();
    }

    /**
     * not implemented
     */
    @Deprecated
	public void remove()
	{}
	
}