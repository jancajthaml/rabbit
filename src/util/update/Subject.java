package util.update;

import struct.Queue;

/**
 * This class represent property that can be linked with Watcher object and bridge its updates
 * 
 * @author Jan Cajthaml
 *
 * @param <ELEMENT>
 */
public class Subject<ELEMENT>
{
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private Queue<Watcher> observers	= new Queue<Watcher>();
	private ELEMENT state				= null;
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Creates new Subject
	 */
	public Subject()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * 
	 * @param o target object to attach to
	 */
	public void attach( Watcher o )
	{ Subject.this.observers.enqueue(o); }
	
	/**
	 * 
	 * @return element itself
	 */
	public ELEMENT getState()
	{ return Subject.this.state; }
	
	/**
	 * cascade update
	 */
	public void refresh()
	{ for(Watcher w : Subject.this.observers) w.update(); }
	
	/**
	 * Change property and update
	 * 
	 * @param in propety value
	 */
	public void setState(ELEMENT in)
	{
		Subject.this.state = in;
		Subject.this.refresh();
	}
	
	/**
	 * uinstall link with target object
	 * 
	 * @param o target attached object
	 */
	public void detach(Watcher o)
	{ Subject.this.observers.remove(o); }
	
}