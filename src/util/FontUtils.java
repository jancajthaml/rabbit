package util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

/**
 * Utility class for working with Fonts
 * 
 * @author Jan Cajthaml
 * 
 */
public class FontUtils
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	/**
	 * Default font size
	 */
	public static final int DEFAULT_FONT_SIZE	= 13;
	
	/**
	 * Default monospaced font 
	 */
    public static final Font DEFAULT_FONT		= new Font(getMonospacedFontName(), Font.PLAIN, DEFAULT_FONT_SIZE);
    
    /**
     * Default label font
     */
    public static final Font BADGE_FONT			= new Font(getLabelFontName(), Font.BOLD, 11);

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	private FontUtils()
	{}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //

    /**
     * Gets Monospaced font name awailable on current running os
     * 
     * @return monospaced font name
     */
	public static String getMonospacedFontName()
	{
		String name				= null;
		GraphicsEnvironment g	= GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] names			= g.getAvailableFontFamilyNames();
		Arrays.sort(names);
		if (SystemUtils.isMac() && Arrays.binarySearch(names, "Tahoma")>=0)		name = "Tahoma";
		else if (Arrays.binarySearch(names, "Courier New")>=0)					name = "Courier New";
		else if (Arrays.binarySearch(names, "Courier")>=0)						name = "Courier";			
		else if (Arrays.binarySearch(names, "Nimbus Mono L")>=0)				name = "Nimbus Mono L";
		else if (Arrays.binarySearch(names, "Lucida Sans Typewriter")>=0)		name = "Lucida Sans Typewriter";
		else if (Arrays.binarySearch(names, "Bitstream Vera Sans Mono")>=0)		name = "Bitstream Vera Sans Mono";
		return (name==null)?"Terminal":name;
	}

	/**
	 * Gets Label font name awaylable on current running os
	 * 
	 * @return label font name 
	 */
    private static String getLabelFontName()
    {
		GraphicsEnvironment g	= GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] names			= g.getAvailableFontFamilyNames();
		Arrays.sort(names);
		if (SystemUtils.isMac() && Arrays.binarySearch(names, "Helvetica Neue")>=0)	return "Helvetica Neue";
		else if (Arrays.binarySearch(names, "Helvetica")>=0)						return "Helvetica";
		else if (Arrays.binarySearch(names, "Arial")>=0)							return "Arial";
		return getMonospacedFontName();
	}
	
}