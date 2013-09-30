package thread;

import java.util.concurrent.Future;
import lang.Destructable;
import static thread.ThreadBase.executor;

/**
 * Pausable/Resumable Memory safe Thread implementation
 * 
 * @author Jan Cajthaml
 *
 */
public abstract class Task implements Destructable
{
	
	// >-------[abstract]---------------------------------------------------------------------------------------< //

	/**
	 * Task feedback
	 */
	public abstract void callback();
	
	/**
	 * Task work
	 * 
	 * @return true if Task should be killed after this work
	 */
	public abstract boolean next();

	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private volatile boolean kill		= false;
	private volatile boolean next		= false;
	private Future<?> publisher			= null;
	private Future<?> timer				= null;
	private volatile boolean suspend	= false;
    private final Object lock			= new Object();
	private PausableTask task			= new PausableTask()
	{
		@Override public void run()
		{
			try
			{
				while(!kill)
				{
					while (!suspend)
					{
						notifyNext();
						notifyCallback();
					}
					synchronized (lock)
					{
						try { lock.wait(); }
						catch (InterruptedException e)
						{
							Thread.currentThread().interrupt();
							return;
						}
					}
				}
			}
			catch (ProducentException e)	{ /*e.printStackTrace();*/	}
			catch (ConsumentException e)	{ /*e.printStackTrace();*/	}
		}
	};

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Sets Time-To-Live value for this Task in ms
	 * 
	 * @param timeMS TTL value
	 */
	public void setTimeToLive(long timeMS)
	{ task.ttl = timeMS; }
	
	/**
	 * Pauses this Task in mid-work for given time 
	 * 
	 * @param time how long to wait
	 */
	public void pause(int time)
	{
		try								{ Thread.sleep(time);		}
		catch (InterruptedException e)	{ /* Ignore Exception */	}
	}

	/**
	 * Pauses this Task after work step
	 */
	public void pause()
	{ suspend = true; }

	/**
	 * Resumes this Task
	 */
	public void resume()
	{
		suspend = false;
		synchronized (lock)
		{ lock.notifyAll(); }
	}
    
	/**
	 * destroys this Task and unlinks all references 
	 */
	public void kill()
	{ $(); }
	
	/**
	 * Checks whenever Task is Alive or has been Killed
	 * 
	 * @return true if Task has not been killed
	 */
	public boolean isAlive()
	{ return !kill;	}
	
	/**
	 * Starts this Task
	 */
	public void start()
	{ task.start();	}

	/**
	 * 
	 */
	public boolean $()
	{
		kill = true;

		stop();
		publisher	= null;
		task=null;
		//Bez poolu + task.join() + task Thread 
		
		try					{ super.finalize();		}
		catch (Throwable e)	{ e.printStackTrace();	}
		System.gc();
		return true;
	}

	private void stop()
	{
		try
		{
			publisher.cancel(true);
			if(timer!=null) timer.cancel(true);
		}
		catch(NullPointerException e){}
	}

	private synchronized void notifyNext() throws ProducentException
	{
		while(next)
			try								{ wait();	}
			catch(InterruptedException e)	{			}
		
		next	= true;
		kill	= kill || next();
		
		notifyAll();
		if(!isAlive()) throw new ProducentException("finished: ");
	}
		
	private synchronized void notifyCallback() throws ConsumentException
	{
		if(!isAlive()) 			throw new ConsumentException();
		while(!next)			try								{ wait();	}
								catch(InterruptedException e)	{			}
		
		next = false;
		
		callback();
		if(!kill)
		{
			notifyAll();
			return;
		}
		throw new ConsumentException();
	}
	
	// >-------[nested]---------------------------------------------------------------------------------------< //

	private abstract class PausableTask implements Runnable
	{
		protected long ttl = Long.MAX_VALUE;
	
		public void start()
		{
			if(ttl!=Long.MAX_VALUE) timer = executor.submit(new Runnable()
			{
				public void run()
				{
					try								{ Thread.sleep(ttl);	}
					catch (InterruptedException e)	{						}
						
					if(isAlive()) stop();
				}
			});
			publisher = executor.submit(this);
		}
	}
	
}