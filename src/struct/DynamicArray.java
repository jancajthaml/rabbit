package struct;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Lightweight Generic Infinite Array implementation 
 *
 * @author Jan Cajthaml
 * 
 */
public class DynamicArray<ELEMENT> implements Iterable<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private ELEMENT[] data	= null;
	private int capacity	= 0;

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new array with capacity for 17 elements
	 */
	public DynamicArray()
	{ this(17); }

	/**
	 * Creates new array with given capacity
	 * 
	 * @param capacity number of buckets
	 */
	@SuppressWarnings("unchecked") public DynamicArray(int capacity)
	{
		if (capacity<0) throw new IllegalArgumentException("Illegal capacity: "+ capacity);
		data		= (ELEMENT[]) new Object[capacity];
		capacity	= 0;
	}

	/**
	 * Creates new array from source element array
	 * 
	 * @param buffer source array
	 */
	@SuppressWarnings("unchecked") public DynamicArray(ELEMENT[] buffer)
	{
		data = (ELEMENT[]) new Object[(int)Math.min((capacity = buffer.length)*110L/100, Integer.MAX_VALUE)];
		System.arraycopy(buffer,0, data,0, capacity);
	}

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Add element to tail
	 * 
	 * @param value element value
	 */
	public void add(ELEMENT value)
	{
		ensureCapacity(capacity + 1);
		data[capacity++] = value;
	}

	/**
	 * Add array of integer to given index
	 * 
	 * @param index element index
	 * @param buffer array of integers
	 */
	public void add(int index, int[] buffer)
	{
		if (index>capacity) new IndexOutOfBoundsException("Index " + index +", not in range [0-" + capacity + "]");
		
		int add	= buffer.length;
		ensureCapacity(capacity+add);
		int moveCount	= capacity - index;
		if (moveCount>0) System.arraycopy(data,index, data,index+add, moveCount);
		System.arraycopy(data,index, buffer,0, moveCount);
		capacity += add;
	}

	/**
	 * Add element to index
	 * 
	 * @param index element index
	 * @param value element value
	 */
	public void add(int index, ELEMENT value)
	{
		if (index>capacity)	new IndexOutOfBoundsException("Index " + index +", not in range [0-" + capacity + "]");
		
		ensureCapacity(capacity+1);
		System.arraycopy(data,index, data,index+1, capacity-index);
		data[index] = value;
		capacity++;
	}

	/**
	 * Is element in array?
	 * 
	 * @param element value
	 * @return true if element is present in array
	 */
	public boolean contains(ELEMENT element)
	{
		for (int i=0; i<capacity; i++)
			if (data[i]==element) return true;
		return false;
	}
	
	/**
	 * Check if there is enought capacity for elements 
	 * 
	 * @param minCapacity minimum capacity
	 */
	@SuppressWarnings("unchecked") private final void ensureCapacity(int minCapacity)
	{
		int oldCapacity = data.length;
		if (minCapacity > oldCapacity)
		{
			ELEMENT[] oldData = data;
			int newCapacity = ((oldCapacity * 3)>>1) + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			data = (ELEMENT[]) new Object[newCapacity];
			System.arraycopy(oldData,0, data,0, capacity);
		}
	}

	/**
	 * Safe get element from array
	 * 
	 * @param i index
	 * @return element value
	 */
	public ELEMENT get(int i)
	{
		if (i>=capacity) new IndexOutOfBoundsException(i+" out of bounds <0-" + (capacity-1) + ">");
		return data[i];
	}
	
	/**
	 * clears the array
	 */
	public void clear()
	{ capacity = 0; }
	
	/**
	 * fills array with one value
	 * 
	 * @param value
	 */
	public void fill(int value)
	{ Arrays.fill(data, value); }
	
	/**
	 * Get element from array
	 * 
	 * @param index element index
	 * @return element element value
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	public ELEMENT getUnsafe(int index)
	{ return data[index]; }
	
	/**
	 * Number of elements
	 * 
	 * @return size number of elements
	 */
	public int getSize()
	{ return capacity; }
	
	/**
	 * Is array empty?
	 * 
	 * @return true if array is emtpy
	 */
	public boolean isEmpty()
	{ return capacity==0; }
	
	/**
	 * Set element value on given index
	 * 
	 * @param index element index
	 * @param value element value
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	public void setUnsafe(int index, ELEMENT value)
	{ data[index] = value; }
	
	/**
	 * fills provided fixed array with saved values saved in infinite array
	 * 
	 * @param array
	 */
	public void toArray(ELEMENT[] array)
	{ Arrays.copyOf(data, getSize(), array.getClass());	}
	
	/**
	 * Fill range from offset to (size-offset) with given value
	 * 
	 * @param offset start offset
	 * @param size number od elements
	 * @param value element value
	 */
	public void insertRange(int offset, int size, int value)
	{
		if (offset>capacity) new IndexOutOfBoundsException(offset+" out of bounds <0-" + (capacity-1) + ">");
		ensureCapacity(capacity+size);
		System.arraycopy(data,offset, data,offset+size, capacity-offset);
		if (value!=0) Arrays.fill(data, offset, offset+size, value);
		capacity += size;
	}

	/**
	 * Removes element on index
	 * 
	 * @param i element index
	 */
	public void remove(int i)
	{
		if (i>=capacity) new IndexOutOfBoundsException(i+" out of bounds <0-" + (capacity-1) + ">");
		
		int toMove = capacity - i - 1;
		if (toMove>0) System.arraycopy(data,i+1, data,i, toMove);
		--capacity;
	}

	/**
	 * Removes range of elements on given indexes from i to j
	 * 
	 * @param i from
	 * @param j to
	 */
	public void removeRange(int i, int j)
	{
		if (i>=capacity || j>capacity) new IndexOutOfBoundsException("{"+i+","+j+"} out of bounds <0-" + (capacity-1) + ">");
		
		int moveCount = capacity - j;
		System.arraycopy(data,j, data,i, moveCount);
		capacity -= (j - i);
	}

	/**
	 * Safe set value of element on given index
	 * 
	 * @param i element index
	 * @param value element value
	 */
	public void set(int i, ELEMENT value)
	{
		if (i>=capacity) new IndexOutOfBoundsException(i+" out of bounds <0-" + (capacity-1) + ">");
		data[i] = value;
	}

	/**
	 * thread-safe iterator
	 */
	public Iterator<ELEMENT> iterator()
	{
		return new Iterator<ELEMENT>()
		{
			int i = 0;
			
			public void remove()		{								}
			public boolean hasNext()	{ return (data[i+1] != null);	}
			public ELEMENT next()		{ return data[++i];				}
		};
	}
	
}