package math;

import java.util.Random;

public class StrictMath
{
	public static boolean FAST_ABS = true;
	public static boolean FAST_FLOOR = true;
	public static boolean FAST_CEIL = true;
	public static boolean FAST_ROUND = true;
	public static boolean FAST_ATAN2 = true;
	public static boolean SINCOS_LUT_ENABLED = true;
	public static boolean SINCOS_LUT_LERP = false;

	private static final float SHIFT23 = 1 << 23;
	private static final float INV_SHIFT23 = 1.0f / SHIFT23;

	public static final float EPSILON = 1.1920928955078125E-7f;
	public static final float PI = (float) Math.PI;
	public static final float TWOPI = (float) (Math.PI * 2);
	public static final float INV_PI = 1f / PI;
	public static final float HALF_PI = PI / 2;
	public static final float QUARTER_PI = PI / 4;
	public static final float THREE_HALVES_PI = TWOPI - HALF_PI;
	public static final float SINCOS_LUT_PRECISION = .00011f;
	public static final int SINCOS_LUT_LENGTH = (int) Math.ceil(Math.PI * 2 / SINCOS_LUT_PRECISION);
	public static final float DEG2RAD = PI / 180;
	public static final float RAD2DEG = 180 / PI;
	public static final float[] sinLUT = new float[SINCOS_LUT_LENGTH];
	public static int CONTACT_STACK_INIT_SIZE = 10;

	static
	{
		for (int i = 0; i < SINCOS_LUT_LENGTH; i++)
			sinLUT[i] = (float) Math.sin(i * SINCOS_LUT_PRECISION);
	}

	public static final float sin(float x)
	{ return SINCOS_LUT_ENABLED?sinLUT(x):((float) Math.sin(x)); }

	public static final float sinLUT(float x)
	{
	    x %= TWOPI;
	    if (x < 0)
	      x += TWOPI;

	    if (SINCOS_LUT_LERP)
	    {
	    	x /= SINCOS_LUT_PRECISION;
	    	final int index = (int) x;
	    	if (index != 0)
	    		x %= index;
	    	return (index == SINCOS_LUT_LENGTH - 1)?((1 - x) * sinLUT[index] + x * sinLUT[0]):((1 - x) * sinLUT[index] + x * sinLUT[index + 1]);
	    }
	      return sinLUT[round(x / SINCOS_LUT_PRECISION) % SINCOS_LUT_LENGTH];
	  }

	  public static final float cos(float x)
	  { return SINCOS_LUT_ENABLED?sinLUT(HALF_PI - x):(float) Math.cos(x); }

	  public static final float abs(final float x)
	  { return (FAST_ABS)?(x > 0 ? x : -x):Math.abs(x); }

	  public static final int abs(int x)
	  {
		  int y = x >> 31;
		return (x ^ y) - y;
	  }

	  public static final int floor(final float x)
	  {
		  if (FAST_FLOOR)
		  {
			  int y = (int) x;
			  if (x < 0 && x != y)
				  return y - 1;
			  return y;
		  }
		  else return (int) Math.floor(x);
	  }

	  public static final int ceil(final float x)
	  {
		  if (FAST_CEIL)
		  {
			  int y = (int) x;
			  if (x > 0 && x != y)
				  return y + 1;
			  return y;
		  }
		  else return (int) Math.ceil(x);
	  }

	  public static final int round(final float x)
	  { return FAST_ROUND?floor(x + .5f):Math.round(x); }

	  public static final int ceilPowerOf2(int x)
	  {
		  int pow2 = 1;
		  while (pow2 < x)
			  pow2 <<= 1;
		  return pow2;
	  }

	  public final static float max(final float a, final float b)
	  { return a > b ? a : b; }

	  public final static int max(final int a, final int b)
	  { return a > b ? a : b; }

	  public final static float min(final float a, final float b)
	  { return a < b ? a : b; }

	  public final static int min(final int a, final int b)
	  { return a < b ? a : b; }

	  public final static float map(final float val, final float fromMin, final float fromMax, final float toMin, final float toMax)
	  { return toMin + ((val - fromMin) / (fromMax - fromMin)) * (toMax - toMin); }

	  public final static float clamp(final float a, final float low, final float high)
	  { return max(low, min(a, high)); }

	  public final static int nextPowerOfTwo(int x)
	  {
		  x |= x >> 1;
	  	  x |= x >> 2;
		  x |= x >> 4;
		  x |= x >> 8;
		  x |= x >> 16;
		  return x + 1;
	  }

	  public final static boolean isPowerOfTwo(final int x)
	  { return x > 0 && (x & x - 1) == 0; }

	  public static final float atan2(final float y, final float x)
	  { return FAST_ATAN2?fastAtan2(y, x):((float) Math.atan2(y,x)); }

	  public static final float fastAtan2(float y, float x)
	  {
		  if (x == 0.0f)
		  {
			  if (y > 0.0f) return HALF_PI;
			  if (y == 0.0f) return 0.0f;
			  return -HALF_PI;
		  }
		  float atan;
		  final float z = y / x;
		  if (abs(z) < 1.0f)
		  {
			  atan = z / (1.0f + 0.28f * z * z);
			  if (x < 0.0f)
			  {
				  if (y < 0.0f) return atan - PI;
				  return atan + PI;
			  }
		  }
		  else
		  {
			  atan = HALF_PI - z / (z * z + 0.28f);
			  if (y < 0.0f) return atan - PI;
		  }
		  return atan;
	  }
	  
	  public static final float reduceAngle(float theta)
	  {
		  theta %= TWOPI;
		  if (abs(theta) > PI)
			  theta = theta - TWOPI;
		  
		  if (abs(theta) > HALF_PI)
			  theta = PI - theta;

		  return theta;
	  }

	  public static final float randomFloat(float argLow, float argHigh)
	  { return (float) Math.random() * (argHigh - argLow) + argLow; }

	  public static final float randomFloat(Random r, float argLow, float argHigh)
	  { return r.nextFloat() * (argHigh - argLow) + argLow; }

	  public static final float sqrt(float x)
	  { return (float) Math.sqrt(x); }

	  public static final float pow(float a, float b) {
	    float x = Float.floatToRawIntBits(a);
	    x *= INV_SHIFT23;
	    x -= 127;
	    float y = x - (x >= 0 ? (int) x : (int) x - 1);
	    b *= x + (y - y * y) * 0.346607f;
	    y = b - (b >= 0 ? (int) b : (int) b - 1);
	    y = (y - y * y) * 0.33971f;
	    return Float.intBitsToFloat((int) ((b + 127 - y) * SHIFT23));
	  }
}