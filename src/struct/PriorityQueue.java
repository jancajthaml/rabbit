package struct;

import java.util.Iterator;

/**
 * Priority Queue with 2 priorities HIGH and LOW
 *
 * @author Jan Cajthaml
 */
@SuppressWarnings("unchecked")
public class PriorityQueue<ELEMENT> implements Iterable<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private Queue<ELEMENT> high	= new Queue<ELEMENT>();
	private Queue<ELEMENT> low	= new Queue<ELEMENT>();

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates Priority Queue
	 */
	public PriorityQueue()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * 
	 * @param e element
	 * @param priority HIGH | LOW
	 */
	public void enqueue(ELEMENT e, boolean priority)
	{
		if(priority)	PriorityQueue.this.high.enqueue(e);
		else			PriorityQueue.this.low.enqueue(e);
	}
	
	/**
	 * Removes all elements
	 */
	public void clear()
	{
		PriorityQueue.this.high.clear();
		PriorityQueue.this.low.clear();
	}

	/**
	 * 
	 * @return element next element value
	 */
	public ELEMENT dequeue()
	{ return (PriorityQueue.this.high.isEmpty())?PriorityQueue.this.low.dequeue():PriorityQueue.this.high.dequeue(); }
	
	/**
	 * Priority iterator
	 */
	public Iterator<ELEMENT> iterator()
	{ return new MergeIterators<ELEMENT>(PriorityQueue.this.high.iterator(),PriorityQueue.this.low.iterator()); }
	
}
