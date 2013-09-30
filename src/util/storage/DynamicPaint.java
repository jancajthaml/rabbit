package util.storage;

import java.awt.Color;
import java.awt.Paint;

/**
 * This class represents Linkable Paint that does not need global actualisation.
 * DynamicPaint can be linked via attach() method to any Watcher
 * 
 * @author Jan Cajthaml
 *
 */
public class DynamicPaint extends DynamicColor
{
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private Paint value = Color.red;
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	/**
	 * 
	 * @param image
	 */
	protected DynamicPaint(String image)
	{ super(image);	}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Gets Paint value
	 * 
	 * @return value
	 */
	public Paint getPaint()
	{ return DynamicPaint.this.value; }
	
	/**
	 * Change Paint
	 * 
	 * @param paint value
	 */
	public void setColor(Paint paint)
	{
		DynamicPaint.this.value = paint;
		demainUpdate();
	}
	
	/**
	 * Prints Hexadecimal value of color
	 */
	//FIXME use object to string
	public String toString()
	{
		if(DynamicPaint.this.value instanceof Color)
		{
			String rgb = Integer.toHexString(((Color) DynamicPaint.this.value).getRGB());
			return image+" --> #"+rgb.substring(2, rgb.length());
		}
		return image+" --> #"+DynamicPaint.this.value;
	}
	
}