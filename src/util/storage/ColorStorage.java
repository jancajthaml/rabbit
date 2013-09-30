package util.storage;

import io.file.SmartFile;
import java.awt.Color;
import java.awt.Image;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import struct.Queue;
import util.update.Watcher;

/**
 * Color Utility class
 * 
 * @author Jan Cajthaml
 *
 */
public class ColorStorage
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	private static Queue<ColorTheme> skins	= new Queue<ColorTheme>();
	private static ColorTheme current		= null;
	public static boolean HOLD_INIT			= false;
	//FIXME use SmartFile to determine location instead
	private static String path				= new File("").getAbsolutePath();
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * Utility class private ctor
	 */
	private ColorStorage() {}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * links object with color
	 * 
	 * @param name color name
	 * @param watcher target object
	 */
	public static void attach(String name, Watcher watcher)
	{ for(ColorTheme skin : skins) skin.attach(name,watcher); }
	
	/**
	 * Icon of current theme
	 * 
	 * @param name Icon name
	 * @return theme Icon
	 */
	public static Image getIcon(String name)
	{ return current.getIcon(name); }
	
	/**
	 * Color of current theme
	 *  
	 * @param name Color name
	 * @return theme Color
	 */
	public static Color getColor(String name)
	{ return getColorWrapper(name).getColor(); }
	
	/**
	 * Paint of current theme
	 * 
	 * @param name Paint name
	 * @return theme Paint
	 */
	public static Paint getPaint(String name)
	{ return getPaintWrapper(name).getPaint(); }
	
	/**
	 * DynamicColor of current theme
	 * 
	 * @param name DynamicColor name
	 * @return theme DynamicColor
	 */
	public static DynamicColor getColorWrapper(String name)
	{ return current.getColor(name); }
	
	/**
	 * DynamicPaint of current theme
	 * 
	 * @param name DynamicPaint name
	 * @return theme DynamicPaint
	 */
	public static DynamicPaint getPaintWrapper(String name)
	{ return current.getPaint(name); }
	
	/**
	 * Sets Color of current theme
	 * 
	 * @param name Color name
	 * @param c Color value
	 */
	public static void setColor(String name, Color c)
	{ current.setColor(name,c); }
	
	//FIXME DELETE
	private static void fixColor(String name, Color c)
	{
		for(ColorTheme theme : skins) theme.setColor(name,c);
	}

	/**
	 * Theme specific boolean property
	 * 
	 * @param name property name
	 * @return property value
	 */
	public static boolean getBool(String name)
	{ return current.getBool(name); }
	
	/**
	 * Get list of awailable themes
	 * 
	 * @return themes list
	 */
	public static struct.Queue<ColorTheme> getSkinList()
	{ return skins; }

	/**
	 * Change current skin
	 * 
	 * @param skinName name of the Theme
	 */
	public static void changeSkin(String skinName)
	{
		System.out.println("changing skin to "+skinName);
		for(ColorTheme theme : skins)
		{
			
			if(theme.toString().equalsIgnoreCase(skinName))
			{
				ColorStorage.current=theme;
				ColorStorage.current.cascadeUpdate();
				break;
			}
		}
		PrefferenceStorage.STYLESHEET=skinName;
	}

	/**
	 * get Icon by its specific name (not filename)
	 * 
	 * @param name Icon name
	 * @return Icon
	 */
	public static BufferedImage getSharedIcon(String name)
	{ return ColorTheme.getShaderIcon(name); }

	/**
	 * load and initialize Themes
	 */
	public static void init()
	{
		ColorTheme.NILC.setColor(Color.yellow);
		ColorTheme.NILP.setColor(Color.yellow);
		
		String fileName=path+"/../data/themes/";
		
		for(File f : new File(fileName).listFiles())
		{
			if(!f.isHidden() && SmartFile.hasExtension(f.getName(), "xml"))
			{
				String name = SmartFile.cutExtension(f.getName());
				System.err.println("ext cut -- "+f.getName()+">"+name);
				skins.enqueue(new ColorTheme(name));
			}
		}
	
		for(ColorTheme skin : skins)
		{
			current = skin;
			Color c = ColorStorage.getColor("splash.background");
			ColorStorage.setColor("splash.background", new Color(c.getRed(),c.getGreen(),c.getBlue(),150));

			c = ColorStorage.getColor("tree.file.blank.foreground.shadow");
			ColorStorage.setColor("tree.file.blank.foreground.shadow", new Color(c.getRed(),c.getGreen(),c.getBlue(),150));

			c = ColorStorage.getColor("tree.file.selected.foreground.shadow");
			ColorStorage.setColor("tree.file.selected.foreground.shadow", new Color(c.getRed(),c.getGreen(),c.getBlue(),80));

			c = ColorStorage.getColor("tree.file.cathegory.shadow.foreground");
			ColorStorage.setColor("tree.file.cathegory.shadow.foreground", new Color(c.getRed(),c.getGreen(),c.getBlue(),255));
			
			c = ColorStorage.getColor("selection.background");
			ColorStorage.setColor("selection.background", new Color(c.getRed(),c.getGreen(),c.getBlue(),50));
		}
		
		ColorStorage.fixColor("transparent", new Color(0,0,0,0));
		
		for(ColorTheme skin : skins) if(!skin.toString().equalsIgnoreCase(""))
		{
			current = skin;
			break;
		}
		
	}
	
	/**
	 * delete all images used in Splash Screen
	 */
	public static void disposeSplash()
	{ ColorTheme.disposeSplash(); }
	
	/**
	 * Gets currently active Theme name
	 * 
	 * @return active theme name
	 */
	public static String getCurrentThemeName()
	{ return current.toString().split("\\.")[0]; }
	
	static
	{
		if(!HOLD_INIT)
		{
			init();
		}
	}
}