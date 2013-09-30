package thread;

/**
 * 1st step Exception for Task
 *  
 * @author Jan Cajthaml
 *
 */
@SuppressWarnings("serial")
public class ProducentException extends Exception
{

	/**
	 * Creates empty ProducentException Exception
	 */
	public ProducentException()
	{}
	
	/**
	 * Creates ProducentException Exception with message
	 * 
	 * @param msg Exception message
	 */
	public ProducentException(String msg)
	{ super(msg); }
	
	/**
	 * Creates ProducentException Exception with message and throwable
	 * 
	 * @param msg Exception message
	 * @param t Exception throwable
	 */
	public ProducentException(String msg, Throwable t)
	{ super(msg,t); }
	
	/**
	 * Creates ProducentException Exception with throwable
	 * 
	 * @param t Exception throwable
	 */
	public ProducentException(Throwable t)
	{ super(t); }
		
}