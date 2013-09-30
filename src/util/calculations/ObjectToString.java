package util.calculations;

import java.util.Arrays;

/**
 * Utility class for Object String formatting
 *
 * @author Jan Cajthaml
 */
//FIXME need more object integration
public class ObjectToString
{

	// >-------[methods]---------------------------------------------------------------------------------------< //

	/**
	 * Array to string
	 * 
	 * @param o source object
	 * 
	 * @return formatted string
	 */
	public static String arrayToString(Object[] o)
	{ return Arrays.toString(o); }
	
	/**
	 * Generic Array to string
	 * 
	 * @param o source object
	 * 
	 * @return formatted string
	 */
	public static String genericArrayToString(Object[] o)
	{ return "<"+genericToString(o[0])+">"+arrayToString(o); }
	
	/**
	 * Generic Type to string
	 * 
	 * @param o source object
	 * 
	 * @return formatted string
	 */
	public static String genericToString(Object o)
	{ return o.getClass().getSimpleName(); }

}
