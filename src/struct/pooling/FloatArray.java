package struct.pooling;


import java.util.HashMap;

/**
 * Not thread safe float[] pooling.
 * @author Jan Cajthaml
 */
public class FloatArray
{
	
	private final HashMap<Integer, float[]> map = new HashMap<Integer, float[]>();
	
	public float[] get(int length)
	{
		assert(length > 0);
				
		if(!map.containsKey(length))
			map.put(length, new float[length]);
		
		return map.get(length);
	}
}