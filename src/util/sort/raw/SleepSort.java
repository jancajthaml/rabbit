package util.sort.raw;

import java.util.concurrent.CountDownLatch;

/**
 * Sleep Sort algorithm (experimental sudo-bucked sort algorithm)
 *
 * @author Jan Cajthaml
 */
public class SleepSort
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private static CountDownLatch doneSignal	= null;

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private SleepSort() {}

	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * sort given collection
	 *  
	 * @param elements collection of integers
	 */
	public static void sort(int ... elements)
	{
		for (final int element : elements)
		{
			new thread.Task()
			{
				@Override public void callback() { System.out.print(element+"  "); }
				@Override public boolean next()
				{
					doneSignal.countDown();
					try { doneSignal.await(); }
					catch (InterruptedException e) {}
					pause(element);
					return true;
				}
				
			}.start();
		}
	}

}