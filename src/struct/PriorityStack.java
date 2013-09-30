package struct;

import java.util.Iterator;

/**
 * Priority Stack with 2 priorities HIGH and LOW
 *
 * @author Jan Cajthaml
 */
@SuppressWarnings("unchecked")
public class PriorityStack<ELEMENT> implements Iterable<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private Stack<ELEMENT> high	= new Stack<ELEMENT>();
	private Stack<ELEMENT> low	= new Stack<ELEMENT>();
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Creates Priority Stack
	 */
	public PriorityStack()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * 
	 * @param e element
	 * @param priority HIGH | LOW
	 */
	public void push(ELEMENT e,boolean priority)
	{
		if(priority)	PriorityStack.this.high.push(e);
		else			PriorityStack.this.low.push(e);
	}

	/**
	 * Removes all elements
	 */
	public void clear()
	{
		PriorityStack.this.high.clear();
		PriorityStack.this.low.clear();
	}
	
	/**
	 * 
	 * @return element next element value
	 */
	public ELEMENT pop()
	{ return (PriorityStack.this.high.isEmpty())?PriorityStack.this.high.pop():PriorityStack.this.low.pop(); }
	
	/**
	 * Priority iterator
	 */
	public Iterator<ELEMENT> iterator()
	{ return new MergeIterators<ELEMENT>(PriorityStack.this.high.iterator(),PriorityStack.this.low.iterator()); }
	
}