package util.memory;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Resource Pool
 * 
 * @author Jan Cajthaml
 *
 * @param <ELEMENT> type locked Resource
 */
public abstract class ObjectPool<ELEMENT>
{

	// >-------[abstract]---------------------------------------------------------------------------------------< //
	
	protected abstract ELEMENT create();
	public abstract boolean validate(ELEMENT o);
	public abstract void expire(ELEMENT o);

	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	//FIXME AtomicLong
	private long expirationTime					= 3L;
	private Hashtable<ELEMENT, Long> locked		= new Hashtable<ELEMENT, Long>();
	private Hashtable<ELEMENT, Long> unlocked	= new Hashtable<ELEMENT, Long>();

	// >-------[ctors]---------------------------------------------------------------------------------------< //
	
	public ObjectPool() {}

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * retrieve Object from pool
	 * 
	 * @return Object recycled resource from pool
	 */
	public synchronized ELEMENT checkOut()
	{
		long now	= System.currentTimeMillis();
		ELEMENT t	= null;
		
		if (unlocked.size() > 0)
		{
			Enumeration<ELEMENT> e = unlocked.keys();
			while (e.hasMoreElements())
			{
				t = e.nextElement();
				if ((now - unlocked.get(t)) > expirationTime)
				{
					unlocked.remove(t);
					expire(t);
					t = null;
				}
				else
				{
					if (validate(t))
					{
						unlocked.remove(t);
						locked.put(t, now);
						return (t);
					}
					else
					{
						unlocked.remove(t);
						expire(t);
						t = null;
					}
				}
			}
		}
		
		t = create();
		
		locked.put(t, now);
		return t;
	}

	/**
	 * Puts Object into pool
	 * 
	 * @param object Object to recycle in future
	 */
	public synchronized void checkIn(ELEMENT object)
	{
		locked.remove(object);
		unlocked.put(object, System.currentTimeMillis());
	}
	
}