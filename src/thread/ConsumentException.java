package thread;

/**
 * 2nd step Exception for Task
 *
 * @author Jan Cajthaml
 */
@SuppressWarnings("serial")
public class ConsumentException extends Exception
{
	
	/**
	 * Creates empty Consument Exception
	 */
	public ConsumentException()
	{}
	
	/**
	 * Creates Consument Exception with message
	 * 
	 * @param msg Exception message
	 */
	public ConsumentException(String msg)
	{ super(msg); }
	
	/**
	 * Creates Consument Exception with message and throwable
	 * 
	 * @param msg Exception message
	 * @param t Exception throwable
	 */
	public ConsumentException(String msg, Throwable t)
	{ super(msg,t); }
	
	/**
	 * Creates Consument Exception with throwable
	 * 
	 * @param t Exception throwable
	 */
	public ConsumentException(Throwable t)
	{ super(t); }
	
}
