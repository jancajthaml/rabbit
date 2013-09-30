package thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for execution series of Runnables one by one safely
 * 
 * @author Jan Cajthaml
 *
 */
public class SerialExecutor
{
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //

	private SerialExecutor()
	{}
	
    // >-------[methods]---------------------------------------------------------------------------------------< //
    
	/**
	 * Creates new Cached Thread Pool
	 * 
	 * @return pool
	 */
    public static ExecutorService newCachedThreadPool()
    { return new ThreadPoolExecutor(60, 200, 60000, TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(20)); }

    /**
     * Executes set of Runnables sequentionaly
     * 
     * @param runnables set of Runnables
     */
	public static void execute(Runnable ... runnables)
	{
    	final BoundedSemaphore semaphore = new BoundedSemaphore(1);

    	for (int i=0; i<runnables.length; i++)
    	{
			try								{ semaphore.take();		}
			catch (InterruptedException e)	{ e.printStackTrace();	}

    		new Thread(runnables[i])
    		{
    			public void run()
    			{
    				try { synchronized(semaphore) { super.run(); } }
    				finally
    				{
    					try								{ semaphore.release();	}
    					catch (InterruptedException e)	{ e.printStackTrace();	}
    				}    				
    			}
    		}.start();
    	}
	}

	// >-------[nested]---------------------------------------------------------------------------------------< //

	private static class BoundedSemaphore
	{
		private int signals = 0;
		private int bound   = 0;

		public BoundedSemaphore(int upperBound)
		{ this.bound = upperBound; }

		/**
		 * 
		 * @throws InterruptedException
		 */
		public synchronized void take() throws InterruptedException
		{
			while(this.signals == bound) wait();
			this.signals++;
			this.notify();
		}

		/**
		 * 
		 * @throws InterruptedException
		 */
		public synchronized void release() throws InterruptedException
		{
			while(this.signals == 0) wait();
			this.signals--;
			this.notify();
		}
	}
	
}