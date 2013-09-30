package tests;


public class HashSetTest extends BasicPerformanceTest
{
	
	public static final int INNER_ITERS = 10000;
	public static final int OUTER_ITERS = 200;

	private java.util.HashSet<Double> FILL_J	= new java.util.HashSet<Double>(INNER_ITERS*OUTER_ITERS);
	private struct.HashSet<Double> FILL_S		= new struct.HashSet<Double>(INNER_ITERS*OUTER_ITERS);
	private static Double[] data				= new Double[INNER_ITERS*OUTER_ITERS];
	
	public static String[] tests = new String[]
	{
		"java.util - new"		,
		"struct - new"			,
		"java.util - fill"		,
		"struct - fill"			,
		"java.util - remove"	,
		"struct - remove"
	};
	
	public HashSetTest()
	{ super(6, OUTER_ITERS); }
	
	@Override public void runTest(int argNum)
	{
		for(int i=0; i<INNER_ITERS; i++)
		{
			switch(argNum)
			{
				case 0	: new java.util.HashSet<Object>();	break;
				case 1	: new struct.HashSet<Object>();		break;
				case 2	: FILL_J.add(data[i]);				break;
				case 3	: FILL_S.add(data[i]);				break;
				case 4	: FILL_J.remove(data[i]);			break;
				case 5	: FILL_S.remove(data[i]);			break;
			}
		}
	}
	
	@Override
	public String getTestName(int argNum)
	{ return tests[argNum]; }
	
	public static void main(String[] c)
	{
		for(int i=0; i<INNER_ITERS*OUTER_ITERS; i++)
			data[i]=StrictMath.random();
		
		(new HashSetTest()).go();
	}

}