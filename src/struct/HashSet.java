package struct;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Direct HashSet without need of HashMap
 *
 * @author Jan Cajthaml
 */
//UNCHECKED
@SuppressWarnings("unchecked")
public class HashSet<ELEMENT extends Object> extends java.util.AbstractSet<ELEMENT>
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	protected Object NIL			= new Object();
	protected Object DELETED		= new Object();
	protected int elements			= 0;
	protected int freecells			= 0;
	protected int mods				= 0;
	protected ELEMENT[] buffer		= null;

	// >-------[ctors]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new HashSet with size 3
	 */
	public HashSet()
	{ this(3); }
  
	/**
	 * Creates new HashSet with given size
	 * 
	 * @param size size of HashSet
	 */
	public HashSet(int size)
	{
		buffer		= (ELEMENT[])new Object[(size==0 ? 1 : size)]; 
		elements	= 0;
		freecells	= buffer.length;
		mods		= 0;
	}

	/**
	 * Creates new HashSet from Collection
	 * 
	 * @param c source Collection
	 */
	public HashSet(Collection<ELEMENT> c)
	{
		this(c.size());
		addAll(c);
	}

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * iterator
	 */
	public Iterator<ELEMENT> iterator()
	{ return new CompactHashIterator<ELEMENT>(); }

	/**
	 * number of not null elements
	 */
	public int size()
	{ return elements; }
	
	/**
	 * is HashSet empty?
	 */
	public boolean isEmpty()
	{ return elements == 0; }

	/**
	 * is element present in HashSet
	 */
	public boolean contains(Object o)
	{
		if (o == null) return false;
		
		int hash	= o.hashCode();
		int index	= (hash & 0x7FFFFFFF) % buffer.length;
		int offset	= 1;
		
		while(buffer[index] != null && !(buffer[index].hashCode() == hash && buffer[index].equals(o)))
		{
			index	= ((index + offset) & 0x7FFFFFFF) % buffer.length;
			offset	= (offset<<1) + 1;
			
			if (offset == -1)	offset = 2;
		}
		return buffer[index] != null;
	}

	/**
	 * add element to HashSet
	 */
	public boolean add(ELEMENT o)
	{
		if (o == null) return false;
		
		int hash		= o.hashCode();
		int index		= (hash & 0x7FFFFFFF) % buffer.length;
		int offset		= 1;
		int deletedix	= -1;
    
		while(buffer[index] != null && !(buffer[index].hashCode() == hash && buffer[index].equals(o)))
		{
			if (buffer[index] == DELETED)	deletedix = index;
			
			index	= ((index + offset) & 0x7FFFFFFF) % buffer.length;
			offset	= offset*2 + 1;
			
			if (offset == -1)	offset = 2;
		}
		if (buffer[index] == null)
		{
			if (deletedix != -1)	index = deletedix;
			else					freecells--;
			mods++;
			elements++;
			buffer[index] = o;
			if (1 - (freecells / (double) buffer.length) > 0.75)
			{
				rehash(buffer.length);
				if (1 - (freecells / (double) buffer.length) > 0.75)
					rehash(buffer.length*2 + 1);
			}
			return true;
		}
		return false;
	}

	/**
	 * remove element from HashSet
	 */
	public boolean remove(Object o)
	{
		if (o == null) o = NIL;
		
		int hash	= o.hashCode();
		int index	= (hash & 0x7FFFFFFF) % buffer.length;
		int offset	= 1;
    
		while(buffer[index] != null && !(buffer[index].hashCode() == hash && buffer[index].equals(o)))
		{
			
			index	= ((index + offset) & 0x7FFFFFFF) % buffer.length;
			offset	= (offset<<1) + 1;
			
			if (offset == -1)	offset = 2;
		}
		if (buffer[index] != null)
		{
			buffer[index] = (ELEMENT) DELETED;
			mods++;
			elements--;
			return true;
		}
		return false;
	}
  
	/**
	 * delete all elements in HashSet
	 */
	public void clear()
	{
		elements = 0;
		
		for (int ix = 0; ix < buffer.length; ix++) buffer[ix] = null;
		
		freecells = buffer.length;
		mods++;
	}

	/**
	 * Return array of elements in HashSet
	 */
	public Object[] toArray()
	{
		Object[] result = new Object[elements];
		Object[] buffer = this.buffer;
		int pos			= 0;
		
		for (int i = 0; i < buffer.length; i++)
			if (buffer[i] != null && buffer[i] != DELETED)
				result[pos++] = (buffer[i] == NIL)?null:buffer[i];
		
		return result;
	}

	/**
	 * Fills target array with elements in HashSet
	 */
	public Object[] toArray(Object elems[])
	{
		int size = elements;
		if (elems.length < size)	elems = (Object[])java.lang.reflect.Array.newInstance(elems.getClass().getComponentType(), size);
		
		Object[] buffer	= this.buffer;
		int pos			= 0;
		
		for (int i = 0; i < buffer.length; i++)
			if (buffer[i] != null && buffer[i] != DELETED)
				elems[pos++] = (buffer[i] == NIL)?null:buffer[i];
		
		return elems;
	}
  
	/**
	 * Recalculate hash position of elements in HashSet with to given capacity
	 * 
	 * @param newCapacity capacity
	 */
	protected void rehash(int newCapacity)
	{
		int oldCapacity			= buffer.length;
		ELEMENT[] newObjects	= (ELEMENT[])new Object[newCapacity];

		for (int i = 0; i < oldCapacity; i++)
		{
			ELEMENT o = buffer[i];
			if (o == null || o == DELETED)	continue;
      
			int hash	= o.hashCode();
			int index	= (hash & 0x7FFFFFFF) % newCapacity;
			int offset	= 1;

			while(newObjects[index] != null)
			{
				index	= ((index + offset) & 0x7FFFFFFF) % newCapacity;
				offset	= (offset<<1) + 1;
				
				if (offset == -1)	offset = 2;
			}
			newObjects[index] = o;
		}
		
		buffer		= newObjects;
		freecells	= buffer.length - elements;
	}
  
	// >-------[nested]---------------------------------------------------------------------------------------< //

	/**
	 * 
	 * @author Jan Cajthaml
	 *
	 * @param <T>
	 */
	//RENAME
	private class CompactHashIterator<T extends ELEMENT> implements Iterator<T>
	{

		private int index;
		private int lastReturned = -1;
		private int expectedmods;

		/**
		 * 
		 */
		public CompactHashIterator()
		{
			for (index = 0; index < buffer.length && (buffer[index] == null || buffer[index] == DELETED); index++);
			expectedmods = mods;
		}

		/**
		 * 
		 */
		public boolean hasNext()
		{ return index < buffer.length; }

		/**
		 * 
		 */
		public T next()
		{
			if (mods != expectedmods)	throw new ConcurrentModificationException();
			
			int length = buffer.length;
			
			if (index >= length)
			{
				lastReturned = -2;
				throw new NoSuchElementException();
			}
			
			lastReturned = index;
			
			for (index += 1; index < length && (buffer[index] == null || buffer[index] == DELETED); index++);
			return (T) ((buffer[lastReturned] == NIL)?null:buffer[lastReturned]);
		}

		/**
		 * 
		 */
		public void remove()
		{
			if (mods != expectedmods)						throw new ConcurrentModificationException();
			if (lastReturned == -1 || lastReturned == -2)	throw new IllegalStateException();
			
			if (buffer[lastReturned] != null && buffer[lastReturned] != DELETED)
			{
				buffer[lastReturned] = (ELEMENT) DELETED;
				elements--;
				mods++;
				expectedmods = mods;
			}
		}
	}
	
}