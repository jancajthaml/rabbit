package struct;

import java.util.Iterator;

/**
 * Lightweight Queue implementation
 *
 * @author Jan Cajthaml
 * 
 */
public class Queue<ELEMENT> implements Iterable<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private LinkedList<ELEMENT> data = new LinkedList<ELEMENT>();

    // >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new empty Queue
	 */
	public Queue()
	{}
	
    // >-------[methods]---------------------------------------------------------------------------------------< //
    
	/**
	 * Inserts element into end of the Queue 
	 * 
	 * @param e source element
	 */
    public void enqueue(ELEMENT e)
    {
    	//if(data.contains(e)) return;	//unique
    	data.addLast(e);
    }

    /**
     * Gets and removes element from front of the Queue
     * 
     * @return element from front
     */
    public ELEMENT dequeue()
    {
    	if(data.isEmpty()) return null;
        return data.removeFirst();
	}

    /**
     * Gets element from front of the Queue
     * 
     * @return element from front
     */
    public ELEMENT front()
    { return data.peekFirst(); }

    /**
     * Clears Queue
     */
    public void clear()
    { data.clear(); }
    
    /**
     * Check whenever Queue is Empty
     * 
     * @return true if is Empty
     */
    public boolean isEmpty()
    { return data.isEmpty(); }

    /**
     * In order Queue iterator
     */
    public Iterator<ELEMENT> iterator()
    { return data.iterator(); }

    /**
     * Gets number of elements in Queue
     * 
     * @return number of elements
     */
	public int size()
	{ return data.size(); }

	/**
	 * Gets element on specific index
	 * 
	 * @param index source index
	 * 
	 * @return element
	 */
	//FIXME delete and use LinkedList
	public ELEMENT get(int index)
	{ return data.get(index); }

	/**
	 * Removes element
	 * 
	 * @param elem element to be removed
	 */
	public void remove(ELEMENT elem)
	{ data.removeFirstOccurrence(elem); }
    
	/**
	 * @return Queue[a1, a1, ..., an]
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Queue[");
		
		for(ELEMENT e : this) sb.append(e+", ");
		
		String res = sb.toString();
		return res.substring(0,res.length()-2)+"]";
	}

}