package util.memory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Simplistic Memory Cleaner
 * Dependent on Java Heap
 * Will be rethought in future
 * 
 * @author Jan Cajthaml
 *
 */
public class MemoryCleaner
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private static Runtime runtime		= Runtime.getRuntime();
    ScheduledThreadPoolExecutor stpe	= new ScheduledThreadPoolExecutor(1);
	private static Thread cleaner		= new Thread()
	{
		public void run()
		{
			for(;;)
			{
				try								{ Thread.sleep(4000);	}
				catch (InterruptedException e)	{						}
				MemoryCleaner.runtime.gc();
			}
		}
	};
	
	
	// >-------[post-process]---------------------------------------------------------------------------------------< //
	
	static
	{
		MemoryCleaner.runtime.traceInstructions(false);
		MemoryCleaner.runtime.traceMethodCalls(false);
	}

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	public MemoryCleaner() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Prints Heap state
	 */
	public static void dump()
	{
		
		ThreadMXBean bean	= ManagementFactory.getThreadMXBean( );
		System.out.println("total memory:\t\t"+runtime.maxMemory());
		System.out.println("alocated memory:\t\t"+runtime.totalMemory());
		System.out.println("free memory:\t\t"+runtime.freeMemory());
		System.out.println("total started thread:\t"+bean.getTotalStartedThreadCount());
		System.out.println("total alive thread:\t"+bean.getThreadCount()+" "+Thread.activeCount());
		System.out.println("total daemon thread:\t"+bean.getDaemonThreadCount());	
	}
	
	/**
	 * Prints all Threads trace
	 */
	public static void traceAllThreads()
	{
		ThreadMXBean bean	= ManagementFactory.getThreadMXBean( );
		for(ThreadInfo info : bean.dumpAllThreads(true, true))
		System.out.println("\t"+info);
		for(long id : bean.getAllThreadIds())
			System.out.println("\t>>> "+id);
	}
	
	/**
	 * Initialize cleaner thread
	 */
	public static void init()
	{ cleaner.start(); }

}