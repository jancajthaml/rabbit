package tests;


public class LinkedListTest extends BasicPerformanceTest
{
	
	public static final int INNER_ITERS = 5000;
	public static final int OUTER_ITERS = 200;

	private static java.util.LinkedList<Double> FILL_J	= new java.util.LinkedList<Double>();
	private static struct.LinkedList<Double> FILL_S		= new struct.LinkedList<Double>();
	
	private static java.util.LinkedList<Double> FIRST_J	= new java.util.LinkedList<Double>();
	private static struct.LinkedList<Double> FIRST_S		= new struct.LinkedList<Double>();
	
	private static java.util.LinkedList<Double> LAST_J	= new java.util.LinkedList<Double>();
	private static struct.LinkedList<Double> LAST_S		= new struct.LinkedList<Double>();
	
	private static Double[] data = new Double[INNER_ITERS*OUTER_ITERS];
	
	public static String[] tests = new String[]{
		"java.util - new",
		"struct - new",
		"java.util - fill",
		"struct - fill",
		"java.util - remove",
		"struct - remove",
		"java.util - remFirst",
		"struct - remFirst",
		"java.util - remLast",
		"struct - remLast"
	};
	
	public LinkedListTest()
	{ super(10, OUTER_ITERS); }
	
	@Override public void runTest(int argNum)
	{
		for(int i=0; i<INNER_ITERS; i++)
		{
			switch(argNum)
			{
				case 0	: new java.util.LinkedList<Object>();	break;
				case 1	: new struct.LinkedList<Object>();		break;
				case 2	: FILL_J.add(data[i]);					break;
				case 3	: FILL_S.add(data[i]);					break;
				case 4	: FILL_J.remove(data[i]);				break;
				case 5	: FILL_S.remove(data[i]);				break;
				case 6	: FIRST_J.removeFirst();				break;
				case 7	: FIRST_S.removeFirst();				break;
				case 8	: LAST_J.removeLast();					break;
				case 9	: LAST_S.removeLast();					break;
			}
		}
	}
	
	@Override
	public String getTestName(int argNum)
	{ return tests[argNum]; }
	
	public static void main(String[] c)
	{
		for(int i=0; i<INNER_ITERS*OUTER_ITERS; i++)
		{
			data[i]=StrictMath.random();
			FIRST_J.addFirst(data[i]);
			FIRST_S.addFirst(data[i]);
			LAST_J.addLast(data[i]);
			LAST_S.addLast(data[i]);
		}
		for(int i=0; i<1000; i++) //WarmUp
			try { Thread.sleep(1); } catch (InterruptedException e) { }
		(new LinkedListTest()).go();
	}
	
}
