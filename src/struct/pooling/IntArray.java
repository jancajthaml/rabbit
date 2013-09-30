package struct.pooling;


import java.util.HashMap;

/**
 * Not thread safe int[] pooling
 * @author Jan Cajthaml
 */
public class IntArray
{	
	private final HashMap<Integer, int[]> map = new HashMap<Integer, int[]>();
	
	public int[] get(int length)
	{
		assert(length > 0);
		
		if(!map.containsKey(length))
			map.put(length, new int[length]);

		return map.get(length);
	}

}