package struct;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 
 * @author Jan Cajthaml
 *
 * @param <E>
 */
//UNCHECKED
//UNCHECKED
@SuppressWarnings("serial")
public class LinkedList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable, Deque<E>
{

	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private E[] values;
	private int size;
	private int start;
	private int end;
	private int gapSize;
	private int gapIndex;
	private int gapStart;

	// >-------[ctor]---------------------------------------------------------------------------------------< //
    
	/**
	 * 
	 * @param copy
	 * @param other
	 */
	LinkedList(boolean copy, LinkedList<E> other)
    {
        if (!copy) return;
        this.values		= other.values;
        this.size		= other.size;
        this.start		= other.start;
        this.end		= other.end;
        this.gapSize	= other.gapSize;
        this.gapIndex	= other.gapIndex;
        this.gapStart	= other.gapStart;
    }

	/**
	 * Creates new LinkedList for 10 elements
	 */
	public LinkedList()
	{ this(10); }

	/**
	 * Creates new LinkedList for capacity elements
	 * 
	 * @param capacity number of elements
	 */
	@SuppressWarnings("unchecked") public LinkedList(int capacity)
	{ init((E[]) new Object[capacity], true); }

	/**
	 * Creates new LinkedList from given Collection
	 * 
	 * @param other source collection
	 */
	public LinkedList(Collection<? extends E> other)
	{ init(toArray(other), false); }
	
	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new LinkedList
	 * 
	 * @return new LinkedList
	 */
	public static <E> LinkedList<E> newInstance()
    { return new LinkedList<E>(); }

	/**
	 * Creates new LinkedList from array of elements
	 * 
	 * @param elems array of elements
	 * @return new LinkedList
	 */
	public static <E> LinkedList<E> newInstance(E... elems)
	{
		LinkedList<E> list = new LinkedList<E>(elems.length);
		
		for (E elem: elems)	list.add(elem);
		
		return list;
	}

	/**
	 * Converts fake index to physical index
	 * 
	 * @param idx fake index
	 * @return physical index
	 */
	private final int _index(int idx)
	{
		int _index = idx+start;
		
		if (idx >= gapIndex)			_index += gapSize;
		if (_index >= values.length)	_index -= values.length;
		
		return _index;
	}

	/**
	 * 
	 * @param idx0
	 * @param idx1
	 * @return
	 */
	private int[] _index(int idx0, int idx1)
	{
		assert(idx0 >=0 && idx1 <= size && idx0 <= idx1);
		
		if (idx0 == idx1) return new int[0];

		idx1--;
		int pidx0 = _index(idx0);
		
		if (idx1 == idx0) return new int[] { pidx0, pidx0+1 };

		int pidx1 = _index(idx1);
		
		if (pidx0 < pidx1) return (gapSize > 0 && pidx0 < gapStart && pidx1 > gapStart)?new int[]{ pidx0, gapStart, gapStart+gapSize, pidx1+1 }:new int[] { pidx0, pidx1+1 };
		
		else
		{
			if (gapSize > 0 && pidx1 > gapStart && gapStart > 0)
				return new int[] { pidx0, values.length, 0, gapStart, gapStart+gapSize, pidx1+1 };
			
			else if (gapSize > 0 && pidx0 < gapStart && gapStart+gapSize < values.length)
				return new int[] { pidx0, gapStart, gapStart+gapSize, values.length, 0, pidx1+1 };
			
			else
			{
				int end = values.length;
				if (gapSize > 0 && gapStart > pidx0) end = gapStart;
				
				int start = 0;
				if (gapSize > 0 &&  (gapStart+gapSize)%values.length < pidx1+1)
					start = (gapStart+gapSize)%values.length;
			
				return new int[] { pidx0, end, start, pidx1+1 };
			}
		}
	}

	/**
	 * Converts Collection to array
	 * 
	 * @param coll source Collection
	 * @return array of elements
	 */
	static Object[] toArray(Collection<?> coll)
	{
    	Object[] values = coll.toArray();
    	return (values.getClass() != Object[].class)?Arrays.copyOf(values, values.length, Object[].class):values;
	}

	/**
	 * Create deep copy of LinkedList
	 * 
	 * @return copy of LinkedList
	 */
	@SuppressWarnings("unchecked") public LinkedList<E> copy()
    { return (LinkedList<E>) clone(); }

	/**
	 * 
	 * @return unmodifiable LinkedList
	 */
    public LinkedList<E> unmodifiableList()
    { return new ImmutableLinkedList<E>(this); }

    /**
     * deep copy of LinkedList
     */
	@SuppressWarnings("unchecked") @Override public Object clone()
	{
		try
		{
			LinkedList<E> list = (LinkedList<E>) super.clone();
		    init(toArray(), false);
		    return list;
		}
		catch (CloneNotSupportedException e)
		{ throw new InternalError(); }
    }

	/**
	 * remove all elements
	 */
	@Override public void clear()
	{ init(null, true); }

	/**
	 * 
	 * @param newValues
	 * @param empty
	 */
	@SuppressWarnings("unchecked") void init(Object[] newValues, boolean empty)
	{
		if (newValues != null)
			values = (E[]) newValues;

		size		= (empty)?0:values.length;
		start		= 0;
		end			= 0;
		gapSize		= 0;
		gapStart	= 0;
		gapIndex	= 0;
	}

	/**
	 * number of elements
	 */
	@Override public int size()
	{ return size; }

	/**
	 * LinkedList capacity
	 * 
	 * @return capacity
	 */
	public int capacity()
	{ return values.length; }

	/**
	 * 
	 */
    @Override public E get(int index)
    {
    	if (index < 0 || index >= size) throw new IllegalArgumentException("Invalid index: " + index + " (size: " + size + ")");
        return _get(index);
    }

    /**
     * 
     * @param index
     * @return
     */
    protected E _get(int index)
    {
    	int _index = index+start;
    	
        if (index >= gapIndex)			_index += gapSize;
        if (_index >= values.length)	_index -= values.length;
        
        return values[_index];
    }

    /**
     * 
     */
	@Override public E set(int index, E elem)
	{
		checkIndex(index);
		return _set(index, elem);
	}

	/**
	 * 
	 * @param index
	 * @param elem
	 * @return
	 */
    protected E _set(int index, E elem)
    {
    	int _index		= _index(index);
        E oldElem		= values[_index];
        values[_index]	= elem;
        return oldElem;
    }

    /**
     * 
     * @param index
     * @param elem
     * @return
     */
    protected E _reset(int index, E elem)
    {
        int _index		= _index(index);
        E oldElem		= values[_index];
        values[_index]	= elem;
        return oldElem;
    }
    
    /**
     * 
     */
    protected void _modify() {}

    /**
     * Inserts element
     */
    @Override public boolean add(E elem)
    { return _add(-1, elem); }

    /**
     * Inderts element on index
     */
	@Override public void add(int index, E elem)
	{
		checkIndexAdd(index);
		_add(index, elem);
	}

	protected boolean _add(int index, E elem)
	{
        ensureCapacity(size() + 1);

		if (index == -1) index = size;
		
        int _index;
		
		if (index == size && (end != start || size == 0))
		{
			_index = end;
			end++;
			if (end >= values.length)
				end -= values.length;
		}
		else if (index == 0 && (end != start || size == 0))
		{
			start--;
			if (start < 0)		start += values.length;
			
			_index = start;
			if (gapSize > 0)	gapIndex++;
		}
		else if (gapSize > 0 && index == gapIndex)
		{
			_index = gapStart+gapSize-1;
			if (_index >= values.length)
				_index -= values.length;
			
			gapSize--;
		}
		else
		{
			_index = _index(index);

			if (gapSize == 0)
			{
				if (start < end && start > 0)
				{
					int len1 = _index-start;
					int len2 = end-_index;
					if (len1 <= len2)
					{
						moveData(start, 0, len1);
						
						gapSize		= start-1;
						gapStart	= len1;
						gapIndex	= len1;
						start		= 0;
						
						_index--;
					}
					else
					{
						moveData(_index, values.length-len2, len2);
						
						gapSize		= values.length-end-1;
						gapStart	= _index+1;
						gapIndex	= index+1;
						end			= 0;
					}
				}
				else if (_index < end)
				{
					int len			= end-_index;
					int rightSize	= (start-end+values.length)%values.length;
					
					moveData(_index, end+rightSize-len, len);
					
					end				= start;
					gapSize			= rightSize-1;
					gapStart		= _index+1;
					gapIndex		= index+1;
				}
				else
				{
					int len			= _index-start;
					int rightSize	= start-end;
					
					moveData(start, end, len);
					
					start			-= rightSize;
					end				= start;
					gapSize			= rightSize-1;
					gapStart		= start+len;
					gapIndex		= index;
					
					_index--;
				}
			}
			else
			{
				boolean moveLeft;
				int gapEnd = (gapStart+gapSize-1) % values.length + 1;
				if (gapEnd < gapStart)
				{
					int len1 = _index-gapEnd;
					int len2 = gapStart-_index-1;
					moveLeft = (len1 <= len2);
				}
				else moveLeft = (_index > gapStart);
				
				if (moveLeft)
				{
					int src = gapStart+gapSize;
					int dst = gapStart;
					int len = _index-gapEnd;
					moveGap(src, dst, len);
					_index--;
					gapSize--;
					gapIndex = index;
					gapStart += len;
					if (gapStart >= values.length)
						gapStart -= values.length;

					if (index == 0)
					{
						start = _index;
						if ((gapStart + gapSize) % values.length == start)
						{
							end		= gapStart;
							gapSize	= 0;
						}
					}
				}
				else
				{
					int src = _index;
					int dst = _index+gapSize;
					int len = gapStart-_index;
					moveGap(src, dst, len);
					gapSize--;
					gapStart = _index+1;
					gapIndex = index+1;

					if (index == 0)
					{
						start = _index;
						end = _index;
					}
					else if (index == size)
					{
						if ((gapStart + gapSize) % values.length == start)
						{
							end = gapStart;
							gapSize = 0;
						}
					}
				}
			}
		}

		values[_index] = elem;
		size++;

		return true;
	}

	private void moveGap(int src, int dst, int len)
	{
		if (src > values.length)	src -= values.length;
		if (dst > values.length)	dst -= values.length;

		if (start >= src && start < src+len)
		{
			start += dst-src;
			if (start >= values.length) start -= values.length;
		}
		if (end >= src && end < src+len)
		{
			end += dst-src;
			if (end >= values.length)	end -= values.length;
		}
		if (dst+len <= values.length)
		{
			moveData(src, dst, len);
		}
		else
		{
			int len2 = dst+len - values.length;
			int len1 = len - len2;
			if (!(src <= len2 && len2 < dst))
			{
				moveData(src+len1, 0, len2);
				moveData(src, dst, len1);
			}
			else
			{
				moveData(src, dst, len1);
				moveData(src+len1, 0, len2);
			}
		}
	}
	
	private void moveData(int src, int dst, int len)
	{
		System.arraycopy(values, src, values, dst, len);

		if (src <= dst)
		{
			int start	= src;
			int end		= (dst < src+len) ? dst : src+len;

			for (int i=start; i<end; i++) 
				values[i] = null;
			
		}
		else
		{
			int start	= (src > dst+len) ? src : dst+len;
			int end		= src+len;
			
			for (int i=start; i<end; i++)
				values[i] = null;
		}
	}

	/**
	 * Removes element on given index
	 */
	@Override public E remove(int index)
	{
		checkIndex(index);
		return _remove(index);
	}

	/**
	 * Removes element on given index
	 *  
	 * @param index element index
	 * 
	 * @return true if removal was successful
	 */
	protected E _remove(int index)
	{
		int _index;

		if (index == size-1)
		{
		
			end--;
			if (end < 0)
				end += values.length;
			
			_index = end;

			if (gapSize > 0)
			{
				if (gapIndex == index)
				{
					end = gapStart;
					gapSize = 0;
				}
			}
		
		}
		else if (index == 0)
		{

			_index = start;
			start++;
			if (start >= values.length)
				start -= values.length;
			
			if (gapSize > 0)
			{
				if (gapIndex == 1)
				{
					start += gapSize;
					if (start >= values.length)
						start -= values.length;
					
					gapSize = 0;
				}
				else gapIndex--;				
			}
		}
		else
		{
			_index = _index(index);

			if (gapSize == 0)
			{
				gapIndex = index;
				gapStart = _index;
				gapSize = 1;
			}
			else if (index == gapIndex)
			{
				gapSize++;
			}
			else if (index == gapIndex-1)
			{
				gapStart--;
				if (gapStart < 0) gapStart += values.length;
				gapSize++;
				gapIndex--;
			}
			else
			{
			
				boolean moveLeft;
				int gapEnd = (gapStart+gapSize-1) % values.length + 1;
				if (gapEnd < gapStart)
				{
					int len1 = _index-gapEnd;
					int len2 = gapStart-_index-1;
					moveLeft = (len1 <= len2);
					
				}
				else moveLeft = (_index > gapStart);
				
				if (moveLeft)
				{
					int src = gapStart+gapSize;
					int dst = gapStart;
					int len = _index-gapEnd;
					moveGap(src, dst, len);
					gapStart += len;
					if (gapStart >= values.length)
						gapStart -= values.length;
					gapSize++;
					
				}
				else
				{
					int src = _index+1;
					int dst = _index+gapSize+1;
					int len = gapStart-_index-1;
					moveGap(src, dst, len);
					gapStart = _index;
					gapSize++;
				}
				gapIndex = index;
			}
		}

		E removed = values[_index];
		values[_index] = null;
		size--;

		return removed;
	}

	/**
	 * 
	 * @param minCapacity
	 */
	@SuppressWarnings("unchecked") public void ensureCapacity(int minCapacity)
	{
		_modify();

		int oldCapacity = values.length;
		
		if (minCapacity <= oldCapacity) return;
		
	    int newCapacity = ((oldCapacity * 3)>>1) + 1;
	    
	    if (newCapacity < minCapacity) newCapacity = minCapacity;
    	
		E[] newValues = null;
		
		if (start == 0) newValues = Arrays.copyOf(values, newCapacity);
		else if (start > 0)
		{
			int grow = newCapacity-values.length;
			newValues = (E []) new Object[newCapacity];
			System.arraycopy(values, 0, newValues, 0, start);
			System.arraycopy(values, start, newValues, start+grow, values.length-start);
			
			if (gapStart > start && gapSize > 0) gapStart += grow;
			
			start += grow;
		}
		if (end == 0 && size != 0) end = values.length;
		
		values = newValues;
	}

	/**
	 * 
	 */
    @Override public boolean equals(Object obj)
    {
    	if (obj == this)				return true;
    	if (!(obj instanceof List<?>))	return false;
    	
    	@SuppressWarnings("unchecked")
		List<E> list = (List<E>) obj;
    	if (size != list.size())	return false;
    	
    	for (int i=0; i<size; i++)
    	{
    		if (!softEquals(_get(i), list.get(i)))	return false;
    	}
    	return true;
    }

    /**
     * 
     */
    @Override public int hashCode()
    {
    	int hashCode = 1;
    	
    	for (int i=0; i<size; i++)
    		hashCode = 31*hashCode + _hashCode(_get(i));
    	
    	return hashCode;
    }

    /**
     * 
     */
	@Override public String toString()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		
		for (int i=0; i<size(); i++)
		{
			if (i > 0) buf.append(", ");
			buf.append(_get(i));
		}
		
		buf.append("]");
		return buf.toString();
	}

	/**
	 * Checks whenever list is empty
	 */
	@Override public boolean isEmpty()
	{ return size == 0; }

	private static boolean softEquals(Object elem1, Object elem2)
	{
		if (elem1 == null)
		{
			if (elem2 == null) return true;
		}
		else if (elem1.equals(elem2)) return true;
		
		return false;
	}

	private static int _hashCode(Object elem)
	{ return (elem == null)?0:elem.hashCode(); }

	/**
	 * Gets first index of given element if its present otherwise -1
	 */
	@Override public int indexOf(Object elem)
	{
		for (int i=0; i<size; i++)
			if (softEquals(_get(i), elem)) return i;

		return -1;
	}

	/**
	 * Gets last index of given element if its present otherwise -1
	 */
	@Override public int lastIndexOf(Object elem)
	{
		for (int i=size-1; i>=0; i--)
			if (softEquals(_get(i), elem)) return i;

		return -1;
	}

	/**
	 * 
	 */
	@Override public boolean remove(Object elem)
	{
		int index = indexOf(elem);
		if (index == -1) return false;
		
		_remove(index);
		return true;
	}

	/**
	 * 
	 */
	@Override public boolean contains(Object elem)
	{ return indexOf(elem) != -1; }

	/**
	 * Searchs for element from given collection
	 * 
	 * @param coll source collection
	 * 
	 * @return true if any element was present in this list
	 */
	//FIXME Use binary search
	public boolean containsAny(Collection<?> coll)
	{
	    for (Object elem: coll) if (contains(elem)) return true;
	    return false;
	}

	/**
	 * 
	 */
	@Override public boolean containsAll(Collection<?> coll)
	{
	    for (Object elem: coll) if (!contains(elem)) return false;
	    return true;
	}

	/**
	 * Removes all elements present in given collection
	 */
	@Override public boolean removeAll(Collection<?> coll)
	{
	    boolean modified = false;
	    
		for (int i=0; i<size; i++)
		{
			if (coll.contains(_get(i)))
			{
				_remove(i);
				i--;
				modified = true;
			}
		}
		return modified;
    }

	/**
	 * Removes all elements present in given list
	 * 
	 * @param coll source collection
	 * 
	 * @return true if removal was successful
	 */
    public boolean removeAll(LinkedList<?> coll)
    {
        boolean modified = false;
        
		for (int i=0; i<size; i++)
		{
			if (coll.contains(_get(i)))
			{
				_remove(i);
				i--;
				modified = true;
			}
		}
		return modified;
    }

    /**
     * 
     */
	@Override public boolean retainAll(Collection<?> coll)
	{
	    boolean modified = false;
	    
		for (int i=0; i<size; i++)
		{
			if (!coll.contains(_get(i)))
			{
				_remove(i);
				i--;
				modified = true;
			}
		}
		return modified;
    }

    /**
     * Gets array of elements in list
     */
	@Override public Object[] toArray()
	{
		Object[] array = new Object[size];
		_ToArray(array, 0, size);
        return array;
	}

	/**
	 * Gets array of elements in list
	 * 
	 * @param index offset
	 * @param len size
	 * 
	 * @return array of elements
	 */
	public Object[] toArray(int index, int len)
	{
		Object[] array = new Object[len];
		_ToArray(array, index, len);
        return array;
	}

	/**
	 * Fills given array with elements in list
	 */
	@SuppressWarnings("unchecked") @Override public <T> T[] toArray(T[] array)
	{
        if (array.length < size) array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        
        _ToArray(array, 0, size);
        
        if (array.length > size) array[size] = null;
        
        return array;
	}

	/**
	 * 
	 * @param array
	 * @param index
	 * @param len
	 */
	private <T> void _ToArray(T[] array, int index, int len)
	{
		int[] _index	= _index(index, index+len);
		int pos			= 0;
		
        for (int i=0; i<_index.length; i+=2)
        {
        	int num = _index[i+1] - _index[i];
        	System.arraycopy(values, _index[i], array, pos, num);
        	pos += num;
        }
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked") @Override public boolean addAll(Collection<? extends E> coll)
	{ return _addAll(-1, (E[]) toArray(coll)); }

	/**
	 * 
	 */
    @SuppressWarnings("unchecked") @Override public boolean addAll(int index, Collection<? extends E> coll)
    {
        checkIndexAdd(index);
        return _addAll(index, (E[]) toArray(coll));
    }

    /**
     * Inserts array of elements
     * 
     * @param elems source vector of elements
     * 
     * @return true if addition was successful
     */
	public boolean addAll(E... elems)
	{ return _addAll(-1, elems); }

	/**
	 * Inserts array of elements from index
	 * 
	 * @param index offset index
	 * @param elems source vector of elements
	 * 
	 * @return true if addition was successful
	 */
    public boolean addAll(int index, E... elems)
    {
        checkIndexAdd(index);
        return _addAll(index, elems);
    }

    /**
     * Inserts List of elements
     * 
     * @param list source list
     * 
     * @return true if addition was successful
     */
    public boolean addAll(LinkedList<? extends E> list)
    { return _addAll(-1, list); }

    /**
     * Inserts List of elements from index
     * 
     * @param index offset index
     * @param list source list
     * 
     * @return true if addition was successful
     */
	public boolean addAll(int index, LinkedList<? extends E> list)
	{
		checkIndexAdd(index);
		return _addAll(index, list);
	}

	private boolean _addAll(int index, LinkedList<? extends E> list)
	{
        ensureCapacity(size() + list.size());

		int size = list.size();
		
		if (size == 0) return false;
		
		for (int i=0; i<list.size(); i++)
		{
			_add(index, list._get(i));
            if (index != -1)
                index++;
		}
		return true;
	}

	private boolean _addAll(int index, E[] array)
	{
        ensureCapacity(size() + array.length);

		if (array.length == 0) return false;
		
		for (E elem: array)
		{
			_add(index, elem);
			if (index != -1)
			    index++;
		}
		return true;
	}

	/**
	 * 
	 */
	@Override public Iterator<E> iterator()
	{ return new Iter(true); }

	/**
	 * 
	 */
    @Override public ListIterator<E> listIterator()
    { return new ListIter(0); }

    /**
     * 
     */
    @Override public ListIterator<E> listIterator(int index)
    { return new ListIter(index); }


    /**
     * Gets element from head
     */
    @Override public E getFirst()
    { return _get(0); }

    /**
     * Gets element from tail
     */
    @Override public E getLast()
    { return _get(size-1); }

    /**
     * Insert element before head
     */
    @Override public void addFirst(E elem)
    { add(0, elem); }

    /**
     * Insert element to tail
     */
    @Override public void addLast(E elem)
    { add(elem); }

    /**
     * Removes first element
     */
    @Override public E removeFirst()
    { return _remove(0); }

    /**
     * Removes last element
     */
    @Override public E removeLast()
    { return _remove(size-1); }


    /**
     * 
     */
    @Override public E peek()
    { return (size == 0)?null:getFirst(); }

    /**
     * 
     */
    @Override public E element()
    { return getFirst(); }

    /**
     * 
     */
    @Override public E poll()
    { return (size == 0)?null:removeFirst(); }

    /**
     * 
     */
	@Override public E remove()
	{ return removeFirst(); }

	/**
	 * 
	 */
	@Override public boolean offer(E elem)
	{ return add(elem); }


	/**
	 * Queue iterator
	 */
	@Override public Iterator<E> descendingIterator()
	{ return new Iter(false); }

	/**
	 * insert first
	 */
	@Override public boolean offerFirst(E elem)
	{
        addFirst(elem);
        return true;
	}

	/**
	 * insert last
	 */
	@Override public boolean offerLast(E elem)
	{
        addLast(elem);
        return true;
	}

	/**
	 * Get first element
	 */
	@Override public E peekFirst()
	{ return (size == 0)?null:getFirst(); }

	/**
	 * Get last element
	 */
	@Override public E peekLast()
	{ return (size == 0)?null:getLast(); }

	/**
	 * Get & Remove first element
	 */
	@Override public E pollFirst()
	{ return (size == 0)?null:removeFirst(); }

	/**
	 * Get & Remove last element
	 */
	@Override public E pollLast()
	{ return (size == 0)?null:removeLast(); }

	/**
	 * Stack get
	 */
	@Override public E pop()
	{ return removeFirst(); }

	/**
	 * Stack add
	 */
	@Override public void push(E elem)
	{ addFirst(elem); }

	/**
	 * Removes first occurence of element
	 */
	@Override public boolean removeFirstOccurrence(Object elem)
	{
		int index = indexOf(elem);
		
		if (index == -1) return false;
		
		_remove(index);
		return true;
	}

	/**
	 * Removes last occurence of element
	 */
	@Override public boolean removeLastOccurrence(Object elem)
	{
		int index = lastIndexOf(elem);
		
		if (index == -1) return false;
		
		_remove(index);
		return true;
	}

	/**
	 * Serialization writer
	 * 
	 * @param oos stream
	 * 
	 * @throws IOException
	 */
    private void writeObject(ObjectOutputStream oos) throws IOException
    {
        oos.writeInt(size);
        for (int i=0; i<size; i++) oos.writeObject(_get(i));
    }

    /**
     * Serialization reader
     * 
     * @param ois stream
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
        size = ois.readInt();
        values = (E[]) new Object[size];

        for (int i=0; i<size; i++) values[i] = (E) ois.readObject();
    }

    /**
     * Gets element from index if its present in given length
     * 
     * @param index element index
     * @param len clip size
     * 
     * @return match
     */
    public LinkedList<E> get(int index, int len)
    {
        checkRange(index, len);

        LinkedList<E> list = new LinkedList<E>(len);
        
        for (int i=0; i<len; i++) list.add(_get(index+i));
        
        return list;
    }

    /**
     * Type safe convertion of list element into array
     * 
     * @param index offset index
     * @param len number to copy
     * 
     * @return type safe array
     */
    public E[] getArray(int index, int len)
    {
        checkRange(index, len);

        @SuppressWarnings("unchecked")
        E[] array = (E[]) new Object[len];
        
        for (int i=0; i<len; i++) array[i] = _get(index+i);
        
        return array;
    }
    
    public E[] getArray()
    {
        return getArray(0,size);
    }

    /**
     * 
     * @param index
     * @param list
     */
    public void setAll(int index, LinkedList<E> list)
    {
        checkRange(index, list.size());

        for (int i=0; i<list.size(); i++) _set(index+i, list.get(i));
    }

    /**
     * 
     * @param index
     * @param coll
     */
    public void setAll(int index, Collection<E> coll)
    {
        checkRange(index, coll.size());
        
        int i				= 0;
        Iterator<E> iter	= coll.iterator();
        
        while (iter.hasNext())
        {
            _set(index+i, iter.next());
            i++;
        }
    }

    /**
     * 
     * @param index
     * @param elems
     */
    public void setAll(int index, E... elems)
    {
        checkRange(index, elems.length);

        for (int i=0; i<elems.length; i++) _set(index+i, elems[i]);
    }

    /**
     * 
     * @param index
     * @param len
     */
	public void remove(int index, int len)
	{
    	checkRange(index, len);

		for (int i=index+len-1; i>=index; i--) _remove(i);
	}

	/**
	 * 
	 * @param len
	 * @param elem
	 */
	public void init(int len, E elem)
	{
	    checkLength(len);

	    int size = size();
        if (len < size)
        {
            remove(len, size-len);
            fill(0, len, elem);
        }
        else
        {
            fill(0, size, elem);
            for (int i=size; i<len; i++)
                add(elem);
        }
	}

	/**
	 * 
	 * @param len
	 * @param elem
	 */
	public void resize(int len, E elem)
	{
	    checkLength(len);

	    int size = size();
        if (len < size)
            remove(len, size-len);
        else
        {
            for (int i=size; i<len; i++)
                add(elem);
        }
	}

	/**
	 * 
	 * @param elem
	 */
    public void fill(E elem)
    {
        for (int i=0; i<size; i++)
            _set(i, elem);
    }

    /**
     * 
     * @param index
     * @param len
     * @param elem
     */
    public void fill(int index, int len, E elem)
    {
    	checkRange(index, len);

    	for (int i=0; i<len; i++)
    		_set(index+i, elem);
    }

    /**
     * 
     * @param srcIndex
     * @param dstIndex
     * @param len
     */
    public void copy(int srcIndex, int dstIndex, int len)
    {
    	checkRange(srcIndex, len);
    	checkRange(dstIndex, len);

    	if (srcIndex < dstIndex)		for (int i=len-1; i>=0; i--)	_reset(dstIndex+i, _get(srcIndex+i));
    	else if (srcIndex > dstIndex)	for (int i=0; i<len; i++)		_reset(dstIndex+i, _get(srcIndex+i));
    	
    }

    /**
     * 
     * @param srcIndex
     * @param dstIndex
     * @param len
     */
    public void move(int srcIndex, int dstIndex, int len)
    {
    	checkRange(srcIndex, len);
    	checkRange(dstIndex, len);

    	if (srcIndex < dstIndex)		for (int i=len-1; i>=0; i--)	_reset(dstIndex+i, _get(srcIndex+i));
    	else if (srcIndex > dstIndex)	for (int i=0; i<len; i++)		_reset(dstIndex+i, _get(srcIndex+i));
    	

    	if (srcIndex < dstIndex)
    	{
    		fill(srcIndex, Math.min(len, dstIndex-srcIndex), null);
    	}
    	else if (srcIndex > dstIndex)
    	{
    		int fill = Math.min(len, srcIndex-dstIndex);
    		fill(srcIndex+len-fill, fill, null);
    	}
    }

    /**
     * 
     */
    public void reverse()
    { reverse(0, size); }

    /**
     * 
     * @param index
     * @param len
     */
    public void reverse(int index, int len)
    {
    	checkRange(index, len);

    	int pos1	= index;
		int pos2	= index+len-1;
    	int mid		= len>>1;
    	
    	for (int i=0; i<mid; i++)
    	{
    		E swap	= _get(pos1);
    		swap	= _reset(pos2, swap);
    		
    		_reset(pos1, swap);
    		pos1++;
    		pos2--;
    	}
    }

    /**
     * 
     * @param index1
     * @param index2
     * @param len
     */
    public void swap(int index1, int index2, int len)
    {
    	checkRange(index1, len);
    	checkRange(index2, len);
    	
    	if ((index1 < index2 && index1+len > index2) || index1 > index2 && index2+len > index1)
    		throw new IllegalArgumentException("Swap ranges overlap");
    	

    	for (int i=0; i<len; i++)
    	{
    		E swap	= _get(index1+i);
    		swap	= _reset(index2+i, swap);
    		_reset(index1+i, swap);
    	}
    }

    /**
     * 
     * @param distance
     */
    public void rotate(int distance)
    { rotate(0, size, distance); }

    /**
     * 
     * @param index
     * @param len
     * @param distance
     */
    public void rotate(int index, int len, int distance)
    {
    	checkRange(index, len);

        distance = distance % size;
        if (distance < 0)	distance += size;
        if (distance == 0)	return;

        int num = 0;
        
        for (int start=0; num != size; start++)
        {
            E elem = _get(index+start);
            int i = start;
            do
            {
                i += distance;
                if (i >= len) i -= len;
                elem = _reset(index+i, elem);
                num++;
            } while (i != start);
        }
    }

    /**
     * Sorts values
     * 
     * @param comparator provider comparator
     */
    public void sort(Comparator<? super E> comparator)
    { sort(0, size, comparator); }

    /**
     * Sorts values
     * 
     * @param index offset
     * @param len size
     * @param comparator provider comparator
     */
    public void sort(int index, int len, Comparator<? super E> comparator)
    {
    	checkRange(index, len);
    	init(toArray(), false);
    	Arrays.sort(values, index, index+len, comparator);
    }

    /**
     * Performs Binary Search for value
     * 
     * @param key value
     * @param comparator provider comparator
     * 
     * @return match
     */
    public <K> int binarySearch(K key, Comparator<? super K> comparator)
    { return binarySearch(0, size, key, comparator); }

    /**
     * Performs Binary Search for value
     * 
     * @param index offset
     * @param len size
     * @param key value
     * @param comparator provider comparator
     * 
     * @return match
     */
    @SuppressWarnings("unchecked") public <K> int binarySearch(int index, int len, K key, Comparator<? super K> comparator)
    {
    	checkRange(index, len);
    	init(toArray(), false);
    	return Arrays.binarySearch((Object[]) values, index, index+len, key, (Comparator<Object>) comparator);
    }

    private void checkIndex(int index)
    { if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index: " + index + " (size: " + size + ")"); }

	private void checkIndexAdd(int index)
	{ if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index: " + index + " (size: " + size + ")"); }

	private void checkRange(int index, int len)
	{ if (index < 0 || len < 0 || index+len > size) throw new IndexOutOfBoundsException("Invalid range: " + index + "/" + len + " (size: " + size + ")"); }

    private void checkLength(int length)
    { if (length < 0) throw new IndexOutOfBoundsException("Invalid length: " + length); }

	// >-------[nested]---------------------------------------------------------------------------------------< //

    private class Iter implements Iterator<E>
    {
    	boolean forward;
    	int index;
    	int remove;

    	public Iter(boolean forward)
    	{
    		this.forward = forward;

    		if (forward)	index = 0;
    		else 			index = size-1;
    		
    		remove = -1;
    	}

		@Override public boolean hasNext()
		{
			if (forward)	return index != size();
			else 			return index != -1;
		}

		@Override public E next()
		{
			if (forward)
			{
				if (index >= size()) throw new NoSuchElementException();
			}
			else
			{
				if (index < 0) throw new NoSuchElementException();
			}
			
			E elem	= get(index);
			remove	= index;
			index	= (forward)?(index+1):(index-1);
			
			return elem;
		}

		@Override public void remove()
		{
			if (remove == -1) throw new IllegalStateException("No current element to remove");
			
			LinkedList.this.remove(remove);
			
			if (index > remove) index--;
			remove = -1;
		}
    }
    
    private static class ImmutableLinkedList<E> extends LinkedList<E>
    {
        private ImmutableLinkedList(LinkedList<E> that)			{ super(true, that); }
        @Override protected boolean _add(int index, E elem)		{ throw new UnsupportedOperationException("list is read-only"); }
        @Override protected E _set(int index, E elem)			{ throw new UnsupportedOperationException("list is read-only"); }
        @Override protected E _reset(int index, E elem)			{ throw new UnsupportedOperationException("list is read-only"); }
        @Override protected E _remove(int index)				{ throw new UnsupportedOperationException("list is read-only"); }
        @Override protected void _modify()						{ throw new UnsupportedOperationException("list is read-only"); }
    };

    private class ListIter implements ListIterator<E>
    {
    	int index;
    	int remove;

    	public ListIter(int index)
    	{
   			this.index = index;
    		this.remove = -1;
    	}

		@Override public boolean hasNext()
		{ return index < size(); }

		@Override public boolean hasPrevious()
		{ return index > 0; }

		@Override public E next()
		{
			if (index >= size()) throw new NoSuchElementException();
			
			E elem = LinkedList.this.get(index);
			remove = index;
			index++;
			return elem;
		}

		@Override public int nextIndex()
		{ return index; }

		@Override public E previous()
		{
			if (index <= 0) throw new NoSuchElementException();
			
			index--;
			E elem = LinkedList.this.get(index);
			remove = index;
			return elem;
		}

		@Override public int previousIndex()
		{ return index-1; }

		@Override public void remove()
		{
			if (remove == -1) throw new IllegalStateException("No current element to remove");
			
			LinkedList.this.remove(remove);
			
			if (index > remove) index--;
			
			remove = -1;
		}

		@Override public void set(E e)
		{
			if (remove == -1) throw new IllegalStateException("No current element to set");
			LinkedList.this.set(remove, e);
		}

		@Override public void add(E e)
		{ LinkedList.this.add(index, e); }
    }
    
}