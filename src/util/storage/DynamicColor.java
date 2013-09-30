package util.storage;

import java.awt.Color;
import util.update.Subject;
import util.update.Watcher;

/**
 * This class represents Linkable Color that does not need global actualisation.
 * DynamicColor can be linked via attach() method to any Watcher
 * 
 * @author Jan Cajthaml
 *
 */
public class DynamicColor
{

	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private Color value				= Color.red;
	protected String image			= "";
	private Subject<Color> subject	= new Subject<Color>(); 	

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * 
	 * @param name Color name
	 */
	protected DynamicColor(String name)
	{ DynamicColor.this.image = name; }
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Gets Color value
	 * 
	 * @return value
	 */
	public Color getColor()
	{ return DynamicColor.this.value; }
	
	/**
	 * Links Color to object
	 * 
	 * @param w target object
	 */
	public void attach(Watcher w)
	{ DynamicColor.this.subject.attach(w); }
	
	/**
	 * update all linked objects
	 */
	public void demainUpdate()
	{ DynamicColor.this.subject.refresh(); }
	
	/**
	 * Change Color
	 * 
	 * @param color value
	 */
	public void setColor(Color color)
	{
		DynamicColor.this.value = color;
		demainUpdate();
	}
	
	/**
	 * Prints Hexadecimal value of color
	 */
	//FIXME use Object to String
	public String toString()
	{
		if(DynamicColor.this.value instanceof Color)
		{
			String rgb = Integer.toHexString(((Color) DynamicColor.this.value).getRGB());
			return DynamicColor.this.image+" --> #"+rgb.substring(2, rgb.length());
		}
		return DynamicColor.this.image+" --> #"+DynamicColor.this.value;
	}
	
}