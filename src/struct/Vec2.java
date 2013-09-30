package struct;

/**
 * Lightweight fixed 2-integer Vector implementation
 *
 * @author Jan Cajthaml
 */
public class Vec2
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	/**
	 * a1 value
	 */
	public int x;
	
	/**
	 * a2 value
	 */
	public int y;

	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new Vector{a1,a2}
	 * 
	 * @param x a1 value
	 * @param y a2 value
	 */
	public Vec2(int x, int y)
	{
		Vec2.this.x = x;
		Vec2.this.y = y;
	}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Formats class like: Vec2[a1, a2]
	 */
	public String toString()
	{ return "Vec2["+Vec2.this.x+","+Vec2.this.y+"]"; }
	
}