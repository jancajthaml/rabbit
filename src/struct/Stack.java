package struct;

import java.util.Iterator;

/**
 * Lightweight Stack implementation
 * 
 * @author Jan Cajthaml
 *
 * @param <ELEMENT>
 */
public class Stack<ELEMENT> implements Iterable<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private LinkedList<ELEMENT> data = new LinkedList<ELEMENT>();

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new Stack
	 */
	public Stack()
	{}
	
    // >-------[methods]---------------------------------------------------------------------------------------< //

    /**
     * Pushes element into stack
     * 
     * @param element
     */
    public void push(ELEMENT element)
    { data.addLast(element); }
    
    /**
     * Get element from top of stack
     * 
     * @return element value
     */
    public ELEMENT pop()
    {
        return data.removeLast();
	}

    /**
     * Clears the stack
     */
    public void clear()
    { data.clear(); }
    
    /**
     * Check whenever stack is empty
     * 
     * @return true if Stack is empty
     */
    public boolean isEmpty()
    { return data.isEmpty(); }

    /**
     * In order iterator
     */
    public Iterator<ELEMENT> iterator()
    {
    	return data.iterator();
    }

}