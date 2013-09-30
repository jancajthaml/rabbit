package util;

import java.awt.Component;

/**
 * Target Platform Utility class
 * 
 * @author Jan Cajthaml
 *
 */
public class SystemUtils
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private static String dot											= new java.io.File("").getAbsolutePath();
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	private SystemUtils()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
    /**
     * Gets Current java version
     * 
     * @return Java version
     */
    public static String getJavaVersion()
    { return System.getProperty("java.version"); }

    /**
     * Gets Target platform
     * 
     * @return Version of target platform
     */
    public static String getOsVersion()
    { return System.getProperty("os.version"); }

    /**
     * Checks if OS is Mac OSX
     * 
     * @return true if target platform is Mac OSX
     */
    public static boolean isMac()
    { return System.getProperty("os.name").startsWith("Mac OS"); }

	/**
	 * Checks if OS is MS Windows
	 * 
	 * @return true if target platform is MS Widnows
	 */
	public static boolean isWin()
	{ return System.getProperty("os.name").startsWith("Win"); }
	
    /**
     * Provides audio feedback for error action 
     * 
     * @param component target
     */
	public static void errorFeedback(Component component)
	{ javax.swing.UIManager.getLookAndFeel().provideErrorFeedback(component); }

	/**
	 * Checks if VM is running on old Java Platform
	 * 
	 * @return true if Java is old (v. 1.4)
	 */
	public static boolean isOldJava()
	{ return "1.4".equals(System.getProperty("java.specification.version")); }

	/**
	 * Gets "/." path
	 * 
	 * @return Local dot path
	 */
	public static String getRoot()
	{ return dot; }
    
}