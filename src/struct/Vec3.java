package struct;

/**
 * Lightweight fixed 3-float Vector implementation
 *
 * @author Jan Cajthaml
 */
public class Vec3
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	/**
	 * a1 value
	 */
	public float x	= 0f;
	
	/**
	 * a2 value
	 */
	public float y	= 0f;
	
	/**
	 * a3 value
	 */
	public float z	= 0f;
	
	// >-------[ctor]---------------------------------------------------------------------------------------< //

	/**
	 * Creates new Vector{a1,a2,a3}
	 * 
	 * @param x a1 value
	 * @param y a2 value
	 * @param z a3 value
	 */
	public Vec3(float x, float y, float z)
	{
		Vec3.this.x	= x;
		Vec3.this.y	= y;
		Vec3.this.z	= z;
	}
	
	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Formats class like: Vec3[a1, a2, a3]
	 */
	public String toString()
	{ return "Vec3["+Vec3.this.x+","+Vec3.this.y+","+Vec3.this.z+"]"; }
	
}