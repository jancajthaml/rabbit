package struct;

/**
 * Lightweight fixed 4-integer Vector implementation
 *
 * @author Jan Cajthaml
 */
public class Vec4
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //

	/**
	 * a1 value
	 */
	public int a	= 0;

	/**
	 * a2 value
	 */
	public int b	= 0;
	
	/**
	 * a3 value
	 */
	public int c	= 0;
	
	/**
	 * a4 value
	 */
	public int d	= 0;
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new Vector{a1,a2,a3,a4}
	 * 
	 * @param a a1 value
	 * @param b a2 value
	 * @param c a3 value
	 * @param d a4 value
	 */
	public Vec4(int a, int b, int c, int d)
	{
		Vec4.this.a	= a;
		Vec4.this.b	= b;
		Vec4.this.c	= c;
		Vec4.this.d	= d;
	}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Formats class like: Vec4[a1, a2, a3, a4]
	 */
	public String toString()
	{ return "Vec4["+Vec4.this.a+","+Vec4.this.b+","+Vec4.this.c+","+Vec4.this.d+"]"; }
	
}