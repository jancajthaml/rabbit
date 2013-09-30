package thread;

import lang.Destructable;

/**
 * WatchDog Runnable implementation
 *
 * @author Jan Cajthaml
 */
public abstract class WatchDog implements Destructable
{
	
	// >-------[abstract]---------------------------------------------------------------------------------------< //

	/**
	 * Task to Execute after watchdog wasn't killed
	 */
	public abstract void exec();

	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private float seed			= 1.0f/1000;
	private float percent		= 0;
	private boolean kill		= false;
	private thread.Task process	= new thread.Task()
	{
		@Override public void callback()
		{}
		
		@Override public boolean next()
		{
			if(WatchDog.this.percent<1.0f) WatchDog.this.percent=Math.min(1.0f, (WatchDog.this.percent+WatchDog.this.seed));
			else
			{
				exec();
				pause();
			}			
			pause(1);
			return kill;
		}
	};
	
	// >-------[ctors]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new WatchDog with 100ms death rate
	 */
	public WatchDog()
	{
		WatchDog.this.process.start();
		WatchDog.this.process.pause();
	}

	/**
	 * Creates new WatchDog with "duration" ms death rate
	 */
	public WatchDog(int duration)
	{
		this.setDuration(duration);
		WatchDog.this.process.start();
		WatchDog.this.process.pause();
	}

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Start WatchDog work
	 */
	public void start()
	{
		reset();
		WatchDog.this.process.resume();
	}
	
	/**
	 * Tick
	 */
	public void rollback()
	{
		reset();
		WatchDog.this.process.pause();
		WatchDog.this.process.resume();
	}
	
	/**
	 * Set duration before executing Watch Dog work
	 * 
	 * @param time value in ms
	 */
	public void setDuration(int time)
	{ WatchDog.this.seed=1.0f/time;	}
	
	/**
	 * rollback
	 */
	private void reset()
	{ WatchDog.this.percent=0; }

	/**
	 * stop Watch Dog work 
	 */
	public void stop()
	{
		reset();
		WatchDog.this.process.pause();
	}
	
	/**
	 * describe destructor work
	 */
	public boolean $()
	{
		kill	=	true;
		process.kill();
		process	=	null;
		try					{ super.finalize();		}
		catch (Throwable e) { e.printStackTrace();	}
		System.gc();
		return true;
	}

}