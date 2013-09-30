package tests;

public abstract class BasicPerformanceTest
{
	public abstract void runTest(int testNum);

	public abstract String getTestName(int testNum);

	private final int numTests, iters;
	protected final long[] times;

	public BasicPerformanceTest(int numTests, int iters)
	{
		this.numTests = numTests;
		this.iters = iters;
		times = new long[numTests];
		for (int i = 0; i < numTests; i++)
			times[i] = 0;
	}

	public double getTestTime(int testNum)
	{ return times[testNum] * 1.0 / 1000000; }

	public void go()
	{
		long prev, after;
		for (int i = 0; i < iters; i++)
		{
			println(i * 100.0 / iters + "%");
			for (int test = 0; test < numTests; test++)
			{
				prev = System.nanoTime();
				runTest(test);
				after = System.nanoTime();
				times[test] += after - prev;
			}
		}
		for (int test = 0; test < numTests; test++)
		{
			times[test] /= iters;
		}
		printResults();
  }

  public void printResults()
  {
	  printf("%-20s%20s%20s\n", "Test Name", "Milliseconds Avg", "FPS (optional)");

	  for (int i = 0; i < numTests; i++)
	  {
		  double milliseconds = times[i] * 1.0 / 1000000;
		  if (getFrames(i) != 0)
		  {
			  double fps = getFrames(i) * 1000d / milliseconds;
			  printf("%-20s%20.4f%20.4f\n", getTestName(i), milliseconds, fps);
		  }
		  else printf("%-20s%20.4f\n", getTestName(i), milliseconds);
	  }
  }

  public int getFrames(int testNum)
  { return 0; }

  public void println(String s)
  { System.out.println(s); }
  
  public void printf(String s, Object... args)
  { System.out.printf(s, args); }
  
}