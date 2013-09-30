package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Recycling Thread Pool for ALL THREADS used in Application
 * 
 * @author Jan Cajthaml
 *
 */
public class ThreadBase
{

	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	/**
	 * Cached Thread pool
	 */
	public static final ExecutorService executor = Executors.newCachedThreadPool();

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	protected ThreadBase()
	{}

}
